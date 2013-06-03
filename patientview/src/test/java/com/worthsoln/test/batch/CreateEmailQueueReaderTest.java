package com.worthsoln.test.batch;

import com.worthsoln.batch.CreateEmailQueueReader;
import com.worthsoln.patientview.model.*;
import com.worthsoln.patientview.model.enums.GroupEnum;
import com.worthsoln.patientview.model.enums.SendEmailEnum;
import com.worthsoln.service.EmailQueueManager;
import com.worthsoln.service.JobManager;
import com.worthsoln.test.helpers.ServiceHelpers;
import com.worthsoln.test.service.BaseServiceTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 *
 */
public class CreateEmailQueueReaderTest extends BaseServiceTest {

    @Autowired
    private CreateEmailQueueReader createEmailQueueReader;

    @Inject
    private ServiceHelpers serviceHelpers;

    @Inject
    private JobManager jobManager;

    @Inject
    private EmailQueueManager emailQueueManager;


    @Test
    public void testRead() throws Exception {
        Specialty specialty1 = serviceHelpers.createSpecialty("Specialty 1", "Specialty1", "Test description");
        User adminUser = serviceHelpers.createUserWithMapping("adminuser", "test@admin.com", "p", "Admin", "unitA", "nhs1", specialty1);
        User user1 = serviceHelpers.createUserWithMapping("testname1", "test1@admin.com", "p", "test1", "unitA", "nhstest1", specialty1);
        User user2 = serviceHelpers.createUserWithMapping("testname2", "test2@admin.com", "p", "test2", "unitA", "nhstest2", specialty1);
        User user3 = serviceHelpers.createUserWithMapping("testname3-GP", "test3@admin.com", "p", "test3", "unitA", "nhstest3", specialty1);
        User user4 = serviceHelpers.createUserWithMapping("testname4", "test4@admin.com", "p", "test4", "unitB", "nhstest4", specialty1);

        Conversation conversation = serviceHelpers.createConversation("Test subject", user1, user2, true);
        Message message = serviceHelpers.createMessage(conversation, user1, user2, "This is a message", true);

        // Add SpecialtyUserRole
        serviceHelpers.createSpecialtyUserRole(specialty1, adminUser, "unitadmin");
        serviceHelpers.createSpecialtyUserRole(specialty1, user1, "patient");
        serviceHelpers.createSpecialtyUserRole(specialty1, user2, "patient");
        serviceHelpers.createSpecialtyUserRole(specialty1, user3, "patient");
        serviceHelpers.createSpecialtyUserRole(specialty1, user4, "patient");

        Job job = new Job();
        job.setCreator(adminUser);
        job.setGroupEnum(GroupEnum.ALL_PATIENTS);
        job.setSpecialty(specialty1);
        job.setCreated(new Date());
        job.setMessage(message);
        job.setStatus(SendEmailEnum.PENDING);
        jobManager.save(job);

        List<Job> jobs = jobManager.getJobList(SendEmailEnum.PENDING);
        assertEquals("Wrong number of job list", 1, jobs.size());

        createEmailQueueReader.refresh(jobs);

        List<Object> checkEmailQueues = createEmailQueueReader.getList();

        assertEquals("Wrong number of EmailQueue list", 2, checkEmailQueues.size());

        assertTrue("Job not stored", ((EmailQueue)checkEmailQueues.get(0)).getJob().getId() == job.getId());
        assertTrue("Job not stored", ((EmailQueue)checkEmailQueues.get(1)).getJob().getId() == job.getId());
        assertTrue("Message not stored", ((EmailQueue)checkEmailQueues.get(0)).getMessage().getId() == message.getId());
        assertTrue("Message not stored", ((EmailQueue)checkEmailQueues.get(1)).getMessage().getId() == message.getId());
        assertTrue("User 1 not stored", ((EmailQueue)checkEmailQueues.get(0)).getRecipient().getId() == user1.getId());
        assertTrue("User 2 not stored", ((EmailQueue)checkEmailQueues.get(1)).getRecipient().getId() == user2.getId());

    }

}
