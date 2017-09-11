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
package neo4j_sisapi.tmsapi;

import neo4j_sisapi.*;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neo4j.graphdb.GraphDatabaseService;
/**
 *
 * @author Elias Tzortzakakis <tzortzak@ics.forth.gr>
 */
public class TMSAPIClass {
    public final static int TMS_APISucc = 0;
    public final static int TMS_APIFail =-1;
    public final static int MAX_TMS_API_ERROR_MESSAGE_LEN = 20000;

    // defines used by MoveToHierarchy operation
    public final static int MOVE_NODE_ONLY = 0;
    public final static int MOVE_NODE_AND_SUBTREE = 1;
    public final static int CONNECT_NODE_AND_SUBTREE = 2;

    //public final static int MAX_COM_LEN = 20000;
    //public static final int MAX_STRING = 500;

    //<editor-fold defaultstate="collapsed" desc="C++ code...">  
    // define options for the creation of different kinds of thesauri nodes
    /*
    #define CREATE_ALTERNATIVE_TERM 0
    #define CREATE_USED_FOR_TERM    1
    #define CREATE_EDITOR           2
    #define CREATE_SOURCE           3
    #define CREATE_AMERICAN_WORD    4
    #define CREATE_DANISH_WORD      5
    #define CREATE_CATALAN_WORD     6
    #define CREATE_SPANISH_WORD     7
    #define CREATE_PORTUGUESE_WORD  8
    #define CREATE_GERMAN_WORD      9
    #define CREATE_GREEK_WORD       10
    #define CREATE_ITALIAN_WORD     11
    #define CREATE_FRENCH_WORD      12
    #define CREATE_ENGLISH_WORD     13
    */    
    
    //</editor-fold> 
    // define options for the creation of different kinds of thesauri nodes
    protected static final int CREATE_ALTERNATIVE_TERM=0;
    protected static final int CREATE_USED_FOR_TERM= 1;
    protected static final int CREATE_EDITOR= 2;
    protected static final int CREATE_SOURCE = 3;
    
    protected static final int DELETE_SOURCE=0;
    protected static final int DELETE_EDITOR=1;

    
    //<editor-fold defaultstate="collapsed" desc="C++ code...">  
    //From File u_creatdescrattr.cpp
    /*
    #define CREATION_FOR_NEW_DESCRIPTOR			0
    #define CREATION_FOR_VERSIONED_DESCRIPTOR		1
    */
    //</editor-fold>
    protected static final int CREATION_FOR_NEW_DESCRIPTOR = 0;
    protected static final int CREATION_FOR_VERSIONED_DESCRIPTOR =1;

    //<editor-fold defaultstate="collapsed" desc="C++ code...">  
    //From File u_deldescrattr.cpp
    /*
    #define DELETION_FOR_NEW_DESCRIPTOR			0
    #define DELETION_FOR_VERSIONED_DESCRIPTOR		1
     */
    //</editor-fold>
    protected static final int DELETION_FOR_NEW_DESCRIPTOR = 0;
    protected static final int DELETION_FOR_VERSIONED_DESCRIPTOR = 1;
        
    static final int ADD_CLASSIFY  = 0;
    static final int  DEL_CLASSIFY =1;
    
    enum DIRECTION {FROM_NODE, TO_NODE};
    public TMSAPIClass(){
        
        
    }
    
    
    // <editor-fold defaultstate="collapsed" desc="C++ Code">
    /*
    // sis_api and SIS_Connection member classes
   // to be used for C++ connection-interface with SIS-API
    
    sis_api *QC;
    SIS_Connection *CC;

    char userOperation[LOGINAM_SIZE];
    char userOperationLow[LOGINAM_SIZE];
    char errorMessage[MAX_TMS_API_ERROR_MESSAGE_LEN];
    // The names of the existing thesaurus in currently used data base
    l_name ThesaurusNames[100];
    // Categories of attributes allowed to be edited by TMS API
    l_name FacetCategories[MAX_FACET_CATEGORIES][2];
    l_name HierarchyCategories[MAX_HIERARCHY_CATEGORIES][2];
    l_name NewDescriptorCategories[MAX_NEW_DESCRIPTOR_CATEGORIES][2];
    l_name VersionedDescriptorCategories[MAX_VERSIONED_DESCRIPTOR_CATEGORIES][2];
    l_name CommentCategories[MAX_COMMENT_CATEGORIES][2];
    */
    // </editor-fold> 

    
    QClass QC;
    StringObject userOperation = new StringObject("");
    StringObject userOperationLow = new StringObject("");
    StringObject errorMessage = new StringObject("");
    // The names of the existing thesaurus in currently used data base
    Vector<String> ThesaurusNames = new Vector<String>();
    // Categories of attributes allowed to be edited by TMS API
    Vector<String[]> FacetCategories = new Vector();
    Vector<String[]> HierarchyCategories= new Vector();
    Vector<String[]> NewDescriptorCategories = new Vector();
    Vector<String[]> VersionedDescriptorCategories = new Vector();
    Vector<String[]> CommentCategories = new Vector();
    
    //<editor-fold defaultstate="collapsed" desc="C++ code...">     
 //FROM FILE u_msgs.h
 /**********************************************************************
 * 
 *                       Semantic Index System
 *
 *   COPYRIGHT (c) 1992 by Institute of Computer Science,
 *                         Foundation of Research and Technology - Hellas
 *                         POBox 1385, Heraklio Crete, GR-711 10 GREECE
 *
 *
 *                       ALL RIGHTS RESERVED
 *
 *   This software is furnished under license and may be used only in
 *   accordance with the terms of that license and with the inclusion
 *   of the above copright notice.  This software may not be provided
 *   or otherwise made available to, or used by, any other person. No
 *   title to or ownership of the software is hereby transferred.
 *
 *
 *   Module  :  u_msgs.h
 *   Version :  203.1
 *
 *   Purpose :
 *
 *   Author  : Karamaounas Polykarpos , Fundulaki Rena
 *   Creation Date :  1/1/98           Date of last update : 1/13/99
 *
 *   Remarks :
 *
 *
 *********************************************************************/
    /*
    #define	INDIVIDUAL				"Individual"
    #define	_DEPENDENT				"dependent"
    #define	_NECESSARY				"necessary"
    #define	_GARBAGE_COLLECTED	"garbage_collected"
    #define _INTERCONNECTED 		"interconnected"
    #define _DIRECTED_ACYCLIC_GRAPH "DAG"
    
    #define EMPTY_STRING	"Empty string for "
    #define GAVE_NAME_TO_STR "gave_name_to"
    #define IN_GETTHESFCLASS		"Unable to get thesaurus from schema class\n"
    #define IN_GETHIERARCHY			"Unable to get hierarchies for "
    #define IN_OBJECTINCLASS		"Failure in ObjectinClass for "
    #define IN_TERMINOBS	        	"Failure in TermisObsolete for "
    #define IN_ISTOPTERM				"Failure in isTopTerm for "
    #define IN_THESVERSION			"Unable to get thesaurus version\n"
    #define IN_TERMPRFHIER			"Unable to get term prefix from hierarchy "
    #define IN_THESNAMEFHIER		"Unable to get thesaurus name from hierarchy "
    #define IN_THESNAME				"Unable to get thesaurus name\n"
    #define IN_CLASSPREFIXFTHES	"Unable to get hierarchy/facet prefix from thesaurus "
    #define IN_TERMPREFIXFTHES		"Unable to get term prefix from thesaurus "
    #define OBJECT_CANNOT_BE_DELETED "has instances and cannot be deleted\n"
    #define OBJECT_DOES_NOT_EXIST	 "does not exist in database\n"
    #define OBJECT_EXISTS		 "already exists in database\n"
    #define CYCLE_DETECTION      "You cannot add a link under category <%s> to node <%s> because a directed cycle of this kind of links will be created. This is not allowed because the above category is of type DAG (Directed Acyclic Graph)"
    #define IT_IS_NECESSARY_TO_HAVE_LINKS_UNDER_CATEGORY "You tried to delete the last attribute under attribute class %s which is of type Necessary. It is necessary for the %s to have at least one attribute under attribute class %s."
    #define TERM_STR "HierarchyTerm"
        */
        //</editor-fold>  
    public static final String INDIVIDUAL = "Individual";
    protected static final String _DEPENDENT = "dependent";
    public static final String _NECESSARY = "necessary";
    public static final String _GARBAGE_COLLECTED = "garbage_collected";
    public static final String _INTERCONNECTED = "interconnected";
    public static final String _DIRECTED_ACYCLIC_GRAPH = "DAG";
    public static final String _BACKWARDS_SORTED = "backwards_sorted";
    protected static final String EMPTY_STRING = "Δεν δόθηκε όνομα για ";//"Empty string for ";
    protected static final String GAVE_NAME_TO_STR = "gave_name_to";
    protected static final String IN_GETTHESFCLASS = "Unable to get thesaurus from schema class\n";
    protected static final String IN_GETHIERARCHY = "Unable to get hierarchies for ";
    protected static final String IN_OBJECTINCLASS = "Failure in ObjectinClass for ";
    protected static final String IN_TERMINOBS = "Failure in TermisObsolete for ";
    protected static final String IN_ISTOPTERM = "Failure in isTopTerm for ";
    protected static final String IN_THESVERSION = "Unable to get thesaurus version\n";
    protected static final String IN_TERMPRFHIER = "Unable to get term prefix from hierarchy ";
    protected static final String IN_THESNAMEFHIER = "Unable to get thesaurus name from hierarchy ";
    protected static final String IN_THESNAME = "Unable to get thesaurus name\n";
    protected static final String IN_CLASSPREFIXFTHES = "Unable to get hierarchy/facet prefix from thesaurus ";
    protected static final String IN_TERMPREFIXFTHES = "Unable to get term prefix from thesaurus ";
    protected static final String OBJECT_CANNOT_BE_DELETED = "has instances and cannot be deleted\n";
    protected static final String OBJECT_DOES_NOT_EXIST = "δεν υπάρχει στην βάση\n";//"does not exist in database\n";
    protected static final String OBJECT_EXISTS = " υπάρχει ήδη στην βάση\n";//"already exists in database\n";
    protected static final String CYCLE_DETECTION = "You cannot add a link under category <%s> to node <%s> because a directed cycle of this kind of links will be created. This is not allowed because the above category is of type DAG (Directed Acyclic Graph)";
    protected static final String IT_IS_NECESSARY_TO_HAVE_LINKS_UNDER_CATEGORY = "You tried to delete the last attribute under attribute class %s which is of type Necessary. It is necessary for the %s to have at least one attribute under attribute class %s.";
    protected static final String TERM_STR = "HierarchyTerm";
    protected static final String HAS_PREFIX ="has_prefix";
    protected static final String INVALID_TRANSLATION_KEYWORD = "Translation Word %s is not defined in the database";
    // Open/Close the SIS TMS-API
    public int ALMOST_DONE_create_TMS_API_Session(QClass Q, String selectedThesaurus){
        
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //SIS_CREATE_DLL_DECL(int) init_TMS_API()
        SIS_CREATE_DLL_DECL(int) create_TMS_API_Session(int *TMS_API_sessionID, int SIS_API_sessionID)
        {
                sis_api *QC;
           SIS_Connection *CC;
                SH.GetSession(SIS_API_sessionID, &QC, &CC);
           if(QC == NULL || CC == NULL) return -1;
                tms_api *tmsApi = new tms_api(QC, CC);
           // insert a new TMS_API entry to global table
           // and return the index (TMS_API_sessionID)
           // inform session handler
           *TMS_API_sessionID = TMS_SH.AddSession(tmsApi, SIS_API_sessionID);
                return 0;
        }
        */
        // </editor-fold> 
        
        // <editor-fold defaultstate="collapsed" desc="tms_api(sis_api *qc, SIS_Connection *cc) C++ Code">
        /*
        tms_api::tms_api()
            Constructor of tms_api class.
            FUNCTION: - gets all the names of the existing thesaurus in
                currently used data base
              - intializes userOperation member with the first found thesaurus name
              - intializes errorMessage member with NULL
              - fills the arrays with the allowed categories to be used by
                CreateAttribute() functions for Facets, for Hierarchies,
                for New Descriptors, for Versioned Descriptors
        tms_api::tms_api(sis_api *qc, SIS_Connection *cc)
        {
                // inform sis_api and SIS_Connection member classes
           // to be used for C++ connection-interface with SIS-API
           QC = qc;
           CC = cc;

                // fill the array with the names of the existing thesaurus
           // in currently used data base
           CC->begin_query();
           QC->reset_name_scope();
                if (QC->set_current_node("Thesaurus") < 0) {
                strcpy(errorMessage, "Failed to QC->set_current_node(Thesaurus)\n");
              return;
           }
           int thesaurus_set;
                if ((thesaurus_set = QC->get_instances(0)) < 0) {
               strcpy(errorMessage, "Failed to get_instances(Thesaurus)\n");
               return;
           }
           QC->reset_set(thesaurus_set);
           l_name thesaurus_name;
           int i=0;
           char *thes_name_without_prefix;
                while (QC->return_nodes(thesaurus_set, thesaurus_name) != -1) {
                thes_name_without_prefix = strchr(thesaurus_name, '`');
                strcpy(ThesaurusNames[i], &(thes_name_without_prefix[1]));
              i++;
                }
           // mark with eof the last unused element of the array
           *(ThesaurusNames[i]) = 0;
           QC->free_set(thesaurus_set);
           // intialize userOperation with the first found thesaurus name
           SetThesaurusName(ThesaurusNames[0]);
                //strcpy(userOperation, ThesaurusNames[0]);
           // intialize errorMessage
           *errorMessage = 0;
           // end query session
           CC->end_query();
        }        
        */
        // </editor-fold> 
        
        //throw new UnsupportedOperationException();
        
        QC = Q;
        //QC.TEST_create_SIS_CS_Session(useGraphDb);
        
        //QC.TEST_begin_query();
        QC.reset_name_scope();
        if(QC.set_current_node(new StringObject("Thesaurus"))<0){
            errorMessage.setValue("Failed to QC->set_current_node(Thesaurus)\n");
            QC.reset_name_scope();
            return TMS_APIFail;
        }
        int thesaurus_set;
        if ((thesaurus_set = QC.get_instances(0)) < 0) {
            errorMessage.setValue("Failed to get_instances(Thesaurus)\n");
            QC.reset_name_scope();
            return TMS_APIFail;
        }
        QC.reset_set(thesaurus_set);
        
        Vector<Return_Nodes_Row> retVals = new Vector<Return_Nodes_Row>();
        
        if(QC.bulk_return_nodes(thesaurus_set, retVals)!=QClass.APIFail){
            for(Return_Nodes_Row row: retVals){
                String[] res = row.get_v1_cls_logicalname().split("`");
                if (res.length == 1) {
                    continue;
                }
                if (res[1] != null) {
                    ThesaurusNames.add(res[1]);
                }
            }
        }
        // mark with eof the last unused element of the array
        //*(ThesaurusNames[i]) = 0;
        QC.free_set(thesaurus_set);
        // intialize userOperation with the first found thesaurus name
        if(selectedThesaurus!=null && ThesaurusNames.contains(selectedThesaurus)){
            SetThesaurusName(selectedThesaurus);
        }
        else{
            SetThesaurusName(ThesaurusNames.get(0));
        }
        //strcpy(userOperation, ThesaurusNames[0]);

        // intialize errorMessage
        errorMessage.setValue("");
        // end query session
        //QC.TEST_end_query();
        //QC.TEST_release_SIS_Session();
        
        QC.reset_name_scope();
        return TMS_APISucc;
    }
    
    public int ALMOST_DONE_release_TMS_API_Session(){
        
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        */
        // </editor-fold> 
        
        //throw new UnsupportedOperationException();
        return TMS_APISucc;
    }
    
    public int  NOT_IMPLEMENTED_AbandonFacet(StringObject facet){
        throw new UnsupportedOperationException();
    }
    
    public int  NOT_IMPLEMENTED_AbandonHierarchy(StringObject hierarchy){
        throw new UnsupportedOperationException();
    }

    public int  CHECK_ClassifyHierarchyInFacet(StringObject hierarchyName, StringObject facetName){
        // <editor-fold defaultstate="collapsed" desc="JNI Code">
        /*
        JNIEXPORT jint JNICALL Java_sisapi_TMSAPIClass_ClassifyHierarchyInFacet
            (JNIEnv *env, jobject, jint TMSsessionID, jobject jhier, jobject jclass)
          {
                  l_name chier;
                  l_name cclass;
                  LNameJ2C(env, jhier, chier);
                  LNameJ2C(env, jclass, cclass);
                  return ClassifyHierarchyInFacet(TMSsessionID,(char*)chier, (char*) cclass);
          }

        */
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*        
//	tms_api::ClassifyHierarchyInFacet()
//---------------------------------------------------------------------
//	INPUT: - hierarchyName : the name of a hierarchy to be classified
//			   facetName : the name of a facet under wich the hierarchy will be classified
//	OUTPUT: - TMS_APISucc in case the given hierarchy is classified succesfully
//           - TMS_APIFail otherwise
//	FUNCTION: classifies the given hierarchy
//----------------------------------------------------------------
int tms_api::ClassifyHierarchyInFacet(char *hierarchyName, char *facetName)
{
	int ret = Add_Del_ClassifyHierarchyInFacet(hierarchyName, facetName, ADD_CLASSIFY);
   char tmpErrorMessage[512];
	if (ret==APIFail) {
   	abort_classify(hierarchyName, facetName, tmpErrorMessage);
      strcat(errorMessage, "\n");
      strcat(errorMessage, tmpErrorMessage);
      return TMS_APIFail;
	}

	commit_classify(hierarchyName, facetName, errorMessage);
   return TMS_APISucc;
}
        */
        // </editor-fold> 
        int ret = Add_Del_ClassifyHierarchyInFacet(hierarchyName.getValue(), facetName.getValue(), ADD_CLASSIFY);
        if (ret==TMS_APIFail) {
            StringObject tmpErrorMessage = new StringObject();
            abort_classify(hierarchyName, facetName, tmpErrorMessage);
            errorMessage.setValue(errorMessage.getValue()+"\n"+tmpErrorMessage.getValue());
            //strcat(errorMessage, "\n");
            //strcat(errorMessage, tmpErrorMessage);
            return TMS_APIFail;
	}

	commit_classify(hierarchyName, facetName, errorMessage);
        return TMS_APISucc;
    }
    
    void FillArraysOfAllowedCategories(){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        void tms_api::FillArraysOfAllowedCategories()
        {
                // fill the arrays with the allowed categories to be used by CreateAttribute() functions
           // for Facets (#1)
           strcpy(FacetCategories[0][0], "Facet");
           strcpy(FacetCategories[0][1], "letter_code");
           // for Hierarchies (#1)
           strcpy(HierarchyCategories[0][0], "Facet");
           strcpy(HierarchyCategories[0][1], "letter_code");
           // for New Descriptors (#12)
           sprintf(NewDescriptorCategories[0][0], "%sHierarchyTerm", userOperation);
           sprintf(NewDescriptorCategories[0][1], "%s_ALT", userOperation);
           sprintf(NewDescriptorCategories[1][0], "%sHierarchyTerm", userOperation);
           sprintf(NewDescriptorCategories[1][1], "%s_display", userOperationLow);
           sprintf(NewDescriptorCategories[2][0], "%sHierarchyTerm", userOperation);
           sprintf(NewDescriptorCategories[2][1], "%s_editor", userOperationLow);
           sprintf(NewDescriptorCategories[3][0], "%sHierarchyTerm", userOperation);
           sprintf(NewDescriptorCategories[3][1], "%s_found_in", userOperationLow);
           sprintf(NewDescriptorCategories[4][0], "%sHierarchyTerm", userOperation);
           sprintf(NewDescriptorCategories[4][1], "%s_modified", userOperationLow);
           sprintf(NewDescriptorCategories[5][0], "%sHierarchyTerm", userOperation);
           sprintf(NewDescriptorCategories[5][1], "%s_found_in", userOperationLow);
           sprintf(NewDescriptorCategories[6][0], "%sHierarchyTerm", userOperation);
           sprintf(NewDescriptorCategories[6][1], "%s_RT", userOperation);
           sprintf(NewDescriptorCategories[7][0], "%sHierarchyTerm", userOperation);
           sprintf(NewDescriptorCategories[7][1], "%s_UF", userOperation);
        // removed because this category is handled by comments handling functions
        //   sprintf(NewDescriptorCategories[8][0], "%sThesaurusConcept", userOperation);
        //   sprintf(NewDescriptorCategories[8][1], "%s_scope_note", userOperationLow);
           sprintf(NewDescriptorCategories[8][0], "%sThesaurusConcept", userOperation);
           sprintf(NewDescriptorCategories[8][1], "%s_translation, to_EN", userOperation);
           sprintf(NewDescriptorCategories[9][0], "%sThesaurusConcept", userOperation);
           sprintf(NewDescriptorCategories[9][1], "%s_translation, to_GR", userOperation);
           sprintf(NewDescriptorCategories[10][0], "%sThesaurusConcept", userOperation);
           sprintf(NewDescriptorCategories[10][1], "%s_translation, to_IT", userOperation);
           sprintf(NewDescriptorCategories[11][0], "%sHierarchyTerm", userOperation);
           sprintf(NewDescriptorCategories[11][1], "%s_created", userOperationLow);
           // for Versioned Descriptors (#11 : the same with previous, without the last one)
           sprintf(VersionedDescriptorCategories[0][0], "%sHierarchyTerm", userOperation);
           sprintf(VersionedDescriptorCategories[0][1], "%s_ALT", userOperation);
           sprintf(VersionedDescriptorCategories[1][0], "%sHierarchyTerm", userOperation);
           sprintf(VersionedDescriptorCategories[1][1], "%s_display", userOperationLow);
           sprintf(VersionedDescriptorCategories[2][0], "%sHierarchyTerm", userOperation);
           sprintf(VersionedDescriptorCategories[2][1], "%s_editor", userOperationLow);
           sprintf(VersionedDescriptorCategories[3][0], "%sHierarchyTerm", userOperation);
           sprintf(VersionedDescriptorCategories[3][1], "%s_found_in", userOperationLow);
           sprintf(VersionedDescriptorCategories[4][0], "%sHierarchyTerm", userOperation);
           sprintf(VersionedDescriptorCategories[4][1], "%s_modified", userOperationLow);
           sprintf(VersionedDescriptorCategories[5][0], "%sHierarchyTerm", userOperation);
           sprintf(VersionedDescriptorCategories[5][1], "%s_found_in", userOperationLow);
           sprintf(VersionedDescriptorCategories[6][0], "%sHierarchyTerm", userOperation);
           sprintf(VersionedDescriptorCategories[6][1], "%s_RT", userOperation);
           sprintf(VersionedDescriptorCategories[7][0], "%sHierarchyTerm", userOperation);
           sprintf(VersionedDescriptorCategories[7][1], "%s_UF", userOperation);
        // removed because this category is handled by comments handling functions
        //   sprintf(VersionedDescriptorCategories[8][0], "%sThesaurusConcept", userOperation);
        //   sprintf(VersionedDescriptorCategories[8][1], "%s_scope_note", userOperationLow);
           sprintf(VersionedDescriptorCategories[8][0], "%sThesaurusConcept", userOperation);
           sprintf(VersionedDescriptorCategories[8][1], "%s_translation, to_EN", userOperation);
           sprintf(VersionedDescriptorCategories[9][0], "%sThesaurusConcept", userOperation);
           sprintf(VersionedDescriptorCategories[9][1], "%s_translation, to_GR", userOperation);
           sprintf(VersionedDescriptorCategories[10][0], "%sThesaurusConcept", userOperation);
           sprintf(VersionedDescriptorCategories[10][1], "%s_translation, to_IT", userOperation);

           // fill the CommentCategories array with the allowed categories
           // to be used by comments handling functions
           // They are: INTERSECTION({gai(MERIMEEThesaurusNotionType->MERIMEE_description),
           // gai(Individual->text_type)}

           // begin a query session
           CC->begin_query();
           QC->reset_name_scope();
           l_name fromClass1, category1;
           sprintf(fromClass1, "%sThesaurusNotionType", userOperation); // MERIMEEThesaurusNotionType
                if (QC->set_current_node(fromClass1) < 0) {
                sprintf(errorMessage, "Failed to QC->set_current_node(%s)\n", fromClass1);
              return;
           }
           sprintf(category1, "%s_description", userOperation); // MERIMEE_description
                if (QC->set_current_node(category1) < 0) {
                sprintf(errorMessage, "Failed to QC->set_current_node(%s)\n", category1);
              return;
           }
           int instSet1 = QC->get_all_instances(0);

           QC->reset_name_scope();
                if (QC->set_current_node("Individual") < 0) {
                sprintf(errorMessage, "Failed to QC->set_current_node(%s)\n", "Individual");
              return;
           }
                if (QC->set_current_node("text_type") < 0) {
                sprintf(errorMessage, "Failed to QC->set_current_node(%s)\n", "text_type");
              return;
           }
           int instSet2 = QC->get_all_instances(0);
           QC->set_intersect(instSet1, instSet2);
           QC->free_set(instSet2);
           QC->reset_set(instSet1);
           l_name cls, label;
           cm_value cmv;
           int counter = 0;
           while (QC->return_link(instSet1, cls, label, &cmv) != -1) {
                strcpy(CommentCategories[counter][0], cls);
              strcpy(CommentCategories[counter][1], label);
                // free allocated space
                        switch (cmv.type) {
                                case TYPE_STRING : case TYPE_NODE   :
                                        free(cmv.value.s) ;
                        }
              counter++;
           }

           // get the intersection of the two above sets

           // free used sets
           QC->free_set(instSet1);

                // end query session
           CC->end_query();
           // fill the rest unfilled cells of CommentCategories array with '\0'
           for (int i=counter; i<MAX_COMMENT_CATEGORIES; i++) {
                *(CommentCategories[i][0]) = 0;
              *(CommentCategories[i][1]) = 0;
           }
        }
        */
        // </editor-fold> 
        
        FacetCategories.clear();
        HierarchyCategories.clear();
        NewDescriptorCategories.clear();
        VersionedDescriptorCategories.clear();
        
        // fill the arrays with the allowed categories to be used by CreateAttribute() functions
        // for Facets (#1)
        String[] category = {"Facet","letter_code"};
        FacetCategories.add(category);
        
        // for Hierarchies (#1)
        category[0] = "Facet"; category[1]="letter_code";
        HierarchyCategories.add(category);
        
        // for New Descriptors (#12)
        // for Versioned Descriptors (#11 : the same with previous, without the last one)
        //0
        category[0] = String.format("%sHierarchyTerm", userOperation.getValue());
        category[1] = String.format("%s_ALT", userOperation.getValue());
        NewDescriptorCategories.add(category);
        VersionedDescriptorCategories.add(category);
        //1
        category[0] = String.format("%sHierarchyTerm", userOperation.getValue());
        category[1] = String.format("%s_display", userOperationLow.getValue());
        NewDescriptorCategories.add(category);
        VersionedDescriptorCategories.add(category);
        //2
        category[0] = String.format("%sHierarchyTerm", userOperation.getValue());
        category[1] = String.format("%s_editor", userOperationLow.getValue());
        NewDescriptorCategories.add(category);
        VersionedDescriptorCategories.add(category);
        //3
        category[0] = String.format("%sHierarchyTerm", userOperation.getValue());
        category[1] = String.format("%s_found_in", userOperationLow.getValue());
        NewDescriptorCategories.add(category);
        VersionedDescriptorCategories.add(category);
        //4
        category[0] = String.format("%sHierarchyTerm", userOperation.getValue());
        category[1] = String.format("%s_modified", userOperationLow.getValue());
        NewDescriptorCategories.add(category);
        VersionedDescriptorCategories.add(category);
        //5 - SAME AS 3?
        category[0] = String.format("%sHierarchyTerm", userOperation.getValue());
        //category[1] = String.format("%s_found_in", userOperationLow);//MUST BE BUG IN TMS API AND Documentation
        category[1] = String.format("%s_not_found_in", userOperationLow.getValue());
        NewDescriptorCategories.add(category);
        VersionedDescriptorCategories.add(category);
        //6
        //also RTs may become dynamic and need handling as Decriptor->BT
        category[0] = String.format("%sHierarchyTerm", userOperation.getValue());
        category[1] = String.format("%s_RT", userOperation.getValue());
        NewDescriptorCategories.add(category);
        VersionedDescriptorCategories.add(category);
        //7
        category[0] = String.format("%sHierarchyTerm", userOperation.getValue());
        category[1] = String.format("%s_UF", userOperation.getValue());
        NewDescriptorCategories.add(category);
        VersionedDescriptorCategories.add(category);
        
        /*
        SKIPPED THE FOLLOWING BECAUSE THESE ARE DYNAMIC
        ALSO: //Found ThesaurusConcept but this category is defined as AAATerm->AAA_trasnlation, to_EN in current model
       
        // removed because this category is handled by comments handling functions
       //   sprintf(NewDescriptorCategories[8][0], "%sThesaurusConcept", userOperation);
       //   sprintf(NewDescriptorCategories[8][1], "%s_scope_note", userOperationLow);
          sprintf(NewDescriptorCategories[8][0], "%sThesaurusConcept", userOperation);
          sprintf(NewDescriptorCategories[8][1], "%s_translation, to_EN", userOperation);
          sprintf(NewDescriptorCategories[9][0], "%sThesaurusConcept", userOperation);
          sprintf(NewDescriptorCategories[9][1], "%s_translation, to_GR", userOperation);
          sprintf(NewDescriptorCategories[10][0], "%sThesaurusConcept", userOperation);
          sprintf(NewDescriptorCategories[10][1], "%s_translation, to_IT", userOperation);
       
        */
        
        //11 (versioned descriptors the same without the last one
        category[0] = String.format("%sHierarchyTerm", userOperation.getValue());
        category[1] = String.format("%s_created", userOperationLow.getValue());
        NewDescriptorCategories.add(category);
        
        
        // fill the CommentCategories array with the allowed categories
        // to be used by comments handling functions
        // They are: INTERSECTION({gai(MERIMEEThesaurusNotionType->MERIMEE_description),
        // gai(Individual->text_type)}

        // begin a query session
        //QC.TEST_begin_query();
        QC.reset_name_scope();
        StringObject fromClass1 = new StringObject(userOperation.getValue().concat("ThesaurusNotionType"));// MERIMEEThesaurusNotionType
        StringObject category1 = new StringObject(userOperation.getValue().concat("_description"));// MERIMEE_description

        if (QC.set_current_node(fromClass1) < 0) {
            //sprintf(errorMessage, "Failed to QC->set_current_node(%s)\n", fromClass1);
            errorMessage.setValue("Failed to QC->set_current_node("+fromClass1.getValue()+")\n");
            QC.reset_name_scope();
            return;
        }

        if (QC.set_current_node(category1) < 0) {
            //sprintf(errorMessage, "Failed to QC->set_current_node(%s)\n", category1);
            errorMessage.setValue("Failed to QC->set_current_node("+category1.getValue()+")\n");
            QC.reset_name_scope();
            return;
        }
        int instSet1 = QC.get_all_instances(0);

        QC.reset_name_scope();
        if (QC.set_current_node(new StringObject(INDIVIDUAL) ) < 0) {            
            //sprintf(errorMessage, "Failed to QC->set_current_node(%s)\n", "Individual");
            errorMessage.setValue("Failed to QC->set_current_node("+INDIVIDUAL+")\n");
            QC.reset_name_scope();
            return;
        }
        if (QC.set_current_node(new StringObject("text_type")) < 0) {
            errorMessage.setValue("Failed to QC->set_current_node("+"text_type"+")\n");
            //sprintf(errorMessage, "Failed to QC->set_current_node(%s)\n", "text_type");
            QC.reset_name_scope();
            return;
        }
        
        
        int instSet2 = QC.get_all_instances(0);
        QC.set_intersect(instSet1, instSet2);
        QC.free_set(instSet2);
        QC.reset_set(instSet1);
        
        //StringObject cls = new StringObject();
        //StringObject label = new StringObject();
        //CMValue cmv = new CMValue();
        //int counter = 0;
        Vector<Return_Link_Row> retVals = new Vector<Return_Link_Row>();
        if(QC.bulk_return_link(instSet1, retVals)!=QClass.APIFail){
            for(Return_Link_Row row: retVals){
                String[] commentCateg = {row.get_v1_cls(), row.get_v2_label()};
                CommentCategories.add(commentCateg);
                //counter++;
            }
        }        

        // get the intersection of the two above sets

        // free used sets
        QC.free_set(instSet1);

        QC.reset_name_scope();
        // end query session
        //QC.TEST_end_query();
           
        
    }
    
    int Add_Del_ClassifyHierarchyInFacet(String hierarchyName, String facetName, int option){
        
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
//tms_api::Add_Del_ClassifyHierarchyInFacet()
//---------------------------------------------------------------------
//	INPUT: - hierarchyName : the name of a hierarchy to be (de)classified
//			   className : the name of a class under wich the hierarchy will be (de)classified
//			   option : ADD_CLASSIFY or DEL_CLASSIFY
//	OUTPUT: - TMS_APISucc in case the given hierarchy is (de)classified succesfully
//           - TMS_APIFail in case:
//					· hierarchyName is not the name of any existing hierarchy of
//                 currently used thesaurus.
//					· facetName is not the name of any existing facet of currently
//                 used thesaurus.
//	FUNCTION: (de)classifies the given hierarchy depending on the value of input option
//----------------------------------------------------------------
int tms_api::Add_Del_ClassifyHierarchyInFacet(char *hierarchyName, char *facetName, int option)
{
	// check if given hierarchy exists in data base
   QC->reset_name_scope();
   int hierarchySysid;
   if ((hierarchySysid = QC->set_current_node(hierarchyName)) < 0) {
      sprintf(errorMessage, translate("%s does not exist in data base"), hierarchyName);
      return TMS_APIFail;
   }

   // check if given hierarchy is a hierarchy of current thesaurus
   // if it belongs to MERIMEEThesaurusClass
   // looking for MERIMEEThesaurusClass
   l_name givenClass;
   strcpy(givenClass, userOperation); // MERIMEE
   strcat(givenClass, "ThesaurusClass"); // MERIMEEThesaurusClass
	int ret = ObjectinClass(hierarchyName, givenClass, errorMessage);
	if (ret == TMS_APIFail){
		sprintf(errorMessage,translate("%s is not a %s hierarchy"), hierarchyName, userOperation);
      return TMS_APIFail;
	}

	// check if given facet exists in data base
   QC->reset_name_scope();
   if (QC->set_current_node(facetName) < 0) {
      sprintf(errorMessage, translate("%s does not exist in data base"), facetName);
      return TMS_APIFail;
   }

   // check if given facet is a facet of current thesaurus
   // if it belongs to MERIMEEFacet
	// looking for MERIMEEFacet
   int card;
   l_name thesaurus_facet;
   strcpy(givenClass, userOperation); // MERIMEE
   strcat(givenClass, "ThesaurusClassType"); // MERIMEEThesaurusClassType
   ret = GetThesaurusObject(thesaurus_facet, NULL, "Facet", NULL, givenClass, &card);
   if (ret==TMS_APIFail) return TMS_APIFail;
	ret = ObjectinClass(facetName, thesaurus_facet, errorMessage);
	if (ret == TMS_APIFail){
		sprintf(errorMessage,translate("%s is not a %s facet"), facetName, userOperation);
      return TMS_APIFail;
	}

   // (de)classify hierarchyName under facetName
	IDENTIFIER Ihierarchy;
	strcpy(Ihierarchy.loginam, hierarchyName);
	Ihierarchy.tag = ID_TYPE_LOGINAM;
	IDENTIFIER Ifacet;
	strcpy(Ifacet.loginam, facetName);
	Ifacet.tag = ID_TYPE_LOGINAM;

	switch (option) {
		case ADD_CLASSIFY : ret = QC->Add_IsA(&Ihierarchy, &Ifacet);
                          break;
      case DEL_CLASSIFY : ret = QC->Delete_IsA(&Ihierarchy, &Ifacet);
                          break;
	}
   return ret;
}        
        */
        // </editor-fold> 
        
        // check if given hierarchy exists in data base
        QC.reset_name_scope();
        long hierarchySysidL = QC.set_current_node(new StringObject(hierarchyName));
        if ( hierarchySysidL < 0) {
            
            //sprintf(errorMessage, translate("%s does not exist in data base"), hierarchyName);
            errorMessage.setValue(hierarchyName +" does not exist in data base.");
            return TMS_APIFail;
        }

        // check if given hierarchy is a hierarchy of current thesaurus
        // if it belongs to MERIMEEThesaurusClass
        // looking for MERIMEEThesaurusClass
        StringObject givenClass = new StringObject(userOperation.getValue()+ "ThesaurusClass");
        //strcpy(givenClass, userOperation); // MERIMEE
        //strcat(givenClass, "ThesaurusClass"); // MERIMEEThesaurusClass
	int ret = ObjectinClass(new StringObject(hierarchyName), givenClass, errorMessage);
	if (ret == TMS_APIFail){
            //sprintf(errorMessage,translate("%s is not a %s hierarchy"), hierarchyName, userOperation);
            errorMessage.setValue(hierarchyName +" is not a " + userOperation.getValue() +" hierarchy");
            return TMS_APIFail;
	}

        // check if given facet exists in data base
        QC.reset_name_scope();
        long facetIdL = QC.set_current_node(new StringObject(facetName));
        if ( facetIdL < 0) {
            //sprintf(errorMessage, translate("%s does not exist in data base"), facetName);
            errorMessage.setValue(facetName +" does not exist in data base.");
            return TMS_APIFail;
        }
        
        // check if given facet is a facet of current thesaurus
        // if it belongs to MERIMEEFacet
        // looking for MERIMEEFacet
        IntegerObject card = new IntegerObject(0);
        StringObject thesaurus_facet = new StringObject();
        givenClass.setValue(userOperation.getValue()+"ThesaurusClassType");
        //strcpy(givenClass, userOperation); // MERIMEE
        //strcat(givenClass, "ThesaurusClassType"); // MERIMEEThesaurusClassType
        ret = GetThesaurusObject(thesaurus_facet, null, new StringObject("Facet"), null, givenClass, card);
        if (ret==TMS_APIFail) {
            return TMS_APIFail;        
        }
        
        ret = ObjectinClass(new StringObject(facetName), thesaurus_facet, errorMessage);
	if (ret == TMS_APIFail){
            //sprintf(errorMessage,translate("%s is not a %s facet"), facetName, userOperation);
            errorMessage.setValue(facetName +" is not a " + userOperation.getValue() +" facet");
            return TMS_APIFail;
	}

   // (de)classify hierarchyName under facetName
	Identifier Ihierarchy = new Identifier(hierarchySysidL);        
	//strcpy(Ihierarchy.loginam, hierarchyName);
	//Ihierarchy.tag = ID_TYPE_LOGINAM;
        Identifier Ifacet = new Identifier(facetIdL);
	//IDENTIFIER Ifacet;
	//strcpy(Ifacet.loginam, facetName);
	//Ifacet.tag = ID_TYPE_LOGINAM;

	switch (option) {
            case ADD_CLASSIFY :{ 
                ret = QC.CHECK_Add_IsA(Ihierarchy, Ifacet);
                break;
            }
            case DEL_CLASSIFY : {
                ret = QC.CHECK_Delete_IsA(Ihierarchy, Ifacet);
                break;
            }
        }
        return ret;
        
    }

    int CreateNode(StringObject node, int option){
    
        /*-------------------------------------------------------------------
							tms_api::CreateNode()
---------------------------------------------------------------------
	INPUT: - node : the name of a new node
   		 - option : CREATE_ALTERNATIVE_TERM or CREATE_USED_FOR_TERM or
          CREATE_EDITOR or CREATE_SOURCE or CREATE_AMERICAN_WORD or CREATE_DANISH_WORD or
          CREATE_CATALAN_WORD or CREATE_SPANISH_WORD or CREATE_PORTUGUESE_WORD or
          CREATE_GERMAN_WORD or CREATE_GREEK_WORD or CREATE_ITALIAN_WORD or
          CREATE_FRENCH_WORD or CREATE_ENGLISH_WORD
	OUTPUT: - TMS_APISucc in case the given CreateNode is added succesfully
           - TMS_APIFail in case:
					· A node with the given name already exists
					· Input node does not contain the correct prefix for a node
                 (specified by option) of currently used thesaurus.
	FUNCTION: creates a new node. The kind of the new node is specified by option
----------------------------------------------------------------
int tms_api::CreateNode(char *node, int option)
{
	// abort if node name is empty
	if (*node == '\0'){
		sprintf(errorMessage,"%s%s", translate(EMPTY_STRING), translate("Node"));
		return TMS_APIFail;
	}
	//abort if node already exists
	QC->reset_name_scope();
	int nodeSySId = QC->set_current_node(node);
	if (nodeSySId >= 0) {
		strcpy(errorMessage, node);
		strcat(errorMessage, " ");
		strcat(errorMessage, translate(OBJECT_EXISTS));
		return TMS_APIFail;
	}
   // get the name of the Class under which the new node will be instatiated
	int card, ret;
	l_name givenClass, ClassName;
   switch (option) {
   	case CREATE_ALTERNATIVE_TERM :
      	// looking for MERIMEEAlternativeTerm
			strcpy(givenClass, userOperation); // MERIMEE
			strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
			ret = GetThesaurusObject(ClassName, NULL, "AlternativeTerm", NULL, givenClass, &card);
			if (ret==TMS_APIFail) return TMS_APIFail;
			break;
   	case CREATE_USED_FOR_TERM :
      	// looking for MERIMEEUsedForTerm
			strcpy(givenClass, userOperation); // MERIMEE
			strcat(givenClass, "ThesaurusExpressionType"); // MERIMEEThesaurusExpressionType
			ret = GetThesaurusObject(ClassName, NULL, "UsedForTerm", NULL, givenClass, &card);
			if (ret==TMS_APIFail) return TMS_APIFail;
			break;
   	case CREATE_EDITOR : sprintf(ClassName, "%sEditor", userOperation); // MERIMEEEditor
      				  			break;
   	case CREATE_SOURCE : strcpy(ClassName, "Source"); break;
      // Subclasses of class Word
		
   	//case CREATE_AMERICAN_WORD : strcpy(ClassName, "AmericanWord"); break;
   	//case CREATE_DANISH_WORD : strcpy(ClassName, "DanishWord"); break;
   	//case CREATE_CATALAN_WORD : strcpy(ClassName, "CatalanWord"); break;
	//	case CREATE_SPANISH_WORD : strcpy(ClassName, "SpanishWord"); break;
	//	case CREATE_PORTUGUESE_WORD : strcpy(ClassName, "PortugueseWord"); break;
	//	case CREATE_GERMAN_WORD : strcpy(ClassName, "GermanWord"); break;
	//	case CREATE_GREEK_WORD : strcpy(ClassName, "GreekWord"); break;
	//	case CREATE_ITALIAN_WORD : strcpy(ClassName, "ItalianWord"); break;
	//	case CREATE_FRENCH_WORD : strcpy(ClassName, "FrenchWord"); break;
   	//case CREATE_ENGLISH_WORD : strcpy(ClassName, "EnglishWord");	break;
      

   }
   CreateNodeSubRoutine(node, (char*)ClassName);
}
        */
        
// abort if node name is empty
	if (node == null || node.getValue()==null || node.getValue().length()==0){
            //sprintf(errorMessage,"%s%s", translate(EMPTY_STRING), translate("Node"));
            errorMessage.setValue(String.format("%s%s", EMPTY_STRING, "Node"));
            return TMS_APIFail;
	}
	//abort if node already exists
	QC.reset_name_scope();
	long nodeSySIdL = QC.set_current_node(node);
	if (nodeSySIdL !=QClass.APIFail) {
            //strcpy(errorMessage, node);
            //strcat(errorMessage, " ");
            //strcat(errorMessage, translate(OBJECT_EXISTS));
            errorMessage.setValue(node.getValue() + " " + OBJECT_EXISTS);
            return TMS_APIFail;
	}
        
        // get the name of the Class under which the new node will be instatiated
	IntegerObject card = new IntegerObject(0);
        int ret = TMS_APIFail;
        boolean directPrefixesOnly = false;
	//l_name givenClass, ClassName;
        StringObject ClassName = new StringObject();
        switch (option) {
            case CREATE_ALTERNATIVE_TERM :{
                //directPrefixesOnly = true; //not sure if needed never tested
                // looking for MERIMEEAlternativeTerm
                StringObject givenClass = new StringObject(userOperation.getValue()+"ThesaurusNotionType");
                //strcpy(givenClass, userOperation); // MERIMEE
                //strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
                ret = GetThesaurusObject(ClassName, null, new StringObject("AlternativeTerm"), null, givenClass, card);
                if (ret==TMS_APIFail) {
                    return TMS_APIFail;
                }
                break;
            }
   	case CREATE_USED_FOR_TERM :{
                // looking for MERIMEEUsedForTerm
            directPrefixesOnly = true;
            StringObject givenClass = new StringObject(userOperation.getValue()+"ThesaurusExpressionType");
            //strcpy(givenClass, userOperation); // MERIMEE
            //strcat(givenClass, "ThesaurusExpressionType"); // MERIMEEThesaurusExpressionType
            ret = GetThesaurusObject(ClassName, null, new StringObject("UsedForTerm"), null, givenClass, card);
            if (ret==TMS_APIFail) {
                return TMS_APIFail;
            }
            break;
        }
      	
   	case CREATE_EDITOR : {
            //sprintf(ClassName, "%sEditor", userOperation); // MERIMEEEditor
            ClassName.setValue(userOperation.getValue()+"Editor");
            break;
        }
   	case CREATE_SOURCE : {
            //strcpy(ClassName, "Source");
            ClassName.setValue("Source");
            break;
        }
        // Subclasses of class Word
        /*
        case CREATE_AMERICAN_WORD : strcpy(ClassName, "AmericanWord"); break;
   	case CREATE_DANISH_WORD : strcpy(ClassName, "DanishWord"); break;
   	case CREATE_CATALAN_WORD : strcpy(ClassName, "CatalanWord"); break;
		case CREATE_SPANISH_WORD : strcpy(ClassName, "SpanishWord"); break;
		case CREATE_PORTUGUESE_WORD : strcpy(ClassName, "PortugueseWord"); break;
		case CREATE_GERMAN_WORD : strcpy(ClassName, "GermanWord"); break;
		case CREATE_GREEK_WORD : strcpy(ClassName, "GreekWord"); break;
		case CREATE_ITALIAN_WORD : strcpy(ClassName, "ItalianWord"); break;
		case CREATE_FRENCH_WORD : strcpy(ClassName, "FrenchWord"); break;
   	case CREATE_ENGLISH_WORD : strcpy(ClassName, "EnglishWord");	break;
        */
        }//end of switch
        
        return CreateNodeSubRoutine(node, ClassName,directPrefixesOnly);        
        //return TMS_APISucc;
    }
    
    int CreateNodeSubRoutine(StringObject node, StringObject ClassName, boolean directPrefixesOnly){
        /*
        //abort if ClassName does not exist
	QC->reset_name_scope();
	int ClassSysid = QC->set_current_node(ClassName);
	if (ClassSysid <= 0) {
		strcpy(errorMessage, ClassName);
		strcat(errorMessage, " ");
		strcat(errorMessage, translate(OBJECT_DOES_NOT_EXIST));
		return TMS_APIFail;
	}

   // get the correct prefix of the node to be created
   int numOfPrefixes;
   l_name prefix_node;
   *prefix_node = '\0';
	GetPrefixOfClass(ClassSysid, prefix_node, &numOfPrefixes, errorMessage);

	// abort if node has not the correct prefix
	if (strncmp(prefix_node, node, strlen(prefix_node))) {
		sprintf(errorMessage, translate("%s has not the correct prefix: %s"), node, prefix_node);
		return TMS_APIFail;
	}
  	// abort if node contains only prefix
   int onlyPrefix = StringContainsOnlyPrefix(prefix_node, node);
	if (onlyPrefix) {
		sprintf(errorMessage, translate("Given node must not be blank after prefix: %s"), prefix_node);
		return TMS_APIFail;
	}

	IDENTIFIER Inode;
	strcpy(Inode.loginam, node);
	Inode.tag = ID_TYPE_LOGINAM;

	IDENTIFIER Iclass;
	strcpy(Iclass.loginam, ClassName);
	Iclass.tag = ID_TYPE_LOGINAM;

	// create the node
	int ret = QC->Add_Node(&Inode, SIS_API_TOKEN_CLASS);
	if (ret==APIFail) { abort_create(Inode.loginam, errorMessage); return TMS_APIFail;}

	// instantiate node under ClassName
	ret = QC->Add_Instance(&Inode, &Iclass);
	if (ret==APIFail) { abort_create(Inode.loginam, errorMessage); return TMS_APIFail;}

	commit_create(Inode.loginam, errorMessage); return TMS_APISucc;
        */
        //abort if ClassName does not exist
	QC.reset_name_scope();
	long ClassSysidL = QC.set_current_node(ClassName);
	if (ClassSysidL <= 0) {
		//strcpy(errorMessage, ClassName);
		//strcat(errorMessage, " ");
		//strcat(errorMessage, translate(OBJECT_DOES_NOT_EXIST));
            errorMessage.setValue(ClassName.getValue()+" " + OBJECT_DOES_NOT_EXIST);
            return TMS_APIFail;
	}

        // get the correct prefix of the node to be created
        IntegerObject numOfPrefixes = new IntegerObject(0);
        StringObject prefix_node = new StringObject();
        //*prefix_node = '\0';
	GetPrefixOfClass(ClassSysidL, prefix_node, numOfPrefixes, errorMessage,directPrefixesOnly);

	// abort if node has not the correct prefix
	//if (strncmp(prefix_node, node, strlen(prefix_node))) {
        if(node.getValue().startsWith(prefix_node.getValue())==false){
            //sprintf(errorMessage, translate("%s has not the correct prefix: %s"), node, prefix_node);
            errorMessage.setValue(String.format("%s has not the correct prefix: %s", node.getValue(), prefix_node.getValue()));
            return TMS_APIFail;
        }
        
  	// abort if node contains only prefix
        //int onlyPrefix = StringContainsOnlyPrefix(prefix_node, node);
	//if (onlyPrefix) {
        if(node.getValue().equals(prefix_node.getValue())){
            //sprintf(errorMessage, translate("Given node must not be blank after prefix: %s"), prefix_node);
            errorMessage.setValue(String.format("Given node must not be blank after prefix: %s", prefix_node.getValue()));
            return TMS_APIFail;
	}

	Identifier Inode = new Identifier(node.getValue());
	//strcpy(Inode.loginam, node);
	//Inode.tag = ID_TYPE_LOGINAM;

        Identifier Iclass = new Identifier(ClassSysidL);
	//IDENTIFIER Iclass;
	//strcpy(Iclass.loginam, ClassName);
	//Iclass.tag = ID_TYPE_LOGINAM;

	// create the node
	int ret = QC.CHECK_Add_Node(Inode, QClass.SIS_API_TOKEN_CLASS,true);
	if (ret==QClass.APIFail) { 
            abort_create(node, errorMessage); 
            return TMS_APIFail;
        }

	// instantiate node under ClassName
	ret = QC.CHECK_Add_Instance(Inode, Iclass);
	if (ret==QClass.APIFail) { 
            abort_create(node, errorMessage); return TMS_APIFail;
        }

	commit_create(node, errorMessage); 
        return TMS_APISucc;        
    }
    
    int GetPrefixOfClass(long classSysidL, StringObject prefix, IntegerObject numOfPrefixes, StringObject message, boolean directOnly){
        /*
                        tms_api::GetPrefixOfClass()
	looks for any prefix defined for classSysid in data base:
   gets all superclasses of nodeSysid and all links pointing
   from them of category <Individual->has_prefix>. It fills string prefix
   with the <to-value> name of the first <has_prefix> link found
int tms_api::GetPrefixOfClass(int classSysid, char *prefix, int *numOfPrefixes, char *message)
{
	message[0] = '\0';
   prefix[0] = '\0';
	QC->reset_name_scope();
	if (QC->set_current_node_id(classSysid) == ERROR) {
      sprintf(message, "In GetPrefixOfClass : fail to QC->set_current_node(%d)", classSysid);
		return ERROR;
	}
   int target_class_set = QC->set_get_new();
   QC->set_put(target_class_set);

   int classes_set_id;
	if ((classes_set_id = QC->get_all_superclasses(DEFAULT_NODE)) == ERROR) {
		sprintf(message, "In GetPrefixOfClass : fail to get_all_superclasses(%d)", classSysid);
		return ERROR;
   }
   QC->set_union(classes_set_id, target_class_set);
   QC->free_set(target_class_set);
   return (GetPrefixOfClassesSet(classes_set_id, prefix, numOfPrefixes, message));
}

        */
        message.setValue("");
        prefix.setValue("");
	QC.reset_name_scope();
	if (QC.set_current_node_id(classSysidL) == QClass.APIFail) {
            //sprintf(message, "In GetPrefixOfClass : fail to QC->set_current_node(%d)", classSysid);
            message.setValue(String.format("In GetPrefixOfClass : fail to QC->set_current_node(%d)", classSysidL));                    
            return TMS_APIFail;
	}
        
        int target_class_set = QC.set_get_new();
        QC.set_put(target_class_set);
        int classes_set_id = target_class_set;
        if(directOnly==false){
            classes_set_id = QC.get_all_superclasses(0);
            if (classes_set_id == QClass.APIFail) {
                //sprintf(message, "In GetPrefixOfClass : fail to get_all_superclasses(%d)", classSysid);
                message.setValue(String.format("In GetPrefixOfClass : fail to get_all_superclasses(%d)", classSysidL));                    
                return TMS_APIFail;
            }

            QC.set_union(classes_set_id, target_class_set);
            QC.free_set(target_class_set);
        }
        return (GetPrefixOfClassesSet(classes_set_id, prefix, numOfPrefixes, message));
    }
    
    int GetPrefixOfClassesSet(int classes_set_id, StringObject prefix, IntegerObject numOfPrefixes, StringObject message){
        /*-----------------------------------------------------------------
                tms_api::GetPrefixOfClassesSet()
	looks for any prefix defined for set of classes classes_set_id in data base:
   gets all links pointing from them of category <Individual->has_prefix>.
   It fills string prefix with the <to-value> name of the first <has_prefix> link found
-----------------------------------------------------------------
int tms_api::GetPrefixOfClassesSet(int classes_set_id, char *prefix, int *numOfPrefixes, char *message)
{
   int linksSet;
	if ((linksSet = QC->get_link_from_by_category(classes_set_id, INDIVIDUAL, HAS_PREFIX)) == ERROR) {
		strcpy(message, "In GetPrefixOfClassesSet : fail to get_link_from_by_category()");
      goto GetPrefixOfClassesSetExitPoint;
   }
	if (QC->reset_set(linksSet) == ERROR) {
		strcpy(message, "In GetPrefixOfClassesSet : fail to QC->reset_set()");
		goto GetPrefixOfClassesSetExitPoint;
   }

   char cls[LOGINAM_SIZE];
   int fromid, sysid, flag;
   cm_value cmv;
   if ((*numOfPrefixes = QC->set_get_card(linksSet)) == ERROR) {
		strcpy(message, "In GetPrefixOfClassesSet : fail to QC->set_get_card()");
		goto GetPrefixOfClassesSetExitPoint;
   }
   if (*numOfPrefixes == 0) {
   	// let message empty in case of no prefixes found
		goto GetPrefixOfClassesSetExitPoint;
   }
	if (QC->return_link_id(linksSet, cls, &fromid, &sysid, &cmv, &flag) == ERROR) {
		strcpy(message, "In GetPrefixOfClassesSet : fail to QC->return_link_id()");
		goto GetPrefixOfClassesSetExitPoint;
   }
   strcpy(prefix, cmv.value.s);
   // free allocated space
	switch (cmv.type) {
		case TYPE_STRING : case TYPE_NODE :
			free(cmv.value.s);
	}
   if (*numOfPrefixes > 1) {
		sprintf(message, "%d prefixes were found", *numOfPrefixes);
		goto GetPrefixOfClassesSetExitPoint;
   }
	return SUCCESS;

GetPrefixOfClassesSetExitPoint:
	QC->free_set(classes_set_id);
   QC->free_set(linksSet);
   return ERROR;
}
        */
        
        int linksSet = QC.get_link_from_by_category(classes_set_id,new StringObject(INDIVIDUAL),new StringObject( HAS_PREFIX));
	if (linksSet == QClass.APIFail) {
            //strcpy(message, "In GetPrefixOfClassesSet : fail to get_link_from_by_category()");
            //goto GetPrefixOfClassesSetExitPoint;
            message.setValue("In GetPrefixOfClassesSet : fail to get_link_from_by_category()");
            QC.free_set(classes_set_id);
            QC.free_set(linksSet);
            return TMS_APIFail;                    
        }
        
	if (QC.reset_set(linksSet) == QClass.APIFail) {
            //strcpy(message, "In GetPrefixOfClassesSet : fail to QC->reset_set()");
            //goto GetPrefixOfClassesSetExitPoint;
            message.setValue("In GetPrefixOfClassesSet : fail to QC->reset_set()");
            QC.free_set(classes_set_id);
            QC.free_set(linksSet);
            return TMS_APIFail;                    
        }

        int num = QC.set_get_card(linksSet);
        if(num==QClass.APIFail){
            //if ((*numOfPrefixes = QC->set_get_card(linksSet)) == ERROR) {
		//strcpy(message, "In GetPrefixOfClassesSet : fail to QC->set_get_card()");
		//goto GetPrefixOfClassesSetExitPoint;
            //}
            message.setValue("In GetPrefixOfClassesSet : fail to QC->set_get_card()");
            QC.free_set(classes_set_id);
            QC.free_set(linksSet);
            return TMS_APIFail;           
        }   
        if(num==0){
            // let message empty in case of no prefixes found
            QC.free_set(classes_set_id);
            QC.free_set(linksSet);
            return TMS_APIFail;           
        }
        numOfPrefixes.setValue(num);
        
        //char cls[LOGINAM_SIZE];
        //int fromid, sysid, flag;
        //cm_value cmv;
   
        //if (QC->return_link_id(linksSet, cls, &fromid, &sysid, &cmv, &flag) == ERROR) {
	//	strcpy(message, "In GetPrefixOfClassesSet : fail to QC->return_link_id()");
	//	goto GetPrefixOfClassesSetExitPoint;
        //}
   
        // free allocated space
	//switch (cmv.type) {
	//	case TYPE_STRING : case TYPE_NODE :
	//		free(cmv.value.s);
	//}
        Vector<Return_Link_Row> retVals = new Vector<Return_Link_Row>();
        if(QC.bulk_return_link(linksSet, retVals)==QClass.APIFail){
            //no need for return_link_id
            //strcpy(message, "In GetPrefixOfClassesSet : fail to QC->return_link_id()");
            //goto GetPrefixOfClassesSetExitPoint;
            message.setValue("In GetPrefixOfClassesSet : fail to QC->bulk_return_link()");
            QC.free_set(classes_set_id);
            QC.free_set(linksSet);
            return TMS_APIFail;   
        }
        //strcpy(prefix, cmv.value.s);
        prefix.setValue(retVals.get(0).get_v3_cmv().getString());
        
        if (num > 1) {
            //sprintf(message, "%d prefixes were found", *numOfPrefixes);
            //goto GetPrefixOfClassesSetExitPoint;
            message.setValue(num + " prefixes were found");
            QC.free_set(classes_set_id);
            QC.free_set(linksSet);
            return TMS_APIFail;   
        }
   
	return TMS_APISucc;


    }
        
    int ClassPrefixfThesaurus(StringObject thesaurus, StringObject prefix_class, StringObject message){
    /*
    
       			tms_api::ClassPrefixfThesaurus()
-------------------------------------------------------------------
	INPUT: - thesaurus, the name of the given thesaurus
   	    - prefix_class, to be filled with the defined class prefix of the given thesaurus
			 - message, an allocated string which will be filled with
          	an error message in case of an error occurs
	OUTPUT: - TMS_APISucc in case no error query execution happens
           - TMS_APIFail in case an error query execution happens
   FUNCTION: gets the defined class prefix of the given thesaurus
   			 This is done by:
             - getting the links pointing to the given thesaurus
               and under category ("ThesaurusClassType","of_thesaurus")
             - getting the from_values of the above links
             - getting the links pointing from the above nodes
               and under category ("ThesaurusClassType","Class`UsesAsPrefix")
             - getting the to_values of the above links
             - informing the prefix_class with the above node
	ATTENTION: - this function must be called inside a query session

int tms_api::ClassPrefixfThesaurus(char *thesaurus, char *prefix_class, char *message)
{
  // get the links pointing to the given thesaurus
  // and under category ("ThesaurusClassType","of_thesaurus")
  QC->reset_name_scope();
  int thesaurusSySId = QC->set_current_node(thesaurus);
  if (thesaurusSySId<=0) { sprintf(message,"%s%s",translate(IN_CLASSPREFIXFTHES),thesaurus); return TMS_APIFail;}

  int ret_set1 = QC->get_link_to_by_category(0,"ThesaurusClassType","of_thesaurus");
  if ( (ret_set1==-1) || (QC->set_get_card(ret_set1)!=1) ){
    QC->free_set(ret_set1);
    sprintf(message,"%s%s",translate(IN_CLASSPREFIXFTHES),thesaurus); return TMS_APIFail;}

  // get the from_values of the above links
  int ret_set2 = QC->get_from_value(ret_set1);
  QC->free_set(ret_set1);
  if ( ( ret_set2==-1) || (QC->set_get_card(ret_set2)!=1) ){
    QC->free_set(ret_set2);
    sprintf(message,"%s%s",translate(IN_CLASSPREFIXFTHES),thesaurus); return TMS_APIFail;}

  // get the links pointing from the above nodes
  // and under category ("ThesaurusClassType","Class`UsesAsPrefix")
  ret_set1 = QC->get_link_from_by_category(ret_set2,"ThesaurusClassType","Class`UsesAsPrefix");
  QC->free_set(ret_set2);
  if ( (ret_set1==-1) || (QC->set_get_card(ret_set1)!=1) ){
    QC->free_set(ret_set1);
    sprintf(message,"%s%s",translate(IN_CLASSPREFIXFTHES),thesaurus); return TMS_APIFail;}

  // get the to_values of the above links
  ret_set2 = QC->get_to_value(ret_set1);
  QC->free_set(ret_set1);
  if ( ( ret_set2==-1) || (QC->set_get_card(ret_set2)!=1) ){
    QC->free_set(ret_set2);
    sprintf(message,"%s%s",translate(IN_CLASSPREFIXFTHES),thesaurus); return TMS_APIFail;}

  // inform the prefix_class with the above node
  QC->reset_set(ret_set2);
  int ret_val = QC->return_nodes(ret_set2,prefix_class);
  QC->free_set(ret_set2);
  if (ret_val==-1) { sprintf(message,"%s%s",translate(IN_CLASSPREFIXFTHES),thesaurus); return TMS_APIFail;}

  return TMS_APISucc;
}
    */
  // get the links pointing to the given thesaurus
  // and under category ("ThesaurusClassType","of_thesaurus")
        QC.reset_name_scope();
        long thesaurusSySIdL = QC.set_current_node(thesaurus);
        if (thesaurusSySIdL<=0) { 
            //sprintf(message,"%s%s",translate(IN_CLASSPREFIXFTHES),thesaurus); 
            message.setValue(String.format("%s%s", IN_CLASSPREFIXFTHES, thesaurus.getValue()));
            return TMS_APIFail;
        }
        
        int ret_set1 = QC.get_link_to_by_category(0,new StringObject("ThesaurusClassType"),new StringObject("of_thesaurus"));
        
        if ( (ret_set1==-1) || (QC.set_get_card(ret_set1)!=1) ){
            QC.free_set(ret_set1);
            //sprintf(message,"%s%s",translate(IN_CLASSPREFIXFTHES),thesaurus); 
            message.setValue(String.format("%s%s", IN_CLASSPREFIXFTHES, thesaurus.getValue()));
            return TMS_APIFail;
        }

        // get the from_values of the above links
        int ret_set2 = QC.get_from_value(ret_set1);
        QC.free_set(ret_set1);
        if ( ( ret_set2==-1) || (QC.set_get_card(ret_set2)!=1) ){
            QC.free_set(ret_set2);
            //sprintf(message,"%s%s",translate(IN_CLASSPREFIXFTHES),thesaurus); 
            message.setValue(String.format("%s%s", IN_CLASSPREFIXFTHES, thesaurus.getValue()));
            return TMS_APIFail;
        }

        // get the links pointing from the above nodes
        // and under category ("ThesaurusClassType","Class`UsesAsPrefix")
        ret_set1 = QC.get_link_from_by_category(ret_set2,new StringObject("ThesaurusClassType"),new StringObject("Class`UsesAsPrefix"));
        QC.free_set(ret_set2);
        if ( (ret_set1==-1) || (QC.set_get_card(ret_set1)!=1) ){
            QC.free_set(ret_set1);
            //sprintf(message,"%s%s",translate(IN_CLASSPREFIXFTHES),thesaurus); 
            message.setValue(String.format("%s%s", IN_CLASSPREFIXFTHES, thesaurus.getValue()));
            return TMS_APIFail;
        }
        
        // get the to_values of the above links
        ret_set2 = QC.get_to_value(ret_set1);
        QC.free_set(ret_set1);
        if ( ( ret_set2==-1) || (QC.set_get_card(ret_set2)!=1) ){
            QC.free_set(ret_set2);
            //sprintf(message,"%s%s",translate(IN_CLASSPREFIXFTHES),thesaurus); 
            message.setValue(String.format("%s%s", IN_CLASSPREFIXFTHES, thesaurus.getValue()));
            return TMS_APIFail;
        }

        // inform the prefix_class with the above node
        QC.reset_set(ret_set2);
        int ret_val = QC.return_nodes(ret_set2,prefix_class);
        QC.free_set(ret_set2);
        if (ret_val==-1) { 
            //sprintf(message,"%s%s",translate(IN_CLASSPREFIXFTHES),thesaurus); 
            message.setValue(String.format("%s%s", IN_CLASSPREFIXFTHES, thesaurus.getValue()));
            return TMS_APIFail;
        }
        
        return TMS_APISucc;
    }
    
    //CreateDescriptorAttribute
    int CreateDescAttr(/*String selectedThesaurus,*/ StringObject linkName, StringObject descriptorName, CMValue toValue, int level, int catSet, int creationMode) {
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*-------------------------------------------------------------------
        tms_api::CreateDescriptorAttribute()
        ---------------------------------------------------------------------
        INPUT: - linkName : the name of the new attribute ('\0' or NULL for unnamed)
        descriptorName : the from-value of the new attribute
        toValue : the to-value of the new attribute
        catSet : the set with the categories of the attribute
        creationMode : CREATION_FOR_NEW_DESCRIPTOR or CREATION_FOR_VERSIONED_DESCRIPTOR
        OUTPUT: - TMS_APISucc in case the given link is created succesfully
        - TMS_APIFail in case:
        · descriptorName is not the name of any existing new/released descriptor of
        currently used  thesaurus.
        · The given catSet contains categories which are not allowed
        to be used for the creation of a new/released descriptor attribute.
        Available categories for the creation of a new/released concept attribute
        (<thes_nameU> and <thes_nameL> are the upper and lower case
        names of the currently selected thesaurus,
        for example: MERIMEE, merimee):
        Category from-class					Category name
        -------------------					------------
        <thes_ nameU>HierarchyTerm			<thes_ nameU>_ALT
        <thes_ nameU>HierarchyTerm			<thes_nameL>_display
        <thes_ nameU>HierarchyTerm			<thes_nameL>_editor
        <thes_ nameU>HierarchyTerm			<thes_nameL>_found_in
        <thes_ nameU>HierarchyTerm			<thes_nameL>_modified
        <thes_ nameU>HierarchyTerm			<thes_nameL>_found_in
        <thes_ nameU>HierarchyTerm			<thes_ nameU>_RT
        <thes_ nameU>HierarchyTerm			<thes_ nameU>_UF
        <thes_ nameU>ThesaurusConcept		<thes_ nameU>_translation, to_EN
        <thes_ nameU>ThesaurusConcept		<thes_ nameU>_translation, to_EL
        <thes_ nameU>ThesaurusConcept		<thes_ nameU>_translation, to_IT
        (only for new descriptors):
        <thes_ nameU>HierarchyTerm			<thes_nameL>_created
        FUNCTION: creates the given attribute
        ATTENTION: permited only for:
        - creationMode = CREATION_FOR_NEW_DESCRIPTOR
        new descriptors (belong to MERIMEENewDescriptor)
        - creationMode = CREATION_FOR_VERSIONED_DESCRIPTOR
        released descriptors (belong to MERIMEEDescriptor)
        ----------------------------------------------------------------*/
        //</editor-fold >
        // <editor-fold defaultstate="collapsed" desc="C++ code..."> 
        /*int tms_api::CreateDescriptorAttribute(char *linkName, char *descriptorName, cm_value *toValue, int level, int catSet, int creationMode)
        {
                int ret;
           l_name thes_descriptor, thes_new_descriptor;

                // check if given descriptor exists in data base
           QC->reset_name_scope();
           if (QC->set_current_node(descriptorName) < 0) {
              sprintf(errorMessage, translate("%s does not exist in data base"), descriptorName);
              return TMS_APIFail;
           }

           // looking for MERIMEEDescriptor
           int card;
           l_name givenClassFrom, givenClass;
           strcpy(givenClass, userOperation); // MERIMEE
           strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
           ret = GetThesaurusObject(thes_descriptor, NULL, "Descriptor", NULL, givenClass, &card);
                if (ret==TMS_APIFail) {
              sprintf(errorMessage, translate("Failed to refer to class <%s%s>"), userOperation, "Descriptor");
              return TMS_APIFail;
           }
                // looking for MERIMEENewDescriptor
                strcpy(givenClass, userOperation); // MERIMEE
                strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
                ret = GetThesaurusObject(thes_new_descriptor, NULL, "NewDescriptor", NULL, givenClass, &card);
                if (ret==TMS_APIFail) {
              sprintf(errorMessage, translate("Failed to refer to class <%s%s>"), userOperation, "NewDescriptor");
              return TMS_APIFail;
           }

                if (creationMode == CREATION_FOR_NEW_DESCRIPTOR) { // for a New Descriptor
                   // check if given descriptor is a new descriptor of current thesaurus
                   // if it belongs to MERIMEENewDescriptor
                        ret = ObjectinClass(descriptorName, thes_new_descriptor, errorMessage);
                        if (ret == TMS_APIFail){
                                sprintf(errorMessage,translate("%s is not a %s descriptor"), descriptorName, thes_new_descriptor);
                      return TMS_APIFail;
                        }
           }
           else { // for a Versioned Descriptor
                   // check if given descriptor is a versioned descriptor of current thesaurus
                   // if it belongs to MERIMEEDescriptor
                        ret = ObjectinClass(descriptorName, thes_descriptor, errorMessage);
                        if (ret == TMS_APIFail){
                                sprintf(errorMessage,translate("%s is not a %s descriptor"), descriptorName, thes_descriptor);
                      return TMS_APIFail;
                        }
                   // AND if it does NOT belong to MERIMEENewDescriptor
                        ret = ObjectinClass(descriptorName, thes_new_descriptor, errorMessage);
                        if (ret == TMS_APISucc){
                                sprintf(errorMessage,translate("%s is a %s descriptor"), descriptorName, thes_new_descriptor);
                      return TMS_APIFail;
                        }
           }

           // check if given category contains a permitted category for Descriptors
           QC->reset_set(catSet);
           l_name fromCateg, categName;
           cm_value toCateg;
           int allowedCategory = 0;
           int i;
                while(QC->return_link(catSet, fromCateg, categName, &toCateg)!=-1) {
                if (creationMode == CREATION_FOR_NEW_DESCRIPTOR) { // for a New Descriptor
                        for(i=0; i<MAX_NEW_DESCRIPTOR_CATEGORIES; i++) {
                        if (!strcmp(fromCateg, NewDescriptorCategories[i][0]) && !strcmp(categName, NewDescriptorCategories[i][1])) {
                                allowedCategory = 1;
                       break;
                         }
                      }
              }
                   else { // for a Versioned Descriptor
                        for(i=0; i<MAX_VERSIONED_DESCRIPTOR_CATEGORIES; i++) {
                        if (!strcmp(fromCateg, VersionedDescriptorCategories[i][0]) && !strcmp(categName, VersionedDescriptorCategories[i][1])) {
                                allowedCategory = 1;
                       break;
                         }
                      }
              }
                // free alocated space fro cm_value
                        switch (toCateg.type) {
                                case TYPE_STRING : case TYPE_NODE :
                                        free(toCateg.value.s);
                        }
              if (allowedCategory) break;
           }
                if (!allowedCategory){
                        sprintf(errorMessage, translate("The given category set for the creation of a descriptor attribute contains categories which are not allowed to be used.\n"));
              return TMS_APIFail;
                }

                // Create the hierarchy attribute
           if (!linkName) *linkName = 0;
                ret = CreateAttribute(linkName, descriptorName, toValue, level, catSet);
                if (ret==APIFail) {abort_create_attribute(descriptorName, linkName, errorMessage); return TMS_APIFail;}

                commit_create_attribute(descriptorName, linkName, errorMessage); return TMS_APISucc;
        }
        */
        //</editor-fold>
        
        
        int ret;
        StringObject thes_descriptor =new StringObject();
        StringObject thes_new_descriptor = new StringObject();

	// check if given descriptor exists in data base
        QC.reset_name_scope();
        if (QC.set_current_node(descriptorName) < 0) {
            //sprintf(errorMessage, translate("%s does not exist in data base"), descriptorName);
            errorMessage.setValue(String.format("%s does not exist in data base", descriptorName.getValue()));
            return TMS_APIFail;
        }
        
        // looking for MERIMEEDescriptor
        IntegerObject card = new IntegerObject();
        StringObject givenClassFrom = new StringObject();
        StringObject givenClass = new StringObject(userOperation.getValue()+"ThesaurusNotionType");
        //strcpy(givenClass, userOperation); // MERIMEE
        //strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
        ret = GetThesaurusObject(thes_descriptor, null, new StringObject("Descriptor"), null, givenClass, card);
	if (ret==TMS_APIFail) {
            //sprintf(errorMessage, translate("Failed to refer to class <%s%s>"), userOperation, "Descriptor");
            errorMessage.setValue(String.format("Failed to refer to class <%s%s>", userOperation.getValue(), "Descriptor"));
            return TMS_APIFail;
        }
        
	// looking for MERIMEENewDescriptor
        givenClass.setValue(userOperation.getValue()+"ThesaurusNotionType");
	//strcpy(givenClass, userOperation); // MERIMEE
	//strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
	ret = GetThesaurusObject(thes_new_descriptor, null, new StringObject("NewDescriptor"), null, givenClass, card);
	if (ret==TMS_APIFail) {
            //sprintf(errorMessage, translate("Failed to refer to class <%s%s>"), userOperation, "NewDescriptor");
            errorMessage.setValue(String.format("Failed to refer to class <%s%s>", userOperation.getValue(), "NewDescriptor"));
            return TMS_APIFail;
        }

	if (creationMode == CREATION_FOR_NEW_DESCRIPTOR) { // for a New Descriptor
	   // check if given descriptor is a new descriptor of current thesaurus
	   // if it belongs to MERIMEENewDescriptor
            ret = ObjectinClass(descriptorName, thes_new_descriptor, errorMessage);
            if (ret == TMS_APIFail){
                //sprintf(errorMessage,translate("%s is not a %s descriptor"), descriptorName, thes_new_descriptor);
                errorMessage.setValue(String.format("%s is not a %s descriptor", descriptorName.getValue(), thes_new_descriptor.getValue()));
                return TMS_APIFail;
            }
        }
        else { // for a Versioned Descriptor
            // check if given descriptor is a versioned descriptor of current thesaurus
            // if it belongs to MERIMEEDescriptor
            ret = ObjectinClass(descriptorName, thes_descriptor, errorMessage);
            if (ret == TMS_APIFail){
                //sprintf(errorMessage,translate("%s is not a %s descriptor"), descriptorName, thes_descriptor);
                errorMessage.setValue(String.format("%s is not a %s descriptor", descriptorName.getValue(), thes_descriptor.getValue()));
                return TMS_APIFail;
            }
            // AND if it does NOT belong to MERIMEENewDescriptor
            ret = ObjectinClass(descriptorName, thes_new_descriptor, errorMessage);
            if (ret == TMS_APISucc){
                //sprintf(errorMessage,translate("%s is a %s descriptor"), descriptorName, thes_new_descriptor);
                errorMessage.setValue(String.format("%s is a %s descriptor", descriptorName.getValue(), thes_new_descriptor.getValue()));
                return TMS_APIFail;
            }
        }
        
        // check if given category contains a permitted category for Descriptors
        
        // <editor-fold defaultstate="collapsed" desc="REPLACING CATEGORY CHECKING ALGORITHM - more loose">
        //
        /*
        
   QC->reset_set(catSet);
   l_name fromCateg, categName;
   cm_value toCateg;
   int allowedCategory = 0;
   int i;
	while(QC->return_link(catSet, fromCateg, categName, &toCateg)!=-1) {
   	if (creationMode == CREATION_FOR_NEW_DESCRIPTOR) { // for a New Descriptor
	   	for(i=0; i<MAX_NEW_DESCRIPTOR_CATEGORIES; i++) {
	      	if (!strcmp(fromCateg, NewDescriptorCategories[i][0]) && !strcmp(categName, NewDescriptorCategories[i][1])) {
	         	allowedCategory = 1;
               break;
	         }
	      }
      }
	   else { // for a Versioned Descriptor
	   	for(i=0; i<MAX_VERSIONED_DESCRIPTOR_CATEGORIES; i++) {
	      	if (!strcmp(fromCateg, VersionedDescriptorCategories[i][0]) && !strcmp(categName, VersionedDescriptorCategories[i][1])) {
	         	allowedCategory = 1;
               break;
	         }
	      }
      }
   	// free alocated space fro cm_value
		switch (toCateg.type) {
			case TYPE_STRING : case TYPE_NODE :
				free(toCateg.value.s);
		}
      if (allowedCategory) break;
   }
	if (!allowedCategory){
		sprintf(errorMessage, translate("The given category set for the creation of a descriptor attribute contains categories which are not allowed to be used.\n"));
      return TMS_APIFail;
	}
        */
        // </editor-fold> 
        
        
        QC.reset_set(catSet);
        StringObject fromCateg = new StringObject();
        StringObject categName= new StringObject();
        Vector<Return_Link_Row> retLvals = new Vector<Return_Link_Row>();
        if(QC.bulk_return_link(catSet, retLvals)==QClass.APIFail){
            QC.free_set(catSet);
            return TMS_APIFail;
        }        
        //QC.free_set(catSet);
        int allowedCategory = 0;
        for(Return_Link_Row row: retLvals){
            fromCateg.setValue(row.get_v1_cls());
            categName.setValue(row.get_v2_label());
            //instead of category control
            if(categName.getValue().contains(userOperation.getValue())|| categName.getValue().contains(userOperationLow.getValue())){
                allowedCategory = 1;
                break;
            }
        }        
        if (allowedCategory==0){
            //sprintf(errorMessage, translate("The given category set for the creation of a descriptor attribute contains categories which are not allowed to be used.\n"));
            errorMessage.setValue(String.format("The given category set for the creation of a descriptor attribute contains categories which are not allowed to be used.\n"));
            return TMS_APIFail;
        }

        // Create the hierarchy attribute
        //if (!linkName) *linkName = 0;
        
        ret = CreateAttribute(linkName, descriptorName, toValue, level, catSet);
	if (ret==QClass.APIFail) {
            abort_create_attribute(descriptorName, linkName, errorMessage); 
            return TMS_APIFail;
        }

	commit_create_attribute(descriptorName, linkName, errorMessage); return TMS_APISucc;
    }
    
    int Attribute_Creation(StringObject from, CMValue to, int category_set) {
        
        // <editor-fold defaultstate="collapsed" desc="Comments...">     
        /*-----------------------------------------------------------------
         tms_api::Check_Attribute_Creation()
         -------------------------------------------------------------------
         INPUT:  - from, the from value of the detected link
                 - to, the to value of the detected link
                 - category_set, the set of categories of the detected link
         OUTPUT: - TMS_APIFail in case a cycle has been detected
                 - TMS_APISucc in case no cycle has been detected
         FUNCTION: A new link is going to be added from <from> to <to> under
                   category_set. It checks if any of the categories in category_set
                   is of kind DAG. If so, it checks if the addition of the new link
                   causes a cycle.
         ATTENTION: - this function must be called inside a query session
         -----------------------------------------------------------------*/
         //</editor-fold>
        // <editor-fold defaultstate="collapsed" desc="C++ code..."> 
        /*
        int tms_api::Check_Attribute_Creation(char *from, cm_value *to, int category_set)
        {
                // get the sysid of the from value
           int target_sysid;
                QC->reset_name_scope();
                target_sysid = QC->set_current_node(from);

                // scan the set with the categories
           QC->reset_set(category_set);
           cm_value cmv;
           int fromid, linkSysid, categid, flag;
           l_name cls, label, categ, fromcls;
           while (QC->return_full_link_id(category_set, cls, &fromid, label, &linkSysid, categ, fromcls, &categid, &cmv, &flag) != ERROR) {
                        // free allocated space
                        switch (cmv.type) {
                        case TYPE_STRING : case TYPE_NODE :
                                free(cmv.value.s);
                        }

              // check if current category is of type DAG
                        int category_is_DAG = LinkIsOfCategory(linkSysid, INDIVIDUAL, _DIRECTED_ACYCLIC_GRAPH);
                   if (category_is_DAG == -1) {
                strcpy(errorMessage, "Failed to execute LinkIsOfCategory()");
                        return TMS_APIFail;
                   }
              // if so, check for cycle
              if (category_is_DAG) {
                                int cycle;
                                if (CycleDetected(target_sysid, to, cls, label, &cycle) == ERROR) {
                        strcpy(errorMessage, "Failed to execute CycleDetected()");
                        return TMS_APIFail;
                                }
                                if (cycle) {
                    sprintf(errorMessage, translate(CYCLE_DETECTION), label, to->value.s);
                                        return TMS_APIFail;
                                }
                        }
                }
           return TMS_APISucc;
        }*/
        //</editor-fold>
        
        // get the sysid of the from value
        long target_sysidL;
        QC.reset_name_scope();
        target_sysidL = QC.set_current_node(from);

        // scan the set with the categories
        QC.reset_set( category_set);
        //CMValue cmv = new CMValue();

        //IntegerObject fromid = new IntegerObject();
        //IntegerObject linkSysid = new IntegerObject();
        //IntegerObject categid = new IntegerObject();
        //IntegerObject flag = new IntegerObject();

        //StringObject cls = new StringObject();
        //StringObject label = new StringObject();
        //StringObject categ = new StringObject();
        //StringObject fromcls = new StringObject();

        Vector<Return_Full_Link_Id_Row> retVals = new Vector<Return_Full_Link_Id_Row>();
        if(QC.bulk_return_full_link_id(category_set, retVals)!=QClass.APIFail){
            for(Return_Full_Link_Id_Row row:retVals){
                // check if current category is of type DAG
                int category_is_DAG = ATTENTION_LinkIsOfCategory(row.get_v4_linkId(), new StringObject(INDIVIDUAL), new StringObject(_DIRECTED_ACYCLIC_GRAPH));
                if (category_is_DAG  == QClass.APIFail) {
                    //strcpy(errorMessage, "Failed to execute LinkIsOfCategory()");
                    Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "Failed to execute LinkIsOfCategory()");
                    return TMS_APIFail;
                }
                // if so, check for cycle
                if (category_is_DAG != QClass.FALSEval) {
                    IntegerObject cycle = new IntegerObject(0);
                    if (CycleDetected(target_sysidL, to, new StringObject(row.get_v1_cls()), new StringObject(row.get_v3_label()), cycle) == QClass.APIFail) {
                        //strcpy(errorMessage, "Failed to execute CycleDetected()");
                        Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "Failed to execute CycleDetected()");
                        return TMS_APIFail;
                    }
                    if (cycle.getValue() != 0) {
                        // sprintf(errorMessage, translate(CYCLE_DETECTION), label, to ->  value.s);
                        Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "Failed to execute CycleDetected()");
                        return TMS_APIFail;
                    }
                }
            }
        }
        /*
        while (Q.return_full_link_id( category_set, cls, fromid, label, linkSysid, categ, fromcls, categid, cmv, flag) != QClass.APIFail) {
            // free allocated space
            

            // check if current category is of type DAG
            int category_is_DAG = LinkIsOfCategory(linkSysid.getValue(), new StringObject(INDIVIDUAL), new StringObject(_DIRECTED_ACYCLIC_GRAPH));
            if (category_is_DAG  == -1) {
                //strcpy(errorMessage, "Failed to execute LinkIsOfCategory()");
                Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, Parameters.LogFilePrefix+"Failed to execute LinkIsOfCategory()");
                return WebTMS_APIFail;
            }
            // if so, check for cycle
            if (category_is_DAG != 0) {
                int cycle = 0;
                if (CycleDetected(target_sysidL, to, cls, label, cycle) == QClass.APIFail) {
                    //strcpy(errorMessage, "Failed to execute CycleDetected()");
                    Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, Parameters.LogFilePrefix+"Failed to execute CycleDetected()");
                    return WebTMS_APIFail;
                }
                if (cycle != 0) {
                    // sprintf(errorMessage, translate(CYCLE_DETECTION), label, to ->  value.s);
                    Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, Parameters.LogFilePrefix+"Failed to execute CycleDetected()");
                    return WebTMS_APIFail;
                }
            }
        }
        */
        return TMS_APISucc;
    }
    
    int CreateAttribute(StringObject linkName, StringObject from, CMValue toValue, int level, int catSet) {


        //<editor-fold defaultstate="collapsed" desc="Comments..."> 
    /*-------------------------------------------------------------------
    tms_api::CreateAttribute()
    ---------------------------------------------------------------------
    INPUT: - linkName : the name of the new attribute ('\0' or NULL for unnamed)
    from : the from-value of the new attribute
    toValue : the to-value of the new attribute
    level : the instantiation level of the new attribute
    catSet : the set with the categories of the attribute
    OUTPUT: - TMS_APISucc in case the given link is created succesfully
    - TMS_APIFail otherwise
    FUNCTION: creates the given attribute
    ----------------------------------------------------------------*/
     //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="C++ code..."> 
    /*
    int tms_api::CreateAttribute(char *linkName, char *from, cm_value *toValue, int level, int catSet)
    {
            QC->reset_name_scope();
       int fromSysid = QC->set_current_node(from);
       IDENTIFIER I_from(fromSysid);

       if (Check_Attribute_Creation(from, toValue, catSet) == TMS_APIFail) return TMS_APIFail;

       int ret;
       if (linkName && (*linkName)) { // named attribute
            IDENTIFIER I_link;
          strcpy(I_link.loginam, linkName);
          I_link.tag = ID_TYPE_LOGINAM;
          ret = QC->Add_Named_Attribute(&I_link, &I_from, toValue, level, catSet);
            } else { // unnamed attribute
          ret = QC->Add_Unnamed_Attribute(&I_from, toValue, catSet);
       }
       if(ret == APIFail) return TMS_APIFail;
            return TMS_APISucc;
    }
    *///</editor-fold>

        //int SisSessionId = sis_session.getValue();
        QC.reset_name_scope();
        long fromSysidL = QC.set_current_node( from);
        Identifier I_from = new Identifier(fromSysidL);

        if (Attribute_Creation(from, toValue, catSet) == TMS_APIFail) {
            return TMS_APIFail;
        }
        int ret;
        int card = QC.set_get_card(catSet);
        //if (linkName && (*  linkName)) { // named attribute
        
        if (linkName != null&& linkName.getValue() != null && linkName.getValue().length() > 0) {// && !linkNameStr.startsWith(" ")) {// named attribute //  !linkNameStr.startsWith(" ")) {// named attribute
            Identifier I_link = new Identifier();
            I_link.setValue(linkName.getValue());
            I_link.setTag(Identifier.ID_TYPE_LOGINAM);
            ret = QC.CHECK_Add_Named_Attribute(I_link, I_from, toValue, level, catSet,true);
        } else { // unnamed attribute
            ret = QC.CHECK_Add_Unnamed_Attribute( I_from, toValue, catSet);
        }
        if (ret == QClass.APIFail) {
            return TMS_APIFail;
        }
        return TMS_APISucc;
    }
    
    int CycleDetected(long fromSysidL, CMValue to_cmv, StringObject categoryFromName, StringObject categoryName, IntegerObject cycle) {

         
        //<editor-fold defaultstate="collapsed" desc="Comments...">
    /*-----------------------------------------------------------------
    tms_api::CycleDetected()
    -------------------------------------------------------------------
    INPUT: - fromSysid, the sysid of from value of the detected link
    - to_cmv, the to value of the detected link
    - categoryFromName, the from value of the category of the detected link
    - categoryName, the name of the category of the detected link
    - cycle an integer which will be filled with the result of the detection
    OUTPUT: - TMS_APISucc in case no error query/update execution happens
    - TMS_APIFail in case an error query/update execution happens
    FUNCTION: checks if by adding a link from "fromSysid" to "to_cmv"
    under the category "categInfo", a directed cycle of this kind of links
    is created and informs parameter "cycle" (1 in case a cycle has been
    detected, 0 otherwise). A cycle of this kind will be created when:
    from_node belongs to {QC->get_to_values_by_category(to_node, categInfo)}
    ATTENTION: - this function must be called inside a query session
    -----------------------------------------------------------------*/
    //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="C++ code..."> 
    /*
    int tms_api::CycleDetected(int fromSysid, cm_value *to_cmv, l_name categoryFromName, l_name categoryName, int *cycle)
    {
            // first check if link is going to be added to the same node
       // with the from node. If so, there is a cycle.
            int toSysid;
            if (QC->get_classid(to_cmv->value.s, &toSysid) < 0) {
          fprintf(stderr, "In CycleDetected : fail to get_class_id(%s)", to_cmv->value.s);
                    return TMS_APIFail;
            }
       if (fromSysid == toSysid) {
            *cycle = 1;
            return TMS_APISucc;
       }

       // QC->get_to_values_by_category(to_node, categInfo)
            QC->reset_name_scope();
            if (QC->set_current_node(to_cmv->value.s) < 0) {
          fprintf(stderr, "In CycleDetected : fail to QC->set_current_node(%s)", to_cmv->value.s);
                    return TMS_APIFail;
            }

            // set the category
            categories_set catSet;
       strcpy((catSet[0]).fcl, categoryFromName);
       strcpy((catSet[0]).cat, categoryName);
       (catSet[0]).direction = FORWARD;
       strcpy((catSet[1]).fcl, "end");
       strcpy((catSet[1]).cat, "end");
       (catSet[1]).direction = 0; // <=> end categories setting
       QC->set_categories(catSet);
       int link_set_id;
            if ((link_set_id = QC->get_traverse_by_category(0, NOISA)) < 0) {
          fprintf(stderr, "In CycleDetected : fail to get_traverse_by_category()");
                    return TMS_APIFail;
            }

       QC->reset_set(link_set_id);
       int to_values_set;
       if ((to_values_set = QC->get_to_value(link_set_id)) < 0) {
          fprintf(stderr, "In CycleDetected : fail to QC->get_to_value()");
                    return TMS_APIFail;
            }

       // check if from_node belongs to
       // {QC->get_to_values_by_category(to_node, categInfo)}
       QC->reset_name_scope();
            if (QC->set_current_node_id(fromSysid) < 0) {
          fprintf(stderr, "In CycleDetected : fail to QC->set_current_node_id(%d)", fromSysid);
                    return TMS_APIFail;
            }

       QC->reset_set(to_values_set);
       if (QC->set_member_of(to_values_set) != -1) {
            *cycle = 1;
       }
       else {
                    *cycle = 0;
       }
       QC->free_set(link_set_id);
       QC->free_set(to_values_set);
       return TMS_APISucc;
    }
    */
    //</editor-fold>

        
        PrimitiveObject_Long toSysidL = new PrimitiveObject_Long();
        if (QC.get_classid( new StringObject(to_cmv.getString()), toSysidL) < 0) {
            //fprintf(stderr, "In CycleDetected : fail to get_class_id(%s)", to_cmv ->  value.s);
            Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "In CycleDetected : fail to get_class_id(" + to_cmv.getString() + ")");
            return TMS_APIFail;
        }
        if (fromSysidL == toSysidL.getValue()) {
            cycle.setValue(1);
            return TMS_APISucc;
        }

        // QC->get_to_values_by_category(to_node, categInfo)
	QC.reset_name_scope();
        if (QC.set_current_node( new StringObject(to_cmv.getString())) < 0) {
            //fprintf(stderr, "In CycleDetected : fail to QC->set_current_node(%s)", to_cmv ->  value.s);
            Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "In CycleDetected : fail to QC->set_current_node(" + to_cmv.getString() + ")");
            return TMS_APIFail;
        }
	
	// set the category
        CategorySet[] categs = new CategorySet[1];
        CategorySet csobj1 = new CategorySet(categoryFromName.getValue(), categoryName.getValue(), QClass.Traversal_Direction.FORWARD);
        //CategorySet csobj2 = new CategorySet(strfclB.toString(), strcatB.toString(), 0);
        categs[0] = csobj1;
        //categs[1] = csobj2;
        QC.set_categories( categs);
	
        int link_set_id = QC.get_traverse_by_category(0, QClass.Traversal_Isa.NOISA);
	if (link_set_id < 0) {
            //fprintf(stderr, "In CycleDetected : fail to get_traverse_by_category()");
            Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "In CycleDetected : fail to QC->get_traverse_by_category(" + to_cmv.getString() + ")");
            return TMS_APIFail;
        }
        
        QC.reset_set( link_set_id);
        int to_values_set = QC.get_to_value( link_set_id);
        if (to_values_set < 0) {
            //fprintf(stderr, "In CycleDetected : fail to QC->get_to_value()");
            Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "In CycleDetected : fail to QC->get_to_value()");
            return TMS_APIFail;
        }
        
        // check if from_node belongs to
        // {QC->get_to_values_by_category(to_node, categInfo)}
        QC.reset_name_scope();
        if (QC.set_current_node_id( fromSysidL) < 0) {
            //fprintf(stderr, "In CycleDetected : fail to QC->set_current_node_id(%d)", fromSysid);
            Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "In CycleDetected : fail to QC->set_current_node_id(" + fromSysidL + ")");
            return TMS_APIFail;
        }

        QC.reset_set( to_values_set);
        if (QC.set_member_of( to_values_set) != -1) {
            cycle.setValue(1);
        } else {
            cycle.setValue(0);
        }
        QC.free_set( link_set_id);
        QC.free_set( to_values_set);
        return TMS_APISucc;
        
    }
    
    int DelDescrAttr(long linkSysidL, StringObject descriptorName, int deletionMode){
        //<editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*-------------------------------------------------------------------
        tms_api::DeleteDescriptorAttribute()
        ---------------------------------------------------------------------
        INPUT: - linkSysid : the sysid of a new/released descriptor's link
        descriptorName : the name of a new/released descriptor
        deletionMode : DELETION_FOR_NEW_DESCRIPTOR or DELETION_FOR_VERSIONED_DESCRIPTOR
        OUTPUT: - TMS_APISucc in case the given link is deleted succesfully
        - TMS_APIFail in case:
        · DescriptorName is not the name of any existing new/released
        descriptor of currently used thesaurus.
        · linkSysid is not the sysid of any existing link pointing
        from the given descriptor.
        FUNCTION: deletes the given link
        ATTENTION: permited only for:
        - deletionMode = DELETION_FOR_NEW_DESCRIPTOR
        new descriptors (belong to MERIMEENewDescriptor)
        - deletionMode = DELETION_FOR_VERSIONED_DESCRIPTOR
        released descriptors (belong to MERIMEEDescriptor)
        ----------------------------------------------------------------*/
    //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="C++ code..."> 
        /*
        int tms_api::DeleteDescriptorAttribute(int linkSysid, char *descriptorName, int deletionMode)
        {

            int ret;
            l_name thes_descriptor, thes_new_descriptor;

                // check if given descriptor exists in data base
            QC->reset_name_scope();
            if (QC ->  set_current_node(descriptorName) < 0) {
                sprintf(errorMessage, translate("%s does not exist in data base"), descriptorName);
                return TMS_APIFail;
            }

            // looking for MERIMEEDescriptor
            int card;
            l_name givenClassFrom, givenClass;
            strcpy(givenClass, userOperation); // MERIMEE
            strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
            ret = GetThesaurusObject(thes_descriptor, NULL, "Descriptor", NULL, givenClass, &  card);
            if (ret == TMS_APIFail) {
                sprintf(errorMessage, translate("Failed to refer to class <%s%s>"), userOperation, "Descriptor");
                return TMS_APIFail;
            }
            // looking for MERIMEENewDescriptor
            strcpy(givenClass, userOperation); // MERIMEE
            strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
            ret = GetThesaurusObject(thes_new_descriptor, NULL, "NewDescriptor", NULL, givenClass, &  card);
            if (ret == TMS_APIFail) {
                sprintf(errorMessage, translate("Failed to refer to class <%s%s>"), userOperation, "NewDescriptor");
                return TMS_APIFail;
            }

            if (deletionMode == DELETION_FOR_NEW_DESCRIPTOR) { // for a New Descriptor
                // check if given descriptor is a new descriptor of current thesaurus
                // if it belongs to MERIMEENewDescriptor
                ret = ObjectinClass(descriptorName, thes_new_descriptor, errorMessage);
                if (ret == TMS_APIFail) {
                    sprintf(errorMessage, translate("%s is not a %s descriptor"), descriptorName, thes_new_descriptor);
                    return TMS_APIFail;
                }
            } else { // for a Versioned Descriptor
                // check if given descriptor is a versioned descriptor of current thesaurus
                // if it belongs to MERIMEEDescriptor
                ret = ObjectinClass(descriptorName, thes_descriptor, errorMessage);
                if (ret == TMS_APIFail) {
                    sprintf(errorMessage, translate("%s is not a %s descriptor"), descriptorName, thes_descriptor);
                    return TMS_APIFail;
                }
                // AND if it does NOT belong to MERIMEENewDescriptor
                ret = ObjectinClass(descriptorName, thes_new_descriptor, errorMessage);
                if (ret == TMS_APISucc) {
                    sprintf(errorMessage, translate("%s is a %s descriptor"), descriptorName, thes_new_descriptor);
                    return TMS_APIFail;
                }
            }

            // check if the given link exists in data base and it is one of the links pointing from the given target object
            if (!IsLinkPointingFromTarget(linkSysid, descriptorName)) {
                return TMS_APIFail;
            }
            // check if the category of the given link belongs to the permitted categories for Hierarchies
            // get the categories of the link
            int linkClasses = QC ->  get_classes(0);
            QC->reset_set(linkClasses);
            l_name fromCateg, categName;
            cm_value toCateg;
            int allowedCategory = 0;
            int i;
            while (QC ->  return_link(linkClasses, fromCateg, categName, &  toCateg) != -1) {
                if (deletionMode == DELETION_FOR_NEW_DESCRIPTOR) { // for a New Descriptor
                    for (i = 0; i < MAX_NEW_DESCRIPTOR_CATEGORIES; i++) {
                        if (!strcmp(fromCateg, NewDescriptorCategories[i][0]) && !strcmp(categName, NewDescriptorCategories[i][1])) {
                            allowedCategory = 1;
                            break;
                        }
                    }
                } else { // for a Versioned Descriptor
                    for (i = 0; i < MAX_VERSIONED_DESCRIPTOR_CATEGORIES; i++) {
                        if (!strcmp(fromCateg, VersionedDescriptorCategories[i][0]) && !strcmp(categName, VersionedDescriptorCategories[i][1])) {
                            allowedCategory = 1;
                            break;
                        }
                    }
                }
                // free alocated space fro cm_value
                switch (toCateg.type) {
                    case TYPE_STRING:
                    case TYPE_NODE:
                        free(toCateg.value.s);
                }
                if (allowedCategory) {
                    break;
                }
            }
            QC->free_set(linkClasses);
            if (!allowedCategory) {
                sprintf(errorMessage, translate("Given link for deletion (%d) is not under one of the categories which are allowed to be used for descriptors.\n"), linkSysid);
                return TMS_APIFail;
            }

            // check if link to be deleted is the last depedent one
            if (IsLastDepedentLink(descriptorName, linkSysid, fromCateg, categName)) {
                sprintf(errorMessage, translate(IT_IS_NECESSARY_TO_HAVE_LINKS_UNDER_CATEGORY), categName, descriptorName, categName);
            }

            // check if deleted link is garbage collected =>
            // try to delete the to_value of the deleted link
            int IsGarbageCollected = LinkIsOfCategory(linkSysid, INDIVIDUAL, _GARBAGE_COLLECTED); // gen_queries

            // Delete the descriptor attribute
            ret = DeleteAttribute(linkSysid, descriptorName, IsGarbageCollected);
            if (ret == APIFail) {
                abort_delete_attribute(descriptorName, linkSysid, errorMessage);
                return TMS_APIFail;
            }

            commit_delete_attribute(descriptorName, linkSysid, errorMessage);
            return TMS_APISucc;
        }
        */
        //</editor-fold>
        
        
        int ret;
        StringObject thes_descriptor = new StringObject();
        StringObject thes_new_descriptor = new StringObject();

	// check if given descriptor exists in data base
        QC.reset_name_scope();
        if (QC.set_current_node(descriptorName) < 0) {
            //sprintf(errorMessage, translate("%s does not exist in data base"), descriptorName);
            errorMessage.setValue(String.format("%s does not exist in data base", descriptorName.getValue()));
            return TMS_APIFail;
        }
        
        IntegerObject card = new IntegerObject();
        
        StringObject givenClass =  new StringObject(userOperation.getValue() + "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
        ret = GetThesaurusObject(thes_descriptor, null, new StringObject("Descriptor"), null, givenClass, card);
        if (ret == TMS_APIFail) {
            //sprintf(errorMessage, translate("Failed to refer to class <%s%s>"), userOperation, "Descriptor");
            errorMessage.setValue("Failed to refer to class  <" +  userOperation.getValue() +  "Descriptor>");
            return TMS_APIFail;
        }

	// looking for MERIMEENewDescriptor
        // looking for MERIMEENewDescriptor
        givenClass.setValue(userOperation.getValue() + "ThesaurusNotionType"); // MERIMEEThesaurusNotionType   
        ret = GetThesaurusObject(thes_new_descriptor, null, new StringObject("NewDescriptor"), null, givenClass, card);
        if (ret == TMS_APIFail) {
            //sprintf(errorMessage, translate("Failed to refer to class <%s%s>"), userOperation, "NewDescriptor");
            errorMessage.setValue("Failed to refer to class <" +  userOperation.getValue() +  "NewDescriptor>");
            return TMS_APIFail;
        }
	
        
        
        if (deletionMode == DELETION_FOR_NEW_DESCRIPTOR) { // for a New Descriptor
            // check if given descriptor is a new descriptor of current thesaurus
            // if it belongs to MERIMEENewDescriptor
            ret = ObjectinClass(descriptorName, thes_new_descriptor, errorMessage);
            if (ret == TMS_APIFail) {
                //sprintf(errorMessage, translate("%s is not a %s descriptor"), descriptorName, thes_new_descriptor);
                errorMessage.setValue(String.format("%s is not a %s descriptor", descriptorName.getValue(), thes_new_descriptor.getValue()));
                return TMS_APIFail;
            }
        } else { // for a Versioned Descriptor
            // check if given descriptor is a versioned descriptor of current thesaurus
            // if it belongs to MERIMEEDescriptor
            ret = ObjectinClass(descriptorName, thes_descriptor, errorMessage);
            if (ret == TMS_APIFail) {
                //sprintf(errorMessage, translate("%s is not a %s descriptor"), descriptorName, thes_descriptor);
                errorMessage.setValue(String.format("%s is not a %s descriptor", descriptorName.getValue(), thes_descriptor.getValue()));
                return TMS_APIFail;
            }
            // AND if it does NOT belong to MERIMEENewDescriptor
            ret = ObjectinClass(descriptorName, thes_new_descriptor, errorMessage);
            if (ret == TMS_APISucc) {
                //sprintf(errorMessage, translate("%s is a %s descriptor"), descriptorName, thes_new_descriptor);
                errorMessage.setValue(String.format("%s is a %s descriptor", descriptorName.getValue(),thes_new_descriptor.getValue()));
                return TMS_APIFail;
            }
        }
        
        // check if the given link exists in data base and it is one of the links pointing from the given target object
        //if (!IsLinkPointingFromTarget(linkSysid, descriptorName)) {
	//if (IsLinkPointingFromTarget(linkSysidL, descriptorName)!=QClass.TRUEval) {
        if (IsLinkPointingFromTarget(linkSysidL, descriptorName)==QClass.FALSEval) {
            return TMS_APIFail;
	}

        // <editor-fold defaultstate="collapsed" desc="REPLACING CATEGORY CHECKING ALGORITHM - more loose">
        //
        /*
        // check if the category of the given link belongs to the permitted categories for Hierarchies
        // get the categories of the link
        int linkClasses = QC->get_classes(0);
        QC->reset_set(linkClasses);
        l_name fromCateg, categName;
        cm_value toCateg;
        int allowedCategory = 0;
        int i;
             while(QC->return_link(linkClasses, fromCateg, categName, &toCateg)!=-1) {
             if (deletionMode == DELETION_FOR_NEW_DESCRIPTOR) { // for a New Descriptor
                     for(i=0; i<MAX_NEW_DESCRIPTOR_CATEGORIES; i++) {
                     if (!strcmp(fromCateg, NewDescriptorCategories[i][0]) && !strcmp(categName, NewDescriptorCategories[i][1])) {
                             allowedCategory = 1;
                    break;
                      }
                   }
           }
                else { // for a Versioned Descriptor
                     for(i=0; i<MAX_VERSIONED_DESCRIPTOR_CATEGORIES; i++) {
                     if (!strcmp(fromCateg, VersionedDescriptorCategories[i][0]) && !strcmp(categName, VersionedDescriptorCategories[i][1])) {
                             allowedCategory = 1;
                    break;
                      }
                   }
           }
             // free alocated space fro cm_value
                     switch (toCateg.type) {
                             case TYPE_STRING : case TYPE_NODE :
                                     free(toCateg.value.s);
                     }
           if (allowedCategory) break;
        }
        QC->free_set(linkClasses);
             if (!allowedCategory){
                     sprintf(errorMessage, translate("Given link for deletion (%d) is not under one of the categories which are allowed to be used for descriptors.\n"), linkSysid);
           return TMS_APIFail;
             }
             
        */
        // </editor-fold> 
        
        int linkClasses = QC.get_classes(0);
        QC.reset_set(linkClasses);
        StringObject fromCateg = new StringObject();
        StringObject categName= new StringObject();
        Vector<Return_Link_Row> retLvals = new Vector<Return_Link_Row>();
        if(QC.bulk_return_link(linkClasses, retLvals)==QClass.APIFail){
            QC.free_set(linkClasses);
            return TMS_APIFail;
        }        
        QC.free_set(linkClasses);
        int allowedCategory = 0;
        for(Return_Link_Row row: retLvals){
            fromCateg.setValue(row.get_v1_cls());
            categName.setValue(row.get_v2_label());
            //instead of category control
            if(categName.getValue().contains(userOperation.getValue())|| categName.getValue().contains(userOperationLow.getValue())){
                allowedCategory = 1;
                break;
            }
        }        
        if (allowedCategory==0){
            //sprintf(errorMessage, translate("Given link for deletion (%d) is not under one of the categories which are allowed to be used for descriptors.\n"), linkSysid);
            errorMessage.setValue(String.format("Given link for deletion (%d) is not under one of the categories which are allowed to be used for descriptors.\n", linkSysidL));
            return TMS_APIFail;
        }
        
        if (IsLastDepedentLink(descriptorName, linkSysidL, fromCateg, categName)==QClass.TRUEval) {
            //sprintf(errorMessage, translate(IT_IS_NECESSARY_TO_HAVE_LINKS_UNDER_CATEGORY), categName, descriptorName, categName);
            errorMessage.setValue(String.format(IT_IS_NECESSARY_TO_HAVE_LINKS_UNDER_CATEGORY, categName.getValue(),descriptorName.getValue(),categName.getValue()));
            //no return statement existed added one
            return TMS_APIFail;
        }
        
        // check if deleted link is garbage collected =>
        // try to delete the to_value of the deleted link
        int IsGarbageCollected = ATTENTION_LinkIsOfCategory(linkSysidL, new StringObject(INDIVIDUAL), new StringObject(_GARBAGE_COLLECTED)); // gen_queries
        
        //elias addition
        if(IsGarbageCollected==QClass.APIFail){
            return TMS_APIFail;
        }
        
	// Delete the descriptor attribute
	ret = DeleteAttribute(linkSysidL, descriptorName, IsGarbageCollected);
	if (ret==TMS_APIFail) {
            abort_delete_attribute(descriptorName, linkSysidL, errorMessage); 
            return TMS_APIFail;
        }

	commit_delete_attribute(descriptorName, linkSysidL, errorMessage); 
        return TMS_APISucc;
        
    }
    
    /**
     * checks if the given link sysid is pointing from the given object
     * 
     * this function must be called inside a query session
     * 
     * return QClass.FALSEval or QClass.TRUEval
     * 
     * @param linkSysidL
     * @param fromName
     * @return 
     */
    int IsLinkPointingFromTarget(long linkSysidL,StringObject fromName){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
//	tms_api::IsLinkPointingFromTarget()
//---------------------------------------------------------------------
//	INPUT: -	linkSysid : the sysid of the link to be checked
//          - fromName : the object to be checked
//	OUTPUT: - 1 in case the given link sysid is pointing from the given object
//           - 0 otherwise (in this case, errorMessage is filled with the corresponding message)
//	FUNCTION: checks if the given link sysid is pointing from the given object
//   ATTENTION: - this function must be called inside a query session

int tms_api::IsLinkPointingFromTarget(int linkSysid, char *fromName)
{
   // check if the given object exists in data base
   QC->reset_name_scope();
	if (QC->set_current_node(fromName) < 0) {
		sprintf(errorMessage, translate("Given object (%s) does not exist in data base\n"), fromName);
      return 0;
   }
   // get the sysid of the given object
   int objectSysid;
   QC->get_classid(fromName, &objectSysid);
   // check if the given link sysid exists in data base
   QC->reset_name_scope();
	if (QC->set_current_node_id(linkSysid) < 0) {
		sprintf(errorMessage, translate("Given link (%d) does not exist in data base\n"), linkSysid);
      return 0;
   }
   // check if the given link sysid is pointing from the given object
   int from_value_set = QC->get_from_value(0);
	if (from_value_set < 0) {
		sprintf(errorMessage, translate("Failed to get the from value of the given link (%d).\n"), linkSysid);
      QC->free_set(from_value_set);
      return 0;
   }
   QC->reset_set(from_value_set);
   int linkFromSysid;
   l_name node, Sclass;
   QC->return_full_nodes(from_value_set, &linkFromSysid, node, Sclass);
   QC->free_set(from_value_set);
   if (objectSysid != linkFromSysid) {
		sprintf(errorMessage, translate("Given link (%d) is not pointing from the given object (%s)\n"), linkSysid, fromName);
      return 0;
   }
   return 1;
}
        */
        // </editor-fold> 
        // check if the given object exists in data base
        QC.reset_name_scope();
        if (QC.set_current_node(fromName) < 0) {
            //sprintf(errorMessage, translate("Given object (%s) does not exist in data base\n"), fromName);
            errorMessage.setValue(String.format("Given object (%s) does not exist in data base\n", fromName.getValue()));
            //return 0;
            return QClass.FALSEval;            
        }
        // get the sysid of the given object
        PrimitiveObject_Long objectSysidL = new PrimitiveObject_Long();
        QC.get_classid(fromName, objectSysidL);
        // check if the given link sysid exists in data base
        QC.reset_name_scope();
        if (QC.set_current_node_id(linkSysidL) < 0) {
            //sprintf(errorMessage, translate("Given link (%d) does not exist in data base\n"), linkSysid);
            errorMessage.setValue(String.format("Given link (%d) does not exist in data base\n", linkSysidL));
            //return 0;
            return QClass.FALSEval;
        }
        
        // check if the given link sysid is pointing from the given object
        int from_value_set = QC.get_from_value(0);
        if (from_value_set < 0) {
            //sprintf(errorMessage, translate("Failed to get the from value of the given link (%d).\n"), linkSysid);
            errorMessage.setValue(String.format("Failed to get the from value of the given link (%d).\n", linkSysidL));
            QC.free_set(from_value_set);
            //return 0;
            return QClass.FALSEval;
        }
        QC.reset_set(from_value_set);
        
        Vector<Return_Full_Nodes_Row> retFNvals = new Vector<Return_Full_Nodes_Row>();
        
        if(QC.bulk_return_full_nodes(from_value_set, retFNvals)==QClass.APIFail){
            QC.free_set(from_value_set);
            errorMessage.setValue("IsLinkPointingFromTarget failed in QC.bulk_return_full_nodes.\n");
            return QClass.FALSEval;
        }
        QC.free_set(from_value_set);
        //QC->return_full_nodes(from_value_set, &linkFromSysid, node, Sclass);
        for(Return_Full_Nodes_Row row: retFNvals){
            if(objectSysidL.getValue()==row.get_v1_sysid()){
                break;
            }
            else{
                //sprintf(errorMessage, translate("Given link (%d) is not pointing from the given object (%s)\n"), linkSysid, fromName);
                errorMessage.setValue(String.format("Given link (%d) is not pointing from the given object (%s)\n", linkSysidL,fromName.getValue()));
                //return 0;
                return QClass.FALSEval;
            }
        }
        
        //return 1;
        return QClass.TRUEval;
    }
    
    /**
     * checks if the given link is the last depedent one for the given fromName
     * 
     * return QClass.FALSEval or QClass.TRUEval
     * 
     * @return 
     */
    int IsLastDepedentLink(StringObject fromName, long linkSysidL, StringObject fromCateg, StringObject categName){
        
        // check if given link is depedent
        int isDepedent = ATTENTION_LinkIsOfCategory(linkSysidL, new StringObject(INDIVIDUAL), new StringObject(_DEPENDENT));
        //if (!isDepedent) {
        if(isDepedent==QClass.FALSEval){
            //return 0;
            return QClass.FALSEval;
        }
        //elias addition
        if(isDepedent==QClass.APIFail){
            //return 0;
            return QClass.FALSEval;
        }
        // link is depedent. Check if this is the last one under the given category
        QC.reset_name_scope();
        long fromSysidL = QC.set_current_node(fromName);
        if (fromSysidL < 0) {
            //return 0;
            return QClass.FALSEval;
        }
        int linkSet = QC.get_link_from_by_category(0, fromCateg, categName);
        int linkSetCard = QC.set_get_card(linkSet);
        QC.free_set(linkSet);
        if (linkSetCard > 1) {
            //return 0;
            return QClass.FALSEval;
        }
        return QClass.TRUEval;
    }
    
    /**
     * checks if the given link is under the given category
     * 
     * return QClass.TRUEval QClass.FALSEval or  QClass.APIFail !!!
     * @param linkSysidL
     * @param from_categ
     * @param label_categ
     * @return 
     */
    int ATTENTION_LinkIsOfCategory(long linkSysidL, StringObject from_categ, StringObject label_categ){
        // get all classes of target link
	if (QC.reset_name_scope() < 0) {
            //fprintf (stderr, "LinkIsOfCategory: failed to QC->reset_name_scope()\n");
            Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "LinkIsOfCategory: failed to QC->reset_name_scope()\n");
            return QClass.APIFail;
	}
	if (QC.set_current_node_id(linkSysidL) < 0) {
            //fprintf (stderr, "LinkIsOfCategory: failed to QC->set_current_node_id(%d)\n", linkSysidL);
            Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, String.format("LinkIsOfCategory: failed to QC->set_current_node_id(%d)\n", linkSysidL));
            return QClass.APIFail;
	}
        
        int classes_set = QC.get_all_classes(0);
        
        if (classes_set < 0) {
            //fprintf (stderr, "LinkIsOfCategory: failed to QC->get_all_classes()\n");
            Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "LinkIsOfCategory: failed to QC->get_all_classes()\n");
            QC.free_set(classes_set);
            return QClass.APIFail;
	}
        //Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "For link with Id: "+linkSysidL+" cardinality of all classes: "+QC.set_get_card(classes_set));
        //QC.TEST_printIdsOfSet(classes_set);

        // set current node the target category
        if (QC.reset_name_scope() < 0) {
            //fprintf (stderr, "LinkIsOfCategory: failed to QC->reset_name_scope()\n");
            Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "LinkIsOfCategory: failed to QC->reset_name_scope()\n");
            QC.free_set(classes_set);
            return QClass.APIFail;
        }
        
        if (QC.set_current_node(from_categ) < 0) {
            //fprintf (stderr, "LinkIsOfCategory: failed to QC->set_current_node(%s)\n", from_categ);
            Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "LinkIsOfCategory: failed to QC->set_current_node("+from_categ.getValue()+")\n");
            QC.free_set(classes_set);
            return QClass.APIFail;
        }
        
        if (QC.set_current_node(label_categ) < 0) {
            //fprintf (stderr, "LinkIsOfCategory: failed to QC->set_current_node(%s)\n", label_categ);
            Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "LinkIsOfCategory: failed to QC->set_current_node("+label_categ.getValue()+")\n");
            QC.free_set(classes_set);
            return QClass.APIFail;
	}

        // check if target category is member of the set with all classes of target link
        if (QC.reset_set(classes_set) < 0) {
            //fprintf (stderr, "LinkIsOfCategory: failed to QC->reset_set()\n");
            Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "LinkIsOfCategory: failed to QC->reset_set()\n");
            QC.free_set(classes_set);
            return QClass.APIFail;
        }
        
        if (QC.set_member_of(classes_set) < 0) {
            //BUGFIX STARTS
            
            int directClass_set = QC.get_classes( classes_set);
            if (directClass_set  < 0) {
                //fprintf(stderr, "LinkIsOfCategory: failed to QC->get_all_classes()\n");
                Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, String.format("LinkIsOfCategory: failed to QC->get_classes(%d)\n", linkSysidL));
                //QC.free_set( directClass_set);
                QC.free_set(classes_set);
                return QClass.APIFail;
            }
            QC.reset_set( directClass_set);
            
            if (QC.set_member_of(directClass_set) < 0) {
                QC.free_set( directClass_set);
                QC.free_set(classes_set);
                return QClass.FALSEval;
            }
            QC.free_set( directClass_set);
            //int classes_set = QC.get_all_classes(0);
            //BUGFIX ENDS
            
            //QC.free_set(classes_set);
            //return QClass.FALSEval;
        }
        QC.free_set(classes_set);
        
        return QClass.TRUEval;
    }
    
    int DeleteAttribute(long linkSysidL,StringObject objectName, int IsGarbageCollected){
        /*
        // check if link to be deleted is garbage collected
   int toValueSysid;
   if (IsGarbageCollected) { // get the to_value of the link
   	QC->reset_name_scope();
      if (QC->set_current_node_id(linkSysid) < 0) return TMS_APIFail;
      int toValueSet = QC->get_to_value(0);
      QC->reset_set(toValueSet);
      l_name node, Sclass;
      QC->return_full_nodes(toValueSet, &toValueSysid, node, Sclass);
      QC->free_set(toValueSet);
   }

	QC->reset_name_scope();
   int objectSysid = QC->set_current_node(objectName);
   IDENTIFIER I_from(objectSysid);
   IDENTIFIER I_link(linkSysid);

   int ret;
   if (IS_UNNAMED(linkSysid)) { // unnamed attribute
      ret = QC->Delete_Unnamed_Attribute(&I_link);
	} else { // named attribute
      ret = QC->Delete_Named_Attribute(&I_link, &I_from);
   }

   // check if deleted link is garbage collected
   if (IsGarbageCollected) { // try to delete the to_value of the deleted link
   	IDENTIFIER I_toValue(toValueSysid);
      ret = QC->Delete_Node(&I_toValue);
   }

   if(ret == APIFail) return TMS_APIFail;
	return TMS_APISucc;
        */
        
        // check if link to be deleted is garbage collected
        PrimitiveObject_Long toValueSysid = new PrimitiveObject_Long();
        if (IsGarbageCollected==QClass.TRUEval) { // get the to_value of the link
            QC.reset_name_scope();
            if (QC.set_current_node_id(linkSysidL) < 0) {
                return TMS_APIFail;
            }
            int toValueSet = QC.get_to_value(0);
            QC.reset_set(toValueSet);
        
            
            StringObject node = new StringObject();
            StringObject Sclass = new StringObject();
            QC.return_full_nodes(toValueSet, toValueSysid, node, Sclass);
            
            QC.free_set(toValueSet);
        }

	QC.reset_name_scope();
        long objectSysidL = QC.set_current_node(objectName);
        Identifier I_from = new Identifier(objectSysidL);
        Identifier I_link = new Identifier(linkSysidL);
        //IDENTIFIER I_from(objectSysid);
        //IDENTIFIER I_link(linkSysid);
        
        int ret;
        if (QC.CHECK_isUnNamedLink(linkSysidL)) { // unnamed attribute
            ret = QC.CHECK_Delete_Unnamed_Attribute(I_link);
        } else { // named attribute
            ret = QC.CHECK_Delete_Named_Attribute(I_link, I_from);
        }

        // check if deleted link is garbage collected
        if (IsGarbageCollected==QClass.TRUEval && ret!=QClass.APIFail) { // try to delete the to_value of the deleted link
            Identifier I_toValue = new Identifier(toValueSysid.getValue());
            //IDENTIFIER I_toValue(toValueSysid);
            //try to delete if not nothing is wrong
            QC.CHECK_Delete_Node(I_toValue);
            /*
            //BUG FIX IN ORDER TO ENSURE THAT GARBAGE COLLECTED LINK VALUE DOES NOT 
            //HAVE ANY OTHER TO LINKS e.g FROM OTHER THESAURI LIKE DATES (NO THESAURIC PREFIX)
            QC.reset_name_scope();
            QC.set_current_node_id(toValueSysid.getValue());
            int set_links = QC.get_link_to(0);
            QC.reset_set(set_links);
            if(QC.set_get_card(set_links)==0){
                int ret1 = QC.CHECK_Delete_Node( I_toValue);
                if(ret1 == QClass.APIFail){
                    QC.reset_error_message();
                }
            }
            QC.free_set(set_links);
            QC.reset_name_scope();
            */
        }
        if(ret == QClass.APIFail) {
            return TMS_APIFail;
        }
	return TMS_APISucc;
    }
    
    int ThesaurusName(StringObject thesaurus, StringObject thesaurusSortName, StringObject message){
        /*
        
       			tms_api::ThesaurusName()
-------------------------------------------------------------------
	INPUT: - thesaurus, the name of the given thesaurus
   	    - prefix, to be filled with the sort name of the given thesaurus
            e.g. for Thesaurus`MERIMEE sort name = MERIMEE
			 - message, an allocated string which will be filled with
          	an error message in case of an error occurs
	OUTPUT: - TMS_APISucc in case no error query execution happens
           - TMS_APIFail in case an error query execution happens
   FUNCTION: gets the the sort name of the given thesaurus

int tms_api::ThesaurusName(char *thesaurus, char *prefix, char *message)
{
  char	*tmp_prefix;
  int	i;

  tmp_prefix = strchr(thesaurus,'`');
  if (tmp_prefix==NULL){ sprintf(message,IN_THESNAME); return TMS_APIFail; }

  for (i=1;i<strlen(tmp_prefix);i++)
    prefix[i-1] = tmp_prefix[i];

  prefix[i-1] = '\0';

  return TMS_APISucc;
}
        */
        
        if(thesaurus.getValue().contains("`")==false){
            //sprintf(message,IN_THESNAME); return TMS_APIFail;
            message.setValue(IN_THESNAME);
            return TMS_APIFail;
        }
        //prefix.setValue(thesaurus.getValue().split("`")[0] + "`");
        //in a sample c++ program with char thesaurus[] = { 'T','h','e','s','a','u','r','u','s','`','A','N','T','H','R','O', '\0' }; 
        //the value returned was ANTHRO ( this is also the case described in comments)
        thesaurusSortName.setValue(thesaurus.getValue().split("`")[1]);

        return TMS_APISucc;
    }
        
    public int  CHECK_CreateAlternativeTerm(StringObject term){
        int ret = CreateNode(term, CREATE_ALTERNATIVE_TERM);
        return ret;
    }
    
    public int  CHECK_CreateDescriptor(StringObject term, StringObject btterm){
        return CHECK_CreateDescriptor(term, btterm,"");
    }
    
    public int  CHECK_CreateDescriptor(StringObject term, StringObject btterm, String transliteration){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        char   thesaurus[LOGINAM_SIZE];
  char	prefix_thesaurus[MAX_STRING];
  char	prefix_term[MAX_STRING];
  char	thesaurus_descriptor[LOGINAM_SIZE];
  char	thesaurus_hierarchy_term[LOGINAM_SIZE];
  char	thesaurus_obsolete_term[LOGINAM_SIZE];
  char	bt_cat[LOGINAM_SIZE];
  char	hierarchy[BUF][LOGINAM_SIZE];
  int		ret, num_hierarchies;

  ret = GetThesaurus(thesaurus, errorMessage);
  if (ret==TMS_APIFail)  return TMS_APIFail;

  if (term[0]=='\0'){
	sprintf(errorMessage,"%s %s", translate(EMPTY_STRING), translate("Term"));
	return TMS_APIFail;
  }

  if (btterm[0]=='\0'){
	sprintf(errorMessage,"%s %s", EMPTY_STRING, translate("Broader Term"));
   return TMS_APIFail;
  }

  //abort if descriptor already exists
  QC->reset_name_scope();
  int termSySId = QC->set_current_node(term);
  if (termSySId>=0) {
  	strcpy(errorMessage, term);
   strcat(errorMessage, " ");
   strcat(errorMessage, translate(OBJECT_EXISTS));
	//sprintf(message,"%s %s",term,OBJECT_EXISTS);
   return TMS_APIFail;
  }

  //abort if broader term does not exist
  QC->reset_name_scope();
  int bttermSySId = QC->set_current_node(btterm);
  if (bttermSySId<=0) {
  	strcpy(errorMessage, btterm);
   strcat(errorMessage, " ");
   strcat(errorMessage, translate(OBJECT_DOES_NOT_EXIST));
	//sprintf(message,"%s %s",btterm,OBJECT_DOES_NOT_EXIST);
   return TMS_APIFail;
  }

  ret = ThesaurusName(thesaurus,prefix_thesaurus, errorMessage);
  if (ret==TMS_APIFail) //{ abort_transaction(); return TMS_APIFail; }
	  {return TMS_APIFail; }

   // looking for MERIMEEDescriptor
   int card;
   l_name givenClass;
   strcpy(givenClass, userOperation); // MERIMEE
   strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
   ret = GetThesaurusObject(thesaurus_descriptor, NULL, "Descriptor", NULL, givenClass, &card);
   if (ret==TMS_APIFail) return TMS_APIFail;

	sprintf(thesaurus_hierarchy_term,"%s%s",prefix_thesaurus,"HierarchyTerm");

   // looking for MERIMEEObsoleteDescriptor
   ret = GetThesaurusObject(thesaurus_obsolete_term, NULL, "ObsoleteDescriptor", NULL, givenClass, &card);
   if (ret==TMS_APIFail) return TMS_APIFail;

  //abort if btterm is an obsolete descriptor
  ret = ObjectinClass(btterm,thesaurus_obsolete_term, errorMessage);
  if (ret==TMS_APISucc){
   sprintf(errorMessage, translate("%s is an %s obsolete descriptor"), btterm, prefix_thesaurus);
   return TMS_APIFail;
  }

  //abort if btterm is not an aat_hierarchy_term
  ret = ObjectinClass(btterm,thesaurus_hierarchy_term, errorMessage);
  if (ret==TMS_APIFail){
   sprintf(errorMessage, translate("%s is not a %s descriptor"), btterm, prefix_thesaurus);
   return TMS_APIFail;
  }

  //get the hierarchies of the broader term
  num_hierarchies = gethierarchy(hierarchy,btterm,errorMessage);
  if (num_hierarchies<=0) return TMS_APIFail;

  ret = TermPrefixfHierarchy(hierarchy[0],prefix_term,errorMessage);
  if (ret==TMS_APIFail) //{ abort_transaction(); return TMS_APIFail;}
	  {return TMS_APIFail;}

	// abort if descriptor has not the correct prefix
	if (strncmp(prefix_term, term, strlen(prefix_term))) { // "str" does not contain "prefix"
		sprintf(errorMessage, translate("%s has not the correct prefix: %s"), term, prefix_term);
		return TMS_APIFail;
	}
  	// abort if descriptor contains only prefix
   int onlyPrefix = StringContainsOnlyPrefix(prefix_term, term);
	if (onlyPrefix) {
		sprintf(errorMessage, translate("Given descriptor must not be blanck after prefix: %s"), prefix_term);
		return TMS_APIFail;
	}

  IDENTIFIER Iterm;
  strcpy(Iterm.loginam,term);
  Iterm.tag = ID_TYPE_LOGINAM;

  IDENTIFIER Ibtterm;
  strcpy(Ibtterm.loginam,btterm);
  Ibtterm.tag = ID_TYPE_LOGINAM;

  IDENTIFIER Ithesaurus_descriptor;
  strcpy(Ithesaurus_descriptor.loginam,thesaurus_descriptor);
  Ithesaurus_descriptor.tag = ID_TYPE_LOGINAM;

  IDENTIFIER Inew_descriptor;
  // looking for MERIMEENewDescriptor
  strcpy(givenClass, userOperation); // MERIMEE
  strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
  ret = GetThesaurusObject(Inew_descriptor.loginam, NULL, "NewDescriptor", NULL, givenClass, &card);
  if (ret==TMS_APIFail) return TMS_APIFail;
  //strcpy(Inew_descriptor.loginam,"NewDescriptor");
  Inew_descriptor.tag = ID_TYPE_LOGINAM;

  IDENTIFIER Ithesaurus_hierarchy_term;
  strcpy(Ithesaurus_hierarchy_term.loginam,thesaurus_hierarchy_term);
  Ithesaurus_hierarchy_term.tag = ID_TYPE_LOGINAM;

  //retell individual descriptor in Token end
  ret = QC->Add_Node(&Iterm,SIS_API_TOKEN_CLASS);
  if (ret==APIFail) { abort_create(Iterm.loginam,errorMessage); return TMS_APIFail;}

  //retell descriptor in aatdescriptor end
  ret = QC->Add_Instance(&Iterm,&Ithesaurus_descriptor);
  if (ret==APIFail) { abort_create(Iterm.loginam,errorMessage); return TMS_APIFail;}

  int i;
  for (i=0;i<num_hierarchies;i++){

    IDENTIFIER Ihierarchy;
    strcpy(Ihierarchy.loginam,hierarchy[i]);
    Ihierarchy.tag = ID_TYPE_LOGINAM;

    //retell descriptor in hierarchy end
    ret = QC->Add_Instance(&Iterm,&Ihierarchy);
    if (ret==APIFail) { abort_create(Iterm.loginam,errorMessage); return TMS_APIFail;}

  }

  //retell descriptor in newdescriptor end
  ret = QC->Add_Instance(&Iterm,&Inew_descriptor);
  if (ret==APIFail) { abort_create(Iterm.loginam,errorMessage); return TMS_APIFail;}

  //at this point associate the term with its broader term through the
  //"BT" link.

  QC->reset_name_scope();
  termSySId = QC->set_current_node(Iterm.loginam);
  if (termSySId<=0) { abort_create(Iterm.loginam,errorMessage); return TMS_APIFail;}

  QC->reset_name_scope();
  bttermSySId = QC->set_current_node(Ibtterm.loginam);
  if (bttermSySId<=0) { abort_create(Iterm.loginam,errorMessage); return TMS_APIFail;}

  cm_value link_to;
  QC->assign_node(&link_to,Ibtterm.loginam,bttermSySId);

  IDENTIFIER	belongs_to_from_value(termSySId);

   // looking for MERIMEE_BT
   l_name givenClassFrom;
   strcpy(givenClass, userOperation); // MERIMEE
   strcat(givenClass, "_relation"); // MERIMEE_relation
   sprintf(givenClassFrom, "%s%s", userOperation, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
   ret = GetThesaurusObject(bt_cat, "Descriptor", "BT", givenClassFrom, givenClass, &card);
   if (ret==TMS_APIFail) {
      sprintf(errorMessage, translate("Failed to refer to category <%s_BT>"), userOperation);
      return TMS_APIFail;
   }

  int ret_set1 = QC->set_get_new();

  QC->reset_name_scope();
  ret = QC->set_current_node(thesaurus_descriptor);
  //ret = set_current_node(Ithesaurus_hierarchy_term.loginam);
  if (ret<=0) { abort_create(Iterm.loginam,errorMessage); return TMS_APIFail;}

  ret = QC->set_current_node(bt_cat);
  if (ret<=0) { abort_create(Iterm.loginam,errorMessage); return TMS_APIFail;}

  QC->set_put(ret_set1);

  //retell descriptor
  //	with aat_bt
  //		: btterm
  //end
  ret = CreateAttribute(NULL, term, &link_to, -1, ret_set1);
  //ret = EF_Add_Unnamed_xxxx(&belongs_to_from_value,&link_to,ret_set1, errorMessage);
  QC->free_set(ret_set1);
  if (ret==APIFail) { abort_create(Iterm.loginam,errorMessage); return TMS_APIFail;}

  commit_create(Iterm.loginam,errorMessage); return TMS_APISucc;
        */
        // </editor-fold> 
        
        StringObject thesaurus= new StringObject();
        int ret = GetThesaurusName(thesaurus);
        if (ret==TMS_APIFail)  {
            return TMS_APIFail;
        }
        if (term==null || term.getValue()==null || term.getValue().length()==0){
            //sprintf(errorMessage,"%s %s", translate(EMPTY_STRING), translate("Term"));
            errorMessage.setValue(String.format("%s %s", EMPTY_STRING,"Term"));
            return TMS_APIFail;
        }
        
        //if (btterm[0]=='\0'){
        if (btterm==null || btterm.getValue()==null || btterm.getValue().length()==0){
            //sprintf(errorMessage,"%s %s", EMPTY_STRING, translate("Broader Term"));
            errorMessage.setValue(String.format("%s %s", EMPTY_STRING,"Broader Term"));
            return TMS_APIFail;
        }

        //abort if descriptor already exists
        QC.reset_name_scope();
        long termSySIdL = QC.set_current_node(term);
        if (termSySIdL>0) {
            errorMessage.setValue(term.getValue() + " " + OBJECT_EXISTS);
            //strcpy(errorMessage, term);
            //strcat(errorMessage, " ");
            //strcat(errorMessage, translate(OBJECT_EXISTS));
            //sprintf(message,"%s %s",term,OBJECT_EXISTS);
            return TMS_APIFail;
        }

        //abort if broader term does not exist
        QC.reset_name_scope();
        long bttermSySIdL = QC.set_current_node(btterm);
        if (bttermSySIdL<=0) {
            errorMessage.setValue(btterm.getValue() + " " + OBJECT_DOES_NOT_EXIST);
            //strcpy(errorMessage, btterm);
            //strcat(errorMessage, " ");
            //strcat(errorMessage, translate(OBJECT_DOES_NOT_EXIST));
            //sprintf(message,"%s %s",btterm,OBJECT_DOES_NOT_EXIST);
            return TMS_APIFail;
        }
        
        StringObject prefix_thesaurus = new StringObject();
        ret = ThesaurusName(thesaurus,prefix_thesaurus, errorMessage);
        if (ret==TMS_APIFail){
            return TMS_APIFail; 
        }
        
        StringObject thesaurus_hierarchy_term = new StringObject(prefix_thesaurus.getValue()+"HierarchyTerm");
	//sprintf(thesaurus_hierarchy_term,"%s%s",prefix_thesaurus,"HierarchyTerm");
        
        StringObject thesaurus_descriptor = new StringObject();
        IntegerObject card = new IntegerObject();
        StringObject givenClass = new StringObject(userOperation.getValue()+"ThesaurusNotionType");
        ret = GetThesaurusObject(thesaurus_descriptor, null, new StringObject("Descriptor"), null, givenClass, card);
        if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }

        StringObject thesaurus_obsolete_term = new StringObject();
        // looking for MERIMEEObsoleteDescriptor
        ret = GetThesaurusObject(thesaurus_obsolete_term, null, new StringObject("ObsoleteDescriptor"), null, givenClass, card);
        if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }

        //abort if btterm is an obsolete descriptor
        ret = ObjectinClass(btterm,thesaurus_obsolete_term, errorMessage);
        if (ret==TMS_APISucc){
            //sprintf(errorMessage, translate("%s is an %s obsolete descriptor"), btterm, prefix_thesaurus);
            errorMessage.setValue(btterm.getValue() + " is an " + prefix_thesaurus.getValue()+" obsolete descriptor");
            return TMS_APIFail;
        }
        
        //abort if btterm is not an aat_hierarchy_term
        ret = ObjectinClass(btterm,thesaurus_hierarchy_term, errorMessage);
        if (ret==TMS_APIFail){
            //sprintf(errorMessage, translate("%s is not a %s descriptor"), btterm, prefix_thesaurus);
            errorMessage.setValue(btterm.getValue() + " is not a " + prefix_thesaurus.getValue()+" descriptor");
            return TMS_APIFail;
        }

        Vector<StringObject> hierarchy = new Vector<StringObject>();
        //get the hierarchies of the broader term
        int num_hierarchies = gethierarchy(hierarchy,btterm,errorMessage);
        if (num_hierarchies<=0) {
            return TMS_APIFail;
        }
        StringObject prefix_term = new StringObject();
        
        ret = TermPrefixfHierarchy(hierarchy.get(0),prefix_term,errorMessage);
        if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }

	// abort if descriptor has not the correct prefix
	//if (strncmp(prefix_term, term, strlen(prefix_term))) { // "str" does not contain "prefix"
        if(term.getValue().startsWith(prefix_term.getValue())==false){
            //sprintf(errorMessage, translate("%s has not the correct prefix: %s"), term, prefix_term);
            errorMessage.setValue(String.format("%s has not the correct prefix: %s", term.getValue(), prefix_term.getValue()));
            return TMS_APIFail;
	}
  	// abort if descriptor contains only prefix
        //int onlyPrefix = StringContainsOnlyPrefix(prefix_term, term);
	//if (onlyPrefix) {
        if(term.getValue().equals(prefix_term.getValue())){
            //sprintf(errorMessage, translate("Given descriptor must not be blanck after prefix: %s"), prefix_term);
            errorMessage.setValue(String.format("Given descriptor must not be blanck after prefix: %s", prefix_term.getValue()));
            return TMS_APIFail;
	}
        
        Identifier Iterm = new Identifier(term.getValue());
        Identifier Ibtterm = new Identifier(bttermSySIdL);

        Identifier Ithesaurus_descriptor = new Identifier(thesaurus_descriptor.getValue());
        
        StringObject newDescr = new StringObject();
        givenClass.setValue(userOperation.getValue()+"ThesaurusNotionType");
        ret = GetThesaurusObject(newDescr, null, new StringObject("NewDescriptor"), null, givenClass, card);
        if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }
        Identifier Inew_descriptor = new Identifier(newDescr.getValue());
        
        Identifier Ithesaurus_hierarchy_term = new Identifier(thesaurus_hierarchy_term.getValue());

        
        //retell individual descriptor in Token end
        //2 more arguements were added so that an id value for uri construction 
        //and a transliteration property can also be assigned to the descriptor
        ret = QC.CHECK_Add_Node(Iterm,QClass.SIS_API_TOKEN_CLASS,true,transliteration,userOperation.getValue(),true);
        if (ret==QClass.APIFail) { 
            abort_create(term,errorMessage); 
            return TMS_APIFail;
        }
        
        //retell descriptor in aatdescriptor end
        ret = QC.CHECK_Add_Instance(Iterm,Ithesaurus_descriptor);
        if (ret==QClass.APIFail) { 
            abort_create(term,errorMessage); 
            return TMS_APIFail;
        }
        
        int i;
        for (i=0;i<num_hierarchies;i++){
            Identifier Ihierarchy = new Identifier(hierarchy.get(i).getValue());
            
            //retell descriptor in hierarchy end
            ret = QC.CHECK_Add_Instance(Iterm,Ihierarchy);
            if (ret==QClass.APIFail) { 
               abort_create(term,errorMessage); return TMS_APIFail;
            }
        }

        //retell descriptor in newdescriptor end
        ret = QC.CHECK_Add_Instance(Iterm,Inew_descriptor);
        if (ret==QClass.APIFail) { 
            abort_create(term,errorMessage); return TMS_APIFail;
        }
        
        //at this point associate the term with its broader term through the "BT" link.
        
        //not needed we got the id in Iterm
        //QC->reset_name_scope();
        //termSySId = QC->set_current_node(Iterm.loginam);
        //if (termSySId<=0) { abort_create(Iterm.loginam,errorMessage); return TMS_APIFail;}

        //not needed we already got it in bttermSySIdL
        //QC.reset_name_scope();
        //bttermSySId = QC->set_current_node(Ibtterm.loginam);
        //if (bttermSySId<=0) { abort_create(Iterm.loginam,errorMessage); return TMS_APIFail;}

        CMValue link_to = new CMValue();
        link_to.assign_node(btterm.getValue(),bttermSySIdL);
        
        Identifier belongs_to_from_value = new Identifier(Iterm.getSysid());
        //IDENTIFIER	belongs_to_from_value(termSySId);
        
        // looking for MERIMEE_BT
        StringObject givenClassFrom = new StringObject(userOperation.getValue()+"ThesaurusNotionType");
        //sprintf(givenClassFrom, "%s%s", userOperation, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
        givenClass.setValue(userOperation.getValue() + "_relation");
        //strcpy(givenClass, userOperation); // MERIMEE
        //strcat(givenClass, "_relation"); // MERIMEE_relation
        StringObject bt_cat = new StringObject();
        ret = GetThesaurusObject(bt_cat, new StringObject("Descriptor"), new StringObject("BT"), givenClassFrom, givenClass, card);
        if (ret==TMS_APIFail) {
            //sprintf(errorMessage, translate("Failed to refer to category <%s_BT>"), userOperation);
            errorMessage.setValue("Failed to refer to category <"+userOperation.getValue() +"_BT>");
            return TMS_APIFail;
        }
        int ret_set1 = QC.set_get_new();
        QC.reset_name_scope();
        long scnret = QC.set_current_node(thesaurus_descriptor);
        //ret = set_current_node(Ithesaurus_hierarchy_term.loginam);
        if (scnret<=0) { 
            abort_create(term,errorMessage); 
            return TMS_APIFail;
        }
        scnret = QC.set_current_node(bt_cat);
        if (scnret<=0) { 
            abort_create(term,errorMessage); 
            return TMS_APIFail;
        }
        
        QC.set_put(ret_set1);

        //retell descriptor
        //	with aat_bt
        //		: btterm
        //end
        ret = CreateAttribute(null, term, link_to, -1, ret_set1);
        //ret = EF_Add_Unnamed_xxxx(&belongs_to_from_value,&link_to,ret_set1, errorMessage);
        QC.free_set(ret_set1);
        if (ret==QClass.APIFail) { 
            abort_create(term,errorMessage); 
            return TMS_APIFail;
        }
        commit_create(term,errorMessage); 
        return TMS_APISucc;
    }
    
    int TermPrefixfHierarchy(StringObject hierarchy, StringObject term_prefix, StringObject message) {
        
        // get all the classes of the given hierarchy
        QC.reset_name_scope();
        long hierarchySySIdL = QC.set_current_node(hierarchy);
        if (hierarchySySIdL<=0) { 
            //sprintf(message,"%s%s",translate(IN_TERMPRFHIER),hierarchy); 
            message.setValue(IN_TERMPRFHIER+hierarchy.getValue());
            return TMS_APIFail;
        }
        
        int ret_set1 = QC.get_all_classes(0);
        if ((ret_set1==-1) || (QC.set_get_card(ret_set1)==0)){
            //sprintf(message,"%s%s",translate(IN_TERMPRFHIER),hierarchy);
            message.setValue(IN_TERMPRFHIER+hierarchy.getValue());
            QC.free_set(ret_set1); 
            return TMS_APIFail;
        }
        
        // get the links pointing from the above classes
        // and under category ("ThesaurusClassType","of_thesaurus")
        QC.reset_set(ret_set1);
        int ret_set2=QC.get_link_from_by_category(ret_set1,new StringObject("ThesaurusClassType"),new StringObject("of_thesaurus"));
        QC.free_set(ret_set1);
        if ((ret_set2==-1) || (QC.set_get_card(ret_set2)!=1)){
            //sprintf(message,"%s%s",translate(IN_TERMPRFHIER),hierarchy);
            message.setValue(IN_TERMPRFHIER+hierarchy.getValue());
            QC.free_set(ret_set2); 
            return TMS_APIFail;
        }

        // get the to_values of the above links
        QC.reset_set(ret_set2);
        ret_set1 = QC.get_to_value(ret_set2);
        QC.free_set(ret_set2);
        if ((ret_set1==-1) || (QC.set_get_card(ret_set1)==0)){
            //sprintf(message,"%s%s",translate(IN_TERMPRFHIER),hierarchy);
            message.setValue(IN_TERMPRFHIER+hierarchy.getValue());
            QC.free_set(ret_set1); 
            return TMS_APIFail;
        }

        // get the links pointing to the above node
        // and under category ("ThesaurusNotionType","of_thesaurus")
        QC.reset_set(ret_set1);
        ret_set2=QC.get_link_to_by_category(ret_set1,new StringObject("ThesaurusNotionType"), new StringObject("of_thesaurus"));
        QC.free_set(ret_set1);
        
        if ((ret_set2==-1) || (QC.set_get_card(ret_set2)!=1)){
            //sprintf(message,"%s%s",translate(IN_TERMPRFHIER),hierarchy);
            message.setValue(IN_TERMPRFHIER+hierarchy.getValue());
            QC.free_set(ret_set2); 
            return TMS_APIFail;
        }

        // get the from_values of the above links
        QC.reset_set(ret_set2);
        ret_set1 = QC.get_from_value(ret_set2);
        QC.free_set(ret_set2);
        if ((ret_set1==-1) || (QC.set_get_card(ret_set1)==0)){
            //sprintf(message,"%s%s",translate(IN_TERMPRFHIER),hierarchy);
            message.setValue(IN_TERMPRFHIER+hierarchy.getValue());
            QC.free_set(ret_set1); 
            return TMS_APIFail;
        }
        
        // get the links pointing from the above nodes
        // and under category ("ThesaurusNotionType","Notion`UsesAsPrefix")
        QC.reset_set(ret_set1);
        ret_set2 = QC.get_link_from_by_category(ret_set1,new StringObject("ThesaurusNotionType"),new StringObject("Notion`UsesAsPrefix"));
        QC.free_set(ret_set1);
        
        if ((ret_set2==-1) || (QC.set_get_card(ret_set2)!=1)){
            //sprintf(message,"%s%s",translate(IN_TERMPRFHIER),hierarchy);
            message.setValue(IN_TERMPRFHIER+hierarchy.getValue());
            QC.free_set(ret_set2); 
            return TMS_APIFail;
        }

        // get the to_values of the above links
        QC.reset_set(ret_set2);
        ret_set1 = QC.get_to_value(ret_set2);
        QC.free_set(ret_set2);
        if ((ret_set1==-1) || (QC.set_get_card(ret_set1)==0)){
            //sprintf(message,"%s%s",translate(IN_TERMPRFHIER),hierarchy);
            message.setValue(IN_TERMPRFHIER+hierarchy.getValue());
            QC.free_set(ret_set1); 
            return TMS_APIFail;
        }

        // inform term_prefix with above node
        QC.reset_set(ret_set1);
        int ret_val = QC.return_nodes(ret_set1, term_prefix);
        QC.free_set(ret_set1);
        if (ret_val==-1) { 
            //sprintf(message,"%s%s",translate(IN_TERMPRFHIER),hierarchy); 
            message.setValue(IN_TERMPRFHIER+hierarchy.getValue());
            return TMS_APIFail;
        }
        return TMS_APISucc;
    }
    
    int gethierarchy(Vector<StringObject> hierarchy, StringObject term, StringObject message){
        // get in a set all the terms having "BT" connection with the given term (including it)
        QC.reset_name_scope();
        long termSySIdL = QC.set_current_node(term);
        if (termSySIdL<=0) {
            //sprintf(message,"%s%s",translate(IN_GETHIERARCHY),term);
            message.setValue(IN_GETHIERARCHY+term.getValue());
            return TMS_APIFail;
        }

        CategorySet[] categsf = new CategorySet[1];
        CategorySet csobj1 = new CategorySet("Descriptor", "BT", QClass.Traversal_Direction.FORWARD);
        categsf[0] = csobj1;
        //categories_set categsf;
        //strcpy(categsf[0].fcl,"Descriptor");
        //strcpy(categsf[0].cat,"BT");
        //categsf[0].direction = FORWARD;
        //strcpy(categsf[1].fcl,"end");
        //strcpy(categsf[1].cat,"end");
        //categsf[1].direction = 0;

        int ret_categs = QC.set_categories(categsf);
        if (ret_categs==-1){
            //sprintf(message,"%s%s",translate(IN_GETHIERARCHY),term);
            message.setValue(IN_GETHIERARCHY+term.getValue());
            return TMS_APIFail;
        }

        int ret_set1 = QC.get_traverse_by_category(0,QClass.Traversal_Isa.NOISA);
        if (ret_set1==-1){
            //sprintf(message,"%s%s",translate(IN_GETHIERARCHY),term);
            message.setValue(IN_GETHIERARCHY+term.getValue());
            QC.free_set(ret_set1);
            return TMS_APIFail;
        }

        // get the links pointing from the above and under the category ("TopTerm","belongs_to_hierarchy")
        int ret_set2 = QC.get_to_value(ret_set1);
        QC.free_set(ret_set1);
        if (ret_set2==-1) {
            QC.free_set(ret_set2);
            //sprintf(message,"%s%s",translate(IN_GETHIERARCHY),term);
            message.setValue(IN_GETHIERARCHY+term.getValue());
            return TMS_APIFail;
        }

        int ret_set3 = QC.set_get_new();
        QC.set_put(ret_set3);

        //---------------
        QC.reset_set(ret_set2);
        QC.reset_set(ret_set3);

        QC.set_union(ret_set2,ret_set3);
        QC.free_set(ret_set3);

        // get the to_values of the above links
        ret_set1 = QC.get_link_from_by_category(ret_set2,new StringObject("TopTerm"),new StringObject("belongs_to_hierarchy"));
        QC.free_set(ret_set2);
        if ((ret_set1==-1) || (QC.set_get_card(ret_set1)==0)){
            QC.free_set(ret_set1);
            //sprintf(message,"%s%s",translate(IN_GETHIERARCHY),term);
            message.setValue(IN_GETHIERARCHY+term.getValue());
            return TMS_APIFail;
        }

        ret_set3 = QC.get_to_value(ret_set1);
        QC.free_set(ret_set1);
        if ((ret_set3==-1) || (QC.set_get_card(ret_set3)==0)){
            QC.free_set(ret_set3);
            //sprintf(message,"%s%s",translate(IN_GETHIERARCHY),term);
            message.setValue(IN_GETHIERARCHY+term.getValue());
            return TMS_APIFail;
        }
        // getting hierarchies for specified term.

        //l_name label;
        QC.reset_set(ret_set3);
        int retnum = 0;
        Vector<Return_Nodes_Row> retVals = new Vector<Return_Nodes_Row>();
        if(QC.bulk_return_nodes(ret_set3, retVals)==QClass.APIFail){
            message.setValue(IN_GETHIERARCHY+term.getValue());
            return TMS_APIFail;
        }
        for(Return_Nodes_Row row : retVals){
            hierarchy.add(new StringObject(row.get_v1_cls_logicalname()));
            retnum++;
        }
        //while (QC.return_nodes(ret_set3,label) !=-1){
//          strcpy(hierarchy[l],label);
  //        retnum++;
    //    }

        QC.free_set(ret_set3);
        return retnum;
    }
    
    public int  CHECK_CreateEditor(StringObject editor){
        int ret = CreateNode(editor, CREATE_EDITOR);
        return ret;
    }
        
    public int  CHECK_CreateFacet(StringObject facet){
        return CHECK_CreateFacet(facet,"");
    }
    
    public int  CHECK_CreateFacet(StringObject facet, String transliteration){
        
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
   	tms_api::CreateFacet()
----------------------------------------------------------------
INPUT: - facet : a name for a new facet
OUTPUT: - TMS_APISucc in case given facet is created succesfully
        - TMS_APIFail in case:
 A facet with the same name already exists.
· Input FacetName does not contain the correct prefix for a facet of currently used thesaurus.
FUNCTION: creates a new facet.
//----------------------------------------------------------------
int tms_api::CreateFacet(char *facet)
{
  int ret;
  char prefix_thesaurus[MAX_STRING], prefix_class[MAX_STRING];
  char class_facet[LOGINAM_SIZE], thesaurus_class[LOGINAM_SIZE], thesaurus[LOGINAM_SIZE];

  if (facet[0]=='\0'){
    sprintf(errorMessage,"%s %s",translate(EMPTY_STRING),translate("Facet"));
    return TMS_APIFail;
  }

  ret = GetThesaurus(thesaurus,errorMessage);
  if (ret==TMS_APIFail) return TMS_APIFail;

  //abort if facet already exists
  QC->reset_name_scope();
  int facetSySId = QC->set_current_node(facet);
  if (facetSySId>=0) {
    sprintf(errorMessage,"%s %s",facet,translate(OBJECT_EXISTS));
    return TMS_APIFail;
  }

	// get the correct prefixes
	ret = ThesaurusName(thesaurus,prefix_thesaurus,errorMessage);
	if (ret==TMS_APIFail) return TMS_APIFail;

	ret = ClassPrefixfThesaurus(thesaurus,prefix_class,errorMessage);
	if (ret==TMS_APIFail) return TMS_APIFail;

	// abort if facet has not the correct prefix
	if (strncmp(prefix_class, facet, strlen(prefix_class))) {
		sprintf(errorMessage, translate("%s has not the correct prefix: %s"), facet, prefix_class);
		return TMS_APIFail;
	}
  	// abort if facet contains only prefix
   int onlyPrefix = StringContainsOnlyPrefix(prefix_class, facet);
	if (onlyPrefix) {
		sprintf(errorMessage, translate("Given descriptor must not be blanck after prefix: %s"), prefix_class);
		return TMS_APIFail;
	}

   // looking for MERIMEEFacet
   int card;
   l_name givenClass;
   strcpy(givenClass, userOperation); // MERIMEE
   strcat(givenClass, "ThesaurusClassType"); // MERIMEEThesaurusClassType
   ret = GetThesaurusObject(class_facet, NULL, "Facet", NULL, givenClass, &card);
   if (ret==TMS_APIFail) //{ abort_transaction(); return TMS_APIFail;}
	   {return TMS_APIFail;}

   // looking for MERIMEEClass
   ret = GetThesaurusObject(thesaurus_class, NULL, "ThesaurusClass", NULL, givenClass, &card);
   if (ret==TMS_APIFail) return TMS_APIFail;

  IDENTIFIER Ifacet;
  strcpy(Ifacet.loginam,facet);
  Ifacet.tag = ID_TYPE_LOGINAM;

  IDENTIFIER Inew_thesaurus_class;
   // looking for MERIMEENewThesaurusClass
   strcpy(givenClass, userOperation); // MERIMEE
   strcat(givenClass, "ThesaurusClassType"); // MERIMEEThesaurusClassType
   ret = GetThesaurusObject(Inew_thesaurus_class.loginam, NULL, "NewThesaurusClass", NULL, givenClass, &card);
   if (ret==TMS_APIFail) return TMS_APIFail;
  //sprintf(Inew_thesaurus_class.loginam,"%s%s", userOperation, translate("NewThesaurusClass"));
  Inew_thesaurus_class.tag = ID_TYPE_LOGINAM;

  IDENTIFIER Iclass_facet;
  strcpy(Iclass_facet.loginam,class_facet);
  Iclass_facet.tag = ID_TYPE_LOGINAM;

  IDENTIFIER Ithesaurus_class;
  strcpy(Ithesaurus_class.loginam,thesaurus_class);
  Ithesaurus_class.tag = ID_TYPE_LOGINAM;

  //retell individual facet in S_Class end
  ret = QC->Add_Node(&Ifacet,SIS_API_S_CLASS);
  if (ret==APIFail) { abort_create(Ifacet.loginam,errorMessage); return TMS_APIFail; }

  //retell facet in newthesaurusclass end
  ret = QC->Add_Instance(&Ifacet,&Inew_thesaurus_class);
  if (ret==APIFail) { abort_create(Ifacet.loginam,errorMessage); return TMS_APIFail; }

  //retell facet in aatfacet end
  ret = QC->Add_Instance(&Ifacet,&Iclass_facet);
  if (ret==APIFail) { abort_create(Ifacet.loginam,errorMessage); return TMS_APIFail; }

  //retell facet in aatclass end
  ret = QC->Add_Instance(&Ifacet,&Ithesaurus_class);
  if (ret==APIFail) { abort_create(Ifacet.loginam,errorMessage); return TMS_APIFail; }

  commit_create(Ifacet.loginam,errorMessage); return TMS_APISucc;
}
        */
        // </editor-fold> 
        
        int ret;
        StringObject prefix_thesaurus = new StringObject();//not used but makes a check so leave it
        StringObject prefix_class = new StringObject();
        StringObject class_facet = new StringObject();
        StringObject thesaurus_class = new StringObject();
        StringObject thesaurus = new StringObject();
        
        if (facet==null || facet.getValue().length()==0){
            //sprintf(errorMessage,"%s %s",translate(EMPTY_STRING),translate("Facet"));
            errorMessage.setValue(EMPTY_STRING +" Facet");
            return TMS_APIFail;
        }
        
        ret = GetThesaurusName(thesaurus);
        if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }

        //abort if facet already exists
        QC.reset_name_scope();
        long facetSySIdL = QC.set_current_node(facet);
        if (facetSySIdL>0) {
            //sprintf(errorMessage,"%s %s",facet,translate(OBJECT_EXISTS));
            errorMessage.setValue(facet.getValue() +" "+OBJECT_EXISTS);
            return TMS_APIFail;
        }

	// get the correct prefixes
	ret = ThesaurusName(thesaurus,prefix_thesaurus,errorMessage);
	if (ret==TMS_APIFail) {
           return TMS_APIFail;
        }

	ret = ClassPrefixfThesaurus(thesaurus,prefix_class,errorMessage);
	if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }

	// abort if facet has not the correct prefix
	if (facet.getValue().startsWith(prefix_class.getValue())==false) {
            //sprintf(errorMessage, translate("%s has not the correct prefix: %s"), facet, prefix_class);
            errorMessage.setValue(String.format("%s has not the correct prefix: %s", facet.getValue(), prefix_class.getValue()));
            return TMS_APIFail;
	}
  	// abort if facet contains only prefix
        //int onlyPrefix = StringContainsOnlyPrefix(prefix_class, facet);
	if (prefix_class.getValue().equals(facet.getValue())) {
            //sprintf(errorMessage, translate("Given descriptor must not be blanck after prefix: %s"), prefix_class);
            errorMessage.setValue("Given descriptor must not be blanck after prefix: "+prefix_class.getValue());
            return TMS_APIFail;
	}

        // looking for MERIMEEFacet
        IntegerObject card = new IntegerObject(0);
        StringObject givenClass = new StringObject(userOperation.getValue() + "ThesaurusClassType");
        //strcpy(givenClass, userOperation); // MERIMEE
        //strcat(givenClass, "ThesaurusClassType"); // MERIMEEThesaurusClassType
        ret = GetThesaurusObject(class_facet, null, new StringObject("Facet"), null, givenClass, card);
        if (ret==TMS_APIFail){
            return TMS_APIFail;
        }
        // looking for MERIMEEClass
        ret = GetThesaurusObject(thesaurus_class, null, new StringObject("ThesaurusClass"), null, givenClass, card);
        if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }
        
        Identifier Ifacet = new Identifier(facet.getValue());
        //IDENTIFIER Ifacet;
        //strcpy(Ifacet.loginam,facet);
        //Ifacet.tag = ID_TYPE_LOGINAM;
        
        StringObject Inew_thesaurus_classLoginam = new StringObject();
        givenClass.setValue(userOperation.getValue()+"ThesaurusClassType");
        //strcpy(givenClass, userOperation); // MERIMEE
        //strcat(givenClass, "ThesaurusClassType"); // MERIMEEThesaurusClassType
        ret = GetThesaurusObject(Inew_thesaurus_classLoginam, null, new StringObject("NewThesaurusClass"), null, givenClass, card);
        
        
        
        // looking for MERIMEENewThesaurusClass
        if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }
        //sprintf(Inew_thesaurus_class.loginam,"%s%s", userOperation, translate("NewThesaurusClass"));
        Identifier Inew_thesaurus_class = new Identifier(Inew_thesaurus_classLoginam.getValue());
        //IDENTIFIER Inew_thesaurus_class;
        //Inew_thesaurus_class.tag = ID_TYPE_LOGINAM;
        
        Identifier Iclass_facet = new Identifier(class_facet.getValue());
        //strcpy(Iclass_facet.loginam,class_facet);
        //Iclass_facet.tag = ID_TYPE_LOGINAM;
        
        Identifier Ithesaurus_class = new Identifier(thesaurus_class.getValue());
        //IDENTIFIER Ithesaurus_class;
        //strcpy(Ithesaurus_class.loginam,thesaurus_class);
        //Ithesaurus_class.tag = ID_TYPE_LOGINAM;
        
        //retell individual facet in S_Class end
        ret = QC.CHECK_Add_Node(Ifacet,QClass.SIS_API_S_CLASS,true,transliteration,userOperation.getValue(),true);
        if (ret==QClass.APIFail) { 
            abort_create(facet,errorMessage); 
            return TMS_APIFail; 
        }
        
        //retell facet in newthesaurusclass end
        ret = QC.CHECK_Add_Instance(Ifacet,Inew_thesaurus_class);
        if (ret==QClass.APIFail) { 
            abort_create(facet,errorMessage); 
            return TMS_APIFail; 
        }
        
        //retell facet in aatfacet end
        ret = QC.CHECK_Add_Instance(Ifacet,Iclass_facet);
        if (ret==QClass.APIFail) { 
            abort_create(facet,errorMessage); 
            return TMS_APIFail; 
        }

        //retell facet in aatclass end
        ret = QC.CHECK_Add_Instance(Ifacet,Ithesaurus_class);
        if (ret==QClass.APIFail) { 
            abort_create(facet,errorMessage); 
            return TMS_APIFail; 
        }
        
        commit_create(facet,errorMessage); 
        return TMS_APISucc;        
    }
    
    public int  CHECK_CreateHierarchy(StringObject hierarchy, StringObject facet){
        return CHECK_CreateHierarchy(hierarchy,facet,"");
    }
    
    public int  CHECK_CreateHierarchy(StringObject hierarchy, StringObject facet, String transliteration){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
-----------------------------------------------------------------
tms_api::CreateHierarchy()
----------------------------------------------------------------
INPUT: - hierarchy : a name for a new hierarchy
- facet : the name of an existing facet
OUTPUT: - TMS_APISucc in case given hierarchy is created succesfully
- TMS_APIFail in case:
· A hierarchy with the same name already exists.
· facet is not the name of any existing facet of currently
used thesaurus.
· Input hierarchy does not contain the correct prefix for
a hierarchy of currently used thesaurus.
FUNCTION: The hierarchy is added in the knowledge base and classified
in the specified facet. The top term of the hierarchy is
created and appropriately associated with it.
----------------------------------------------------------------
int tms_api::CreateHierarchy(char *hierarchy, char *facet)
{
	char 	prefix_term[MAX_STRING];
	char 	prefix_thesaurus[MAX_STRING];
	char 	prefix_class[MAX_STRING];
	char 	prefix_thesaurus_l[MAX_STRING];
	char	thesaurus[LOGINAM_SIZE];
	char	top_term[LOGINAM_SIZE];
	char	thesaurus_class[LOGINAM_SIZE];
	char	hierarchy_class[LOGINAM_SIZE];
	char	facet_class[LOGINAM_SIZE];
	char 	top_term_class[LOGINAM_SIZE];
	char 	belongs_cat[LOGINAM_SIZE];
	char *tmp;
	int ret;

	if (hierarchy[0]=='\0'){
		sprintf(errorMessage,"%s %s",translate(EMPTY_STRING),translate("Hierarchy")); return TMS_APIFail;
	}

	ret = GetThesaurus(thesaurus,errorMessage);
	if (ret==TMS_APIFail) return TMS_APIFail;

	//abort if hierarchy already exists
	QC->reset_name_scope();
	int hierarchySySId = QC->set_current_node(hierarchy);
	if (hierarchySySId>=0) {
		sprintf(errorMessage,"%s %s",hierarchy,translate(OBJECT_EXISTS));
		return TMS_APIFail;
	}

	ret = ThesaurusName(thesaurus,prefix_thesaurus,errorMessage);
	if (ret==TMS_APIFail) return TMS_APIFail;

	ret = TermPrefixfThesaurus(thesaurus,prefix_term,errorMessage);
	if (ret==TMS_APIFail) return TMS_APIFail;

	ret = ClassPrefixfThesaurus(thesaurus,prefix_class,errorMessage);
	if (ret==TMS_APIFail) return TMS_APIFail;

	int i;
	for (i=0;i<strlen(prefix_thesaurus) && prefix_thesaurus[i] != '\0';i++)
		prefix_thesaurus_l[i] = tolower(prefix_thesaurus[i]);

	prefix_thesaurus_l[i] = '\0';

	// looking for MERIMEEThesaurusClass
	int card;
	l_name givenClass;
	strcpy(givenClass, userOperation); // MERIMEE
	strcat(givenClass, "ThesaurusClassType"); // MERIMEEThesaurusClassType
	ret = GetThesaurusObject(thesaurus_class, NULL, "ThesaurusClass", NULL, givenClass, &card);
	if (ret==TMS_APIFail) return TMS_APIFail;

	// looking for MERIMEEHierarchy
	ret = GetThesaurusObject(hierarchy_class, NULL, "Hierarchy", NULL, givenClass, &card);
	if (ret==TMS_APIFail) return TMS_APIFail;
	// sprintf(hierarchy_class,"%sHierarchy",prefix_thesaurus);

	// looking for MERIMEETopTerm
	strcpy(givenClass, userOperation); // MERIMEE
	strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
	ret = GetThesaurusObject(top_term_class, NULL, "TopTerm", NULL, givenClass, &card);
	if (ret==TMS_APIFail) return TMS_APIFail;

	// looking for MERIMEEFacet
	strcpy(givenClass, userOperation); // MERIMEE
	strcat(givenClass, "ThesaurusClassType"); // MERIMEEThesaurusClassType
	ret = GetThesaurusObject(facet_class, NULL, "Facet", NULL, givenClass, &card);
	if (ret==TMS_APIFail) return TMS_APIFail;

	if (facet[0]=='\0')
		sprintf(facet,"%s%s",prefix_class, "TopFacet");

	// abort if hierarchy has not the correct prefix
	if (strncmp(prefix_class, hierarchy, strlen(prefix_class))) { // "str" does not contain "prefix"
		sprintf(errorMessage, translate("%s has not the correct prefix: %s"), hierarchy, prefix_class);
		return TMS_APIFail;
	}
	// abort if hierarchy contains only prefix
	int onlyPrefix = StringContainsOnlyPrefix(prefix_class, hierarchy);
	if (onlyPrefix) {
		sprintf(errorMessage, translate("Given hierarchy must not be blanck after prefix: %s"), prefix_class);
		return TMS_APIFail;
	}

	// check if given facet exists in DB
	QC->reset_name_scope();
	int facetSySId = QC->set_current_node(facet);
	if (facetSySId<=0) {
		sprintf(errorMessage,"%s %s",facet,translate(OBJECT_DOES_NOT_EXIST));
		return TMS_APIFail;
	}
	//abort if facet is not an instance of aatfacet
	int ret1 = ObjectinClass(facet,facet_class,errorMessage);
	int ret2 = ObjectinClass(facet,hierarchy_class,errorMessage);
	if ((ret1==TMS_APIFail) || (ret2==TMS_APISucc)){
		sprintf(errorMessage,translate("%s is not an instance of %s\n"),facet,facet_class);
		return TMS_APIFail;
	}

	sprintf(top_term,"%s",prefix_term);

	//get top term name from hierarchy name
	int length = strlen(top_term);
	tmp = strchr(hierarchy,'`');
	if (tmp==NULL) { abort_create(hierarchy,errorMessage); return TMS_APIFail; }
	for (i=1;i<strlen(tmp);i++)
		top_term[length+i-1] = tmp[i];

	top_term[length+i-1] = '\0';

	//abort if top term already exists
	QC->reset_name_scope();
	int toptermSySId = QC->set_current_node(top_term);
	if (toptermSySId>=0) {
		sprintf(errorMessage,"%s %s",top_term,translate(OBJECT_EXISTS));
		return TMS_APIFail;
	}

	IDENTIFIER Ifacet; // assign an identifier to the given facet
	strcpy(Ifacet.loginam,facet);
	Ifacet.tag = ID_TYPE_LOGINAM;

	IDENTIFIER Ihierarchy; //assign an identifier to the given hierarchy
	strcpy(Ihierarchy.loginam,hierarchy);
	Ihierarchy.tag = ID_TYPE_LOGINAM;

	IDENTIFIER Itopterm; //assign an identifier to the given top term
	strcpy(Itopterm.loginam,top_term);
	Itopterm.tag = ID_TYPE_LOGINAM;

	IDENTIFIER Inew_thesaurus_class;
	// looking for MERIMEENewThesaurusClass
	strcpy(givenClass, userOperation); // MERIMEE
	strcat(givenClass, "ThesaurusClassType"); // MERIMEEThesaurusClassType
	ret = GetThesaurusObject(Inew_thesaurus_class.loginam, NULL, "NewThesaurusClass", NULL, givenClass, &card);
	if (ret==TMS_APIFail) return TMS_APIFail;
	//strcpy(Inew_thesaurus_class.loginam,"NewThesaurusClass");
	Inew_thesaurus_class.tag = ID_TYPE_LOGINAM;

	IDENTIFIER Ihierarchy_class;
	// looking for MERIMEEHierarchyClass
	// TO_DO: to be taken by GetThesaurusObject()
	
	//strcpy(givenClass, userOperation); // MERIMEE
	//strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
	//ret = GetThesaurusObject(Ihierarchy_class.loginam, NULL, "HierarchyClass", NULL, givenClass, &card);
	//if (ret==TMS_APIFail) { abort_transaction(); return TMS_APIFail;}
	
	l_name Ihierarchy_class_tmp;
	sprintf(Ihierarchy_class_tmp,"%sHierarchyClass", userOperation);
	strcpy(Ihierarchy_class.loginam, translate(Ihierarchy_class_tmp));
	Ihierarchy_class.tag = ID_TYPE_LOGINAM;

	// not needed - Polykarpos
	//IDENTIFIER Irestricted_hierarchy;
	//strcpy(Irestricted_hierarchy.loginam,"RestrictedHierarchy");
	//Irestricted_hierarchy.tag = ID_TYPE_LOGINAM;
	

	IDENTIFIER Inew_descriptor;
	// looking for MERIMEENewDescriptor
	strcpy(givenClass, userOperation); // MERIMEE
	strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
	ret = GetThesaurusObject(Inew_descriptor.loginam, NULL, "NewDescriptor", NULL, givenClass, &card);
	if (ret==TMS_APIFail) return TMS_APIFail;
	//strcpy(Inew_descriptor.loginam,"NewDescriptor");
	Inew_descriptor.tag = ID_TYPE_LOGINAM;

	IDENTIFIER Itop_term_class;
	strcpy(Itop_term_class.loginam,top_term_class);
	Itop_term_class.tag = ID_TYPE_LOGINAM ;

	IDENTIFIER Ithesaurus_hierarchy_class;
	strcpy(Ithesaurus_hierarchy_class.loginam,hierarchy_class);
	Ithesaurus_hierarchy_class.tag = ID_TYPE_LOGINAM;

	IDENTIFIER Ithesaurus_class;
	strcpy(Ithesaurus_class.loginam,thesaurus_class);
	Ithesaurus_class.tag = ID_TYPE_LOGINAM;

	//retell individual hierarchy in S_Class end
	ret = QC->Add_Node(&Ihierarchy,SIS_API_S_CLASS);
	if (ret==APIFail) {abort_create(Ihierarchy.loginam,errorMessage);return TMS_APIFail; }

	//retell hierarchy in aathierarchy end
	ret = QC->Add_Instance(&Ihierarchy,&Ithesaurus_hierarchy_class);
	if (ret==APIFail) { abort_create(Ihierarchy.loginam,errorMessage);return TMS_APIFail;}

	//retell hierarchy in newthesaurusclass end
	ret = QC->Add_Instance(&Ihierarchy,&Inew_thesaurus_class);
	if (ret==APIFail) { abort_create(Ihierarchy.loginam,errorMessage);return TMS_APIFail;}

	//retell hierarchy in aatclass end
	ret = QC->Add_Instance(&Ihierarchy,&Ithesaurus_class);
	if (ret==APIFail) { abort_create(Ihierarchy.loginam,errorMessage);return TMS_APIFail;}

	//retell hierarchy isa hierarchyclass
	ret = QC->Add_IsA(&Ihierarchy,&Ihierarchy_class);
	if (ret==APIFail) { abort_create(Ihierarchy.loginam,errorMessage);return TMS_APIFail;}

	// not needed - Polykarpos
	//retell hierarchy isa restrictedhierarchy end
	//ret = Add_IsA(&Ihierarchy,&Irestricted_hierarchy);
	//if (ret==APIFail) { abort_create(Ihierarchy.loginam,message);return TMS_APIFail;}
	

	//retell hierarchy isa facet end
	ret = QC->Add_IsA(&Ihierarchy,&Ifacet);
	if (ret==APIFail) { abort_create(Ihierarchy.loginam,errorMessage);return TMS_APIFail;}

	//retell individual topterm in Token end
	ret = QC->Add_Node(&Itopterm,SIS_API_TOKEN_CLASS);
	if (ret==APIFail) { abort_create(Ihierarchy.loginam,errorMessage);return TMS_APIFail;}

	//retell topterm in aattopterm end
	ret = QC->Add_Instance(&Itopterm,&Itop_term_class);
	if (ret==APIFail) { abort_create(Ihierarchy.loginam,errorMessage);return TMS_APIFail;}

	//retell topterm in hierarchy end
	ret = QC->Add_Instance(&Itopterm,&Ihierarchy);
	if (ret==APIFail) { abort_create(Ihierarchy.loginam,errorMessage);return TMS_APIFail;}

	//retell topterm in newdescriptor end
	ret = QC->Add_Instance(&Itopterm,&Inew_descriptor);
	if (ret==APIFail) { abort_create(Ihierarchy.loginam,errorMessage);return TMS_APIFail;}

	//at this point associate top term with the hierarchy through the
	//		"belongs_to_aat_hierarchy" link.

	QC->reset_name_scope();
	toptermSySId = QC->set_current_node(Itopterm.loginam);
	if(toptermSySId<=0){abort_create(Ihierarchy.loginam,errorMessage);return TMS_APIFail;}

	QC->reset_name_scope();
	hierarchySySId = QC->set_current_node(Ihierarchy.loginam);
	if (hierarchySySId<=0) {
		abort_create(Ihierarchy.loginam,errorMessage); return TMS_APIFail; }

	cm_value link_to;
	QC->assign_node(&link_to,Ihierarchy.loginam,hierarchySySId);

	IDENTIFIER	belongs_to_from_value(toptermSySId);

	int ret_set1 = QC->set_get_new();

	QC->reset_name_scope();
	l_name belongs_cat_tmp;
	sprintf(belongs_cat_tmp,"belongs_to_%s_hierarchy",prefix_thesaurus_l);
	strcpy(belongs_cat, translate(belongs_cat_tmp));

	ret = QC->set_current_node(Itop_term_class.loginam);
	if (ret<=0) { abort_create(Ihierarchy.loginam,errorMessage);return TMS_APIFail;}

	//TO_DO : to be taken by the rule!!!!
	ret = QC->set_current_node(belongs_cat);
	if (ret<=0) { abort_create(Ihierarchy.loginam,errorMessage);return TMS_APIFail;}

	QC->set_put(ret_set1);

	//retell topterm
	//		with belongs_to_aat_hierarchy
	//			: hierarchy
	//end

	ret = CreateAttribute(NULL, top_term, &link_to, -1, ret_set1);
	//ret = EF_Add_Unnamed_xxx(&belongs_to_from_value,&link_to,ret_set1, errorMessage);
	QC->free_set(ret_set1);
	if (ret==APIFail){ abort_create(Ihierarchy.loginam,errorMessage);return TMS_APIFail;}

	commit_create(Ihierarchy.loginam,errorMessage); return TMS_APISucc;
}


        */
        // </editor-fold> 
        
        int ret;

	if (hierarchy==null || hierarchy.getValue()==null || hierarchy.getValue().length()==0){
            //sprintf(errorMessage,"%s %s",translate(EMPTY_STRING),translate("Hierarchy")); 
            errorMessage.setValue(String.format("%s %s", EMPTY_STRING, "Hierarchy"));
            return TMS_APIFail;            
	}
        
        StringObject thesaurus = new StringObject();
        //StringObject  = new StringObject();
        StringObject prefix_thesaurus = new StringObject();
        StringObject prefix_term = new StringObject();
        StringObject prefix_class = new StringObject();
        StringObject prefix_thesaurus_l = new StringObject();
        
        
	ret = GetThesaurusName(thesaurus);
	if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }

	//abort if hierarchy already exists
	QC.reset_name_scope();
	long hierarchySySIdL = QC.set_current_node(hierarchy);
	if (hierarchySySIdL>=0) {
            //sprintf(errorMessage,"%s %s",hierarchy,translate(OBJECT_EXISTS));
            errorMessage.setValue(String.format("%s %s", hierarchy.getValue(), OBJECT_EXISTS));
            return TMS_APIFail;
        }

	ret = ThesaurusName(thesaurus,prefix_thesaurus,errorMessage);
	if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }

	ret = TermPrefixfThesaurus(thesaurus.getValue(),prefix_term,errorMessage);
	if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }

	ret = ClassPrefixfThesaurus(thesaurus,prefix_class,errorMessage);
	if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }

	//int i;
	//for (i=0;i<strlen(prefix_thesaurus) && prefix_thesaurus[i] != '\0';i++)
		//prefix_thesaurus_l[i] = tolower(prefix_thesaurus[i]);

	//prefix_thesaurus_l[i] = '\0';
        //prefix_thesaurus_l.setValue(prefix_thesaurus.getValue().substring(0, prefix_thesaurus.getValue().length()-1).toLowerCase());
        prefix_thesaurus_l.setValue(prefix_thesaurus.getValue().substring(0, prefix_thesaurus.getValue().length()).toLowerCase());

	// looking for MERIMEEThesaurusClass
	IntegerObject card = new IntegerObject(0);
        StringObject givenClass = new StringObject(userOperation.getValue()+"ThesaurusClassType");
        StringObject thesaurus_class = new StringObject();
        StringObject hierarchy_class = new StringObject();
        StringObject facet_class = new StringObject();
        StringObject top_term_class = new StringObject();
	//l_name givenClass;
	//strcpy(givenClass, userOperation); // MERIMEE
	//strcat(givenClass, "ThesaurusClassType"); // MERIMEEThesaurusClassType
	ret = GetThesaurusObject(thesaurus_class, null, new StringObject("ThesaurusClass"), null, givenClass, card);
	if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }

	// looking for MERIMEEHierarchy
	ret = GetThesaurusObject(hierarchy_class, null, new StringObject("Hierarchy"), null, givenClass, card);
	if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }
	// sprintf(hierarchy_class,"%sHierarchy",prefix_thesaurus);

	// looking for MERIMEETopTerm
	//strcpy(givenClass, userOperation); // MERIMEE
	//strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
        givenClass.setValue(userOperation.getValue()+"ThesaurusNotionType");
	ret = GetThesaurusObject(top_term_class, null, new StringObject("TopTerm"), null, givenClass, card);
	if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }

	// looking for MERIMEEFacet
	//strcpy(givenClass, userOperation); // MERIMEE
	//strcat(givenClass, "ThesaurusClassType"); // MERIMEEThesaurusClassType
        givenClass.setValue(userOperation.getValue()+"ThesaurusClassType");
	ret = GetThesaurusObject(facet_class, null, new StringObject("Facet"), null, givenClass, card);
	if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }

        if (facet==null || facet.getValue()==null || facet.getValue().length()==0){
            //sprintf(facet,"%s%s",prefix_class, "TopFacet");
            if(facet==null){
                facet= new StringObject();
            }
            facet.setValue(prefix_class.getValue()+"TopFacet");
	}
	//if (facet[0]=='\0')
		//sprintf(facet,"%s%s",prefix_class, "TopFacet");

	// abort if hierarchy has not the correct prefix
	if (hierarchy.getValue().startsWith(prefix_class.getValue())==false){
            //strncmp(prefix_class, hierarchy, strlen(prefix_class))) { // "str" does not contain "prefix"
            //sprintf(errorMessage, translate("%s has not the correct prefix: %s"), hierarchy, prefix_class);
            errorMessage.setValue(String.format("%s has not the correct prefix: %s", hierarchy.getValue(), prefix_class.getValue()));
            return TMS_APIFail;
        }
	// abort if hierarchy contains only prefix
	boolean onlyPrefix = prefix_class.getValue().equals(hierarchy.getValue());//StringContainsOnlyPrefix(prefix_class, hierarchy);
	if (onlyPrefix) {
            //sprintf(errorMessage, translate("Given hierarchy must not be blanck after prefix: %s"), prefix_class);
            errorMessage.setValue(String.format("Given hierarchy must not be blanck after prefix: %s", prefix_class.getValue()));
            return TMS_APIFail;
	}

	// check if given facet exists in DB
	QC.reset_name_scope();
	long facetSySIdL = QC.set_current_node(facet);
	if (facetSySIdL<=0) {
            //sprintf(errorMessage,"%s %s",facet,translate(OBJECT_DOES_NOT_EXIST));
            errorMessage.setValue(String.format("%s %s", facet.getValue(),OBJECT_DOES_NOT_EXIST));
            return TMS_APIFail;
        }
	//abort if facet is not an instance of aatfacet
	int ret1 = ObjectinClass(facet,facet_class,errorMessage);
	int ret2 = ObjectinClass(facet,hierarchy_class,errorMessage);
        if ((ret1==TMS_APIFail) || (ret2==TMS_APISucc)){
            //sprintf(errorMessage,translate("%s is not an instance of %s\n"),facet,facet_class);
            errorMessage.setValue(String.format("%s is not an instance of %s\n", facet.getValue(),facet_class.getValue()));
            return TMS_APIFail;
	}

        StringObject top_term = new StringObject(prefix_term.getValue());
	//sprintf(top_term,"%s",prefix_term);

	//get top term name from hierarchy name
	int length = top_term.getValue().length();
	
	if (hierarchy.getValue().contains("`")==false) { 
            
            abort_create(hierarchy,errorMessage); 
            return TMS_APIFail; 
        }
        String tmp = hierarchy.getValue().split("`")[1];
	//for (i=1;i<strlen(tmp);i++)
	//	top_term[length+i-1] = tmp[i];

	//top_term[length+i-1] = '\0';
        top_term.setValue(top_term.getValue()+tmp);

	//abort if top term already exists
	QC.reset_name_scope();
	long toptermSySIdL = QC.set_current_node(top_term);
	if (toptermSySIdL>=0) {
            //sprintf(errorMessage,"%s %s",top_term,translate(OBJECT_EXISTS));
            errorMessage.setValue(String.format("%s %s", top_term.getValue(),OBJECT_EXISTS));
            return TMS_APIFail;
        }

        Identifier Ifacet = new Identifier(facetSySIdL);
	//IDENTIFIER Ifacet; // assign an identifier to the given facet
	//strcpy(Ifacet.loginam,facet);
	//Ifacet.tag = ID_TYPE_LOGINAM;

        Identifier Ihierarchy = new Identifier(hierarchy.getValue());
	//IDENTIFIER Ihierarchy; //assign an identifier to the given hierarchy
	//strcpy(Ihierarchy.loginam,hierarchy);
	//Ihierarchy.tag = ID_TYPE_LOGINAM;

        Identifier Itopterm = new Identifier(top_term.getValue());
	//IDENTIFIER Itopterm; //assign an identifier to the given top term
	//strcpy(Itopterm.loginam,top_term);
	//Itopterm.tag = ID_TYPE_LOGINAM;

        //Identifier Inew_thesaurus_class = new Identifier(top_term.getValue());
	//IDENTIFIER Inew_thesaurus_class;
        givenClass.setValue(userOperation.getValue()+"ThesaurusClassType");
	// looking for MERIMEENewThesaurusClass
	//strcpy(givenClass, userOperation); // MERIMEE
	//strcat(givenClass, "ThesaurusClassType"); // MERIMEEThesaurusClassType
        StringObject Inew_thesaurus_classStrObj = new StringObject();
	ret = GetThesaurusObject(Inew_thesaurus_classStrObj, null, new StringObject("NewThesaurusClass"), null, givenClass, card);
	if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }
        Identifier Inew_thesaurus_class = new Identifier(Inew_thesaurus_classStrObj.getValue());
	//strcpy(Inew_thesaurus_class.loginam,"NewThesaurusClass");
	
        //Inew_thesaurus_class.tag = ID_TYPE_LOGINAM;
        
        
        //IDENTIFIER Ihierarchy_class;
	// looking for MERIMEEHierarchyClass
	// TO_DO: to be taken by GetThesaurusObject()
	/*
	strcpy(givenClass, userOperation); // MERIMEE
	strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
	ret = GetThesaurusObject(Ihierarchy_class.loginam, NULL, "HierarchyClass", NULL, givenClass, &card);
	if (ret==TMS_APIFail) { abort_transaction(); return TMS_APIFail;}
	*/
	//l_name Ihierarchy_class_tmp;
	//sprintf(Ihierarchy_class_tmp,"%sHierarchyClass", userOperation);
	//strcpy(Ihierarchy_class.loginam, translate(Ihierarchy_class_tmp));
        //Ihierarchy_class.tag = ID_TYPE_LOGINAM;
        Identifier Ihierarchy_class = new Identifier(userOperation.getValue()+"HierarchyClass");

	/* not needed - Polykarpos
	IDENTIFIER Irestricted_hierarchy;
	strcpy(Irestricted_hierarchy.loginam,"RestrictedHierarchy");
	Irestricted_hierarchy.tag = ID_TYPE_LOGINAM;
	*/

	//IDENTIFIER Inew_descriptor;
	// looking for MERIMEENewDescriptor
	//strcpy(givenClass, userOperation); // MERIMEE
	//strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
        givenClass.setValue(userOperation.getValue()+"ThesaurusNotionType");
        StringObject Inew_descriptorStrObj = new StringObject();
	ret = GetThesaurusObject(Inew_descriptorStrObj, null, new StringObject("NewDescriptor"), null, givenClass, card);
	if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }
	//strcpy(Inew_descriptor.loginam,"NewDescriptor");
	//Inew_descriptor.tag = ID_TYPE_LOGINAM;
        Identifier Inew_descriptor = new Identifier(Inew_descriptorStrObj.getValue());

        Identifier Itop_term_class = new Identifier(top_term_class.getValue());
	//IDENTIFIER Itop_term_class;
	//strcpy(Itop_term_class.loginam,top_term_class);
	//Itop_term_class.tag = ID_TYPE_LOGINAM ;

        Identifier Ithesaurus_hierarchy_class = new Identifier(hierarchy_class.getValue());
	//IDENTIFIER Ithesaurus_hierarchy_class;
	//strcpy(Ithesaurus_hierarchy_class.loginam,hierarchy_class);
	//Ithesaurus_hierarchy_class.tag = ID_TYPE_LOGINAM;

        Identifier Ithesaurus_class = new Identifier(thesaurus_class.getValue());
	//IDENTIFIER Ithesaurus_class;
	//strcpy(Ithesaurus_class.loginam,thesaurus_class);
	//Ithesaurus_class.tag = ID_TYPE_LOGINAM;

	//retell individual hierarchy in S_Class end
	ret = QC.CHECK_Add_Node(Ihierarchy,QClass.SIS_API_S_CLASS, true,transliteration,userOperation.getValue(),true);
	if (ret==QClass.APIFail) { 
            abort_create(hierarchy,errorMessage);
            return TMS_APIFail;
        }

	//retell hierarchy in aathierarchy end
	ret = QC.CHECK_Add_Instance(Ihierarchy,Ithesaurus_hierarchy_class);
	if (ret==QClass.APIFail) { 
            abort_create(hierarchy,errorMessage);
            return TMS_APIFail;
        }

	//retell hierarchy in newthesaurusclass end
	ret = QC.CHECK_Add_Instance(Ihierarchy,Inew_thesaurus_class);
	if (ret==QClass.APIFail) { 
            abort_create(hierarchy,errorMessage);
            return TMS_APIFail;
        }

	//retell hierarchy in aatclass end
	ret = QC.CHECK_Add_Instance(Ihierarchy,Ithesaurus_class);
	if (ret==QClass.APIFail) { 
            abort_create(hierarchy,errorMessage);
            return TMS_APIFail;
        }

	//retell hierarchy isa hierarchyclass
	ret = QC.CHECK_Add_IsA(Ihierarchy,Ihierarchy_class);
	if (ret==QClass.APIFail) { 
            abort_create(hierarchy,errorMessage);
            return TMS_APIFail;
        }

	/* not needed - Polykarpos
	//retell hierarchy isa restrictedhierarchy end
	ret = Add_IsA(&Ihierarchy,&Irestricted_hierarchy);
	if (ret==APIFail) { abort_create(Ihierarchy.loginam,message);return TMS_APIFail;}
	*/

	//retell hierarchy isa facet end
	ret = QC.CHECK_Add_IsA(Ihierarchy,Ifacet);
	if (ret==QClass.APIFail) { 
            abort_create(hierarchy,errorMessage);
            return TMS_APIFail;
        }

	//retell individual topterm in Token end
	ret = QC.CHECK_Add_Node(Itopterm,QClass.SIS_API_TOKEN_CLASS,true,transliteration,userOperation.getValue(),true);
	if (ret==QClass.APIFail) { 
            abort_create(hierarchy,errorMessage);
            return TMS_APIFail;
        }

	//retell topterm in aattopterm end
	ret = QC.CHECK_Add_Instance(Itopterm,Itop_term_class);
	if (ret==QClass.APIFail) { 
            abort_create(hierarchy,errorMessage);
            return TMS_APIFail;
        }

	//retell topterm in hierarchy end
	ret = QC.CHECK_Add_Instance(Itopterm,Ihierarchy);
	if (ret==QClass.APIFail) { 
            abort_create(hierarchy,errorMessage);
            return TMS_APIFail;
        }

	//retell topterm in newdescriptor end
	ret = QC.CHECK_Add_Instance(Itopterm,Inew_descriptor);
	if (ret==QClass.APIFail) { 
            abort_create(hierarchy,errorMessage);
            return TMS_APIFail;
        }

	//at this point associate top term with the hierarchy through the
	//		"belongs_to_aat_hierarchy" link.

	QC.reset_name_scope();
	toptermSySIdL = QC.set_current_node(top_term);
	if(toptermSySIdL<=0){
            abort_create(hierarchy,errorMessage);
            return TMS_APIFail;
        }

	QC.reset_name_scope();
	hierarchySySIdL = QC.set_current_node(hierarchy);
	if (hierarchySySIdL<=0) {
            abort_create(hierarchy,errorMessage);
            return TMS_APIFail; 
        }

        CMValue link_to = new CMValue();
        link_to.assign_node(hierarchy.getValue(), hierarchySySIdL);
	//cm_value link_to;
	//QC.assign_node(&link_to,Ihierarchy.loginam,hierarchySySId);

        Identifier belongs_to_from_value = new Identifier(toptermSySIdL);
	//IDENTIFIER	belongs_to_from_value(toptermSySId);

	int ret_set1 = QC.set_get_new();

	QC.reset_name_scope();
	//l_name belongs_cat_tmp;
	//sprintf(belongs_cat_tmp,"belongs_to_%s_hierarchy",prefix_thesaurus_l);
	//strcpy(belongs_cat, translate(belongs_cat_tmp));

        StringObject belongs_cat = new StringObject(String.format("belongs_to_%s_hierarchy", prefix_thesaurus_l.getValue()));
	long retL = QC.set_current_node(top_term_class);
	if (retL<=0) { 
            abort_create(hierarchy,errorMessage);
            return TMS_APIFail;
        }

	//TO_DO : to be taken by the rule!!!!
	retL = QC.set_current_node(belongs_cat);
	if (retL<=0) { 
            abort_create(hierarchy,errorMessage);
            return TMS_APIFail;
        }

	QC.set_put(ret_set1);

	//retell topterm
	//		with belongs_to_aat_hierarchy
	//			: hierarchy
	//end

	ret = CreateAttribute(null, top_term, link_to, -1, ret_set1);
	//ret = EF_Add_Unnamed_xxx(&belongs_to_from_value,&link_to,ret_set1, errorMessage);
	QC.free_set(ret_set1);
	if (ret==QClass.APIFail){ 
            abort_create(hierarchy,errorMessage);
            return TMS_APIFail;
        }

	commit_create(hierarchy,errorMessage); 
        return TMS_APISucc;
    }
    
    public int  CHECK_CreateSource(StringObject source){
        int ret = CreateNode(source, CREATE_SOURCE);
        return ret;
    }
    
    public int  CHECK_CreateTranslationWord(StringObject TranslationWord, StringObject TranslationWordClass){
        /*
        // abort if translation category name is invalid
	if (*TranslationWordClass == '\0'){
	   sprintf(errorMessage,"%s%s", translate(EMPTY_STRING), translate("Translation Word Category"));
		return TMS_APIFail;
	}
	//abort if translationCategory does not exists
	QC->reset_name_scope();
	int translationCategory = QC->set_current_node(TranslationWordClass);
	if (translationCategory < 0) {
 		sprintf(errorMessage,"%s%s", translate(INVALID_TRANSLATION_KEYWORD), translate("Translation Word Category"));
		return TMS_APIFail;
	}

   //as in create node
   // abort if node name is empty
	if (*TranslationWord == '\0'){
		sprintf(errorMessage,"%s%s", translate(EMPTY_STRING), translate("Node"));
		return TMS_APIFail;
	}
	//abort if node already exists
	QC->reset_name_scope();
	int nodeSySId = QC->set_current_node(TranslationWord);
	if (nodeSySId >= 0) {
		strcpy(errorMessage, TranslationWord);
		strcat(errorMessage, " ");
		strcat(errorMessage, translate(OBJECT_EXISTS));
		return TMS_APIFail;
	}

	int ret = CreateNodeSubRoutine(TranslationWord, TranslationWordClass);
   return ret;

        */
        // abort if translation category name is invalid
	if (TranslationWordClass == null || TranslationWordClass.getValue() == null || TranslationWordClass.getValue().length() ==0){
            //sprintf(errorMessage,"%s%s", translate(EMPTY_STRING), translate("Translation Word Category"));
            errorMessage.setValue(EMPTY_STRING+"Translation Word Category");
            return TMS_APIFail;
	}
	//abort if translationCategory does not exists
	QC.reset_name_scope();
	long translationCategoryL = QC.set_current_node(TranslationWordClass);
	if (translationCategoryL < 0) {
            //sprintf(errorMessage,"%s%s", translate(INVALID_TRANSLATION_KEYWORD), translate("Translation Word Category"));
            errorMessage.setValue(INVALID_TRANSLATION_KEYWORD+"Translation Word Category");
            return TMS_APIFail;
	}

   //as in create node
   // abort if node name is empty
	if (TranslationWord == null || TranslationWord.getValue() == null || TranslationWord.getValue().length() ==0){
            //sprintf(errorMessage,"%s%s", translate(EMPTY_STRING), translate("Node"));
            errorMessage.setValue(EMPTY_STRING+"Node");
            return TMS_APIFail;
	}
	//abort if node already exists
	QC.reset_name_scope();
	long nodeSySIdL = QC.set_current_node(TranslationWord);
	if (nodeSySIdL != QClass.APIFail) {
            //strcpy(errorMessage, TranslationWord);
            //strcat(errorMessage, " ");
            //strcat(errorMessage, translate(OBJECT_EXISTS));
            errorMessage.setValue(TranslationWord.getValue()+" " + OBJECT_EXISTS);
            return TMS_APIFail;
	}

        int ret = CreateNodeSubRoutine(TranslationWord, TranslationWordClass,false);
        return ret;

    }
    
    public int  CHECK_DeClassifyHierarchyFromFacet(StringObject hierarchyName, StringObject facetName){
        /*
//tms_api::DeClassifyHierarchyFromFacet()
//---------------------------------------------------------------------
//	INPUT: - hierarchyName : the name of a hierarchy to be declassified
//			   facetName : the name of a facet under wich the hierarchy will be declassified
//	OUTPUT: - TMS_APISucc in case the given hierarchy is declassified succesfully
//           - TMS_APIFail otherwise
//	FUNCTION: declassifies the given hierarchy
int tms_api::DeClassifyHierarchyFromFacet(char *hierarchyName, char *facetName)
{
	int ret = Add_Del_ClassifyHierarchyInFacet(hierarchyName, facetName, DEL_CLASSIFY);
   char tmpErrorMessage[512];
	if (ret==APIFail) {
   	abort_declassify(hierarchyName, facetName, tmpErrorMessage);
      strcat(errorMessage, "\n");
      strcat(errorMessage, tmpErrorMessage);
      return TMS_APIFail;
	}

	commit_declassify(hierarchyName, facetName, errorMessage);
   return TMS_APISucc;
}
        */
        
        int ret = Add_Del_ClassifyHierarchyInFacet(hierarchyName.getValue(), facetName.getValue(), DEL_CLASSIFY);
        if (ret==TMS_APIFail) {
            StringObject tmpErrorMessage = new StringObject();
            abort_declassify(hierarchyName, facetName, tmpErrorMessage);
            errorMessage.setValue(errorMessage.getValue()+"\n"+tmpErrorMessage.getValue());
            //strcat(errorMessage, "\n");
            //strcat(errorMessage, tmpErrorMessage);
            return TMS_APIFail;
	}

	commit_declassify(hierarchyName, facetName, errorMessage);
        return TMS_APISucc;
    }
    
    public int  CHECK_DeleteFacet(StringObject facet){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
-------------------------------------------------------------------
tms_api::DeleteFacet()
---------------------------------------------------------------------
INPUT: - facet : the name of a new facet
OUTPUT: - TMS_APISucc in case the given facet is deleted succesfully
- TMS_APIFail in case:
· facet is not the name of any existing new facet
of currently used thesaurus.
FUNCTION: deletes the given facet
ATTENTION: permited only for NEW facets (belong to MERIMEENewThesaurusClass)
----------------------------------------------------------------
int tms_api::DeleteFacet(char *facet)
{
	int	ret;
	char thesaurus_facet[LOGINAM_SIZE];

	// looking for MERIMEEFacet
	int card;
	l_name givenClass;
	strcpy(givenClass, userOperation); // MERIMEE
	strcat(givenClass, "ThesaurusClassType"); // MERIMEEThesaurusClassType
	ret = GetThesaurusObject(thesaurus_facet, NULL, "Facet", NULL, givenClass, &card);
	if (ret==TMS_APIFail) //{ abort_transaction(); return TMS_APIFail;}
	{return TMS_APIFail;}

	// looking for MERIMEENewThesaurusClass
	l_name new_thesaurus_class;
	ret = GetThesaurusObject(new_thesaurus_class, NULL, "NewThesaurusClass", NULL, givenClass, &card);
	if (ret==TMS_APIFail)// { abort_transaction(); return TMS_APIFail;}
	{return TMS_APIFail;}

	// check if given facet exists in data base
	QC->reset_name_scope();
	if (QC->set_current_node(facet) < 0) {
		sprintf(errorMessage, translate("%s does not exist in data base"), facet);
		return TMS_APIFail;
	}

	// check if given facet is a facet of current thesaurus
	// if it belongs to MERIMEEFacet
	ret = ObjectinClass(facet, thesaurus_facet, errorMessage);
	if (ret == TMS_APIFail){
		sprintf(errorMessage,translate("%s is not a %s facet"), facet, userOperation);
		return TMS_APIFail;
	}

	// check if given facet is a NEW facet of current thesaurus
	// if it belongs to MERIMEENewThesaurusClass
	ret = ObjectinClass(facet, new_thesaurus_class, errorMessage);
	if (ret == TMS_APIFail){
		sprintf(errorMessage,translate("%s is not a %s facet"), facet, new_thesaurus_class);
		return TMS_APIFail;
	}
	
	//IDENTIFIER Ifacet; //create identifier for facet.
	//strcpy(Ifacet.loginam,facet);
	//Ifacet.tag = ID_TYPE_LOGINAM;

	// delete the facet
	//ret = Delete_Node(&Ifacet);
	
	// delete the facet
	ret = DeleteNode(facet);
	if (ret==APIFail) {abort_delete(facet, errorMessage); return TMS_APIFail; }

	commit_delete(facet, errorMessage); return TMS_APISucc;        
        */
        // </editor-fold> 
        int	ret;
	StringObject thesaurus_facet= new StringObject();

	// looking for MERIMEEFacet
        StringObject givenClass= new StringObject(userOperation.getValue()+"ThesaurusClassType");
	IntegerObject card = new IntegerObject();
	//l_name givenClass;
	//strcpy(givenClass, userOperation); // MERIMEE
	//strcat(givenClass, "ThesaurusClassType"); // MERIMEEThesaurusClassType
	ret = GetThesaurusObject(thesaurus_facet, null, new StringObject("Facet"), null, givenClass, card);
	if (ret==TMS_APIFail){
            return TMS_APIFail;
        }

	// looking for MERIMEENewThesaurusClass
        StringObject new_thesaurus_class= new StringObject();
	ret = GetThesaurusObject(new_thesaurus_class, null, new StringObject("NewThesaurusClass"), null, givenClass, card);
	if (ret==TMS_APIFail){
            return TMS_APIFail;
        }

	// check if given facet exists in data base
	QC.reset_name_scope();
        long facetIdL = QC.set_current_node(facet);
	if ( facetIdL < 0) {
            //sprintf(errorMessage, translate("%s does not exist in data base"), facet);
            errorMessage.setValue(String.format("%s does not exist in data base", facet.getValue()));
            return TMS_APIFail;
	}

	// check if given facet is a facet of current thesaurus
	// if it belongs to MERIMEEFacet
	ret = ObjectinClass(facet, thesaurus_facet, errorMessage);
	if (ret == TMS_APIFail){
            //sprintf(errorMessage,translate("%s is not a %s facet"), facet, userOperation);
            errorMessage.setValue(String.format("%s is not a %s facet", facet.getValue(),userOperation.getValue()));
            return TMS_APIFail;
	}

	// check if given facet is a NEW facet of current thesaurus
	// if it belongs to MERIMEENewThesaurusClass
	ret = ObjectinClass(facet, new_thesaurus_class, errorMessage);
	if (ret == TMS_APIFail){
            //sprintf(errorMessage,translate("%s is not a %s facet"), facet, new_thesaurus_class);
            errorMessage.setValue(String.format("%s is not a %s facet", facet.getValue(),new_thesaurus_class.getValue()));
            return TMS_APIFail;
	}
	
	//IDENTIFIER Ifacet; //create identifier for facet.
	//strcpy(Ifacet.loginam,facet);
	//Ifacet.tag = ID_TYPE_LOGINAM;

	// delete the facet
	//ret = Delete_Node(&Ifacet);
	
	// delete the facet
	ret = CHECK_DeleteNode(facet);
	if (ret==QClass.APIFail) {
            abort_delete(facet, errorMessage); 
            return TMS_APIFail; 
        }

	commit_delete(facet, errorMessage); 
        return TMS_APISucc;
    }
    public int  CHECK_DeleteHierarchy(StringObject hierarchy){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
-------------------------------------------------------------------
tms_api::DeleteHierarchy()
---------------------------------------------------------------------
INPUT: - ohierarchy : the name of a non released hierarchy
- nhierarchy : the new name for a hierarchy
OUTPUT: - TMS_APISucc in case the given hierarchy is deleted succesfully
- TMS_APIFail in case:
· One or more descriptors are classified in the specific hierarchy.
· HierarchyName is not the name of any existing non released
hierarchy of currently used thesaurus.
· There are links targeting to the top term of the hierarchy
originating from other descriptors.
FUNCTION: deletes the given hierarchy and its top term.
ATTENTION: permitted ONLY for non released hierarchies (belonging to
MERIMEEThesaurusClass and to MERIMEENewThesaurusClass)             
----------------------------------------------------------------
int tms_api::DeleteHierarchy(char *hierarchy)
{
	char 	prefix_thesaurus[MAX_STRING];
	char 	prefix_thesaurus_l[MAX_STRING];
	char 	top_term_class[LOGINAM_SIZE];
	char 	belongs_category[LOGINAM_SIZE];
	int		ret, i;

	// check if given hierarchy exists in data base
	QC->reset_name_scope();
	if (QC->set_current_node(hierarchy) < 0) {
		sprintf(errorMessage, translate("%s does not exist in data base"), hierarchy);
		return TMS_APIFail;
	}

	// check if given hierarchy is a hierarchy of current thesaurus
	// if it belongs to MERIMEEThesaurusClass
	// looking for MERIMEEThesaurusClass
	l_name givenClass;
	strcpy(givenClass, userOperation); // MERIMEE
	strcat(givenClass, "ThesaurusClass"); // MERIMEEThesaurusClass
	ret = ObjectinClass(hierarchy, givenClass, errorMessage);
	if (ret == TMS_APIFail){
		sprintf(errorMessage,translate("%s is not a %s hierarchy"), hierarchy, userOperation);
		return TMS_APIFail;
	}

	// check if given hierarchy is a new hierarchy of current thesaurus
	// if it belongs to MERIMEENewThesaurusClass
	// looking for MERIMEENewThesaurusClass
	int card;
	l_name new_thesaurus_class;
	strcpy(givenClass, userOperation); // MERIMEE
	strcat(givenClass, "ThesaurusClassType"); // MERIMEEThesaurusClassType
	ret = GetThesaurusObject(new_thesaurus_class, NULL, "NewThesaurusClass", NULL, givenClass, &card);
	if (ret==TMS_APIFail) return TMS_APIFail;
	ret = ObjectinClass(hierarchy, new_thesaurus_class, errorMessage);
	if (ret == TMS_APIFail){
		sprintf(errorMessage,translate("%s is not a %s hierarchy"), hierarchy, new_thesaurus_class);
		return TMS_APIFail;
	}

	//abort if hierarchy is instantiated
	QC->reset_name_scope();
	QC->set_current_node(hierarchy);
	int ret_set = QC->get_instances(0);
	if (ret_set==-1) { abort_delete(hierarchy,errorMessage); return TMS_APIFail; }

	if ( QC->set_get_card(ret_set)!=1 ) {
		sprintf(errorMessage,"%s %s",hierarchy,translate(OBJECT_CANNOT_BE_DELETED));
		return TMS_APIFail;
	}

	QC->free_set(ret_set);

	ret = ThesaurusNamefHierarchy(hierarchy,prefix_thesaurus,errorMessage);
	if (ret==TMS_APIFail) return TMS_APIFail;

	for (i=0;i<strlen(prefix_thesaurus) && prefix_thesaurus[i] != '\0';i++)
		prefix_thesaurus_l[i] = tolower(prefix_thesaurus[i]);

	prefix_thesaurus_l[i] = '\0';

	IDENTIFIER Ihierarchy;
	strcpy(Ihierarchy.loginam,hierarchy);
	Ihierarchy.tag = ID_TYPE_LOGINAM ;

	// looking for MERIMEETopTerm
	strcpy(givenClass, userOperation); // MERIMEE
	strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
	ret = GetThesaurusObject(top_term_class, NULL, "TopTerm", NULL, givenClass, &card);
	if (ret==TMS_APIFail) return TMS_APIFail;

	//TO_DO : to be taken by the rule!!!!
	l_name belongs_category_tmp;
	sprintf(belongs_category_tmp,"belongs_to_%s_hierarchy",prefix_thesaurus_l);
	strcpy(belongs_category, translate(belongs_category_tmp));

	QC->reset_name_scope();
	QC->set_current_node(hierarchy);
	int ret_set1 = QC->get_link_to_by_category(0,top_term_class,belongs_category);
	if ( (ret_set1==-1) || (QC->set_get_card(ret_set1) != 1 ) )
	{ abort_delete(Ihierarchy.loginam,errorMessage); return TMS_APIFail; }

	l_name label ;
	int	fromLink;
	cm_value cmv;
	int link_sysid;
	int	traversed;

	QC->reset_set(ret_set1);

	ret = QC->return_link_id(ret_set1,label,&fromLink,&link_sysid,&cmv,&traversed);
	// free allocated space
	switch (cmv.type) {
	case TYPE_STRING : case TYPE_NODE :
		free(cmv.value.s);
	}

	if (ret==-1) { abort_delete(Ihierarchy.loginam,errorMessage); return TMS_APIFail;}

	int ret_set2 = QC->get_from_value(ret_set1);
	QC->free_set(ret_set1);
	if (ret_set2==-1) {abort_delete(Ihierarchy.loginam,errorMessage); return TMS_APIFail;}

	l_name top_term;

	QC->reset_set(ret_set2);
	QC->return_nodes(ret_set2,top_term);
	QC->free_set(ret_set2);
	//if (ret==-1) { abort_delete(Ihierarchy.loginam,message); return TMS_APIFail;}

	IDENTIFIER belongs_link(link_sysid); //assign identifier at link_sysid

	//retell topterm
	//	with belongs_to_aat_hierarchy
	//		attof belongs_to_aat_hierarchy : #
	//end
	ret = QC->Delete_Unnamed_Attribute(&belongs_link);
	if (ret==APIFail) {abort_delete(Ihierarchy.loginam,errorMessage); return TMS_APIFail;}

	QC->reset_name_scope();
	int top_termSySId = QC->set_current_node(top_term);

	int ret_set3 = QC->get_link_from(0); int ret_set4 = QC->get_link_to(0);
	if ((QC->set_get_card(ret_set3)!=0) || (QC->set_get_card(ret_set4)!=0)){

		sprintf(errorMessage,translate("%s has links originating(targeting) from|to it and cannot be deleted\n"),top_term);
		abort_delete(Ihierarchy.loginam,errorMessage); return TMS_APIFail;

	}

	IDENTIFIER Itopterm(top_termSySId);

	//retell topterm # end
	ret = DeleteNode(top_term);
	//ret = Delete_Node(&Itopterm);
	if (ret==APIFail) {abort_delete(Ihierarchy.loginam,errorMessage); return TMS_APIFail;}

	//retell hierarchy # end
	ret = DeleteNode(hierarchy);
	//ret = Delete_Node(&Ihierarchy);
	if (ret==APIFail) {abort_delete(Ihierarchy.loginam,errorMessage); return TMS_APIFail;}

	commit_delete(Ihierarchy.loginam,errorMessage); return TMS_APISucc;        
        */
        // </editor-fold> 
        StringObject prefix_thesaurus = new StringObject();
	StringObject prefix_thesaurus_l = new StringObject();
	StringObject top_term_class = new StringObject();
	StringObject belongs_category = new StringObject();
        //int		ret, i;

	// check if given hierarchy exists in data base
	QC.reset_name_scope();
        long hierIdL = QC.set_current_node(hierarchy);
	if (hierIdL < 0) {
            //sprintf(errorMessage, translate("%s does not exist in data base"), hierarchy);
            errorMessage.setValue(String.format("%s does not exist in data base", hierarchy.getValue()));
            return TMS_APIFail;
	}

	// check if given hierarchy is a hierarchy of current thesaurus
	// if it belongs to MERIMEEThesaurusClass
	// looking for MERIMEEThesaurusClass
        
        StringObject givenClass = new StringObject(userOperation.getValue()+"ThesaurusClass");
	//l_name givenClass;
	//strcpy(givenClass, userOperation); // MERIMEE
	//strcat(givenClass, "ThesaurusClass"); // MERIMEEThesaurusClass
	int ret = ObjectinClass(hierarchy, givenClass, errorMessage);
	if (ret == TMS_APIFail){
            //sprintf(errorMessage,translate("%s is not a %s hierarchy"), hierarchy, userOperation);
            errorMessage.setValue(String.format("%s is not a %s hierarchy", hierarchy.getValue(),userOperation.getValue()));
            return TMS_APIFail;
	}

	// check if given hierarchy is a new hierarchy of current thesaurus
	// if it belongs to MERIMEENewThesaurusClass
	// looking for MERIMEENewThesaurusClass
	IntegerObject card = new IntegerObject(0);
	StringObject new_thesaurus_class = new StringObject();
        givenClass.setValue(userOperation.getValue()+"ThesaurusClassType");
                
	//strcpy(givenClass, userOperation); // MERIMEE
	//strcat(givenClass, "ThesaurusClassType"); // MERIMEEThesaurusClassType
	ret = GetThesaurusObject(new_thesaurus_class, null, new StringObject("NewThesaurusClass"), null, givenClass, card);
	if (ret==TMS_APIFail) return TMS_APIFail;
	ret = ObjectinClass(hierarchy, new_thesaurus_class, errorMessage);
	if (ret == TMS_APIFail){
            //sprintf(errorMessage,translate("%s is not a %s hierarchy"), hierarchy, new_thesaurus_class);
            errorMessage.setValue(String.format("%s is not a %s hierarchy", hierarchy.getValue(),new_thesaurus_class.getValue()));
            return TMS_APIFail;
	}

	//abort if hierarchy is instantiated
	QC.reset_name_scope();
	QC.set_current_node(hierarchy);
	int ret_set = QC.get_instances(0);
	if (ret_set==-1) { 
            abort_delete(hierarchy,errorMessage); 
            return TMS_APIFail; 
        }

	if ( QC.set_get_card(ret_set)!=1 ) {
            //sprintf(errorMessage,"%s %s",hierarchy,translate(OBJECT_CANNOT_BE_DELETED));
            errorMessage.setValue(String.format("%s %s", hierarchy.getValue(),OBJECT_CANNOT_BE_DELETED));
            return TMS_APIFail;
	}

	QC.free_set(ret_set);

	ret = ThesaurusNamefHierarchy(hierarchy,prefix_thesaurus,errorMessage);
	if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }

        if(prefix_thesaurus.getValue().contains("`")==false){
            return TMS_APIFail;
        }
	//for (i=0;i<strlen(prefix_thesaurus) && prefix_thesaurus[i] != '\0';i++)
	//	prefix_thesaurus_l[i] = tolower(prefix_thesaurus[i]);

	//prefix_thesaurus_l[i] = '\0';
        prefix_thesaurus_l.setValue(prefix_thesaurus.getValue().split("`")[1].toLowerCase());
        
        
	Identifier Ihierarchy = new Identifier(hierIdL);
	//strcpy(Ihierarchy.loginam,hierarchy);
	//Ihierarchy.tag = ID_TYPE_LOGINAM ;

	// looking for MERIMEETopTerm
        givenClass.setValue(userOperation.getValue()+"ThesaurusNotionType");
	//strcpy(givenClass, userOperation); // MERIMEE
	//strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
	ret = GetThesaurusObject(top_term_class, null, new StringObject("TopTerm"), null, givenClass, card);
	if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }

	//TO_DO : to be taken by the rule!!!!
	//l_name belongs_category_tmp;
	//sprintf(belongs_category_tmp,"belongs_to_%s_hierarchy",prefix_thesaurus_l);
	//strcpy(belongs_category, translate(belongs_category_tmp));

        belongs_category.setValue(String.format("belongs_to_%s_hierarchy", prefix_thesaurus_l.getValue()));
	QC.reset_name_scope();
	QC.set_current_node(hierarchy);
	int ret_set1 = QC.get_link_to_by_category(0,top_term_class,belongs_category);
	if ( (ret_set1==-1) || (QC.set_get_card(ret_set1) != 1 ) ){ 
            abort_delete(hierarchy,errorMessage); 
            return TMS_APIFail; 
        }

	StringObject label = new StringObject();
	PrimitiveObject_Long fromLinkL = new PrimitiveObject_Long();
	CMValue cmv = new CMValue();
        PrimitiveObject_Long link_sysidL = new PrimitiveObject_Long();
	IntegerObject traversed = new IntegerObject();

	QC.reset_set(ret_set1);

	ret = QC.return_link_id(ret_set1,label,fromLinkL,link_sysidL,cmv,traversed);
	
	if (ret==-1) { 
            abort_delete(hierarchy,errorMessage); 
            return TMS_APIFail;
        }

	int ret_set2 = QC.get_from_value(ret_set1);
	QC.free_set(ret_set1);
	if (ret_set2==-1) {
            abort_delete(hierarchy,errorMessage); 
            return TMS_APIFail;
        }

	StringObject top_term = new StringObject();

	QC.reset_set(ret_set2);
	QC.return_nodes(ret_set2,top_term);
	QC.free_set(ret_set2);
	//if (ret==-1) { abort_delete(Ihierarchy.loginam,message); return TMS_APIFail;}

	Identifier belongs_link = new Identifier(link_sysidL.getValue()); //assign identifier at link_sysid

	//retell topterm
	//	with belongs_to_aat_hierarchy
	//		attof belongs_to_aat_hierarchy : #
	//end
	ret = QC.CHECK_Delete_Unnamed_Attribute(belongs_link);
	if (ret==QClass.APIFail) {
            abort_delete(hierarchy,errorMessage); 
            return TMS_APIFail;
        }

	QC.reset_name_scope();
	long top_termSySIdL = QC.set_current_node(top_term);

	int ret_set3 = QC.get_link_from(0); 
        int ret_set4 = QC.get_link_to(0);
	if ((QC.set_get_card(ret_set3)!=0) || (QC.set_get_card(ret_set4)!=0)){
            //sprintf(errorMessage,translate("%s has links originating(targeting) from|to it and cannot be deleted\n"),top_term);
            errorMessage.setValue(String.format("%s has links originating(targeting) from|to it and cannot be deleted\n", top_term.getValue()));
            abort_delete(hierarchy,errorMessage); 
            return TMS_APIFail;
	}

	Identifier Itopterm = new Identifier(top_termSySIdL);

	//retell topterm # end
	ret = CHECK_DeleteNode(top_term);
	//ret = Delete_Node(&Itopterm);
	if (ret==QClass.APIFail) {
            abort_delete(hierarchy,errorMessage); 
            return TMS_APIFail;
        }
	//retell hierarchy # end
	ret = CHECK_DeleteNode(hierarchy);
	//ret = Delete_Node(&Ihierarchy);
	if (ret==QClass.APIFail) {
            abort_delete(hierarchy,errorMessage); 
            return TMS_APIFail;
        }

	commit_delete(hierarchy,errorMessage); 
        return TMS_APISucc;
    }
    
    /**
     * DeleteNewDescriptor()
     * ---------------------------------------------------------------------
     * INPUT: - descriptorName : the name of a descriptor
     * OUTPUT: - TMS_APISucc in case the given descriptor is deleted succesfully
     * - TMS_APIFail in case:
     * · DescriptorName is not the name of any existing new descriptor of currently used thesaurus.
     *
     * FUNCTION: deletes the given descriptor
     * 
     * ATTENTION: permited only for NEW descriptors (belong to MERIMEENewDescriptor)
     *
     * @param descriptorName
     * @return 
     */
    public int  CHECK_DeleteNewDescriptor(StringObject descriptorName){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
------------------------------------------------------------------
tms_api::DeleteNewDescriptor()
---------------------------------------------------------------------
INPUT: - descriptorName : the name of a descriptor
OUTPUT: - TMS_APISucc in case the given descriptor is deleted succesfully
- TMS_APIFail in case:
· DescriptorName is not the name of any existing new
descriptor of currently used thesaurus.
FUNCTION: deletes the given descriptor
ATTENTION: permited only for NEW descriptors (belong to MERIMEENewDescriptor)
----------------------------------------------------------------
int tms_api::DeleteNewDescriptor(char *descriptorName)
{
	// check if given descriptor exists in data base
	QC->reset_name_scope();
	int descriptorSysid;
	if ((descriptorSysid = QC->set_current_node(descriptorName)) < 0) {
		sprintf(errorMessage, translate("%s does not exist in data base"), descriptorName);
		return TMS_APIFail;
	}

	// looking for MERIMEENewDescriptor
	int card;
	l_name givenClass, thes_new_descriptor;
	strcpy(givenClass, userOperation); // MERIMEE
	strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
	int ret = GetThesaurusObject(thes_new_descriptor, NULL, "NewDescriptor", NULL, givenClass, &card);
	if (ret==TMS_APIFail) {
		sprintf(errorMessage, translate("Failed to refer to class <%s%s>"), userOperation, "NewDescriptor");
		return TMS_APIFail;
	}

	// check if given descriptor is a new descriptor of current thesaurus
	// if it belongs to MERIMEENewDescriptor
	ret = ObjectinClass(descriptorName, thes_new_descriptor, errorMessage);
	if (ret == TMS_APIFail){
		sprintf(errorMessage,translate("%s is not a %s descriptor"), descriptorName, thes_new_descriptor);
		return TMS_APIFail;
	}
	//	IDENTIFIER Idescriptor(descriptorSysid); //create identifier for descriptor.

	// delete the descriptor
	//	ret = Delete_Node(&Idescriptor);
	ret = DeleteNode(descriptorName);
	if (ret==APIFail) {abort_delete(descriptorName, errorMessage); return TMS_APIFail; }

	commit_delete(descriptorName, errorMessage);
	return TMS_APISucc;
}        
        */
                
            
        // </editor-fold> 
                // check if given descriptor exists in data base
        QC.reset_name_scope();
        long descriptorSysidL = QC.set_current_node(descriptorName);
        if (descriptorSysidL < 0) {
            //sprintf(errorMessage, translate("%s does not exist in data base"), descriptorName);
            errorMessage.setValue(String.format("%s does not exist in data base", descriptorName.getValue()));
            return TMS_APIFail;
        }

        // looking for MERIMEENewDescriptor
        IntegerObject card = new IntegerObject(0);
        StringObject thes_new_descriptor= new StringObject();
        StringObject givenClass= new StringObject(userOperation.getValue()+"ThesaurusNotionType");
        //l_name givenClass, thes_new_descriptor;
        //strcpy(givenClass, userOperation); // MERIMEE
        //strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
        int ret = GetThesaurusObject(thes_new_descriptor, null, new StringObject("NewDescriptor"), null, givenClass, card);
        if (ret == TMS_APIFail) {
            //sprintf(errorMessage, translate("Failed to refer to class <%s%s>"), userOperation, "NewDescriptor");
            errorMessage.setValue(String.format("Failed to refer to class <%s%s>", userOperation.getValue(),"NewDescriptor"));
            return TMS_APIFail;
        }

	// check if given descriptor is a new descriptor of current thesaurus
        // if it belongs to MERIMEENewDescriptor
        ret = ObjectinClass(descriptorName, thes_new_descriptor, errorMessage);
        if (ret == TMS_APIFail) {
            //sprintf(errorMessage, translate("%s is not a %s descriptor"), descriptorName, thes_new_descriptor);
            errorMessage.setValue(String.format("%s is not a %s descriptor", descriptorName.getValue(),thes_new_descriptor.getValue()));
            return TMS_APIFail;
        }
	//	IDENTIFIER Idescriptor(descriptorSysid); //create identifier for descriptor.

	// delete the descriptor
        //	ret = Delete_Node(&Idescriptor);
        ret = CHECK_DeleteNode(descriptorName);
        if (ret == QClass.APIFail) {
            abort_delete(descriptorName, errorMessage);
            return TMS_APIFail;
        }

        commit_delete(descriptorName, errorMessage);
        return TMS_APISucc;
    }
    
    int CHECK_DeleteNode(StringObject nodeName){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        -------------------------------------------------------------------
tms_api::DeleteNode()
---------------------------------------------------------------------
INPUT: - nodeName : the name of the object to be deleted
OUTPUT: - TMS_APISucc in case the given object is deleted succesfully
- TMS_APIFail otherwise
FUNCTION: 1. Redirects the attribute links of the given object if necessary
2. Deletes the given object
3. Deletes the links of the given object pointing to primitive values
----------------------------------------------------------------
int tms_api::DeleteNode(char *nodeName)
{
	QC->reset_name_scope();
	int nodeSysid;
	if ((nodeSysid = QC->set_current_node(nodeName)) < 0) {
		sprintf(errorMessage, translate("%s does not exist in data base"), nodeName);
		return TMS_APIFail;
	}
	// EF actions (TO DO??) (see ef\delete_inst.cpp)
	//NO: if (RedirectIsA(nodeName) == TMS_APIFail) return TMS_APIFail;
	//-----------------------------------------------------------------
	//For current TMS-API RedirectIsA() is not needed because it supports
	//only the deletions of: Facets, Hierarchies, Descriptors, Sources
	//and Editors which cannot be IsA related with other subclasses which
	//would need any IsA redirection.
	//-----------------------------------------------------------------
	//YES: if (RedirectAttributeLink(nodeName) == TMS_APIFail) return TMS_APIFail;
	//YES: if (DelLinksOfCategory(_NECESSARY, FROM_NODE) == TMS_APIFail) return TMS_APIFail;
	//YES: if (DelLinksToPrmtv() == TMS_APIFail) return TMS_APIFail;
	
	if (RedirectAttributeLink(nodeName) == TMS_APIFail) return TMS_APIFail;
	if (DelLinksOfCategory(nodeName, _NECESSARY, FROM_NODE) == TMS_APIFail) return TMS_APIFail;
	if (DelLinksToPrmtv(nodeName) == TMS_APIFail) return TMS_APIFail;

	// delete the node
	IDENTIFIER I_name(nodeName);
	if (QC->Delete_Node(&I_name) == APIFail) return TMS_APIFail;
	return TMS_APISucc;
        */
        // </editor-fold> 
        
        QC.reset_name_scope();
	long nodeSysidL = QC.set_current_node(nodeName);
	if (nodeSysidL < 0) {
            //sprintf(errorMessage, translate("%s does not exist in data base"), nodeName);
            errorMessage.setValue(String.format("%s does not exist in data base", nodeName.getValue()));
            return TMS_APIFail;
	}
        
	// EF actions (TO DO??) (see ef\delete_inst.cpp)
	//NO: if (RedirectIsA(nodeName) == TMS_APIFail) return TMS_APIFail;
	//-----------------------------------------------------------------
	//For current TMS-API RedirectIsA() is not needed because it supports
	//only the deletions of: Facets, Hierarchies, Descriptors, Sources
	//and Editors which cannot be IsA related with other subclasses which
	//would need any IsA redirection.
	//-----------------------------------------------------------------
	//YES: if (RedirectAttributeLink(nodeName) == TMS_APIFail) return TMS_APIFail;
	//YES: if (DelLinksOfCategory(_NECESSARY, FROM_NODE) == TMS_APIFail) return TMS_APIFail;
	//YES: if (DelLinksToPrmtv() == TMS_APIFail) return TMS_APIFail;
	
	if (CHECK_RedirectAttributeLink(nodeName) == TMS_APIFail) {
            return TMS_APIFail;
        }
	if (CHECK_DelLinksOfCategory(nodeName, new StringObject(_NECESSARY), DIRECTION.FROM_NODE) == TMS_APIFail) {
            return TMS_APIFail;
        }
	if (CHECK_DelLinksToPrmtv(nodeName) == TMS_APIFail) {
            return TMS_APIFail;
        }

	// delete the node
	//Identifier I_name = new Identifier(nodeName.getValue());
        Identifier I_name = new Identifier(nodeSysidL);
        
	if (QC.CHECK_Delete_Node(I_name) == QClass.APIFail) {
            return TMS_APIFail;
        }
	return TMS_APISucc;
    }
    
    //NOT SURE IT IS TESTED
    int CHECK_DelLinksToPrmtv(StringObject target_name){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        -------------------------------------------------------------------
tms_api::DelLinksToPrmtv()
---------------------------------------------------------------------
INPUT: - target_name : the from-value of the links to be deleted
OUTPUT: - TMS_APISucc in case the specified links are deleted succesfully
- TMS_APIFail otherwise
FUNCTION: deletes the links of target_name pointing to primitive values
----------------------------------------------------------------
int tms_api::DelLinksToPrmtv(char *target_name)
{
	int fromid, link_sysid, sysid, categ_id;
	int flag;
	int card, count;
	int set_id;
	char cls[LOGINAM_SIZE], label[LOGINAM_SIZE];
	char categ_name[LOGINAM_SIZE], from_categ[LOGINAM_SIZE];
	cm_value  cmv;   // Variable that stores values returned by the pqi

	QC->reset_name_scope();
	QC->set_current_node(target_name);
	//SetNewCurrentNode(target_name) ;
	if ((set_id = QC->get_link_from(DEFAULT_NODE)) < 0) return TMS_APIFail;
	if ((card = QC->set_get_card(set_id)) < 0) return TMS_APIFail;
	QC->reset_set(set_id);
	for (count = 0 ; count < card ; count++) {
		if (QC->return_full_link_id(set_id, cls, &fromid, label, &sysid, categ_name, from_categ, &categ_id, &cmv, &flag) < 0) {
			return TMS_APIFail;
		}
		//
		// if type is not (empty or NODE) ==> primitive type
		// then delete the attribute
		//
		if ((cmv.type != TYPE_EMPTY) && (cmv.type != TYPE_NODE)) {
			//
			// This point needs optimization. I can delete an object knowing
			// just its obj_id, which can be found from answerer !!
			//
			//
			//			QC->get_classid(target_name, &target_sysid) ;
			//			target_sysid is the same as fromid
			//
			QC->get_linkid(cls, label, &link_sysid);
			if (DeleteAttribute(link_sysid, target_name, 0) == TMS_APIFail) {
				//if (transaction->DeleteAttributeLink(fromid, NULL, label, link_sysid, &cmv) == ERROR)  {
				// free allocated space
				switch (cmv.type) {
				case TYPE_STRING : case TYPE_NODE :
					free(cmv.value.s);
				}
				return TMS_APIFail;
			}
		}
		// free allocated space
		switch (cmv.type) {
		case TYPE_STRING : case TYPE_NODE :
			free(cmv.value.s);
		}
	}
	return TMS_APISucc;
        */
        // </editor-fold> 
        //int fromid, link_sysid, sysid, categ_id;
	//int flag;
	IntegerObject card = new IntegerObject(0);
        //IntegerObject count = new IntegerObject(0);
        
	//StringObject cls = new StringObject();
        //StringObject label = new StringObject();
        //StringObject categ_name = new StringObject();
        //StringObject from_categ = new StringObject();
	
	  // Variable that stores values returned by the pqi

	QC.reset_name_scope();
	QC.set_current_node(target_name);
	//SetNewCurrentNode(target_name) ;
        int set_id = QC.get_link_from(0);
	if (set_id< 0) {
            return TMS_APIFail;
        }
        card.setValue(QC.set_get_card(set_id));
	if (card.getValue() < 0) {
            return TMS_APIFail;
        }
	QC.reset_set(set_id);
        Vector<Return_Link_Row> rlVals = new Vector<Return_Link_Row>();
        if(QC.bulk_return_link(set_id, rlVals)==QClass.APIFail){
            return TMS_APIFail;
        }
        
        for(Return_Link_Row row: rlVals){
            long linkIdL = row.get_Neo4j_NodeId();
            //cls.setValue(row.get_v1_cls());
            //label.setValue(row.get_v2_label());
            CMValue  cmv = row.get_v3_cmv();
            if(cmv.getType()!=CMValue.TYPE_EMPTY && cmv.getType()!= CMValue.TYPE_NODE){
                if (DeleteAttribute(linkIdL, target_name, 0) == TMS_APIFail) {
                    return TMS_APIFail;
                }
                //
                // This point needs optimization. I can delete an object knowing
                // just its obj_id, which can be found from answerer !!
                //
                //
                //			QC.get_classid(target_name, &target_sysid) ;
                //			target_sysid is the same as fromid
                //
                //QC.get_linkid(cls, label, &link_sysid);
                //if (DeleteAttribute(link_sysid, target_name, 0) == TMS_APIFail) {
                        //if (transaction.DeleteAttributeLink(fromid, NULL, label, link_sysid, &cmv) == ERROR)  {
                        // free allocated space
                  //      switch (cmv.type) {
                    //    case TYPE_STRING : case TYPE_NODE :
                      //          free(cmv.value.s);
                        //}
                        //return TMS_APIFail;
            }
        }
        /*
	for (count = 0 ; count < card ; count++) {
		if (QC.return_full_link_id(set_id, cls, &fromid, label, &sysid, categ_name, from_categ, &categ_id, &cmv, &flag) < 0) {
			return TMS_APIFail;
		}
		//
		// if type is not (empty or NODE) ==> primitive type
		// then delete the attribute
		//
		if ((cmv.type != TYPE_EMPTY) && (cmv.type != TYPE_NODE)) {
			//
			// This point needs optimization. I can delete an object knowing
			// just its obj_id, which can be found from answerer !!
			//
			//
			//			QC.get_classid(target_name, &target_sysid) ;
			//			target_sysid is the same as fromid
			//
			QC.get_linkid(cls, label, &link_sysid);
			if (DeleteAttribute(link_sysid, target_name, 0) == TMS_APIFail) {
				//if (transaction.DeleteAttributeLink(fromid, NULL, label, link_sysid, &cmv) == ERROR)  {
				// free allocated space
				switch (cmv.type) {
				case TYPE_STRING : case TYPE_NODE :
					free(cmv.value.s);
				}
				return TMS_APIFail;
			}
		}
		// free allocated space
		switch (cmv.type) {
		case TYPE_STRING : case TYPE_NODE :
			free(cmv.value.s);
		}
	}
        */
        
	return TMS_APISucc;
        
    }
    //NOT SURE IT IS TESTED
    int CHECK_DelLinksOfCategory(StringObject target_name,StringObject category, DIRECTION direction){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
       -------------------------------------------------------------------
tms_api::DelLinksOfCategory()
---------------------------------------------------------------------
INPUT: - target_name : the from-value of the links to be deleted
- category : the specified category pointed from INDIVIDUAL
- direction : FROM_NODE or TO_NODE
OUTPUT: - TMS_APISucc in case the specified links are deleted succesfully
- TMS_APIFail otherwise
FUNCTION: deletes the links from target_name, under metacategory:
(INDIVIDUAL, category)
----------------------------------------------------------------
int tms_api::DelLinksOfCategory(char *target_name, char *category, DIRECTION direction)
{
	int set_id, neces_set;
	int FoundCategory;

	FoundCategory = 1;
	QC->reset_name_scope();
	QC->set_current_node(INDIVIDUAL);
	// SetNewCurrentNode(INDIVIDUAL);
	if (QC->set_current_node(category) < 0) {
		// OK There is no category (Individual, $category))
		QC->reset_error_message();
		FoundCategory = 0;
	}
	if (FoundCategory) {
		QC->reset_name_scope();
		QC->set_current_node(target_name);
		if (direction == FROM_NODE) {
			set_id = QC->get_link_from_by_meta_category(DEFAULT_NODE, INDIVIDUAL, category) ;
		} else if (direction == TO_NODE) {
			set_id = QC->get_link_to_by_meta_category(DEFAULT_NODE, INDIVIDUAL, category) ;
		}
		if (set_id < 0) return TMS_APIFail;
		QC->reset_name_scope();
		QC->set_current_node(INDIVIDUAL);
		//SetNewCurrentNode(INDIVIDUAL);
		QC->set_current_node(category);
		neces_set = QC->get_instances(DEFAULT_NODE);
		//
		// neces_set contains all the categories,
		// which are instances Of (Individual, $category).
		// set_id contains the link from or to target_name,
		// which are under metacategory (Individual, $category)
		//
		if (DeleteLinks(target_name, set_id, neces_set) == TMS_APIFail) {
			QC->free_set(set_id);
			QC->free_set(neces_set);
			return TMS_APIFail;
		}
		QC->free_set(set_id);
		QC->free_set(neces_set);
	}
	return TMS_APISucc;
        */
        // </editor-fold> 
        int set_id = TMS_APIFail;
        int neces_set = TMS_APIFail;
	boolean FoundCategory = true;

        QC.reset_name_scope();
        QC.set_current_node(new StringObject(INDIVIDUAL));
        // SetNewCurrentNode(INDIVIDUAL);
        if (QC.set_current_node(category) < 0) {
            // OK There is no category (Individual, $category))
            QC.reset_error_message();
            FoundCategory = false;
        }
        if (FoundCategory) {
            QC.reset_name_scope();
            QC.set_current_node(target_name);
            if (direction == DIRECTION.FROM_NODE) {
                set_id = QC.get_link_from_by_meta_category(0, new StringObject(INDIVIDUAL), category);
            } else if (direction == DIRECTION.TO_NODE) {
                set_id = QC.get_link_to_by_meta_category(0, new StringObject(INDIVIDUAL), category);
            }
            if (set_id < 0) {
                return TMS_APIFail;
            }
            QC.reset_name_scope();
            QC.set_current_node(new StringObject(INDIVIDUAL));
            //SetNewCurrentNode(INDIVIDUAL);
            QC.set_current_node(category);
            neces_set = QC.get_instances(0);
            //
            // neces_set contains all the categories,
            // which are instances Of (Individual, $category).
            // set_id contains the link from or to target_name,
            // which are under metacategory (Individual, $category)
            //
            if (DeleteLinks(target_name, set_id, neces_set) == TMS_APIFail) {
                QC.free_set(set_id);
                QC.free_set(neces_set);
                return TMS_APIFail;
            }
            QC.free_set(set_id);
            QC.free_set(neces_set);
        }
        return TMS_APISucc;

    }
    
    //NOT SURE IT IS TESTED
    int DeleteLinks(StringObject target_name, int set_id, int neces_set){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        -------------------------------------------------------------------
tms_api::DeleteLinks()
---------------------------------------------------------------------
INPUT: - target_name : the from-value of the links to be deleted
- set_id : the links to be deleted. It contains links <from>
or <to> target_name, under	categories (INDIVIDUAL, _NECESSARY)
or (INDIVIDUAL, _INVERSE_NECESSARY) respectively.
- neces_set : a set of links to filter set_id. It contains all
the categories, which are instances Of (INDIVIDUAL, _NECESSARY)
or (INDIVIDUAL, _INVERSE_NECESSARY) respectively.
OUTPUT: - TMS_APISucc in case the specified links are deleted succesfully
- TMS_APIFail otherwise
FUNCTION: deletes the links of set_id, found in the instances of neces_set.
----------------------------------------------------------------
int tms_api::DeleteLinks(char *target_name, int set_id, int neces_set)
{
	int fromid, flag;
	int target_sysid, link_sysid, categ_id;
	char cls[LOGINAM_SIZE], label[LOGINAM_SIZE];
	int class_set;
	cm_value  cmv, cmv1;   // Variable that stores values returned by the pqi

	if ((class_set = QC->get_classes(set_id)) < 0) return TMS_APIFail;
	QC->set_intersect(class_set, neces_set);
	// now class_set contains the classes of the links from target_name
	// which are members of (INDIVIDUAL, _NECESSARY)
	int emptySet = (QC->set_get_card(class_set) > 0) ? 0 : 1;
	if (emptySet) {
		//if (IsEmptySet(class_set, NULL) == EF_TRUE) {
		QC->free_set(class_set) ;
		return TMS_APISucc;
	}
	QC->reset_set(set_id);
	while (QC->return_link(set_id, cls, label, &cmv) >= 0) {
		// if QC->return_link == ERROR,  no link under meta_category
		// (INDIVIDUAL, category)
		QC->reset_set(class_set);
		while (QC->return_link_id(class_set, cls, &fromid, &categ_id, &cmv1, &flag) >= 0) {
			// free allocated space
			switch (cmv1.type) {
			case TYPE_STRING : case TYPE_NODE :
				free(cmv1.value.s);
			}
			// This point needs optimization. I can delete an object knowing
			// just its obj_id, which can be found from answerer !!
			QC->get_classid(target_name, &target_sysid);
			QC->get_linkid(target_name, label, &link_sysid);
			if (DeleteAttribute(link_sysid, target_name, 0) == TMS_APIFail) {
				//if (transaction->DeleteAttributeLink(target_sysid, NULL, label, link_sysid, &cmv) == ERROR)  {
				// free allocated space
				switch (cmv.type) {
				case TYPE_STRING : case TYPE_NODE :
					free(cmv.value.s);
				}
				return TMS_APIFail;
			}
		}
		// free allocated space
		switch (cmv.type) {
		case TYPE_STRING : case TYPE_NODE :
			free(cmv.value.s);
		}
	}
	QC->free_set(class_set);
	return TMS_APISucc;
        */
        // </editor-fold> 
 
	//int fromid, flag;
	//int target_sysid, link_sysid, categ_id;
	//char cls[LOGINAM_SIZE], label[LOGINAM_SIZE];
	//int class_set;
	//cm_value  cmv, cmv1;   // Variable that stores values returned by the pqi
        int class_set = QC.get_classes(set_id);
	if (class_set < 0) {
            return TMS_APIFail;
        }
	QC.set_intersect(class_set, neces_set);
	// now class_set contains the classes of the links from target_name
	// which are members of (INDIVIDUAL, _NECESSARY)
	boolean emptySet = (QC.set_get_card(class_set) > 0) ? false : true;
	if (emptySet) {
            //if (IsEmptySet(class_set, NULL) == EF_TRUE) {
            QC.free_set(class_set) ;
            return TMS_APISucc;
	}
	QC.reset_set(set_id);
        
        Vector<Return_Link_Row> retVals = new Vector<Return_Link_Row>();
        QC.bulk_return_link(set_id, retVals);
        
        for(Return_Link_Row row: retVals){
            
            
            long link_sysidL = row.get_Neo4j_NodeId();
            if (DeleteAttribute(link_sysidL, target_name, 0) == TMS_APIFail) {
                //if (transaction->DeleteAttributeLink(target_sysid, NULL, label, link_sysid, &cmv) == ERROR)  {
                
                return TMS_APIFail;
            }
        }
        
        /* no meaning!!! why double loop ???? since all we need is if (DeleteAttribute(link_sysid, target_name, 0) == TMS_APIFail) {
        while (QC->return_link(set_id, cls, label, &cmv) >= 0) {
		// if QC->return_link == ERROR,  no link under meta_category
		// (INDIVIDUAL, category)
		QC->reset_set(class_set);
		while (QC->return_link_id(class_set, cls, &fromid, &categ_id, &cmv1, &flag) >= 0) {
			// free allocated space
			switch (cmv1.type) {
			case TYPE_STRING : case TYPE_NODE :
				free(cmv1.value.s);
			}
			// This point needs optimization. I can delete an object knowing
			// just its obj_id, which can be found from answerer !!
			QC->get_classid(target_name, &target_sysid);
			QC->get_linkid(target_name, label, &link_sysid);
			if (DeleteAttribute(link_sysid, target_name, 0) == TMS_APIFail) {
				//if (transaction->DeleteAttributeLink(target_sysid, NULL, label, link_sysid, &cmv) == ERROR)  {
				// free allocated space
				switch (cmv.type) {
				case TYPE_STRING : case TYPE_NODE :
					free(cmv.value.s);
				}
				return TMS_APIFail;
			}
		}
		// free allocated space
		switch (cmv.type) {
		case TYPE_STRING : case TYPE_NODE :
			free(cmv.value.s);
		}
	}
        */
	QC.free_set(class_set);
	return TMS_APISucc;
    }
    
    //NOT SURE IT IS TESTED PROBABLY NEVER ACTUALLY USED
    int CHECK_RedirectAttributeLink(StringObject target_name){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        ------------------------------------------------------------------
tms_api::RedirectAttributeLink()
---------------------------------------------------------------------
INPUT: - target_name : the name of an object
OUTPUT: - TMS_APISucc in case the given object's attribute links
are redirected succesfully
- TMS_APIFail otherwise
FUNCTION: Redirects the attribute links of the given object if necessary
----------------------------------------------------------------
int tms_api::RedirectAttributeLink(char *target_name)
{
	int all_classes ;
	int to_categories, from_categories ;
	int FoundInterCon ; // flag set if category (Individual, interconnected) is found in the DB.

	FoundInterCon = 1;
	QC->reset_name_scope();
	QC->set_current_node(INDIVIDUAL);
	//SetNewCurrentNode(INDIVIDUAL);
	if (QC->set_current_node(_INTERCONNECTED) < 0) {
		// OK There is no category (Individual, interconnected))
		QC->reset_error_message();
		FoundInterCon = 0;
	}

	if (FoundInterCon) {
		QC->reset_name_scope();
		if (QC->set_current_node(target_name) < 0) return TMS_APIFail;
		if ((all_classes = QC->get_all_classes(DEFAULT_NODE)) < 0) return TMS_APIFail;
		if ((to_categories = QC->get_link_to_by_category(all_classes, INDIVIDUAL, _INTERCONNECTED)) < 0) return TMS_APIFail;
		if ((from_categories = QC->get_link_from_by_category(all_classes, INDIVIDUAL, _INTERCONNECTED)) < 0) return TMS_APIFail;
		if (QC->set_intersect(to_categories, from_categories) < 0) return TMS_APIFail;
		int emptySet = (QC->set_get_card(to_categories) > 0) ? 0 : 1;
		if (!emptySet) {
			//if (IsEmptySet(to_categories, NULL) == EF_FALSE) {
			// Maybe I can redirect something ...
			if (RedirectAttributeLink(target_name, to_categories) == TMS_APIFail) return TMS_APIFail;
			
			//if (redirect != (RedirectClass*) NULL) {
			//delete redirect ;
			//redirect = NULL ;
			//}
			//redirect = new RedirectClass(this, transaction) ;
			//checkNew(redirect, "DeleteInstanceClass::RedirectAttributeLink()");
			//redirect->RedirectAttributeLink(target_name, to_categories) ;
			
		}
		if (DelLinksOfCategory(target_name, _INTERCONNECTED, FROM_NODE) == TMS_APIFail) return TMS_APIFail;
		if (DelLinksOfCategory(target_name, _INTERCONNECTED, TO_NODE) == TMS_APIFail) return TMS_APIFail;
	}
	return TMS_APISucc;
        */
        // </editor-fold> 
        boolean FoundInterCon =false; // flag set if category (Individual, interconnected) is found in the DB.

	QC.reset_name_scope();
	QC.set_current_node(new StringObject(INDIVIDUAL));
	//SetNewCurrentNode(INDIVIDUAL);
	if (QC.set_current_node(new StringObject(_INTERCONNECTED)) < 0) {
		// OK There is no category (Individual, interconnected))
		QC.reset_error_message();
		FoundInterCon = false;
	}

	if (FoundInterCon) {
		QC.reset_name_scope();
		if (QC.set_current_node(target_name) < 0) {
                    return TMS_APIFail;
                }
                int all_classes = QC.get_all_classes(0);
		if (all_classes< 0) {
                    return TMS_APIFail;
                }
                int to_categories = QC.get_link_to_by_category(all_classes, new StringObject(INDIVIDUAL), new StringObject(_INTERCONNECTED));
		if (to_categories < 0) {
                    return TMS_APIFail;
                }
                int from_categories = QC.get_link_from_by_category(all_classes, new StringObject(INDIVIDUAL), new StringObject(_INTERCONNECTED));
		if (from_categories < 0) {
                    return TMS_APIFail;
                }
		if (QC.set_intersect(to_categories, from_categories) < 0) {
                    return TMS_APIFail;
                }
		boolean emptySet = (QC.set_get_card(to_categories) > 0) ? false : true;
		if (!emptySet) {
			//if (IsEmptySet(to_categories, NULL) == EF_FALSE) {
			// Maybe I can redirect something ...
			if (RedirectAttributeLink(target_name, to_categories) == TMS_APIFail) {
                            return TMS_APIFail;
                        }
			
			//if (redirect != (RedirectClass*) NULL) {
			//delete redirect ;
			//redirect = NULL ;
			//}
			//redirect = new RedirectClass(this, transaction) ;
			//checkNew(redirect, "DeleteInstanceClass::RedirectAttributeLink()");
			//redirect->RedirectAttributeLink(target_name, to_categories) ;
			
		}
		if (CHECK_DelLinksOfCategory(target_name, new StringObject(_INTERCONNECTED), DIRECTION.FROM_NODE) == TMS_APIFail) {
                    return TMS_APIFail;
                }
		if (CHECK_DelLinksOfCategory(target_name, new StringObject(_INTERCONNECTED), DIRECTION.TO_NODE) == TMS_APIFail) {
                    return TMS_APIFail;
                }
	}
	return TMS_APISucc;
        
    }
    
    //NOT SURE IT IS TESTED PROBABLY NEVER ACTUALLY USED
    int RedirectAttributeLink(StringObject target_name, int cat_set_id){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        -------------------------------------------------------------------
tms_api::RedirectAttributeLink()
---------------------------------------------------------------------
INPUT: - target_name : the name of an object
- cat_set_id : the categories of the links to be redirected
OUTPUT: - TMS_APISucc in case the given object's attribute links
are redirected succesfully
- TMS_APIFail otherwise
FUNCTION: The objects that points to target_name, are redirected to
the objects that are pointed by the target_name if these
are under the same metacategory. This is done for all the
metacategories of cat_set_id.
----------------------------------------------------------------
int tms_api::RedirectAttributeLink(char *target_name, int cat_set_id)
{
	int from_links, to_links;
	char fromClass[LOGINAM_SIZE], category[LOGINAM_SIZE];
	char from_cls[LOGINAM_SIZE], to_cls[LOGINAM_SIZE], label[LOGINAM_SIZE];
	cm_value from_cmv, to_cmv;
	cm_value	cmv; 	// Variable that stores values returned by the pqi 
	int ret_val;

	QC->reset_set(cat_set_id) ;
	while (QC->return_link(cat_set_id, fromClass, category, &cmv) >= 0) {
		QC->reset_name_scope();
		QC->set_current_node(target_name);
		//SetNewCurrentNode(target_name);
		// free allocated space
		switch (cmv.type) {
		case TYPE_STRING : case TYPE_NODE :
			free(cmv.value.s);
			cmv.value.s = NULL;
		}

		if ((from_links = QC->get_link_from_by_category(DEFAULT_NODE, fromClass, category)) < 0) return TMS_APIFail;
		//
		// Daskal 7-7-95 :
                // from_links are the links that has from_value the target_name
		//
		if ((to_links = QC->get_link_to_by_category(DEFAULT_NODE, fromClass, category)) < 0) return TMS_APIFail;
		//
		// Daskal 7-7-95 :
		// to_links are the links that has to_value the target_name
		//

		// filter the to_links  : get only them that ar DIRECT instances of current category
		to_links = GetFromSetDirectInstancesOfCategory(to_links, fromClass, category);
		QC->reset_set(to_links);
		// filter the from_links: get only them that ar DIRECT instances of current category
		from_links = GetFromSetDirectInstancesOfCategory(from_links, fromClass, category);
		QC->reset_set(from_links);
		//
		//--// filter the to_links  : get only them that ar DIRECT instances of current category
		//set_filter_cond( 	BELONGS(
		//LINK(fromClass, category),
		//GET_CLASSES(SET_ID(to_links))
		//));
		//QC->reset_set(to_links) ;
		//to_links = get_filtered(to_links);
		//QC->reset_set(to_links) ;

		//QC->reset_set(from_links) ;
		// ----filter the from_links: get only them that ar DIRECT instances of current category
		//set_filter_cond( 	BELONGS(
		//LINK(fromClass, category),
		//GET_CLASSES(SET_ID(from_links))
		//));
		//QC->reset_set(from_links) ;
		//from_links = get_filtered(from_links);
		//QC->reset_set(from_links) ;
		//

		while (QC->return_link(to_links, to_cls, label, &to_cmv) >= 0) {
			// free allocated space
			switch (to_cmv.type) {
			case TYPE_STRING : case TYPE_NODE   :
				free(to_cmv.value.s);
				to_cmv.value.s = NULL;
			}
			if (to_cmv.type != TYPE_NODE) {
				fprintf (stderr, "RedirectClass -- RedirectAttributeLink :  Cannot create a link that has not as from_value a Node 	!!!\n") ;
				continue;
			}
			QC->reset_set(from_links) ;
			while (QC->return_link(from_links, from_cls, label, &from_cmv) >= 0) {
				//
				// Create link without label.
				// What should I put for label anyway ?
				//
				//
				// I dont use AddAttributeLink with sysids; it is a bit complicated.
				// I use the one with logical names ...
				
				//ret_val = transaction->AddAttributeLink(to_cls, fromClass, category, EMPTY_STRING, &from_cmv) ;
				int catSet = QC->set_get_new();
				QC->reset_name_scope();
				QC->set_current_node(fromClass);
				QC->set_current_node(category);
				QC->set_put(catSet);
				if (CreateAttribute(NULL, to_cls, &from_cmv, -1, catSet) == TMS_APIFail) return TMS_APIFail;
				QC->free_set(catSet);
				// free allocated space
				switch (from_cmv.type) {
				case TYPE_STRING : case TYPE_NODE   :
					free(from_cmv.value.s);
					from_cmv.value.s = NULL;
				}
			}
		}
	} // each time I reach here, I change category 
	return TMS_APISucc;
        */
        // </editor-fold> 
        
	QC.reset_set(cat_set_id) ;
        
        Vector<Return_Link_Row> retLvals = new Vector<Return_Link_Row>();
        QC.bulk_return_link(cat_set_id, retLvals);
        
        for(Return_Link_Row RL1row: retLvals){
            //while (QC.return_link(cat_set_id, fromClass, category, &cmv) >= 0) {
            long linkIdL = RL1row.get_Neo4j_NodeId();
            StringObject fromClass = new StringObject(RL1row.get_v1_cls());
            StringObject category = new StringObject(RL1row.get_v2_label());
            CMValue cmv = RL1row.get_v3_cmv();
            
            QC.reset_name_scope();
            QC.set_current_node(target_name);
            int from_links = QC.get_link_from_by_category(0, fromClass, category);
            if (from_links < 0) {
                return TMS_APIFail;
            }
            //from_links are the links that has from_value the target_name
            
            int to_links = QC.get_link_to_by_category(0, fromClass, category);
            if (to_links < 0) {
                return TMS_APIFail;
            }
            //to_links are the links that has to_value the target_name
            
            // filter the to_links  : get only them that ar DIRECT instances of current category
            to_links = GetFromSetDirectInstancesOfCategory(to_links, fromClass, category);
            QC.reset_set(to_links);
            // filter the from_links: get only them that ar DIRECT instances of current category
            from_links = GetFromSetDirectInstancesOfCategory(from_links, fromClass, category);
            QC.reset_set(from_links);
            
            Vector<Return_Link_Row> retVals2 = new Vector<Return_Link_Row>();
            QC.bulk_return_link(to_links, retVals2);
            for(Return_Link_Row row2: retVals2){
                //while (QC.return_link(to_links, to_cls, label, &to_cmv) >= 0) {
                
                StringObject to_cls = new StringObject(row2.get_v1_cls());
                StringObject label = new StringObject(row2.get_v2_label());
                CMValue to_cmv = row2.get_v3_cmv();
                if(to_cmv.getType()!=CMValue.TYPE_NODE){
                    //fprintf (stderr, "RedirectClass -- RedirectAttributeLink :  Cannot create a link that has not as from_value a Node 	!!!\n") ;
                    Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "RedirectClass -- RedirectAttributeLink :  Cannot create a link that has not as from_value a Node 	!!!\n");
                    continue;
                }
                
                QC.reset_set(from_links) ;
                
                
                Vector<Return_Link_Row> retVals3 = new Vector<Return_Link_Row>();
                QC.bulk_return_link(from_links, retVals3);
                
                
                for(Return_Link_Row row3 : retVals3){
                    //while (QC.return_link(from_links, from_cls, label, &from_cmv) >= 0) {
                    StringObject from_cls = new StringObject(row3.get_v1_cls());
                    label = new StringObject(row3.get_v2_label());
                    CMValue from_cmv = row3.get_v3_cmv();
                    
                    
                    //
                    // Create link without label.
                    // What should I put for label anyway ?
                    //
                    //
                    // I dont use AddAttributeLink with sysids; it is a bit complicated.
                    // I use the one with logical names ...

                    //ret_val = transaction.AddAttributeLink(to_cls, fromClass, category, EMPTY_STRING, &from_cmv) ;
                    int catSet = QC.set_get_new();
                    QC.reset_name_scope();
                    QC.set_current_node(fromClass);
                    QC.set_current_node(category);
                    QC.set_put(catSet);
                    if (CreateAttribute(null, to_cls, from_cmv, -1, catSet) == TMS_APIFail) {
                        return TMS_APIFail;
                    }
                    QC.free_set(catSet);                    
                }
                
            }
                
        } // each time I reach here, I change category 		
	return TMS_APISucc;
    }
    
    //NEVER ACTUALLY TESTED 
    int GetFromSetDirectInstancesOfCategory(int linksSet, StringObject fromClass, StringObject category){
        // prepare a set containing the inserted category
	PrimitiveObject_Long categSysidL = new PrimitiveObject_Long();
	QC.get_linkid(fromClass, category, categSysidL);
	QC.reset_name_scope();
	QC.set_current_node_id(categSysidL.getValue());
	int set_with_category = QC.set_get_new();
	QC.set_put(set_with_category);

	//l_name from;
	//int fcid, lsysid, flag;
	//cm_value cmv;
	int filteredSet = QC.set_get_new();
	int directClassesOfLink;
	int set_card;
	// for each link of linkSet
	QC.reset_set(linksSet);
        
        Vector<Return_Link_Row> retVals = new Vector<Return_Link_Row>();
        QC.bulk_return_link(linksSet, retVals);
        for(Return_Link_Row row : retVals){
            //while (QC.return_link_id(linksSet, from, &fcid, &lsysid, &cmv, &flag) >= 0) {
            // get its direct classes
            QC.reset_name_scope();
            QC.set_current_node_id(row.get_Neo4j_NodeId());
            directClassesOfLink = QC.get_classes(0);
            // check if inserted category is direct class of current link
            QC.set_intersect(directClassesOfLink, set_with_category);
            set_card = QC.set_get_card(directClassesOfLink);
            if (set_card > 0) { // inserted category is direct class of current link
                    // add this link to set to be returned
                    QC.set_put(filteredSet);
            }
            QC.free_set(directClassesOfLink);
        }
        
	QC.free_set(set_with_category);
	QC.free_set(linksSet);

	return filteredSet;
    }
    
    public int  CHECK_DeleteSource(StringObject sourceName){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        --------------------------------------------------------------
tms_api::DeleteSource()
----------------------------------------------------------------
INPUT: - sourceName : the name of a source to be deleted
OUTPUT: - TMS_APISucc in case the given source is deleted succesfully
- TMS_APIFail otherwise
FUNCTION: deletes the given source
----------------------------------------------------------------
int tms_api::DeleteSource(char *sourceName)
{
	int ret = DeleteSourceEditor(sourceName, DELETE_SOURCE);
	return ret;
}
        */
        // </editor-fold> 
        int ret = CHECK_DeleteSourceEditor(sourceName, DELETE_SOURCE);
	return ret;
    }
    
    int CHECK_DeleteEditor(StringObject editorName){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*--------------------------------------------------------------
tms_api::DeleteEditor()
----------------------------------------------------------------
INPUT: - editorName : the name of an editor to be deleted
OUTPUT: - TMS_APISucc in case the given editor is deleted succesfully
- TMS_APIFail otherwise
FUNCTION: deletes the given editor
----------------------------------------------------------------
int tms_api::DeleteEditor(char *editorName)
{
	int ret = DeleteSourceEditor(editorName, DELETE_EDITOR);
	return ret;
}
        */
        // </editor-fold> 
        int ret = CHECK_DeleteSourceEditor(editorName, DELETE_EDITOR);
	return ret;
    }
    
    int CHECK_DeleteSourceEditor(StringObject source_editorName, int option){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*--------------------------------------------------------------
tms_api::DeleteSourceEditor()
----------------------------------------------------------------
INPUT: - source_editorName : the name of a source or an editor to be deleted
OUTPUT: - TMS_APISucc in case the given source/editor is deleted succesfully
- TMS_APIFail in case:
- source_editorName is not the name of any existing source/editor
of currently used thesaurus
FUNCTION: deletes the given source/editor
----------------------------------------------------------------
int tms_api::DeleteSourceEditor(char *source_editorName, int option)
{
	// check if given source_editor exists in data base
	QC->reset_name_scope();
	int source_editorSysid;
	if ((source_editorSysid = QC->set_current_node(source_editorName)) < 0) {
		sprintf(errorMessage, translate("%s does not exist in data base"), source_editorName);
		return TMS_APIFail;
	}

	// get the name of the Class Source or MERIMEEEditor
	l_name ClassName;
	switch (option) {
	case DELETE_EDITOR : sprintf(ClassName, "%sEditor", userOperation); // MERIMEEEditor
		break;
	case DELETE_SOURCE : strcpy(ClassName, "Source"); break;
	}

	//abort if ClassName does not exist
	QC->reset_name_scope();
	int ClassSysid = QC->set_current_node(ClassName);
	if (ClassSysid <= 0) {
		strcpy(errorMessage, ClassName);
		strcat(errorMessage, " ");
		strcat(errorMessage, translate(OBJECT_DOES_NOT_EXIST));
		return TMS_APIFail;
	}

	// check if given source_editor is a source_editor of current thesaurus
	int ret = ObjectinClass(source_editorName, ClassName, errorMessage);
	if (ret == TMS_APIFail){
		sprintf(errorMessage,translate("%s is not a %s"), source_editorName, ClassName);
		return TMS_APIFail;
	}

	IDENTIFIER I_source_editorName(source_editorName);

	ret = QC->Delete_Node(&I_source_editorName);
	if (ret==APIFail) {abort_delete(source_editorName, errorMessage); return TMS_APIFail;}

	commit_delete(source_editorName, errorMessage);
	return TMS_APISucc;
        */
        // </editor-fold> 
        // check if given source_editor exists in data base
	QC.reset_name_scope();
	long source_editorSysidL = QC.set_current_node(source_editorName);
	if (source_editorSysidL < 0) {
            //sprintf(errorMessage, translate("%s does not exist in data base"), source_editorName);
            errorMessage.setValue(String.format("%s does not exist in data base", source_editorName.getValue()));
            return TMS_APIFail;
	}

	// get the name of the Class Source or MERIMEEEditor
	StringObject ClassName = new StringObject();
        if(option==DELETE_EDITOR){
            //sprintf(ClassName, "%sEditor", userOperation); // MERIMEEEditor
            ClassName.setValue(userOperation.getValue()+"Editor");
        }
        else if (option == DELETE_SOURCE){
            ClassName.setValue("Source");
        }
	
	//abort if ClassName does not exist
	QC.reset_name_scope();
	long ClassSysidL = QC.set_current_node(ClassName);
	if (ClassSysidL <= 0) {
            //strcpy(errorMessage, ClassName);
            //strcat(errorMessage, " ");
            //strcat(errorMessage, translate(OBJECT_DOES_NOT_EXIST));
            errorMessage.setValue(ClassName.getValue()+" " + OBJECT_DOES_NOT_EXIST);
            return TMS_APIFail;
	}

	// check if given source_editor is a source_editor of current thesaurus
	int ret = ObjectinClass(source_editorName, ClassName, errorMessage);
	if (ret == TMS_APIFail){
            //sprintf(errorMessage,translate("%s is not a %s"), source_editorName, ClassName);
            errorMessage.setValue(String.format("%s is not a %s", source_editorName.getValue(),ClassName.getValue()));
            return TMS_APIFail;
	}

	Identifier I_source_editorName = new Identifier(source_editorSysidL);

	ret = QC.CHECK_Delete_Node(I_source_editorName);
	if (ret==QClass.APIFail) {
            abort_delete(source_editorName, errorMessage); 
            return TMS_APIFail;
        }

	commit_delete(source_editorName, errorMessage);
	return TMS_APISucc;
    }
    
    /**
     *  -------------------------------------------------------------------
        tms_api::MoveToHierarchy()
       ---------------------------------------------------------------------
       INPUT: - term : the name of a released descriptor
        hierarchy : the name of a hierarchy
        bhierarchy : the name of a hierarchy
        btterm : the name of a released descriptor
        curOption : MOVE_NODE_ONLY or MOVE_NODE_AND_SUBTREE or CONNECT_NODE_AND_SUBTREE
        OUTPUT: - TMS_APISucc in case the given descriptor is moved to another
        hierarchy succesfully
        - TMS_APIFail in case:
        FUNCTION: Depending on the value of input Option:
        · MOVE_NODE_ONLY
        In this case, the concept is detached from the selected hierarchy
        (as in the case of "Abandon Descriptor" and is classified in the
        new hierarchy. A broader term relation is established between
        the concept and the given broader term.
        · MOVE_NODE_AND_SUBTREE
        In this case, the concept and its subtree of broader term
        relations are detached from the selected hierarchy and are
        reclassified in the new hierarchy. A broader term relation
        is established between the concept and the given broader term.
        · CONNECT_NODE_AND_SUBTREE
        In this case, the concept and its subtree of broader term
        relations are NOT detached from the selected hierarchy
        (as in previous case) and are multiply classified in the
        new hierarchy. A broader term relation is established between
        the concept and the given broader term.
     * @param term
     * @param hierarchy
     * @param bhierarchy
     * @param btterm
     * @param curOption
     * @return 
     */
    public int  CHECK_MoveToHierarchy(StringObject term, StringObject hierarchy, StringObject bhierarchy, StringObject btterm, int curOption){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
-------------------------------------------------------------------
 tms_api::MoveToHierarchy()
---------------------------------------------------------------------
INPUT: - term : the name of a released descriptor
 hierarchy : the name of a hierarchy
 bhierarchy : the name of a hierarchy
 btterm : the name of a released descriptor
 curOption : MOVE_NODE_ONLY or MOVE_NODE_AND_SUBTREE or CONNECT_NODE_AND_SUBTREE
 OUTPUT: - TMS_APISucc in case the given descriptor is moved to another
 hierarchy succesfully
 - TMS_APIFail in case:
 FUNCTION: Depending on the value of input Option:
 · MOVE_NODE_ONLY
 In this case, the concept is detached from the selected hierarchy
 (as in the case of "Abandon Descriptor" and is classified in the
 new hierarchy. A broader term relation is established between
 the concept and the given broader term.
 · MOVE_NODE_AND_SUBTREE
 In this case, the concept and its subtree of broader term
 relations are detached from the selected hierarchy and are
 reclassified in the new hierarchy. A broader term relation
 is established between the concept and the given broader term.
 · CONNECT_NODE_AND_SUBTREE
 In this case, the concept and its subtree of broader term
 relations are NOT detached from the selected hierarchy
 (as in previous case) and are multiply classified in the
 new hierarchy. A broader term relation is established between
 the concept and the given broader term.
        
        char  prefix_thesaurus[MAX_STRING];
  char  hierarchy_term[LOGINAM_SIZE];
  char  hierarchy_descr[LOGINAM_SIZE];
  char  hierarchy_class[LOGINAM_SIZE];
  char  broader_term[LOGINAM_SIZE];

  if (btterm[0]=='\0'){
    strcpy(errorMessage, translate(EMPTY_STRING));
    strcat(errorMessage, " ");
    strcat(errorMessage, translate("Broader Term"));
    //sprintf(message,"%s %s",EMPTY_STRING,"Broader Term");
    return TMS_APIFail;
  }

  if (bhierarchy[0]=='\0'){
    strcpy(errorMessage, translate(EMPTY_STRING));
    strcat(errorMessage, " ");
    strcat(errorMessage, translate("To Hierarchy"));
    //sprintf(message,"%s %s",EMPTY_STRING,"To Hierarchy");
    return TMS_APIFail;
  }

  // check if new BT term exists in DB
  QC->reset_name_scope();
  int termSySId = QC->set_current_node(term);
  if (termSySId<=0) {
    strcpy(errorMessage, term);
    strcat(errorMessage, " ");
    strcat(errorMessage, translate(OBJECT_DOES_NOT_EXIST));
    return TMS_APIFail;
  }

  // check if current hierarchy exists in DB
  QC->reset_name_scope();
  int hierarchySySId = QC->set_current_node(hierarchy);
  if (hierarchySySId<=0) {
    strcpy(errorMessage, hierarchy);
    strcat(errorMessage, " ");
    strcat(errorMessage, translate(OBJECT_DOES_NOT_EXIST));
    return TMS_APIFail;
  }

  // check if new hierarchy exists in DB
  QC->reset_name_scope();
  int bhierarchySySId = QC->set_current_node(bhierarchy);
  if (bhierarchySySId<=0) {
    strcpy(errorMessage, bhierarchy);
    strcat(errorMessage, " ");
    strcat(errorMessage, translate(OBJECT_DOES_NOT_EXIST));
    return TMS_APIFail;
  }

  // check if new BT term exists in DB
  QC->reset_name_scope();
  int bttermSySId = QC->set_current_node(btterm);
  if (bttermSySId<=0) {
    strcpy(errorMessage, btterm);
    strcat(errorMessage, " ");
    strcat(errorMessage, translate(OBJECT_DOES_NOT_EXIST));
    return TMS_APIFail;
  }

  int ret = ThesaurusNamefHierarchy(hierarchy,prefix_thesaurus,errorMessage); // MERIMEE
  if (ret==TMS_APIFail) //{ abort_transaction(); return TMS_APIFail;}
	  {return TMS_APIFail;}

  sprintf(hierarchy_term,"%s%s",prefix_thesaurus,"HierarchyTerm"); // MERIMEEHierarchyTerm

   // looking for MERIMEEDescriptor
   int card;
   l_name givenClassFrom, givenClass;
   strcpy(givenClass, userOperation); // MERIMEE
   strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
   ret = GetThesaurusObject(hierarchy_descr, NULL, "Descriptor", NULL, givenClass, &card);
	if (ret==TMS_APIFail) {
      sprintf(errorMessage, translate("Failed to refer to class <%s%s>"), userOperation, "Descriptor");
      return TMS_APIFail;
   }

   // looking for MERIMEE_BT
   strcpy(givenClass, userOperation); // MERIMEE
   strcat(givenClass, "_relation"); // MERIMEE_relation
   sprintf(givenClassFrom, "%s%s", userOperation, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
   ret = GetThesaurusObject(broader_term, "Descriptor", "BT", givenClassFrom, givenClass, &card);
	if (ret==TMS_APIFail) {
      sprintf(errorMessage, translate("Failed to refer to category <%s_BT>"), userOperation);
      return TMS_APIFail;
	}

   // looking for MERIMEEHierarchy
   strcpy(givenClass, userOperation); // MERIMEE
   strcat(givenClass, "ThesaurusClassType"); // MERIMEEThesaurusClassType
   ret = GetThesaurusObject(hierarchy_class, NULL, "Hierarchy", NULL, givenClass, &card);
  	if (ret==TMS_APIFail) return TMS_APIFail;

  // check if given descriptor belongs to current thesaurus hierarchy
  ret = ObjectinClass(btterm,hierarchy_term,errorMessage);
  if (ret==TMS_APIFail) {
    sprintf(errorMessage,translate("%s is not a %s descriptor"),btterm,prefix_thesaurus);
    return TMS_APIFail;
  }

  // check if given descriptor belongs to given hierarchy
  ret = ObjectinClass(term,hierarchy,errorMessage);
  if (ret==TMS_APIFail) {
    sprintf(errorMessage,translate("%s does not belong to %s"),term,hierarchy);
    return TMS_APIFail;
  }

  // check if current hierarchy is a hierarchy of current thesaurus
  ret = ObjectinClass(hierarchy,hierarchy_class,errorMessage);
  if (ret==TMS_APIFail){
    sprintf(errorMessage,translate("%s is not a %s hierarchy"),hierarchy,prefix_thesaurus);
    return TMS_APIFail;
  }

  // check if new hierarchy is a hierarchy of current thesaurus
  ret = ObjectinClass(bhierarchy,hierarchy_class,errorMessage);
  if (ret==TMS_APIFail){
    sprintf(errorMessage,translate("%s is not a %s hierarchy"),bhierarchy,prefix_thesaurus);
    return TMS_APIFail;
  }

  // check if given descriptor is a Top term
  ret = isTopTerm(term,hierarchy,errorMessage);
  if (ret==TMS_APISucc) {
    sprintf(errorMessage,translate("%s is (%s)'s top term and cannot be moved to another hierarchy\n"),term,hierarchy);
    return TMS_APIFail;
  }

  // check if new BT term belongs to new hierarchy
  ret = ObjectinClass(btterm,bhierarchy,errorMessage);
  if (ret==TMS_APIFail) {
    sprintf(errorMessage,translate("%s does not belong to %s"),btterm,bhierarchy);
    return TMS_APIFail; }

	// ---------check the option set for this operation
   switch (curOption) {
   	case MOVE_NODE_ONLY : return MoveNodeOnly(term, hierarchy, bhierarchy, btterm, curOption, hierarchy_descr, broader_term, errorMessage);
      case MOVE_NODE_AND_SUBTREE : return Move_ConnectNodeAndSubtree(term, hierarchy, bhierarchy, btterm, curOption, hierarchy_descr, broader_term, errorMessage);
      case CONNECT_NODE_AND_SUBTREE : Move_ConnectNodeAndSubtree(term, hierarchy, bhierarchy, btterm, curOption, hierarchy_descr, broader_term, errorMessage);
   }
        
        */
        // </editor-fold> 
        
        
        
        //char prefix_thesaurus[MAX_STRING];
        //char hierarchy_term[LOGINAM_SIZE];
        //char hierarchy_descr[LOGINAM_SIZE];
        //char hierarchy_class[LOGINAM_SIZE];
        //char broader_term[LOGINAM_SIZE];
        if (btterm==null || btterm.getValue()==null || btterm.getValue().length()== 0) {
            errorMessage.setValue(String.format("%s %s", EMPTY_STRING, "Broader Term"));
            //strcpy(errorMessage, translate(EMPTY_STRING));
            //strcat(errorMessage, " ");
            //strcat(errorMessage, translate("Broader Term"));
            //sprintf(message,"%s %s",EMPTY_STRING,"Broader Term");
            return TMS_APIFail;
        }

        //if (bhierarchy[0] == '\0') {
        if (bhierarchy==null || bhierarchy.getValue()==null || bhierarchy.getValue().length()== 0) {
            errorMessage.setValue(String.format("%s %s", EMPTY_STRING, "To Hierarchy"));
            //strcpy(errorMessage, translate(EMPTY_STRING));
            //strcat(errorMessage, " ");
            //strcat(errorMessage, translate("To Hierarchy"));
            //sprintf(message,"%s %s",EMPTY_STRING,"To Hierarchy");
            return TMS_APIFail;
        }
        //ELIAS Improvement STARTS
        /*
        if (hierarchy==null || hierarchy.getValue()==null || hierarchy.getValue().length()== 0) {
            errorMessage.setValue(String.format("%s %s", EMPTY_STRING, "To Hierarchy"));
            //strcpy(errorMessage, translate(EMPTY_STRING));
            //strcat(errorMessage, " ");
            //strcat(errorMessage, translate("To Hierarchy"));
            //sprintf(message,"%s %s",EMPTY_STRING,"To Hierarchy");
            return TMS_APIFail;
        }
        //no need to do anything if previous is the same with the new one!
        boolean sameHierarchy = hierarchy.getValue().equals(bhierarchy.getValue());
        if(sameHierarchy){
            return TMS_APISucc;
        }*/
        //ELIAS Improvement ENDS
        // check if new BT term exists in DB
        QC.reset_name_scope();
        long termSySIdL = QC.set_current_node(term);
        if (termSySIdL <= 0) {
            errorMessage.setValue(String.format("%s %s", term.getValue(), OBJECT_DOES_NOT_EXIST));
            //strcpy(errorMessage, term);
            //strcat(errorMessage, " ");
            //strcat(errorMessage, translate(OBJECT_DOES_NOT_EXIST));
            return TMS_APIFail;
        }
        // check if current hierarchy exists in DB
        QC.reset_name_scope();
        long hierarchySySIdL = QC.set_current_node(hierarchy);
        if (hierarchySySIdL <= 0) {
            errorMessage.setValue(String.format("%s %s", hierarchy.getValue(), OBJECT_DOES_NOT_EXIST));
            //strcpy(errorMessage, hierarchy);
            //strcat(errorMessage, " ");
            //strcat(errorMessage, translate(OBJECT_DOES_NOT_EXIST));
            return TMS_APIFail;
        }
        // check if new hierarchy exists in DB
        QC.reset_name_scope();
        long bhierarchySySIdL = QC.set_current_node(bhierarchy);
        if (bhierarchySySIdL <= 0) {
            errorMessage.setValue(String.format("%s %s", bhierarchy.getValue(), OBJECT_DOES_NOT_EXIST));
            //strcpy(errorMessage, bhierarchy);
            //strcat(errorMessage, " ");
            //strcat(errorMessage, translate(OBJECT_DOES_NOT_EXIST));
            return TMS_APIFail;
        }
        // check if new BT term exists in DB
        QC.reset_name_scope();
        long bttermSySIdL = QC.set_current_node(btterm);
        if (bttermSySIdL <= 0) {
            errorMessage.setValue(String.format("%s %s", btterm.getValue(), OBJECT_DOES_NOT_EXIST));
            //strcpy(errorMessage, btterm);
            //strcat(errorMessage, " ");
            //strcat(errorMessage, translate(OBJECT_DOES_NOT_EXIST));
            return TMS_APIFail;
        }

        StringObject prefix_thesaurus = new StringObject();
        int ret = ThesaurusNamefHierarchy(hierarchy, prefix_thesaurus, errorMessage); // MERIMEE
        if (ret == TMS_APIFail) {
            return TMS_APIFail;
        }

        StringObject hierarchy_term = new StringObject(prefix_thesaurus.getValue() + "HierarchyTerm");
        //sprintf(hierarchy_term, "%s%s", prefix_thesaurus, "HierarchyTerm"); // MERIMEEHierarchyTerm

        // looking for MERIMEEDescriptor
        IntegerObject card = new IntegerObject();
        StringObject hierarchy_descr = new StringObject();
        StringObject givenClassFrom = new StringObject();
        StringObject givenClass = new StringObject(userOperation.getValue()+"ThesaurusNotionType");
        //l_name givenClassFrom, givenClass;
        //strcpy(givenClass, userOperation); // MERIMEE
        //strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
        ret = GetThesaurusObject(hierarchy_descr, null, new StringObject("Descriptor"), null, givenClass,  card);
        if (ret == TMS_APIFail) {
            //sprintf(errorMessage, translate("Failed to refer to class <%s%s>"), userOperation, "Descriptor");
            errorMessage.setValue(String.format("Failed to refer to class <%s%s>", userOperation.getValue(), "Descriptor"));
            return TMS_APIFail;
        }

        // looking for MERIMEE_BT
        givenClassFrom.setValue(userOperation.getValue()+"ThesaurusNotionType");
        givenClass.setValue(userOperation.getValue()+"_relation");
        
        //strcpy(givenClass, userOperation); // MERIMEE
        //strcat(givenClass, "_relation"); // MERIMEE_relation
        //sprintf(givenClassFrom, "%s%s", userOperation, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
        
        StringObject broader_term = new StringObject();
        ret = GetThesaurusObject(broader_term, new StringObject("Descriptor"), new StringObject("BT"), givenClassFrom, givenClass,  card);
        if (ret == TMS_APIFail) {
            //sprintf(errorMessage, translate("Failed to refer to category <%s_BT>"), userOperation);
            errorMessage.setValue(String.format("Failed to refer to category <%s_BT>", userOperation.getValue()));
            return TMS_APIFail;
        }

        // looking for MERIMEEHierarchy
        givenClass.setValue(userOperation.getValue()+"ThesaurusClassType");
        //strcpy(givenClass, userOperation); // MERIMEE
        //strcat(givenClass, "ThesaurusClassType"); // MERIMEEThesaurusClassType
        StringObject hierarchy_class = new StringObject();
        ret = GetThesaurusObject(hierarchy_class, null, new StringObject("Hierarchy"), null, givenClass, card);
        if (ret == TMS_APIFail) {
            return TMS_APIFail;
        }

        // check if given descriptor belongs to current thesaurus hierarchy
        ret = ObjectinClass(btterm, hierarchy_term, errorMessage);
        if (ret == TMS_APIFail) {
            //sprintf(errorMessage, translate("%s is not a %s descriptor"), btterm, prefix_thesaurus);
            errorMessage.setValue(String.format("%s is not a %s descriptor", btterm.getValue(), prefix_thesaurus.getValue()));
            return TMS_APIFail;
        }

        // check if given descriptor belongs to given hierarchy
        ret = ObjectinClass(term, hierarchy, errorMessage);
        if (ret == TMS_APIFail) {
            //sprintf(errorMessage, translate("%s does not belong to %s"), term, hierarchy);
            errorMessage.setValue(String.format("%s does not belong to %s", term.getValue(), hierarchy.getValue()));
            return TMS_APIFail;
        }

        // check if current hierarchy is a hierarchy of current thesaurus
        ret = ObjectinClass(hierarchy, hierarchy_class, errorMessage);
        if (ret == TMS_APIFail) {
            //sprintf(errorMessage, translate("%s is not a %s hierarchy"), hierarchy, prefix_thesaurus);
            errorMessage.setValue(String.format("%s is not a %s hierarchy", hierarchy.getValue(), prefix_thesaurus.getValue()));
            return TMS_APIFail;
        }

        // check if new hierarchy is a hierarchy of current thesaurus
        ret = ObjectinClass(bhierarchy, hierarchy_class, errorMessage);
        if (ret == TMS_APIFail) {
            //sprintf(errorMessage, translate("%s is not a %s hierarchy"), bhierarchy, prefix_thesaurus);
            errorMessage.setValue(String.format("%s is not a %s hierarchy", bhierarchy.getValue(), prefix_thesaurus.getValue()));
            return TMS_APIFail;
        }

        String previouVal = errorMessage.getValue();
        // check if given descriptor is a Top term
        ret = isTopTerm(term, hierarchy, errorMessage);
        if (ret == TMS_APISucc) {
            //sprintf(errorMessage, translate("%s is (%s)'s top term and cannot be moved to another hierarchy\n"), term, hierarchy);
            errorMessage.setValue(String.format("%s is (%s)'s top term and cannot be moved to another hierarchy\n", term.getValue(), hierarchy.getValue()));
            return TMS_APIFail;
        }
        else{
            errorMessage.setValue(previouVal);
        }

        // check if new BT term belongs to new hierarchy
        ret = ObjectinClass(btterm, bhierarchy, errorMessage);
        if (ret == TMS_APIFail) {
            //sprintf(errorMessage, translate("%s does not belong to %s"), btterm, bhierarchy);
            errorMessage.setValue(String.format("%s does not belong to %s", btterm.getValue(), bhierarchy.getValue()));
            return TMS_APIFail;
        }

        // ---------check the option set for this operation
        switch (curOption) {
            case MOVE_NODE_ONLY:{
                return MoveNodeOnly(term, hierarchy, bhierarchy, btterm, curOption, hierarchy_descr, broader_term, errorMessage);
            }
            case MOVE_NODE_AND_SUBTREE:{
                return Move_ConnectNodeAndSubtree(term, hierarchy, bhierarchy, btterm, curOption, hierarchy_descr, broader_term, errorMessage);
            }
            case CONNECT_NODE_AND_SUBTREE:{
                //No return in this case
                //case CONNECT_NODE_AND_SUBTREE : Move_ConnectNodeAndSubtree(term, hierarchy, bhierarchy, btterm, curOption, hierarchy_descr, broader_term, errorMessage);
                Move_ConnectNodeAndSubtree(term, hierarchy, bhierarchy, btterm, curOption, hierarchy_descr, broader_term, errorMessage);
                break;
            }
            default:{
                return TMS_APIFail;
            }   
        }
        return TMS_APISucc;
    }
    
    
    int Move_ConnectNodeAndSubtree(StringObject term, StringObject hierarchy, StringObject bhierarchy, 
            StringObject btterm, int curOption, StringObject hierarchy_descr, StringObject broader_term, StringObject message){

        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        -------------------------------------------------------------------
							tms_api::Move_ConnectNodeAndSubtree()
---------------------------------------------------------------------
	INPUT: - term : the name of a released descriptor
			   hierarchy : the name of a hierarchy
			   bhierarchy : the name of a hierarchy
            btterm : the name of a released descriptor
            curOption : MOVE_NODE_ONLY or MOVE_NODE_AND_SUBTREE or CONNECT_NODE_AND_SUBTREE
            hierarchy_descr : the Descriptor class for current thesaurus
            					(for example : MERIMEEDescriptor)
            broader_term : the BT category for current thesaurus
            					(for example : MERIMEE_BT)
				message : an allocated sring to be filled with a possible error message
	OUTPUT: - TMS_APISucc in case the given descriptor is moved to another
    			 hierarchy succesfully
           - TMS_APIFail in case:
	FUNCTION: Depending on the value of input Option:
				· MOVE_NODE_AND_SUBTREE
					In this case, the concept and its subtree of broader term
               relations are detached from the selected hierarchy and are
               reclassified in the new hierarchy. A broader term relation
               is established between the concept and the given broader term.
				· CONNECT_NODE_AND_SUBTREE
					In this case, the concept and its subtree of broader term
               relations are NOT detached from the selected hierarchy
               (as in previous case) and are multiply classified in the
               new hierarchy. A broader term relation is established between
               the concept and the given broader term.
----------------------------------------------------------------
        
        int tms_api::Move_ConnectNodeAndSubtree(char *term, char *hierarchy, char *bhierarchy, char *btterm, int curOption, char *hierarchy_descr, char *broader_term, char *message)
{
	// target term
	IDENTIFIER Iterm(term);
   // old hierarchy
	IDENTIFIER Ihierarchy(hierarchy);
   // new hierarchy
	IDENTIFIER Ibhierarchy(bhierarchy);
   // new broader term
	IDENTIFIER Ibtterm(btterm);

	// create set cat_set with category: (MERIMEEHierarchyTerm, MERIMEE_BT)
	QC->reset_name_scope();
	if (QC->set_current_node(hierarchy_descr) <= 0) { // MERIMEEDescriptor
   //if (QC->set_current_node(hierarchy_term) <= 0) { // MERIMEEHierarchyTerm
		abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
		return TMS_APIFail;
	}
	if (QC->set_current_node(broader_term) <= 0) { // MERIMEE_BT
		abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
		return TMS_APIFail;
	}
	int cat_set = QC->set_get_new();
	QC->set_put(cat_set);

	QC->reset_name_scope();
   // target term
	if (QC->set_current_node(Iterm.loginam) <= 0) {
		abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
		return TMS_APIFail;
	}

	// calculate the set with the subtree of the target
	// set the category
	categories_set catSet;
   strcpy((catSet[0]).fcl, hierarchy_descr); // MERIMEEDescriptor
	//strcpy((catSet[0]).fcl, hierarchy_term); // MERIMEEHierarchyTerm
   strcpy((catSet[0]).cat, broader_term); // MERIMEE_BT
   (catSet[0]).direction = BACKWARD;
   strcpy((catSet[1]).fcl, "end");
   strcpy((catSet[1]).cat, "end");
   (catSet[1]).direction = 0; // <=> end categories setting
   QC->set_categories(catSet);
   int subtree_link_set_id;
	if ((subtree_link_set_id = QC->get_traverse_by_category(0, NOISA)) == ERROR) {
      abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
		return TMS_APIFail;
	}

   QC->reset_set(subtree_link_set_id);
   int subtree_set_id;
   if ((subtree_set_id = QC->get_from_value(subtree_link_set_id)) == ERROR) {
      abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
		return TMS_APIFail;
	}
   QC->free_set(subtree_link_set_id);

   // glfbc(target, (MERIMEEHierarchyTerm, MERIMEE_BT))
   int ret_set1;
	if ((ret_set1 = QC->get_link_from_by_category(0, hierarchy_descr, broader_term)) == -1) {
	//if ((ret_set1 = get_link_from_by_category(0, hierarchy_term, broader_term)) == -1) {
		abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
		return TMS_APIFail;
	}

   if (curOption == MOVE_NODE_AND_SUBTREE) {
		// delete all bt links originating from target term.
		int from_sysid, link_sysid;
		cm_value to_value;
		int trav;
		l_name link_name;

		QC->reset_set(ret_set1);
   	while (QC->return_link_id(ret_set1,link_name,&from_sysid,&link_sysid,&to_value,&trav) != -1){
			IDENTIFIER link_ident(link_sysid);
			if (QC->Delete_Unnamed_Attribute(&link_ident) == APIFail) {
				abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
				return TMS_APIFail;
			}
		   // free allocated space
			switch (to_value.type) {
				case TYPE_STRING : case TYPE_NODE :
					free(to_value.value.s);
			}
		}
		QC->free_set(ret_set1);
	}

   // check if the new hierarchy is the same with the old
   int sameHierarchy = (!strcmp(hierarchy, bhierarchy)) ? 1 : 0;

	if (!sameHierarchy) {
		// add term to the new hierarchy
		if (QC->Add_Instance(&Iterm,&Ibhierarchy) == APIFail) {
			abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
			return TMS_APIFail;
		}
		// remove target term from the given hierarchy
	   if (curOption == MOVE_NODE_AND_SUBTREE) {
			if (QC->Delete_Instance(&Iterm, &Ihierarchy) == APIFail) {
				abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
				return TMS_APIFail;
			}
	   }
   }

  // add BT link from target term to btterm
  QC->reset_name_scope();
  int termSySId;
  if ((termSySId = QC->set_current_node(Iterm.loginam)) <= 0) {
    abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
    return TMS_APIFail;
  }
  QC->reset_name_scope();
  int bttermSySId = QC->set_current_node(Ibtterm.loginam);
  if (bttermSySId<=0) {
    abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
    return TMS_APIFail;
  }
  cm_value btterm_value;
  QC->assign_node(&btterm_value,Ibtterm.loginam,bttermSySId);

  IDENTIFIER term_value(termSySId);
  if (CreateAttribute(NULL, term, &btterm_value, -1, cat_set) == APIFail) {
  //if (EF_Add_Unnamed_xxx(&term_value, &btterm_value, cat_set, message) == APIFail) {
    //abort_transaction();
    return TMS_APIFail;
  }

	// reclassification:
	// for each node x of the subtree of the target
	// remove x from the old hierarchy (if curOption == MOVE_NODE_AND_SUBTREE)
   // and add x to the new hierarchy
	QC->reset_set(subtree_set_id);
   int node_sysid;
   l_name node_name, level;

//---------------testing-------------------
//
//	int subtree_set_card = QC->set_get_card(subtree_set_id);
//   int *subtree_sysids = new int [subtree_set_card*sizeof(int)];
//   int *current_sysid = subtree_sysids;
//	while (return_full_nodes(subtree_set_id, current_sysid, node_name, level) != -1) {
//   	current_sysid++;
//   }
//   int i;
//   current_sysid = subtree_sysids;
//   for (i=0; i<subtree_set_card; i++) {
//		// remove node from the old hierarchy
//		IDENTIFIER InodeTerm(*current_sysid);
//      if (curOption == MOVE_NODE_AND_SUBTREE) {
//			if (Delete_Instance(&InodeTerm, &Ihierarchy) == APIFail) {
//				abort_move_to_hierarchy(Iterm.loginam, Ibhierarchy.loginam, message);
//				return TMS_APIFail;
//			}
//      }
//      current_sysid++;
//   }
//   current_sysid = subtree_sysids;
//   for (i=0; i<subtree_set_card; i++) {
//		// remove node from the old hierarchy
//		IDENTIFIER InodeTerm(*current_sysid);
//		// add node to the new hierarchy
//		if (Add_Instance(&InodeTerm, &Ibhierarchy) == APIFail) {
//			abort_move_to_hierarchy(Iterm.loginam, Ibhierarchy.loginam, message);
//			return TMS_APIFail;
//		}
//      current_sysid++;
//   }
//   delete subtree_sysids;
//
//
//   for (i=0; i<subtree_set_card; i++) {
//		// remove node from the old hierarchy
//		IDENTIFIER InodeTerm(*current_sysid);
//      if (curOption == MOVE_NODE_AND_SUBTREE) {
//			if (Delete_Instance(&InodeTerm, &Ihierarchy) == APIFail) {
//				abort_move_to_hierarchy(Iterm.loginam, Ibhierarchy.loginam, message);
//				return TMS_APIFail;
//			}
//      }
//		// add node to the new hierarchy
//		if (Add_Instance(&InodeTerm, &Ibhierarchy) == APIFail) {
//			abort_move_to_hierarchy(Iterm.loginam, Ibhierarchy.loginam, message);
//			return TMS_APIFail;
//		}
//      current_sysid++;
//   }
//   delete subtree_sysids;


//---------------end of testing-------------------
//
//	if (!sameHierarchy) {
//		while (return_full_nodes(subtree_set_id, &node_sysid, node_name, level) != -1) {
//      	IDENTIFIER InodeTerm(node_sysid);
//			// add node to the new hierarchy
//			if (Add_Instance(&InodeTerm, &Ibhierarchy) == APIFail) {
//				abort_move_to_hierarchy(Iterm.loginam, Ibhierarchy.loginam, message);
//				return TMS_APIFail;
//			}
//			// remove node from the old hierarchy
//	      if (curOption == MOVE_NODE_AND_SUBTREE) {
//				if (Delete_Instance(&InodeTerm, &Ihierarchy) == APIFail) {
//					abort_move_to_hierarchy(Iterm.loginam, Ibhierarchy.loginam, message);
//					return TMS_APIFail;
//				}
//	      }
//		}
//   }

	if (!sameHierarchy) {
     	IDENTIFIER InodeTerm(node_sysid);
		// add node to the new hierarchy
		if (QC->Add_Instance_Set(subtree_set_id, &Ibhierarchy) == APIFail) {
			abort_move_to_hierarchy(Iterm.loginam, Ibhierarchy.loginam, message);
			return TMS_APIFail;
		}
		// remove node from the old hierarchy
      if (curOption == MOVE_NODE_AND_SUBTREE) {
			if (QC->Delete_Instance_Set(subtree_set_id, &Ihierarchy) == APIFail) {
				abort_move_to_hierarchy(Iterm.loginam, Ibhierarchy.loginam, message);
				return TMS_APIFail;
			}
      }
   }

	QC->free_set(subtree_set_id);
   QC->free_set(cat_set);

	commit_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message); return TMS_APISucc;
}
        */
        // </editor-fold> 
        
        // target term
	//IDENTIFIER Iterm(term);
        // old hierarchy
        //IDENTIFIER Ihierarchy(hierarchy);
        Identifier Ihierarchy = new Identifier(hierarchy.getValue());
        // new hierarchy
        //IDENTIFIER Ibhierarchy(bhierarchy);
        Identifier Ibhierarchy = new Identifier(bhierarchy.getValue());
        // new broader term
	//IDENTIFIER Ibtterm(btterm);

	// create set cat_set with category: (MERIMEEHierarchyTerm, MERIMEE_BT)
	QC.reset_name_scope();
        if (QC.set_current_node(hierarchy_descr) <= 0) { // MERIMEEDescriptor
            //if (QC->set_current_node(hierarchy_term) <= 0) { // MERIMEEHierarchyTerm
            abort_move_to_hierarchy(term,bhierarchy,message);
            return TMS_APIFail;
        }
	if (QC.set_current_node(broader_term) <= 0) { // MERIMEE_BT
            //abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
            abort_move_to_hierarchy(term,bhierarchy,message);
            return TMS_APIFail;
        }
	int cat_set = QC.set_get_new();
	QC.set_put(cat_set);

	QC.reset_name_scope();
        // target term
        long termSysIdL = QC.set_current_node(term);
	if ( termSysIdL<= 0) {
            abort_move_to_hierarchy(term,bhierarchy,message);
            return TMS_APIFail;
	}
        Identifier Iterm = new Identifier(termSysIdL);

	// calculate the set with the subtree of the target
	// set the category
        CategorySet[] catSet = new CategorySet[1];
        CategorySet csobj1 = new CategorySet(hierarchy_descr.getValue(), broader_term.getValue(), QClass.Traversal_Direction.BACKWARD);
        catSet[0] = csobj1;
	//categories_set catSet;
        //strcpy((catSet[0]).fcl, hierarchy_descr); // MERIMEEDescriptor
	//strcpy((catSet[0]).cat, broader_term); // MERIMEE_BT
        //(catSet[0]).direction = BACKWARD;
        //strcpy((catSet[1]).fcl, "end");
        //strcpy((catSet[1]).cat, "end");
        //(catSet[1]).direction = 0; // <=> end categories setting
        QC.set_categories(catSet);
        
        int subtree_link_set_id = QC.get_traverse_by_category(0, QClass.Traversal_Isa.NOISA);
	if (subtree_link_set_id == QClass.APIFail) {
            abort_move_to_hierarchy(term,bhierarchy,message);
            return TMS_APIFail;
	}
        
        QC.reset_set(subtree_link_set_id);
        int subtree_set_id = QC.get_from_value(subtree_link_set_id);
        if (subtree_set_id == QClass.APIFail) {
            abort_move_to_hierarchy(term,bhierarchy,message);
            return TMS_APIFail;
        }
        QC.free_set(subtree_link_set_id);
        
        // glfbc(target, (MERIMEEHierarchyTerm, MERIMEE_BT))
        int ret_set1= QC.get_link_from_by_category(0, hierarchy_descr, broader_term);
	if (ret_set1  == QClass.APIFail) {
            //if ((ret_set1 = get_link_from_by_category(0, hierarchy_term, broader_term)) == -1) {
            abort_move_to_hierarchy(term,bhierarchy,message);
            return TMS_APIFail;
        }
        
        if (curOption == MOVE_NODE_AND_SUBTREE) {
            // delete all bt links originating from target term.
            /*int from_sysid, link_sysid;
            cm_value to_value;
            int trav;
            l_name link_name;
            */
            QC.reset_set(ret_set1);
            Vector<Return_Link_Row> rLvals = new Vector<Return_Link_Row>();
            if(QC.bulk_return_link(ret_set1, rLvals)==QClass.APIFail){
                abort_move_to_hierarchy(term,bhierarchy,message);
                return TMS_APIFail;
            }
            for(Return_Link_Row row : rLvals){
                Identifier link_ident = new Identifier(row.get_Neo4j_NodeId());
                if (QC.CHECK_Delete_Unnamed_Attribute(link_ident) == QClass.APIFail) {
                    abort_move_to_hierarchy(term,bhierarchy,message);
                    return TMS_APIFail;
                }
            }
            /*
            while (QC.return_link_id(ret_set1,link_name,&from_sysid,&link_sysid,&to_value,&trav) != -1){
                IDENTIFIER link_ident(link_sysid);
                if (QC.Delete_Unnamed_Attribute(&link_ident) == APIFail) {
                    abort_move_to_hierarchy(term,bhierarchy,message);
                    return TMS_APIFail;
                }
                // free allocated space
                switch (to_value.type) {
                    case TYPE_STRING : case TYPE_NODE :
                        free(to_value.value.s);
                }
            }*/
            QC.free_set(ret_set1);
	}
        
        // check if the new hierarchy is the same with the old
        boolean sameHierarchy = hierarchy.getValue().equals(bhierarchy.getValue());

	if (!sameHierarchy) {
            // add term to the new hierarchy
            if (QC.CHECK_Add_Instance(Iterm,Ibhierarchy) == QClass.APIFail) {
                abort_move_to_hierarchy(term,bhierarchy,message);
                return TMS_APIFail;
            }
            // remove target term from the given hierarchy
            if (curOption == MOVE_NODE_AND_SUBTREE) {
                if (QC.CHECK_Delete_Instance(Iterm, Ihierarchy) == QClass.APIFail) {
                    abort_move_to_hierarchy(term,bhierarchy,message);
                    return TMS_APIFail;
                }
            }
        }
        
        // add BT link from target term to btterm
        //QC.reset_name_scope();
        //int termSySId; has been done and id stored in Iterm whereas logical name in term
        //if ((termSySId = QC.set_current_node(Iterm.loginam)) <= 0) {
            //abort_move_to_hierarchy(term,bhierarchy,message);
            //return TMS_APIFail;
        //}
        QC.reset_name_scope();
        long bttermSySIdL = QC.set_current_node(btterm);
        if (bttermSySIdL<=0) {
            abort_move_to_hierarchy(term,bhierarchy,message);
            return TMS_APIFail;
        }
        Identifier Ibtterm = new Identifier(bttermSySIdL);
        CMValue btterm_value = new CMValue();
        btterm_value.assign_node(btterm.getValue(), bttermSySIdL);
        //cm_value btterm_value;
        //QC.assign_node(&btterm_value,Ibtterm.loginam,bttermSySId);

        //IDENTIFIER term_value(termSySId);
        if (CreateAttribute(null, term, btterm_value, -1, cat_set) == QClass.APIFail) {
            //if (EF_Add_Unnamed_xxx(&term_value, &btterm_value, cat_set, message) == APIFail) {
            //abort_transaction();
            return TMS_APIFail;
        }

	// reclassification:
	// for each node x of the subtree of the target
	// remove x from the old hierarchy (if curOption == MOVE_NODE_AND_SUBTREE)
        // and add x to the new hierarchy
        QC.reset_set(subtree_set_id);
        //int node_sysid;
        //l_name node_name, level;

	if (!sameHierarchy) {
            //IDENTIFIER InodeTerm(node_sysid);
            // add node to the new hierarchy
            if (QC.CHECK_IMPROVE_Add_Instance_Set(subtree_set_id, Ibhierarchy) == QClass.APIFail) {
                abort_move_to_hierarchy(term,bhierarchy,message);
                return TMS_APIFail;
            }
            // remove node from the old hierarchy
            if (curOption == MOVE_NODE_AND_SUBTREE) {
                if (QC.CHECK_IMPROVE_Delete_Instance_Set(subtree_set_id, Ihierarchy) == QClass.APIFail) {
                    abort_move_to_hierarchy(term,bhierarchy,message);
                    return TMS_APIFail;
                }
            }
        }
        

        QC.free_set(subtree_set_id);
        QC.free_set(cat_set);

	commit_move_to_hierarchy(term,bhierarchy,message); 
        return TMS_APISucc;
    }
            
    int MoveNodeOnly(StringObject term, StringObject hierarchy, StringObject bhierarchy, 
            StringObject btterm, int curOption, StringObject hierarchy_descr, StringObject broader_term, StringObject message){
        
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        -------------------------------------------------------------------
							tms_api::MoveNodeOnly()
---------------------------------------------------------------------
	INPUT: - term : the name of a released descriptor
			   hierarchy : the name of a hierarchy
			   bhierarchy : the name of a hierarchy
            btterm : the name of a released descriptor
            curOption : no usage here
            hierarchy_descr : the Descriptor class for current thesaurus
            					(for example : MERIMEEDescriptor)
            broader_term : the BT category for current thesaurus
            					(for example : MERIMEE_BT)
            message : an allocated sring to be filled with a possible error message
	OUTPUT: - TMS_APISucc in case the given descriptor is moved to another
    			 hierarchy succesfully
           - TMS_APIFail in case:
	FUNCTION: the concept is detached from the selected hierarchy
             (as in the case of "Abandon Descriptor" and is classified in the
             new hierarchy. A broader term relation is established between
             the concept and the given broader term.
----------------------------------------------------------------
        
        int tms_api::MoveNodeOnly(char *term, char *hierarchy, char *bhierarchy, char *btterm, int curOption, char *hierarchy_descr, char *broader_term, char *message)
{
  int ret;

  IDENTIFIER	Iterm; //create identifier for hierarchy.
  strcpy(Iterm.loginam,term);
  Iterm.tag = ID_TYPE_LOGINAM;

  IDENTIFIER Ihierarchy;
  strcpy(Ihierarchy.loginam,hierarchy);
  Ihierarchy.tag = ID_TYPE_LOGINAM;

  IDENTIFIER Ibhierarchy;
  strcpy(Ibhierarchy.loginam,bhierarchy);
  Ibhierarchy.tag = ID_TYPE_LOGINAM;

  IDENTIFIER Ibtterm;
  strcpy(Ibtterm.loginam,btterm);
  Ibtterm.tag = ID_TYPE_LOGINAM;

  //establishing appropriate bt links of term
  QC->reset_name_scope();
  int htSySId = QC->set_current_node(hierarchy_descr); // MERIMEEDescriptor
  //int htSySId = QC->set_current_node(hierarchy_term); // MERIMEEHierarchyTerm
  if (htSySId<=0) {
    abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
    return TMS_APIFail;
  }
  int btSySId = QC->set_current_node(broader_term); // MERIMEE_BT
  if (btSySId<=0) {
    abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
    return TMS_APIFail;
  }
  int cat_set = QC->set_get_new();
  QC->set_put(cat_set);

  QC->reset_name_scope();

  int termSySId = QC->set_current_node(Iterm.loginam);
  if (termSySId<=0) {
    abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
    return TMS_APIFail;
  }

  int ret_set1;
  ret_set1 = QC->get_link_from_by_category(0,hierarchy_descr, broader_term);
  //ret_set1 = get_link_from_by_category(0,hierarchy_term,broader_term);
  if (ret_set1==-1) {
    abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
    return TMS_APIFail;
  }

  int ret_set2;
  ret_set2 = QC->get_to_value(ret_set1);
  if (ret_set2==-1) {
    abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
    return TMS_APIFail;
  }

  //in ret_set2 all broader terms
  int ret_set3;
  ret_set3 = QC->get_link_to_by_category(0,hierarchy_descr, broader_term);
  //ret_set3 = get_link_to_by_category(0,hierarchy_term,broader_term);
  if (ret_set3==-1) {
    abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
    return TMS_APIFail;
  }

  int ret_set4;
  ret_set4 = QC->get_from_value(ret_set3);
  if (ret_set4==-1) {
    abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
    return TMS_APIFail;
  }

  //delete all bt and nt links originating and targeting respectively from obsolete term.
  int from_sysid;
  int link_sysid;
  cm_value to_value;
  int trav;
  l_name link_name;

  QC->reset_set(ret_set1);
  while ((ret=QC->return_link_id(ret_set1,link_name,&from_sysid,&link_sysid,&to_value,&trav))!=-1){
		IDENTIFIER link_ident(link_sysid);
		ret = QC->Delete_Unnamed_Attribute(&link_ident);
		if (ret ==APIFail) {
			abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
			return TMS_APIFail;
      }
		// free allocated space
		switch (to_value.type) {
			case TYPE_STRING : case TYPE_NODE :
				free(to_value.value.s);
		}
  }

  QC->free_set(ret_set1);

  QC->reset_set(ret_set3);
  //in this case term is a leaf node.
  if (QC->set_get_card(ret_set3) == 0 ){
    ;
  } else {
    while ( (ret=QC->return_link_id(ret_set3,link_name,&from_sysid,&link_sysid,&to_value,&trav))!=-1){
      IDENTIFIER link_ident(link_sysid);
      ret = QC->Delete_Unnamed_Attribute(&link_ident);
      if (ret ==APIFail) {
			abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
			return TMS_APIFail;
      }
		switch (to_value.type) {
			case TYPE_STRING : case TYPE_NODE :
				free(to_value.value.s);
		}
    }
  }

  //now establish appropriate bt links from narrower to broader terms
  QC->free_set(ret_set3);

  QC->reset_set(ret_set4);
  if (QC->set_get_card(ret_set4)==0){
    ;
  } else {

    int from_node_sysid;
    l_name from_node_name;
    l_name level;

    int to_node_sysid;
    l_name to_node_name;

    cm_value to_node;

    while ((ret=QC->return_full_nodes(ret_set4,&from_node_sysid,from_node_name,level))!=-1) {

      IDENTIFIER from_node_ident(from_node_sysid);
      QC->reset_set(ret_set2);
      while ((ret= QC->return_full_nodes(ret_set2,&to_node_sysid,to_node_name,level))!=-1){

			QC->assign_node(&to_node,to_node_name,to_node_sysid);
         ret = CreateAttribute(NULL, from_node_name, &to_node, -1, cat_set);
		   //ret = EF_Add_Unnamed_xxx(&from_node_ident, &to_node, cat_set, message);
			if (ret==APIFail) {
		   	//abort_transaction();
				return TMS_APIFail;
			}
      }
    }
  }

  QC->free_set(ret_set4);
  QC->free_set(ret_set2);

   // check if the new hierarchy is the same with the old
   int sameHierarchy = (!strcmp(hierarchy, bhierarchy)) ? 1 : 0;

   if (!sameHierarchy) {
   	//adding term to the new hierarchy
		if (QC->Add_Instance(&Iterm,&Ibhierarchy) == APIFail) {
			abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
			return TMS_APIFail;
		}
		//removing term from the given hierarchy
		if (QC->Delete_Instance(&Iterm,&Ihierarchy) == APIFail) {
			abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
			return TMS_APIFail;
		}
	}

  //establishing appropriate bt link to btterm
  QC->reset_name_scope();
  termSySId = QC->set_current_node(Iterm.loginam);
  if (termSySId<=0) {
    abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
    return TMS_APIFail;
  }

  QC->reset_name_scope();
  int bttermSySId = QC->set_current_node(Ibtterm.loginam);
  if (bttermSySId<=0) {
    abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
    return TMS_APIFail;
  }
  
  cm_value btterm_value;
  QC->assign_node(&btterm_value,Ibtterm.loginam,bttermSySId);

  IDENTIFIER term_value(termSySId);

  ret = CreateAttribute(NULL, term, &btterm_value, -1, cat_set);
  //ret = EF_Add_Unnamed_xxx(&term_value, &btterm_value, cat_set, message);
  if (ret==APIFail) {
    //abort_transaction();
    return TMS_APIFail;
  }
  QC->free_set(cat_set);

  commit_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message); return TMS_APISucc;
}
        */
        // </editor-fold> 
        int ret;
        
        

        //IDENTIFIER	Iterm; //create identifier for hierarchy.
        //strcpy(Iterm.loginam,term);
        //Iterm.tag = ID_TYPE_LOGINAM;
        
        Identifier Ihierarchy = new Identifier(hierarchy.getValue());
        
        //IDENTIFIER Ihierarchy;
        //strcpy(Ihierarchy.loginam,hierarchy);
        //Ihierarchy.tag = ID_TYPE_LOGINAM;
        
        Identifier Ibhierarchy = new Identifier(bhierarchy.getValue());
        //IDENTIFIER Ibhierarchy;
        //strcpy(Ibhierarchy.loginam,bhierarchy);
        //Ibhierarchy.tag = ID_TYPE_LOGINAM;
        
        //Identifier Ibtterm = new Identifier(btterm.getValue());
        
        //IDENTIFIER Ibtterm;
        //strcpy(Ibtterm.loginam,btterm);
        //Ibtterm.tag = ID_TYPE_LOGINAM;

        //establishing appropriate bt links of term
        QC.reset_name_scope();
        long htSySIdL = QC.set_current_node(hierarchy_descr); // MERIMEEDescriptor
        //int htSySId = QC->set_current_node(hierarchy_term); // MERIMEEHierarchyTerm
        if (htSySIdL<=0) {
            abort_move_to_hierarchy(term,bhierarchy,message);
            return TMS_APIFail;
        }
        
        long btSySIdL = QC.set_current_node(broader_term); // MERIMEE_BT
        if (btSySIdL<=0) {
            abort_move_to_hierarchy(term,bhierarchy,message);
            return TMS_APIFail;
        }
        
        int cat_set = QC.set_get_new();
        QC.set_put(cat_set);
        
        QC.reset_name_scope();
        
        long termSySIdL = QC.set_current_node(term);
        if (termSySIdL<=0) {
            abort_move_to_hierarchy(term,bhierarchy,message);
            return TMS_APIFail;
        }
        Identifier Iterm = new Identifier(termSySIdL);
        
        int ret_set1;
        ret_set1 = QC.get_link_from_by_category(0,hierarchy_descr, broader_term);
        //ret_set1 = get_link_from_by_category(0,hierarchy_term,broader_term);
        if (ret_set1==-1) {
            abort_move_to_hierarchy(term,bhierarchy,message);
            return TMS_APIFail;
        }
        
        int ret_set2;
        ret_set2 = QC.get_to_value(ret_set1);
        if (ret_set2==-1) {
            abort_move_to_hierarchy(term,bhierarchy,message);
            return TMS_APIFail;
        }

        //in ret_set2 all broader terms
        int ret_set3;
        ret_set3 = QC.get_link_to_by_category(0,hierarchy_descr, broader_term);
        //ret_set3 = get_link_to_by_category(0,hierarchy_term,broader_term);
        if (ret_set3==-1) {
            abort_move_to_hierarchy(term,bhierarchy,message);
            return TMS_APIFail;
        }
        
        int ret_set4;
        ret_set4 = QC.get_from_value(ret_set3);
        if (ret_set4==-1) {
            abort_move_to_hierarchy(term,bhierarchy,message);
            return TMS_APIFail;
        }
        
        
        //delete all bt and nt links originating and targeting respectively from obsolete term.
        Vector<Return_Link_Row> retLvals = new Vector<Return_Link_Row>();
        if(QC.bulk_return_link(ret_set1, retLvals)==QClass.APIFail){
            abort_move_to_hierarchy(term,bhierarchy,message);
            return TMS_APIFail;
        }
        
        for(Return_Link_Row row: retLvals){
            Identifier link_ident = new Identifier(row.get_Neo4j_NodeId());
            ret = QC.CHECK_Delete_Unnamed_Attribute(link_ident);
            if (ret ==QClass.APIFail) {
                //abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
                abort_move_to_hierarchy(term,bhierarchy,message);
                return TMS_APIFail;
            }
        }
        
                //int from_sysid;
  //int link_sysid;
  //cm_value to_value;
  //int trav;
  //l_name link_name;
        QC.reset_set(ret_set1);
  /*      
  while ((ret=QC->return_link_id(ret_set1,link_name,&from_sysid,&link_sysid,&to_value,&trav))!=-1){
		IDENTIFIER link_ident(link_sysid);
		ret = QC->Delete_Unnamed_Attribute(&link_ident);
		if (ret ==APIFail) {
			//abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
                        abort_move_to_hierarchy(term,bhierarchy,message);
			return TMS_APIFail;
      }
		// free allocated space
		switch (to_value.type) {
			case TYPE_STRING : case TYPE_NODE :
				free(to_value.value.s);
		}
  }
*/
        QC.free_set(ret_set1);
        QC.reset_set(ret_set3);
        //in this case term is a leaf node.
        
        if (QC.set_get_card(ret_set3) == 0 ){
            ;
        } else {
            retLvals.clear();
            if(QC.bulk_return_link(ret_set3, retLvals)==QClass.APIFail){
                abort_move_to_hierarchy(term,bhierarchy,message);
                return TMS_APIFail;
            }

            for(Return_Link_Row row: retLvals){
                Identifier link_ident = new Identifier(row.get_Neo4j_NodeId());
                ret = QC.CHECK_Delete_Unnamed_Attribute(link_ident);
                if (ret ==QClass.APIFail) {
                    //abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
                    abort_move_to_hierarchy(term,bhierarchy,message);
                    return TMS_APIFail;
                }
            }
            /*
            while ( (ret=QC->return_link_id(ret_set3,link_name,&from_sysid,&link_sysid,&to_value,&trav))!=-1){
                IDENTIFIER link_ident(link_sysid);
                ret = QC->Delete_Unnamed_Attribute(&link_ident);
                if (ret ==APIFail) {
                    //abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
                    abort_move_to_hierarchy(term,bhierarchy,message);
                    return TMS_APIFail;
                }
                
		switch (to_value.type) {
			case TYPE_STRING : case TYPE_NODE :
				free(to_value.value.s);
		}
            }
            */
        }
        
        //now establish appropriate bt links from narrower to broader terms
        QC.free_set(ret_set3);
        QC.reset_set(ret_set4);
        if (QC.set_get_card(ret_set4)==0){
            ;
        } else {
            Vector<Return_Nodes_Row> rn1 = new Vector<Return_Nodes_Row>();
            
            if(QC.bulk_return_nodes(ret_set4, rn1)==QClass.APIFail){
                abort_move_to_hierarchy(term,bhierarchy,message);
                return TMS_APIFail;
            }
            
            for(Return_Nodes_Row row1: rn1){
                Identifier from_node_ident = new Identifier(row1.get_Neo4j_NodeId());
                StringObject from_node_name = new StringObject(row1.get_v1_cls_logicalname());
                
                Vector<Return_Nodes_Row> rn2 = new Vector<Return_Nodes_Row>();
            
                if(QC.bulk_return_nodes(ret_set2, rn2)==QClass.APIFail){
                    abort_move_to_hierarchy(term,bhierarchy,message);
                    return TMS_APIFail;
                }
                
                for(Return_Nodes_Row row2: rn2){
                    
                    CMValue to_node = new CMValue();
                    to_node.assign_node(row2.get_v1_cls_logicalname(), row2.get_Neo4j_NodeId());
                    ret = CreateAttribute(null, from_node_name, to_node, -1, cat_set);
                    if (ret==QClass.APIFail) {
                        //abort_transaction();
                        return TMS_APIFail;
                    }
                }
            }
            /*
        int from_node_sysid;
        l_name from_node_name;
        l_name level;

        int to_node_sysid;
        l_name to_node_name;

        cm_value to_node;

        while ((ret=QC->return_full_nodes(ret_set4,&from_node_sysid,from_node_name,level))!=-1) {

          IDENTIFIER from_node_ident(from_node_sysid);
          QC->reset_set(ret_set2);
          while ((ret= QC->return_full_nodes(ret_set2,&to_node_sysid,to_node_name,level))!=-1){

                            QC->assign_node(&to_node,to_node_name,to_node_sysid);
             ret = CreateAttribute(NULL, from_node_name, &to_node, -1, cat_set);
                       //ret = EF_Add_Unnamed_xxx(&from_node_ident, &to_node, cat_set, message);
                            if (ret==APIFail) {
                            //abort_transaction();
                                    return TMS_APIFail;
                            }
          }
        }
        */
        }//else card>0
        
        QC.free_set(ret_set4);
        QC.free_set(ret_set2);
        
        // check if the new hierarchy is the same with the old
        boolean sameHierarchy = hierarchy.getValue().equals(bhierarchy.getValue());
        //int sameHierarchy = (!strcmp(hierarchy, bhierarchy)) ? 1 : 0;
        
        if (!sameHierarchy) {
            //adding term to the new hierarchy
            if (QC.CHECK_Add_Instance(Iterm,Ibhierarchy) == QClass.APIFail) {
                //abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
                abort_move_to_hierarchy(term,bhierarchy,message);
                return TMS_APIFail;
            }

            //removing term from the given hierarchy
            if (QC.CHECK_Delete_Instance(Iterm,Ihierarchy) == QClass.APIFail) {
                //abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
                abort_move_to_hierarchy(term,bhierarchy,message);
                return TMS_APIFail;
            }
	}

        //establishing appropriate bt link to btterm
        //QC.reset_name_scope();
        //termSySId = QC->set_current_node(Iterm.loginam);
        //if (termSySId<=0) {
            //abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
          //  abort_move_to_hierarchy(term,bhierarchy,message);
            //return TMS_APIFail;
        //}

        QC.reset_name_scope();
        long bttermSySIdL = QC.set_current_node(btterm);
        if (bttermSySIdL<=0) {
            //abort_move_to_hierarchy(Iterm.loginam,Ibhierarchy.loginam,message);
            abort_move_to_hierarchy(term,bhierarchy,message);
            return TMS_APIFail;
        }
        
        CMValue btterm_value = new CMValue();
        btterm_value.assign_node(btterm.getValue(), bttermSySIdL);
        //IDENTIFIER term_value(termSySId);Iterm
        ret = CreateAttribute(null, term, btterm_value, -1, cat_set);
        //ret = EF_Add_Unnamed_xxx(&term_value, &btterm_value, cat_set, message);
        if (ret==TMS_APIFail) {
            //abort_transaction();
            return TMS_APIFail;
        }
        
        QC.free_set(cat_set);
        commit_move_to_hierarchy(term,bhierarchy,message); 
        return TMS_APISucc;
    }

    
    int ThesaurusNamefHierarchy(StringObject hierarchy, StringObject  prefix, StringObject message){
        /*-----------------------------------------------------------------
       			tms_api::ThesaurusNamefHierarchy()
        -------------------------------------------------------------------
	INPUT:  - hierarchy, the name of the given hierarchy
                - prefix, to be filled with the sort name of the thesaurus
                  where the given hierarchy belongs to
        e.g. for Thesaurus`MERIMEE sort name = MERIMEE
			 - message, an allocated string which will be filled with
          	an error message in case of an error occurs
	OUTPUT: - TMS_APISucc in case no error query execution happens
           - TMS_APIFail in case an error query execution happens
   FUNCTION: gets the sort name of the thesaurus where the given hierarchy belongs to
   			 This is done by:
             - getting all classes of the given hierarchy
             - getting the links pointing from the above classes
               and under category ("ThesaurusClassType","of_thesaurus")
             - getting the to_value of the above links
             - informing the prefix with the sort name of the above node
	ATTENTION: - this function must be called inside a query session
-----------------------------------------------------------------*/
        // get all classes of the given hierarchy
        QC.reset_name_scope();
        long hierarchySySIdL = QC.set_current_node(hierarchy);
        if (hierarchySySIdL<=0) { 
            //sprintf(message,"%s%s",translate(IN_THESNAMEFHIER),hierarchy); 
            message.setValue(String.format("%s%s", IN_THESNAMEFHIER,hierarchy.getValue()));
            return TMS_APIFail;
        }
        
        int ret_set1 = QC.get_all_classes(0);
        if ((ret_set1==-1) || (QC.set_get_card(ret_set1)==0)){
            QC.free_set(ret_set1);
            //sprintf(message,"%s%s",translate(IN_THESNAMEFHIER),hierarchy); 
            message.setValue(String.format("%s%s", IN_THESNAMEFHIER,hierarchy.getValue()));
            return TMS_APIFail;
        }

        // get the links pointing from the above classes
        // and under category ("ThesaurusClassType","of_thesaurus")
        QC.reset_set(ret_set1);
        int ret_set2=QC.get_link_from_by_category(ret_set1,new StringObject("ThesaurusClassType"),new StringObject("of_thesaurus"));
        QC.free_set(ret_set1);
        if ((ret_set2==-1) || (QC.set_get_card(ret_set2)==0)){
            QC.free_set(ret_set2);
            //sprintf(message,"%s%s",translate(IN_THESNAMEFHIER),hierarchy); 
            message.setValue(String.format("%s%s", IN_THESNAMEFHIER,hierarchy.getValue()));
            return TMS_APIFail;
        }
        
        // get the to_value of the above links
        QC.reset_set(ret_set2);
        ret_set1 = QC.get_to_value(ret_set2);
        QC.free_set(ret_set2);
        if ((ret_set1==-1) || (QC.set_get_card(ret_set1)==0)){
            QC.free_set(ret_set1);
            //sprintf(message,"%s%s",translate(IN_THESNAMEFHIER),hierarchy); 
            message.setValue(String.format("%s%s", IN_THESNAMEFHIER,hierarchy.getValue()));
            return TMS_APIFail;
        }

        // inform the prefix with the sort name of the above node
        QC.reset_set(ret_set1);
        StringObject thesaurus = new StringObject();
        int ret_val = QC.return_nodes(ret_set1,thesaurus);
        QC.free_set(ret_set1);
        if (ret_val==-1) { 
            //sprintf(message,"%s%s",translate(IN_THESNAMEFHIER),hierarchy); 
            message.setValue(String.format("%s%s", IN_THESNAMEFHIER,hierarchy.getValue()));
            return TMS_APIFail;
        }

        if(thesaurus==null||thesaurus.getValue()==null || thesaurus.getValue().contains("`")==false){
            //sprintf(message,"%s%s",translate(IN_THESNAMEFHIER),hierarchy); 
            message.setValue(String.format("%s%s", IN_THESNAMEFHIER,hierarchy.getValue()));
            return TMS_APIFail;
        }
        
        prefix.setValue(thesaurus.getValue().split("`")[1]);
        
        /*char	*tmp;
        tmp = strchr(thesaurus,'`');
        if (tmp==NULL){ sprintf(message,"%s%s",translate(IN_THESNAMEFHIER),hierarchy); return TMS_APIFail;}

        int i;
        for (i=1;i<strlen(tmp);i++) {
          prefix[i-1] = tmp[i];
        }
        prefix[i-1] = '\0';
        */
        
        return TMS_APISucc;
      }
    
   
    int isTopTerm(StringObject term, StringObject hierarchy, StringObject message){
        /*-----------------------------------------------------------------
                                tms_api::isTopTerm()
        -------------------------------------------------------------------
                INPUT: - term, the name of the given term
                    - hierarchy, the name of the given hierarchy
                                 - message, an allocated string which will be filled with
                        an error message in case of an error occurs
                OUTPUT: - TMS_APISucc in case the given term is a top term in the given hierarchy
                   - TMS_APIFail in case the given term is not a top term in the given hierarchy
           FUNCTION: checks if the given term is a top term in the given hierarchy.
                                 This is done by:
                     - checking if the given term belongs to the given hierarchy
                     - checking if the given term has at least one link pointing
                       from it and under the category ("TopTerm","belongs_to_hierarchy")
                ATTENTION: - this function must be called inside a query session
        -----------------------------------------------------------------*/
        int ret_set1;
        
        // check if the given term belongs to the given hierarchy
        int ret_val = ObjectinClass(term, hierarchy, message);
        if (ret_val == TMS_APIFail) {
            return TMS_APIFail;
        }
        
        ret_val = ObjectinClass(term,new StringObject("TopTerm"),message);
        if (ret_val == TMS_APIFail) {
            return TMS_APIFail;
        }

        // check if the given term has at least one link pointing
        // from it and under the category ("TopTerm","belongs_to_hierarchy")
        QC.reset_name_scope();
        long termSySIdL = QC.set_current_node(term);
        if (termSySIdL<=0) {
            //sprintf(message,"%s%s , %s",translate(IN_ISTOPTERM),term,hierarchy); 
            message.setValue(String.format("%s%s , %s",IN_ISTOPTERM, term.getValue(), hierarchy.getValue()));
            return TMS_APIFail;
        }
        
        ret_set1 = QC.get_link_from_by_category(0,new StringObject("TopTerm"),new StringObject("belongs_to_hierarchy"));
        if (ret_set1==-1){
            //sprintf(message,"%s%s , %s",translate(IN_ISTOPTERM),term,hierarchy); 
            message.setValue(String.format("%s%s , %s",IN_ISTOPTERM, term.getValue(), hierarchy.getValue()));
            return TMS_APIFail;
        }
        if (QC.set_get_card(ret_set1)==0){
            QC.free_set(ret_set1);
            //sprintf(message,"%s%s , %s",translate(IN_ISTOPTERM),term,hierarchy); 
            message.setValue(String.format("%s%s , %s",IN_ISTOPTERM, term.getValue(), hierarchy.getValue()));
            return TMS_APIFail;
        }
        
        if (QC.set_get_card(ret_set1)==1) { 
            QC.free_set(ret_set1); 
            return TMS_APISucc; 
        }
        return TMS_APIFail;
    }

    boolean isStringObjectEmpty(StringObject checkStrObj){
        if(checkStrObj==null || checkStrObj.getValue()==null|| checkStrObj.getValue().length()==0){
            return true;
        }
        return false;
    }
    public int  CHECK_RenameFacet(StringObject ofacet, StringObject nfacet){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        -------------------------------------------------------------------
tms_api::RenameFacet()
---------------------------------------------------------------------
INPUT: - ofacet : the name of a facet
- nfacet : the new name for a facet
OUTPUT: - TMS_APISucc in case the given facet is renamed succesfully
- TMS_APIFail in case:
· A facet with the same new name already exists.
· ofacet is not the name of any existing facet of
currently used thesaurus.
· The top term with the same new name already exists.
· Input nfacet does not contain the correct prefix
for a facet of currently used thesaurus.
FUNCTION: renames the given facet
----------------------------------------------------------------
int tms_api::RenameFacet(char *ofacet, char *nfacet)
{
	char thesaurus_name[MAX_STRING];
	char thesaurus_name_l[MAX_STRING];

	char prefix_term[MAX_STRING];

	char otopterm[LOGINAM_SIZE];
	char ntopterm[LOGINAM_SIZE];
	char ntopterm_t[LOGINAM_SIZE];

	char top_term_class[LOGINAM_SIZE];
	char belongs_category[LOGINAM_SIZE];

	char *tmp;

	int ret;

	if (nfacet[0]=='\0'){
		sprintf(errorMessage,"%s %s",translate(EMPTY_STRING),translate("New name")); return TMS_APIFail; }

	// check if old facet name exists in DB
	QC->reset_name_scope();
	int ofacetSySId = QC->set_current_node(ofacet);
	if (ofacetSySId<0) {
		sprintf(errorMessage,"%s %s",ofacet, translate(OBJECT_DOES_NOT_EXIST));
		//abort_transaction(); return TMS_APIFail;
		return TMS_APIFail;
	}

	// check if old facet is a facet of current thesaurus
	// if it belongs to MERIMEEFacet
	// looking for MERIMEEFacet
	int card;
	l_name givenClass, thesaurus_facet;
	strcpy(givenClass, userOperation); // MERIMEE
	strcat(givenClass, "ThesaurusClassType"); // MERIMEEThesaurusClassType
	ret = GetThesaurusObject(thesaurus_facet, NULL, "Facet", NULL, givenClass, &card);
	if (ret==TMS_APIFail) //{ abort_transaction(); return TMS_APIFail;}
	{return TMS_APIFail;}

	ret = ObjectinClass(ofacet, thesaurus_facet, errorMessage);
	if (ret == TMS_APIFail){
		sprintf(errorMessage,translate("%s is not a %s facet"), ofacet, userOperation);
		//abort_transaction(); return TMS_APIFail;
		return TMS_APIFail;
	}

	l_name thesaurus;
	ret = GetThesaurus(thesaurus,errorMessage);
	if (ret==TMS_APIFail) return TMS_APIFail;

	// get the correct prefix for a facet
	l_name prefix_class;
	ret = ClassPrefixfThesaurus(thesaurus,prefix_class,errorMessage);
	if (ret==TMS_APIFail) //{ abort_transaction(); return TMS_APIFail;}
	{return TMS_APIFail;}

	// abort if new facet has not the correct prefix
	if (strncmp(prefix_class, nfacet, strlen(prefix_class))) {
		sprintf(errorMessage, translate("%s has not the correct prefix: %s"), nfacet, prefix_class);
		//abort_transaction();
		return TMS_APIFail;
	}
	// abort if new facet contains only prefix
	int onlyPrefix = StringContainsOnlyPrefix(prefix_class, nfacet);
	if (onlyPrefix) {
		sprintf(errorMessage, translate("Given facet must not be blanck after prefix: %s"), prefix_class);
		//abort_transaction();
		return TMS_APIFail;
	}

	// check if new facet name exists in DB
	QC->reset_name_scope();
	int nfacetSySId = QC->set_current_node(nfacet);
	if (nfacetSySId>=0) {
		sprintf(errorMessage,"%s %s",nfacet,translate(OBJECT_EXISTS));
		//abort_transaction(); return TMS_APIFail;
		return TMS_APIFail;
	}

	IDENTIFIER Iofacet;
	strcpy(Iofacet.loginam,ofacet);
	Iofacet.tag = ID_TYPE_LOGINAM;

	IDENTIFIER Infacet;
	strcpy(Infacet.loginam,nfacet);
	Infacet.tag = ID_TYPE_LOGINAM;

	ret = QC->Rename_Node(&Iofacet,&Infacet);
	if (ret==APIFail) {
		abort_rename(ofacet,nfacet,errorMessage); return TMS_APIFail;}

	commit_rename(Iofacet.loginam,Infacet.loginam,errorMessage);
	return TMS_APISucc;
        }
        */
        // </editor-fold> 
        
	StringObject thesaurus_name = new StringObject();
	StringObject thesaurus_name_l = new StringObject();

	StringObject prefix_term = new StringObject();

	StringObject otopterm = new StringObject();
	StringObject ntopterm = new StringObject();
	StringObject ntopterm_t = new StringObject();

	StringObject top_term_class = new StringObject();
	StringObject belongs_category = new StringObject();

	StringObject tmp = new StringObject();

	int ret;

        
	if (isStringObjectEmpty(nfacet)){
            //sprintf(errorMessage,"%s %s",translate(EMPTY_STRING),translate("New name")); 
            errorMessage.setValue(String.format("%s %s", EMPTY_STRING, "New name"));
            return TMS_APIFail; 
        }
        if (isStringObjectEmpty(ofacet)){
            //sprintf(errorMessage,"%s %s",translate(EMPTY_STRING),translate("New name")); 
            errorMessage.setValue(String.format("%s %s", EMPTY_STRING, "Old name"));
            return TMS_APIFail; 
        }

	// check if old facet name exists in DB
	QC.reset_name_scope();
	long ofacetSySIdL = QC.set_current_node(ofacet);
	if (ofacetSySIdL<0) {
            //sprintf(errorMessage,"%s %s",ofacet, translate(OBJECT_DOES_NOT_EXIST));
            errorMessage.setValue(String.format("%s %s", ofacet.getValue(), OBJECT_DOES_NOT_EXIST));
            //abort_transaction(); return TMS_APIFail;
            return TMS_APIFail;
	}

	// check if old facet is a facet of current thesaurus
	// if it belongs to MERIMEEFacet
	// looking for MERIMEEFacet
	IntegerObject card = new IntegerObject(0);
        StringObject thesaurus_facet = new StringObject();
        StringObject givenClass = new StringObject(userOperation.getValue()+"ThesaurusClassType");
	//l_name givenClass, thesaurus_facet;
	//strcpy(givenClass, userOperation); // MERIMEE
	//strcat(givenClass, "ThesaurusClassType"); // MERIMEEThesaurusClassType
	ret = GetThesaurusObject(thesaurus_facet, null, new StringObject("Facet"), null, givenClass, card);
	if (ret==TMS_APIFail){
            return TMS_APIFail;
        }

	ret = ObjectinClass(ofacet, thesaurus_facet, errorMessage);
	if (ret == TMS_APIFail){
            //sprintf(errorMessage,translate("%s is not a %s facet"), ofacet, userOperation);
            errorMessage.setValue(String.format("%s is not a %s facet", ofacet.getValue(), userOperation.getValue()));
            //abort_transaction(); return TMS_APIFail;
            return TMS_APIFail;
	}

	StringObject thesaurus = new StringObject();
	ret = GetThesaurusName(thesaurus);
	if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }

	// get the correct prefix for a facet
	StringObject prefix_class = new StringObject();
	ret = ClassPrefixfThesaurus(thesaurus,prefix_class,errorMessage);
	if (ret==TMS_APIFail){
            return TMS_APIFail;
        }

	// abort if new facet has not the correct prefix
        if(nfacet.getValue().startsWith(prefix_class.getValue())==false){
	//if (strncmp(prefix_class, nfacet, strlen(prefix_class))) {
            //sprintf(errorMessage, translate("%s has not the correct prefix: %s"), nfacet, prefix_class);
            errorMessage.setValue(String.format("%s has not the correct prefix: %s", nfacet.getValue(), prefix_class.getValue()));
            //abort_transaction();
            return TMS_APIFail;
	}
	// abort if new facet contains only prefix
	boolean onlyPrefix = prefix_class.getValue().equals(nfacet.getValue());//StringContainsOnlyPrefix(prefix_class, nfacet);
	if (onlyPrefix) {
            //sprintf(errorMessage, translate("Given facet must not be blanck after prefix: %s"), prefix_class);
            errorMessage.setValue(String.format("Given facet must not be blanck after prefix: %s", prefix_class.getValue()));
            //abort_transaction();
            return TMS_APIFail;
	}

	// check if new facet name exists in DB
	QC.reset_name_scope();
	long nfacetSySIdL = QC.set_current_node(nfacet);
	if (nfacetSySIdL>=0) {
            //sprintf(errorMessage,"%s %s",nfacet,translate(OBJECT_EXISTS));
            errorMessage.setValue(String.format("%s %s", nfacet.getValue(),OBJECT_EXISTS));
            //abort_transaction(); return TMS_APIFail;
            return TMS_APIFail;
	}

        Identifier Iofacet = new Identifier(ofacet.getValue());
	//IDENTIFIER Iofacet;
	//strcpy(Iofacet.loginam,ofacet);
	//Iofacet.tag = ID_TYPE_LOGINAM;

        Identifier Infacet = new Identifier(nfacet.getValue());
	//IDENTIFIER Infacet;
	//strcpy(Infacet.loginam,nfacet);
	//Infacet.tag = ID_TYPE_LOGINAM;

	ret = QC.CHECK_Rename_Node(Iofacet,Infacet);
	if (ret==QClass.APIFail) {
            abort_rename(ofacet,nfacet,errorMessage); 
            return TMS_APIFail;
        }

	commit_rename(ofacet,nfacet,errorMessage);
	return TMS_APISucc;        
    }
    
    public int  CHECK_RenameHierarchy(StringObject ohierarchy, StringObject nhierarchy){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
-------------------------------------------------------------------
tms_api::RenameHierarchy()
---------------------------------------------------------------------
INPUT: - ohierarchy : the name of a hierarchy
- nhierarchy : the new name for a hierarchy
OUTPUT: - TMS_APISucc in case the given hierarchy is renamed succesfully
- TMS_APIFail in case:
· A hierarchy with the same new name already exists
· ohierarchy is not the name of any existing hierarchy of
currently used thesaurus.
· The top term with the same new name already exists.
· Input NewHierarchyName does not contain the correct prefix
for a hierarchy of currently used thesaurus.
FUNCTION: renames the given hierarchy. The hierarchy and its top term
are appropriately renamed given the new hierarchy name.
----------------------------------------------------------------
int tms_api::RenameHierarchy(char *ohierarchy, char *nhierarchy)
{
	char thesaurus_name[MAX_STRING];
	char thesaurus_name_l[MAX_STRING];
	char prefix_term[MAX_STRING];
	char otopterm[LOGINAM_SIZE];
	char ntopterm[LOGINAM_SIZE];
	char ntopterm_t[LOGINAM_SIZE];
	char top_term_class[LOGINAM_SIZE];
	char belongs_category[LOGINAM_SIZE];
	char *tmp;
	int ret;

	if (nhierarchy[0]=='\0'){
		sprintf(errorMessage,"%s %s",translate(EMPTY_STRING),translate("New name")); return TMS_APIFail; }

	// check if old hierarchy name exists in DB
	QC->reset_name_scope();
	int ohierarchySySId = QC->set_current_node(ohierarchy);
	if (ohierarchySySId<0) {
		sprintf(errorMessage,"%s %s",ohierarchy, translate(OBJECT_DOES_NOT_EXIST));
		return TMS_APIFail;
	}

	// check if old hierarchy is a hierarchy of current thesaurus
	// if it belongs to MERIMEEThesaurusClass
	// looking for MERIMEEThesaurusClass
	l_name givenClass;
	strcpy(givenClass, userOperation); // MERIMEE
	strcat(givenClass, "ThesaurusClass"); // MERIMEEThesaurusClass
	ret = ObjectinClass(ohierarchy, givenClass, errorMessage);
	if (ret == TMS_APIFail){
		sprintf(errorMessage,translate("%s is not a %s hierarchy"), ohierarchy, userOperation);
		return TMS_APIFail;
	}

	// check if new hierarchy contains the correct prefix
	// get the correct prefixes
	l_name thesaurus, prefix_class;
	ret = GetThesaurus(thesaurus,errorMessage);
	if (ret==TMS_APIFail) return TMS_APIFail;

	ret = ClassPrefixfThesaurus(thesaurus,prefix_class,errorMessage);
	if (ret==TMS_APIFail) //{ abort_transaction(); return TMS_APIFail;}
	{return TMS_APIFail;}

	// abort if new hierarchy has not the correct prefix
	if (strncmp(prefix_class, nhierarchy, strlen(prefix_class))) { // "str" does not contain "prefix"
		sprintf(errorMessage, translate("%s has not the correct prefix: %s"), nhierarchy, prefix_class);
		return TMS_APIFail;
	}
	// abort if new hierarchy contains only prefix
	int onlyPrefix = StringContainsOnlyPrefix(prefix_class, nhierarchy);
	if (onlyPrefix) {
		sprintf(errorMessage, translate("Given hierarchy must not be blanck after prefix: %s"), prefix_class);
		return TMS_APIFail;
	}

	// check if new hierarchy name exists in DB
	QC->reset_name_scope();
	int nhierarchySySId = QC->set_current_node(nhierarchy);
	if (nhierarchySySId>=0) {
		sprintf(errorMessage,"%s %s",nhierarchy,translate(OBJECT_EXISTS));
		return TMS_APIFail;
	}

	ret = TermPrefixfHierarchy(ohierarchy,prefix_term,errorMessage);
	if (ret==TMS_APIFail) return TMS_APIFail;

	tmp = strchr(nhierarchy,'`');
	if (tmp==NULL) {
		abort_rename(ohierarchy,nhierarchy,errorMessage); return TMS_APIFail;}
	int k;
	for (k=1;k<strlen(tmp);k++)
		ntopterm_t[k-1] = tmp[k];

	ntopterm_t[k-1] = '\0';

	sprintf(ntopterm,"%s%s",prefix_term,ntopterm_t);

	QC->reset_name_scope();
	int ntoptermSySId = QC->set_current_node(ntopterm);
	if (ntoptermSySId>=0){
		sprintf(errorMessage,"%s %s",ntopterm,translate(OBJECT_EXISTS));
		return TMS_APIFail; }

	ret = ThesaurusNamefHierarchy(ohierarchy,thesaurus_name,errorMessage);
	if (ret==TMS_APIFail) return TMS_APIFail;

	int i=0;
	for (i=0;i<strlen(thesaurus_name) && thesaurus_name[i] != '\0';i++)
		thesaurus_name_l[i] = tolower(thesaurus_name[i]);

	thesaurus_name_l[i] = '\0';

	// looking for MERIMEETopTerm
	int card;
	strcpy(givenClass, userOperation); // MERIMEE
	strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
	ret = GetThesaurusObject(top_term_class, NULL, "TopTerm", NULL, givenClass, &card);
	if (ret==TMS_APIFail) return TMS_APIFail;

	// sprintf(top_term_class,"%s%s",thesaurus_name, translate("TopTerm"));
	//TO_DO : to be taken by the rule!!!!
	l_name belongs_category_tmp;
	sprintf(belongs_category_tmp, "belongs_to_%s_hierarchy",thesaurus_name_l);
	strcpy(belongs_category, translate(belongs_category_tmp));

	QC->reset_name_scope();
	QC->set_current_node(ohierarchy);

	int ret_set1 = QC->get_link_to_by_category(0,top_term_class,belongs_category);
	if ((ret_set1==-1) || (QC->set_get_card(ret_set1)!=1))
	{ abort_rename(ohierarchy,nhierarchy,errorMessage); return TMS_APIFail;}

	int ret_set2 = QC->get_from_value(ret_set1);
	QC->free_set(ret_set1);
	if (ret_set2==-1) {
		abort_rename(ohierarchy,nhierarchy,errorMessage); return TMS_APIFail;}

	QC->reset_set(ret_set2);
	ret = QC->return_nodes(ret_set2,otopterm);
	QC->free_set(ret_set2);
	if (ret==-1) { abort_rename(ohierarchy,nhierarchy,errorMessage); return TMS_APIFail;}

	IDENTIFIER Iohierarchy;
	strcpy(Iohierarchy.loginam,ohierarchy);
	Iohierarchy.tag = ID_TYPE_LOGINAM;

	IDENTIFIER Inhierarchy;
	strcpy(Inhierarchy.loginam,nhierarchy);
	Inhierarchy.tag = ID_TYPE_LOGINAM;

	IDENTIFIER Iotopterm;
	strcpy(Iotopterm.loginam,otopterm);
	Iotopterm.tag = ID_TYPE_LOGINAM;

	IDENTIFIER Intopterm;
	strcpy(Intopterm.loginam,ntopterm);
	Intopterm.tag = ID_TYPE_LOGINAM;

	ret = QC->Rename_Node(&Iohierarchy,&Inhierarchy);
	if (ret==APIFail) {
		abort_rename(ohierarchy,nhierarchy,errorMessage); return TMS_APIFail;}

	ret = QC->Rename_Node(&Iotopterm,&Intopterm);
	if (ret==APIFail) {
		abort_rename(ohierarchy,nhierarchy,errorMessage); return TMS_APIFail;}

	commit_rename(Iohierarchy.loginam,Inhierarchy.loginam,errorMessage);
	return TMS_APISucc;        
        */
        // </editor-fold> 
        
	StringObject thesaurus_name = new StringObject();
	StringObject thesaurus_name_l = new StringObject();
	StringObject prefix_term = new StringObject();
	StringObject otopterm = new StringObject();
	StringObject ntopterm = new StringObject();
	StringObject ntopterm_t = new StringObject();
	StringObject top_term_class = new StringObject();
	//StringObject belongs_category = new StringObject();
	StringObject tmp = new StringObject();
	int ret;

        if(isStringObjectEmpty(nhierarchy)){
            //sprintf(errorMessage,"%s %s",translate(EMPTY_STRING),translate("New name")); 
            errorMessage.setValue(String.format("%s %s", EMPTY_STRING, "New name"));
            return TMS_APIFail; 
        }
	
        if(isStringObjectEmpty(ohierarchy)){
            //sprintf(errorMessage,"%s %s",translate(EMPTY_STRING),translate("New name")); 
            errorMessage.setValue(String.format("%s %s", EMPTY_STRING, "Old name"));
            return TMS_APIFail; 
        }
		

	// check if old hierarchy name exists in DB
	QC.reset_name_scope();
	long ohierarchySySIdL = QC.set_current_node(ohierarchy);
	if (ohierarchySySIdL<0) {
            //sprintf(errorMessage,"%s %s",ohierarchy, translate(OBJECT_DOES_NOT_EXIST));
            errorMessage.setValue(String.format("%s %s", ohierarchy.getValue(), OBJECT_DOES_NOT_EXIST));
            return TMS_APIFail;
	}

	// check if old hierarchy is a hierarchy of current thesaurus
	// if it belongs to MERIMEEThesaurusClass
	// looking for MERIMEEThesaurusClass
	StringObject givenClass = new StringObject(userOperation.getValue()+"ThesaurusClass");
	//strcpy(givenClass, userOperation); // MERIMEE
	//strcat(givenClass, "ThesaurusClass"); // MERIMEEThesaurusClass
	ret = ObjectinClass(ohierarchy, givenClass, errorMessage);
	if (ret == TMS_APIFail){
            //sprintf(errorMessage,translate("%s is not a %s hierarchy"), ohierarchy, userOperation);
            errorMessage.setValue(String.format("%s is not a %s hierarchy", ohierarchy.getValue(), userOperation.getValue()));
            return TMS_APIFail;
	}

	// check if new hierarchy contains the correct prefix
	// get the correct prefixes
	StringObject thesaurus = new StringObject();
        StringObject prefix_class = new StringObject();
	ret = GetThesaurusName(thesaurus);
	if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }

	ret = ClassPrefixfThesaurus(thesaurus,prefix_class,errorMessage);
	if (ret==TMS_APIFail){
            return TMS_APIFail;
        }

	// abort if new hierarchy has not the correct prefix
	//if (strncmp(prefix_class, nhierarchy, strlen(prefix_class))) { // "str" does not contain "prefix"
        if(nhierarchy.getValue().startsWith(prefix_class.getValue())==false){
            //sprintf(errorMessage, translate("%s has not the correct prefix: %s"), nhierarchy, prefix_class);
            errorMessage.setValue(String.format("%s has not the correct prefix: %s", nhierarchy.getValue(), prefix_class.getValue()));
            return TMS_APIFail;
	}
        
	// abort if new hierarchy contains only prefix
	boolean onlyPrefix = prefix_class.getValue().equals(nhierarchy.getValue());//StringContainsOnlyPrefix(prefix_class, nhierarchy);
	if (onlyPrefix) {
            //sprintf(errorMessage, translate("Given hierarchy must not be blanck after prefix: %s"), prefix_class);
            errorMessage.setValue(String.format("Given hierarchy must not be blanck after prefix: %s", prefix_class.getValue()));
            return TMS_APIFail;
	}

	// check if new hierarchy name exists in DB
	QC.reset_name_scope();
	long nhierarchySySIdL = QC.set_current_node(nhierarchy);
	if (nhierarchySySIdL>=0) {
            //sprintf(errorMessage,"%s %s",nhierarchy,translate(OBJECT_EXISTS));
            errorMessage.setValue(String.format("%s %s", nhierarchy.getValue(), OBJECT_EXISTS));
            return TMS_APIFail;
	}

	ret = TermPrefixfHierarchy(ohierarchy,prefix_term,errorMessage);
	if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }

        if(nhierarchy.getValue().contains("`")==false){
            abort_rename(ohierarchy,nhierarchy,errorMessage); 
            return TMS_APIFail;
        }
        
	//tmp = strchr(nhierarchy,'`');
	//if (tmp==NULL) {
	//abort_rename(ohierarchy,nhierarchy,errorMessage); 
            //return TMS_APIFail;
        //}	
	//int k;
	//for (k=1;k<strlen(tmp);k++)
	//	ntopterm_t[k-1] = tmp[k];

	//ntopterm_t[k-1] = '\0';
        ntopterm_t.setValue(nhierarchy.getValue().split("`")[1]);
        ntopterm.setValue(prefix_term.getValue()+ntopterm_t.getValue());
	//sprintf(ntopterm,"%s%s",prefix_term,ntopterm_t);

	QC.reset_name_scope();
	long ntoptermSySIdL = QC.set_current_node(ntopterm);
	if (ntoptermSySIdL>=0){
            //sprintf(errorMessage,"%s %s",ntopterm,translate(OBJECT_EXISTS));
            errorMessage.setValue(String.format("%s %s", ntopterm.getValue(), OBJECT_EXISTS));
            return TMS_APIFail; 
        }
        
	ret = ThesaurusNamefHierarchy(ohierarchy,thesaurus_name,errorMessage);
	if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }

	//int i=0;
	//for (i=0;i<strlen(thesaurus_name) && thesaurus_name[i] != '\0';i++)
	//	thesaurus_name_l[i] = tolower(thesaurus_name[i]);

	//thesaurus_name_l[i] = '\0';

        //prefix_thesaurus_l.setValue(thesaurus_name.getValue().substring(0, thesaurus_name.getValue().length()-1).toLowerCase());
        thesaurus_name_l.setValue(thesaurus_name.getValue().substring(0, thesaurus_name.getValue().length()).toLowerCase());
        
	// looking for MERIMEETopTerm
	IntegerObject card = new IntegerObject();
        givenClass.setValue(userOperation.getValue()+"ThesaurusNotionType");
	//strcpy(givenClass, userOperation); // MERIMEE
	//strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
	ret = GetThesaurusObject(top_term_class, null, new StringObject("TopTerm"), null, givenClass, card);
	if (ret==TMS_APIFail) {
            return TMS_APIFail;
        }

	// sprintf(top_term_class,"%s%s",thesaurus_name, translate("TopTerm"));
	//TO_DO : to be taken by the rule!!!!
	//l_name belongs_category_tmp;
	//sprintf(belongs_category_tmp, "belongs_to_%s_hierarchy",thesaurus_name_l);
	//strcpy(belongs_category, translate(belongs_category_tmp));

        StringObject belongs_category = new StringObject(String.format("belongs_to_%s_hierarchy", thesaurus_name_l.getValue()));
	QC.reset_name_scope();
	QC.set_current_node(ohierarchy);

	int ret_set1 = QC.get_link_to_by_category(0,top_term_class,belongs_category);
	if ((ret_set1==-1) || (QC.set_get_card(ret_set1)!=1)){ 
            abort_rename(ohierarchy,nhierarchy,errorMessage); 
            return TMS_APIFail;
        }

	int ret_set2 = QC.get_from_value(ret_set1);
	QC.free_set(ret_set1);
	if (ret_set2==-1) {
            abort_rename(ohierarchy,nhierarchy,errorMessage); 
            return TMS_APIFail;
        }

	QC.reset_set(ret_set2);
	ret = QC.return_nodes(ret_set2,otopterm);
	QC.free_set(ret_set2);
	if (ret==-1) { 
            abort_rename(ohierarchy,nhierarchy,errorMessage); 
            return TMS_APIFail;
        }

        Identifier Iohierarchy = new Identifier(ohierarchySySIdL);
	//IDENTIFIER Iohierarchy;
	//strcpy(Iohierarchy.loginam,ohierarchy);
	//Iohierarchy.tag = ID_TYPE_LOGINAM;
        Identifier Inhierarchy = new Identifier(nhierarchy.getValue());

	//IDENTIFIER Inhierarchy;
	//strcpy(Inhierarchy.loginam,nhierarchy);
	//Inhierarchy.tag = ID_TYPE_LOGINAM;

        Identifier Iotopterm = new Identifier(otopterm.getValue());
	//IDENTIFIER Iotopterm;
	//strcpy(Iotopterm.loginam,otopterm);
	//Iotopterm.tag = ID_TYPE_LOGINAM;

        Identifier Intopterm = new Identifier(ntopterm.getValue());
	//IDENTIFIER Intopterm;
	//strcpy(Intopterm.loginam,ntopterm);
	//Intopterm.tag = ID_TYPE_LOGINAM;

	ret = QC.CHECK_Rename_Node(Iohierarchy,Inhierarchy);
	if (ret==QClass.APIFail) {
            abort_rename(ohierarchy,nhierarchy,errorMessage); 
            return TMS_APIFail;
        }

	ret = QC.CHECK_Rename_Node(Iotopterm,Intopterm);
	if (ret==QClass.APIFail) {
            abort_rename(ohierarchy,nhierarchy,errorMessage); 
            return TMS_APIFail;
        }
        
	commit_rename(ohierarchy,nhierarchy,errorMessage);
	return TMS_APISucc;
    }
    
    public int  CHECK_RenameNewDescriptor(StringObject descriptorName, StringObject newDescriptorName){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
-------------------------------------------------------------------
tms_api::RenameNewDescriptor()
---------------------------------------------------------------------
INPUT: - descriptorName : the name of a descriptor
newDescriptorName : the name of a new descriptor
OUTPUT: - TMS_APISucc in case the given descriptor is renamed succesfully
- TMS_APIFail in case:
· A descriptor with the same new name already exists
· OldDescriptorName is not the name of any existing descriptor
of currently used thesaurus.
· Input NewDescriptorName does not contain the correct prefix
for a descriptor of currently used thesaurus.
FUNCTION: renames the given descriptor to newDescriptorName
ATTENTION: permited only for NEW descriptors (belong to MERIMEENewDescriptor)
----------------------------------------------------------------
int tms_api::RenameNewDescriptor(char *descriptorName, char *newDescriptorName)
{
	// check if given descriptor exists in data base
	QC->reset_name_scope();
	int descriptorSysid;
	if ((descriptorSysid = QC->set_current_node(descriptorName)) < 0) {
		sprintf(errorMessage, translate("%s does not exist in data base"), descriptorName);
		return TMS_APIFail;
	}

	// check if given new descriptor does not exist in data base
	QC->reset_name_scope();
	if (QC->set_current_node(newDescriptorName) > 0) {
		sprintf(errorMessage, translate("%s exists in data base"), newDescriptorName);
		return TMS_APIFail;
	}

	// get the correct prefix of terms of current thesaurus
	l_name prefix_class, thesaurus;
	GetThesaurus(thesaurus, errorMessage);
	if (TermPrefixfThesaurus(thesaurus, prefix_class, errorMessage) == TMS_APIFail) return TMS_APIFail;

	// abort if new descriptor has not the correct prefix
	if (strncmp(prefix_class, newDescriptorName, strlen(prefix_class))) {
		sprintf(errorMessage, translate("%s has not the correct prefix: %s"), newDescriptorName, prefix_class);
		//abort_transaction();
		return TMS_APIFail;
	}
	// abort if new descriptor contains only prefix
	int onlyPrefix = StringContainsOnlyPrefix(prefix_class, newDescriptorName);
	if (onlyPrefix) {
		sprintf(errorMessage, translate("Given facet must not be blanck after prefix: %s"), prefix_class);
		//abort_transaction();
		return TMS_APIFail;
	}

	// looking for MERIMEENewDescriptor
	int card;
	l_name givenClass, thes_new_descriptor;
	strcpy(givenClass, userOperation); // MERIMEE
	strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
	int ret = GetThesaurusObject(thes_new_descriptor, NULL, "NewDescriptor", NULL, givenClass, &card);
	if (ret==TMS_APIFail) {
		sprintf(errorMessage, translate("Failed to refer to class <%s%s>"), userOperation, "NewDescriptor");
		return TMS_APIFail;
	}

	// check if given descriptor is a new descriptor of current thesaurus
	// if it belongs to MERIMEENewDescriptor
	ret = ObjectinClass(descriptorName, thes_new_descriptor, errorMessage);
	if (ret == TMS_APIFail){
		sprintf(errorMessage,translate("%s is not a %s descriptor"), descriptorName, thes_new_descriptor);
		return TMS_APIFail;
	}

	IDENTIFIER I_current_name(descriptorName);
	IDENTIFIER I_new_name(newDescriptorName);

	ret = QC->Rename_Node(&I_current_name, &I_new_name);
	if (ret==APIFail) {abort_rename(descriptorName, newDescriptorName, errorMessage); return TMS_APIFail;}

	commit_rename(descriptorName, newDescriptorName, errorMessage);
	return TMS_APISucc;
}        
        */
        // </editor-fold> 
        
	// check if given descriptor exists in data base
	QC.reset_name_scope();
	long descriptorSysidL = QC.set_current_node(descriptorName);
	if (descriptorSysidL < 0) {
            //sprintf(errorMessage, translate("%s does not exist in data base"), descriptorName);
            errorMessage.setValue(String.format("%s does not exist in data base", descriptorName.getValue()));
            return TMS_APIFail;
	}

	// check if given new descriptor does not exist in data base
	QC.reset_name_scope();
        if (QC.set_current_node(newDescriptorName) > 0) {
            //sprintf(errorMessage, translate("%s exists in data base"), newDescriptorName);
            errorMessage.setValue(String.format("%s exists in data base", newDescriptorName.getValue()));
            return TMS_APIFail;
	}

	// get the correct prefix of terms of current thesaurus
	//l_name prefix_class, thesaurus;
        StringObject prefix_class = new StringObject();
        StringObject thesaurus = new StringObject();
	int ret = GetThesaurusName(thesaurus);
        if(ret==TMS_APIFail){
            return TMS_APIFail;
        }
	if (TermPrefixfThesaurus(thesaurus.getValue(), prefix_class, errorMessage) == TMS_APIFail) {
            return TMS_APIFail;
        }

	// abort if new descriptor has not the correct prefix
	//if (strncmp(prefix_class, newDescriptorName, strlen(prefix_class))) {
        if(newDescriptorName.getValue().startsWith(prefix_class.getValue())==false){
            //sprintf(errorMessage, translate("%s has not the correct prefix: %s"), newDescriptorName, prefix_class);
            errorMessage.setValue(String.format("%s has not the correct prefix: %s", newDescriptorName.getValue(),prefix_class.getValue()));
            //abort_transaction();
            return TMS_APIFail;
	}
	// abort if new descriptor contains only prefix
	boolean onlyPrefix = prefix_class.getValue().equals(newDescriptorName.getValue());//StringContainsOnlyPrefix(prefix_class, newDescriptorName);
	if (onlyPrefix) {
            //sprintf(errorMessage, translate("Given facet must not be blanck after prefix: %s"), prefix_class);
            errorMessage.setValue(String.format("Given facet must not be blanck after prefix: %s", prefix_class.getValue()));
            //abort_transaction();
            return TMS_APIFail;
	}

	// looking for MERIMEENewDescriptor
	IntegerObject card = new IntegerObject(0);
        StringObject thes_new_descriptor = new StringObject();
        StringObject givenClass = new StringObject(userOperation.getValue()+"ThesaurusNotionType");
	//l_name givenClass, thes_new_descriptor;
	//strcpy(givenClass, userOperation); // MERIMEE
	//strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
	ret = GetThesaurusObject(thes_new_descriptor, null, new StringObject("NewDescriptor"), null, givenClass, card);
	if (ret==TMS_APIFail) {
            //sprintf(errorMessage, translate("Failed to refer to class <%s%s>"), userOperation, "NewDescriptor");
            errorMessage.setValue(String.format("Failed to refer to class <%s%s>", userOperation.getValue(),"NewDescriptor"));
            return TMS_APIFail;
	}

	// check if given descriptor is a new descriptor of current thesaurus
	// if it belongs to MERIMEENewDescriptor
	ret = ObjectinClass(descriptorName, thes_new_descriptor, errorMessage);
	if (ret == TMS_APIFail){
            //sprintf(errorMessage,translate("%s is not a %s descriptor"), descriptorName, thes_new_descriptor);
            errorMessage.setValue(String.format("%s is not a %s descriptor", descriptorName.getValue(), thes_new_descriptor.getValue()));
            return TMS_APIFail;
	}
        
        Identifier I_current_name = new Identifier(descriptorSysidL);
        Identifier I_new_name = new Identifier(newDescriptorName.getValue());
	//IDENTIFIER I_current_name(descriptorName);
	//IDENTIFIER I_new_name(newDescriptorName);

	ret = QC.CHECK_Rename_Node(I_current_name, I_new_name);
	if (ret==QClass.APIFail) {
            abort_rename(descriptorName, newDescriptorName, errorMessage); 
            return TMS_APIFail;
        }

	commit_rename(descriptorName, newDescriptorName, errorMessage);
	return TMS_APISucc;

    }
    
    // functions for setting/getting the name of the thesaurus that
    // TMSApi programmer will work with
    public int  SetThesaurusName(String userOp){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
            tms_api::SetThesaurusName()
        ----------------------------------------------------------------
	INPUT: - userOp : the name of a new thesaurus to work with
	OUTPUT: - TMS_APISucc in case given thesaurus name exists
   			 in data base as a name of an existing thesaurus
           - TMS_APIFail otherwise
	FUNCTION: checks if given thesaurus name exists in data base
   			 as a name of an existing thesaurus (f.e. MERIMEE)
	ATTENTION: the given thesaurus name must be without it's
   			  prefix, as it exists in data base
              (f.e. must be given as MERIMEE and not as Thesaurus`MERIMEE)
        ----------------------------------------------------------------
int tms_api::SetThesaurusName(char *userOp)
{
	// check if input thesaurus name exists in data base
   // as a name of an existing thesaurus
   int i = 0;
   while (*(ThesaurusNames[i])) {
   	if (!strcmp(userOp, ThesaurusNames[i])) {
      	strcpy(userOperation, userOp);
         // inform member userOperationLow with the lower case version of the nput thesaurus name (merimme)
			int length = strlen(userOperation);
			for (int i=0; i<length; i++) {
				userOperationLow[i] = tolower(userOperation[i]);
         }
			userOperationLow[length] = 0;
         FillArraysOfAllowedCategories();
         return TMS_APISucc;
      }
      i++;
   }
   // error input thesaurus name
   sprintf(errorMessage, "Given thesaurus name: <%s> is not the name of any existing thesaurus.\n", userOp);
   strcat(errorMessage, "Available existing thesaurus names:\n");
   i = 0;
   while (*(ThesaurusNames[i])) {
		strcat(errorMessage, ThesaurusNames[i]);
      strcat(errorMessage, "\n");
      i++;
   }
	return TMS_APIFail;
}
        */
        // </editor-fold> 
        
        if(ThesaurusNames.contains(userOp)){
            this.userOperation.setValue(userOp);
            this.userOperationLow.setValue(userOp.toLowerCase());
            FillArraysOfAllowedCategories();
            return TMS_APISucc;
        }
        String thesNamesStr = ThesaurusNames.toString().replace("[", "").replace("]", "").replaceAll(", ", "\n");
        errorMessage.setValue("Given thesaurus name: <%s> is not the name of any existing thesaurus.\n");
        errorMessage.setValue(errorMessage.getValue()+ "Available existing thesaurus names:\n"+thesNamesStr);
        
        return TMS_APIFail;
    }
    
    public int  GetThesaurusName(StringObject userOp){
        //
        
/*
tms_api::GetThesaurus()
	INPUT: - thesaurus : an allocated string
	OUTPUT: - TMS_APISucc
	FUNCTION: returns the currently used data base thesaurus name
				 (for example: Thesaurus`MERIMEE)

int tms_api::GetThesaurus(char *thesaurus, char *message)
{
  sprintf(thesaurus,"%s%s","Thesaurus`",userOperation);
  message[0] = '\0';

  return TMS_APISucc;
}
*/
        userOp.setValue("Thesaurus`"+userOperation.getValue());
        return TMS_APISucc;
    }
    public int  GetThesaurusNameWithoutPrefix(StringObject userOp){
        //
        
/*
tms_api::GetThesaurus()
	INPUT: - thesaurus : an allocated string
	OUTPUT: - TMS_APISucc
	FUNCTION: returns the currently used data base thesaurus name
				 (for example: Thesaurus`MERIMEE)

int tms_api::GetThesaurus(char *thesaurus, char *message)
{
  sprintf(thesaurus,"%s%s","Thesaurus`",userOperation);
  message[0] = '\0';

  return TMS_APISucc;
}
*/
        userOp.setValue(userOperation.getValue());
        return TMS_APISucc;
    }
    
    public int  NOT_IMPLEMENTED_UndoAbandonFacet(StringObject facet){
        throw new UnsupportedOperationException();
    }
    
    public int  NOT_IMPLEMENTED_UndoAbandonHierarchy(StringObject hierarchy){
        throw new UnsupportedOperationException();
    }
    
    public int  NOT_IMPLEMENTED_UndoRenameDescriptor(StringObject descriptor){
        throw new UnsupportedOperationException();
    }
    
    // error messages
    public int  ALMOST_DONE_GetTMS_APIErrorMessage(StringObject message){
        //ALSO USED FROM WEBTMS API but gets access directly to the erroMSg object
        
        // <editor-fold defaultstate="collapsed" desc="JNI Code">
        /*
        Class:     sisapi_TMSAPIClass
        Method:    GetTMS_APIErrorMessage
        Signature: (ILsisapi/StringObject;)I

       JNIEXPORT jint JNICALL Java_sisapi_TMSAPIClass_GetTMS_1APIErrorMessage
         (JNIEnv *env, jobject, jint TMSsessionID, jobject jmess)
       {
               char *mess = GetTMS_APIErrorMessage(TMSsessionID);
               stringObjectC2J(env, mess, jmess, MAX_TMS_API_ERROR_MESSAGE_LEN);
               return TMS_APISucc;
       }
       */
        // </editor-fold> 
        
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        char *tms_api::GetTMS_APIErrorMessage()
{
	// get message from globalError if any
	char message[MAX_TMS_API_ERROR_MESSAGE_LEN], full_message[MAX_TMS_API_ERROR_MESSAGE_LEN];
   char *translated_message;
   int NumOfStrParams, StrSize, NumOfIntParams;
   QC->get_user_error_message(message, &NumOfStrParams, &StrSize, &NumOfIntParams);
   int server_error_message_exists = (*message) ? 1 : 0;
   char *StrParams;
   int *IntParams;
   StrParams = (char*)malloc(NumOfStrParams*StrSize);
   IntParams = (int*)malloc(NumOfIntParams*sizeof(int));
   QC->get_user_error_params(StrParams, NumOfStrParams, StrSize, IntParams, NumOfIntParams);
   translated_message = translate(message);
	strcpy(full_message, "Uknown error message");
   ConstructMessage(full_message, translated_message, StrParams, IntParams, NumOfStrParams, StrSize, NumOfIntParams);
	QC->reset_user_error_message();
   //WarningMsg(full_message);

   if (server_error_message_exists) {
	   strcat(errorMessage, "\nSERVER ERROR MESSAGE:\n");
      strcat(errorMessage, full_message);
   }
	return errorMessage;
}
        */
        // </editor-fold>
        message.setValue(errorMessage.getValue());
        return TMS_APISucc;
    }
    
    public int  CHECK_CreateDescriptorAttribute(StringObject linkName, StringObject descriptorName, CMValue toValue, int catSet){
        //ALSO USED FROM WEBTMS API
        // <editor-fold defaultstate="collapsed" desc="Comments...">    
        /*-------------------------------------------------------------------
        tms_api::CreateDescriptorAttribute()
        ---------------------------------------------------------------------
        INPUT: - linkName : the name of the new attribute ('\0' or NULL for unnamed)
        descriptorName : the from-value of the new attribute
        toValue : the to-value of the new attribute
        catSet : the set with the categories of the attribute
        OUTPUT: - TMS_APISucc in case the given link is created succesfully
        - TMS_APIFail in case:
        · descriptorName is not the name of any existing released descriptor of
        currently used  thesaurus.
        · The given catSet contains categories which are not allowed
        to be used for the creation of a released descriptor attribute.
        Available categories for the creation of a released concept attribute
        (<thes_nameU> and <thes_nameL> are the upper and lower case
        names of the currently selected thesaurus,
        for example: MERIMEE, merimee):
        Category from-class					Category name
        -------------------					------------
        <thes_ nameU>HierarchyTerm			<thes_ nameU>_ALT
        <thes_ nameU>HierarchyTerm			<thes_nameL>_display
        <thes_ nameU>HierarchyTerm			<thes_nameL>_editor
        <thes_ nameU>HierarchyTerm			<thes_nameL>_found_in
        <thes_ nameU>HierarchyTerm			<thes_nameL>_modified
        <thes_ nameU>HierarchyTerm			<thes_nameL>_found_in
        <thes_ nameU>HierarchyTerm			<thes_ nameU>_RT
        <thes_ nameU>HierarchyTerm			<thes_ nameU>_UF
        <thes_ nameU>ThesaurusConcept		<thes_ nameU>_translation, to_EN
        <thes_ nameU>ThesaurusConcept		<thes_ nameU>_translation, to_EL
        <thes_ nameU>ThesaurusConcept		<thes_ nameU>_translation, to_IT
        FUNCTION: creates the given attribute
        ATTENTION: permited only for released descriptors (belong to MERIMEEDescriptor)
        ----------------------------------------------------------------*/
        //</editor-fold >
        //<editor-fold defaultstate="collapsed" desc="C++ code...">   
        /*int tms_api::CreateDescriptorAttribute(char *linkName, char *descriptorName, cm_value *toValue, int catSet)
        {
                int level = -1;
                CreateDescriptorAttribute(linkName, descriptorName, toValue, level, catSet, CREATION_FOR_VERSIONED_DESCRIPTOR);
        }*/
            
        //</editor-fold >
        int level = -1; 
        if(CreateDescAttr(linkName, descriptorName, toValue, level, catSet, CREATION_FOR_VERSIONED_DESCRIPTOR) == TMS_APIFail){
            //errorMessage.setValue(errorMessage.getValue().concat(WCD.errorMessage.getValue()));
            return  TMS_APIFail;
        }
        else{
            //errorMessage.setValue(errorMessage.getValue().concat(""));
            return  TMS_APISucc;
        }
    }
    
    void abort_create(StringObject nobject, StringObject message){
        //<editor-fold defaultstate="collapsed" desc="C++ code...">
        /*
        void tms_api::abort_create(char *nobject,char *message)
        {
          sprintf(message,translate("Failed to add %s in database.Creation failed"),nobject);
          //abort_transaction();
        }
        */
        //</editor-fold>
        //sprintf(message,translate("Failed to add %s in database.Creation failed"),nobject);
        message.setValue("Αποτυχία προσθήκης του κόμβου " + nobject.getValue() + " στην βάση. Η δημιουργία απέτυχε.");
    }
    
    void abort_create_attribute(StringObject fobject, StringObject lobject, StringObject message) {
   
        //<editor-fold defaultstate="collapsed" desc="C++ code...">   
        /*
        //operation : create attribute
        void tms_api::abort_create_attribute(char *fobject, char *lobject, char *message)
        {
          sprintf(message,translate("Failed to create link %s from %s"),lobject,fobject);
          //abort_transaction();
        }
        */
        //</editor-fold>
        message.setValue("Αποτυχία δημιουργίας συνδέσμου " + lobject.getValue() + " από τον κόμβο " + fobject.getValue());
        //abort_transaction();

    }
    
    void abort_classify(StringObject objectName, StringObject className, StringObject message){
      //sprintf(message,translate("Failed to classify %s under %s"), objectName, className);
        message.setValue(String.format("Failed to classify %s under %s",objectName.getValue(), className.getValue()));
    }

    void abort_declassify(StringObject objectName, StringObject className, StringObject message){
        //sprintf(message,translate("Failed to declassify %s under %s"), objectName, className);
        message.setValue(String.format("Failed to declassify %s under %s",objectName.getValue(), className.getValue()));
    }
        
    void abort_delete_attribute(StringObject fobject, long linkSysidL, StringObject message){
        //<editor-fold defaultstate="collapsed" desc="C++ code..."> 
        /*
        void tms_api::abort_delete_attribute(char *fobject, int linkSysid, char *message)
        {
          sprintf(message,translate("Failed to delete link (%d) from %s"),linkSysid,fobject);
          //abort_transaction();
        }
        */
        //</editor-fold>
        message.setValue("Αποτυχία διαγραφής συνδέσμου " + linkSysidL + " από τον κόμβο " + fobject.getValue());
        
    }
    
    void abort_get_comment_size(StringObject objectName, StringObject message){
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*
        void tms_api::abort_get_comment_size(char *objectName, char *message)
        {
          sprintf(message,translate("Failed to get the comment size of %s"), objectName);
        } 
         */
        //</editor-fold> 
        message.setValue("Αποτυχία κατά την ανάκτηση μεγέθους του σχόλιου από τον περιγραφέα " + objectName.getValue());
    }
    
    void abort_get_comment(StringObject objectName, StringObject message){
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*
        void tms_api::abort_get_comment(char *objectName, char *message)
        {
          sprintf(message,translate("Failed to get the comment of %s"), objectName);
        }

         */
        //</editor-fold> 
        message.setValue("Αποτυχία κατά την ανάκτηση σχόλιου από τον περιγραφέα " + objectName.getValue());
    }
    
    void abort_del_comment(StringObject objectName, StringObject message){
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*
        void tms_api::abort_del_comment(char *objectName, char *message)
        {
          sprintf(message,translate("Failed to delete the comment of %s"), objectName);
        }
         */
        //</editor-fold> 
        message.setValue("Αποτυχία διαγραφής σχολίου από τον περιγραφέα " + objectName.getValue());
    }
    
    void abort_set_comment(StringObject objectName, StringObject message){
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*
        void tms_api::abort_set_comment(char *objectName, char *message)
        {
          sprintf(message,translate("Failed to set the comment of %s"), objectName);
        }
         */
        //</editor-fold> 
        message.setValue("Αποτυχία κατά την ενημέρρωση τους σχολίου του περιγραφέα " + objectName.getValue());
    }
    
    void abort_move_to_hierarchy(StringObject nobject,StringObject to_nobject,StringObject message){
        //sprintf(message,translate("%s is not moved to %s"),nobject,to_nobject);
        message.setValue(String.format("%s is not moved to %s", nobject.getValue(),to_nobject.getValue()));
        //abort_transaction();
    }
    //operation : rename object
    void abort_rename(StringObject oobject,StringObject nobject,StringObject message){
        //sprintf(message,translate("Failed to rename %s to %s"),oobject,nobject);
        message.setValue(String.format("Failed to rename %s to %s", oobject.getValue(),nobject.getValue()));
        //abort_transaction();
    }
    
    //operation : delete object
    void abort_delete(StringObject nobject, StringObject message) {
        //sprintf(message,translate("Failed to delete %s from database.Deletion failed"),nobject);
        message.setValue(String.format("Failed to delete %s from database.Deletion failed", nobject.getValue()));
        //abort_transaction();
    }

    void commit_delete(StringObject nobject, StringObject message) {
        //sprintf(message,translate("<%s> deleted from the database"),nobject);
        //  end_transaction();
        //ELIAS BUG FIX errorMsg seems to be filled by ObjectInClass when no error has actuallyy ocuured until now
        if(message.getValue()!=null && message.getValue().length()>0){            
            message.setValue("");
        }
    }
    
    void commit_rename(StringObject nobject,StringObject nnobject,StringObject message){
        //sprintf(message,translate("%s renamed to %s"),nobject,nnobject);
        //  end_transaction();
    }
    
    void commit_move_to_hierarchy(StringObject nobject,StringObject to_nobject,StringObject message){
        //ELIAS BUG FIX errorMsg seems to be filled by ObjectInClass when no error has ocuured until now
        if(message.getValue()!=null && message.getValue().length()>0){
            
            //Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO,"Deleting probably useless error Message from commit_move_to_hierarchy: "+ message.getValue() );
            message.setValue("");
        }
        //sprintf(message,translate("%s moved to %s"),nobject,to_nobject);
        //  end_transaction();
    }


    void commit_get_comment_size(StringObject objectName, StringObject message){
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*
        void tms_api::commit_get_comment_size(char *objectName, char *message)
        {
          sprintf(message,translate("Succeeded to get the comment size of %s"), objectName);
        }
         */
        //</editor-fold> 
        //message.setValue("Επιτυχής ανάκτηση μεγέθους σχόλιου από τον περιγραφέα " + objectName.getValue());
        //Commit messages deleted because at some points code checks if errorMsg is empty in order to find out whether an error occured or not
    }
         
    void commit_get_comment(StringObject objectName, StringObject message){
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*
        void tms_api::commit_get_comment(char *objectName, char *message)
        {
          sprintf(message,translate("Succeeded to get the comment of %s"), objectName);
        }
         */
        //</editor-fold> 
        //message.setValue("Επιτυχής ανάκτηση σχόλιου από τον περιγραφέα " + objectName.getValue());
        //Commit messages deleted because at some points code checks if errorMsg is empty in order to find out whether an error occured or not
    }
     
    void commit_set_comment(StringObject objectName, StringObject message){
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*
        void tms_api::commit_set_comment(char *objectName, char *message)
        {
          sprintf(message,translate("Succeeded to set the comment of %s"), objectName);
        }
         */
        //</editor-fold> 
        //message.setValue("Επιτυχής ενημέρρωση τους σχολίου του περιγραφέα " + objectName.getValue());
        //Commit messages deleted because at some points code checks if errorMsg is empty in order to find out whether an error occured or not
    }
    
    void commit_del_comment(StringObject objectName, StringObject message){
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*
        void tms_api::commit_del_comment(char *objectName, char *message)
        {
          sprintf(message,translate("Succeeded to delete the comment of %s"), objectName);
        }
         */
        //</editor-fold> 
        //message.setValue("Επιτυχής διαγραφή σχολίου από τον περιγραφέα " + objectName.getValue());
        //Commit messages deleted because at some points code checks if errorMsg is empty in order to find out whether an error occured or not
    }
    
    void commit_classify(StringObject objectName, StringObject className, StringObject message){
      //sprintf(message,translate("Succeeded to classify %s under %s"), objectName, className);
        message.setValue(String.format("Succeeded to classify %s under %s",objectName.getValue(), className.getValue()));
    }
    
    void commit_create_attribute(StringObject fobject, StringObject lobject, StringObject message) {
        //<editor-fold defaultstate="collapsed" desc="C++ code...">   
        /*
        void tms_api::commit_create_attribute(char *fobject, char *lobject, char *message)
        {
        sprintf(message,translate("Succeded to create link %s from %s"),lobject,fobject);
        //  end_transaction();
        }
         */
        //</editor-fold>
        //message.setValue("Επιτυχία δημιουργίας συνδέσμου " + lobject.getValue() + " από τον κόμβο " + fobject.getValue());
        //Commit messages deleted because at some points code checks if errorMsg is empty in order to find out whether an error occured or not
    }
    
    void commit_declassify(StringObject objectName, StringObject className, StringObject message){
        //sprintf(message,translate("Succeeded to declassify %s under %s"), objectName, className);
        message.setValue(String.format("Succeeded to declassify %s under %s",objectName.getValue(), className.getValue()));
    }

    void commit_create(StringObject nobject, StringObject message){
        //<editor-fold defaultstate="collapsed" desc="C++ code...">
        /*
        void tms_api::commit_create(char *nobject,char *message)
        {
          sprintf(message,translate("%s added in database"),nobject);
        //  end_transaction();
        }
        */
        //</editor-fold>
        //sprintf(message,translate("%s added in database"),nobject);
    }
   
    void commit_delete_attribute(StringObject fobject, long linkSysidL, StringObject message){
        //<editor-fold defaultstate="collapsed" desc="C++ code...">
        /*
        void tms_api::commit_delete_attribute(char *fobject, int linkSysid, char *message)
        {
          sprintf(message,translate("Succeded to delete link (%d) from %s"),linkSysid,fobject);
        //  end_transaction();
        }
        */
        //</editor-fold>
        //message.setValue("Επιτυχία διαγραφής συνδέσμου " + linkSysid + " από τον κόμβο " + fobject.getValue());
        
    }
    
    public int  CHECK_CreateNewDescriptorAttribute(StringObject linkName, StringObject descriptorName, CMValue toValue, int catSet){
        //ALSO USED FROM WEBTMS API
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
    /*-------------------------------------------------------------------
    tms_api::CreateNewDescriptorAttribute()
    ---------------------------------------------------------------------
    INPUT: - linkName : the name of the new attribute ('\0' or NULL for unnamed)
    descriptorName : the from-value of the new attribute
    toValue : the to-value of the new attribute
    catSet : the set with the categories of the attribute
    OUTPUT: - TMS_APISucc in case the given link is created succesfully
    - TMS_APIFail in case:
    · descriptorName is not the name of any existing new descriptor of
    currently used  thesaurus.
    · The given catSet contains categories which are not allowed
    to be used for the creation of a new descriptor attribute.
    Available categories for the creation of a new concept attribute
    (<thes_nameU> and <thes_nameL> are the upper and lower case
    names of the currently selected thesaurus,
    for example: MERIMEE, merimee):
    Category from-class					Category name
    -------------------					------------
    <thes_ nameU>HierarchyTerm			<thes_ nameU>_ALT
    <thes_ nameU>HierarchyTerm			<thes_nameL>_display
    <thes_ nameU>HierarchyTerm			<thes_nameL>_editor
    <thes_ nameU>HierarchyTerm			<thes_nameL>_found_in
    <thes_ nameU>HierarchyTerm			<thes_nameL>_modified
    <thes_ nameU>HierarchyTerm			<thes_nameL>_found_in
    <thes_ nameU>HierarchyTerm			<thes_ nameU>_RT
    <thes_ nameU>HierarchyTerm			<thes_ nameU>_UF
    <thes_ nameU>ThesaurusConcept		<thes_ nameU>_translation, to_EN
    <thes_ nameU>ThesaurusConcept		<thes_ nameU>_translation, to_EL
    <thes_ nameU>ThesaurusConcept		<thes_ nameU>_translation, to_IT
    <thes_ nameU>HierarchyTerm			<thes_nameL>_created
    FUNCTION: creates the given attribute
    ATTENTION: permited only for NEW descriptors (belong to MERIMEENewDescriptor)
    ----------------------------------------------------------------*/
    //</editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">      
    /*int tms_api::CreateNewDescriptorAttribute(char *linkName, char *descriptorName, cm_value *toValue, int catSet)
    {
        int level = -1;
        CreateDescriptorAttribute(linkName, descriptorName, toValue, level, catSet, CREATION_FOR_NEW_DESCRIPTOR);
    }*/
     //</editor-fold>    
        
        int level = -1; 
        if(CreateDescAttr(linkName,  descriptorName,  toValue,  level,  catSet, CREATION_FOR_NEW_DESCRIPTOR) == TMS_APIFail){
            
            
            //errorMessage.setValue(errorMessage.getValue().concat(WCD.errorMessage.getValue()));
            return  TMS_APIFail;
        }
        else{
            //errorMessage.setValue(errorMessage.getValue().concat(""));
            return  TMS_APISucc;
        }

    }
    
    public int  CHECK_CreateUsedForTerm(StringObject term){
        //USED IN WEBTMS API ONLY
        int ret = CreateNode(term, CREATE_USED_FOR_TERM);
        return ret;
    }
   
    public int  CHECK_DeleteDescriptorAttribute(long linkSysidL, StringObject descriptorName){
        //USED IN WEBTMS API ONLY
        return DelDescrAttr(linkSysidL, descriptorName, DELETION_FOR_VERSIONED_DESCRIPTOR);
    }
    
    public int  CHECK_DeleteNewDescriptorAttribute(long linkSysidL, StringObject descriptorName){
        //USED IN WEBTMS API ONLY
        return DelDescrAttr(linkSysidL, descriptorName, DELETION_FOR_NEW_DESCRIPTOR);
    }
    
    public int  DeleteDescriptorComment(StringObject descriptorName, StringObject fromCommentCategory, StringObject commentCategory){
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*--------------------------------------------------------------
        tms_api::DeleteDescriptorComment()
        ----------------------------------------------------------------
        INPUT:  - descriptorName : the name of a descriptor
                - fromCommentCategory : the from class of a comment category
                - commentCategory : the name of a comment category
        OUTPUT: - TMS_APISucc in case the specified comment of the given descriptor
                  is deleted succesfully
                - TMS_APIFail otherwise
        FUNCTION: deletes the comment for the given descriptor. The kind of the
                  comment to be deleted is defined by the given comment category
                  (fromCommentCategory, commentCategory).
        ----------------------------------------------------------------*/
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*in file handle_comm.cpp
        int tms_api::DeleteDescriptorComment(char *descriptorName, char *fromCommentCategory, char *commentCategory)
        {
           int ret = Get_SetDescriptorComment(descriptorName, NULL, NULL, fromCommentCategory, commentCategory, DEL_DESCRIPTOR_COMMENT);
           return ret;
        }
        */
        // </editor-fold>         

        int ret = Get_SetDescriptorComment(descriptorName, null, null, fromCommentCategory, commentCategory, CommentActions.DEL_DESCRIPTOR_COMMENT);
        return ret;
    }
    
    public int  GetDescriptorCommentSize( StringObject descriptorName, IntegerObject comment_size, StringObject fromCommentCategory, StringObject commentCategory){
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*--------------------------------------------------------------
         tms_api::GetDescriptorCommentSize()
        ----------------------------------------------------------------
        INPUT:  - descriptorName : the name of a descriptor
                - comment_size : an integer to be filled with the size
                  of the comment of the given descriptor
                - fromCommentCategory : the from class of a comment category
                - commentCategory : the name of a comment category
        OUTPUT: - TMS_APISucc in case the specified comment of the given descriptor
                  is returned succesfully
                - TMS_APIFail otherwise
        FUNCTION: gets the comment size of the given descriptor. The kind of the returned
                  comment is defined by the given comment category
                  (fromCommentCategory, commentCategory).
        ----------------------------------------------------------------*/
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*in file handle_comm.cpp
        int tms_api::GetDescriptorCommentSize(char *descriptorName, int *comment_size, char *fromCommentCategory, char *commentCategory)
        {
           int ret = Get_SetDescriptorComment(descriptorName, NULL, comment_size, fromCommentCategory, commentCategory, GET_DESCRIPTOR_COMMENT_SIZE);
           return ret;
        }
        */
        // </editor-fold> 

        int ret = Get_SetDescriptorComment(descriptorName, null, comment_size, fromCommentCategory, commentCategory, CommentActions.GET_DESCRIPTOR_COMMENT_SIZE);
        return ret;
    }
    
    public int  GetDescriptorComment(StringObject descriptorName, StringObject comment, StringObject fromCommentCategory, StringObject commentCategory){
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*--------------------------------------------------------------
        tms_api::GetDescriptorComment()
        ----------------------------------------------------------------
        INPUT:  - descriptorName : the name of a descriptor
                - comment : an allocated string
                - fromCommentCategory : the from class of a comment category
                - commentCategory : the name of a comment category
        OUTPUT: - TMS_APISucc in case the specified comment of the given descriptor
                  is returned succesfully
                - TMS_APIFail otherwise
        FUNCTION: gets the comment of the given descriptor. The kind of the returned
                  comment is defined by the given comment category
                  (fromCommentCategory, commentCategory).
                  The comment is returned in string comment which must be initially allocated.
        ATTENTION: string comment must be initially allocated.
        ----------------------------------------------------------------*/
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*in file handle_comm.cpp
        int tms_api::GetDescriptorComment(char *descriptorName, char *comment, char *fromCommentCategory, char *commentCategory)
        {
           *comment = 0;
           int ret = Get_SetDescriptorComment(descriptorName, comment, NULL, fromCommentCategory, commentCategory, GET_DESCRIPTOR_COMMENT);
           return ret;
        }
        */
        // </editor-fold>         
        
        int ret = Get_SetDescriptorComment(descriptorName, comment, null, fromCommentCategory, commentCategory, CommentActions.GET_DESCRIPTOR_COMMENT);
        return ret;
    }
    
    public int  SetDescriptorComment(StringObject descriptorName, StringObject comment, StringObject fromCommentCategory, StringObject commentCategory){
        //USED IN WEBTMS API ONLY
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*--------------------------------------------------------------
        tms_api::SetDescriptorComment()
        ----------------------------------------------------------------
        INPUT: - descriptorName : the name of a descriptor
        - comment : a string with a new comment value
        - fromCommentCategory : the from class of a comment category
        - commentCategory : the name of a comment category
        OUTPUT: - TMS_APISucc in case the specified comment of the given descriptor
        is set succesfully
        - TMS_APIFail otherwise
        FUNCTION: sets the comment for the given descriptor. The kind of the new
        comment is defined by the given comment category
        (fromCommentCategory, commentCategory).
        The new comment is given in string comment.
        ----------------------------------------------------------------*/
        // </editor-fold> 
        //<editor-fold defaultstate="collapsed" desc="C++ code...">
        /*in file handle_comm.cpp
        int tms_api::SetDescriptorComment(char *descriptorName, char *comment, char *fromCommentCategory, char *commentCategory)
        {
           int ret = Get_SetDescriptorComment(descriptorName, comment, NULL, fromCommentCategory, commentCategory, SET_DESCRIPTOR_COMMENT);
           return ret;
        }
        */
        // </editor-fold>         

        int ret = Get_SetDescriptorComment(descriptorName,comment,null,fromCommentCategory,commentCategory,CommentActions.SET_DESCRIPTOR_COMMENT);
        return ret;
    }
    
    private enum CommentActions { GET_DESCRIPTOR_COMMENT_SIZE, GET_DESCRIPTOR_COMMENT,SET_DESCRIPTOR_COMMENT, DEL_DESCRIPTOR_COMMENT};

    
    int GetThesaurusObject(StringObject retObject, StringObject givenSuperClassFrom, StringObject givenSuperClass,
            StringObject givenClassFrom, StringObject givenClass, IntegerObject card) {
        //<editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*-------------------------------------------------------------------
        tms_api::GetThesaurusObject()
        -------------------------------------------------------------------
        INPUT: - retObject an allocated string which will be filled with
        the searching node/link name
        - givenSuperClassFrom (in case of searching a link name):
        the from value of the category which is superclass of
        the searching link name, (in case of searching a node name): NULL
        - givenSuperClass (in case of searching a link name):
        the category name which is superclass of the searching link name,
        (in case of searching a node name): the superclass of the searching node name
        -	givenClassFrom (in case of searching a link name):
        the from value of the category which is class of
        the searching link name, (in case of searching a node name): NULL
        -	givenClass (in case of searching a link name):
        the category name which is class of the searching link name,
        (in case of searching a node name): the class of the searching node name
        -	card the number of found node/link names. Normally, it must be ONLY ONE.
        OUTPUT: - TMS_APISucc in case no error query execution happens
        - TMS_APIFail in case an error query execution happens
        FUNCTION: returns a node (case of testSuperClassFrom, testClassFrom = NULL)
        or a link (testSuperClassFrom, testClassFrom != NULL) of current thesaurus,
        applying the following rules:
        - for <thes>Descriptor returns:
        intersection(instances(<thes>ThesaurusNotionType), subclasses(givenSuperClass))
        - for <thes>Facet, <thes>Hierarchy returns:
        intersection(instances(<thes>ThesaurusClassType), subclasses(givenSuperClass))
        - for <thes>category returns:
        intersection(instances(<thes>ThesaurusNotionType, <thes>_relation), subclasses(givenSuperClassFrom, givenSuperClass))
        This function is called any time we want to refer to a node or
        a link of current thesaurus, which can be translated to any
        language. So, in this way, the code is independent by any translation
        ATTENTION: - this function must be called inside a query session
        -------------------------------------------------------------------*/
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="C++ code...">    
        
        /*
        int tms_api: :GetThesaurusObject(char *retObject, char *givenSuperClassFrom, char *givenSuperClass, char *givenClassFrom, char *givenClass, int *card)
        {
            int sub_class_set, class_set;

            // get the direct subclasses of givenSuperClass
            if (QC- > reset_name_scope() == ERROR) {
                fprintf(stderr, "In - GetThesaurusObject : Failed to QC->reset_name_scope()\n");
                return TMS_APIFail;
            }
            if (givenSuperClassFrom) { // case of link
                if (QC ->  set_current_node(givenSuperClassFrom) == ERROR) {
                    fprintf(stderr, "In - GetThesaurusObject : Failed to QC->set_current_node for <%s>\n", givenSuperClassFrom);
                    return TMS_APIFail;
                }
            }
            if (QC ->  set_current_node(givenSuperClass) == ERROR) {
                fprintf(stderr, "In - GetThesaurusObject : Failed to QC->set_current_node for <%s>\n", givenSuperClass);
                return TMS_APIFail;
            }
            if ((sub_class_set = QC ->  get_subclasses(0)) == ERROR) {
                fprintf(stderr, "In - GetThesaurusObject : Failed to get_sublasses\n");
                return TMS_APIFail;
            }
            // get the direct instances of givenClass
            if (QC ->  reset_name_scope() == ERROR) {
                fprintf(stderr, "In - GetThesaurusObject : Failed to QC->reset_name_scope()\n");
                return TMS_APIFail;
            }
            if (givenClassFrom) { // case of link
                if (QC ->  set_current_node(givenClassFrom) == ERROR) {
                    fprintf(stderr, "In - GetThesaurusObject : Failed to QC->set_current_node for <%s>\n", givenClassFrom);
                    return TMS_APIFail;
                }
            }
            if (QC ->  set_current_node(givenClass) == ERROR) {
                fprintf(stderr, "In - GetThesaurusObject : Failed to QC->set_current_node for <%s>\n", givenClass);
                return TMS_APIFail;
            }
            if ((class_set = QC ->  get_instances(0)) == ERROR) {
                fprintf(stderr, "In - GetThesaurusObject : Failed to get_instances\n");
                return TMS_APIFail;
            }
            // get the intersection of sub_class_set and class_set
            if (QC ->  set_intersect(sub_class_set, class_set) == ERROR) {
                fprintf(stderr, "In - GetThesaurusObject : Failed to QC->set_intersect\n");
                return TMS_APIFail;
            }
            // get the item of the intersection (it MUST be always one)
            QC->reset_set(sub_class_set);
            *  card = QC ->  set_get_card(sub_class_set);
            *  card = ERROR;
            while (QC ->  return_nodes(sub_class_set, retObject) != ERROR) {
            }

            QC->free_set(sub_class_set);
            QC->free_set(class_set);
            return TMS_APISucc;
        }
        */
        //</editor-fold>
        
        // get the direct subclasses of givenSuperClass
        if (QC.reset_name_scope() == QClass.APIFail) {
            //fprintf(stderr, "In - GetThesaurusObject : Failed to QC->reset_name_scope()\n");
            Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "In - GetThesaurusObject : Failed to QC->reset_name_scope\n");
            return TMS_APIFail;
        }
        
        if (givenSuperClassFrom!=null && givenSuperClassFrom.getValue()!=null && givenSuperClassFrom.getValue().length()>0) { // case of link
            if (QC.set_current_node(givenSuperClassFrom) == QClass.APIFail) {
                //fprintf (stderr, "In - GetThesaurusObject : Failed to QC->set_current_node for <%s>\n",givenSuperClassFrom);
                Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "In - GetThesaurusObject : Failed to QC->set_current_node for "+givenSuperClassFrom.getValue()+"\n");
                return TMS_APIFail;
            }
        }
        
	if (QC.set_current_node(givenSuperClass) == QClass.APIFail) {
            //fprintf (stderr, "In - GetThesaurusObject : Failed to QC->set_current_node for <%s>\n",givenSuperClass);
            Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "In - GetThesaurusObject : Failed to QC->set_current_node for "+givenSuperClass.getValue()+"\n");
            return TMS_APIFail;
        }
        int sub_class_set = QC.get_subclasses(0);
	if (sub_class_set == QClass.APIFail ) {
            //fprintf (stderr, "In - GetThesaurusObject : Failed to get_sublasses\n");
            Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "In - GetThesaurusObject : Failed to get_sublasses\n");
            return TMS_APIFail;
        }
        
        // get the direct instances of givenClass
        if (QC.reset_name_scope() == QClass.APIFail) {
            //fprintf(stderr, "In - GetThesaurusObject : Failed to QC->reset_name_scope()\n");
            Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "In - GetThesaurusObject : Failed to QC->reset_name_scope\n");
            return TMS_APIFail;
        }
        
        if (givenClassFrom!=null && givenClassFrom.getValue()!=null && givenClassFrom.getValue().length()>0) { // case of link
            if (QC.set_current_node(givenClassFrom) == QClass.APIFail) {
                //fprintf (stderr, "In - GetThesaurusObject : Failed to QC->set_current_node for <%s>\n",givenClassFrom);
                Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "In - GetThesaurusObject : Failed to QC->set_current_node for "+givenClassFrom.getValue()+"\n");
                return TMS_APIFail;
            }
        }
        
	if (QC.set_current_node(givenClass) == QClass.APIFail) {
            //fprintf (stderr, "In - GetThesaurusObject : Failed to QC->set_current_node for <%s>\n",givenClass);
            Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "In - GetThesaurusObject : Failed to QC->set_current_node for "+givenClass.getValue()+"\n");
            return TMS_APIFail;
        }
        int class_set = QC.get_instances(0);
	if (class_set == QClass.APIFail ) {
            //fprintf (stderr, "In - GetThesaurusObject : Failed to get_instances\n");
            Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "In - GetThesaurusObject : Failed to get_instances\n");
            return TMS_APIFail;
        }
        
        // get the intersection of sub_class_set and class_set
        if (QC.set_intersect(sub_class_set, class_set) == QClass.APIFail ) {
            //fprintf (stderr, "In - GetThesaurusObject : Failed to QC->set_intersect\n");
            Logger.getLogger(TMSAPIClass.class.getName()).log(Level.INFO, "In - GetThesaurusObject : Failed to QC->set_intersect\n");
            return TMS_APIFail;
        }
        
        // get the item of the intersection (it MUST be always one)
        QC.reset_set(sub_class_set);
        card.setValue(QC.set_get_card(sub_class_set));
        card.setValue(QClass.APIFail);
   
        
        while (QC.return_nodes( sub_class_set, retObject) != QClass.APIFail) {
        }

        QC.free_set( sub_class_set);
        QC.free_set( class_set);
        
        return TMS_APISucc;   
    }
    
    int  Get_SetDescriptorComment(StringObject descriptorName, StringObject comment, 
            IntegerObject comment_size, StringObject fromCommentCategory, StringObject commentCategory , CommentActions option){
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*--------------------------------------------------------------
        tms_api::Get_SetDescriptorComment()
        ----------------------------------------------------------------
        INPUT:  - descriptorName : the name of a descriptor
                - comment :
                     * option = GET_DESCRIPTOR_COMMENT_SIZE: NULL
                     * option = GET_DESCRIPTOR_COMMENT: an allocated string
                     * option = SET_DESCRIPTOR_COMMENT: a string with a new comment value
                     * option = DEL_DESCRIPTOR_COMMENT: NULL
                - comment_size :
                     * option = GET_DESCRIPTOR_COMMENT_SIZE: pointer to an integer
                     * option = GET_DESCRIPTOR_COMMENT: NULL
                     * option = SET_DESCRIPTOR_COMMENT: NULL
                     * option = DEL_DESCRIPTOR_COMMENT: NULL
                - fromCommentCategory : the from class of a comment category
                - commentCategory : the name of a comment category
                - option : GET_DESCRIPTOR_COMMENT or SET_DESCRIPTOR_COMMENT or DEL_DESCRIPTOR_COMMENT
        OUTPUT: - TMS_APISucc in case the specified comment (or size) of the given descriptor
                  is returned/set/deleted succesfully
                - TMS_APIFail in case
                    · descriptorName is not the name of any existing descriptor of
                    currently used thesaurus.
                    · given comment category (fromCommentCategory, commentCategory)
                    is not one of the available by the currently used thesaurus
                    and the current TMS model. (for example:
                    MERIMEEThesaurusConcept->merimee_scope_note for thesaurus MERIMEE).
                    · option = SET_DESCRIPTOR_COMMENT and given new comment string
                    comment is NULL or empty.
        FUNCTION: gets/sets/deletes the comment (or size) of the given descriptor.
                  The kind of the returned comment is defined by the given comment
                  category (fromCommentCategory, commentCategory).
                  The comment is returned in string comment which must be initially allocated.
        ATTENTION: in case option = GET_DESCRIPTOR_COMMENT:
                   string comment must be initially allocated.
        ----------------------------------------------------------------*/
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*in file handle_comm.cpp
        int tms_api::Get_SetDescriptorComment(char *descriptorName, char *comment, int *comment_size, char *fromCommentCategory, char *commentCategory, int option)
        {
            // check if given descriptor exists in data base
            QC->reset_name_scope();
            int descriptorSysid;
            if ((descriptorSysid = QC ->  set_current_node(descriptorName)) < 0) {
                sprintf(errorMessage, translate("%s does not exist in data base"), descriptorName);
                return TMS_APIFail;
            }

            // looking for MERIMEEDescriptor
            int card;
            l_name givenClass, thes_descriptor;
            strcpy(givenClass, userOperation); // MERIMEE
            strcat(givenClass, "ThesaurusNotionType"); // MERIMEEThesaurusNotionType
            int ret = GetThesaurusObject(thes_descriptor, NULL, "Descriptor", NULL, givenClass, &  card);
            if (ret == TMS_APIFail) {
                sprintf(errorMessage, translate("Failed to refer to class <%s%s>"), userOperation, "Descriptor");
                return TMS_APIFail;
            }

            // check if given descriptor is a descriptor of current thesaurus
            // if it belongs to MERIMEEDescriptor
            ret = ObjectinClass(descriptorName, thes_descriptor, errorMessage);
            if (ret == TMS_APIFail) {
                sprintf(errorMessage, translate("%s is not a %s descriptor"), descriptorName, thes_descriptor);
                return TMS_APIFail;
            }

            // check if given comment category is one of the available by the current TMS model
            // maybe MERIMEEThesaurusConcept->merimee_scope_note is not the only
            // kind of comment category defined in TMS model
            // That's why the comment-category is given as input in these comment handling functions
            int allowedCommentCategory = 0;
            int counter = 0;
            while ((counter < MAX_COMMENT_CATEGORIES) && *  (CommentCategories[counter][0])) {
                if (!strcmp(fromCommentCategory, CommentCategories[counter][0]) &&
                        !strcmp(commentCategory, CommentCategories[counter][1])) {
                    allowedCommentCategory = 1;
                    break;
                }
                counter++;
            }
            if (!allowedCommentCategory) {
                sprintf(errorMessage, translate("The given category (%s, %s) for the creation of a descriptor comment is not one of the categories which are allowed to be used.\n"), fromCommentCategory, commentCategory);
                strcat(errorMessage, translate("Allowed categories for the creation of a descriptor comment:\n"));
                int counter = 0;
                while ((counter < MAX_COMMENT_CATEGORIES) && *  (CommentCategories[counter][0])) {
                    strcat(errorMessage, CommentCategories[counter][0]);
                    strcat(errorMessage, ", ");
                    strcat(errorMessage, CommentCategories[counter][1]);
                    strcat(errorMessage, "\n");
                    counter++;
                }
                return TMS_APIFail;
            }
            PairInfo categ;
            strcpy(categ.name, fromCommentCategory); // MERIMEEThesaurusConcept
            strcpy(categ.label, commentCategory); //merimee_scope_note

            // create a new HandleCommentsClass
            HandleCommentsClass * commentsClass = new HandleCommentsClass(QC, CC, descriptorName, &  categ);

            switch (option) {
                case GET_DESCRIPTOR_COMMENT_SIZE:
                case GET_DESCRIPTOR_COMMENT:
                    if (commentsClass ->  editComment() == TMS_APIFail) {
                        delete commentsClass;
                        if (option == GET_DESCRIPTOR_COMMENT_SIZE) {
                            abort_get_comment_size(descriptorName, errorMessage);
                        } else {
                            abort_get_comment(descriptorName, errorMessage);
                        }
                        return TMS_APIFail;
                    }
                    if (option == GET_DESCRIPTOR_COMMENT_SIZE) {
                        // just get the size of the comment
                        *  comment_size = commentsClass ->  GetCommentSize();
                        commit_get_comment_size(descriptorName, errorMessage);
                        break;
                    }
                    commentsClass->fillStringWithComment(comment);
                    commit_get_comment(descriptorName, errorMessage);
                    break;
                case SET_DESCRIPTOR_COMMENT:
                    if (!comment || IsEmptyString(comment)) {
                        sprintf(errorMessage, translate("NULL or empty comment cannot be set to descriptor %s"), descriptorName);
                        return TMS_APIFail;
                    }
                    if (commentsClass ->  CommitComment(descriptorName, comment) == TMS_APIFail) {
                        delete commentsClass;
                        abort_set_comment(descriptorName, errorMessage);
                        return TMS_APIFail;
                    }
                    commit_set_comment(descriptorName, errorMessage);
                    break;
                case DEL_DESCRIPTOR_COMMENT:
                    if (commentsClass ->  CommitComment(descriptorName, NULL) == TMS_APIFail) {
                        delete commentsClass;
                        abort_del_comment(descriptorName, errorMessage);
                        return TMS_APIFail;
                    }
                    commit_del_comment(descriptorName, errorMessage);
                    break;
            }

            // deallocate commentsClass
            delete commentsClass;

            return TMS_APISucc;
        }
         */
        // </editor-fold>         

        // check if given descriptor exists in data base
        QC.reset_name_scope();
        long descriptorSysidL = QC.set_current_node(descriptorName);
        if (descriptorSysidL < 0) {
            //sprintf(errorMessage, translate("%s does not exist in data base"), descriptorName);
            errorMessage.setValue(String.format("%s does not exist in data base", descriptorName.getValue()));
            return TMS_APIFail;
        }
        // looking for MERIMEEDescriptor
        IntegerObject card = new IntegerObject(0);
        StringObject thes_descriptor = new StringObject();
        StringObject givenClass = new StringObject(userOperation.getValue() + "ThesaurusNotionType");

        int ret = GetThesaurusObject(thes_descriptor, null, new StringObject("Descriptor"), null, givenClass, card);
        if (ret == TMS_APIFail) {

            //sprintf(errorMessage, translate("Failed to refer to class <%s%s>"), userOperation, "Descriptor");
            errorMessage.setValue(String.format("Failed to refer to class <%s%s>", userOperation.getValue(), "Descriptor"));
            return TMS_APIFail;
        }

        StringObject tempErrorMsg = new StringObject();
        // check if given descriptor is a descriptor of current thesaurus
        // if it belongs to MERIMEEDescriptor
        ret = ObjectinClass(descriptorName, thes_descriptor, tempErrorMsg);
        if (ret == TMS_APIFail) {

            //BUG FIX -- THIS TEST SHOULD BE EXTENDED IN ORDER TO ALLOW COMMENT HANDLING FOR TOP TERMS IF NOT IN CLASS AAADESCRIPTOR
            ret = GetThesaurusObject(thes_descriptor, null, new StringObject("TopTerm"), null, givenClass, card);
            if (ret == TMS_APIFail) {
                //sprintf(errorMessage, translate("Failed to refer to class <%s%s>"), userOperation, "Descriptor");
                errorMessage.setValue(errorMessage.getValue().concat("Failed to refer to class <" + userOperation.getValue() + "TopTerm>"));
                return TMS_APIFail;
            }

            // check if given descriptor is a descriptor of current thesaurus
            // if it belongs to MERIMEEDescriptor 
            // Top Terms should also be alloweded here
            ret = ObjectinClass(descriptorName, thes_descriptor, tempErrorMsg);
            if (ret == TMS_APIFail) {

                //BUG FIX -- THIS TEST SHOULD BE EXTENDED IN ORDER TO ALLOW COMMENT HANDLING FOR SOURCES NOT IN CLASS AAADESCRIPTOR OR AAATOPTERM
                thes_descriptor.setValue("Source");
                if (QC.reset_name_scope() == QClass.APIFail) {
                    errorMessage.setValue(errorMessage.getValue().concat(" In - Get_SetDescriptorComment : Failed to QC->reset_name_scope"));
                    return TMS_APIFail;
                }

                if (QC.set_current_node(thes_descriptor) == QClass.APIFail) {
                    //sprintf(errorMessage, translate("Failed to refer to class <%s%s>"), userOperation, "Descriptor");
                    errorMessage.setValue(errorMessage.getValue().concat("Failed to refer to class  Source"));
                    return TMS_APIFail;
                }
                ret = ObjectinClass(descriptorName, thes_descriptor, tempErrorMsg);
                if (ret == TMS_APIFail) {
                    //sprintf(errorMessage, translate("%s is not a %s descriptor"), descriptorName, thes_descriptor);
                    // errorMessage Handled by ObjectinClass
                    //errorMessage.setValue(errorMessage.getValue().concat("Ο Όρος " + descriptorName + "δεν ανήκει στην κλάση " + thes_descriptor.getValue() ));
                    errorMessage.setValue(tempErrorMsg.getValue());
                    return TMS_APIFail;
                }
            }

        }

        // <editor-fold defaultstate="collapsed" desc="REPLACING CATEGORY CHECKING ALGORITHM - more loose">
        /*
        // check if given comment category is one of the available by the current TMS model
        // maybe MERIMEEThesaurusConcept->merimee_scope_note is not the only
        // kind of comment category defined in TMS model
        // That's why the comment-category is given as input in these comment handling functions
        int allowedCommentCategory = 0;
        int counter = 0;
        while ((counter < MAX_COMMENT_CATEGORIES) && *(CommentCategories[counter][0])) {
            if (!strcmp(fromCommentCategory, CommentCategories[counter][0]) &&
                    !strcmp(commentCategory, CommentCategories[counter][1])) {
                allowedCommentCategory = 1;
                break;
            }
            counter++;
        }
        if (!allowedCommentCategory) {
            sprintf(errorMessage, translate("The given category (%s, %s) for the creation of a descriptor comment is not one of the categories which are allowed to be used.\n"), fromCommentCategory, commentCategory);
            strcat(errorMessage, translate("Allowed categories for the creation of a descriptor comment:\n"));
            int counter = 0;
            while ((counter < MAX_COMMENT_CATEGORIES) && *(CommentCategories[counter][0])) {
                strcat(errorMessage, CommentCategories[counter][0]);
                strcat(errorMessage, ", ");
                strcat(errorMessage, CommentCategories[counter][1]);
                strcat(errorMessage, "\n");
                counter++;
            }
            return TMS_APIFail;
        }
        */
        // </editor-fold>
        
        int allowedCommentCategory = 0;
        //more llose check 
        if(commentCategory.getValue().equals("source_note")&& fromCommentCategory.getValue().equals("Source")){
            allowedCommentCategory = 1;
        }
        else if (commentCategory.getValue().contains(userOperation.getValue()) || commentCategory.getValue().contains(userOperationLow.getValue())) {
            allowedCommentCategory = 1;
        } else if (fromCommentCategory.getValue().contains(userOperation.getValue()) || fromCommentCategory.getValue().contains(userOperationLow.getValue())) {
            allowedCommentCategory = 1;
        }

        if (allowedCommentCategory == 0) {
            //sprintf(errorMessage, translate("The given category (%s, %s) for the creation of a descriptor comment is not one of the categories which are allowed to be used.\n"), fromCommentCategory, commentCategory);
            //strcat(errorMessage, translate("Allowed categories for the creation of a descriptor comment:\n"));
            errorMessage.setValue(String.format("The given category (%s, %s) for the creation of a descriptor comment is not one of the categories which are allowed to be used.\n", fromCommentCategory.getValue(), commentCategory.getValue()));
            return TMS_APIFail;
        }

        PairInfo categ = new PairInfo(fromCommentCategory, commentCategory);

        //create a new HandleCommentsClass
        HandleCommentsClass commentsClass = new HandleCommentsClass(descriptorName, categ);

        switch (option) {
            case GET_DESCRIPTOR_COMMENT_SIZE:
            case GET_DESCRIPTOR_COMMENT: {
                if (commentsClass.editComment(QC) == TMS_APIFail) {
                    if (option == CommentActions.GET_DESCRIPTOR_COMMENT_SIZE) {
                        abort_get_comment_size(descriptorName, errorMessage);
                    } else {
                        abort_get_comment(descriptorName, errorMessage);
                    }
                    return TMS_APIFail;
                }
                if (option == CommentActions.GET_DESCRIPTOR_COMMENT_SIZE) {
                    // just get the size of the comment
                    comment_size.setValue(commentsClass.GetCommentSize());
                    commit_get_comment_size(descriptorName, errorMessage);
                    break;
                }
                commentsClass.fillStringWithComment(comment);
                commit_get_comment(descriptorName, errorMessage);
                break;
            }
            case SET_DESCRIPTOR_COMMENT: {
                if (comment == null || IsEmptyString(comment) == QClass.TRUEval) {
                    //sprintf(errorMessage, translate("NULL or empty comment cannot be set to descriptor %s"), descriptorName);
                    errorMessage.setValue("NULL or empty comment cannot be set to descriptor " + descriptorName.getValue());//NULL or empty comment cannot be set to descriptor %s"), descriptorName);
                    return TMS_APIFail;
                }
                if (commentsClass.CommitComment(QC, descriptorName, comment) == TMS_APIFail) {
                    abort_set_comment(descriptorName, errorMessage);
                    return TMS_APIFail;
                }
                commit_set_comment(descriptorName, errorMessage);
                break;
            }
            case DEL_DESCRIPTOR_COMMENT: {
                if (commentsClass.CommitComment(QC, descriptorName, null) == TMS_APIFail) {
                    //delete commentsClass;
                    abort_del_comment(descriptorName, errorMessage);
                    return TMS_APIFail;
                }
                commit_del_comment(descriptorName, errorMessage);
                break;
            }
        }

        return TMS_APISucc;


    }
   
    int IsEmptyString(StringObject str){
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*-----------------------------------------------------------------
                        IsEmptyString()
            returns 1 if "str" contains only spaces, otherwise it returns 0
        -----------------------------------------------------------------*/
        //</editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">      
        /*
        int IsEmptyString(char *str)
        {
           char *c = str;
           for (c = str; *c; c++) {
                if (*c != ' ') return 0;
           }
           return 1;
        }
         */
        //</editor-fold>  
        String temp = str.getValue();
        temp = temp.replaceAll("\r\n", " ");
        temp = temp.replaceAll("\n", " ");
        temp = temp.replaceAll("\r", " ");
        temp = temp.replaceAll(" +", " ").trim();
        if(temp.length()>0){
            return QClass.FALSEval;
        }
            /*
        for(int i =0; i < temp.length(); i++){
            if(temp.charAt(i) != ' ' )
                return 0;
        }*/

       //return 1;
        return QClass.TRUEval;    
    }
  
    
    int ObjectinClass(StringObject object,StringObject object_class, StringObject message){
        
        //<editor-fold defaultstate="collapsed" desc="Comments...">
    /*-----------------------------------------------------------------
    tms_api::ObjectinClass()
    -------------------------------------------------------------------
    INPUT: - object, the name of the given node
    - object_class, the name of the given class
    - message, an allocated string which will be filled with
    an error message in case of an error occurs
    OUTPUT: - TMS_APISucc in case the given node belongs to the given class
    - TMS_APIFail in case the given node does not belong to the given class
    FUNCTION: checks if the given node belongs to the given class.
    This is done by:
    - getting all the classes of the given node
    - checking if the given class belongs to the above set of classes
    ATTENTION: - this function must be called inside a query session
    -----------------------------------------------------------------*/
    //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="C++ code..."> 
    /*
    int tms_api::ObjectinClass(char *object, char *object_class, char *message)
    {
            // get all the classes of the given node
            QC->reset_name_scope();
            int objectSySId = QC->set_current_node(object);
            if (objectSySId<=0) {
                    sprintf(message,"%s%s%s",translate(IN_OBJECTINCLASS),object,object_class); return TMS_APIFail; }

            int ret_set1 = QC->get_all_classes(0);
            if ((ret_set1==-1) || (QC->set_get_card(ret_set1)==0)){
                    sprintf(message,"%s%s%s",translate(IN_OBJECTINCLASS),object,object_class); return TMS_APIFail; }

            // check if the given class belongs to the above set of classes
            QC->reset_name_scope();
            int classSySId = QC->set_current_node(object_class);
            if (classSySId<=0) {
                    sprintf(message,"%s%s%s",translate(IN_OBJECTINCLASS),object,object_class); return TMS_APIFail; }

            QC->reset_set(ret_set1);
            int belongs = (QC->set_member_of(ret_set1) == -1) ?  TMS_APIFail : TMS_APISucc;
            QC->free_set(ret_set1);

       if (belongs == TMS_APIFail) {
            sprintf(message, "%s%s , %s", translate(IN_OBJECTINCLASS), object, object_class);
       }
       return belongs;
    }
    */
    //</editor-fold>

        
        // get all the classes of the given node
        QC.reset_name_scope();

        long objectSySIdL = QC.set_current_node( object);
        if (objectSySIdL <= 0) {
            //sprintf(message, "%s%s%s", translate(IN_OBJECTINCLASS), object, object_class);
            //sprintf(message,"%s%s%s",translate(IN_OBJECTINCLASS),object,object_class);
            message.setValue(message.getValue().concat(String.format("%s%s%s", IN_OBJECTINCLASS,object.getValue(),object_class.getValue() )));
            return TMS_APIFail;
        }

        int ret_set1 = QC.get_all_classes( 0);
        if ((ret_set1 == -1) || (QC.set_get_card( ret_set1) == 0)) {
            //sprintf(message, "%s%s%s", translate(IN_OBJECTINCLASS), object, object_class);
            //message.setValue(message.getValue().concat("Δεν βρέθηκε ο κόμβος : '" + object.getValue() + "' στην κλάσση '" + object_class.getValue() + "'."));
            message.setValue(message.getValue().concat(String.format("%s%s%s", IN_OBJECTINCLASS,object.getValue(),object_class.getValue() )));
            return TMS_APIFail;
        }

        // check if the given class belongs to the above set of classes
        QC.reset_name_scope();
        long classSySIdL = QC.set_current_node( object_class);
        if (classSySIdL <= 0) {
            //sprintf(message, "%s%s%s", translate(IN_OBJECTINCLASS), object, object_class);
            message.setValue(message.getValue().concat(String.format("%s%s%s", IN_OBJECTINCLASS,object.getValue(),object_class.getValue() )));
            return TMS_APIFail;
        }

        QC.reset_set( ret_set1);
        int belongs = (QC.set_member_of( ret_set1) == -1) ? TMS_APIFail : TMS_APISucc;
        QC.free_set( ret_set1);

        if (belongs == TMS_APIFail) {
            //sprintf(message, "%s%s , %s", translate(IN_OBJECTINCLASS), object, object_class);
            //message.setValue(message.getValue().concat("Δεν βρέθηκε ο κόμβος : '" + object.getValue() + "' στην κλάσση '" + object_class.getValue() + "'."));    
            message.setValue(message.getValue().concat(String.format("%s%s%s", IN_OBJECTINCLASS,object.getValue(),object_class.getValue() )));
        }
        return belongs;
		
    }
    
    //#################################################
    // ALL THE FOLLOWING FIUNCTIONS ARE NOT DECLARED PUBLIC ON PURPOSE
    // THEY ARE NOT USED BY WEBTMS SO THEY WILL NOT BE IMPLEMENTED FOR NOW
    // SINCE THEY ONLY THROW EXCEPTIONS THEY REMAIN NOT PUBLIC
    //#################################################
    
    // creations
    int TEMP_SKIP_CreateFacetAttribute(StringObject linkName, StringObject facetName, CMValue toValue, int catSet){
        throw new UnsupportedOperationException();
    }
    int TEMP_SKIP_CreateHierarchyAttribute(StringObject linkName, StringObject hierarchyName, CMValue toValue, int catSet){
        throw new UnsupportedOperationException();
    }
    int TEMP_SKIP_CreateInterThesRelation(StringObject term, StringObject category, StringObject to_term){
        throw new UnsupportedOperationException();
    }
    
    // classifications
    int  TEMP_SKIP_ClassifyNewDescriptor(StringObject descriptorName, StringObject className){
        throw new UnsupportedOperationException();
    }int TEMP_SKIP_DeClassifyNewDescriptor(StringObject descriptorName, StringObject className){
        throw new UnsupportedOperationException();
    }int TEMP_SKIP_ClassifySource(StringObject sourceName, StringObject className){
        throw new UnsupportedOperationException();
    }
    int  TEMP_SKIP_DeClassifySource(StringObject sourceName, StringObject className){
        throw new UnsupportedOperationException();
    }
    // deletions
    int TEMP_SKIP_DeleteFacetAttribute(int linkSysid, StringObject facetName){
        throw new UnsupportedOperationException();
    }
    int TEMP_SKIP_DeleteHierarchyAttribute(int linkSysid, StringObject hierarchyName){
        throw new UnsupportedOperationException();
    }
    
    int TEMP_SKIP_DeleteInterThesRelation(StringObject term, StringObject category, StringObject to_term){
        throw new UnsupportedOperationException();
    }
    int TEMP_SKIP_DeleteBroaderTermLink(StringObject FromTerm, StringObject ToTerm){
        throw new UnsupportedOperationException();
    }
    
    
    
    // abandones
    int  TEMP_SKIP_AbandonDescriptor(StringObject term){
        throw new UnsupportedOperationException();
    }
    int  TEMP_SKIP_UndoAbandonDescriptor(StringObject term, StringObject btterm, StringObject hierarchy){
        throw new UnsupportedOperationException();
    }
    // renames
    int TEMP_SKIP_RenameDescriptor(RenameNamesCouple[] nameCouples){
        throw new UnsupportedOperationException();
    }
    int TEMP_SKIP_RenameSource(StringObject sourceName, StringObject newSourceName){
        throw new UnsupportedOperationException();
    }
    int TEMP_SKIP_RenameEditor(StringObject editorName, StringObject newEditorName){
        throw new UnsupportedOperationException();
    }
    
    
    
    // various functions quring the database
    int TermPrefixfThesaurus(String thesaurus, StringObject prefix_term, StringObject message){
        // get the links pointing to the given thesaurus
        // and under category ("ThesaurusClassType","of_thesaurus")
        QC.reset_name_scope();
        long thesaurusSySIdL = QC.set_current_node(new StringObject(thesaurus));
        if (thesaurusSySIdL<=0) {
            //sprintf(message,"%s%s",translate(IN_TERMPREFIXFTHES),thesaurus); 
            message.setValue(IN_TERMPREFIXFTHES+thesaurus);
            return TMS_APIFail; 
        }
        
        int ret_set1 = QC.get_link_to_by_category(0,new StringObject("ThesaurusNotionType"),new StringObject("of_thesaurus"));
        if ( (ret_set1==-1) || (QC.set_get_card(ret_set1)!=1) ){
            QC.free_set(ret_set1);
            //sprintf(message,"%s%s",translate(IN_TERMPREFIXFTHES),thesaurus); 
            message.setValue(IN_TERMPREFIXFTHES+thesaurus);
            return TMS_APIFail;
        }
        
        // get the from_values of the above links
        int ret_set2 = QC.get_from_value(ret_set1);
        QC.free_set(ret_set1);
        if ( ( ret_set2==-1) || (QC.set_get_card(ret_set2)!=1) ){
            QC.free_set(ret_set2);
            //sprintf(message,"%s%s",translate(IN_TERMPREFIXFTHES),thesaurus); 
            message.setValue(IN_TERMPREFIXFTHES+thesaurus);
            return TMS_APIFail;
        }
        
        // get the links pointing from the above nodes
        // and under category ("ThesaurusNotionType","Notion`UsesAsPrefix")
        ret_set1 = QC.get_link_from_by_category(ret_set2,new StringObject("ThesaurusNotionType"),new StringObject("Notion`UsesAsPrefix"));
        QC.free_set(ret_set2);
        if ( (ret_set1==-1) || (QC.set_get_card(ret_set1)!=1) ){
            QC.free_set(ret_set1);
            //sprintf(message,"%s%s",translate(IN_TERMPREFIXFTHES),thesaurus); 
            message.setValue(IN_TERMPREFIXFTHES+thesaurus);
            return TMS_APIFail;
        }

        // get the to_values of the above links
        ret_set2 = QC.get_to_value(ret_set1);
        QC.free_set(ret_set1);
        if ( ( ret_set2==-1) || (QC.set_get_card(ret_set2)!=1) ){
            QC.free_set(ret_set2);
            //sprintf(message,"%s%s",translate(IN_TERMPREFIXFTHES),thesaurus); 
            message.setValue(IN_TERMPREFIXFTHES+thesaurus);
            return TMS_APIFail;
        }

        // inform the prefix_term with the above node
        QC.reset_set(ret_set2);
        int ret_val = QC.return_nodes(ret_set2,prefix_term);
        QC.free_set(ret_set2);
        if (ret_val==-1) {
            //sprintf(message,"%s%s",translate(IN_TERMPREFIXFTHES),thesaurus); 
            message.setValue(IN_TERMPREFIXFTHES+thesaurus);
            return TMS_APIFail;
        }
        
        QC.free_set(ret_set1);
        QC.free_set(ret_set2);
        return TMS_APISucc;
        
    }
    
}
