package com.yyy.huojia.exchange;

public class ExchangeList {

    /**
     * iRecNo : 55
     * iOutBscDataStockMRecNo : 59
     * iInBscDataStockMRecNo : 131
     * sBillNo : MD1912-0001
     * dDate : 2019-12-14T00:00:00
     * sOutStockName : 原材料仓库
     * sInStockName : 不良品仓
     * fQty : 8935.0
     * fTotal : 35588.11
     * sUserName : 管理员
     */

    private int iRecNo;
    private int iOutBscDataStockMRecNo;
    private int iInBscDataStockMRecNo;
    private String sBillNo;
    private String dDate;
    private String sOutStockName;
    private String sInStockName;
    private double fQty;
    private double fTotal;
    private String sUserName;

    public int getIRecNo() {
        return iRecNo;
    }

    public void setIRecNo(int iRecNo) {
        this.iRecNo = iRecNo;
    }

    public int getIOutBscDataStockMRecNo() {
        return iOutBscDataStockMRecNo;
    }

    public void setIOutBscDataStockMRecNo(int iOutBscDataStockMRecNo) {
        this.iOutBscDataStockMRecNo = iOutBscDataStockMRecNo;
    }

    public int getIInBscDataStockMRecNo() {
        return iInBscDataStockMRecNo;
    }

    public void setIInBscDataStockMRecNo(int iInBscDataStockMRecNo) {
        this.iInBscDataStockMRecNo = iInBscDataStockMRecNo;
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

    public String getSOutStockName() {
        return sOutStockName;
    }

    public void setSOutStockName(String sOutStockName) {
        this.sOutStockName = sOutStockName;
    }

    public String getSInStockName() {
        return sInStockName;
    }

    public void setSInStockName(String sInStockName) {
        this.sInStockName = sInStockName;
    }

    public double getFQty() {
        return fQty;
    }

    public void setFQty(double fQty) {
        this.fQty = fQty;
    }

    public double getFTotal() {
        return fTotal;
    }

    public void setFTotal(double fTotal) {
        this.fTotal = fTotal;
    }

    public String getSUserName() {
        return sUserName;
    }

    public void setSUserName(String sUserName) {
        this.sUserName = sUserName;
    }
}
