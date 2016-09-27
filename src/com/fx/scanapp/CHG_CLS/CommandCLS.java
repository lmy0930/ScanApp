package com.fx.scanapp.CHG_CLS;

import android.os.Handler;
import android.os.Message;

import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
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
					Machine.cmand_bstatus=3;
					Machine.cmand_status = 2;
					Machine.getInstance().nextdo = "请刷主管权限";
				} else {
					Machine.cmand_status = 3;
					Machine.getInstance().nextdo = "请刷料盘序列号";
				}
				Message msg = handler.obtainMessage();
				msg.what = 0x11;
				handler.sendMessage(msg);
				
			}
		}).start();
//		 
//         if (string.IsNullOrEmpty(CHGCLS.料站编号))
//         {
//             CHGCLS.料站编号 = threadpar.InputText;
//             ShowCue(LogMsgType.Warning, "数据处理中..");
//
//             if (!CHGCLS.CheckKpIndex(out Err, out Msg))
//             {
//                 ShowMsg(LogMsgType.Error, Err);
//                 //   CHGCLS.料盘序列号 = string.Empty;
//                 CHGCLS.料站编号 = string.Empty;
//                 ShowCue(LogMsgType.Warning, "请重刷料站");
//                 PlayFailSound();
//                 WriteInI("1");
//                 ///////////////////////////////////////////////////////
//                 threadpar.PlaySound = true; //启动警报器
//                 threadpar._SetTime = 20000; //设定警报器延时时间
//                 if (threadPlayErr != null)  //初始化线程，
//                     threadPlayErr.Abort();  //如果线程存在则退出
//                 threadpar.msg = Err;
//                 threadpar.cue = "请重刷料站";
//                 ///////////////////////////////////////////
//                 return;
//             }
//             else
//             {
//                 //////////////////////////////////////////////////
//                 threadpar.PlaySound = false; //关闭警报器
//                 if (!threadpar.ThreadFlag1) //如果线程还没退出来则手动退出
//                     threadPlayErr.Abort();
//                 threadpar.ThreadFlag1 = true;
//                 threadpar.msg = "";
//                 threadpar.cue = "";
//                 ////////////////////////////////////////////////////
//                 if (!string.IsNullOrEmpty(Msg))
//                 {
//                     ShowMsg(LogMsgType.Normal, Msg + "正在初始化..");
//                     Msg = string.Empty;
//                     CHGCLS = new 换班类(smtkpmonitor);
//                     smtcmd = Smt_Cmd.NON;
//                     ShowCue(LogMsgType.Outgoing, "请刷作业代码");
//                     return;
//                 }
//             }
//             ShowMsg(LogMsgType.Incoming, string.Format("请按照上料表顺序刷入资料--料号:[{1}] + 料站[{2}]已经确认,  还有[{0}]颗料待确认",
//                 CHGCLS.ISmtKpMaterialPreparationTotal - CHGCLS.iSmtKpMaterialPreparation,
//                 CHGCLS.SmtKpMaterialPreparation.Rows[CHGCLS.iSmtKpMaterialPreparation]["kpnumber"].ToString(),
//                 CHGCLS.SmtKpMaterialPreparation.Rows[CHGCLS.iSmtKpMaterialPreparation]["stationno"].ToString()));
//             ShowCue(LogMsgType.Outgoing, "请刷入下一站料盘编号和料站编号");
//             return;
//         }
//         ShowMsg(LogMsgType.Error, "错误!!没有刷初始化命令,请重新开始..");
//         ShowCue(LogMsgType.Outgoing, "请刷初始化命令");
//         WriteInI("1");
//         PlayFailSound();
//         //}
//         break;
//     #endregion
		
	}

}
