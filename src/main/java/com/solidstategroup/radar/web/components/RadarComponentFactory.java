package com.solidstategroup.radar.web.components;


import com.solidstategroup.radar.web.models.RadarModelFactory;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.validation.IValidator;

import java.io.Serializable;
import java.util.List;

public class RadarComponentFactory {

    public static Label getSuccessMessageLabel(String id, final Form form, final List<Component> componentsToUpdate) {
        return new Label(id, RadarModelFactory.getSuccessMessageModel(form)) {
            {
                setOutputMarkupId(true);
                setOutputMarkupPlaceholderTag(true);
                form.add(this);
                componentsToUpdate.add(this);
            }

            @Override
            public boolean isVisible() {
                return form.isSubmitted() && !form.hasError();
            }
        };
    }

    public static Label getErrorMessageLabel(String id, final Form form, final List<Component> componentsToUpdate) {
        return new Label(id, "Please fix all errors") {
            {
                setOutputMarkupId(true);
                setOutputMarkupPlaceholderTag(true);
                form.add(this);
                componentsToUpdate.add(this);
            }

            @Override
            public boolean isVisible() {
                return form.isSubmitted() && form.hasError();
            }
        };
    }

    public static PasswordTextField getRequiredPasswordTextField(String id, final WebMarkupContainer parent,
                                                                 final List<Component> componentsToUpdateList) {
        return new PasswordTextField(id) {
            {
                setRequired(true);
                RadarFormComponentFeedbackIndicator radarFormComponentFeedbackIndicator =
                        new RadarFormComponentFeedbackIndicator(getId() + "FeedbackIndicator", this);
                parent.add(radarFormComponentFeedbackIndicator);
                radarFormComponentFeedbackIndicator.setOutputMarkupId(true);
                radarFormComponentFeedbackIndicator.setOutputMarkupPlaceholderTag(true);
                componentsToUpdateList.add(radarFormComponentFeedbackIndicator);
            }
        };
    }

    public static PasswordTextField getRadarPasswordTextFieldWithValidation(String id, IValidator validator,
                                                                            boolean required,
                                                                    WebMarkupContainer container,
                                                                    List<Component> componentsToUpdate) {
        return new RadarPasswordTextFieldWithValidation(id, validator, required, container, componentsToUpdate);
    }

}
