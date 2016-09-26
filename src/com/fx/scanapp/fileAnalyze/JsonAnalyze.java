package com.fx.scanapp.fileAnalyze;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonAnalyze {

	public static String Jsoncreat(HashMap<String, String> map)//��װjson��ʽ����
	{
		JSONObject jsonobject=new JSONObject();
		for(Map.Entry<String, String> element : map.entrySet())
		{
			try {
				jsonobject.put(element.getKey(), element.getValue());
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return jsonobject.toString();
		
	}
}
