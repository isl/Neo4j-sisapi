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
public class Identifier {
    public final static int ID_TYPE_UNDEFINED = 0;
    public final static int ID_TYPE_SYSID = 1;
    
    public final static int ID_TYPE_LOGINAM = 2;
    

    int tag;
    Object value;

    public Identifier()
    {
      super();
      this.tag = 0;
      this.value = new String("");
    }

    public Identifier(long id)
    {
      super();
      this.tag = ID_TYPE_SYSID;
      this.value = new Long(id);
    }

    public Identifier(String str)
    {
      super();
      this.tag = ID_TYPE_LOGINAM;
      this.value = new String(str);
    }

    public int getTag() {
      return tag;
    }

    public String getLogicalName() {
      return (String) value;
    }

    public long getSysid() {
      return ((Long) value).intValue();
    }

    public void setTag(int t) {
      tag = t;
    }

    public void setValue(String v) {
      value = v;
    }

    public void setValue(long vi) {
      value = new Long(vi);      
    }

    public String toString() {
      return (String) value;
    }
}

