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
public class Return_Full_Nodes_Row {
    PrimitiveObject_Long nodeId;
    StringObject logicalname;
    StringObject systemClass;
    
    // augmented with 2 properties in order to retrieve information about the
    // - Node reference Id (Facet/Hierarchy/Term/Source), and
    // - Trasliteration of logicalname that can be used for search or sorting
    PrimitiveObject_Long referenceId;    
    StringObject transliteration;  
    
    public Return_Full_Nodes_Row(){
        nodeId = new PrimitiveObject_Long();
        referenceId = new PrimitiveObject_Long();
        logicalname = new StringObject("");
        systemClass = new StringObject("");     
        transliteration = new StringObject("");
    }
    
    Return_Full_Nodes_Row(long lId, String lname, scala.collection.convert.Wrappers.SeqWrapper labels,long refId, String lnameTransliteration){
        this(lId,lname,labels);
        this.referenceId.setValue(refId);
        this.transliteration.setValue(lnameTransliteration);
        
    }
    
    Return_Full_Nodes_Row(long lId, String lname, scala.collection.convert.Wrappers.SeqWrapper labels){
        this();
        this.nodeId.setValue(lId);
        this.logicalname.setValue(lname);
        
        
        String type = "";
        String level ="";
        
        if(labels==null || labels.size()==0){
            this.systemClass.setValue(Configs.SystemClass_SubStringForNotDefined);
        }
        else{
            if(labels.contains(Configs.Neo4j_Level_Token)){
                level = Configs.Neo4j_Level_Token;
            }
            else if(labels.contains(Configs.Neo4j_Level_S_Class)){
                level = Configs.Neo4j_Level_S_Class;
            }            
            else if(labels.contains(Configs.Neo4j_Level_M1_Class)){
                level = Configs.Neo4j_Level_M1_Class;
            }
            else if(labels.contains(Configs.Neo4j_Level_M2_Class)){
                level = Configs.Neo4j_Level_M2_Class;
            }
            else if(labels.contains(Configs.Neo4j_Level_M3_Class)){
                level = Configs.Neo4j_Level_M3_Class;
            }
            else if(labels.contains(Configs.Neo4j_Level_M4_Class)){
                level = Configs.Neo4j_Level_M4_Class;
            }
            
            if(labels.contains(Configs.Neo4j_Key_For_Type_AttributeStr)){
                type = Configs.SystemClass_SubStringForAttribute;
            }
            else if(labels.contains(Configs.Neo4j_Key_For_Type_IndividualStr)){
                type = Configs.SystemClass_SubStringForIndividual;
            }
            
            
            
            
            /*
            for(int i=0; i<labels.size(); i++){
                String str = labels[i];
                if(str.equals(Configs.Neo4j_Key_For_Type_AttributeStr)){
                    type = Configs.SystemClass_SubStringForAttribute;
                }
                else if(str.equals(Configs.Neo4j_Key_For_Type_IndividualStr)){
                    type = Configs.SystemClass_SubStringForIndividual;
                }
                else if(str.equals(Configs.Neo4j_Level_Token)){
                    level = Configs.Neo4j_Level_Token;
                }
                else if(str.equals(Configs.Neo4j_Level_S_Class)){
                    level = Configs.Neo4j_Level_S_Class;
                }
                else if(str.equals(Configs.Neo4j_Level_M1_Class)){
                    level = Configs.Neo4j_Level_M1_Class;
                }
                else if(str.equals(Configs.Neo4j_Level_M2_Class)){
                    level = Configs.Neo4j_Level_M2_Class;
                }
                else if(str.equals(Configs.Neo4j_Level_M3_Class)){
                    level = Configs.Neo4j_Level_M3_Class;
                }
                else if(str.equals(Configs.Neo4j_Level_M4_Class)){
                    level = Configs.Neo4j_Level_M4_Class;
                }
            }
            */
            if(level.length()==0 && type.length()==0){
                this.systemClass.setValue(Configs.SystemClass_SubStringForNotDefined);
            }
            else{
                this.systemClass.setValue(type+"_"+level);
            }
        }
        
    }
    
    
    public long get_Neo4j_NodeId(){
        return nodeId.getValue();
    }
    
    /**
     * v1 stands for variable in position 1 after set_id in 
     * the signature of the base function return_full_nodes
     * @return 
     */
    public long get_v1_sysid(){
        return nodeId.getValue();
    }    
    
    /**
     * v2 stands for variable in position 2 after set_id in 
     * the signature of the base function return_full_nodes
     * @return 
     */
    public String get_v2_node_logicalname(){
        return logicalname.getValue();
    }
    
    /**
     * v3 stands for variable in position 3 after set_id in 
     * the signature of the base function return_full_nodes
     * @return 
     */
    public String get_v3_Sclass_logicalname(){
        return systemClass.getValue();
    }
    
    public long get_v4_long_referenceId(){
        return referenceId.getValue();
    }
    
    public String get_v5_node_transliteration(){
        return transliteration.getValue();
    }
}
