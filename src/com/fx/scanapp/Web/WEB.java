package com.fx.scanapp.Web;


import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;

public class WEB {
	private static String TAG = "WEB";
	// Webservice��������ַ
	private static  String SERVER_URL = "http://172.16.173.231/SFIS_WEBSER_TEST/tSmtKpMonitor.asmx";
	//http://172.16.173.231/SFIS_WEBSER_WINCE/tUserInfo.asmx
	// ���õ�webservice����ռ�
	private static final String PACE = "http://tempuri.org/";
	// ���õķ�����
	private static String METHOD = null;

	private static String SOAPACTION = null;

	private static String returntype;// ����ֵ���ͣ�

	//HashMap<String, String> map = new HashMap<String, String>();

	public static void setMethod(String Method) {
		METHOD = Method;
		SOAPACTION = PACE + METHOD;
		Log.d(TAG, "setMethod:"+METHOD);
	}
	
	public static Object WebServices(HashMap<String,String> map){
        
		//��ӡ���������Ϣ
		for (Map.Entry<String,String> element : map.entrySet()) {
        	Log.d(TAG, METHOD + " "+element.getKey()+":"+element.getValue());
		}
		
		// ����HttpTransportSE��˵���� ����webservice��������ַ
		final HttpTransportSE httpSE = new HttpTransportSE(SERVER_URL);
		httpSE.debug = true;
		// ����soapObject���󲢴��������ռ䡢������������
		SoapObject soapObject = new SoapObject(PACE,METHOD);
		for (Map.Entry<String,String> element : map.entrySet()) {
			soapObject.addProperty(element.getKey(), element.getValue());
		}
		// ����SoapSerializationEnvelope���󲢴���SOAPЭ��İ汾��
		final SoapSerializationEnvelope soapserial = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		soapserial.bodyOut = soapObject;
		// ������.NET�ṩ��Web service���������õļ�����
		soapserial.dotNet = true;
		// ����HttpTransportSE�����call���������� webserice
		try {
			httpSE.call(SOAPACTION, soapserial);
		} catch(SocketTimeoutException e1){
			e1.printStackTrace();
			Log.d(TAG, e1.getMessage());
			return null;
		}catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.d(TAG, e1.getMessage());
			return null;
		} catch (XmlPullParserException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			Log.d(TAG, e1.getMessage());
			return null;
		}
		try {
			if (soapserial.getResponse() != null) {
				// ��ȡ��������Ӧ���ص�SOAP��Ϣ
				SoapObject result = (SoapObject) soapserial.bodyIn;
				Log.d(TAG, "Result:");
				Log.d(TAG, result.toString());
				if (returntype!=null&&returntype.equalsIgnoreCase("list")) {
					SoapObject detail = (SoapObject) result.getProperty(METHOD
							+ "Result");

					List<String> arraylist = new ArrayList<String>();
					Log.d(TAG, "Result:");
					// ����������Ϣ
					for (int i = 0; i < detail.getPropertyCount(); i++) {
						arraylist.add(detail.getProperty(i).toString());
						Log.d(TAG, detail.getProperty(i).toString());
					}
					return arraylist;
				}
				return result.toString();
			}
		}catch(SoapFault e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
			return null;
		}catch(IOException e){
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
			return null;
		}catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, e.getMessage());
			return null;
		}
		return null;
	}

    public static void changeURL(String URL)
    {
		SERVER_URL = URL;
	}
    
    public static void setRturnType(String Type){
    	returntype=Type;
    }
}
