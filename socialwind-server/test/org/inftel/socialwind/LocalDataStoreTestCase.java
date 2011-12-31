package org.inftel.socialwind;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

import junit.framework.TestCase;

public abstract class LocalDataStoreTestCase extends TestCase {

    /** Helper para AppEngine Local Services **/
    LocalServiceTestHelper helper = new LocalServiceTestHelper(
            new LocalDatastoreServiceTestConfig());

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        helper.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        helper.tearDown();
    }

}
