package org.patientview.radar.dao;

import org.patientview.radar.model.Phenotype;
import org.patientview.radar.model.sequenced.ClinicalData;

import java.util.List;

public interface ClinicalDataDao {

    void saveClinicalDate(ClinicalData clinicalData);

    ClinicalData getClinicalData(long id);

    List<ClinicalData> getClinicalDataByRadarNumber(long radarNumber);

    ClinicalData getFirstClinicalDataByRadarNumber(long radarNumber);

    Phenotype getPhenotype(long id);
    
    List<Phenotype> getPhenotypes();

}
