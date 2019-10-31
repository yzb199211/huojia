package com.yyy.huojia.check.model;

public class CheckList {

    /**
     * iRecNo : 312
     * sBillNo : MI1910-0002
     * dDate : 2019-10-25T00:00:00
     * sStockName : 原材料仓库
     * sCustShortName : 山东日照钢铁股份有限公司
     * fQty : 8000
     * sUserName : 管理员
     * dInputDate : 2019-10-25T11:16:03
     * sRed : 否
     * iBscDataCustomerRecNo:183,
     * iBscDataStockMRecNo:59
     */

    private int iRecNo;
    private String sBillNo;
    private String dDate;
    private String sStockName;
    private int iBscDataStockMRecNo;
    private String sCustShortName;
    private int iBscDataCustomerRecNo;
    private int fQty;
    private String sUserName;
    private String dInputDate;
    private String sRed;

    public int getiBscDataStockMRecNo() {
        return iBscDataStockMRecNo;
    }

    public void setiBscDataStockMRecNo(int iBscDataStockMRecNo) {
        this.iBscDataStockMRecNo = iBscDataStockMRecNo;
    }

    public int getiBscDataCustomerRecNo() {
        return iBscDataCustomerRecNo;
    }

    public void setiBscDataCustomerRecNo(int iBscDataCustomerRecNo) {
        this.iBscDataCustomerRecNo = iBscDataCustomerRecNo;
    }

    public int getIRecNo() {
        return iRecNo;
    }

    public void setIRecNo(int iRecNo) {
        this.iRecNo = iRecNo;
    }

    public String getSBillNo() {
        return sBillNo;
    }

    public void setSBillNo(String sBillNo) {
        this.sBillNo = sBillNo;
    }

    public String getDDate() {
        return dDate;
    }

    public void setDDate(String dDate) {
        this.dDate = dDate;
    }

    public String getSStockName() {
        return sStockName;
    }

    public void setSStockName(String sStockName) {
        this.sStockName = sStockName;
    }

    public String getSCustShortName() {
        return sCustShortName;
    }

    public void setSCustShortName(String sCustShortName) {
        this.sCustShortName = sCustShortName;
    }

    public int getFQty() {
        return fQty;
    }

    public void setFQty(int fQty) {
        this.fQty = fQty;
    }

    public String getSUserName() {
        return sUserName;
    }

    public void setSUserName(String sUserName) {
        this.sUserName = sUserName;
    }

    public String getDInputDate() {
        return dInputDate;
    }

    public void setDInputDate(String dInputDate) {
        this.dInputDate = dInputDate;
    }

    public String getSRed() {
        return sRed;
    }

    public void setSRed(String sRed) {
        this.sRed = sRed;
    }
}
