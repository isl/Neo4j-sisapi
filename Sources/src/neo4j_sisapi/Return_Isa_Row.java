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
public class Return_Isa_Row {
    StringObject obj1 = new StringObject("");
    StringObject obj2 = new StringObject("");
    
    public Return_Isa_Row(){
        
        obj1 = new StringObject("");
        obj2 = new StringObject("");
    }
    
    Return_Isa_Row(String obj1Str, String obj2Str){
        this();
        this.obj1.setValue(obj1Str);
        this.obj2.setValue(obj2Str);
    }
    
    /**
     * v1 stands for variable in position 1 after set_id in 
     * the signature of the base function return_isa
     * @return 
     */
    public String get_v1_obj1_logicalname(){
        return obj1.getValue();
    }
    
    /**
     * v2 stands for variable in position 2 after set_id in 
     * the signature of the base function return_isa
     * @return 
     */
    public String get_v2_obj2_logicalname(){
        return obj2.getValue();
    }
}
