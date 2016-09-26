package com.fx.scanapp.SCARCITY;

import android.os.Handler;
import android.os.Message;

import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
import com.fx.scanapp.fileAnalyze.FileAnalyze;

public class CommandSCARCITY extends TaskNode {
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
	public void setTaskLevel(int level) {
		taskLevel = level;

	}

	@Override
	public void doTask() {	
		Machine.getInstance().inputmsg = msg;
		new Thread(new Runnable() {
			
			@Override
			public void run() {				
				//主管权限检查
				if (FileAnalyze.ReadINI()) {
					Machine.cmand_bstatus=3;
					Machine.cmand_status = 2;
					Machine.getInstance().nextdo = "请刷主管权限";
				} else {
					Machine.cmand_status = 3;
					Machine.getInstance().nextdo = "请刷料盘编号";
				}
				Message msg = handler.obtainMessage();
				msg.what = 0x11;
				handler.sendMessage(msg);
				
			}
		}).start();
	}
}

