package com.solidstategroup.radar.web.panels.generic;


import com.solidstategroup.radar.model.Demographics;
import com.solidstategroup.radar.model.generic.MedicalResult;
import com.solidstategroup.radar.service.generic.MedicalResultManager;
import com.solidstategroup.radar.web.RadarApplication;
import com.solidstategroup.radar.web.components.ComponentHelper;
import com.solidstategroup.radar.web.components.RadarComponentFactory;
import com.solidstategroup.radar.web.components.RadarDateTextField;
import com.solidstategroup.radar.web.components.RadarTextFieldWithValidation;
import com.solidstategroup.radar.web.panels.PatientDetailPanel;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public class MedicalResultsPanel extends Panel {

    public static final String TEST_RESULT_NULL_DATE_MESSAGE = "Test result must have a date";
    public static final String TEST_RESULT_AT_LEAST_ONE = "A test result must be entered";

    @SpringBean
    private MedicalResultManager medicalResultManager;

    public MedicalResultsPanel(String id, MedicalResult medicalResult, Demographics demographics) {
        super(id);

        // components to update on ajax refresh
        final List<Component> componentsToUpdateList = new ArrayList<Component>();
        IModel<MedicalResult> model = new Model<MedicalResult>(medicalResult);

        // create form and components

        Form<MedicalResult> form = new Form<MedicalResult>("form", new CompoundPropertyModel<MedicalResult>(model)) {
            @Override
            protected void onSubmit() {
                MedicalResult medicalResult = getModelObject();
                // test result cannot have a null date
                if (medicalResult.getBloodUrea() != null && medicalResult.getBloodUreaDate() == null) {
                    get("bloodUreaDate").error(TEST_RESULT_NULL_DATE_MESSAGE);
                }
                if (medicalResult.getSerumCreatanine() != null && medicalResult.getCreatanineDate() == null) {
                    get("creatanineDate").error(TEST_RESULT_NULL_DATE_MESSAGE);
                }
                if (medicalResult.getWeight() != null && medicalResult.getWeightDate() == null) {
                    get("weightDate").error(TEST_RESULT_NULL_DATE_MESSAGE);
                }
                if (medicalResult.getHeight() != null && medicalResult.getHeightDate() == null) {
                    get("heightDate").error(TEST_RESULT_NULL_DATE_MESSAGE);
                }
                if (medicalResult.getBpSystolic() != null && medicalResult.getBpSystolicDate() == null) {
                    get("bpSystolicDate").error(TEST_RESULT_NULL_DATE_MESSAGE);
                }
                if (medicalResult.getBpDiastolic() != null && medicalResult.getBpDiastolicDate() == null) {
                    get("bpDiastolicDate").error(TEST_RESULT_NULL_DATE_MESSAGE);
                }
                if (medicalResult.getAntihypertensiveDrugs() != null
                        && medicalResult.getAntihypertensiveDrugsDate() == null) {
                    get("antihypertensiveDrugsDate").error(TEST_RESULT_NULL_DATE_MESSAGE);
                }
                if (medicalResult.getBpSystolic() == null && medicalResult.getBpDiastolic() == null) {
                    System.out.println("error");
                    error(TEST_RESULT_AT_LEAST_ONE);
                }

                if (!hasError()) {
                    medicalResultManager.save(medicalResult);
                }
            }
        };
        add(form);

        PatientDetailPanel patientDetail = new PatientDetailPanel("patientDetail", demographics, "Medical Results");
        patientDetail.setOutputMarkupId(true);
        form.add(patientDetail);
        componentsToUpdateList.add(patientDetail);

        form.add(new RadarTextFieldWithValidation<Double>("bloodUrea", null, form, componentsToUpdateList));
        form.add(new RadarDateTextField("bloodUreaDate", form, componentsToUpdateList));

        form.add(new RadarTextFieldWithValidation<Double>("serumCreatanine", null, form, componentsToUpdateList));
        form.add(new RadarDateTextField("creatanineDate", form, componentsToUpdateList));

        form.add(new RadarTextFieldWithValidation<Double>("weight", null, form, componentsToUpdateList));
        form.add(new RadarDateTextField("weightDate", form, componentsToUpdateList));

        form.add(new RadarTextFieldWithValidation<Double>("height", null, form, componentsToUpdateList));
        form.add(new RadarDateTextField("heightDate", form, componentsToUpdateList));

        form.add(new RadarTextFieldWithValidation<Integer>("bpSystolic", null, form, componentsToUpdateList));
        form.add(new RadarDateTextField("bpSystolicDate", form, componentsToUpdateList));

        form.add(new RadarTextFieldWithValidation<Integer>("bpDiastolic", null, form, componentsToUpdateList));
        form.add(new RadarDateTextField("bpDiastolicDate", form, componentsToUpdateList));

        form.add(new RadarDateTextField("bpDate", form, componentsToUpdateList));

        RadioGroup<MedicalResult.YesNo> antihypertensiveDrugs = new RadioGroup<MedicalResult.YesNo>(
                "antihypertensiveDrugs");
        antihypertensiveDrugs.add(new Radio("yes", new Model(MedicalResult.YesNo.YES)));
        antihypertensiveDrugs.add(new Radio("no", new Model(MedicalResult.YesNo.NO)));
        antihypertensiveDrugs.add(new Radio("unknown", new Model(MedicalResult.YesNo.UNKNOWN)));
        form.add(antihypertensiveDrugs);

        form.add(new RadarDateTextField("antihypertensiveDrugsDate", form, componentsToUpdateList));

        final Label successMessage = RadarComponentFactory.getSuccessMessageLabel("successMessage", form,
                componentsToUpdateList);
        Label errorMessage = RadarComponentFactory.getErrorMessageLabel("errorMessage", form, componentsToUpdateList);

        form.add(new AjaxSubmitLink("save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                ComponentHelper.updateComponentsIfParentIsVisible(target, componentsToUpdateList);
                target.appendJavaScript(RadarApplication.FORM_IS_DIRTY_FALSE_SCRIPT);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                ComponentHelper.updateComponentsIfParentIsVisible(target, componentsToUpdateList);
            }
        });
    }
}
