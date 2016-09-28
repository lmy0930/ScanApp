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
//          ShowCue(LogMsgType.Warning, "数据处理中..");
//          if (!CHGCLS.CheckKpIndex(out Err, out Msg))
//          {
//              ShowMsg(LogMsgType.Error, Err);
//              //   CHGCLS.料盘序列号 = string.Empty;
//              CHGCLS.料站编号 = string.Empty;
//              ShowCue(LogMsgType.Warning, "请重刷料站");
//              PlayFailSound();
//              WriteInI("1");
//          }
//          else
//          {
//              if (!string.IsNullOrEmpty(Msg))
//              {
//                  ShowMsg(LogMsgType.Normal, Msg + "正在初始化..");
//                  Msg = string.Empty;
//                  CHGCLS = new 换班类(smtkpmonitor);
//                  smtcmd = Smt_Cmd.NON;
//                  ShowCue(LogMsgType.Outgoing, "请刷作业代码");
//                  return;
//              }
//          }
		}
//      if (string.IsNullOrEmpty(CHGCLS.料站编号))
//      {
//          CHGCLS.料站编号 = threadpar.InputText;
//          ShowCue(LogMsgType.Warning, "数据处理中..");
//
//          if (!CHGCLS.CheckKpIndex(out Err, out Msg))
//          {
//              ShowMsg(LogMsgType.Error, Err);
//              //   CHGCLS.料盘序列号 = string.Empty;
//              CHGCLS.料站编号 = string.Empty;
//              ShowCue(LogMsgType.Warning, "请重刷料站");
//              PlayFailSound();
//              WriteInI("1");
//          }
//          else
//          {
//              if (!string.IsNullOrEmpty(Msg))
//              {
//                  ShowMsg(LogMsgType.Normal, Msg + "正在初始化..");
//                  Msg = string.Empty;
//                  CHGCLS = new 换班类(smtkpmonitor);
//                  smtcmd = Smt_Cmd.NON;
//                  ShowCue(LogMsgType.Outgoing, "请刷作业代码");
//                  return;
//              }
//          }
//          ShowMsg(LogMsgType.Incoming, string.Format("请按照上料表顺序刷入资料--料号:[{1}] + 料站[{2}]已经确认,  还有[{0}]颗料待确认",
//              CHGCLS.ISmtKpMaterialPreparationTotal - CHGCLS.iSmtKpMaterialPreparation,
//              CHGCLS.SmtKpMaterialPreparation.Rows[CHGCLS.iSmtKpMaterialPreparation]["kpnumber"].ToString(),
//              CHGCLS.SmtKpMaterialPreparation.Rows[CHGCLS.iSmtKpMaterialPreparation]["stationno"].ToString()));
//          ShowCue(LogMsgType.Outgoing, "请刷入下一站料盘编号和料站编号");
//          return;
//      }
//      ShowMsg(LogMsgType.Error, "错误!!没有刷初始化命令,请重新开始..");
//      ShowCue(LogMsgType.Outgoing, "请刷初始化命令");
//      WriteInI("1");
//      PlayFailSound();
//      //}
//      break;

	}

}
