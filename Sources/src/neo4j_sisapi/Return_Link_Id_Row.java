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
public class Return_Link_Id_Row {
    PrimitiveObject_Long linkId;
    PrimitiveObject_Long clsId;
    StringObject cls;
    PrimitiveObject_Long sysId;
    CMValue cmv;
    IntegerObject traversed;
    
    public Return_Link_Id_Row(){
        linkId = new PrimitiveObject_Long();
        clsId = new PrimitiveObject_Long();
        cls = new StringObject();
        sysId = new PrimitiveObject_Long();
        cmv = new CMValue();
        cmv.assign_empty();                
        traversed = new IntegerObject();
    }
    
    Return_Link_Id_Row(long lId, 
            long clsIdVal, 
            String clsStr, 
            long sysidVal /*should be the same as lId*/, 
            CMValue cmvVal, 
            int traversedVal){
        this();
        this.linkId.setValue(lId);
        this.clsId.setValue(clsIdVal);
        this.cls.setValue(clsStr);
        this.sysId.setValue(sysidVal);
        cmvVal.copyToOtherObject(this.cmv);
        traversed.setValue(traversedVal);
    }
    
    public long get_Neo4j_NodeId(){
        return linkId.getValue();
    }
    
    /**
     * v1 stands for variable in position 1 after set_id in 
     * the signature of the base function return_link_id
     * 
     * @return the logical name cls of the object that points to the link 
     * (the link from logical name)
     */
    public String get_v1_cls(){
        return cls.getValue();
    }
    
    /**
     * v2 stands for variable in position 2 after set_id in 
     * the signature of the base function return_link_id
     * 
     * @return 
     */
    public long get_v2_fcid(){
        return clsId.getValue();
    }
    
    /**
     * v3 stands for variable in position 3 after set_id in 
     * the signature of the base function return_link_id
     * 
     * @return the system identifier of the link object
     */
    public long get_v3_sysid(){
        return sysId.getValue();
    }
    
    /**
     * v4 stands for variable in position 4 after set_id in 
     * the signature of the base function return_link_id
     * 
     * @return 
     */
    public CMValue get_v4_cmv(){
        return cmv.getCmvCopy();
    }
    
    /**
     * v5 stands for variable in position 5 after set_id in 
     * the signature of the base function return_link_id
     * 
     * @return 
     */
    public int get_v5_traversed(){
        return traversed.getValue();
    }
}
