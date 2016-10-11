package com.fx.scanapp.SCARCITY;

import java.util.HashMap;
import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
import com.fx.scanapp.DateType.ColParameter;
import com.fx.scanapp.DateType.Material;
import com.fx.scanapp.Web.WEB;
import com.fx.scanapp.fileAnalyze.FileAnalyze;
import com.fx.scanapp.fileAnalyze.JsonAnalyze;
import com.fx.scanapp.fileAnalyze.XMLAnalyze;

public class CommandCheckStation extends TaskNode {
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message m) {
			super.handleMessage(m);
			if (m.what == 0x11) {
				Machine.getInstance().execute();
			}
		}
	};
	/**
	 * <NewDataSet> 
	 * <Table> 
	 * <STATUS>3</STATUS> 
	 * <MACHINEID>060101SL</MACHINEID>
	 * <SIDE>T</SIDE>
	 * </Table> 
	 * </NewDataSet></

	 * */
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
		final Message m = handler.obtainMessage();
		m.what = 0x11;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				if (FileAnalyze.ReadINI()) {
					Machine.cmand_bstatus = Machine.cmand_status;
					Machine.cmand_status = 2;
					Machine.getInstance().nextdo = "请刷主管权限";
				} 
				else {
					List<Material> dt=Machine.getInstance().data_KpNumberInSEQ;
					int count=0;
					for (Material material : dt) {
						if(material.getSTATIONNO().equalsIgnoreCase(msg))
							count++;	
					}
					 if (dt == null ||count< 1)
	                    {
						    Machine.getInstance().nextdo = "料站号码不存在或刷入料号与料站不匹配\n请刷正确的料站.. ";
	                        //PlayFailSound();
						    FileAnalyze.WriteINI("1");
							Machine.cmand_bstatus = Machine.cmand_status;
							Machine.cmand_status = 2;
	                    }
	                    else
	                    {
	                    	Machine.getInstance().nextdo = "正在提交缺料信息";
	                    	//handler.sendMessage(m);
	                    	String Err;
	                        if ((Err=addInformation())!=null)
	                        {
	                        	Machine.getInstance().nextdo = Err+"\n请刷入缺料料盘编号";
	                            //PlayFailSound();
	                            FileAnalyze.WriteINI("1");
	    						Machine.cmand_bstatus = 3;
	    						Machine.cmand_status = 2;
	                        }
	                        else
	                        {
	                        	Machine.getInstance().nextdo = "缺料信息已经提交,请等待补料..\n请刷入作业命令";
	    						Machine.cmand_status = 0;
	                        }
	                    }
	               }
				handler.sendMessage(m);
					
				}                    

		}).start();

	}
	
	@SuppressWarnings("unchecked")
	public String addInformation(){
		String Err;
		WEB.changeURL("http://172.16.173.231/SFIS_WEBSER_TEST/tSmtKpMonitor.asmx");
		WEB.setMethod("CheckSCARCITYStation");
		HashMap map = new HashMap<String, String>();
		map.put("Masterid", Machine.getInstance().masterID.split(" ")[0]);
		map.put("woId",Machine.getInstance().masterID.split(" ")[1]);
		map.put("Machine",Machine.getInstance().clist.get(0).getMACHINEID());
		map.put("Station",msg.substring(0, 2));
		String rb;
		try {
			rb =  WEB.WebServices(map).toString().split("=")[1]
					.split(";")[0];
            if(!rb.equalsIgnoreCase("true")){
            	Err = "错误!!一个车台同时只能有一个缺料信息";
                return Err;
            }
            else
            {
            	String[] arr料盘编号 = Machine.getInstance().material.split("\\|");
            	WEB.setMethod("InsertSmtKpMonitor");
            	map.clear();
            	map.put("MASTERID", Machine.getInstance().masterID.split(" ")[0]);
        		map.put("WOID",Machine.getInstance().masterID.split(" ")[1]);
        		map.put("MACHINEID",Machine.getInstance().clist.get(0).getMACHINEID());
        		map.put("STATIONNO",msg);
        		map.put("CDATA",String.valueOf(ColParameter.缺料));
        		map.put("KPNUMBER",arr料盘编号[0]);
        		map.put("SCARCITYUSER", Machine.getInstance().user);
        		rb = (String) WEB.WebServices(map).toString().split("=")[1]
    					.split(";")[0];
            }
			
		} catch (Exception e) {
        	Err = "错误!!缺料信息添加失败,请重新操作";
            return Err;
		}
         return null;
     }
}
