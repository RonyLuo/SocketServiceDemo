package com.example.socketdemo;

/*
 * Interface of MessageListener, used to notice
 */
public interface MessageListener {
	public static final int MESSAGE_DATA_FROM_SERVER = 0x23;

	public static final int SOCKET_READ_TIME_OUT = 0x25;
	public static final int SOCKET_NOT_CONNECT = 0x26;

	public static final int MESSAGE_SOCKET_CLOSE = 0x27;

	public static final int MESSAGE_CONNECTION_ERROR = 0x29;

	public static final int SEND_SOCKET_DATA_ERROR = 0x30;

	void onMessage(int code, String msg);
}
