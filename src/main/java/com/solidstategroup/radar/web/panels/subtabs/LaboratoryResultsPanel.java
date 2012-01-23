package com.solidstategroup.radar.web.panels.subtabs;

import com.solidstategroup.radar.dao.ClinicalDataDao;
import com.solidstategroup.radar.dao.DiagnosisDao;
import com.solidstategroup.radar.dao.LabDataDao;
import com.solidstategroup.radar.model.sequenced.ClinicalData;
import com.solidstategroup.radar.model.sequenced.LabData;
import com.solidstategroup.radar.web.components.RadarComponentFactory;
import com.solidstategroup.radar.web.components.RadarRequiredDateTextField;
import com.solidstategroup.radar.web.components.RadarTextFieldWithValidation;
import com.solidstategroup.radar.web.models.RadarModelFactory;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.RangeValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LaboratoryResultsPanel extends Panel {
    @SpringBean
    private LabDataDao labDataDao;
    @SpringBean
    private DiagnosisDao diagnosisDao;
    @SpringBean
    private ClinicalDataDao clinicalDataDao;

    public LaboratoryResultsPanel(String id, final IModel<Long> radarNumberModel) {
        super(id);

        final List<Component> componentsToUpdate = new ArrayList<Component>();

        final CompoundPropertyModel<LabData> model = new CompoundPropertyModel<LabData>(new LoadableDetachableModel<LabData>() {
            @Override
            public LabData load() {
                LabData labDataModelObject = null;

                if (radarNumberModel.getObject() != null) {
                    Long radarNumber = null;
                    try {
                        radarNumber = radarNumberModel.getObject();
                    } catch (ClassCastException e) {
                        Object obj = radarNumberModel.getObject();
                        radarNumber = Long.parseLong((String) obj);
                    }
                    List<LabData> labDatas = labDataDao.getLabDataByRadarNumber(radarNumber);
                    labDataModelObject = !labDatas.isEmpty() ? labDatas.get(0) : null;
                }

                if (labDataModelObject == null) {
                    labDataModelObject = new LabData();
                    labDataModelObject.setSequenceNumber(1);
                }
                return labDataModelObject;
            }
        });

        final IModel<ClinicalData> clinicalDataModel = RadarModelFactory.getFirstClinicalDataModel(radarNumberModel, clinicalDataDao);

        final Form<LabData> form = new Form<LabData>("form", new CompoundPropertyModel<LabData>(model)) {
            @Override
            protected void onSubmit() {
                LabData labData = getModelObject();
                Long radarNumber;

                if (labData.getRadarNumber() == null) {
                    try {
                        radarNumber = radarNumberModel.getObject();
                    } catch (ClassCastException e) {
                        Object obj = radarNumberModel.getObject();
                        radarNumber = Long.parseLong((String) obj);
                    }

                    labData.setRadarNumber(radarNumber);
                }


                ClinicalData clinicalData = clinicalDataModel.getObject();
                if (clinicalData != null) {
                    Double height = clinicalData.getHeight();
                    Double serumCreatanine = getModelObject().getSerumCreatinine();
                    Double creatnineClearance = null;
                    if (height != null && serumCreatanine != null) {
                        creatnineClearance = height * 40 / serumCreatanine;
                        get("creatanineClearanceInfo").setVisible(false);
                    } else{
                        get("creatanineClearanceInfo").setVisible(true);
                    }
                    labData.setCreatinineClearance(creatnineClearance);
                }
                labDataDao.saveLabDate(labData);
                get("successMessage").setVisible(!hasError());
                get("successMessageDown").setVisible(!hasError());

            }
        };
        add(form);

        Label successLabel = RadarComponentFactory.getSuccessMessageLabel("successMessage", form, componentsToUpdate);
        Label successLabelDown = RadarComponentFactory.getSuccessMessageLabel("successMessageDown", form,
                componentsToUpdate);

        RadarRequiredDateTextField labResultsDate = new RadarRequiredDateTextField("date", form, componentsToUpdate);
        form.add(labResultsDate);

        // Blood fields
        form.add(new RadarTextFieldWithValidation("hb", new RangeValidator<Double>(2.0, 20.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("wbc", new RangeValidator<Double>(0.1, 30.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("neutrophils", new RangeValidator<Double>(0.1, 80.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("platelets", new RangeValidator<Double>(1.0, 800.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("sodium", new RangeValidator<Double>(90.0, 180.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("potassium", new RangeValidator<Double>(1.0, 9.9), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("bun", new RangeValidator<Double>(1.0, 100.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("serumCreatinine", new RangeValidator<Double>(10.0, 2800.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("protein", new RangeValidator<Double>(5.0, 90.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("albumin", new RangeValidator<Double>(5.0, 60.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("crp", new RangeValidator<Double>(0.0, 200.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("totalCholesterol", new RangeValidator<Double>(1.0, 30.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("hdlCholesterol", new RangeValidator<Double>(0.1, 30.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("ldlCholesterol", new RangeValidator<Double>(1.0, 30.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("triglycerides", new RangeValidator<Double>(0.0, 30.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("thyroxine", new RangeValidator<Double>(0.0, 30.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("tsh", new RangeValidator<Double>(0.0, 50.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("phosphate", new RangeValidator<Double>(0.1, 5.6), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("ferritin", new RangeValidator<Double>(1.0, 5000.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("inr", new RangeValidator<Double>(0.5, 5.0), form, componentsToUpdate));

        // Urinalysis - dipstick
        DropDownChoice<LabData.UrineVolumeCondition> urineVolumeCondition =
                new DropDownChoice<LabData.UrineVolumeCondition>("urineVolumeCondition");
        form.add(urineVolumeCondition);
        urineVolumeCondition.setVisible(false); // this is no longer used
        form.add(new DropDownChoice<LabData.Haematuria>("haematuria", Arrays.asList(LabData.Haematuria.NOT_TESTED,
                LabData.Haematuria.MICROSCOPIC, LabData.Haematuria.MACROSCOPIC, LabData.Haematuria.NEGATIVE),
                new ChoiceRenderer<LabData.Haematuria>("label", "id")));
        form.add(new DropDownChoice<LabData.Proteinuria>("proteinuria", Arrays.asList(LabData.Proteinuria.values()),
                new ChoiceRenderer<LabData.Proteinuria>("label", "id")));

        DropDownChoice<LabData.Albuminuria> albuminuria = new DropDownChoice<LabData.Albuminuria>("albuminuria");
        albuminuria.setVisible(false); // this does not seem to be used in the previous site
        form.add(albuminuria);

        form.add(new YesNoNdRadioGroup("leucocytesUrine"));
        form.add(new YesNoNdRadioGroup("nitrite"));
        form.add(new YesNoNdRadioGroup("glucose"));

        // Urinalysis - lab
        form.add(new RadarTextFieldWithValidation("urineVolume", new RangeValidator<Double>(0.0, 4000.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("proteinCreatinineRatio", new RangeValidator<Double>(0.0, 15000.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("albuminCreatinineRatio", new RangeValidator<Double>(1.0, 3000.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("osmolality", new RangeValidator<Double>(200.0, 350.0), form, componentsToUpdate));
        form.add(new CheckBox("bacteria"));
        form.add(new DropDownChoice<LabData.Present>("dysmorphicErythrocytes",
                Arrays.asList(LabData.Present.values()), new ChoiceRenderer<LabData.Present>("label", "id")));
        form.add(new DropDownChoice<LabData.Present>("redCellCast", Arrays.asList(LabData.Present.values()),
                new ChoiceRenderer<LabData.Present>("label", "id")));
        form.add(new DropDownChoice<LabData.Present>("whiteCellCasts", Arrays.asList(LabData.Present.values()),
                new ChoiceRenderer<LabData.Present>("label", "id")));

        // Creatinine clearance
        TextField creatinineClearance = new TextField("creatinineClearance");
        componentsToUpdate.add(creatinineClearance);
        creatinineClearance.setOutputMarkupId(true);
        creatinineClearance.setOutputMarkupPlaceholderTag(true);
        form.add(creatinineClearance);

        Label creatanineClearanceInfo = new Label("creatanineClearanceInfo",
                "Make sure values have been entered for height on the Clinical Picture section and " +
                        "Serum Creatanine");
        creatanineClearanceInfo.setVisible(false);
        form.add(creatanineClearanceInfo);
        componentsToUpdate.add(creatanineClearanceInfo);
        creatanineClearanceInfo.setOutputMarkupId(true);
        creatanineClearanceInfo.setOutputMarkupPlaceholderTag(true);

        // Antibodies and infections
        form.add(new DropDownChoice<LabData.Anca>("anca", Arrays.asList(LabData.Anca.values()),
                new ChoiceRenderer<LabData.Anca>("label", "id")));
        form.add(new DropDownChoice<LabData.PositiveNegativeNotDone>("ena",
                Arrays.asList(LabData.PositiveNegativeNotDone.values()), new ChoiceRenderer("label", "id")));
        form.add(new DropDownChoice<LabData.PositiveNegativeNotDone>("ana",
                Arrays.asList(LabData.PositiveNegativeNotDone.values()), new ChoiceRenderer("label", "id")));
        form.add(new DropDownChoice<LabData.PositiveNegativeNotDone>("antiDsDna",
                Arrays.asList(LabData.PositiveNegativeNotDone.values()), new ChoiceRenderer("label", "id")));
        form.add(new DropDownChoice<LabData.PositiveNegativeNotDone>("cryoglobulins",
                Arrays.asList(LabData.PositiveNegativeNotDone.values()), new ChoiceRenderer("label", "id")));
        form.add(new DropDownChoice<LabData.PositiveNegativeNotDone>("antiGbm",
                Arrays.asList(LabData.PositiveNegativeNotDone.values()), new ChoiceRenderer("label", "id")));
        form.add(new TextArea("dnaAntibodies"));
        form.add(new RadarTextFieldWithValidation("igG", new RangeValidator<Double>(0.0, 20.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("igA", new RangeValidator<Double>(0.0, 10.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("igM", new RangeValidator<Double>(0.0, 10.0), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("complementC3", new RangeValidator<Double>(0.01, 9.99), form, componentsToUpdate));
        form.add(new RadarTextFieldWithValidation("complementC4", new RangeValidator<Double>(0.01, 9.99), form, componentsToUpdate));

        final IModel<Boolean> complementOtherDetailsVisibility = new Model<Boolean>(false);
        complementOtherDetailsVisibility.setObject(model.getObject().getComplementOther() != null);

        CheckBox complementOtherSelected = new CheckBox("complementOtherSelected");
        complementOtherSelected.add(new AjaxFormComponentUpdatingBehavior("onClick") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                complementOtherDetailsVisibility.setObject(model.getObject().getComplementOtherSelected());
                target.add(componentsToUpdate.toArray(new Component[componentsToUpdate.size()]));
            }
        });

        // Complement other details
        MarkupContainer complementOtherDetailContainer = new WebMarkupContainer("complementOtherDetailContainer") {
            @Override
            public boolean isVisible() {
                return complementOtherDetailsVisibility.getObject();
            }
        };
        complementOtherDetailContainer.add(new TextArea("complementOther"));
        form.add(complementOtherDetailContainer);
        componentsToUpdate.add(complementOtherDetailContainer);
        complementOtherDetailContainer.setOutputMarkupId(true);
        complementOtherDetailContainer.setOutputMarkupPlaceholderTag(true);

        form.add(complementOtherSelected);


        form.add(new DropDownChoice<LabData.PositiveNegativeUnknown>("c3NephriticFactor",
                Arrays.asList(LabData.PositiveNegativeUnknown.values()), new ChoiceRenderer("label", "id")));
        form.add(new RadarTextFieldWithValidation("antiClqAntibodies", new RangeValidator<Double>(0.0, 150.0), form, componentsToUpdate));
        form.add(new TextField("antistreptolysin"));

        form.add(new DropDownChoice<LabData.PositiveNegativeUnknown>("hepatitisB",
                Arrays.asList(LabData.PositiveNegativeUnknown.values()), new ChoiceRenderer("label", "id")));
        form.add(new DropDownChoice<LabData.PositiveNegativeUnknown>("hepatitisC",
                Arrays.asList(LabData.PositiveNegativeUnknown.values()), new ChoiceRenderer("label", "id")));
        form.add(new DropDownChoice<LabData.PositiveNegativeUnknown>("hivAntibody",
                Arrays.asList(LabData.PositiveNegativeUnknown.values()), new ChoiceRenderer("label", "id")));

        RadioGroup<Boolean> dnaTakenFactorH = new RadioGroup<Boolean>("dnaTakenFactorH");
        dnaTakenFactorH.add(new Radio<Boolean>("yes", new Model<Boolean>(Boolean.TRUE)));
        dnaTakenFactorH.add(new Radio<Boolean>("no", new Model<Boolean>(Boolean.FALSE)));
        form.add(dnaTakenFactorH);

        form.add(new DropDownChoice<LabData.Immunoglobulins>("ebv", Arrays.asList(LabData.Immunoglobulins.values()),
                new ChoiceRenderer("label", "id")));
        form.add(new DropDownChoice<LabData.Immunoglobulins>("cmvSerology", Arrays.asList(
                LabData.Immunoglobulins.values()),
                new ChoiceRenderer("label", "id")));
        form.add(new CheckBox("cmvSymptomatic"));
        form.add(new DropDownChoice<LabData.PositiveNegativeNotDone>("parvovirusAntibody",
                Arrays.asList(LabData.PositiveNegativeNotDone.values()), new ChoiceRenderer("label", "id")));
        form.add(new CheckBox("otherInfection"));

        LaboratoryAjaxSubmitLink save = new LaboratoryAjaxSubmitLink("save") {
            @Override
            protected List<? extends Component> getComponentsToUpdate() {
                return componentsToUpdate;
            }
        };

        LaboratoryAjaxSubmitLink saveDown = new LaboratoryAjaxSubmitLink("saveDown") {

            @Override
            protected List<? extends Component> getComponentsToUpdate() {
                return componentsToUpdate;
            }
        };

        form.add(save, saveDown);
    }

    private final class YesNoNdRadioGroup extends RadioGroup<Boolean> {
        private YesNoNdRadioGroup(String id) {
            super(id);
            // Yes
            Radio<Boolean> yes = new Radio<Boolean>("yes", new Model<Boolean>(Boolean.TRUE));
            add(yes, new FormComponentLabel("yesLabel", yes));

            // No
            Radio<Boolean> no = new Radio<Boolean>("no", new Model<Boolean>(Boolean.FALSE));
            add(no, new FormComponentLabel("noLabel", no));

            // ND
            Radio<Boolean> nd = new Radio<Boolean>("nd", new Model<Boolean>());
            add(nd, new FormComponentLabel("ndLabel", nd));
        }
    }


    private abstract class LaboratoryAjaxSubmitLink extends AjaxSubmitLink {

        public LaboratoryAjaxSubmitLink(String id) {
            super(id);
        }

        @Override
        public void onSubmit(AjaxRequestTarget target, Form<?> form) {
            target.add(getComponentsToUpdate().toArray(new Component[getComponentsToUpdate().size()]));
        }

        @Override
        protected void onError(AjaxRequestTarget target, Form<?> form) {
            target.add(getComponentsToUpdate().toArray(new Component[getComponentsToUpdate().size()]));
        }

        protected abstract List<? extends Component> getComponentsToUpdate();
    }

}
