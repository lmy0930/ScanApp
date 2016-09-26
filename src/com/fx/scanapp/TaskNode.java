package com.fx.scanapp;

public abstract class TaskNode {
	/**
	 * author:������
	 * createdate:2016-9-18
	 * description:����������������ģʽ�е�����ڵ�ģ�ͣ������ڵ���ʵ������ڵ㡣
	                               �ɴ˿ɽ����ӵ������߼��ֽ�ɾ��������Ƭ����ͬ������Ƭ����
	                ChainRespon�Լ���������������װ������ִ��˳�򣬴Ӷ�ʵ��ҵ
	                               ���߼��Ľ��
	 */
	protected TaskNode nextTask;//����ڵ�βָ��
	protected TaskRequest taskrequest;//��������
	protected int taskLevel;//���񼶱�
	protected String msg;//������Ϣ
	
	
	
	public final void TaskRequest(TaskRequest task)
	{
		/**
		 * ���ƣ�TaskRequest()
		 * �������ͣ�TaskRequest����
		 * ����ֵ����
		 * ���ã�TaskRequest()��������ƥ����������ļ��������ڵ�����ļ��𣬲����ô���������ת������һ����ڵ�
		*/
		if(getTaskLevel()==task.getTaskLevel())			
		{
			System.out.println("��ǰ����ȼ�ƥ�䣬ִ�д���");
			this.msg=task.Msg;
			doTask();
		}
		else
		{
			if(nextTask!=null)
			{
				System.out.println("��ǰ����ȼ���ƥ�䣬������һ���ڴ���");
				nextTask.TaskRequest(task);
			}
			else
			{
				System.out.println("���л����޷�����ǰ����");
			}
		}
	}
	
	public final void setnext(TaskNode taskNode)
	{
		/**
		 * ���ƣ�setnext()
		 * �������ͣ�TaskNode����ڵ�βָ��
		 * ����ֵ����
		 * ���ã�setnext()�������ڹ�����������ڵ�����ӹ�ϵ
		*/
		this.nextTask=taskNode;
	}
	
	public final void send(int taskLevel,String msg)
	{
		/**
		 * ���ƣ�send()
		 * �������ͣ�int�� ���񼶱�,String�� ������Ϣ
		 * ����ֵ����
		 * ���ã�send()�������ڴ����������󲢷���
		*/
		taskrequest=new TaskRequest();
		taskrequest.setRequest(taskLevel, msg);
		TaskRequest(taskrequest);
		
	}
	
	public abstract float getTaskLevel();
	
	public abstract void setTaskLevel(int level);
	
	public abstract void doTask();
	

}
