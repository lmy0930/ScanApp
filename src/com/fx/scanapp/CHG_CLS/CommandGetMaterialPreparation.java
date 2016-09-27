package com.fx.scanapp.CHG_CLS;

import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.fx.scanapp.Machine;
import com.fx.scanapp.TaskNode;
import com.fx.scanapp.CommonTask.PublicMethods;
import com.fx.scanapp.DateType.NewDataSet;
import com.fx.scanapp.fileAnalyze.FileAnalyze;

public class CommandGetMaterialPreparation extends TaskNode{
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
					Machine.cmand_bstatus = Machine.cmand_status;
					Machine.cmand_status = 2;
					Machine.getInstance().nextdo = "��ˢ����Ȩ��";
				} else {
					String Material;// Trsn��Ϣ
					if ((Material = PublicMethods.GetTrsnMaterial(msg,
							Machine.getInstance().masterID.split(" ")[1])) == null) {
						// PlayFailSound();
						FileAnalyze.WriteINI("1");
						Machine.cmand_bstatus=Machine.cmand_status;
						Machine.cmand_status = 2;
					} else {
						if (Material.split("\\|").length != 5) {
							Machine.getInstance().nextdo = "���һ�����ʽ����\n��ˢ���̱��.. ";
							// PlayFailSound();
							FileAnalyze.WriteINI("1");
						} else {
			                List<NewDataSet> SmtKpMaterialPreparation =
			                                    GetMaterialPreparation(
			                                    		Machine.getInstance().masterID.split(" ")[1],
			                        		            Machine.getInstance().masterID.split(" ")[0]);
			                    if (SmtKpMaterialPreparation.size() < 1)
			                    {
			                    	Machine.getInstance().nextdo = "�����³�ʼ�� \nECN ���!!!!!!!!";
			                        //PlayFailSound();
			                        FileAnalyze.WriteINI("1");
			                    }
			                    else
			                    {
			                        CHGCLS.smtkpstation = SmtKpMaterialPreparation.DefaultView.ToTable(true, "stationno");
			                      //�жϵڼ�����վ�Ƿ���������
			    		            bool flag = false;
			    		            foreach (DataRow dr in CHGCLS.SmtKpMaterialPreparation.Select(string.Format("stationno='{0}'",
			    		                 CHGCLS.smtkpstation.Rows[CHGCLS.passtotal]["stationno"].ToString())))
			    		            {
			    		                if (dr["kpnumber"].ToString().ToUpper() == threadpar.InputText.Split('|')[0])
			    		                {
			    		                    flag = true;
			    		                    break;
			    		                }
			    		            }
			    		            if (!flag)
			    		            {
			    		            	Machine.getInstance().nextdo = "ˢ���ϺŴ���\n�밴����վ��˳��ˢ��..";
			    		                //PlayFailSound();
			    		                FileAnalyze.WriteINI("1");
			    		            }

			    		            int it = -1;
			    		            for (int i = 0; i < 5; i++)
			    		            {
			    		                if (Material.split("\\|")[0].equalsIgnoreCase(
			    		                    SmtKpMaterialPreparation.get(i).getKPNUMBER()))
			    		                {
			    		                    it = i;
			    		                    break;
			    		                }
			    		            }
			    		            if (it != -1)
			    		            {
			    		                CHGCLS.iSmtKpMaterialPreparation = CHGCLS.iSmtKpMaterialPreparation + it;

			    		            }
			    		            else
			    		            {
			    		            	Machine.getInstance().nextdo = "ˢ���ϺŴ���\n�밴����վ��˳��ˢ��..";
			    		                //PlayFailSound();
			    		            	FileAnalyze.WriteINI("1");

			    		                return;
			    		            }
			    		            Machine.getInstance().material=Material;
			    		            Machine.getInstance().nextdo ="�Ϻ��Ѿ���¼:"+ Material.split("\\|")[0]+"ˢ���ϺŴ���\n��ˢ��վ���";
			    		        }
			                }
						}
				}
			}
		}).start();	
	}
	
	private List<NewDataSet> GetMaterialPreparation(String WOID,String MASTERID){
		return null;
		
	}

}
