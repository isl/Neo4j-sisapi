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


/**
 * storage class for Tokens
 * 
 * Individual Token
 * 
 * @author Elias Tzortzakakis <tzortzak@ics.forth.gr>
 */
class Neo4j_Object_token_node extends Neo4j_Object_Base implements Neo4j_Object{
    
    Neo4j_Object_token_node(long neoId, /*boolean isNode,*/ Neo4j_Object.Types type, Neo4j_Object.Levels level){
        
        super(neoId,/*isNode,*/type,level);     
    }
    
    @Override
    public boolean isTypeAttribute() {
        return false;
    }

    @Override
    public boolean isTypeIndividual() {
        return true;
    }

    @Override
    public boolean isTypeNotDefined() {
        return false;
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
    
    @Override
    public int getLinkBy(DBaccess db, PQI_Set ret){
        return db.DBACCESS_getLinkTo(id, ret);
        
    }
    
    @Override
    public int getLink(DBaccess db, PQI_Set ret){
        return db.DBACCESS_getLinkFrom(id, ret);
        
    }

    /*
    @Override
    public boolean isLevelToken() {
        return true;
    }
*/
}
