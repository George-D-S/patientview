package com.solidstategroup.radar.model.alport;

import com.solidstategroup.radar.model.BaseModel;
import com.solidstategroup.radar.model.generic.DiseaseGroup;

import java.util.Date;

public class Medicine extends BaseModel {

    private String nhsNo;
    private String chiNo;
    private DiseaseGroup diseaseGroup;
    private String name;
    private String dose;
    private Date startDate;
    private Date endDate;

    public String getNhsNo() {
        return nhsNo;
    }

    public void setNhsNo(String nhsNo) {
        this.nhsNo = nhsNo;
    }

    public String getChiNo() {
        return chiNo;
    }

    public void setChiNo(String chiNo) {
        this.chiNo = chiNo;
    }

    public DiseaseGroup getDiseaseGroup() {
        return diseaseGroup;
    }

    public void setDiseaseGroup(DiseaseGroup diseaseGroup) {
        this.diseaseGroup = diseaseGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
