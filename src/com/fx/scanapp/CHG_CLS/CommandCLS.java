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
					Machine.getInstance().nextdo = "��ˢ����Ȩ��";
				} else {
					if (Integer.parseInt(Machine.getInstance().clist.get(0).getSTATUS()) == ColParameter.�ѻ��� ||
							Integer.parseInt(Machine.getInstance().clist.get(0).getSTATUS()) == ColParameter.����)
                        {
						Machine.cmand_status = 3;
						Machine.getInstance().nextdo = "��ˢ�뻻��ָ��\n��ˢ�������к�";
                        }
                        else
                        {
                            //PlayFailSound();
                        	Machine.getInstance().nextdo = "��վ��δˢ����\n����ˢ����,��ˢ����";
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
