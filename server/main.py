"""The most basic chat protocol possible.

run me with twistd -y chatserver.py, and then connect with multiple
telnet clients to port 1025
"""

from twisted.protocols import basic
import json

responseOK = "OK"
responseERROR = "ERROR"

error_room_not_found = "room_not_found"
error_cant_message_room = "cannot_message_room"
error_invalid_command = "invalid_command"
error_login_required = "login_required"
error_login_failed = "login_failed"
error_already_joined = "already_joined"
error_not_joined = "not_joined"
error_user_not_found = "user_not_found"

response_login_successful = "login_successful"
response_room_created_successfuly = "room_created_successfuly"
response_joined_room_successfuly = "joined_room_successfuly"
response_left_room_successfuly = "left_room_successfuly"
response_my_info = "my_info"
response_user_info = "user_info"
response_get_rooms = "get_rooms"
response_room_info = "room_info"
response_message = "message"
response_user_enter_room = "user_enter_room"
response_user_left_room = "user_left_room"

IDNumber = 0

def splitWithDefault(string, default):
    if ' ' in string:
        return split(string,1)
    else:
        return (default, string)

class System:
    instance = None
    def __init__(self):
        if System.instance == None:
            System.instance = self
            Room("global")

    def checkCridentials(self,username,password):
        global IDNumber
        if username == password and username != None:
            IDNumber += 1
            return {"username": username, "ID": str(IDNumber)}
        else:
            return None

    def extractCridentials(self,cridentials):
        try:
            username, password = cridentials.split(' ')
            return (username, password)
        except:
            return (None, None)

    @staticmethod
    def getInstance():
        if System.instance == None:
            System.instance = System()
        return System.instance

class Room:
    def __init__(self, name):
        self.name = name
        self.subscribers = {}
        Room.rooms[name] = self

    def subscribe(self, user):
        for subscriber in self.getSubscribers():
            subscriber.respond(response_user_enter_room,
                               responseOK,
                               {"message": self.getName()}, None,
                               {"user_info":user.getPublicInfo()})
        self.subscribers[user.info["ID"]] = user
        print "Subscribing", user.info["username"], "to room", self.getName()
        return True

    def unsubscribe(self, user):
        del self.subscribers[user.info["ID"]]
        print "Unsubscribing", user.info["username"], "from room", self.getName()
        for subscriber in self.getSubscribers():
            subscriber.respond(response_user_left_room,
                               responseOK,
                               {"message": self.getName()}, None,
                               {"user_info":user.getPublicInfo()})
        if len(self.getSubscribers()) == 0 and self.getName() != "global":
            del Room.rooms[self.getName()]
        return True

    def getSubscribers(self):
        return self.subscribers.values()

    def getName(self):
        return self.name

    def getInfo(self):
        return {"name": self.getName(), "users": self.subscribers.keys()}

    @staticmethod
    def getRoom(name):
        try:
            return Room.rooms[name]
        except KeyError:
            return None

    @staticmethod
    def getRooms():
        return Room.rooms.keys()

    rooms = {}


class MyChat(basic.LineReceiver):

    requireLogin = ["message", "join_room", "create_room", "leave_room", "get_my_info"] 

    def connectionMade(self):
        self.delimiter = "\n"
        print "Got new client!"
        self.commands = {
            "disconnect": self.disconnect,
            "login": self.login,
            "message": self.sendMessage,
            "join_room": self.joinRoom,
            "create_room": self.createRoom,
            "get_rooms": self.getRooms,
            "get_my_info": self.getMyInfo,
            "get_user_info": self.getUserInfo,
            "get_room_info": self.getRoomInfo,
            "leave_room": self.leaveRoom
        }
        self.factory.clients.append(self)
        self.info = None
        self.rooms = []

    def connectionLost(self, reason):
        if self.info != None and "username" in self.info.keys():
            print "Logged out:", self.info["username"]
        else:
            print "Client disconnected"
        self.factory.clients.remove(self)
        for room in self.rooms:
            Room.getRoom(room).unsubscribe(self)

    def subscribe(self, roomName):
        self.rooms.append(roomName)
        Room.getRoom(roomName).subscribe(self)

    def unsubscribe(self, roomName):
        self.rooms.remove(roomName)
        Room.getRoom(roomName).unsubscribe(self)

    def lineReceived(self, line):
        line = line.lstrip().rstrip()
        if not ' ' in line:
            command = line
            arg = None
        else:
            command, arg = line.split(" ", 1)
        if not command in self.commands.keys():
            self.respondERROR(command, error_invalid_command)
        else:
            if not self.isLoggedIn() and command in MyChat.requireLogin:
                self.respondERROR(command, error_login_required)
            else:
                if arg:
                    self.commands[command](arg)
                else:
                    self.commands[command]()

    def isLoggedIn(self):
        return self.info != None

    def getPublicInfo(self):
        ret = self.info
        ret["rooms"] = self.rooms
        return ret

    def login(self, cridentials):
        username, password = System.getInstance().extractCridentials(cridentials)
        self.info = System.getInstance().checkCridentials(username, password)
        if self.info:
            self.respondOK(self.info["ID"], response_login_successful)
            self.subscribe("global")
        else:
            self.respondERROR("Login failed", error_login_failed)

    def respondAll(self, responseType, responseCode, responseInfo, error_code = None):
        for c in self.factory.clients:
            c.respond(responseType, responseCode, responseInfo)

    def respond(self, responseType, responseCode, responseInfo, error_code = None, other_dict = None):
        res = {"type": responseType, "code": responseCode, "info": responseInfo, "error_code": error_code}
        if other_dict != None:
            for k in other_dict.keys():
                res[k] = other_dict[k]
        self.transport.write(json.dumps(res)+"\n")

    def respondOK(self, info, response_type):
        self.respond(response_type, responseOK, {"message": info})

    def respondERROR(self, info, error_code):
        self.respond("response", responseERROR, {"message": info}, error_code)

    def disconnect(self):
        self.transport.loseConnection()

    def sendMessage(self, message):
        if ' ' not in message:
            self.respondERROR("", error_room_not_found)
            return
        roomName, message = message.split(' ', 1)
        room = Room.getRoom(roomName)
        if room == None:
            self.respondERROR(roomName, error_room_not_found)
        else:
            if not roomName in self.rooms:
                self.respondERROR(roomName, error_cant_message_room)
            else:
                for c in self.factory.clients:
                    c.respond(response_message, responseOK, {
                        "from": self.info["ID"],
                        "message": message,
                        "room": room.getName()})

    def getUserInfo(self, userID):
        for c in self.factory.clients:
            if c.info != None and "ID" in c.info.keys() and str(c.info["ID"]) == str(userID):
                self.respond(response_user_info, responseOK, None, None, {"user_info":c.getPublicInfo()})
                return
        self.respondERROR(userID, error_user_not_found)

    def getMyInfo(self):
        self.respond(response_my_info, responseOK, None, None, {"user_info":self.getPublicInfo()})

    def getRooms(self):
        self.respond(response_get_rooms, responseOK, None, None, {"room_names":Room.getRooms()})

    def createRoom(self, name):
        Room(name)
        self.respondOK(name, response_room_created_successfuly)

    def joinRoom(self, name):
        room = Room.getRoom(name)
        if room == None:
            self.respondERROR(name, error_room_not_found)
        else:
            if name in self.rooms:
                self.respondERROR(name, error_already_joined)
            else:
                self.subscribe(name)
                self.respondOK(name, response_joined_room_successfuly)

    def leaveRoom(self, name):
        if not name in self.rooms:
            self.respondERROR(name, error_not_joined)
        else:
            self.unsubscribe(name)
            self.respondOK(name, response_left_room_successfuly)

    def getRoomInfo(self, name):
        room = Room.getRoom(name)
        if room == None:
            self.respondERROR(name, error_room_not_found)
        else:
            self.respond(response_room_info, responseOK, None, None, {"room_info": room.getInfo()})



from twisted.internet import protocol, reactor
from twisted.application import service, internet

factory = protocol.ServerFactory()
factory.protocol = MyChat
factory.clients = []

reactor.listenTCP(8000, factory)
reactor.run()