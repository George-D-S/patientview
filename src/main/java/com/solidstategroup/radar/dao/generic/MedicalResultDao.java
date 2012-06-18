package com.solidstategroup.radar.dao.generic;

import com.solidstategroup.radar.model.generic.MedicalResult;

public interface MedicalResultDao {

    void save(MedicalResult medicalResult);

    MedicalResult getById(String id);

}
