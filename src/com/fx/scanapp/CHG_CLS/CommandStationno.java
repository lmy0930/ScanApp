package com.fx.scanapp.CHG_CLS;

import com.fx.scanapp.TaskNode;

public class CommandStationno extends TaskNode {

	@Override
	public float getTaskLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTaskLevel(int level) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doTask() {
		if(msg!=null)
		{
//          ShowCue(LogMsgType.Warning, "���ݴ�����..");
//          if (!CHGCLS.CheckKpIndex(out Err, out Msg))
//          {
//              ShowMsg(LogMsgType.Error, Err);
//              //   CHGCLS.�������к� = string.Empty;
//              CHGCLS.��վ��� = string.Empty;
//              ShowCue(LogMsgType.Warning, "����ˢ��վ");
//              PlayFailSound();
//              WriteInI("1");
//          }
//          else
//          {
//              if (!string.IsNullOrEmpty(Msg))
//              {
//                  ShowMsg(LogMsgType.Normal, Msg + "���ڳ�ʼ��..");
//                  Msg = string.Empty;
//                  CHGCLS = new ������(smtkpmonitor);
//                  smtcmd = Smt_Cmd.NON;
//                  ShowCue(LogMsgType.Outgoing, "��ˢ��ҵ����");
//                  return;
//              }
//          }
		}
//      if (string.IsNullOrEmpty(CHGCLS.��վ���))
//      {
//          CHGCLS.��վ��� = threadpar.InputText;
//          ShowCue(LogMsgType.Warning, "���ݴ�����..");
//
//          if (!CHGCLS.CheckKpIndex(out Err, out Msg))
//          {
//              ShowMsg(LogMsgType.Error, Err);
//              //   CHGCLS.�������к� = string.Empty;
//              CHGCLS.��վ��� = string.Empty;
//              ShowCue(LogMsgType.Warning, "����ˢ��վ");
//              PlayFailSound();
//              WriteInI("1");
//          }
//          else
//          {
//              if (!string.IsNullOrEmpty(Msg))
//              {
//                  ShowMsg(LogMsgType.Normal, Msg + "���ڳ�ʼ��..");
//                  Msg = string.Empty;
//                  CHGCLS = new ������(smtkpmonitor);
//                  smtcmd = Smt_Cmd.NON;
//                  ShowCue(LogMsgType.Outgoing, "��ˢ��ҵ����");
//                  return;
//              }
//          }
//          ShowMsg(LogMsgType.Incoming, string.Format("�밴�����ϱ�˳��ˢ������--�Ϻ�:[{1}] + ��վ[{2}]�Ѿ�ȷ��,  ����[{0}]���ϴ�ȷ��",
//              CHGCLS.ISmtKpMaterialPreparationTotal - CHGCLS.iSmtKpMaterialPreparation,
//              CHGCLS.SmtKpMaterialPreparation.Rows[CHGCLS.iSmtKpMaterialPreparation]["kpnumber"].ToString(),
//              CHGCLS.SmtKpMaterialPreparation.Rows[CHGCLS.iSmtKpMaterialPreparation]["stationno"].ToString()));
//          ShowCue(LogMsgType.Outgoing, "��ˢ����һվ���̱�ź���վ���");
//          return;
//      }
//      ShowMsg(LogMsgType.Error, "����!!û��ˢ��ʼ������,�����¿�ʼ..");
//      ShowCue(LogMsgType.Outgoing, "��ˢ��ʼ������");
//      WriteInI("1");
//      PlayFailSound();
//      //}
//      break;

	}

}
