package com.solidstategroup.radar.service.impl;

import com.solidstategroup.radar.dao.ImmunosuppressionDao;
import com.solidstategroup.radar.model.Demographics;
import com.solidstategroup.radar.model.Immunosuppression;
import com.solidstategroup.radar.model.ImmunosuppressionTreatment;
import com.solidstategroup.radar.model.exception.InvalidModelException;
import com.solidstategroup.radar.service.DemographicsManager;
import com.solidstategroup.radar.service.ImmunosuppressionManager;
import com.solidstategroup.radar.service.TreatmentManager;
import com.solidstategroup.radar.util.RadarUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class ImmunosuppressionManagerImpl implements ImmunosuppressionManager {

    ImmunosuppressionDao immunosuppressionDao;
    DemographicsManager demographicsManager;

    public void saveImmunosuppressionTreatment(ImmunosuppressionTreatment immunosuppression) throws
            InvalidModelException {


        // validation
        List<String> errors = new ArrayList<String>();
        List<ImmunosuppressionTreatment> ummunosupressionList = immunosuppressionDao.
                getImmunosuppressionTreatmentByRadarNumber(immunosuppression.getRadarNumber());

        //  must have finish date before you can start it again
        for (ImmunosuppressionTreatment existingImmunosuppression : ummunosupressionList) {
            if (existingImmunosuppression.getId().equals(immunosuppression.getId())) {
                continue;
            }
            if (existingImmunosuppression.getEndDate() == null) {
                errors.add(TreatmentManager.PREVIOUS_TREATMENT_NOT_CLOSED_ERROR);
                break;
            }
        }

        // dates must not overlap
        for (ImmunosuppressionTreatment existingImmunosuppression : ummunosupressionList) {
            if (existingImmunosuppression.getId().equals(immunosuppression.getId())) {
                continue;
            }
            if (existingImmunosuppression.getEndDate() != null) {
                if (RadarUtility.isEventsOverlapping(existingImmunosuppression.getStartDate(),
                        existingImmunosuppression.getEndDate(), immunosuppression.getStartDate(),
                        immunosuppression.getEndDate())) {
                    errors.add(TreatmentManager.OVERLAPPING_ERROR);
                    break;
                }
            }
        }

        List<Date> datesToCheck = Arrays.asList(immunosuppression.getStartDate(), immunosuppression.getEndDate());

        // cannot be before date of birth
        Demographics demographics = demographicsManager.getDemographicsByRadarNumber(immunosuppression.getRadarNumber());
        if (demographics != null) {
            Date dob = demographics.getDateOfBirth();
            if (dob != null) {
                for (Date date : datesToCheck) {
                    if (date != null) {
                        if (dob.compareTo(date) > 0) {
                            errors.add(TreatmentManager.BEFORE_DOB_ERROR);
                            break;
                        }
                    }
                }

            }
        }

        // cannot be after today
        Date today = new Date();
        for (Date date : datesToCheck) {
            if (date != null) {
                if (today.compareTo(date) < 0) {
                    errors.add(TreatmentManager.AFTER_TODAY_ERROR);
                    break;
                }
            }
        }

        if (!errors.isEmpty()) {
            InvalidModelException exception = new InvalidModelException("immunosupression model is not valid");
            exception.setErrors(errors);
            throw exception;
        }
        immunosuppressionDao.saveImmunosuppressionTreatment(immunosuppression);
    }

    public void deleteImmunosuppressionTreatment(ImmunosuppressionTreatment immunosuppressionTreatment) {
        immunosuppressionDao.deleteImmunosuppressionTreatment(immunosuppressionTreatment);
    }

    public ImmunosuppressionTreatment getImmunosuppressionTreatment(long id) {
        return immunosuppressionDao.getImmunosuppressionTreatment(id);
    }

    public List<ImmunosuppressionTreatment> getImmunosuppressionTreatmentByRadarNumber(long radarNumber) {
        return immunosuppressionDao.getImmunosuppressionTreatmentByRadarNumber(radarNumber);
    }

    public List<Immunosuppression> getImmunosuppressions() {
        return immunosuppressionDao.getImmunosuppressions();
    }

    public Immunosuppression getImmunosuppression(long id) {
        return immunosuppressionDao.getImmunosuppression(id);
    }

    public ImmunosuppressionDao getImmunosuppressionDao() {
        return immunosuppressionDao;
    }

    public void setImmunosuppressionDao(ImmunosuppressionDao immunosuppressionDao) {
        this.immunosuppressionDao = immunosuppressionDao;
    }

    public void setDemographicsManager(DemographicsManager demographicsManager) {
        this.demographicsManager = demographicsManager;
    }
}
