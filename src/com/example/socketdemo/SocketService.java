package com.example.socketdemo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

/**
 * 
 * @author larsonZhong@163.com
 * 
 */
public class SocketService extends Service {
	private final IBinder mBinder = new SocketBinder();
	private Socket socket = null;
	private static final String HOST = "192.168.1.135";
	private static final int PORT = 3456;
	// SenderThread senderThread;
	ReceiverThread receivcerThread;
	private String TAG = ">>>>SocketService<<<<";

	class SocketBinder extends Binder implements IService {
		@Override
		public boolean sendData(String dataStr) {
			return sendDataBySocket(dataStr);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	public boolean sendDataBySocket(String socketData) {
		if (socket != null && socket.isConnected() && !socket.isClosed()) {
			Log.v(TAG, ">>>>>>send socketData=" + socketData);
			try {
				PrintWriter print = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				print.println(socketData);
				print.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			LogUtil.d(TAG, "Socket is not alive!");
		}
		return false;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		initSocket();
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		// senderThread.interrupt();
		receivcerThread.interrupt();
		super.onDestroy();
	}

	// private class SenderThread extends Thread {
	// @Override
	// public void run() {
	// super.run();
	// try {
	// PrintWriter out = new PrintWriter(new BufferedWriter(
	// new OutputStreamWriter(socket.getOutputStream())), true);
	//
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }

	private void initSocket() {
		new Thread() {
			public void run() {
				try {
					socket = new Socket(HOST, PORT);
					// socket一启动则连接socket，并连接服务器，开启接受线程和发送线程
					// senderThread = new SenderThread();
					// senderThread.start();
					receivcerThread = new ReceiverThread();
					receivcerThread.start();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			};
		}.start();
	}

	private class ReceiverThread extends Thread {
		@Override
		public void run() {
			super.run();
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				while (true) {
					if (socket.isConnected()) {
						if (!socket.isInputShutdown()) {
							String content;
							if ((content = in.readLine()) != null) {
								content += "\n";
								SocketManager.getInstance().onMessageEvent(0,
										content);
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
