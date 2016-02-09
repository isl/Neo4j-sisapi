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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Elias Tzortzakakis <tzortzak@ics.forth.gr>
 */
public class CMValue {
    public static final int TYPE_INT = 10;
    public static final int TYPE_STRING = 11;
    //public static final int TYPE_FLOAT = 12; //NOT SUPPORTED YET
    public static final int TYPE_NODE = 13;
    public static final int TYPE_EMPTY = 14;
    // public static final int TYPE_TIME = 15;//NOT SUPPORTED YET
    // public static final int TYPE_SYSID = 16;//NOT SUPPORTED YET
    /*
    int assign_node(cm_value *val, char *s, int id);
    int assign_string(cm_value *val, char *s);
    int assign_int(cm_value *val, int n);
    int assign_float(cm_value *val, float r);
    int assign_time(cm_value *val, TIME t);
    int assign_empty(cm_value *val);
    */
    
    int type;
    long sysid;
    Time t;
    String s;
    int n;
    float r;

    public CMValue() {
      reset_values();
    }

    void reset_values() {
      type = TYPE_EMPTY;
      sysid = 0;
      t = null;
      s = null;
      n = 0;
      r = 0.0F;
    }
    ///////////////////////////////////////////////////////

    public CMValue getCmvCopy(){
        CMValue retVal = new CMValue();
        if(this.type==TYPE_EMPTY){
            retVal.assign_empty();
            return retVal;
        }
        if(this.type==TYPE_INT){
            retVal.assign_int(this.getInt());
            return retVal;
        }
        if(this.type==TYPE_STRING){
            retVal.assign_string(this.getString());
            return retVal;
        }
        if(this.type==TYPE_NODE){
            retVal.assign_node(this.getString(), this.getSysid());
            return retVal;
        }
        if(Configs.boolDebugInfo){
            Logger.getLogger(CMValue.class.getName()).log(Level.INFO, "Copy called on CMV that is not EMTPY, INT,STRING, or Node");
        }
        return null;
    }
    void copyToOtherObject(CMValue other){
        
        if(this.type==TYPE_EMPTY){
            other.assign_empty();
        }
        else if(this.type==TYPE_INT){
            other.assign_int(this.getInt());            
        }
        else if(this.type==TYPE_STRING){
            other.assign_string(this.getString());
        }
        else if(this.type==TYPE_NODE){
            other.assign_node(this.getString(), this.getSysid());            
        }
        else if(Configs.boolDebugInfo){
            Logger.getLogger(CMValue.class.getName()).log(Level.INFO, "Copy called on CMV that is not EMTPY, INT,STRING, or Node TYPE:" + this.type);
        }
        
    }
    /*
    public void assign_time(Time time) {
      reset_values();
      type = TYPE_TIME;
      t = time;
    }
    */

    public void assign_node(String str, long id) {
      reset_values();
      type = TYPE_NODE;
      sysid = id;
      s = new String(str);
    }

    public void assign_string(String str) {
      reset_values();
      type = TYPE_STRING;
      s = new String(str);
    }
    public void assign_int(int nn) {
      reset_values();
      type = TYPE_INT;
      n = nn;
    }

    public void assign_empty() {
      reset_values();
    }

    /*
    public void assign_float(float f) {
      reset_values();
      type = TYPE_FLOAT;
      r = f;
    }*/


  //-------------------------------------------------------


    public int getType() {
      return type;
    }

    public long getSysid() {
      return sysid;
    }

    public Time getTime() {
      return t;
    }

    public String getString() {
      return s;
    }
    public int getInt() {
      return n;
    }
    public float getFloat() {
      return r;
    }

  /////////////////////////////////////////////////////////////////

    public String toString() {
      return "[CMValue "+type+" "+sysid+" "+t+" "+
             //GreekConverter.Win2DosString(s) + " " + n + " " + r + "]";
              s + " " + n + " " + r + "]";
    }

    static public String toHex(String str) {
      int len = str.length();
      StringBuffer sb = new StringBuffer(2*len-1);
      for (int i=0; i<len; i++) {
        if (i>0) sb.append(" ");
        sb.append(Integer.toHexString( (int) str.charAt(i) ) );
      }
      return sb.toString();
    } 

}
