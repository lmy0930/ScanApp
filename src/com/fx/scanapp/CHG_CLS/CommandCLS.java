package com.fx.scanapp.CHG_CLS;

import android.os.Handler;
import android.os.Message;

import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
import com.fx.scanapp.DateType.ColParameter;
import com.fx.scanapp.fileAnalyze.FileAnalyze;

public class CommandCLS extends TaskNode{
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
				if (FileAnalyze.ReadINI()) {
					Machine.cmand_bstatus=1;
					Machine.cmand_status = 2;
					Machine.getInstance().nextdo = "请刷主管权限";
				} else {
					if (Integer.parseInt(Machine.getInstance().clist.get(0).getSTATUS()) == ColParameter.已换线 ||
							Integer.parseInt(Machine.getInstance().clist.get(0).getSTATUS()) == ColParameter.下线)
                        {
						Machine.cmand_status = 3;
						Machine.getInstance().nextdo = "已刷入换班指令\n请刷料盘序列号";
                        }
                        else
                        {
                            //PlayFailSound();
                        	Machine.getInstance().nextdo = "料站表未刷换线\n请线刷换线,再刷换班";
                        	FileAnalyze.WriteINI("1");
                        }
				}
				Message msg = handler.obtainMessage();
				msg.what = 0x11;
				handler.sendMessage(msg);
				
			}
		}).start();		
	}

}
