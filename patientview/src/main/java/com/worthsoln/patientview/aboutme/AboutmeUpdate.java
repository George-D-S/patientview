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

package com.worthsoln.patientview.aboutme;

import com.worthsoln.patientview.model.Aboutme;
import com.worthsoln.patientview.model.User;
import com.worthsoln.patientview.logon.LogonUtils;
import com.worthsoln.patientview.model.UserMapping;
import com.worthsoln.patientview.user.UserUtils;
import com.worthsoln.utils.LegacySpringUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class AboutmeUpdate extends Action {

    public ActionForward execute(
            ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        User user = UserUtils.retrieveUser(request);

        String id = BeanUtils.getProperty(form, "id");
        String aboutme = BeanUtils.getProperty(form, "aboutme");
        String talkabout = BeanUtils.getProperty(form, "talkabout");
        String nhsno = BeanUtils.getProperty(form, "nhsno");

        Aboutme aboutMe = null;

        if (id == null || "".equals(id)) {
            UserMapping userMapping = UserUtils.retrieveUserMappingsPatientEntered(user);
            nhsno = userMapping.getNhsno();

            aboutMe = new Aboutme(nhsno, aboutme, talkabout);
        } else {
            Long idLong = Long.decode(id);

            aboutMe = new Aboutme(idLong, nhsno, aboutme, talkabout);
        }

        LegacySpringUtils.getAboutmeManager().save(aboutMe);

        request.setAttribute("user", user);
        request.setAttribute("aboutme", aboutMe);

        return LogonUtils.logonChecks(mapping, request, "success");
    }

}
