package com.fx.scanapp.CHG_WO;

import java.util.HashMap;

import android.os.Message;

import com.fx.scanapp.Machine;
import com.fx.scanapp.CHG_CLS.CommandStationno;
import com.fx.scanapp.DateType.ColParameter;
import com.fx.scanapp.Web.WEB;
import com.fx.scanapp.fileAnalyze.FileAnalyze;
import com.fx.scanapp.fileAnalyze.JsonAnalyze;

public class CommandStationnoCHGWO extends CommandStationno {
	@Override
	public void doTask() {
		Machine.getInstance().inputmsg = msg;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if(msg!=null)
				{
					if(Machine.getInstance().stationno.get(0).equalsIgnoreCase(msg)){
						    WriteLog();
							int i=Machine.getInstance().stationno.size()-1;
							Machine.getInstance().nextdo="请按照上料表顺序刷入资料--料号:["+Machine.getInstance().material.split("\\|")[0]
									                      +"料站["+msg+"]已经确认,还有["+i+"]颗料待确认";
							if(i!=0)
							{
								Machine.getInstance().nextdo+="\n请刷入下一站料盘编号和料站编号";
								Machine.cmand_status = 3;
								Machine.getInstance().stationno.remove(0);
							}
							else
							{

							    EditSmtIOStatus(Machine.getInstance().masterID.split(" ")[0],
							    		Machine.getInstance().masterID.split(" ")[1], ColParameter.已换线);
								GetSmtIO(Machine.getInstance().masterID.split(" ")[0],
										Machine.getInstance().masterID.split(" ")[1]);
							    Machine.getInstance().nextdo+="\n换线完成";
								Machine.getInstance().nextdo+="\n正在初始化..\n请刷作业代码";
								Machine.getInstance().stationno.remove(0);
								Machine.cmand_status = 0;
				                    
							}
					}
					else{
						Machine.getInstance().nextdo="料站错误\n请重刷料站";
//		                PlayFailSound();
						FileAnalyze.WriteINI("1");
						Machine.cmand_bstatus = Machine.cmand_status;
						Machine.cmand_status = 2;
					}
				}
				Message msg = handler.obtainMessage();
				msg.what = 0x11;
				handler.sendMessage(msg);
				
			}
		}).start();
	}
	
	
	@Override
	public boolean WriteLog() {
        HashMap<String, String> mst = new HashMap<String, String>();
        mst.put("USERID", Machine.getInstance().user);
        mst.put("MASTERID", Machine.getInstance().masterID.split(" ")[0]); //this.NEW备料表序列号.Split(' ')[0],
        mst.put("WOID", Machine.getInstance().masterID.split(" ")[1]);
        mst.put("PCBSIDE", Machine.getInstance().SmtKpMaterialPreparation.get(0).getSIDE()); // _dr["side"].ToString(),
        mst.put("MACHINEID", Machine.getInstance().SmtKpMaterialPreparation.get(0).getMACHINEID()); //_dr["machineId"].ToString(),
        mst.put("STATIONID", msg); // _dr["stationno"].ToString(),
        mst.put("FEEDERID", "NA");
        mst.put("LOTID", Machine.getInstance().material.split("\\|")[3]); //arr料盘序列号[3],
        mst.put("KPNUMBER", Machine.getInstance().material.split("\\|")[0]);//_dr["kpnumber"].ToString(),
        // inputtime = Convert.ToDateTime(smtkpmonitor.GetServerDateTime()),// DateTime.Now,
        mst.put("DATA", ColParameter.changeline.toUpperCase());
        mst.put("VENDERCODE", Machine.getInstance().material.split("\\|")[1]); //arr料盘序列号[1],
        mst.put("LOTQTY", Machine.getInstance().material.split("\\|")[4]);  //int.Parse(arr料盘序列号[4]),
        mst.put("MODELNAME", Machine.getInstance().SmtKpMaterialPreparation.get(0).getPARTNUMBER());
        mst.put("DATECODE", Machine.getInstance().material.split("\\|")[2]); //arr料盘序列号[2]
        mst.put("KP_SN", Machine.getInstance().Trsn);
		String str = JsonAnalyze.Jsoncreat(mst);
		mst.clear();
	    mst.put("distring",str);
	    mst.put("ROWID", "");
	    mst.put("cdata", "3");
		WEB.changeURL("http://172.16.173.231/SFIS_WEBSER_TEST/tSmtKpMonitor.asmx");
		WEB.setMethod("InsertSmtKpNormalLog");
		WEB.WebServices(mst);
        return true;
	}

}
