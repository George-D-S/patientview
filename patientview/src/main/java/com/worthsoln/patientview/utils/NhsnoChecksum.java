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

package com.worthsoln.patientview.utils;

import com.worthsoln.patientview.user.UserUtils;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.Resources;

import javax.servlet.http.HttpServletRequest;

public final class NhsnoChecksum {

    private NhsnoChecksum() {

    }

    public static boolean nhsnoChecksum(
            Object bean,
            ValidatorAction va,
            Field field,
            ActionMessages errors,
            HttpServletRequest request) {

        String value = ValidatorUtils.getValueAsString(
                bean,
                field.getProperty());

        boolean checksumValid = UserUtils.isNhsNumberValid(value);

        if (!checksumValid) {
            errors.add(field.getKey(), Resources.getActionMessage(request, va, field));
        }

        return checksumValid;
    }
}
