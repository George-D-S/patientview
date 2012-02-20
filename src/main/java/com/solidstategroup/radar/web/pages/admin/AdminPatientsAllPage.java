package com.solidstategroup.radar.web.pages.admin;

import com.solidstategroup.radar.model.enums.ExportType;
import com.solidstategroup.radar.service.DemographicsManager;
import com.solidstategroup.radar.service.ExportManager;
import com.solidstategroup.radar.service.DiagnosisManager;
import com.solidstategroup.radar.web.dataproviders.DemographicsDataProvider;
import com.solidstategroup.radar.web.RadarApplication;
import com.solidstategroup.radar.web.components.SortLink;
import com.solidstategroup.radar.web.components.SearchField;
import com.solidstategroup.radar.web.components.ClearLink;
import com.solidstategroup.radar.model.Demographics;
import com.solidstategroup.radar.model.Diagnosis;
import com.solidstategroup.radar.model.filter.DemographicsFilter;
import com.solidstategroup.radar.web.resources.RadarResourceFactory;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.ajax.markup.html.navigation.paging.AjaxPagingNavigator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.model.Model;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class AdminPatientsAllPage extends AdminsBasePage {
    @SpringBean
    private DemographicsManager demographicsManager;
    @SpringBean
    private ExportManager exportManager;
    @SpringBean
    private DiagnosisManager diagnosisManager;

    private static final int RESULTS_PER_PAGE = 10;

    public AdminPatientsAllPage() {
        final DemographicsDataProvider demographicsDataProvider = new DemographicsDataProvider(demographicsManager);

        add(new ResourceLink("exportPdf", RadarResourceFactory.getExportResource(
                exportManager.getDemographicsExportData(ExportType.PDF), "patients-all" +
                AdminsBasePage.EXPORT_FILE_NAME_SUFFIX, ExportType.PDF)));

        add(new ResourceLink("exportCsv", RadarResourceFactory.getExportResource(
                exportManager.getDemographicsExportData(ExportType.CSV), "patients-all" +
                AdminsBasePage.EXPORT_FILE_NAME_SUFFIX, ExportType.CSV)));

        final WebMarkupContainer demographicsContainer = new WebMarkupContainer("demographicsContainer");
        demographicsContainer.setOutputMarkupId(true);
        add(demographicsContainer);

        final DataView<Demographics> demographicsList = new DataView<Demographics>("demographics",
                demographicsDataProvider) {
            @Override
            protected void populateItem(Item<Demographics> item) {
                builtDataViewRow(item);
            }
        };
        demographicsList.setItemsPerPage(RESULTS_PER_PAGE);
        demographicsContainer.add(demographicsList);

        // add paging element
        demographicsContainer.add(new AjaxPagingNavigator("navigator", demographicsList));

        // add sort links to the table column headers
        for (Map.Entry<String, String> entry : getSortFields().entrySet()) {
            add(new SortLink(entry.getKey(), entry.getValue(), demographicsDataProvider, demographicsList,
                    Arrays.asList(demographicsContainer)));
        }

        // button to clear all the filter fields for each colum
        final ClearLink clearButton = new ClearLink("clearButton", demographicsDataProvider, demographicsList,
                demographicsContainer);
        add(clearButton);

        // add a search field to the top of each column - these will AND each search
        for (Map.Entry<String, String> entry : getFilterFields().entrySet()) {
            add(new SearchField(entry.getKey(), entry.getValue(), demographicsDataProvider, demographicsList,
                    Arrays.asList(demographicsContainer, clearButton)));
        }
    }

    /**
     * Build a row in the dataview from the object
     *
     * @param item Item<Demographics>
     */
    private void builtDataViewRow(Item<Demographics> item) {
        Demographics demographics = item.getModelObject();
        item.add(new BookmarkablePageLink<AdminPatientsAllPage>("edit", AdminPatientAllPage.class,
                AdminPatientAllPage.getPageParameters(demographics)));
        item.add(new Label("radarNo", demographics.getId().toString()));
        item.add(DateLabel.forDatePattern("dateRegistered", new Model<Date>(demographics.getDateRegistered()),
                RadarApplication.DATE_PATTERN));
        item.add(new Label("forename", demographics.getForename()));
        item.add(new Label("surname", demographics.getSurname()));

        // build address
        List<String> addressValues = new ArrayList<String>();

        if (demographics.getAddress1() != null && demographics.getAddress1().length() > 0) {
            addressValues.add(demographics.getAddress1());
        }

        if (demographics.getAddress2() != null && demographics.getAddress2().length() > 0) {
            addressValues.add(demographics.getAddress2());
        }

        if (demographics.getAddress3() != null && demographics.getAddress3().length() > 0) {
            addressValues.add(demographics.getAddress3());
        }

        if (demographics.getAddress4() != null && demographics.getAddress4().length() > 0) {
            addressValues.add(demographics.getAddress4());
        }

        if (demographics.getPostcode() != null && demographics.getPostcode().length() > 0) {
            addressValues.add(demographics.getPostcode());
        }

        item.add(new Label("address", StringUtils.join(addressValues, ", ")));

        String diagnosisAbbrev = "";
        Diagnosis diagnosis = diagnosisManager.getDiagnosisByRadarNumber(demographics.getId());

        if (diagnosis != null && diagnosis.getDiagnosisCode() != null) {
            diagnosisAbbrev = diagnosis.getDiagnosisCode().getAbbreviation();
        }

        item.add(new Label("diagnosis", diagnosisAbbrev));

        String consultantSurname = "", consultantForename = "", centreAbbrv = "";

        if (demographics.getConsultant() != null) {
            consultantSurname = demographics.getConsultant().getSurname();
            consultantForename = demographics.getConsultant().getForename();

            if (demographics.getConsultant().getCentre() != null) {
                centreAbbrv = demographics.getConsultant().getCentre().getAbbreviation();
            }
        }

        item.add(new Label("consultantSurname", consultantSurname));
        item.add(new Label("consultantForename", consultantForename));
        item.add(new Label("centre", centreAbbrv));
    }

    /**
     * List of columns that can be used to sort the results - will return ID of el to be bound to and the field to sort
     *
     * @return Map<String, DemographicsFilter.UserField>
     */
    private Map<String, String> getSortFields() {
        return new HashMap<String, String>() {
            {
                put("orderByRadarNo", DemographicsFilter.UserField.RADAR_NO.getDatabaseFieldName());
                put("orderByDateRegistered", DemographicsFilter.UserField.REGISTRATION_DATE.getDatabaseFieldName());
                put("orderByForename", DemographicsFilter.UserField.FORENAME.getDatabaseFieldName());
                put("orderBySurname", DemographicsFilter.UserField.SURNAME.getDatabaseFieldName());
                put("orderByAddress", DemographicsFilter.UserField.ADDRESS.getDatabaseFieldName());
                put("orderByDiagnosis", DemographicsFilter.UserField.DIAGNOSIS.getDatabaseFieldName());
                put("orderByConsultantSurname", DemographicsFilter.UserField.CONSULTANT_SURNAME.getDatabaseFieldName());
                put("orderByConsultantForename",
                        DemographicsFilter.UserField.CONSULTANT_FORNAME.getDatabaseFieldName());
                put("orderByCentre", DemographicsFilter.UserField.CENTRE.getDatabaseFieldName());
            }
        };
    }

    /**
     * List of column filters - will return ID of el to be bound to and the field to filter
     *
     * @return Map<String, DemographicsFilter.UserField>
     */
    private Map<String, String> getFilterFields() {
        return new HashMap<String, String>() {
            {
                put("searchRadarNo", DemographicsFilter.UserField.RADAR_NO.getDatabaseFieldName());
                put("searchDiagnosis", DemographicsFilter.UserField.DIAGNOSIS.getDatabaseFieldName());
                put("searchConsultantSurname", DemographicsFilter.UserField.CONSULTANT_SURNAME.getDatabaseFieldName());
                put("searchConsultantForename", DemographicsFilter.UserField.CONSULTANT_FORNAME.getDatabaseFieldName());
                put("searchCentre", DemographicsFilter.UserField.CENTRE.getDatabaseFieldName());
                // TODO: add the date filter
            }
        };
    }
}
