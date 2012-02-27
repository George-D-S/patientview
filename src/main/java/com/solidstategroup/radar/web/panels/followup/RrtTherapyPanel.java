package com.solidstategroup.radar.web.panels.followup;

import com.solidstategroup.radar.model.Diagnosis;
import com.solidstategroup.radar.model.Transplant;
import com.solidstategroup.radar.model.exception.InvalidModelException;
import com.solidstategroup.radar.model.user.User;
import com.solidstategroup.radar.service.DemographicsManager;
import com.solidstategroup.radar.service.DiagnosisManager;
import com.solidstategroup.radar.service.TransplantManager;
import com.solidstategroup.radar.web.RadarApplication;
import com.solidstategroup.radar.web.RadarSecuredSession;
import com.solidstategroup.radar.web.behaviours.RadarBehaviourFactory;
import com.solidstategroup.radar.web.components.RadarDateTextField;
import com.solidstategroup.radar.web.components.RadarRequiredDateTextField;
import com.solidstategroup.radar.web.components.RadarRequiredDropdownChoice;
import com.solidstategroup.radar.web.components.YesNoRadioGroup;
import com.solidstategroup.radar.web.models.RadarModelFactory;
import com.solidstategroup.radar.web.panels.FollowUpPanel;
import com.solidstategroup.radar.web.panels.tables.DialysisTablePanel;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class RrtTherapyPanel extends Panel {
    @SpringBean
    private DemographicsManager demographicsManager;
    @SpringBean
    private DiagnosisManager diagnosisManager;
    @SpringBean
    private TransplantManager transplantManager;

    public RrtTherapyPanel(String id, final IModel<Long> radarNumberModel) {
        super(id);
        setOutputMarkupId(true);
        setOutputMarkupPlaceholderTag(true);

        // General details
        TextField radarNumber = new TextField("radarNumber", radarNumberModel);
        radarNumber.setEnabled(false);
        add(radarNumber);

        add(new TextField("hospitalNumber", RadarModelFactory.getHospitalNumberModel(radarNumberModel,
                demographicsManager)));

        add(new TextField("diagnosis", new PropertyModel(RadarModelFactory.getDiagnosisCodeModel(radarNumberModel,
                diagnosisManager), "abbreviation")));

        add(new TextField("firstName", RadarModelFactory.getFirstNameModel(radarNumberModel, demographicsManager)));
        add(new TextField("surname", RadarModelFactory.getSurnameModel(radarNumberModel, demographicsManager)));
        add(new TextField("dob", RadarModelFactory.getDobModel(radarNumberModel, demographicsManager)));

        final IModel<Date> esrfDateModel = new LoadableDetachableModel<Date>() {
            @Override
            protected Date load() {
                Diagnosis diagnosis = RadarModelFactory.getDiagnosisModel(radarNumberModel, diagnosisManager).getObject();
                if (diagnosis != null) {
                    return diagnosis.getEsrfDate();
                }
                return null;
            }
        };
        add(new DateLabel("esrfDate", esrfDateModel, new PatternDateConverter(RadarApplication.DATE_PATTERN, true)) {
            @Override
            public boolean isVisible() {
                return esrfDateModel.getObject() != null;
            }
        });
        add(new WebMarkupContainer("esrfNotEnteredContainer") {
            @Override
            public boolean isVisible() {
                return esrfDateModel.getObject() == null;
            }
        });

        // Reusable panel for the dialysis table
        add(new DialysisTablePanel("dialysisContainer", radarNumberModel));

        final IModel transplantListModel = new AbstractReadOnlyModel<List>() {
            @Override
            public List getObject() {

                if (radarNumberModel.getObject() != null) {
                    return transplantManager.getTransplantsByRadarNumber(radarNumberModel.getObject());
                }
                return Collections.emptyList();

            }
        };

        final IModel editTransplantModel = new Model<Transplant>();
        final List<Component> addTransplantFormComponentsToUpdate = new ArrayList<Component>();
        final List<Component> editTransplantFormComponentsToUpdate = new ArrayList<Component>();

        final WebMarkupContainer transplantsContainer = new WebMarkupContainer("transplantsContainer");
        add(transplantsContainer);
        transplantsContainer.setOutputMarkupPlaceholderTag(true);
        transplantsContainer.setOutputMarkupId(true);

        // Container for edit transplants
        final MarkupContainer editTransplantContainer = new WebMarkupContainer("editTransplantContainer") {
            @Override
            public boolean isVisible() {
                return editTransplantModel.getObject() != null;
            }
        };

        editTransplantContainer.setOutputMarkupPlaceholderTag(true);
        editTransplantContainer.setOutputMarkupPlaceholderTag(true);
        add(editTransplantContainer);

        final IModel<Transplant.RejectData> addRejectModel = new CompoundPropertyModel<Transplant.RejectData>(
                new Model<Transplant.RejectData>());
        // Container for reject transplants
        final MarkupContainer rejectDataContainer = new WebMarkupContainer("rejectDataContainer") {
            @Override
            public boolean isVisible() {
                return addRejectModel.getObject() != null;
            }
        };
        rejectDataContainer.setOutputMarkupPlaceholderTag(true);
        rejectDataContainer.setOutputMarkupId(true);

        add(rejectDataContainer);

        // Transplants table
        transplantsContainer.add(new ListView<Transplant>("transplants", transplantListModel) {
            @Override
            protected void populateItem(final ListItem<Transplant> item) {
                item.setModel(new CompoundPropertyModel<Transplant>(item.getModelObject()));
                item.add(DateLabel.forDatePattern("date", RadarApplication.DATE_PATTERN));
                item.add(new Label("modality.description"));
                item.add(new Label("recurr", new AbstractReadOnlyModel<Object>() {
                    @Override
                    public Object getObject() {
                        return Boolean.TRUE.equals(item.getModelObject().getRecurr()) ? "yes" : "no";
                    }
                }));
                item.add(DateLabel.forDatePattern("dateRecurr", RadarApplication.DATE_PATTERN));
                item.add(DateLabel.forDatePattern("dateFailureRejectData.failureDate", RadarApplication.DATE_PATTERN));

                IModel rejectDataListModel = new AbstractReadOnlyModel<List>() {
                    @Override
                    public List getObject() {
                        return transplantManager.getRejectDataByTransplantNumber(item.getModelObject().getId());
                    }
                };
                final WebMarkupContainer rejectDataListContainer = new WebMarkupContainer("rejectDataListContainer");
                rejectDataListContainer.setOutputMarkupId(true);
                rejectDataContainer.setOutputMarkupPlaceholderTag(true);
                rejectDataListContainer.add(new ListView<Transplant.RejectData>("rejectDataList", rejectDataListModel) {
                    @Override
                    protected void populateItem(final ListItem<Transplant.RejectData> rejectDataListItem) {
                        rejectDataListItem.setModel(new CompoundPropertyModel<Transplant.RejectData>(
                                rejectDataListItem.getModelObject()));
                        rejectDataListItem.add(DateLabel.forDatePattern("rejectedDate", RadarApplication.DATE_PATTERN));
                        rejectDataListItem.add(DateLabel.forDatePattern("biopsyDate", RadarApplication.DATE_PATTERN));
                        AjaxLink ajaxDeleteLink = new AjaxLink("deleteLink") {
                            @Override
                            public void onClick(AjaxRequestTarget target) {
                                transplantManager.deleteRejectData(rejectDataListItem.getModelObject());
                                target.add(rejectDataListContainer);
                            }
                        };
                        rejectDataListItem.add(ajaxDeleteLink);
                        ajaxDeleteLink.add(RadarBehaviourFactory.getDeleteConfirmationBehaviour());

                        AuthenticatedWebSession session = RadarSecuredSession.get();
                        if (session.isSignedIn()) {
                            if (session.getRoles().hasRole(User.ROLE_PATIENT)) {
                                ajaxDeleteLink.setVisible(false);
                            }
                        }

                    }
                });

                item.add(rejectDataListContainer);

                // Delete, edit and add reject buttons
                AjaxLink ajaxDeleteLink = new AjaxLink("deleteLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        Transplant transplant = item.getModelObject();
                        transplantManager.deleteTransplant(transplant);
                        target.add(addTransplantFormComponentsToUpdate.toArray(new Component[
                                addTransplantFormComponentsToUpdate.size()]));
                        target.add(transplantsContainer);
                    }
                };
                item.add(ajaxDeleteLink);
                ajaxDeleteLink.add(RadarBehaviourFactory.getDeleteConfirmationBehaviour());
                AjaxLink ajaxEditLink = new AjaxLink("editLink") {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        editTransplantModel.setObject(item.getModelObject());
                        target.add(editTransplantContainer);
                    }
                };
                item.add(ajaxEditLink);
                AjaxLink ajaxAddRejectLink = new AjaxLink("addRejectLink") {
                    {
                        if (item.getModelObject().getDateFailureRejectData() != null) {
                            if (item.getModelObject().getDateFailureRejectData().getFailureDate() != null) {
                                setVisible(false);
                            }
                        }
                    }

                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        Transplant.RejectData rejectData = new Transplant.RejectData();
                        rejectData.setTransplantId(item.getModelObject().getId());
                        addRejectModel.setObject(rejectData);
                        target.add(rejectDataContainer);
                    }
                };
                item.add(ajaxAddRejectLink);

                AuthenticatedWebSession session = RadarSecuredSession.get();
                if (session.isSignedIn()) {
                    if (session.getRoles().hasRole(User.ROLE_PATIENT)) {
                        ajaxDeleteLink.setVisible(false);
                        ajaxEditLink.setVisible(false);
                        ajaxAddRejectLink.setVisible(false);
                    }
                }
            }
        });

        final List<Component> rejectDataComponentsToUpdate = new ArrayList<Component>();
        // Form for adding reject data - model probably needs changing
        Form<Transplant.RejectData> rejectDataForm = new Form<Transplant.RejectData>("form", addRejectModel);
        rejectDataForm.add(new RadarDateTextField("rejectedDate", rejectDataForm, rejectDataComponentsToUpdate));
        rejectDataForm.add(new RadarDateTextField("biopsyDate", rejectDataForm, rejectDataComponentsToUpdate));
        rejectDataForm.add(new AjaxSubmitLink("add") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.add(rejectDataComponentsToUpdate.toArray(new Component[rejectDataComponentsToUpdate.size()]));
                transplantManager.saveRejectData((Transplant.RejectData) form.getModelObject());
                addRejectModel.setObject(null);
                target.add(rejectDataContainer);
                target.add(transplantsContainer);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(rejectDataComponentsToUpdate.toArray(new Component[rejectDataComponentsToUpdate.size()]));
            }
        });
        rejectDataForm.add(new AjaxLink("cancel") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                addRejectModel.setObject(null);
                target.add(rejectDataContainer);
            }
        });
        rejectDataForm.add(DateLabel.forDatePattern("transplantDate", new Model<Date>(), "dd/MM/yyyy"));
        rejectDataContainer.add(rejectDataForm);

        // Edit transplant form
        Form<Transplant> editTransplantForm =
                new TransplantForm("form", new CompoundPropertyModel<Transplant>(editTransplantModel), editTransplantFormComponentsToUpdate);
        editTransplantContainer.add(editTransplantForm);

        editTransplantForm.add(new AjaxSubmitLink("save") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.add(editTransplantContainer);
                target.add(transplantsContainer);

                try {
                    transplantManager.saveTransplant((Transplant) form.getModelObject());
                } catch (InvalidModelException e) {
                    for (String error : e.getErrors()) {
                        error(error);
                    }
                    return;
                }

                editTransplantModel.setObject(null);

            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(editTransplantFormComponentsToUpdate.toArray(
                        new Component[editTransplantFormComponentsToUpdate.size()]));
            }
        });
        editTransplantForm.add(new AjaxLink("cancel") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                editTransplantModel.setObject(null);
                target.add(editTransplantContainer);
            }
        });

        // Add transplant form
        Form<Transplant> addTransplantForm =
                new TransplantForm("addTransplantForm", new CompoundPropertyModel<Transplant>(new Transplant()),
                        addTransplantFormComponentsToUpdate);
        addTransplantForm.add(new AjaxSubmitLink("add") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                target.add(form);
                Transplant transplant = (Transplant) form.getModelObject();
                transplant.setRadarNumber(radarNumberModel.getObject());
                try {
                    transplantManager.saveTransplant(transplant);
                } catch (InvalidModelException e) {
                    for (String error : e.getErrors()) {
                        error(error);
                    }
                    return;
                }
                form.getModel().setObject(new Transplant());
                transplantsContainer.setVisible(true);
                target.add(transplantsContainer);
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(addTransplantFormComponentsToUpdate.toArray(new Component[addTransplantFormComponentsToUpdate.size()]));
            }
        });
        addTransplantForm.setOutputMarkupId(true);
        addTransplantForm.setOutputMarkupPlaceholderTag(true);
        add(addTransplantForm);

    }

    private final class TransplantForm extends Form<Transplant> {
        @SpringBean
        private TransplantManager transplantManager;

        private TransplantForm(String id, IModel<Transplant> transplantIModel, List<Component> componentsToUpdate) {
            super(id, transplantIModel);
            RadarRequiredDateTextField date = new RadarRequiredDateTextField("date", this, componentsToUpdate);
            add(date);
            RadarRequiredDropdownChoice modality = new RadarRequiredDropdownChoice("modality",
                    transplantManager.getTransplantModalitites(), new ChoiceRenderer("description", "id"), this,
                    componentsToUpdate);
            add(modality);
            YesNoRadioGroup recurr = new YesNoRadioGroup("recurr") {
                /**
                 * disable input if editing and date of failure is set
                 */
                @Override
                public boolean isEnabled() {
                    Transplant transplant = TransplantForm.this.getModelObject();
                    if (transplant != null) {
                        if (transplant.getDateFailureRejectData() != null && transplant.getId() != null) {
                            if (transplant.getDateFailureRejectData().getFailureDate() != null) {
                                return false;
                            }
                        }
                    }
                    return true;
                }
            };
            add(recurr);
            RadarDateTextField dateRecurr = new RadarDateTextField("dateRecurr", this, componentsToUpdate) {
                /**
                 * disable input if editing and date of failure is set
                 */
                @Override
                public boolean isEnabled() {
                    Transplant transplant = TransplantForm.this.getModelObject();
                    if (transplant != null) {
                        if (transplant.getDateFailureRejectData() != null && transplant.getId() != null) {
                            if (transplant.getDateFailureRejectData().getFailureDate() != null) {
                                return false;
                            }
                        }
                    }
                    return true;
                }
            };
            add(dateRecurr);
            RadarDateTextField dateFailure = new RadarDateTextField("dateFailureRejectData.failureDate", this,
                    componentsToUpdate);
            add(dateFailure);

            FeedbackPanel editTransplantFeedback = new FeedbackPanel("transplantFeedback", new IFeedbackMessageFilter() {
                public boolean accept(FeedbackMessage feedbackMessage) {
                    for(String errorMessage : Arrays.asList(TransplantManager.BEFORE_PREVIOUS_FAILURE_DATE,
                            TransplantManager.PREVIOUS_TRANSPLANT_NOT_FAILED_ERROR,
                            TransplantManager.TRANSPLANTS_INTERVAL_ERROR, TransplantManager.RECURRANCE_DATE_ERROR)) {
                        if(feedbackMessage.getMessage().equals(errorMessage)) {
                            return true;
                        }
                    }
                    return false;
                }
            });

            add(editTransplantFeedback);
            editTransplantFeedback.setOutputMarkupPlaceholderTag(true);
            componentsToUpdate.add(editTransplantFeedback);
        }
    }

    @Override
    public boolean isVisible() {
        return ((FollowUpPanel) getParent()).getCurrentTab().equals(FollowUpPanel.CurrentTab.RRT_THERAPY);
    }
}
