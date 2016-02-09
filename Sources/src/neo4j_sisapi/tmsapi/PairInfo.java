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
package neo4j_sisapi.tmsapi;

import neo4j_sisapi.StringObject;
/**
 *
 * @author Elias Tzortzakakis <tzortzak@ics.forth.gr>
 */
class PairInfo {
    StringObject label;
    StringObject name;
    
    PairInfo(){
        label = new StringObject();
        name = new StringObject();
    }
    
    PairInfo(String nameStr,String labelStr){
        
        name  = new StringObject(nameStr);
        label = new StringObject(labelStr);
    }
    
    PairInfo(StringObject nameObj,StringObject labelObj){
        name  = new StringObject(nameObj.getValue());
        label = new StringObject(labelObj.getValue());
    }
}
