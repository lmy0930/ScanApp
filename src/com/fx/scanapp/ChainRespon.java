package com.fx.scanapp;

public abstract class ChainRespon {
	/**
	 * author:������
	 * createdate:2016-9-18
	 * description:�����������������Ĺ��������䵼����ɴ���ҵ���߼����������Ĺ�����ʵ����
	                              ͨ����ʵ����ʵ������Ƭ��װ��ͼ������ã�ʵ�ָ�������ģ���������ϣ�
	                              ����ҵ���߼������Ϲ�ϵ��
	 */
	public abstract void build();
	/**
	 * ���ƣ�build()
	 * �������ͣ���
	 * ����ֵ����
	 * ���ã�build()������������������װ�͹�����
	*/
	public abstract TaskNode getChain();
	/**
	 * ���ƣ�getChain()
	 * �������ͣ���
	 * ����ֵ��TaskNode
	 * ���ã���ȡ������ͷ�ڵ㡣
	*/

}
