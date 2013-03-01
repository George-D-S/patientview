package com.worthsoln.test.repository.ibd;

import com.worthsoln.ibd.model.enums.colitis.NumberOfStoolsNighttime;
import com.worthsoln.ibd.model.symptoms.ColitisSymptoms;
import com.worthsoln.ibd.model.enums.Feeling;
import com.worthsoln.ibd.model.enums.colitis.NumberOfStoolsDaytime;
import com.worthsoln.ibd.model.enums.colitis.PresentBlood;
import com.worthsoln.ibd.model.enums.colitis.ToiletTiming;
import com.worthsoln.repository.ibd.ColitisSymptomsDao;
import com.worthsoln.test.repository.BaseDaoTest;
import org.junit.Test;

import javax.inject.Inject;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ColitisDaoTest extends BaseDaoTest {

    @Inject
    private ColitisSymptomsDao colitisSymptomsDao;

    @Test
    public void testAddGetColitis() throws Exception {

        ColitisSymptoms colitisSymptoms = getTestObject();

        colitisSymptomsDao.save(colitisSymptoms);

        assertTrue("Invalid id for new ColitisSymptoms", colitisSymptoms.getId() > 0);

        ColitisSymptoms checkColitisSymptoms = colitisSymptomsDao.get(colitisSymptoms.getId());

        assertNotNull(checkColitisSymptoms);
        assertEquals("NHS no not persisted", checkColitisSymptoms.getNhsno(), colitisSymptoms.getNhsno());
        assertEquals("ColitisSymptoms date not persisted", checkColitisSymptoms.getSymptomDate(), colitisSymptoms.getSymptomDate());
        assertEquals("ColitisSymptoms stools day not persisted", checkColitisSymptoms.getNumberOfStoolsDaytime(), colitisSymptoms.getNumberOfStoolsDaytime());
        assertEquals("ColitisSymptoms stools night not persisted", checkColitisSymptoms.getNumberOfStoolsNighttime(), colitisSymptoms.getNumberOfStoolsNighttime());
        assertEquals("ColitisSymptoms toilet timing not persisted", checkColitisSymptoms.getToiletTiming(), colitisSymptoms.getToiletTiming());
        assertEquals("ColitisSymptoms present blood not persisted", checkColitisSymptoms.getPresentBlood(), colitisSymptoms.getPresentBlood());
        assertEquals("ColitisSymptoms feeling not persisted", checkColitisSymptoms.getFeeling(), colitisSymptoms.getFeeling());

    }

    private ColitisSymptoms getTestObject() throws Exception {
        ColitisSymptoms colitisSymptoms = new ColitisSymptoms();

        colitisSymptoms.setNhsno("1234567890");
        colitisSymptoms.setSymptomDate(new Date());
        colitisSymptoms.setNumberOfStoolsDaytime(NumberOfStoolsDaytime.SEVEN_TO_NINE);
        colitisSymptoms.setNumberOfStoolsNighttime(NumberOfStoolsNighttime.FOUR_TO_SIX);
        colitisSymptoms.setToiletTiming(ToiletTiming.HAVING_ACCIDENTS);
        colitisSymptoms.setPresentBlood(PresentBlood.A_TRACE);
        colitisSymptoms.setFeeling(Feeling.BELOW_PAR);

        return colitisSymptoms;
    }

}
