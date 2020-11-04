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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;

/**
 *
 * @author Elias Tzortzakakis <tzortzak@ics.forth.gr>
 */
public class Configs {

    
    public enum Labels implements Label { Type_Attribute, Type_Individual, M1_Class, M2_Class, M3_Class, M4_Class, S_Class, Token, PrimitiveClass, Common, UniqueInDB, Generic }

    public enum Rels implements RelationshipType {RELATION, ISA, INSTANCEOF }
    
    public enum Attributes{Neo4j_Id, MaxNeo4j_Id, ThesaurusReferenceId, MaxThesaurusReferenceId,Logicalname, Transliteration, Type, Value} 
    
    
    static final String regExForUnNamed="^Label[0-9abcdef]+$";
    //Neo4j_Id = 4 for Telos_Object
    static final String getNextSystemNumberQuery = "MATCH(n:"+Labels.Common.name()+"{"+Attributes.Logicalname.name()+":\"Telos_Object\"}) "
            + " set n."+Attributes.MaxNeo4j_Id.name()+" =n."+Attributes.MaxNeo4j_Id.name()+" +1 return n."+Attributes.MaxNeo4j_Id.name()+" as maxId";
        
    static final String getNextThesaurusReferenceIdQuery = "MATCH(n:"+Labels.Common.name()+"{"+Attributes.Logicalname.name()+":\"Thesaurus`%THES%\"}) "
            + " set n."+Attributes.MaxThesaurusReferenceId.name()+"=n."+Attributes.MaxThesaurusReferenceId.name()+"+1 return n."+Attributes.MaxThesaurusReferenceId.name()+" as maxId";
    //Single Counter added in Thesaurus`%THES% node. It is more flexible than the separate counters (it can easily be extended to include more properties)
    
    //static final String getNextThesaurusFacetIdQuery = "MATCH(n:Common{Logicalname:\"%THES%Facet\"}) set n.MaxThesaurusFacetId=n.MaxThesaurusFacetId+1 return n.MaxThesaurusFacetId as maxId";
    //static final String getNextThesaurusHierarchyIdQuery = "MATCH(n:Common{Logicalname:\"%THES%Hierarchy\"}) set n.MaxThesaurusHierarchyId=n.MaxThesaurusHierarchyId+1 return n.MaxThesaurusHierarchyId as maxId";
    //static final String getNextThesaurusTermIdQuery = "MATCH(n:Common{Logicalname:\"%THES%HierarchyTerm\"}) set n.MaxThesaurusTermId=n.MaxThesaurusTermId+1 return n.MaxThesaurusTermId as maxId";
    //static final String getNextSourceIdQuery = "MATCH(n:Common{Logicalname:\"Source\"}) set n.MaxSourceId=n.MaxSourceId+1 return n.MaxSourceId as maxId";
    
    //Initialize val 
    /*
        MATCH(n:Common) with max (n.Neo4j_Id) as newVal
        MATCH(t:Common{Neo4j_Id:4})
        SET t.MaxNeo4j_Id= newVal 
        return t

    OR 
    
        MATCH(n:Common) with max (n.Neo4j_Id) as newVal
        MATCH(t:Common{Logicalname:"Telos_Object"})
        SET t.MaxNeo4j_Id= newVal 
        return t    
    */
    
    static boolean boolDebugInfo = true;
    //static final int MAX_NEO4j_ID_FOR_PRIMITIVE = 2;
    static final int MAX_IDS_PER_QUERY = 500;
    
    //public static final String GenericLabelName = Labels.Generic.name();
    //public static final String UniqueInDBLabelName = Labels.UniqueInDB.name();
    public static final String CommonLabelName = Labels.Common.name();
    
    //static final String Neo4j_Level_Token = Labels.Token.name();
    //static final String Neo4j_Level_S_Class = Labels.S_Class.name();
    //static final String Neo4j_Level_M1_Class = Labels.M1_Class.name();
    //static final String Neo4j_Level_M2_Class = Labels.M2_Class.name();
    //static final String Neo4j_Level_M3_Class = Labels.M3_Class.name();
    //static final String Neo4j_Level_M4_Class = Labels.M4_Class.name();
    
    static final String Neo4j_Key_For_Type_IndividualStr = Labels.Type_Individual.name();
    static final String Neo4j_Key_For_Type_AttributeStr = Labels.Type_Attribute.name();
    static final String Neo4j_Key_For_PrimitiveClass_Str = Labels.PrimitiveClass.name();
    
    static List<String> systemLabels = new ArrayList(Arrays.asList(Arrays.stream(Labels.values()).map( x -> x.name()).collect(Collectors.toList())));
    
    
    public static final String Key_Primitive_Value_Type = Attributes.Type.name();
    public static final String Key_Primitive_Value = Attributes.Value.name();
    
    //MATCH (n:Type_Attribute) WHERE has(n.Type) RETURN DISTINCT  n.Type
    static final String Primitive_Value_Type_INT = "INT";
    static final String Primitive_Value_Type_STR = "STR";
    
    //static final boolean Neo4j_Id_used_as_node_label = true;
    //static final boolean CastNeo4jIdAsInt = true;
    static final boolean CastSysIdAsInt = true;
        
    static final String SystemClass_SubStringForAttribute = "Attribute";
    static final String SystemClass_SubStringForIndividual = "Individual";
    static final String SystemClass_SubStringForNotDefined = "Telos_Object";
    
    static final String TelosObjectAttributeFromClsname =  "Telos_Object";
    static final String TelosObjectAttributeFromLinkname =  "attribute";
    
    
    static final String Neo4j_Node_LogicalName_For_MaxNeo4jId = "Telos_Object";
    public static final String Neo4j_Key_For_MaxNeo4jId = Attributes.MaxNeo4j_Id.name();
    
    static final String Neo4j_Node_LogicalName_For_MaxThesaurusReferenceId = "Thesaurus`%THES%";
    public static final String Neo4j_Key_For_MaxThesaurusReferenceId = Attributes.MaxThesaurusReferenceId.name();
    
    public static final String Neo4j_Key_For_Neo4j_Id = Attributes.Neo4j_Id.name();
    public static final String Neo4j_Key_For_Logicalname = Attributes.Logicalname.name();
    public static final String Neo4j_Key_For_Transliteration = Attributes.Transliteration.name();
    public static final String Neo4j_Key_For_ThesaurusReferenceId = Attributes.ThesaurusReferenceId.name();
    
    public static final String csvExportFromNodeLabel = "fromNode";
    public static final String csvExportToNodeLabel = "toNode";
    
    
}

