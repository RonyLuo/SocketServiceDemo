package com.example.socketdemo;

import java.util.ArrayList;
import java.util.List;

public class SocketManager {

	private static SocketManager manager;

	public static SocketManager getInstance() {
		if (manager == null)
			manager = new SocketManager();
		return manager;
	}

	/*
	 * The list of MessageListener
	 */
	public static List<MessageListener> messageListenerlist = new ArrayList<MessageListener>();

	/*
	 * Regist MessageListener to get message
	 */
	public void registMessageListener(MessageListener messageListener) {
		messageListenerlist.add(messageListener);
	}

	/*
	 * Remove all MessageListener
	 */
	public void removeAllListeners() {
		messageListenerlist.clear();
	}

	/*
	 * Remove MessageListener
	 */
	public void unResgistListener(MessageListener messageListener) {
		if (messageListenerlist.contains(messageListener)) {
			messageListenerlist.remove(messageListener);
		}
	}

	public void onMessageEvent(int msgCode, String msg) {
		if (messageListenerlist.size() != 0) {
			for (MessageListener listener : messageListenerlist) {
				listener.onMessage(msgCode, msg);
			}
		} else
			LogUtil.d("App hasn't regist any MessageListener!");
	}
}
