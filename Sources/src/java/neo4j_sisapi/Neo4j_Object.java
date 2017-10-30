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
 * equivelant of telos_object class
 * 
 * telos_object is the superclass for the special classes in which
 * the telos objects are stored. telos objects are described by
 * three sets :
 *  the set of classes it is an instance of
 *  the set of its superclasses if it is a class
 *  the set of its attributes
 *  attributes are objects by themselves which have two
 *  additional fields :
 *  the "from" object where it is defined in
 *  the "to" object which is the attribute value itself
 *
 * All references to objects in the above mentioned sets and
 * in the "to" and "from" fields are done by system identifiers.
 * All these sets (fields) are accompanied by the corresponding
 * inverse link sets (i.e. :"from" <-> "link", "to" <-> "link_by",
 * "isa" <-> "subclass","inst_of" <-> "inst_by"), except for
 * references to builtin system classes.
 * These sets are implemented by fixed size arrays ("slots" )
 * as a starting point and are extended dynamically into extensions
 * which are not futher structured or specialized. These fixed size
 * arrays should be later adjusted to the average space neeeded, to
 * avoid extensive use of extensions and waste of disk/memory space.
 * See below for details on the format.
 *
 * Specializations of telos_object are done to avoid unnecessary
 * slots for specific object groups, for example objects of the
 * token level don't have instances or to or from values.
 *
 * The built-in classes of telos are specializations which are 
 * compiled in the code as constants. They don't use internal
 * set representations and do not hold sets of the identifiers
 * of objects where they are referenced in.
 * 
 * Format of the slots:
 * The last sysid carries a sentinel bit (SYSID_END). An absolute
 * zero is invalid before the last sysid and indicates a corrupted
 * database or programming error. If the set is continued beyond
 * the end of the slot's array or beyond a contiguous field of
 * elements of arbitrary size within an extension, the last element
 * of the array or the contiguous field  becomes an "continuation"
 * address, which is a virtual address over the space of all the
 * extensions of the object, indicating the storage location of
 * the next element of the set. The continuation address carries
 * the "SYSID_END" bit and the "SYSID_CONT" bit. Hence in the
 * current implementation sysids are restricted to 30 bits.
 * the decision was made to allow for slot arrays of size 1, in
 * the case we mostly do not have more elements (e.g. inst_of,
 * isa, link of attribute classes).
 * The decision to use virtual addresses was made to avoid updating
 * all these addresses when loading an object from disk.
 * Depending on the order during data input, the contents of
 * one slot may be highly fragmented over the object's extensions,
 * requiring unnecessary space for continuation addresses and
 * unnecessary jumps during retrieval. This is why the contents of
 * an object having extensions should be compressed with the
 * "copy" method" before saving on disk.
 *
 * Attention!!!
 * 
 * There is a set of subclasses of telos_object not implemented i.e.
 * sys_class, trigger_node
 * and the subclasses of sys_class i.e. Individual, IndClass, AttriClass, 
 * Attribute, T_Object and Class
 * 
 * @author Elias Tzortzakakis <tzortzak@ics.forth.gr>
 */
interface Neo4j_Object {
    //
   /*
    
 //
 // private declarations:
 //

//
 //  generic routines handling the dynamic set representation
 // in the "slots" of the telos_object instances 
 //
    // find the position to put a sysid into any slot                  
    int find_id(SYSID **slot,SYSID **lim,SYSID id,int insType,get_cont_cash *gocp);

    // find the sysid to be deleted in any slot                        
    int find_iddel(SYSID **slot,SYSID **lim,SYSID id,get_cont_cash *gocp);

    // after "find_id", shift the rest of the slots contents up        
    int shift_up_id(SYSID *slot,SYSID *lim,int flag,SYSID id,int insType,get_cont_cash *gocp);

    // after "find_iddel", shift the rest of the slots contents down   
    int shift_down_id(SYSID *slot,SYSID *lim,get_cont_cash *gocp);

    // copy contents of any slot slot0 to an array(!) slot1 until      
    // end of contents of slot0 or limit slim in slot1.                
    int copy_id(SYSID **slot0,SYSID **slot1,SYSID *slim);

    // allocate a new free space on the extension space                
    SYSID *alloc_ext(int flag,SYSID *sp,SYSID *lim,int insType,get_cont_cash *gocp);

protected:
    void mark_last_type(SYSID *sp,int insType);
    int insertAfterLast(SYSID id);
    
    // put a sysid to any slot of a telos object:                      
    int generic_putsys(SYSID *slot,SYSID *slim, SYSID id,int insType);

    // get all sysid's from any slot of a telos object:                
    int getsys_by_copy(SYSID *slot,SSET *set);
    void getsys_by_union(SYSID *slot,SYSID_SET *set);
    SYSID_SET *generic_getsys(SYSID *slot,SYSID_SET *set);

    // delete a sysid from any slot of a telos object:                 
    void generic_delsys(SYSID *slot,SYSID *slim, SYSID id);
    
    // added by Manos Theodorakis at 29/9/93 
    int generic_is_not_empty( SYSID *slot) {
        return( slot->id == SYSID_END ? 0 : 1);
    }

    // copy contents of slot slot0 of objects op completely to         
    // slot1 of the calling object (uses copy_id )                     
    int trans_id(SYSID *slot0,SYSID *slot1,SYSID *slim,telos_object *op);

    // added by G. Georgiannakis at 20/1/93 
    void very_new_trans_id(SYSID *slot0,SYSID *slot1,SYSID *slim,telos_object *op);

    // using the extension-relative virtual address "ca", get a pointer
    // into the corresponding extension and its last element *lim      
	SYSID *get_cont(int ca,SYSID **lim,get_cont_cash *gocp);

    // get and link a new extension
    telos_oext *get_ext(get_cont_cash *gocp);

    // deallocate all extensions of the object calling             
    void del_ext();

    // print the contents of any slot for debugging                    
    void print_id(SYSID *slot);

    // print the contents of any slot for debugging                    
    int stat_id(SYSID *slot);

    // print the contents of the (common) superclasses part for debugging
    void print_com();

	telos_oext	*lastExt;
	int			lastMarkerType;

  public:
    SYSID	o_sysid;	// the objects sysid 
    telos_oext	*o_ext;		// address of extension 
    short		o_type;		// object type 
    short		o_flag;		// object flag 
    int		o_size;		// size overall extensions 


	void	set_lastExt(telos_oext *ptr) { lastExt = ptr; }
	void	reset_markers() {
		lastExt = (telos_oext *)0;
		lastMarkerType = 0;
	}

	// methods used for demand loading on objects extensions 
	int				load_extensions();
	void			set_ext_loaded() { o_flag |= O_EXTENSIONS_LOADED; };
	int				ext_loaded() { return o_flag & (O_EXTENSIONS_LOADED); }
	void			mark_ext_not_loaded(void *start_t);


	telos_object() { reset_markers(); }
	virtual ~telos_object() { }

    // handle the "unresolved object" flag during data input 
	int IsUnresolved() {return(o_flag&O_UNRESOLVED);};
	void putResolved() {o_flag &= ~O_UNRESOLVED;};
	void putUnresolved() {o_flag |= O_UNRESOLVED;};

	// flag O_CLASS_ATTR is set if a link is a class attribute 
	// (Lemonia) 
	int IsClassAttr() {return(o_flag&O_CLASS_ATTR); }
	void setClassAttrFlag() { o_flag |= O_CLASS_ATTR; }

        // possibly never used 
	void unsetClassAttrFlag() { o_flag &= ~O_CLASS_ATTR; }

	// flag O_NO_INSTANCES is set if a category has no instances 
	// (Lemonia) 
	int HasNoInst() {return(o_flag&O_NO_INSTANCES); }
	void setNoInstFlag() { o_flag |= O_NO_INSTANCES; }

        //possibly never used 
	void unsetNoInstFlag() { o_flag &= ~O_NO_INSTANCES; }

	int IsExplicit() {return(o_flag&O_EXPLICIT_LEVEL);};
	void putDeduced() {o_flag &= ~O_EXPLICIT_LEVEL;};
	void putExplicit() {o_flag |= O_EXPLICIT_LEVEL;};

    // get all System class I am ISA of 
    // Token through M4_Class, nodes and links separated 
        virtual SYSID_SET *getSysIsa(SYSID_SET *set, sys_cat*); 

    // get all System class I am instance of 
    // Token through M4_Class, nodes and links separated 
	virtual SYSID_SET *getSysInstOf(SYSID_SET *set, sys_cat *); 

    // get the level-System class I am instance of 
    // Token through M4_Class, nodes and links not separated 
    virtual SYSID getSysClass(); 

    // Am I member of this set of classes? In case of a System class, 
    // it answers yes if a ISA relation holds to one of these 
    virtual int d_belongs_to(SYSID_SET *set, sys_cat*) {
	return(set->set_member_of(o_sysid));};

    // Same for a single sysid 
    virtual int d_belongs_to(SYSID id) {
	return(o_sysid.id == id.id);};
    
    // the "copy" method copies from "cp" to the object calling, 
    // compressing and ordering the contents of the extensions 
    // caution: it allocates a new set of extensions for the   
    // calling object without deallocating the extensions of the 
    // object pointed to by "cp"                               
    virtual int copy(telos_object *) {return(-1);};
    virtual int new_copy(telos_object *) {return(-1);};

    //a complete printout of the object for debugging 
    virtual void print() {};

    // all get methods exept getTo,getFrom unite the contents of 
    // the named slot with the contents of the set in the arglist
    // the set pointer is returned again for convenience only 
    virtual SYSID_SET *getInstOf(SYSID_SET *set) {return(set);};
    virtual SYSID_SET *getInstBy(SYSID_SET *set) {return(set);};

    //next method is added by Lemonia 
    virtual SYSID_SET *getClassAttr(SYSID_SET *set) {return(set);};
    virtual SYSID_SET *getIsa(SYSID_SET *set) {return(set);};
    virtual SYSID_SET *getSubclass(SYSID_SET *set) {return(set);};
    virtual SYSID_SET *getLink(SYSID_SET *set) {return(set);};
    virtual SYSID_SET *getLinkBy(SYSID_SET *set) {return(set);};
    virtual SYSID_SET *getTrigger(SYSID_SET *set) {return(set);};
    virtual prs_val getToTagged();
    // {return(prs_val());};
    virtual SYSID getTo() {return(SYSID(0));};
    virtual SYSID getFrom(){ fprintf(stderr,"no getFrom in class with id=%x, type %x\n",o_sysid.id,o_type); return(SYSID(0));};

    // put methods put one sysid to the named slot 
    // only the "to" value of a token level attribute may 
    //contain a primitive value (float,integer,string) 
    virtual int putInstOf(SYSID ) {return(0);};
    virtual int putInstBy(SYSID ) {return(0);};

    //next method added by Lemonia 
    virtual int putClassAttr(SYSID ) {return(0);};
    virtual int putIsa(SYSID ) {return(0);};
    virtual int putSubclass(SYSID ) {return(0);};
    virtual int putLink(SYSID ) {return(0);};
    virtual int putLinkBy(SYSID ) {return(0);};
    virtual int putTrigger(SYSID ) {return(0);};
    virtual int putTo(SYSID ) {return(0);};
    virtual int putTo(prs_val *) {return(0);};//put a non sysid value 
    virtual void putFrom(SYSID ) {};

    // delete methods find and delete a sysid in the named slot 
    // To and From hold one value only, no need to specify it 
    virtual void delInstOf(SYSID ) {};
    virtual void delInstBy(SYSID ) {};
							 //next method added by Lemonia 
	virtual void delClassAttr(SYSID ) {};
    virtual void delIsa(SYSID ) {};
    virtual void delSubclass(SYSID ) {};
    virtual void delLink(SYSID ) {};
    virtual void delLinkBy(SYSID ) {};
    virtual void delTrigger(SYSID ) {};
    virtual void delTo() {};
    virtual void delFrom() {};
    */
    
    /*
    //server3.0\src\sis_kernel\objects.h
    
    	Line 417: class sys_class : public telos_object {
        #############################################################
        SYSID    inst_of[CLN_INOF_SZ];	// whome I am instance of 

        protected:
        SYSID    link[SYSCLN_LINK_SZ];	// attribute classes using "from" a system class

        public:
        sys_class(int type,SYSID id);
        sys_class();
        virtual ~sys_class() {del_ext();};
        int copy(sys_class *);
        int new_copy(sys_class *cp);
        int d_belongs_to(SYSID_SET *set, sys_cat*);
            SYSID_SET *getSysIsa(SYSID_SET *set, sys_cat*); 

        SYSID_SET *getInstOf(SYSID_SET *set);
        SYSID_SET *getLink(SYSID_SET *set);

        int putInstOf(SYSID id);
        int putLink(SYSID id);

            int existInstOf() { return( generic_is_not_empty(inst_of)); }
            int existLink() { return( generic_is_not_empty(link)); }

            void delInstOf(SYSID id);
        void delLink(SYSID id);
            void print();
        };
    
	Line 449: class class_node : public telos_object {
        #########################################################
        friend class token_node;                
        SYSID    inst_of[CLN_INOF_SZ];	// whome I am instance of 
        SYSID    inst_by[CLN_INBY_SZ];	// who is instance of me
        SYSID    isa[CLN_ISA_SZ];	// my superclasses      
        SYSID    subclass[CLN_SUBC_SZ];	// my subclasses        
        SYSID    link[CLN_LINK_SZ];	// links to other objects
        SYSID    link_by[CLN_LKBY_SZ];	// links to me		 
        SYSID    trigger[CLN_TRG_SZ];	// my trigger nodes      

        // if a class node turns out to be a token, it is transformed
        // by "fromClass" during data input. access to private class_node 
        // methods is needed for efficiency reasons 
        //friend token_node::fromClass(class_node *cp);

        public:
            class_node(SYSID id);
            virtual ~class_node() {del_ext();};
                void print();
                void stat();
            int copy(class_node *cp);
            int new_copy(class_node *cp);

            SYSID_SET *getInstOf(SYSID_SET *set);
            SYSID_SET *getInstBy(SYSID_SET *set);
            SYSID_SET *getIsa(SYSID_SET *set);
            SYSID_SET *getSubclass(SYSID_SET *set);
            SYSID_SET *getLink(SYSID_SET *set);
            SYSID_SET *getLinkBy(SYSID_SET *set);
            SYSID_SET *getTrigger(SYSID_SET *set);

            int putInstOf(SYSID id);
            int putInstBy(SYSID id);
            int putIsa(SYSID id);
            int putSubclass(SYSID id);
            int putLink(SYSID id);
            int putLinkBy(SYSID id);
            int putTrigger(SYSID id);

            int existInstOf() { return( generic_is_not_empty(inst_of)); }
            int existInstBy() { return( generic_is_not_empty(inst_by)); }
            int existIsa() { return( generic_is_not_empty(isa)); }
            int existSubclass() { return( generic_is_not_empty(subclass)); }
            int existLink() { return( generic_is_not_empty(link)); }
            int existLinkBy() { return( generic_is_not_empty(link_by)); }

            void delInstOf(SYSID id);
            void delInstBy(SYSID id);
            void delIsa(SYSID id);
            void delSubclass(SYSID id);
            void delLink(SYSID id);
            void delLinkBy(SYSID id);
            void delTrigger(SYSID id);
        };
    

	Line 505: class token_node : public telos_object {
	Line 547: class link_class : public telos_object {
	Line 617: class link_node : public telos_object {
	Line 657: class trigger_node : public telos_object {
    
    class link_class : public telos_object {
    friend class link_node;
    SYSID    from;			// where I am defined in 
    SYSID    to;			// where I am pointing to 
    SYSID    inst_of[LKC_INOF_SZ];	// whome I am instance of 
    SYSID    inst_by[LKC_INBY_SZ];	// whome is instance of me
    SYSID	 class_attr[LKC_CLATTR_SZ]; // who is class attribute of me 
    SYSID    isa[LKC_ISA_SZ];		// my superclasses        
    SYSID    subclass[LKC_SUBC_SZ];	// my subclasses          
    SYSID    link[LKC_LINK_SZ];		// links to other objects 
    SYSID    trigger[LKC_TRG_SZ];	// my trigger nodes       
    
    inline SYSID link_class::getTo() { return(to);}
    
    
    class link_node : public telos_object {
    SYSID    from;		// where I am defined in 
    TOID     to;		// where I am pointing to 
				// hold a place for primitive value!
    SYSID    inst_of[LKN_INOF_SZ];	// whome I am instance of 
    SYSID   link[LKN_LINK_SZ];		// links to other objects 
    
    inline SYSID link_node::getTo()
    {
    if (to.tg == SYSID_TAG)
        return(SYSID(to.sid));
      else
        return(SYSID(0));
    }
    */
    public enum Types {NOT_DEFINED, Type_Individual, Type_Attribute};
    public enum Levels {NOT_DEFINED, Token, S_Class, M1_Class, M2_Class, M3_Class, M4_Class};
    
    public boolean isTypeAttribute();
    public boolean isTypeIndividual();
    public boolean isTypeNotDefined();
    
    public boolean isLevelUndefined();
    public boolean isLevelToken();
    public boolean isLevelSClass();
    public boolean isLevelM1Class();
    public boolean isLevelM2Class();
    public boolean isLevelM3Class();
    public boolean isLevelM4Class();
    
    public long getNeo4jId();
    
    int getInstOf(DBaccess db, PQI_Set set);
    
    int getLinkBy(DBaccess db, PQI_Set ret);
    int getLink(DBaccess db, PQI_Set ret);
    
    //public PQI_Set getLink(DBaccess db);
     //public PQI_Set getInstOf(DBaccess db);
    //public PQI_Set getLinkBy(DBaccess db);
    
    long getTo(DBaccess db);
    long getFrom(DBaccess db);
    
    long getFromNode(DBaccess db, PQI_Set ret);
    long getToNode(DBaccess db, PQI_Set ret);
    
    @Override
    public String toString();
}
