package com.fx.scanapp;

public  class TaskRequest {
	/**
	 * author:������
	 * createdate:2016-9-18
	 * description:����������������ģʽ�е������������ͣ���������������������ļ��������
	 */
	private Object obj;
	protected float TaskLevel;
	protected String Msg;
	
	public TaskRequest(Object obj) {
		this.obj=obj;
	}
	
	public TaskRequest() {
	}
	
	public void setRequest(int taskLevel,String msg){
		this.TaskLevel=taskLevel;
		Msg=msg;
		
	}
	
	public  float getTaskLevel(){
		return TaskLevel;
	}

}
