package com.yyy.huojia.check.model;

public class BarCode {

    /**
     * sBatchNo : TEST10086
     * fQty : 8000.0
     * sCode : 011250115
     * sName : A3钢卷 1250*1.15
     * sElements : 1250*1.15
     * sUnitName : 公斤
     * sPurOrderNo : 20191025
     * sPurOrderMatMBillNo : PM1910-0002
     */

    private String sBatchNo;
    private double fQty;
    private double fStockQty;
    private double fPcQty;
    private String sCode;
    private String sName;
    private String sElements;
    private String sUnitName;
    private String sPurOrderNo;
    private String sPurOrderMatMBillNo;
    private int iBscDataCustomerRecNo;
    private String sCustShortName;

    public void setfStockQty(double fStockQty) {
        this.fStockQty = fStockQty;
    }

    public void setfPcQty(double fPcQty) {
        this.fPcQty = fPcQty<fStockQty?fPcQty:fStockQty;
    }

    public int getfStockQty() {
        return new Double(fStockQty).intValue();
    }

    public int getfPcQty() {
        return new Double(fPcQty).intValue();
    }

    public int getiBscDataCustomerRecNo() {
        return iBscDataCustomerRecNo;
    }

    public void setiBscDataCustomerRecNo(int iBscDataCustomerRecNo) {
        this.iBscDataCustomerRecNo = iBscDataCustomerRecNo;
    }

    public String getsCustShortName() {
        return sCustShortName;
    }

    public void setsCustShortName(String sCustShortName) {
        this.sCustShortName = sCustShortName;
    }

    public String getSBatchNo() {
        return sBatchNo;
    }

    public void setSBatchNo(String sBatchNo) {
        this.sBatchNo = sBatchNo;
    }

    public int getFQty() {
        this.fQty = fPcQty - fStockQty;
        return (new Double(fQty)).intValue();
    }

    public void setFQty(double fQty) {
        this.fQty = fQty;
    }

    public String getSCode() {
        return sCode;
    }

    public void setSCode(String sCode) {
        this.sCode = sCode;
    }

    public String getSName() {
        return sName;
    }

    public void setSName(String sName) {
        this.sName = sName;
    }

    public String getSElements() {
        return sElements;
    }

    public void setSElements(String sElements) {
        this.sElements = sElements;
    }

    public String getSUnitName() {
        return sUnitName;
    }

    public void setSUnitName(String sUnitName) {
        this.sUnitName = sUnitName;
    }

    public String getSPurOrderNo() {
        return sPurOrderNo;
    }

    public void setSPurOrderNo(String sPurOrderNo) {
        this.sPurOrderNo = sPurOrderNo;
    }

    public String getSPurOrderMatMBillNo() {
        return sPurOrderMatMBillNo;
    }

    public void setSPurOrderMatMBillNo(String sPurOrderMatMBillNo) {
        this.sPurOrderMatMBillNo = sPurOrderMatMBillNo;
    }
}
