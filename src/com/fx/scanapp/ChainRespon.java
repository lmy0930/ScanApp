package com.fx.scanapp;

public abstract class ChainRespon {
	/**
	 * author:刘梦雨
	 * createdate:2016-9-18
	 * description:本抽象类是任务链的构建器，其导出类可创建业务逻辑各任务链的构建器实例。
	                              通过该实例可实现任务片的装配和级别设置，实现各个任务模块的自由组合，
	                              降低业务逻辑间的耦合关系。
	 */
	public abstract void build();
	/**
	 * 名称：build()
	 * 参数类型：无
	 * 返回值：无
	 * 作用：build()函数用于任务链的组装和构建。
	*/
	public abstract TaskNode getChain();
	/**
	 * 名称：getChain()
	 * 参数类型：无
	 * 返回值：TaskNode
	 * 作用：获取任务链头节点。
	*/

}
