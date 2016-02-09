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
import java.util.Arrays;
import java.util.List;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;

/**
 *
 * @author Elias Tzortzakakis <tzortzak@ics.forth.gr>
 */
class Configs {

    static final String regExForUnNamed="^Label[0-9abcdef]+$";
    //Neo4j_Id = 4 for Telos_Object
    static final String getNextSystemNumberQuery = "MATCH(n:Common{Logicalname:\"Telos_Object\"}) set n.MaxNeo4j_Id=n.MaxNeo4j_Id+1 return n.MaxNeo4j_Id as maxId";
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
    static final int MAX_UNIONS_PER_QUERY = 100;
    static final String CommonLabelName = "Common";
    static final boolean Neo4j_Id_used_as_node_label = true;
    static final boolean CastNeo4jIdAsInt = true;
    static final boolean CastSysIdAsInt = true;
    
    static final String Neo4j_Key_For_PrimitiveClass_Str = "PrimitiveClass";
    static final String Neo4j_Key_For_Type_AttributeStr = "Type_Attribute";
    static final String Neo4j_Key_For_Type_IndividualStr = "Type_Individual";
    static final String Neo4j_Level_Token = "Token";
    static final String Neo4j_Level_S_Class = "S_Class";
    static final String Neo4j_Level_M1_Class = "M1_Class";
    static final String Neo4j_Level_M2_Class = "M2_Class";
    static final String Neo4j_Level_M3_Class = "M3_Class";
    static final String Neo4j_Level_M4_Class = "M4_Class";
    static final String SystemClass_SubStringForAttribute = "Attribute";
    static final String SystemClass_SubStringForIndividual = "Individual";
    static final String SystemClass_SubStringForNotDefined = "Telos_Object";
    
    static final String TelosObjectAttributeFromClsname =  "Telos_Object";
    static final String TelosObjectAttributeFromLinkname =  "attribute";
    final static String Key_Primitive_Value_Type = "Type";
    final static String Key_Primitive_Value = "Value";
    
    //MATCH (n:Type_Attribute) WHERE has(n.Type) RETURN DISTINCT  n.Type
    final static String Primitive_Value_Type_INT = "INT";
    final static String Primitive_Value_Type_STR = "STR";
    
    static List<String> systemLabels =  Arrays.asList(Neo4j_Key_For_Type_AttributeStr, Neo4j_Key_For_Type_IndividualStr,
                                                     CommonLabelName,
                                                     Neo4j_Level_Token, Neo4j_Level_S_Class,
                                                     Neo4j_Level_M1_Class,Neo4j_Level_M2_Class,Neo4j_Level_M3_Class,Neo4j_Level_M4_Class,
                                                     Neo4j_Key_For_PrimitiveClass_Str);
    
    enum Labels implements Label {

        Type_Attribute, Type_Individual, M1_Class, M2_Class, M3_Class, M4_Class, S_Class, Token, PrimitiveClass, Common
    }

    enum Rels implements RelationshipType {

        RELATION, ISA, INSTANCEOF
    }
    
}

