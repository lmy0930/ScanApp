package com.fx.scanapp.Init;

import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class CommandInit extends TaskNode {
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0x11) {
				Machine.getInstance().execute();
			}
		}
	};

	@Override
	public float getTaskLevel() {
		return taskLevel;
	}

	@Override
	public void doTask() {// 任务逻辑
		System.out.println("进行初始化操作");
		Log.i("Node", "init");
		Machine m = Machine.getInstance();
		m.user = "";
		m.masterID = "";
		m.station=null;
		m.inputmsg = msg;
		m.nextdo = "初始化完成\n请刷入用户权限";
		m.cmand_status = 2;

		new Thread(new Runnable() {

			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				msg.what = 0x11;
				handler.sendMessage(msg);
			}
		}).start();

	}

	@Override
	public void setTaskLevel(int level) {
		taskLevel = level;

	}

}
