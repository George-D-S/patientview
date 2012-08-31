package com.solidstategroup.radar.test.dao;

import com.solidstategroup.radar.dao.DemographicsDao;
import com.solidstategroup.radar.dao.UserDao;
import com.solidstategroup.radar.model.Centre;
import com.solidstategroup.radar.model.Demographics;
import com.solidstategroup.radar.model.filter.PatientUserFilter;
import com.solidstategroup.radar.model.filter.ProfessionalUserFilter;
import com.solidstategroup.radar.model.user.AdminUser;
import com.solidstategroup.radar.model.user.PatientUser;
import com.solidstategroup.radar.model.user.ProfessionalUser;
import com.solidstategroup.radar.model.user.User;
import com.solidstategroup.radar.util.RadarUtility;
import com.solidstategroup.radar.util.TripleDes;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UserDaoTest extends BaseDaoTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoTest.class);
    
    @Autowired
    private UserDao userDao;

    @Autowired
    private DemographicsDao demographicsDao;

    @Test
    public void testAddGetAdminUser() throws Exception {
        AdminUser adminUser = new AdminUser();
        adminUser.setEmail("admin@radar101.com");
        adminUser.setUsername("admin@radar101.com");
        adminUser.setForename("Admin");
        adminUser.setSurname("Admin");
        adminUser.setPassword(RadarUtility.generateNewPassword());

        userDao.saveAdminUser(adminUser);

        AdminUser checkAdminUser = userDao.getAdminUser(adminUser.getEmail());

        assertNotNull(checkAdminUser);
        assertTrue("Does not have a valid ID", checkAdminUser.hasValidId());
        assertEquals("Email not persisted", checkAdminUser.getEmail(), adminUser.getEmail());
        assertEquals("Username not persisted", checkAdminUser.getUsername(), adminUser.getUsername());
        assertEquals("Forename not persisted", checkAdminUser.getForename(), adminUser.getForename());
        assertEquals("Surname not persisted", checkAdminUser.getSurname(), adminUser.getSurname());
        assertEquals("Password not persisted", checkAdminUser.getPassword(), adminUser.getPassword());
    }

    @Test
    public void testAddGetProfessionalUser() throws Exception {
        ProfessionalUser professionalUser = new ProfessionalUser();
        professionalUser.setEmail("professional@radar101.com");
        professionalUser.setUsername("professional@radar101.com");
        professionalUser.setForename("Eddard");
        professionalUser.setSurname("Stark");
        professionalUser.setRole("Centre role");
        professionalUser.setGmc("testGmc");
        professionalUser.setPassword(RadarUtility.generateNewPassword());

        userDao.saveProfessionalUser(professionalUser);

        ProfessionalUser checkProfessionalUser = userDao.getProfessionalUser(professionalUser.getEmail());
        assertNotNull(checkProfessionalUser);
        assertTrue("Does not have a valid ID", checkProfessionalUser.hasValidId());
        assertEquals("Email not persisted", checkProfessionalUser.getEmail(), professionalUser.getEmail());
        assertEquals("Username not persisted", checkProfessionalUser.getUsername(), professionalUser.getUsername());
        assertEquals("Forename not persisted", checkProfessionalUser.getForename(), professionalUser.getForename());
        assertEquals("Surname not persisted", checkProfessionalUser.getSurname(), professionalUser.getSurname());
        assertEquals("Password not persisted", checkProfessionalUser.getPassword(), professionalUser.getPassword());
        assertEquals("Role not persisted", checkProfessionalUser.getRole(), professionalUser.getRole());
        assertEquals("GMC not persisted", checkProfessionalUser.getGmc(), professionalUser.getGmc());
    }

    @Test
    public void testGetProfessionalUserById() throws Exception {
        ProfessionalUser professionalUser = new ProfessionalUser();
        professionalUser.setEmail("professional@radar101.com");
        professionalUser.setUsername("professional@radar101.com");
        professionalUser.setForename("Eddard");
        professionalUser.setSurname("Stark");
        professionalUser.setRole("Centre role");
        professionalUser.setGmc("testGmc");
        professionalUser.setPassword(RadarUtility.generateNewPassword());

        userDao.saveProfessionalUser(professionalUser);

        ProfessionalUser checkProfessionalUser = userDao.getProfessionalUser(professionalUser.getId());
        assertNotNull(checkProfessionalUser);
    }

    @Test
    public void testDeleteProfessionalUser() throws Exception {
        ProfessionalUser professionalUser = new ProfessionalUser();
        professionalUser.setEmail("professional@radar101.com");
        professionalUser.setUsername("professional@radar101.com");
        professionalUser.setForename("Eddard");
        professionalUser.setSurname("Stark");
        professionalUser.setRole("Centre role");
        professionalUser.setGmc("testGmc");
        professionalUser.setPassword(RadarUtility.generateNewPassword());

        userDao.saveProfessionalUser(professionalUser);

        // delete the user and try to pull back
        userDao.deleteProfessionalUser(professionalUser);

        ProfessionalUser checkProfessionalUser = userDao.getProfessionalUser(professionalUser.getEmail());
        assertNull(checkProfessionalUser);
    }

    @Test
    public void testGetProfessionalUsersInOrder() throws Exception {
        ProfessionalUser professionalUser1 = new ProfessionalUser();
        professionalUser1.setEmail("professional1@radar101.com");
        professionalUser1.setUsername("professional1@radar101.com");
        professionalUser1.setForename("Eddard");
        professionalUser1.setSurname("Stark");
        professionalUser1.setRole("Centre role");
        professionalUser1.setGmc("testGmc");
        professionalUser1.setPassword(RadarUtility.generateNewPassword());

        userDao.saveProfessionalUser(professionalUser1);

        ProfessionalUser professionalUser2 = new ProfessionalUser();
        professionalUser2.setEmail("professional2@radar101.com");
        professionalUser2.setUsername("professional2@radar101.com");
        professionalUser2.setForename("Tyrion");
        professionalUser2.setSurname("Lannister");
        professionalUser2.setRole("Centre role");
        professionalUser2.setGmc("testGmc");
        professionalUser2.setPassword(RadarUtility.generateNewPassword());

        userDao.saveProfessionalUser(professionalUser2);

        List<ProfessionalUser> checkProfessionalUsers = userDao.getProfessionalUsers(new ProfessionalUserFilter(),
                -1, -1);

        assertTrue("No professional users found", !checkProfessionalUsers.isEmpty()
                && checkProfessionalUsers.size() > 0);
        assertTrue("To many professional users found", checkProfessionalUsers.size() == 2);

        // first one should Tyrion
        assertEquals("First user in list is not correct", checkProfessionalUsers.get(0).getId(),
                professionalUser2.getId());
    }

    @Test
    public void testSearchProfessionalUsers() throws Exception {
        ProfessionalUser professionalUser1 = new ProfessionalUser();
        professionalUser1.setEmail("professional1@radar101.com");
        professionalUser1.setUsername("professional1@radar101.com");
        professionalUser1.setForename("Eddard");
        professionalUser1.setSurname("Stark");
        professionalUser1.setRole("Centre role");
        professionalUser1.setGmc("testGmc");
        professionalUser1.setPassword(RadarUtility.generateNewPassword());

        userDao.saveProfessionalUser(professionalUser1);

        ProfessionalUser professionalUser2 = new ProfessionalUser();
        professionalUser2.setEmail("professional2@radar101.com");
        professionalUser2.setUsername("professional2@radar101.com");
        professionalUser2.setForename("Tyrion");
        professionalUser2.setSurname("Lannister");
        professionalUser2.setRole("Centre role");
        professionalUser2.setGmc("testGmc");
        professionalUser2.setPassword(RadarUtility.generateNewPassword());

        userDao.saveProfessionalUser(professionalUser2);

        ProfessionalUserFilter professionalUserFilter = new ProfessionalUserFilter();
        professionalUserFilter.addSearchCriteria(ProfessionalUserFilter.UserField.EMAIL.getDatabaseFieldName(),
                professionalUser2.getEmail());

        List<ProfessionalUser> checkProfessionalUsers = userDao.getProfessionalUsers(professionalUserFilter, -1, -1);

        assertTrue("No professional users found", !checkProfessionalUsers.isEmpty()
                && checkProfessionalUsers.size() > 0);
        assertTrue("To many professional users found", checkProfessionalUsers.size() == 1);

        // first one should Tyrion
        assertEquals("First user in list is not correct", checkProfessionalUsers.get(0).getId(),
                professionalUser2.getId());
    }

    @Test
    public void testGetAdminUser() {
        AdminUser adminUser = userDao.getAdminUser("ihaynes@data-insite.co.uk");
        assertNotNull(adminUser);
    }

    @Test
    public void testGetPatientUser() {
        // Get a user
        PatientUser patientUser = userDao.getPatientUser("ihaynes@data-insite.co.uk");

        // This patient should exist in our test dataset
        assertNotNull(patientUser);
    }

    @Test
    public void testGetUnknownPatientUser() {
        // Get a user that doesn't exist
        PatientUser patientUser = userDao.getPatientUser("doesnt@exist.com");
        assertNull(patientUser);
    }

    @Test
    public void testSavePatientUser() throws Exception {
        // Construct the user
        PatientUser patientUser = new PatientUser();
        patientUser.setUsername("test_user");
        //patientUser.setRadarNumber(123);
        patientUser.setDateOfBirth(new Date());
        //patientUser.setPasswordHash(User.getPasswordHash("password12"));

        // Save
        userDao.savePatientUser(patientUser);

        // Make sure we have an ID and a date registered
        assertTrue("Saved user doesn't have an ID", patientUser.getId() > 0);
        // Make sure it has a date registered
        //assertNotNull("No date registered", patientUser.getDateRegistered());

        // Try and get the patient user - should get our new user
        patientUser = userDao.getPatientUser("test_user");
        assertNotNull("Saved user was null on getting frmo DAO", patientUser);
    }

    @Test
    public void testGetPatientUsers() {
        List<PatientUser> patientUsers = userDao.getPatientUsers(new PatientUserFilter(), -1, -1);
        assertNotNull(patientUsers);
        assertTrue(patientUsers.size() > 0);
    }

    @Test
    public void testGetPatientUsersPage1() {
        List<PatientUser> patientUsers = userDao.getPatientUsers(new PatientUserFilter(), 1, 1);
        assertNotNull(patientUsers);
        assertTrue(patientUsers.size() == 1);
    }

    @Test
    public void testSearchPatientUsers() {
        PatientUserFilter userFilter = new PatientUserFilter();
        userFilter.addSearchCriteria(PatientUserFilter.UserField.RADAR_NO.getDatabaseFieldName(), "246");
        List<PatientUser> patientUsers = userDao.getPatientUsers(userFilter, -1, -1);
        assertNotNull(patientUsers);
        assertTrue(patientUsers.size() > 0);
    }

    @Test
    public void testDeletePatientUser() throws Exception {
        PatientUser patientUser = userDao.getPatientUser(1L);
        Long radarNo = patientUser.getRadarNumber();

        userDao.deletePatientUser(patientUser);

        // have to check the user was deleted and their demographics record
        patientUser = userDao.getPatientUser(1L);
        Demographics demographics = demographicsDao.getDemographicsByRadarNumber(radarNo);

        assertNull("User was found after being deleted", patientUser);
        assertNull("Demographics for user was found after being deleted", demographics);
    }

    @Test
    public void testSaveNewProfessionalUser() throws Exception {
        ProfessionalUser professionalUser = new ProfessionalUser();
        //professionalUser.setUsernameHash(User.getUsernameHash("test_admin_user"));
        //professionalUser.setPasswordHash(User.getPasswordHash("password12"));
        professionalUser.setSurname("test_surname");
        professionalUser.setForename("test_forename");
        professionalUser.setTitle("test_title");
        professionalUser.setRole("test_role");
        professionalUser.setGmc("test_gmc");
        professionalUser.setEmail("test_email");
        professionalUser.setPhone("test_phone");

        Centre centre = new Centre();
        centre.setId((long) 10);
        professionalUser.setCentre(centre);

        userDao.saveProfessionalUser(professionalUser);

        assertTrue("Saved user doesn't have an ID", professionalUser.getId() > 0);
        //assertNotNull("No date registered", professionalUser.getDateRegistered());

        professionalUser = userDao.getProfessionalUser("test_email");
        assertNotNull("Saved user was null on getting from DAO", professionalUser);
    }

    @Test
    public void testSaveExistingProfessionalUser() throws Exception {
        // have to make a user first
        ProfessionalUser professionalUser = userDao.getProfessionalUser("marklittle@nhs.net");
        professionalUser.setSurname("edit 3");

        userDao.saveProfessionalUser(professionalUser);

        professionalUser = userDao.getProfessionalUser("marklittle@nhs.net");
        assertTrue("User surname has not been updated", professionalUser.getSurname().equals("edit 3"));
    }

    @Test
    public void testGetProfessionalUser() {
        // Get a user
        ProfessionalUser professionalUser = userDao.getProfessionalUser("marklittle@nhs.net");

        // This professional user should exist in our test database
        assertNotNull(professionalUser);

        // Check the various strings
        assertEquals("Wrong surname", "Little", professionalUser.getSurname());
        assertEquals("Wrong forename", "Mark", professionalUser.getForename());
        assertEquals("Wrong title", "Dr", professionalUser.getTitle());
        assertEquals("Wrong role", "Senior Lecturer", professionalUser.getRole());

        assertEquals("Wrong email", "marklittle@nhs.net", professionalUser.getEmail());

        // Username should have been set
        //assertNotNull("Username is null", professionalUser.getUsernameHash());

        // Password should have been set
        //assertNotNull("Password hash is null", professionalUser.getPasswordHash());
    }

    @Test
    public void testGetUnknownProfessionalUser() {
        // Get an unknown user
        ProfessionalUser professionalUser = userDao.getProfessionalUser("no@no.com");
        assertNull("Unknown user isn't null", professionalUser);
    }
    
    @Test
    public void testGetProfessionalUsers() {
        List<ProfessionalUser> professionalUsers = userDao.getProfessionalUsers(new ProfessionalUserFilter(), -1, -1);
        assertNotNull(professionalUsers);
        assertTrue(professionalUsers.size() > 0);
    }

    @Test
    public void testGetProfessionalUsersPage1() {
        List<ProfessionalUser> professionalUsers = userDao.getProfessionalUsers(new ProfessionalUserFilter(), 1, 1);
        assertNotNull(professionalUsers);
        assertTrue(professionalUsers.size() == 1);
    }
    
//    @Test
//    public void testSearchProfessionalUsers() {
//        ProfessionalUserFilter userFilter = new ProfessionalUserFilter();
//        userFilter.addSearchCriteria(ProfessionalUserFilter.UserField.FORENAME.getDatabaseFieldName(), "fiona");
//        List<ProfessionalUser> professionalUsers = userDao.getProfessionalUsers(userFilter, -1, -1);
//        assertNotNull(professionalUsers);
//        assertTrue(professionalUsers.size() > 0);
//    }
    
    @Test
    public void outputLoginDetails() {

        LOGGER.info("Login details for test db only");

        // super user
        String email = "hugh.mccarthy@UHBristol.nhs.uk";
        ProfessionalUser professionalUser = userDao.getProfessionalUser(email);
        try {
            //String password = TripleDes.decrypt(professionalUser.getPasswordHash());
            //LOGGER.info("super user | email: " + email + " | password: " + password);
        } catch (Exception e) {
            LOGGER.error(e.toString());
        }
    }

}
