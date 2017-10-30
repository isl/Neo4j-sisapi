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

import neo4j_sisapi.Neo4j_Object.Levels;
import neo4j_sisapi.Neo4j_Object.Types;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * Used in order to define common behavior
 * 
 * @author Elias Tzortzakakis <tzortzak@ics.forth.gr>
 */
class Neo4j_Object_Base {
    
    long id;
    //boolean isNode_not_relationship;
    Types _type = Types.NOT_DEFINED;
    Levels _level = Levels.NOT_DEFINED; 
    boolean isOfUndefinedLevel = false;
    boolean isToken = false;
    boolean isSclass = false;
    boolean isM1class = false;
    boolean isM2class = false;
    boolean isM3class = false;
    boolean isM4class = false;
    
    Neo4j_Object_Base(long neoId, /*boolean isNode, */Types type, Levels level){
        id= neoId;
        //isNode_not_relationship = isNode;  
        _type = type;
        _level = level;
        if(level!=Levels.Token){
            if(level==Levels.S_Class){
                isSclass = true;
            }
            else if(level==Levels.M1_Class){
                isM1class = true;
            }
            else if(level==Levels.M2_Class){
                isM2class = true;
            }
            else if(level==Levels.M3_Class){
                isM3class = true;
            }
            else if(level==Levels.M4_Class){
                isM4class = true;
            }
            else{
                isOfUndefinedLevel = true;
            }
        }
        else{
            isToken = true;
        }
    }
    
    public long getNeo4jId(){
        return this.id;
    }
    
    public boolean isLevelSClass(){
        return isSclass;
    }
    public boolean isLevelM1Class(){
        return isM1class;
    }
    public boolean isLevelM2Class(){
        return isM2class;
    }
    public boolean isLevelM3Class(){
        return isM3class;
    }
    public boolean isLevelM4Class(){
        return isM4class;
    }
    public boolean isLevelToken(){
        return isToken;                
    }
    public boolean isLevelUndefined(){
        return isOfUndefinedLevel;
    }
    
    
    public long getTo(DBaccess db) {
        if(_type==Types.Type_Attribute){
            return db.DBACCESS_get_From_or_To(id,false);
        }
        else{
            return 0;
        }
    }
    
    int getInstOf(DBaccess db, PQI_Set set){
        //return set;
        return QClass.APISucc;
    }
     
    
    public long getFrom(DBaccess db) {
            return QClass.APISucc;
    }
    
    public int getLinkBy(DBaccess db, PQI_Set ret) {
        return QClass.APISucc;
    }
    public int getLink(DBaccess db, PQI_Set ret) {
        return QClass.APISucc;
    }

    
    @Override
    public String toString(){
        //return "Neo4jObject "+ this.id;
        //return this.getClass().getName()+" "+ this.id;
        return ""+ this.id;
    }
    
    
}
