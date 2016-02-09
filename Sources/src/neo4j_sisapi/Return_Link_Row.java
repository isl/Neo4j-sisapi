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
public class Return_Link_Row {
    PrimitiveObject_Long linkId;
    StringObject cls;
    StringObject label;
    CMValue cmv;
    
    public Return_Link_Row(){
        linkId = new PrimitiveObject_Long();
        cls = new StringObject();
        label = new StringObject();
        cmv = new CMValue();
        cmv.assign_empty();                
    }
    
    Return_Link_Row(long lId, String clsStr, String labelStr,CMValue cmvVal){
        this();
        this.linkId.setValue(lId);
        this.cls.setValue(clsStr);
        this.label.setValue(labelStr);
        cmvVal.copyToOtherObject(this.cmv);
    }
    
    
    public long get_Neo4j_NodeId(){
        return linkId.getValue();
    }
    
    /**
     * v1 stands for variable in position 1 after set_id in 
     * the signature of the base function return_link
     * @return 
     */
    public String get_v1_cls(){
        return cls.getValue();
    }
    
    /**
     * v2 stands for variable in position 2 after set_id in 
     * the signature of the base function return_link
     * @return 
     */
    public String get_v2_label(){
        return label.getValue();
    }
    
    /**
     * v3 stands for variable in position 3 after set_id in 
     * the signature of the base function return_link
     * @return 
     */
    public CMValue get_v3_cmv(){
        return cmv.getCmvCopy();
    }
}
