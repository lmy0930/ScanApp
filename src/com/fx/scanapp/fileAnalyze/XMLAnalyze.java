package com.fx.scanapp.fileAnalyze;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

public class XMLAnalyze {

	public static <E> List<E> getNewDataSet(String xml,String packagename) throws XMLException {//ͨ��xml��������
		ByteArrayInputStream tInputStringStream = null;
		List<E> list = null;
		E object = null;//ע��ڵ�����
		String str=null;
		Class cl = null;
		Method[] methods;
		Field[] fields;
	
		try {
			cl = Class.forName(packagename);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		methods = cl.getDeclaredMethods();
		fields = cl.getDeclaredFields();
		Log.v("XML", "start analyze");
		try {
			if (xml != null && !xml.trim().equals("")) {
				tInputStringStream = new ByteArrayInputStream(xml.getBytes());
				XmlPullParser parser = Xml.newPullParser();
				parser.setInput(tInputStringStream, "UTF-8");
				int eventType = parser.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					Log.v("XML", "running analyze2");
					switch (eventType) {
					case XmlPullParser.START_DOCUMENT:// �ĵ���ʼ�¼�,���Խ������ݳ�ʼ������
						list = new ArrayList<E>();
						break;
					case XmlPullParser.START_TAG:// ��ʼԪ���¼�
						String name = parser.getName();
						Log.v("XML", "creat date");
						if(name.equalsIgnoreCase("Table"))// ��ʼ��ǩ
						{
							object = (E) cl.newInstance();
							list.add(object);
							Log.v("XML", "creat date");
							break;
						}
						else
						{
							for(int i = 0; i < fields.length; i++){
								Log.v("XML", fields[i].getName());
								if (name.equalsIgnoreCase(fields[i].getName())) {
										str = parser.nextText();
										fields[i].setAccessible(true);
										fields[i].set(object, str);
										Log.v("XML", "add" + fields[i].getName());
										break;
								}
							}
						}
						break;
					case XmlPullParser.END_TAG:// ����Ԫ���¼�
						break;
					}
					eventType = parser.next();
				}
				tInputStringStream.close();
			}
		} catch (XmlPullParserException e) {
			throw new XMLException(e.toString());
		} catch (IOException e) {
		    throw new XMLException(e.toString());
		} catch (Exception e) {
		    throw new XMLException(e.toString());
		}
		return list;
	}
}
