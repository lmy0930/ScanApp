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
					Machine.getInstance().nextdo = "��ˢ����Ȩ��";
				} else {
					Machine.cmand_status = 3;
					Machine.getInstance().nextdo = "��ˢ�������к�";
				}
				Message msg = handler.obtainMessage();
				msg.what = 0x11;
				handler.sendMessage(msg);
				
			}
		}).start();
//		 
//         if (string.IsNullOrEmpty(CHGCLS.��վ���))
//         {
//             CHGCLS.��վ��� = threadpar.InputText;
//             ShowCue(LogMsgType.Warning, "���ݴ�����..");
//
//             if (!CHGCLS.CheckKpIndex(out Err, out Msg))
//             {
//                 ShowMsg(LogMsgType.Error, Err);
//                 //   CHGCLS.�������к� = string.Empty;
//                 CHGCLS.��վ��� = string.Empty;
//                 ShowCue(LogMsgType.Warning, "����ˢ��վ");
//                 PlayFailSound();
//                 WriteInI("1");
//                 ///////////////////////////////////////////////////////
//                 threadpar.PlaySound = true; //����������
//                 threadpar._SetTime = 20000; //�趨��������ʱʱ��
//                 if (threadPlayErr != null)  //��ʼ���̣߳�
//                     threadPlayErr.Abort();  //����̴߳������˳�
//                 threadpar.msg = Err;
//                 threadpar.cue = "����ˢ��վ";
//                 ///////////////////////////////////////////
//                 return;
//             }
//             else
//             {
//                 //////////////////////////////////////////////////
//                 threadpar.PlaySound = false; //�رվ�����
//                 if (!threadpar.ThreadFlag1) //����̻߳�û�˳������ֶ��˳�
//                     threadPlayErr.Abort();
//                 threadpar.ThreadFlag1 = true;
//                 threadpar.msg = "";
//                 threadpar.cue = "";
//                 ////////////////////////////////////////////////////
//                 if (!string.IsNullOrEmpty(Msg))
//                 {
//                     ShowMsg(LogMsgType.Normal, Msg + "���ڳ�ʼ��..");
//                     Msg = string.Empty;
//                     CHGCLS = new ������(smtkpmonitor);
//                     smtcmd = Smt_Cmd.NON;
//                     ShowCue(LogMsgType.Outgoing, "��ˢ��ҵ����");
//                     return;
//                 }
//             }
//             ShowMsg(LogMsgType.Incoming, string.Format("�밴�����ϱ�˳��ˢ������--�Ϻ�:[{1}] + ��վ[{2}]�Ѿ�ȷ��,  ����[{0}]���ϴ�ȷ��",
//                 CHGCLS.ISmtKpMaterialPreparationTotal - CHGCLS.iSmtKpMaterialPreparation,
//                 CHGCLS.SmtKpMaterialPreparation.Rows[CHGCLS.iSmtKpMaterialPreparation]["kpnumber"].ToString(),
//                 CHGCLS.SmtKpMaterialPreparation.Rows[CHGCLS.iSmtKpMaterialPreparation]["stationno"].ToString()));
//             ShowCue(LogMsgType.Outgoing, "��ˢ����һվ���̱�ź���վ���");
//             return;
//         }
//         ShowMsg(LogMsgType.Error, "����!!û��ˢ��ʼ������,�����¿�ʼ..");
//         ShowCue(LogMsgType.Outgoing, "��ˢ��ʼ������");
//         WriteInI("1");
//         PlayFailSound();
//         //}
//         break;
//     #endregion
		
	}

}
