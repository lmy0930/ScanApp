package com.fx.scanapp.CommonTask;

import java.util.HashMap;
import java.util.List;

import com.fx.scanapp.Machine;
import com.fx.scanapp.DateType.Material;
import com.fx.scanapp.Web.WEB;
import com.fx.scanapp.fileAnalyze.JsonAnalyze;
import com.fx.scanapp.fileAnalyze.XMLAnalyze;

public class PublicMethods {
	public static String GetTrsnMaterial(String msg, String WOID) {
		WEB.changeURL("http://172.16.173.231/SFIS_WEBSER_TEST/tSmtKpMonitor.asmx");
		WEB.setMethod("Get_TrsnData_ForMobile");
		HashMap map = new HashMap<String, String>();
		map.put("TRSN", msg);
		String str = JsonAnalyze.Jsoncreat(map);
		map.clear();
		map.put("Json", str);
		String dt;
		try {
			dt = (String) WEB.WebServices(map).toString().split("Result=")[1]
					.split(";")[0];
		} catch (Exception e) {
			dt = null;
			return dt;
		}
		try {
			List<Material> material = XMLAnalyze.getNewDataSet(dt,
					(new Material()).getClass().getName());
			if (!WOID.equalsIgnoreCase(material.get(0).getWOID())) {
				HashMap<String, String> mst = new HashMap<String, String>();
				mst.put("NEW_WOID", WOID);
				String Fstr = JsonAnalyze.Jsoncreat(mst);
				mst.clear();
				mst.put("Json", Fstr);
				WEB.setMethod("Get_Smt_WO_Merge_ForMobile");
				WEB.setRturnType("list");
				List<String> dtSmtMoMerge = (List<String>) WEB.WebServices(mst);
				WEB.setRturnType(null);
				if (dtSmtMoMerge.size() == 0)// �ж��ϲ��ľɹ��������Ϲ����Ƿ���ͬ
				{
					Machine.getInstance().nextdo = "���Ϲ�����ͬ\n��ˢ��Ӧ������Ψһ����";
					return null;
				} else {
					for (String element : dtSmtMoMerge) {// �ж��ϲ��ľɹ��������Ϲ����Ƿ���ͬ
						if (element.equalsIgnoreCase(WOID)) {
							String MaterialSn = material.get(0).getKP_NO()
									+ "|" + material.get(0).getVENDER_ID()
									+ "|" + material.get(0).getDATE_CODE()
									+ "|" + material.get(0).getLOT_CODE() + "|"
									+ material.get(0).getQTY();
							return MaterialSn;
						}
					}
					Machine.getInstance().nextdo = "���Ϲ�����ͬ\n��ˢ��Ӧ������Ψһ����";
					return null;

				}
			} else {
				String MaterialSn = material.get(0).getKP_NO() + "|"
						+ material.get(0).getVENDER_ID() + "|"
						+ material.get(0).getDATE_CODE() + "|"
						+ material.get(0).getLOT_CODE() + "|"
						+ material.get(0).getQTY();
				return MaterialSn;
			}
		} catch (Exception e) {
			Machine.getInstance().nextdo = "Ψһ�������\n��ˢ��ȷ��Ψһ����";
			return null;

		}
	}

}
