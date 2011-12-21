package com.solidstategroup;

import com.solidstategroup.web.pages.BasePatientPage;
import com.solidstategroup.web.pages.RadarApplication;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.Before;
import org.junit.Test;

/**
 * Simple test using the WicketTester
 */
public class TestHomePage
{
	private WicketTester tester;

	@Before
	public void setUp()
	{
		tester = new WicketTester(new RadarApplication());
	}

	@Test
	public void homepageRendersSuccessfully()
	{
		//start and render the test page
		tester.startPage(BasePatientPage.class);

		//assert rendered page class
		tester.assertRenderedPage(BasePatientPage.class);
	}
}
