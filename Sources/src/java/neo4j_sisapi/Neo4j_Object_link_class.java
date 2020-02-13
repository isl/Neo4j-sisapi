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

/**
 * storage class for attribute classes simple through M4 level
 * Because the level of an attribute is the minimum of the levels 
 * of the "to" and the "from" object, the "to" cannot be a primitive 
 * value                                                       
 * 
 * Attributes cannot be referenced by other attributes, only the 
 * "to" values. Hence we need no "link_by" slot. Attributes on 
 * attributes are allowed however                             
 *
 * Attribute Class
 * 
 * @author Elias Tzortzakakis <tzortzak@ics.forth.gr>
 */
class Neo4j_Object_link_class extends Neo4j_Object_Base implements Neo4j_Object{
    
    
    Neo4j_Object_link_class(long neoId, /*boolean isNode,*/ Neo4j_Object.Types type, Neo4j_Object.Levels level){
        
        super(neoId,/*isNode,*/type,level);     
    }
    

    @Override
    public boolean isTypeAttribute() {
        return true;
    }

    @Override
    public boolean isTypeIndividual() {
        return false;
    }

    @Override
    public boolean isTypeNotDefined() {
        return false;
    }

    /*
    @Override
    public boolean isLevelToken() {
        return false;
    }
*/
    @Override
    public long getFrom(DBaccess db) {
            return db.DBACCESS_get_From_or_To(id,true);
    }
    @Override
    public long getTo(DBaccess db){
        /*
        inline SYSID link_class::getTo()
        {
        return(to);
        }
        */
        //return -1;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return db.DBACCESS_get_From_or_To(id,false);
    }

    @Override
    public long getFromNode(DBaccess db, PQI_Set ret) {
       return QClass.APISucc; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public long getToNode(DBaccess db, PQI_Set ret) {
        return QClass.APISucc;    
    }
    @Override
    public int getInstOf(DBaccess db, PQI_Set set){
        return db.DBACCESS_getInstOf(id, set);
    }
    
    @Override
    public int getLink(DBaccess db, PQI_Set ret){
        return db.DBACCESS_getLinkFrom(id, ret);
        
    }
}
