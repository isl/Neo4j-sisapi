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


import neo4j_sisapi.Neo4j_Object;
import neo4j_sisapi.Neo4j_Object_Base;

/**
 * 
 * storage class for S_Class through M4_Class nodes 
 * These objects are present even in an empty data base 
 * 
 * Will not be supported
 * 
 * @author Elias Tzortzakakis <tzortzak@ics.forth.gr>
 */
class Neo4j_Object_sys_class extends Neo4j_Object_Base implements Neo4j_Object{
    
    
    Neo4j_Object_sys_class(long neoId, /*boolean isNode,*/ Neo4j_Object.Types type, Neo4j_Object.Levels level){
        
        super(neoId,/*isNode,*/type,level);     
    }


    
    /*
    @Override
    public boolean isLevelToken() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    */

    @Override
    public long getTo(DBaccess db) {
        return 0;
    }
    @Override
    public long getFrom(DBaccess db) {
        return 0;
    }
    
    
    
    /*@Override
    public int getLink(DBaccess db) {
        return APIFail;
    }*/

    @Override
    public boolean isTypeAttribute() {
        return _type==Types.Type_Attribute;
    }

    @Override
    public boolean isTypeIndividual() {
        return _type==Types.Type_Individual;    
    }

    @Override
    public boolean isTypeNotDefined() {
        return _type==Types.NOT_DEFINED;    
    }

    @Override
    public long getFromNode(DBaccess db, PQI_Set ret) {
        return QClass.APISucc;
    }

    @Override
    public long getToNode(DBaccess db, PQI_Set ret) {
        return QClass.APISucc;
    }
    @Override
    public int getInstOf(DBaccess db, PQI_Set set){
        return QClass.APISucc;
    }

}
