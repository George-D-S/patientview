<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>

<tiles:insert definition="patient.layout" flush="true">

  <tiles:put name="left_nav" value="/common/left_nav_empty.jsp" />
  <tiles:put name="body" value="/ibd/body/crohns-body.jsp"/>

</tiles:insert>