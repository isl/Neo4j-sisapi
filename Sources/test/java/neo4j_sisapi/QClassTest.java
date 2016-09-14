/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neo4j_sisapi;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;

/**
 *
 * @author Elias
 */
public class QClassTest {
    
    private String getDatabasePath(){
        ClassLoader classLoader = getClass().getClassLoader();
        File file = null;
        try{
            file = new File(classLoader.getResource("Database").getFile());
        }
        catch(Exception ex){
            ex.printStackTrace(System.out);
            fail("\r\nCould not find Database folder under test/resources folder\r\n");            
        }
        if(file==null){
            fail("\r\nCould not find Database folder under test/resources folder\r\n");            
        }
        return file.getAbsolutePath();
    }
    //Small sample - test database
    //Neo4j_Id:5474Logicalname:AATDEMOClass`Styles and Periods //Hierarchy with 474 instances
    
    //large sample - test database
    //Neo4j_Id:10829Logicalname:AATDEMOClass`Styles and Periods //Hierarchy with 4358 instances
    
    // <editor-fold defaultstate="collapsed" desc="Empty Auto generated Code">
    public QClassTest() {
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
    //</editor-fold>
    
    //Following answerer list we present tests about
    /*
    (1) LIST GENERAL COMMANDS
    (2) LIST SIMPLE QUERY COMMANDS
    (3) LIST TRANSITIVE QUERY COMMANDS
    (4) LIST SET OPERATION COMMANDS
    (5) LIST SET FILTERING - STRING MATCHING COMMANDS
    (6) LIST UPDATE COMMANDS
    (7) LIST RETURN INFO COMMANDS
    */
    
    
    // <editor-fold defaultstate="collapsed" desc="(1) LIST GENERAL COMMANDS">
    // <editor-fold defaultstate="collapsed" desc="commands list">
    /*
    (1) LIST GENERAL COMMANDS
    (bgnq) Begin Query
    (endq) End Query
    NOT IMPLEMENTED: (rq  ) Reset Query
    (rns ) Reset Name Scope
    NOT IMPLEMENTED: (pns ) Pop Name Scope
    (scn ) Set Current Node
    (scni) Set Current Node Id
    NOT IMPLEMENTED: (sncn) Set New Current Node
    
    ADDED: open_connection
    ADDED: close_connection
    ADDED: create_SIS_CS_Session
    ADDED: release_SIS_Session
    */
    // </editor-fold>
    
    /**
     * Test of TEST_begin_query method, of class QClass.     
    @Test
    public void testTEST_begin_query() {
    }*/

    /**
     * Test of TEST_end_query method, of class QClass.     
    @Test
    public void testTEST_end_query() {
    }*/

    
    /**
     * Test of reset_name_scope method, of class QClass.    
    @Test
    public void testReset_name_scope() {
    }*/
    
    /**
     * Test of set_current_node method, of class QClass.     
    @Test
    public void testSet_current_node() {
    }*/
    
    /**
     * Test of set_current_node_id method, of class QClass.    
    @Test
    public void testSet_current_node_id() {
    }*/
    
    /**
     * Test of TEST_open_connection method, of class QClass.
    @Test
    public void testTEST_open_connection() {
    }*/

    /**
     * Test of TEST_close_connection method, of class QClass.
    @Test
    public void testTEST_close_connection() {
    }*/

    /**
     * Test of TEST_create_SIS_CS_Session method, of class QClass.
    @Test
    public void testTEST_create_SIS_CS_Session() {
    }*/

    /**
     * Test of TEST_release_SIS_Session method, of class QClass.    
    @Test
    public void testTEST_release_SIS_Session() {
    }*/
    
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="(2) LIST SIMPLE QUERY COMMANDS">
    // <editor-fold defaultstate="collapsed" desc="commands list">
    /*
    (2) LIST SIMPLE QUERY COMMANDS
    (gc  ) Get Classes
    (gac ) Get All Classes
    (gSc ) Get System Class
    (gaSc) Get All System Classes
    (gi  ) Get Instances
    (gai ) Get All Instances
    (gI ) Get Class Attributes Of
    (gaI ) Get All Class Attributes Of
    (gsc ) Get Super Classes
    (gasc) Get All Super Classes
    (gSsc) Get All Sys Super Classes
    (gsbc) Get Sub Classes
    (gasb) Get All Sub Classes
    (glf ) Get Link From
    (gLf ) Get Class Attributes From
    (giLf ) Get Class Attributes From
    (gilf) Get Inherited Link From
    (gilt) Get Inherited Link To
    (glt ) Get Link To
    (gcf ) Get Category From
    (gct ) Get Category To
    (glfc) Get Link From By Category
    (glfm) Get Link From By Meta Category
    (gltc) Get Link To By Category
    (gltm) Get Link To By Meta Category
    (gclf) Get Category Of Link From
    (gtn ) Get To Node
    (gfn ) Get From Node
    (gtnc) Get To Node By Category
    (gfnc) Get From Node By Category
    (gtnm) Get To Node By Meta Category
    (gfnm) Get From Node By Meta Category
    (gfv ) Get From Value
    (gtv ) Get To Value
    */
    // </editor-fold>
    
    /**
     * Test of get_instances method, of class QClass.
     */
    @Test
    public void testGet_instances() {
        
        
        //"C:\\Projects\\THEMAS_DB_Folder\\Database"
        String targetNodeName =  "AATDEMOClass`Styles and Periods";
        long expNodeNameId = 5474;//10829
        int  expInstancesCount  = 474;
        
        /*
        //"C:\\Users\\Elias\\BackupFiles\\Documents\\Neo4j\\default.graphdb"
        String targetNodeName =  "AATDEMOClass`Styles and Periods";
        long expNodeNameId = 10829;
        int  expInstancesCount  = 4358;
        */
        
        
        
        GraphDatabaseService graphDb = new GraphDatabaseFactory()
                        .newEmbeddedDatabaseBuilder(getDatabasePath())
                        //.setConfig(GraphDatabaseSettings.allow_store_upgrade, "true")
                        //.setConfig(GraphDatabaseSettings.keep_logical_logs, "2000k txs")
                        .setConfig(GraphDatabaseSettings.keep_logical_logs, "false")
                        //.setConfig(GraphDatabaseSettings.pagecache_memory, "512000000")                        
                        //.setConfig(GraphDatabaseSettings.cache_type, "none")
                        .newGraphDatabase();
        
        QClass Q = new QClass();        
        
        long ret = Q.TEST_create_SIS_CS_Session(graphDb);
        if(ret==QClass.APIFail){
            fail("testGet_instances failed @ TEST_create_SIS_CS_Session");
        }
        ret = Q.TEST_open_connection();
        if(ret==QClass.APIFail){
            fail("testGet_instances failed @ TEST_open_connection");
        }
        ret = Q.TEST_begin_query();
        if(ret==QClass.APIFail){
            fail("testGet_instances failed @ TEST_begin_query");
        }        
                
        
        long result = Q.set_current_node(new StringObject(targetNodeName));
        
        int set = Q.get_instances(0);
        if(set==QClass.APIFail){
            fail("testGet_instances failed @ get_instances(0)");
        }
        int cardSet = Q.set_get_card(set);
        if(cardSet==QClass.APIFail){
            fail("testGet_instances failed @ set_get_card");
        }
        //System.out.println("cardinality: " + cardSet);
        ret = Q.TEST_end_query();
        if(ret==QClass.APIFail){
            fail("testGet_instances failed @ TEST_end_query");
        }
        ret = Q.TEST_close_connection();
        if(ret==QClass.APIFail){
            fail("testGet_instances failed @ TEST_close_connection");
        }

        ret = Q.TEST_release_SIS_Session();
        if(ret==QClass.APIFail){
            fail("testGet_instances failed @ TEST_release_SIS_Session");
        }
        
        assertEquals(expNodeNameId, result);        
        assertEquals(expInstancesCount, cardSet);   
        
    }
    
    
    /**
     * Test of get_link_from method, of class QClass.
     */
    @Test
    public void testGet_link_from() {
    }

    /**
     * Test of get_link_to method, of class QClass.
     */
    @Test
    public void testGet_link_to() {
    }

    /**
     * Test of get_from_node method, of class QClass.
     */
    @Test
    public void testGet_from_node() {
    }

    /**
     * Test of get_to_node method, of class QClass.
     */
    @Test
    public void testGet_to_node() {
    }

    /**
     * Test of get_from_value method, of class QClass.
     */
    @Test
    public void testGet_from_value() {
    }

    /**
     * Test of get_to_value method, of class QClass.
     */
    @Test
    public void testGet_to_value() {
    }

    /**
     * Test of get_inher_link_from method, of class QClass.
     */
    @Test
    public void testGet_inher_link_from() {
    }

    /**
     * Test of get_inher_link_to method, of class QClass.
     */
    @Test
    public void testGet_inher_link_to() {
    }

    /**
     * Test of get_all_instances method, of class QClass.
     */
    @Test
    public void testGet_all_instances() {
    }

    /**
     * Test of get_classes method, of class QClass.
     */
    @Test
    public void testGet_classes() {
    }

    /**
     * Test of get_all_classes method, of class QClass.
     */
    @Test
    public void testGet_all_classes() {
    }

    /**
     * Test of get_subclasses method, of class QClass.
     */
    @Test
    public void testGet_subclasses() {
    }

    /**
     * Test of get_all_subclasses method, of class QClass.
     */
    @Test
    public void testGet_all_subclasses() {
    }

    /**
     * Test of get_superclasses method, of class QClass.
     */
    @Test
    public void testGet_superclasses() {
    }

    /**
     * Test of get_all_superclasses method, of class QClass.
     */
    @Test
    public void testGet_all_superclasses() {
    }
    
    /**
     * Test of get_from_node_by_category method, of class QClass.
     */
    @Test
    public void testGet_from_node_by_category() {
    }

    /**
     * Test of get_to_node_by_category method, of class QClass.
     */
    @Test
    public void testGet_to_node_by_category() {
    }

    /**
     * Test of get_link_from_by_category method, of class QClass.
     */
    @Test
    public void testGet_link_from_by_category() {
    }

    /**
     * Test of get_link_from_by_meta_category method, of class QClass.
     */
    @Test
    public void testGet_link_from_by_meta_category() {
    }

    /**
     * Test of get_link_to_by_meta_category method, of class QClass.
     */
    @Test
    public void testGet_link_to_by_meta_category() {
    }

    /**
     * Test of get_link_to_by_category method, of class QClass.
     */
    @Test
    public void testGet_link_to_by_category() {
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="(3) LIST TRANSITIVE QUERY COMMANDS">
    // <editor-fold defaultstate="collapsed" desc="commands list">
    /*
    (3) LIST TRANSITIVE QUERY COMMANDS
    (sc  ) Set Categories
    (sdep) Set Depth
    (tc  ) Traverse by Category
    (tmc ) Traverse by Meta Category
    (stvc) Set To Value Condition
    (sfvc) Set From Value Condition
    (stlc) Set To Link Condition
    (sflc) Set From Link Condition
    (tal ) Traverse by All Links
    (tall) General Traverse All Links
    */
    // </editor-fold>
    
    /**
     * Test of set_categories method, of class QClass.
     */
    @Test
    public void testSet_categories() {
    }
    
    /**
     * Test of set_depth method, of class QClass.
     */
    @Test
    public void testSet_depth() {
    }
    
    /**
     * Test of get_traverse_by_category method, of class QClass.
     */
    @Test
    public void testGet_traverse_by_category() {
    }

    /**
     * Test of FOR_DELETE_get_traverse_by_category_With_SIS_Server_Implementation method, of class QClass.
     */
    @Test
    public void testFOR_DELETE_get_traverse_by_category_With_SIS_Server_Implementation() {
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="(4) LIST SET OPERATION COMMANDS">
    // <editor-fold defaultstate="collapsed" desc="commands list">
    /*
    (4) LIST SET OPERATION COMMANDS
    (fs  ) Free Set
    (fas ) Free All Sets
    (sgn ) Set Get New
    (sgc ) Set Get Cardinality

    (su  ) Set Union
    (scpy) Set Copy
    (si  ) Set Intersect
    (sd  ) Set Difference
    ADDED: Set_disjoint
    (sput) Set Put
    (sppr) Set Put Primitive
    NOT IMPLEMENTED: (sdel) Set Delete (removes everything from the set - clear?)
    (smo ) Set Member Of
    ADDED: reset_set
    */
    
    /**
     * Test of free_set method, of class QClass.    
    @Test
    public void testFree_set() {
    } */

    /**
     * Test of free_all_sets method, of class QClass.    
    @Test
    public void testFree_all_sets() {
    } */
    
    /**
     * Test of set_get_new method, of class QClass.    
    @Test
    public void testSet_get_new() {
    } */

    /**
     * Test of set_get_card method, of class QClass.    
    @Test
    public void testSet_get_card() {
    } */

    /**
     * Test of set_union method, of class QClass.
     */
    @Test
    public void testSet_union() {
    }
    
    /**
     * Test of set_copy method, of class QClass.
     */
    @Test
    public void testSet_copy() {
    }
    
    /**
     * Test of set_intersect method, of class QClass.
     */
    @Test
    public void testSet_intersect() {
    }
    
    /**
     * Test of set_difference method, of class QClass.
     */
    @Test
    public void testSet_difference() {
    }
    
    /**
     * Test of set_disjoint method, of class QClass.
     */
    @Test
    public void testSet_disjoint() {
    }
    
    /**
     * Test of set_put method, of class QClass.
     */
    @Test
    public void testSet_put() {
    }

    /**
     * Test of set_put_prm method, of class QClass.
     */
    @Test
    public void testSet_put_prm() {
    }

    /**
     * Test of set_member_of method, of class QClass.
     */
    @Test
    public void testSet_member_of() {
    }

    /**
     * Test of reset_set method, of class QClass.     
    @Test
    public void testReset_set() {
    }*/
    // </editor-fold>
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="(5) LIST SET FILTERING - STRING MATCHING COMMANDS">
    // <editor-fold defaultstate="collapsed" desc="commands list">
    /*
    (5) LIST SET FILTERING - STRING MATCHING COMMANDS
    (sfc ) Set Filter Condition
    (gf  ) Get a Filtered set
    (gm  ) Get a Matched set
    (gmci) Get a Matched set (case insensitive)
    (gmpr) Get a Matched String
    (gplt) Get Less Than String
    (gple) Get Greater Than String
    (gpeq) Get Equal String
    (gpne) Get Not Equal String
    */
    // </editor-fold>
    
    /**
     * Test of get_matched method, of class QClass.
     */
    @Test
    public void testGet_matched() {
    }

    /**
     * Test of get_matched_string method, of class QClass.
     */
    @Test
    public void testGet_matched_string() {
    }

    /**
     * Test of get_matched_ToneAndCaseInsensitive method, of class QClass.
     */
    @Test
    public void testGet_matched_ToneAndCaseInsensitive() {
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="(6) LIST UPDATE COMMANDS">
    // <editor-fold defaultstate="collapsed" desc="commands list">
    /*
    (6) LIST UPDATE COMMANDS
    (bgnt) Begin Transaction
    (endt) End Transaction
    (abrt) Abort Transaction
    (an  ) Add Node
    (dn  ) Delete Node
    (rn  ) Rename Node
    (ana ) Add Named Attribute
    (dna ) Delete Named Attribute
    (rna ) Rename Named Attribute
    (cnat) Change Named Attribute To
    (aua ) Add Unnamed Attribute
    (dua ) Delete Unnamed Attribute
    (cuat) Change Unnamed Attribute To
    (ain ) Add Instance
    (din ) Delete Instance
    (cint) Change Instance To
    (aisa) Add ISA
    (disa) Delete ISA
    (cit ) Change ISA To
    */
    // </editor-fold>
    
    
    /**
     * Test of CHECK_Add_Node method, of class QClass.
     */
    @Test
    public void testCHECK_Add_Node() {
    }

    /**
     * Test of CHECK_Add_Named_Attribute method, of class QClass.
     */
    @Test
    public void testCHECK_Add_Named_Attribute() {
    }

    /**
     * Test of CHECK_Add_Unnamed_Attribute method, of class QClass.
     */
    @Test
    public void testCHECK_Add_Unnamed_Attribute() {
    }

    /**
     * Test of CHECK_Add_Instance method, of class QClass.
     */
    @Test
    public void testCHECK_Add_Instance() {
    }

    /**
     * Test of CHECK_IMPROVE_Add_Instance_Set method, of class QClass.
     */
    @Test
    public void testCHECK_IMPROVE_Add_Instance_Set() {
    }

    /**
     * Test of CHECK_Add_IsA method, of class QClass.
     */
    @Test
    public void testCHECK_Add_IsA() {
    }

    /**
     * Test of NOT_USED_CHECK_Change_Instance_To method, of class QClass.
     */
    @Test
    public void testNOT_USED_CHECK_Change_Instance_To() {
    }

    /**
     * Test of NOT_USED_CHECK_Change_IsA_To method, of class QClass.
     */
    @Test
    public void testNOT_USED_CHECK_Change_IsA_To() {
    }

    /**
     * Test of CHECK_Delete_Node method, of class QClass.
     */
    @Test
    public void testCHECK_Delete_Node() {
    }

    /**
     * Test of CHECK_Delete_Named_Attribute method, of class QClass.
     */
    @Test
    public void testCHECK_Delete_Named_Attribute() {
    }

    /**
     * Test of CHECK_Delete_Unnamed_Attribute method, of class QClass.
     */
    @Test
    public void testCHECK_Delete_Unnamed_Attribute() {
    }

    /**
     * Test of CHECK_Delete_Instance method, of class QClass.
     */
    @Test
    public void testCHECK_Delete_Instance() {
    }

    /**
     * Test of CHECK_IMPROVE_Delete_Instance_Set method, of class QClass.
     */
    @Test
    public void testCHECK_IMPROVE_Delete_Instance_Set() {
    }

    /**
     * Test of CHECK_Delete_IsA method, of class QClass.
     */
    @Test
    public void testCHECK_Delete_IsA() {
    }

    /**
     * Test of CHECK_Rename_Node method, of class QClass.
     */
    @Test
    public void testCHECK_Rename_Node() {
    }

    /**
     * Test of CHECK_Rename_Named_Attribute method, of class QClass.
     */
    @Test
    public void testCHECK_Rename_Named_Attribute() {
    }

    
    /**
     * Test of TEST_begin_transaction method, of class QClass.
     */
    @Test
    public void testTEST_begin_transaction() {
    }

    /**
     * Test of TEST_end_transaction method, of class QClass.
     */
    @Test
    public void testTEST_end_transaction() {
    }

    /**
     * Test of TEST_abort_transaction method, of class QClass.
     */
    @Test
    public void testTEST_abort_transaction() {
    }

    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="(7) LIST RETURN INFO COMMANDS">
    // <editor-fold defaultstate="collapsed" desc="commands list">
    /*
    (7) LIST RETURN INFO COMMANDS
    (rn  ) Return Nodes
    (ren ) Return Edge Nodes
    (rfn ) Return Full Nodes
    (rprs) Return Parser Values
    (rc  ) Return Categories
    (rci ) Return Category Identifiers
    (rl  ) Return Link
    (rli ) Return Link Ids
    (rfl ) Return Full Link
    (rfli) Return Full Link Ids
    (risa) Return isA pairs
    (risi) Return isA pairs and Ids
    (rins) Return Instance pairs
    (rini) Return Instance pairs Ids
    (rf  ) Return Field
    (rhl ) Return Hidden Links
    (rxml) Return XML Description
    (spc1) Set 1st Projection Condition
    (spc2) Set 2nd Projection Condition
    (spc3) Set 3rd Projection Condition
    (spc4) Set 4th Projection Condition
    (snp ) Set Number of Projections
    (rp  ) Return Projection
    */
    // </editor-fold>
    
    /**
     * Test of bulk_return_prm method, of class QClass.
     */
    @Test
    public void testBulk_return_prm() {
    }
    
    /**
     * Test of return_prm method, of class QClass.
     */
    @Test
    public void testReturn_prm() {
    }

    /**
     * Test of return_nodes method, of class QClass.
     */
    @Test
    public void testReturn_nodes() {
    }
    
    /**
     * Test of bulk_return_nodes method, of class QClass.
     */
    @Test
    public void testBulk_return_nodes() {
    }
    
    /**
     * Test of return_full_nodes method, of class QClass.
     */
    @Test
    public void testReturn_full_nodes() {
    }
    
    /**
     * Test of bulk_return_full_nodes method, of class QClass.
     */
    @Test
    public void testBulk_return_full_nodes() {
        
    }

    /**
     * Test of return_link method, of class QClass.
     */
    @Test
    public void testReturn_link() {
    }

    /**
     * Test of return_link_id method, of class QClass.
     */
    @Test
    public void testReturn_link_id() {
    }
    
    /**
     * Test of bulk_return_link method, of class QClass.
     */
    @Test
    public void testBulk_return_link() {
    }

    /**
     * Test of bulk_return_link_id method, of class QClass.
     */
    @Test
    public void testBulk_return_link_id() {
    }
    
    /**
     * Test of return_full_link method, of class QClass.
     */
    @Test
    public void testReturn_full_link() {
    }

    /**
     * Test of bulk_return_full_link method, of class QClass.
     */
    @Test
    public void testBulk_return_full_link() {
    }
    
    /**
     * Test of return_full_link_id method, of class QClass.
     */
    @Test
    public void testReturn_full_link_id() {
    }
    
    /**
     * Test of bulk_return_full_link_id method, of class QClass.
     */
    @Test
    public void testBulk_return_full_link_id() {
    }

    /**
     * Test of return_isA method, of class QClass.
     */
    @Test
    public void testReturn_isA() {
    }
    /**
     * Test of bulk_return_isA method, of class QClass.
     */
    @Test
    public void testBulk_return_isA() {
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Other - Utils">
    
    
    // <editor-fold defaultstate="collapsed" desc="Low Level">
    /**
     * Test of get_classid method, of class QClass.     
    @Test
    public void testGet_classid() {
    }*/
        
    /**
     * Test of get_linkid method, of class QClass.    
    @Test
    public void testGet_linkid() {
    }*/
    
    /**
     * Test of CHECK_isUnNamedLink method, of class QClass.     
    @Test
    public void testCHECK_isUnNamedLink() {
    }*/    
    
    /**
     * Test of get_loginam method, of class QClass.     
    @Test
    public void testGet_loginam() {
    }*/
    
    /**
     * Test of get_error_message method, of class QClass.     
    @Test
    public void testGet_error_message() {
    }*/
    
    /**
     * Test of reset_error_message method, of class QClass.     
    @Test
    public void testReset_error_message() {
    }*/
    
    
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Other commands list">
    /*
    Utility functions implemented for comparison with old api
    
    TEST_printIdsOfSet
    TEST_get_SYSIDS_of_set
    TEST_get_SysId_From_Neo4jId
    */
    // </editor-fold>
    /**
     * Test of TEST_printIdsOfSet method, of class QClass.
     */
    @Test
    public void testTEST_printIdsOfSet() {
    }

    /**
     * Test of TEST_get_SYSIDS_of_set method, of class QClass.
     */
    @Test
    public void testTEST_get_SYSIDS_of_set() {
    }

    /**
     * Test of TEST_get_SysId_From_Neo4jId method, of class QClass.
     */
    @Test
    public void testTEST_get_SysId_From_Neo4jId() {
    }
    // </editor-fold>
    

    

    
}
