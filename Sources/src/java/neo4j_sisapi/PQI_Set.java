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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Elias Tzortzakakis <tzortzak@ics.forth.gr>
 */
class PQI_Set {
    
    //<editor-fold defaultstate="collapsed" desc="C++">
    /*
    
    class SET : public SYSID_SET 
    class SYSID_SET {
    ...
    SSET       *firstset;          
    SET_FIXED  fixedset;
    SSET       *cur_set;     pointer to set with current position 
    ...
    int card_id;

  public:
    ....
    int   set_get_card()                           { return card_id; }
    int   set_put(SYSID id);                  // put sysid id in set 
    ...
    }
    
#ifndef SET_H
#define SET_H

#include "sis_kernel/set_sizes.h"
#include "sis_kernel/newset.h"
#include "sis_kernel/set_fixed.h"
#include "sis_kernel/set_dynamic.h"

#define  S_TOP  (char)0
#define  S_END  (char)1

class telos_object;

class SET : public SYSID_SET {

    TIME *time;       // all time values of this set // //Anthi.
    char **string;    // all strings of this set //
    float *real;      // all reals of this set //
    int   *number;    // all numbers of this set //
    LOGINAM **ln;     // all logical names of this set //
    int card_t;       // number of time values in set // //Anthi.
    int card_s;       // number of strings in set //
    int card_n;       // number of numbers in set //
    int card_r;       // number of reals in set //
    int card_ln;      // number of logical names in set //
    char sort_t;      // time values in set sorted top or end// //Anthi.
    char sort_s;      // strings in set sorted top or end//
    char sort_n;      // numbers in set sorted top or end//
    char sort_r;      // reals in set sorted top or end//
    char sort_ln;     // logical names in set sorted top or end//
    int point_tg;     // set internal pointer for tagged values//

  public:
     SET();            // constructor //
// next function is now virtual for Polivios's code (Stavroula)
     virtual ~SET();            // destructor //
     //~SET();            // destructor //
     int set_put(SYSID id) {return SYSID_SET::set_put(id);};
     int set_put(prs_val *val);    // put a tagged value in the set //
     int set_put(float val);      // put a float value in the set //
     int set_put(int val);        // put a int value in the set //
     int set_put(LOGINAM *val);   // put a logical name in the set //
     int set_put(TIME val);       // put a time value in the set // //Anthi.
     int set_put(char *s);        // put a string value in the set //

     int set_del(SYSID id) {return SYSID_SET::set_del(id);};
     int set_del(prs_val *val);    // del a tagged value in the set //
     int set_del(float val);      // del a float value in the set //
     int set_del(int val);        // del a int value in the set //
     int set_del(LOGINAM *val);   // del a logical name in the set //
     int set_del(TIME val);       // del a time value in the set // //Anthi.
     int set_del(char *s);        // del a string value in the set //

     // set_get's return 0 if empty or exhausted //
     prs_val set_get_prs();    // get a tagged value from the set //
     int set_get_card();       // get number of elements in the set//

     int set_union(SET *set);        // union of set with this set //
     void set_intersect(SYSID_SET *set) {SYSID_SET::set_intersect(set);};
     void set_intersect(SET *set);   // intersection of set with this set //
     void set_difference(SYSID_SET *set) {SYSID_SET::set_difference(set);};
     void set_difference(SET *set);  // difference of set with this set //
     int set_disjoint(SET *set);     // is intersection with this set empty?//
     int set_equal(SET *set);        // is intersection with this set empty?//
     int set_member_of(SYSID id) {return SYSID_SET::set_member_of(id);};
     int set_member_of(int val);     // is this int value in the set ? //
     int set_member_of(float val);   // is this real value in the set ? //
     int set_member_of(LOGINAM *val); //is this logical name in the set ?//
     int set_member_of(TIME val);     // is this time value in the set ? ////Anthi.
     int set_member_of(char *s);      // is this string in the set ? //
     int set_member_of(prs_val *val);  // is this tagged value in the set ?//

     void set_pos(int pos);           // set pointer to pos'th element //
     void set_clear();                // clear set whitout deallocating //
// next function is now virtual for Polivios's code, 
	 virtual int set_tuple_mode() { return (0); };	
     void set_print();
     void set_copy(SET *set);
};

#endif // SET_H //
    
class SYSID_SET {

    friend class telos_object;

    SSET       *firstset;          
    SET_FIXED  fixedset;
    SSET       *cur_set;    pointer to set with current position 

    int card_id;

  public:
    SYSID_SET();                                      //constructor 
    ~SYSID_SET();                                     // destructor 

    int   set_get_card()                           { return card_id; }
		
    int   set_put(SYSID id);                  // put sysid id in set 
    int   set_del(SYSID id);             // delete sysid id from set 

    int   set_union(SYSID_SET *set);            // union detween sets
    void  set_intersect(SYSID_SET *set); // intersection between sets 
    void  set_difference(SYSID_SET *set);  // difference between sets
    int   set_disjoint(SYSID_SET *set);        // are sets disjoint ?
    int   set_equal(SYSID_SET *set);             // are sets sequal ?
    int   set_member_of(SYSID id);            // is sysid id in set ?

    void  set_pos(int pos);                // number of sysids in set
    SYSID set_get_id();                       // get a sysid from set
    void  set_clear();              // clear set without deallocating
    void  set_clear_lower();     // clear set from the begginig up to
                                                  // current position
    void  set_clear_lower(SYSID id);   // clear set from the begginig
    // up to id, if exists, or up to the bigest SYSID smaller than id
    void  set_print();                   // print the elements of set
    int   set_copy(SYSID_SET *set);           // copy set to this set 
    void  rest_delete(SSET *set0);     // deletes from set0, and set0 

};    
    
    
    
class SET_FIXED : public SSET {
   
    SYSID sysid[64];	 all sysid's of this set 


  public:
     SET_FIXED();	                            // constructor 
     ~SET_FIXED();	                            // destructor     
    

    

class SET_DYNAMIC: public SSET {
  
    SYSID *sysid;


  public:
    
    void  set_put_lower(SYSID id)         { sysid[0] = id; card_id++; }
    
    */
    //</editor-fold>
    private ArrayList<Long> nodesSet = new ArrayList<>();
    //nodesHashSet (HashSet) contains the same data as nodeSet (ArrayList)    
    //HashSet nodesHashSet is used in contains operations O(1) vs O(n) 
    //ArrayList nodeSet is still necessary for function get_val_by_pos(int reqestedPos) 
    private HashSet<Long> nodesHashSet = new HashSet<>();
    //private ArrayList<Time> timeSet = new ArrayList<Time>();
    private ArrayList<Integer> intSet = new ArrayList<>();
    private ArrayList<String> stringSet = new ArrayList<>();
    //private ArrayList<Float> floatSet = new ArrayList<Float>();
    private int card_id;       // number of node ids in the set
    
    private int card_s;       // number of strings in set 
    private int card_n;       // number of numbers in set 
    
    //private int card_t;       // number of time values in set 
    //private int card_r;       // number of reals in set 
    //private int card_ln;      // number of logical names in set
    //char sort_t;      // time values in set sorted top or end
    //char sort_s;      // strings in set sorted top or end
    //char sort_n;      // numbers in set sorted top or end
    //char sort_r;      // reals in set sorted top or end
    //char sort_ln;     // logical names in set sorted top or end
    
    private int point_id;
    private int point_tg; //set internal pointer for tagged values
    
    
    PQI_Set(){
        
        nodesSet = new ArrayList<>();
        nodesHashSet = new HashSet<>();
        intSet = new ArrayList<>();
        stringSet = new ArrayList<>();
        this.set_clear();        
    }
    
    //in general set_get's return 0 if empty or exhausted
    
    //not exactly like C++ set_get_prs not prs_val structure
    int set_get_prs(CMValue retCmv){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        prs_val SET::set_get_prs()    // get a tagged value from the set 
        {
            //the pointer point_tg crosses all fields except sysid's 
            register p = point_tg++;
	

            if ( p < card_ln ){ //only a pointer to the LOGINAM is returned 
                if(sort_ln == S_TOP) 
                    return(prs_val(ln[p]));
                else 
                    return(prs_val(ln[SET_LOGINAM_SIZE-card_ln+p]));
            }
            else if ( (p -= card_ln) < card_s ){// only a pointer to the string is returned 
                if(sort_s == S_TOP) 
                    return(prs_val(string[p]));
                else 
                    return(prs_val(string[SET_STRING_SIZE-card_s+p]));
            }
            else if ( (p -= card_s) < card_n ){
                if(sort_n == S_TOP) 
                    return(prs_val(number[p]));
                else 
                    return(prs_val(number[SET_NUMBER_SIZE-card_n+p]));
            }
            else if ( (p -= card_n) < card_r ){
                if(sort_r == S_TOP) 
                    return(prs_val(real[p]));
                else 
                    return(prs_val(real[SET_REAL_SIZE-card_r+p]));
            }
            else if ( (p -= card_r) < card_t )  //Added by Anthi.
            {
                if(sort_t == S_TOP) return(prs_val(time[p]));
                else return(prs_val(time[SET_TIME_SIZE-card_t+p]));
            }
            else {
                prs_val tmp = prs_val();
                return(tmp);
            //return(prs_val());
            }
        }
        */
        // </editor-fold> 
        
        //get a tagged value from the set
        int p = point_tg++;
        if(p<this.stringSet.size()){
            retCmv.assign_string(this.stringSet.get(p));
            return QClass.APISucc;
        }
        else if((p -= this.stringSet.size())<this.intSet.size() && p>=0){
            retCmv.assign_int(this.intSet.get(p));
            return QClass.APISucc;
        }
        else{
            retCmv.assign_empty();
        }
            
        return QClass.APIFail;
    }
   
    //perhaps a collections.sort may be applied here
    int set_bulk_get_prs(ArrayList<Return_Prm_Row> retRows){
        //do not clear retRows it contains data retrieved for nodes
        this.stringSet.stream().forEach((str) -> {
            retRows.add(new Return_Prm_Row(str));
        });
        this.intSet.stream().forEach((intVal) -> {
            retRows.add(new Return_Prm_Row(intVal));
        });
    
        return QClass.APISucc;
    }
    
    //clear contents of PQI_Set Structure
    final void set_clear(){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
	/*
	void SET::set_clear()    // clear set without deallocating 
        {
                SYSID_SET::set_clear();
                card_t = 0; //Anthi.
                card_r = 0;
                card_n = 0;
                card_ln = 0;
                card_s = 0;
                sort_t = S_TOP; //Anthi.
                sort_r = S_TOP;
                sort_n = S_TOP;
                sort_ln = S_TOP;
                sort_s = S_TOP;
                point_tg=0;
        }

        
        void SYSID_SET::set_clear()
        {
        #ifdef SET_PROF
                set_prof[fill_pos(card_id)]++;
        #endif

                card_id = 0;

                // rest_delete must always be called. Otherwise a memory leak occurs.
                // For more info look at ::rest_delete. Antonis 6/2/1998. 
                //if (firstset->nextset != (SSET *)0)
                //    {
                rest_delete(firstset);
                //    }
                firstset = &fixedset;
                firstset->set_clear();
                set_pos(0);
        }
        */
	// </editor-fold>
        
        this.card_id = 0;
        this.set_pos(0);
        
        //this.card_r = 0;
        //this.card_t = 0;
        //this.card_ln = 0;
        this.card_n = 0;        
        this.card_s = 0;        
        this.nodesSet.clear(); 
        this.nodesHashSet.clear();
        this.stringSet.clear();
        this.intSet.clear();
        this.point_tg =0;
    }
    
    ArrayList<Long> get_Neo4j_Ids(){
        return new ArrayList<>(nodesHashSet);
    }
    
    int set_member_of(long id){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        
        //membership test in a single SET_DYNAMIC.
        // Returns 1 in success, and 0 if fail.
        //
        int SET_DYNAMIC::set_member_of(SYSID id)
        {
            if ( bin_search(0, card_id - 1, id) == -1 )
                return(0);
            
            return(1);
        }        
        */
        // </editor-fold>         

        if(this.containsNeo4jId(id)){
            return QClass.APISucc;            
        }
        return QClass.APIFail;
    }
    
    /**
     * if this set is disjoint with set return true, 
     * otherwise return false
     * 
     * @param set
     * @return 
     */
    boolean set_disjoint(PQI_Set set){
        
        /*
        for(long rec: set.nodesHashSet){
            if(this.containsNeo4jId(rec)){
                return false;
            }
        }
        for(int rec: set.intSet){
            if(this.containsPrimitiveInt(rec)){
                return false;
            }
        }
        for(String rec: set.stringSet){
            if(this.containsPrimitiveString(rec)){
                return false;
            }
        }
        */
        if (!set.nodesHashSet.stream().noneMatch((rec) -> (this.containsNeo4jId(rec)))) {
            return false;
        }
        if (!set.intSet.stream().noneMatch((rec) -> (this.containsPrimitiveInt(rec)))) {
            return false;
        }
        return set.stringSet.stream().noneMatch((rec) -> (this.containsPrimitiveString(rec)));
    }
    
    boolean set_equal(PQI_Set set){
        
        if(this.set_get_card()!=set.set_get_card()){
            return false;
        }
        
        //check if set contains the same as current set
        /*
        for(long rec: this.nodesHashSet){
        if(set.containsNeo4jId(rec)==false){
        return false;
        }
        }
        for(int rec: this.intSet){
        if(set.containsPrimitiveInt(rec)==false){
        return false;
        }
        }
        for(String rec: this.stringSet){
        if(set.containsPrimitiveString(rec)==false){
        return false;
        }
        }
         */
        if (!this.nodesHashSet.stream().noneMatch((rec) -> (set.containsNeo4jId(rec)==false))) {
            return false;
        }
        if (!this.intSet.stream().noneMatch((rec) -> (set.containsPrimitiveInt(rec)==false))) {
            return false;
        }
        return this.stringSet.stream().noneMatch((rec) -> (set.containsPrimitiveString(rec)==false));        
    }
    
    //After this operation the first set is the intersection
    //of the two sets given as arguments.
    int set_intersect(PQI_Set set){
        
        PQI_Set newSet = new PQI_Set();
        /*
        for(Long rec : set.nodesHashSet){
        if(this.containsNeo4jId(rec)){
        newSet.set_putNeo4j_Id(rec);
        }
        }
        for(int rec : set.intSet){
        if(this.containsPrimitiveInt(rec)){
        newSet.set_putPrimitiveInteger(rec);
        }
        }        
        for(String rec : set.stringSet){
        if(this.containsPrimitiveString(rec)){
        newSet.set_putPrimitiveString(rec);
        }
        }
         */
        set.nodesHashSet.stream().filter((rec) -> (this.containsNeo4jId(rec))).forEach((rec) -> {
            newSet.set_putNeo4j_Id(rec);
        });
        set.intSet.stream().filter((rec) -> (this.containsPrimitiveInt(rec))).forEach((rec) -> {
            newSet.set_putPrimitiveInteger(rec);
        });
        
        set.stringSet.stream().filter((rec) -> (this.containsPrimitiveString(rec))).forEach((rec) -> {
            newSet.set_putPrimitiveString(rec);
        });
        
        this.set_clear();
        this.set_union(newSet);
        
        return QClass.APISucc;
    }
    
    void set_pos(int pos){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*

void SET::set_pos(int pos)
{
	point_tg = pos;
	SYSID_SET::set_pos(pos);
#ifdef DEBUG
	printf("set_pos: point=%d,lim=%d\n",point_id,lim_id);
#endif
}

// 
// sets the internal point_id to pos. If pos is equal to zero   
// then the set is suppozed to be available for handling from   
// the beginning. Otherwise the set can be handled from the     
// pos-th element and after.                                    
// 
// 
// The SYSID_SET is a double linked list of fixed size arrays of SYSIDs.
// nextset points to the next set and prevset to the previous set.
// card_id holds the cardinality of each Set (the number of SYSIDs in the array)
// point_id points to the current SYSID in the array. When the SET changes
// point_id is invalid and thus it should be set either to 0 or card_id.
void SYSID_SET::set_pos(int pos)
{
	if (pos == 0)
	{
		cur_set = firstset;
		cur_set->point_id = pos;
	}
	else
	{
		if(pos > 0)
		{
			if (pos >= card_id)
			{
				printf("SYSID_SET::set_pos(absolute) : position requested exceeds the limits of set.\n");
				return;
			}

			SSET *cur;
			int  elements_now = 0;

			cur = firstset;
			do
			{
				elements_now += cur->card_id;
				if (elements_now <= pos)
				{
					cur = cur->nextset;
				}
				else
				{
					break;
				}
			} while (1);

			cur_set = cur;
			cur_set->point_id = cur->card_id - (elements_now - pos);
		}
		else
		{
			// pos < 0. Move pos positions backward from the current position.
			// if pos is greater than card_id this function fails
			// if point_id+pos >=0 the the requested position is within the current
			// array and thus the current set point_id is set to the required pos.
			if((cur_set->point_id+pos) >= 0)
			{
				cur_set->point_id += pos;
				return;
			}

			// The reqired pos is in a previous SET and thus we must move backwards
			// in the linked list adding the card_id of each set to pos until
			// point_id + pos >= 0. When this happens we have reached the required
			// SET and then it is enough to make point_id point to the required
			// SYSID.
			SSET *cur;
			int  elements_now = 0;

			cur = cur_set;
			pos = pos + cur->point_id;
			do
			{
				if(cur != firstset)
					cur = cur->prevset;
				else
				{
					printf("SYSID_SET::set_pos(relative backwards) : position requested exceeds the limits of set.\n");
					return;
				}
				cur->point_id = cur->card_id;
				if((cur->card_id + pos)>=0)
				{
					cur->point_id = cur->point_id+pos;
					break;
				}
				else
				{
					pos = pos + cur->card_id;
				}
			} while (1);
			cur_set = cur;
		}
	}
}        
        */
        // </editor-fold> 
        
        point_tg = pos;
        
        
        if(pos==0){
            this.point_id =0;
        }
        else if(pos>0){
            if((this.point_id + pos)>=this.set_get_card()){
                Logger.getLogger(PQI_Set.class.getName()).log(Level.INFO, "PQI_Set::set_pos(absolute) : position requested exceeds the limits of set.\n");
                return;
            }
            this.point_id += pos;
        }
        else{//pos <0
            if((this.point_id +pos)<0){
                Logger.getLogger(PQI_Set.class.getName()).log(Level.INFO, "SYSID_SET::set_pos(relative backwards) : position requested exceeds the limits of set.\n");
                return;
            }
            this.point_id += pos;
        }        
    }
    
    int set_putNeo4j_Id(long id){
        // <editor-fold defaultstate="collapsed" desc="C++ code.">
  /*      
//put a SYSID in a set. It checks for the appropriate sset to put it
//in. If there is mo space, it allocates a new sset to put the new
//sysid in.

int SYSID_SET::set_put(SYSID id)
{
	//printf("set_put   %d\n", id.id);
	if (card_id == 0) {
		firstset->set_put_lower(id);
		card_id++;
		return(0);
	}

	if (globalError.flag()) {
		return(-1);
	}

	int result;

	result = firstset->set_put(id);
	if (result == 0) {
		card_id++;
		return(0);
	}
	if (result == -1) {
		return(-1);
	}
	return(0);
}
        */  
        // </editor-fold>
        if(this.containsNeo4jId(id)==false){
            this.nodesSet.add(id);
            this.nodesHashSet.add(id);
            card_id++;
            
            return QClass.APISucc;
        }
        return QClass.APISucc;
    }
    
    int set_putPrimitiveInteger(int prmVal){
        if(this.containsPrimitiveInt(prmVal)==false){
            this.intSet.add(prmVal);
            card_n++;
            return QClass.APISucc;
        }
        return QClass.APISucc;        
    }
    
    int set_putPrimitiveTime(Time prmVal){
        throw new UnsupportedOperationException();
    }
    int set_putPrimitiveFloat(float prmVal){
        throw new UnsupportedOperationException();
    }
    int set_putPrimitiveString(String prmVal){
        
        if(this.containsPrimitiveString(prmVal)==false){
            this.stringSet.add(prmVal);
            card_s++;
            return QClass.APISucc;
        }
        return QClass.APISucc;       
    }
    
    int set_get_card(){
        return (this.nodesHashSet.size()+this.intSet.size()+this.stringSet.size());
    }
    
    int set_union(PQI_Set set){
        if(set==null){
            return QClass.APISucc;
        }
        /*
        for(long rec: set.nodesHashSet){
        this.set_putNeo4j_Id(rec);
        }
        for(int rec: set.intSet){
        this.set_putPrimitiveInteger(rec);
        }
        for(String rec: set.stringSet){
        this.set_putPrimitiveString(rec);
        }
         */
        set.nodesHashSet.stream().forEach((rec) -> {
            this.set_putNeo4j_Id(rec);
        });
        set.intSet.stream().forEach((rec) -> {
            this.set_putPrimitiveInteger(rec);
        });
        set.stringSet.stream().forEach((rec) -> {
            this.set_putPrimitiveString(rec);
        });
        return QClass.APISucc;
    }
    
    int set_difference(PQI_Set set){
        
        PQI_Set newSet = new PQI_Set();
        /*
        for(Long rec : this.nodesHashSet){
        if(set.containsNeo4jId(rec)==false){
        newSet.set_putNeo4j_Id(rec);
        }
        }
         */
        this.nodesHashSet.stream().filter((rec) -> (set.containsNeo4jId(rec)==false)).forEach((rec) -> {
            newSet.set_putNeo4j_Id(rec);
        });
        this.intSet.stream().filter((rec) -> (set.containsPrimitiveInt(rec))).forEach((rec) -> {
            newSet.set_putPrimitiveInteger(rec);
        });
        
        this.stringSet.stream().filter((rec) -> (set.containsPrimitiveString(rec))).forEach((rec) -> {
            newSet.set_putPrimitiveString(rec);
        });
        
        this.set_clear();
        this.set_union(newSet);
        
        return QClass.APISucc;
    }
    
    
    //copy all elements of set into this set.
    int set_copy(PQI_Set set){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        // copy all elements of set into this set. If malloc fails for
        // any reason, return -1, otherwise 0.
        int SYSID_SET::set_copy(SYSID_SET *set)
        {
                SSET *tmp;
                SSET *cur;
                SSET *thistmp;

                tmp = set->firstset;
                thistmp = firstset;
                thistmp->set_copy(tmp->set_get_sysids(),0,tmp->card_id);
                card_id += thistmp->card_id;
                while ( (tmp = tmp->nextset) != (SSET *)0) {
                        cur = thistmp->getnextset();
                        if ( globalError.flag() ) {
                                return(-1);
                        }
                        thistmp = cur;
                        thistmp->set_copy(tmp->set_get_sysids(),0,tmp->card_id);
                        card_id += thistmp->card_id;
                }
                return(0);
        }
        
        */
        // </editor-fold>
        
        
        this.set_clear();
        this.set_union(set);
        
        return QClass.APISucc;
        
    }
    

    //return APISucc or the requested Id
    long set_get_id(){
        
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        // it assumes that cur_set and point_id are always valid.       
        // in case that one of them is invalid, we might have wrong     
        // return value or core (if cur_set points to a very low address
        SYSID SYSID_SET::set_get_id()
        {
            // return SYSID(0) if the set has ended. Check if there is 
            // a next small set, and return SYSID(0) if it is NULL,    
            // or if it has zero cardinality                           
            if (cur_set->point_id >= cur_set->card_id) {
                if (cur_set->nextset == (SSET *)0) {
                    return(SYSID(0));
                }
                else {
                    if (cur_set->nextset->card_id == 0) {
                        return(SYSID(0));
                    }
                }
            }

            // if there are more elements in cur_set to be examined 
            // just update point_id. Otherwise go to the next set   
            // of cur_set, update point_id and return the sysid.    
            if (cur_set->point_id < cur_set->card_id) {
                return(*cur_set->get_val_by_pos(cur_set->point_id++));
            }
            else {
                  if (cur_set->nextset) {
                      cur_set = cur_set->nextset;
                  }
                  cur_set->point_id = 1;
                  return(cur_set->get_lower());
            }
        }

        */
        // </editor-fold>
        
        // return SYSID(0) if the set has ended.
        if(point_id>=this.nodesSet.size()){
            return QClass.APISucc;
        }
        
        //SYSID *get_val_by_pos(int pos)            { return &(sysid[pos]); }
        
        return this.get_val_by_pos(point_id++);
        //return(*cur_set->get_val_by_pos(cur_set->point_id++));
        //return(*cur_set->get_val_by_pos(cur_set->point_id++));
        
        //return QClass.APIFail;
    }
    
    private long get_val_by_pos(int requestedPos){
        //SYSID *get_val_by_pos(int pos)            { return &(sysid[pos]); }
        
        return this.nodesSet.get(requestedPos);
    }
    
    boolean containsNeo4jId(long linkId){
        return this.nodesHashSet.contains(linkId);
    }
    
    boolean containsPrimitiveInt(int prmInt){
        /*
        for(int id : this.intSet ){
        if(id==prmInt){
        return true;
        }
        }
         */ 

        return this.intSet.stream().anyMatch((id) -> (id==prmInt));
    }
    
    boolean containsPrimitiveString(String prmStr){
        /*
        for(String s : this.stringSet ){
        if(s.equals(prmStr)){
        return true;
        }
        }        
        return false;
         */ 
        return this.stringSet.stream().anyMatch((s) -> (s.equals(prmStr)));
        
    }
}
