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
public class Return_Prm_Row implements Comparable<Return_Prm_Row>{
    CMValue cmv = new CMValue();
    
    public Return_Prm_Row(){
        cmv.assign_empty();
    }
    
    /**
     * Constructor for node cmvalue
     * @param lname
     * @param sysid 
     */
    Return_Prm_Row(String lname, long sysid){
        this();
        cmv.assign_node(lname, sysid);        
    }
    
    /**
     * Constructor for integer primitive value
     * @param invVal 
     */
    Return_Prm_Row(int invVal){
        this();
        cmv.assign_int(invVal);
    }
    
    /**
     * Constructor for string primitive value
     * @param str 
     */
    Return_Prm_Row(String str){
        this();
        cmv.assign_string(str);        
    }
    
    /**
     * General Constructor
     * @param cmvToCopy 
     */
    Return_Prm_Row(CMValue cmvToCopy){
        this();
        
        int cmvType = cmvToCopy.getType();
        switch(cmvType){
            case CMValue.TYPE_NODE:{
                cmv.assign_node(cmvToCopy.getString(), cmvToCopy.getSysid());
            }
            case CMValue.TYPE_INT:{
                cmv.assign_int(cmvToCopy.getInt());
            }
            case CMValue.TYPE_STRING:{
                cmv.assign_string(cmvToCopy.getString());
            }
            case CMValue.TYPE_EMPTY:{
                //do nothing
            }
            default:{
                if(Configs.boolDebugInfo){
                    throw new UnsupportedOperationException();
                }
            }
        }
    }
    
    /**
     * v1 stands for variable in position 1 after set_id in 
     * the signature of the base function return_prm
     * 
     * @return 
     */
    public CMValue get_v1_cmv(){
        return cmv.getCmvCopy();
    }

    @Override
    public int compareTo(Return_Prm_Row o) {
        int thisType = this.cmv.getType();
        int otherType = o.cmv.getType();
        if(thisType==otherType){
            switch(thisType){
                case CMValue.TYPE_INT:{
                    if(this.cmv.getInt()>=o.cmv.getInt()){
                        return 1;
                    }
                    else{
                        return -1;
                    }
                }
                case CMValue.TYPE_STRING:{
                    return this.cmv.getString().compareTo(o.cmv.getString());                        
                }
                case CMValue.TYPE_NODE:{
                    if(this.cmv.getSysid()>=o.cmv.getSysid()){
                        return 1;
                    }
                    else{
                        return -1;
                    }
                }
                default: {
                    return 0;
                }
            }
        }
        
        if(thisType == CMValue.TYPE_NODE){
            return -1;
        }
        else if(thisType == CMValue.TYPE_STRING){
            if(otherType== CMValue.TYPE_NODE){
                return 1;
            }
            else{
                return -1;
            }
        }        
        else /*if(thisType == CMValue.TYPE_INT)*/{
            return 1;
        }
    }
}
