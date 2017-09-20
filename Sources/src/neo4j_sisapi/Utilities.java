/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neo4j_sisapi;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;


class Utilities {
        
    String removePrefix(String s) {
        
        if (s.contains("`")) {
            return s.substring(s.indexOf("`") + 1);
        } else {
            return s;
        }
        
    }
    
    Vector<Long> collectSequenctiallyAsubsetOfValues(int startindex,int howmanyToGet, Vector<Long> targetVals){
        Vector<Long> returnVals = new Vector<Long>();
        if(howmanyToGet<=0){
            throw new UnsupportedOperationException("collectSequenctiallyAsubsetOfValues was called with howmanyToGet: " +howmanyToGet);
        }
        int maxIndex =targetVals.size(); 
        if(startindex<maxIndex){
            for(int i = 0; i< howmanyToGet; i++){
                
                if((startindex+i)>=maxIndex){
                    break;
                }
                else{
                    returnVals.add(targetVals.get(i+startindex));
                }
            }
        }        
        return returnVals;
    }
    
    void handleException(Exception ex) {
        Logger.getLogger(DBaccess.class.getName()).log(Level.INFO, ex.getMessage());
        if (Configs.boolDebugInfo) {            
            ex.printStackTrace(System.out);
        }
    }
    
}
