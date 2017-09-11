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

import java.util.Vector;
/*
MATCH(n:Common:Token)-[:INSTANCEOF*1..]->(m:Common{Logicalname:"AATDEMODescriptor"}) 
return n.Logicalname, replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(lower(n.Logicalname),'ά','α'),'έ','ε'),'ή','η'),'ί','ι'),'ό','ο'),'ύ','υ'),'ώ','ω'),'ϊ','ι'),'ΐ','ι'),'ϋ','υ'),'ΰ','υ') as str 
order by str 
SKIP 100 
LIMIT 500

Get Links to 

MATCH(n:Common:Token)-[:INSTANCEOF*1..]->(m:Common{Logicalname:"ALLMERGEDDescriptor"}) 
with  n as starting, replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(lower(n.Logicalname),'ά','α'),'έ','ε'),'ή','η'),'ί','ι'),'ό','ο'),'ύ','υ'),'ώ','ω'),'ϊ','ι'),'ΐ','ι'),'ϋ','υ'),'ΰ','υ') as str 
order by str
SKIP 1299
LIMIT 50
MATCH (starting)<-[r:RELATION]-(m)<-[:RELATION]-(to)
WITH m as link, starting.Neo4j_Id as startingNeo4jId, starting.Logicalname as startingLogName,CASE has(m.Type) WHEN TRUE THEN null ELSE to.Neo4j_Id END as toId, CASE has(m.Type) WHEN TRUE THEN null ELSE to.Logicalname END as toName, to:Type_Attribute as toIsAttribute,CASE startNode(r) WHEN starting then 'Outgoing' ELSE 'Incoming' END as Direction 
MATCH (link)-[:INSTANCEOF]->(categ)<-[:RELATION]-(fromCls)
return startingNeo4jId, startingLogName, 
       link.Neo4j_Id, link.Logicalname,link.Type ,link.Value, 
	   fromCls.Neo4j_Id, fromCls.Logicalname, categ.Neo4j_Id, categ.Logicalname, 
	   toId, toName, toIsAttribute, Direction


get Links from

MATCH(n:Common:Token)-[:INSTANCEOF*1..]->(m:Common{Logicalname:"ALLMERGEDDescriptor"}) 
with  n as starting, replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(lower(n.Logicalname),'ά','α'),'έ','ε'),'ή','η'),'ί','ι'),'ό','ο'),'ύ','υ'),'ώ','ω'),'ϊ','ι'),'ΐ','ι'),'ϋ','υ'),'ΰ','υ') as str 
order by str
SKIP 1299
LIMIT 50
MATCH (starting)-[r:RELATION]->(m)-[:RELATION*0..1]->to)
WHERE has(m.Type) OR to.Neo4j_Id <> m.Neo4j_Id
WITH m as link, starting.Neo4j_Id as startingNeo4jId, starting.Logicalname as startingLogName,CASE has(m.Type) WHEN TRUE THEN null ELSE to.Neo4j_Id END as toId, CASE has(m.Type) WHEN TRUE THEN null ELSE to.Logicalname END as toName, to:Type_Attribute as toIsAttribute,CASE startNode(r) WHEN starting then 'Outgoing' ELSE 'Incoming' END as Direction 
MATCH (link)-[:INSTANCEOF]->(categ)<-[:RELATION]-(fromCls)
return startingNeo4jId, startingLogName, 
       link.Neo4j_Id, link.Logicalname,link.Type ,link.Value, 
	   fromCls.Neo4j_Id, fromCls.Logicalname, categ.Neo4j_Id, categ.Logicalname, 
	   toId, toName, toIsAttribute, Direction

*/
/**
 *
 * @author Elias Tzortzakakis <tzortzak@ics.forth.gr>
 */
class UtilitiesString {
    final char FWNHENTA[] =          {'α', 'ε', 'η', 'ι', 'ο', 'υ', 'ω', 'Α', 'Ε', 'Η', 'Ι', 'Ο', 'Υ', 'Ω'};
    final char FWNHENTA_ME_TONO[] =  {'ά', 'έ', 'ή', 'ί', 'ό', 'ύ', 'ώ', 'Ά', 'Έ', 'Ή', 'Ί', 'Ό', 'Ύ', 'Ώ'};        
        
    /*---------------------------------------------------------------------
                            UtilitiesString()
    ----------------------------------------------------------------------*/                
    UtilitiesString() {
    }  
    
    /*-----------------------------------------------------------------
            GetToneAndCaseInsensitiveComparisonsOfPattern()
    -----------------------------------------------------------------*/    
    Vector<String> GetToneAndCaseInsensitiveComparisonsOfPattern(String pattern) {
        //Logger.getLogger(UtilitiesString.class.getName()).log(Level.INFO, Parameters.LogFilePrefix+"GetToneAndCaseInsensitiveComparisonsOfPattern(" + pattern + ")");
        //Logger.getLogger(UtilitiesString.class.getName()).log(Level.INFO, Parameters.LogFilePrefix+"--------------------------------------------");        
        String patternAllLower = pattern.toLowerCase();
        String patternAllCaps = pattern.toUpperCase();
        String patternFirstCharacterOfEachWordCapital = GetPatternWithFirstCharacterOfEachWordCapital(pattern);
    
        // fill a Vector with: the original pattern, 
        // the pattern with all characters to lowercase and the pattern with all characters to uppercase
        Vector<String> patternsVector = new Vector<String>();
        patternsVector.add(pattern);
        if (patternsVector.contains(patternAllLower) == false) {
            patternsVector.add(patternAllLower);
        }
        if (patternsVector.contains(patternAllCaps) == false) {
            patternsVector.add(patternAllCaps);
        }
        if (patternsVector.contains(patternFirstCharacterOfEachWordCapital) == false) {
            patternsVector.add(patternFirstCharacterOfEachWordCapital);
        }        
        // get their tone comparisons
        Vector<String> ToneAndCaseInsensitiveComparisons = new Vector<String>();
        for(int i =0; i< patternsVector.size(); i++) {
            Vector<String> patternToneComparisons = new Vector<String>();
            patternToneComparisons = GetToneComparisonsOfPattern(patternsVector.get(i));
            for(int j =0; j< patternToneComparisons.size(); j++) {
                ToneAndCaseInsensitiveComparisons.add(patternToneComparisons.get(j));
            }
        }  
        // in case of blank pattern
        if (ToneAndCaseInsensitiveComparisons.size() == 0) {
            ToneAndCaseInsensitiveComparisons.add("");
        }
        // test
        /*Logger.getLogger(UtilitiesString.class.getName()).log(Level.INFO, Parameters.LogFilePrefix+"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        for(int i =0; i< ToneAndCaseInsensitiveComparisons.size(); i++) {
            Logger.getLogger(UtilitiesString.class.getName()).log(Level.INFO, Parameters.LogFilePrefix+i+1 + ". PATTERN COMPARISON: " + ToneAndCaseInsensitiveComparisons.get(i));
        }                        
        */
        return ToneAndCaseInsensitiveComparisons;
    }    
    
    /*-----------------------------------------------------------------
            GetPatternWithFirstCharacterOfEachWordCapital()
    -----------------------------------------------------------------*/    
    String GetPatternWithFirstCharacterOfEachWordCapital(String pattern) {
        // get the words of the pattern
        String[] words = pattern.split(" ");
        Vector<String> wordsVector = new Vector<String>();
        for(int i =0; i< words.length; i++) {
            if (words[i].equals("") == false) {
                wordsVector.add(words[i]);
            }
        }        
        for(int i =0; i< wordsVector.size(); i++) {        
            String word = wordsVector.get(i);
            String wordFirstCharacterCap = "";
            if (word.length() > 1) {
                wordFirstCharacterCap = word.substring(0, 1).toUpperCase() + word.substring(1, word.length());
            }  
            wordsVector.set(i, wordFirstCharacterCap);
        }
        String patternFirstCharacterCap = "";
        for(int i =0; i< wordsVector.size(); i++) {        
            if (i > 0) { // initial blank character for all words except the 1st one
                patternFirstCharacterCap += " ";
            }
            patternFirstCharacterCap += wordsVector.get(i);
        }
        
        return patternFirstCharacterCap;
    }       
    
    /*-----------------------------------------------------------------
                        GetToneComparisonsOfPattern()
    -----------------------------------------------------------------*/    
    Vector<String> GetToneComparisonsOfPattern(String pattern) {
        //Logger.getLogger(UtilitiesString.class.getName()).log(Level.INFO, Parameters.LogFilePrefix+"GetToneComparisonsOfPattern(" + pattern + ")");
        //Logger.getLogger(UtilitiesString.class.getName()).log(Level.INFO, Parameters.LogFilePrefix+"--------------------------------------------");
        
        // get the words of the pattern
        String[] words = pattern.split(" ");
        Vector<String> wordsVector = new Vector<String>();
        for(int i =0; i< words.length; i++) {
            if (words[i].equals("") == false) {
                wordsVector.add(words[i]);
            }
        }
        Vector<String> patternToneComparisons = new Vector<String>();
        // for each word of the pattern
        int wordsVectorSize = wordsVector.size();
        for(int i =0; i< wordsVectorSize; i++) {
            String word = wordsVector.get(i);
            Vector<String> wordToneComparisons = new Vector<String>();
            wordToneComparisons = GetToneComparisonsOfWord(word);
            int wordToneComparisonsSize = wordToneComparisons.size();
            if (i == 0) {
                for(int j = 0; j < wordToneComparisonsSize; j++) {
                    patternToneComparisons.add(wordToneComparisons.get(j));
                }
            }
            else {
                Vector<String> newPatternToneComparisons = new Vector<String>();
                for(int k =0; k < patternToneComparisons.size(); k++) {
                    String currentPattern = patternToneComparisons.get(k);
                    for(int j = 0; j < wordToneComparisonsSize; j++) {
                        String newWord = wordToneComparisons.get(j);
                        String currentPatternWithNewWord = currentPattern + " " + newWord;
                        newPatternToneComparisons.add(currentPatternWithNewWord);
                    }                    
                }
                patternToneComparisons.clear();
                for(int k =0; k < newPatternToneComparisons.size(); k++) {
                    patternToneComparisons.add(newPatternToneComparisons.get(k));
                }
            }
        }
        
        // test
        /*
        Logger.getLogger(UtilitiesString.class.getName()).log(Level.INFO, Parameters.LogFilePrefix+"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        for(int i =0; i< patternToneComparisons.size(); i++) {
            Logger.getLogger(UtilitiesString.class.getName()).log(Level.INFO, Parameters.LogFilePrefix+i+1 + ". PATTERN COMPARISON: " + patternToneComparisons.get(i));
        }                
         */
        return patternToneComparisons;
    }
            
    /*-----------------------------------------------------------------
                        GetToneComparisonsOfWord()
    -----------------------------------------------------------------*/    
    private Vector<String> GetToneComparisonsOfWord(String word) {
        //Logger.getLogger(UtilitiesString.class.getName()).log(Level.INFO, Parameters.LogFilePrefix+"GetToneComparisonsOfWord(" + word + ")");
        //Logger.getLogger(UtilitiesString.class.getName()).log(Level.INFO, Parameters.LogFilePrefix+"--------------------------------------------");
        char wordCharArray[] = word.toCharArray();
        // kane th leksh ATONH
        int wordLen = wordCharArray.length;
        int FWNHENTA_ME_TONOLen = FWNHENTA_ME_TONO.length;
        for(int i =0; i< wordLen; i++) {
            char wordChar = wordCharArray[i];
            // FWNHENTA_ME_TONO, FWNHENTA
            for(int j =0; j< FWNHENTA_ME_TONOLen; j++) {
                if (wordChar == FWNHENTA_ME_TONO[j]) {
                    wordCharArray[i] = FWNHENTA[j];
                }
            }
        }
        word = String.copyValueOf(wordCharArray);
        //Logger.getLogger(UtilitiesString.class.getName()).log(Level.INFO, Parameters.LogFilePrefix+"ATONO word = " + word);
        // mazepse ta fwnhenta ths lekshs, ta indexes toys kai ta antistoixa tonoymena fwnhenta
        wordCharArray = word.toCharArray();
        wordLen = wordCharArray.length;
        Vector wordFwnhenta = new Vector();
        Vector wordFwnhentaIndexes = new Vector();
        Vector wordTonoymenaFwnhenta = new Vector();
        int FWNHENTALen = FWNHENTA.length;
        for(int i =0; i< wordLen; i++) {
            char wordChar = wordCharArray[i];
            for(int j =0; j< FWNHENTALen; j++) {
                if (wordChar == FWNHENTA[j]) {
                    wordFwnhenta.add(wordChar);
                    wordFwnhentaIndexes.add(i);
                    wordTonoymenaFwnhenta.add(FWNHENTA_ME_TONO[j]);
                }
            }
        }      
        // test
        /*
        for(int i =0; i< wordFwnhenta.size(); i++) {
            Logger.getLogger(UtilitiesString.class.getName()).log(Level.INFO, Parameters.LogFilePrefix+"EXEI FWNHEN: " + wordFwnhenta.get(i) + " sto index: " + wordFwnhentaIndexes.get(i) + " me antistoixo tonoymeno fwnhen: " + wordTonoymenaFwnhenta.get(i));
        }
         */
        // ftiakse toys syndyasmoys twn leksewn
        Vector<String> wordToneComparisons = new Vector<String>();
        // bale to ATONO word
        wordToneComparisons.add(word);
        // bale toys syndyasmoys toy word me tonoys
        for(int i =0; i< wordFwnhenta.size(); i++) {
            wordCharArray = word.toCharArray();
            Integer indx = new Integer(wordFwnhentaIndexes.get(i).toString());
            char tonoymenoFwnhen = wordTonoymenaFwnhenta.get(i).toString().charAt(0);
            wordCharArray[indx.intValue()] = tonoymenoFwnhen;
            wordToneComparisons.add(String.copyValueOf(wordCharArray));
        }
        // test
        /*
        for(int i =0; i< wordToneComparisons.size(); i++) {
            Logger.getLogger(UtilitiesString.class.getName()).log(Level.INFO, Parameters.LogFilePrefix+"WORD COMPARISON: " + wordToneComparisons.get(i));
        } 
         */       
        
        return wordToneComparisons;
    }
}
