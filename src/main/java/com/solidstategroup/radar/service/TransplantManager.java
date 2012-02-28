package com.solidstategroup.radar.service;

import com.solidstategroup.radar.model.Transplant;
import com.solidstategroup.radar.model.exception.InvalidModelException;

import java.util.List;

public interface TransplantManager {

    public static final String BEFORE_PREVIOUS_FAILURE_DATE = "Cannot add transplant before a previous transplant "
            + "failure date";
    public static final String TRANSPLANTS_INTERVAL_ERROR = "Transplants must be greater than 14 days apart";
    public static final String RECURRANCE_DATE_ERROR = "Recurrance date must be greate than transplant date";

    void saveTransplant(Transplant transplant) throws InvalidModelException;

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
