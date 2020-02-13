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

import java.util.HashMap;
import java.util.ArrayList;
import static neo4j_sisapi.QClass.APIFail;
import static neo4j_sisapi.QClass.APISucc;

/**
 *
 * @author Elias Tzortzakakis <tzortzak@ics.forth.gr>
 */
class Sets_Class {
    
    //#define  STATUS_FREE     -4  //value for proj_status, denoting set is unused
    static final int STATUS_FREE     = -4;
    
    /*
    
    int  status[NTMPSETS];
    int  proj_status[NTMPSETS]; // STATUS_FREE set is free  
                                // -1  read object from answerer set  
                                // n >= 0  read object from set (field) n  
    
    */
    private ArrayList<PQI_Set> sets = new ArrayList<PQI_Set>();
    private ArrayList<Integer> proj_status = new ArrayList<Integer>();
    
    Sets_Class(){
        super();
    }

    void ClearAndReset(){
        sets.clear();
        proj_status.clear();
        sets = new ArrayList<PQI_Set>();
        proj_status = new ArrayList<Integer>();
    }
    
     enum Set_Operation_Identifiers {
        _SET_UNION,
        _SET_COPY,
        _SET_INTERSECT,
        _SET_DIFFERENCE,
        _SET_DISJOINT,
        _SET_EQUAL
    };
    
    int set_operation(Set_Operation_Identifiers q_id, int set_id1, int set_id2) {
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        int sets_class::set_operation(int op, int set_id1, int set_id2)
        {
            SET_TUPLE *set1;
            SET_TUPLE *set2;

            if ((set1 = (SET_TUPLE *) return_set(set_id1)) == NULL)
              return -1;

            if ((set2 = (SET_TUPLE *) return_set(set_id2)) == NULL)
              return -1;

            // reset_set(set_id1);
            // reset_set(set_id2);
            switch (op) {
               //case _SET_UNION      : return (set1->set_union(set2));
                   case _SET_UNION      : set1->tuple_union((SET_TUPLE*)set2); return 0;
               //case _SET_COPY       : set1->set_copy(set2); return 0;
                   case _SET_COPY       : *set1=((SET_TUPLE*)(set2)); return 0;
               case _SET_INTERSECT  : set1->set_intersect(set2);  return 0;
               //case _SET_DIFFERENCE : set1->set_difference(set2); return 0;
                   case _SET_DIFFERENCE : set1->tuple_difference((SET_TUPLE*)set2); return 0;
               case _SET_DISJOINT   : return set1->set_disjoint(set2);
               case _SET_EQUAL      : return set1->set_equal(set2);
            }
            return -1;
        }
        */
        // </editor-fold> 
        
        PQI_Set set1 = this.return_set(set_id1);
        if(set1==null){
            return APIFail;
        }
        PQI_Set set2 = this.return_set(set_id2);
        if(set2==null){
            return APIFail;
        }
        switch (q_id) {
            case _SET_UNION:{
                set1.set_union(set2);
                return APISucc;
            }
            case _SET_COPY:{
                set1.set_copy(set2);
                return APISucc;
            }
            case _SET_INTERSECT:{
                set1.set_intersect(set2);
                return APISucc;
            }
            case _SET_DIFFERENCE:{
                set1.set_difference(set2);
                return APISucc;
            }
            case _SET_DISJOINT:{
                if(set1.set_disjoint(set2)){
                    return QClass.TRUEval;
                }
                return QClass.FALSEval;
            }
            case _SET_EQUAL:{
                if(set1.set_equal(set2)){
                    return QClass.TRUEval;
                }
                return QClass.FALSEval;
            }
            default: {

                return APIFail;
            }
        }
    }
    
    int  set_member_of(int set_id, long id){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //
        //  Check if SYSID id exists in set set_id.
        //  Returns 0 on success and -1 if any error occured.
        //
       int sets_class::set_member_of(int set_id, SYSID id)
       {
           if ((set_id <= 0) || (set_id > NTMPSETS))
             return -1;

           if (proj_status[set_id-1] == STATUS_FREE)
             return -1;
           else
             return (sets[set_id-1]).set_member_of(id)- 1;// this is because set_put()
                                                         // returns 1 if exists, else 0
       }
        */
        // </editor-fold>         
        
        if(setIdOutOfBounds(set_id)){
            return APIFail;
        }
        if(proj_status.get(set_id-1)==STATUS_FREE){
            return APIFail;
        }        
        return this.sets.get(set_id-1).set_member_of(id);
    }
    
    //free the set corresponding to set identifier set_id
    int free_set(int set_id){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*

        //  Clear (free) set set_id for future reuse.
        int sets_class::free_set(int set_id)
        {
            if ((set_id <= 0) || (set_id > NTMPSETS))
              return -1;

            sets[set_id-1].set_clear();
            proj_status[set_id-1] = STATUS_FREE;
            return 0;
        }
        
        */
        // </editor-fold>         
        if(setIdOutOfBounds(set_id)){
            return APIFail;
        }
        sets.get(set_id-1).set_clear();
        proj_status.set(set_id-1,STATUS_FREE);
        
        return QClass.APISucc;        
    }
    
    //Clear (free) all temporary sets for future reuse.
    void free_all_sets(){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //  Clear (free) all temporary sets for future reuse.
        void sets_class::free_all_sets()
       {
           int i;

           for(i=0; i<NTMPSETS; i++) {
             if (proj_status[i] != STATUS_FREE) {
                proj_status[i] = STATUS_FREE;
                sets[i].set_clear();
             }
           }
       }        
        */
        // </editor-fold>         
        for(int i=0; i< sets.size();i++){
            if(proj_status.get(i) != STATUS_FREE){
                proj_status.set(i, STATUS_FREE);
                sets.get(i).set_clear();
            }
        }        
    }
    
    int reset_set(int set_id){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        // Set set's pointer at the beginning of the set
        int sets_class::reset_set(int set_id)
       {
           if ((set_id <= 0) || (set_id > NTMPSETS))
             return -1;

           if (proj_status[set_id-1] == STATUS_FREE)
             return -1;

           sets[set_id-1].set_pos(0);   // reset pointer
           if(sets[set_id-1].set_tuple_mode())
                       sets[set_id-1].reset_return_tuple(); // reset the return tuple function
           proj_status[set_id-1] = -1;  // set set status
           return 0;
       }
        */
        // </editor-fold> 
        if(setIdOutOfBounds(set_id)){
            return APIFail;            
        }
        if(proj_status.get(set_id-1) == STATUS_FREE){
            return APIFail;
        }
        
        this.sets.get(set_id-1).set_pos(0);// reset pointer
        this.proj_status.set(set_id-1, -1);
        
        return APISucc;
    }
    
    //Returns a new set id 
    int return_new_set(){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        // Returns a new set id or -1 if no free set exists.
        // Set ids are 1 to NTMPSETS ...
        int sets_class::return_new_set()
        {
            int i;

            for(i=0; i<NTMPSETS; i++) 
              if (proj_status[i] == STATUS_FREE) {
                proj_status[i] = -1; // defaul value of used set
                return (i+1);
              }
            fprintf(stderr," *** ERROR *** : out of answer sets\n");
            return -1;
        }        
        */
        // </editor-fold>         
        
        for(int i=0; i<proj_status.size(); i++){
            if(proj_status.get(i) == STATUS_FREE){
                //proj_status[i] = -1; // defaul value of used set
                proj_status.set(i, -1); // defaul value of used set
                return i+1;
            }
        }
        
        //we do not have limitations to the number of sets
        //fprintf(stderr," *** ERROR *** : out of answer sets\n");
        //return -1;
        
        sets.add(new PQI_Set());
        proj_status.add(-1);        
        return (sets.size());
        
    }
        
    // Return the PQI_Set of set with identifier set_id OR NULL!!!
    PQI_Set return_set(int set_id){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //
        //  Returns a pointer to the set with id set_id or NULL if
        //  set_id doesn't exist.
        //
       SET *sets_class::return_set(int set_id)
       {
           if ((set_id <= 0) || (set_id > NTMPSETS))
             return NULL; 

           if (proj_status[set_id-1] == STATUS_FREE)
             return NULL;
           else 
             return &(sets[set_id-1]);
       }
        */
        // </editor-fold> 
        
        if(setIdOutOfBounds(set_id)){
            return null;
        }
        if (proj_status.get(set_id-1) == STATUS_FREE){
             return null;
        }
        return this.sets.get(set_id-1);
    }
    
    //put SYSID id in set set_id
    int set_put(int set_id, long id){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //
        //  Put SYSID id in set set_id
        //  Returns 0 on success and -1 if any error occured.
        //
       int sets_class::set_put(int set_id, SYSID id)
       {
           if ((set_id <= 0) || (set_id > NTMPSETS))
             return -1;

           if (proj_status[set_id-1] == STATUS_FREE)
             return -1;
           else
             return (sets[set_id-1]).set_put(id);
       }
       */
        // </editor-fold> 

        if(setIdOutOfBounds(set_id)){
            return APIFail;
        }
        
        if(proj_status.get(set_id-1)==STATUS_FREE){
            return APIFail;
        }
        
        return sets.get(set_id-1).set_putNeo4j_Id(id);
    }
    
    int set_put_Primitive(int set_id, CMValue cmv){
        int cmvType = cmv.getType();
        
        switch(cmvType){
            case CMValue.TYPE_INT:{
                return sets.get(set_id-1).set_putPrimitiveInteger(cmv.getInt());                
            }
            case CMValue.TYPE_STRING:{
                return sets.get(set_id-1).set_putPrimitiveString(cmv.getString());
            }
            default:{
                
                if(Configs.boolDebugInfo){
                    throw new UnsupportedOperationException(" Primitives accepted for set_put_prm are of type int and String only");
                }                
            }
        }
        return APISucc;
    }
    
    //get cardinality of set set_id
    int set_get_card(int set_id){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        //
        //  Return the cardinality of set set_id 
        //  or -1  if set_id doesn't exist.
        //
        int sets_class::set_get_card(int set_id)
        {
           if ((set_id <= 0) || (set_id > NTMPSETS))
             return -1;

           if (proj_status[set_id-1] == STATUS_FREE)
             return -1;
           else
             return (sets[set_id-1]).set_get_card();
        }

        */
        // </editor-fold> 
        
        if (setIdOutOfBounds(set_id)) {
            return QClass.APIFail;
        }
        if(proj_status.get(set_id-1)==STATUS_FREE){
            return APIFail;
        }
        return this.sets.get(set_id-1).set_get_card();

    }
    
    
    private boolean setIdOutOfBounds(int set_id){
        if(set_id<=0){
            return true;
        }
        if(set_id>this.sets.size()){
            return true;
        }
        return false;
    }
    
    
        
}
