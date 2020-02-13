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
public class Return_Full_Link_Id_Row {
    
    StringObject cls;
    PrimitiveObject_Long clsId;    
    // augmented with 2 properties in order to retrieve information about the from value of the link (the to value is a CMValue that is already augmented by these 2 properties)
    // - Node reference Id (Facet/Hierarchy/Term/Source), and
    // - Trasliteration of logicalname that can be used for search or sorting
    PrimitiveObject_Long clsRefId;    
    StringObject clsTransliteration;    
    
    StringObject label;
    PrimitiveObject_Long linkId;
    StringObject categ;
    StringObject fromCls;
    PrimitiveObject_Long categid;
    CMValue cmv;
    IntegerObject unique_category;    
    
    public Return_Full_Link_Id_Row(){
        cls = new StringObject("");
        clsTransliteration = new StringObject("");
        clsId = new PrimitiveObject_Long();
        clsRefId = new PrimitiveObject_Long();
        label= new StringObject("");
        linkId = new PrimitiveObject_Long();
        categ = new StringObject("");
        fromCls = new StringObject("");
        categid = new PrimitiveObject_Long();
        cmv = new CMValue();
        cmv.assign_empty();
        unique_category = new IntegerObject();
    }
    
    Return_Full_Link_Id_Row(long linkIdVal, String clsStr, long clsIdVal, String labelStr,String categStr, String fromClsStr, long categIdVal,CMValue cmvVal, int uniqueVal ){
        this();
        this.linkId.setValue(linkIdVal);
        this.cls.setValue(clsStr);
        this.clsId.setValue(clsIdVal);
        this.label.setValue(labelStr);
        this.categ.setValue(categStr);
        this.fromCls.setValue(fromClsStr);
        this.categid.setValue(categIdVal);
        cmvVal.copyToOtherObject(cmv);
        this.unique_category.setValue(uniqueVal);
    }
    
    Return_Full_Link_Id_Row(long linkIdVal, String clsStr, long clsIdVal, String labelStr,String categStr, String fromClsStr, long categIdVal,CMValue cmvVal, int uniqueVal,long  clsRefIdVal, String clsTranslitVal){
        this();
        this.linkId.setValue(linkIdVal);
        this.cls.setValue(clsStr);
        this.clsId.setValue(clsIdVal);
        this.clsRefId.setValue(clsRefIdVal);
        this.clsTransliteration.setValue(clsTranslitVal);
        this.label.setValue(labelStr);
        this.categ.setValue(categStr);
        this.fromCls.setValue(fromClsStr);
        this.categid.setValue(categIdVal);
        cmvVal.copyToOtherObject(cmv);
        this.unique_category.setValue(uniqueVal);
    }
    /*
    int return_full_link_id(int sessionID, int set_id, l_name cls, int *clsid, 
    l_name label, int *linkid, l_name categ, l_name fromcls, int *categid, 
    cm_value *cmv, int *unique_category)
    
    Return the objects of set set_id. It is supposed that the objects are link 
    nodes are so it returns the logical name of each object (label), its system 
    identifier (*linkid), the logical name of the class where it is pointing 
    from (cls) and its system identifier (*clsid), the object it is pointing to 
    (cmv) which is a structure cm_value described later and also the category of
    the returned link (from_cls, categ) and its system identifier (*categid).
    
    Flag unique_category indicates if given category is unique (link object may 
    have more than one class) and flag traversed indicates if the specific link 
    belongs to a category that was previously set with the set_categories() 
    function with direction BACKWARD.
    
    */
    public long get_Neo4j_NodeId(){
        return linkId.getValue();
    }
    
    /**
     * v1 stands for variable in position 1 after set_id in 
     * the signature of the base function return_full_link_id
     * 
     * @return 
     */
    public String get_v1_cls(){
        return cls.getValue();
    }
    
    /**
     * v2 stands for variable in position 2 after set_id in 
     * the signature of the base function return_full_link_id
     * 
     * @return 
     */
    public long get_v2_clsid(){
        return clsId.getValue();
    }
    
    /**
     * v3 stands for variable in position 3 after set_id in 
     * the signature of the base function return_full_link_id
     * 
     * @return 
     */
    public String get_v3_label(){
        return label.getValue();
    }
    
    /**
     * v4 stands for variable in position 4 after set_id in 
     * the signature of the base function return_full_link_id
     * 
     * @return 
     */
    public long get_v4_linkId(){
        return linkId.getValue();
    }
    
    /**
     * v5 stands for variable in position 5 after set_id in 
     * the signature of the base function return_full_link_id
     * 
     * @return 
     */
    public String get_v5_categ(){
        return categ.getValue();
    }
    
    /**
     * v6 stands for variable in position 6 after set_id in 
     * the signature of the base function return_full_link_id
     * 
     * @return 
     */
    public String get_v6_fromCls(){
        return fromCls.getValue();
    }
    
    /**
     * v7 stands for variable in position 7 after set_id in 
     * the signature of the base function return_full_link_id
     * 
     * @return 
     */
    public long get_v7_categid(){
        return categid.getValue();
    }
    
    /**
     * v8 stands for variable in position 8 after set_id in 
     * the signature of the base function return_full_link
     * 
     * @return 
     */
    public CMValue get_v8_cmv(){
        return cmv.getCmvCopy();
    }
    
    /**
     * v9 stands for variable in position 9 after set_id in 
     * the signature of the base function return_full_link_id
     * 
     * @return 
     */
    public int get_v9_unique_category(){
        return unique_category.getValue();
    }
    
    
    /**
     * v10 stands for variable in position 10 after set_id in 
     * the signature of the base function return_full_link_id
     * 
     * @return 
     */
    public String get_v10_clsTransliteration(){
        return clsTransliteration.getValue();
    }
    
    /**
     * v11 stands for variable in position 11 after set_id in 
     * the signature of the base function return_full_link_id
     * 
     * @return 
     */
    public long get_v11_clsRefid(){
        return clsRefId.getValue();
    }
    
}
