<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="com.worthsoln.ibd.model.symptoms.SymptomsData" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%--
  ~ PatientView
  ~
  ~ Copyright (c) Worth Solutions Limited 2004-2013
  ~
  ~ This file is part of PatientView.
  ~
  ~ PatientView is free software: you can redistribute it and/or modify it under the terms of the
  ~ GNU General Public License as published by the Free Software Foundation, either version 3 of the License,
  ~ or (at your option) any later version.
  ~ PatientView is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
  ~ the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  ~ GNU General Public License for more details.
  ~ You should have received a copy of the GNU General Public License along with PatientView in a file
  ~ titled COPYING. If not, see <http://www.gnu.org/licenses/>.
  ~
  ~ @package PatientView
  ~ @link http://www.patientview.org
  ~ @author PatientView <info@patientview.org>
  ~ @copyright Copyright (c) 2004-2013, Worth Solutions Limited
  ~ @license http://www.gnu.org/licenses/gpl-3.0.html The GNU General Public License V3.0
  --%>

<bean:define id="symptomsGraphData" name="graphData" type="com.worthsoln.ibd.model.symptoms.SymptomsGraphData" />
<%
    String scoresString = "";
    String datesString = "";

    List<Integer> graphScores = new ArrayList<Integer>();
    List<String> graphDates = new ArrayList<String>();

    for (SymptomsData symptomsData : symptomsGraphData.getGraphData()) {
        graphScores.add(symptomsData.getScore());
        graphDates.add(symptomsData.getDate());
    }

    if (!graphScores.isEmpty()) {
        scoresString = StringUtils.join(graphScores.toArray(), ", ");
    }

    if (!graphDates.isEmpty()) {
        datesString = "\"" + StringUtils.join(graphDates.toArray(), "\", \"") + "\"";
    }
%>
{
"error": "<%=symptomsGraphData.getError()%>",
"scores": [<%=scoresString%>],
"dates": [<%=datesString%>]
}
