package com.fx.scanapp.CHG_KP;

import java.util.HashMap;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Xml;

import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
import com.fx.scanapp.DateType.ColParameter;
import com.fx.scanapp.DateType.Kpnumber;
import com.fx.scanapp.DateType.Material;
import com.fx.scanapp.Web.WEB;
import com.fx.scanapp.fileAnalyze.FileAnalyze;
import com.fx.scanapp.fileAnalyze.JsonAnalyze;
import com.fx.scanapp.fileAnalyze.XMLAnalyze;
import com.fx.scanapp.fileAnalyze.XMLException;

public class CommandCHGKPCLS extends TaskNode implements ColParameter {
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
				if (Machine.getInstance().station== null) {
					 if (!CHgKp())
					 {
					 Machine.getInstance().materialID=null;
					 //PlayFailSound();
					 FileAnalyze.WriteINI("1");
						Machine.cmand_bstatus = Machine.cmand_status;
						Machine.cmand_status = 2;
					 }
					 else
					 {
					   Machine.getInstance().nextdo ="换料完成 ";
				       Machine.getInstance().nextdo +="\n请刷作业代码..";
						Machine.cmand_status = 0;
					}
				} else {
					 Machine.getInstance().nextdo ="错误!!没有刷初始化命令,请重新开始..";
					 FileAnalyze.WriteINI("1");
					// PlayFailSound();
				}
				Message msg = handler.obtainMessage();
				msg.what = 0x11;
				handler.sendMessage(msg);
				
			}
		}).start();
	}

	private boolean CHgKp() {
		String[] arr料盘序列号 = Machine.getInstance().materialID.split("\\|");
		if (Machine.getInstance().masterID != null
				&& Machine.getInstance().materialID != null && msg != null)// 料站不为空
		{
			WEB.changeURL("http://172.16.173.231/SFIS_WEBSER_TEST/tSmtKpMonitor.asmx");
			WEB.setMethod("CheckKpSupply_ForMobile");
			HashMap<String, String> mst = new HashMap<String, String>();
			mst.put("MASTERID", Machine.getInstance().masterID.split(" ")[0]);
			mst.put("WOID", Machine.getInstance().masterID.split(" ")[1]);
			mst.put("STATION", msg);
			mst.put("KPNUMBER","");
			mst.put("CDATA", String.valueOf(ColParameter.已经补料));
			String str = JsonAnalyze.Jsoncreat(mst);
			mst.clear();
			mst.put("Json", str);
			String rb = (String) WEB.WebServices(mst).toString().split("=")[1]
					.split(";")[0];
			/**
			 * <NewDataSet>
			 * <Table>
			 * <ROWID>761193</ROWID> <MASTERID>S021672</MASTERID>
			 * <WOID>702000973</WOID> <MACHINEID>060101SL</MACHINEID>
			 * <STATIONNO>0113L</STATIONNO> <CDATA>1</CDATA>
			 * <KPNUMBER>AF-C1000683</KPNUMBER>
			 * <SCARCITYTIME>2015-12-23T21:10:18+08:00</SCARCITYTIME>
			 * <SUPPLYUSER>K01408</SUPPLYUSER> <FLAG>0</FLAG> <QTY>10000</QTY>
			 * <VENDERCODE>200216</VENDERCODE> <DATECODE>20151204</DATECODE>
			 * <LOTID>20151204</LOTID> <TRSN>1512040002561</TRSN>
			 * </Table>
			 * </NewDataSet>
			 */
			List<Material> dt;
			try {
				dt = XMLAnalyze.getNewDataSet(rb, (new Material())
						.getClass().getName());
				if (dt.size() > 1) {
					Machine.getInstance().nextdo = String.format(
							"错误!!!在补料中发现, 一个备料编号下,站位[{0}]存在多笔资料", msg);
					return false;
				}

				if (dt.size() < 1) {
					Machine.getInstance().nextdo = String.format(
							"错误!!! 站位<{0}>不存在备料信息", msg);
					return false;
				}

				// 判定该料备料室是否已经补料
				if (!dt.get(0).getCDATA()
						.equalsIgnoreCase(String.valueOf(ColParameter.已经补料)))
					Machine.getInstance().nextdo = "错误!!! 该物料还没有备料,不能进行换料";
				// 比对料号
				if (!dt.get(0).getKPNUMBER().equalsIgnoreCase(arr料盘序列号[0])) {
					Machine.getInstance().nextdo = "错误!!! 错料,新料盘信息与上料位置不符!";
					return false;
				}
				// //比对厂商
				if (!dt.get(0).getVENDERCODE().equalsIgnoreCase(arr料盘序列号[1])){
					Machine.getInstance().nextdo = "警告!!! 新上的物料与上次物料的厂商不一致!";
					return false;
				}
				 //比对datecode
				 if (!dt.get(0).getDATECODE().equalsIgnoreCase(arr料盘序列号[2])){
						Machine.getInstance().nextdo = "警告!!! 新上的物料与上次物料的DATECODE不一致!";
						return false;
				 }
				    
				 //比对批次
				 if (!dt.get(0).getLOTID().equalsIgnoreCase(arr料盘序列号[3])){
						Machine.getInstance().nextdo = "警告!!! 新上的物料与上次物料的批次不一致!";
						return false;
				 }
				 
			} catch (XMLException e) {
				e.printStackTrace();
				Machine.getInstance().nextdo = e.toString();
				return false;
			}
			 mst.clear();
			 mst.put("USERID", Machine.getInstance().user + "-" + Machine.getInstance().mlist.USERID);
			 mst.put("MASTERID",
			 Machine.getInstance().masterID.split(" ")[0]);
			 mst.put("WOID", Machine.getInstance().masterID.split(" ")[1]);
			 mst.put("PCBSIDE", Machine.getInstance().换料面别);
			 mst.put("MACHINEID",dt.get(0).getMACHINEID());//this.换料机台号);
			 mst.put("STATIONID",msg);
			 mst.put("FEEDERID", "NA");
			 mst.put("LOTID", arr料盘序列号[3]);
			 mst.put("KPNUMBER", arr料盘序列号[0]);
			 mst.put("DATA",ColParameter.changekp.toUpperCase());
			 mst.put("VENDERCODE", arr料盘序列号[1]);
			 mst.put("LOTQTY", arr料盘序列号[4]);
			 mst.put("MODELNAME",Machine.getInstance().换料Model);
			 mst.put("DATECODE", arr料盘序列号[2]);
			 mst.put("KP_SN", Machine.getInstance().Trsn);
		     String Djson = JsonAnalyze.Jsoncreat(mst);
		     mst.clear();
		     mst.put("distring",Djson);
		     mst.put("ROWID", dt.get(0).getROWID());
		     mst.put("cdata", "2");
			 WEB.changeURL("http://172.16.173.231/SFIS_WEBSER_TEST/tSmtKpMonitor.asmx");
			 WEB.setMethod("InsertSmtKpNormalLog");
			 Machine.getInstance().nextdo=(String)WEB.WebServices(mst).toString().split("=")[1]
						.split(";")[0];
			 if (!Machine.getInstance().nextdo.equalsIgnoreCase("OK"))
			     return false;
			
			 return true;
		}
		return false;
	}
}
