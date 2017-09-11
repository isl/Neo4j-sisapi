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
 * WebSite: http://www.ics.forth.gr/isl/cci.html
 * 
 * =============================================================================
 * Authors: 
 * =============================================================================
 * Elias Tzortzakakis <tzortzak@ics.forth.gr>
 * 
 * This file is part of the Neo4j-sisapi api.
 */
package neo4j_sisapi;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.TransactionTerminatedException;
import org.neo4j.graphdb.schema.ConstraintDefinition;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;


/**
 * ATTENTION!!!
 * Different behavior than expected for 
 * Telos_Object, Telos_Object->attribute, and Individual
 * 
 * <br/>
 * NOTE: Instead of return_xxx functions use bulk_return_xxx
 * <br/> 
 * 
 * @author Elias Tzortzakakis <tzortzak@ics.forth.gr>
 */

public class QClass {
    
    public static final int SIS_API_TOKEN_CLASS = 9;
    public static final int SIS_API_S_CLASS = 8;
    public static final int SIS_API_M1_CLASS = 7;
    public static final int SIS_API_M2_CLASS = 6;
    public static final int SIS_API_M3_CLASS = 5;
    public static final int SIS_API_M4_CLASS = 4;
    
    public final static int STRING_LESS_EQUAL = 0;
    public final static int STRING_LESS_THAN = 1;
    public final static int STRING_EQUAL = 2;
    public final static int STRING_NOT_EQUAL = 3;
    public final static int STRING_MATCHED = 4;

    //<editor-fold defaultstate="collapsed" desc="C++ declarations">
    
    
    /*    
    // The following indicate a function success or failure
     public final static int API_DB_CHANGED = 0;
     public final static int API_DB_NOT_CHANGED = 1;

     // The following values are only returned by begin_query, begin_transaction in order to produce
     // a warning from gain, ef about the need of expanding the system hash tables of-line.
     public final static int API_HASH_TABLES_NEED_EXPANSION = 0x04; // Returned only by begin_query, begin_transaction
     public final static int API_HASH_TABLES_EXPANDING = 0x08; // Returned only by begin_query, begin_transaction - waiting

     // The following values are only returned by return_field.
     public final static int END_OF_SET = 0;  // End of answer set, EMPTY_FIELD as well 

     public final static int END_OF_FIELD = 1;
     public final static int END_OF_TUPLE = 2;
     public final static int EMPTY_FIELD = 3; // END_OF_FIELD as well 

     public final static int MIDDLE_OF_FIELD = 4;
     public final static int EMPTY_FIELD_END_OF_TUPLE = 5;

     // API Traversal Values     
     public final static int FORWARD = 1;
     public final static int BACKWARD = 2;
     public final static int BOTH_DIR = 3;

    

     /*
     public final static int NOISA = -1;  // should not be >=0  

     public final static int UPWARDS = 1;
     public final static int DOWNWARDS = 2;
     public final static int UP_DOWN = 3;
     */
    /*
     get_matched_string function match_type argument
    
     public final static int STRING_LESS_EQUAL = 0;
     public final static int STRING_LESS_THAN = 1;
     public final static int STRING_EQUAL = 2;
     public final static int STRING_NOT_EQUAL = 3;
     public final static int STRING_MATCHED = 4;
     */
    /*
     Add_node and Add...Attribute functions Level argument
    
     public final static int SIS_API_TOKEN_CLASS = 9;
     public final static int SIS_API_S_CLASS = 8;
     public final static int SIS_API_M1_CLASS = 7;
     public final static int SIS_API_M2_CLASS = 6;
     public final static int SIS_API_M3_CLASS = 5;
     public final static int SIS_API_M4_CLASS = 4;
     */
    //</editor-fold> 
    
    semanticChecker  api_sem_check = null;
    //public final static int ERROR = -1;
    
    /**
     * Value returned if an action failed.
     */
    
    private boolean insideQuery  = false;
    private boolean insideTransaction  = false;
    
    public final static int APIFail = -1;
    final static int ERROR = -1;
    /**
     * Value returned if an action completed successfully.
     */
    public final static int APISucc = 0;
    
    //#define TRUE  1
    //#define FALSE 0
    public final static int TRUEval = 1;
    public final static int FALSEval = 0;

    public final static int FORWARD =1;
    public final static int BACKWARD =2;
    public final static int BOTH_DIR =3;

    public static enum Traversal_Direction {

        /**
         * Follow outgoing links
         */
        FORWARD, 
        /**
         * Follow incoming links
         */
        BACKWARD, 
        /**
         * Follow both incoming and outgoing links
         */
        BOTH_DIR
    };

    public static enum Traversal_Isa {

        NOISA, UPWARDS, DOWNWARDS, UP_DOWN
    };

    private Sets_Class tmp_sets = null;//sets_class       tmp_sets;
    
    ApiError globalError = new ApiError();
    
    //Node curNode = null;
    Vector<CategorySet> currentCategories = new Vector<CategorySet>();

    //SYSID current_node;
    //Vector<Node> nodeStack = new Vector<Node>();
    //Vector<Neo4j_Id> propertyContainerStack = new Vector<Neo4j_Id>();
    
    DBaccess db = null;
    Utilities utils = new Utilities();
    Vector<Long> CurrentNode_Ids_Stack = new Vector<Long>();

    
    
    

    
    
    //SYSID categories[NUMBER_OF_CATEGORIES];
    //int   directions[NUMBER_OF_CATEGORIES];

    Vector<Long> categories = new Vector<Long>();
    Vector<Traversal_Direction> directions = new Vector<Traversal_Direction> ();
    //SET   forward_set;   // store forward categs and subclasses
    //SET   backward_set;  // store backward categes and subclasses
    PQI_Set forward_set = new PQI_Set();
    PQI_Set backward_set = new PQI_Set();
    
    int depth =-1;  //depth level for traverse queries 
    
    PQI_Set   edge_set = new PQI_Set();      // set of nodes where traversing stopped because of depth control, but could  continue... 
    
    
    /**
     * Constructor of Q class
     */
    public QClass() {
        
        CurrentNode_Ids_Stack = new Vector<Long>();
        tmp_sets = new Sets_Class();

    }
    /**
     * 
     * Given the logical name lname of a node object, the function returns the 
     * system identifier sysid of this object.
     * 
     * @param lname
     * @param sysid
     * @return 
     */
    public int get_classid(StringObject lname, PrimitiveObject_Long sysid){
        //SisServer get_classid returned: 8390956 for Label80092c unnammed A_T
        //SisServer get_classid returned: 0 for of_thesaurus named A_C but not unique logicalname
        //SisServer get_classid returned: 0 for METALA_BT // A_C but with logical name that was unique
        
        
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //  Get sysid of node object with logical name lname.
        //  If there is no object with logical name lname, sysid is 0.
        //  Returns 0 on success, ErrorCode on error.
        int sis_api::get_classid(l_name lname, int *sysid)
        #ifdef CLIENT_SERVER
        {
                CHECK_FILES("get_classid");
                int result;
                // result = RFCache->InvalidateCache(); Not required to invalidate caches
                // here. This is an optimazation to improve client-server performance.
                // if(result < 0) return result;  // Comms error
                if((result=SComm->send_int(  _GET_CLASSID))>0)
                        if((result=SComm->send_str(  lname))>0)
                                if((result=SComm->recv_int(  sysid))>0)
                                        return 0;
                return result;
        }
        #else
        {
                SYSID tmpid;
                LOGINAM tmplname;

                tmplname = LOGINAM(lname);
                tmpid = getSysid(&tmplname);
                *sysid = tmpid.id;

                globalError.reset();  // just in case any error occured.
                return 0;
        }
        #endif
        
        SYSID sis_api::getSysid(LOGINAM *lname)
        {
                return api_translator->getSysid(lname, SYSID());
        }

        // ---------------------------------------------------------------------
	// getSysid -- returns the sysid that have logical name the first name
	//	of full name ln and its from-object have sysid sys. If that sysid
	//	not exist it returns SYSID(0).
	// ---------------------------------------------------------------------
	SYSID	getSysid( LOGINAM *ln, SYSID sys) {
		Loginam	*tmpL;
		SYSID	retVal;
        int id;

		// check added by georgian to support unnamed objects 
		if ( ln == ( LOGINAM * ) 0 ) {
			return SYSID(0);
		}
		if ( (id = ln->unnamed_id()) ) {
// 				printf("getSysid( LOGINAM *ln, SYSID sys) called on unnamed link 0x%x\n",id);
				return (SYSID(id));
//		} else {
		}
			tmpL = inv_convertion(ln);
			retVal = symbolTable->getSysid( tmpL, sys);
			delete	tmpL;
			return( retVal);
	//	}
	}

        */
        // </editor-fold>         
        
        if(!check_files("get_classid")){
            return APIFail;
        }
        
        sysid.setValue(db.getClassId(lname.getValue()));
        if(sysid.getValue() == APIFail){
            sysid.setValue(0);
            return APIFail;
        }
        
        return APISucc;
    }
    
       
    
    /**
     * Given the logical name label of a link object, the function returns the 
     * system identifier sysid of this object. Since the logical name of a link 
     * object may not be unique in the SIS base, the logical name fromcls of the
     * node object the link is pointing from must be given.
     * 
     * @param fromcls
     * @param label
     * @param sysid
     * @return 
     */
    public int get_linkid(StringObject fromcls, StringObject label, PrimitiveObject_Long sysid){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //  Get sysids of link object with logical name label, pointing
        //  from class with logical name fromcls.
        //  If there is no such a link object, sysid is 0.
        //  Returns 0 on success, ErrorCode on error.
        int sis_api::get_linkid(l_name fromcls, l_name label, int *sysid)
        #ifdef CLIENT_SERVER
        {
                CHECK_FILES("get_linkid");
                int result;
                // result = RFCache->InvalidateCache();  Not required to invalidate caches
                // here. This is an optimazation to improve client-server performance.
                // if(result < 0) return result;  // Comms error
                if((result=SComm->send_int(  _GET_LINKID))>0)
                        if((result=SComm->send_str(  fromcls))>0)
                                if((result=SComm->send_str(  label))>0)
                                        if((result=SComm->recv_int(  sysid))>0)
                                                return 0;
                return result;
        }
        #else
        {
                SYSID   tmpid;
                LOGINAM tmpfromcls;
                char   tmplabel[1024];

                strcpy(tmplabel, label);
                tmpfromcls = LOGINAM(fromcls);
                tmpid = getSysid(&tmpfromcls, tmplabel);
                *sysid = tmpid.id;

                globalError.reset();  // just in case any error occured.
                return 0;
        }
        #endif
        */
        // </editor-fold>         
        
        if(!check_files("get_linkid")){
            return APIFail;
        }
        
        sysid.setValue(db.getLinkId(fromcls.getValue(),label.getValue()));
        if(sysid.getValue() == APIFail){
            return APIFail;
        }
        
        return APISucc;
    }
    
    /**
     * Given the system identifier sysid of an object, the function returns 
     * the logical name lname of this object.
     * 
     * @param sysid
     * @param lname
     * @return 
     */
    public int get_loginam(PrimitiveObject_Long sysid, StringObject lname){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold>         
        
        if(!check_files("get_loginam")){
            return APIFail;
        }
        
        return db.getStringPropertyOfNode(sysid.getValue(), db.Neo4j_Key_For_Logicalname, lname);
    }
    
    /**
     * Sets message value to the last error message encountered.
     * @param message
     * @return 
     */
    public int get_error_message(StringObject message){
        
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        int sis_api::get_error_message(char *message)
		#ifdef CLIENT_SERVER
		{
			int result=0;
			CHECK_FILES("get_error_message");
			if((result=SComm->send_int(_GET_ERROR_MESSAGE)) > 0)
				if((result=SComm->recv_str(message)) > 0)
					return 0;
			return -1;
		}
		#else
		{
			globalError.getMessage(message);
			return 0;
		}
		#endif
        */
        // </editor-fold> 
        if(!check_files("get_error_message")){
            return APIFail;
        }
        globalError.getMessage(message);
        return APISucc;
    }
    
    /**
     * Clears the error object of the api.
     * 
     * @return 
     */
    public int reset_error_message(){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        int sis_api::reset_error_message(void)
        #ifdef CLIENT_SERVER
        {
                int result=0;
                CHECK_FILES("reset_error_message");
                if((result=SComm->send_int(_RESET_ERROR_MESSAGE))>0);
                return 0;
                return -1;
        }
        #else
        {
                globalError.reset();
                return 0;
        }
        #endif
        */
        // </editor-fold>         
        
        if(!check_files("reset_error_message")){
            return APIFail;
        }
        globalError.reset();        
        return APISucc;
    }
    
    /**
     * Each set has a pointer that moves along the objects of the set and is 
     * used when we get these objects one-by-one. 
     * This function just sets this pointer to the first item of the set and 
     * its call is necessary before first call of a function that reads the 
     * next object in a set.
     * 
     * @param set_id
     * @return 
     */
    public int reset_set(int set_id){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        return tmp_sets.reset_set(set_id);
        */
        // </editor-fold> 
        if(!check_files("reset_set")){
            return APIFail;
        }
        return tmp_sets.reset_set(set_id);
    }
    
    /**
     * Similar to return_prm but gets all information at once not id per id as 
     * return_prm does for nodes. 
     * Returns a structure that contains everything return_prm would return
     * 
     * @param set_id
     * @param retVals
     * @return 
     */
    public int bulk_return_prm(int set_id, Vector<Return_Prm_Row> retVals){
        if(!check_files("bulk_return_prm")){
            return APIFail;
        }
        PQI_Set requestedSet = this.tmp_sets.return_set(set_id);
        if(requestedSet==null){
            return APIFail;
        }
        Vector<Long> setIds = requestedSet.get_Neo4j_Ids();
        
        retVals.clear();
        if(setIds.size()==0){
            return APISucc;
        }
        if(db.get_Bulk_Return_Prm_Nodes(setIds, retVals)==QClass.APIFail){
            retVals.clear();
            return APIFail;
        }
        //now similar code with return prm in order to collect primitives
        
        requestedSet.set_bulk_get_prs(retVals);
        Collections.sort(retVals);
        return APISucc;
    }
    
    /**
     * Similar to return full nodes but gets all information at once not id per id as 
     * return_full_nodes does. Returns a structure that contains everything return_full_nodes
     * would return
     * 
     * @param set_id
     * @param retVals
     * @return 
     */
    public int bulk_return_full_nodes(int set_id, Vector<Return_Full_Nodes_Row> retVals){
        if(!check_files("bulk_return_full_nodes")){
            return APIFail;
        }
        PQI_Set requestedSet = this.tmp_sets.return_set(set_id);
        if(requestedSet==null){
            return APIFail;
        }
        Vector<Long> setIds = requestedSet.get_Neo4j_Ids();
        
        retVals.clear();
        
        if(setIds.size()==0){
            return APISucc;
        }
        
        if(db.get_Bulk_Return_Full_Nodes_Rows(setIds, retVals)==QClass.APIFail){
            return APIFail;
        }
        
        return APISucc;
    }
    
    
    
    /**
     * An answer set may contain primitive values (integers, strings etc) and 
     * the previous functions would fail to read these items. This function 
     * returns the next object in set set_id whatever it is (node or primitive). 
     * 
     * Attention!!!
     * This function returns firstly the nodes then the integers and then the Strings
     * but there is no sorting inside these groups as there was in sis-server at
     * least for primitives.
     * 
     * @param set_id
     * @param cmv
     * @return 
     */
    public int return_prm(int set_id, CMValue cmv){
        //Followed different approach from C++ do not know what prs_val is
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //  Give next object/primitive value in set set_id.
        //  Returns 0 on success, ErrorCode on error or -1 if end of set reached.
        int sis_api::return_prm(int set_id, cm_value *cmv)
        #ifdef CLIENT_SERVER
        {
                CHECK_FILES("return_prm");
                int result;

                if(PreviousRF != RF_Return_prs)
                {
                        PreviousRF = RF_Return_prs;
                        result = RFCache->InvalidateCache();
                        if(result < 0) return result;  // Comms error
                }

                result = RFCache->ReadCache(RF_Return_prs , set_id, "%cm", cmv);
                return result;
        }
        #else
        {
                int      ret;
                prs_val  prs_value;
                SYSID    toid;

                CHECK_FILES("return_prm");
                ret = return_prs(set_id, &prs_value, &toid);

                if (ret == -1) {
                        return ret;
                }

                ret = copy_prs(&prs_value, cmv, toid.id );

                return ret;
        }
        #endif
        
        //  Give next object/primitive value from set set_id.
        //  Returns 0 on success, -1 when an error occured or end of set
        //  reached.
        int sis_api::return_prs(int set_id, prs_val *prs_value, SYSID *toid)
        {
                SYSID    tmpid;
                SET     *setptr;
                LOGINAM *ln;

                if ((setptr = tmp_sets.return_set(set_id)) == NULL) {
                        return -1;
                }

                if ((tmpid = setptr->set_get_id()).id) {
                        if ((ln = getLoginam(tmpid)) == NULL) {
                                return -1;
                        }
                        *prs_value = prs_val(ln);  // !!! ln should be deleted after use !!!
                        *toid      = tmpid;
                        return 0;
                }

                if ((*prs_value = setptr->set_get_prs()).tag() != UNDEF_TAG)  {
                        toid->id = 0;
                        return 0;
                }
                else {  //  end of set
                        return -1;
                }
        }
        */
        // </editor-fold>         
        
        if(!check_files("return_prm")){
            return APIFail;
        }
        
        PQI_Set setptr = this.tmp_sets.return_set(set_id);
        if(setptr==null){
            return APIFail;
        }
        
        //first search inside set nodes
        long tmpid =setptr.set_get_id();
        if(tmpid!=0){
            StringObject lname = new StringObject();
            int lnRet  = getLoginamString(tmpid, lname);
            if(lnRet==APIFail || lname.getValue()==null || lname.getValue().length()==0){
                return APIFail;
            }
            
            cmv.assign_empty();
            cmv.assign_node(lname.getValue(), tmpid);
            return APISucc;                    
        }
        
        //then search for primitive vals
        
        
        int ret = setptr.set_get_prs(cmv);
        if(ret==APIFail){
            cmv.assign_empty();
            return APIFail;
        }
        return APISucc;
    }
    
    /**
     * Return the system identifier sysid, the logical name node and the 
     * system class Sclass of the next object in set set_id.
     * 
     * @param set_id
     * @param sysid
     * @param node
     * @param Sclass
     * @return 
     */
    public int return_full_nodes(int set_id, PrimitiveObject_Long sysid, StringObject node, StringObject Sclass){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //  Give the system identifier sysid the logical name node and
        //  the system class of the next object in set set_id.
        //  Returns 0 on success, ErrorCode on error or -1 if end of set reached.
        int sis_api::return_full_nodes(int set_id, int *sysid, l_name node, l_name Sclass)
        {
            int      ret;
            SYSID    tmpid;

            CHECK_FILES("return_full_nodes");
            ret = return_id(set_id, &tmpid);

            if (ret == -1) {
                    globalError.reset();
                    return ret;
            }

            // there is an answer
            *sysid = tmpid.id;
            if (getLoginamString(tmpid, node) == -1) {
                    globalError.reset();
                    return -1;
            }

            if (Sclass != (char *)0) {
                    tmpid = getSysClass(tmpid);
                    if (getLoginamString(tmpid, Sclass) == -1) {
                            globalError.reset();
                            return -1;
                    }
            }

            globalError.reset();  // just in case any error occured.
            return ret;
        }
        */   
        // </editor-fold> 
        
        if(!check_files("return_full_nodes")){
            return APIFail;
        }
        PrimitiveObject_Long idObj = new PrimitiveObject_Long();
        int ret = return_id(set_id, idObj);
        if (ret == APIFail) {
            globalError.reset();
            return APIFail;
	}
        
        Node n = db.getNeo4jNodeByNeo4jId(idObj.getValue());
        if(n!=null){
            sysid.setValue(idObj.value);
            node.setValue((String) n.getProperty(db.Neo4j_Key_For_Logicalname));
            Sclass.setValue(db.getNeo4jNodeSystemClass(n));
        }
        
        globalError.reset();  // just in case any error occured.
        return APISucc;
    }
    
    /**
     * Similar to return nodes but gets all information at once not id per id as 
     * return_nodes does. Returns a structure that contains everything return_nodes
     * would return plus the node id
     * 
     * @param set_id
     * @param retVec
     * @return 
     */
    public int bulk_return_nodes(int set_id, Vector<Return_Nodes_Row> retVals){
        if(!check_files("bulk_return_nodes")){
            return APIFail;
        }
        PQI_Set requestedSet = this.tmp_sets.return_set(set_id);
        if(requestedSet==null){
            return APIFail;
        }
        Vector<Long> setIds = requestedSet.get_Neo4j_Ids();
        
        retVals.clear();
        
        if(setIds.size()==0){
            return APISucc;
        }
        if(db.get_Bulk_Return_Nodes_Rows(setIds, retVals)==QClass.APIFail){
            return APIFail;
        }
        
        return APISucc;
    }
    
    /**
     * Return the logical name cls of the next object in set set_id.
     * 
     * Like most of the return functions, when called, they give the 
     * answer for the next object in the set they apply on and return 
     * APISucc(0).
     * 
     * If any error occurred or end of set reached they return APIFail(-1)
     * 
     * @param set_id
     * @param cls
     * @return 
     */
    public int return_nodes(int set_id, StringObject cls){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        int      ret;
	SYSID    tmpid;

	CHECK_FILES("return_nodes");
	ret = return_id(set_id, &tmpid);

	if (ret == -1) {
		globalError.reset();
		return -1;
	}
	if (getLoginamString(tmpid, cls) == -1) {
		globalError.reset();
		return -1;
	}
	globalError.reset();  // just in case any error occured.
	return ret;
        */
        // </editor-fold> 
        
        if(!check_files("return_nodes")){
            return APIFail;
        }
        PrimitiveObject_Long idObj = new PrimitiveObject_Long();
        int ret = return_id(set_id, idObj);
        if (ret == APIFail) {
            globalError.reset();
            return APIFail;
	}
        if (getLoginamString(idObj.getValue(), cls) == -1) {
            globalError.reset();
            return APIFail;
        }
        
        globalError.reset();  // just in case any error occured.
	return APISucc;
       
    }
    
    /**
     * Similar to return link id but gets all information at once not id per id as 
     * return_link_id does. Returns a structure that contains everything return_link_id
     * would return.
     * 
     * @param set_id
     * @param retVals
     * @return 
     */
    public int bulk_return_link_id(int set_id, Vector<Return_Link_Id_Row> retVals){
        if(!check_files("bulk_return_link_id")){
            return APIFail;
        }
        PQI_Set requestedSet = this.tmp_sets.return_set(set_id);
        if(requestedSet==null){
            return APIFail;
        }
        Vector<Long> setIds = requestedSet.get_Neo4j_Ids();
        
        retVals.clear();
        
        if(setIds.size()==0){
            return APISucc;
        }
        
        if(db.get_Bulk_Return_Link_Id_Rows(setIds, retVals,backward_set)==QClass.APIFail){
            return APIFail;
        }
        
        return APISucc;
    }
    
    /**
     * Similar to return link but gets all information at once not id per id as 
     * return_link does. Returns a structure that contains everything return_link
     * would return plus the link id
     * 
     * @param set_id
     * @param retVals
     * @return 
     */
    public int bulk_return_link(int set_id, Vector<Return_Link_Row> retVals){
        
        
        if(!check_files("bulk_return_links")){
            return APIFail;
        }
        PQI_Set requestedSet = this.tmp_sets.return_set(set_id);
        if(requestedSet==null){
            return APIFail;
        }
        Vector<Long> setIds = requestedSet.get_Neo4j_Ids();
        
        retVals.clear();
        
        if(setIds.size()==0){
            return APISucc;
        }
        /*
        if(setIds.size()==1){
            
            StringObject cls = new StringObject();
            StringObject label = new StringObject();
            CMValue cmv = new CMValue();
            
            db.getValsOfReturnLink(setIds.get(0), cls, label, cmv);
            retVals.add(new Return_Link_Row(setIds.get(0),cls.getValue(),label.getValue(),cmv));
        }
        else{
            */
        
        if(db.get_Bulk_Return_Link_Rows(setIds, retVals)==QClass.APIFail){
            return APIFail;
        }
        //}
        
        
        
        return APISucc;
    }
    
    boolean linkTraversedBackwards(long id, PQI_Set local_b_set){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        // Returns TRUE if link object id was traversed backwards (if traverded
        // with one transitive query) or else (default) return FALSE.
        int sis_api::linkTraversedBackwards(SYSID id, SET *b_set)
        {
                SET      tmpset;


                if (getClasses(id, &tmpset) != -1) {
                        if (! tmpset.set_disjoint(b_set) ) {
                                return TRUE;
                        }
                }

                return FALSE;
        }
        */
        // </editor-fold>         
        PQI_Set tmpset = new PQI_Set();
        if(db.getClasses(id, tmpset)!=APIFail){
            if(tmpset.set_disjoint(local_b_set)==false){
                return true;
            }
        }
        return false;
    }
    
    private int return_link_id(int set_id, PrimitiveObject_Long sysid, IntegerObject traversed){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //  Give next link object (SYSID) from set set_id and a flag
        //  indicating if the link was traversed BACKWARD or FORWARD
        //  (if ever traversed) by a traverse query. Default FORWARD.
        //  Returns 0 on success, -1 when an error occured or end of set
        //  reached.

        int sis_api::return_link_id(int set_id, SYSID *sysid, int *traversed)
        {
                SET *setptr;

                if ((setptr = tmp_sets.return_set(set_id)) == NULL) {
                        return -1;
                }

                if ((*sysid = setptr->set_get_id()).id == 0)  {  // end of set
                        return -1;
                }
                if (linkTraversedBackwards(*sysid, &backward_set)) {
                        *traversed = BACKWARD;
                }
                else {
                        *traversed = FORWARD;
                }
                return 0;
        }
        */
        // </editor-fold>         
        
        PQI_Set setptr = this.tmp_sets.return_set(set_id);
        if(setptr==null){
            return APIFail;
        }
        long idVal = setptr.set_get_id();
        if(idVal<=0){
            return APIFail;
        }
        sysid.setValue(idVal);
        
        if(linkTraversedBackwards(idVal,backward_set)){
            traversed.setValue(QClass.BACKWARD);
        }
        else{
            traversed.setValue(QClass.FORWARD);
        }
        return APISucc;
    }
    
    /**
     * For the next object in set set_id, supposes it is a link object, and 
     * returns the logical name cls of the object, its system identifier sysid, 
     * the system identifier fcid of the object it is pointing from and the 
     * object (or primitive value) cmv it is pointing to. The flag traversed 
     * indicates if the specific link belongs to a category that was previously 
     * set with the set_categories() function with direction BACKWARD.
     * 
     * @param set_id
     * @param cls
     * @param fcid
     * @param sysid
     * @param cmv
     * @param traversed
     * @return 
     */
    public int return_link_id(int set_id, 
            StringObject cls, 
            PrimitiveObject_Long fcid,
            PrimitiveObject_Long sysid, 
            CMValue cmv, 
            IntegerObject traversed){

        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //  Give logical name label, sysid fcid of from-class, sysid toid of
        //  to-class and sysid of next  object in set set_id supposing it to
        //  be a link object. If object is not a link object cdid and toid
        //  are zero. toid is alse zero when to value of link is primitive.
        //  Returns 0 on success, ErrorCode on error or -1 if end of set reached.
        int sis_api::return_link_id(int set_id, l_name cls,int *fcid, int *sysid,cm_value *cmv, int *traversed)
        #ifdef CLIENT_SERVER
        {
                CHECK_FILES("return_link_id");
                int result;

                if(PreviousRF != RF_Return_link_id)
                {
                        PreviousRF = RF_Return_link_id;
                        result = RFCache->InvalidateCache();
                        if(result < 0) return result;  // Comms error
                }

                result = RFCache->ReadCache(RF_Return_link_id , set_id, "%ln%ip%ip%cm%ip", cls, fcid, sysid, cmv, traversed);
                return result;
        }
        #else
        {
                //int sis_api::return_link_id(int set_id, l_name cls,int *fcid, int *sysid,cm_value *cmv, int *traversed)
                int      ret;
                SYSID    tmpid, classid;
                prs_val  prs_value;

                CHECK_FILES("return_link_id");
                ret = return_link_id(set_id, &tmpid, traversed);

                if (ret == -1) {
                        return ret;
                }
                *sysid = tmpid.id;        // set link's sysid

                if (getFrom(tmpid, &classid) != -1) {
                        *fcid = classid.id ;     // set from-class's sysid
                        if (getLoginamString(classid, cls) == -1) { // set link's label
                                globalError.reset();
                                return -1;
                        }
                }
                else {
                        *fcid = 0;            // or on error set to zero
                        cls[0] = '\0';
                }

                if (getToId(tmpid, &prs_value, &classid) != -1) {
                        ret = copy_prs(&prs_value, cmv, classid.id);
                }
                else {
                        Ass_val->assign_empty(cmv);
                }

                globalError.reset();  // just in case any error occured.
                return ret;
        }
        #endif
        
        
        
        */
        // </editor-fold>         
        
        if(!check_files("return_link_id")){
            return APIFail;
        }
        PrimitiveObject_Long tmpid = new PrimitiveObject_Long();
        int ret = return_id(set_id, tmpid);
        if (ret == APIFail) {
            globalError.reset();
            return APIFail;
	}
        
        return db.get_Single_Return_Link_Id(tmpid.getValue(), cls, fcid,sysid, cmv,traversed,backward_set);
        /*
        int ret = return_link_id(set_id, tmpid, traversed);
        if (ret == APIFail) {
            return APIFail;
	}
        sysid.setValue(tmpid.getValue());
        long classid = db.DBACCESS_get_From_or_To(tmpid.getValue(), true);
        if(classid!=APIFail){
            fcid.setValue(classid);
            if(getLoginamString(classid, cls)==APIFail){
                globalError.reset();
                return APIFail;
            }
        }
        else{
            fcid.setValue(0);
            cls.setValue("");
        }
        */
        /*
                if (getToId(tmpid, &prs_value, &classid) != -1) {
                        ret = copy_prs(&prs_value, cmv, classid.id);
                }
                else {
                        Ass_val->assign_empty(cmv);
                }
        
        
        int sis_api::getToId(SYSID objSysid, prs_val *prs_value, SYSID *toid)
        {
                telos_object *obj_ptr;
                SYSID         tmpid;
                LOGINAM      *tmpname;

                if ((obj_ptr = api_sys_cat->loadObject(objSysid)) == NULL) {
                        globalError.putMessage(" >getToId");
                        return -1;
                }
                if (O_IsNode(obj_ptr->o_type)) {
                        api_sys_cat->unloadObject(objSysid);
                        globalError.putMessage(" >getTo : No to value");
                        return -1;
                }

                if ((tmpid = obj_ptr->getTo()).id)  {  // Points to a node
                        tmpname = getLoginam(tmpid);
                        *prs_value = prs_val(tmpname);
                        *toid      = tmpid;
                        api_sys_cat->unloadObject(objSysid);
                        return (globalError.checkError(" >getToId"));
                }

                // Points to a node
                *prs_value = obj_ptr->getToTagged();
                toid->id   = 0;
                api_sys_cat->unloadObject(objSysid);

                return (globalError.checkError(" >getTo"));
        }

        */
        /*
        globalError.reset();  // just in case any error occured.
        return ret;
        */
    }
    
    /**
     * For the next object in set set_id, supposes it is a link object, and 
     * returns the logical label name of the object, the logical name cls of 
     * the class which it is pointing from and the object it is pointing to cmv.
     * 
     * Since this object may not be a class but a primitive value (string, 
     * integer etc.) it is returned in a structure cm_value.
     * 
     * Like most of the return functions, when called, they give the 
     * answer for the next object in the set they apply on and return 
     * APISucc(0).
     * 
     * If any error occurred or end of set reached they return APIFail(-1)
     * 
     * @param set_id
     * @param cls
     * @param label
     * @param cmv
     * @return 
     */
    public int return_link(int set_id, StringObject cls, StringObject label, CMValue cmv){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //  Give from-class name, logical name label and to-value of next
        // object in set set_id supposing it to be a link object.
        // Returns 0 on success, ErrorCode on error or -1 if end of set reached.

        int sis_api::return_link(int set_id, l_name cls, l_name label, cm_value *cmv){
            int      ret;
            SYSID    tmpid,
                    classid;
            prs_val  prs_value;

            CHECK_FILES("return_link");
            ret = return_id(set_id, &tmpid);

            if (ret == -1) {
                    return ret;
            }

            if (getLoginamString(tmpid, label) == -1) {
                    globalError.reset();
                    return -1;
            }

            if (getFrom(tmpid, &classid) != -1) {
                    if (getLoginamString(classid, cls) == -1) {
                            globalError.reset();
                            return -1;
                    }
            }
            else cls[0] = '\0';		// strcpy(cls,"");

            if (getToId(tmpid, &prs_value, &classid) != -1) {
                    ret = copy_prs(&prs_value, cmv, classid.id);
            }
            else {
                    Ass_val->assign_empty(cmv);
            }

            globalError.reset();  // just in case any error occured.
            return ret;        
        }
        
        */
        // </editor-fold> 
        
        if(!check_files("return_link")){
            return APIFail;
        }
        PrimitiveObject_Long idObj = new PrimitiveObject_Long();
        int ret = return_id(set_id, idObj);
        if (ret == APIFail) {
            globalError.reset();
            return APIFail;
	}
        
        return db.get_Single_Return_Link(idObj.getValue(), cls, label, cmv);
    }
    
    int getLoginamString(long objectSysId, StringObject name){
     
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //  Fils the buff string with the longinam name.
        //  Returns -1 if fails... and 0 on succexs
        int sis_api::getLoginamString(SYSID sid, l_name name)
        {
                if(IS_UNNAMED(sid.id))
                        set_unamed_label(name, sid.id);
                else {
                        Loginam *ln = api_translator->getLogicalNamePtr(sid);
                        if (!ln) return -1;
                        ln->string(name);
                }
                return 0;
        }
        */
        // </editor-fold> 
        if(db.getStringPropertyOfNode(objectSysId,db.Neo4j_Key_For_Logicalname, name)!=APIFail){
            return APISucc;
        }
        return APIFail;
    }
    
    
    int return_id(int set_id, PrimitiveObject_Long sysid){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">        
        /*
        //  Give next object (SYSID) from set set_id.
        //  Returns 0 on success, -1 when an error occured or end of set
        //  reached.
        int sis_api::return_id(int set_id, SYSID *sysid)
        {
                SET *setptr;

                if ((setptr = tmp_sets.return_set(set_id)) == NULL) {
                        return -1;
                }

                if ((*sysid = setptr->set_get_id()).id != 0)  {
                        return 0;
                }
                else { //  end of set
                        return -1;
                }
        }
        */
        // </editor-fold>
        
        PQI_Set rSet = this.tmp_sets.return_set(set_id);
        if(rSet==null){
            return APIFail;
        }
        long idVal = rSet.set_get_id();
        if(idVal!=APISucc){
            sysid.setValue(idVal);
            return APISucc;
        }
        return APIFail;
    }
    

    /**
     * This function puts the current node into set set_id.
     * 
     * @param setId
     * @return 
     */
    public int set_put(int setId) {
        
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        if (no_current_node("set_put")) {
		return -1;       // there is no current node set
	}
	return tmp_sets.set_put(set_id, *current_node);
        */
        // </editor-fold> 
        
        if(!check_files("set_put")){
            return APIFail;
        }
        
        if(no_current_node("set_put")){
            return APIFail;
        }
        
        return this.tmp_sets.set_put(setId, CurrentNode_Ids_Stack.lastElement());
    }
    
    /**
     * This function puts the primitive value (integer, string, time etc)
     * cmval into set set_id.
     * @param set_id
     * @param cmv
     * @return 
     */
    public int set_put_prm(int set_id,CMValue cmv){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        int sis_api::set_put_prm(int set_id, cm_value *cmval)
        #ifdef CLIENT_SERVER
        {
                int ret;

                if (!cmval) return -1;

                CHECK_FILES("set_put_prm");
                int result;
                result = RFCache->InvalidateCache();
                if(result < 0) return result;  // Comms error
                if((result=SComm->send_int(  _SET_PUT_PRI))>0)
                {
                        if((result=SComm->send_int(  set_id))<0)
                                return result;
                        if((result=SComm->send_prs(  cmval))<0)
                                return result;
                        if((result=SComm->recv_int(  &ret))>0)
                                return ret;
                }
                return result;
        }
        #else
        {
                switch (cmval->type) {
                case TYPE_INT     : return tmp_sets.set_put(set_id, cmval->value.n);
                case TYPE_FLOAT   : return tmp_sets.set_put(set_id, cmval->value.n);
                case TYPE_STRING  : return tmp_sets.set_put(set_id, cmval->value.s);
                case TYPE_TIME    : return tmp_sets.set_put(set_id, cmval->value.t);
                default		       : return -1;
                }
        }
        #endif        
        */
        // </editor-fold> 
        
        if(!check_files("set_put_prm")){
            return APIFail;
        }
        int cmvType = cmv.getType();
        switch(cmvType){
            case CMValue.TYPE_INT:{
                return this.tmp_sets.set_put_Primitive(set_id, cmv);                
            }
            /*
            case CMValue.TYPE_FLOAT:{
                if(Configs.boolDebugInfo){
                    throw new UnsupportedOperationException(" Primitives accepted for set_put_prm are of type int and String only");
                }                
                return APIFail;
            }
            */
            case CMValue.TYPE_STRING:{
                return this.tmp_sets.set_put_Primitive(set_id, cmv);
            }
            /*
            case CMValue.TYPE_TIME:{
                
                if(Configs.boolDebugInfo){
                    throw new UnsupportedOperationException(" Primitives accepted for set_put_prm are of type int and String only");
                }                
                return APIFail;
            }
            */
            default: {
                if(Configs.boolDebugInfo){
                    throw new UnsupportedOperationException(" Primitives accepted for set_put_prm are of type int and String only");
                }                
                return APIFail;
            }        
        }
        
                
    }
    
    /**
     * This function copies set2 to set1.
     * 
     * @param set_id1
     * @param set_id2
     * @return 
     */
    public int set_copy(int set_id1, int set_id2){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        return tmp_sets.set_operation(_SET_COPY, set_id1, set_id2);
        */
        // </editor-fold> 
        if(!check_files("set_copy")){
            return APIFail;
        }
        return tmp_sets.set_operation(Sets_Class.Set_Operation_Identifiers._SET_COPY, set_id1, set_id2);
    }
    
    /**
     * After this operation the first set is the difference of the two sets 
     * given as arguments. If the two sets are tuples the difference 
     * operation is performed to the tuples.
     * 
     * @param set_id1
     * @param set_id2
     * @return 
     */
    public int set_difference(int set_id1, int set_id2){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        int sis_api::set_difference(int set_id1, int set_id2)
        #ifdef CLIENT_SERVER
        {
                int ret;

                CHECK_FILES("set_difference");
                int result;
                result = RFCache->InvalidateCache();
                if(result < 0) return result;  // Comms error
                if((result=SComm->send_int(  _SET_DIFFERENCE))>0)
                        if((result=SComm->send_int(  set_id1))>0)
                                if((result=SComm->send_int(  set_id2))>0)
                                        if((result=SComm->recv_int(  &ret))>0)
                                                return ret;
                return result;
        }
        #else
        {
                return tmp_sets.set_operation(_SET_DIFFERENCE, set_id1, set_id2);
        }
        #endif
        */
        // </editor-fold> 
        if(!check_files("set_difference")){
            return APIFail;
        }
        return tmp_sets.set_operation(Sets_Class.Set_Operation_Identifiers._SET_DIFFERENCE, set_id1, set_id2);
    }
    
    /**
     * This function returns TRUE (1) if the intersection of set1, set2 is empty or FALSE (0) if it is not empty.
     * 
     * use QClass.TRUEval and QClass.FALSEval to check the result
     * 
     * @param set_id1
     * @param set_id2
     * @return 
     */
    public int set_disjoint(int set_id1, int set_id2){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        int sis_api::set_disjoint(int set_id1, int set_id2)
        #ifdef CLIENT_SERVER
        {
                int ret;

                CHECK_FILES("set_disjoint");
                int result;
                result = RFCache->InvalidateCache();
                if(result < 0) return result;  // Comms error
                if((result=SComm->send_int(  _SET_DISJOINT))>0)
                        if((result=SComm->send_int(  set_id1))>0)
                                if((result=SComm->send_int(  set_id2))>0)
                                        if((result=SComm->recv_int(  &ret))>0)
                                                return ret;
                return result;
        }
        #else
        {
                return tmp_sets.set_operation(_SET_DISJOINT, set_id1, set_id2);
        }
        #endif        
        */
        // </editor-fold> 
        
        if(!check_files("set_disjoint")){
            return APIFail;
        }
        return tmp_sets.set_operation(Sets_Class.Set_Operation_Identifiers._SET_DISJOINT, set_id1, set_id2);                
    }
    /** Not checked yet so this is why it is not still public (does check include primitive values?)
     * 
     * This function returns TRUE (1) if set1, set2 are equal or FALSE (0) if they are not.
     * 
     * @param set_id1
     * @param set_id2
     * @return 
     */
    int set_equal(int set_id1, int set_id2){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        int sis_api::set_disjoint(int set_id1, int set_id2)
        #ifdef CLIENT_SERVER
        {
                int ret;

                CHECK_FILES("set_disjoint");
                int result;
                result = RFCache->InvalidateCache();
                if(result < 0) return result;  // Comms error
                if((result=SComm->send_int(  _SET_DISJOINT))>0)
                        if((result=SComm->send_int(  set_id1))>0)
                                if((result=SComm->send_int(  set_id2))>0)
                                        if((result=SComm->recv_int(  &ret))>0)
                                                return ret;
                return result;
        }
        #else
        {
                return tmp_sets.set_operation(_SET_DISJOINT, set_id1, set_id2);
        }
        #endif        
        */
        // </editor-fold> 
        
        if(!check_files("set_equal")){
            return APIFail;
        }
        return tmp_sets.set_operation(Sets_Class.Set_Operation_Identifiers._SET_EQUAL, set_id1, set_id2);                
    }
    /**
     * After this operation the first set is the intersection of the two sets 
     * given as arguments.
     * 
     * @param set_id1
     * @param set_id2
     * @return 
     */
    public int set_intersect(int set_id1, int set_id2){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        */
        // </editor-fold> 
        
        if(!check_files("set_intersect")){
            return APIFail;
        }
        return tmp_sets.set_operation(Sets_Class.Set_Operation_Identifiers._SET_INTERSECT, set_id1, set_id2);                
    }
    
    /**
     * After this operation the first set is the union of the two sets given as 
     * arguments. If the two sets are tuples the union is performed to the 
     * tuples (provided they have the same number of columns).
     * 
     * @param set_id1 the identifier of the set that will contain the union of the 2 sets
     * @param set_id2 the identifier of the set that we want to union with set_id1
     * 
     * @return return APISucc(0) on success and APIFail(-1) on error
     */
    public int set_union(int set_id1, int set_id2)
    {
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        int sis_api::set_union(int set_id1, int set_id2)
        {
            return tmp_sets.set_operation(_SET_UNION, set_id1, set_id2);
        }
        */
        // </editor-fold> 
        
        if(!check_files("set_union")){
            return APIFail;
        }
        
        return this.tmp_sets.set_operation(Sets_Class.Set_Operation_Identifiers._SET_UNION, set_id1, set_id2);
        
    }

    /**
     * This function checks whether the current node exists in set set_id.
     * 
     * @param set_id
     * @return 
     */
    public int set_member_of(int set_id){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //
        //  Check if current node is contained in set set_id.
        //  Returns 0 if it exists, -1 otherwise or any error occured.
        int sis_api::set_member_of(int set_id)
        #ifdef CLIENT_SERVER
        {
                int ret;

                CHECK_FILES("set_member_of");
                int result;
                // result = RFCache->InvalidateCache(); Not required to invalidate caches
                // here. This is an optimazation to improve client-server performance.
                // if(result < 0) return result;  // Comms error
                if((result=SComm->send_int(  _SET_MEMBER_OF))>0)
                        if((result=SComm->send_int(  set_id))>0)
                                if((result=SComm->recv_int(  &ret))>0)
                                        return ret;
                return result;
        }
        #else
        {
                if (no_current_node("set_member_of")) {
                        return -1;       // there is no current node set
                }
                return tmp_sets.set_member_of(set_id, *current_node);
        }
        #endif
        */
        // </editor-fold> 
        
        if(!check_files("set_member_of")){
            return APIFail;
        }
        
        if(no_current_node("set_member_of")){
            return APIFail;
        }
        return tmp_sets.set_member_of(set_id, this.CurrentNode_Ids_Stack.lastElement());
    }
    
    /**
     * This function returns the set identifier of a new empty set.
     *
     * @return
     */
    public int set_get_new() {
        
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        return tmp_sets.return_new_set();
        */
        // </editor-fold> 
        if(!check_files("set_get_new")){
            return APIFail;
        }
        return this.tmp_sets.return_new_set();
    }

    /**
     * This function returns the number of object that exist in set set_id.
     */
    public int set_get_card(int set_id) {
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        return tmp_sets.set_get_card(set_id);
        */
        // </editor-fold> 
        if(!check_files("set_get_card")){
            return APIFail;
        }
        return this.tmp_sets.set_get_card(set_id);
    }

    /**
     * This function free the temporary set set_id so that it can be used later
     * by some other query. Freeing a set is like closing a file descriptor in
     * UNIX.
     */
    public int free_set(int set_id) {
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        int sis_api::free_set(int set_id)
        {
            return tmp_sets.free_set(set_id);
        }
        */
        // </editor-fold> 
        if(!check_files("free_set")){
            return APIFail;
        }
        return this.tmp_sets.free_set(set_id);
    }

    /**
     * This function frees all the temporary sets.
     * 
     * @return 
     */
    public int free_all_sets() {

        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*

        int sis_api::free_all_sets(void)
        #ifdef CLIENT_SERVER
        {
                CHECK_FILES("free_all_sets");
                int result;
                result = RFCache->InvalidateCache();
                if(result < 0) return result;  // Comms error
                if((result=SComm->send_int(  _FREE_ALL_SETS))>0)
                        return 0;
                return result;
        }
        #else
        {
                tmp_sets.free_all_sets();
                return 0;
        }
        */
        // </editor-fold> 
        
        if(!check_files("free_all_sets")){
            return APIFail;
        }
        tmp_sets.free_all_sets();
        return APISucc;
    }
    
    /**
     * Set the depth the recursive queries are going to traverse links, starting 
     * from a node object. 
     * 
     * If depth is a negative integer recursive queries traverse links with no limit to depth.
     * @param newDepthValue
     * @return 
     */
    public int set_depth(int newDepthValue){
        // <editor-fold defaultstate="collapsed" desc="C++ code.">
        /*
        // START OF: sis_api::set_depth(int dep) 
        //
        //  Set depth level for transitive queries. depth should be a
        //  positive integer or 0. If depth = -1 then transitive queries
        //  traverse all posible paths with no limit to depth.
        //  Returns 0 on success, ErrorCode on error.
        
        CHECK_FILES("set_depth");
	if (dep >= 0)
		depth = dep;
	else
		depth = -1;
	return 0;
        */
        // </editor-fold>
        
        if(!check_files("set_depth")){
            return APIFail;
        }
        if(newDepthValue>=0){
            depth = newDepthValue;
        }
        else{
            depth =-1;
        }
        return APISucc;
    }
    
    /**
     * Set current name scope the whole SIS base. The next set_current_node()
     * will refer to a class node.
     *
     * @return ApiSucc in success ApiFail in failure.
     */
    public int reset_name_scope() {
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        int sis_api::reset_name_scope()
        #ifdef CLIENT_SERVER
	//  Reset name scope to whole SIB. Next set_current_node() will
	//  suppose that its argument is the logical name of a node object.
	//  Returns 0 on success, ErrorCode on error (not in query mode).
	{
                CHECK_FILES("reset_name_scope");
                int result;
                result = RFCache->InvalidateCache();
                if(result < 0) return result;  // Comms error
                if((result=SComm->send_int(  _RESET_NAME_SCOPE)) > 0)
                        return 0;
                else
                        return result;
        }
        #else
        {
                stack[0] = SYSID(0);
                current_node = &(stack[0]);
                return 0;
        }
        #endif
        */        
        // </editor-fold> 
        
        if (!check_files("reset_name_scope")) {
            return APIFail;
        }
        
        this.CurrentNode_Ids_Stack.clear();
        return APISucc;
    }

    /**
     * Set current node the object with logical name node. If the name scope is
     * the whole SIS base (default at the beginning of a query session or result
     * of reset_name_scope() call) the argument must be the name of a node
     * object otherwise it must be the logical name of a link object pointing
     * from the current node. The function returns the system id of the current
     * node or APIFail(-1) on failure.
     *
     * @param str A StringObject that contains the logical name 
 of the object that will be set as current node
     * 
     * @return The function returns the system id of the current node or
     * APIFail(-1) on failure.
     */
    public long set_current_node(StringObject str) {
        //<editor-fold defaultstate="collapsed" desc="C++ References">
        /*
         answerer
        
         //---------------------------------------   
         //Set Current Node
         //---------------------------------------
         void scn()
         {
         int    id;
         l_name name;

         printf("\n    SET CURRENT NODE  \n");
         printf("    Enter logical name of node : ");
         scan_for_loginam(name);
         //SCANF("%s", name);

         if ((id = set_current_node(name)) != -1) {
         printf("\n    CURRENT NODE IS NOW: %s (SYSID %d)\n",name, id);;
         }
         else {
         printf("\n    *** ERROR ***  QUERY FAILED \n");;
         }
         }

         typedef struct us_sysid {
         int id;        // sysid itself, integer number of  24 bits.
         us_sysid()        {id = 0;}
         us_sysid(int ii)    {id = ii;}
         } SYSID;
        
    
         SYSID *current_node;
         SYSID stack[NAME_STACK_SIZE];
         Translator       *api_translator;

         // Returns the SYSID of the Class having the logical name lname
         // Returns SYSID 0 if lname Class lname doesn't exist.
         SYSID sis_api::getSysid(LOGINAM *lname) {
         return api_translator->getSysid(lname, SYSID());
         }
        
    
         //  Returns the SYSID of the attribute with LOGINAM attr
         //  of the class with SYSID cls
         //  If there is not such an attribute, returns SYSID 0        
         SYSID sis_api::getSysid(SYSID cls, LOGINAM *attr) {
         return   api_translator->getSysid(attr, cls);
         }


         // ---------------------------------------------------------------------
         // getSysid -- returns the sysid that have logical name the first name
         //	of full name ln and its from-object have sysid sys. If that sysid
         //	not exist it returns SYSID(0).
         // ---------------------------------------------------------------------
         SYSID	getSysid( LOGINAM *ln, SYSID sys) {
        
         Loginam	*tmpL;
         SYSID	retVal;
         int id;
        
         // check added by georgian to support unnamed objects 
         if ( ln == ( LOGINAM * ) 0 ) {
         return SYSID(0);
         }
        
         if ( (id = ln->unnamed_id()) ) {
         // printf("getSysid( LOGINAM *ln, SYSID sys) called on unnamed link 0x%x\n",id);
         return (SYSID(id));
         //		} else {
         }
        
         tmpL = inv_convertion(ln);
         retVal = symbolTable->getSysid( tmpL, sys);
         delete	tmpL;
         return( retVal);
        
         //}
         }
        
         int push_name(SYSID id) {
         int retval;
         current_node++;
         if (current_node == &(stack[NAME_STACK_SIZE])) {
         retval = -1;
         }
         else {
         *current_node = id;
         retval = 0;
         }
        
         return retval;
         };
        
         */
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="set current node C++ Code">
        /*
        
         FILE: "cpp_api\q_class_pblc.cpp"
        
        
         // START OF: set_current_node(l_name node)
         //
         //  Set current node the object with logical name str.
         //  If name scope is the whole SIB str is supposed to be a node
         //  object. If there is already a current node str is supposed to
         //  be the lagical name of a link pointing from the current node.
         //  Returns the sysid of the new current node or -1 if any error
         //  occured.
         //
         int sis_api::set_current_node(l_name node) {

         // Check if the length of the given string to be used as a logical name
         // is less than LOGINAM_SIZE
         if(strlen(node) >= 128)
         {
         // The given string is longer than a loginam an thus it cannot be a logical name. set_current_node should fail.
         return -1;
         }	
         LOGINAM lname(node);
         SYSID   id;

         if (current_node->id == 0) {  // Global scope
         if ((id = getSysid(&lname)).id !=0) {
         if (push_name(id) != -1) {
         return id.id;;
         }
         else { // push_name() failed
         return -1;
         }
         }
         else  { // getSysid() failed
         return -1;
         }
         }
         else  { // See only the attribute labels of the current node
         if ((id = getSysid(*current_node, &lname)).id != 0) {
         if (push_name(id) != -1) {
         return id.id;
         }  // push_name() failed
         else {
         return -1;
         }
         }
         else  {  // getSysid() failed
         return -1;
         }
         }
         return -1;
         }   

         END OF: set_current_node(l_name node) 
        
         */
        //</editor-fold>
        //check if in a query session
        //check lengths
        //if no current node is set then just try set it
        //if current node is set then try find a node relatied to last current node with the logicalname given 
        //check if in a query session
        
        if (!check_files("set_current_node")) {
            return QClass.APIFail;
        }

        //check lengths
        if (str == null || str.getValue() == null || str.getValue().length() == 0) {
            return QClass.APIFail;
        }

        return db.setCurrentNode(CurrentNode_Ids_Stack, str.getValue());        
    }
    
    /**
     * Set current node the object with system identifier nodeid. This object 
     * is now at the top of the name stack and if it is a link object the name 
     * stack is changed properly (see section 3 where name stack is described).
     * @param nodeid
     * @return 
     */
    public long set_current_node_id(long nodeid){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        int sis_api::set_current_node_id(int nodeid)
        #ifdef CLIENT_SERVER
	//  Set current node the object with system identifier nodeid.
	//  Returns nodeid on success, ErrorCode on error.
	{
                int ret;

                CHECK_FILES("set_current_node_id");
                if (nodeid < 0) return APIFail;
                int result;
                // result = RFCache->InvalidateCache();  Not required to invalidate caches
                // here. This is an optimazation to improve client-server performance.
                // if(result < 0) return result;  // Comms error
                if((result=SComm->send_int(  _SET_CURRENT_NODE_ID)) > 0)
                        if((result=SComm->send_int(  nodeid))>0)
                                if((result=SComm->recv_int(  &ret))>0)
                                        return ret;
                return result;
        }
        #else
	//  Set current node the object with system idectifier id.
	//  Returns id on success, -1 when an error occured.
	{
                SYSID  tmpstack[NAME_STACK_SIZE];
                SYSID *idptr;
                SYSID *limptr = &(tmpstack[NAME_STACK_SIZE-1]);  // leave space for id 0
                SYSID  fromid;


                if (! existsId(SYSID(nodeid))) {  // There in no object with sysid id
        #ifdef DEBUG
                        fprintf(stderr,"*** ERROR *** qp::set_current_node_id : ");
                        fprintf(stderr,"No object with sysid : %d \n", id);
        #endif
                        return -1;
                }

                idptr = tmpstack;
                *idptr = nodeid;
                // This was : if  ( (idptr->nodeid) > MAX_BUILT_IN_ID ) {
                // However the nodeid is not a member of SYSID. SYSID has a
                // member named id. This strange enough was working before
                // the attempt to make the multithread server.
                if  ( (idptr->id) > MAX_BUILT_IN_ID ) {
                        while (getFrom(*idptr, &fromid) != -1) {
                                *(++idptr) = fromid;
                                if (idptr > limptr) { // stack overflow
                                        return -1;
                                }
                                
                                // Stop in case of builtin object because
                                // it can be accessed directly succesfully.
                                
                                if ( (idptr->id) <= MAX_BUILT_IN_ID ) {
                                        break ;
                                }
                        }
                }

                globalError.reset(); // getFrom created an error message
                // copy tmpstack in query class's stack
                current_node = stack;
                *current_node++ = SYSID(0); // first id in stack in SYSID(0)
                while (idptr >= tmpstack)
                {
                        //*current_node++ = *idptr--;
                        *current_node = *idptr;
                        current_node++;
                        idptr--;
                }

                current_node--;
                return nodeid;
        }
        #endif

        */
        // </editor-fold> 
        
        if (!check_files("set_current_node_id")) {
            return QClass.APIFail;
        }

        //check lengths
        if (nodeid <=0) {
            return QClass.APIFail;
        }

        return db.setCurrentNodeById(CurrentNode_Ids_Stack, nodeid);    
    }
    
    /**
     * If set_id is 0, get the links pointing from the current node. The answer set 
     * contains the system identifiers of these links.
     * 
     * If set_id is a positive integer, apply get_link_from() on each object in 
     * temporary set set_id.
     * 
     * ATTENTION!!!
     * there is also a case that is not included here 
     * get the "class attributes" defined in the classes of current node
     * 
     * Class attibutes are attributes declared at the level of a Class but they 
     * hold for all instances of this class with one common To value 
     * (not necessarily primitive)
     * 
     * @param set_id If set to 0 then apply function to current node. If set to
     * a positive integer then search internally for a set with this identifier
     * and apply this function to every object contained in the set
     *
     * @return the identifier or the answer set or ApiFail     
     */
    public int get_link_from(int set_id) {
        if (!check_files("get_link_from")) {
            return QClass.APIFail;
        }

        return simple_query(Simple_Query_Identifiers._GET_LINK_FROM, set_id);
    }
    
    /**
     * If set_id is 0, get the links pointing to the current node. The answer set
     * contains the system identifiers of these links.
     * 
     * If set_id is a positive integer, apply get_link_to() on each object in temporary
     * set set_id.
     * 
     * @param set_id If set to 0 then apply function to current node. If set to
     * a positive integer then search internally for a set with this identifier
     * and apply this function to every object contained in the set
     *
     * @return the identifier or the answer set or ApiFail
     */
    public int get_link_to(int set_id) {
        if (!check_files("get_link_to")) {
            return QClass.APIFail;
        }

        return simple_query(Simple_Query_Identifiers._GET_LINK_TO, set_id);
    }
    
    /**
     * If set_id is 0, get all objects that are connected to the current node by links
     * pointing from them. The answer set contains the system identifiers of these
     * objects.
     * 
     * If set_id is a positive integer, apply get_from_node() on each object in
     * temporary set set_id.
     * 
     * @param set_id If set to 0 then apply function to current node. If set to
     * a positive integer then search internally for a set with this identifier
     * and apply this function to every object contained in the set
     *
     * @return the identifier or the answer set or ApiFail
     */
    public int get_from_node(int set_id){
        /*
        int sis_api::get_to_value(int set_id)
        {
            CHECK_FILES("get_to_value");
            return  simple_query(_GET_TO_VALUE, set_id);
        }
        */
        if (!check_files("get_from_node")) {
            return QClass.APIFail;
        }

        return simple_query(Simple_Query_Identifiers._GET_FROM_NODE, set_id);
    }
    
    /**
     * If set_id is 0, get all objects that are connected to the current node by links
     * pointing to them. Links must be instances of some link class that is instance of
     * the category given. The meta-category is defined by the name of the link and
     * the class of which it is pointing from. The answer set contains the system
     * identifiers of these objects.
     * 
     * @param set_id If set to 0 then apply function to current node. If set to
     * a positive integer then search internally for a set with this identifier
     * and apply this function to every object contained in the set
     *
     * @return the identifier or the answer set or ApiFail
     */
    public int get_to_node(int set_id){
        /*
        int sis_api::get_to_value(int set_id)
        {
            CHECK_FILES("get_to_value");
            return  simple_query(_GET_TO_VALUE, set_id);
        }
        */
        if (!check_files("get_to_node")) {
            return QClass.APIFail;
        }

        return simple_query(Simple_Query_Identifiers._GET_TO_NODE, set_id);
    }
 
     /**
     * Get the objects that the link objects in temporary set set_id are 
     * pointing from. 
     * 
     * The answer set contains the system identifiers of these objects.
     * 
     * If set_id is 0, apply get_from_value() on current node.
     * 
     * @param If set to 0 then apply function to current node. If set to
     * a positive integer then search internally for a set with this identifier
     * and apply this function to every object contained in the set
     * 
     * @return the identifier or the answer set or ApiFail
     */
    public int get_from_value(int set_id){
        /*
        int sis_api::get_from_value(int set_id)
{
	CHECK_FILES("get_from_value");
	return  simple_query(_GET_FROM_VALUE, set_id);
}
        */
        if (!check_files("get_from_value")) {
            return QClass.APIFail;
        }

        return simple_query(Simple_Query_Identifiers._GET_FROM_VALUE, set_id);
    }
    
    /**
     * Get the objects that the link objects in temporary set set_id are pointing to. 
     * 
     * The answer set contains the system identifiers of these objects, or/and primitive values.
     * 
     * If set_id is 0, apply get_to_value() on current node.
     * 
     * ATTENTION !!!
     * The PQI_Set structure must be augmented in order to hold any primitive values
     * right now no primitive values are kept
     * 
     * @param If set to 0 then apply function to current node. If set to
     * a positive integer then search internally for a set with this identifier
     * and apply this function to every object contained in the set
     * 
     * @return the identifier or the answer set or ApiFail
     */
    public int get_to_value(int set_id){
        /*
        int sis_api::get_to_value(int set_id)
        {
            CHECK_FILES("get_to_value");
            return  simple_query(_GET_TO_VALUE, set_id);
        }
        */
        if (!check_files("get_to_value")) {
            return QClass.APIFail;
        }

        return simple_query(Simple_Query_Identifiers._GET_TO_VALUE, set_id);
    }
    
    /**
     * If set_id is 0, get the instances of the current node. The answer set
     * contains the system identifiers of these objects.
     *
     * If set_id is a positive integer, apply get_instances() on each object in
     * temporary set set_id.
     *
     * Returns the set identifier where the answer is stored or ApiFail if error
     * occurred.
     *
     * @param set_id If set to 0 then apply function to current node. If set to
     * a positive integer then search internally for a set with this identifier
     * and apply this function to every object contained in the set
     *
     * @return the identifier or the answer set or ApiFail
     */
    public int get_instances(int set_id) {
        //<editor-fold desc="C++ code">
        /*
         FILE: cpp_api\q_class_pblc.cpp

        
         START OF: sis_api::get_instances 
         //
         //  Get a set that contains the instances of the appling object.
         //  Returns the set identifier where the answer is stored and
         //  ErrorCode when an error occured.

         //LOOK OUT. This function has only one body
         //because the bodies for the standalone and
         //client-server version are the same. In case
         //of any changes be sure to check if two bodies
         //should be created.
         int sis_api::get_instances(int set_id)
         {
         if (!query_session && !transaction_session) { fprintf((&__iob_func()[2])," *** ERROR *** (%s):Can not execute query",("get_instances")); fprintf((&__iob_func()[2])," - call begin_query() first\n"); return -1; };;
         return  simple_query(-214, set_id);
         }
         //END OF: sis_api::get_instances         
         */
        //</editor-fold>

        if (!check_files("get_instances")) {
            return QClass.APIFail;
        }

        return simple_query(Simple_Query_Identifiers._GET_INSTANCES, set_id);
    }
    
    
    /**
     * If set_id is 0, get the links pointing from the current node and the links
     * inherited from all superclasses or current node.
     * 
     * If set_id is a positive integer, apply get_inher_link_from() on each object in
     * temporary set set_id.
     * 
     * @param set_id If set to 0 then apply function to current node. If set to
     * a positive integer then search internally for a set with this identifier
     * and apply this function to every object contained in the set
     *
     * @return the identifier or the answer set or ApiFail 
     */
    public int get_inher_link_from(int set_id) {
        if (!check_files("get_inher_link_from")) {
            return QClass.APIFail;
        }

        return simple_query(Simple_Query_Identifiers._GET_INHER_LINK_FROM, set_id);
    }
    
    /**
     * If set_id is 0, get the links pointing to the current node and the links 
     * inherited from all superclasses or current node and pointing to them.
     * 
     * If set_id is a positive integer, apply get_inher_link_to() on each 
     * object in temporary set set_id
     * 
     * @param set_id
     * @return 
     */
    public int get_inher_link_to(int set_id) {
        if (!check_files("get_inher_link_to")) {
            return QClass.APIFail;
        }

        return simple_query(Simple_Query_Identifiers._GET_INHER_LINK_TO, set_id);
    }
    /**
     * If set_id is 0, get all the instances of the current node calculating the instances
     * of all subclasses. The answer set contains the system identifiers of these
     * objects.
     * 
     * If set_id is a positive integer, apply get_all_instances() on each object in
     * temporary set set_id.
     * 
     * @param set_id If set to 0 then apply function to current node. If set to
     * a positive integer then search internally for a set with this identifier
     * and apply this function to every object contained in the set
     *
     * @return the identifier or the answer set or ApiFail
     */
    public int get_all_instances(int set_id) {
         if (!check_files("get_all_instances")) {
            return QClass.APIFail;
        }

        return simple_query(Simple_Query_Identifiers._GET_ALL_INSTANCES, set_id);
    }

    /**
     * If set_id is 0, get classes of which the current node is instance of. The return
     * value is the descriptor of the answer set that contains the objects (system
     * identifiers) of the classes of the current node.
     * 
     * If set_id is a positive integer, apply get_classes() on each object in temporary
     * set set_id.
     * 
     If set to 0 then apply function to current node. If set to
     * a positive integer then search internally for a set with this identifier
     * and apply this function to every object contained in the set
     *
     * @return the identifier or the answer set or ApiFail
     */
    public int get_classes(int set_id) {
        
        if (!check_files("get_classes")) {
            return QClass.APIFail;
        }

        return simple_query(Simple_Query_Identifiers._GET_CLASSES, set_id);
    }
    
    
    /**
     * If set_id is 0, get classes of the ISA transitive closure of the classes of which
     * the current node is instance of. The answer set contains the system identifiers
     * of these classes.
     * 
     * If set_id is a positive integer, apply get_all_classes() on each object in
     * temporary set set_id.
     * 
     * @param set_id If set to 0 then apply function to current node. If set to
     * a positive integer then search internally for a set with this identifier
     * and apply this function to every object contained in the set
     *
     * @return the identifier or the answer set or ApiFail
     */
    public int get_all_classes(int set_id) {
        
        if (!check_files("get_all_classes")) {
            return QClass.APIFail;
        }

        return simple_query(Simple_Query_Identifiers._GET_ALL_CLASSES, set_id);
    }   
    
    
    /**
     * If set_id is 0, get the classes which are isA of the current node. The answer set
     * contains the system identifiers of these objects.
     * 
     * If set_id is a positive integer, apply get_subclasses() on each object in
     * temporary set set_id.
     * 
     * @param set_id If set to 0 then apply function to current node. If set to
     * a positive integer then search internally for a set with this identifier
     * and apply this function to every object contained in the set
     *
     * @return the identifier or the answer set or ApiFail
     */
    public int get_subclasses(int set_id) {
        
        if (!check_files("get_subclasses")) {
            return QClass.APIFail;
        }

        return simple_query(Simple_Query_Identifiers._GET_SUBCLASSES, set_id);
    }   
    
    /**
     * If set_id is 0, get the classes of the subclass-transitive closure of the classes that
     * are isA of the current node. The answer set contains the system identifiers of
     * these objects.
     * 
     * If set_id is a positive integer, apply get_all_subclasses() on each object in
     * temporary set set_id.
     * 
     * @param set_id If set to 0 then apply function to current node. If set to
     * a positive integer then search internally for a set with this identifier
     * and apply this function to every object contained in the set
     *
     * @return the identifier or the answer set or ApiFail 
     */
    public int get_all_subclasses(int set_id) {
        
        if (!check_files("get_all_subclasses")) {
            return QClass.APIFail;
        }

        return simple_query(Simple_Query_Identifiers._GET_ALL_SUBCLASSES, set_id);
    }   
    
    /**
     * If set_id is 0, get the classes that the current code is isA of. The answer set
     * contains the system identifiers of these objects.
     * 
     * If set_id is a positive integer, apply get_superclasses() on each object in
     * temporary set set_id.
     * 
     * @param set_id If set to 0 then apply function to current node. If set to
     * a positive integer then search internally for a set with this identifier
     * and apply this function to every object contained in the set
     *
     * @return the identifier or the answer set or ApiFail 
     */
    public int get_superclasses(int set_id) {
        
        if (!check_files("get_superclasses")) {
            return QClass.APIFail;
        }

        return simple_query(Simple_Query_Identifiers._GET_SUPERCLASSES, set_id);
    }  
    
    
    /**
     * If set_id is 0, get the classes of the isA transitive closure of the classes the
     * current no`de is isA of. The answer set contains the system identifiers of these
     * objects.
     * 
     * If set_id is a positive integer, apply get_all_superclasses() on each object in
     * temporary set set_id.
     * 
     * @param set_id If set to 0 then apply function to current node. If set to
     * a positive integer then search internally for a set with this identifier
     * and apply this function to every object contained in the set
     *
     * @return the identifier or the answer set or ApiFail 
     */
    public int get_all_superclasses(int set_id) {
        
        if (!check_files("get_all_superclasses")) {
            return QClass.APIFail;
        }

        return simple_query(Simple_Query_Identifiers._GET_ALL_SUPERCLASSES, set_id);
    }
    
    public void TEST_printIdsOfSet(int set_id){
        PQI_Set readSet =  this.tmp_sets.return_set(set_id);
        
        if(readSet!=null){
            
            for(long l : readSet.get_Neo4j_Ids()){
                Logger.getLogger(QClass.class.getName()).log(Level.INFO,l+", ");
            }
            
        }
    }
    
    public Vector<Long> TEST_get_SYSIDS_of_set(int set_id) {
        
        PQI_Set readSet =  this.tmp_sets.return_set(set_id);
        if(readSet!=null){
            Vector<Long>  returnResults = readSet.get_Neo4j_Ids();
            if(Configs.CastSysIdAsInt){
                Hashtable<Long, Integer> tmpRes = new Hashtable<Long, Integer>();
                db.getIntPropertyOfNodes(returnResults, db.Neo4j_Key_For_SysId, tmpRes);
                returnResults.clear();
                Enumeration<Long> hashEnum = tmpRes.keys();
                while(hashEnum.hasMoreElements()){
                    long curId = hashEnum.nextElement();
                    long newVal = (long) tmpRes.get(curId);
                    if(returnResults.contains(newVal)==false){
                        returnResults.add(newVal);
                    }
                }
                return returnResults;
            }
            else{
                Hashtable<Long, Long> tmpRes = new Hashtable<Long, Long>();
                db.getLongPropertyOfNodes(returnResults, db.Neo4j_Key_For_SysId, tmpRes);
                returnResults.clear();
                Enumeration<Long> hashEnum = tmpRes.keys();
                while(hashEnum.hasMoreElements()){
                    long curId = hashEnum.nextElement();
                    long newVal = tmpRes.get(curId);
                    if(returnResults.contains(newVal)==false){
                        returnResults.add(newVal);
                    }
                }
                return returnResults;
            }
        }
        
        return new Vector<Long>();        
    }

    public long TEST_get_SysId_From_Neo4jId(long neo4jId) {
        return db.TEST_get_SysId_From_Neo4jId(neo4jId);        
    }

    
    /**
     * Set the categories for any subsequent call of some special recursive query.
     * Whenever this function is called, any previous set of categories is deleted.
     * 
     * @param categories
     * @return 
     */
    public int set_categories(CategorySet[] categs){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        int set_categories(int sessionID, categories_set categs)
        
        typedef char name_buffer[INPUT_LOGINAM_SIZE];
        struct category_set {
            name_buffer fcl;
            name_buffer cat;
            int direction;  FORWARD, BACKWARD, BOTH_DIR 
        };
        typedef struct category_set categories_set[NUMBER_OF_CATEGORIES];
        
        //
	//  Set categories categs (see client_serv.h) for use by transitive
	//  and projection queries.
	//
	// WAS : int sis_api::set_categories(int cnt, categories_set categs)
        {
                // WAS : int i;
                int i, cnt;
                SYSID   tmpid;
                int     ret1 = 0,
                        ret2 = 0,
                        ret3 = 0,
                        ret4 = 0;

                // Added to normalise with the PQI CLIENT SERVER API
                // {
                //count # of categories
                for(cnt=0; (cnt<NUMBER_OF_CATEGORIES) && (categs[cnt].direction); cnt++);
                // }

                //SET   forward_set;   // store forward categs and subclasses
                //SET   backward_set;  // store backward categes and subclasses
                forward_set.set_clear();
                backward_set.set_clear();

                for(i=0; i<cnt; i++) {
                        LOGINAM tmpfcl(categs[i].fcl);
                        //SYSID categories[NUMBER_OF_CATEGORIES];
                        //int   directions[NUMBER_OF_CATEGORIES];

                        // getSysid(LOGINAM*, LOGINAM*) changed to getSysid(LOGINAM*, char*) 
                        categories[i] = getSysid(&tmpfcl, categs[i].cat);
                        tmpid = categories[i];
                        directions[i] = categs[i].direction;
                        if ((directions[i] == FORWARD) || (directions[i] == BOTH_DIR)) {
                                if (tmpid.id != 0) {
                                        ret1 = forward_set.set_put(tmpid);
                                        ret2 = getAllSubClasses(tmpid, &forward_set);
                                }
                        }
                        if ((directions[i] == BACKWARD) || (directions[i] == BOTH_DIR)) {
                                if (tmpid.id != 0) {
                                        ret3 = backward_set.set_put(tmpid);
                                        ret4 = getAllSubClasses(tmpid, &backward_set);
                                }
                        }
                        if ((ret1 == -1) || (ret2 == -1) || (ret3 == -1) || (ret4 == -1)) {
                                break;
                        }
                }

                if ((ret1 == -1) || (ret2 == -1) || (ret3 == -1) || (ret4 == -1)) {
                        forward_set.set_clear();
                        backward_set.set_clear();
                        num_of_categories = 0;
                        return -1;
                }
                num_of_categories  = cnt;
                return 0;
        }
        */
        // </editor-fold> 
        
        if(!check_files("set_categories")){
            return APIFail;
        }
        forward_set.set_clear();
        backward_set.set_clear();
        categories.clear();
        directions.clear();
        int ret1 =APIFail;
        int ret2 =APIFail;
        int ret3 =APIFail;
        int ret4 =APIFail;
        for(int i=0; i< categs.length; i++){
            CategorySet categ = categs[i];
            //String tmpfcl = categ.fcl;
            //categories[i] = getSysid(&tmpfcl, categs[i].cat);
            long tmpid = db.getSysid_For_Set_Categories(categ.fcl,categ.cat);
            if(tmpid==APIFail){
                ret1 = APIFail;
                //ret3 = APIFail;
                break;
            }
            else{
                ret1 = APISucc;
                ret3 = APISucc;
            }
            categories.add(tmpid);
            
            Traversal_Direction tmpDir = categs[i].direction;
            directions.add(tmpDir);
            if (tmpDir == Traversal_Direction.FORWARD || tmpDir==Traversal_Direction.BOTH_DIR) {
                
                forward_set.set_putNeo4j_Id(tmpid);//.add(new PQI_SetRecord(tmpid));
                //ret2 = getAllSubClasses(tmpid, &forward_set);
                ret2 = db.getAllSuper_or_AllSubClasses(tmpid,forward_set,false);
            }
            else{
                ret2 = APISucc;
            }
            if (tmpDir == Traversal_Direction.BACKWARD || tmpDir==Traversal_Direction.BOTH_DIR) {                
                backward_set.set_putNeo4j_Id(tmpid);//.add(new PQI_SetRecord(tmpid));
                //ret4 = getAllSubClasses(tmpid, &backward_set);                
                ret4 = db.getAllSuper_or_AllSubClasses(tmpid,backward_set,false);
            }
            else{
                ret4 = APISucc;
            }
            
            if ((ret1 == APIFail) || (ret2 == APIFail) || (ret3 == APIFail) || (ret4 == APIFail)) {
                    break;
            }
        }
        
        if ((ret1 == APIFail) || (ret2 == APIFail) || (ret3 == APIFail) || (ret4 == APIFail)) {
            forward_set.set_clear();
            backward_set.set_clear();
            categories.clear();
            directions.clear();
            return APIFail;
        }
        
        
        return APISucc;
    }

    /**
     * Before calling this query function some categories must have been defined with
     * the set_categories() function. If set_id is 0, starts from the current node and
     * traverses all the links whose category is one of those previously defined with
     * the set_categories() function. With the set_depth() function the depth of
     * traversing can be controlled. The isa argument can be any of UPWARDS,
     * DOWNWARDS, UP_DOWN or NOISA. If UPWARDS is used, then for each node visited
     * on traversing, all superclasses of this node are visited too. If DOWNWARDS is
     * used, then for each node visited on traversing, all subclasses of this node are
     * visited too. In case of UP_DOWN both all superclasses and all subclasses for
     * each node are traversed. Nothing of these two happens when NOISA is used.
     * The answer set contains the system identifiers of all the traversed links.
     * 
     * If set_id is a positive integer, apply get_traverse_by_category() on each
     * object in temporary set set_id.
     * 
     * @param set_id
     * @param isa
     * @return 
     * 
     */
    public int get_traverse_by_category(int set_id, Traversal_Isa isa){
        //check files
        if(!check_files("get_traverse_by_category")){
            /*
            fprintf(stderr," *** ERROR *** (get_traverse_by_category):Can not execute query"); \
            fprintf(stderr," - call begin_query() first\n");     
            */
            return APIFail;
        }
        
        //check if set categories has been called
        if(categories.size()==0){
            /*
            if (num_of_categories == 0) {
                fprintf(stderr,"*** ERROR *** qp::get_traverse_by_category : ");
                fprintf(stderr,"Set categories first \n");
                return -1;
            }
            */
            return APIFail;
        }
        
        int ret = APIFail;
        //skipped mode about tuple mechanism - do not know what it is
        
        //get a return set identifier
        int new_set_id = set_get_new();
        PQI_Set startingIds = new PQI_Set();
        if(set_id==0){
            startingIds.set_putNeo4j_Id(CurrentNode_Ids_Stack.lastElement());//.add(new PQI_SetRecord(CurrentNode_Ids_Stack.lastElement()));
        }
        else{
            PQI_Set readSet = this.tmp_sets.return_set(set_id);
            if(readSet==null){
                return APIFail;
            }            
            readSet.set_copy(startingIds);
        }
        Vector<Long> checked_set = new Vector<Long>();
        PQI_Set retSet = tmp_sets.return_set(new_set_id);
        //if(depth>=0){
        ret = db.getTraverseByCategoryWithDepthControl(startingIds,forward_set,backward_set,depth,isa,edge_set,retSet,checked_set);
        //}
        //else{
//            ret = db.getTraverseByCategory(startingIds,forward_set,backward_set,depth,isa,edge_set,tmp_sets.get(new_set_id),checked_set);
  //      }
        
        //ON_ERROR_RETURN(ret,new_set_id);  // free new_set_id and return -1
        if(ret==APIFail){
            //if ((globalError.flag()) || (ret == -1))  { 
            //      globalError.reset();
            //      tmp_sets.free_set(set_id);
            //      return -1;
            //}
            free_set(new_set_id);
            return APIFail;
        }
        return new_set_id;        
    }
    
    
    public int FOR_DELETE_get_traverse_by_category_With_SIS_Server_Implementation(int set_id, Traversal_Isa isa){
        // <editor-fold defaultstate="collapsed" desc="C++ code.">
        /*
        //  Get a set that contains all the links traversed, starting
        //  from the appling object and traversing all links which are
        //  instances of the categories previously defined with the
        //  set_categories() function.
        //  For each category, if the direction was set to FORWARD
        //  only the links pointing from the node are checked, if the
        //  direction was set to BACKWARD only the links pointing to
        //  the node are checked and if direction was set to BOTH DIR
        //  all links are checked.
        //  Cycle detection is performed.
        //  With the set_depth() command we can define the depth for
        //  the transitive query.
        //  isa defines whether for each node query will visit any of
        //  its superclasses or subclasses. If isa is NOISA then none of
        //  them is visited. If isa is UPWARDS then all superclasses of
        //  each node are visited, if isa is DOWNWARDS then all subclasses
        //  of each node are visited and if isa is UP_DOWN  all superclasses
        //  and all subclasses of each node are visited.
        //  Returns the set identifier where the answer is stored and
        //  ErrorCode when an error occured.
        int sis_api::get_traverse_by_category(int set_id, int isa)
        {
	int  new_set_id,
		ret;
	SET *writeset;
	SET *setptr;
	SET  checked_set;

	CHECK_FILES("get_traverse_by_category");

	if (num_of_categories == 0) {
		fprintf(stderr,"*** ERROR *** qp::get_traverse_by_category : ");
		fprintf(stderr,"Set categories first \n");
		return -1;
	}

	//Tuple mechanism 
	setptr=0;
	if (set_id) {
		if ((setptr = tmp_sets.return_set(set_id)) == NULL)
		{
			return -1;      // set doesn't exist...
		}
	}
	if ((setptr) && (setptr->set_tuple_mode()))
	{
		writeset=setptr;
		new_set_id=set_id;
	}
	else GET_NEW_SET_OR_RETURN(new_set_id,writeset);
	// new_set_id is the new set for storing the answer and writeset is the SET*


	if (set_id == 0) { // apply query to the current node
		if (no_current_node("get_traverse_by_category")) {
			tmp_sets.free_set(new_set_id);
			return -1;       // there is no current node set
		}
		edge_set.set_clear(); // Clear private member edge_set for re-calculation
		ret = getTraverseByCategory(*current_node, &forward_set, &backward_set,
			depth, isa, &edge_set, writeset, &checked_set);
	}
	else {
		if ((setptr = tmp_sets.return_set(set_id)) == NULL) {
			return -1;       // set doesn't exist...
		}
		edge_set.set_clear(); // Clear private member edge_set for re-calculation
		ret = getTraverseByCategory(setptr, &forward_set, &backward_set,
			depth, isa, &edge_set, writeset, &checked_set);
	}

	ON_ERROR_RETURN(ret,new_set_id);  // free new_set_id and return -1

	return new_set_id;
}
     
        */
        // </editor-fold>
        
        
        //local vars 
        //int  new_set_id,ret;
	//SET *writeset;
	//SET *setptr;
	//SET  checked_set;
        int ret = APIFail;
        PQI_Set checked_set = new PQI_Set();
                
        //check files
        if(!check_files("FOR_DELETE_get_traverse_by_category_With_SIS_Server_Implementation")){
            /*
            fprintf(stderr," *** ERROR *** (get_traverse_by_category):Can not execute query"); \
            fprintf(stderr," - call begin_query() first\n");     
            */
            return APIFail;
        }
        
        //check if set categories has been called
        if(categories.size()==0){
            /*
            if (num_of_categories == 0) {
                fprintf(stderr,"*** ERROR *** qp::get_traverse_by_category : ");
                fprintf(stderr,"Set categories first \n");
                return -1;
            }
            */
            return APIFail;
        }
        
        //skipped mode about tuple mechanism - do not know what it is
        
        //get a return set identifier
        int new_set_id = set_get_new();
        
        if(set_id==0){
            
            /*
            if (no_current_node("get_traverse_by_category")) {
			tmp_sets.free_set(new_set_id);
			return -1;       // there is no current node set
		}
		edge_set.set_clear(); // Clear private member edge_set for re-calculation
		ret = getTraverseByCategory(*current_node, &forward_set, &backward_set, depth, isa, &edge_set, writeset, &checked_set);
            */
            if(no_current_node("get_traverse_by_category")){
                free_set(new_set_id);
                return APIFail;
            }
            edge_set.set_clear();
            PQI_Set retSet = this.tmp_sets.return_set(new_set_id);
            
            ret = db.getTraverseByCategory_With_SIS_Server_Implementation(CurrentNode_Ids_Stack.lastElement(),forward_set,backward_set,depth,isa,edge_set,retSet,checked_set);
        }
        else{
            /*
            if ((setptr = tmp_sets.return_set(set_id)) == NULL) {
			return -1;       // set doesn't exist...
		}
		edge_set.set_clear(); // Clear private member edge_set for re-calculation
		ret = getTraverseByCategory(setptr, &forward_set, &backward_set,
			depth, isa, &edge_set, writeset, &checked_set);
            */
            PQI_Set targetSet = this.tmp_sets.return_set(set_id);
            if(targetSet==null){
                return APIFail;
            }
            edge_set.set_clear();
            PQI_Set retSet = this.tmp_sets.return_set(new_set_id);
            //PQI_Set writeset = tmp_sets.get(new_set_id);
            ret = db.getTraverseByCategory_With_SIS_Server_Implementation(targetSet,forward_set,backward_set,depth,isa,edge_set,retSet,checked_set);
            //System.exit(-1);
            //throw new UnsupportedOperationException();
            
        }
        //ON_ERROR_RETURN(ret,new_set_id);  // free new_set_id and return -1
        if(ret==APIFail){
            //if ((globalError.flag()) || (ret == -1))  { 
            //      globalError.reset();
            //      tmp_sets.free_set(set_id);
            //      return -1;
            //}
            free_set(new_set_id);
            return APIFail;
        }
        return new_set_id;
    }
    
    private boolean check_transaction(String function){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        #ifdef CLIENT_SERVER
 //  Extensively used macro at the beggining of each function to
 // check existnce of connection with the server
#define CHECK_FILES(x)  if(!query_session && !transaction_session){ \
	sprintf(Err.LastErrorOtherInfo, "QUERY ERROR : %s", (x));  \
	Err.SetErrorID(EC_CANNOT_EXECUTE_QUERY);  \
	return -1;\
};

#define CHECK_TRANSACTION(x)  if(!transaction_session){ \
	sprintf(Err.LastErrorOtherInfo, "UPDATE FUNCTION ERROR : %s", (x));  \
	Err.SetErrorID(EC_NOT_IN_TRANSACTION_MODE);  \
	return -1;\
};

#define FOR_EACH_ID_IN_SET(sid,st)    while((sid = st.set_get_id()).id)
#define FOR_EACH_ID_IN_SETPTR(sid,st) while((sid = st->set_get_id()).id)

#else

 //  Extensively used macro at the beggining of each function to
 //  check existnce in query session
 
#define CHECK_FILES(x)  if (!query_session && !transaction_session) {                              \
			 fprintf(stderr," *** ERROR *** (%s):Can not execute query",(x)); \
			 fprintf(stderr," - call begin_query() first\n");                 \
			 return -1;                                                       \
		  };
#define CHECK_TRANSACTION(x)  if (!transaction_session) {                              \
			 fprintf(stderr," *** ERROR *** (%s):Can not execute update function",(x)); \
			 fprintf(stderr," - call begin_transaction() first\n");                 \
			 return -1;                                                       \
		  };
        
        */
        // </editor-fold> 
        if(insideTransaction==false){
            //System.out.print();
            Logger.getLogger(QClass.class.getName()).log(Level.SEVERE, " *** ERROR *** ("+function+"):Can not execute update function"+" - call begin_transaction() first\n");
            return false;
        }
        return true;
    }
        
	
    private boolean check_files(String function) {
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        #ifdef CLIENT_SERVER
 //  Extensively used macro at the beggining of each function to
 // check existnce of connection with the server
#define CHECK_FILES(x)  if(!query_session && !transaction_session){ \
	sprintf(Err.LastErrorOtherInfo, "QUERY ERROR : %s", (x));  \
	Err.SetErrorID(EC_CANNOT_EXECUTE_QUERY);  \
	return -1;\
};

#define CHECK_TRANSACTION(x)  if(!transaction_session){ \
	sprintf(Err.LastErrorOtherInfo, "UPDATE FUNCTION ERROR : %s", (x));  \
	Err.SetErrorID(EC_NOT_IN_TRANSACTION_MODE);  \
	return -1;\
};

#define FOR_EACH_ID_IN_SET(sid,st)    while((sid = st.set_get_id()).id)
#define FOR_EACH_ID_IN_SETPTR(sid,st) while((sid = st->set_get_id()).id)

#else

 //  Extensively used macro at the beggining of each function to
 //  check existnce in query session
 
#define CHECK_FILES(x)  if (!query_session && !transaction_session) {                              \
			 fprintf(stderr," *** ERROR *** (%s):Can not execute query",(x)); \
			 fprintf(stderr," - call begin_query() first\n");                 \
			 return -1;                                                       \
		  };
#define CHECK_TRANSACTION(x)  if (!transaction_session) {                              \
			 fprintf(stderr," *** ERROR *** (%s):Can not execute update function",(x)); \
			 fprintf(stderr," - call begin_transaction() first\n");                 \
			 return -1;                                                       \
		  };
        */
        // </editor-fold> 
        
        if(insideQuery==false){
            //System.out.print();
            Logger.getLogger(QClass.class.getName()).log(Level.SEVERE, " *** ERROR *** ("+function+"):Can not execute query" + " - call begin_query() first\n");
            return false;
        }
        /*
        if (CurrentNode_Ids_Stack == null) {
            if (Configs.boolDebugInfo) {
                Logger.getLogger(QClass.class.getName()).log(Level.INFO, function+" called but not in a query");
            }
            return false;
        }*/
        /*
         if (this.nameStack == null) {
         if (boolDebugInfo) {
         Logger.getLogger(QClass.class.getName()).log(Level.INFO, "Not in a query");
         }
         return QClass.APIFail;
         }
         */
        return true;
    }

    private boolean no_current_node(String function) {
        if (CurrentNode_Ids_Stack != null && CurrentNode_Ids_Stack.size() > 0) {
            return false;
        } else {
            //fprintf(stderr,"*** WARNING *** ::%s: Set current node first \n",s);
            if (Configs.boolDebugInfo) {
                Logger.getLogger(QClass.class.getName()).log(Level.WARNING, "*** WARNING *** ::" + function + ": Set current node first.");
            }
            return true;
        }
        /*
         if (current_node->id != 0)
         {
         return 0;
         }
         else
         {
         fprintf(stderr,"*** WARNING *** ::%s: Set current node first \n",s);
         return 1;
         }
         */
    }

   
    private enum Simple_Query_Identifiers {
        /*NOT IMPLEMENTED*/
        _GET_FILTERED, _GET_SYSCLASS, _GET_ALL_SYSCLASSES, _GET_ALL_SYSSUPERCLASSES, 
        _GET_CLASS_ATTR, _GET_CLASS_ATTR_FROM, _GET_ALL_CLASS_ATTR,
        _GET_INHER_CLASS_ATTR, _GET_CATEGORY_FROM,_GET_CATEGORY_TO,
        
        /*IMPLEMENTED*/
        _GET_INHER_LINK_FROM, _GET_INHER_LINK_TO,
        _GET_CLASSES, _GET_ALL_CLASSES, 
        _GET_INSTANCES, _GET_ALL_INSTANCES, 
        _GET_SUPERCLASSES, _GET_ALL_SUPERCLASSES, 
        _GET_SUBCLASSES, _GET_ALL_SUBCLASSES, 
       
        _GET_LINK_FROM, _GET_FROM_VALUE, _GET_FROM_NODE, 
        _GET_LINK_TO,   _GET_TO_VALUE,  _GET_TO_NODE
    };

    private int simple_query_on_specific_id(Simple_Query_Identifiers q_id, long objSysid, PQI_Set writeset){
        switch (q_id) {
            case _GET_INSTANCES: {
                return db.NOT_USED_getInstances(objSysid, writeset);
            }
            case _GET_ALL_INSTANCES: {
                return db.getAllInstances(objSysid, writeset);
            }
            case _GET_LINK_FROM: {
                return db.getLinkFrom(objSysid, writeset);
            }
            case _GET_LINK_TO: {
                return db.getLinkTo(objSysid, writeset);
            }
            case _GET_CLASSES: {
                return db.getClasses(objSysid, writeset);
            }
            case _GET_ALL_CLASSES: {
                return db.getAllClasses(objSysid, writeset);
            }
            case _GET_SUPERCLASSES: {
                return db.getSuper_or_SubClasses(objSysid, writeset,true);
            }
            case _GET_SUBCLASSES: {
                return db.getSuper_or_SubClasses(objSysid, writeset,false);
            }
            case _GET_ALL_SUPERCLASSES: {
                return db.getAllSuper_or_AllSubClasses(objSysid, writeset,true);
            }
            case _GET_ALL_SUBCLASSES: {
                return db.getAllSuper_or_AllSubClasses(objSysid, writeset,false);
            }
            case _GET_FROM_VALUE:{
          
                long id = db.getFrom(objSysid, writeset);
                if(id>0){
                    writeset.set_putNeo4j_Id(id);//writeset.add(new PQI_SetRecord(id));
                    return APISucc;
                }
                return APIFail;
                
            }
            case _GET_TO_VALUE:{
                long id = db.getTo(objSysid, writeset);
                if(id>0){
                    writeset.set_putNeo4j_Id(id);
                    return APISucc;
                }
                return APIFail;
            }
            case _GET_FROM_NODE:{
                return db.getFromNode(objSysid, writeset);
            }
            case _GET_TO_NODE:{
                return db.getToNode(objSysid, writeset);
            }
            case _GET_INHER_LINK_FROM:{
                return db.getInherLinkFrom(objSysid, writeset);
            }
            default: {

                return APIFail;
            }
        }
        //return APIFail;
    }
    
    
    private int simple_query_on_SET(Simple_Query_Identifiers q_id, PQI_Set targetset, PQI_Set writeset){
        switch (q_id) {
            case _GET_INSTANCES: {
                return db.getInstancesSET(targetset, writeset);
            }
            case _GET_ALL_INSTANCES: {
                return db.getAll_Or_Simple_InstancesSET(targetset, writeset,true);
            }
            case _GET_LINK_FROM: {
                return db.getLinkFrom_Or_ToSET(targetset, writeset, true);
            }
            case _GET_LINK_TO: {
                return db.getLinkToSET(targetset, writeset);
            }
            case _GET_CLASSES: {
                return db.getClassesSET(targetset, writeset);
            }
            case _GET_ALL_CLASSES: {
                return db.getAll_Or_Simple_ClassesSET(targetset, writeset,true);
            }
            case _GET_SUPERCLASSES: {
                return db.getSuper_or_SubClassesSET(targetset, writeset,true);
            }
            case _GET_SUBCLASSES: {
                return db.getSuper_or_SubClassesSET(targetset, writeset,false);
            }
            case _GET_ALL_SUPERCLASSES: {
                return db.getAllSuper_or_AllSubClassesSET(targetset, writeset,true);
            }
            case _GET_ALL_SUBCLASSES: {
                return db.getAllSuper_or_AllSubClassesSET(targetset, writeset,false);
            }
            case _GET_FROM_VALUE:{
                return db.getFrom_Or_To_ValueSET(targetset, writeset,true);               
            }
            case _GET_TO_VALUE:{
                //return db.getToSET(targetset, writeset);                
                return db.getFrom_Or_To_ValueSET(targetset, writeset,false); 
            }
            case _GET_FROM_NODE:{
                return db.getFrom_Or_To_NodeSET(targetset, writeset, true);
            }
            case _GET_TO_NODE:{
                return db.getToNodeSET(targetset, writeset);
            }
            case _GET_INHER_LINK_FROM:{
                return db.getInherLinkFrom_Or_To_SET(targetset, writeset,true);
            }
            default: {
                 for(long newId : targetset.get_Neo4j_Ids()){           
                        if((simple_query_on_specific_id(q_id,newId,writeset)==APIFail)){
                            return APIFail;
                        }                
                    }
                return APIFail;
            }
        }
        //return APIFail;
    }

    private int simple_query(Simple_Query_Identifiers q_id, int set_id) {
        
        if(!check_files("categ_query")){
            return APIFail;
        }
        
        PQI_Set setptr = new PQI_Set();
        int new_set_id = set_get_new();
        if(new_set_id==APIFail){
            return APIFail;
        }
        PQI_Set writeset=tmp_sets.return_set(new_set_id);
        if(writeset==null){
            free_set(new_set_id);
            return APIFail;
        }
        int ret = QClass.APIFail;
        
        if (set_id == 0) {   
            // apply query on current node
            if (no_current_node("categ_query")) {
                tmp_sets.free_set(new_set_id);
                return APIFail;    // there is no current node set
            }
            
            setptr.set_putNeo4j_Id(CurrentNode_Ids_Stack.lastElement());
        }
        else{
            setptr.set_copy(tmp_sets.return_set(set_id));
        }
        
        //
        switch (q_id) {
            case _GET_ALL_CLASSES: {
                ret = db.getAll_Or_Simple_ClassesSET(setptr, writeset,true);
                break;
            }
            case _GET_ALL_INSTANCES: {
                ret = db.getAll_Or_Simple_InstancesSET(setptr, writeset,true);
                break;
            }
            case _GET_ALL_SUBCLASSES: {
                ret = db.getAllSuper_or_AllSubClassesSET(setptr, writeset,false);
                break;
            }
            case _GET_ALL_SUPERCLASSES: {
                ret = db.getAllSuper_or_AllSubClassesSET(setptr, writeset,true);
                break;
            }
            case _GET_CLASSES: {
                ret = db.getAll_Or_Simple_ClassesSET(setptr, writeset,false);
                break;
            }
            case _GET_FROM_NODE:{
                ret = db.getFrom_Or_To_NodeSET(setptr, writeset, true);
                break;
            }
            case _GET_FROM_VALUE:{
                ret = db.getFrom_Or_To_ValueSET(setptr, writeset,true);               
                break;
            }
            case _GET_INHER_LINK_FROM:{
                ret = db.getInherLinkFrom_Or_To_SET(setptr, writeset,true);
                break;
            }
            case _GET_INHER_LINK_TO:{
                ret = db.getInherLinkFrom_Or_To_SET(setptr, writeset,false);
                break;
            }
            case _GET_INSTANCES: {
                ret = db.getAll_Or_Simple_InstancesSET(setptr, writeset,false);
                break;
            }            
            case _GET_LINK_FROM: {
                ret = db.getLinkFrom_Or_ToSET(setptr, writeset, true);
                break;
            }
            case _GET_LINK_TO: {
                ret = db.getLinkFrom_Or_ToSET(setptr, writeset, false);
                break;
            }
            case _GET_SUBCLASSES: {
                ret = db.getSuper_or_SubClassesSET(setptr, writeset,false);
                break;
            }            
            case _GET_SUPERCLASSES: {
                ret = db.getSuper_or_SubClassesSET(setptr, writeset,true);
                break;
            }
            case _GET_TO_NODE:{
                ret = db.getFrom_Or_To_NodeSET(setptr, writeset, false);
                break;
            }
            case _GET_TO_VALUE:{                
                ret = db.getFrom_Or_To_ValueSET(setptr, writeset,false); 
                break;
            }
            
            default: {
                Logger.getLogger(QClass.class.getName()).log(Level.INFO, "UNSUPPORTED OPPERRATION:"+ q_id.name());
                 for(long newId : setptr.get_Neo4j_Ids()){                     
                     if((simple_query_on_specific_id(q_id,newId,writeset)==APIFail)){
                         return APIFail;
                     }                
                 }
                 
                 throw new UnsupportedOperationException();
            }
        }
        
        //ON_ERROR_RETURN(ret,new_set_id);  // free new_set_id and return -1
        if ((globalError.flag()==APIFail) || (ret == APIFail))  {
            globalError.reset();
            tmp_sets.free_set(new_set_id);
            return APIFail;
        }
        
        return new_set_id;
    }
    // simple_query
    // Used for queries that doesn't have any argument except of the set_id.
    //Returns the set identifier where the answer is stored and
    //  -1 when an error occured.
    // int q_id == Query Identifier
    // int set_id set_identifier
    private int OLD_simple_query(Simple_Query_Identifiers q_id, int set_id) {

        int new_set_id = this.set_get_new();
        int ret = QClass.APISucc;
        PQI_Set writeset = new PQI_Set();

        

        // new_set_id is the new set for storing the answer and writeset is the SET*
        if (set_id == 0) {
            // apply query on current node
            if (no_current_node("simple_query")) {
                //tmp_sets.free_set(new_set_id);
                free_set(new_set_id);
                return APIFail;    // there is no current node set
            }

            long objSysid = this.CurrentNode_Ids_Stack.lastElement();
            ret = simple_query_on_specific_id(q_id,objSysid,writeset);
            

        } else {       // apply query on each object in set set_id
            
            PQI_Set targetSet = this.tmp_sets.return_set(set_id);
            
            if(targetSet == null){
                return APIFail;
            }
            
            //Vector<Long> vals = targetSet.get_Neo4j_Ids();
            
            ret = simple_query_on_SET(q_id,targetSet,writeset);
            if(db.DebugInfo){
                Logger.getLogger(QClass.class.getName()).log(Level.INFO, "**********************************************************************");
                Logger.getLogger(QClass.class.getName()).log(Level.INFO, "ret value: "+ ret +"\nWriteSet values after simple_query_on_SET call: "+writeset.get_Neo4j_Ids());
                Logger.getLogger(QClass.class.getName()).log(Level.INFO, "**********************************************************************");
            }
            
        }

        if (ret == QClass.APIFail) {
            //ON_ERROR_RETURN(ret,new_set_id);  // free new_set_id and return -1
            //if globalerror.flag or ret --QClass.ApiFail {
            //global error reset
            //free set
            //return fail
            free_set(new_set_id);
            return QClass.APIFail;
        }
        
        //this.tmp_sets.put(new_set_id, writeset);
        //this.tmp_sets.return_set(new_set_id).set_clear();
        //this.tmp_sets.return_set(new_set_id).addAll(writeset);
        this.tmp_sets.return_set(new_set_id).set_copy(writeset);
        //writeset.set_copy(this.tmp_sets.return_set(new_set_id));
        

        return new_set_id;
        
        
        // <editor-fold defaultstate="collapsed" desc="C++ code.">
        /*
         int    new_set_id, ret = 0;
         SET   *writeset;
         SET   *setptr;

         // Tuple mechanism 
         setptr=0;
         if (set_id)
         {
         if ((setptr = tmp_sets.return_set(set_id)) == NULL)
         {
         return -1;      // set doesn't exist...
         }
         }
        
         if ((setptr) && (setptr->set_tuple_mode()))
         {
         writeset=setptr;
         new_set_id=set_id;
         }
         else
         GET_NEW_SET_OR_RETURN(new_set_id,writeset);
        
         // new_set_id is the new set for storing the answer and writeset is the SET*

         if (set_id == 0)
         {   // apply query on current node
         if (no_current_node("simple_query"))
         {
         tmp_sets.free_set(new_set_id);
         return -1;    // there is no current node set
         }

         switch (q_id) {
         case _GET_FILTERED           : ret = getFiltered(*current_node, &tmp_sets, &filter_cond, writeset);
         break;
         case _GET_CLASSES            : ret = getClasses(*current_node, writeset);
         break;
         case _GET_ALL_CLASSES        : ret = getAllClasses(*current_node, writeset);
         break;
         case _GET_SYSCLASS           : ret = getSysClass(*current_node, writeset);
         break;
         case _GET_ALL_SYSCLASSES     : ret = getAllSysClasses(*current_node, writeset);
         break;
         case _GET_INSTANCES          : ret = getInstances(*current_node, writeset);
         break;
         case _GET_ALL_INSTANCES      : ret = getAllInstances(*current_node, writeset);
         break;
         case _GET_SUPERCLASSES       : ret = getSuperClasses(*current_node, writeset);
         break;
         case _GET_ALL_SUPERCLASSES   : ret = getAllSuperClasses(*current_node, writeset);
         break;
         case _GET_ALL_SYSSUPERCLASSES: ret = getAllSysSuperClasses(*current_node, writeset);
         break;
         case _GET_SUBCLASSES         : ret = getSubClasses(*current_node, writeset);
         break;
         case _GET_ALL_SUBCLASSES     : ret = getAllSubClasses(*current_node, writeset);
         break;
         case _GET_LINK_FROM          : ret = getLinkFrom(*current_node, writeset);
         break;

         // queries on class attrs.
         case _GET_CLASS_ATTR_FROM    : ret = getClassAttrFrom(*current_node, writeset);
         break;
         case _GET_CLASS_ATTR         : ret = getClassAttrOf(*current_node, writeset);
         break;
         case _GET_ALL_CLASS_ATTR     : ret = getAllClassAttrOf(*current_node, writeset);
         break;
         case _GET_INHER_CLASS_ATTR    : ret = getInherClassAttr(*current_node, writeset);
         break;

         case _GET_INHER_LINK_FROM    : ret = getInherLinkFrom(*current_node, writeset);
         break;
         case _GET_INHER_LINK_TO      : ret = getInherLinkTo(*current_node, writeset);
         break;
         case _GET_LINK_TO            : ret = getLinkTo(*current_node, writeset);
         break;
         case _GET_CATEGORY_FROM      : ret = getCategoryFrom(*current_node, writeset);
         break;
         case _GET_CATEGORY_TO        : ret = getCategoryTo(*current_node, writeset);
         break;
         case _GET_TO_NODE            : ret = getToNode(*current_node, writeset);
         break;
         case _GET_FROM_NODE          : ret = getFromNode(*current_node, writeset);
         break;
         case _GET_TO_VALUE           : ret = DBACCESS_get_From_or_To(*current_node, writeset);
         break;
         case _GET_FROM_VALUE         : ret = getFrom(*current_node, writeset);
         break;

         }
         }
         else
         {       // apply query on each object in set set_id
         if ((setptr = tmp_sets.return_set(set_id)) == NULL)
         {
         // Free the allocated new set
         tmp_sets.free_set(new_set_id);
         return -1;      // set doesn't exist...
         }

         switch (q_id) {
         case _GET_FILTERED           : ret = getFiltered(setptr, &tmp_sets, &filter_cond, writeset);
         break;
         case _GET_CLASSES            : ret = getClasses(setptr, writeset);
         break;
         case _GET_ALL_CLASSES        : ret = getAllClasses(setptr, writeset);
         break;
         case _GET_SYSCLASS           : ret = getSysClass(setptr, writeset);
         break;
         case _GET_ALL_SYSCLASSES     : ret = getAllSysClasses(setptr, writeset);
         break;
         case _GET_INSTANCES          : ret = getInstances(setptr, writeset);
         break;
         case _GET_ALL_INSTANCES      : ret = getAllInstances(setptr, writeset);
         break;
         case _GET_SUPERCLASSES       : ret = getSuperClasses(setptr, writeset);
         break;
         case _GET_ALL_SUPERCLASSES   : ret = getAllSuperClasses(setptr, writeset);
         break;
         case _GET_ALL_SYSSUPERCLASSES: ret = getAllSysSuperClasses(setptr, writeset);
         break;
         case _GET_SUBCLASSES         : ret = getSubClasses(setptr, writeset);
         break;
         case _GET_ALL_SUBCLASSES     : ret = getAllSubClasses(setptr, writeset);
         break;
         case _GET_LINK_FROM          : ret = getLinkFrom(setptr, writeset);
         break;
         // queries on class attrs.
         case _GET_CLASS_ATTR_FROM    : ret = getClassAttrFrom(setptr, writeset);
         break;
         case _GET_CLASS_ATTR         : ret = getClassAttrOf(setptr, writeset);
         break;
         case _GET_ALL_CLASS_ATTR     : ret = getAllClassAttrOf(setptr, writeset);
         break;
         case _GET_INHER_CLASS_ATTR   : ret = getInherClassAttr(setptr, writeset);
         break;

         case _GET_INHER_LINK_FROM    : ret = getInherLinkFrom(setptr, writeset);
         break;
         case _GET_INHER_LINK_TO      : ret = getInherLinkTo(setptr, writeset);
         break;
         case _GET_LINK_TO            : ret = getLinkTo(setptr, writeset);
         break;
         case _GET_CATEGORY_FROM      : ret = getCategoryFrom(setptr, writeset);
         break;
         case _GET_CATEGORY_TO        : ret = getCategoryTo(setptr, writeset);
         break;
         case _GET_TO_NODE            : ret = getToNode(setptr, writeset);
         break;
         case _GET_FROM_NODE          : ret = getFromNode(setptr, writeset);
         break;
         case _GET_TO_VALUE           : ret = DBACCESS_get_From_or_To(setptr, writeset);
         break;
         case _GET_FROM_VALUE         : ret = getFrom(setptr, writeset);
         break;
         }
         }

         ON_ERROR_RETURN(ret,new_set_id);  // free new_set_id and return -1

         return new_set_id;
         }
         */
        // </editor-fold>
    }
    
    void NOT_USED_ChangeNodePropertyNeo4j_Id(Long Neo4j_Id, String NewLogicalname) {
        db.ChangeNodePropertyNeo4j_Id(Neo4j_Id, NewLogicalname);
    }
    
    void NOT_USED_ChangeNodeLabelNeo4j_Id(String OriginalLogicalname, String NewLogicalname) {
        db.ChangeNodeLabelNeo4j_Id(OriginalLogicalname, NewLogicalname);
    }
    
    /**
     * Adds a node with the logical name node_name at instantiation level level 
     * (SIS_API_TOKEN_CLASS,SIS_API_S_CLASS,SIS_API_M1_CLASS,SIS_API_M2_CLASS,
     * SIS_API_M3_CLASS,SIS_API_M4_CLASS).
     * 
     * @param node_name
     * @param level
     * @return 
     */
    public int CHECK_Add_Node(Identifier node_name, int level, boolean updateIdentifierWithId){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        START OF: sis_api::Add_Node 
        //  Add_Node :
        //Adds a node with the logical name name at instantiation level level.
        //        
        //Returns 1 on Success, -1 on Failure.
        //        
        // Reasons to fail : A node with given Logical Name & instantiation Level already exists.
        int sis_api::Add_Node(IDENTIFIER *node_name, int level)
        #ifdef CLIENT_SERVER
        {
                CHECK_TRANSACTION("Add_Node");
                int result;
                result = RFCache->InvalidateCache();
                if(result < 0) return result;  // Comms error

                // Send the function identifier
                if(SComm->send_int(  _ADD_NODE)<0)
                        return -1;
                // Send the IDENTIFIER node_name
                if(SComm->send_IDENTIFIER(  node_name) < 0)
                        return -1; // Failure on socket comms
                // Send the instantiation level
                if(SComm->send_int(  level) < 0)
                        return -1;

                // Get the result of the function on the server
                if(SComm->recv_int(&result) < 0)
                        return -1;

                return result;
        }
        #else
        {
                // In case of Add_Node IDENTIFIER can only be a logical name.
                if(node_name->tag != ID_TYPE_LOGINAM)
                        return APIFail;
                LOGINAM name(node_name->loginam);
                int result = api_sem_check->checkCurrentObject(&name, level);
                if (result == ERROR)  // -1 = ERROR
                        return APIFail;

                return APISucc;
        }
        #endif
        
        
        
        // --------------------------------------------------------------------------
// semanticChecker::checkCurrentObject --
// --------------------------------------------------------------------------

int	semanticChecker::checkCurrentObject( LOGINAM *currObjLn, int sysClass)
{
	SYSID	currS;

#ifdef DEBUG
	char    DEBUG_str[maxMessLen];
	strcpy( DEBUG_str, ">>> In checkCurrentObject(";
	translator->getString( currObjLn, DEBUG_str);
	printf( "%s,%d)\n", DEBUG_str, sysClass); 
#endif

// returns the sysid that has logical name the first param and its from- 
// object has sysid the second param, it returns 0 if the object is not  
// defined yet. In the next line the second param is SYSID(0) because
// the object under checking is an individual. 

	if ( (currS = translator->getSysid( currObjLn, SYSID(0))).id == 0) {

// searches in the symbol table to find if an object with the given logical
// name exists. If such an object does not exist next method creates one.
// Otherwise a type checking takes place 
// If its type is unresolved then it should be changed to the type determined 
// by the second param. If the type is resolved but different from this one 
// determined by the second param next method returns an error.
// Finally, if the type is identical to this determined by the second param
// the object is marked as resolved. 

		if ( createIndividualObject( currObjLn, sysClass) == ERROR) {
			globalError.putMessage( " >semanticChecker::checkCurrentObject");
			return( ERROR);
		}

		return( OK);
	}

// an entry for an object with the given logical name has been made into the symbol table 

	currObj = sysCatalog->loadObject( currS);
	if ( currObj == (T_O *)0) {
		errLoadObject( currS, ">semanticChecker::checkCurrentObject", translator);
		return( ERROR);
	}

	if ( currObj->IsUnresolved()) {
		if ( createIndividualObject( currObjLn, sysClass) == ERROR) {
			globalError.putMessage( " >semanticChecker::checkCurrentObject");
			return( ERROR);
		}

		return( OK);
	}

	// the object is resolved 
	int		oType;

	oType = getSysClassType( sysClass);
	if ( oType == ERROR) {
		errMsg( MISSING_IN_LEVEL, currObjLn, translator);
		return( ERROR);
	}
	if ( oType != currObj->o_type) {
		errMsg( TYPE_CONFLICT, currObjLn, translator);
		return( ERROR);
	}

	isLink = 0;

//	initUpdateSets();

	return( OK);
}

        */
        // </editor-fold> 
        
        if(!check_transaction("Add_Node")){
            return APIFail;
        }
        if(node_name==null || node_name.tag != Identifier.ID_TYPE_LOGINAM || node_name.getLogicalName()==null ||node_name.getLogicalName().length()==0){
            return APIFail;            
        }
        
        //check if name is ok
        if(api_sem_check.newNodeNameIsOk(node_name.getLogicalName())==false){
            return APIFail;
        }
        
        //check if node already exists
        if(db.getClassId(node_name.getLogicalName())!=APIFail){
            return APIFail;
        }
        
        
        //create the labels array that new node will be assigned
        Label levelLabel = getCurrentLevelLabelFromInt(level);
        Label[] labelArray = null;
        
        if(levelLabel!=null){
            labelArray = new Label[3];
            labelArray[0] = Configs.Labels.Common;
            labelArray[1] = Configs.Labels.Type_Individual;
            labelArray[2] = levelLabel;
        }
        else{
            return APIFail;                            
        }
        
        long newNeo4jId = db.getNextNeo4jId();
        
        try{
            
            Node newNode = db.graphDb.createNode(labelArray);
            //if(Configs.CastNeo4jIdAsInt){
              //  newNode.setProperty(db.Neo4j_Key_For_Neo4j_Id, Integer.parseInt(""+newNeo4jId));
            //}
            //else{
                newNode.setProperty(db.Neo4j_Key_For_Neo4j_Id, newNeo4jId);
            //}
            
            newNode.setProperty(db.Neo4j_Key_For_Logicalname, node_name.getLogicalName());
        
        }
        catch(Exception ex){
            utils.handleException(ex);
            return APIFail;
        }
        if(updateIdentifierWithId){
            node_name.value = newNeo4jId;
            node_name.tag = Identifier.ID_TYPE_SYSID;
        }
        return APISucc;
    }
        
    private boolean nodeLevelGreaterOrEqual(Node targetNode, int targetLevel){
        
        if(targetLevel==QClass.SIS_API_TOKEN_CLASS){
            if(targetNode.hasLabel(Configs.Labels.Token)){
                return true;
            }
            if(targetNode.hasLabel(Configs.Labels.S_Class)){
                return true;
            }
            if(targetNode.hasLabel(Configs.Labels.M1_Class)){
                return true;
            }
            if(targetNode.hasLabel(Configs.Labels.M2_Class)){
                return true;
            }
            if(targetNode.hasLabel(Configs.Labels.M3_Class)){
                return true;
            }
            if(targetNode.hasLabel(Configs.Labels.M4_Class)){
                return true;
            }
            return false;
        }
        
        if(targetLevel==QClass.SIS_API_S_CLASS){
            if(targetNode.hasLabel(Configs.Labels.S_Class)){
                return true;
            }
            if(targetNode.hasLabel(Configs.Labels.M1_Class)){
                return true;
            }
            if(targetNode.hasLabel(Configs.Labels.M2_Class)){
                return true;
            }
            if(targetNode.hasLabel(Configs.Labels.M3_Class)){
                return true;
            }
            if(targetNode.hasLabel(Configs.Labels.M4_Class)){
                return true;
            }
            return false;
        }
        
        if(targetLevel==QClass.SIS_API_M1_CLASS){
            if(targetNode.hasLabel(Configs.Labels.M1_Class)){
                return true;
            }
            if(targetNode.hasLabel(Configs.Labels.M2_Class)){
                return true;
            }
            if(targetNode.hasLabel(Configs.Labels.M3_Class)){
                return true;
            }
            if(targetNode.hasLabel(Configs.Labels.M4_Class)){
                return true;
            }
            return false;
        }
        
        if(targetLevel==QClass.SIS_API_M2_CLASS){
            if(targetNode.hasLabel(Configs.Labels.M2_Class)){
                return true;
            }
            if(targetNode.hasLabel(Configs.Labels.M3_Class)){
                return true;
            }
            if(targetNode.hasLabel(Configs.Labels.M4_Class)){
                return true;
            }
            return false;
        }
        
        if(targetLevel==QClass.SIS_API_M3_CLASS){
            if(targetNode.hasLabel(Configs.Labels.M3_Class)){
                return true;
            }
            if(targetNode.hasLabel(Configs.Labels.M4_Class)){
                return true;
            }
            return false;
        }
        
        if(targetLevel==QClass.SIS_API_M4_CLASS){
            if(targetNode.hasLabel(Configs.Labels.M4_Class)){
                return true;
            }
            return false;
        }
        
        return false;
    }
    
    private Label getNextLevelLabelFromInt(int targetLevel){
        if(targetLevel==QClass.SIS_API_TOKEN_CLASS){
            return Configs.Labels.S_Class; 
        }
        
        if(targetLevel==QClass.SIS_API_S_CLASS){
            return Configs.Labels.M1_Class; 
        }
        
        if(targetLevel==QClass.SIS_API_M1_CLASS){
            return Configs.Labels.M2_Class; 
        }
        
        if(targetLevel==QClass.SIS_API_M2_CLASS){
            return Configs.Labels.M3_Class; 
        }
        
        if(targetLevel==QClass.SIS_API_M3_CLASS){
            return Configs.Labels.M4_Class; 
        }
        
        
        return null;
    }
    
    private Label getCurrentLevelLabelFromInt(int targetLevel){
        if(targetLevel==QClass.SIS_API_TOKEN_CLASS){
            return Configs.Labels.Token; 
        }
        
        if(targetLevel==QClass.SIS_API_S_CLASS){
            return Configs.Labels.S_Class; 
        }
        
        if(targetLevel==QClass.SIS_API_M1_CLASS){
            return Configs.Labels.M1_Class; 
        }
        
        if(targetLevel==QClass.SIS_API_M2_CLASS){
            return Configs.Labels.M2_Class; 
        }
        
        if(targetLevel==QClass.SIS_API_M3_CLASS){
            return Configs.Labels.M3_Class; 
        }
        
        if(targetLevel==QClass.SIS_API_M4_CLASS){
            return Configs.Labels.M4_Class; 
        }
        return null;
    }
    
    /**
     * Adds a named attribute with the logical name attribute pointing from 
     * from to cm_value to at instantiation level iLevel (SIS_API_TOKEN_CLASS, 
     * SIS_API_S_CLASS,SIS_API_M1_CLASS,SIS_API_M2_CLASS,SIS_API_M3_CLASS,
     * SIS_API_M4_CLASS) with categories the categories given in the set 
     * identifier catSet. If catSet is -1 then no categories are used. 
     * The function fails if any of from or to does not exist, or any of 
     * iLevel or catSet is invalid
     * 
     * @param attribute
     * @param from
     * @param to
     * @param iLevel
     * @param catSet
     * @return 
     */
    public int CHECK_Add_Named_Attribute(Identifier attribute, Identifier from, CMValue to, int initialLevel, int catSet, boolean updateIdentifierWithId){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //  Add_Named_Attribute :
//		Adds a named attribute with the logical name IDENTIFIER *attribute pointing from
//		IDENTIFIER *from to cm_value *to at instantiation level iLevel with categories
//		the categories given in the set identifier catSet. If catSet is -1 then no
//		categories are used.
//		Returns 1 on Success, -1 on Failure.
//		Reasons to fail : 	A named attribute with the characteristics given
//							  		already exists.
int sis_api::Add_Named_Attribute(IDENTIFIER *attribute,IDENTIFIER *from, cm_value *to, int iLevel, int catSet)
#ifdef CLIENT_SERVER
{
	CHECK_TRANSACTION("Add_Named_Attribute");
	int result;
	result = RFCache->InvalidateCache();
	if(result < 0) return result;  // Comms error
	// Send the function identifier
	if(SComm->send_int(  _ADD_NAMED_ATTRIB) < 0)
		return -1;
	// Send the IDENTIFIER attribute
	if(SComm->send_IDENTIFIER(  attribute) < 0)
		return -1; // Failure on socket comms
	// Send the IDENTIFIER from
	if(SComm->send_IDENTIFIER(  from) < 0)
		return -1; // Failure on socket comms
	// Send the cmvalue to
	if(SComm->send_prs(  to) < 0)
		return -1;
	// Send the instantiation level
	if(SComm->send_int(  iLevel) < 0)
		return -1;
	// Send the set of categories identifier
	if(SComm->send_int(  catSet) < 0)
		return -1;

	// Get the result of the function on the server
	if(SComm->recv_int(  &result) < 0)
		return -1;

	return result;
}
#else
{
	LOGINAM  *tmplName1, *tmplName2;
	LOGINAM  *lAttribute, *lFrom;
	int       status;
	l_name    cCategory, cFrom;
	int       iCategory;
	char      buf[LOGINAM_SIZE+1];
	SYSID     *sysId;

	// Check preconditions
	if (attribute != 0 && (attribute->tag != ID_TYPE_LOGINAM || from->tag != ID_TYPE_SYSID))
		return APIFail;

	// Create a LOGINAM for 'attribute'
	if (attribute == 0)
		lAttribute = 0;
	else {
		if (GetLogicalName(attribute,&tmplName1) == APIFail)
			return APIFail;
		lAttribute = new LOGINAM(tmplName1->string(buf));
		if (tmplName1) delete tmplName1;
		if (lAttribute == 0)
			return APIFail;
	}

	// Create a LOGINAM for 'from'
	if (GetLogicalName(from,&tmplName2) == APIFail)
		return APIFail;
	lFrom = new LOGINAM(tmplName2->string(buf));
	if (tmplName2) delete tmplName2;
	if (lFrom == 0)
		return APIFail;

	// Create a prs_val for the 'to object' :
	// Copy the cm_value to given by the caller to a prs_val
	// which is required by the Semantic Checker.
	prs_val prsTo;
	LOGINAM tmp_loginam("empty");
	Copy_cm_value(&prsTo, to, &tmp_loginam);

	// Reset the flag indicating that we want to create a class attribute
	api_sem_check->resetClassAttrFlag();

	// Create the attribute object
	if (iLevel == -1) 
	{
		// In this case the default level, applyied by the semantic checker, will
		// be used for the link level.
		// set current object to be the from value
		SYSID From(from->sysid);
		if(api_sem_check->checkCurrentObject(From) == ERROR)
			return APIFail ;
		api_sem_check->initializeAttOf();
		api_sem_check->initUpdateSets();
		api_sem_check->reset_toValuesOfUnamedLinks();
		api_sem_check->setUpPointers();

		if(api_sem_check->isLinkCurrObj())
		{
			if(api_sem_check->setUpCurrentObject() == ERROR)
				return APIFail ;
		}
		reset_set(catSet);
		if(catSet != -1)
		{
			FULATTNAM cat;
			while (return_categ_ids(catSet, &iCategory, cFrom, cCategory) != -1)
			{
				if (constructFULATTNAM(iCategory, cCategory, &cat) != -1)
				{
					status = api_sem_check->updateCategories(NULL, &cat);
				}
				else
					status = ERROR;
				// pk            free_set(fromSet);
				if (status == ERROR) return APIFail;
			}
		}
		else
		{
			api_sem_check->setUpAttributeCategory();
		}
		// Add the link
		status = api_sem_check->addAttributes(lAttribute, &prsTo);
		if(status == ERROR)
		{
			// addAttributes failed
			goto Add_Named_Attribute_Exit_Point;
		}
	}
	else
	{
		// The link will be added at level iLevel
		status = api_sem_check->checkAttributeObject(lAttribute, iLevel, lFrom, &prsTo);
		if(status == ERROR)
		{
			// checkAttributeObject
			goto Add_Named_Attribute_Exit_Point;
		}	
		// Add categories if needed
		if (catSet == -1)
		{
			api_sem_check->setUpAttributeCategory();
			api_sem_check->checkLinkObject();
		}
		else
		{
			api_sem_check->initUpdateSets();
			reset_set(catSet);
			while (return_categ_ids(catSet, &iCategory, cFrom, cCategory) != -1)
			{
				sysId = new SYSID(iCategory);
				status = api_sem_check->updateInstanceOf(0, sysId);
				delete sysId;
				if (status == ERROR) return APIFail;   // ERROR = -1
			}
			if(api_sem_check->executeUpdateCommands() == ERROR)
				return APIFail;  // ERROR = -1
			if(api_sem_check->setUpCurrentObject() == ERROR) // ERROR = -1
				return APIFail;
			api_sem_check->initializeAttOf();
			api_sem_check->resetClassAttrFlag();
			api_sem_check->refreshNames();
		}
	}	
Add_Named_Attribute_Exit_Point:
	if (lAttribute)
		delete lAttribute;
	if (lFrom)
		delete lFrom;
	if (status == ERROR) // ERROR = -1
		return APIFail;

	return APISucc;
}
#endif
        */
        // </editor-fold> 
        
        if(!check_transaction("Add_Named_Attribute")){
            return APIFail;
        }
        if(attribute ==null || from ==null){
            return APIFail;
        }
        // Check preconditions
	if (attribute != null && attribute.tag != Identifier.ID_TYPE_LOGINAM || from.tag != Identifier.ID_TYPE_SYSID){
            return APIFail;
        }
        //added by elias
        if(attribute.getLogicalName()==null || attribute.getLogicalName().length() ==0 || to==null ){
            return APIFail;
        }
        
        
        
        if(!api_sem_check.newNamedAttributeNameIsOk(attribute.getLogicalName())){
            return APIFail;
        }
        
        //check if from and to value exist
        //check if from and to value are of >= than iLevel         
        //check categories if catSet !=-1 then everything in cat set must be of iLevel i+1 and of type attribute
        //check if from -> attribute combination exists       
        
        //check if from and to value exist
        PrimitiveObject_Long fromId = new PrimitiveObject_Long();
        if(GetSYSID(from, fromId)==APIFail){
            return APIFail;
        }
        
        Node fromNode = db.getNeo4jNodeByNeo4jId(fromId.getValue());
        if(fromNode ==null){
            return APIFail;
        }
        
        Node toNode = null;
        if(to.type == CMValue.TYPE_NODE){
            toNode = db.getNeo4jNodeByNeo4jId(to.getSysid());
            if(toNode == null){
                return APIFail;
            }
        }
         
        
        //from and to nodes exist
        int iLevel = initialLevel;
        if(initialLevel==-1){
            //apply the label of its from value
            if(fromNode.hasLabel(Configs.Labels.Token)){
                iLevel = QClass.SIS_API_TOKEN_CLASS;
            }
            else if(fromNode.hasLabel(Configs.Labels.S_Class)){
                iLevel = QClass.SIS_API_S_CLASS;
            }
            else if(fromNode.hasLabel(Configs.Labels.M1_Class)){
                iLevel = QClass.SIS_API_M1_CLASS;
            }
            else if(fromNode.hasLabel(Configs.Labels.M2_Class)){
                iLevel = QClass.SIS_API_M2_CLASS;
            }
            else if(fromNode.hasLabel(Configs.Labels.M3_Class)){
                iLevel = QClass.SIS_API_M3_CLASS;
            }
            else if(fromNode.hasLabel(Configs.Labels.M4_Class)){
                iLevel = QClass.SIS_API_M4_CLASS;
            }
            
        }
        if(iLevel!=-1){ //case of Telos_Object - > attribute
            //check if from and to value are of >= than iLevel
            if(!nodeLevelGreaterOrEqual(fromNode, iLevel) ){
                return APIFail;
            }

            if(toNode!=null){
                if(!nodeLevelGreaterOrEqual(toNode, iLevel) ){
                    return APIFail;
                }
            }
        }
        Label levelLabel = getCurrentLevelLabelFromInt(iLevel);
        Vector<Node> categoryNodes = null;
        
        if(catSet>0 && levelLabel!=null){
            //check categories if catSet !=-1 then everything in cat set must be of iLevel i+1 and of type attribute
            PQI_Set categSet = this.tmp_sets.return_set(catSet);
            if(categSet==null){
                return APIFail;                
            }
            
            Label categLevel = getNextLevelLabelFromInt(iLevel);
            if(categLevel==null){
                return APIFail;
            }
            Vector<Long> categories = categSet.get_Neo4j_Ids();
            categoryNodes = db.getNeo4jNodesByNeo4jIds(categories);
           
            for(Node categNode : categoryNodes){
                if(categNode.hasLabel(categLevel)==false || categNode.hasLabel(Configs.Labels.Type_Attribute)==false){
                    return APIFail;
                }
            }
        }
        else{
            categoryNodes = new Vector<Node>();
            if(levelLabel!=null){
                categoryNodes.add(db.GetTelosObjectAttributeNode());
            }
        }
        
        Label[] labelArray = null;
        
        if(levelLabel!=null){
            labelArray = new Label[3];
            labelArray[0] = Configs.Labels.Common;
            labelArray[1] = Configs.Labels.Type_Attribute;
            labelArray[2] = levelLabel;
        }
        else{
            labelArray = new Label[2];
            labelArray[0] = Configs.Labels.Common;
            labelArray[1] = Configs.Labels.Type_Attribute;
            //return APIFail;                            
        }
        
        //check if from -> attribute combination exists   
        Iterator<Relationship> fromNodeRelIter = fromNode.getRelationships(Configs.Rels.RELATION, Direction.OUTGOING).iterator();
        while(fromNodeRelIter.hasNext()){
            String attrLoginam = (String)fromNodeRelIter.next().getEndNode().getProperty(db.Neo4j_Key_For_Logicalname);
            if(attrLoginam.equals(attribute.getLogicalName())){
                return APIFail;
            }
        }
        
        long newNeo4jId = db.getNextNeo4jId();
        
        
        try{
            
            Node newNode = db.graphDb.createNode(labelArray);
            //if(Configs.CastNeo4jIdAsInt){
              //  newNode.setProperty(db.Neo4j_Key_For_Neo4j_Id, Integer.parseInt(""+newNeo4jId));
            //}
            //else{
                newNode.setProperty(db.Neo4j_Key_For_Neo4j_Id, newNeo4jId);
            //}
            newNode.setProperty(db.Neo4j_Key_For_Logicalname, attribute.getLogicalName());


            fromNode.createRelationshipTo(newNode, Configs.Rels.RELATION);
            if(toNode!=null){
                newNode.createRelationshipTo(toNode, Configs.Rels.RELATION);
            }
            else{
                if(to.type == CMValue.TYPE_INT){
                    newNode.setProperty(Configs.Key_Primitive_Value_Type, Configs.Primitive_Value_Type_INT);
                    newNode.setProperty(Configs.Key_Primitive_Value, to.getInt());
                }
                else if(to.type == CMValue.TYPE_STRING){
                    newNode.setProperty(Configs.Key_Primitive_Value_Type, Configs.Primitive_Value_Type_STR);
                    newNode.setProperty(Configs.Key_Primitive_Value, to.getString());
                }
            }

            //create instance of relationships
            if(categoryNodes.size()>0){
                for(Node n: categoryNodes){
                    newNode.createRelationshipTo(n, Configs.Rels.INSTANCEOF);
                }
                
            }
        }
        catch(Exception ex){
            Logger.getLogger(QClass.class.getName()).log(Level.INFO, ex.getMessage());
            utils.handleException(ex);
            return APIFail;
        }
        if(updateIdentifierWithId){
            attribute.value = newNeo4jId;
            attribute.tag = Identifier.ID_TYPE_SYSID;
        }
        
        return APISucc;
    }
    
    /**
     * Adds an unnamed attribute pointing from from to cm_value to with 
     * categories the categories given in the set identified by catSet.
     * 
     * If catSet is -1 then no categories are used. The function fails 
     * if any of from or to does not exist, or catSet is invalid.
     * 
     * @param from
     * @param to
     * @param catSet
     * @return 
     */
    public int CHECK_Add_Unnamed_Attribute(Identifier from, CMValue to, int catSet){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
START OF: sis_api::Add_Unnamed_Attribute 
//  Add_Unnamed_Attribute :
//  Adds a unnamed attribute pointing from IDENTIFIER * from to cm_value * to
//  with categories the categories given in the set identified by catSet.
//  If catSet is -1 then no categories are used.
//        
//  Returns 1 on Success, -1 on Failure.
//        
//  Reasons to fail :	
//  An unnamed attribute with the characteristics given already exists.
        
int sis_api::Add_Unnamed_Attribute(IDENTIFIER * from, cm_value * to, int catSet)
#ifdef CLIENT_SERVER
{
	CHECK_TRANSACTION("Add_Unnamed_Attribute");
	int result;
	result = RFCache->InvalidateCache();
	if(result < 0) return result;  // Comms error
	// Send the function identifier
	if(SComm->send_int(  _ADD_UNNAMED_ATTRIB) < 0)
		return -1;
	// Send the IDENTIFIER from
	if(SComm->send_IDENTIFIER(  from) < 0)
		return -1; // Failure on socket comms
	// Send the cmvalue to
	if(SComm->send_prs(  to) < 0)
		return -1;
	// Send the set of categories identifier
	if(SComm->send_int(  catSet) < 0)
		return -1;

	// Get the result of the function on the server
	if(SComm->recv_int(  &result) < 0)
		return -1;

	return result;
}
#else
{
	LOGINAM *lFromObject;
	LOGINAM *lToNode;
	prs_val *prsTo;
	int      status;
	l_name   cCategory, cFrom;
	int      iCategory;

	// Check if the given From is a sysid. This is done to ensure that the from
	// object is unique. This is neccesary because the from object could be a
	// a link.
	if(from->tag != ID_TYPE_SYSID) return APIFail;

	// Retrieve the logical name of from
	if (GetLogicalName(from,&lFromObject) == APIFail)
		return APIFail;

	// Set the current object
	api_sem_check->resetClassAttrFlag();
	status = api_sem_check->checkCurrentObject(lFromObject);
	if (lFromObject) delete lFromObject;
	if(status == ERROR) return APIFail; // ERROR = -1
	// If the current object is a link call setupCurrentObject
	if (api_sem_check->isLinkCurrObj()) if (api_sem_check->setUpCurrentObject() == ERROR) // ERROR = -1
		return APIFail;

	// Initialize the update of the current object
	api_sem_check->initializeAttOf();
	api_sem_check->reset_toValuesOfUnamedLinks();
	api_sem_check->setUpPointers();

	// Add categories if needed
	if (catSet == -1)
		api_sem_check->setUpAttributeCategory();
	else {
		LOGINAM *lCategory;
		reset_set(catSet);
		while (return_categ_ids(catSet, &iCategory, cFrom, cCategory) != -1) {
			if ((lCategory = api_translator->getFulName(iCategory)) == 0)
				return APIFail;
			status = api_sem_check->updateCategories(0,lCategory);
			delete lCategory;
			if (status == ERROR) return APIFail; // ERROR = -1
		}
	}

	// Add the attribute
	// CAUTION:  The last argument of semanticChecker::changeAttributes should
	// contain a pointer to LOGINAM! So, we must check the type of 'to': if it
	// contains a LOGINAM pointer then everything is fine, but if it does not
	// we should retrieve the corresponding LOGINAM and create a prs_val from it
	if (to->sysid  && to->type!=TYPE_NODE) {
		if ((lToNode = api_translator->getFulName(SYSID(to->sysid))) == 0)
			return APIFail;
		if ((prsTo = new prs_val(lToNode)) == 0)
			return APIFail;
	}
	else {
		if ((lToNode = new LOGINAM("empty")) == 0)
			return APIFail;
		if ((prsTo = new prs_val) == 0)
			return APIFail;
		Copy_cm_value(prsTo, to, lToNode);
	}

	status = api_sem_check->addAttributes(0, prsTo);
	// Final Clean-up
	delete prsTo;
	delete lToNode;
	api_sem_check->refreshResolvedCats();
	api_sem_check->refreshNames();
	api_sem_check->resetClassAttrFlag();

	return  status == ERROR ? APIFail : APISucc; // ERROR = -1
}
#endif  
        
        
        
        // --------------------------------------------------------------------------
// semanticChecker::checkCurrentObject --
//	It checks whether or not an object with given logical name has been
// already defined and if the first happens then updates the variables of
// semantic checker
// --------------------------------------------------------------------------
int	semanticChecker::checkCurrentObject( LOGINAM *currObjLn)
{
	SYSID	currS;
	int		status;

#ifdef DEBUG
	char    DEBUG_str[maxMessLen];
	DEBUG_str[0] = '\0';
	translator->getString( currObjLn, DEBUG_str);
	printf( ">>> In checkCurrentObject( %s, 0x%x)\n", DEBUG_str);
#endif

	//Check if not exists return error
	//Check if not unique return error


	if ( (status = translator->getSysid( currObjLn, &currS)) == NOT_EXIST) {
		errMsg( NOT_CREATE_OBJ, currObjLn, translator);
		return( ERROR);
	}
	if ( status == NOT_UNIQUE) {
		errMsg( "(>semanticChecker::checkCurrentObject) the object %s is not unique in the DB.\n", currObjLn, translator);
		return( ERROR);
	}
	if ( status == ERROR) {
		globalError.putMessage( " >semanticChecker::checkCurrentObject");
		return( ERROR);
	}

	// an entry for an object with the given logical name
	// has been made into the symbol table

	currObj = sysCatalog->loadObject( currS);
	if ( currObj == (T_O *)0) {
		errLoadObject( currS, ">semanticChecker::checkCurrentObject", translator);
		return( ERROR);
	}

	if ( currObj->IsUnresolved()) {
		errMsg( NOT_RETELL_UNRES_OBJ, currObjLn, translator);
		return( ERROR);
	}

	if ( O_IsLink( currObj->o_type)) {
		loSys = currS;
		currLinkObj = currObj;
		currS = currLinkObj->getFrom();
		currObj = sysCatalog->loadObject( currS);
		if ( currObj == (T_O *)0) {
			errLoadObject( currS, ">semanticChecker::checkCurrentObject", translator);
			return( ERROR);
		}
		setUpPointers();
		isLink = 1;
	} else
		isLink = 0;

	return( OK);
}
        */
        // </editor-fold> 
        
        if(!check_transaction("Add_Unnamed_Attribute")){
            return APIFail;
        }
        
        if(from==null || to==null){
            return APIFail;
        }
        
        if(from.tag != Identifier.ID_TYPE_SYSID) {
            return APIFail;
        }
        
        // <editor-fold defaultstate="collapsed" desc="no attribute to attribute allowed in this token level">
        //if (GetLogicalName(from,&lFromObject) == APIFail)
		//return APIFail;

        /*
	// Set the current object
	api_sem_check->resetClassAttrFlag();
	status = api_sem_check->checkCurrentObject(lFromObject);
        if (api_sem_check->isLinkCurrObj()) if (api_sem_check->setUpCurrentObject() == ERROR) // ERROR = -1
		return APIFail;
        
        // --------------------------------------------------------------------------
        // semanticChecker::setUpCurrentObject --
        // --------------------------------------------------------------------------

        int	semanticChecker::setUpCurrentObject()
        {
        #ifdef DEBUG
                printf(">>> In setUpCurrentObject()\n");
        #endif

                if ( (currObj = sysCatalog->loadObject( loSys)) == (T_O *)0) {
                        errLoadObject( loSys, " >semanticChecker::setUpCurrentObject", translator);
                        return( ERROR);
                }
                return( OK);
        }
        */
        // </editor-fold>
        
        Node fromNode =db.getNeo4jNodeByNeo4jId(from.getSysid());
        //Check if from value exists
        if(fromNode==null){
            return APIFail;
        }
        
        //Check if from value is an attribute of token level
        if(fromNode.hasLabel(Configs.Labels.Type_Attribute) && fromNode.hasLabel(Configs.Labels.Token)){
            return APIFail;
        }
        
        Node toNode = null;
        if(to.type == CMValue.TYPE_NODE){
            toNode = db.getNeo4jNodeByNeo4jId(to.getSysid());
            if(toNode == null){
                return APIFail;
            }
        }
        
        
        
        // <editor-fold defaultstate="collapsed" desc="Add categories if needed">
        
        /*
	if (catSet == -1)
		api_sem_check->setUpAttributeCategory();
	
	// ---------------------------------------------------------------------------
	// semanticChecker::setUpAttributeCategory --
	// Declare the current link object as an instance of attribute category
	// ---------------------------------------------------------------------------
	//  int	semanticChecker::setUpAttributeCategory(){
        //      if ( resolvedCats.insertRef( SYSID(ATTRIBUTE_CLASS_ID)) == ERROR) {
	//		globalError.putMessage( ERROR_RESOLVED_CAT);
	//		return( ERROR);
	//	}
	//	return( OK);
	//}
	
	else {
		LOGINAM *lCategory;
		reset_set(catSet);
		while (return_categ_ids(catSet, &iCategory, cFrom, cCategory) != -1) {
			if ((lCategory = api_translator->getFulName(iCategory)) == 0)
				return APIFail;
			status = api_sem_check->updateCategories(0,lCategory);
			delete lCategory;
			if (status == ERROR) return APIFail; // ERROR = -1
		}
	}
*/
        // </editor-fold>
        
        //unnamed atteibutes only in token level
        Label levelLabel = Configs.Labels.Token;
        Vector<Node> categoryNodes = null;
        Vector<Long> categories = new Vector<Long>();
        if(catSet>0){
            //check categories if catSet !=-1 then everything in cat set must be of iLevel i+1 and of type attribute
            PQI_Set categSet = this.tmp_sets.return_set(catSet);
            if(categSet==null){
                return APIFail;                
            }
            // unnameed attribute should all be instaces of s_class
            Label categLevel = Configs.Labels.S_Class;
            if(categLevel==null){
                return APIFail;
            }
            categories = categSet.get_Neo4j_Ids();
            categoryNodes = db.getNeo4jNodesByNeo4jIds(categories);
           
            for(Node categNode : categoryNodes){
                if(categNode.hasLabel(categLevel)==false || categNode.hasLabel(Configs.Labels.Type_Attribute)==false){
                    return APIFail;
                }
            }
        }
        else{
            // unnameed attribute should all be instaces of s_class THIS DOES not stand here
            categoryNodes = new Vector<Node>();
            categoryNodes.add(db.GetTelosObjectAttributeNode());
            categories.add(db.getNodeNeo4jId(categoryNodes.get(0)));
        }
        
        /*
        // Add the attribute
	// CAUTION:  The last argument of semanticChecker::changeAttributes should
	// contain a pointer to LOGINAM! So, we must check the type of 'to': if it
	// contains a LOGINAM pointer then everything is fine, but if it does not
	// we should retrieve the corresponding LOGINAM and create a prs_val from it
	if (to->sysid  && to->type!=TYPE_NODE) {
		if ((lToNode = api_translator->getFulName(SYSID(to->sysid))) == 0)
			return APIFail;
		if ((prsTo = new prs_val(lToNode)) == 0)
			return APIFail;
	}
	else {
		if ((lToNode = new LOGINAM("empty")) == 0)
			return APIFail;
		if ((prsTo = new prs_val) == 0)
			return APIFail;
		Copy_cm_value(prsTo, to, lToNode);
	}

	status = api_sem_check->addAttributes(0, prsTo);
        */
        
        //get a new name for the attribute and check if this new name exists in the database
        
        
        // if to cmv is of type node
        // check if another unnammed attribute exists from the from node 
        // to the to node which is of the same categories
        
        //create the attribute 
        
        //check if from and to value exist (if to is node not primitive)
        //check if from and to value are of >= than iLevel (if to is node not primitive) -- SKIP this always happens for tokens
        //check categories if catSet !=-1 then everything in cat set must be of S_Class and of type attribute
        //check if from -> attribute combination exists
        
        
        
         
        
        //from and to nodes exist
        
        Label[] labelArray = null;
        
        if(levelLabel!=null){
            labelArray = new Label[3];
            labelArray[0] = Configs.Labels.Common;
            labelArray[1] = Configs.Labels.Type_Attribute;
            labelArray[2] = levelLabel;
        }
        else{
            return APIFail;                            
        }
        
        
        
        //check if from value has unnammed link to to value with the same characteristics
        Iterator<Relationship> fromNodeRelIter = fromNode.getRelationships(Configs.Rels.RELATION, Direction.OUTGOING).iterator();
        while(fromNodeRelIter.hasNext()){
            Node attrNode = fromNodeRelIter.next().getEndNode();
            String attrLoginam = (String)attrNode.getProperty(db.Neo4j_Key_For_Logicalname);
            //only search unnammed attrbitues
            if(attrLoginam.matches(Configs.regExForUnNamed)==false){
                continue;
            }
            Vector<Long> attrClasses = getClassesOfNode(attrNode);
            if(attrClasses.containsAll(categories) ==false ||  categories.containsAll(attrClasses)==false){
                continue;
            }
            
            
            //now check if they are pointing to the same to value (node or primitive
            if(to.type==CMValue.TYPE_INT){
                if(attrNode.hasProperty(Configs.Key_Primitive_Value_Type) && attrNode.hasProperty(Configs.Key_Primitive_Value)){
                    String type = (String) attrNode.getProperty(Configs.Key_Primitive_Value_Type);
                    if(type.equals(Configs.Primitive_Value_Type_INT) ==false){
                        continue;
                    }
                    int   value = (int) attrNode.getProperty(Configs.Key_Primitive_Value);
                    if(to.getInt()==value){
                        //Node with the same characteristics exists
                        //return APIFail;
                        //ELIAS BUGFIX 2015-09-24
                        return APISucc;
                    }
                    continue;                    
                }
            }
            else if (to.type == CMValue.TYPE_STRING){
                if(attrNode.hasProperty(Configs.Key_Primitive_Value_Type) && attrNode.hasProperty(Configs.Key_Primitive_Value)){
                    String type = (String) attrNode.getProperty(Configs.Key_Primitive_Value_Type);
                    if(type.equals(Configs.Primitive_Value_Type_STR)==false){
                        continue;
                    }
                    String value = (String) attrNode.getProperty(Configs.Key_Primitive_Value);
                    if(to.getString().equals(value)){
                        //Node with the same characteristics exists
                        //return APIFail;
                        //ELIAS BUGFIX 2015-09-24
                        return APISucc;
                    }
                    continue;                    
                }
            }
            else if (to.type==CMValue.TYPE_NODE){
                if(attrNode.hasRelationship(Configs.Rels.RELATION, Direction.OUTGOING)==false){
                    continue;
                }
                long targetNeo4jId = to.getSysid();
                
                for(Relationship attrRel: attrNode.getRelationships(Configs.Rels.RELATION, Direction.OUTGOING)){
                    long toNodeId = db.getNodeNeo4jId(attrRel.getEndNode());
                    if(targetNeo4jId==toNodeId){
                        // API fail because it hasthe same characteristics
                        //return APIFail;
                        //ELIAS BUGFIX 2015-09-24
                        return APISucc;
                    }
                }
                
            }
        }
        
        long newNeo4jId = db.getNextNeo4jId();
        String newLogicalName = api_sem_check.getNewUnnamedAttributeLogicalName(newNeo4jId);
                
        try{
            
            Node newNode = db.graphDb.createNode();
            for(Label lbl : labelArray){
                newNode.addLabel(lbl);
            }
            newNode.setProperty(db.Neo4j_Key_For_Logicalname, newLogicalName);
            //newNode..createNode(labelArray);
            fromNode.createRelationshipTo(newNode, Configs.Rels.RELATION);
            if(toNode!=null){
                newNode.createRelationshipTo(toNode, Configs.Rels.RELATION);
            }
            else{
                if(to.type == CMValue.TYPE_INT){
                    newNode.setProperty(Configs.Key_Primitive_Value_Type, Configs.Primitive_Value_Type_INT);
                    newNode.setProperty(Configs.Key_Primitive_Value, to.getInt());
                }
                else if(to.type == CMValue.TYPE_STRING){
                    newNode.setProperty(Configs.Key_Primitive_Value_Type, Configs.Primitive_Value_Type_STR);
                    newNode.setProperty(Configs.Key_Primitive_Value, to.getString());
                }
            }

            //create instance of relationships
            if(categoryNodes.size()>0){
                for(Node n: categoryNodes){
                    newNode.createRelationshipTo(n, Configs.Rels.INSTANCEOF);
                }
                
            }
            
            //if(Configs.CastNeo4jIdAsInt){
                
                //newNode.setProperty(db.Neo4j_Key_For_Neo4j_Id, Integer.parseInt(""+newNeo4jId));
            //}
            //else{
                newNode.setProperty(db.Neo4j_Key_For_Neo4j_Id, newNeo4jId);
            //}
            
        }
        catch(Exception ex){
            Logger.getLogger(QClass.class.getName()).log(Level.INFO, ex.getMessage());
            utils.handleException(ex);
            return APIFail;
        }
        
        return APISucc;                
    }
    
    Vector<Long> getClassesOfNode(Node n){
        Vector<Long> retVals = new Vector<Long>();
        if(n==null || n.hasRelationship(Configs.Rels.INSTANCEOF, Direction.OUTGOING)==false){
            return retVals;
        }
        
        
        for(Relationship rel : n.getRelationships(Configs.Rels.INSTANCEOF, Direction.OUTGOING)){
            retVals.add(db.getNodeNeo4jId(rel.getEndNode()));
        }
        return retVals;
    }
    
    /**
     * Adds an instance pointing from from to to. 
     * 
     * It fails if any of from or to does not exist.
     * @param from
     * @param to
     * @return 
     */
    public int CHECK_Add_Instance(Identifier from, Identifier to){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //  Add_Instance :
        //		Adds an Instance pointing from IDENTIFIER * from to IDENTIFIER * to.
        //    Returns 1 on Success, -1 on Failure. (WRONG !! ON SUCCESS RETURNS APISucc == 0
        //    Reasons to fail : 	An instance with the characteristics given
        //									already exists.
        int sis_api::Add_Instance(IDENTIFIER * from, IDENTIFIER * to)
        #ifdef CLIENT_SERVER
        {
                CHECK_TRANSACTION("Add_Instance");
                int result;
                result = RFCache->InvalidateCache();
                if(result < 0) return result;  // Comms error
                // Send the function identifier
                if(SComm->send_int(  _ADD_INSTANCE) < 0)
                        return -1;
                // Send the IDENTIFIER from
                if(SComm->send_IDENTIFIER(  from) < 0)
                        return -1; // Failure on socket comms
                // Send the IDENTIFIER to
                if(SComm->send_IDENTIFIER(  to) < 0)
                        return -1; // Failure on socket comms

                // Get the result of the function on the server
                if(SComm->recv_int(  &result) < 0)
                        return -1;

                return result;
        }
        #else
        {
                return Modify(SIS_API_INSTOF,SIS_API_CREATE,from,to);
        }
        


        */
        // </editor-fold> 
        if(!check_transaction("Add_Instance")){
            return APIFail;
        }
        return Modify(ModifyRelation.SIS_API_INSTOF, ModifyOperation.SIS_API_CREATE, from, to,null);
    }
    
    private boolean nodesAreNotConnectedThrough_InstanceOf_Or_ISA_Relationship(Node firstNode, Node secondNode, RelationshipType relType){
        if(firstNode==null || secondNode==null){
            return false;
        }
        
        Iterator<Relationship> relIter = firstNode.getRelationships(relType, Direction.OUTGOING).iterator();
        while(relIter.hasNext()){
            if(relIter.next().getEndNode().equals(secondNode)){
                return false;
            }
        }
        return true;
    }
    
    private boolean nodesAreOfTheSameLevel(Node firstNode, Node secondNode){
        if(firstNode==null || secondNode==null){
            return false;
        }
        if(firstNode.hasLabel(Configs.Labels.Token)){
            if(secondNode.hasLabel(Configs.Labels.Token)){
                return true;
            }
            return false;
        }
        if(firstNode.hasLabel(Configs.Labels.S_Class)){
            if(secondNode.hasLabel(Configs.Labels.S_Class)){
                return true;
            }
            return false;
        }
        if(firstNode.hasLabel(Configs.Labels.M1_Class)){
            if(secondNode.hasLabel(Configs.Labels.M1_Class)){
                return true;
            }
            return false;
        }
        if(firstNode.hasLabel(Configs.Labels.M2_Class)){
            if(secondNode.hasLabel(Configs.Labels.M2_Class)){
                return true;
            }
            return false;
        }
        if(firstNode.hasLabel(Configs.Labels.M3_Class)){
            if(secondNode.hasLabel(Configs.Labels.M3_Class)){
                return true;
            }
            return false;
        }
        if(firstNode.hasLabel(Configs.Labels.M4_Class)){
            if(secondNode.hasLabel(Configs.Labels.M4_Class)){
                return true;
            }
            return false;
        }
        
        //Telos_Object case
        return false;
    }
    
    private boolean nodesAreOfConsecutiveLevels(Node firstNode, Node secondNode){
        if(firstNode==null || secondNode==null){
            return false;
        }
        if(firstNode.hasLabel(Configs.Labels.Token)){
            if(secondNode.hasLabel(Configs.Labels.S_Class)){
                return true;
            }
            return false;
        }
        if(firstNode.hasLabel(Configs.Labels.S_Class)){
            if(secondNode.hasLabel(Configs.Labels.M1_Class)){
                return true;
            }
            return false;
        }
        if(firstNode.hasLabel(Configs.Labels.M1_Class)){
            if(secondNode.hasLabel(Configs.Labels.M2_Class)){
                return true;
            }
            return false;
        }
        if(firstNode.hasLabel(Configs.Labels.M2_Class)){
            if(secondNode.hasLabel(Configs.Labels.M3_Class)){
                return true;
            }
            return false;
        }
        if(firstNode.hasLabel(Configs.Labels.M3_Class)){
            if(secondNode.hasLabel(Configs.Labels.M4_Class)){
                return true;
            }
            return false;
        }
        
        //Telos_Object case
        return false;
    }
    
    enum ModifyRelation {SIS_API_INSTOF, SIS_API_ISA};
    enum ModifyOperation {SIS_API_CREATE,SIS_API_REMOVE,SIS_API_REDIRECT} 
    
    int Modify(ModifyRelation relation, ModifyOperation operation, Identifier source, Identifier target, Identifier newTarget){
        
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        int Modify(int relation, int operation, IDENTIFIER *source, IDENTIFIER *target, IDENTIFIER *newTarget = 0);
        
        // Purpose
//   %Modify is a generic function which is intended to perform an update
//   operation (creation, removal, and redirection) on %instOf or %isA
//   relationships.
// Input
//   %relation                   : can be either %SIS_API_INSTOF or %SIS_API_ISA
//   %operation                  : can be one of %SIS_API_REMOVE, %SIS_API_CREATE and %SIS_API_REDIRECT
//   %source, %target, %newTarget: determine the particular relationship
// Method
//   First %source is set as the current object, then the system identifiers of
//   %target and %newTarget are retrieved and finally one of the functions
//   updateIsa or updateInstOf is called according to %relation and %operation.
// Author
//   Marios Sintichakis
int sis_api::Modify(int relation, int operation, IDENTIFIER *source, IDENTIFIER *target, IDENTIFIER *newTarget)
{
	LOGINAM *lSource;
	SYSID    iTarget,iNewTarget;
	int      status;

	// Initialize update sets
	api_sem_check->initUpdateSets();

	// Set the current object
	if (GetLogicalName(source,&lSource) == APIFail)
		return APIFail;
	status = api_sem_check->checkCurrentObject(lSource);
	if (status == ERROR)
		return APIFail;
	else
		delete lSource; // incase of error already deleted

	// Find the system identifiers for `*i2' and `*i3'
	if (GetSYSID(target,&iTarget) == APIFail)
		return APIFail;
	if (newTarget != 0 && GetSYSID(newTarget,&iNewTarget) == APIFail)
		return APIFail;

	// Call the appropriate update function
	switch (relation) {
	case SIS_API_INSTOF:
		switch (operation) {
		case SIS_API_CREATE :
			status = api_sem_check->updateInstanceOf(0,&iTarget);
			break;
		case SIS_API_REMOVE :
			status = api_sem_check->updateInstanceOf(&iTarget,0);
			break;
		case SIS_API_REDIRECT :
			status = api_sem_check->updateInstanceOf(&iTarget,&iNewTarget);
			break;
		};
		break;
	case SIS_API_ISA:
		switch (operation) {
		case SIS_API_CREATE :
			status = api_sem_check->updateIsa(0,&iTarget);
			break;
		case SIS_API_REMOVE :
			status = api_sem_check->updateIsa(&iTarget,0);
			break;
		case SIS_API_REDIRECT :
			status = api_sem_check->updateIsa(&iTarget,&iNewTarget);
			break;
		};
		break;
	}

	if (status == ERROR)
		return APIFail;

	// Update
	if (api_sem_check->executeUpdateCommands() == ERROR)
		return APIFail;

	return APISucc;
}
        */
        // </editor-fold> 
        
        /*
        LOGINAM *lSource;
	SYSID    iTarget,iNewTarget;
	int      status;
        */
        //StringObject lSource = new StringObject();
        PrimitiveObject_Long iSource = new PrimitiveObject_Long();
        PrimitiveObject_Long iTarget = new PrimitiveObject_Long();
        PrimitiveObject_Long iNewTarget = new PrimitiveObject_Long();
	int      status = ERROR;
        // Initialize update sets
//	api_sem_check.initUpdateSets();
        
        // Set the current object (No need to do this step - of no use
	//if (GetLogicalName(source,lSource) == APIFail){
//            return APIFail;
  //      }
        
        // <editor-fold defaultstate="collapsed" desc="SKIPPED TESTS">
        //status = api_sem_check->checkCurrentObject(lSource);
        /*
        // --------------------------------------------------------------------------
        // semanticChecker::checkCurrentObject --
        //	It checks whether or not an object with given logical name has been
        // already defined and if the first happens then updates the variables of
        // semantic checker
        // --------------------------------------------------------------------------
        int	semanticChecker::checkCurrentObject( LOGINAM *currObjLn)
        {
                SYSID	currS;
                int		status;

        ////Check if not exists return error
	//Check if not unique return error


	if ( (status = translator->getSysid( currObjLn, &currS)) == NOT_EXIST) {
		errMsg( NOT_CREATE_OBJ, currObjLn, translator);
		return( ERROR);
	}
	if ( status == NOT_UNIQUE) {
		errMsg( "(>semanticChecker::checkCurrentObject) the object %s is not unique in the DB.\n", currObjLn, translator);
		return( ERROR);
	}
	if ( status == ERROR) {
		globalError.putMessage( " >semanticChecker::checkCurrentObject");
		return( ERROR);
	}

	// an entry for an object with the given logical name
	// has been made into the symbol table

	currObj = sysCatalog->loadObject( currS);
	if ( currObj == (T_O *)0) {
		errLoadObject( currS, ">semanticChecker::checkCurrentObject", translator);
		return( ERROR);
	}

	if ( currObj->IsUnresolved()) {
		errMsg( NOT_RETELL_UNRES_OBJ, currObjLn, translator);
		return( ERROR);
	}

	if ( O_IsLink( currObj->o_type)) {
		loSys = currS;
		currLinkObj = currObj;
		currS = currLinkObj->getFrom();
		currObj = sysCatalog->loadObject( currS);
		if ( currObj == (T_O *)0) {
			errLoadObject( currS, ">semanticChecker::checkCurrentObject", translator);
			return( ERROR);
		}
		setUpPointers();
		isLink = 1;
	} else
		isLink = 0;
        
// --------------------------------------------------------------------------
// getSysid -- inputs a full path name (fulname) and returns :
//    EXIST_ONE,    if the fulname corresponds to exactly one object and its
//                sysid is returned into retSys
//    NOT_EXIST,    if the given fulname corresponds to a no-one object
//                (the retSys returns zero)
//    NOT_UNIQUE,    if the fulname is not fully resolved (corresponds to more
//                than one objects)
//    ERROR,        if an error occurs
// --------------------------------------------------------------------------

int Translator::getSysid( LOGINAM *fulnam, SYSID *retSys)
{
    LOGINAM    *fromNam;
    SYSID    fulnamS;
    Loginam    *tmpL;
    int        status;
    int        id;

    //line added by georgian to check that actually the fulnam  exists
    if (  fulnam == ( LOGINAM *)0 ) {
        return(NOT_EXIST);
        } 

    if( (id = fulnam->unnamed_id()) )
            {
            *retSys = SYSID(id);
//            printf("Translator::getSysid( LOGINAM *fulnam, SYSID *retSys) called on unnamed sysid 0x%x\n", id);
            return( EXIST_ONE);
            }

    if ( (fromNam = fulnam->getfromnam()) == (LOGINAM *)0) {
        SYSID_SET    tmp;
        if ( (tmpL = inv_convertion( fulnam)) == (Loginam *)0)
            { return( ERROR); }
        symbolTable->getSysid( tmpL, &tmp);
        delete    tmpL;
        switch ( tmp.set_get_card()) {
          case ERROR: return( ERROR);
          case    0:    *retSys = SYSID(0);
                    return( NOT_EXIST);
          case    1:    tmp.set_pos( 0);
                    *retSys = tmp.set_get_id();
                    return( EXIST_ONE);
        }
        return( NOT_UNIQUE);
    }

    if ( (status = getSysid( fromNam, &fulnamS)) == EXIST_ONE) {
        if ( (tmpL = inv_convertion( fulnam)) == (Loginam *)0)
            { return( ERROR); }
        *retSys = symbolTable->getSysid( tmpL, fulnamS);
        delete    tmpL;
        if ( retSys->id == 0)
            { return( NOT_EXIST); }
        return( EXIST_ONE);
    }

    return( status);
}
        */
	//if (status == ERROR)
		//return APIFail;
	//else
		//delete lSource; // incase of error already deleted
// </editor-fold>
        
        
        // does some tests so check it
        if(GetSYSID(source,iSource)==APIFail){
            return APIFail;
        }
        
        // Find the system identifiers for `*i2' and `*i3'
        if(GetSYSID(target,iTarget)==APIFail){
            return APIFail;
        }
        if(newTarget!=null && GetSYSID(newTarget,iNewTarget)==APIFail){
            return APIFail;
        }
        
        // Call the appropriate update function
        switch (relation) {
            case SIS_API_INSTOF: {
                switch (operation) {
                    case SIS_API_CREATE: {

                        //check if consecutive levels
                        //check if relation exists
                        Node startNode = db.getNeo4jNodeByNeo4jId(iSource.getValue());
                        Node endNode = db.getNeo4jNodeByNeo4jId(iTarget.getValue());

                        if (nodesAreOfConsecutiveLevels(startNode, endNode) ) {
                            if(nodesAreNotConnectedThrough_InstanceOf_Or_ISA_Relationship(startNode, endNode, Configs.Rels.INSTANCEOF)==false){
                                
                                //Elias Bugfix 2015-0904
                                //
                        //So here it should return APIFail but this caused bugs and is of no meaning
                        //Further searching showd that we have the following lines of code in old PQI-API
                        ////    Reasons to fail : 	An instance with the characteristics given already exists.
                        //int sis_api::Add_Instance(IDENTIFIER * from, IDENTIFIER * to)
                        //return Modify(SIS_API_INSTOF,SIS_API_CREATE,from,to);
                        
                        //int sis_api::Modify(int relation, int operation, IDENTIFIER *source, IDENTIFIER *target, IDENTIFIER *newTarget)
                        //switch (relation) {
	//case SIS_API_INSTOF:
		//switch (operation) {
		//case SIS_API_CREATE :
			//status = api_sem_check->updateInstanceOf(0,&iTarget);
                        
                        //NOTE THAT updateInstanceOf is called with 0
                        
                        /*
                        
int	semanticChecker::updateInstanceOf( SYSID *destS, SYSID *newdestS)
{
	if ( destS) {
		switch ( existInstanceOf( *destS)) {
		  case ERROR : {
				globalError.putMessage( " >semanticChecker::updateInstanceOf");
				return( ERROR);
				}
		  case FALSE : { return( OK); }
		  default:
				if ( deletedInstOf.set_put( *destS) == ERROR) {
					globalError.putMessage( " >semanticChecker::updateInstanceOf");
					return( ERROR);
				}
		}
	}

	if ( newdestS) {
		if ( addedInstOf.set_put( *newdestS) == ERROR) {
			globalError.putMessage( " >semanticChecker::updateInstanceOf");
			return( ERROR);
		}
	}

	return( OK);
}

                        Code does not get in existInstanceOf part because it is called with 0
                        and the only reason that addedInstOf.set_put( *newdestS) would return error is in case not enough memory could be allocated
                        */
                        
                                return APISucc;
                            }
                            startNode.createRelationshipTo(endNode, Configs.Rels.INSTANCEOF);
                            return APISucc;
                        }
                        //status = api_sem_check->updateInstanceOf(0,&iTarget);
                        break;
                    }
                    case SIS_API_REMOVE: {
                        //check relationship exists 
                        //check if consecutive levels ?? (if exists should already be true) SKIP
                        //do we need to check if it is the last one for tokens??? SKIP
                        Node startNode = db.getNeo4jNodeByNeo4jId(iSource.getValue());
                        Node endNode = db.getNeo4jNodeByNeo4jId(iTarget.getValue());

                        if(startNode==null || endNode==null){
                            return ERROR;
                        }

                        Iterator<Relationship> relIter = startNode.getRelationships(Configs.Rels.INSTANCEOF, Direction.OUTGOING).iterator();
                        while(relIter.hasNext()){
                            Relationship rel = relIter.next();
                            if(rel.getEndNode().equals(endNode)){
                                rel.delete();
                                return APISucc;            
                            }
                        }
                        break;
                    }
                    case SIS_API_REDIRECT: {
                        
                        Node startNode = db.getNeo4jNodeByNeo4jId(iSource.getValue());
                        Node endNode = db.getNeo4jNodeByNeo4jId(iTarget.getValue());
                        Node newEndNode = db.getNeo4jNodeByNeo4jId(iNewTarget.getValue());
                        
                        if (nodesAreOfConsecutiveLevels(startNode, newEndNode)) {
                            if(nodesAreNotConnectedThrough_InstanceOf_Or_ISA_Relationship(startNode, newEndNode, Configs.Rels.INSTANCEOF)==false){
                                return APISucc;
                            }
                            boolean deletedOldRel = false;
                            Iterator<Relationship> relIter = startNode.getRelationships(Configs.Rels.INSTANCEOF, Direction.OUTGOING).iterator();
                            while(relIter.hasNext()){
                                Relationship rel = relIter.next();
                                if(rel.getEndNode().equals(endNode)){
                                    rel.delete();
                                    deletedOldRel = true;
                                    break;
                                }
                            }
                            if(deletedOldRel){
                                startNode.createRelationshipTo(newEndNode, Configs.Rels.INSTANCEOF);
                            }
                            return APISucc;
                        }
                        //status = api_sem_check->updateInstanceOf(&iTarget,&iNewTarget);
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
            }
            case SIS_API_ISA: {
                switch (operation) {
                    case SIS_API_CREATE: {
                        //status = api_sem_check->updateIsa(0,&iTarget);

                        //check if consecutive levels
                        //check if relation exists
                        Node startNode = db.getNeo4jNodeByNeo4jId(iSource.getValue());
                        Node endNode = db.getNeo4jNodeByNeo4jId(iTarget.getValue());
                        if (nodesAreOfTheSameLevel(startNode, endNode)) {
                            if(nodesAreNotConnectedThrough_InstanceOf_Or_ISA_Relationship(startNode, endNode, Configs.Rels.ISA)==false){
                                return APISucc;
                            }
                            startNode.createRelationshipTo(endNode, Configs.Rels.ISA);
                            return APISucc;
                        }
                        break;
                    }
                    case SIS_API_REMOVE: {
                        //status = api_sem_check->updateIsa(&iTarget,0);
                        Node startNode = db.getNeo4jNodeByNeo4jId(iSource.getValue());
                        Node endNode = db.getNeo4jNodeByNeo4jId(iTarget.getValue());

                        if(startNode==null || endNode==null){
                            return ERROR;
                        }

                        Iterator<Relationship> relIter = startNode.getRelationships(Configs.Rels.ISA, Direction.OUTGOING).iterator();
                        while(relIter.hasNext()){
                            Relationship rel = relIter.next();
                            if(rel.getEndNode().equals(endNode)){
                                rel.delete();
                                return APISucc;            
                            }
                        }
                        break;
                    }
                    case SIS_API_REDIRECT: {
                        //status = api_sem_check->updateIsa(&iTarget,&iNewTarget);
                        
                        Node startNode = db.getNeo4jNodeByNeo4jId(iSource.getValue());
                        Node endNode = db.getNeo4jNodeByNeo4jId(iTarget.getValue());
                        Node newEndNode = db.getNeo4jNodeByNeo4jId(iNewTarget.getValue());
                        
                        if (nodesAreOfConsecutiveLevels(startNode, newEndNode) ) {
                            if(nodesAreNotConnectedThrough_InstanceOf_Or_ISA_Relationship(startNode, newEndNode, Configs.Rels.ISA)==false){
                                return APISucc;
                            }
                            boolean deletedOldRel = false;
                            Iterator<Relationship> relIter = startNode.getRelationships(Configs.Rels.ISA, Direction.OUTGOING).iterator();
                            while(relIter.hasNext()){
                                Relationship rel = relIter.next();
                                if(rel.getEndNode().equals(endNode)){
                                    rel.delete();
                                    deletedOldRel = true;
                                    break;
                                }
                            }
                            if(deletedOldRel){
                                startNode.createRelationshipTo(newEndNode, Configs.Rels.ISA);
                            }
                            return APISucc;
                        }
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
            default: {
                break;
            }
        }

        if (status == ERROR) {
            return APIFail;
        }

	// Update
        //if (api_sem_check->executeUpdateCommands() == ERROR)
        //return APIFail;
        return APIFail;
    }
    
    private int GetSYSID(Identifier obj_ID, PrimitiveObject_Long sysid){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        ////////////////////////////////////////////////////////////////////////
//  GetSYSID :
//          GetSYSID is used to get a SYSID from the given IDENTIFIER.
//          Returns APISuccess on Succ, APIFail on Failure.
//
int sis_api::GetSYSID(IDENTIFIER *obj_ID, SYSID *sysid)
{
	// Check if the obj_ID is valid
	if(obj_ID->tag != ID_TYPE_LOGINAM && obj_ID->tag != ID_TYPE_SYSID)
		return APIFail;
	// If the node name is a loginam get the SYSID
	if(obj_ID->tag == ID_TYPE_LOGINAM)
	{
		// Make a loginam
		LOGINAM name(obj_ID->loginam);
		// Get the node SYSID. If the name is not unique or does not exist fail.
		if(api_translator->getSysid(&name, sysid) != EXIST_ONE)
			return APIFail;
	}
	else
	{
		// If the node_name is a sysid setup the SYSID
		sysid->id = obj_ID->sysid;
	}
	return APISucc;
}
        */
        // </editor-fold> 
        if(obj_ID.tag!= Identifier.ID_TYPE_LOGINAM && obj_ID.tag!= Identifier.ID_TYPE_SYSID){
            return APIFail;
        }
        // If the node name is a loginam get the SYSID
        if(obj_ID.tag == Identifier.ID_TYPE_LOGINAM){
            /*
            // Make a loginam
		LOGINAM name(obj_ID->loginam);
		// Get the node SYSID. If the name is not unique or does not exist fail.
		if(api_translator->getSysid(&name, sysid) != EXIST_ONE)
			return APIFail;
            
            */     
            long tmp = db.getClassId(obj_ID.getLogicalName());
            if(tmp>0){
                sysid.setValue(tmp);                
            }
            else{
                throw new UnsupportedOperationException();
                //sysid.setValue(0);
                //return APIFail;
            }
        }
        else{
            // If the node_name is a sysid setup the SYSID
		sysid.setValue(obj_ID.getSysid());
        }
        
        return APISucc;        
    }
    
    private int GetLogicalName(Identifier obj_ID, StringObject lnam){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        /////////////////////////////////////////////////////////////////////
//  GetLogicalName :
//          GetLogicalName is used to get a logical name from the
//          given IDENTIFIER.
//          Returns APISuccess on Succ, APIFail on Failure.
//
//          The space is allocated in translator  or in this function
//          and thus it should be deleted by the user.
//
int sis_api::GetLogicalName(IDENTIFIER *obj_ID, LOGINAM **lnam)
{
	// Check if the obj_ID is valid
	if(obj_ID->tag != ID_TYPE_LOGINAM && obj_ID->tag != ID_TYPE_SYSID)
		return APIFail;
	// If the node name is a loginam return it
	if(obj_ID->tag == ID_TYPE_LOGINAM)
	{
		// Make a loginam
		*lnam = new LOGINAM(obj_ID->loginam);
	}
	else
	{
		// If the node_name is a sysid get the LOGINAM
		*lnam = api_translator->getFulName(SYSID(obj_ID->sysid));
	}
	if(*lnam != NULL)
		return APISucc;
	else
		return APIFail;
}
        */
        // </editor-fold> 
        
        if(obj_ID.tag!= Identifier.ID_TYPE_LOGINAM && obj_ID.tag!= Identifier.ID_TYPE_SYSID){
            return APIFail;
        }
        // If the node name is a loginam return it
        if(obj_ID.tag == Identifier.ID_TYPE_LOGINAM){
            lnam.setValue(obj_ID.getLogicalName());            
        }
        else{
            // If the node_name is a sysid get the LOGINAM
            getLoginamString(obj_ID.getSysid(), lnam);
        }
        
        if(lnam.value!=null && lnam.value.length()>0){
            return APISucc;
        }
        else{
            return APIFail;
        }
    }
    
    /**
     * Adds all instances in set from_set pointing to to.
     * 
     * It fails if an instance with the characteristics given 
     * already exists.
     * 
     * @param from_set
     * @param to
     * @return 
     */
    public int CHECK_IMPROVE_Add_Instance_Set(int from_set, Identifier to){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        // START OF: sis_api::Add_Instance_Set
        //  Add_Instance_Set :
        //		Adds all Instances in set from_set pointing to IDENTIFIER * to.
        //    Returns 1 on Success, -1 on Failure.
        //    Reasons to fail : 	An instance with the characteristics given already exists.
        
int sis_api::Add_Instance_Set(int from_set, IDENTIFIER * to)
#ifdef CLIENT_SERVER
{
	CHECK_TRANSACTION("Add_Instance_Set");
	int result;
	result = RFCache->InvalidateCache();
	if(result < 0) return result;  // Comms error
	// Send the function identifier
	if(SComm->send_int(  _ADD_INSTANCE_SET) < 0)
		return -1;
	// Send the set of objects
	if(SComm->send_int(  from_set) < 0)
		return -1; // Failure on socket comms
	// Send the IDENTIFIER to
	if(SComm->send_IDENTIFIER(  to) < 0)
		return -1; // Failure on socket comms

	// Get the result of the function on the server
	if(SComm->recv_int(  &result) < 0)
		return -1;

	return result;
}
#else
{
	int node_sysid;
	reset_set(from_set);
	l_name node_name, level;
	while (return_full_nodes(from_set, &node_sysid, node_name, level) != -1) {
		// add node as instance of to
		IDENTIFIER InodeTerm(node_sysid);
		// call Add_instance on every node of set (if one fails the whole set fails)
		if (Add_Instance(&InodeTerm, to) == APIFail)
			return APIFail;
	}
	// Return Success
	return APISucc;
}
#endif
        */
        // </editor-fold> 
        if(!check_transaction("Add_Instance_Set")){
            return APIFail;
        }
        
        PQI_Set fromSet = this.tmp_sets.return_set(from_set);
        if(fromSet==null){
            return APIFail;
        }
        Vector<Long> fromIds = fromSet.get_Neo4j_Ids();
        
        if(fromIds.size()==0){
            return APISucc;
        }
        boolean errorOccured = false;
        for(long fromL : fromIds){
            if(CHECK_Add_Instance(new Identifier(fromL), to)==APIFail){
                errorOccured = true;
                break;
            }
        }
        
        if(errorOccured){
            return APIFail;
        }
        return APISucc;
    }
    
    /**
     * Adds an isA pointing from from to to. 
     * 
     * It fails if any of from or to does not exist.
     * 
     * @param from
     * @param to
     * @return 
     */
    public int CHECK_Add_IsA(Identifier from, Identifier to){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        
        if(!check_transaction("Add_IsA")){
            return APIFail;
        }
        return Modify(ModifyRelation.SIS_API_ISA, ModifyOperation.SIS_API_CREATE, from, to,null);
    }
    
    /**
     * Change the To value of an instance. 
     * 
     * The instance is specified by the from value in IDENTIFIER from and the 
     * To value IDENTIFIER to. The new To value for the instance is specified 
     * by IDENTIFIER NewTo. 
     * 
     * It fails if from is not an instance of to or the New To value does not exist.
     * 
     * @param from
     * @param to
     * @param NewTo
     * @return 
     */
    public int NOT_USED_CHECK_Change_Instance_To(Identifier from, Identifier to,Identifier NewTo){
        if(!check_transaction("Change_Instance_To")){
            return APIFail;
        }
        return Modify(ModifyRelation.SIS_API_INSTOF, ModifyOperation.SIS_API_REDIRECT, from, to,NewTo);
    }
    
    /**
     * Change the To value of an isA. 
     * 
     * The instance is specified by the from value in IDENTIFIER from and the To
     * value IDENTIFIER to. The new To value for the instance is specified by 
     * IDENTIFIER NewTo. 
     * 
     * It fails if there is on isA between the given from and to, 
     * or the New To value does not exist.
     * 
     * @param from
     * @param to
     * @param NewTo
     * @return 
     */
    public int NOT_USED_CHECK_Change_IsA_To(Identifier from, Identifier to,Identifier NewTo){
        if(!check_transaction("Change_IsA_To")){
            return APIFail;
        }
        return Modify(ModifyRelation.SIS_API_ISA, ModifyOperation.SIS_API_REDIRECT, from, to,NewTo);
    }
    
    /**
     * Deletes a node with the logical name (or sysid) node_name. 
     * 
     * It fails if the node does not exist or has dependencies with 
     * other objects.
     * 
     * @param node_name
     * @return 
     */
    public int CHECK_Delete_Node(Identifier node_name){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //  Delete_Node :
//		Deletes a node with the logical name node_name OR sysID (given in name).
//    Returns 1 on Success, -1 on Failure.
//		Reasons to fail : 	A node with the given IDENTIFIER * does not exist.
//									The given node cannot be deleted because dependencies
//									with other objects exist.
int sis_api::Delete_Node(IDENTIFIER * node_name)
#ifdef CLIENT_SERVER
{
	CHECK_TRANSACTION("Delete_Node");
	int result;
	result = RFCache->InvalidateCache();
	if(result < 0) return result;  // Comms error
	// Send the function identifier
	if(SComm->send_int(  _DELETE_NODE) < 0)
		return -1;
	// Send the IDENTIFIER node_name
	if(SComm->send_IDENTIFIER(  node_name) < 0)
		return -1; // Failure on socket comms

	// Get the result of the function on the server
	if(SComm->recv_int(  &result) < 0)
		return -1;

	return result;
}
#else
{
	SYSID nodeId;

	// In order to delete the given node its SYSID is required.
	if(GetSYSID(node_name, &nodeId) == APIFail)
		return APIFail;

	// Call the semantic checker to Delete the node
	if (api_sem_check->checkAndDeleteObject(nodeId) == ERROR) // ERROR = -1
		return APIFail;

	// Return success
	return APISucc;
}
#endif
        
        
        
        // --------------------------------------------------------------------------
// semanticChecker::checkAndDeleteObject --
// --------------------------------------------------------------------------

int	semanticChecker::checkAndDeleteObject( SYSID objSid)
{
	if ( !objSid.id) {
		return( OK);
	}

	int	errorNum;

	if ( (errorNum = checkDeletion( objSid)) != 0) {
		handleError( errorNum, objSid, translator);
		return( ERROR);
	}

	if ( deleteObjectWithoutChecking( objSid) == ERROR) {
		globalError.putMessage( " >checkAndDeleteObject");
		return( ERROR);
	}

	return( OK);
}
        
        
        
        
        
// --------------------------------------------------------------------------
//  semanticChecker::checkDeletion -- checks whether or not the given object 
//  can be deleted.    Returns 0 if the object can be deleted and an error code 
//  otherwhise.
// --------------------------------------------------------------------------

int    semanticChecker::checkDeletion( SYSID delSid)
{
    SYSID_SET    tmp;
    T_O    *delObj;

    delObj = sysCatalog->loadObject( delSid);
	 if ( delObj == (T_O *)0) {
        errLoadObject( delSid, " >checkDeletion", translator);
        return( ERROR);
    }

//    delObj->getInstBy( &tmp);
// previous line was replaced by the method getInstancesBy(). This method 
// gets the instances of "delObj" taking into account the case where "delObj" 
// is a category declared with no instances. 

    getInstancesBy(delObj->o_sysid, &tmp);

    if ( tmp.set_get_card()) {
        sysCatalog->unloadObject( delSid);
        // object can't be deleted, because it has instances
        return( HAS_INSTANCES);
    }

// if the obect is a category then it may have class attributes.
	 if ( (O_IsLink(delObj->o_type)) && (delObj->o_type != O_LINK_TOKEN) ) {
        tmp.set_clear();
        delObj->getClassAttr(&tmp);
        if ( tmp.set_get_card()) {
            sysCatalog->unloadObject( delSid);
            // object can't be deleted, because it has class attributes
            return(HAS_CLASS_ATTRS);
        }
    }

    tmp.set_clear();
    delObj->getLink( &tmp);
    if ( tmp.set_get_card()) {
        sysCatalog->unloadObject( delSid);
        // object can't be deleted, because it has links originated from it
        return( HAS_LINKS);
    }

    tmp.set_clear();
    delObj->getLinkBy( &tmp);
    if ( tmp.set_get_card()) {
        sysCatalog->unloadObject( delSid);
        // object can't be deleted, because it has links pointed to it
        return( HAS_LINKS_BY);
	 }

    sysCatalog->unloadObject( delSid);
    return( OK);
}

        */
        // </editor-fold> 
        
        if(!check_transaction("Delete_Node")){
            return APIFail;
        }
        if(node_name==null){
            return APIFail;
        }
        // In order to delete the given node its SYSID is required.
        PrimitiveObject_Long iNodeForDelete = new PrimitiveObject_Long();
        GetSYSID(node_name, iNodeForDelete);
        if(iNodeForDelete.getValue()<=0){
            return APIFail;
        }
        
        //FROM checkDeletion
        //object must exist
        //if object has instances APIFail
        //if object has subclasses APIFail
        //if object is attibute - Link then check if it has class attributes
        //if object has links to or from then APIFail
        
        //object must exist
        Node n = db.getNeo4jNodeByNeo4jId(iNodeForDelete.getValue());
        if(n==null){
            return APIFail;
        }
        
        //if object has instances APIFail
        if(n.hasRelationship(Configs.Rels.INSTANCEOF, Direction.INCOMING)){
            return APIFail;
        }
        //if object has subclasses APIFail
        if(n.hasRelationship(Configs.Rels.ISA, Direction.INCOMING)){
            return APIFail;
        }
        
        //if object has links to or from then APIFail
        if(n.hasRelationship(Configs.Rels.RELATION, Direction.BOTH)){
            if(db.DebugInfo){
                for(Relationship rel: n.getRelationships(Configs.Rels.RELATION, Direction.OUTGOING)){
                    Logger.getLogger(QClass.class.getName()).log(Level.INFO, "Outgoing rel: "+ rel.getEndNode().getProperty("Logicalname").toString());
                }
                for(Relationship rel: n.getRelationships(Configs.Rels.RELATION, Direction.INCOMING)){
                    Logger.getLogger(QClass.class.getName()).log(Level.INFO, "Incoming rel: "+ rel.getStartNode().getProperty("Logicalname").toString());
                }
            }
            return APIFail;
        }
        
        Vector<Relationship> relsToDelete = new Vector<Relationship>();
        try{
            
            Iterator<Relationship> classIter = n.getRelationships(Configs.Rels.INSTANCEOF, Direction.OUTGOING).iterator();
            while(classIter.hasNext()){
                Relationship rel = classIter.next();
                if(relsToDelete.contains(rel)==false){
                    relsToDelete.add(rel);
                }
            }
            
            Iterator<Relationship> superClassIter = n.getRelationships(Configs.Rels.ISA, Direction.OUTGOING).iterator();
            while(superClassIter.hasNext()){
                Relationship rel = superClassIter.next();
                if(relsToDelete.contains(rel)==false){
                    relsToDelete.add(rel);
                }
            }
            
            for(Relationship rel : relsToDelete){
                rel.delete();
            }
            
            deleteNodeWithProperties(n);
            n=null;
        }
        catch(Exception ex){
            utils.handleException(ex);
            return APIFail;
        }
        return APISucc;
    }
    
    void deleteNodeWithProperties(Node n){
        if(n==null){
            return ;
        }
        Vector<String> propsToDelete = new Vector<String>();
        Vector<Label> labelsToRemove = new Vector<Label>();
        Iterator<Label> lblIter =  n.getLabels().iterator();
        while(lblIter.hasNext()){
            labelsToRemove.add(lblIter.next());
        }
        if(n.hasProperty(db.Neo4j_Key_For_Neo4j_Id)){
            propsToDelete.add(db.Neo4j_Key_For_Neo4j_Id);
        }
        Iterator<String> propIter = n.getPropertyKeys().iterator();
        while(propIter.hasNext()){
            String propName = propIter.next();
            if(propsToDelete.contains(propName)==false){
                propsToDelete.add(propName);
            }
        }
        for(Label l : labelsToRemove){
            n.removeLabel(l);
        }
        for(String str : propsToDelete){
            n.removeProperty(str);
        }
        //db.graphDb.index().getNodeAutoIndexer().getAutoIndex()..index().forNodes(db.Neo4j_Key_For_Neo4j_Id).remove(n,db.Neo4j_Key_For_Neo4j_Id);
        //Schema sc = db.graphDb.schema();
        //for(IndexDefinition def : sc.getIndexes(Configs.Labels.Common)){
//            def.
  ///      }
        
        n.delete();        
        n = null;
        //for(ConstraintDefinition def : sc.getConstraints(Configs.Labels.Common)){
          //  def.;
            //break;
        //}
        
        //db.graphDb.schema().constraintFor(Configs.Labels.Common).assertPropertyIsUnique(db.Neo4j_Key_For_Neo4j_Id).create();
    }
    
    /**
     * Deletes a named attribute with the logical name (or sysid) link_name from from. 
     * The from parameter must always specify a SYSID. 
     * 
     * It fails if the link link_name does not exist or has dependencies 
     * with other objects.
     * 
     * @param link_name
     * @param from
     * @return 
     */
    public int CHECK_Delete_Named_Attribute(Identifier link_name, Identifier from){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
 START OF: sis_api::Delete_Named_Attribute 
//  Delete_Named_Attribute :
//		Deletes a Named_Attribute with the logical name IDENTIFIER *attribute
//		from IDENTIFIER * from OR with the given sysID (in link_name).
//    Returns 1 on Success, -1 on Failure.
//		Reasons to fail : 	A link with the given IDENTIFIER * does not exist.
//									The given link cannot be deleted because dependencies
//									with other objects exist.
int sis_api::Delete_Named_Attribute(IDENTIFIER *attribute, IDENTIFIER * from)
#ifdef CLIENT_SERVER
{
	CHECK_TRANSACTION("Delete_Named_Attribute");
	int result;
	result = RFCache->InvalidateCache();
	if(result < 0) return result;  // Comms error
	// Send the function identifier
	if(SComm->send_int(  _DELETE_NAMED_ATTRIB) < 0)
		return -1;
	// Send the IDENTIFIER attribute
	if(SComm->send_IDENTIFIER(  attribute) < 0)
		return -1; // Failure on socket comms
	// Send the IDENTIFIER from
	if(SComm->send_IDENTIFIER(  from) < 0)
		return -1; // Failure on socket comms

	// Get the result of the function on the server
	if(SComm->recv_int(  &result) < 0)
		return -1;

	return result;
}
#else
{
	SYSID    iFrom;
	SYSID    iLink;
	LOGINAM *lLink;

	// Retrieve the system identifier of %attribute
	switch (attribute->tag) {
	case ID_TYPE_SYSID:
		if (GetSYSID(attribute,&iLink) == APIFail)
			return APIFail;
		break;
	case ID_TYPE_LOGINAM:
		// %from should contain a system identifier
		if (from->tag != ID_TYPE_SYSID)
			return APIFail;
		if (GetSYSID(from,&iFrom) == APIFail)
			return APIFail;
		if (GetLogicalName(attribute,&lLink) == APIFail)
			return APIFail;
		iLink = api_translator->getSysid(lLink,iFrom);
		delete lLink;
		if (iLink.id == 0)
			return APIFail;
		break;
	default:
		return APIFail;
	}

	return api_sem_check->checkAndDeleteObject(iLink) == ERROR ? APIFail : APISucc;  // ERROR = -1
}
#endif
        
        
int	semanticChecker::checkAndDeleteObject( SYSID objSid)
{
	if ( !objSid.id) {
		return( OK);
	}

	int	errorNum;

	if ( (errorNum = checkDeletion( objSid)) != 0) {
		handleError( errorNum, objSid, translator);
		return( ERROR);
	}

	if ( deleteObjectWithoutChecking( objSid) == ERROR) {
		globalError.putMessage( " >checkAndDeleteObject");
		return( ERROR);
	}

	return( OK);
}
        
        // --------------------------------------------------------------------------
//  semanticChecker::checkDeletion -- checks whether or not the given object 
//  can be deleted.    Returns 0 if the object can be deleted and an error code 
//  otherwhise.
// --------------------------------------------------------------------------

int    semanticChecker::checkDeletion( SYSID delSid)
{
    SYSID_SET    tmp;
    T_O    *delObj;

    delObj = sysCatalog->loadObject( delSid);
	 if ( delObj == (T_O *)0) {
        errLoadObject( delSid, " >checkDeletion", translator);
        return( ERROR);
    }

//    delObj->getInstBy( &tmp);
      // previous line was replaced by the method getInstancesBy(). This method 
      // gets the instances of "delObj" taking into account the case where 
      // "delObj" is a category declared with no instances. 
    getInstancesBy(delObj->o_sysid, &tmp);

    if ( tmp.set_get_card()) {
        sysCatalog->unloadObject( delSid);
        // object can't be deleted, because it has instances
        return( HAS_INSTANCES);
    }
    
    //if the obect is a category then it  may have class attributes. 
	 if ( (O_IsLink(delObj->o_type)) && (delObj->o_type != O_LINK_TOKEN) ) {
        tmp.set_clear();
        delObj->getClassAttr(&tmp);
        if ( tmp.set_get_card()) {
            sysCatalog->unloadObject( delSid);
            // object can't be deleted, because it has class attributes
            return(HAS_CLASS_ATTRS);
        }
    }

    tmp.set_clear();
    delObj->getLink( &tmp);
    if ( tmp.set_get_card()) {
        sysCatalog->unloadObject( delSid);
        // object can't be deleted, because it has links originated from it
        return( HAS_LINKS);
    }

    tmp.set_clear();
    delObj->getLinkBy( &tmp);
    if ( tmp.set_get_card()) {
        sysCatalog->unloadObject( delSid);
        // object can't be deleted, because it has links pointed to it
        return( HAS_LINKS_BY);
	 }

    sysCatalog->unloadObject( delSid);
    return( OK);
}
        
        
        
// --------------------------------------------------------------------------
// deleteObjectWithoutChecking --
//	deletes the given object without any checking
// --------------------------------------------------------------------------

int    semanticChecker::deleteObjectWithoutChecking( SYSID delSid)
{
	SYSID_SET	tmp;
	T_O			*delObj, *tmpO;
	SYSID		tmpSid;

    // delete the object from the instantiatedBy-slot of its classes
    delObj = sysCatalog->loadObject( delSid);
    if ( delObj == (T_O *)0) {
        errLoadObject( delSid, " >deleteObjectWithoutChecking", translator);
        return( ERROR);
    }

    delObj->getInstOf( &tmp);

					// if the link is a class attribute we should
					// delete it from the field class_attr of his
					// category-obj and not from inst_by (Lemonia)
    if ( delObj->IsClassAttr() ) {
        if ( generic_delete( &tmp, delSid, &T_O::delClassAttr) == ERROR) {
             sysCatalog->unloadObject( delSid);
             return( ERROR);
        }
    }
    else {
		  if ( generic_delete( &tmp, delSid, &T_O::delInstBy) == ERROR) {
            sysCatalog->unloadObject( delSid);
            return( ERROR);
        }
    }

    // delete the object from the subclass-slot of its immediate superclasses
    tmp.set_clear();
    delObj->getIsa( &tmp);
    if ( generic_delete( &tmp, delSid, &T_O::delSubclass ) == ERROR) {
        sysCatalog->unloadObject( delSid);
        return( ERROR);
    }

    // delete the object from the superclass-slot of its subclasses
    tmp.set_clear();
    delObj->getSubclass(&tmp);
    if ( generic_delete( &tmp, delSid, &T_O::delIsa) == ERROR) {
        sysCatalog->unloadObject( delSid);
        return( ERROR);
    }

    // delete the object from the relevant Link and LinkBy slots, if any
    if (O_IsLink(delObj->o_type)) {
		  if ( (tmpSid = delObj->getTo()).id != 0) {
            // the link object doesn't point to a primitive value
            tmpO = sysCatalog->loadObject(tmpSid);
            if ( tmpO == (T_O *)0) {
                sysCatalog->unloadObject( delSid);
                errLoadObject( tmpSid, " >deleteObjectWithoutChecking", translator);
                return( ERROR);
            }
            tmpO->delLinkBy( delSid);
        }
        sysCatalog->unloadObject(tmpSid);

        tmpSid = delObj->getFrom();
        tmpO = sysCatalog->loadObject(tmpSid);
        if ( tmpO == (T_O *)0) {
            errLoadObject( tmpSid, " >deleteObjectWithoutChecking", translator);
            sysCatalog->unloadObject( delSid);
            return( ERROR);
        }
        tmpO->delLink( delSid);
        sysCatalog->unloadObject(tmpSid);
    }

    // delete the object from the symbol table and the system catalog
    sysCatalog->deleteObject( delSid, translator->getSymTable());

	 return( OK);
}        
        */
        // </editor-fold> 
        
        //1. both from and link_name must exist
        //2. if it has instances or subclasses then it cannot be deleted
        //3. if it has class attributes then it cannot be deleted
        //4. if it is pointed by other objects then it can not be deleted (attribute to attribute case) 
        //   also check if relationship with from exists
        //5. if it has other links then it can not be deleted (attribute to attribute case)
        
        
        if(!check_transaction("Delete_Named_Attribute")){
            return APIFail;
        }
        if(link_name==null || from ==null){
            return APIFail;
        }
        
        if (link_name.tag==Identifier.ID_TYPE_LOGINAM && from.tag != Identifier.ID_TYPE_SYSID){
            // %from should contain a system identifier
            return APIFail;
        }
        
        PrimitiveObject_Long iFrom = new PrimitiveObject_Long();
        PrimitiveObject_Long iLink = new PrimitiveObject_Long();
        
        int ret = GetSYSID(from, iFrom);
        if(ret == APIFail){
            return APIFail;
        }
        
        ret = GetSYSID(link_name, iLink);
        if(ret == APIFail){
            return APIFail;
        }
        
        //1. both from and link_name must exist
        Node linkNode = db.getNeo4jNodeByNeo4jId(iLink.getValue());
        Node fromNode = db.getNeo4jNodeByNeo4jId(iFrom.getValue());
        
        if(linkNode ==null || fromNode ==null){
            return APIFail;
        }
        //2. if it has instances or subclasses then it cannot be deleted
        if(linkNode.hasRelationship(Configs.Rels.INSTANCEOF, Direction.INCOMING)){
            return APIFail;
        }        
        if(linkNode.hasRelationship(Configs.Rels.ISA, Direction.INCOMING)){
            return APIFail;
        }
        //3. skipped no class attrbiutes
        
        //4. if it is pointed by other objects then it can not be deleted (attribute to attribute case) 
        //   also check if relationship with from exists
        boolean fromRelationshipFound = false;
        boolean otherIncomingRelationshipfound = false;
        Iterator<Relationship> incomingRels = linkNode.getRelationships(Configs.Rels.RELATION, Direction.INCOMING).iterator();
        while(incomingRels.hasNext()){
            Relationship rel = incomingRels.next();
            if(rel.getStartNode().equals(fromNode)){
                fromRelationshipFound = true;
            }
            else{
                otherIncomingRelationshipfound = true;
                break;
            }
        }
        
        if(fromRelationshipFound==false || otherIncomingRelationshipfound ==true){
            return APIFail;
        }
        
        
        //5. if it has other links then it can not be deleted (attribute to attribute case)
        int outgoingCounter = 0 ;
        Iterator<Relationship> outgoingRels = linkNode.getRelationships(Configs.Rels.RELATION, Direction.OUTGOING).iterator();
        while(incomingRels.hasNext()){
            Relationship rel = outgoingRels.next();
            if(rel.getEndNode().hasLabel(Configs.Labels.Type_Individual) ==false){
                return APIFail;
            }        
            outgoingCounter ++;
        }
        
        if(outgoingCounter>1){
            return APIFail;                    
        }
        
        try{
            //delete relationships and finally delete node
            Vector<Relationship> relsToDelete  = new Vector<Relationship>();
            Iterator<Relationship> allRels = linkNode.getRelationships().iterator();
            while(allRels.hasNext()){
                Relationship rel = allRels.next();
                if(relsToDelete.contains(rel)==false){
                    relsToDelete.add(rel);
                }
            }

            for(Relationship rel: relsToDelete){
                rel.delete();
            }

            deleteNodeWithProperties(linkNode);
            linkNode=null;
        }
        catch(Exception ex){
            utils.handleException(ex);
            return APIFail;
        }
        
        return APISucc;
    }
    
    /**
     * Deletes an unnamed attribute with the given SYSID (given in attribute). 
     * 
     * It fails if a link with the given SYSID attribute does not exist or 
     * has dependencies with other objects.
     * 
     * @param attribute
     * @return 
     */
    public int CHECK_Delete_Unnamed_Attribute(Identifier attribute){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
START OF: sis_api::Delete_Unnamed_Attribute 
//  Delete_Unnamed_Attribute :
//		Deletes an Unamed Attribute with the logical name pointing from IDENTIFIER *
//		from to IDENTIFIER * to OR with the given sysID (given in sysID).
//    Returns 1 on Success, -1 on Failure.
//		Reasons to fail : 	A link with the given IDENTIFIER * attribute does not
//									exist.
//							  		The given link cannot be deleted because dependencies
//						   	  	with other objects exist.
int sis_api::Delete_Unnamed_Attribute(IDENTIFIER * attribute)
#ifdef CLIENT_SERVER
{
	CHECK_TRANSACTION("Delete_Unnamed_Attribute");
	int result;
	result = RFCache->InvalidateCache();
	if(result < 0) return result;  // Comms error
	// Send the function identifier
	if(SComm->send_int(  _DELETE_UNNAMED_ATTRIB) < 0)
		return -1;
	// Send the IDENTIFIER attribute
	if(SComm->send_IDENTIFIER(  attribute) < 0)
		return -1; // Failure on socket comms

	// Get the result of the function on the server
	if(SComm->recv_int(  &result) < 0)
		return -1;

	return result;
}
#else
{
	if (attribute->tag != ID_TYPE_SYSID)
		return APIFail;

	return Delete_Named_Attribute(attribute,0);
}
#endif        
        */
        // </editor-fold> 
        
        if(!check_transaction("Delete_Unnamed_Attribute")){
            return APIFail;
        }
        if(attribute== null || attribute.tag != Identifier.ID_TYPE_SYSID){
            return APIFail;
        }
        //the following code will not work and i am not sure that i want it 
        //to make it work --> allow null case, thats why i repeat the apporpriate steps 
        //return CHECK_Delete_Named_Attribute(attribute, null);        
        
        PrimitiveObject_Long iLink = new PrimitiveObject_Long();
        
        int ret = GetSYSID(attribute, iLink);
        if(ret == APIFail){
            return APIFail;
        }
        
        //1. link_name must exist
        Node linkNode = db.getNeo4jNodeByNeo4jId(iLink.getValue());
        
        if(linkNode ==null){
            return APIFail;
        }
        //2. if it has instances or subclasses then it cannot be deleted
        if(linkNode.hasRelationship(Configs.Rels.INSTANCEOF, Direction.INCOMING)){
            return APIFail;
        }        
        if(linkNode.hasRelationship(Configs.Rels.ISA, Direction.INCOMING)){
            return APIFail;
        }
        //3. skipped no class attrbiutes
        
        //4. if it is pointed by more than one objects then it can not be deleted (attribute to attribute case) 
        //   also check if relationship with from exists
        int homanyIncomingRelations = 0;
        Iterator<Relationship> incomingRels = linkNode.getRelationships(Configs.Rels.RELATION, Direction.INCOMING).iterator();
        while(incomingRels.hasNext()){
            Relationship rel = incomingRels.next();
            homanyIncomingRelations++;
        }
        
        if(homanyIncomingRelations>1){
            return APIFail;
        }
        
        
        //5. if it has other links then it can not be deleted (attribute to attribute case)
        int outgoingCounter = 0 ;
        Iterator<Relationship> outgoingRels = linkNode.getRelationships(Configs.Rels.RELATION, Direction.OUTGOING).iterator();
        while(incomingRels.hasNext()){
            Relationship rel = outgoingRels.next();
            if(rel.getEndNode().hasLabel(Configs.Labels.Type_Individual) ==false){
                return APIFail;
            }        
            outgoingCounter ++;
        }
        
        if(outgoingCounter>1){
            return APIFail;                    
        }
        
        try{
            //delete relationships and finally delete node
            Vector<Relationship> relsToDelete  = new Vector<Relationship>();
            Iterator<Relationship> allRels = linkNode.getRelationships().iterator();
            while(allRels.hasNext()){
                Relationship rel = allRels.next();
                if(relsToDelete.contains(rel)==false){
                    relsToDelete.add(rel);
                }
            }

            for(Relationship rel: relsToDelete){
                rel.delete();
            }

            deleteNodeWithProperties(linkNode);
            linkNode=null;
        }
        catch(Exception ex){
            utils.handleException(ex);
            return APIFail;
        }
        
        return APISucc;
        
    }
    
    /**
     * Deletes an instance pointing from from to to. 
     * 
     * It fails if an instance with the given from, 
     * to does not exist or the given link has dependencies 
     * with other objects exist.
     * 
     * @param from
     * @param to
     * @return 
     */
    public int CHECK_Delete_Instance(Identifier from, Identifier to){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        if(!check_transaction("Delete_Instance")){
            return APIFail;
        }
        return Modify(ModifyRelation.SIS_API_INSTOF, ModifyOperation.SIS_API_REMOVE, from, to,null);
    }
    
    
    
    /**
     * Deletes all the instances of set from_set pointing to to. 
     * 
     * It fails it an instance with the given from does not exist or 
     * has dependencies with other objects.
     * 
     * @param from_set
     * @param to
     * @return 
     */
    public int CHECK_IMPROVE_Delete_Instance_Set(int from_set, Identifier to){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        // START OF: sis_api::Delete_Instance_Set 
        //  Delete_Instance_Set :
        //  Deletes all Instances of set from_set pointing  to IDENTIFIER * to
        //
        //  Returns 1 on Success, -1 on Failure.
        //
        //  Reasons to fail : 	An instance with the given IDENTIFIER * does not exist.
        //
        //  The given link cannot be deleted because dependencies with other objects exist.
        //
int sis_api::Delete_Instance_Set(int from_set, IDENTIFIER * to)
#ifdef CLIENT_SERVER
{
	CHECK_TRANSACTION("Delete_Instance_Set");
	int result;
	result = RFCache->InvalidateCache();
	if(result < 0) return result;  // Comms error
	// Send the function identifier
	if(SComm->send_int(  _DELETE_INSTANCE_SET) < 0)
		return -1;
	// Send the set of objects
	if(SComm->send_int(  from_set) < 0)
		return -1; // Failure on socket comms
	// Send the IDENTIFIER to
	if(SComm->send_IDENTIFIER(  to) < 0)
		return -1; // Failure on socket comms

	// Get the result of the function on the server
	if(SComm->recv_int(  &result) < 0)
		return -1;

	return result;
}
#else
{
	int node_sysid;
	reset_set(from_set);
	l_name node_name, level;
	while (return_full_nodes(from_set, &node_sysid, node_name, level) != -1) {
		// delete the node as instance of to
		IDENTIFIER InodeTerm(node_sysid);
		// call Add_instance on every node of set (if one fails the whole set fails)
		if (Delete_Instance(&InodeTerm, to) == APIFail)
			return APIFail;
	}
	// Return Success
	return APISucc;
}
#endif
        */
        // </editor-fold> 
        if(!check_transaction("Delete_Instance_Set")){
            return APIFail;
        }
        
        PQI_Set fromSet = this.tmp_sets.return_set(from_set);
        if(fromSet==null){
            return APIFail;
        }
        Vector<Long> fromIds = fromSet.get_Neo4j_Ids();
        
        if(fromIds.size()==0){
            return APISucc;
        }
        boolean errorOccured = false;
        for(long fromL : fromIds){
            if(CHECK_Delete_Instance(new Identifier(fromL), to)==APIFail){
                errorOccured = true;
                break;
            }
        }
        
        if(errorOccured){
            return APIFail;
        }
        return APISucc;
    }
    
    /**
     * Deletes an isA pointing from from to to. 
     * 
     * It fails if an isA with the given from, to does not exist or 
     * the given isA has dependencies with other objects.
     * 
     * @param from
     * @param to
     * @return 
     */
    public int CHECK_Delete_IsA(Identifier from, Identifier to){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        if(!check_transaction("Delete_IsA")){
            return APIFail;
        }
        return Modify(ModifyRelation.SIS_API_ISA, ModifyOperation.SIS_API_REMOVE, from, to,null);
    }
    
    /**
     * Change the name of a node. 
     * 
     * The current node is specified by IDENTIFIER 
     * node and the new logical name for the node by NewNodeName. 
     * 
     * It fails if a node with the given IDENTIFIER node does not exist 
     * or a node with the given IDENTIFIER NewNodeName already exists.
     * 
     * @param node
     * @param NewNodeName
     * @return 
     */
    public int CHECK_Rename_Node(Identifier node, Identifier NewNodeName){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
//  Rename_Node :
//		Change the name of a node. The current node is specified by IDENTIFIER *
//		node and the new Logical Name for the node by NewNodeName.
//    Returns 1 on Success, -1 on Failure.
//		Reasons to fail : 	A node with the given IDENTIFIER * does not exist.
int sis_api::Rename_Node(IDENTIFIER * node, IDENTIFIER * NewNodeName)
#ifdef CLIENT_SERVER
{
	CHECK_TRANSACTION("Rename_Node");
	int result;
	result = RFCache->InvalidateCache();
	if(result < 0) return result;  // Comms error
	// Send the function identifier
	if(SComm->send_int(  _RENAME_NODE) < 0)
		return -1;
	// Send the IDENTIFIER node
	if(SComm->send_IDENTIFIER(  node) < 0)
		return -1; // Failure on socket comms
	// Send the IDENTIFIER NewNodeName
	if(SComm->send_IDENTIFIER(  NewNodeName) < 0)
		return -1; // Failure on socket comms

	// Get the result of the function on the server
	if(SComm->recv_int(  &result) < 0)
		return -1;

	return result;
}
#else
{
	LOGINAM *lnam[1];
	// node can be a sysid or logical name.
	if(node->tag != ID_TYPE_LOGINAM && node->tag != ID_TYPE_SYSID)
		return APIFail;
	// NewNodeName must be a logical name
	if(node->tag != ID_TYPE_LOGINAM)
		return APIFail;
	// Setup the new node name in a LOGINAM
	LOGINAM NewName(NewNodeName->loginam);
	// Now get the logical name of node. This function allocates space which
	// must be deleted when finished with it.
	if(GetLogicalName(node, lnam) == APIFail)
	{
		if(*lnam != NULL) delete *lnam;
		return APIFail;
	}
	// Now we have the node name in lnam. Call the semantic checker to do the
	// rename
	if(api_sem_check->renameObject(*lnam, &NewName) == ERROR)  // ERROR = -1
	{
		if(*lnam != NULL) delete *lnam;
		return APIFail;
	}
	delete *lnam;
	// Ok. Function succeded.
	return APISucc;
}
#endif        
        */
        // </editor-fold> 
        
        if(!check_transaction("Rename_Node")){
            return APIFail;                    
        }
        
        if(node==null || NewNodeName == null || NewNodeName.tag!= Identifier.ID_TYPE_LOGINAM){
            return APIFail;
        }
        
        if(NewNodeName.getLogicalName() ==null || NewNodeName.getLogicalName().length()==0){
            return APIFail;
        }
        
        //check node exists
        //check Newname is ok
        //check that New Name does not exist
        
        //check Newname is ok
        if(api_sem_check.newNodeNameIsOk(NewNodeName.getLogicalName())==false){
            return APIFail;
        }
        
        //check node exists
        PrimitiveObject_Long iNodeForRename = new PrimitiveObject_Long();
        GetSYSID(node, iNodeForRename);
        if(iNodeForRename.getValue()<=0){
            return APIFail;
        }
        
        //check that New Name does not exist ( GetSYSID still throws an exception if not found by getClassId)
        long iNewNodeNameL = db.getClassId(NewNodeName.getLogicalName());        
        if(iNewNodeNameL>0){
            return APIFail;
        }
        
        //rename node
        try{
            Node n = db.getNeo4jNodeByNeo4jId(iNodeForRename.getValue());
            if(n==null){
                return APIFail;
            }
            
            n.setProperty(db.Neo4j_Key_For_Logicalname, NewNodeName.getLogicalName());
        }
        catch(Exception ex){
            utils.handleException(ex);
            return APIFail;
        }
        return APISucc;
    }
    
    /**
     * Change the name of a named attribute. 
     * 
     * The current named attribute is specified by IDENTIFIER name (logical name) 
     * and the from value, or the SYSID in name. The new logical name for the 
     * named attribute is specified by NewName. It fails if a named attribute 
     * with the given IDENTIFIER name does not exist from IDENTIFIER from or a 
     * named nttribute with the given IDENTIFIER NewName already exists from 
     * IDENTIFIER from.
     * 
     * @param name
     * @param from
     * @param NewName
     * @return 
     */
    public int CHECK_Rename_Named_Attribute(Identifier name, Identifier from, Identifier NewName){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        START OF: sis_api::Rename_Named_Attribute 
//  Rename_Named_Attribute :
//		Change the name of a Named Attribute. The current Named Attribute is
//    specified by IDENTIFIER *attribute (Logical Name), the from value
//		OR the sysID in name. The new Logical Name for the Named Attribute is
//		specified by NewName.
//    Returns 1 on Success, -1 on Failure.
//		Reasons to fail : 	A Named Attribute with the given IDENTIFIER * does
//									not exist.
int sis_api::Rename_Named_Attribute(IDENTIFIER *attribute, IDENTIFIER * from, IDENTIFIER * NewName)
#ifdef CLIENT_SERVER
{
	CHECK_TRANSACTION("Rename_Named_Attribute");
	int result;
	result = RFCache->InvalidateCache();
	if(result < 0) return result;  // Comms error
	// Send the function identifier
	if(SComm->send_int(  _RENAME_NAMED_ATTRIB) < 0)
		return -1;
	// Send the IDENTIFIER attribute
	if(SComm->send_IDENTIFIER(  attribute) < 0)
		return -1; // Failure on socket comms
	// Send the IDENTIFIER from
	if(SComm->send_IDENTIFIER(  from) < 0)
		return -1; // Failure on socket comms
	// Send the IDENTIFIER NewName
	if(SComm->send_IDENTIFIER(  NewName) < 0)
		return -1; // Failure on socket comms

	// Get the result of the function on the server
	if(SComm->recv_int(  &result) < 0)
		return -1;

	return result;
}
#else
{
	LOGINAM *lOld;
	LOGINAM *lNew;
	LOGINAM *lAttribute;
	LOGINAM *lFrom;
	SYSID    iFrom;
	int      status;

	// Retrieve the logical name of %attribute
	switch (attribute->tag) {
	case ID_TYPE_SYSID:
		if (GetLogicalName(attribute,&lOld) == APIFail)
			return APIFail;
		break;
	case ID_TYPE_LOGINAM:
		// from should contain a system identifier
		if (from->tag != ID_TYPE_SYSID)
			return APIFail;
		if (GetSYSID(from,&iFrom) == APIFail)
			return APIFail;
		if ((lFrom = api_translator->getLogicalName(iFrom)) == 0)
			return APIFail;
		if (GetLogicalName(attribute,&lAttribute) == APIFail) {
			delete lFrom;
			return APIFail;
		}
		lOld = new FULATTNAM(lAttribute,lFrom);
		break;
	default:
		return APIFail;
	}

	// %newName should contain a logical name
	if (NewName->tag != ID_TYPE_LOGINAM) {
		delete lOld;
		return APIFail;
	}
	if (GetLogicalName(NewName,&lNew) == APIFail) {
		delete lOld;
		return APIFail;
	}

	status = api_sem_check->renameObject(lOld,lNew);
	delete lOld;
	delete lNew;

	return status  == ERROR ? APIFail : APISucc;  // ERROR = -1
}
#endif
        */
        // </editor-fold> 
        
        if(!check_transaction("Rename_Named_Attribute")){
            return APIFail;
        }
        //similar checks to the add attribute wiil apply 
        
        if(name ==null || from ==null){
            return APIFail;
        }
        // Check preconditions
	if (name.tag != Identifier.ID_TYPE_SYSID || from.tag != Identifier.ID_TYPE_SYSID){
            return APIFail;
        }
        
        //added by elias
        if(NewName.getLogicalName()==null || NewName.getLogicalName().length() ==0){
            return APIFail;
        }
        
        if(!api_sem_check.newNamedAttributeNameIsOk(NewName.getLogicalName())){
            return APIFail;
        }
        
        //check if from and to value exist
        //SKIP: check if from and to value are of >= than iLevel (already done)        
        //SKIP: check categories if catSet !=-1 then everything in cat set must be of iLevel i+1 and of type attribute
        //check if from -> new Attribute name combination exists 
        
        //check if from and link value exist
        PrimitiveObject_Long fromId = new PrimitiveObject_Long();
        if(GetSYSID(from, fromId)==APIFail){
            return APIFail;
        }
        
        PrimitiveObject_Long targetId = new PrimitiveObject_Long();
        if(GetSYSID(name, targetId)==APIFail){
            return APIFail;
        }
        
        Node fromNode = db.getNeo4jNodeByNeo4jId(fromId.getValue());
        Node targetLinkNode = db.getNeo4jNodeByNeo4jId(targetId.getValue());
        
        if(fromNode ==null || targetLinkNode ==null){
            return APIFail;
        }
        
        //from node exists
        try{
            //check if from -> attribute combination exists
            Iterator<Relationship> fromNodeRelIter = fromNode.getRelationships(Configs.Rels.RELATION, Direction.OUTGOING).iterator();
            while(fromNodeRelIter.hasNext()){
                String attrLoginam = (String)fromNodeRelIter.next().getEndNode().getProperty(db.Neo4j_Key_For_Logicalname);
                if(attrLoginam.equals(NewName.getLogicalName())){
                    return APIFail;
                }
            }

        
            targetLinkNode.setProperty(db.Neo4j_Key_For_Logicalname, NewName.getLogicalName());
        }
        catch(Exception ex){
            utils.handleException(ex);
            return APIFail;
        }
        return APISucc;
        
    }
    
    /**
     * Starts a querying session. 
     * 
     * During this session all API query functions can be called to 
     * retrieve information from the SIS base. Possible return values are: 
     * APIFail(-1), API_DB_CHANGED(0), API_DB_NOT_CHANGED(1), 
     * API_HASH_TABLES_NEED_EXPANSION(4), API_HASH_TABLES_EXPANDING(8).
     * 
     * @return 
     */
    public int TEST_begin_query(){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        
        if(!insideQuery){
            if(tx==null){
                tx = db.graphDb.beginTx();
            }
            beganTransaction = java.util.Calendar.getInstance().getTime().toString()+": begin_query ";
            //Logger.getLogger(QClass.class.getName()).log(Level.INFO, "beginTx from: TEST_begin_query");
            
            
        }
        insideQuery = true;
        //throw new UnsupportedOperationException();
        CurrentNode_Ids_Stack = new Vector<Long>();
        this.tmp_sets = new Sets_Class();
        return QClass.APISucc;
        /*TEST begin Query code
        public int TEST_begin_query() {
        if (this.CurrentNode_Ids_Stack != null) {
            if (Configs.boolDebugInfo) {
                Logger.getLogger(QClass.class.getName()).log(Level.INFO, "Already in a query");
            }
            return QClass.APIFail;
        }
        CurrentNode_Ids_Stack = new Vector<Long>();
        this.tmp_sets = new Sets_Class();
        return QClass.APISucc;
        //public native int begin_query(int sessionID);
        //throw new UnsupportedOperationException();
        */
    }
    void bugfix(){
        /*
        tx = db.graphDb.beginTx();
        try {
            
            Node n = db.graphDb.createNode();//.getNeo4jNodeByNeo4jId(5);
            n.setProperty(db.Neo4j_Key_For_Neo4j_Id, db.IMPROVE_getNextSystemNumber());
            //Logger.getLogger(QClass.class.getName()).log(Level.INFO, (String)n.getProperty(db.Neo4j_Key_For_Logicalname));
            n.delete();
            //n.setProperty(db.Neo4j_Key_For_Logicalname, "Dummy Node");
            //tx.success();
            
            //txnew.close();
            tx.success();
        }
        catch ( TransactionTerminatedException ignored )
        {
            db.handleException(ignored);
            //return String.format( "Created tree up to depth %s in 1 sec", depth );
        }
        finally{
            tx.close();
            tx=null;
        }
        */
        
    }
    /**
     * Ends a querying session.
     * 
     * @return 
     */
    public int TEST_end_query(){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        //throw new UnsupportedOperationException();
        
        if(insideQuery){
            free_all_sets();
            insideQuery = false;            
        }
        if(tx!=null){
            //Logger.getLogger(QClass.class.getName()).log(Level.INFO, "tx.success from end query");
            tx.success();
            beganTransaction = "";
            tx.close();
            tx=null;
            bugfix();
            insideTransaction = false;
        }
        CurrentNode_Ids_Stack = new Vector<Long>();
        this.tmp_sets = new Sets_Class();
        return QClass.APISucc;
        /*
        TEST_end_query() {
        //public native int end_query(int sessionID);
        if (this.CurrentNode_Ids_Stack == null) {
            if (Configs.boolDebugInfo) {
                Logger.getLogger(QClass.class.getName()).log(Level.INFO, "Not in a query");
            }
            return QClass.APIFail;
        }
        this.CurrentNode_Ids_Stack.clear();
        this.tmp_sets.ClearAndReset();
        this.CurrentNode_Ids_Stack = null;
        this.tmp_sets = null;
        return QClass.APISucc;
        */
    }
    private String beganTransaction = "";
    /**
     * Starts a transaction session. 
     * 
     * During this session API query and update functions may be called to 
     * retrieve and modify information from the SIS base. The system will not 
     * permit operations that will leave the database inconsistent. 
     * 
     * Possible return values are: APIFail(-1), API_DB_CHANGED(0), 
     * API_DB_NOT_CHANGED(1), API_HASH_TABLES_NEED_EXPANSION(4), 
     * API_HASH_TABLES_EXPANDING(8).
     * 
     * @return 
     */
    public int TEST_begin_transaction(){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        //throw new UnsupportedOperationException();
        //Logger.getLogger(QClass.class.getName()).log(Level.INFO, "Begin transaction called");
        
        this.insideQuery = true;
        this.insideTransaction = true;
        if(tx == null){
            tx = db.graphDb.beginTx();
        }
        beganTransaction = java.util.Calendar.getInstance().getTime().toString()+": begin_transaction ";
        CurrentNode_Ids_Stack = new Vector<Long>();
        this.tmp_sets = new Sets_Class();
        return APISucc;
        
    }
    
    /**
     * Ends a transaction session by committing the changes to the database files. 
     * 
     * It returns 1 on success, APIFail(-1) on failure. 
     * In case of failure the operations that took place between a 
     * begin_transaction() and an end_transaction() have not been committed.
     * 
     * @return 
     */
    public int TEST_end_transaction(){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        //throw new UnsupportedOperationException();
        if(insideTransaction || tx!=null){
            free_all_sets();
            //Logger.getLogger(QClass.class.getName()).log(Level.INFO, "tx.sucess from end transaction");
            tx.success();
            tx.close();
            beganTransaction = "";
            tx = null;
            bugfix();
            
            insideQuery = false;
            insideTransaction = false;
        }
        return APISucc;
    }
    
    /**
     * Aborts a transaction session. 
     * 
     * It returns 1 on success, APIFail(-1) on failure. 
     * In case of failure the operations that took place between a 
     * begin_transaction() and an abort_transaction() have not been committed.
     * 
     * @return 
     */
    public int TEST_abort_transaction(){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        if(insideQuery){
            free_all_sets();
            insideQuery = false;
        }
        if(insideTransaction || tx!=null){
            
            //Logger.getLogger(QClass.class.getName()).log(Level.INFO, beganTransaction +"\r\n"+java.util.Calendar.getInstance().getTime().toString()+": tx.failure from abort transaction");
            Logger.getLogger(QClass.class.getName()).log(Level.SEVERE, "Message From Logger: "+java.util.Calendar.getInstance().getTime().toString()+": tx.failure from abort transaction");
            tx.failure();
            tx.close();
            tx = null;    
            bugfix();
            insideTransaction = false;
        }
        return APISucc;
    }
    
    /**
     * This function is used to open a connection to the SIS server determined 
     * by create_SIS_CS_Session() or create_SIS_SA_Session() function. 
     * With the first implementation described, this function opens the
     * communication socket that establishes the communication with the server. 
     * With the second implementation this function just returns successfully.
     * Between an open_connection() and a close_connection() any number of calls
     * to begin_query()/end_query() and begin_transaction/end_transaction() 
     * can be performed.
     * 
     * @return 
     */
    public int TEST_open_connection(){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        //throw new UnsupportedOperationException();
        
        //free_all_sets();
        return APISucc;
    }
    Transaction tx =null;
    /**
     * This function is used to close a connection to a server, previously 
     * opened with open_connection(). With the first implementation described 
     * this function closes the communication sockets, that is it terminates 
     * the communication with the server. With the second implementation this 
     * function just returns successfully.
     * 
     * @return 
     */
    public int TEST_close_connection(){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        //throw new UnsupportedOperationException();
        
        return APISucc;
    }
    
    /**
     * Creates a session and sets the hostname and the port that this querying 
     * session will interact with. 
     * 
     * The variable sessionID will contain new session id. 
     * If this function is not called, any other call to some other function 
     * of the API will fail. The DBUserName and DBUserPassword will be used in 
     * the future to validate the requested connection. Currently they are not 
     * used but the should not be NULL.
     * 
     * In case the application is linked with the second implementation of API 
     * (direct access to the SIS database) the following functions are used.
     * Note that, in this case, only one session should be created.
     * 
     * @param serv_host
     * @param serv_port
     * @param DBUserName
     * @param DBUserPassword
     * @return 
     */
    public int TEST_create_SIS_CS_Session(GraphDatabaseService useGraphDb/*String serv_host, int serv_port, String DBUserName, String DBUserPassword*/){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        
        db=new DBaccess(useGraphDb);
        
        //if(useGraphDb.equals(db.graphDb)){
//            Logger.getLogger(QClass.class.getName()).log(Level.INFO, "true");
  //      }
    //    else{
      //      Logger.getLogger(QClass.class.getName()).log(Level.INFO, "false");
        //}
        //throw new UnsupportedOperationException();
        if(this.db.graphDb!=null){
            api_sem_check = new semanticChecker(db);
            return APISucc;            
        }
        
        return APIFail;
    }
    
    /**
     * This function should be called to release the session (sessionID) 
     * created by create_SIS_CS_Session() or create_SIS_SA_Session().
     * 
     * @return 
     */
    public int TEST_release_SIS_Session(){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        //throw new UnsupportedOperationException();
        return APISucc;
    }
    
    /**
     * ATTENTION !!! NOT IMPLEMENTED use bulk_return_full_link instead<br/>
     * <br/>
     * Return the objects of set set_id. <br/>
     * <br/>
     * It is supposed that the objects are link nodes are so it returns the 
     * logical name of each object (label), the logical name of the class where 
     * it pointing from (cls), the object it is pointing to (cmv) which is a 
     * structure cm_value described later and also the category of the returned 
     * link (from_cls, categ).
     * <br/>
     * Flag unique_category indicates if given category is unique (link object 
     * may have more than one class) and flag traversed indicates if the 
     * specific link belongs to a category that was previously set with the 
     * set_categories() function with direction BACKWARD.
     * <br/>
     * @param set_id
     * @param cls
     * @param label
     * @param categ
     * @param fromcls
     * @param cmv
     * @param unique_category
     * @param traversed
     * @return 
     */
    public int return_full_link( int set_id, StringObject cls, StringObject label, StringObject categ, StringObject fromcls, CMValue cmv, IntegerObject unique_category, IntegerObject traversed){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        throw new UnsupportedOperationException();
    }
    
    /**
     * ATTENTION !!! NOT IMPLEMENTED use bulk_return_full_link_id instead
     * 
     * Return the objects of set set_id. 
     * 
     * It is supposed that the objects are link nodes are so it returns the 
     * logical name of each object (label), its system identifier (*linkid), 
     * the logical name of the class where it is pointing from (cls) and its 
     * system identifier (*clsid), the object it is pointing to (cmv) which is 
     * a structure cm_value described later and also the category of the 
     * returned link (from_cls, categ) and its system identifier (*categid).
     * 
     * Flag unique_category indicates if given category is unique (link object 
     * may have more than one class) and flag traversed indicates if the 
     * specific link belongs to a category that was previously set with the 
     * set_categories() function with direction BACKWARD.
     * 
     * @param set_id
     * @param cls
     * @param clsid
     * @param label
     * @param linkid
     * @param categ
     * @param fromcls
     * @param categid
     * @param cmv
     * @param unique_category
     * @return 
     */
    public int return_full_link_id(int set_id, StringObject cls, PrimitiveObject_Long clsid, StringObject label,
            PrimitiveObject_Long linkid, StringObject categ, StringObject fromcls, PrimitiveObject_Long categid, CMValue cmv,
            IntegerObject unique_category){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        throw new UnsupportedOperationException();
    }
 
    
    /**
     * ATTENTION !!! NOT IMPLEMENTED use bulk_return_return_isA instead
     * 
     * Return the logical names ob1 and ob2 of a pair of objects A and B, 
     * existing in set set_id and A isA B.
     * 
     * @param set_id
     * @param ob1
     * @param ob2
     * @return 
     */
    public int return_isA(int set_id, StringObject ob1, StringObject ob2){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        throw new UnsupportedOperationException();
    }
    
    /**
     * similar to return_full_link but in bulk mode not one by one
     * 
     * @param set_id
     * @param retVals
     * @return 
     */
    public int bulk_return_full_link(int set_id, Vector<Return_Full_Link_Row> retVals){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        if(!check_files("bulk_return_full_link")){
            return APIFail;
        }
        PQI_Set requestedSet = this.tmp_sets.return_set(set_id);
        if(requestedSet==null){
            return APIFail;
        }
        Vector<Long> setIds = requestedSet.get_Neo4j_Ids();
        
        retVals.clear();
        
        if(setIds.size()==0){
            return APISucc;
        }
        
        if(db.get_Bulk_Return_Full_Link_Rows(setIds, retVals,backward_set.get_Neo4j_Ids())==QClass.APIFail){
            return APIFail;
        }
        return APISucc;
    }
    
    /**
     * similar to return_full_link_id but in bulk mode not one by one
     * @param set_id
     * @param retVals
     * @return 
     */
    public int bulk_return_full_link_id(int set_id, Vector<Return_Full_Link_Id_Row> retVals){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        if(!check_files("bulk_return_full_link_id")){
            return APIFail;
        }
        PQI_Set requestedSet = this.tmp_sets.return_set(set_id);
        if(requestedSet==null){
            return APIFail;
        }
        Vector<Long> setIds = requestedSet.get_Neo4j_Ids();
        
        retVals.clear();
        
        if(setIds.size()==0){
            return APISucc;
        }
        
        if(db.get_Bulk_Return_Full_Link_Id_Rows(setIds, retVals)==QClass.APIFail){
            return APIFail;
        }
        return APISucc;
    }
    
    /**
     * similar to return_isA but in bulk mode not one by one
     * @param set_id
     * @param retVals
     * @return 
     */
    public int bulk_return_isA(int set_id, Vector<Return_Isa_Row> retVals){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        if(!check_files("bulk_return_isA")){
            return APIFail;
        }
        PQI_Set requestedSet = this.tmp_sets.return_set(set_id);
        if(requestedSet==null){
            return APIFail;
        }
        Vector<Long> setIds = requestedSet.get_Neo4j_Ids();
        
        retVals.clear();
        
        if(setIds.size()==0){
            return APISucc;
        }
        
        if(db.get_Bulk_Return_Isa_Rows(setIds, retVals)==QClass.APIFail){
            return APIFail;
        }
        return APISucc;
    }

    /**
     * If set_id is 0, get all objects that are connected to the current node by
     * links pointing from them. Links must be instances of the category given. 
     * The category is defined by the name of the link and the class of which 
     * it is pointing from. 
     * 
     * The answer set contains the system identifiers of these objects. 
     * In the case of class attributes the returned objects are the computed 
     * instance set.
     * 
     * If set_id is a positive integer, apply get_from_node_by_category() 
     * on each object in temporary set set_id.
     * 
     * @param set_id
     * @param fromcls
     * @param categ
     * @return 
     */
    public int get_from_node_by_category(int set_id, StringObject fromcls, StringObject categ){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        
        if(!check_files("get_from_node_by_category")){
            return APIFail;
        }
        return categ_query(CATEG_QUERY_IDENDIFIERS._GET_FROM_NODE_BY_CATEGORY, set_id, fromcls, categ);
    }
    
    /**
     * If set_id is 0, get all objects that are connected to the current node 
     * by links pointing to them. Links must be instances of the category given.
     * The category is defined by the name of the link and the class of which it
     * is pointing from. The answer set contains the system identifiers of these
     * objects.
     * 
     * If set_id is a positive integer, apply get_to_node_by_category() 
     * on each object in temporary set set_id.
     * 
     * @param set_id
     * @param fromcls
     * @param categ
     * @return 
     */
    public int get_to_node_by_category(int set_id, StringObject fromcls, StringObject categ){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        if(!check_files("get_to_node_by_category")){
            return APIFail;
        }
        return categ_query(CATEG_QUERY_IDENDIFIERS._GET_TO_NODE_BY_CATEGORY, set_id, fromcls, categ);
    }
    
    enum CATEG_QUERY_IDENDIFIERS { _GET_LINK_FROM_BY_CATEGORY,_GET_LINK_FROM_BY_META_CATEGORY,_GET_LINK_TO_BY_CATEGORY,_GET_LINK_TO_BY_META_CATEGORY,
    _GET_TO_NODE_BY_CATEGORY,_GET_FROM_NODE_BY_CATEGORY,_GET_TO_NODE_BY_META_CATEGORY,_GET_FROM_NODE_BY_META_CATEGORY};
    /*
    int GET_NEW_SET_OR_RETURNAPIFail(PQI_Set writeset){
        int new_set_id = APIFail;
        if ((new_set_id = tmp_sets.return_new_set()) == APIFail) {  
		return APIFail;
	}
        writeset = tmp_sets.return_set(new_set_id);
        
        
	return new_set_id;
    }*//*
    int GET_NEW_SET_OR_RETURNAPIFail(PQI_Set writeset){
        int new_set_id = APIFail;
        if ((new_set_id = tmp_sets.return_new_set()) == APIFail) {  
		return APIFail;
	}
        writeset = tmp_sets.return_set(new_set_id);
        
        
	return new_set_id;
    }*/
    
    
    
    int categ_query(CATEG_QUERY_IDENDIFIERS q_id,int set_id, StringObject fromcls, StringObject categ){
        
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        // Execute a query with a category as argument.
        //  Returns the set identifier where the answer is stored and
        //  ErrorCode when an error occured.
        int sis_api::categ_query(int q_id, int set_id, l_name fromcls, l_name categ)
        #ifdef CLIENT_SERVER
        {
                int ret;

                CHECK_FILES("categ_query");
                int result;
                result = RFCache->InvalidateCache();
                if(result < 0) return result;  // Comms error
                if((result=SComm->send_int(  q_id))>0)
                        if((result=SComm->send_int(  set_id))>0)
                                if((result=SComm->send_str(  fromcls))>0)
                                        if((result=SComm->send_str(  categ))>0)
                                                if((result=SComm->recv_int(  &ret))>0)
                                                        return ret;
                return result;
        }
        #else
        {
                int    new_set_id,
                        ret;
                SET   *writeset;
                SET   *setptr;

                // Tuple mechanism 
                setptr=0;
                if (set_id) {
                        if ((setptr = tmp_sets.return_set(set_id)) == NULL)
                        {
                                return -1;      // set doesn't exist...
                        }
                }
                if ((setptr) && (setptr->set_tuple_mode()))
                {
                        writeset=setptr;
                        new_set_id=set_id;
                }
                else GET_NEW_SET_OR_RETURN(new_set_id,writeset);
                // new_set_id is the new set for storing the answer and writeset is the SET*


                if (set_id == 0) {   // apply query on current node
                        if (no_current_node("categ_query")) {
                                tmp_sets.free_set(new_set_id);
                                return -1;    // there is no current node set
                        }

                        switch (q_id) {
                        case _GET_LINK_FROM_BY_CATEGORY :
                                ret = getLinkFromByCategory(*current_node,fromcls,categ,writeset);
                                break;
                        case _GET_LINK_FROM_BY_META_CATEGORY :
                                ret = getLinkFromByMetaCategory(*current_node,fromcls,categ,writeset);
                                break;
                        case _GET_LINK_TO_BY_CATEGORY :
                                ret = getLinkToByCategory(*current_node,fromcls,categ,writeset);
                                break;
                        case _GET_LINK_TO_BY_META_CATEGORY :
                                ret = getLinkToByMetaCategory(*current_node,fromcls,categ,writeset);
                                break;
                        case _GET_TO_NODE_BY_CATEGORY :
                                ret = getToNodeByCategory(*current_node,fromcls,categ,writeset);
                                break;
                        case _GET_FROM_NODE_BY_CATEGORY :
                                ret = getFromNodeByCategory(*current_node,fromcls,categ,writeset);
                                break;
                        case _GET_TO_NODE_BY_META_CATEGORY :
                                ret = getToNodeByMetaCategory(*current_node,fromcls,categ,writeset);
                                break;
                        case _GET_FROM_NODE_BY_META_CATEGORY :
                                ret = getFromNodeByMetaCategory(*current_node,fromcls,categ,writeset);
                                break;
                        }
                }
                else{       // apply query on each object in set set_id
                        if ((setptr = tmp_sets.return_set(set_id)) == NULL) {
                                return -1;      // set doesn't exist...
                        }

                        switch (q_id) {
                        case _GET_LINK_FROM_BY_CATEGORY :
                                ret = getLinkFromByCategory(setptr,fromcls,categ,writeset);
                                break;
                        case _GET_LINK_FROM_BY_META_CATEGORY :
                                ret = getLinkFromByMetaCategory(setptr,fromcls,categ,writeset);
                                break;
                        case _GET_LINK_TO_BY_CATEGORY :
                                ret = getLinkToByCategory(setptr,fromcls,categ,writeset);
                                break;
                        case _GET_LINK_TO_BY_META_CATEGORY :
                                ret = getLinkToByMetaCategory(setptr,fromcls,categ,writeset);
                                break;
                        case _GET_TO_NODE_BY_CATEGORY :
                                ret = getToNodeByCategory(setptr,fromcls,categ,writeset);
                                break;
                        case _GET_FROM_NODE_BY_CATEGORY :
                                ret = getFromNodeByCategory(setptr,fromcls,categ,writeset);
                                break;
                        case _GET_TO_NODE_BY_META_CATEGORY :
                                ret = getToNodeByMetaCategory(setptr,fromcls,categ,writeset);
                                break;
                        case _GET_FROM_NODE_BY_META_CATEGORY :
                                ret = getFromNodeByMetaCategory(setptr,fromcls,categ,writeset);
                                break;
                        }
                }

                ON_ERROR_RETURN(ret,new_set_id);  // free new_set_id and return -1

                return new_set_id;
        }
        #endif
        */
        // </editor-fold> 
        
        if(!check_files("categ_query")){
            return APIFail;
        }
        
        PQI_Set setptr = new PQI_Set();
        int new_set_id = set_get_new();
        if(new_set_id==APIFail){
            return APIFail;
        }
        PQI_Set writeset=tmp_sets.return_set(new_set_id);
        if(writeset==null){
            free_set(new_set_id);
            return APIFail;
        }
        int ret = QClass.APIFail;
        
        if (set_id == 0) {   
            // apply query on current node
            if (no_current_node("categ_query")) {
                tmp_sets.free_set(new_set_id);
                return APIFail;    // there is no current node set
            }
            
            setptr.set_putNeo4j_Id(CurrentNode_Ids_Stack.lastElement());
        }
        else{
            setptr.set_copy(tmp_sets.return_set(set_id));
        }
        
        //
        switch(q_id){
            case _GET_LINK_FROM_BY_CATEGORY:{
                ret = db.getLink_From_or_To_ByCategory(setptr.get_Neo4j_Ids(),fromcls,categ,writeset,true);
                break;
            }
            case _GET_LINK_TO_BY_CATEGORY:{
                ret = db.getLink_From_or_To_ByCategory(setptr.get_Neo4j_Ids(),fromcls,categ,writeset,false);
                break;
            }
            case _GET_LINK_FROM_BY_META_CATEGORY:{
                ret = db.getLink_From_or_To_ByMETACategory(setptr.get_Neo4j_Ids(),fromcls,categ,writeset,true);
                break;
            }
            case _GET_LINK_TO_BY_META_CATEGORY:{
                ret = db.getLink_From_or_To_ByMETACategory(setptr.get_Neo4j_Ids(),fromcls,categ,writeset,false);
                break;
            }
            case _GET_FROM_NODE_BY_CATEGORY:{
                ret = db.getFrom_or_To_NodeByCategory(setptr.get_Neo4j_Ids(),fromcls,categ,writeset,true);
                break;
            }
            case _GET_TO_NODE_BY_CATEGORY:{
                ret = db.getFrom_or_To_NodeByCategory(setptr.get_Neo4j_Ids(),fromcls,categ,writeset,false);
                break;
            }
            default:{
                Logger.getLogger(QClass.class.getName()).log(Level.INFO, q_id.name());
                throw new UnsupportedOperationException();
            }
        }
        
        //ON_ERROR_RETURN(ret,new_set_id);  // free new_set_id and return -1
        if ((globalError.flag()==APIFail) || (ret == APIFail))  {
            globalError.reset();
            tmp_sets.free_set(new_set_id);
            return APIFail;
        }
        
        return new_set_id;
        
    }
    
    /**
     * If set_id is 0, get the links pointing from the current node and are 
     * instances of the category given. The category is defined by the name of 
     * the link and the class of which it is pointing from. The answer set 
     * contains the system identifiers of these links.
     * 
     * If set_id is a positive integer, apply get_link_from_by_category() 
     * on each object in temporary set set_id.
     * 
     * @param set_id
     * @param fromcls
     * @param categ
     * @return 
     */
    public int get_link_from_by_category(int set_id, StringObject fromcls, StringObject categ){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        int sis_api::get_link_from_by_category(int set_id, l_name fromcls, l_name categ)
        {
                CHECK_FILES("get_link_from_by_category");
                return  categ_query(_GET_LINK_FROM_BY_CATEGORY,set_id,fromcls,categ);
        }
        */
        // </editor-fold> 
        if(!check_files("get_link_from_by_category")){
            return APIFail;
        }
        return categ_query(CATEG_QUERY_IDENDIFIERS._GET_LINK_FROM_BY_CATEGORY, set_id, fromcls, categ);
    }
    /**
     * If set_id is 0, get the links pointing from the current node and are 
     * instances of some links class that is instance of the category given. 
     * 
     * The meta-category is defined by the name of the link and the class of 
     * which it is pointing from. The answer set contains the system identifiers
     * of these links.
     * 
     * If set_id is a positive integer, apply get_link_from_by_meta_category() 
     * on each object in temporary set set_id.
     * 
     * @param set_id
     * @param fromcls
     * @param categ
     * @return 
     */
    public int get_link_from_by_meta_category(int set_id, StringObject fromcls, StringObject categ){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        int sis_api::get_link_from_by_meta_category(int set_id, l_name fromcls, l_name categ)
        {
                CHECK_FILES("get_link_from_by_meta_category");
                return  categ_query(_GET_LINK_FROM_BY_META_CATEGORY,set_id,fromcls,categ);
        }
        */
        // </editor-fold> 
        if(!check_files("get_link_from_by_meta_category")){
            return APIFail;
        }
        return categ_query(CATEG_QUERY_IDENDIFIERS._GET_LINK_FROM_BY_META_CATEGORY, set_id, fromcls, categ);
    }
    
    /**
     * If set_id is 0, get the links pointing to the current node and are 
     * instances of some links class that is instance of the category given. 
     * 
     * The meta-category is defined by the name of the link and the class of 
     * which it is pointing from. The answer set contains the system identifiers
     * of these links.
     * 
     * If set_id is a positive integer, apply get_link_to_by_meta_category() 
     * on each object in temporary set set_id.
     * 
     * @param set_id
     * @param fromcls
     * @param categ
     * @return 
     */
    public int get_link_to_by_meta_category(int set_id, StringObject fromcls, StringObject categ){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        int sis_api::get_link_to_by_meta_category(int set_id, l_name fromcls, l_name categ)
        {
                CHECK_FILES("get_link_to_by_meta_category");
                return  categ_query(_GET_LINK_TO_BY_META_CATEGORY,set_id,fromcls,categ);
        }
        */
        // </editor-fold> 
        if(!check_files("get_link_to_by_meta_category")){
            return APIFail;
        }
        return categ_query(CATEG_QUERY_IDENDIFIERS._GET_LINK_TO_BY_META_CATEGORY, set_id, fromcls, categ);
    }
    
    /**
     * If set_id is 0, get the links pointing to the current node and are 
     * instances of the category given. The category is defined by the name 
     * of the link and the class of which it is pointing from. The answer set 
     * contains the system identifiers of these links.
     * 
     * If set_id is a positive integer, apply get_link_to_by_category() 
     * on each object in temporary set set_id.
     * 
     * @param set_id
     * @param fromcls
     * @param categ
     * @return 
     */
    public int get_link_to_by_category(int set_id, StringObject fromcls, StringObject categ){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        if(!check_files("get_link_to_by_category")){
            return APIFail;
        }
        return categ_query(CATEG_QUERY_IDENDIFIERS._GET_LINK_TO_BY_CATEGORY, set_id, fromcls, categ);
    }
    
    /**
     * From all the objects in the set obj_set_id select those objects that 
     * match any of the string patterns in the set ptrn_set_id. 
     * 
     * The match is case sensitive. The set ptrn_set_id can be constructed 
     * using the set_put_prm() function. The answer set contains all the 
     * selected objects. 
     * 
     * If obj_set_id is 0, perform the match on the current node and if there 
     * is a match put the current node in the answer set or return an empty set.
     * 
     * @param obj_set_id
     * @param ptrn_set_id
     * @return 
     */
    public int get_matched(int obj_set_id, int ptrn_set_id){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*        
        //  Get a set of pattern_strings to be matched to the first set
        //  that contains the objects
        //  Returns the set containing the matched objects on success, -1 on error.
        int sis_api::get_matched(int obj_set_id, int ptrn_set_id)
        #ifdef CLIENT_SERVER
        {
                int ret;

                CHECK_FILES("get_matched");
                int result;
                result = RFCache->InvalidateCache();
                if(result < 0) return result;  // Comms error
                if((result=SComm->send_int(  _GET_MATCHED))>0)
                        if((result=SComm->send_int(  obj_set_id))>0)
                                if((result=SComm->send_int(  ptrn_set_id))>0)
                                        if((result=SComm->recv_int(  &ret))>0)
                                                return ret;
                return result;
        }
        #else
        {
                int  new_set_id, ret;
                SET *writeset;
                SET *setptr, *ptrn_setptr;

                CHECK_FILES("get_matched");

                if ( (!(ptrn_set_id)) ||  // pattern set can't be current node
                        ((ptrn_setptr = tmp_sets.return_set(ptrn_set_id)) == NULL) )
                        return -1;      // set doesn't exist...

                // Tuple mechanism 
                setptr=0;
                if (obj_set_id) {
                        if ((setptr = tmp_sets.return_set(obj_set_id)) == NULL) {
                                return -1;      // set doesn't exist...
                        }
                }
                if ((setptr) && (setptr->set_tuple_mode())) {
                        writeset=setptr;
                        new_set_id=obj_set_id;
                }
                else GET_NEW_SET_OR_RETURN(new_set_id,writeset);
                // new_set_id is the new set for storing the answer and writeset is the SET*


                if (obj_set_id == 0) { // apply query on current node
                        if (no_current_node("get_matched")) {
                                tmp_sets.free_set(new_set_id);
                                return -1;       // there is no current node set
                        }
                        ret = getMatched(*current_node, ptrn_setptr, writeset);
                }
                else {
                        if ((setptr = tmp_sets.return_set(obj_set_id)) == NULL) {
                                return -1;       // set doesn't exist...
                        }
                        ret = getMatched(setptr, ptrn_setptr, writeset);
                }

                ON_ERROR_RETURN(ret,new_set_id);  // free new_set_id and return -1

                return new_set_id;
        }
        #endif
        */
        // </editor-fold> 
        
        int  ret;
        PQI_Set setptr = new PQI_Set();
        PQI_Set ptrn_setptr = new PQI_Set();
        
        if(!check_files("get_matched")){
            return APIFail;
        }
        
        if ( ptrn_set_id ==0 ){
            // pattern set can't be current node
            return APIFail;
        }
        ptrn_setptr = tmp_sets.return_set(ptrn_set_id);
        if(ptrn_setptr==null){
             // set doesn't exist...
            return APIFail;
        }
        
        
        int new_set_id = this.set_get_new();
        PQI_Set writeset=tmp_sets.return_set(new_set_id);
        if(writeset==null){
            free_set(new_set_id);
            return APIFail;
        }
        
        if(obj_set_id==0){
            if (no_current_node("get_matched")) {
                tmp_sets.free_set(new_set_id);
                return APIFail;       // there is no current node set
            }
            setptr.set_putNeo4j_Id(CurrentNode_Ids_Stack.lastElement());
        }
        else{
            
            setptr = tmp_sets.return_set(obj_set_id);
            if(setptr == null){
                tmp_sets.free_set(new_set_id);
                return APIFail;
            }

        }
        
        //writes directly to writeset since any exception occurs 
        //only before the first writing to writeset
        ret = db.getMatched(setptr.get_Neo4j_Ids(), ptrn_setptr, writeset);
        
        //ON_ERROR_RETURN(ret,new_set_id);  // free new_set_id and return -1
        if ((globalError.flag()==APIFail) || (ret == APIFail))  {
            globalError.reset();
            tmp_sets.free_set(new_set_id);
            return APIFail;
        }
        
        return new_set_id;        
    }
    
    public enum MatchStringTypes {STRING_MATCHED,STRING_LESS_EQUAL,STRING_LESS_THAN,STRING_EQUAL,STRING_NOT_EQUAL};
    
    
    /**
     * ATTENTION !!! NOT IMPLEMENTED
     * 
     * From all the objects in the set obj_set_id select those objects that 
     * match the string in cmv according to the match type criterion 
     * (STRING_MATCHED, STRING_LESS_EQUAL, STRING_LESS_THAN, STRING_EQUAL, 
     * STRING_NOT_EQUAL). In case of STRING_MATCHED it performs the selection 
     * using the string as a pattern string as described above. In all other 
     * cases the match is performed on the exact string. The answer set contains
     * all the selected objects. If obj_set_id is 0, perform the match on the 
     * current node and if there is a match put the current node in the answer 
     * set or return an empty set.
     * 
     * @param obj_set_id
     * @param cmv
     * @param match_type
     * @return 
     */
    public int get_matched_string(int obj_set_id, CMValue cmv, MatchStringTypes match_type){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //  Get a pattern_string to be matched to the set that contains the
        // objects according to the match type.
        // Returns the set containing the matched objects on success, -1 on error
        
        int sis_api::get_matched_string(int obj_set_id, cm_value *cmv, int match_type)
        #ifdef CLIENT_SERVER
        {
                int ret;

                CHECK_FILES("get_matched_string");
                int result;
                result = RFCache->InvalidateCache();
                if(result < 0) return result;  // Comms error
                if((result=SComm->send_int(_GET_MATCHED_STRING))>0)
                        if((result=SComm->send_int(obj_set_id))>0)
                                if((result=SComm->send_prs(cmv))>0)
                                        if((result=SComm->send_int(match_type))>0)
                                                if((result=SComm->recv_int(&ret))>0)
                                                        return ret;
                return result;
        }
        #else
        {
                prs_val val;
                int  new_set_id, ret;
                SET *writeset;
                SET *setptr;
                char* ptrn_str;

                Copy_cm_value(&val, cmv, NULL);
                if ((val.tag() != STRING_TAG) || (!val.sval()))
                        return -1;      // string doesn't exist...

                // Tuple mechanism 
                setptr=0;
                if (obj_set_id) {
                        if ((setptr = tmp_sets.return_set(obj_set_id)) == NULL) {
                                return -1;      // set doesn't exist...
                        }
                }
                if ((setptr) && (setptr->set_tuple_mode())) {
                        writeset=setptr;
                        new_set_id=obj_set_id;
                }
                else GET_NEW_SET_OR_RETURN(new_set_id,writeset);
                // new_set_id is the new set for storing the answer and writeset is the SET*

                ptrn_str = val.sval(); // no need for strdup

                if (obj_set_id != 0) { // does not apply query on current node
                        if ((setptr = tmp_sets.return_set(obj_set_id)) == NULL) {
                                return -1;       // set doesn't exist...
                        }
                        ret = getMatchedString(setptr, ptrn_str, match_type, writeset);
                }

                ON_ERROR_RETURN(ret,new_set_id);  // free new_set_id and return -1

                return new_set_id;
        }
        #endif
        */
        // </editor-fold> 
        
        
        int  ret =APIFail;
        //PQI_Set setptr = new PQI_Set();
        String ptrn_str = "";
        
        if(!check_files("get_matched_string")){
            return APIFail;
        }
     
        //if ((val.tag() != STRING_TAG) || (!val.sval())) return -1;      // string doesn't exist...
        //this 3rd case not sure about zero length
        if(cmv.type!=CMValue.TYPE_STRING || cmv.getString()==null || cmv.getString().length()==0){
            return APIFail;
        }
        
        if(obj_set_id==0){
             // does not apply query on current node
            return APIFail; 
        }
        //NOT IMPLEMENTED
        if(obj_set_id!=0){
            Logger.getLogger(QClass.class.getName()).log(Level.INFO, "get_matched_string called with str val = "+ cmv.getString());
            throw new UnsupportedOperationException();
        }
        
        int new_set_id = this.set_get_new();
        PQI_Set writeset=tmp_sets.return_set(new_set_id);
        if(writeset==null){
            free_set(new_set_id);
            return APIFail;
        }
        
        
	ptrn_str = cmv.getString(); // no need for strdup

	
        PQI_Set setptr = tmp_sets.return_set(obj_set_id);
        
        if(setptr == null){
            tmp_sets.free_set(new_set_id);
            return APIFail;
        }

        ret = db.getMatchedString(setptr.get_Neo4j_Ids(), ptrn_str, match_type, writeset);
        
        //ON_ERROR_RETURN(ret,new_set_id);  // free new_set_id and return -1
        if ((globalError.flag()==APIFail) || (ret == APIFail))  {
            globalError.reset();
            tmp_sets.free_set(new_set_id);
            return APIFail;
        }
        
        
        return APISucc;
    }
    
    //get_matched_ToneAndCaseInsensitive
    public int get_matched_ToneAndCaseInsensitive(int set_target, String searchVal, boolean SEARCH_MODE_CASE_TONE_INSENSITIVE) {
        //USED IN WEBTMS API ONLY
        /*
        -----------------------------------------------------------------
                get_matched_ToneAndCaseInsensitive()
    -----------------------------------------------------------------------
    INPUT: - int sisSessionId: the current SIS-API session id
           - int set_target: the set to be searched
           - String searchVal: the pattern to be searched (in UI encoding)
           - boolean SEARCH_MODE_CASE_TONE_INSENSITIVE: true in case the search will  be done in tone 
             and case insensitive mode. This value is configured by the same parameter of web.xml            
    OUTPUT: - int set_results: the result set of SIS-API get_matched()
    FUNCTION: - in case of SEARCH_MODE_CASE_TONE_INSENSITIVE = true:
                calls the SIS-API get_matched() with a set of ALL Tone and Case insensitive comparisons of searchVal
              - in case of SEARCH_MODE_CASE_TONE_INSENSITIVE = false:
                calls the SIS-API get_matched() with the given searchVal
    CALLED BY: all search mechanisms of WebTMS instead of SIS-API get_matched()
    -----------------------------------------------------------------
        */
        /*    
    public int get_matched_ToneAndCaseInsensitive(int sisSessionId, int set_target, String searchVal, boolean SEARCH_MODE_CASE_TONE_INSENSITIVE) {
        UtilitiesString US = new UtilitiesString();
        Vector<String> InputValuesVector = new Vector();
        if (SEARCH_MODE_CASE_TONE_INSENSITIVE) {
            InputValuesVector = US.GetToneAndCaseInsensitiveComparisonsOfPattern(searchVal);
        }
        else {
            InputValuesVector.add(searchVal);
        }
        int ptrn_set = Q.set_get_new();
        int InputValuesSize = InputValuesVector.size();
        for (int i = 0; i < InputValuesSize; i++) {
            CMValue prm_val = new CMValue();
            prm_val.assign_string(InputValuesVector.get(i));
            Q.set_put_prm( ptrn_set, prm_val);            
        }

        Q.reset_set(set_target);
        Q.reset_set(ptrn_set);
        int set_results = Q.get_matched(set_target, ptrn_set);
        Q.free_set(ptrn_set);
        Q.reset_set(set_results);
        return set_results;
    }        
        */
        
        UtilitiesString US = new UtilitiesString();
        Vector<String> InputValuesVector = new Vector<String>();
        if (SEARCH_MODE_CASE_TONE_INSENSITIVE) {
            InputValuesVector = US.GetToneAndCaseInsensitiveComparisonsOfPattern(searchVal);
        }
        else {
            InputValuesVector.add(searchVal);
        }
        int ptrn_set = set_get_new();
        int InputValuesSize = InputValuesVector.size();
        for (int i = 0; i < InputValuesSize; i++) {
            CMValue prm_val = new CMValue();
            prm_val.assign_string(InputValuesVector.get(i));
            set_put_prm( ptrn_set, prm_val);            
        }

        reset_set(set_target);
        reset_set(ptrn_set);
        int set_results = get_matched(set_target, ptrn_set);
        free_set(ptrn_set);
        reset_set(set_results);
        return set_results;
    }
    
    public boolean CHECK_isUnNamedLink(long linkSystemId){
        
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*-----------------------------------------------------------------
                            IsNamedLink()
        -------------------------------------------------------------------
        INPUT:  - sysid : the sysid of a link
        OUTPUT: - 1 in case the specifeid link is named
                - 0 otherwise
        FUNCTION: chekcs if the specifeid link is named. It call
                  macro IS_UNNAMED for this check.
        -----------------------------------------------------------------*/
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ Code..."> 
        /* in file tms_api.cpp
        int IsNamedLink(int sysid)
        {
            if (IS_UNNAMED(sysid)) {
                    return 0;
            } else {
                    return 1;
            }
            return 0;
        }
        */
        // </editor-fold> 
        /*
        public static int IS_UNNAMED(long _ID_){
        TMSAPIClass TA = new TMSAPIClass();
        return TA.IS_UNNAMED(_ID_);
        //return ((_ID_) & (UNNAMED_BIT)) ;
    } 
    
    protected static int IsNamedLink(long sysid){
        
        if (IS_UNNAMED(sysid)!=0) {
            return 0;
        } else {
            return 1;
        }
    }
        */
        
        Node n = db.getNeo4jNodeByNeo4jId(linkSystemId);
        
        if(n!=null && n.hasLabel(Configs.Labels.Type_Attribute) && n.hasLabel(Configs.Labels.Token)){
            return true;
        }
        
        String lname = (String) n.getProperty(db.Neo4j_Key_For_Logicalname);
        if(lname.matches(Configs.regExForUnNamed)){
            return true;
        }
        return false;
    }
}