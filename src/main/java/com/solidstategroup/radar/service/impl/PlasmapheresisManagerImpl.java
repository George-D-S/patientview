package com.solidstategroup.radar.service.impl;

import com.solidstategroup.radar.dao.PlasmapheresisDao;
import com.solidstategroup.radar.model.Plasmapheresis;
import com.solidstategroup.radar.model.PlasmapheresisExchangeUnit;
import com.solidstategroup.radar.model.exception.InvalidModelException;
import com.solidstategroup.radar.service.PlasmapheresisManager;
import com.solidstategroup.radar.service.TreatmentManager;
import com.solidstategroup.radar.util.RadarUtility;

import java.util.ArrayList;
import java.util.List;


public class PlasmapheresisManagerImpl implements PlasmapheresisManager {

    private PlasmapheresisDao plasmapheresisDao;

    public void savePlasmapheresis(Plasmapheresis plasmapheresis) throws InvalidModelException {
        // validation
        List<String> errors = new ArrayList<String>();
        List<Plasmapheresis> plasmapheresisList = plasmapheresisDao.
                getPlasmapheresisByRadarNumber(plasmapheresis.getRadarNumber());

        //  must have finish date before you can start it again
        for (Plasmapheresis existingPlasmapheresis : plasmapheresisList) {
            if (existingPlasmapheresis.getId().equals(plasmapheresis.getId())) {
                continue;
            }
            if (existingPlasmapheresis.getEndDate() == null) {
                errors.add(TreatmentManager.PREVIOUS_TREATMENT_NOT_CLOSED_ERROR);
                break;
            }
        }

        // dates must not overlap
        for (Plasmapheresis existingPlasmapheresis : plasmapheresisList) {
            if (existingPlasmapheresis.getId().equals(plasmapheresis.getId())) {
                continue;
            }
            if (RadarUtility.isEventsOverlapping(existingPlasmapheresis.getStartDate(),
                    existingPlasmapheresis.getEndDate(), plasmapheresis.getStartDate(),
                    plasmapheresis.getEndDate())) {
                errors.add(TreatmentManager.OVERLAPPING_ERROR);
                break;
            }

        }

        if (!errors.isEmpty()) {
            InvalidModelException exception = new InvalidModelException("plasmapheresis model is not valid");
            exception.setErrors(errors);
            throw exception;
        }
        plasmapheresisDao.savePlasmapheresis(plasmapheresis);
    }

    public void deletePlasmaPheresis(Plasmapheresis plasmapheresis) {
        plasmapheresisDao.deletePlasmaPheresis(plasmapheresis);
    }

    public Plasmapheresis getPlasmapheresis(long id) {
        return plasmapheresisDao.getPlasmapheresis(id);
    }

    public List<Plasmapheresis> getPlasmapheresisByRadarNumber(long radarNumber) {
        return plasmapheresisDao.getPlasmapheresisByRadarNumber(radarNumber);
    }

    public PlasmapheresisExchangeUnit getPlasmapheresisExchangeUnit(long id) {
        return plasmapheresisDao.getPlasmapheresisExchangeUnit(id);
    }

    public List<PlasmapheresisExchangeUnit> getPlasmapheresisExchangeUnits() {
        return plasmapheresisDao.getPlasmapheresisExchangeUnits();
    }

    public PlasmapheresisDao getPlasmapheresisDao() {
        return plasmapheresisDao;
    }

    public void setPlasmapheresisDao(PlasmapheresisDao plasmapheresisDao) {
        this.plasmapheresisDao = plasmapheresisDao;
    }
}
