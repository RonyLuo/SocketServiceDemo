package com.example.socketdemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @since 2016/03/26
 * @author larsonzhong@163.com
 * 
 */
public class MainActivity extends Activity implements MessageListener {
	private TextView tv_msg = null;
	private EditText ed_msg = null;
	private Button btn_send = null;
	private IService socketService;
	private SocketConnection conn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tv_msg = (TextView) findViewById(R.id.TextView);
		ed_msg = (EditText) findViewById(R.id.EditText01);
		btn_send = (Button) findViewById(R.id.Button02);

		btn_send.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				String msg = ed_msg.getText().toString();
				socketService.sendData(msg);
			}
		});

		bindSocketService();
		SocketManager.getInstance().registMessageListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SocketManager.getInstance().unResgistListener(this);
		unbindService(conn);
	}

	/**
	 * bind socket service
	 */
	private void bindSocketService() {
		conn = new SocketConnection();
		Intent intent = new Intent(MainActivity.this, SocketService.class);
		bindService(intent, conn, BIND_AUTO_CREATE);
		startService(intent);
	}

	public class SocketConnection implements ServiceConnection {
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			LogUtil.d(">>>>>>>>>>>onServiceConnected");
			socketService = (IService) arg1;
			mHandler.sendEmptyMessage(200);
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {

		}

	}

	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 200) {
				btn_send.setEnabled(true);
			} else if (msg.what == 0) {
				String content = (String) msg.obj;
				tv_msg.setText(content);
			}

		}
	};

	@Override
	public void onMessage(int code, String msg) {
		Message message = new Message();
		message.what = 0;
		message.obj = msg;
		mHandler.sendMessage(message);

	}
}