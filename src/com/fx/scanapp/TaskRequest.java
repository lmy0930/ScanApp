package com.fx.scanapp;

public  class TaskRequest {
	/**
	 * author:刘梦雨
	 * createdate:2016-9-18
	 * description:本抽象类是责任链模式中的任务请求类型，包含并可设置任务请求的级别和内容
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
