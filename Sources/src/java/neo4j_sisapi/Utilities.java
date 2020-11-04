/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neo4j_sisapi;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utilities {

    
    public enum CsvExportMode { ALL, ONLY_GENERIC, ONLY_DATA}
    public LinkedHashSet<Long> csv_export_GetLabelIds(QClass q, Configs.Labels targetLabel, CsvExportMode csvMode){
        
        
        return q.db.csv_export_GetLabelIds(targetLabel, csvMode);
    }
    
    public LinkedHashMap<Long, LinkedHashSet<Long>> csv_export_GetRelIds(QClass q, Configs.Rels targetRelation, CsvExportMode csvMode){
        return q.db.csv_export_GetRelIds(targetRelation, csvMode);
    }
    
    public LinkedHashMap<Long, ArrayList<Object>> csv_export_GetProperty(QClass q, Configs.Attributes targetAttribute, CsvExportMode csvMode){
        return q.db.csv_export_GetProperty(targetAttribute, csvMode);
    }
    
    
    
    String removePrefix(String s) {

        if (s.contains("`")) {
            return s.substring(s.indexOf("`") + 1);
        } else {
            return s;
        }

    }

    ArrayList<Long> collectSequenctiallyAsubsetOfValues(int startindex, int howmanyToGet, ArrayList<Long> targetVals) {
        //return new ArrayList(targetVals.subList(startindex, startindex+howmanyToGet));
        ArrayList<Long> returnVals = new ArrayList<Long>();
        if (howmanyToGet <= 0) {
            throw new UnsupportedOperationException("collectSequenctiallyAsubsetOfValues was called with howmanyToGet: " + howmanyToGet);
        }
        int maxIndex = targetVals.size();
        if (startindex < maxIndex) {
            for (int i = 0; i < howmanyToGet; i++) {

                if ((startindex + i) >= maxIndex) {
                    break;
                } else {
                    returnVals.add(targetVals.get(i + startindex));
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

    String prepareStringForCypher(String searchVal) {
        return searchVal.replace("\\","\\\\");
    }
    
    private final String  regExEscape="\\\\";
    String prepareStringForCypherLikeClaues(String searchVal) {
        
        String retVal = searchVal.replace("\\","\\\\\\\\");
        retVal = retVal.replace("(",regExEscape+"(");
        retVal = retVal.replace(")",regExEscape+")");
        retVal = retVal.replace("[",regExEscape+"[");
        retVal = retVal.replace("]",regExEscape+"]");
        retVal = retVal.replace("{",regExEscape+"{");
        retVal = retVal.replace("}",regExEscape+"}");
        retVal = retVal.replace("?",regExEscape+"?");
        retVal = retVal.replace(".",regExEscape+".");
        retVal = retVal.replace("+",regExEscape+"+");
        
        return retVal;
    }

}
