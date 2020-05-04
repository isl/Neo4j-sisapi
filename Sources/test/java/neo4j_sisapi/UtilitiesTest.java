/*
 * Copyright 2015 Institute of Computer Science,
 *                Foundation for Research and Technology - Hellas.
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 *      http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations
 * under the Licence.
 * 
 * =============================================================================
 * Contact: 
 * =============================================================================
 * Address: N. Plastira 100 Vassilika Vouton, GR-700 13 Heraklion, Crete, Greece
 *     Tel: +30-2810-391632
 *     Fax: +30-2810-391638
 *  E-mail: isl@ics.forth.gr
 * WebSite: https://www.ics.forth.gr/isl/centre-cultural-informatics
 * 
 * =============================================================================
 * Authors: 
 * =============================================================================
 * Elias Tzortzakakis <tzortzak@ics.forth.gr>
 * 
 * This file is part of the Neo4j-sisapi api.
 */
package neo4j_sisapi;

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Elias
 */
public class UtilitiesTest {
    
    public UtilitiesTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of removePrefix method, of class Utilities.
     
    @Test
    public void testRemovePrefix() {
        System.out.println("removePrefix");
        String s = "";
        Utilities instance = new Utilities();
        String expResult = "";
        String result = instance.removePrefix(s);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
*/
    /**
     * Test of collectSequenctiallyAsubsetOfValues method, of class Utilities.
     
    @Test
    public void testCollectSequenctiallyAsubsetOfValues() {
        System.out.println("collectSequenctiallyAsubsetOfValues");
        int startindex = 0;
        int howmanyToGet = 0;
        ArrayList<Long> targetVals = null;
        Utilities instance = new Utilities();
        ArrayList<Long> expResult = null;
        ArrayList<Long> result = instance.collectSequenctiallyAsubsetOfValues(startindex, howmanyToGet, targetVals);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
*/
    /**
     * Test of handleException method, of class Utilities.
     
    @Test
    public void testHandleException() {
        System.out.println("handleException");
        Exception ex = null;
        Utilities instance = new Utilities();
        instance.handleException(ex);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
*/
    /**
     * Test of prepareStringForCypher method, of class Utilities.

    @Test
    public void testPrepareStringForCypher() {
        System.out.println("prepareStringForCypher");
        String searchVal = "";
        Utilities instance = new Utilities();
        String expResult = "";
        String result = instance.prepareStringForCypher(searchVal);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
     */
    /**
     * Test of prepareStringForCypherLikeClaues method, of class Utilities.
     */
    @Test
    public void testPrepareStringForCypherLikeClaues() {
        System.out.println("prepareStringForCypherLikeClaues");
        String searchVal = "(";
        Utilities instance = new Utilities();
        String expResult = "\\\\(";
        String result = instance.prepareStringForCypherLikeClaues(searchVal);
        assertEquals(expResult, result);
        
    }
    
}
