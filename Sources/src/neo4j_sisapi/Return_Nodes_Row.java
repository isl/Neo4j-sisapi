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
 *
 * @author Elias Tzortzakakis <tzortzak@ics.forth.gr>
 */
public class Return_Nodes_Row {
    PrimitiveObject_Long nodeId;
    StringObject v1_cls;

    // augmented with 2 properties in order to retrieve information about the
    // - Node reference Id (Facet/Hierarchy/Term/Source), and
    // - Trasliteration of logicalname that can be used for search or sorting
    PrimitiveObject_Long referenceId;    
    StringObject transliteration;    
	    
    public Return_Nodes_Row(){
        nodeId = new PrimitiveObject_Long();
        referenceId = new PrimitiveObject_Long();
        v1_cls = new StringObject();
        transliteration = new StringObject();
    }
    
    Return_Nodes_Row(long lId, String lname){
        this();
        this.nodeId.setValue(lId);
        this.v1_cls.setValue(lname);        
    }
    
    Return_Nodes_Row(long lId, String lname,long cid, String transliterationStr){
        this();
        this.nodeId.setValue(lId);
        this.referenceId.setValue(cid);
        this.v1_cls.setValue(lname);
        this.transliteration.setValue(transliterationStr);
    }
    
    
    public long get_Neo4j_NodeId(){
        return nodeId.getValue();
    }
    
    /**
     * v1 stands for variable in position 1 after set_id in 
     * the signature of the base function return_nodes
     * 
     * @return 
     */
    public String get_v1_cls_logicalname(){
        return v1_cls.getValue();
    }
    
    /**
     * v2 stands for variable in position 2 after set_id in 
     * the signature of the base function return_nodes
     * 
     * @return 
     */
    public long get_v2_long_referenceId(){
        return referenceId.getValue();
    }
    
    
    /**
     * v3 stands for variable in position 3 after set_id in 
     * the signature of the base function return_nodes
     * 
     * @return 
     */
    public String get_v3_cls_transliteration(){
        return transliteration.getValue();
    }
}
