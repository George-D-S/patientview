package com.solidstategroup.radar.test.web;

import com.solidstategroup.radar.web.pages.content.DiseaseIndexPage;
import org.junit.Test;

/**
 * Simple test using the WicketTester
 */
public class TestDiseaseIndexPage extends BasePageTest {

    @Test
    public void homepageRendersSuccessfully() {
        //start and renderTemplate the test page
        tester.startPage(DiseaseIndexPage.class);

        //assert rendered page class
        tester.assertRenderedPage(DiseaseIndexPage.class);
    }
}
