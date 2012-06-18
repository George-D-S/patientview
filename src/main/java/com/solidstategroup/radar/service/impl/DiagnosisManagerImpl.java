package com.solidstategroup.radar.service.impl;

import com.solidstategroup.radar.dao.DiagnosisDao;
import com.solidstategroup.radar.model.ClinicalPresentation;
import com.solidstategroup.radar.model.Diagnosis;
import com.solidstategroup.radar.model.DiagnosisCode;
import com.solidstategroup.radar.model.Karotype;
import com.solidstategroup.radar.service.DiagnosisManager;

import java.util.List;


public class DiagnosisManagerImpl implements DiagnosisManager {

    private DiagnosisDao diagnosisDao;

    public void saveDiagnosis(Diagnosis diagnosis) {
        diagnosisDao.saveDiagnosis(diagnosis);
    }

    public Diagnosis getDiagnosis(long id) {
        return diagnosisDao.getDiagnosis(id);
    }

    public Diagnosis getDiagnosisByRadarNumber(long radarNumber) {
        return diagnosisDao.getDiagnosisByRadarNumber(radarNumber);
    }

    public DiagnosisCode getDiagnosisCode(long id) {
        return diagnosisDao.getDiagnosisCode(id);
    }

    public List<DiagnosisCode> getDiagnosisCodes() {
        return diagnosisDao.getDiagnosisCodes();
    }

    public ClinicalPresentation getClinicalPresentation(long id) {
        return diagnosisDao.getClinicalPresentation(id);
    }

    public List<ClinicalPresentation> getClinicalPresentations() {
        return diagnosisDao.getClinicalPresentations();
    }

    public Karotype getKarotype(long id) {
        return diagnosisDao.getKarotype(id);
    }

    public List<Karotype> getKarotypes() {
        return diagnosisDao.getKarotypes();
    }

    public DiagnosisDao getDiagnosisDao() {
        return diagnosisDao;
    }

    public void setDiagnosisDao(DiagnosisDao diagnosisDao) {
        this.diagnosisDao = diagnosisDao;
    }
}
