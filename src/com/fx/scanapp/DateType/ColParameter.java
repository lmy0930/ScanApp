package com.fx.scanapp.DateType;

public interface ColParameter {
    // STATUS
    
        int 正在备料=1;
        int 备料完成=2;
        int 正在换线=3;
        int 已换线=4;
        int 下线=5;

    //CDATA
        int 缺料=0;
        int 已经补料=1;
        int 刷完换料=2;
        int 首盘备料=3;
        int 删除=4;

    //NDATA
        String changeline="changeline";
        String scarcity="scarcity";
        String changekp="changekp";
        String changeclass="changeclass";

}
