package com.solidstategroup.radar.service;

import com.solidstategroup.radar.model.Transplant;

import java.util.List;

public interface TransplantManager {

    void saveTransplant(Transplant transplant);

    void deleteTransplant(Transplant transplant);

    Transplant getTransplant(long id);

    List<Transplant> getTransplantsByRadarNumber(long radarNumber);

    List<Transplant.Modality> getTransplantModalitites();

    Transplant.Modality getTransplantModality(long id);

    void saveRejectData(Transplant.RejectData rejectData);

    void deleteRejectData(Transplant.RejectData rejectData);

    List<Transplant.RejectData> getRejectDataByTransplantNumber(Long transplantNumber);

    Transplant.RejectData getRejectData(Long id);

}
