package com.yyy.huojia.input.model;

public class NotArrived {

    /**
     * iPurOrderMatMRecNo : 88
     * iFinish : 0
     * iBscDataCustomerRecNo : 183
     * iRecNo : 901
     * iBscDataMatRecNo : 248
     * sBatchNo : TEST10086
     * fQty : 8000.0
     * fInQty : 2000.0
     * fNotInQty : 6000.0
     * fPrice : 4.0
     * fTotal : 32000.0
     * fNotInTotal : 24000.0
     * sReMark : null
     * sCode : 011250115
     * sName : A3钢卷 1250*1.15
     * sElements : 1250*1.15
     * sUnitName : 公斤
     * sPurOrderNo : 20191025
     * sBillNo : PM1910-0002
     * dDate : 2019-10-25T00:00:00
     * sCustShortName : 山东日照钢铁股份有限公司
     */

    private int iPurOrderMatMRecNo;
    private int iFinish;
    private int iBscDataCustomerRecNo;
    private int iRecNo;
    private int iBscDataMatRecNo;
    private String sBatchNo;
    private double fQty;
    private double fInQty;
    private double fNotInQty;
    private double fPrice;
    private double fTotal;
    private double fNotInTotal;
    private Object sReMark;
    private String sCode;
    private String sName;
    private String sElements;
    private String sUnitName;
    private String sPurOrderNo;
    private String sBillNo;
    private String dDate;
    private String sCustShortName;

    public int getIPurOrderMatMRecNo() {
        return iPurOrderMatMRecNo;
    }

    public void setIPurOrderMatMRecNo(int iPurOrderMatMRecNo) {
        this.iPurOrderMatMRecNo = iPurOrderMatMRecNo;
    }

    public int getIFinish() {
        return iFinish;
    }

    public void setIFinish(int iFinish) {
        this.iFinish = iFinish;
    }

    public int getIBscDataCustomerRecNo() {
        return iBscDataCustomerRecNo;
    }

    public void setIBscDataCustomerRecNo(int iBscDataCustomerRecNo) {
        this.iBscDataCustomerRecNo = iBscDataCustomerRecNo;
    }

    public int getIRecNo() {
        return iRecNo;
    }

    public void setIRecNo(int iRecNo) {
        this.iRecNo = iRecNo;
    }

    public int getIBscDataMatRecNo() {
        return iBscDataMatRecNo;
    }

    public void setIBscDataMatRecNo(int iBscDataMatRecNo) {
        this.iBscDataMatRecNo = iBscDataMatRecNo;
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

    public void setFQty(double fQty) {
        this.fQty = fQty;
    }

    public int getFInQty() {
        return new Double(fInQty).intValue();
    }

    public void setFInQty(double fInQty) {
        this.fInQty = fInQty;
    }

    public int getFNotInQty() {
        return new Double(fNotInQty).intValue();
    }

    public void setFNotInQty(double fNotInQty) {
        this.fNotInQty = fNotInQty;
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

    public int getFNotInTotal() {
        return new Double(fNotInTotal).intValue();
    }

    public void setFNotInTotal(double fNotInTotal) {
        this.fNotInTotal = fNotInTotal;
    }

    public Object getSReMark() {
        return sReMark;
    }

    public void setSReMark(Object sReMark) {
        this.sReMark = sReMark;
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

    public String getSCustShortName() {
        return sCustShortName;
    }

    public void setSCustShortName(String sCustShortName) {
        this.sCustShortName = sCustShortName;
    }
}
