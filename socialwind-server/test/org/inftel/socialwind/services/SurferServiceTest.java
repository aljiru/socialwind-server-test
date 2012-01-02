package org.inftel.socialwind.services;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SurferServiceTest {
    
    /** Helper para AppEngine Local Services **/
    LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() throws Exception {
        helper.setUp();
    }

    @After
    public void tearDown() throws Exception {
        helper.tearDown();
    }

    @Test
    public final void testEntityManager() {
        assertNotNull(SurferService.entityManager());
    }

    @Test
    public final void testCountSurfers() {
        Integer expected = SurferService.findAllSurfers().size();
        Integer actual = SurferService.countSurfers().intValue();
        assertEquals(expected, actual);
    }

    @Test
    public final void testFindAllSurfers() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testFindSurfer() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testFindSurferEntries() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testPersist() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testRemove() {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public final void testSetSurferLocation() {
        fail("Not yet implemented"); // TODO
    }

}
