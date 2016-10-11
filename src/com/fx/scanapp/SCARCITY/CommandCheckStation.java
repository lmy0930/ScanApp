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
					Machine.getInstance().nextdo = "��ˢ����Ȩ��";
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
						    Machine.getInstance().nextdo = "��վ���벻���ڻ�ˢ���Ϻ�����վ��ƥ��\n��ˢ��ȷ����վ.. ";
	                        //PlayFailSound();
						    FileAnalyze.WriteINI("1");
							Machine.cmand_bstatus = Machine.cmand_status;
							Machine.cmand_status = 2;
	                    }
	                    else
	                    {
	                    	Machine.getInstance().nextdo = "�����ύȱ����Ϣ";
	                    	//handler.sendMessage(m);
	                    	String Err;
	                        if ((Err=addInformation())!=null)
	                        {
	                        	Machine.getInstance().nextdo = Err+"\n��ˢ��ȱ�����̱��";
	                            //PlayFailSound();
	                            FileAnalyze.WriteINI("1");
	    						Machine.cmand_bstatus = 3;
	    						Machine.cmand_status = 2;
	                        }
	                        else
	                        {
	                        	Machine.getInstance().nextdo = "ȱ����Ϣ�Ѿ��ύ,��ȴ�����..\n��ˢ����ҵ����";
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
            	Err = "����!!һ����̨ͬʱֻ����һ��ȱ����Ϣ";
                return Err;
            }
            else
            {
            	String[] arr���̱�� = Machine.getInstance().material.split("\\|");
            	WEB.setMethod("InsertSmtKpMonitor");
            	map.clear();
            	map.put("MASTERID", Machine.getInstance().masterID.split(" ")[0]);
        		map.put("WOID",Machine.getInstance().masterID.split(" ")[1]);
        		map.put("MACHINEID",Machine.getInstance().clist.get(0).getMACHINEID());
        		map.put("STATIONNO",msg);
        		map.put("CDATA",String.valueOf(ColParameter.ȱ��));
        		map.put("KPNUMBER",arr���̱��[0]);
        		map.put("SCARCITYUSER", Machine.getInstance().user);
        		rb = (String) WEB.WebServices(map).toString().split("=")[1]
    					.split(";")[0];
            }
			
		} catch (Exception e) {
        	Err = "����!!ȱ����Ϣ���ʧ��,�����²���";
            return Err;
		}
         return null;
     }
}
