package com.karelherink.flickrsearch.util;

import junit.framework.TestCase;

public class UtilsTest extends TestCase {

    public void testToCommaSeparated() throws Exception {
        assertEquals("", Utils.toCommaSeparated("   "));
        assertEquals("a", Utils.toCommaSeparated(" a  "));
        assertEquals("a,b", Utils.toCommaSeparated(" a   \t b   "));
        assertEquals("abc", Utils.toCommaSeparated("abc"));

        try {
            Utils.toCommaSeparated(null);
            fail();
        }
        catch (NullPointerException npe) {
            //expected
        }
        catch (Throwable t) {
            fail();
        }
    }

}
