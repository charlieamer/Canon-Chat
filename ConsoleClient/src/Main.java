import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.charlieamer.cannonchat.client.ChatClientController;
import com.charlieamer.cannonchat.client.ChatInterpreterListener;


public class Main {
	public static void main(String[] args) throws Exception {
		ConsoleChatListener chatListener = new ConsoleChatListener();
		ChatInterpreterListener interpreter = new ChatInterpreterListener(chatListener);
		ChatClientController ccc = new ChatClientController("localhost", 8000, interpreter);
		interpreter.setChatClientController(ccc);
		ccc.Start();
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		while (!interpreter.IsLoggedIn()) {
			System.out.print("Enter username: ");
			System.out.flush();
			String username = bufferRead.readLine();
			interpreter.Login(username, username);
			while(!interpreter.IsReady()) {
				System.out.println("waiting...");
				Thread.sleep(1000);
			}
		}
		while (true) {
			System.out.print("Enter message (type disconnect to quit): ");
			System.out.flush();
			String message = bufferRead.readLine();
			if (message.equals("disconnect")) {
				interpreter.Disconnect();
				break;
			}
			interpreter.Message("global", message);
		}
		ccc.Stop();
	}
}
