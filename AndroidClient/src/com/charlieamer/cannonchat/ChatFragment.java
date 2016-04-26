package com.charlieamer.cannonchat;

import java.util.ArrayList;

import com.google.gson.Gson;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChatFragment extends Fragment implements OnClickListener {
	ArrayList<Message> messages;
	Button sendButton;
	LinearLayout layout;
	EditText messageText;
	
	private static final String SAVE_KEY = "MESSAGES";
	
	public static ArrayList<Message> BundleToMessages(Bundle b) {
		Gson gson = new Gson();
		ArrayList<Message> ret = new ArrayList<Message>();
		String[] msgs = b.getStringArray(SAVE_KEY);
		if (msgs != null)
			for (String m : msgs) {
				ret.add(gson.fromJson(m, Message.class));
			}
		return ret;
	}
	
	public static void MessagesToBundle(ArrayList<Message> messages, Bundle b) {
		Gson gson = new Gson();
		String[] arr = new String[messages.size()];
		for (int i=0; i<messages.size(); i++)
			arr[i] = gson.toJson(messages.get(i));
		b.putStringArray(SAVE_KEY, arr);
	}
	
	public void AddMessage(Message m) {
		messages.add(m);
		TextView tv = new TextView(getActivity());
		if (m.from != null)
			tv.setText(m.from.username + ": " + m.message);
		else
		{
			tv.setText(m.message);
			tv.setTextColor(Color.LTGRAY);
			tv.setTypeface(null, Typeface.ITALIC);
		}
		layout.addView(tv);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		MessagesToBundle(messages, outState);
		super.onSaveInstanceState(outState);
	}
	
	public void FillMessages(ArrayList<Message> newMessages) {
		for (Message m : newMessages)
			AddMessage(m);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		messages = new ArrayList<Message>();
		
		messages = BundleToMessages(getArguments());
		View rootView = inflater.inflate(R.layout.fragment_chat, container,
				false);
		
		layout = (LinearLayout)rootView.findViewById(R.id.message_area);
		TextView text = new TextView(getActivity());
		text.setText("Logged in successfuly");
		layout.addView(text);
		sendButton = (Button)rootView.findViewById(R.id.chat_send);
		sendButton.setOnClickListener(this);
		messageText = (EditText)rootView.findViewById(R.id.chat_text);
		
		FillMessages(BundleToMessages(getArguments()));
		
		return rootView;
	}

	@Override
	public void onClick(View v) {
		String txt = messageText.getText().toString().trim();
		if (!txt.equals("")) {
			messageText.setText("");
			((ChatActivity)getActivity()).SendMessage(txt);
		}
	}
}
