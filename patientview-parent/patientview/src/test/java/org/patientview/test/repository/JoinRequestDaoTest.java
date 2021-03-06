/*
 * PatientView
 *
 * Copyright (c) Worth Solutions Limited 2004-2013
 *
 * This file is part of PatientView.
 *
 * PatientView is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 * PatientView is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with PatientView in a file
 * titled COPYING. If not, see <http://www.gnu.org/licenses/>.
 *
 * @package PatientView
 * @link http://www.patientview.org
 * @author PatientView <info@patientview.org>
 * @copyright Copyright (c) 2004-2013, Worth Solutions Limited
 * @license http://www.gnu.org/licenses/gpl-3.0.html The GNU General Public License V3.0
 */

package org.patientview.test.repository;

import org.patientview.patientview.model.JoinRequest;
import org.patientview.repository.JoinRequestDao;
import org.junit.Test;

import javax.inject.Inject;

import java.util.Date;

import static org.junit.Assert.assertTrue;

public class JoinRequestDaoTest extends BaseDaoTest {

    @Inject
    JoinRequestDao joinRequestDao;

    @Test
    public void testAddGetJoinRequest() throws Exception {
        JoinRequest joinRequest = getTestObject();

        /**
         * add
         */
        joinRequestDao.save(joinRequest);
        assertTrue("Can't save joinRequest", joinRequest.getId() > 0);
    }

    private JoinRequest getTestObject() throws Exception {
        JoinRequest joinRequest = new JoinRequest();

        joinRequest.setFirstName("Jack");
        joinRequest.setLastName("London");
        joinRequest.setDateOfBirth(new Date());
        joinRequest.setEmail("jack@london.com");
        joinRequest.setNhsNo("9434765919");
        joinRequest.setUnitcode("SNC01");

        return joinRequest;
    }

}
