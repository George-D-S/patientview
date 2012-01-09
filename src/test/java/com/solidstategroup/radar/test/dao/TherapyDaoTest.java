package com.solidstategroup.radar.test.dao;

import com.solidstategroup.radar.dao.TherapyDao;
import com.solidstategroup.radar.model.sequenced.Therapy;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TherapyDaoTest extends BaseDaoTest {

    @Autowired
    private TherapyDao therapyDao;

    @Test
    public void testGetTherapy() {
        Therapy therapy = therapyDao.getTherapy(6L);
        assertNotNull("Therapy object was null", therapy);
    }

    @Test
    public void testGetTherapyUnknownId() {
        Therapy therapy = therapyDao.getTherapy(69787L);
        assertNull("Therapy object was null", therapy);
    }

    @Test
    public void testGetTherapyByRadarNumber() {
        List<Therapy> therapy = therapyDao.getTherapyByRadarNumber(6L);
        assertNotNull("Therapy object was null", therapy);
    }
}
