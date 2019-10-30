package com.yyy.huojia.output.model;

public class BarCode {

    /**
     * iSerial : 1
     * sBatchNo : H3B19A015711
     * fQty : 9040.0
     * sCode : 011250140
     * sName : A3钢卷 1250*1.4
     * sElements : 1250*1.40
     * sUnitName : 公斤
     * fPrice : 4.0
     * fTotal : 36160.0
     * sProTaskOrderMBillNo : PTO19102401
     * sStockShortName : 山东日照钢铁股份有限公司
     */

    private int iSerial;
    private String sBatchNo;
    private double fQty;
    private String sCode;
    private String sName;
    private String sElements;
    private String sUnitName;
    private double fPrice;
    private double fTotal;
    private String sProTaskOrderMBillNo;
    private String sStockShortName;
    private int iBscDataMatRecNo;

    public int getiBscDataMatRecNo() {
        return iBscDataMatRecNo;
    }

    public int getISerial() {
        return iSerial;
    }

    public void setISerial(int iSerial) {
        this.iSerial = iSerial;
    }

    public String getSBatchNo() {
        return sBatchNo;
    }

    public void setSBatchNo(String sBatchNo) {
        this.sBatchNo = sBatchNo;
    }

    public int getFQty() {
        return new Double(fQty).intValue();
    }

    public void setFQty(int fQty) {
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

    public double getFPrice() {
        return fPrice;
    }

    public void setFPrice(double fPrice) {
        this.fPrice = fPrice;
    }

    public int getFTotal() {
        return new Double(fTotal).intValue();
    }

    public void setFTotal(double fTotal) {
        this.fTotal = fTotal;
    }

    public String getSProTaskOrderMBillNo() {
        return sProTaskOrderMBillNo;
    }

    public void setSProTaskOrderMBillNo(String sProTaskOrderMBillNo) {
        this.sProTaskOrderMBillNo = sProTaskOrderMBillNo;
    }

    public String getSStockShortName() {
        return sStockShortName;
    }

    public void setSStockShortName(String sStockShortName) {
        this.sStockShortName = sStockShortName;
    }
}
