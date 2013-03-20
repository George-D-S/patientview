<%@ page import="com.worthsoln.utils.LegacySpringUtils" %>
<%@ page import="com.worthsoln.patientview.model.Specialty" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<html:xhtml/>

<logic:notPresent name="conversation">
    <div class="alert alert-error">
        <strong>Conversation not found.</strong>
    </div>
</logic:notPresent>
<logic:present name="conversation">
    <div class="page-header">
        <div>
            <html:link action="/patient/conversations" styleClass="btn">< back to Messages</html:link>
        </div>

        <h1>
            <br />
            <bean:write name="conversation" property="otherUser.name" />
        </h1>
    </div>

    <section class="js-messages">
        <logic:present name="messages">
            <logic:notEmpty name="messages">
                <logic:iterate name="messages" id="message" indexId="index">
                    <article class="message" id="message-<bean:write name="message" property="id" />">
                        <h4 class="author"><bean:write name="message" property="sender.name" /> <span class="label label-inverse pull-right date"><bean:write name="message" property="friendlyDate" /></span></h4>

                        <div class="content dull">
                            <bean:write name="message" property="formattedContent" filter="false"/>
                        </div>
                    </article>
                </logic:iterate>
            </logic:notEmpty>
            <logic:empty name="messages">
                <div class="alert">
                    <strong>You do not have any messages.</strong>
                </div>
            </logic:empty>
        </logic:present>
    </section>

    <%
        Specialty specialty = LegacySpringUtils.getSecurityUserManager().getLoggedInSpecialty();
        String context = specialty != null ? "/" + specialty.getContext() : "";
    %>
    <section class="new-message-container" id="response">
        <form action="<%=context%>/patient/send-message.do" class="js-message-form">
            <input type="hidden" name="js-message-redirect" value="<%=context%>/patient/conversation.do" />
            <input type="hidden" class="js-message-recipient-id" name="recipientId" value="<bean:write name="recipientId" />" />
            <textarea rows="6" cols="3" name="content" class="span12 new-message js-message-content"></textarea>
            <div class="alert alert-error js-message-errors" style="display: none">
                <strong>You do not have any messages.</strong>
            </div>
            <input type="submit" value="Reply" class="pull-right btn btn-primary js-message-submit-btn" />
        </form>
    </section>
</logic:present>

<script src="/js/messages.js" type="text/javascript"></script>

