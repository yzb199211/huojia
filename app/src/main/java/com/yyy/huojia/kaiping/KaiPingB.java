package com.yyy.huojia.kaiping;

import java.util.Objects;

public class KaiPingB {

    /**
     * iFlag : 1
     * sBatchNo : 076544
     * sName : A3钢带 90*1.0
     * sElements : 90*1.0
     * sUnitName : 公斤
     * sProTaskOrderMBillNo : ETO20061601
     * iBscDataMatRecNo : 57
     * fLength : 0
     */

    private int iFlag;
    private String sBatchNo;
    private String sName;
    private String sElements;
    private String sUnitName;
    private String sProTaskOrderMBillNo;
    private int iBscDataMatRecNo;
    private int fLength;

    public KaiPingB() {
    }

    public KaiPingB(String sBatchNo) {
        this.sBatchNo = sBatchNo;
    }

    public int getIFlag() {
        return iFlag;
    }

    public void setIFlag(int iFlag) {
        this.iFlag = iFlag;
    }

    public String getSBatchNo() {
        return sBatchNo;
    }

    public void setSBatchNo(String sBatchNo) {
        this.sBatchNo = sBatchNo;
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

    public String getSProTaskOrderMBillNo() {
        return sProTaskOrderMBillNo;
    }

    public void setSProTaskOrderMBillNo(String sProTaskOrderMBillNo) {
        this.sProTaskOrderMBillNo = sProTaskOrderMBillNo;
    }

    public int getIBscDataMatRecNo() {
        return iBscDataMatRecNo;
    }

    public void setIBscDataMatRecNo(int iBscDataMatRecNo) {
        this.iBscDataMatRecNo = iBscDataMatRecNo;
    }

    public int getFLength() {
        return fLength;
    }

    public void setFLength(int fLength) {
        this.fLength = fLength;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KaiPingB kaiPingB = (KaiPingB) o;
        return sBatchNo.equals(kaiPingB.sBatchNo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sBatchNo);
    }
}
