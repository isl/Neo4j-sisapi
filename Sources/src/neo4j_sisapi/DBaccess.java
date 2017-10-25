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

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static neo4j_sisapi.QClass.APIFail;
import static neo4j_sisapi.QClass.APISucc;
import org.neo4j.graphdb.Direction;

//import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.QueryExecutionException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Uniqueness;

/**
 *
 * @author Elias Tzortzakakis <tzortzak@ics.forth.gr>
 */
class DBaccess {

    GraphDatabaseService graphDb = null;
    Utilities utils = new Utilities();
    
    boolean useCommonLabel = false;
    
    
    //To check given SET with RETURN SET values.
    //boolean DebugInfo = false;
    boolean DebugInfo = false;//Configs.boolDebugInfo;
    
    
    final String Neo4j_Key_For_SysId = "SysId";
    
    
    private final String Neo4j_isRelationship = "isRelationship";
    

    
    public DBaccess(GraphDatabaseService useGraphDb) {
        this.graphDb = useGraphDb;
        this.useCommonLabel = true; ////label should be used unless otherwise stated --old code: = isLabelUsed(Configs.CommonLabelName);        
    }
    
    private String getCommonLabelStr(){ return (useCommonLabel ? ":" + Configs.CommonLabelName : ""); }
    
    
    private String prepareNeo4jIdPropertyFilterForCypher(long val){
        return Configs.Neo4j_Key_For_Neo4j_Id+":"+val;
    }
    
    /**
     * sets the correct key and escapes potential invalid characters
     * @param str
     * @return 
     */
    private String prepareLogicalNameForCypher(String str){
        /*if(str.contains("'")){
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "Contains ' ");
        }*/
        return Configs.Neo4j_Key_For_Logicalname+":'"+str.replace("'", "\\'")+"'";
    }
    
    // SET retSysids contains all the immediate instances of object with sysid  objSysid.
    // Returns 0 on success, -1 when an error occured.
    // (is changed by Lemonia in order to give the class attrs too)    
    int NOT_USED_getInstances(long objSysid, PQI_Set retSysids) {

        //check if node still exists in the database?
        //if not return api fail
        Neo4j_Object neoObj = loadObject(objSysid);
        if (neoObj ==null) {
            return APIFail;
        }
        
        //if neoObj is of level Token then it never has instances
        if(neoObj.isLevelToken()){
            return APISucc;
        }
        
        //if "obj_ptr" is a no instance category then get around ???
        String query = " MATCH(n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(objSysid)+"})<-[:INSTANCEOF]-(m) "+
                       " RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";
              
        if (query.length() == 0) {
            return APIFail;
        }

        Result res = this.graphDb.execute(query);
        try {
            while (res.hasNext()) {

                Map<String, Object> row = res.next();
                long val = getNeo4jIdFromObject(row.get(Configs.Neo4j_Key_For_Neo4j_Id));
                
                if (val > 0) {
                    retSysids.set_putNeo4j_Id(val);                    
                } else {
                    if(Configs.boolDebugInfo){
                        throw new UnsupportedOperationException();
                    }
                }
            }
            return APISucc;
        } catch (Exception ex) {
            utils.handleException(ex);
            return APIFail;
        } finally {
            res.close();
            res = null;
        }
    }
    
    int getInstancesSET(PQI_Set setIDs, PQI_Set retSysids) {
        
        
        if(CheckAllExist(setIDs) == APIFail){
             return APIFail;
        }
        
        ArrayList<Long> ids = setIDs.get_Neo4j_Ids();
        
        if(DebugInfo == true)
        {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getInstancesSET input IDs: "+setIDs.get_Neo4j_Ids());
        }

        String query = "";

        ArrayList<Long> tmpvec = new ArrayList<Long>();
                        
        int loopIndex = 0;
        int maxIndex = ids.size();

        if(maxIndex==0){
            return APISucc;
        }
        
        while (loopIndex < maxIndex) {

            ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex, Configs.MAX_IDS_PER_QUERY, ids);
            loopIndex += subSetofIds.size();
            if(subSetofIds.size()==0){
                break;
            }
            query ="";        
            if(subSetofIds.size()==1){
               query =  " MATCH(n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0))+"})<-[:INSTANCEOF]-(m) "+
                        " RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";
            }
            else {
                query = " MATCH(n"+getCommonLabelStr()+")<-[:INSTANCEOF]-(m) "+
                        " WHERE n."+Configs.Neo4j_Key_For_Neo4j_Id +" IN " +subSetofIds.toString() + 
                        " RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id +" as "+ Configs.Neo4j_Key_For_Neo4j_Id +" ";
                
                /*query = "UNWIND "+subSetofIds+" AS ArrayList "
                        +" MATCH (n"+getCommonLabelStr()+"{"+Configs.Neo4j_Key_For_Neo4j_Id+":ArrayList})<-[:INSTANCEOF]-(m) "
                        +" RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id +" as "+ Configs.Neo4j_Key_For_Neo4j_Id +" ";*/
            }
            
            //do the job do not return
            Result res = graphDb.execute(query);
            try {
                while (res.hasNext()) {
                    
                    long val = getNeo4jIdFromObject(res.next().get(Configs.Neo4j_Key_For_Neo4j_Id)); 
                    
                    if (val > 0) {
                        tmpvec.add(val);
                    }
                    else {                   
                        if(Configs.boolDebugInfo){                                
                            throw new UnsupportedOperationException();                            
                        }                        
                        
                    }
                }
                
            } catch (Exception ex) {
                utils.handleException(ex);
                return APIFail;
            } finally {
                res.close();
                res = null;
            }
        }
            
        
        for(Long l:tmpvec)
        {
            retSysids.set_putNeo4j_Id(l);
        }
        
        if(DebugInfo)
        {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getInstancesSET retSysids size: "+retSysids.get_Neo4j_Ids().size());
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getInstancesSET retSysids : "+retSysids.get_Neo4j_Ids());
        }
        
        return APISucc;
    }
    
    int getAllInstances(long objSysid, PQI_Set retSysids) {
         
        //check if node still exists in the database?
        //if not return api fail
        Neo4j_Object neoObj = loadObject(objSysid);
        if (neoObj ==null) {
            return APIFail;
        }
        
        String query = " MATCH(n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(objSysid)+"})<-[:ISA*0..]-(k)<-[:INSTANCEOF]-(m) "+
                        " RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";
                
        Result res = this.graphDb.execute(query);
        try {
            while (res.hasNext()) {

                Map<String, Object> row = res.next();
                long val = getNeo4jIdFromObject(row.get(Configs.Neo4j_Key_For_Neo4j_Id));

                if (val > 0) {
                    retSysids.set_putNeo4j_Id(val);
                    //return true;
                } 
                else {                   
                    if(Configs.boolDebugInfo){                                
                        throw new UnsupportedOperationException();                            
                    }                        
                }
            }
            return APISucc;
        } catch (Exception ex) {
            utils.handleException(ex);
            return APIFail;
        } finally {
            res.close();
            res = null;
        }
            
        //return APIFail;
     }
    
    //used in getClassesSET but kept the same naming convention getInstOfSET as in  sisapi
    int DBACCESS_getInstOfSET(PQI_Set setIDs, PQI_Set retSysids){
        
        ArrayList<Long> ids = new ArrayList<Long>();
        ids = setIDs.get_Neo4j_Ids();
        
        if(DebugInfo)
        {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** DBACCESS_getInstOfSET input IDs: "+setIDs.get_Neo4j_Ids());
        }
        
        String query = "";

        ArrayList<Long> tmpvec = new ArrayList<Long>();
                
        int loopIndex = 0;
        int maxIndex = ids.size();

        if(maxIndex==0){
            return APISucc;
        }
        
        while (loopIndex < maxIndex) {

            ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex,Configs.MAX_IDS_PER_QUERY, ids);
            loopIndex += subSetofIds.size();
            if(subSetofIds.size()==0){
                break;
            }
            query ="";        
            if(subSetofIds.size()==1){
               query =  " MATCH(n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0))+"})-[:INSTANCEOF]->(m) "+
                        " RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";
            }
            else {
                query = " MATCH(n"+getCommonLabelStr()+")-[:INSTANCEOF]->(m) "+
                        " WHERE n."+Configs.Neo4j_Key_For_Neo4j_Id +" IN " +subSetofIds.toString() + 
                        " RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id +" as "+ Configs.Neo4j_Key_For_Neo4j_Id +" ";
                /*
                query = "UNWIND "+subSetofIds+" AS ArrayList "
                       +" MATCH (n"+getCommonLabelStr()+"{"+Configs.Neo4j_Key_For_Neo4j_Id+":ArrayList})-[:INSTANCEOF]->(m) "
                       +" RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";
				*/
            }
            
            //do the job do not return
            Result res = graphDb.execute(query);
            try {
                while (res.hasNext()) {
                    Map<String, Object> row = res.next();
                    long val = getNeo4jIdFromObject(row.get(Configs.Neo4j_Key_For_Neo4j_Id));

                    if (val > 0) {
                        tmpvec.add(val);
                    }
                    else {
                        if(Configs.boolDebugInfo){
                            throw new UnsupportedOperationException();
                        }
                    }
                }

            } catch (Exception ex) {
                utils.handleException(ex);
                return APIFail;
            } finally {
                res.close();
                res = null;
            }
        }   
        
        for(Long l:tmpvec)
        {
            retSysids.set_putNeo4j_Id(l);
        }
        
        if(DebugInfo == true)
        {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** DBACCESS_getInstOfSET retSysids size: "+retSysids.get_Neo4j_Ids().size());
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** DBACCESS_getInstOfSET retSysids: "+retSysids.get_Neo4j_Ids());
        }
            
        return APISucc;
    }
    
    
    int DBACCESS_getInstOf(long objSysid, PQI_Set retSysids){
        
        ArrayList<Long> tmpvec = new ArrayList<Long>();
        
        String query = " MATCH(n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(objSysid)+"})-[:INSTANCEOF]->(m) "+
                        " RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";

        Result res = this.graphDb.execute(query);
        try {
            while (res.hasNext()) {
                Map<String, Object> row = res.next();
                long val = getNeo4jIdFromObject(row.get(Configs.Neo4j_Key_For_Neo4j_Id));

                if (val > 0) {
                    tmpvec.add(val);
                } 
                else {
                    if(Configs.boolDebugInfo){
                        throw new UnsupportedOperationException();
                    }
                }
            }

        } catch (Exception ex) {
            utils.handleException(ex);
            return APIFail;
        } finally {
            res.close();
            res = null;
        }
                
        
        for(Long l:tmpvec)
        {
            retSysids.set_putNeo4j_Id(l);
        }
        return APISucc;
    }
    
    int getClassesSET(PQI_Set setIDs, PQI_Set retSysids) {
        
         if(CheckAllExist(setIDs) == APIFail){
             return APIFail;
         }
         
         return DBACCESS_getInstOfSET(setIDs, retSysids);
    }
    
    int getClasses(long objSysid, PQI_Set retSysids) {

        //check if node still exists in the database?
        //if not return api fail
        Neo4j_Object neoObj = loadObject(objSysid);
        if (neoObj ==null) {
            return APIFail;
        }
        //Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, neoObj.toString());
        return neoObj.getInstOf(this, retSysids);
    }

    int getAllClasses(long objSysid, PQI_Set retSysids) {

        //check if node still exists in the database?
        //if not return api fail
        Neo4j_Object neoObj = loadObject(objSysid);
        if (neoObj ==null) {
            return APIFail;
        }
        
        String query = " MATCH(n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(objSysid)+"})-[:INSTANCEOF]->(k)-[r:ISA*0..]->(m) "+
                        " RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";
                
        Result res = this.graphDb.execute(query);
        try {
            while (res.hasNext()) {

                Map<String, Object> row = res.next();
                long val = getNeo4jIdFromObject(row.get(Configs.Neo4j_Key_For_Neo4j_Id));

                if (val > 0) {
                    retSysids.set_putNeo4j_Id(val);
                } 
                else {                   
                    if(Configs.boolDebugInfo){                                
                        throw new UnsupportedOperationException();                            
                    }                        
                }
            }
            return APISucc;
        } catch (Exception ex) {
            utils.handleException(ex);
            return APIFail;
        } finally {
            res.close();
            res = null;
        }
                
 }
    
    //this function should return 0 if no to value was found or -1 on error
    long DBACCESS_get_From_or_To(long neo4jId, boolean linksFromInsteadOfTo){

        String query = "MATCH (n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(neo4jId)+"}) "+
                        (linksFromInsteadOfTo?"<-":"-") + "[:RELATION]"+(linksFromInsteadOfTo?"-":"->")+"(m) "
                        + "RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";

        
        if (query.length() == 0) {
            return QClass.APIFail;
        }

        Result res = this.graphDb.execute(query);

        
        try {
            while (res.hasNext()) {
                Map<String, Object> row = res.next();
                
                long val = getNeo4jIdFromObject(row.get(Configs.Neo4j_Key_For_Neo4j_Id));
				
                
                if (val >= 0) {
                    return val;                    
                }
                else {                   
                    if(Configs.boolDebugInfo){                                
                        throw new UnsupportedOperationException();                            
                    }                        
                }

            }
            return APISucc;
        } catch (Exception ex) {
            utils.handleException(ex);
            return APIFail;
        } finally {
            res.close();
            res = null;
        }
        
    }
  
    
    
    private int DBACCESS_get_From_or_To_For_TraverseByCategory(ArrayList<Long> attributeIds,boolean linksFromInsteadOfTo, boolean excludeTokens, ArrayList<Long> returnSet){
        
        if(attributeIds.size()==0){
            return APISucc;
        }
        
        int loopIndex =0;
        int maxIndex = attributeIds.size();
        
        while(loopIndex<maxIndex){
            ArrayList<Long> subSetIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex, Configs.MAX_IDS_PER_QUERY, attributeIds);
            loopIndex += subSetIds.size();
            
            if(subSetIds.size()==0){
                break;
            }
            
            String query = "";
           
            if(subSetIds.size()==1){
                query = "MATCH (n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(subSetIds.get(0))+"}) "+
                    (linksFromInsteadOfTo?" <-[:RELATION]- ":" -[:RELATION]-> ") + "(m) "+
                    (excludeTokens? "WHERE NOT(\""+Configs.Neo4j_Level_Token+"\" IN labels(m) ) ":"")+
                     "RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";
            }
            else{
                query = "MATCH (n"+getCommonLabelStr()+") "+
                    (linksFromInsteadOfTo?" <-[:RELATION]- ":" -[:RELATION]-> ") + "(m) "+
                    " WHERE n." +Configs.Neo4j_Key_For_Neo4j_Id+" IN " + subSetIds.toString() +" " + 
                    (excludeTokens? "AND NOT(\""+Configs.Neo4j_Level_Token+"\" IN labels(m) ) ":"")+
                     "RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";
            }
                    
            Result res = this.graphDb.execute(query);


            try {
                while (res.hasNext()) {
                    
                    long val = getNeo4jIdFromObject(res.next().get(Configs.Neo4j_Key_For_Neo4j_Id));

                    if (val > 0 && returnSet.contains(val)==false) {
                        returnSet.add(val);
                    }
                    else {                   
                        if(Configs.boolDebugInfo){                                
                            throw new UnsupportedOperationException();                            
                        }                        
                    }

                }
                
            } catch (Exception ex) {
                utils.handleException(ex);
                return APIFail;
            } finally {
                res.close();
                res = null;
            }   
        }
        
        return APISucc;
    }
    
    
    int getFromNode(long neo4jId,PQI_Set retSysids){
        Neo4j_Object neoObj = loadObject(neo4jId);
        if (neoObj ==null) {
            return APIFail;
        }
        
        return (int)neoObj.getFromNode(this, retSysids);
        
    }
    
    int getToNodeSET(PQI_Set setIDs, PQI_Set retSysids){
        if(CheckAllExist(setIDs) == APIFail){
             return APIFail;
         }
        
        return DBACCESS_getFromNode_Or_ToNodeSET(setIDs, retSysids,false);        
    }
    
    int getToNode(long neo4jId,PQI_Set retSysids){
        Neo4j_Object neoObj = loadObject(neo4jId);
        if (neoObj ==null) {
            return APIFail;
        }
        return (int)neoObj.getToNode(this, retSysids);
        
    }
    
    int DBACCESS_getFromNode_Or_ToNodeSET(PQI_Set setIDs, PQI_Set retSysids, boolean linksFromInsteadOfToNode){
        
        ArrayList<Long> ids = new ArrayList<Long>();
        ids = setIDs.get_Neo4j_Ids();
        
        if(DebugInfo == true)
        {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** DBACCESS_getFromNode_Or_ToNodeSET input IDs: "+setIDs.get_Neo4j_Ids());
        }
        
        String query = "";

        ArrayList<Long> tmpvec = new ArrayList<Long>();
        
        int loopIndex = 0;
        int maxIndex = ids.size();

        if(maxIndex==0){
            return APISucc;
        }
        
        while (loopIndex < maxIndex) {

            ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex,Configs.MAX_IDS_PER_QUERY, ids);
            loopIndex += subSetofIds.size();
            if(subSetofIds.size()==0){
                break;
            }
            query ="";        
            if(subSetofIds.size()==1){
                query = "MATCH (n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0))+"}) "
                        +(linksFromInsteadOfToNode?"<-":"-") + "[:RELATION]"+(linksFromInsteadOfToNode?"-":"->")+"(k:"+Configs.Neo4j_Key_For_Type_AttributeStr+")"
                        +(linksFromInsteadOfToNode?"<-":"-") + "[:RELATION]"+(linksFromInsteadOfToNode?"-":"->")+"(m) "
                        + "RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";
            }
            else { 
                
                query = " MATCH (n"+getCommonLabelStr()+") "
                        +(linksFromInsteadOfToNode?"<-":"-") + "[:RELATION]"+(linksFromInsteadOfToNode?"-":"->")+"(k:"+Configs.Neo4j_Key_For_Type_AttributeStr+")"
                        +(linksFromInsteadOfToNode?"<-":"-") + "[:RELATION]"+(linksFromInsteadOfToNode?"-":"->")+"(m) "
                        + " WHERE n."+Configs.Neo4j_Key_For_Neo4j_Id +" in " + subSetofIds.toString() 
                        + " RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";
            }
            
            //do the job do not return
            Result res = graphDb.execute(query);
                try {
                    while (res.hasNext()) {
                        Map<String, Object> row = res.next();
                        long val = getNeo4jIdFromObject(row.get(Configs.Neo4j_Key_For_Neo4j_Id));
						                
                        if (val > 0) {
                            tmpvec.add(val);
                        }
                        else {
                            if(Configs.boolDebugInfo){
                                throw new UnsupportedOperationException();
                            }
                        }
                    }
                  
                } catch (Exception ex) {
                    utils.handleException(ex);
                    return APIFail;
                } finally {
                    res.close();
                    res = null;
                }                               
              }            
            
        
        for(Long l:tmpvec)
        {
            retSysids.set_putNeo4j_Id(l);
        }
        
        if(DebugInfo == true)
        {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** DBACCESS_getFromNode_Or_ToNodeSET retSysids size: "+retSysids.get_Neo4j_Ids().size());
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** DBACCESS_getFromNode_Or_ToNodeSET retSysids: "+retSysids.get_Neo4j_Ids());
        }
            
        return APISucc;
    }
    
    int DBACCESS_getFromNode_Or_ToNode(long neo4jId,PQI_Set retSysids, boolean linksFromInsteadOfToNode){
        
        String query = "MATCH (n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(neo4jId)+"}) "
                        +(linksFromInsteadOfToNode?"<-":"-") + "[:RELATION]"+(linksFromInsteadOfToNode?"-":"->")+"(k:"+Configs.Neo4j_Key_For_Type_AttributeStr+")"
                        +(linksFromInsteadOfToNode?"<-":"-") + "[:RELATION]"+(linksFromInsteadOfToNode?"-":"->")+"(m) "
                        + "RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";

         
        
        Result res = this.graphDb.execute(query);

        try {
            while (res.hasNext()) {
                Map<String, Object> row = res.next();
                
                long val = getNeo4jIdFromObject(row.get(Configs.Neo4j_Key_For_Neo4j_Id));
                
                if (val > 0) {
                    retSysids.set_putNeo4j_Id(val);
                } else {
                    if(Configs.boolDebugInfo){
                        throw new UnsupportedOperationException();
                    }
                }

            }
            return APISucc;
        } catch (Exception ex) {
            utils.handleException(ex);
            return APIFail;
        } finally {
            res.close();
            res = null;
        }
        
    }
    
    
    int getInherLinkFrom(long neo4jId, PQI_Set retSysids){
        
        String query = "MATCH (n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(neo4jId)+"}) "
                        +"-[:ISA*0..]->(k)-[:RELATION]->(m:"+Configs.Neo4j_Key_For_Type_AttributeStr+")"
                        + "RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";


        Result res = this.graphDb.execute(query);

        
        try {
            while (res.hasNext()) {
                Map<String, Object> row = res.next();
                
                long val = getNeo4jIdFromObject(row.get(Configs.Neo4j_Key_For_Neo4j_Id));
                
                if (val > 0) {
                    retSysids.set_putNeo4j_Id(val);
                } else {
                    if(Configs.boolDebugInfo){
                        throw new UnsupportedOperationException();
                    }
                }

            }
            return APISucc;
        } catch (Exception ex) {
            utils.handleException(ex);
            return APIFail;
        } finally {
            res.close();
            res = null;
        }      
    }
    
    
    int DBACCESS_getLinkFromSET(PQI_Set setIDs, PQI_Set retSysids){
        
        ArrayList<Long> ids = new ArrayList<Long>();
        ids = setIDs.get_Neo4j_Ids();
        
        if(DebugInfo == true)
        {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** DBACCESS_getLinkFromSET input IDs: "+setIDs.get_Neo4j_Ids());
        }
        
        String query = "";

        ArrayList<Long> tmpvec = new ArrayList<Long>();
        
        int loopIndex = 0;
        int maxIndex = ids.size();

        if(maxIndex==0){
            return APISucc;
        }
        
        while (loopIndex < maxIndex) {

            ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex,Configs.MAX_IDS_PER_QUERY, ids);
            loopIndex += subSetofIds.size();
            if(subSetofIds.size()==0){
                break;
            }
            query ="";        
            if(subSetofIds.size()==1){
               query = "MATCH (n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0))+"})-[:RELATION]->(m:"+Configs.Neo4j_Key_For_Type_AttributeStr+") "
                        + "RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";
            }
            else {          
                query = " MATCH (n"+getCommonLabelStr()+")-[:RELATION]->(m:"+Configs.Neo4j_Key_For_Type_AttributeStr+") "
                        + " WHERE n."+Configs.Neo4j_Key_For_Neo4j_Id +" in " +subSetofIds.toString() 
                       +" RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";                
                
            }
            
            //do the job do not return
            Result res = graphDb.execute(query);
            try {
                while (res.hasNext()) {
                    Map<String, Object> row = res.next();
                    long val = getNeo4jIdFromObject(row.get(Configs.Neo4j_Key_For_Neo4j_Id));

                    if (val > 0) {
                        tmpvec.add(val);
                    }
                    else {                   
                        if(Configs.boolDebugInfo){                                
                            throw new UnsupportedOperationException();                            
                        }                        
                    }
                }

            } catch (Exception ex) {
                utils.handleException(ex);
                return APIFail;
            } finally {
                res.close();
                res = null;
            }
        }
            
        for(Long l:tmpvec)
        {
            retSysids.set_putNeo4j_Id(l);
        }
        
        if(DebugInfo == true)
        {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** DBACCESS_getLinkFromSET retSysids size: "+retSysids.get_Neo4j_Ids().size());
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** DBACCESS_getLinkFromSET retSysids: "+retSysids.get_Neo4j_Ids());
        }
            
        return APISucc;
    }
    
    int DBACCESS_getLinkFrom(long objSysid, PQI_Set retSysids){
         
        String query = "MATCH (n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(objSysid)+"})-[:RELATION]->(m:"+Configs.Neo4j_Key_For_Type_AttributeStr+") "
                        + "RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";
               

        Result res = this.graphDb.execute(query);

        try {
            while (res.hasNext()) {
                Map<String, Object> row = res.next();
                
                long val = getNeo4jIdFromObject(row.get(Configs.Neo4j_Key_For_Neo4j_Id));
				
                
                if (val > 0) {
                    retSysids.set_putNeo4j_Id(val);
                }
                else {                   
                    if(Configs.boolDebugInfo){                                
                        throw new UnsupportedOperationException();                            
                    }                        
                }

            }
            return APISucc;
        } catch (Exception ex) {
            utils.handleException(ex);
            return APIFail;
        } finally {
            res.close();
            res = null;
        }
     }
    
    int getLinkFrom(long objSysid, PQI_Set retSysids) {
        
        // PENDING CASE have not found yet an example
        
        // ATTENTION!!!
        // there is also a case that is not included here:
        // get class attributes defined in the classes of objSysid
        
        // Class attibutes are attributes declared at the level of a 
        // Class but they hold for all instances of this class with 
        // one common To value  (not necessarily primitive)
        
        // <editor-fold defaultstate="collapsed" desc="C++ code.">
        /*
        int sis_api::getLinkFrom(SYSID objSysid, SET *retSysids)
        {
	telos_object    *obj_ptr;
	 SET             instOfCl, supCl;

								// "instOfCl" is used in order to keep all
								// the classes that the object with sysid
								// "objSysid" is an instance of. 
            instOfCl.set_clear();

								// "supCl" is used in order to keep all the 
								// super-classes that "objSysid" is "isA" of
								// calculating the isA transitive closure on
								// the  hierarchy graph. 
            supCl.set_clear();


								// "api_sys_cat" is a pointer to the system 
										  // catalogue. loadObject() loads the object 
                                // with sysid "objSysid". It returns the    
                                // memory address of this object or NULL in 
										  // case of failure. 
            if ((obj_ptr = api_sys_cat->loadObject(objSysid)) == NULL) {
                return (globalError.checkError(" >getLinkFrom")) ;
            }

								// get the links pointed by "obj_ptr" 
            obj_ptr->getLink(retSysids);

								// now, we should search for instance  
								// attributes... 

								// get the classes that "obj_ptr" is an 
								// instance of. 
		obj_ptr->getInstOf(&instOfCl);


            if (instOfCl.set_get_card() > 0) {
								// get all super classes of all objects  
								// with sysids belonging to "instOfCl"set
		getAllSuperClasses(&instOfCl, &supCl);

								// get the class attributes of all cla-
								// sses of the set "instOfCl" 
		getClassAttrFrom(&instOfCl, retSysids);

								// get the class attributes of all cla-
								// sses of the set "supCl" 
		getClassAttrFrom(&supCl, retSysids);
	}

	api_sys_cat->unloadObject(objSysid);

	return (globalError.checkError(" >getLinkFrom")) ;
        }
        */
        // </editor-fold>
        
        
        Neo4j_Object neoObj = loadObject(objSysid);
        if (neoObj ==null) {
            return APIFail;
        }

       return neoObj.getLink(this, retSysids);
    }
    
    
    int getTraverseByCategory_With_SIS_Server_Implementation(PQI_Set objSysids, PQI_Set f_set, PQI_Set b_set, int depth, QClass.Traversal_Isa isa, PQI_Set edge_set, PQI_Set retSysids, PQI_Set  checked_set){
        // <editor-fold defaultstate="collapsed" desc="C++ code.">
        /*
        
	SYSID obj_sysid;

	 if (objSysids->set_get_card() == 0) {
		return (globalError.checkError(" >getTraverseByCategory_With_SIS_Server_Implementation"));
	 }

	objSysids->set_pos(0);
	if (objSysids->set_tuple_mode()) {
		//query_func6 func(&(sis_api::getTraverseByCategory_With_SIS_Server_Implementation), this,depth,isa,f_set,b_set,edge_set, checked_set);
		query_func6 func(&sis_api::getTraverseByCategory_With_SIS_Server_Implementation, this,depth,isa,f_set,b_set,edge_set, checked_set);
		(((SET_TUPLE *)objSysids)->tuple_unary_query(&func));
	 } else
	 while((obj_sysid = objSysids->set_get_id()).id) {
		if (getTraverseByCategory_With_SIS_Server_Implementation(obj_sysid, f_set, b_set, depth, isa,
										  edge_set, retSysids, checked_set) == -1) {
		  globalError.putMessage(" >getTraverseByCategory_With_SIS_Server_Implementation");
		  return -1;
		}
	 }
	 return (globalError.checkError(" >getTraverseByCategory_With_SIS_Server_Implementation"));
        
        */
        // </editor-fold>
        
        if(objSysids.set_get_card()==0){
            //return (globalError.checkError(" >getTraverseByCategory_With_SIS_Server_Implementation"));
            return APISucc;
        }
        // traverse the set of links 
        ArrayList<Long> objSysidVec = objSysids.get_Neo4j_Ids();
        for(long obj_sysid : objSysidVec){

            if(DBaccess.this.getTraverseByCategory_With_SIS_Server_Implementation(obj_sysid, f_set, b_set, depth, isa, edge_set, retSysids, checked_set)==QClass.APIFail){
                //globalError.putMessage(" >getTraverseByCategory_With_SIS_Server_Implementation");
		return APIFail;
            }
        }
        return APISucc;
    }
    
    
    long getNodeNeo4jId(Node n){
        long retVal = -1;
        if(n.getProperty(Configs.Neo4j_Key_For_Neo4j_Id) instanceof Integer){
            
        
        //if(Configs.CastNeo4jIdAsInt){
            retVal = (int) n.getProperty(Configs.Neo4j_Key_For_Neo4j_Id);
        }
        else{
            retVal = (long) n.getProperty(Configs.Neo4j_Key_For_Neo4j_Id);
        }
        return retVal;
    }
	
    long getNodeThesaurusReferenceId(Node n){
        long retVal = -1;
        if(n.hasProperty(Configs.Neo4j_Key_For_ThesaurusReferenceId)){
            if(n.getProperty(Configs.Neo4j_Key_For_ThesaurusReferenceId) instanceof Integer){


            //if(Configs.CastNeo4jIdAsInt){
                retVal = (int) n.getProperty(Configs.Neo4j_Key_For_ThesaurusReferenceId);
            }
            else{
                retVal = (long) n.getProperty(Configs.Neo4j_Key_For_ThesaurusReferenceId);
            }
        }
        return retVal;
    }
	
    String getNodeTransliterationString(Node n){
        String retVal = "";
        if(n.hasProperty(Configs.Neo4j_Key_For_Transliteration)){
            retVal = (String) n.getProperty(Configs.Neo4j_Key_For_Transliteration);            
        }
        return retVal;
    }
	
    ArrayList<Long> getNodeClassesNeo4jIds(Node n){
        ArrayList<Long> retVals = new ArrayList<Long>();
        
        
        Iterator<Relationship> instanceOfRels =  n.getRelationships(Configs.Rels.INSTANCEOF, Direction.OUTGOING).iterator();
        
        while(instanceOfRels.hasNext()){
            Relationship instanceOfRel = instanceOfRels.next();
            long classId = getNodeNeo4jId(instanceOfRel.getEndNode());
            retVals.add(classId);
        }
        return retVals;
    }
    
    
    Node getNeo4jNodeByNeo4jId(long neo4jId){
        if(neo4jId<=0){
            return null;
        }
        String query = " MATCH (n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(neo4jId)+"}) RETURN n ";
        Result res = graphDb.execute(query);
        try{
            Node n = null;
            while (res.hasNext()) {
                 n = (Node) res.next().get("n");
                
                
            }
            return n;
        }
        catch(Exception ex){
            utils.handleException(ex);
            return null;
        }
        finally{
            res.close();
            res = null;
        }
        //return null;
    }
    String getNeo4jNodeSystemClass(Node n){
        String SystemClass = "";
        
        if(n.hasLabel(Configs.Labels.Type_Attribute)){
            SystemClass+=Configs.SystemClass_SubStringForAttribute;
        }
        else if(n.hasLabel(Configs.Labels.Type_Individual)){
            SystemClass+=Configs.SystemClass_SubStringForIndividual;
        }
        else{
            SystemClass+=Configs.SystemClass_SubStringForNotDefined;
        }
        
        if(n.hasLabel(Configs.Labels.Token)){
            return SystemClass+"_"+ Configs.Neo4j_Level_Token;
        }
        if(n.hasLabel(Configs.Labels.S_Class)){
            return SystemClass+"_"+ Configs.Neo4j_Level_S_Class;
        }
        if(n.hasLabel(Configs.Labels.M1_Class)){
            return SystemClass+"_"+ Configs.Neo4j_Level_M1_Class;
        }
        if(n.hasLabel(Configs.Labels.M2_Class)){
            return SystemClass+"_"+ Configs.Neo4j_Level_M2_Class;
        }
        if(n.hasLabel(Configs.Labels.M3_Class)){
            return SystemClass+"_"+ Configs.Neo4j_Level_M3_Class;
        }
        if(n.hasLabel(Configs.Labels.M4_Class)){
            return SystemClass+"_"+ Configs.Neo4j_Level_M4_Class;
        }
        return SystemClass;
    }
    
    int get_Bulk_Return_Full_Nodes_Rows(ArrayList<Long> nodeIds, ArrayList<Return_Full_Nodes_Row> retRows){
        
        int loopIndex = 0;
        int maxIndex = nodeIds.size();
        
        retRows.clear();
        
        if(maxIndex==0){            
            return APISucc;
        }
        boolean abort = false;
        while (loopIndex < maxIndex) {

            //the query is a little bit larger since it also requests logical name so 
            //here i use a value slightly less than Configs.MAX_IDS_PER_QUERY
            ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex, (Configs.MAX_IDS_PER_QUERY-30), nodeIds);
            loopIndex += subSetofIds.size();
            if(subSetofIds.size()==0){
                break;
            }
            String query ="";        
            if(subSetofIds.size()==1){
                
                query = " MATCH (n"+getCommonLabelStr()+"{"+ prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0))+"}) "
                        + " RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id +" as id, n."+Configs.Neo4j_Key_For_Logicalname +" as lname, labels(n) as lbls, n."+Configs.Neo4j_Key_For_ThesaurusReferenceId +" as refId, n."+Configs.Neo4j_Key_For_Transliteration +" as translit ";
						
            }
            else {
                
                query = " MATCH (n"+getCommonLabelStr()+") "+
                        " WHERE n."+Configs.Neo4j_Key_For_Neo4j_Id +" in " +subSetofIds.toString() +
                        " RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id +" as id, n."+Configs.Neo4j_Key_For_Logicalname +" as lname, labels(n) as lbls, n."+Configs.Neo4j_Key_For_ThesaurusReferenceId +" as refId, n."+Configs.Neo4j_Key_For_Transliteration +" as translit ";;
						
            }
            
            //do the job do not return
            Result res = null;
            try{
                res = graphDb.execute(query);
                while (res.hasNext()) {
                    Map<String,Object> row = res.next();
                    long idVal = getNeo4jIdFromObject(row.get("id"));
                    
                    String lname = (String)row.get("lname");
                    //Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, row.get("lbls").getClass().getCanonicalName());
                    
                    scala.collection.convert.Wrappers.SeqWrapper labels = (scala.collection.convert.Wrappers.SeqWrapper)row.get("lbls");
                    
                    long refIdVal = getNeo4jIdFromObject(row.get("refId"));
                    String transliteration = (String)row.get("translit");
                    
                    if(idVal>0 && lname.length()>0){
                        
                        retRows.add(new Return_Full_Nodes_Row(idVal,lname,labels,refIdVal,transliteration));
                    }
                    else{
                        abort = true;
                        break;
                    }
                }                
            }
            catch(Exception ex){
                utils.handleException(ex);
                abort = true;                
            }
            finally{
                if(res!=null){
                    res.close();
                    res = null;
                }
            }
            
            if(abort){
                retRows.clear();
                return APIFail;
            }
        }//while loop ending
        
        
        return APISucc;
    }
    
    int get_Bulk_Return_Prm_Nodes(ArrayList<Long> nodeIds, ArrayList<Return_Prm_Row> retRows){
        int loopIndex = 0;
        int maxIndex = nodeIds.size();
        
        retRows.clear();
        
        if(maxIndex==0){            
            return APISucc;
        }
        boolean abort = false;
        while (loopIndex < maxIndex) {

            //the query is a little bit larger since it also requests logical name so 
            //here i use a value slightly less than Configs.MAX_IDS_PER_QUERY
            ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex, (Configs.MAX_IDS_PER_QUERY-20), nodeIds);
            loopIndex += subSetofIds.size();
            if(subSetofIds.size()==0){
                break;
            }
            String query ="";        
            if(subSetofIds.size()==1){
                
                query = " MATCH (n"+getCommonLabelStr()+"{"+ prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0))+"}) "
                        + " RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id +" as id, n."+Configs.Neo4j_Key_For_Logicalname +" as lname ";
						
            }
            else {
                
                query = " MATCH (n"+getCommonLabelStr()+") "+
                        " WHERE n."+Configs.Neo4j_Key_For_Neo4j_Id +" in " +subSetofIds.toString() +
                        " RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id +" as id, n."+Configs.Neo4j_Key_For_Logicalname +" as lname ";						
            }
            
            //do the job do not return
            Result res = graphDb.execute(query);
            try{
                while (res.hasNext()) {
                    Map<String,Object> row = res.next();
                    long idVal = getNeo4jIdFromObject(row.get("id"));
                    
                    String lname = (String)row.get("lname");
                    if(idVal>0 && lname.length()>0){
                        
                        retRows.add(new Return_Prm_Row(lname, idVal));
                    }
                    else{
                        abort = true;
                        break;
                    }
                }                
            }
            catch(Exception ex){
                utils.handleException(ex);
                abort = true;                
            }
            finally{
                res.close();
                res = null;
            }
            
            if(abort){
                retRows.clear();
                return APIFail;
            }
        }//while loop ending
        
        
        return APISucc;
    }
    
    int get_Bulk_Return_Nodes_Rows(ArrayList<Long> nodeIds, ArrayList<Return_Nodes_Row> retRows){
        
        int loopIndex = 0;
        int maxIndex = nodeIds.size();
        
        retRows.clear();
        
        if(maxIndex==0){            
            return APISucc;
        }
        boolean abort = false;
        while (loopIndex < maxIndex) {

            //the query is a little bit larger since it also requests logical name so 
            //here i use a value slightly less than Configs.MAX_IDS_PER_QUERY
            ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex, (Configs.MAX_IDS_PER_QUERY-20), nodeIds);
            loopIndex += subSetofIds.size();
            if(subSetofIds.size()==0){
                break;
            }
            String query ="";        
            if(subSetofIds.size()==1){
                
                query = " MATCH (n"+getCommonLabelStr()+"{"+ prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0))+"}) "
                        + " RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id +" as id, n."+Configs.Neo4j_Key_For_Logicalname +" as lname, n."+Configs.Neo4j_Key_For_ThesaurusReferenceId +" as refId, n."+Configs.Neo4j_Key_For_Transliteration +" as translit ";
						
            }
            else {
                
                query = " MATCH (n"+getCommonLabelStr()+") "+
                        " WHERE n."+Configs.Neo4j_Key_For_Neo4j_Id +" in " +subSetofIds.toString() +
                        " RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id +" as id, n."+Configs.Neo4j_Key_For_Logicalname +" as lname, n."+Configs.Neo4j_Key_For_ThesaurusReferenceId +" as refId, n."+Configs.Neo4j_Key_For_Transliteration +" as translit ";
						
            }
            
            //do the job do not return
            Result res = graphDb.execute(query);
            try{
                while (res.hasNext()) {
                    Map<String,Object> row = res.next();
                    long idVal = getNeo4jIdFromObject(row.get("id"));
                    
                    String lname = (String)row.get("lname");
                    
                    long refIdVal = getNeo4jIdFromObject(row.get("refId"));
                    String transliteration = (String)row.get("translit");
                    
                    if(idVal>0 && lname.length()>0){
                        retRows.add(new Return_Nodes_Row(idVal,lname,refIdVal,transliteration));
                    }
                    else{
                        abort = true;
                        break;
                    }
                }                
            }
            catch(Exception ex){
                utils.handleException(ex);
                abort = true;                
            }
            finally{
                res.close();
                res = null;
            }
            
            if(abort){
                retRows.clear();
                return APIFail;
            }
        }//while loop ending
        
        
        return APISucc;
    }
    
    //if everything is in token level then link can be easily queried via 1 cypher query
    int get_Bulk_Return_Full_Link_Rows(ArrayList<Long> linkIds, ArrayList<Return_Full_Link_Row> retRows,ArrayList<Long> b_set){
        ArrayList<Node> nodes = getNeo4jNodesByNeo4jIds(linkIds);
        if(linkIds.size()!= nodes.size()){
            return APIFail;
        }
        retRows.clear();
        //check if everything is type attribute
        TraversalDescription bothDirectionsTr = this.graphDb.traversalDescription()
                .uniqueness(Uniqueness.RELATIONSHIP_GLOBAL)
                //depth one is the only one that we are intereset in
                .evaluator(Evaluators.toDepth(1))
                //only follow relationships of type Relation in both directions
                .relationships(Configs.Rels.RELATION, Direction.BOTH)
                .relationships(Configs.Rels.INSTANCEOF, Direction.OUTGOING)
                //an extra evaluator is needed for excluding starting nodes that are not attibutes
                .evaluator(new Evaluator() {
                        public Evaluation evaluate(Path path) {

                            if(path.length()==0){
                                Node endNode = path.endNode();
                                //only interested in attributes
                                if(endNode.hasLabel(Configs.Labels.Type_Attribute)==false){
                                    //we are interested though in their id 
                                    return Evaluation.INCLUDE_AND_PRUNE;
                                }
                                return Evaluation.INCLUDE_AND_CONTINUE;
                            }
                            //PROBABLY NOT NEEDED AS WE HAVE THE .evaluator(Evaluators.toDepth(1))
                            return Evaluation.INCLUDE_AND_PRUNE;
                        }
                    });
        
        /* Return_Full_Link_Row fields
        PrimitiveObject_Long linkId;
        StringObject cls;
        StringObject label;
        StringObject categ;
        StringObject fromCls;
        CMValue cmv;
        IntegerObject unique_category;
        IntegerObject traversed;
          Return_Full_Link_Row(long lId, 
            String clsStr, 
            String labelStr,
            String categStr,
            String fromClsStr,             
            CMValue cmvVal,
            int unique_categoryVal,
            int traversedVal){
        */
        
        //each link has 1 and only one from value
        HashMap<Long, String> Clss = new HashMap<Long,String>();
        HashMap<Long, String> labels = new HashMap<Long,String>();
        //link id i already have
        
        HashMap<Long, String> categs = new HashMap<Long,String>();
        HashMap<Long, String> fromClss = new HashMap<Long,String>();
        
        //do not need it for return just to check unique and traversed categories
        HashMap<Long, Long> categIds = new HashMap<Long,Long>();
        
        
        //each link must have one and only one to value (node or primitive) 
        //but may be also the from value of another link
        HashMap<Long, CMValue> cmvalues = new HashMap<Long,CMValue>();
        
        HashMap<Long, Boolean> uniqueCategs = new HashMap<Long,Boolean>();
        HashMap<Long, Boolean> traversedCategs = new HashMap<Long,Boolean>();
        
        
        
        for (Path path : bothDirectionsTr.traverse(nodes)) {
            Node pathStartNode = path.startNode();
            long pathStartNodeId = getNodeNeo4jId(pathStartNode);
            
            //take all lnames and primitive values if exist
            if(path.length()==0){
                if(labels.containsKey(pathStartNodeId)==false){
                    String lname = (String) pathStartNode.getProperty(Configs.Neo4j_Key_For_Logicalname);
                    labels.put(pathStartNodeId, lname);
                }
                
                if(linkIds.contains(pathStartNodeId)){
                    
                    //get primitive if exists
                    if(nodeHasPrimitive(pathStartNode)){
                        CMValue cmVal = getCmvalueFromNodeOrNull(pathStartNode,true);
                        if(cmVal!=null){
                            if(cmvalues.containsKey(pathStartNodeId)){
                                if(Configs.boolDebugInfo){
                                    Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "Link with id: {0} found with more that one values. {1} {2}", new Object[]{pathStartNodeId, cmVal.toString(), cmvalues.get(pathStartNodeId).toString()});
                                }
                                return APIFail;
                            }
                            cmvalues.put(pathStartNodeId, cmVal);
                        }
                    }
                }
            }
            else{
                for(Relationship rel : path.relationships()){
                    
                    if(rel.isType(Configs.Rels.INSTANCEOF)){
                        long startNodeId = getNodeNeo4jId(rel.getStartNode());
                        Node classNode = rel.getEndNode();
                        long classId = getNodeNeo4jId(classNode);
                        
                        //category of the returned link (from_cls, categ).
                        //Flag unique_category indicates if given category is unique (link object may have more than one class)
                        
                        //if(classids.containsKey(startNodeId)==false){
                        //    classids.put(startNodeId, new PQI_Set());
                        //}
                        //classids.get(startNodeId).set_putNeo4j_Id(classId);
                        if(categIds.containsKey(startNodeId)==false){
                            categIds.put(startNodeId, classId);
                            String categName = (String)classNode.getProperty(Configs.Neo4j_Key_For_Logicalname);
                            categs.put(startNodeId, categName);
                            if(classNode.hasLabel(Configs.Labels.Type_Attribute)==false){
                                throw new UnsupportedOperationException();
                            }
                            Relationship clasFromRel = classNode.getSingleRelationship(Configs.Rels.RELATION, Direction.INCOMING);
                            String fclsname =(String) clasFromRel.getStartNode().getProperty(Configs.Neo4j_Key_For_Logicalname);
                            fromClss.put(startNodeId, fclsname);
                        }
                        else{
                            if(categIds.get(startNodeId)!=classId){
                                uniqueCategs.put(startNodeId, false);
                            }
                        }
                        
                        if(b_set.contains(startNodeId)){
                            traversedCategs.put(startNodeId, true);
                        }
                        
                        //fromCls
                        continue;
                    }
                    
                    Node linkNode = null;
                    long linkId = -1;
                    boolean isForward = false;
                    linkNode = rel.getStartNode();
                    linkId = getNodeNeo4jId(linkNode);
                    Node otherNode = null;
                    
                    if(linkId == pathStartNodeId){

                        isForward = true;
                        otherNode = rel.getEndNode();
                        
                        //relEndNode = rel.getEndNode();                    
                    }
                    else{
                        isForward = false;
                        //relEndNode = rel.getStartNode();
                        linkNode = rel.getEndNode();
                        linkId = getNodeNeo4jId(linkNode);
                        otherNode = rel.getStartNode();
                    }
                    
                    if(isForward){ //get the to value otherNode
                        if(otherNode.hasLabel(Configs.Labels.Type_Attribute)){
                            //attibute to attribute case exclude from results
                        }
                        else{
                            String toLname = (String) otherNode.getProperty(Configs.Neo4j_Key_For_Logicalname);
                            Long toId = getNodeNeo4jId(otherNode);
                            CMValue cmVal = new CMValue();
                            cmVal.assign_node(toLname, toId);
                            if(cmvalues.containsKey(linkId)){
                                if(Configs.boolDebugInfo){
                                    Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "Link with id: " +linkId +" found with more that one values. " + cmVal.toString() +" " +cmvalues.get(linkId).toString());
                                }
                                return APIFail;
                            }
                            cmvalues.put(linkId, cmVal);
                        }
                    }
                    else{//get the from cls value from otherNode
                        String cls = (String) otherNode.getProperty(Configs.Neo4j_Key_For_Logicalname);
                        Clss.put(linkId, cls);
                    }
                }//relationships iterator
            }//else case of if path.length() ==0
            
        }//paths iterator
        
        
        
        for(Long linkId : linkIds){
            String cls="";
            String label ="";
            //linkId
            String categ="";
            String fromCls="";
            CMValue cmval = new CMValue();
            cmval.assign_empty();
            int unique = QClass.APISucc;
            int traversed = QClass.FORWARD;
            
            if(Clss.containsKey(linkId)){
                cls=Clss.get(linkId);
            }
            if(labels.containsKey(linkId)){
                label = labels.get(linkId);
            }
            if(categs.containsKey(linkId)){
                categ = categs.get(linkId);
            }
            if(fromClss.containsKey(linkId)){
                fromCls = fromClss.get(linkId);
            }
            if(cmvalues.containsKey(linkId)){
                cmvalues.get(linkId).copyToOtherObject(cmval);
            }
            if(uniqueCategs.containsKey(linkId)){
                unique = QClass.APIFail;
            }
            if(traversedCategs.containsKey(linkId)){
                traversed = QClass.BACKWARD;
            }
            retRows.add(new Return_Full_Link_Row(linkId, cls,label,categ,fromCls,cmval,unique,traversed));
        }
        
        
        return APISucc;
    }
    
    int resetCounter_For_Neo4jId(){
        
        //update MaxNeo4j_Id property in Telos_Object node
            String query = "MATCH(n:"+Configs.CommonLabelName+") with max (n."+Configs.Neo4j_Key_For_Neo4j_Id+") as newVal " +
                    "MATCH(t:"+Configs.CommonLabelName+"{"+Configs.Neo4j_Key_For_Logicalname+":\""+Configs.Neo4j_Node_LogicalName_For_MaxNeo4jId+"\"}) " +
                    "SET t."+Configs.Neo4j_Key_For_MaxNeo4jId+" = newVal " +
                    "return t."+Configs.Neo4j_Key_For_MaxNeo4jId+" as "+ Configs.Neo4j_Key_For_MaxNeo4jId;
            Result res = null;
            try{
                res = graphDb.execute(query);
            }
            catch(Exception ex){
                utils.handleException(ex);
            }
            if (res == null) {
                return APIFail;
            }
            else{
                res.close();
            }
                
        

        return APISucc;
    }
    
    int resetCounter_For_ThesaurusReferenceId(String thesaurusName, long resetToSpecifiedValue){
        //another policy might be to count Facets, Hierarchies, Terms and assign the sum of them. But this is more flexible
                
        //update MaxNeo4j_Id property in Thesarus node
        /* e.g. 
        Match(n:Common{Logicalname:"Thesaurus`ANCIENT"})<-[:RELATION]-(link:Common{Logicalname:"ANCIENT`of_thesaurus"})<-[:RELATION]-(m)<-[:ISA*0..]-(k)<-[:INSTANCEOF*0..1]-(p) with max(p.ThesaurusReferenceId) as newVal return newVal
        */
        String query = "";
        if(resetToSpecifiedValue>0){
           query = " MATCH(t:"+Configs.CommonLabelName+"{"+Configs.Neo4j_Key_For_Logicalname+":\""+Configs.Neo4j_Node_LogicalName_For_MaxThesaurusReferenceId.replace("%THES%", thesaurusName.toUpperCase())+"\"}) " +
                    " SET t."+Configs.Neo4j_Key_For_MaxThesaurusReferenceId+" = "+resetToSpecifiedValue + 
                    " return t."+Configs.Neo4j_Key_For_MaxThesaurusReferenceId+" as "+ Configs.Neo4j_Key_For_MaxThesaurusReferenceId;
        }
        else{
            query = " Match(n:"+Configs.CommonLabelName+"{"+Configs.Neo4j_Key_For_Logicalname+":\"Thesaurus`"+thesaurusName.toUpperCase()+"\"}) "+ 
                    " <-[:RELATION]-(link:"+Configs.CommonLabelName+"{"+Configs.Neo4j_Key_For_Logicalname+":\""+thesaurusName.toUpperCase()+"`of_thesaurus\"})<-[:RELATION]-(m)<-[:ISA*0..]-(k)<-[:INSTANCEOF*0..1]-(p) with max(p."+Configs.Neo4j_Key_For_ThesaurusReferenceId+") as newVal " +
                    " MATCH(t:"+Configs.CommonLabelName+"{"+Configs.Neo4j_Key_For_Logicalname+":\""+Configs.Neo4j_Node_LogicalName_For_MaxThesaurusReferenceId.replace("%THES%", thesaurusName.toUpperCase())+"\"}) " +
                    " SET t."+Configs.Neo4j_Key_For_MaxThesaurusReferenceId+" = newVal " +
                    " return t."+Configs.Neo4j_Key_For_MaxThesaurusReferenceId+" as "+ Configs.Neo4j_Key_For_MaxThesaurusReferenceId;
        
        }
        //System.out.println("resetCounter_For_ThesaurusReferenceId\r\n============================\r\n"+query);
        
        Result res = null;
        try{
            res = graphDb.execute(query);
        }
        catch(Exception ex){
            utils.handleException(ex);
        }
        if (res == null) {
            return APIFail;
        }
        else{
            res.close();                
        }
         
        
        return APISucc;
    }
    
    int get_Bulk_Return_Full_Link_Id_Rows(ArrayList<Long> linkIds, ArrayList<Return_Full_Link_Id_Row> retRows){
        /*
        - The following query is applicable to tokens where categ is instance of 1 and only one category 
        - needs some optimization 
        --    instead of , in MATCH use cascading matches with WITH clause
        --    categs should include the sub-superclasses??? not here but this stands in get_links_to_by_category
        --    ArrayList<Node> nodes = getNeo4jNodesByNeo4jIds(linkIds); is useless anymore but we could ask if everything exists and also get the list of things that are not tokens
        e.g. Neo4j_Id:60470 Logicalname:AATDEMOEN`cameras
        MATCH(n:Common{Logicalname:"AATDEMOEN`cameras"})-[:RELATION]-(m) return m.Neo4j_Id
        
        MATCH (from)-[:RELATION]-> (n:Common:Type_Attribute) -[:RELATION*0..1]->(to) 
               ,(n:Common:Type_Attribute) -[:INSTANCEOF]->(categ)<-[:RELATION]-(fromSuper) 
         WHERE n.Neo4j_Id IN [1559496, 1559495, 1559513, 1559511, 1559512, 1559508, 1559510, 1559509, 1559505, 
                                1559506, 1559507, 1559503, 1559504, 1559500, 1559501, 1559502, 1559497, 1559498, 
                                1559499, 1091565, 1091564, 1012758, 940785, 60471, 251787, 251785, 369288, 369286, 
                                458306, 458308, 458302, 458304, 458314, 458310, 458312, 458300, 458298, 523483, 719265, 
                                719263, 719264, 719262, 719261, 719259, 719260, 719258, 719257, 851241, 851237, 851239, 
                                851240, 851238, 62048, 61667, 60912, 60791, 64164, 62977, 62778, 64592, 65618, 64994, 
                                66617, 66559, 66694, 68054, 68773, 68932, 68271] 
                   AND ( has(n.Type) OR to.Neo4j_Id <> n.Neo4j_Id ) 
         RETURN from.Neo4j_Id as fromId, from.Logicalname as fromLabel, n.Neo4j_Id as linkId, n.Logicalname as linkLabel, n.Type as linkTypeVal, 
                n.Value as linkVal, to.Neo4j_Id as toId, to.Logicalname as toLabel, categ.Neo4j_Id as categId, categ.Logicalname as categLabel, 
                fromSuper.Logicalname as categFromLabel  
        */
        ArrayList<Node> nodes = getNeo4jNodesByNeo4jIds(linkIds);
        if(linkIds.size()!= nodes.size()){
            return APIFail;
        }
        retRows.clear();
        //check if everything is type attribute
        TraversalDescription bothDirectionsTr = this.graphDb.traversalDescription()
                .uniqueness(Uniqueness.RELATIONSHIP_GLOBAL)
                //depth one is the only one that we are intereset in
                .evaluator(Evaluators.toDepth(1))
                //only follow relationships of type Relation in both directions
                .relationships(Configs.Rels.RELATION, Direction.BOTH)
                .relationships(Configs.Rels.INSTANCEOF, Direction.OUTGOING)
                //an extra evaluator is needed for excluding starting nodes that are not attibutes
                .evaluator(new Evaluator() {
                        public Evaluation evaluate(Path path) {

                            if(path.length()==0){
                                Node endNode = path.endNode();
                                //only interested in attributes
                                if(endNode.hasLabel(Configs.Labels.Type_Attribute)==false){
                                    //we are interested though in their id 
                                    return Evaluation.INCLUDE_AND_PRUNE;
                                }
                                return Evaluation.INCLUDE_AND_CONTINUE;
                            }
                            //PROBABLY NOT NEEDED AS WE HAVE THE .evaluator(Evaluators.toDepth(1))
                            return Evaluation.INCLUDE_AND_PRUNE;
                        }
                    });
        /*
        Return_Full_Link_Id_Row r;
        r.get_v1_cls();
        r.get_v2_clsid();
        r.get_v3_label();
        r.get_v4_linkId();
        r.get_v5_categ();
        r.get_v6_fromCls();
        r.get_v7_categid();
        r.get_v8_cmv();
        r.get_v9_unique_category();
        */
        
        //each link has 1 and only one from value
        HashMap<Long, String> Clss = new HashMap<Long,String>();
        
        HashMap<Long, Long> ClsIds = new HashMap<Long,Long>();
        HashMap<Long, Long> ClsRefIds = new HashMap<Long,Long>();
        HashMap<Long, String> ClsTransliterations = new HashMap<Long,String>();
        HashMap<Long, String> labels = new HashMap<Long,String>();
        //link id i already have
        
        HashMap<Long, String> categs = new HashMap<Long,String>();
        HashMap<Long, String> fromClss = new HashMap<Long,String>();
        HashMap<Long, Long> categIds = new HashMap<Long,Long>();
        //each link must have one and only one to value (node or primitive) 
        //but may be also the from value of another link
        HashMap<Long, CMValue> cmvalues = new HashMap<Long,CMValue>();
        
        HashMap<Long, Boolean> uniqueCategs = new HashMap<Long,Boolean>();
        
        
        for (Path path : bothDirectionsTr.traverse(nodes)) {
            Node pathStartNode = path.startNode();
            long pathStartNodeId = getNodeNeo4jId(pathStartNode);
            
            //take all lnames and primitive values if exist
            if(path.length()==0){
                if(labels.containsKey(pathStartNodeId)==false){
                    String lname = (String) pathStartNode.getProperty(Configs.Neo4j_Key_For_Logicalname);
                    labels.put(pathStartNodeId, lname);
                }
                
                if(linkIds.contains(pathStartNodeId)){
                    
                    //get primitive if exists
                    if(nodeHasPrimitive(pathStartNode)){
                        CMValue cmVal = getCmvalueFromNodeOrNull(pathStartNode,true);
                        if(cmVal!=null){
                            if(cmvalues.containsKey(pathStartNodeId)){
                                if(Configs.boolDebugInfo){
                                    Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "Link with id: {0} found with more that one values. {1} {2}", new Object[]{pathStartNodeId, cmVal.toString(), cmvalues.get(pathStartNodeId).toString()});
                                }
                                return APIFail;
                            }
                            cmvalues.put(pathStartNodeId, cmVal);
                        }
                    }
                }
            }
            else{
                for(Relationship rel : path.relationships()){
                    
                    if(rel.isType(Configs.Rels.INSTANCEOF)){
                        long startNodeId = getNodeNeo4jId(rel.getStartNode());
                        Node classNode = rel.getEndNode();
                        long classId = getNodeNeo4jId(classNode);
                        /*
                        category of the returned link (from_cls, categ).
                        Flag unique_category indicates if given category is unique (link object may have more than one class)
                        */
                        //if(classids.containsKey(startNodeId)==false){
                        //    classids.put(startNodeId, new PQI_Set());
                        //}
                        //classids.get(startNodeId).set_putNeo4j_Id(classId);
                        if(categIds.containsKey(startNodeId)==false){
                            categIds.put(startNodeId, classId);
                            String categName = (String)classNode.getProperty(Configs.Neo4j_Key_For_Logicalname);
                            categs.put(startNodeId, categName);
                            if(classNode.hasLabel(Configs.Labels.Type_Attribute)==false){
                                throw new UnsupportedOperationException();
                            }
                            Relationship clasFromRel = classNode.getSingleRelationship(Configs.Rels.RELATION, Direction.INCOMING);
                            String fclsname =(String) clasFromRel.getStartNode().getProperty(Configs.Neo4j_Key_For_Logicalname);
                            fromClss.put(startNodeId, fclsname);
                        }
                        else{
                            if(categIds.get(startNodeId)!=classId){
                                uniqueCategs.put(startNodeId, false);
                            }
                        }
                        
                        //fromCls
                        continue;
                    }
                    
                    Node linkNode = null;
                    long linkId = -1;
                    boolean isForward = false;
                    linkNode = rel.getStartNode();
                    linkId = getNodeNeo4jId(linkNode);
                    Node otherNode = null;
                    
                    if(linkId == pathStartNodeId){

                        isForward = true;
                        otherNode = rel.getEndNode();
                        
                        //relEndNode = rel.getEndNode();                    
                    }
                    else{
                        isForward = false;
                        //relEndNode = rel.getStartNode();
                        linkNode = rel.getEndNode();
                        linkId = getNodeNeo4jId(linkNode);
                        otherNode = rel.getStartNode();
                    }
                    
                    if(isForward){ //get the to value otherNode
                        if(otherNode.hasLabel(Configs.Labels.Type_Attribute)){
                            //attibute to attribute case exclude from results
                        }
                        else{
                            String toLname = (String) otherNode.getProperty(Configs.Neo4j_Key_For_Logicalname);
                            Long toId = getNodeNeo4jId(otherNode);
                            CMValue cmVal = new CMValue();
                            Long toRefId = getNodeThesaurusReferenceId(otherNode);
                            String translit = "";
                            if(otherNode.hasProperty(Configs.Neo4j_Key_For_Transliteration)){
                                translit=(String) otherNode.getProperty(Configs.Neo4j_Key_For_Transliteration);
                            }
                            
                            cmVal.assign_node(toLname, toId,translit,toRefId);
                            
                            if(cmvalues.containsKey(linkId)){
                                if(Configs.boolDebugInfo){
                                    Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "Link with id: {0} found with more that one values. {1} {2}", new Object[]{linkId, cmVal.toString(), cmvalues.get(linkId).toString()});
                                }
                                return APIFail;
                            }
                            cmvalues.put(linkId, cmVal);
                        }
                    }
                    else{//get the from cls value from otherNode
                        String cls = (String) otherNode.getProperty(Configs.Neo4j_Key_For_Logicalname);
                        Clss.put(linkId, cls);
                        ClsIds.put(linkId, getNodeNeo4jId(otherNode));
                        
                        String clsTranslLit = "";
                        if(otherNode.hasProperty(Configs.Neo4j_Key_For_Transliteration)){
                            clsTranslLit = (String) otherNode.getProperty(Configs.Neo4j_Key_For_Transliteration);
                        }
                        ClsTransliterations.put(linkId, clsTranslLit);
                        long clsRefId = getNodeThesaurusReferenceId(otherNode);
                        ClsRefIds.put(linkId, clsRefId);
                        
                    }
                }//relationships iterator
            }//else case of if path.length() ==0
            
        }//paths iterator
        
        /*
        HashMap<Long, String> Clss = new HashMap<Long,String>();
        HashMap<Long, Long> ClsIds = new HashMap<Long,Long>();
        HashMap<Long, String> labels = new HashMap<Long,String>();
        //link id i already have
        
        HashMap<Long, String> categs = new HashMap<Long,String>();
        HashMap<Long, String> fromClss = new HashMap<Long,String>();
        HashMap<Long, Long> categIds = new HashMap<Long,Long>();
        //each link must have one and only one to value (node or primitive) 
        //but may be also the from value of another link
        HashMap<Long, CMValue> cmvalues = new HashMap<Long,CMValue>();
        
        HashMap<Long, Boolean> uniqueCategs = new HashMap<Long,Boolean>();
        */
        for(Long linkId : linkIds){
            String cls="";
            String clsTranslit="";
            long clsId=-1;
            long clsRefId=-1;
            String label ="";
            //linkId
            String categ="";
            String fromCls="";
            long categId =-1;
            CMValue cmval = new CMValue();
            cmval.assign_empty();
            int unique = QClass.APISucc;
            
            if(Clss.containsKey(linkId)){
                cls=Clss.get(linkId);
            }
            if(ClsTransliterations.containsKey(linkId)){
                clsTranslit=ClsTransliterations.get(linkId);
            }
            if(ClsIds.containsKey(linkId)){
                clsId=ClsIds.get(linkId);
            }
            if(ClsRefIds.containsKey(linkId)){
                clsRefId=ClsRefIds.get(linkId);
            }
            if(labels.containsKey(linkId)){
                label = labels.get(linkId);
            }
            if(categs.containsKey(linkId)){
                categ = categs.get(linkId);
            }
            if(fromClss.containsKey(linkId)){
                fromCls = fromClss.get(linkId);
            }
            if(categIds.containsKey(linkId)){
                categId = categIds.get(linkId);
            }
            if(cmvalues.containsKey(linkId)){
                cmvalues.get(linkId).copyToOtherObject(cmval);
            }
            if(uniqueCategs.containsKey(linkId)){
                unique = QClass.APIFail;
            }
            
            retRows.add(new Return_Full_Link_Id_Row(linkId, cls,clsId,label,categ,fromCls,categId,cmval,unique,clsRefId,clsTranslit));
        }
        
        return APISucc;
        
        
    }
    int get_Bulk_Return_Link_Id_Rows(ArrayList<Long> linkIds, ArrayList<Return_Link_Id_Row> retRows, PQI_Set b_set){
        ArrayList<Node> nodes = getNeo4jNodesByNeo4jIds(linkIds);
        if(linkIds.size()!= nodes.size()){
            return APIFail;
        }
        retRows.clear();
        //check if everything is type attribute
        TraversalDescription bothDirectionsTr = this.graphDb.traversalDescription()
                .uniqueness(Uniqueness.RELATIONSHIP_GLOBAL)
                //depth one is the only one that we are intereset in
                .evaluator(Evaluators.toDepth(1))
                //only follow relationships of type Relation in both directions
                .relationships(Configs.Rels.RELATION, Direction.BOTH)
                .relationships(Configs.Rels.INSTANCEOF, Direction.OUTGOING)
                //an extra evaluator is needed for excluding starting nodes that are not attibutes
                .evaluator(new Evaluator() {
                        public Evaluation evaluate(Path path) {

                            if(path.length()==0){
                                Node endNode = path.endNode();
                                //only interested in attributes
                                if(endNode.hasLabel(Configs.Labels.Type_Attribute)==false){
                                    //we are interested though in their id 
                                    return Evaluation.INCLUDE_AND_PRUNE;
                                }
                                return Evaluation.INCLUDE_AND_CONTINUE;
                            }
                            //PROBABLY NOT NEEDED AS WE HAVE THE .evaluator(Evaluators.toDepth(1))
                            return Evaluation.INCLUDE_AND_PRUNE;
                        }
                    });
        
        //each link has 1 and only one from value
        HashMap<Long, Long> fromClsId = new HashMap<Long,Long>();
        HashMap<Long, String> fromClsass = new HashMap<Long,String>();
        
        //each link must have one and only one to value (node or primitive) 
        //but may be also the from value of another link
        HashMap<Long, CMValue> cmvalues = new HashMap<Long,CMValue>();
        
        HashMap<Long, PQI_Set> classids = new HashMap<Long,PQI_Set>();
        
        
        for (Path path : bothDirectionsTr.traverse(nodes)) {
            Node pathStartNode = path.startNode();
            long pathStartNodeId = getNodeNeo4jId(pathStartNode);
            
            //take all lnames and primitive values if exist
            if(path.length()==0){
                
                if(linkIds.contains(pathStartNodeId)){
                    
                    //get primitive if exists
                    if(nodeHasPrimitive(pathStartNode)){
                        CMValue cmVal = getCmvalueFromNodeOrNull(pathStartNode,true);
                        if(cmVal!=null){
                            if(cmvalues.containsKey(pathStartNodeId)){
                                if(Configs.boolDebugInfo){
                                    Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "Link with id: " +pathStartNodeId +" found with more that one values. " + cmVal.toString() +" " +cmvalues.get(pathStartNodeId).toString());
                                }
                                return APIFail;
                            }
                            cmvalues.put(pathStartNodeId, cmVal);
                        }
                    }
                }
            }
            else{
                for(Relationship rel : path.relationships()){
                    
                    if(rel.isType(Configs.Rels.INSTANCEOF)){
                        long startNodeId = getNodeNeo4jId(rel.getStartNode());
                        long classId = getNodeNeo4jId(rel.getEndNode());
                        if(classids.containsKey(startNodeId)==false){
                            classids.put(startNodeId, new PQI_Set());
                        }
                        classids.get(startNodeId).set_putNeo4j_Id(classId);
                        
                        continue;
                    }
                    
                    Node linkNode = null;
                    long linkId = -1;
                    boolean isForward = false;
                    linkNode = rel.getStartNode();
                    linkId = getNodeNeo4jId(linkNode);
                    Node otherNode = null;
                    
                    if(linkId == pathStartNodeId){

                        isForward = true;
                        otherNode = rel.getEndNode();
                        
                        //relEndNode = rel.getEndNode();                    
                    }
                    else{
                        isForward = false;
                        //relEndNode = rel.getStartNode();
                        linkNode = rel.getEndNode();
                        linkId = getNodeNeo4jId(linkNode);
                        otherNode = rel.getStartNode();
                    }
                    
                    if(isForward){ //get the to value otherNode
                        if(otherNode.hasLabel(Configs.Labels.Type_Attribute)){
                            //attibute to attribute case exclude from results
                        }
                        else{
                            String toLname = (String) otherNode.getProperty(Configs.Neo4j_Key_For_Logicalname);
                            Long toId = getNodeNeo4jId(otherNode);
                            CMValue cmVal = new CMValue();
                            cmVal.assign_node(toLname, toId);
                            if(cmvalues.containsKey(linkId)){
                                if(Configs.boolDebugInfo){
                                    Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "Link with id: {0} found with more that one values. {1} {2}", new Object[]{linkId, cmVal.toString(), cmvalues.get(linkId).toString()});
                                }
                                return APIFail;
                            }
                            cmvalues.put(linkId, cmVal);
                        }
                    }
                    else{//get the from cls value from otherNode
                        String cls = (String) otherNode.getProperty(Configs.Neo4j_Key_For_Logicalname);
                        fromClsass.put(linkId, cls);
                        fromClsId.put(linkId, getNodeNeo4jId(otherNode));
                    }
                }//relationships iterator
            }//else case of if path.length() ==0
            
        }//paths iterator
        
        linkIds.stream().forEach((linkId) -> {
            long fromClsIdVal = 0;
            String fromClsName ="";
            CMValue cmval = new CMValue();
            cmval.assign_empty();
            int traversedVal = QClass.FORWARD;
            
            if(fromClsId.containsKey(linkId)){
                fromClsIdVal = fromClsId.get(linkId);
            }
            if(fromClsass.containsKey(linkId)){
                fromClsName = fromClsass.get(linkId);
            }
            if(cmvalues.containsKey(linkId)){
                cmvalues.get(linkId).copyToOtherObject(cmval);
            }
            
            if(classids.containsKey(linkId)){
                if(classids.get(linkId).set_disjoint(b_set)==false){
                    traversedVal = QClass.BACKWARD;
                }
            }
            retRows.add(new Return_Link_Id_Row(linkId,fromClsIdVal,fromClsName,linkId,cmval,traversedVal));
        });
        
        return APISucc;
    }
    
    int get_Bulk_Return_Isa_Rows(ArrayList<Long> linkIds, ArrayList<Return_Isa_Row> retRows){
        ArrayList<Node> nodes = getNeo4jNodesByNeo4jIds(linkIds);
        if(linkIds.size()!= nodes.size()){
            return APIFail;
        }
        retRows.clear();
        
        //check if everything is type attribute
        TraversalDescription bothDirectionsTr = this.graphDb.traversalDescription()
                .uniqueness(Uniqueness.RELATIONSHIP_GLOBAL)
                //depth one is the only one that we are intereset in
                .evaluator(Evaluators.toDepth(1))
                //only follow relationships of type Relation in both directions
                .relationships(Configs.Rels.ISA, Direction.OUTGOING)
                //we do not need any other evaluator apart from depth
                /*
                .evaluator(new Evaluator() {
                        public Evaluation evaluate(Path path) {

                            Node endNode = path.endNode();

                            if(path.length()==0){
                                
                                //only interested in attributes
                                if(endNode.hasLabel(Configs.Labels.Type_Attribute)==false){
                                    //we are interested though in their id and logicalname
                                    return Evaluation.INCLUDE_AND_PRUNE;
                                }
                                return Evaluation.INCLUDE_AND_CONTINUE;
                            }
                            //PROBABLY NOT NEEDED AS WE HAVE THE .evaluator(Evaluators.toDepth(1))
                            return Evaluation.INCLUDE_AND_PRUNE;
                        }
                    })*/;
        HashMap<String, ArrayList<String>> isaRelationships = new HashMap<String, ArrayList<String>>();
        for (Path path : bothDirectionsTr.traverse(nodes)) {
            for(Relationship rel : path.relationships()){
                String startNodeName = (String)rel.getStartNode().getProperty(Configs.Neo4j_Key_For_Logicalname);
                String endNodeName = (String)rel.getEndNode().getProperty(Configs.Neo4j_Key_For_Logicalname);
                //long startNodeId = getNodeNeo4jId(rel.getStartNode());
                //long endNodeId = getNodeNeo4jId(rel.getEndNode());
                if(isaRelationships.containsKey(startNodeName)==false){
                    isaRelationships.put(startNodeName, new ArrayList<String>());
                }
                if(isaRelationships.get(startNodeName).contains(endNodeName)==false){
                   isaRelationships.get(startNodeName).add(endNodeName);
                }
            }
        }
        
        isaRelationships.keySet().stream().forEach((startNodeName) -> {
            ArrayList<String> endNodeNames = isaRelationships.get(startNodeName);
            
            endNodeNames.stream().forEach((endName) -> {
                retRows.add(new Return_Isa_Row(startNodeName, endName));
            });
        });
        
        return APISucc;
    }
    
    int get_Bulk_Return_Link_Rows(ArrayList<Long> linkIds, ArrayList<Return_Link_Row> retRows){
        
        ArrayList<Node> nodes = getNeo4jNodesByNeo4jIds(linkIds);
        if(linkIds.size()!= nodes.size()){
            return APIFail;
        }
        
        retRows.clear();
        
        //check if everything is type attribute
        TraversalDescription bothDirectionsTr = this.graphDb.traversalDescription()
                .uniqueness(Uniqueness.RELATIONSHIP_GLOBAL)
                //depth one is the only one that we are intereset in
                .evaluator(Evaluators.toDepth(1))
                //only follow relationships of type Relation in both directions
                .relationships(Configs.Rels.RELATION, Direction.BOTH)
                //an extra evaluator is needed for excluding starting nodes that are not attibutes
                .evaluator(new Evaluator() {
                        public Evaluation evaluate(Path path) {

                            Node endNode = path.endNode();

                            if(path.length()==0){
                                
                                //only interested in attributes
                                if(endNode.hasLabel(Configs.Labels.Type_Attribute)==false){
                                    //we are interested though in their id and logicalname
                                    return Evaluation.INCLUDE_AND_PRUNE;
                                }
                                return Evaluation.INCLUDE_AND_CONTINUE;
                            }
                            //PROBABLY NOT NEEDED AS WE HAVE THE .evaluator(Evaluators.toDepth(1))
                            return Evaluation.INCLUDE_AND_PRUNE;
                        }
                    });
        
        
        
        HashMap<Long, String> lnames = new HashMap<Long,String>();
        HashMap<Long, String> fromCls = new HashMap<Long,String>();
        HashMap<Long, CMValue> cmvs = new HashMap<Long,CMValue>();
        
        HashMap<Long, Long> ClsRefIds = new HashMap<Long,Long>();
        HashMap<Long, String> ClsTransliterations = new HashMap<Long,String>();
        
        for (Path path : bothDirectionsTr.traverse(nodes)) {
            Node pathStartNode = path.startNode();
            long pathStartNodeId = getNodeNeo4jId(pathStartNode);
            String lname = (String) pathStartNode.getProperty(Configs.Neo4j_Key_For_Logicalname);
            
            
            //take all lnames and primitive values if exist
            if(path.length()==0){
                
                if(linkIds.contains(pathStartNodeId)&& lnames.containsKey(pathStartNodeId) ==false){
                    
                    lnames.put(pathStartNodeId, lname);
                    //ClsRefIds.put(pathStartNodeId,startNodeRefId);
                    //ClsTransliterations.put(startNodeRefId, startNodTtransliterationStr);
                    
                    
                    //get primitive if exists
                    if(nodeHasPrimitive(pathStartNode)){
                        CMValue cmVal = getCmvalueFromNodeOrNull(pathStartNode,true);
                        if(cmVal!=null){
                            if(cmvs.containsKey(pathStartNodeId)){
                                if(Configs.boolDebugInfo){
                                    Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "Link with id: " +pathStartNodeId +" found with more that one values. " + cmVal.toString() +" " +cmvs.get(pathStartNodeId).toString());
                                }
                                return APIFail;
                            }
                            cmvs.put(pathStartNodeId, cmVal);
                        }
                    }
                }
            }
            else{
                for(Relationship rel : path.relationships()){
                    Node linkNode = null;
                    long linkId = -1;
                    boolean isForward = false;
                    linkNode = rel.getStartNode();
                    linkId = getNodeNeo4jId(linkNode);
                    Node otherNode = null;
                    
                    if(linkId == pathStartNodeId){

                        isForward = true;
                        otherNode = rel.getEndNode();
                        
                        //relEndNode = rel.getEndNode();                    
                    }
                    else{
                        isForward = false;
                        //relEndNode = rel.getStartNode();
                        linkNode = rel.getEndNode();
                        linkId = getNodeNeo4jId(linkNode);
                        otherNode = rel.getStartNode();
                    }
                    
                    if(isForward){ //get the to value otherNode
                        if(otherNode.hasLabel(Configs.Labels.Type_Attribute)){
                            //attibute to attribute case exclude from results
                        }
                        else{
                            String toLname = (String) otherNode.getProperty(Configs.Neo4j_Key_For_Logicalname);
                            Long toId = getNodeNeo4jId(otherNode);
                            CMValue cmVal = new CMValue();
                            Long toRefId = getNodeThesaurusReferenceId(otherNode);
                            String toTransliterationVal = getNodeTransliterationString(otherNode);
                            
                            cmVal.assign_node(toLname, toId,toTransliterationVal,toRefId);
                            if(cmvs.containsKey(linkId)){
                                if(Configs.boolDebugInfo){
                                    Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "Link with id: " +linkId +" found with more that one values. " + cmVal.toString() +" " +cmvs.get(linkId).toString());
                                }
                                return APIFail;
                            }
                            cmvs.put(linkId, cmVal);
                        }
                    }
                    else{//get the from cls value from otherNode
                        String cls = (String) otherNode.getProperty(Configs.Neo4j_Key_For_Logicalname);
                        long clsRefId = getNodeThesaurusReferenceId(otherNode);
                        String clsTtransliterationStr = getNodeTransliterationString(otherNode);
                        fromCls.put(linkId, cls);
                        ClsRefIds.put(linkId, clsRefId);
                        ClsTransliterations.put(linkId, clsTtransliterationStr);
                        
                    }
                }//relationships iterator
            }//else case of if path.length() ==0
            
        }//paths iterator
        //lnames must have taken everything both for individuals and attibutes
        lnames.keySet().stream().forEach((id) -> {
            Long refId = -1L;
            
            String lname = lnames.get(id);
            String className = "";
            String transliterationStr ="";
            CMValue cmval = new CMValue();
            cmval.assign_empty();
            
            if(ClsRefIds.containsKey(id)){
                refId = ClsRefIds.get(id);
            }
            if(fromCls.containsKey(id)){
                className = fromCls.get(id);
            }
            if(ClsTransliterations.containsKey(id)){
                transliterationStr= ClsTransliterations.get(id);
            }
            
            if(cmvs.containsKey(id)){
                cmvs.get(id).copyToOtherObject(cmval);
            }
            
            retRows.add(new Return_Link_Row(id, className,lname,cmval,refId,transliterationStr));
        });
        
        return APISucc;
    }
    boolean  nodeHasPrimitive(Node n){
        if(n.hasProperty(Configs.Key_Primitive_Value) && n.hasProperty(Configs.Key_Primitive_Value_Type)){
            return true;
        }
        return false;
    }
    CMValue getCmvalueFromNodeOrNull(Node n, boolean hasPrimitiveChecked){
        CMValue cmv =new CMValue();
        cmv.assign_empty();
        if(hasPrimitiveChecked|| nodeHasPrimitive(n)){
            String type = (String) n.getProperty(Configs.Key_Primitive_Value_Type);
            Object val =  n.getProperty(Configs.Key_Primitive_Value);
            
            if(type.equals(Configs.Primitive_Value_Type_INT)){
                cmv.assign_int((int)val);
            }
            else if(type.equals(Configs.Primitive_Value_Type_STR)){
                cmv.assign_string((String)val);
            }
        }
        else{
            if(Configs.boolDebugInfo){
                Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "getCmvalueFromNodeOrNull returns null");
            }
            return null;
        }
        
        return cmv;        
    }
    
    int get_Single_Return_Link_Id(long linkId, StringObject fromClass,PrimitiveObject_Long fromClassId, 
            /*StringObject label,*/ PrimitiveObject_Long sysId, CMValue cmv, IntegerObject traversed, PQI_Set b_set){
    
        Node n = getNeo4jNodeByNeo4jId(linkId);
        if(n==null){
            return APIFail;            
        }
        //get sys id
        //label.setValue((String) n.getProperty(Configs.Neo4j_Key_For_Logicalname));
        sysId.setValue(linkId);
        
        if(n.hasLabel(Configs.Labels.Type_Attribute)==false){
            fromClassId.setValue(0);
            fromClass.setValue("");
            cmv.assign_empty();
            traversed.setValue(QClass.FORWARD);
            return APISucc;
        }
        
        //get from cls label
        Relationship fromRel = n.getSingleRelationship(Configs.Rels.RELATION, Direction.INCOMING);
        if(fromRel!=null){
            fromClass.setValue((String)fromRel.getStartNode().getProperty(Configs.Neo4j_Key_For_Logicalname));
            fromClassId.setValue(getNodeNeo4jId(fromRel.getStartNode()));
        }
        
        
        
        //get cmv value (either primitive stored inside the link node or relationship to another non-attribute node
        cmv.assign_empty();
        if(nodeHasPrimitive(n)){
            CMValue tmpCmv = getCmvalueFromNodeOrNull(n,true);
            if(tmpCmv!=null){
                tmpCmv.copyToOtherObject(cmv);
            }           
        }
        else{            
            Iterator<Relationship> relIter = n.getRelationships(Configs.Rels.RELATION, Direction.OUTGOING).iterator();
            while(relIter.hasNext()){
                Node endNode= relIter.next().getEndNode();
                if(endNode.hasLabel(Configs.Labels.Type_Attribute)){

                }
                else{
                    if(cmv.type==CMValue.TYPE_EMPTY){
                        String toNodeLname = (String)endNode.getProperty(Configs.Neo4j_Key_For_Logicalname);
                        long endNodeId = getNodeNeo4jId(endNode);

                        cmv.assign_node(toNodeLname, endNodeId);
                    }
                    else{
                        Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "Error case: link with id:"+ linkId +" returning more than 1 cmvalue " );
                        return APIFail;
                    }
                }
            }
        }
        
        PQI_Set classIds = new PQI_Set();
        Iterator<Relationship> relIter = n.getRelationships(Configs.Rels.INSTANCEOF, Direction.OUTGOING).iterator();
        while(relIter.hasNext()){
            long endNodeId= getNodeNeo4jId(relIter.next().getEndNode());
            classIds.set_putNeo4j_Id(endNodeId);
        }
        
        if(classIds.set_disjoint(b_set)==false){
            traversed.setValue(QClass.BACKWARD);
        }
        else{
            traversed.setValue(QClass.FORWARD);
        }
        return APISucc;
    }
    
    int get_Single_Return_Link(long linkId, StringObject fromClass, StringObject label, CMValue cmv){
        
        Node n = getNeo4jNodeByNeo4jId(linkId);
        if(n==null){
            return APIFail;            
        }
        
        //get label
        label.setValue((String) n.getProperty(Configs.Neo4j_Key_For_Logicalname));
        
        if(n.hasLabel(Configs.Labels.Type_Attribute)==false){
            fromClass.setValue("");
            cmv.assign_empty();
            return APISucc;
        }
        //get from cls label
        Relationship fromRel = n.getSingleRelationship(Configs.Rels.RELATION, Direction.INCOMING);
        if(fromRel!=null){
            fromClass.setValue((String)fromRel.getStartNode().getProperty(Configs.Neo4j_Key_For_Logicalname));
        }
        
        
        //get cmv value (either primitive stored inside the link node or relationship to another non-attribute node
        cmv.assign_empty();
        if(nodeHasPrimitive(n)){
            CMValue tmpCmv = getCmvalueFromNodeOrNull(n,true);
            if(tmpCmv!=null){
                tmpCmv.copyToOtherObject(cmv);
            }           
        }
        else{            
            Iterator<Relationship> relIter = n.getRelationships(Configs.Rels.RELATION, Direction.OUTGOING).iterator();
            while(relIter.hasNext()){
                Node endNode= relIter.next().getEndNode();
                if(endNode.hasLabel(Configs.Labels.Type_Attribute)){

                }
                else{
                    if(cmv.type==CMValue.TYPE_EMPTY){
                        String toNodeLname = (String)endNode.getProperty(Configs.Neo4j_Key_For_Logicalname);
                        long endNodeId = getNodeNeo4jId(endNode);

                        cmv.assign_node(toNodeLname, endNodeId);
                    }
                    else{
                        Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "Error case: link with id:"+ linkId +" returning more than 1 cmvalue " );
                        return APIFail;
                    }
                }
            }
        }
        
        
        
        return APISucc;
    }
    
    int getIntPropertyOfNodes(ArrayList<Long> nodeIds, String propertyKey, HashMap<Long,Integer> retVals){
        
        int loopIndex = 0;
        int maxIndex = nodeIds.size();

        if(maxIndex==0){
            return APISucc;
        }
        
        while (loopIndex < maxIndex) {

            ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex, Configs.MAX_IDS_PER_QUERY,nodeIds);
            loopIndex += subSetofIds.size();
            if(subSetofIds.size()==0){
                break;
            }
            String query ="";        
            if(subSetofIds.size()==1){
                //build query 
                query = "MATCH(n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0))+"}) "+
                        "RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id +" as id, n."+propertyKey +" as i ";
            }
            else {
                //build query 
                query = "MATCH(n"+getCommonLabelStr()+") "+
                        "WHERE n."+Configs.Neo4j_Key_For_Neo4j_Id +" IN " + subSetofIds.toString() +" "+
                        "RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id +" as id, n."+propertyKey +" as i ";
            }
            
            //do the job do not return
            Result res = graphDb.execute(query);
            try{
                while (res.hasNext()) {
                    Map<String, Object> row = res.next();
                    long val = getNeo4jIdFromObject(row.get("id"));
                    
                    int intVal = (int) row.get("i");
                    
                    if(retVals.containsKey(val)==false){
                        retVals.put(val, intVal);
                    }
                    else{
                        if(retVals.get(val)!= intVal){
                            
                            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "ERROR same Neo4j Ids different Int "+ propertyKey+" properties: " + retVals.get(val) +" " + intVal);
                            return APIFail;
                        }
                    }
                    
                    
                }


            }
            catch(Exception ex){
                utils.handleException(ex);
                return APIFail;
            }
            finally{
                res.close();
                res = null;
            }
        }
    
        return APISucc;
    }
    int getLongPropertyOfNodes(ArrayList<Long> nodeIds, String propertyKey, HashMap<Long,Long> retVals){
        
        int loopIndex = 0;
        int maxIndex = nodeIds.size();

        if(maxIndex==0){
            return APISucc;
        }
        
        while (loopIndex < maxIndex) {

            ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex, Configs.MAX_IDS_PER_QUERY, nodeIds);
            loopIndex += subSetofIds.size();
            if(subSetofIds.size()==0){
                break;
            }
            String query ="";        
            if(subSetofIds.size()==1){
                //build query 
                query = "MATCH(n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0))+"}) "+
                        "RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id +" as id, n."+propertyKey +" as i ";
            }
            else {
                //build query 
                query = "MATCH(n"+getCommonLabelStr()+") "+
                        "WHERE n."+Configs.Neo4j_Key_For_Neo4j_Id +" IN " + subSetofIds.toString() +" "+
                        "RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id +" as id, n."+propertyKey +" as i ";
            }
            
            //do the job do not return
            Result res = graphDb.execute(query);
            try{
                while (res.hasNext()) {
                    Map<String, Object> row = res.next();
                    long val = getNeo4jIdFromObject(row.get("id"));
                    
                    long intVal = (long) row.get("i");
                    
                    if(retVals.containsKey(val)==false){
                        retVals.put(val, intVal);
                    }
                    else{
                        if(retVals.get(val)!= intVal){
                            
                            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "ERROR same Neo4j Ids different long "+ propertyKey+" properties: " + retVals.get(val) +" " + intVal);
                            return APIFail;
                        }
                    }

                }
            }
            catch(Exception ex){
                utils.handleException(ex);
                return APIFail;
            }
            finally{
                res.close();
                res = null;
            }
        }
    
        return APISucc;
    }
    
    //get the sysId of link based on loginum
    long getLinkId(String fromCls, String label){
        //Did not follow the C++ approach
        
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //  Returns the SYSID of the link named cat, of class fcl
        //  Returns SYSID 0 if this link doesn't exist
        SYSID sis_api::getSysid(LOGINAM *fcl, char *cat)
        {
                SYSID  classid, categ_id;
                char   *category;

                // We scan to get the first category 
                if ( (category = scan_for_categories(cat)) == NULL) {
                        globalError.putMessage(" >getSysid");
                        return SYSID(0);
                }
                classid = api_translator->getSysid(fcl, SYSID(0));
                if ( (classid.id != 0) && (*category != '\0') ) {
                        LOGINAM tmpcat(category);
                        // Now we get the unique sysid of the given category 
                        categ_id = api_translator->getSysid(&tmpcat, classid);
                }
                else {
                        strcpy(cat, category);
                        free(category);
                        return SYSID(0);
                }
                // we scan to get the other categories until to reach the last of them 
                while (*cat != '\0' ){
                        category = scan_for_categories(cat);
                        LOGINAM tmpcat1(category);
                        categ_id = api_translator->getSysid(&tmpcat1, categ_id);
                }
                if (categ_id.id != 0) {
                        strcpy(cat, category);
                        free(category);
                        return categ_id;
                }
                else {
                        strcpy(cat, category);
                        free(category);
                        return SYSID(0);
                }
        }
        
        
        
        // Returns a category from a string of categories (categories are  
        // seperated by ^). Also, at the end, input variable "categ"       
        // contains the remaining string of categories                     
        
        char *sis_api::scan_for_categories(char *categ)
        {
                char *tmp;
                char *categ_prt;
                int i;
                int started_with_parenthesis = FALSE;


                tmp = (char *)malloc(1024*sizeof(char));
                if (tmp == NULL) {
                        globalError.putMessage("Malloc failed >scan_for_categories");
                        return (char *)0;
                }

                // Forget the leading '(' ! (if this exists) 
                if(categ[0]!='(') {
                        categ_prt= &categ[0];
                        started_with_parenthesis = FALSE;
                } else {
                        categ_prt= &categ[1];
                        started_with_parenthesis = TRUE;
                }

                i = 0;
                do {
                        // Since we don't have reach at the end of the string
                        // or to a category separator continue               
                        while(*categ_prt != '^' && *categ_prt != '\0') {
                                tmp[i++] = *categ_prt++;
                                if (i > INPUT_LOGINAM_SIZE) {
                                        fprintf(stderr, "Logical name :'%s' exceeds INPUT_LOGINAM_SIZE = %d\n",tmp, INPUT_LOGINAM_SIZE);
                                        break;
                                        // =====>         
                                }
                        } // end while 

                        // Since we copied the ), we must delete it 
                        if (( tmp[i-1] == ')') && (started_with_parenthesis == TRUE)) {
                                i--;
                        }

                        if( *categ_prt == '^' || *categ_prt == '\0' ) {
                                break;
                                //===>    
                        }

                } while(1);

                if( *categ_prt != '\0')	{
                        categ_prt++; // Since we copied the ^, we must pass it 
                }
                tmp[i++] = 0;
                strcpy(categ,categ_prt);
                return tmp;
        }
        */
        // </editor-fold>  
        
        if(fromCls==null || fromCls.length()==0 || label==null || label.length()==0){
            return APIFail;
        }
        
        String query = " MATCH (m"+getCommonLabelStr()+"{"+prepareLogicalNameForCypher(fromCls)+"}) "+
                " -[:RELATION]-> (n"+getCommonLabelStr()+"{"+prepareLogicalNameForCypher(label)+"}), "+
                " (n:"+Configs.Neo4j_Key_For_Type_AttributeStr+") " +
                " RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id+ " as id ";
        Result res = graphDb.execute(query);
        long returnVal = APIFail;
        try{
            while (res.hasNext()) {
                Map<String,Object> row = res.next();
                long tmpVal = getNeo4jIdFromObject(row.get("id"));
                
                if(tmpVal>0){
                    if(returnVal==APIFail){
                        returnVal = tmpVal;
                    }
                    else{
                        if(Configs.boolDebugInfo){
                            throw new UnsupportedOperationException("function getLinkId should only find one value >0");
                        }
                        return APIFail;
                    }
                }
                
            }
        }
        catch(Exception ex){
            utils.handleException(ex);
            return APIFail;
        }        
        finally{
            res.close();
            res = null;
        }
        
        return returnVal;                
    }
    
    
    /**
     * get the Neo4j_Id of node based on lname 
     * For unnammed attributes it will return their Neo4j_Id
     * For named attributes it will return APIFail
     * For non-attributes it will return their Neo4j_Id
     * @param lname
     * @return 
     */
    long getClassId(String lname){
        //Did not follow the C++ approach
        
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
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
        
        if(lname==null || lname.length()==0){
            return APIFail;
        }
        String query = "";
        
        //SisServer get_classid returned: 8390956 for Label80092c unnammed A_T
        //SisServer get_classid returned: 0 for of_thesaurus named A_C but not unique logicalname
        //SisServer get_classid returned: 0 for METALA_BT // A_C but with logical name that was unique
        if(lname.matches(Configs.regExForUnNamed)){
             //printf("getSysid( LOGINAM *ln, SYSID sys) called on unnamed link 0x%x\n",id);
            //Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "get_classid called on unnamed link " + lname.getValue());
            query = " MATCH (n"+getCommonLabelStr()+"{"+prepareLogicalNameForCypher(lname)+"}) "+//, (n:"+Configs.Neo4j_Key_For_Type_IndividualStr+") " +
                    //" WHERE \""+Configs.Neo4j_Key_For_Type_AttributeStr+"\" NOT IN labels(n) " +
                    " RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id+ " as id ";
        }
        else{
            query = " MATCH (n"+getCommonLabelStr()+"{"+prepareLogicalNameForCypher(lname)+"}) "+//, (n:"+Configs.Neo4j_Key_For_Type_IndividualStr+") " +
                    " WHERE NOT  (\""+Configs.Neo4j_Key_For_Type_AttributeStr+"\" IN labels(n))  " +
                    " RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id+ " as id ";
        }
        Result res = graphDb.execute(query);
        long returnVal = APIFail;
        try{
            while (res.hasNext()) {

                long tmpVal = getNeo4jIdFromObject(res.next().get("id"));
                
                if(tmpVal>0){
                    if(returnVal==APIFail){
                        returnVal = tmpVal;
                    }
                    else{
                        if(Configs.boolDebugInfo){
                            throw new UnsupportedOperationException("function getClassId should only find one value >0");
                        }
                        return APIFail;
                    }
                }
                
            }
        }
        catch(Exception ex){
            utils.handleException(ex);
            return APIFail;
        }        
        finally{
            res.close();
            res = null;
        }
        
        return returnVal;                
    }
    
    int getStringPropertyOfNode(long nodeId, String propertyKey, StringObject retVal){
        
        if(nodeId<=0 || propertyKey.length()==0){
            return APIFail;
        }
        
        String query = " MATCH (n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(nodeId)+"}) RETURN n."+propertyKey+ " as s ";
        Result res = graphDb.execute(query);
        try{
            while (res.hasNext()) {
                retVal.setValue((String) res.next().get("s"));
                return APISucc;
            }
        }
        catch(Exception ex){
            utils.handleException(ex);
            return APIFail;
        }        
        finally{
            res.close();
            res = null;
        }
        
        return APIFail;
    }
    
    
    long getNeo4jIdFromObject(Object o){
        long retVal = -1;
        if(o!=null){
            if(o instanceof Integer){
                retVal = (int) o;
            }
            else{
                retVal = (long) o;
            }
        }
        //if(Configs.CastNeo4jIdAsInt){
            
        //}
        //else{
        //    retVal = (long) o;
        //}
        
        return retVal;
    }
    
    long getThesaurusReferenceIdFromObject(Object o){
        long retVal = -1;
        if(o!=null){
            if(o instanceof Integer){
                retVal = (int) o;
            }
            else{
                retVal = (long) o;
            }
        }
        //if(Configs.CastNeo4jIdAsInt){
            
        //}
        //else{
        //    retVal = (long) o;
        //}
        
        return retVal;
    }
    
    boolean isNodePrimitive(Node n){
        if(n.hasLabel(Configs.Labels.PrimitiveClass)){
            return true;
        }
        else{
            return false;
        }
    }
    
    int FOR_DELETE_getTraverseByCategory(PQI_Set startingIds, PQI_Set f_set, PQI_Set b_set, int depth, QClass.Traversal_Isa isa, PQI_Set edge_set, PQI_Set retSysids, ArrayList<Long> checked_set){
        //ALMOST NEVER RETURNS API FAIL just in case of exception
        
        // <editor-fold defaultstate="collapsed" desc="C++ code.">
        /*
 //  SET retSysids contains all link objects traversed starting
 //  from object objSysid and traversing all links of category
 //  and direction defined in categs and dirs.
 //  Returns 0 on success, -1 when an error occured.
 
int sis_api::getTraverseByCategory_With_SIS_Server_Implementation(SYSID objSysid, SET *f_set, SET *b_set, int depth, int isa,SET *edge_set, SET *retSysids, SET *checked_set) {
	 
	telos_object *label_ptr;
	SET           isaset;
	SET           tmpset;
	SET           class_set;
	SET           FromSet;
	SYSID         tmpid, fromId, node_id;
	int           ret;
	
	if (checked_set->set_member_of(objSysid)) {
            return (globalError.checkError(" >getTraverseByCategory_With_SIS_Server_Implementation"));
	}
	
	// "checked_set" keeps the sysids of the objects in the hierarchy that are alredy checked. 
	 checked_set->set_put(objSysid);

	 // "f_set" contains the set of categories and all their subclasses. This set is not empty if the traverse direction is forward. 
	 if (f_set->set_get_card()) {
            // get all links that points from object with sysid "objSysid", including class attributes too 
            if (getLinkFrom(objSysid, &tmpset) == -1) {
                globalError.putMessage(" >getTraverseByCategory_With_SIS_Server_Implementation");
                return -1;
            }
        
            tmpset.set_pos(0);
            // traverse the set of links 
            while((tmpid = tmpset.set_get_id()).id) {
                class_set.set_clear();
                
                // get all the classes which "tmpid" is an  immediate instance of 
                if (getClasses(tmpid, &class_set) != -1) {
                    // if the the intersection of "class_set" with "f_set" is not an empty set 
                    if (! (class_set.set_disjoint(f_set))) {
                        if (depth == 0) {
                            edge_set->set_put(objSysid);
                            break;
                        }
                        else {
                            retSysids->set_put(tmpid);   // add label into return set
                            if ((label_ptr = api_sys_cat->loadObject(tmpid)) != 0) {
                                if ((node_id = label_ptr->DBACCESS_get_From_or_To()).id) {
                                    getTraverseByCategory_With_SIS_Server_Implementation(node_id, f_set, b_set, depth-1, isa, edge_set, retSysids, checked_set);
                                }
                                api_sys_cat->unloadObject(tmpid);
                            }
                        }
                    }
                }
            }
	 }
	 
	 // "b_set" contains the set of categories and all their subclasses. 
	 // This set is  not empty if the traverse direction is backward. 
	 if (b_set->set_get_card()) {
            tmpset.set_clear(); 
            
            // get all links that points to the object having sysid "objSysid". 
            if (getLinkTo(objSysid, &tmpset) == -1) {
                globalError.putMessage(" >getTraverseByCategory_With_SIS_Server_Implementation");
                return -1;
            }
        
            tmpset.set_pos(0);
        
            // traverse the set of links 
            while((tmpid = tmpset.set_get_id()).id) {
                class_set.set_clear();
        
                // get all the classes which "tmpid" is an immediate instance of 
                if (getClasses(tmpid, &class_set) != -1) {
                
                    // if the the intersection of "class_set" with "b_set" is not an empty set 
                    if (! (class_set.set_disjoint(b_set))) {
                        if (depth == 0) {
                            edge_set->set_put(objSysid);
                            break;
                        }
                        else {
                            retSysids->set_put(tmpid);   // add label into return set
                            if ((label_ptr = api_sys_cat->loadObject(tmpid)) != 0) {
        
                                // get the from-object of  "label_ptr" 
                                fromId = label_ptr->getFrom();
        
                                // if the link is an class attribute then we should get the instances of its from- object. 
                                if (label_ptr->IsClassAttr()) {
                                    // ATTTENTION ! 
                                    // In case of class attributes with from object a no instance category, 
                                    //the following line must be changed. 
                                    getAllInstances(fromId, &FromSet);
                                }
                                else {
                                    FromSet.set_putNeo4j_Id(fromId);
                                }
        
                                FromSet.set_pos(0);
				
                                // traverse the set of from-nodes 
                                while((node_id = FromSet.set_get_id()).id) {
				
                                    if (node_id.id) {
                                        getTraverseByCategory_With_SIS_Server_Implementation(node_id, f_set, b_set, depth-1, isa, edge_set, retSysids, checked_set);
                                    }
                                }
                                api_sys_cat->unloadObject(tmpid);
                            }
                        }
                    }
                }
            }
	 }

	 if ((isa == 1) || (isa == 3)) {
            ret = getAllSuperClasses(objSysid, &isaset);
	 }
	 if ((isa == 2) || (isa == 3)) {
            ret = getAllSubClasses(objSysid, &isaset);
	 }

	 if ((isa != -1) && (ret != -1)) {
            return getTraverseByCategory_With_SIS_Server_Implementation(&isaset, f_set, b_set, depth,isa, edge_set, retSysids, checked_set);
	 }
	 else {
            return (globalError.checkError(" >getTraverseByCategory_With_SIS_Server_Implementation"));
	 }
    }
        */
        // </editor-fold>
        
        //test of f_set.size() ==0 and b_set.size() ==0 has already been made 
        //in the api public finction and returns api fail
        //So we should never reach here with empty categories set
        
        //Get the starting ids as ArrayList<Long>
        ArrayList<Long> startingIdsCopy = startingIds.get_Neo4j_Ids();
        
        // filter out "checked_set" contents. "checked_set" keeps the sysids 
        // of the objects that are alredy checked. 
        startingIdsCopy.removeAll(checked_set);
        
        //check if we still have work to do or not
        if(startingIdsCopy.size()==0){
            return APISucc;
        }
        
        //if yes then get the corresponding Neo4j nodes so that they 
        //will be used in the neo4j API        
        ArrayList<Node> nodes = getNeo4jNodesByNeo4jIds(startingIdsCopy);
        
        
        //Structures that will be used to detect recursion may be needed for some isa up and down operations
        
        ArrayList<Long> newStartingIds = new ArrayList<Long>();
                
        // all thefollowing nodes will also be checked
        checked_set.addAll(startingIdsCopy);
        
        // <editor-fold defaultstate="collapsed" desc="Implementation1 Get requested links with traversal framework">
        ArrayList<Long> f_longSet = f_set.get_Neo4j_Ids();
        ArrayList<Long> b_longSet = b_set.get_Neo4j_Ids();

        final ArrayList<Long> finalForwardInstanceOfIds = new ArrayList<Long>(f_longSet);
        final ArrayList<Long> finalBackWardInstanceOfIds = new ArrayList<Long>(b_longSet);

        if(f_set.set_get_card()>0 || b_set.set_get_card()>0){                    

            TraversalDescription bothDirectionsTr = this.graphDb.traversalDescription()./*depthFirst().*/
                    uniqueness(Uniqueness.RELATIONSHIP_GLOBAL).
                    relationships(Configs.Rels.RELATION, Direction.BOTH).evaluator(new Evaluator() {
                        public Evaluation evaluate(Path path) {

                            if(path.length()==0){
                                if(isNodePrimitive(path.endNode())){
                                    return Evaluation.EXCLUDE_AND_PRUNE;
                                }
                                return Evaluation.INCLUDE_AND_CONTINUE;
                            }

                            if (path.endNode().hasLabel(Configs.Labels.Type_Attribute)) {

                                ArrayList<Long> classIds = getNodeClassesNeo4jIds(path.endNode());

                                if(finalForwardInstanceOfIds.size()>0 && path.lastRelationship().getEndNode().equals(path.endNode())){
                                     // check if it is included in forward categs
                                    ArrayList<Long> forwardCopy = new ArrayList<Long>(classIds);
                                    forwardCopy.retainAll(finalForwardInstanceOfIds);

                                    if(forwardCopy.size()>0){
                                        return Evaluation.INCLUDE_AND_CONTINUE;
                                    }

                                    return Evaluation.EXCLUDE_AND_PRUNE;
                                }
                                else if(finalBackWardInstanceOfIds.size()>0 && path.lastRelationship().getStartNode().equals(path.endNode())) {
                                    // check if it is included in backward categs

                                    classIds.retainAll(finalBackWardInstanceOfIds);

                                    if(classIds.size()>0){
                                        return Evaluation.INCLUDE_AND_CONTINUE;
                                    }

                                    return Evaluation.EXCLUDE_AND_PRUNE;
                                }

                                return Evaluation.EXCLUDE_AND_PRUNE;

                            } 
                            else {
                                if(isNodePrimitive(path.endNode())){
                                    return Evaluation.EXCLUDE_AND_PRUNE;
                                }
                                return Evaluation.EXCLUDE_AND_CONTINUE;

                            }
                        }
                    });


            //all paths end in attributes so we must get their from 
            //and to values in order to catch the to-node that stopped
            //edge_set
            ArrayList<Long> pathAttributeEndNodes = new ArrayList<Long>();

            for (Path path : bothDirectionsTr.traverse(nodes)) {

                //get end node edge nodes ??? 
                if(path.endNode().hasLabel(Configs.Labels.Type_Attribute)){
                    long nodeId = getNodeNeo4jId(path.endNode());

                    if(pathAttributeEndNodes.contains(nodeId)==false){
                        pathAttributeEndNodes.add(nodeId);
                    }                            
                }

                //iterate through nodes of path in order to find out which nodes to return
                for (Node node : path.nodes()) {

                    if (node.hasLabel(Configs.Labels.Type_Attribute)) {
                        long nodeId = getNodeNeo4jId(node);

                        if (nodeId>0 && path.length()>0 ) {
                            retSysids.set_putNeo4j_Id(nodeId);
                        }
                    }
                }//end of node iterator

                //check for new ids that should be checked for recursion
                for (Relationship rel : path.relationships()) {
                    long startNodeId = getNodeNeo4jId(rel.getStartNode());

                    long endNodeId = getNodeNeo4jId(rel.getEndNode());

                    boolean startNodeIsAttribute = rel.getStartNode().hasLabel(Configs.Labels.Type_Attribute);
                    boolean startNodeIsToken = rel.getStartNode().hasLabel(Configs.Labels.Token);
                    boolean endNodeIsAttribute = rel.getEndNode().hasLabel(Configs.Labels.Type_Attribute);
                    boolean endNodeIsToken = rel.getEndNode().hasLabel(Configs.Labels.Token);
                    boolean endNodeIsPrimitive = rel.getEndNode().hasLabel(Configs.Labels.PrimitiveClass);


                    if(startNodeIsAttribute){

                        if(finalForwardInstanceOfIds.size()>0 ){
                            ArrayList<Long> startNodeClassIds = getNodeClassesNeo4jIds(rel.getStartNode());
                            startNodeClassIds.retainAll(finalForwardInstanceOfIds);
                            if(startNodeClassIds.size()>0){
                                //vale to end node id sto set pou tha elengthei gia recursion
                                if(endNodeId>0 && checked_set.contains(endNodeId)==false && newStartingIds.contains(endNodeId)==false){
                                    if(endNodeIsToken==false && endNodeIsPrimitive==false){
                                        newStartingIds.add(endNodeId);   
                                    }                                            
                                }
                            }
                        }

                    }
                    else{
                        if(startNodeId>0 && checked_set.contains(startNodeId)==false && newStartingIds.contains(startNodeId)==false){

                            if(startNodeIsToken==false){
                                newStartingIds.add(startNodeId);
                            }   
                        }
                    }

                    if(endNodeIsAttribute){

                        if(finalBackWardInstanceOfIds.size()>0){
                            ArrayList<Long> endNodeClassIds = getNodeClassesNeo4jIds(rel.getEndNode());
                            endNodeClassIds.retainAll(finalBackWardInstanceOfIds);
                            if(endNodeClassIds.size()>0){

                                //vale to start node id sto set pou tha elengthei gia recursion
                                if(startNodeId>0 && checked_set.contains(startNodeId)==false && newStartingIds.contains(startNodeId)==false){

                                    if(startNodeIsToken==false){
                                        newStartingIds.add(startNodeId);
                                    }   
                                }
                            }
                        }
                    }
                    else{
                        if(endNodeId>0 && checked_set.contains(endNodeId)==false && newStartingIds.contains(endNodeId)==false){
                            if(endNodeIsToken==false && endNodeIsPrimitive ==false){
                                newStartingIds.add(endNodeId);   
                            }                                            
                        }
                    }                                
                }//end of relationship iterator

            }//end of paths iterator


            //pathAttributeEndNodes
            if(finalForwardInstanceOfIds.size()>0){
                ArrayList<Long> forward = new ArrayList<Long>();
                forward.addAll(pathAttributeEndNodes);
                forward.retainAll(finalForwardInstanceOfIds);
                if(forward.size()>0){
                    //get to values and test if included in newStartingIds
                    ArrayList<Long> retVals = new ArrayList<Long>();
                    if(DBACCESS_get_From_or_To_For_TraverseByCategory(forward, true,true, retVals)==APIFail){
                        return APIFail;
                    }
                    for(Long endVal : retVals){
                        if(endVal>0 && checked_set.contains(endVal)==false && newStartingIds.contains(endVal)==false){
                            newStartingIds.add(endVal);   
                        }
                    }
                }
            }

            if(finalBackWardInstanceOfIds.size()>0){
                ArrayList<Long> backward = new ArrayList<Long>();
                backward.addAll(pathAttributeEndNodes);
                backward.retainAll(finalBackWardInstanceOfIds);
                if(backward.size()>0){
                    //get from values and test if included in newStartingIds
                    ArrayList<Long> retVals = new ArrayList<Long>();
                    if(DBACCESS_get_From_or_To_For_TraverseByCategory(backward, true,true, retVals)==APIFail){
                        return APIFail;
                    }
                    for(Long fromVal : retVals){
                        if(fromVal>0 && checked_set.contains(fromVal)==false && newStartingIds.contains(fromVal)==false){
                            newStartingIds.add(fromVal);   
                        }
                    }
                }
            }                    
        }
        // </editor-fold>

        
        //must check new Starting ids have subclasses or superclasses if yes then recursion is needed
        if(newStartingIds.size()>0){
            if(Configs.boolDebugInfo){
                Collections.sort(newStartingIds);
                Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "Entering recursion for new Starting Ids: "+newStartingIds.size()+" nodes " + newStartingIds.toString());
            }
            PQI_Set newStartingIdsSet = new PQI_Set();
            for(Long val : newStartingIds){
                newStartingIdsSet.set_putNeo4j_Id(val);//.add(new PQI_SetRecord(val));                
                if(val<=2){
                    Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "ATTENTION !!! PRIMITIVE Class was given in traverse by category");
                }
            }
            
            //getTraverseByCategory(node_id, f_set, b_set, depth-1, isa, edge_set, retSysids, checked_set);
            //getTraverseByCategory(node_id, f_set, b_set, depth-1, isa, edge_set, retSysids, checked_set);
            //no return since we want to update the same structures before reaching the isa part of code
            if(newStartingIdsSet.set_get_card()>0){
                FOR_DELETE_getTraverseByCategory(newStartingIdsSet, f_set, b_set, depth-1, isa, edge_set, retSysids, checked_set);
            }
        }
        
        // <editor-fold defaultstate="collapsed" desc="Follow ISA if needed">
        if(isa != QClass.Traversal_Isa.NOISA){
            
            PQI_Set isaset = new PQI_Set();
            if(isa==QClass.Traversal_Isa.UP_DOWN){
                ArrayList<Node> isaStartNodes = getNeo4jNodesByNeo4jIds(startingIdsCopy);
                
                TraversalDescription updownisaTr = this.graphDb.traversalDescription().
                                    uniqueness(Uniqueness.RELATIONSHIP_GLOBAL).
                                    relationships(Configs.Rels.ISA, Direction.BOTH);
                                                                                    
                            for (Path path : updownisaTr.traverse(isaStartNodes)) {
                                for(Node n: path.nodes()){
                                    long nodeId = getNodeNeo4jId(n);
                                    if(nodeId>0 && checked_set.contains(nodeId)==false ){
                                        isaset.set_putNeo4j_Id(nodeId);//.add(new PQI_SetRecord(nodeId));
                                    }
                                }
                            }
                                            
            }
            else {

                
                int startIdsLoopIndex = 0;
                int maxIndex = startingIdsCopy.size();

                while (startIdsLoopIndex < maxIndex) {

                    ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(startIdsLoopIndex, Configs.MAX_IDS_PER_QUERY, startingIdsCopy);
                    startIdsLoopIndex += subSetofIds.size();
                    if(subSetofIds.size()==0){
                        break;
                    }
                    
                    String queryForIsaSet = "";
                    if(isa == QClass.Traversal_Isa.UPWARDS){
                        if(subSetofIds.size()==1){
                            queryForIsaSet = " MATCH(n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0))+"})-[:ISA*1..]->(m) "+
                                             " RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id +" as "+ Configs.Neo4j_Key_For_Neo4j_Id +" ";
                        }
                        else{
                            queryForIsaSet = " MATCH(n"+getCommonLabelStr()+")-[:ISA*1..]->(m) "+
                                             " WHERE n."+Configs.Neo4j_Key_For_Neo4j_Id +" IN " +subSetofIds.toString() + 
                                             " RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id +" as "+ Configs.Neo4j_Key_For_Neo4j_Id +" ";
                        }
                    }
                    else if (isa==QClass.Traversal_Isa.DOWNWARDS){

                        if(subSetofIds.size()==1){
                            queryForIsaSet = " MATCH(n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0))+"})<-[:ISA*1..]-(m) "+
                                             " RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id +" as "+ Configs.Neo4j_Key_For_Neo4j_Id +" ";
                        }
                        else{
                            queryForIsaSet = " MATCH(n"+getCommonLabelStr()+")<-[:ISA*1..]-(m) "+
                                             " WHERE n."+Configs.Neo4j_Key_For_Neo4j_Id +" IN " +subSetofIds.toString() + 
                                             " RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id +" as "+ Configs.Neo4j_Key_For_Neo4j_Id +" ";
                        }                        
                    }

                    if(queryForIsaSet.length()>0){

                        Result res = graphDb.execute(queryForIsaSet);
                        try{
                            while (res.hasNext()) {
                                long nodeId = getNeo4jIdFromObject(res.next().get(Configs.Neo4j_Key_For_Neo4j_Id));
                                
                                if(nodeId>0 && checked_set.contains(nodeId)==false){
                                    isaset.set_putNeo4j_Id(nodeId);//.add(new PQI_SetRecord(nodeId));
                                }
                            }
                        }
                        catch(Exception ex){
                            utils.handleException(ex);
                            return APIFail;
                        }
                        finally{
                            res.close();
                            res = null;
                        }
                    }
                }
            }
            
            
            if(isaset.set_get_card()>0){
                //return getTraverseByCategory(&isaset, f_set, b_set, depth,isa, edge_set, retSysids, checked_set);
                if(Configs.boolDebugInfo){
                    Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "ISA recursion needed for " + isaset.set_get_card()+ " nodes.");
                }                
                return FOR_DELETE_getTraverseByCategory(isaset, f_set, b_set, depth, isa, edge_set, retSysids, checked_set);
            }        
            
        }// if not no isa
        //</editor-fold>
        
        return APISucc;
    }
    
    //isNodeAttributeToAttribute
    int DBACCESS_getCountOfOutgoingRelations(Node n){
        
        Iterator<Relationship> r = n.getRelationships(Direction.OUTGOING, Configs.Rels.RELATION).iterator();
        int count = 0;
        while(r.hasNext()){
            r.next();
            count ++ ;
        }        
        return count ;
    }
    
    /**
     * NOT IMPLEMENTED 
     * 
     */
    int getMatchedString(ArrayList<Long> ids,String ptrn_str, QClass.MatchStringTypes mtch_type, PQI_Set retIds){
        /*
        int sis_api::getMatchedString(SET *obj_set, char* prtn_str, int mtch_type, SET *ret_set)
{
	prs_val prs;

	pattern_info pattrn;
	pattrn.ptrn = prtn_str;
	pattrn.ptrn_end = get_eos(pattrn.ptrn);
	pattrn.type = match_type(&(pattrn.ptrn), &(pattrn.ptrn_end));

	obj_set->set_pos(0); 
	if (obj_set->set_tuple_mode()) {
		//query_func7 func(&(sis_api::getMatchedString), &(sis_api::getMatchedString), this, prtn_str, mtch_type);
		query_func7 func(&sis_api::getMatchedString, &sis_api::getMatchedString, this, prtn_str, mtch_type);
		(((SET_TUPLE *)obj_set)->tuple_unary_query(&func));
	} else {
		// to be fast we first extract all pattern-strigns
		SYSID obj;
		int card, i;

		card = obj_set->set_get_card();
		for (i = 0; i < card; i++) {
			obj = obj_set->set_get_id();
			if (!obj.id) {
				if ((prs = obj_set->set_get_prs()).tag() != UNDEF_TAG)
					getMatchedString(prs, prtn_str, mtch_type, ret_set);
			} else
				getMatchedString(obj, prtn_str, mtch_type, ret_set);
		}
	}
	return (globalError.checkError(" >getMatchedString"));
}
        
        
          Returns 0 on success, -1 when an error occured.
int sis_api::getMatchedString(prs_val prs, char* prtn_str, int mtch_type, SET *ret_set)
{
	int ret;

	switch (mtch_type) {
	case STRING_MATCHED :
		pattern_info pattrn;
		pattrn.ptrn = prtn_str;
		pattrn.ptrn_end = get_eos(pattrn.ptrn);
		pattrn.type = match_type(&(pattrn.ptrn), &(pattrn.ptrn_end));

		if (string_match(prs, pattrn.ptrn, pattrn.ptrn_end,  pattrn.type))
			ret_set->set_put(&prs);
		break;
	case STRING_LESS_EQUAL :
		ret = strcmp(prs.sval(), prtn_str);
		if (ret <= 0) ret_set->set_put(&prs);
		break;
	case STRING_LESS_THAN :
		ret = strcmp(prs.sval(), prtn_str);
		if (ret < 0) ret_set->set_put(&prs);
		break;
	case STRING_EQUAL :
		ret = strcmp(prs.sval(), prtn_str);
		if (ret == 0) ret_set->set_put(&prs);
		break;
	case STRING_NOT_EQUAL :
		ret = strcmp(prs.sval(), prtn_str);
		if (ret != 0) ret_set->set_put(&prs);
		break;
	}
	return(globalError.checkError(" >getMatchedString"));
}		
        
        
        Returns 0 on success, -1 when an error occured.
int sis_api::getMatchedString(SYSID sysid, char* prtn_str, int mtch_type, SET *ret_set)
{
	int ret;
	Loginam *ln;

	switch (mtch_type) {
	case STRING_MATCHED :
		pattern_info pattrn;
		pattrn.ptrn = prtn_str;
		pattrn.ptrn_end = get_eos(pattrn.ptrn);
		pattrn.type = match_type(&(pattrn.ptrn), &(pattrn.ptrn_end));

		if (loginam_match(sysid, pattrn.ptrn, pattrn.ptrn_end, pattrn.type, CASE_SENSITIVE, 0))
			ret_set->set_put(sysid);
		break;
	case STRING_LESS_EQUAL :
		ln = api_translator->getLogicalNamePtr(sysid);
		ret = strcmp(ln->name(), prtn_str);
		if (ret <= 0) ret_set->set_put(sysid);
		break;
	case STRING_LESS_THAN :
		ln = api_translator->getLogicalNamePtr(sysid);
		ret = strcmp(ln->name(), prtn_str);
		if (ret < 0) ret_set->set_put(sysid);
		break;
	case STRING_EQUAL :
		ln = api_translator->getLogicalNamePtr(sysid);
		ret = strcmp(ln->name(), prtn_str);
		if (ret == 0) ret_set->set_put(sysid);
		break;
	case STRING_NOT_EQUAL :
		ln = api_translator->getLogicalNamePtr(sysid);
		ret = strcmp(ln->name(), prtn_str);
		if (ret != 0) ret_set->set_put(sysid);
		break;
	}
	return(globalError.checkError(" >getMatchedString"));
}		

        */
        return APISucc;
    }
    int getMatchedOnTransliteration(ArrayList<Long> ids, String searchVal, boolean exactMatch, PQI_Set retIds){
        //ArrayList<Pattern_info> pattrns = new ArrayList<Pattern_info>();
        //CHECK_setup_patterns(ptrn_set,pattrns);
        
        //no check if all ids exist??
        //HashMap<Long, String> logicalnames = new HashMap<Long,String>();
        
        int loopIndex = 0;
        int maxIndex = ids.size();

        if(maxIndex==0){
            return APISucc;
        }
        
        while (loopIndex < maxIndex) {

            //-30 added because query contains , n."+ Configs.Neo4j_Key_For_Logicalname +" as lname " 
            ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex, Configs.MAX_IDS_PER_QUERY-30, ids);
            loopIndex += subSetofIds.size();
            if(subSetofIds.isEmpty()){
                break;
            }
            String query ="";
            
            String criterion = "";
            if(exactMatch){
                criterion = " n."+Configs.Neo4j_Key_For_Transliteration +" = '" + searchVal+"' " ;
            }
            else{
                criterion = " n."+Configs.Neo4j_Key_For_Transliteration +" =~ '.*" + searchVal+".*' " ;
            }
            
            if(subSetofIds.size()==1){

                query = " MATCH (n"+getCommonLabelStr()+"{"+ prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0))+"}) " + 
                        " WHERE "+ criterion +
                        " RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id +" as id ";//, n."+ Configs.Neo4j_Key_For_Logicalname +" as lname ";

            }
            else {
                query = " MATCH (n"+getCommonLabelStr()+") "+
                        " WHERE n."+Configs.Neo4j_Key_For_Neo4j_Id +" IN " +subSetofIds.toString() + " AND " +
                             criterion  +
                        " RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id +" as id ";// , n."+ Configs.Neo4j_Key_For_Logicalname +" as lname ";

            }
            
            
            
            //do the job do not return
            Result res = graphDb.execute(query);
            try{
                while (res.hasNext()) {
                    Map<String,Object> row = res.next();
                    long id = getNeo4jIdFromObject(row.get("id"));
                    //String lname = (String) row.get("lname");
                 
                    retIds.set_putNeo4j_Id(id);
                    
                    /*if(logicalnames.containsKey(id)&& logicalnames.get(id).equals(lname)==false){
                        throw new UnsupportedOperationException(" In get matched. Have found more than one logical name for Neo4j_Id " + id + "\nlname 1: "+lname+"\nlname 2: "+logicalnames.get(id));
                    }
                    
                    logicalnames.put(id, lname);*/
                }
                //ATTENTION do not return here 
                
            }
            catch(Exception ex){
                utils.handleException(ex);
                return APIFail;
            }
            finally{
                res.close();
                res = null;
            }
        }
    
        return APISucc;
    }
    int getMatched(ArrayList<Long> ids,PQI_Set ptrn_set, PQI_Set retIds){
        
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //  Returns 0 on success, -1 when an error occured.
        int sis_api::getMatched(SYSID objSysid, SET* ptrn_set, SET *retSysids)
        {
                if (loginam_match(objSysid, ptrn_set, CASE_SENSITIVE, 0))
                        retSysids->set_put(objSysid);

                return (globalError.checkError(" >getMatched"));
        }

        //  Returns 0 on success, -1 when an error occurei.
        int sis_api::getMatched(SET *objSysids, SET* ptrn_set, SET *retSysids)
        {
                SYSID  obj_sysid;
                int ptrn_set_size = ptrn_set->set_get_card();
                pattern_info *pattrns = new pattern_info[ptrn_set_size];

                setup_patterns(ptrn_set, pattrns, &ptrn_set_size);

                // NO CODE FOR TUPLES ????? 

                objSysids->set_pos(0);
                FOR_EACH_ID_IN_SETPTR(obj_sysid, objSysids) {
                        int i = 0;
                        for(i = 0; i < ptrn_set_size; i++) {
                                if (loginam_match(obj_sysid, pattrns[i].ptrn,
                                        pattrns[i].ptrn_end,  pattrns[i].type, CASE_SENSITIVE, 0)) {
                                                retSysids->set_put(obj_sysid);
                                                break; // if match then break the "for loop ..."
                                                // thus we implement match(patt1 or patt2 ...)
                                }
                        }
                }

                delete pattrns;
                return (globalError.checkError(" >getMatched"));
        }
        */
        // </editor-fold> 
        
        
        //int ptrn_set_size = ptrn_set.set_get_card();
        ArrayList<Pattern_info> pattrns = new ArrayList<Pattern_info>();
        CHECK_setup_patterns(ptrn_set,pattrns);
        
        //no check if all ids exist??
        HashMap<Long, String> logicalnames = new HashMap<Long,String>();
        
        int loopIndex = 0;
        int maxIndex = ids.size();

        if(maxIndex==0){
            return APISucc;
        }
        
        while (loopIndex < maxIndex) {

            //-30 added because query contains , n."+ Configs.Neo4j_Key_For_Logicalname +" as lname " 
            ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex, Configs.MAX_IDS_PER_QUERY-30, ids);
            loopIndex += subSetofIds.size();
            if(subSetofIds.size()==0){
                break;
            }
            String query ="";
            
            if(subSetofIds.size()==1){
                
                query = " MATCH (n"+getCommonLabelStr()+"{"+ prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0))+"}) " + 
                        " RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id +" as id, n."+ Configs.Neo4j_Key_For_Logicalname +" as lname ";
						
            }
            else {
                query = " MATCH (n"+getCommonLabelStr()+") "+
                        " WHERE n."+Configs.Neo4j_Key_For_Neo4j_Id +" IN " +subSetofIds.toString() + 
                        " RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id +" as id, n."+ Configs.Neo4j_Key_For_Logicalname +" as lname ";
						
            }
            
            //do the job do not return
            Result res = graphDb.execute(query);
            try{
                while (res.hasNext()) {
                    Map<String,Object> row = res.next();
                    long id = getNeo4jIdFromObject(row.get("id"));
                    String lname = (String) row.get("lname");
                 
                    if(logicalnames.containsKey(id)&& logicalnames.get(id).equals(lname)==false){
                        throw new UnsupportedOperationException(" In get matched. Have found more than one logical name for Neo4j_Id " + id + "\nlname 1: "+lname+"\nlname 2: "+logicalnames.get(id));
                    }
                    
                    logicalnames.put(id, lname);
                }
                //ATTENTION do not return here 
                
            }
            catch(Exception ex){
                utils.handleException(ex);
                return APIFail;
            }
            finally{
                res.close();
                res = null;
            }
        }

        
        for(long id : logicalnames.keySet())
        {
            String lname = logicalnames.get(id);
            //skip unnammed attribute
            if(lname.matches(Configs.regExForUnNamed)){
                continue;
            }
            
            for(Pattern_info pattern: pattrns){
                Pattern_info.PatternType type = pattern.getType();
                String patStr = pattern.getPatternString();
                boolean matchFound = false;
                switch(type){
                    case ANY_SUB_STR:{
                        if(lname.contains(patStr)){
                            matchFound = true;
                        }
                        break;
                    }
                    case START_SUB_STR:{
                        if(utils.removePrefix(lname).startsWith(patStr)){
                            matchFound = true;
                        }
                        break;
                    }
                    case END_SUB_STR:{
                        if(lname.endsWith(patStr)){
                            matchFound = true;
                        }
                        break;
                    }
                    default:{
                        matchFound = false;
                    }
                }
                
                if(matchFound){                    
                    retIds.set_putNeo4j_Id(id);
                    break;//break the pattern for loop
                }                
            }//pattern for loop
        }//logical names retrieved loop
    
        return APISucc;
    }
    
    void CHECK_setup_patterns(PQI_Set ptrn_set, ArrayList<Pattern_info> pattrns){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /* creates an array for the patterns 
        pattern_info* sis_api::setup_patterns(SET* ptrn_set,  pattern_info* pattrns,  int *ptrn_set_size)
        {
                prs_val prs;

                // to be fast we first extract all pattern-strigns
                ptrn_set->set_pos(0);
                int i = 0;
                while ((prs = ptrn_set->set_get_prs()).tag() != UNDEF_TAG) {
                        if ((prs.tag() == STRING_TAG)) {
                                // only the ptr is needed, no strcpy
                                // because set::set_get_prs returns on the prs_val::s
                                // a pointer on the allocatated set::string which it
                                // will be freed at the deletion of the set
                                pattrns[i].ptrn = prs.sval();
                                pattrns[i].ptrn_end = get_eos(pattrns[i].ptrn);
                                pattrns[i].type = match_type(&(pattrns[i].ptrn), &(pattrns[i].ptrn_end));
                                i++;
                        } // else ingore prsval, go to next
                }
                *ptrn_set_size = i;	// save the actual number of STRING_TAG's
                return pattrns;
        }
        */
        // </editor-fold> 
        
        pattrns.clear();
        ArrayList<Return_Prm_Row> prsVals = new ArrayList<Return_Prm_Row>();
        ptrn_set.set_bulk_get_prs(prsVals);
        for(Return_Prm_Row row : prsVals){
            CMValue cmv = row.get_v1_cmv();
            if(cmv.type == CMValue.TYPE_STRING){
                pattrns.add(new Pattern_info(cmv.getString()));
            }// else ingore prsval, go to next
            
            
        }
    }
    
    

    
    // WARNING 1 !!! attribute to attribute support has not been tested
    // It is included in the traversal evaluator in case path.legth() ==0
    // Then it SHOULD be irrelevant if attribute to attibute is encoutered or 
    // not. PROBABLY we should act like any other attirubte
    // In the project DYAS where this ape came from attribute to attribute 
    // did not exist.     
    //
    // WARNING 2 !!! edge nodes supported but not tested as they are never used in
    // DYAS project - WebTMS
    int getTraverseByCategoryWithDepthControl(PQI_Set startingIds, PQI_Set f_set, PQI_Set b_set, int depth, QClass.Traversal_Isa isa, PQI_Set edge_set, PQI_Set retSysids, ArrayList<Long> checked_set){
        //ALMOST NEVER RETURNS API FAIL just in case of exception
        //TO DO: ret set should not be affected in case of api fail
        // <editor-fold defaultstate="collapsed" desc="C++ code.">
        /*
 //  SET retSysids contains all link objects traversed starting
 //  from object objSysid and traversing all links of category
 //  and direction defined in categs and dirs.
 //  Returns 0 on success, -1 when an error occured.
 
int sis_api::getTraverseByCategory(SYSID objSysid, SET *f_set, SET *b_set, int depth, int isa,SET *edge_set, SET *retSysids, SET *checked_set) {
	 
	telos_object *label_ptr;
	SET           isaset;
	SET           tmpset;
	SET           class_set;
	SET           FromSet;
	SYSID         tmpid, fromId, node_id;
	int           ret;
	
	if (checked_set->set_member_of(objSysid)) {
            return (globalError.checkError(" >getTraverseByCategory"));
	}
	
	// "checked_set" keeps the sysids of the objects in the hierarchy that are alredy checked. 
	 checked_set->set_put(objSysid);

	 // "f_set" contains the set of categories and all their subclasses. This set is not empty if the traverse direction is forward. 
	 if (f_set->set_get_card()) {
            // get all links that points from object with sysid "objSysid", including class attributes too 
            if (getLinkFrom(objSysid, &tmpset) == -1) {
                globalError.putMessage(" >getTraverseByCategory");
                return -1;
            }
        
            tmpset.set_pos(0);
            // traverse the set of links 
            while((tmpid = tmpset.set_get_id()).id) {
                class_set.set_clear();
                
                // get all the classes which "tmpid" is an  immediate instance of 
                if (getClasses(tmpid, &class_set) != -1) {
                    // if the the intersection of "class_set" with "f_set" is not an empty set 
                    if (! (class_set.set_disjoint(f_set))) {
                        if (depth == 0) {
                            edge_set->set_put(objSysid);
                            break;
                        }
                        else {
                            retSysids->set_put(tmpid);   // add label into return set
                            if ((label_ptr = api_sys_cat->loadObject(tmpid)) != 0) {
                                if ((node_id = label_ptr->DBACCESS_get_From_or_To()).id) {
                                    getTraverseByCategory(node_id, f_set, b_set, depth-1, isa, edge_set, retSysids, checked_set);
                                }
                                api_sys_cat->unloadObject(tmpid);
                            }
                        }
                    }
                }
            }
	 }
	 
	 // "b_set" contains the set of categories and all their subclasses. 
	 // This set is  not empty if the traverse direction is backward. 
	 if (b_set->set_get_card()) {
            tmpset.set_clear(); 
            
            // get all links that points to the object having sysid "objSysid". 
            if (getLinkTo(objSysid, &tmpset) == -1) {
                globalError.putMessage(" >getTraverseByCategory");
                return -1;
            }
        
            tmpset.set_pos(0);
        
            // traverse the set of links 
            while((tmpid = tmpset.set_get_id()).id) {
                class_set.set_clear();
        
                // get all the classes which "tmpid" is an immediate instance of 
                if (getClasses(tmpid, &class_set) != -1) {
                
                    // if the the intersection of "class_set" with "b_set" is not an empty set 
                    if (! (class_set.set_disjoint(b_set))) {
                        if (depth == 0) {
                            edge_set->set_put(objSysid);
                            break;
                        }
                        else {
                            retSysids->set_put(tmpid);   // add label into return set
                            if ((label_ptr = api_sys_cat->loadObject(tmpid)) != 0) {
        
                                // get the from-object of  "label_ptr" 
                                fromId = label_ptr->getFrom();
        
                                // if the link is an class attribute then we should get the instances of its from- object. 
                                if (label_ptr->IsClassAttr()) {
                                    // ATTTENTION ! 
                                    // In case of class attributes with from object a no instance category, 
                                    //the following line must be changed. 
                                    getAllInstances(fromId, &FromSet);
                                }
                                else {
                                    FromSet.set_putNeo4j_Id(fromId);
                                }
        
                                FromSet.set_pos(0);
				
                                // traverse the set of from-nodes 
                                while((node_id = FromSet.set_get_id()).id) {
				
                                    if (node_id.id) {
                                        getTraverseByCategory(node_id, f_set, b_set, depth-1, isa, edge_set, retSysids, checked_set);
                                    }
                                }
                                api_sys_cat->unloadObject(tmpid);
                            }
                        }
                    }
                }
            }
	 }

	 if ((isa == 1) || (isa == 3)) {
            ret = getAllSuperClasses(objSysid, &isaset);
	 }
	 if ((isa == 2) || (isa == 3)) {
            ret = getAllSubClasses(objSysid, &isaset);
	 }

	 if ((isa != -1) && (ret != -1)) {
            return getTraverseByCategory(&isaset, f_set, b_set, depth,isa, edge_set, retSysids, checked_set);
	 }
	 else {
            return (globalError.checkError(" >getTraverseByCategory"));
	 }
    }
        */
        // </editor-fold>
        
        //test of f_set.size() ==0 and b_set.size() ==0 has already been made 
        //in the api public finction and returns api fail
        //So we should never reach here with empty categories set
        
        //Get the starting ids as ArrayList<Long>
        ArrayList<Long> startingIdsCopy = startingIds.get_Neo4j_Ids();
        //Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "\ngetTraverseByCategoryWithDepthControl called " + startingIdsCopy.size() + " " +startingIdsCopy.toString()+"\n");
        // filter out "checked_set" contents. "checked_set" keeps the sysids 
        // of the objects that are alredy checked. 
        startingIdsCopy.removeAll(checked_set);
        
        //check if we still have work to do or not
        if(startingIdsCopy.size()==0){
            return APISucc;
        }
        
        // if yes then get the corresponding Neo4j nodes 
        // so that they will be used in the neo4j API        
        ArrayList<Node> nodes = getNeo4jNodesByNeo4jIds(startingIdsCopy);
        
        
        //Structure that will be used to detect if recursion is needed
        //according also to the isa selection
        ArrayList<Long> newStartingIds = new ArrayList<Long>();
        
        
        // all the following nodes will also be checked in this call
        checked_set.addAll(startingIdsCopy);
        
        //must multiply by 2 becase of in the traversal framework usage
        //the checking condition is different from the one in SIS-Server
        final int depthVal = 2*depth;

        // <editor-fold defaultstate="collapsed" desc="Implementation1 Get requested links with traversal framework">
        ArrayList<Long> f_longSet = f_set.get_Neo4j_Ids();
        ArrayList<Long> b_longSet = b_set.get_Neo4j_Ids();

        final ArrayList<Long> finalForwardInstanceOfIds = new ArrayList<Long>(f_longSet);
        final ArrayList<Long> finalBackWardInstanceOfIds = new ArrayList<Long>(b_longSet);

        if(f_set.set_get_card()>0 || b_set.set_get_card()>0){                

            TraversalDescription bothDirectionsTr = this.graphDb.traversalDescription().breadthFirst();
            if(depthVal<0){
                //bothDirectionsTr = bothDirectionsTr.uniqueness(Uniqueness.RELATIONSHIP_GLOBAL);
                bothDirectionsTr = bothDirectionsTr.uniqueness(Uniqueness.RELATIONSHIP_RECENT);
            }
            else{
                bothDirectionsTr = bothDirectionsTr.uniqueness(Uniqueness.NONE).evaluator(Evaluators.toDepth(depthVal));
            }

            bothDirectionsTr = bothDirectionsTr.relationships(Configs.Rels.RELATION, Direction.BOTH).evaluator(new Evaluator() {
                public Evaluation evaluate(Path path) {

                    Node endNode = path.endNode();

                    if(path.length()==0){
                        if(isNodePrimitive(endNode)){
                            return Evaluation.EXCLUDE_AND_PRUNE;
                        }

                        if(endNode.hasLabel(Configs.Labels.Type_Attribute)){
                            if(DBACCESS_getCountOfOutgoingRelations(endNode)>1){
                                return Evaluation.INCLUDE_AND_CONTINUE;
                            }
                            else{
                                return Evaluation.EXCLUDE_AND_PRUNE;
                            }
                        }

                        return Evaluation.INCLUDE_AND_CONTINUE;
                    }

                    if (endNode.hasLabel(Configs.Labels.Type_Attribute)) {

                        ArrayList<Long> classIds = getNodeClassesNeo4jIds(path.endNode());

                        if(finalForwardInstanceOfIds.size()>0 && path.lastRelationship().getEndNode().equals(endNode)){
                             // check if it is included in forward categs
                            ArrayList<Long> forwardCopy = new ArrayList<Long>(classIds);
                            forwardCopy.retainAll(finalForwardInstanceOfIds);

                            if(forwardCopy.size()>0){

                                return Evaluation.INCLUDE_AND_CONTINUE;
                            }

                            return Evaluation.EXCLUDE_AND_PRUNE;
                        }
                        else if(finalBackWardInstanceOfIds.size()>0 && path.lastRelationship().getStartNode().equals(endNode)) {
                            // check if it is included in backward categs

                            classIds.retainAll(finalBackWardInstanceOfIds);

                            if(classIds.size()>0){
                                return Evaluation.INCLUDE_AND_CONTINUE;
                            }

                            return Evaluation.EXCLUDE_AND_PRUNE;
                        }

                        return Evaluation.EXCLUDE_AND_PRUNE;

                    }
                    else {

                        if(isNodePrimitive(endNode)){
                            return Evaluation.EXCLUDE_AND_PRUNE;
                        }

                        return Evaluation.INCLUDE_AND_CONTINUE;

                    }
                }
            });


            //all paths end in attributes so we must get their from 
            //and to values in order to catch the to-node that stopped
            //edge_set
            //ArrayList<Long> pathAttributeEndNodes = new ArrayList<Long>();
            HashMap<Long, ArrayList<Long>> pathAttributeEndNodesAndClasses = new HashMap<Long, ArrayList<Long>>();


            for (Path path : bothDirectionsTr.traverse(nodes)) {


                //Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "Length: "+ path.length() +" DepthVal: "+depthVal +" " +path.toString());

                if(path.endNode().hasLabel(Configs.Labels.Type_Attribute)){
                    long nodeId = getNodeNeo4jId(path.endNode());

                    if(pathAttributeEndNodesAndClasses.containsKey(nodeId)==false){
                        ArrayList<Long> endNodeClasses = new ArrayList<Long>();
                        Iterator<Relationship> relIter = path.endNode().getRelationships(Direction.OUTGOING, Configs.Rels.INSTANCEOF).iterator();
                        while(relIter.hasNext()){
                            long classNodeId = getNodeNeo4jId(relIter.next().getEndNode());
                            if(endNodeClasses.contains(classNodeId)==false){
                                endNodeClasses.add(classNodeId);
                            }
                        }
                        
                        pathAttributeEndNodesAndClasses.put(nodeId, endNodeClasses);
                    }                            
                }

                //check for new ids that should be checked for recursion
                Node lastNode = path.startNode();
                Node stNode = path.startNode();
                Node endNode = null;


                boolean isFullDepthReached = (depthVal>=0 && (path.length()>=(depthVal)));


                for (Relationship rel : path.relationships()) {

                    boolean isForwardRel = true;                            

                    long startNodeId = -1;
                    long endNodeId = -1;
                    boolean startNodeIsAttribute = false;
                    boolean startNodeIsToken= false;
                    boolean endNodeIsAttribute= false;
                    boolean endNodeIsToken = false;
                    boolean endNodeIsPrimitive= false;

                    if(rel.getStartNode().equals(lastNode)){
                        isForwardRel = true;
                        lastNode = rel.getEndNode();

                        stNode = rel.getStartNode();
                        endNode = rel.getEndNode();

                        startNodeId = getNodeNeo4jId(stNode);
                        endNodeId = getNodeNeo4jId(endNode);
                        startNodeIsAttribute = stNode.hasLabel(Configs.Labels.Type_Attribute);
                        startNodeIsToken = stNode.hasLabel(Configs.Labels.Token);
                        endNodeIsAttribute = endNode.hasLabel(Configs.Labels.Type_Attribute);
                        endNodeIsToken = endNode.hasLabel(Configs.Labels.Token);
                        endNodeIsPrimitive = endNode.hasLabel(Configs.Labels.PrimitiveClass);

                    }
                    else{
                        isForwardRel = false;
                        lastNode = rel.getStartNode();

                        stNode = rel.getEndNode();
                        endNode = rel.getStartNode();

                        startNodeId = getNodeNeo4jId(stNode);
                        endNodeId = getNodeNeo4jId(endNode);                                
                        startNodeIsAttribute = stNode.hasLabel(Configs.Labels.Type_Attribute);
                        startNodeIsToken = stNode.hasLabel(Configs.Labels.Token);
                        endNodeIsAttribute = endNode.hasLabel(Configs.Labels.Type_Attribute);
                        endNodeIsToken = endNode.hasLabel(Configs.Labels.Token);
                        endNodeIsPrimitive = endNode.hasLabel(Configs.Labels.PrimitiveClass);                                
                    }


                    if(isForwardRel){
                        if(startNodeIsAttribute){
                            if(finalForwardInstanceOfIds.size()>0 ){
                                ArrayList<Long> startNodeClassIds = getNodeClassesNeo4jIds(stNode);
                                startNodeClassIds.retainAll(finalForwardInstanceOfIds);
                                if(startNodeClassIds.size()>0){

                                    //for forward links always include start node
                                    if (startNodeId>0 ) {
                                        retSysids.set_putNeo4j_Id(startNodeId);//.add(new PQI_SetRecord(startNodeId));
                                    }

                                    if(isFullDepthReached){
                                        //add end node to edge set
                                        if(endNodeId>0 ){
                                            edge_set.set_putNeo4j_Id(endNodeId);//.add(new PQI_SetRecord(endNodeId)); 
                                        }
                                    }
                                    else{

                                        //vale to end node id sto set pou tha elengthei gia recursion
                                        if(endNodeId>0 && checked_set.contains(endNodeId)==false && newStartingIds.contains(endNodeId)==false){
                                            if(endNodeIsToken==false && endNodeIsPrimitive==false){
                                                newStartingIds.add(endNodeId);   
                                            }                                            
                                        }
                                   }

                                }


                            }
                        }                             
                        else{

                            if(startNodeId>0 && checked_set.contains(startNodeId)==false && newStartingIds.contains(startNodeId)==false){

                                if(startNodeIsToken==false){
                                    newStartingIds.add(startNodeId);
                                }   
                            }
                        }
                    }
                    else{

                        if(endNodeIsAttribute){
                            if(finalBackWardInstanceOfIds.size()>0 ){
                                ArrayList<Long> endNodeClassIds = getNodeClassesNeo4jIds(endNode);
                                endNodeClassIds.retainAll(finalBackWardInstanceOfIds);
                                if(endNodeClassIds.size()>0){

                                    if(isFullDepthReached){
                                        //add start node to edge set
                                        if(startNodeId>0){
                                            edge_set.set_putNeo4j_Id(startNodeId);//.add(new PQI_SetRecord(startNodeId)); 
                                        }
                                    }
                                    else{

                                        //vale to start node id sto set pou tha elengthei gia recursion
                                        if(startNodeId>0 && checked_set.contains(startNodeId)==false && newStartingIds.contains(startNodeId)==false){

                                            if(startNodeIsToken==false){
                                                newStartingIds.add(startNodeId);
                                            }   
                                        }

                                        if (endNodeId>0 ) {

                                            retSysids.set_putNeo4j_Id(endNodeId);//.add(new PQI_SetRecord(endNodeId));                                                    
                                        }
                                    }

                                }


                            }
                        }                             
                        else{

                            if(endNodeId>0 && checked_set.contains(endNodeId)==false && newStartingIds.contains(endNodeId)==false){
                                if(endNodeIsToken==false && endNodeIsPrimitive ==false){
                                    newStartingIds.add(endNodeId);   
                                }                                            
                            }

                        }
                    }


                }//end of relationship iterator

            }//end of paths iterator


            //pathAttributeEndNodes
            if(finalForwardInstanceOfIds.size()>0){
                ArrayList<Long> checkForwardVals = new ArrayList<>();
                //Enumeration<Long> endNodesIter = pathAttributeEndNodesAndClasses.keys();
                //while(endNodesIter.hasMoreElements()){
                //long nodeId = endNodesIter.nextElement();
                pathAttributeEndNodesAndClasses.keySet().stream().forEach((nodeId) -> {
                    
                    ArrayList<Long> nodeClasses = pathAttributeEndNodesAndClasses.get(nodeId);
                    ArrayList<Long> forward = new ArrayList<>();
                    forward.addAll(nodeClasses);
                    forward.retainAll(finalForwardInstanceOfIds);
                    if (forward.size()>0) {
                        checkForwardVals.add(nodeId);
                        retSysids.set_putNeo4j_Id(nodeId);
                    }
                });
                
                if(checkForwardVals.size()>0){
                    //get to values and test if included in newStartingIds
                    ArrayList<Long> retVals = new ArrayList<Long>();
                    if(DBACCESS_get_From_or_To_For_TraverseByCategory(checkForwardVals, true,true, retVals)==APIFail){
                        return APIFail;
                    }
                    for(Long endVal : retVals){
                        if(endVal>0 && checked_set.contains(endVal)==false && newStartingIds.contains(endVal)==false){
                            newStartingIds.add(endVal);   
                        }
                    }
                }
            }

            if(finalBackWardInstanceOfIds.size()>0){
                ArrayList<Long> checkBackwardVals = new ArrayList<>();
                //Enumeration<Long> endNodesIter = pathAttributeEndNodesAndClasses.keys();
                //while(endNodesIter.hasMoreElements()){
                //long nodeId = endNodesIter.nextElement();
                pathAttributeEndNodesAndClasses.keySet().stream().forEach((nodeId) -> {
                    ArrayList<Long> nodeClasses = pathAttributeEndNodesAndClasses.get(nodeId);
                    ArrayList<Long> backward = new ArrayList<>();
                    backward.addAll(nodeClasses);
                    backward.retainAll(finalBackWardInstanceOfIds);
                    if (backward.size()>0) {
                        checkBackwardVals.add(nodeId);
                        retSysids.set_putNeo4j_Id(nodeId);
                    }
                });
                
                if(checkBackwardVals.size()>0){
                    //get from values and test if included in newStartingIds
                    ArrayList<Long> retVals = new ArrayList<>();
                    //shod again get the from value since this was a backwards link
                    if(DBACCESS_get_From_or_To_For_TraverseByCategory(checkBackwardVals, true,true, retVals)==APIFail){
                        return APIFail;
                    }
                    retVals.stream().filter((fromVal) -> (fromVal>0 && checked_set.contains(fromVal)==false && newStartingIds.contains(fromVal)==false)).forEach((fromVal) -> {
                        newStartingIds.add(fromVal);
                    });
                }
            }                    
        }
        // </editor-fold>

        
        //must check new Starting ids have subclasses or superclasses if yes then recursion is needed
        if(newStartingIds.size()>0){
            if(Configs.boolDebugInfo){
                Collections.sort(newStartingIds);
                Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "Entering recursion for new Starting Ids: {0} nodes {1}", new Object[]{newStartingIds.size(), newStartingIds.toString()});
            }
            PQI_Set newStartingIdsSet = new PQI_Set();
            newStartingIds.stream().map((val) -> {
                newStartingIdsSet.set_putNeo4j_Id(val);//.add(new PQI_SetRecord(val));                
                return val;
            }).filter((val) -> (val<=2)).forEach((_item) -> {
                Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "ATTENTION !!! PRIMITIVE Class was given in traverse by category");
            });
            //getTraverseByCategory(node_id, f_set, b_set, depth-1, isa, edge_set, retSysids, checked_set);
            //getTraverseByCategory(node_id, f_set, b_set, depth-1, isa, edge_set, retSysids, checked_set);
            //no return since we want to update the same structures before reaching the isa part of code
            if(newStartingIdsSet.set_get_card()>0){
                getTraverseByCategoryWithDepthControl(newStartingIdsSet, f_set, b_set, depth-1, isa, edge_set, retSysids, checked_set);
            }
        }
        
        // <editor-fold defaultstate="collapsed" desc="Follow ISA if needed">
        if(isa != QClass.Traversal_Isa.NOISA){
            
            PQI_Set isaset = new PQI_Set();
            if(isa==QClass.Traversal_Isa.UP_DOWN){
                ArrayList<Node> isaStartNodes = getNeo4jNodesByNeo4jIds(startingIdsCopy);
                
                TraversalDescription updownisaTr = this.graphDb.traversalDescription().
                                    uniqueness(Uniqueness.RELATIONSHIP_GLOBAL).
                                    relationships(Configs.Rels.ISA, Direction.BOTH);
                                                                                    
                            for (Path path : updownisaTr.traverse(isaStartNodes)) {
                                for(Node n: path.nodes()){
                                    long nodeId = getNodeNeo4jId(n);
                                    if(nodeId>0 && checked_set.contains(nodeId)==false ){
                                        isaset.set_putNeo4j_Id(nodeId);//.add(new PQI_SetRecord(nodeId));
                                    }
                                }
                            }
                                            
            }
            else {

                
                int startIdsLoopIndex = 0;
                int maxIndex = startingIdsCopy.size();

                while (startIdsLoopIndex < maxIndex) {

                    ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(startIdsLoopIndex, Configs.MAX_IDS_PER_QUERY, startingIdsCopy);
                    startIdsLoopIndex += subSetofIds.size();
                    if(subSetofIds.size()==0){
                        break;
                    }
                    
                    String queryForIsaSet = "";
                    if(isa == QClass.Traversal_Isa.UPWARDS){
                        if(subSetofIds.size()==1){
                            queryForIsaSet = " MATCH(n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0))+"})-[:ISA*1..]->(m) "+
                                             " RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id +" as "+ Configs.Neo4j_Key_For_Neo4j_Id +" ";
                        }
                        else{
                            queryForIsaSet = " MATCH(n"+getCommonLabelStr()+")-[:ISA*1..]->(m) "+
                                             " WHERE n."+Configs.Neo4j_Key_For_Neo4j_Id +" IN " +subSetofIds.toString() + 
                                             " RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id +" as "+ Configs.Neo4j_Key_For_Neo4j_Id +" ";
                        }
                    }
                    else if (isa==QClass.Traversal_Isa.DOWNWARDS){

                        if(subSetofIds.size()==1){
                            queryForIsaSet = " MATCH(n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0))+"})<-[:ISA*1..]-(m) "+
                                             " RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id +" as "+ Configs.Neo4j_Key_For_Neo4j_Id +" ";
                        }
                        else{
                            queryForIsaSet = " MATCH(n"+getCommonLabelStr()+")<-[:ISA*1..]-(m) "+
                                             " WHERE n."+Configs.Neo4j_Key_For_Neo4j_Id +" IN " +subSetofIds.toString() + 
                                             " RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id +" as "+ Configs.Neo4j_Key_For_Neo4j_Id +" ";
                        }                        
                    }

                    if(queryForIsaSet.length()>0){

                        Result res = graphDb.execute(queryForIsaSet);
                        try{
                            while (res.hasNext()) {
                                long nodeId = getNeo4jIdFromObject(res.next().get(Configs.Neo4j_Key_For_Neo4j_Id));
                                
                                if(nodeId>0 && checked_set.contains(nodeId)==false){
                                    isaset.set_putNeo4j_Id(nodeId);//new PQI_SetRecord(nodeId));
                                }
                            }
                        }
                        catch(Exception ex){
                            utils.handleException(ex);
                            return APIFail;
                        }
                        finally{
                            res.close();
                            res = null;
                        }
                    }
                }
            }
            
            
            if(isaset.set_get_card()>0){
                //return getTraverseByCategory(&isaset, f_set, b_set, depth,isa, edge_set, retSysids, checked_set);
                if(Configs.boolDebugInfo){
                    Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "ISA recursion needed for " + isaset.set_get_card() + " nodes.");
                }                
                return getTraverseByCategoryWithDepthControl(isaset, f_set, b_set, depth, isa, edge_set, retSysids, checked_set);
            }        
            
        }// if not no isa
        //</editor-fold>
        
        return APISucc;
    }
    
    int getTraverseByCategory_With_SIS_Server_Implementation(long objSysid, PQI_Set f_set, PQI_Set b_set, int depth, QClass.Traversal_Isa isa, PQI_Set edge_set, PQI_Set retSysids, PQI_Set  checked_set){
        // <editor-fold defaultstate="collapsed" desc="C++ code.">
        /*
 //  SET retSysids contains all link objects traversed starting
 //  from object objSysid and traversing all links of category
 //  and direction defined in categs and dirs.
 //  Returns 0 on success, -1 when an error occured.
 
int sis_api::getTraverseByCategory_With_SIS_Server_Implementation(SYSID objSysid, SET *f_set, SET *b_set, int depth, int isa,SET *edge_set, SET *retSysids, SET *checked_set) {
	 
	telos_object *label_ptr;
	SET           isaset;
	SET           tmpset;
	SET           class_set;
	SET           FromSet;
	SYSID         tmpid, fromId, node_id;
	int           ret;
	
	if (checked_set->set_member_of(objSysid)) {
            return (globalError.checkError(" >getTraverseByCategory_With_SIS_Server_Implementation"));
	}
	
	// "checked_set" keeps the sysids of the objects in the hierarchy that are alredy checked. 
	 checked_set->set_put(objSysid);

	 // "f_set" contains the set of categories and all their subclasses. This set is not empty if the traverse direction is forward. 
	 if (f_set->set_get_card()) {
            // get all links that points from object with sysid "objSysid", including class attributes too 
            if (getLinkFrom(objSysid, &tmpset) == -1) {
                globalError.putMessage(" >getTraverseByCategory_With_SIS_Server_Implementation");
                return -1;
            }
        
            tmpset.set_pos(0);
            // traverse the set of links 
            while((tmpid = tmpset.set_get_id()).id) {
                class_set.set_clear();
                
                // get all the classes which "tmpid" is an  immediate instance of 
                if (getClasses(tmpid, &class_set) != -1) {
                    // if the the intersection of "class_set" with "f_set" is not an empty set 
                    if (! (class_set.set_disjoint(f_set))) {
                        if (depth == 0) {
                            edge_set->set_put(objSysid);
                            break;
                        }
                        else {
                            retSysids->set_put(tmpid);   // add label into return set
                            if ((label_ptr = api_sys_cat->loadObject(tmpid)) != 0) {
                                if ((node_id = label_ptr->DBACCESS_get_From_or_To()).id) {
                                    getTraverseByCategory_With_SIS_Server_Implementation(node_id, f_set, b_set, depth-1, isa, edge_set, retSysids, checked_set);
                                }
                                api_sys_cat->unloadObject(tmpid);
                            }
                        }
                    }
                }
            }
	 }
	 
	 // "b_set" contains the set of categories and all their subclasses. 
	 // This set is  not empty if the traverse direction is backward. 
	 if (b_set->set_get_card()) {
            tmpset.set_clear(); 
            
            // get all links that points to the object having sysid "objSysid". 
            if (getLinkTo(objSysid, &tmpset) == -1) {
                globalError.putMessage(" >getTraverseByCategory_With_SIS_Server_Implementation");
                return -1;
            }
        
            tmpset.set_pos(0);
        
            // traverse the set of links 
            while((tmpid = tmpset.set_get_id()).id) {
                class_set.set_clear();
        
                // get all the classes which "tmpid" is an immediate instance of 
                if (getClasses(tmpid, &class_set) != -1) {
                
                    // if the the intersection of "class_set" with "b_set" is not an empty set 
                    if (! (class_set.set_disjoint(b_set))) {
                        if (depth == 0) {
                            edge_set->set_put(objSysid);
                            break;
                        }
                        else {
                            retSysids->set_put(tmpid);   // add label into return set
                            if ((label_ptr = api_sys_cat->loadObject(tmpid)) != 0) {
        
                                // get the from-object of  "label_ptr" 
                                fromId = label_ptr->getFrom();
        
                                // if the link is an class attribute then we should get the instances of its from- object. 
                                if (label_ptr->IsClassAttr()) {
                                    // ATTTENTION ! 
                                    // In case of class attributes with from object a no instance category, 
                                    //the following line must be changed. 
                                    getAllInstances(fromId, &FromSet);
                                }
                                else {
                                    FromSet.set_putNeo4j_Id(fromId);
                                }
        
                                FromSet.set_pos(0);
				
                                // traverse the set of from-nodes 
                                while((node_id = FromSet.set_get_id()).id) {
				
                                    if (node_id.id) {
                                        getTraverseByCategory_With_SIS_Server_Implementation(node_id, f_set, b_set, depth-1, isa, edge_set, retSysids, checked_set);
                                    }
                                }
                                api_sys_cat->unloadObject(tmpid);
                            }
                        }
                    }
                }
            }
	 }

	 if ((isa == 1) || (isa == 3)) {
            ret = getAllSuperClasses(objSysid, &isaset);
	 }
	 if ((isa == 2) || (isa == 3)) {
            ret = getAllSubClasses(objSysid, &isaset);
	 }

	 if ((isa != -1) && (ret != -1)) {
            return getTraverseByCategory_With_SIS_Server_Implementation(&isaset, f_set, b_set, depth,isa, edge_set, retSysids, checked_set);
	 }
	 else {
            return (globalError.checkError(" >getTraverseByCategory_With_SIS_Server_Implementation"));
	 }
    }
        */
        // </editor-fold>
        
        //local vars
        //telos_object *label_ptr;
	//SET           isaset;
	//SET           tmpset;
	//SET           class_set;
	//SET           FromSet;
	//SYSID         tmpid, fromId, node_id;
	//int           ret;
        Neo4j_Object label_ptr = null;
        long node_id;
        long fromId;
        PQI_Set tmpset = new PQI_Set();
        PQI_Set class_set = new PQI_Set();
        PQI_Set isaset = new PQI_Set();
        int ret=APIFail;
        
        if (checked_set.set_member_of(objSysid)==APISucc) {
            //return (globalError.checkError(" >getTraverseByCategory_With_SIS_Server_Implementation"));
            return APISucc;
        }
        
	// "checked_set" keeps the sysids of the objects in the 
        // hierarchy that are alredy checked. 
	checked_set.set_putNeo4j_Id(objSysid);
        
        
        // "f_set" contains the set of categories and all their subclasses. 
        // This set is not empty if the traverse direction is forward. 
	if (f_set.set_get_card()>0) {
            // get all links that points from object with sysid "objSysid", including class attri-butes too 
            if (getLinkFrom(objSysid, tmpset) == APIFail) {
                //globalError.putMessage(" >getTraverseByCategory_With_SIS_Server_Implementation");
                //return -1;
                return APIFail;
            }
            
            ArrayList<Long> tmpsetLongs = tmpset.get_Neo4j_Ids();
            // traverse the set of links 
            for(long tmpid : tmpsetLongs){
                class_set.set_clear();
                
                // get all the classes which "tmpid" is an  immediate instance of 
                if(getClasses(tmpid, class_set)!=APIFail){
                    // if the the intersection of "class_set" with "f_set" is not an empty set 
                    if(class_set.set_disjoint(f_set)==false){
                        if (depth == 0) {
                            edge_set.set_putNeo4j_Id(objSysid);
                            break;
                        }
                        else{
                            retSysids.set_putNeo4j_Id(tmpid);   // add label into return set
                            label_ptr = loadObject(tmpid);
                            if(label_ptr!=null){
                                node_id = label_ptr.getTo(this);
                                //if(node_id!=0){
                                if(node_id>0){
                                    DBaccess.this.getTraverseByCategory_With_SIS_Server_Implementation(node_id, f_set, b_set, depth-1, isa, edge_set, retSysids, checked_set);
                                }
                            }                           
                        }
                    }                    
                }
            }
         }
         
        // "b_set" contains the set of categories and all their subclasses. 
	// This set is  not empty if the traverse direction is backward. 
	if (b_set.set_get_card()>0) {
            tmpset.set_clear();
            
            // get all links that points to the object having sysid "objSysid". 
            if (getLinkTo(objSysid, tmpset) == -1) {
                //globalError.putMessage(" >getTraverseByCategory_With_SIS_Server_Implementation");
                return -1;
            }
            ArrayList<Long> tmpLong = tmpset.get_Neo4j_Ids();
            // traverse the set of links 
            for(long tmpid : tmpLong){
                class_set.set_clear();
                
                
                // get all the classes which "tmpid" is an immediate instance of 
                if(getClasses(tmpid, class_set)!=APIFail){
                    // if the the intersection of "class_set" with "b_set" is not an empty set 
                    if (! (class_set.set_disjoint(b_set))) {
                        if (depth == 0) {
                            edge_set.set_putNeo4j_Id(objSysid);
                            break;
                        }
                        else {
                            retSysids.set_putNeo4j_Id(tmpid);   // add label into return set
                            label_ptr = loadObject(tmpid);
                            if(label_ptr!=null){
                                fromId=label_ptr.getFrom(this);
                                
                                if(fromId>0){
                                    DBaccess.this.getTraverseByCategory_With_SIS_Server_Implementation(fromId, f_set, b_set, depth-1, isa, edge_set, retSysids, checked_set);
                                }
                            }
                        }
                    }
                }
            }
        }
        if(isa==QClass.Traversal_Isa.UPWARDS || isa==QClass.Traversal_Isa.UP_DOWN){
            ret = getAllSuper_or_AllSubClasses(objSysid, isaset,true);
        }
        if(isa==QClass.Traversal_Isa.DOWNWARDS || isa == QClass.Traversal_Isa.UP_DOWN){
            ret = getAllSuper_or_AllSubClasses(objSysid, isaset,false);
        }
        
        if(isa!=QClass.Traversal_Isa.NOISA && ret !=QClass.APIFail){
            return getTraverseByCategory_With_SIS_Server_Implementation(isaset, f_set, b_set, depth,isa, edge_set, retSysids, checked_set);
        }
        else{
            //return (globalError.checkError(" >getTraverseByCategory_With_SIS_Server_Implementation"));
            return APISucc;
        }
        
    }
    
    long getTo(long objSysid, PQI_Set retSysids) {

        //PENDING CASE have not found yet an example
        // ATTENTION!!!
        // there is also a case that is not included here 
        // get links defined in the classes of objSysid 

        Neo4j_Object neoObj = loadObject(objSysid);
        if (neoObj ==null) {
            return APIFail;
        }

        return neoObj.getTo(this);
        
        //neoObj.getLinkBy(this,retSysids);

    }
    
    long getFrom(long objSysid, PQI_Set retSysids) {

        //PENDING CASE have not found yet an example
        // ATTENTION!!!
        // there is also a case that is not included here 
        // get links defined in the classes of objSysid 
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
    
        /*
        telos_object *obj_ptr;
	 SYSID         tmpid;

    if ((obj_ptr = api_sys_cat->loadObject(objSysid)) == NULL) {
		globalError.putMessage(" >getFrom");
      return -1;
    }

    if (O_IsLink(obj_ptr->o_type)) {
      tmpid = obj_ptr->getFrom();
		api_sys_cat->unloadObject(objSysid);
      retSysids->set_put(tmpid);
    }

    return (globalError.checkError(" >getFrom"));
        */
// </editor-fold> 
        
        Neo4j_Object neoObj = loadObject(objSysid);
        if (neoObj ==null) {
            return APIFail;
        }

        return neoObj.getFrom(this);
    }
    
    
    
     
    
    int DBACCESS_getLinkToSET(PQI_Set setIDs, PQI_Set retSysids){
        
        ArrayList<Long> ids = new ArrayList<Long>();
        ids = setIDs.get_Neo4j_Ids();
        
        if(DebugInfo == true)
        {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** DBACCESS_getLinkToSET input IDs: "+setIDs.get_Neo4j_Ids());
        }
        
        String query = "";

        ArrayList<Long> tmpvec = new ArrayList<Long>();
                
        int loopIndex = 0;
        int maxIndex = ids.size();

        if(maxIndex==0){
            return APISucc;
        }
        
        while (loopIndex < maxIndex) {

            ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex, Configs.MAX_IDS_PER_QUERY, ids);
            loopIndex += subSetofIds.size();
            if(subSetofIds.size()==0){
                break;
            }
            query ="";        
            if(subSetofIds.size()==1){
               query = "MATCH (n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0))+"})<-[:RELATION]-(m:"+Configs.Neo4j_Key_For_Type_AttributeStr+")"
                        + "RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";
            }
            else {                
                query =" MATCH (n"+getCommonLabelStr()+")<-[:RELATION]-(m:"+Configs.Neo4j_Key_For_Type_AttributeStr+")"
                       +" WHERE n."+Configs.Neo4j_Key_For_Neo4j_Id +" in " +subSetofIds.toString() 
                       +" RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";
            }
            
            //do the job do not return
            Result res = graphDb.execute(query);
                try {
                    while (res.hasNext()) {
                        Map<String, Object> row = res.next();
                        long val = getNeo4jIdFromObject(row.get(Configs.Neo4j_Key_For_Neo4j_Id));
						              
                        if (val > 0) {
                            tmpvec.add(val);
                        }
                        else {                   
                            if(Configs.boolDebugInfo){                                
                                throw new UnsupportedOperationException();                            
                            }                        
                        }
                    }
                  
                } catch (Exception ex) {
                    utils.handleException(ex);
                    return APIFail;
                } finally {
                    res.close();
                    res = null;
                }
              }
            
        
        for(Long l:tmpvec)
        {
            retSysids.set_putNeo4j_Id(l);
        }
        
        if(DebugInfo == true)
        {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** DBACCESS_getLinkToSET retSysids size: "+retSysids.get_Neo4j_Ids().size());
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** DBACCESS_getLinkToSET retSysids: "+retSysids.get_Neo4j_Ids());
        }
            
        return APISucc;
    }
    
    int DBACCESS_getLinkTo(long id, PQI_Set retSysids){
        
        String query = " MATCH (n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(id)+"})<-[:RELATION]-(m:"+Configs.Neo4j_Key_For_Type_AttributeStr+")"
                       + " RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";

        
        Result res = this.graphDb.execute(query);

        try {
            while (res.hasNext()) {
                Map<String, Object> row = res.next();
                
                long val = getNeo4jIdFromObject(row.get(Configs.Neo4j_Key_For_Neo4j_Id));
                
                if (val > 0) {
                    retSysids.set_putNeo4j_Id(val);
                }
                else {                   
                    if(Configs.boolDebugInfo){                                
                        throw new UnsupportedOperationException();                            
                    }                        
                }

            }
            return APISucc;
        } catch (Exception ex) {
            utils.handleException(ex);
            return APIFail;
        } finally {
            res.close();
            res = null;
        }
    }
    
    int getLinkTo(long objSysid, PQI_Set retSysids) {

        //PENDING CASE have not found yet an example
        // ATTENTION!!!
        // there is also a case that is not included here 
        // get links defined in the classes of objSysid 

        Neo4j_Object neoObj = loadObject(objSysid);
        if (neoObj ==null) {
            return APIFail;
        }
        
        return neoObj.getLinkBy(this, retSysids);
    }
    
    int getLinkToSET(PQI_Set setIDs, PQI_Set retSysids) {
        
        if(CheckAllExist(setIDs) == APIFail){
             return APIFail;
         }
         
         return DBACCESS_getLinkToSET(setIDs, retSysids);
    }
        
    Neo4j_Object loadObject(long objSysid) {

        //Neo4j_Object returnObj = null;
        
        String query = " MATCH (n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(objSysid)+"}) " +
                        " UNWIND labels(n) as labels " +
                        " RETURN labels ";
                

        ArrayList<String> labels = new ArrayList<String>();
        
        Result res = this.graphDb.execute(query);
        try {
            while (res.hasNext()) {

                Map<String, Object> row = res.next();
                String val = (String) row.get("labels");
                labels.add(val);
            }
        } finally {
            res.close();
            res = null;
        }
        
        if(labels.size()>0){
            labels.retainAll(Configs.systemLabels);
            
            Neo4j_Object.Levels level = Neo4j_Object.Levels.NOT_DEFINED;
            Neo4j_Object.Types type = Neo4j_Object.Types.NOT_DEFINED;
            if(labels.contains(Neo4j_isRelationship)){
                //abandoned case
                boolean isNodeNotRelationship = false;
                
                type = Neo4j_Object.Types.Type_Attribute;
                level = Neo4j_Object.Levels.Token;                
                
                //return new Neo4j_Object(objSysid, /*isNodeNotRelationship,*/ type, level);
                return new Neo4j_Object_link_node(objSysid,/* isNodeNotRelationship,*/ type, level);
            }
            else{
                //boolean isNodeNotRelationship = true;
                boolean isPrimitiveClass = labels.contains(Configs.Neo4j_Key_For_PrimitiveClass_Str);
                
                if(labels.contains(Configs.Neo4j_Key_For_Type_AttributeStr)){
                    type = Neo4j_Object.Types.Type_Attribute;
                }
                else if(labels.contains(Configs.Neo4j_Key_For_Type_IndividualStr)){
                    type = Neo4j_Object.Types.Type_Individual;
                }
                
                if(labels.contains(Configs.Neo4j_Level_Token)){
                    level = Neo4j_Object.Levels.Token;
                }
                else if(labels.contains(Configs.Neo4j_Level_S_Class)){
                    level = Neo4j_Object.Levels.S_Class;
                }
                else if(labels.contains(Configs.Neo4j_Level_M1_Class)){
                    level = Neo4j_Object.Levels.M1_Class;
                }
                else if(labels.contains(Configs.Neo4j_Level_M2_Class)){
                    level = Neo4j_Object.Levels.M2_Class;
                }
                else if(labels.contains(Configs.Neo4j_Level_M3_Class)){
                    level = Neo4j_Object.Levels.M3_Class;
                }
                else if(labels.contains(Configs.Neo4j_Level_M4_Class)){
                    level = Neo4j_Object.Levels.M4_Class;
                }
                if(isPrimitiveClass){
                    Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "ATTENTION!!! Returning Neo4j_Object_sys_class Object");
                    return new Neo4j_Object_sys_class(objSysid, /*isNodeNotRelationship,*/ type, level);
                }
                if(level == Neo4j_Object.Levels.Token){
                    if(type==Neo4j_Object.Types.Type_Individual){
                        return new Neo4j_Object_token_node(objSysid, /*isNodeNotRelationship,*/ type, level);
                    }
                    if(type==Neo4j_Object.Types.Type_Attribute){
                        return new Neo4j_Object_link_node(objSysid,/* isNodeNotRelationship,*/ type, level);
                    }                    
                    
                }
                else{
                    if(type==Neo4j_Object.Types.Type_Individual){
                        return new Neo4j_Object_class_node(objSysid, /*isNodeNotRelationship,*/ type, level);
                    }
                    if(type==Neo4j_Object.Types.Type_Attribute){
                        return new Neo4j_Object_link_class(objSysid, /*isNodeNotRelationship,*/ type, level);
                    }
                }
                
                //return null;
                Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "ATTENTION!!! Returning Neo4j_Object_sys_class Object");
                return new Neo4j_Object_sys_class(objSysid, /*isNodeNotRelationship,*/ type, level);
            }
            
        }

        return null;
    }
    
    int getSuper_or_SubClasses(long objSysid, PQI_Set retSysids, boolean getSuperInsteadOfSubclasses) {
        
        Neo4j_Object neoObj = loadObject(objSysid);
        if (neoObj ==null) {
            return APIFail;
        }
        
        if(getSuperInsteadOfSubclasses==false){
            //if neoObj is of level Token then it never has no subclasses
            if(neoObj.isLevelToken()){
                return QClass.APISucc;
            }
        }
        
        String query = " MATCH(n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(objSysid)+"})"
                        +(getSuperInsteadOfSubclasses?"-":"<-")+"[:ISA]"+(getSuperInsteadOfSubclasses?"->":"-")+"(m) "+
                        " RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";
                
        Result res = this.graphDb.execute(query);

        try {
            while (res.hasNext()) {

                Map<String, Object> row = res.next();
                long val = getNeo4jIdFromObject(row.get(Configs.Neo4j_Key_For_Neo4j_Id));

                if (val > 0) {
                    retSysids.set_putNeo4j_Id(val);
                } else {
                    return APIFail;
                }
            }
            return APISucc;
        } catch (Exception ex) {
            utils.handleException(ex);
            return APIFail;
        } finally {
            res.close();
            res = null;
        }
            
    }
    
    
    
    
    
    
    int getAllSuper_or_AllSubClasses(long startingId, PQI_Set returnSet, boolean getSuperInsteadOfSubclasses){
        Neo4j_Object neoObj = loadObject(startingId);
        if (neoObj ==null) {
            
            return APIFail;
        }

        if(getSuperInsteadOfSubclasses==false){
            //if neoObj is of level Token then it never has no subclasses
            if(neoObj.isLevelToken()){
                return QClass.APISucc;
            }
        }
        
        String query = " MATCH (m" + getCommonLabelStr() + "{"+prepareNeo4jIdPropertyFilterForCypher(startingId)+"})"
                        +(getSuperInsteadOfSubclasses?"-":"<-")+"[:ISA*1..]"+(getSuperInsteadOfSubclasses?"->":"-")+"(n) "+ 
                        " RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";
                
        Result res = this.graphDb.execute(query);
        try {
            while (res.hasNext()) {

                Map<String, Object> row = res.next();
                long val = getNeo4jIdFromObject(row.get(Configs.Neo4j_Key_For_Neo4j_Id));
				                 
                if (val > 0) {
                    returnSet.set_putNeo4j_Id(val);//add(new PQI_SetRecord(val));                    
                } 
                else {                   
                    if(Configs.boolDebugInfo){                                
                        throw new UnsupportedOperationException();                            
                    }                        
                }
            }
            return QClass.APISucc;
        } catch (Exception ex) {
            utils.handleException(ex);
            return APIFail;
        } finally {
            res.close();
            res = null;
        }
        
        //return APISucc;
    }
    
    
    long getSysid_For_Set_Categories(String from, String category){
        
        String query = " MATCH (m" + getCommonLabelStr() + "{"+prepareLogicalNameForCypher(from)+"})"+
                            "-[:RELATION]->"+
                            "(n" + getCommonLabelStr() + "{"+prepareLogicalNameForCypher(category) + "}) "
                            + " RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" ";
                
        
        Result res = this.graphDb.execute(query);
        long retVal =APIFail;
        try {            
            while (res.hasNext()) {

                Map<String, Object> row = res.next(); 
                
                long tempval = getNeo4jIdFromObject(row.get(Configs.Neo4j_Key_For_Neo4j_Id));
                
                if (tempval >= 0) {  
                    if(retVal==APIFail){
                        retVal = tempval;                        
                    }
                    else{
                        //SET ERROR that two ids were found
                        return APIFail;
                    }
                    
                } else {
                    return QClass.APIFail;
                }
            }
            
            return retVal;
            
        } catch (Exception ex) {
            utils.handleException(ex);
            return QClass.APIFail;
        } finally {
            res.close();
            res = null;
        }
        
        //return APIFail;
    }
    
    long setCurrentNode(ArrayList<Long> CurrentNode_Ids_Stack, String currentNodeName){
        return setCurrentNode(CurrentNode_Ids_Stack,currentNodeName,null);
    }
    
    long setCurrentNode(ArrayList<Long> CurrentNode_Ids_Stack, String currentNodeName, CMValue retCMValue){
        String query = "";
        
        boolean skipRetCMValue = true;
        if(retCMValue!=null){
            skipRetCMValue = false;
        }

        if (CurrentNode_Ids_Stack.isEmpty()) {

            query = " MATCH (n" + getCommonLabelStr() + "{"+prepareLogicalNameForCypher(currentNodeName) + "}) "+
                    "WHERE NOT( \"" + Configs.Neo4j_Key_For_Type_AttributeStr+"\"  IN labels(n) ) "+
                    " RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" " + 
                    (skipRetCMValue?"":", n."+Configs.Neo4j_Key_For_Logicalname +" as lname, n."+Configs.Neo4j_Key_For_Transliteration +" as translit, n." +Configs.Neo4j_Key_For_ThesaurusReferenceId +" as thesRefId" ) ;
        } else {

            long currentNodeId = CurrentNode_Ids_Stack.get(CurrentNode_Ids_Stack.size()-1);

            query = " MATCH (m" + getCommonLabelStr() + "{"+prepareNeo4jIdPropertyFilterForCypher(currentNodeId)+"})"+
                    "-[:RELATION]->"+
                    "(n" + getCommonLabelStr() + "{"+prepareLogicalNameForCypher(currentNodeName) + "}) " + 
                    " RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+" "+ 
                    (skipRetCMValue ?"":", n."+Configs.Neo4j_Key_For_Logicalname +" as lname, n."+Configs.Neo4j_Key_For_Transliteration +" as translit, n." +Configs.Neo4j_Key_For_ThesaurusReferenceId +" as thesRefId" ) ;
        }

        Result res = this.graphDb.execute(query);
        try {
            long retVal =APIFail;
            while (res.hasNext()) {

                Map<String, Object> row = res.next(); 
                
                long tempval = getNeo4jIdFromObject(row.get(Configs.Neo4j_Key_For_Neo4j_Id));
                
                if (tempval >= 0) {  
                    if(retVal==APIFail){
                        retVal = tempval;
                        CurrentNode_Ids_Stack.add(retVal); 
                        
                        if(!skipRetCMValue){
                            String logName = (String) row.get("lname");
                            String transliteration = (String) row.get("translit");
                            
                            long refId = getThesaurusReferenceIdFromObject(row.get("thesRefId"));                            
                            retCMValue.assign_node(logName, retVal, transliteration, refId);
                        }
                    }
                    else{
                        //SET ERROR that two ids were found
                        return APIFail;
                    }                    
                } else {
                    return QClass.APIFail;
                }
            }
            return retVal;
        } catch (Exception ex) {
            utils.handleException(ex);
            return QClass.APIFail;
        } finally {
            res.close();
            res = null;
        }
    }
    
    long setCurrentNodeById(ArrayList<Long> CurrentNode_Ids_Stack, long currentNodeId){
        
        String query = " MATCH (n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(currentNodeId)+"}),(n:"+Configs.Neo4j_Key_For_Type_IndividualStr+") "+
                        " RETURN n."+Configs.Neo4j_Key_For_Neo4j_Id+" as fromId, n."+Configs.Neo4j_Key_For_Neo4j_Id+" as id" +
                        " UNION " +
                        " MATCH (m"+getCommonLabelStr()+")-[:RELATION]-> (n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(currentNodeId)+"}),(n:"+Configs.Neo4j_Key_For_Type_AttributeStr+") "+
                        " RETURN m."+Configs.Neo4j_Key_For_Neo4j_Id+" as fromId, n."+Configs.Neo4j_Key_For_Neo4j_Id+" as id" ;
                
                

        Result res = this.graphDb.execute(query);
        long fromIdVal = APIFail;
        long newIdVal = APIFail;
        try {
            
            while (res.hasNext()) {

                Map<String, Object> row = res.next(); 
                
                //Thinking about attribute to attribute case but probably 
                //no more than one rows will ever be returned
                if(fromIdVal>0 || newIdVal>0){
                    return APIFail;
                }
                fromIdVal = getNeo4jIdFromObject(row.get("fromId"));
                newIdVal = getNeo4jIdFromObject(row.get("id"));
                
            }
            
        } catch (Exception ex) {
            utils.handleException(ex);
            return QClass.APIFail;
        } finally {
            res.close();
            res = null;
        }
        
        
        if(fromIdVal >0 && newIdVal>0){
            
            CurrentNode_Ids_Stack.clear();
            if(fromIdVal!= newIdVal){
                CurrentNode_Ids_Stack.add(fromIdVal); 
            }
            CurrentNode_Ids_Stack.add(newIdVal); 

            return newIdVal;
        }
        else{
            return APIFail;
        }
    }
	
    boolean isLabelUsed(String labelName){
        
        String query = "MATCH(n:" + labelName + ") Return count(n) as IsDefinedAndUsed LIMIT 1";
        Result res = graphDb.execute(query);

        try{
            while (res.hasNext()) {

                Map<String, Object> row = res.next();
                long cnt = (Long) row.get("IsDefinedAndUsed");
                if (cnt > 0) {
                    return true;
                } else {
                    return false;
                }
            }            
        }
        catch(Exception ex){
            utils.handleException(ex);
        }
        finally{
            res.close();
            res=null;
        }
        
        return false;

    }
    
    boolean isCommonLabelUsed(){
        return this.useCommonLabel;
    }
    
    void setCommonLabelUsed(boolean value){
        this.useCommonLabel = value;
    }
    
    ArrayList<Long> TEST_get_SysIds_OfNeo4jId_Set(ArrayList<Long> set){
        ArrayList<Long> returnVal = new ArrayList<Long>();
                
        int loopIndex =0;
        int maxIndex = set.size();
        while(loopIndex<maxIndex){
            ArrayList<Long> subSetIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex, Configs.MAX_IDS_PER_QUERY, set);
            loopIndex += subSetIds.size();

            if(subSetIds.size()==0){
                break;
            }

            String query = "";
            if(subSetIds.size()==1){
                query = " Match (n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(subSetIds.get(0))+"}) " +
                " RETURN n."+Neo4j_Key_For_SysId+" as p ";
            }
            else{
                query = " Match (n"+getCommonLabelStr()+") WHERE n."+Configs.Neo4j_Key_For_Neo4j_Id+" IN " + subSetIds.toString() + " "+
                " RETURN n."+Neo4j_Key_For_SysId+" as p ";
            }

            Result res = this.graphDb.execute(query);
            try {
                while (res.hasNext()) {

                    Map<String, Object> row = res.next();
                    if (row != null && row.containsKey("p") && row.get("p") != null) {
                        long val =-1;
                        if(Configs.CastSysIdAsInt){
                            val = (int) row.get("p");
                        }
                        else{
                            val = (long) row.get("p");
                        }

                        if (val > 0 && returnVal.contains(val) == false) {
                            returnVal.add(val);
                        }
                    }
                }
            } 
            catch(Exception ex){
                utils.handleException(ex);
                return null;
            }
            finally {
                res.close();
                res = null;
            }

        }
        
        return returnVal;
    }
    
    int FOR_DELETE_traverse_by_category(int set_id, QClass.Traversal_Isa whatMethod) {

        //<editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        

         // START OF: sis_api::get_traverse_by_category_With_SIS_Server_Implementation 

         //
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
         // isa defines whether for each node query will visit any of
         //  its superclasses or subclasses. If isa is NOISA then none of
         //  them is visited. If isa is UPWARDS then all superclasses of
         //  each node are visited, if isa is DOWNWARDS then all subclasses
         //  of each node are visited and if isa is UP_DOWN  all superclasses
         //  and all subclasses of each node are visited.
         //  Returns the set identifier where the answer is stored and
         //  ErrorCode when an error occured.
         //
         int sis_api::get_traverse_by_category_With_SIS_Server_Implementation(int set_id, int isa)
         #ifdef CLIENT_SERVER
         {
         int ret; 

         CHECK_FILES("get_traverse_by_category_With_SIS_Server_Implementation");
         int result; 
         result = RFCache->InvalidateCache(); 
         if(result < 0) return result;  // Comms error 
         if((result=SComm->send_int(  _GET_TRAVERSE_BY_CATEGORY))>0) 
         if((result=SComm->send_int(  set_id))>0) 
         if((result=SComm->send_int(  isa))>0) 
         if((result=SComm->recv_int(  &ret))>0) 
         return ret; 
         return result; 
         }
         #else
         {
         int  new_set_id,
         ret;
         SET *writeset;
         SET *setptr;
         SET  checked_set;

         CHECK_FILES("get_traverse_by_category_With_SIS_Server_Implementation");

         if (num_of_categories == 0) {
         fprintf(stderr,"*** ERROR *** qp::get_traverse_by_category_With_SIS_Server_Implementation : ");
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
         if (no_current_node("get_traverse_by_category_With_SIS_Server_Implementation")) {
         tmp_sets.free_set(new_set_id);
         return -1;       // there is no current node set
         }
         edge_set.set_clear(); // Clear private member edge_set for re-calculation
         ret = getTraverseByCategory_With_SIS_Server_Implementation(*current_node, &forward_set, &backward_set,
         depth, isa, &edge_set, writeset, &checked_set);
         }
         else {
         if ((setptr = tmp_sets.return_set(set_id)) == NULL) {
         return -1;       // set doesn't exist...
         }
         edge_set.set_clear(); // Clear private member edge_set for re-calculation
         ret = getTraverseByCategory_With_SIS_Server_Implementation(setptr, &forward_set, &backward_set,
         depth, isa, &edge_set, writeset, &checked_set);
         }

         ON_ERROR_RETURN(ret,new_set_id);  // free new_set_id and return -1

         return new_set_id;
         }
         #endif
         // END OF: sis_api::get_traverse_by_category_With_SIS_Server_Implementation 


         */
        //</editor-fold>
        //1) CHECK_FILES("get_traverse_by_category_With_SIS_Server_Implementation");
        //2) Check if num of categories is 0 if yes return -1
        //3) if set_id!=0 check if it exists if it is null  else get set in setptr SET* , writeset=setptr;
        //4)  else get new Set GET_NEW_SET_OR_RETURN(new_set_id,writeset); 
        // common of the above 
        // is new_set_id is the new set for storing the answer and writeset is the SET*
        //if current node the check if any current node has been set 
        //ret = getTraverseByCategory_With_SIS_Server_Implementation(*current_node, &forward_set, &backward_set,
        //depth, isa, &edge_set, writeset, &checked_set);
        /*
        int newSet = QClass.APIFail;
        ArrayList<Long> startingIds = new ArrayList<Long>();

        if (set_id == 0) {
            //startingIds.add(curNode.getId());
        } else {
            if (this.tmp_sets.containsKey(set_id) == false) {
                return QClass.APIFail;
            }
        }

        Node[] startingNodes = new Node[startingIds.size()];

        if (set_id == 0) {
            startingNodes[0] = curNode;
        }

        if (startingNodes.length == 0) {
            return QClass.APIFail;
        }

        switch (selectedImplementation) {

            // <editor-fold defaultstate="collapsed" desc="Implementation 1">
            case Implementation1: {
                //For each category:
                //Step1: Read category isa relations to get accepted category names
                //Step2: starting from current node traverse according to category direction
                //       and for each node with 'Type_Attribute' label check if it also has 
                //       a label of the expected category names. If not break travesrsal, if yes continue;

                for (CategorySet currentCateg : currentCategories) {

                    Direction selectedDirection = null;
                    if (currentCateg.direction == QClass.Traversal_Direction.FORWARD) {
                        selectedDirection = Direction.OUTGOING;
                    } else if (currentCateg.direction == QClass.Traversal_Direction.BACKWARD) {
                        selectedDirection = Direction.INCOMING;
                    } else {
                        selectedDirection = Direction.BOTH;
                    }

                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("categname", currentCateg.fcl + "->" + currentCateg.cat);

                    String queryForCategories = "";
                    ArrayList<String> catNames = new ArrayList<String>();
                    PQI_Set returnVals = new PQI_Set();

                    boolean doNotSkipCategoriesQuery = true;

                    if (whatMethod == QClass.Traversal_Isa.DOWNWARDS) {
                        queryForCategories = "MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}})<-[:ISA*0..]-(m)"
                                + " RETURN m.Logicalname as CategLabel";
                    } else if (whatMethod == QClass.Traversal_Isa.UPWARDS) {
                        queryForCategories = "MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}})-[:ISA*0..]->(m)"
                                + " RETURN m.Logicalname as CategLabel";
                    } else if (whatMethod == QClass.Traversal_Isa.UP_DOWN) {
                        queryForCategories = " OPTIONAL MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}})-[:ISA*0..]->(m) "
                                + " RETURN m.Logicalname as CategLabel"
                                + " UNION "
                                + " OPTIONAL MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}})<-[:ISA*0..]-(m) "
                                + " RETURN m.Logicalname as CategLabel";
                    } else if (whatMethod == QClass.Traversal_Isa.NOISA) {
                        queryForCategories = "MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}}) "
                                + " RETURN n.Logicalname as CategLabel";
                        doNotSkipCategoriesQuery = false;
                        catNames.add(currentCateg.fcl + "->" + currentCateg.cat);
                    }

                    if (doNotSkipCategoriesQuery) {
                        Result res = this.graphDb.execute(queryForCategories, params);

                        while (res.hasNext()) {

                            Map<String, Object> row = res.next();

                            String newCategLabel = (String) row.get("CategLabel");
                            catNames.add(newCategLabel);
                        }
                    }

                    final ArrayList<String> catLabelNames = new ArrayList<String>(catNames);

                    TraversalDescription baseTr = this.graphDb.traversalDescription().uniqueness(Uniqueness.RELATIONSHIP_GLOBAL)
                            .relationships(QClass.Rels.RELATION, selectedDirection)
                            .evaluator(new Evaluator() {
                                public Evaluation evaluate(Path path) {

                                    if (path.endNode().hasLabel(QClass.Labels.Type_Attribute)) {
                                        boolean labelFound = false;
                                        for (Label lbl : path.endNode().getLabels()) {
                                            String lblStr = lbl.name();
                                            if (catLabelNames.contains(lblStr)) {
                                                labelFound = true;
                                                break;
                                            }
                                        }
                                        if (labelFound) {
                                            return Evaluation.INCLUDE_AND_CONTINUE;
                                        } else {
                                            return Evaluation.EXCLUDE_AND_PRUNE;
                                        }
                                    } else if (path.endNode().hasLabel(QClass.Labels.Type_Individual)) {
                                        return Evaluation.EXCLUDE_AND_CONTINUE;
                                    } else {
                                        return Evaluation.EXCLUDE_AND_CONTINUE;

                                    }
                                }
                            });

                    for (Path path : baseTr.traverse(startingNodes)) {
                        for (Node node : path.nodes()) {

                            if (node.hasLabel(QClass.Labels.Type_Attribute)) {
                                long linkId = (int) node.getProperty("SysId");

                                if (returnVals.contains(linkId) == false) {
                                    returnVals.add(new PQI_SetRecord(linkId));
                                }
                            }
                        }
                    }

                    if (newSet == QClass.APIFail) {
                        newSet = set_get_new();
                        tmp_sets.put(newSet, returnVals);
                    } else {
                        PQI_Set currentVals = tmp_sets.get(newSet);
                        for (PQI_SetRecord k : returnVals) {
                            if (currentVals.contains(k.getLinkId()) == false) {
                                currentVals.add(k);
                            }
                        }
                        tmp_sets.put(newSet, currentVals);
                    }
                }

                return newSet;
            }
                // </editor-fold>            

            // <editor-fold defaultstate="collapsed" desc="Implementation 2">
            case Implementation2: {

                //For each category:
                //Step1: Read category isa relations to get accepted category ids and get instance of nodes to get their id
                //Step2: starting from current node traverse according to category direction
                //       and for each node with label 'Type_Attribute' check if its id is contained in the above set.
                //       If not break traversal, if yes continue;
                for (CategorySet currentCateg : currentCategories) {

                    Direction selectedDirection = null;
                    if (currentCateg.direction == QClass.Traversal_Direction.FORWARD) {
                        selectedDirection = Direction.OUTGOING;
                    } else if (currentCateg.direction == QClass.Traversal_Direction.BACKWARD) {
                        selectedDirection = Direction.INCOMING;
                    } else {
                        selectedDirection = Direction.BOTH;
                    }

                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("categname", currentCateg.fcl + "->" + currentCateg.cat);

                    String queryForCategories = "";

                    ArrayList<Long> incluededInstances = new ArrayList<Long>();
                    //ArrayList<Integer> returnVals = new ArrayList<Integer>();
                    PQI_Set returnVals = new PQI_Set();

                    if (whatMethod == QClass.Traversal_Isa.DOWNWARDS) {
                        queryForCategories = "MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}})<-[:ISA*0..]-(m) <-[:INSTANCEOF] - (inst)"
                                + " RETURN id(inst) as CategLinkID";
                    } else if (whatMethod == QClass.Traversal_Isa.UPWARDS) {
                        queryForCategories = "MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}})-[:ISA*0..]->(m) <-[:INSTANCEOF] - (inst)"
                                + " RETURN id(inst) as CategLinkID";
                    } else if (whatMethod == QClass.Traversal_Isa.UP_DOWN) {
                        queryForCategories = " OPTIONAL MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}})-[:ISA*0..]->(m) <-[:INSTANCEOF] - (inst) "
                                + " RETURN id(inst) as CategLinkID "
                                + " UNION "
                                + " OPTIONAL MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}})<-[:ISA*0..]-(m) <-[:INSTANCEOF] - (inst) "
                                + " RETURN id(inst) as CategLinkID ";

                    } else if (whatMethod == QClass.Traversal_Isa.NOISA) {

                        queryForCategories = "MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}}) <-[:INSTANCEOF] - (inst)"
                                + " RETURN id(inst) as CategLinkID";
                    }

                    Result res = this.graphDb.execute(queryForCategories, params);

                    while (res.hasNext()) {

                        Map<String, Object> row = res.next();

                        long newCategInstance = (long) row.get("CategLinkID");
                        incluededInstances.add(newCategInstance);
                    }

                    final ArrayList<Long> longIds = new ArrayList<Long>(incluededInstances);

                    TraversalDescription baseTr = this.graphDb.traversalDescription().uniqueness(Uniqueness.RELATIONSHIP_GLOBAL)
                            .relationships(QClass.Rels.RELATION, selectedDirection)
                            .evaluator(new Evaluator() {
                                public Evaluation evaluate(Path path) {

                                    if (path.endNode().hasLabel(QClass.Labels.Type_Attribute)) {

                                        if (longIds.contains(path.endNode().getId())) {
                                            return Evaluation.INCLUDE_AND_CONTINUE;
                                        } else {
                                            return Evaluation.EXCLUDE_AND_PRUNE;
                                        }

                                    } else if (path.endNode().hasLabel(QClass.Labels.Type_Individual)) {
                                        return Evaluation.EXCLUDE_AND_CONTINUE;
                                    } else {
                                        return Evaluation.EXCLUDE_AND_CONTINUE;

                                    }
                                }
                            });

                    for (Path path : baseTr.traverse(startingNodes)) {
                        for (Node node : path.nodes()) {
                            if (node.hasLabel(QClass.Labels.Type_Attribute)) {
                                long SysId = (int) node.getProperty("SysId");
                                if (returnVals.contains(SysId) == false) {
                                    returnVals.add(new PQI_SetRecord(SysId));
                                }
                            }
                        }
                    }

                    if (newSet == QClass.APIFail) {
                        newSet = set_get_new();
                        tmp_sets.put(newSet, returnVals);
                    } else {
                        PQI_Set currentVals = tmp_sets.get(newSet);
                        for (PQI_SetRecord k : returnVals) {
                            if (currentVals.contains(k.getLinkId()) == false) {
                                currentVals.add(k);
                            }
                        }
                        tmp_sets.put(newSet, currentVals);
                    }
                }

                return newSet;
            }
                // </editor-fold>

            // <editor-fold defaultstate="collapsed" desc="Implementation 3">
            case Implementation3: {
                    //TODO: implementation 3 waht about Relation Types in  ? 

                //For each category:
                //Step1: Read category isa relations to get accepted category ids and get instance of nodes to get their logicalname
                //Step2: starting from current node traverse according to category direction
                //       relationships that their type is in the set retrieved before.
                //       If not break traversal, if yes continue;
                //                    
                for (CategorySet currentCateg : currentCategories) {

                    Direction selectedDirection = null;
                    if (currentCateg.direction == QClass.Traversal_Direction.FORWARD) {
                        selectedDirection = Direction.OUTGOING;
                    } else if (currentCateg.direction == QClass.Traversal_Direction.BACKWARD) {
                        selectedDirection = Direction.INCOMING;
                    } else {
                        selectedDirection = Direction.BOTH;
                    }

                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("categname", currentCateg.fcl + "->" + currentCateg.cat);

                    String queryForCategories = "";
                    String queryForInstanceOfIds = "";

                    ArrayList<String> includedCategoryNames = new ArrayList<String>();
                    ArrayList<Long> includedAttributeIds = new ArrayList<Long>();
                    PQI_Set returnVals = new PQI_Set();
                    // 
                        
                     // queryForCategories = "MATCH(n:Type_Attribute{Logicalname:{categname}})<-[:ISA*0..]-(m) <-[:INSTANCEOF] - (inst)" +
                     // " RETURN id(inst) as CategLinkID";
                     // }
                     // else if(whatMethod== Traversal_Isa.UPWARDS){
                     // queryForCategories = "MATCH(n:Type_Attribute{Logicalname:{categname}})-[:ISA*0..]->(m) <-[:INSTANCEOF] - (inst)" +
                     // " RETURN id(inst) as CategLinkID";
                     // }
                     // else if(whatMethod== Traversal_Isa.UP_DOWN){
                     // queryForCategories = " MATCH(n:Type_Attribute{Logicalname:{categname}})-[:ISA*0..]->(m) <-[:INSTANCEOF] - (inst) " +
                     // " RETURN id(inst) as CategLinkID "+
                     // " UNION "+
                     // " MATCH(n:Type_Attribute{Logicalname:{categname}})<-[:ISA*0..]-(m) <-[:INSTANCEOF] - (inst) "+
                     // " RETURN id(inst) as CategLinkID ";

                     // }
                     // else if(whatMethod== Traversal_Isa.NOISA){
                            
                     // queryForCategories = "MATCH(n:Type_Attribute{Logicalname:{categname}}) <-[:INSTANCEOF] - (inst)" +
                     // " RETURN id(inst) as CategLinkID";      
                     
                    if (whatMethod == QClass.Traversal_Isa.DOWNWARDS) {
                        queryForCategories = "MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}})<-[:ISA*0..]-(m) "
                                + " RETURN m.Logicalname as CategLinkName";

                        queryForInstanceOfIds = "MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}})<-[:ISA*0..]-(m) <-[:INSTANCEOF] - (inst) "
                                + " RETURN id(inst) as CategLinkID";
                    } else if (whatMethod == QClass.Traversal_Isa.UPWARDS) {
                        queryForCategories = "MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}})-[:ISA*0..]->(m) "
                                + " RETURN m.Logicalname as CategLinkName";

                        queryForInstanceOfIds = "MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}})-[:ISA*0..]->(m) <-[:INSTANCEOF] - (inst) "
                                + " RETURN id(inst) as CategLinkID";

                    } else if (whatMethod == QClass.Traversal_Isa.UP_DOWN) {
                        queryForCategories = "OPTIONAL MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}})-[:ISA*0..]->(m) "
                                + " RETURN m.Logicalname as CategLinkName"
                                + " UNION "
                                + " OPTIONAL MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}})<-[:ISA*0..]-(m) "
                                + " RETURN m.Logicalname as CategLinkName";

                        queryForInstanceOfIds = "OPTIONAL MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}})-[:ISA*0..]->(m) <-[:INSTANCEOF] - (inst) "
                                + " RETURN id(inst) as CategLinkID "
                                + " UNION "
                                + " OPTIONAL MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}})<-[:ISA*0..]-(m) <-[:INSTANCEOF] - (inst) "
                                + " RETURN id(inst) as CategLinkID ";

                    } else if (whatMethod == QClass.Traversal_Isa.NOISA) {

                        queryForCategories = "MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}}) "
                                + " RETURN n.Logicalname as CategLinkName";

                        queryForInstanceOfIds = "MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}}) <-[:INSTANCEOF] - (inst)"
                                + " RETURN id(inst) as CategLinkID";
                    }

                    Result res = this.graphDb.execute(queryForCategories, params);

                    while (res.hasNext()) {

                        Map<String, Object> row = res.next();

                        String newCategInstance = (String) row.get("CategLinkName");
                        includedCategoryNames.add(newCategInstance);
                    }

                    Result res2 = this.graphDb.execute(queryForInstanceOfIds, params);

                    while (res2.hasNext()) {

                        Map<String, Object> row = res2.next();

                        long newCategInstanceId = (long) row.get("CategLinkID");
                        includedAttributeIds.add(newCategInstanceId);
                    }

                    final ArrayList<String> categNames = new ArrayList<String>(includedCategoryNames);
                    final ArrayList<Long> attrIds = new ArrayList<Long>(includedAttributeIds);
                    //categNames.add(RELATION);

                    TraversalDescription baseTr = this.graphDb.traversalDescription().uniqueness(Uniqueness.RELATIONSHIP_GLOBAL)
                            .evaluator(new Evaluator() {
                                public Evaluation evaluate(Path path) {

                                    if (path.length() == 0) {
                                        return Evaluation.INCLUDE_AND_CONTINUE;
                                    } else if (categNames.contains(path.lastRelationship().getType().name())) {
                                        return Evaluation.INCLUDE_AND_CONTINUE;
                                    } else if (path.endNode().hasLabel(QClass.Labels.Type_Attribute)) {

                                        if (attrIds.contains(path.endNode().getId())) {
                                            return Evaluation.INCLUDE_AND_CONTINUE;
                                        } else {
                                            return Evaluation.EXCLUDE_AND_PRUNE;
                                        }

                                    } else {
                                        return Evaluation.EXCLUDE_AND_CONTINUE;
                                    }

                                }
                            });

                    for (String p : categNames) {
                        baseTr = baseTr.relationships(DynamicRelationshipType.withName(p), selectedDirection);
                    }
                    baseTr = baseTr.relationships(QClass.Rels.RELATION, selectedDirection);

                    for (Path path : baseTr.traverse(startingNodes)) {
                        for (Relationship rel : path.relationships()) {
                            if (rel.hasProperty("SysId")) {
                                long SysId = (int) rel.getProperty("SysId");
                                if (returnVals.contains(SysId) == false) {
                                    returnVals.add(new PQI_SetRecord(SysId));
                                }
                            }
                        }

                        for (Node node : path.nodes()) {
                            if (node.hasLabel(QClass.Labels.Type_Attribute)) {
                                long SysId = (int) node.getProperty("SysId");
                                if (returnVals.contains(SysId) == false) {
                                    returnVals.add(new PQI_SetRecord(SysId));
                                }
                            }
                        }
                    }

                    if (newSet == QClass.APIFail) {
                        newSet = set_get_new();
                        tmp_sets.put(newSet, returnVals);
                    } else {
                        PQI_Set currentVals = tmp_sets.get(newSet);
                        for (PQI_SetRecord k : returnVals) {
                            if (currentVals.contains(k.getLinkId()) == false) {
                                currentVals.add(k);
                            }
                        }
                        tmp_sets.put(newSet, currentVals);
                    }
                }

                return newSet;
            }
                // </editor-fold>       

            // <editor-fold defaultstate="collapsed" desc="Implementation 4">
            case Implementation4: {

                //For each category:
                //Step1: Read category isa relations to get accepted category names and get instance of nodes to get their logicalname
                //Step2: starting from current node traverse according to category direction
                //       relationships that their type is in the set retrieved before.
                //       If not break traversal, if yes continue;
                //                    
                for (CategorySet currentCateg : currentCategories) {

                    Direction selectedDirection = null;
                    if (currentCateg.direction == QClass.Traversal_Direction.FORWARD) {
                        selectedDirection = Direction.OUTGOING;
                    } else if (currentCateg.direction == QClass.Traversal_Direction.BACKWARD) {
                        selectedDirection = Direction.INCOMING;
                    } else {
                        selectedDirection = Direction.BOTH;
                    }

                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("categname", currentCateg.fcl + "->" + currentCateg.cat);

                    String queryForCategories = "";

                    ArrayList<String> includedCategoryNames = new ArrayList<String>();
                    PQI_Set returnVals = new PQI_Set();

                    if (whatMethod == QClass.Traversal_Isa.DOWNWARDS) {
                        queryForCategories = "MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}})<-[:ISA*0..]-(m) "
                                + " RETURN m.Logicalname as CategLinkName";

                    } else if (whatMethod == QClass.Traversal_Isa.UPWARDS) {
                        queryForCategories = "MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}})-[:ISA*0..]->(m) "
                                + " RETURN m.Logicalname as CategLinkName";

                    } else if (whatMethod == QClass.Traversal_Isa.UP_DOWN) {
                        queryForCategories = "OPTIONAL MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}})-[:ISA*0..]->(m) "
                                + " RETURN m.Logicalname as CategLinkName"
                                + " UNION "
                                + " OPTIONAL MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}})<-[:ISA*0..]-(m) "
                                + " RETURN m.Logicalname as CategLinkName";
                    } else if (whatMethod == QClass.Traversal_Isa.NOISA) {

                        queryForCategories = "MATCH(n" + (useCommonLabel ? ":" + CommonLabelName : ":Type_Attribute") + "{Logicalname:{categname}}) "
                                + " RETURN n.Logicalname as CategLinkName";
                    }

                    Result res = this.graphDb.execute(queryForCategories, params);

                    while (res.hasNext()) {

                        Map<String, Object> row = res.next();

                        String newCategInstance = (String) row.get("CategLinkName");
                        includedCategoryNames.add(newCategInstance);
                    }

                    final ArrayList<String> categNames = new ArrayList<String>(includedCategoryNames);
                    ArrayList<Label> categLabels = new ArrayList<Label>();
                    for (String str : categNames) {
                        categLabels.add(DynamicLabel.label(str));
                    }
                    final ArrayList<Label> categLabelNames = new ArrayList<Label>(categLabels);
                        //categNames.add(RELATION);

                    //Implementation4
                    TraversalDescription baseTr = this.graphDb.traversalDescription().uniqueness(Uniqueness.RELATIONSHIP_GLOBAL);

                    for (String p : categNames) {
                        baseTr = baseTr.relationships(DynamicRelationshipType.withName(p), selectedDirection);
                    }
                    baseTr = baseTr.relationships(QClass.Rels.RELATION, selectedDirection);

                    baseTr = baseTr.evaluator(new Evaluator() {
                        public Evaluation evaluate(Path path) {

                            if (path.length() == 0) {
                                return Evaluation.INCLUDE_AND_CONTINUE;
                                //}else if(path.lastRelationship()!=null && path.lastRelationship().getType()!= null && path.lastRelationship().getType().name()!= null && categNames.contains(path.lastRelationship().getType().name())){
                            } else if (categNames.contains(path.lastRelationship().getType().name())) {
                                return Evaluation.INCLUDE_AND_CONTINUE;
                            } else if (path.endNode().hasLabel(QClass.Labels.Type_Attribute)) {

                                boolean valFound = false;
                                for (Label lbl : categLabelNames) {
                                    if (path.endNode().hasLabel(lbl)) {
                                        valFound = true;
                                        break;
                                    }
                                }
                                if (valFound) {
                                    return Evaluation.INCLUDE_AND_CONTINUE;
                                } else {
                                    return Evaluation.EXCLUDE_AND_PRUNE;
                                }
                            } else {
                                return Evaluation.EXCLUDE_AND_CONTINUE;
                            }
                        }
                    });

                    for (Path path : baseTr.traverse(startingNodes)) {
                        for (Relationship rel : path.relationships()) {
                            //if(node.hasLabel(Labels.Type_Attribute)){
                            if (rel.hasProperty("SysId")) {
                                long SysId = (int) rel.getProperty("SysId");
                                if (returnVals.contains(SysId) == false) {
                                    returnVals.add(new PQI_SetRecord(SysId));
                                }
                            }
                        }

                        for (Node node : path.nodes()) {
                            if (node.hasLabel(QClass.Labels.Type_Attribute)) {
                                long SysId = (int) node.getProperty("SysId");
                                if (returnVals.contains(SysId) == false) {
                                    returnVals.add(new PQI_SetRecord(SysId));
                                }
                            }
                        }

                        //Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, copunter +".\t"+path.toString());
                    }

                    if (newSet == QClass.APIFail) {
                        newSet = set_get_new();
                        tmp_sets.put(newSet, returnVals);
                    } else {
                        PQI_Set currentVals = tmp_sets.get(newSet);
                        for (PQI_SetRecord k : returnVals) {
                            if (currentVals.contains(k.getLinkId()) == false) {
                                currentVals.add(k);
                            }
                        }
                        tmp_sets.put(newSet, currentVals);
                    }
                }

                return newSet;

            }
            // </editor-fold>          

            default: {
                return QClass.APIFail;
            }
        }
*/
        return QClass.APIFail;
    }
    
    long TEST_get_SysId_From_Neo4jId(long neo4jId) {
        
        String query = " MATCH (n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(neo4jId)+"}) "+
                        " RETURN n."+Neo4j_Key_For_SysId +" as resourceId ";

        Result res = this.graphDb.execute(query);
        try {
            while (res.hasNext()) {

                Map<String, Object> row = res.next();
                long val = -1;
                if(Configs.CastSysIdAsInt){
                    val = (int) row.get("resourceId");
                }
                else{
                    val = (long) row.get("resourceId");
                }
                if (val > 0) {
                    return val;
                } else {
                    return QClass.APIFail;
                }
            }
        } finally {
            res.close();
            res = null;
        }
                

        return QClass.APIFail;
    }
    
    
    
    
    /*Not valid name of function it is just temporary*/
    String FindHierarachy(String Prefix, String Logicalname, String ChangedNodeProperty) throws UnsupportedEncodingException
    {
        PrintStream out = new PrintStream(System.out,true,"UTF-8");
        String Logicalnames = null;
        String SearchLogicalname = Prefix+ChangedNodeProperty;
        
        out.println("### SearchLogicalname: "+SearchLogicalname);
        
        String query ="MATCH(n) <-[:RELATION]-() <-[:RELATION*0..]-(m{Logicalname:'"+SearchLogicalname+"'})"
                       +" WHERE n.Logicalname =~ '.*"+ Logicalname +"`.*'"
                       + " RETURN n.Logicalname AS Logicalname";
        
        Result res = this.graphDb.execute(query);
        
        
         while (res.hasNext()) {
            Map<String, Object> row = res.next();
            Logicalnames =(String) row.get("Logicalname");
            out.println("Logicalname: "+Logicalnames);
        }
         
             
        return Logicalnames;
    }
    
    /*Not valid name of function it is just temporary*/
    String RenameClass(String Prefix, String Logicalname, String ChangedNodeProperty) throws UnsupportedEncodingException
    {
        String SearchLogicalname = Prefix+ChangedNodeProperty;
        String NewLogicalname = Prefix+ChangedNodeProperty;

        
        String query ="MATCH(n{Logicalname:'"+ SearchLogicalname +"'}) <-[:RELATION]-() <-[:RELATION*0..]-(m)"
                       +" WHERE m.Logicalname =~ '.*"+ Logicalname +".*'"
                       +" RETURN m.Logicalname AS Logicalname";
        
        Result res = this.graphDb.execute(query);
        String val = null;
        PrintStream out = new PrintStream(System.out,true,"UTF-8");
         
        while (res.hasNext()) {
            Map<String, Object> row = res.next();
            val = (String) row.get("Logicalname");  
            out.println("Logicalname: "+val);
        }

        return val;
    }
    
    int ChangeNodeLabel(String OriginalLogicalname, String NewLogicalname) throws UnsupportedEncodingException
    {
        PrintStream out = new PrintStream(System.out,true,"UTF-8");
        String Label = OriginalLogicalname.replaceAll("`", "``");
        
        String query ="MATCH(n:`"+ Label +"`) RETURN n AS Node";
        Result res = this.graphDb.execute(query);

        while (res.hasNext()) {
            Node newNode;
            Map<String, Object> row = res.next();
            newNode = (Node) row.get("Node");
            
            newNode.removeLabel(org.neo4j.graphdb.Label.label(OriginalLogicalname));
            newNode.addLabel(org.neo4j.graphdb.Label.label(NewLogicalname));
            
            out.println("Logicalname: "+newNode.getLabels().toString());
        }

       return 1;
    }
    
    String ChangeNodeProperty(Long id, String ChangedNodeProperty) throws UnsupportedEncodingException
    {
        Node newNode;
        
        Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "NodeID to Change: "+id);
       
        newNode = graphDb.getNodeById(id);
        
        PrintStream out = new PrintStream(System.out,true,"UTF-8");
        out.println("**** Node: "+newNode.getProperty("Logicalname"));
        
        newNode.setProperty("Logicalname", ChangedNodeProperty);
        
        return newNode.getProperty("Logicalname").toString();
    }
    
    
    void ChangeNodePropertyNeo4j_Id(Long Neo4j_Id, String NewLogicalname) 
    {
        try {
            PrintStream out = new PrintStream(System.out,true,"UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DBaccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** ChangeNodePropertyNeo4j_Id Neo4j_Id to Change: "+Neo4j_Id);
       
        String query = "MATCH(n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(Neo4j_Id)+ "}) "
                        +"SET n.Logicalname ='"+ NewLogicalname +"' ";
                       // +"RETURN n.Logicalname AS lo";
       
        Result res = this.graphDb.execute(query);
        
        /*while (res.hasNext()) {
            String newNode = null;
            Map<String, Object> row = res.next();
            newNode = (String) row.get("lo");
            
            out.println("*** ChangeNodePropertyNeo4j_Id: "+newNode);
        }*/
    }
    
    void ChangeNodeLabelNeo4j_Id(String OriginalLogicalname, String NewLogicalname)
    {
        try {
            PrintStream out = new PrintStream(System.out,true,"UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(DBaccess.class.getName()).log(Level.SEVERE, null, ex);
        }
        String Label = OriginalLogicalname.replaceAll("`", "``");
        String NewLabel = NewLogicalname.replaceAll("`", "``");
        Node newNode;
        
        String query ="MATCH(n:`"+ Label +"`)"+" "
                        +"SET n:`"+ NewLabel +"` "
                        +"REMOVE n:`"+ Label +"` ";
                       // +"RETURN n AS Node";
        
        Result res = this.graphDb.execute(query);
        
       /* while (res.hasNext()) {
            Map<String, Object> row = res.next();
            newNode = (Node) row.get("Node");
            
            out.println("*** ChangeNodeLabelNeo4j_Id: "+newNode.getLabels().toString());
        }*/
    }
    
    
    /*
    Input1:
            String Original Logicalname of a Node
    Input2:
            String New Logicalname of a Node
    */
    void RenameNodeLogicalName(String OriginalLogicalname, String NewLogicalname) throws UnsupportedEncodingException
    {
        String tmp_org = null;
        String tmp = null;
        String SplitLogicalname[];
        String SplitChangedLogicalname[];
        
        String delimiter = "`";
        String Prefix = null;
        String Logicalname = null;
        String ChangedLogicalname = null;
        
        long node_id;
        tmp_org = OriginalLogicalname;
        
        node_id = FindNodeId(OriginalLogicalname);
        tmp = ChangeNodeProperty(node_id, NewLogicalname);

        //Split "METALAClass`Υλικά" to PREFIX: METALAClass and Logicalname: Υλικά
        SplitLogicalname = tmp_org.split(delimiter);
        SplitChangedLogicalname = NewLogicalname.split(delimiter);
        
        Prefix = SplitLogicalname[0]+delimiter; //Merge character "`" to METALAClass ( METALAClass` )
        Logicalname = SplitLogicalname[1];
        ChangedLogicalname = SplitChangedLogicalname[1];
        
        PrintStream out = new PrintStream(System.out,true,"UTF-8");
        out.println("******** Node : "+tmp);
        out.println("******** ChangedLogicalname : "+ChangedLogicalname);
        out.println("******** Logicalname : "+Logicalname);
        
        //Find METALAEL`Υλικά
        tmp = RenameClass(Prefix, Logicalname, ChangedLogicalname);
        out.println("Next Node 1 : "+tmp);
        out.println("******** Node : "+tmp);
        out.println("******** ChangedLogicalname : "+ChangedLogicalname);
        out.println("******** Logicalname : "+Logicalname);
        
        //Split "METALAEL`Υλικά" to PREFIX: METALAEL and Logicalname: Υλικά
        SplitLogicalname = tmp.split(delimiter);
        Prefix = SplitLogicalname[0]+delimiter;//Merge character "`" to METALAEL ( METALAEL` )
        String Change = Prefix+ChangedLogicalname;
        
        node_id = FindNodeId(tmp);
        tmp = ChangeNodeProperty(node_id, Change);
        out.println("Next Node 2 : "+tmp);
        out.println("******** Node : "+tmp);
        out.println("******** ChangedLogicalname : "+ChangedLogicalname);
        out.println("******** Logicalname : "+Logicalname);

        //Println just for Validation  
        //PrintStream out = new PrintStream(System.out,true,"UTF-8");
        //out.println("PREFIX: "+Prefix+"  "+Logicalname);
        
        //Find METALAEL`Υλικα`*** 
        tmp = FindHierarachy(Prefix,Logicalname,ChangedLogicalname);
        String Suffix;
        node_id = FindNodeId(tmp);
        SplitLogicalname = tmp.split(delimiter);
        Suffix = SplitLogicalname[2];
        tmp = ChangeNodeProperty(node_id, Change+delimiter+Suffix);
        out.println("Next Node 3 : "+tmp);
        out.println("******** Node : "+tmp);
        out.println("******** ChangedLogicalname : "+ChangedLogicalname);
        out.println("******** Logicalname : "+Logicalname);

        //ChangeNodeLabel(OriginalLogicalname, NewLogicalname);
    }
    
        
    long FindNodeId(String Logicalname)
    {
        String query ="MATCH(n{Logicalname:'"+Logicalname+"'}) RETURN id(n) AS NodeID";
        
        Result res = this.graphDb.execute(query);
        long val = -1;
   
         
        while (res.hasNext()) {
            Map<String, Object> row = res.next();
            val = (Long) row.get("NodeID");  
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "NodeID: "+val);
        }
     
        return val;
    }
    
    int CheckAllExist(PQI_Set set) {

        ArrayList<Long> vec = set.get_Neo4j_Ids();

        long found_ids = 0;
        //int check = APIFail;

        int loopIndex = 0;
        int maxIndex = vec.size();

        if (maxIndex == 0) {
            return APISucc;
        }

        while (loopIndex < maxIndex) {

            ArrayList<Long> subSetIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex, Configs.MAX_IDS_PER_QUERY, vec);
            loopIndex += subSetIds.size();
            if (subSetIds.size() == 0) {
                break;
            }
            String query = "";
            if (subSetIds.size() == 1) {
                query = " MATCH (n" + getCommonLabelStr() + "{" + prepareNeo4jIdPropertyFilterForCypher(subSetIds.get(0)) + "}) "
                        + " RETURN count(n." + Configs.Neo4j_Key_For_Neo4j_Id + ") as cnt ";
            } else {
                query = " MATCH (n" + getCommonLabelStr() + ") "
                        + " WHERE n." + Configs.Neo4j_Key_For_Neo4j_Id + " IN " + subSetIds.toString() + " "
                        + " RETURN count(n." + Configs.Neo4j_Key_For_Neo4j_Id + ") as cnt ";
            }

            //do the job do not return
            Result res = graphDb.execute(query);
            try {
                while (res.hasNext()) {

                    Object obj = res.next().get("cnt");
                    if (obj instanceof Long) {
                        found_ids += (long) obj;
                    } else if (obj instanceof Integer) {
                        found_ids += (int) obj;
                    }
                }
                //ATTENTION do not return here 

            } catch (Exception ex) {
                utils.handleException(ex);
                //check = APIFail;
                break;

            } finally {
                res.close();
                res = null;
            }
        }

        if (DebugInfo == true) {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "Found IDs: " + found_ids + " VEC SIZE: " + vec.size());
        }

        if (found_ids == vec.size()) {
            return APISucc;
        } else {
            return APIFail;
        }
    }
    
    int getFrom_or_To_NodeByCategory(ArrayList<Long> neo4jIds, StringObject fromcls, StringObject categ, PQI_Set writeset, boolean fromIsteadOfTo){
        
        PrimitiveObject_Long categId = new PrimitiveObject_Long();
        categId.setValue(getLinkId(fromcls.getValue(), categ.getValue()));
        if(categId.getValue()==APIFail){
            return APIFail;
        }
        
        ArrayList<Node> nodes = getNeo4jNodesByNeo4jIds(neo4jIds);
        if(nodes.size()!= neo4jIds.size()){
            return APIFail;
        }
        
        PQI_Set allSubclasses = new PQI_Set();
        if(getAllSuper_or_AllSubClasses(categId.getValue(), allSubclasses, false)==APIFail){
            return APIFail;
        }

        allSubclasses.set_putNeo4j_Id(categId.getValue());
        //getInstancesSET(allSubclasses, allSubclasses);

        ArrayList<Long> classesIds = allSubclasses.get_Neo4j_Ids();

        final ArrayList<Long> finalClassIds = new ArrayList<Long>(classesIds);
        
        TraversalDescription trDescription = this.graphDb.traversalDescription()
                .uniqueness(Uniqueness.RELATIONSHIP_GLOBAL)
                //depth 2 is the only one that we are intereset in
                .evaluator(Evaluators.toDepth(2))
                //an extra evaluator is needed for excluding end nodes that are not attibutes
                .evaluator(new Evaluator() {
                        public Evaluation evaluate(Path path) {

                            if(path.length()==1){
                                Node endNode = path.endNode();
                                //only interested in attributes
                                if(endNode.hasLabel(Configs.Labels.Type_Attribute)==false){
                                    return Evaluation.EXCLUDE_AND_PRUNE;
                                }
                                
                                long endNodeId = getNodeNeo4jId(endNode);
                                if(finalClassIds.contains(endNodeId)){
                                    //Not sure about this case
                                    return Evaluation.INCLUDE_AND_CONTINUE;
                                }
                                else{
                                    
                                    //finalClassIds categ id filter
                                    if(endNode.hasRelationship(Configs.Rels.INSTANCEOF, Direction.OUTGOING)==false){
                                        //this case was the motivation for the if(finalClassIds.contains(endNodeId)){
                                        return Evaluation.EXCLUDE_AND_PRUNE;  
                                    }

                                    Iterator<Relationship> relIter = endNode.getRelationships(Configs.Rels.INSTANCEOF, Direction.OUTGOING).iterator();
                                    while(relIter.hasNext()){
                                        long classId = getNodeNeo4jId(relIter.next().getEndNode());
                                        if(finalClassIds.contains(classId)){
                                            return Evaluation.INCLUDE_AND_CONTINUE;
                                        }
                                    }
                                    return Evaluation.EXCLUDE_AND_PRUNE;  
                                }
                            }

                            return Evaluation.INCLUDE_AND_CONTINUE;                            
                        }
                    });
        
        if(fromIsteadOfTo){ //it is the oposite than expected
            trDescription = trDescription.relationships(Configs.Rels.RELATION, Direction.INCOMING);
        }
        else{
            
            trDescription = trDescription.relationships(Configs.Rels.RELATION, Direction.OUTGOING);
        }

        ArrayList<Long> retVals = new ArrayList<Long>();
        
        for(Path path: trDescription.traverse(nodes)){
            if(path.length()<2){
                continue;
            }
            Node endNode = path.endNode();
            retVals.add(getNodeNeo4jId(endNode));            
        }
        
        for(long l: retVals){
            writeset.set_putNeo4j_Id(l);
        }
        
        return APISucc;
    }
    
    int getLink_From_or_To_ByMETACategory(ArrayList<Long> neo4jIds, StringObject fromcls, StringObject categ, PQI_Set writeset, boolean fromIsteadOfTo){
        /*
        SYSID   categid;
	LOGINAM tmpfromcls(fromcls);
	char    message[500];
	char   tmpcateg [1024];

	strcpy(tmpcateg, categ);
	if ((categid = getSysid(&tmpfromcls,tmpcateg)).id == 0) {
		sprintf(message,">getLinkFromByMetaCategory: category(%s,%s) does not exist", fromcls, tmpcateg);
		globalError.putMessage(message);
		return -1;  // category doesn't exist
	}

	return getLinkFromByMetaCategory(objSysid, categid, retSysids);
        
        int sis_api::getLinkFromByMetaCategory(SYSID objSysid,SYSID categid,SET *retSysids)
{
	SET      link_set;
	SET      categ_set;
	SET      meta_categ_set;
	SYSID    linkid;

	// get all links pointed by the object with 
	// sysid "objSysid" including the instance  
	// attributes too. 
	if (getLinkFrom(objSysid, &link_set) == -1) {
		globalError.putMessage(" >getLinkFromByMetaCategory");
		return -1;
	}

	link_set.set_pos(0);

	// traverse the set of links 
	FOR_EACH_ID_IN_SET(linkid, link_set) {
		categ_set.set_clear();
		meta_categ_set.set_clear();

		// get the classes of each link 
		if (getAllClasses(linkid, &categ_set) != -1) {

			// get the classes of each link's category 
			if (getAllClasses(&categ_set, &meta_categ_set) != -1) {
				if (meta_categ_set.set_member_of(categid)) {
					retSysids->set_put(linkid);
				}
			}
		}
	}
	return globalError.checkError(" >getLinkFromByMetaCategory");
}
        */
        
        
        PrimitiveObject_Long categId = new PrimitiveObject_Long();
        categId.setValue(getLinkId(fromcls.getValue(), categ.getValue()));
        if(categId.getValue()==APIFail){
            return APIFail;
        }
        
        ArrayList<Node> nodes = getNeo4jNodesByNeo4jIds(neo4jIds);
        if(nodes.size()!= neo4jIds.size()){
            return APIFail;
        }
        
        //PQI_Set allSubclasses = new PQI_Set();
        //if(getAllSuper_or_AllSubClasses(categId.getValue(), allSubclasses, false)==APIFail){
          //  return APIFail;
        //}

        //allSubclasses.set_putNeo4j_Id(categId.getValue());
        //getInstancesSET(allSubclasses, allSubclasses);

        //ArrayList<Long> classesIds = allSubclasses.get_Neo4j_Ids();

        TraversalDescription trDescription = this.graphDb.traversalDescription()
                .uniqueness(Uniqueness.RELATIONSHIP_GLOBAL)
                //depth one is the only one that we are intereset in
                .evaluator(Evaluators.toDepth(1))
                //an extra evaluator is needed for excluding end nodes that are not attibutes
                .evaluator(new Evaluator() {
                        public Evaluation evaluate(Path path) {

                            if(path.length()==1){
                                Node endNode = path.endNode();
                                //only interested in attributes
                                if(endNode.hasLabel(Configs.Labels.Type_Attribute)==false){
                                    return Evaluation.EXCLUDE_AND_PRUNE;
                                }
                                //PROBABLY NOT NEEDED AS WE HAVE THE .evaluator(Evaluators.toDepth(1))
                                return Evaluation.INCLUDE_AND_PRUNE;
                            }

                            return Evaluation.EXCLUDE_AND_CONTINUE;                            
                        }
                    });

        if(fromIsteadOfTo){
            trDescription = trDescription.relationships(Configs.Rels.RELATION, Direction.OUTGOING);
        }
        else{
            trDescription = trDescription.relationships(Configs.Rels.RELATION, Direction.INCOMING);
        }

        ArrayList<Long> retVals = new ArrayList<Long>();
        for(Path path: trDescription.traverse(nodes)){
            Node endNode = path.endNode();
            //Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, path);
            
            // <editor-fold defaultstate="collapsed" desc="C++ Relevant Code">
            /*
            int sis_api::getLinkFromByMetaCategory(SYSID objSysid,SYSID categid,SET *retSysids)
{
	SET      link_set;
	SET      categ_set;
	SET      meta_categ_set;
	SYSID    linkid;

	// get all links pointed by the object with 
	// sysid "objSysid" including the instance  
	// attributes too. 
	if (getLinkFrom(objSysid, &link_set) == -1) {
		globalError.putMessage(" >getLinkFromByMetaCategory");
		return -1;
	}

	link_set.set_pos(0);

	// traverse the set of links 
	FOR_EACH_ID_IN_SET(linkid, link_set) {
		categ_set.set_clear();
		meta_categ_set.set_clear();

		// get the classes of each link 
		if (getAllClasses(linkid, &categ_set) != -1) {

			// get the classes of each link's category 
			if (getAllClasses(&categ_set, &meta_categ_set) != -1) {
				if (meta_categ_set.set_member_of(categid)) {
					retSysids->set_put(linkid);
				}
			}
		}
	}
	return globalError.checkError(" >getLinkFromByMetaCategory");
}
            */
            // </editor-fold> 
            
            long endNodeId = getNodeNeo4jId(endNode);
            
            PQI_Set InitialSet = new PQI_Set();
            InitialSet.set_putNeo4j_Id(endNodeId);
            PQI_Set categ_set = new PQI_Set();
            PQI_Set meta_categ_set = new PQI_Set();
            
            if (getAll_Or_Simple_ClassesSET(InitialSet, categ_set, true) != QClass.APIFail){
                if(getAll_Or_Simple_ClassesSET(categ_set, meta_categ_set, true)!=QClass.APIFail){
                    if (meta_categ_set.set_member_of(categId.getValue())==APISucc) {
                        retVals.add(endNodeId);
                    }
                }
            }
        }
        for(long l: retVals){
            writeset.set_putNeo4j_Id(l);
        }
        return APISucc;
    }
    
    int getLink_From_or_To_ByCategory(ArrayList<Long> neo4jIds, StringObject fromcls, StringObject categ, PQI_Set writeset, boolean fromIsteadOfTo){
        
        PrimitiveObject_Long categId = new PrimitiveObject_Long();
        categId.setValue(getLinkId(fromcls.getValue(), categ.getValue()));
        if(categId.getValue()==APIFail){
            return APIFail;
        }
        
        ArrayList<Node> nodes = getNeo4jNodesByNeo4jIds(neo4jIds);
        if(nodes.size()!= neo4jIds.size()){
            return APIFail;
        }
        
        PQI_Set allSubclasses = new PQI_Set();
        if(getAllSuper_or_AllSubClasses(categId.getValue(), allSubclasses, false)==APIFail){
            return APIFail;
        }

        allSubclasses.set_putNeo4j_Id(categId.getValue());
        //getInstancesSET(allSubclasses, allSubclasses);

        ArrayList<Long> classesIds = allSubclasses.get_Neo4j_Ids();

        TraversalDescription trDescription = this.graphDb.traversalDescription()
                .uniqueness(Uniqueness.RELATIONSHIP_GLOBAL)
                //depth one is the only one that we are intereset in
                .evaluator(Evaluators.toDepth(1))
                //an extra evaluator is needed for excluding end nodes that are not attibutes
                .evaluator(new Evaluator() {
                        public Evaluation evaluate(Path path) {

                            if(path.length()==1){
                                Node endNode = path.endNode();
                                //only interested in attributes
                                if(endNode.hasLabel(Configs.Labels.Type_Attribute)==false){
                                    return Evaluation.EXCLUDE_AND_PRUNE;
                                }
                                //PROBABLY NOT NEEDED AS WE HAVE THE .evaluator(Evaluators.toDepth(1))
                                return Evaluation.INCLUDE_AND_PRUNE;
                            }

                            return Evaluation.EXCLUDE_AND_CONTINUE;                            
                        }
                    });

        if(fromIsteadOfTo){
            trDescription = trDescription.relationships(Configs.Rels.RELATION, Direction.OUTGOING);
        }
        else{
            trDescription = trDescription.relationships(Configs.Rels.RELATION, Direction.INCOMING);
        }

        ArrayList<Long> retVals = new ArrayList<Long>();
        //retVals.add(getNeo4jIdFromObject(res.next().get(Configs.Neo4j_Key_For_Neo4j_Id)));
        for(Path path: trDescription.traverse(nodes)){
            Node endNode = path.endNode();
            //Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, path);
            
            long endNodeId = getNodeNeo4jId(endNode);
            //if(endNodeId==16927){
//                Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "DEBUG");
  //          }
            if(classesIds.contains(endNodeId)){
                //Not sure about this case
                retVals.add(endNodeId);
                continue;
            }
            
            if(endNode.hasRelationship(Configs.Rels.INSTANCEOF, Direction.OUTGOING)==false){
                //this case was the motivation for the if(classesIds.contains(endNodeId)){
                continue;
            }
            Iterator<Relationship> relIter = endNode.getRelationships(Configs.Rels.INSTANCEOF, Direction.OUTGOING).iterator();
            while(relIter.hasNext()){
              long classId = getNodeNeo4jId(relIter.next().getEndNode());
              if(classesIds.contains(classId)){
                  retVals.add(endNodeId);
                  break;
              }
            }

            
        }
        for(long l: retVals){
            writeset.set_putNeo4j_Id(l);
        }
        return APISucc;
    }
    
    int getSuper_or_SubClassesSET(PQI_Set setIDs, PQI_Set retSysids, boolean getSuperInsteadOfSubclasses) {

        if (CheckAllExist(setIDs) == APIFail) {
            return APIFail;
        }

        ArrayList<Long> ids = setIDs.get_Neo4j_Ids();

        if (DebugInfo == true) {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getSuper_or_SubClassesSET getSuperInsteadOfSubclasses: " + getSuperInsteadOfSubclasses + " input IDs: " + ids);
        }

        ArrayList<Long> tmpvec = new ArrayList<Long>();

        int loopIndex = 0;
        int maxIndex = ids.size();

        if (maxIndex == 0) {
            return APISucc;
        }

        while (loopIndex < maxIndex) {

            ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex, Configs.MAX_IDS_PER_QUERY, ids);
            loopIndex += subSetofIds.size();
            if (subSetofIds.size() == 0) {
                break;
            }
            String query = "";
            if (subSetofIds.size() == 1) {
                query = "MATCH (n" + getCommonLabelStr() + "{" + prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0)) + "}) "
                        + (getSuperInsteadOfSubclasses ? " -[:ISA]-> " : " <-[:ISA]- ") + " (m) "
                        + " RETURN m." + Configs.Neo4j_Key_For_Neo4j_Id + " as " + Configs.Neo4j_Key_For_Neo4j_Id + " ";
            } else {
                query = " MATCH (n" + getCommonLabelStr() + ") "
                        + (getSuperInsteadOfSubclasses ? " -[:ISA]-> " : " <-[:ISA]- ") + " (m) "
                        + " WHERE n." + Configs.Neo4j_Key_For_Neo4j_Id + " IN " + subSetofIds.toString()
                        + " RETURN m." + Configs.Neo4j_Key_For_Neo4j_Id + " as " + Configs.Neo4j_Key_For_Neo4j_Id + " ";
            }

            //do the job do not return
            Result res = graphDb.execute(query);
            try {
                while (res.hasNext()) {

                    long val = getNeo4jIdFromObject(res.next().get(Configs.Neo4j_Key_For_Neo4j_Id));

                    if (val > 0) {
                        tmpvec.add(val);
                    } else {
                        if (Configs.boolDebugInfo) {
                            throw new UnsupportedOperationException();
                        }
                    }
                }

            } catch (Exception ex) {
                utils.handleException(ex);
                return APIFail;
            } finally {
                res.close();
                res = null;
            }
        }

        for (Long l : tmpvec) {
            retSysids.set_putNeo4j_Id(l);
        }

        if (DebugInfo == true) {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getSuper_or_SubClassesSET getSuperInsteadOfSubclasses: " + getSuperInsteadOfSubclasses + " retSysids size: " + retSysids.get_Neo4j_Ids().size());
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getSuper_or_SubClassesSET getSuperInsteadOfSubclasses: " + getSuperInsteadOfSubclasses + " retSysids: " + retSysids.get_Neo4j_Ids());
        }

        return APISucc;

    }
    
    int getLinkFrom_Or_ToSET(PQI_Set setIDs, PQI_Set retSysids, boolean getFromInsteadOfTo) {

        if (CheckAllExist(setIDs) == APIFail) {
            return APIFail;
        }

        ArrayList<Long> ids = setIDs.get_Neo4j_Ids();

        if (DebugInfo == true) {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getLinkFrom_Or_ToSET getFromInsteadOfTo: " + getFromInsteadOfTo + " input IDs: " + ids);
        }

        ArrayList<Long> tmpvec = new ArrayList<Long>();

        int loopIndex = 0;
        int maxIndex = ids.size();

        if (maxIndex == 0) {
            return APISucc;
        }

        while (loopIndex < maxIndex) {

            ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex, Configs.MAX_IDS_PER_QUERY, ids);
            loopIndex += subSetofIds.size();
            if (subSetofIds.size() == 0) {
                break;
            }
            String query = "";
            if (subSetofIds.size() == 1) {
                query = " MATCH (n" + getCommonLabelStr() + "{" + prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0)) + "}) "
                        + (getFromInsteadOfTo ? " -[:RELATION]-> " : " <-[:RELATION]- ") + " (m:" + Configs.Neo4j_Key_For_Type_AttributeStr + ") "
                        +" WHERE NOT( \""+Configs.Labels.PrimitiveClass.name() +"\" IN labels(n))"
                        + " RETURN m." + Configs.Neo4j_Key_For_Neo4j_Id + " as " + Configs.Neo4j_Key_For_Neo4j_Id + " ";
            } else {
                query = " MATCH (n" + getCommonLabelStr() + ") "
                        + (getFromInsteadOfTo ? " -[:RELATION]-> " : " <-[:RELATION]- ") + " (m:" + Configs.Neo4j_Key_For_Type_AttributeStr + ") "
                        + " WHERE n." + Configs.Neo4j_Key_For_Neo4j_Id + " IN " + subSetofIds.toString()
                        +" AND NOT( \""+Configs.Labels.PrimitiveClass.name() +"\" IN labels(n))"
                        + " RETURN m." + Configs.Neo4j_Key_For_Neo4j_Id + " as " + Configs.Neo4j_Key_For_Neo4j_Id + " ";

            }

            //do the job do not return
            Result res = graphDb.execute(query);
            try {
                while (res.hasNext()) {

                    long val = getNeo4jIdFromObject(res.next().get(Configs.Neo4j_Key_For_Neo4j_Id));

                    if (val > 0) {
                        tmpvec.add(val);
                    } else {
                        if (Configs.boolDebugInfo) {
                            throw new UnsupportedOperationException();
                        }
                    }
                }

            } catch (Exception ex) {
                utils.handleException(ex);
                return APIFail;
            } finally {
                res.close();
                res = null;
            }
        }

        for (Long l : tmpvec) {
            retSysids.set_putNeo4j_Id(l);
        }

        if (DebugInfo == true) {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getLinkFrom_Or_ToSET getFromInsteadOfTo: " + getFromInsteadOfTo + " retSysids size: " + retSysids.get_Neo4j_Ids().size());
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getLinkFrom_Or_ToSET getFromInsteadOfTo: " + getFromInsteadOfTo + " retSysids: " + retSysids.get_Neo4j_Ids());
        }

        return APISucc;
    }
    
    int getInherLinkFrom_Or_To_SET(PQI_Set setIDs, PQI_Set retSysids, boolean getFromInsteadOfTo) {

        if (CheckAllExist(setIDs) == APIFail) {
            return APIFail;
        }

        ArrayList<Long> ids = setIDs.get_Neo4j_Ids();

        if (DebugInfo == true) {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getInherLinkFrom_Or_To_SET getFromInsteadOfTo: " + getFromInsteadOfTo + " input IDs: " + ids);
        }

        ArrayList<Long> tmpvec = new ArrayList<Long>();

        int loopIndex = 0;
        int maxIndex = ids.size();

        if (maxIndex == 0) {
            return APISucc;
        }

        while (loopIndex < maxIndex) {

            ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex, Configs.MAX_IDS_PER_QUERY, ids);
            loopIndex += subSetofIds.size();
            if (subSetofIds.size() == 0) {
                break;
            }
            String query = "";

            if (subSetofIds.size() == 1) {
                query = " MATCH (n" + getCommonLabelStr() + "{" + prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0)) + "}) "
                        + (getFromInsteadOfTo ? " -[:ISA*0..]->(k)-[:RELATION]-> " : " -[:ISA*0..]->(k)<-[:RELATION]- ") + " (m:" + Configs.Neo4j_Key_For_Type_AttributeStr + ") "
                        +" WHERE NOT( \""+Configs.Labels.PrimitiveClass.name() +"\" IN labels(n))"
                        + " RETURN m." + Configs.Neo4j_Key_For_Neo4j_Id + " as " + Configs.Neo4j_Key_For_Neo4j_Id + " ";
            } else {
                query = " MATCH (n" + getCommonLabelStr() + ") "
                        + (getFromInsteadOfTo ? " -[:ISA*0..]->(k)-[:RELATION]-> " : " -[:ISA*0..]->(k)<-[:RELATION]- ") + " (m:" + Configs.Neo4j_Key_For_Type_AttributeStr + ") "
                        + " WHERE n." + Configs.Neo4j_Key_For_Neo4j_Id + " IN " + subSetofIds.toString()
                        +" AND NOT( \""+Configs.Labels.PrimitiveClass.name() +"\" IN labels(n))"
                        + " RETURN m." + Configs.Neo4j_Key_For_Neo4j_Id + " as " + Configs.Neo4j_Key_For_Neo4j_Id + " ";
            }

            //do the job do not return
            Result res = graphDb.execute(query);
            try {
                while (res.hasNext()) {
                    long val = getNeo4jIdFromObject(res.next().get(Configs.Neo4j_Key_For_Neo4j_Id));

                    if (val > 0) {
                        tmpvec.add(val);
                    } else {
                        if (Configs.boolDebugInfo) {
                            throw new UnsupportedOperationException();
                        }
                    }
                }

            } catch (Exception ex) {
                utils.handleException(ex);
                return APIFail;
            } finally {
                res.close();
                res = null;
            }
        }

        for (Long l : tmpvec) {
            retSysids.set_putNeo4j_Id(l);
        }

        if (DebugInfo == true) {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getInherLinkFrom_Or_To_SET getFromInsteadOfTo: " + getFromInsteadOfTo + " retSysids size: " + retSysids.get_Neo4j_Ids().size());
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getInherLinkFrom_Or_To_SET getFromInsteadOfTo: " + getFromInsteadOfTo + " retSysids: " + retSysids.get_Neo4j_Ids());
        }

        return APISucc;
    }

    int getFrom_Or_To_ValueSET(PQI_Set setIDs, PQI_Set retSysids, boolean linksFromInsteadOfTo) {

        if (CheckAllExist(setIDs) == APIFail) {
            return APIFail;
        }

        ArrayList<Long> ids = setIDs.get_Neo4j_Ids();

        if (DebugInfo == true) {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getFrom_Or_To_ValueSET linksFromInsteadOfTo: " + linksFromInsteadOfTo + " input IDs: " + ids);
        }
        ArrayList<Long> tempRetIds = new ArrayList<Long>();

        int loopIndex = 0;
        int maxIndex = ids.size();

        if (maxIndex == 0) {
            return APISucc;
        }

        while (loopIndex < maxIndex) {
            ArrayList<Long> subSetIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex, Configs.MAX_IDS_PER_QUERY, ids);
            loopIndex += subSetIds.size();

            if (subSetIds.size() == 0) {
                break;
            }

            String query = "";

            if (subSetIds.size() == 1) {
                query = " MATCH (n" + getCommonLabelStr() + "{" + prepareNeo4jIdPropertyFilterForCypher(subSetIds.get(0)) + "}), (n:" + Configs.Neo4j_Key_For_Type_AttributeStr + ") " +
                        (linksFromInsteadOfTo ? " <-[:RELATION]- " : " -[:RELATION]-> ") + " (m) " +
                        " RETURN m." + Configs.Neo4j_Key_For_Neo4j_Id + " as " + Configs.Neo4j_Key_For_Neo4j_Id + " ";
            } else {
                query = " MATCH (n" + getCommonLabelStr() + "), (n:" + Configs.Neo4j_Key_For_Type_AttributeStr + ") "+
                        (linksFromInsteadOfTo ? " <-[:RELATION]- " : " -[:RELATION]-> ") + " (m) " +
                        " WHERE n." + Configs.Neo4j_Key_For_Neo4j_Id + " IN " + subSetIds.toString() + " " +
                        " RETURN m." + Configs.Neo4j_Key_For_Neo4j_Id + " as " + Configs.Neo4j_Key_For_Neo4j_Id + " ";
            }

            Result res = this.graphDb.execute(query);

            try {
                while (res.hasNext()) {
                    
                    long val = getNeo4jIdFromObject(res.next().get(Configs.Neo4j_Key_For_Neo4j_Id));

                    if (val > 0) {
                        tempRetIds.add(val);
                    } else {
                        if (Configs.boolDebugInfo) {
                            throw new UnsupportedOperationException();
                        }
                    }
                }

            } catch (Exception ex) {
                utils.handleException(ex);
                return APIFail;
            } finally {
                res.close();
                res = null;
            }
        }

        for (Long l : tempRetIds) {
            retSysids.set_putNeo4j_Id(l);
        }

        if (DebugInfo == true) {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getFrom_Or_To_ValueSET linksFromInsteadOfTo: " + linksFromInsteadOfTo + " retSysids size: " + retSysids.get_Neo4j_Ids().size());
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getFrom_Or_To_ValueSET linksFromInsteadOfTo: " + linksFromInsteadOfTo + " retSysids: " + retSysids.get_Neo4j_Ids());
        }

        return APISucc;
    }
    
    int getFrom_Or_To_NodeSET(PQI_Set setIDs, PQI_Set retSysids, boolean getFromInsteadOfTo) {
        
        if (CheckAllExist(setIDs) == APIFail) {
            return APIFail;
        }

        ArrayList<Long> ids = setIDs.get_Neo4j_Ids();

        if (DebugInfo == true) {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getFrom_Or_To_NodeSET getFromInsteadOfTo: " + getFromInsteadOfTo + " input IDs: " + ids);
        }

        ArrayList<Long> tmpvec = new ArrayList<Long>();

        int loopIndex = 0;
        int maxIndex = ids.size();

        if (maxIndex == 0) {
            return APISucc;
        }

        while (loopIndex < maxIndex) {

            ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex, Configs.MAX_IDS_PER_QUERY, ids);
            loopIndex += subSetofIds.size();
            if (subSetofIds.size() == 0) {
                break;
            }
            String query = "";
            if (subSetofIds.size() == 1) {
                query = " MATCH (n" + getCommonLabelStr() + "{" + prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0)) + "}) "
                        + (getFromInsteadOfTo ? " <-[:RELATION]- " : " -[:RELATION]-> ") + " (k:" + Configs.Neo4j_Key_For_Type_AttributeStr + ") "
                        + (getFromInsteadOfTo ? " <-[:RELATION]- " : " -[:RELATION]-> ") + " (m) "
                        +" WHERE NOT( \""+Configs.Labels.PrimitiveClass.name() +"\" IN labels(n))"
                        + " RETURN m." + Configs.Neo4j_Key_For_Neo4j_Id + " as " + Configs.Neo4j_Key_For_Neo4j_Id + " ";
            } else {
                query = " MATCH (n" + getCommonLabelStr() + ") "
                        + (getFromInsteadOfTo ? " <-[:RELATION]- " : " -[:RELATION]-> ") + " (k:" + Configs.Neo4j_Key_For_Type_AttributeStr + ") "
                        + (getFromInsteadOfTo ? " <-[:RELATION]- " : " -[:RELATION]-> ") + " (m) "
                        + " WHERE n." + Configs.Neo4j_Key_For_Neo4j_Id + " IN " + subSetofIds.toString()
                        + " AND NOT( \""+Configs.Labels.PrimitiveClass.name() +"\" IN labels(n))"
                        + " RETURN m." + Configs.Neo4j_Key_For_Neo4j_Id + " as " + Configs.Neo4j_Key_For_Neo4j_Id + " ";
            }

            //do the job do not return
            Result res = graphDb.execute(query);
            try {
                while (res.hasNext()) {

                    long val = getNeo4jIdFromObject(res.next().get(Configs.Neo4j_Key_For_Neo4j_Id));

                    if (val > 0) {
                        tmpvec.add(val);
                    } else {
                        if (Configs.boolDebugInfo) {
                            throw new UnsupportedOperationException();
                        }
                    }
                }

            } catch (Exception ex) {
                utils.handleException(ex);
                return APIFail;
            } finally {
                res.close();
                res = null;
            }
        }

        for (Long l : tmpvec) {
            retSysids.set_putNeo4j_Id(l);
        }

        if (DebugInfo == true) {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getFrom_Or_To_NodeSET getFromInsteadOfTo: " + getFromInsteadOfTo + " retSysids size: " + retSysids.get_Neo4j_Ids().size());
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getFrom_Or_To_NodeSET getFromInsteadOfTo: " + getFromInsteadOfTo + " retSysids: " + retSysids.get_Neo4j_Ids());
        }

        return APISucc;
    }

    int getAllSuper_or_AllSubClassesSET(PQI_Set setIDs, PQI_Set retSysids, boolean getSuperInsteadOfSubclasses) {

        if (CheckAllExist(setIDs) == APIFail) {
            return APIFail;
        }

        ArrayList<Long> ids = setIDs.get_Neo4j_Ids();

        if (DebugInfo == true) {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getAllSuper_or_AllSubClassesSET getSuperInsteadOfSubclasses: " + getSuperInsteadOfSubclasses + " input IDs: " + ids);
        }

        ArrayList<Long> tmpvec = new ArrayList<Long>();

        int loopIndex = 0;
        int maxIndex = ids.size();

        if (maxIndex == 0) {
            return APISucc;
        }

        while (loopIndex < maxIndex) {

            ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex, Configs.MAX_IDS_PER_QUERY, ids);
            loopIndex += subSetofIds.size();
            if (subSetofIds.size() == 0) {
                break;
            }
            String query = "";
            if (subSetofIds.size() == 1) {
                query = "MATCH (n" + getCommonLabelStr() + "{" + prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0)) + "}) "
                        + (getSuperInsteadOfSubclasses ? " -[:ISA*1..]-> " : " <-[:ISA*1..]- ") + " (m) "
                        + " RETURN m." + Configs.Neo4j_Key_For_Neo4j_Id + " as " + Configs.Neo4j_Key_For_Neo4j_Id + " ";
            } else {
                query = " MATCH (n" + getCommonLabelStr() + ") "
                        + (getSuperInsteadOfSubclasses ? " -[:ISA*1..]-> " : " <-[:ISA*1..]- ") + " (m) "
                        + " WHERE n." + Configs.Neo4j_Key_For_Neo4j_Id + " IN " + subSetofIds.toString()
                        + " RETURN m." + Configs.Neo4j_Key_For_Neo4j_Id + " as " + Configs.Neo4j_Key_For_Neo4j_Id + " ";
            }

            //do the job do not return
            Result res = graphDb.execute(query);
            try {
                while (res.hasNext()) {

                    long val = getNeo4jIdFromObject(res.next().get(Configs.Neo4j_Key_For_Neo4j_Id));

                    if (val > 0) {
                        tmpvec.add(val);
                    } else {
                        if (Configs.boolDebugInfo) {
                            throw new UnsupportedOperationException();
                        }
                    }
                }

            } catch (Exception ex) {
                utils.handleException(ex);
                return APIFail;
            } finally {
                res.close();
                res = null;
            }
        }

        for (Long l : tmpvec) {
            retSysids.set_putNeo4j_Id(l);
        }

        if (DebugInfo == true) {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getAllSuper_or_AllSubClassesSET getSuperInsteadOfSubclasses: " + getSuperInsteadOfSubclasses + " retSysids size: " + retSysids.get_Neo4j_Ids().size());
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getAllSuper_or_AllSubClassesSET getSuperInsteadOfSubclasses: " + getSuperInsteadOfSubclasses + " retSysids: " + retSysids.get_Neo4j_Ids());
        }

        return APISucc;

    }

    int getAll_Or_Simple_InstancesSET(PQI_Set setIDs, PQI_Set retSysids, boolean getAllInsteadOfSimple) {

        ArrayList<Long> ids = setIDs.get_Neo4j_Ids();

        if (DebugInfo == true) {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getAll_Or_Simple_InstancesSET getAllInsteadOfSimple: " + getAllInsteadOfSimple + " input IDs: " + ids);
        }

        ArrayList<Long> tmpvec = new ArrayList<Long>();

        int loopIndex = 0;
        int maxIndex = ids.size();

        if (maxIndex == 0) {
            return APISucc;
        }

        while (loopIndex < maxIndex) {

            ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex, Configs.MAX_IDS_PER_QUERY, ids);
            loopIndex += subSetofIds.size();
            if (subSetofIds.size() == 0) {
                break;
            }
            String query = "";
            if (subSetofIds.size() == 1) {
                query = " MATCH(n" + getCommonLabelStr() + "{" + prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0)) + "}) "
                        + (getAllInsteadOfSimple ? " <-[:ISA*0..]-(k)<-[:INSTANCEOF]- " : " <-[:INSTANCEOF]- ") + " (m) "
                        + " RETURN m." + Configs.Neo4j_Key_For_Neo4j_Id + " as " + Configs.Neo4j_Key_For_Neo4j_Id + " ";
            } else {
                query = " MATCH (n" + getCommonLabelStr() + ") "
                        + (getAllInsteadOfSimple ? " <-[:ISA*0..]-(k)<-[:INSTANCEOF]- " : " <-[:INSTANCEOF]- ") + " (m) "
                        + " WHERE n." + Configs.Neo4j_Key_For_Neo4j_Id + " IN " + subSetofIds.toString()
                        + " RETURN m." + Configs.Neo4j_Key_For_Neo4j_Id + " as " + Configs.Neo4j_Key_For_Neo4j_Id + " ";
            }

            //do the job do not return
            Result res = graphDb.execute(query);
            try {
                while (res.hasNext()) {

                    long val = getNeo4jIdFromObject(res.next().get(Configs.Neo4j_Key_For_Neo4j_Id));

                    if (val > 0) {
                        tmpvec.add(val);
                    } else {
                        if (Configs.boolDebugInfo) {
                            throw new UnsupportedOperationException();
                        }
                    }
                }

            } catch (Exception ex) {
                utils.handleException(ex);
                return APIFail;
            } finally {
                res.close();
                res = null;
            }
        }

        for (Long l : tmpvec) {
            retSysids.set_putNeo4j_Id(l);
        }

        if (DebugInfo == true) {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getAll_Or_Simple_InstancesSET getAllInsteadOfSimple: " + getAllInsteadOfSimple + " retSysids size: " + retSysids.get_Neo4j_Ids().size());
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getAll_Or_Simple_InstancesSET getAllInsteadOfSimple: " + getAllInsteadOfSimple + " retSysids: " + retSysids.get_Neo4j_Ids());
        }

        return APISucc;
    }
    
    int getAll_Or_Simple_ClassesSET(PQI_Set setIDs, PQI_Set retSysids, boolean getAllInsteadOfSimple) {

        if (CheckAllExist(setIDs) == APIFail) {
            return APIFail;
        }

        ArrayList<Long> ids = setIDs.get_Neo4j_Ids();

        if (DebugInfo == true) {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** getAll_Or_Simple_ClassesSET getAllInsteadOfSimple: " + getAllInsteadOfSimple + " input IDs: " + ids);
        }

        ArrayList<Long> tmpvec = new ArrayList<Long>();

        int loopIndex = 0;
        int maxIndex = ids.size();

        if (maxIndex == 0) {
            return APISucc;
        }

        while (loopIndex < maxIndex) {

            ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex, Configs.MAX_IDS_PER_QUERY, ids);
            loopIndex += subSetofIds.size();
            if (subSetofIds.size() == 0) {
                break;
            }
            String query = "";

            if (subSetofIds.size() == 1) {
                query = " MATCH(n" + getCommonLabelStr() + "{" + prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0)) + "}) "
                        + (getAllInsteadOfSimple ? " -[:INSTANCEOF]->(k)-[:ISA*0..]-> " : " -[:INSTANCEOF]-> ") + " (m) "
                        + " RETURN m." + Configs.Neo4j_Key_For_Neo4j_Id + " as " + Configs.Neo4j_Key_For_Neo4j_Id + " ";
            } else {
                query = " MATCH (n" + getCommonLabelStr() + ") "
                        + (getAllInsteadOfSimple ? " -[:INSTANCEOF]->(k)-[:ISA*0..]-> " : " -[:INSTANCEOF]-> ") + " (m) "
                        + " WHERE n." + Configs.Neo4j_Key_For_Neo4j_Id + " IN " + subSetofIds.toString()
                        + " RETURN m." + Configs.Neo4j_Key_For_Neo4j_Id + " as " + Configs.Neo4j_Key_For_Neo4j_Id + " ";
            }

            //do the job do not return
            Result res = graphDb.execute(query);
            try {
                while (res.hasNext()) {

                    long val = getNeo4jIdFromObject(res.next().get(Configs.Neo4j_Key_For_Neo4j_Id));

                    if (val > 0) {
                        tmpvec.add(val);
                    } else {
                        if (Configs.boolDebugInfo) {
                            throw new UnsupportedOperationException();
                        }
                    }
                }

            } catch (Exception ex) {
                utils.handleException(ex);
                return APIFail;
            } finally {
                res.close();
                res = null;
            }
        }

        for (Long l : tmpvec) {
            retSysids.set_putNeo4j_Id(l);
        }

        if (DebugInfo == true) {
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** etAll_Or_Simple_ClassesSET getAllInsteadOfSimple: " + getAllInsteadOfSimple + " retSysids size: " + retSysids.get_Neo4j_Ids().size());
            Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, "*** etAll_Or_Simple_ClassesSET getAllInsteadOfSimple: " + getAllInsteadOfSimple + " retSysids: " + retSysids.get_Neo4j_Ids());
        }

        return APISucc;
    }

    int unnamed_id(StringObject lname, PrimitiveObject_Long retVal){
        /*
    int Loginam::unnamed_id()
{
	int id;
#ifndef BIG_ENDIAN
	if(i[0] == 'Labe')
#else
	if(i[0] == 'ebaL')
#endif
	{
		if(c[4] == 'l')
		{
			id = strtol(&c[5],0,16);
			if(IS_UNNAMED(id) && (id > UNNAMED_BIT))
			{
				return(id);
			}
		}
	}
	return(0);
}
    */
        if(lname.value!=null && lname.value.length()>0){
            retVal.setValue(0);
            return APIFail;
        }
        
        if(lname.value.matches(Configs.regExForUnNamed)){
            //here the id returned is the hexconversion to decimal 
            //of the substring after prefix "Label"
            //but the most safe case here is to ask the database for the id
            retVal.setValue(getClassId(lname.getValue()));
            if(retVal.getValue() == APIFail){
                retVal.setValue(0);
                return APIFail;
            }

            return APISucc;
        }
        return(APIFail);
    }
    
    Node GetTelosObjectAttributeNode(){
                
        // ---------------------------------------------------------------------------
        // semanticChecker::setUpAttributeCategory --
        // Declare the current link object as an instance of attribute category
        // ---------------------------------------------------------------------------
        //int	semanticChecker::setUpAttributeCategory()
        //{

        //	if ( resolvedCats.insertRef( SYSID(ATTRIBUTE_CLASS_ID)) == ERROR) {
        //		globalError.putMessage( ERROR_RESOLVED_CAT);
        //		return( ERROR);
        //	}

        //	return( OK);
        //}
        
        String query ="MATCH(n"+getCommonLabelStr()+"{"+prepareLogicalNameForCypher(Configs.TelosObjectAttributeFromClsname)+"}) -[:RELATION] -> "+
                "(m"+getCommonLabelStr()+"{"+prepareLogicalNameForCypher(Configs.TelosObjectAttributeFromLinkname)+"}) RETURN m AS node ";
        
        Result res = this.graphDb.execute(query);
         
        while (res.hasNext()) {
            return (Node)res.next().get("node");            
        }
     
        return null;
    }
    
    //used in order to get next Id of the specific thesaurus and 
    //increment the thesaurus index by 1. This ThesaurusId will
    //be used for uri construction.
    long getNextThesaurusId(String targetThesaurus){
        
        String query = Configs.getNextThesaurusReferenceIdQuery.replace("%THES%",targetThesaurus.toUpperCase());
        
        Result res = graphDb.execute(query);
        try {
            while (res.hasNext()) {
                Object oval = res.next().get("maxId");
                long val=-1;
                if(oval instanceof Integer){
                    val = (int) oval;
                }
                else if(oval instanceof Long){
                    val = (long) oval;
                }
                //long val = getNeo4jIdFromObject();

                if (val > 0) {
                    return val;
                } else {
                    if (Configs.boolDebugInfo) {
                        System.out.println(query + "\r\nval: " + val);
                        throw new UnsupportedOperationException();
                    }
                }
            }
            
        } catch (Exception ex) {
            utils.handleException(ex);
            return APIFail;
        } finally {
            res.close();
            res = null;
        }
        
        return APIFail;
    }
    
    //static long nextSystemNumber = 20000;
    long getNextNeo4jId(){
        //return nextSystemNumber++;
        //for(IndexDefinition def : graphDb.schema().getIndexes(Configs.Labels.Common)){
            
        //}
        //String query = "MATCH(n"+getCommonLabelStr()+") RETURN max(n."+Configs.Neo4j_Key_For_Neo4j_Id+") as maxId ";
        
        //static final String getNextSystemNumberQuery = "MATCH(n:Common{Neo4j_Id:4}) set n.MaxNeo4j_Id=n.MaxNeo4j_Id+1 return n.MaxNeo4j_Id as maxId";
        String query = Configs.getNextSystemNumberQuery;
        
        Result res = graphDb.execute(query);
        try {
            while (res.hasNext()) {
                Object oval = res.next().get("maxId");
                long val=-1;
                if(oval instanceof Integer){
                    val = (int) oval;
                }
                else if(oval instanceof Long){
                    val = (long) oval;
                }
                //long val = getNeo4jIdFromObject();

                if (val > 0) {
                    return val;
                } else {
                    if (Configs.boolDebugInfo) {
                        throw new UnsupportedOperationException();
                    }
                }
            }
            
        } catch (Exception ex) {
            utils.handleException(ex);
            return APIFail;
        } finally {
            res.close();
            res = null;
        }
        
        return APIFail;
    }
    
    
    ArrayList<Node> getNeo4jNodesByNeo4jIds(ArrayList<Long> neo4jIds){
        ArrayList<Node> returnVec = new ArrayList<Node>();
        
        int loopIndex = 0;
        int maxIndex = neo4jIds.size();

        if(maxIndex==0){
            return returnVec;
        }
        
        while (loopIndex < maxIndex) {

            ArrayList<Long> subSetofIds = utils.collectSequenctiallyAsubsetOfValues(loopIndex,Configs.MAX_IDS_PER_QUERY, neo4jIds);
            loopIndex += subSetofIds.size();
            if(subSetofIds.isEmpty()){
                break;
            }
            String query = "";
            
            if(subSetofIds.size()==1){
                query = "MATCH(n"+getCommonLabelStr()+"{"+prepareNeo4jIdPropertyFilterForCypher(subSetofIds.get(0))+"}) RETURN n";
                
            }
            else{
                query = " Match (n"+getCommonLabelStr()+") WHERE n."+Configs.Neo4j_Key_For_Neo4j_Id+" IN " + subSetofIds.toString() + " "+
                        " RETURN n ";
            }
            
            Result res = this.graphDb.execute(query);
            try {
                while (res.hasNext()) {

                    Map<String, Object> row = res.next();
                    returnVec.add((Node) row.get("n"));
                }
            } 
            catch(Exception ex){
                utils.handleException(ex);
                return null;
            }
            finally {
                res.close();
                res = null;
            }
        }
        
        
        return returnVec;
    }

    String findLogicalNameByThesaurusReferenceId(String thesaurusName, long refId) {
        
        String returnVal = "";
        String query = "Match(n:"+Configs.CommonLabelName+"{"+Configs.Neo4j_Key_For_Logicalname+":\"Thesaurus`"+thesaurusName.toUpperCase()+"\"}) "+ 
                " <-[:RELATION]-(link:"+Configs.CommonLabelName+"{"+Configs.Neo4j_Key_For_Logicalname+":\""+thesaurusName.toUpperCase()+"`of_thesaurus\"})<-[:RELATION]-(m)<-[:ISA*0..]-(k)<-[:INSTANCEOF*0..1]-(p:"+Configs.CommonLabelName+"{"+Configs.Neo4j_Key_For_ThesaurusReferenceId+":"+refId+"}) "+
                " return p."+Configs.Neo4j_Key_For_Logicalname+" as lname ";
        
        //System.out.println("resetCounter_For_ThesaurusReferenceId\r\n============================\r\n"+query);
        
        Result res = null;
        try{
            res = graphDb.execute(query);
            if(res!=null){
                while (res.hasNext()) {

                    Map<String, Object> row = res.next();
                    returnVal = (String) row.get("lname");
                }
            }
        }
        catch(Exception ex){
            utils.handleException(ex);
        }
        if (res == null) {
            return "";
        }
        else{
            res.close();                
        }
        return returnVal;
    }
    
    
    boolean IsThesaurusReferenceIdAssigned(String thesaurusCheck, long refIdToCheck){
        boolean returnVal = false;
        String labelForHowmany = "howmanyFound";
        String query = "Match(n:"+Configs.CommonLabelName+"{"+Configs.Neo4j_Key_For_Logicalname+":\"Thesaurus`"+thesaurusCheck.toUpperCase()+"\"}) "+ 
                " <-[:RELATION]-(link:"+Configs.CommonLabelName+"{"+Configs.Neo4j_Key_For_Logicalname+":\""+thesaurusCheck.toUpperCase()+"`of_thesaurus\"})<-[:RELATION]-(m)<-[:ISA*0..]-(k)<-[:INSTANCEOF*0..1]-(p:"+Configs.CommonLabelName+"{"+Configs.Neo4j_Key_For_ThesaurusReferenceId+":"+refIdToCheck+"}) "+
                " return count(p) as "+labelForHowmany+" ";
        
        //System.out.println("resetCounter_For_ThesaurusReferenceId\r\n============================\r\n"+query);
        
        Result res = null;
        try{
            res = graphDb.execute(query);
            if(res!=null){
                while (res.hasNext()) {

                    Map<String, Object> row = res.next();
                    if(row.get(labelForHowmany) instanceof Integer){
                        int howmany = (int) row.get("howmanyFound");
                        if(howmany>0){
                            return true;
                        }
                    }
                    else if (row.get(labelForHowmany) instanceof Long) {
                        long howmany = (long) row.get("howmanyFound");
                        if (howmany > 0) {
                            return true;
                        }
                    }
                }
            }
        }
        catch(Exception ex){
            utils.handleException(ex);
        }
        finally{
            if (res != null) {
                res.close();                
            }
        }
        return returnVal;
    }

    boolean DeleteEmptyThesaurusModel(String selectedThesaurus, ArrayList<Long> excludeList,StringObject errorMsg){
        String thesNametoUpper = selectedThesaurus.toUpperCase();        
        String thesNametoLower = selectedThesaurus.toLowerCase();
        String thesNametoCamel = thesNametoUpper.charAt(0)+thesNametoLower.substring(1);
        
        int maxDepth = 10;
        //MATCH p= (n:Common{Logicalname:\"%GROUPNAME%\"})-[*0.."+maxDepth+"]-(m)  
        //WHERE ALL(c IN nodes(p) WHERE not c:Generic) 
        //return  DISTINCT m.Neo4j_Id, m.Logicalname order by m.Neo4j_Id "
        String baseRetrievalQuery ="MATCH p= (n:"+Configs.CommonLabelName+"{"+Configs.Neo4j_Key_For_Logicalname+":\"%GROUPNAME%\"})-[*0.."+maxDepth+"]-(m) "+
                " WHERE ALL(c IN nodes(p) WHERE not c:"+Configs.GenericLabelName+") "+
                " RETURN DISTINCT m."+Configs.Neo4j_Key_For_Neo4j_Id+" as "+Configs.Neo4j_Key_For_Neo4j_Id+ " , m."+Configs.Neo4j_Key_For_Logicalname+
                " ORDER BY "+Configs.Neo4j_Key_For_Neo4j_Id;
                
        ArrayList<String> groupNames = new ArrayList<>();
        groupNames.add("%THES%".replace("%THES%", thesNametoUpper));
        groupNames.add("ListNonTransl%THES%".replace("%THES%", thesNametoUpper));
        groupNames.add("All%THES%Fac".replace("%THES%", thesNametoUpper));
        groupNames.add("All%Thes%Hier".replace("%Thes%", thesNametoCamel));
        groupNames.add("BT_according_to_%thes%".replace("%thes%", thesNametoLower));
        groupNames.add("NT_according_to_%thes%".replace("%thes%", thesNametoLower));
        groupNames.add("ListNonDescrby%THES%".replace("%THES%", thesNametoUpper));
        groupNames.add("%THES%_SimpleUser".replace("%THES%", thesNametoUpper));
        groupNames.add("%THES%_ExpertUser".replace("%THES%", thesNametoUpper));
        groupNames.add("%THES%-1-SUTMSEFArgQuery".replace("%THES%", thesNametoUpper));
        groupNames.add("Thesaurus`%THES%".replace("%THES%", thesNametoUpper));
        groupNames.add("%THES%_EUTMSEFArgDescription".replace("%THES%", thesNametoUpper));
        
        
       
        
        //detach all created by links and modified by links
        String removeThesCreatedByLinkInstances = "%thes%_created_by".replace("%thes%", thesNametoLower);
        String removeThesCreatedOnLinkInstances = "%thes%_created".replace("%thes%", thesNametoLower);
        
        String removeThesModyfiedByLinkInstances = "%thes%_modified_by".replace("%thes%", thesNametoLower);
        String removeThesModyfiedOnLinkInstances = "%thes%_modified".replace("%thes%", thesNametoLower);
        
        
        String getLinkInstancesQuery = "MATCH(n"+getCommonLabelStr()+") <-[:"+Configs.Rels.INSTANCEOF.name()+"]-(m) WHERE n."+Configs.Neo4j_Key_For_Logicalname +" in [ \"" + 
                removeThesCreatedByLinkInstances+"\",\""+
                removeThesCreatedOnLinkInstances+"\",\""+
                removeThesModyfiedByLinkInstances+"\",\""+
                removeThesModyfiedOnLinkInstances+"\"] "+
                " DETACH DELETE m ";   
        try {
            this.graphDb.execute(getLinkInstancesQuery);
            
        } catch (Exception ex) {
            errorMsg.setValue("Error_WHEN_TRYING_TODETACH_CREATED_AND_MODIFIED_LINKS");
            utils.handleException(ex);
            return false;
        } 
        
        
        String ThesaurusEditor = "%THES%Editor".replace("%THES%", thesNametoUpper);
        
        String detachNonReferecedThesaurusEditorInstances = "MATCH(n"+getCommonLabelStr() +"{"+ prepareLogicalNameForCypher(ThesaurusEditor)+"}) "+
                "<-[:"+Configs.Rels.INSTANCEOF.name()+"]-(m) "+
                " WHERE NOT (m)<-[:"+Configs.Rels.RELATION.name()+"]-() "+
                " DETACH DELETE m";
        
        
        try {
            this.graphDb.execute(detachNonReferecedThesaurusEditorInstances);
            
        } catch (Exception ex) {
            errorMsg.setValue("Error_WHEN_TRYING_TODETACH_NOREFERENCED_INSTANCES_OF_"+ThesaurusEditor);
            utils.handleException(ex);
            return false;
        } 
        
        
        String detachSameNode = "MATCH (n"+getCommonLabelStr() +"{"+ prepareLogicalNameForCypher(ThesaurusEditor)+"}) DETACH DELETE n";
        try {
            this.graphDb.execute(detachSameNode);
            
        } catch (Exception ex) {
            errorMsg.setValue("Error_WHEN_TRYING_TODETACH_"+ThesaurusEditor);
            utils.handleException(ex);
            return false;
        } 
        
        String detachNonReferecedDateInstances = "MATCH(n"+getCommonLabelStr() +"{"+ prepareLogicalNameForCypher("Date")+"}) "+
                "<-[:"+Configs.Rels.INSTANCEOF.name()+"]-(m) "+
                " WHERE NOT (m)<-[:"+Configs.Rels.RELATION.name()+"]-() "+
                " DETACH DELETE m";
        try {
            this.graphDb.execute(detachNonReferecedDateInstances);
            
        } catch (Exception ex) {
            errorMsg.setValue("Error_WHEN_TRYING_TODETACH_NOREFERENCED_INSTANCES_OF_"+"Date");
            utils.handleException(ex);
            return false;
        } 
        //detach delete all attributes that connect to date and editor
        /*<-[:"+Configs.Rels.INSTANCEOF.name()+"*0..1]- (m) ";
        getInstancesQuery += " Return m."+Configs.Neo4j_Key_For_Neo4j_Id +" as "+Configs.Neo4j_Key_For_Neo4j_Id+ ", m."+Configs.Neo4j_Key_For_Logicalname +" as "+Configs.Neo4j_Key_For_Logicalname;
        
        
        ArrayList<Long> instances = new ArrayList<>();
        Identifier from = new Identifier();
        
        Result instanceRels = null;    
        try {
            
            instanceRels = this.graphDb.execute(getInstancesQuery);
            while (instanceRels.hasNext()) {

                Map<String, Object> row = instanceRels.next(); 

                long tempval = getNeo4jIdFromObject(row.get(Configs.Neo4j_Key_For_Neo4j_Id));
                String lname = (String)row.get(Configs.Neo4j_Key_For_Logicalname);
                if (tempval >= 0 ) { 
                    if(lname.equals(removeThes1EditorInstances)){
                        from.setValue(tempval);
                    }
                    else{
                        instances.add(tempval);
                    }

                } else {
                    errorMsg.setValue("NEGATIVE_ID_VALUE_RETURNED_ATDELETEEDITOR_INSTANCES_"+removeThes1EditorInstances);
                    return false;
                }
            }
        } catch (Exception ex) {
            errorMsg.setValue("Exception caught while retrieving editor instances of "+removeThes1EditorInstances);
            utils.handleException(ex);
            return false;
        } finally {
            instanceRels.close();
            instanceRels = null;
        }
        
        if(from.getSysid()<=0){
            errorMsg.setValue("Could not retreve id of "+removeThes1EditorInstances);
            return false;
        }
        for(Long l : instances){
            Identifier to = new Identifier(l);
            if(Q.Modify(QClass.ModifyRelation.SIS_API_INSTOF, QClass.ModifyOperation.SIS_API_REMOVE, from, to,null)==QClass.APIFail){
                errorMsg.setValue("ERROR_AT_DELETE_INSTANCEOF_EDITOR_RELATIONSHIP "+removeThes1EditorInstances);
                return false;
            }
        }*/
        
        ArrayList<Long> detachNodes = new ArrayList<>();
        
        for(String group : groupNames){
            String targetQuery = baseRetrievalQuery.replace("%GROUPNAME%", group);
            
            Result res = null;
            
            try {
                res = this.graphDb.execute(targetQuery);
                while (res.hasNext()) {

                    Map<String, Object> row = res.next(); 

                    long tempval = getNeo4jIdFromObject(row.get(Configs.Neo4j_Key_For_Neo4j_Id));

                    if (tempval >= 0 ) {  
                        detachNodes.add(tempval);
                    } else {
                        errorMsg.setValue("NEGATIVE_ID_VALUE_RETURNED_FOR_GROUP_"+group);
                        return false;
                    }
                }
            } catch (Exception ex) {
                errorMsg.setValue("Exception caught while retrieving relevant nodes of "+group);
                utils.handleException(ex);
                return false;
            } finally {
                res.close();
                res = null;
            }
            
        }
        
        List<Long> detachNodesList = detachNodes.stream().distinct().filter(e -> !excludeList.contains(e)).collect(Collectors.toList());
        
        String detachQuery = "MATCH (n:"+Configs.CommonLabelName+") WHERE n."+Configs.Neo4j_Key_For_Neo4j_Id +" IN " + detachNodesList.toString() +" DETACH DELETE n";
        Result res = null;
        try {
            res = this.graphDb.execute(detachQuery);
        } catch (QueryExecutionException ex) {
            errorMsg.setValue("Exception caught while detaching thesaurus specific nodes with query:\n"+detachQuery);
            utils.handleException(ex);
            return false;
        } finally {
            if(res!=null){
                res.close();
                res = null;
            }
        }
        return true;        
    }
    
    long setCurrentNodeByReferenceId(ArrayList<Long> CurrentNode_Ids_Stack, long referenceId, String targetThesaurus) {
        
        
        String returnVal = "";
        String query = "Match(n:"+Configs.CommonLabelName+"{"+Configs.Neo4j_Key_For_Logicalname+":\"Thesaurus`"+targetThesaurus.toUpperCase()+"\"}) "+ 
                " <-[:RELATION]-(link:"+Configs.CommonLabelName+"{"+Configs.Neo4j_Key_For_Logicalname+":\""+targetThesaurus.toUpperCase()+"`of_thesaurus\"})<-[:RELATION]-(m)<-[:ISA*0..]-(k)<-[:INSTANCEOF*0..1]-(p:"+Configs.CommonLabelName+"{"+Configs.Neo4j_Key_For_ThesaurusReferenceId+":"+referenceId+"}) "+
                " return p."+Configs.Neo4j_Key_For_Neo4j_Id+"  as id ";
        
                
           
        Result res = this.graphDb.execute(query);
        long newIdVal = APIFail;
        try {
            
            while (res.hasNext()) {

                Map<String, Object> row = res.next(); 
                
                newIdVal = getNeo4jIdFromObject(row.get("id"));
                
            }
            
        } catch (Exception ex) {
            utils.handleException(ex);
            return QClass.APIFail;
        } finally {
            res.close();
            res = null;
        }
        
        
        if(newIdVal>0){
            
            CurrentNode_Ids_Stack.clear();
            
            CurrentNode_Ids_Stack.add(newIdVal); 

            return newIdVal;
        }
        else{
            return APIFail;
        }
    }
    
}

