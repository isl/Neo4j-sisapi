/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neo4j_sisapi;

import java.util.Hashtable;

/**
 * Class created in order to hold the response messages from the api.
 * 
 * In order to create a new message follow these steps:
 * 1) Create an errorCode for the message (a public descriptive string that
 *    serves as a mnemonic for the error that occurred.
 * 2) Create a record in the allMessages 2-dimensional array where each element
 *    is a couple of the errorCode keyword and the actual String it contains.
 * @author Elias
 */
public class Messages {
    
    public static final String ErrorCode_For_ThesaurusReferenceIdAlreadyAssigned = "THESAURUS_REFERENCE_ID_ALREADY_ASSIGNED";
    public static final Hashtable<String,String> allMessages = new  Hashtable<String,String>(){{
        put(ErrorCode_For_ThesaurusReferenceIdAlreadyAssigned, "Reference Id %s is already used in thesaurus %s.");        
    }};
        
}
