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
 * @author Elias
 */
public class ModelReader {
    
    
    /**
     * For each class in the model create 3 things (Class logical name: %CLASS%)
     * 1) private final static String Model_Class_%CLASS% = "%CLASS%";
     * 2) public static String getGenericClass_%CLASS%(){ return Model_Class_%CLASS%; }
     * 3) public static String getThesaurusClass_%CLASS%(String selectedThesaurus){ return selectedThesaurus.toUpperCase().concat(Model_Class_%CLASS%); }
     * 
     * 
     * For each attribute - link in the Model create 3 similar things 
     * naming conventions
     */
    
    private final static String Model_Class_HierarchyTerm = "HierarchyTerm";
    public static String getGenericClass_HierarchyTerm(){
        return Model_Class_HierarchyTerm;
    }
    public static String getThesaurusClass_HierarchyTerm(String selectedThesaurus){
        return selectedThesaurus.toUpperCase().concat(Model_Class_HierarchyTerm);
    }
    
    private final static String Model_Class_ExternalLink = "ExternalLink";
    public static String getGenericClass_ExternalLink(){
        return Model_Class_ExternalLink;
    }
    public static String getThesaurusClass_ExternalLink(String selectedThesaurus){
        return selectedThesaurus.toUpperCase().concat(Model_Class_ExternalLink);
    }
    
    private final static String Model_Class_ExternalVocabulary = "ExternalVocabulary";
    public static String getGenericClass_ExternalVocabulary(){
        return Model_Class_ExternalVocabulary;
    }
    public static String getThesaurusClass_ExternalVocabulary(String selectedThesaurus){
        return selectedThesaurus.toUpperCase().concat(Model_Class_ExternalVocabulary);
    }
    
    
    
    private static final String Model_ExtLink_has_label = "has_label";
    public static String getGenericCategory_ExtLink_has_label(){
        return Model_ExtLink_has_label;
    }
    public static String getThesaurusCategory_ExtLink_has_label(String selectedThesaurus){
        return selectedThesaurus.toUpperCase().concat("_").concat(Model_ExtLink_has_label);
    }
    
    
    private static final String Model_ExtLink_belongs_to_ExtVoc = "belongs_to_external_vocabulary";
    public static String getGenericCategory_ExtLink_belongs_to_ExtVoc(){
        return Model_ExtLink_belongs_to_ExtVoc;
    }
    
    public static String getThesaurusCategory_ExtLink_belongs_to_ExtVoc(String selectedThesaurus){
        return selectedThesaurus.toUpperCase().concat("_").concat(Model_ExtLink_belongs_to_ExtVoc);
    }
    
    
    private static final String Model_ExtVoc_attr_has_fullname = "has_fullname";
    public static String getGenericCategory_ExtVoc_has_fullname(){
        return Model_ExtVoc_attr_has_fullname;
    }
    public static String getThesaurusCategory_ExtVoc_has_fullname(String selectedThesaurus){
        return selectedThesaurus.toUpperCase().concat("_").concat(Model_ExtVoc_attr_has_fullname);
    }
    
    
    private static final String Model_ExtVoc_attr_has_description = "has_description";
    public static String getGenericCategory_ExtVoc_has_description(){
        return Model_ExtVoc_attr_has_description;
    }
    public static String getThesaurusCategory_ExtVoc_has_description(String selectedThesaurus){
        return selectedThesaurus.toUpperCase().concat("_").concat(Model_ExtVoc_attr_has_description);
    }
    
    
    private static final String Model_ExtVoc_attr_has_version = "has_version";
    public static String getGenericCategory_ExtVoc_has_version(){
        return Model_ExtVoc_attr_has_version;
    }
    public static String getThesaurusCategory_ExtVoc_has_version(String selectedThesaurus){
        return selectedThesaurus.toUpperCase().concat("_").concat(Model_ExtVoc_attr_has_version);
    }
    
    
    private static final String Model_ExtVoc_attr_release_timestamp = "release_timestamp";
    public static String getGenericCategory_ExtVoc_release_timestamp(){
        return Model_ExtVoc_attr_release_timestamp;
    }
    public static String getThesaurusCategory_ExtVoc_release_timestamp(String selectedThesaurus){
        return selectedThesaurus.toUpperCase().concat("_").concat(Model_ExtVoc_attr_release_timestamp);
    }
    
    
    private static final String Model_ExtVoc_attr_has_uri = "has_uri";
    public static String getGenericCategory_ExtVoc_has_uri(){
        return Model_ExtVoc_attr_has_uri;
    }
    public static String getThesaurusCategory_ExtVoc_has_uri(String selectedThesaurus){
        return selectedThesaurus.toUpperCase().concat("_").concat(Model_ExtVoc_attr_has_uri);
    }
    
    
    
    private static final String Model_HierarchyTerm_attr_has_extenalLink = "has_external_link";
    public static String getGenericCategory_HierTerm_has_ExtlLink(){
        return Model_HierarchyTerm_attr_has_extenalLink;
    }
    public static String getThesaurusCategory_HierTerm_has_ExtlLink(String selectedThesaurus){
        return selectedThesaurus.toUpperCase().concat("_").concat(Model_HierarchyTerm_attr_has_extenalLink);
    }
    
    
    private static final String Model_HierarchyTerm_attr_has_exactMatchExternalLink = "exact_match";
    public static String getGenericCategory_HierTerm_has_ExactMatch_ExtLink(){
        return Model_HierarchyTerm_attr_has_exactMatchExternalLink;
    }    
    public static String getThesaurusCategory_HierTerm_has_ExactMatch_ExtLink(String selectedThesaurus){
        return selectedThesaurus.toUpperCase().concat("_").concat(Model_HierarchyTerm_attr_has_exactMatchExternalLink);
    }
    
    
    private static final String Model_HierarchyTerm_attr_has_closeMatchExternalLink  = "close_match";
    public static String getGenericCategory_HierTerm_has_CloseMatch_ExtLink(){
        return Model_HierarchyTerm_attr_has_closeMatchExternalLink;
    }
    public static String getThesaurusCategory_HierTerm_has_CloseMatch_ExtLink(String selectedThesaurus){
        return selectedThesaurus.toUpperCase().concat("_").concat(Model_HierarchyTerm_attr_has_closeMatchExternalLink);
    }

}
