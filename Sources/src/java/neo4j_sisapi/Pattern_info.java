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
class Pattern_info {

    // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
         types of match 
        #define  ANY_SUB_STR    0
        #define  START_SUB_STR  1
        #define  END_SUB_STR    2

        struct pattern_info {
            char *ptrn;
            char *ptrn_end;
            int type;
        };
        */
        // </editor-fold> 
    enum PatternType{ANY_SUB_STR,START_SUB_STR,END_SUB_STR};
    
    private String patternStr="";
    private String rawPatternStr="";
    private PatternType ptrnType=PatternType.ANY_SUB_STR;
    
    Pattern_info(String rawPattern){
        this.rawPatternStr = rawPattern;
        this.patternStr = rawPattern;
        
        if(rawPatternStr.startsWith("*")&& rawPatternStr.endsWith("*")){
            
            this.ptrnType = PatternType.ANY_SUB_STR;
            
            if(rawPatternStr.length()>2){
                patternStr = rawPatternStr.substring(1, rawPatternStr.length()-1);                
            }
            else{
                patternStr="";
            }            
            
        }
        else if(rawPatternStr.startsWith("*")){
            
            this.ptrnType = PatternType.END_SUB_STR;
            
            if(rawPatternStr.length()>1){
                patternStr = rawPatternStr.substring(1);
            }
            else{
                patternStr = "";
            }
            
        }
        else if(rawPatternStr.endsWith("*")){
            
            this.ptrnType = PatternType.START_SUB_STR;
            
            if(patternStr.length()>1){
                patternStr = rawPatternStr.substring(0, rawPatternStr.length()-1);
            }
            else{
                patternStr="";
            }
        }
        else{
            this.ptrnType = PatternType.ANY_SUB_STR;
            
            patternStr = rawPatternStr;            
        }
    }
    
    String getPatternString(){
        return this.patternStr;
    }
    String get_RAW_PatternString(){
        return this.rawPatternStr;
    }
    PatternType getType(){
        return this.ptrnType;
    }
}
