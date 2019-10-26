package com.yyy.huojia.model;

import com.yyy.yyylibrary.wheel.interfaces.IPickerViewData;

public class Supplier implements IPickerViewData {
    /**
     * sCustID : test001
     * sCustShortName : 测试供应商001
     * sCustName : 测试供应商001
     * iRecNo : 172
     */

    private String sCustID;
    private String sCustShortName;
    private String sCustName;
    private int iRecNo;

    public String getSCustID() {
        return sCustID;
    }

    public void setSCustID(String sCustID) {
        this.sCustID = sCustID;
    }

    public String getSCustShortName() {
        return sCustShortName;
    }

    public void setSCustShortName(String sCustShortName) {
        this.sCustShortName = sCustShortName;
    }

    public String getSCustName() {
        return sCustName;
    }

    public void setSCustName(String sCustName) {
        this.sCustName = sCustName;
    }

    public int getIRecNo() {
        return iRecNo;
    }

    public void setIRecNo(int iRecNo) {
        this.iRecNo = iRecNo;
    }

    @Override
    public String getPickerViewText() {
        return sCustName;
    }
}
