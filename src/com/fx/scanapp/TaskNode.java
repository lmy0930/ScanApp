package com.fx.scanapp;

public abstract class TaskNode {
	/**
	 * author:刘梦雨
	 * createdate:2016-9-18
	 * description:本抽象类是责任链模式中的任务节点模型，可用于导出实体任务节点。
	                               由此可将复杂的任务逻辑分解成精简的任务片，不同的任务片可在
	                ChainRespon以及其子类中自由组装和设置执行顺序，从而实现业
	                               务逻辑的解耦。
	 */
	protected TaskNode nextTask;//任务节点尾指针
	protected TaskRequest taskrequest;//任务请求
	protected int taskLevel;//任务级别
	protected String msg;//任务信息
	
	
	
	public final void TaskRequest(TaskRequest task)
	{
		/**
		 * 名称：TaskRequest()
		 * 参数类型：TaskRequest类型
		 * 返回值：无
		 * 作用：TaskRequest()函数用于匹配任务请求的级别和任务节点自身的级别，并调用处理函数或者转发给下一任务节点
		*/
		if(getTaskLevel()==task.getTaskLevel())			
		{
			System.out.println("当前需求等级匹配，执行处理");
			this.msg=task.Msg;
			doTask();
		}
		else
		{
			if(nextTask!=null)
			{
				System.out.println("当前需求等级不匹配，交由下一环节处理");
				nextTask.TaskRequest(task);
			}
			else
			{
				System.out.println("所有环节无法处理当前请求");
			}
		}
	}
	
	public final void setnext(TaskNode taskNode)
	{
		/**
		 * 名称：setnext()
		 * 参数类型：TaskNode任务节点尾指针
		 * 返回值：无
		 * 作用：setnext()函数用于构建两个任务节点的链接关系
		*/
		this.nextTask=taskNode;
	}
	
	public final void send(int taskLevel,String msg)
	{
		/**
		 * 名称：send()
		 * 参数类型：int型 任务级别,String型 任务信息
		 * 返回值：无
		 * 作用：send()函数用于创建任务请求并发送
		*/
		taskrequest=new TaskRequest();
		taskrequest.setRequest(taskLevel, msg);
		TaskRequest(taskrequest);
		
	}
	
	public abstract float getTaskLevel();
	
	public abstract void setTaskLevel(int level);
	
	public abstract void doTask();
	

}
