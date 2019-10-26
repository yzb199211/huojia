package com.yyy.huojia.model;

import com.yyy.yyylibrary.wheel.interfaces.IPickerViewData;

public class Stock implements IPickerViewData {

    /**
     * sStockName : 原材料仓库
     * iRecNo : 59
     * iBerCh : 0
     * iPurOrder : 1
     * sMatClass : 01
     */

    private String sStockName;
    private int iRecNo;
    private int iBerCh;
    private int iPurOrder;
    private String sMatClass;

    public String getSStockName() {
        return sStockName;
    }

    public void setSStockName(String sStockName) {
        this.sStockName = sStockName;
    }

    public int getIRecNo() {
        return iRecNo;
    }

    public void setIRecNo(int iRecNo) {
        this.iRecNo = iRecNo;
    }

    public int getIBerCh() {
        return iBerCh;
    }

    public void setIBerCh(int iBerCh) {
        this.iBerCh = iBerCh;
    }

    public int getIPurOrder() {
        return iPurOrder;
    }

    public void setIPurOrder(int iPurOrder) {
        this.iPurOrder = iPurOrder;
    }

    public String getSMatClass() {
        return sMatClass;
    }

    public void setSMatClass(String sMatClass) {
        this.sMatClass = sMatClass;
    }

    @Override
    public String getPickerViewText() {
        return sStockName;
    }
}
