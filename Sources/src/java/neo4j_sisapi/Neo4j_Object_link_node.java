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
 * storage class for token level attributes
 * Because the level of an attribute is the minimum of the levels
 * of the "to" and the "from" object, this is the only storage 
 * class where primitive values can appear. This is why the "to"
 * value is stored as a "tagged" value. In case of a string, the
 * string is stored completely on extensions, starting always at
 * the first element of an extension                             
 * 
 * Attributes cannot be referenced by other attributes, only the 
 * "to" values. Hence we need no "link_by" slot. Attributes on 
 * attributes are allowed however                              
 *
 * Attribute Token
 * 
 * @author Elias Tzortzakakis <tzortzak@ics.forth.gr>
 */
class Neo4j_Object_link_node extends Neo4j_Object_Base implements Neo4j_Object{
    
    
    Neo4j_Object_link_node(long neoId, /*boolean isNode, */Neo4j_Object.Types type, Neo4j_Object.Levels level){
        
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
        return true;
    }
*/
    @Override
    public long getTo(DBaccess db){
        
        /*
        inline SYSID link_node::getTo()
        {
        if (to.tg == SYSID_TAG)
            return(SYSID(to.sid));
          else
            return(SYSID(0));
        }
        */
        
        return db.DBACCESS_get_From_or_To(id,false);
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //return -1;
        
    }
    
    @Override
    public long getFrom(DBaccess db) {  
        return db.DBACCESS_get_From_or_To(id,true);
    }
    
        
    @Override
    public int getLink(DBaccess db, PQI_Set ret){
        return db.DBACCESS_getLinkFrom(id, ret);
        
    }
    
    @Override
    public long getFromNode(DBaccess db, PQI_Set ret){
        return db.DBACCESS_getFromNode_Or_ToNode(id, ret, true);
    }
    
    @Override
    public long getToNode(DBaccess db, PQI_Set ret){
        return db.DBACCESS_getFromNode_Or_ToNode(id, ret, false);
    }
    @Override
    public int getInstOf(DBaccess db, PQI_Set set){
        return db.DBACCESS_getInstOf(id, set);
    }
   
}
