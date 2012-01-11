package com.solidstategroup.radar.test.dao;

import com.solidstategroup.radar.dao.TreatmentDao;
import com.solidstategroup.radar.model.Treatment;
import com.solidstategroup.radar.model.TreatmentModality;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class TreatmentDaoTest extends BaseDaoTest {

    @Autowired
    private TreatmentDao treatmentDao;

    @Test
    public void testGetTreatment() {
        Treatment treatment = treatmentDao.getTreatment(10L);
        assertNotNull("Treatment was null", treatment);
        assertEquals("Wrong ID", new Long(10), treatment.getId());
    }

    @Test
    public void testGetTreatmentUnknown() {
        Treatment treatment = treatmentDao.getTreatment(10232L);
        assertNull("Treatment was not null", treatment);
    }

    @Test
    public void testGetTreatmentsByRadarNumber() {
        List<Treatment> treatments = treatmentDao.getTreatmentsByRadarNumber(237L);
        assertNotNull("Treatments list was null", treatments);
        assertEquals("Wrong size", 3, treatments.size());
    }

    @Test
    public void testGetTreatmentModalities() throws Exception {
        List<TreatmentModality> treatmentModalities = treatmentDao.getTreatmentModalities();
        assertNotNull("List was null", treatmentModalities);
        assertEquals("Wrong size", 5, treatmentModalities.size());
    }
}
