package com.worthsoln.patientview.diagnostics;

import com.worthsoln.ibd.action.BaseAction;
import com.worthsoln.patientview.model.Diagnostic;
import com.worthsoln.patientview.model.enums.DiagnosticType;
import com.worthsoln.utils.LegacySpringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 */
public class DiagnosticsAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        List<Diagnostic> diagnosticsImaging
                = LegacySpringUtils.getDiagnosticManager().getForLoggedInUser(DiagnosticType.IMAGING);

        List<Diagnostic> diagnosticsEndoscopy
                = LegacySpringUtils.getDiagnosticManager().getForLoggedInUser(DiagnosticType.ENDOSCOPY);

        request.setAttribute("diagnosticsImaging", diagnosticsImaging);
        request.setAttribute("diagnosticsEndoscopy", diagnosticsEndoscopy);

        return mapping.findForward(SUCCESS);
    }
}
