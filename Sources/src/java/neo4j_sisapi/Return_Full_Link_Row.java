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
 *
 * @author Elias Tzortzakakis <tzortzak@ics.forth.gr>
 */
public class Return_Full_Link_Row {
    
    /*
    int return_full_link(int sessionID, int set_id, l_name cls, l_name label, 
    l_name categ, l_name fromcls, cm_value *cmv, int *unique_category, 
    int *traversed)
    
    Return the objects of set set_id. It is supposed that the objects are link 
    nodes are so it returns the logical name of each object (label), the logical
    name of the class where it pointing from (cls), the object it is pointing to
    (cmv) which is a structure cm_value described later and also the category of
    the returned link (from_cls, categ).
    
    Flag unique_category indicates if given category is unique (link object may 
    have more than one class) and flag traversed indicates if the specific link 
    belongs to a category that was previously set with the set_categories() 
    function with direction BACKWARD.
    */
    
    PrimitiveObject_Long linkId;
    StringObject cls;
    StringObject label;
    StringObject categ;
    StringObject fromCls;
    CMValue cmv;
    IntegerObject unique_category;
    IntegerObject traversed;
    
    Return_Full_Link_Row(){
        linkId = new PrimitiveObject_Long();
        cls = new StringObject("");
        label = new StringObject("");
        categ = new StringObject("");
        fromCls = new StringObject("");
        cmv = new CMValue();
        cmv.assign_empty();
        unique_category = new IntegerObject();
        traversed = new IntegerObject();
    }
    
    Return_Full_Link_Row(long lId, 
            String clsStr, 
            String labelStr,
            String categStr,
            String fromClsStr,             
            CMValue cmvVal,
            int unique_categoryVal,
            int traversedVal){
        this();
        this.linkId.setValue(lId);
        this.cls.setValue(clsStr);
        this.label.setValue(labelStr);
        this.categ.setValue(categStr);
        this.fromCls.setValue(fromClsStr);
        cmvVal.copyToOtherObject(this.cmv);
        unique_category.setValue(unique_categoryVal);
        traversed.setValue(traversedVal);
    }
    
    public long get_Neo4j_NodeId(){
        return linkId.getValue();
    }
    
    /**
     * v1 stands for variable in position 1 after set_id in 
     * the signature of the base function return_full_link
     * 
     * @return 
     */
    public String get_v1_cls(){
        return cls.getValue();
    }
    
    
    /**
     * v2 stands for variable in position 2 after set_id in 
     * the signature of the base function return_full_link
     * 
     * @return 
     */
    public String get_v2_label(){
        return label.getValue();
    }
    
    /**
     * v3 stands for variable in position 3 after set_id in 
     * the signature of the base function return_full_link
     * 
     * @return
     */
    public String get_v3_categ(){
        return categ.getValue();
    }
    
    /**
     * v4 stands for variable in position 4 after set_id in 
     * the signature of the base function return_full_link
     * 
     * @return
     */
    public String get_v4_fromcls(){
        return fromCls.getValue();
    }
    
    /**
     * v5 stands for variable in position 5 after set_id in 
     * the signature of the base function return_full_link
     * 
     * @return 
     */
    public CMValue get_v5_cmv(){
        return cmv.getCmvCopy();
    }
    
    /**
     * v6 stands for variable in position 6 after set_id in 
     * the signature of the base function return_full_link
     * 
     * @return 
     */
    public int get_v6_unique_category(){
        return unique_category.getValue();
    }
    
    /**
     * v7 stands for variable in position 7 after set_id in 
     * the signature of the base function return_full_link
     * 
     * @return 
     */
    public int get_v7_traversed(){
        return traversed.getValue();
    }
}
