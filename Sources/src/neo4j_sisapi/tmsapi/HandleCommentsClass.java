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
package neo4j_sisapi.tmsapi;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import neo4j_sisapi.CMValue;
import neo4j_sisapi.Identifier;
import neo4j_sisapi.IntegerObject;
import neo4j_sisapi.PrimitiveObject_Long;
import neo4j_sisapi.QClass;
import neo4j_sisapi.StringObject;


/**
 *
 * @author Elias Tzortzakakis <tzortzak@ics.forth.gr>
 */
class HandleCommentsClass {

    // <editor-fold defaultstate="collapsed" desc="C++ code...">
    /* file handle_comments.h
    #define _COMMENT		"comment"
    #define HYPERTEXT		"hyperText"
    #define PLAINTEXT		"plainText"
     */
    // </editor-fold>
    final String _COMMENT = "comment";
    final String HYPERTEXT = "hyperText";
    final String PLAINTEXT = "plainText";
    
    // <editor-fold defaultstate="collapsed" desc="C++ code...">
    /* file handle_comments.h
	sis_api *QC;
	SIS_Connection *CC;

	char ***Comments;
	int *numOfComments;
	int numOfCommentlinks;
	char comments[MAX_COM_LEN];
        int comments_size;
	PairInfo Category;
	char TargetNode[LOGINAM_SIZE];
	int is_changed;
	int commSet;
     */
    // </editor-fold>
    //QClass Q;
    ArrayList<ArrayList<StringObject>> Comments;
    IntegerObject[] numOfComments;
    int numOfCommentlinks;
    StringObject comments;  // = new char[TMSAPIClass.MAX_COM_LEN]; //in constructor
    int comments_size;
    PairInfo Category;
    String TargetNode;
    int is_changed;
    IntegerObject commSet;
    
    HandleCommentsClass(StringObject target_name, PairInfo categ){
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*--------------------------------------------------------------
        HandleCommentsClass::HandleCommentsClass()
        ----------------------------------------------------------------
        INPUT:  - target_name : the name the current object to work with
                - categ : one of the allowed categories for the creation
                  of a descriptor comment
        OUTPUT: -
        FUNCTION: constructor of class HandleCommentsClass.
                  initialization of TargetNode, Category, comments,
                  Comments and is_changed members
        ----------------------------------------------------------------*/
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*
        HandleCommentsClass::HandleCommentsClass(sis_api *qc, SIS_Connection *cc, char* target_name, PairInfo *categ)
        {
                QC = qc;
                CC = cc;

                strcpy(TargetNode, target_name);
                if (categ) {
                    strcpy(Category.label, categ ->  label);
                    strcpy(Category.name, categ ->  name);
                } else {
                    Category.label[0] = '\0';
                    Category.name[0] = '\0';
                }
                comments[0] = '\0';
                comments_size = 0;
                Comments = (char ***) 0;
                is_changed = 0;
        }
         */
        // </editor-fold>         
        
        //Q = qc;
        //sis_session = new IntegerObject(SisSessionId);
        comments    = new  StringObject();//new char[TMSAPIClass.MAX_COM_LEN];
        TargetNode  = target_name.getValue();
        if(categ != null){
            Category = new PairInfo(categ.name,categ.label);
        }
        else{
            Category = new PairInfo();
        }
        comments_size = 0;
        Comments = null;
        numOfComments = null;
        is_changed = 0;
        commSet = new IntegerObject();
    }
    
    //check
    int  AddComment(StringObject endp, String txt, StringObject end_com){
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*-----------------------------------------------------------------
        HandleCommentsClass::AddComment()
        cats the next plainText to comments char array. If this array
        is to be overflowed, it is filled by a message.
        -----------------------------------------------------------------*/
        /*--------------------------------------------------------------
        HandleCommentsClass::AddComment()
        ----------------------------------------------------------------
        INPUT:  - endp : a pointer to the next free space of comments char array
                - txt  : the next plainText to be catted to comments char array
                - end_com : a pointer to the end of comments char array
        OUTPUT: - 1 in case the given plainText is catted to comments char array succesfully
                - 0 otherwise
        FUNCTION: cats the next plainText to comments char array. If this array
                  is to be overflowed (last 300 characters), it is filled by a message.
        ----------------------------------------------------------------*/
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*
        int HandleCommentsClass::AddComment(char **endp, char *txt, char *end_com)
        {   
            int len = strlen(txt);

            if ((end_com - *endp) <= 300) {
                // comments array is to be overflowed
                sprintf(*  endp, "\nToo many comments...");
                return (0);
            }
            strcpy(*endp, txt);
            * endp += len;
            return (1);
        }
        */
        // </editor-fold>         

        //int len = txt.length();
        //Not enough storage in db array
        /*
        if ((end_com - *endp) <= 300) {
            // comments array is to be overflowed
            //sprintf(*  endp, "\nToo many comments...");
            endp.setValue("\nToo many comments...");
            return (0);
        }
        */
        String temp = new String("");
        if(endp!=null && endp.getValue()!=null)
            temp = temp.concat(endp.getValue());
        
        temp = temp.concat(txt);
        endp.setValue(temp);
        //strcpy(*endp, txt);
        //* endp += len; 
        return (1);
    }
    
    //check
    void CatComments(){
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*--------------------------------------------------------------
        HandleCommentsClass::CatComments()
        ----------------------------------------------------------------
        INPUT: -
        OUTPUT: -
        FUNCTION: scans Comments structure and cats its contents (strings) at
        comment char array by calling AddComment().
        ----------------------------------------------------------------*/
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*
        void HandleCommentsClass::CatComments()
        {
                char ***ptr1;
                char **ptr2;
                char *endp;
                //char *startp;  Used in code that separates comment text in different
                //lines
                char *end_com; // points to the end of string comments
                int i, j;// count

                endp = &  (comments[0]);
                end_com = &  (comments[MAX_COM_LEN - 1]);

                for (i = 0  , ptr1 =  & Comments[0]; i < numOfCommentlinks; ptr1++, i++) {
                    for (j = 0  , ptr2 =  * ptr1; j < numOfComments[i]; ptr2++, j++) {
                        if (!AddComment(&  endp, *  ptr2, end_com)) {
                            comments_size = strlen(comments) + 1;
                            return;
                        }
                    }
                }
                comments_size = strlen(comments) + 1;
        }
         */
        // </editor-fold>  
        
        /*
        char ***ptr1;
        char **ptr2;
        char *endp;
        */
        //char *startp;  Used in code that separates comment text in different
        //lines
        /*char *end_com; // points to the end of string comments*/
        int i, j;// count

        /*
        endp = &  (comments[0]);
        end_com = &  (comments[MAX_COM_LEN - 1]);
        
        for (i = 0  , ptr1 =  & Comments[0]; i < numOfCommentlinks; ptr1++, i++) {
            for (j = 0  , ptr2 =  * ptr1; j < numOfComments[i]; ptr2++, j++) {
                if (!AddComment(&  endp, *  ptr2, end_com)) {
                    comments_size = comments.length() + 1; // is + 1 now necessary??? \0
                    return;
                }
            }
        }*/
       
        
        for (i = 0 ; i < numOfCommentlinks; i++) {
            if(numOfComments[i]!=null){ //BUG FIX IN ORDER TO HANDLE ERROR NODES WITHOUT LABELS: Hlias 2009 - 06 - 11
                for (j = 0  ;  j < numOfComments[i].getValue(); j++) {
                    //Comments[i][j].getValue(); // == comment i part j of Target 
                    String commentPart = Comments.get(i).get(j).getValue();
                    
                    if (AddComment(comments, commentPart, null)==0) {
                        comments_size = comments.getValue().length() + 1; // is + 1 now necessary??? \0
                        return;
                    }
                }
            }
            else{
                
                Logger.getLogger(HandleCommentsClass.class.getName()).log(Level.INFO, "Warning: in CatComments null pointer occured for node " + TargetNode);
            }
        }
        comments_size = comments.getValue().length()+1;// is + 1 now necessary??? \0
    }
    
    int  CheckLabel(String label, int maxNum){
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*--------------------------------------------------------------
        CheckLabel()
        ----------------------------------------------------------------
        INPUT:  - label : a string containing an integer
                - maxNum : an integer with the maximum allowed value of label
        OUTPUT: - atoi(label)-1 in case the label's integer value is greater than
                  1 and less than maxNum
                - -1 otherwise
        FUNCTION: checks if the label's integer value is greater than 1 and less than maxNum
        ----------------------------------------------------------------*/
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*in file handle_comm.cpp
        int CheckLabel(char *label, int maxNum)
        {
            int i;

            if ((i = atoi(label)) < 1 || i > maxNum) {
               return(-1);
            }
            return(i-1);
        }
         */
        // </editor-fold>         

        int i = Integer.parseInt(label);
       
        if (i < 1 || i > maxNum) {
            return (-1);
        }
        return (i - 1);
    }
    
    //todo
    int  CommitComment(QClass Q, StringObject targetNode,StringObject newComment){
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
         /*--------------------------------------------------------------
        HandleCommentsClass::CommitComment()
        ----------------------------------------------------------------
        INPUT: - targetNode : the name of an object
        - newComment : a new comment value
        OUTPUT: - TMS_APISucc in case the given new comment value is added
        to targetNode succesfully
        - TMS_APIFail otherwise
        FUNCTION: Removes first the comment of the given object and then
        adds the given new comment value to it.
        ----------------------------------------------------------------*/
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*
        int HandleCommentsClass::CommitComment(char *targetNode, char *newComment)
        {
                if (removeComment() == TMS_APIFail) {
                    return TMS_APIFail;
                }
                if (!newComment) { // this function was called for the deletion of this comment
                    comments[0] = '\0';
                    comments_size = 0;
                    is_changed = 0;
                    return TMS_APISucc;
                }
                if (parseCommentFile(QC, CC, targetNode, newComment, Category.name, Category.label) == TMS_APIFail) {
                    return TMS_APIFail;
                }
                comments[0] = '\0';
                comments_size = 0;
                is_changed = 0;
                return TMS_APISucc;
        }
        */
        // </editor-fold> 

        if (removeComment(Q) == TMSAPIClass.TMS_APIFail) {
            return TMSAPIClass.TMS_APIFail;
        }
        if (newComment == null) { // this function was called for the deletion of this comment
            //comments[0] = '\0';
            comments = null;
            comments_size = 0;
            is_changed = 0;
            return TMSAPIClass.TMS_APISucc;
        }
        if (parseCommentFile(Q, targetNode, newComment, Category.name, Category.label) == TMSAPIClass.TMS_APIFail) {
            return TMSAPIClass.TMS_APIFail;
        }
        //comments[0] = '\0';
        comments = null;
        comments_size = 0;
        is_changed = 0;
                
        return TMSAPIClass.TMS_APISucc;
    }
    
    int  editComment(QClass Q){
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*--------------------------------------------------------------
        HandleCommentsClass::editComment()
        ----------------------------------------------------------------
        INPUT: -
        OUTPUT: - TMS_APISucc the specified comments of current
                  object are get succesfully
                - TMS_APIFail otherwise
        FUNCTION: collects the specified comments of current object
        ----------------------------------------------------------------*/
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*in file handle_comm.cpp
        int HandleCommentsClass::editComment()
        {
                //char command[MAX_STR_LENGTH];
                //char tmp_file1[FILENAME_LENGTH];
                //   char *tmp_file1;

                // Finish the query server session
                QC->end_query();

                if (QC ->  begin_query() == APIFail) {
                    fprintf(stderr, "HandleCommentsClass::editComment(): failed to begin_query()\n");
                    return TMS_APIFail;
                }

                if (HasComments()) {
                    if (IsChanged() == 0) {
                        if (GetComments() == -1) {
                            fprintf(stderr, "Cannot get comments from node <%s>\n", TargetNode);
                            return TMS_APIFail;
                        }
                        CatComments();  // copies Comments buffers to comment
                    }
                }

                // Finish the query server session
                if (CC ->  end_query() == APIFail) {
                    fprintf(stderr, "HandleCommentsClass::editComment(): failed to end_query()\n");
                    return TMS_APIFail;
                }
                return TMS_APISucc;
        }
        */
        // </editor-fold> 
      
        //char command[MAX_STR_LENGTH];
        //char tmp_file1[FILENAME_LENGTH];
        //   char *tmp_file1;

        
        /* BUG FIX NO NEED TO END AND START QUERY. USER MUST ALWAYS BE IN A QUERY_SESSION
        // Finish the query server session
        Q.end_query(); //WHY???

        if (Q.begin_query() == QClass.APIFail) {
            //fprintf(stderr, "HandleCommentsClass::editComment(): failed to begin_query()\n");
            System.err.println("HandleCommentsClass::editComment(): failed to begin_query()\n");
            return TMSAPIClass.TMS_APIFail;
        }
        */
 
        if (HasComments(Q)!=0) {
            if (IsChanged() == 0) {
                if (GetComments(Q) == -1) {
                    //fprintf(stderr, "Cannot get comments from node <%s>\n", TargetNode);
                    System.err.println("Cannot get comments from node " + TargetNode);
                    return TMSAPIClass.TMS_APIFail;
                }
                CatComments();  // copies Comments buffers to comment
            }
        }

       /* BUG FIX NO NEED TO END AND START  QUERY EARLIER
        // Finish the query server session
        if (Q.end_query() == QClass.APIFail) {
            //fprintf(stderr, "HandleCommentsClass::editComment(): failed to end_query()\n");
            System.err.println("HandleCommentsClass::editComment(): failed to end_query()\n");
            return TMSAPIClass.TMS_APIFail;
        }
        */
        return TMSAPIClass.TMS_APISucc;
    }
    
    void fillStringWithComment(StringObject str){
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /* file handle_comments.h
        void fillStringWithComment(char *string) {strcpy(string, comments);}
        */
        // </editor-fold>
        str.setValue(comments.getValue());
    }
    
    int  GetComments(QClass Q){
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*--------------------------------------------------------------
        HandleCommentsClass::GetComments()
        ----------------------------------------------------------------
        INPUT: -
        OUTPUT: - 0 in case the comment links of current node are get succesfully
                - -1 otherwise
        FUNCTION: gets the comment links of current node, and then gets the
                  plainText of each hyperText which is pointed by the above links
                  (see the model). These strings are stored in Comments (char***)
                  and they are sorted firstly by the label of theis comment link
                  and secondly by the label of each hyperText. The above labels
                  must be numeric and counted from 1 to the number of the set of
                  the corresponding links. Otherwise, CheckLabel() returns error.
        
        commSet                : set of comment links of current node.
        commParts              : set of links pointing to plainText.
        numOfCommentlinks (int): number of comment links of current node.
        numOfComments (int*)   : number of plainTexts of each hyperText.
        ----------------------------------------------------------------*/
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*
        int HandleCommentsClass::GetComments()
        {
                int commParts, numOfCommentParts, comLinksIndex, comPartsIndex;
                l_name cls, linkLabel, partLabel;
                cm_value cmv, cmv2;

                QC->reset_set(commSet);

                Comments = (char ***) calloc(numOfCommentlinks, sizeof(char **));
                if (Comments == (char ***) 0) {
                    fprintf(stderr, "No space available for creation of comments.\n");
                    fprintf(stderr, "In HandleCommentsClass::GetComments().\n");
                    numOfCommentlinks = 0;
                    exit(1);
                }
                numOfComments = (int *) calloc(numOfCommentlinks, sizeof(int));
                if (numOfComments == (int *) 0) {
                    fprintf(stderr, "No space available for creation of comments.\n");
                    fprintf(stderr, "In HandleCommentsClass::GetComments().\n");
                    exit(1);
                }

                comLinksIndex = 0;
                while (QC ->  return_link(commSet, cls, linkLabel, &  cmv) != -1) {
                    // for each comment link

                    QC->reset_name_scope();
                    QC->set_current_node(cmv.value.s);

                    commParts = QC ->  get_link_from_by_category(0, HYPERTEXT, PLAINTEXT);

                    if ((numOfCommentParts = QC ->  set_get_card(commParts)) <= 0) {
                        continue; // no comment parts for current comment link
                    }
                    Comments[comLinksIndex] = (char **) calloc(numOfCommentParts, sizeof(char *));
                    if (Comments[comLinksIndex] == (char **) 0) {
                        fprintf(stderr, "No space available for creation of comments.\n");
                        fprintf(stderr, "In HandleCommentsClass::GetComments().\n");
                        numOfCommentParts = 0;
                        exit(1);
                    }

                    comPartsIndex = -1;	// in case of links without label
                    QC->reset_set(commParts);
                    while (QC ->  return_link(commParts, cls, partLabel, &  cmv2) != -1) {
                        // for each comment part

                        if (!strncmp(partLabel, "Label", 5)) { // link without label
                            comPartsIndex++;
                        } else { // link has label
                            if ((comPartsIndex = CheckLabel(partLabel, numOfCommentParts)) == -1) {
                                fprintf(stderr, "The link of %s with category plainText, has wrong label: %s.\nIt should be >=1 and <=%d. Take into account \n that labels in comments is a feature not a bug", cmv.value.s, partLabel, numOfCommentParts);
                                Comments[comLinksIndex] = (char **) 0;
                                numOfCommentParts = 0;
                                return (-1);
                            }
                        }
                        Comments[comLinksIndex][comPartsIndex] = cmv2.value.s;
                    }
                    numOfComments[comLinksIndex] = numOfCommentParts;
                    QC->free_set(commParts);
                    comLinksIndex++;

                    // free allocated space ONLY for cmv
                    switch (cmv.type) {
                        case TYPE_STRING:
                        case TYPE_NODE:
                            free(cmv.value.s);
                    }
                }
                QC->free_set(commSet);
                return (0);
        } 
         */
        // </editor-fold>         

        int commParts, numOfCommentParts, comLinksIndex, comPartsIndex;
        StringObject cls = new StringObject();
        StringObject linkLabel = new StringObject();
        StringObject partLabel = new StringObject();

        CMValue cmv = new CMValue();
        CMValue cmv2 = new CMValue();

        Q.reset_set(commSet.getValue());
        Comments = new ArrayList<ArrayList<StringObject>>();
        for(int i=0; i< numOfCommentlinks; i++){
                Comments.add(new ArrayList<StringObject>());
            }
        //Comments.setSize(numOfCommentlinks);
        //Comments = new StringObject[numOfCommentlinks][numOfCommentParts];
        //Comments = (char ***) calloc(numOfCommentlinks, sizeof(char **));
        //if (Comments == (char ***) 0) {
        if(Comments == null){
            //fprintf(stderr, "No space available for creation of comments.\n");
            //fprintf(stderr, "In HandleCommentsClass::GetComments().\n");
            System.err.println("No space available for creation of comments.\nIn HandleCommentsClass::GetComments().\n");
            numOfCommentlinks = 0;
            System.exit(1);
        }
        numOfComments = new IntegerObject[numOfCommentlinks]; 
        //(int *) calloc(numOfCommentlinks, sizeof(int));
        if (numOfComments == null) {
            //fprintf(stderr, "No space available for creation of comments.\n");
            //fprintf(stderr, "In HandleCommentsClass::GetComments().\n");
            System.err.println("No space available for creation of comments.\nIn HandleCommentsClass::GetComments().\n");
            System.exit(1);
        }

        comLinksIndex = 0;
        ArrayList<String> collectNodes = new ArrayList<String>();
        while (Q.return_link(commSet.getValue(), cls, linkLabel, cmv) != -1) {
            // for each comment link
            collectNodes.add(cmv.getString());
        }
        
        for(String collectNode : collectNodes){
            
            Q.reset_name_scope();
            Q.set_current_node(new StringObject(collectNode));
            commParts = Q.get_link_from_by_category(0, new StringObject(HYPERTEXT), new StringObject(PLAINTEXT));
            if ((numOfCommentParts = Q.set_get_card(commParts)) <= 0) {
                Q.free_set(commParts);
                continue; // no comment parts for current comment link
            }

            ArrayList<StringObject> temp = new ArrayList<>();
            for(int i=0; i< numOfCommentParts; i++){
                temp.add(new StringObject(""));
            }
            //temp.setSize(numOfCommentParts);
            Comments.set(comLinksIndex, temp);// = new ArrayList<StringObject>();
            //Comments[comLinksIndex] = (char **) calloc(numOfCommentParts, sizeof(char *)); //Both Comment's array dimensions initialized above
            if (Comments.get(comLinksIndex) == null) {
                //fprintf(stderr, "No space available for creation of comments.\n");
                //fprintf(stderr, "In HandleCommentsClass::GetComments().\n");
                System.err.println("No space available for creation of comments.\nIn HandleCommentsClass::GetComments().\n");
                numOfCommentParts = 0;
                Q.free_set(commParts);
                System.exit(1);
            }
            
            comPartsIndex = -1;	// in case of links without label
            Q.reset_set(commParts);
            while (Q.return_link(commParts, cls, partLabel, cmv2) != -1) {
                // for each comment part

                //if (!strncmp(partLabel, "Label", 5)) { // link without label
                if(!partLabel.getValue().startsWith("Label")){
                    comPartsIndex++;
                } else {//link has label
                    if ((comPartsIndex = CheckLabel(partLabel.getValue(), numOfCommentParts)) == -1) {
                        //fprintf(stderr, "The link of %s with category plainText, has wrong label: %s.\nIt should be >=1 and <=%d. Take into account \n that labels in comments is a feature not a bug", cmv.value.s, partLabel, numOfCommentParts);
                        System.err.println("The link of " + collectNode+ " with category plainText, has wrong label: " + partLabel.getValue() + ".\nIt should be >=1 and <=" + numOfCommentParts + ". Take into account \n that labels in comments is a feature not a bug" );
                        //Comments[comLinksIndex] = (char **) 0;
                        Comments.set(comLinksIndex,null);
                        numOfCommentParts = 0;
                        Q.free_set(commParts);
                        return (-1);
                    }
                }
                Comments.get(comLinksIndex).set(comPartsIndex, new StringObject(cmv2.getString()));
            }
            numOfComments[comLinksIndex] = new IntegerObject(numOfCommentParts);
            Q.free_set(commParts);
            comLinksIndex++;
        }
           

            

            
            

            /*
            // free allocated space ONLY for cmv
            switch (cmv.getType()) {
                case CMValue.TYPE_STRING:
                case CMValue.TYPE_NODE:
                    free(cmv.value.s);
            }
            */
        
       
        Q.free_set(commSet.getValue());
        return (0);
   
    }
    
    int  GetCommentSize(){
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /* file handle_comments.h
        int GetCommentSize() {return comments_size;};
        */
        // </editor-fold>        
        return comments_size;
    }
    
    
    int  HasComments(QClass Q){
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*--------------------------------------------------------------
        HandleCommentsClass::HasComments()
        ----------------------------------------------------------------
        INPUT: -
        OUTPUT: - 1 in case current object TargetNode has at least one link
                  pointing from it, under category (Individual, comment)
                - 0 otherwise
        FUNCTION: checks if current object TargetNode has at least one link
                  pointing from it, under category (Individual, comment)
        ATTENTION: commSet is not freed so as to be used ..
        ----------------------------------------------------------------*/
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*in file handle_comm.cpp
        int HandleCommentsClass::HasComments()
        {
            QC->reset_name_scope();
            QC->set_current_node(TargetNode);

            if (Category.label[0] == '\0') {
                commSet = QC->get_link_from_by_category (0, INDIVIDUAL, _COMMENT);
            } else {
                commSet = QC->get_link_from_by_category (0, Category.name, Category.label);
            }

            if (commSet == -1)
                return 0;

            if ((numOfCommentlinks = QC->set_get_card(commSet)) <= 0 ) {
                // fprintf(stderr, "No comments...");
                return(0); // no comment links associated with current node
            }
            return(1);
        }
        */
        // </editor-fold>         


        Q.reset_name_scope();
        long ssysidL = Q.set_current_node(new StringObject(TargetNode));

        if (Category.label == null || Category.label.getValue().length()==0) {
            commSet.setValue(Q.get_link_from_by_category(0,new StringObject(TMSAPIClass.INDIVIDUAL), new StringObject(_COMMENT)));
        } else {
            commSet.setValue(Q.get_link_from_by_category(0, Category.name, Category.label));
        }

        if (commSet.getValue() == -1)
            return 0;
        Q.reset_set(commSet.getValue());
        if ((numOfCommentlinks = Q.set_get_card(commSet.getValue())) <= 0 ) {
            //fprintf(stderr, "No comments...");
            //System.err.println("No comments");
            return(0); // no comment links associated with current node
        }

        return (1);
    }

    int  IsChanged() { 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /* file handle_comments.h
        int IsChanged() { return is_changed; }
        */
        // </editor-fold>
        return is_changed; 
    }
     

    int tellLink(QClass Q, StringObject from, StringObject categ, StringObject from_cls, StringObject to_value, StringObject label, int type){
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*--------------------------------------------------------------
        void tellLink ()
        ----------------------------------------------------------------
        INPUT:  - from : the name of an object
                - categ : a comment category
                - from_cls : a comment category from class          
                - to_value : a new comment value
                - label : the label of the link to be added
                - type : TYPE_STRING or TYPE_NODE
        OUTPUT: - TMS_APISucc in case the given new link is added
                  to targetNode succesfully
                - TMS_APIFail otherwise
        FUNCTION: adds the given new link to targetNode under the given category.
        ----------------------------------------------------------------*/
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*in file handle_comm.cpp
        int tellLink(sis_api *QC, SIS_Connection *CC, char *from, char *categ, char* from_cls, char* to_value, LOGINAM* label, short type)
        {
            int namedLink = (label != NULL);

           // prepare the from value
            int from_sysid;
            QC->get_classid(from, &from_sysid);
            IDENTIFIER I_from(from_sysid);

            // prepare the to value
            cm_value to_cmv;
            if (type == TYPE_STRING) {
                QC->assign_string(&to_cmv, to_value);
            }
            else {
                int to_sysid;
                QC->get_classid(to_value, &to_sysid);
                QC->assign_node(&to_cmv, to_value, to_sysid);
            }

            // prepare the set with the categories that the link will be under
            int categ_sysid;
            QC->get_linkid(from_cls, categ, &categ_sysid);
            int categ_set_id;
            if (QC->set_current_node_id(categ_sysid) < 0) {
                fprintf (stderr, "tellLink: failed to set_current_node for <%d>\n",categ_sysid);
                return TMS_APIFail;
            }
            if ((categ_set_id = QC->set_get_new()) < 0) {
                fprintf (stderr, "tellLink: failed to set_get_new()\n");
                return TMS_APIFail;
            }
            if (QC->set_put(categ_set_id) < 0) {
                fprintf (stderr, "tellLink: failed to set_put()\n");
                QC->free_set(categ_set_id);
                return TMS_APIFail;
            }

            // add the link
            char message[1024];
            if (namedLink) {
                // prepare the link to be added
                char labelName[LOGINAM_SIZE];
                IDENTIFIER I_link(label->string(labelName));

                if (QC->Add_Named_Attribute(&I_link, &I_from, &to_cmv, -1, categ_set_id) == APIFail) {
                    fprintf(stderr, "tellLink : failed to Add_Named_Attribute()\n");
                    QC->free_set(categ_set_id);
                    return TMS_APIFail;
                }
            }
            else {
                if (QC->Add_Unnamed_Attribute(&I_from, &to_cmv, categ_set_id) == APIFail) {
                    fprintf(stderr, "tellLink : failed to Add_Unnamed_Attribute()\n");
                    QC->free_set(categ_set_id);
                    return TMS_APIFail;
                }
            }

            QC->free_set(categ_set_id);
            return TMS_APISucc;
        }
        */
        // </editor-fold> 

        int namedLink; 
        if(label != null && label.getValue() != null){
            namedLink = 1;
        }
        else{
            namedLink = 0;
        }
        
        // prepare the from value
        PrimitiveObject_Long from_sysid = new PrimitiveObject_Long();
        Q.get_classid( from, from_sysid);
        Identifier I_from = new Identifier(from_sysid.getValue());

        // prepare the to value
        CMValue to_cmv = new CMValue();
        
        if (type == CMValue.TYPE_STRING) {
            
            to_cmv.assign_string(to_value.getValue());
            
        } else {
            
            PrimitiveObject_Long to_sysid = new PrimitiveObject_Long();
            Q.get_classid( to_value, to_sysid);
            to_cmv.assign_node(to_value.getValue(), to_sysid.getValue());
            
        }

        // prepare the set with the categories that the link will be under
        PrimitiveObject_Long categ_sysid = new PrimitiveObject_Long();
        Q.get_linkid( from_cls, categ, categ_sysid);
        int categ_set_id;
        if (Q.set_current_node_id(categ_sysid.getValue()) < 0) {
            //fprintf (stderr, "tellLink: failed to set_current_node for <%d>\n",categ_sysid);
            System.err.println("tellLink: failed to set_current_node for " + categ_sysid.getValue() + "\n");
            return TMSAPIClass.TMS_APIFail;
        }
        if ((categ_set_id = Q.set_get_new()) < 0) {
            //fprintf (stderr, "tellLink: failed to set_get_new()\n");
            System.err.println("tellLink: failed to set_get_new()\n");
            return TMSAPIClass.TMS_APIFail;
        }
        if (Q.set_put(categ_set_id) < 0) {
            //fprintf (stderr, "tellLink: failed to set_put()\n");
            System.err.println("tellLink: failed to set_put()\n");
            Q.free_set(categ_set_id);
            return TMSAPIClass.TMS_APIFail;
        }

        // add the link
        //char message[1024];
        if (namedLink != 0) {
            
            // prepare the link to be added
            Identifier I_link = new Identifier(label.getValue());

            if (Q.CHECK_Add_Named_Attribute(I_link, I_from, to_cmv, -1, categ_set_id,false) == QClass.APIFail) {
                //fprintf(stderr, "tellLink : failed to Add_Named_Attribute()\n");
                System.err.println("tellLink : failed to Add_Named_Attribute()\n");
                Q.free_set(categ_set_id);
                return TMSAPIClass.TMS_APIFail;
            }
        } else {
            if (Q.CHECK_Add_Unnamed_Attribute(I_from, to_cmv, categ_set_id) == QClass.APIFail) {
                //fprintf(stderr, "tellLink : failed to Add_Unnamed_Attribute()\n");
                System.err.println("tellLink : failed to Add_Unnamed_Attribute()\n");
                Q.free_set(categ_set_id);
                return TMSAPIClass.TMS_APIFail;
            }
        }

        Q.free_set(categ_set_id);
        return TMSAPIClass.TMS_APISucc;
    }

     //to do
    int  parseCommentFile(QClass Q, StringObject targetNode, StringObject newComment, StringObject category_from, StringObject category ){
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*--------------------------------------------------------------
        parseCommentFile()
        ----------------------------------------------------------------
        INPUT: - targetNode : the name of an object
        - newComment : a new comment value
        - category_from : a comment category from class
        - category : a comment category
        OUTPUT: - TMS_APISucc in case the given new comment value is added
        to targetNode succesfully
        - TMS_APIFail otherwise
        FUNCTION: adds the given new comment value to targetNode under
        the given comment category.
        ----------------------------------------------------------------*/
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*in file handle_comm.cpp
        int parseCommentFile(sis_api *QC, SIS_Connection *CC, char *targetNode, char *newComment,  char *category_from, char *category)
        {
                char	c;
                char	comment[MAX_COM_LEN];
        //	char	node_nm[LOGINAM_SIZE+1];
                char	comm_nm[LOGINAM_SIZE];
                int   ret_value, i, j, counter;
                char	target[LOGINAM_SIZE];
                char	item[MAX_STRING];
                LOGINAM* hyperText_lp;
                LOGINAM* node_nm_lp;
                LOGINAM* comm_lp;

                // get the name of the node in which the comment will be added
                // ret_value = getNodeName(comm_fp, node_nm, LOGINAM_SIZE);

                // set current node the node in which the comment will be added
                QC->reset_name_scope();
                if (QC->set_current_node(targetNode) == -1 ) {
                    fprintf (stderr," No such node <%s> in the database...\n",targetNode);
                    return TMS_APIFail;
                }
        //
        //   i = 0;
        //   // copy the contents of the file into the comment char array
        //   // up to MAX_COM_LEN characters
        // 
        //    while (!feof(comm_fp)) {
        //        c = getc(comm_fp);
        //        if (i+1 >= MAX_COM_LEN) {
        //            fprintf (stderr, "\n  Error while parsing one comment... array comment excceded...\n");
        //            break;
        //        }
        //        if (!feof(comm_fp)) {
        //            comment[i++] = c;
        //        }
        //    }
        //   
        //    fclose(comm_fp);
        //    comment[i] = '\0';
        //
        //    // remove trailing blanks
        //    i = strlen(comment) - 1;
        //    while (comment[i] == ' ' || comment[i] == '\t' || comment[i] == '\n') {
        //        comment[i--] = '\0';
        //    }
        //
        //    // check for empty comments
        //    if (strlen(comment) == 0) {
        //        fprintf(stderr, "No comments parsed for node <%s>\n", node_nm);
        //        return ERROR;
        //    }
        //

                // create the node: target`comment in Token level
                HyperTextName(targetNode, category,comm_nm);

                IDENTIFIER I_Target_Comment(comm_nm);
                if(QC->Add_Node(&I_Target_Comment, SIS_API_TOKEN_CLASS) == APIFail) {
                    fprintf(stderr, "parseCommentFile : Failed to Add_Node %s .\n", I_Target_Comment.loginam);
                    return TMS_APIFail ;
                }

                IDENTIFIER I_class(HYPERTEXT);
                if (QC->Add_Instance(&I_Target_Comment, &I_class) == APIFail) {
                    fprintf(stderr,"parseCommentFile : failed to Add_Instance\n");
                    return TMS_APIFail;
                }

                // break the comment to parts of MAX_STRING charactrers
                // and link them to the target with "plainText" links
                // with numbered labels begining from 1
                j = strlen(newComment);
                i = 0;
                int label_index = 1;
                while (1) {
                    getNextItem(newComment, item, &i);
                    static char label[64];
                    sprintf(label, "%d ", label_index++);
                    LOGINAM* label_name = new LOGINAM(label);
                    //checkNew(label_name, "parseCommentFile()");
                    if (tellLink(QC, CC, comm_nm, "plainText", HYPERTEXT, item, label_name, TYPE_STRING) == TMS_APIFail) {
                        return TMS_APIFail;
                    }
                        if (i >= j ) 
                            break;	// stop when the index reaches the end of comment.
                }

                // add the unnamed link from target-> target`comment
                // under category "comment"
                //if (tellLink(node_nm, _COMMENT, INDIVIDUAL, comm_nm, NULL, NODE) == ERROR) {
                if (tellLink(QC, CC, targetNode, category, category_from, comm_nm, NULL, TYPE_NODE) == TMS_APIFail) {
                    return TMS_APIFail;
                }

                return TMS_APISucc;
        }
         */
        // </editor-fold> 
        
        char c;
        //StringObject comment = new StringObject();      //not needed
        StringObject comm_nm = new StringObject();
        //StringObject target = new StringObject();       //not needed
        //StringObject item = new StringObject();
        //StringObject hyperText_lp = new StringObject(); //not needed
        //StringObject node_nm_lp = new StringObject();   //not needed
        //StringObject comm_lp = new StringObject();      //not needed
        //int   ret_value,  counter;                      //not needed
        int j;
        IntegerObject i = new IntegerObject();

        // get the name of the node in which the comment will be added
        // ret_value = getNodeName(comm_fp, node_nm, LOGINAM_SIZE);

        // set current node the node in which the comment will be added
        Q.reset_name_scope();
        if (Q.set_current_node(targetNode) == -1 ) {
            //fprintf (stderr," No such node <%s> in the database...\n",targetNode);
            System.err.println("Δεν βρέθηκε όρος " + targetNode.getValue() + " στην βάση δεδομένων.");
            return TMSAPIClass.TMS_APIFail;
        }
//
//   i = 0;
//   // copy the contents of the file into the comment char array
//   // up to MAX_COM_LEN characters
// 
//    while (!feof(comm_fp)) {
//        c = getc(comm_fp);
//        if (i+1 >= MAX_COM_LEN) {
//            fprintf (stderr, "\n  Error while parsing one comment... array comment excceded...\n");
//            break;
//        }
//        if (!feof(comm_fp)) {
//            comment[i++] = c;
//        }
//    }
//   
//    fclose(comm_fp);
//    comment[i] = '\0';
//
//    // remove trailing blanks
//    i = strlen(comment) - 1;
//    while (comment[i] == ' ' || comment[i] == '\t' || comment[i] == '\n') {
//        comment[i--] = '\0';
//    }
//
//    // check for empty comments
//    if (strlen(comment) == 0) {
//        fprintf(stderr, "No comments parsed for node <%s>\n", node_nm);
//        return ERROR;
//    }
//

        // create the node: target`comment in Token level
        HyperTextName(targetNode, category,comm_nm);

        Identifier I_Target_Comment = new Identifier(comm_nm.getValue());
        if(Q.CHECK_Add_Node(I_Target_Comment, QClass.SIS_API_TOKEN_CLASS,true) == QClass.APIFail) {
            //fprintf(stderr, "parseCommentFile : Failed to Add_Node %s .\n", I_Target_Comment.loginam);
            System.err.println("parseCommentFile : Failed to Add_Node " + I_Target_Comment.getLogicalName()+".\n");
            return TMSAPIClass.TMS_APIFail ;
        }

        Identifier I_class = new Identifier(HYPERTEXT);
        if (Q.CHECK_Add_Instance(I_Target_Comment, I_class) == QClass.APIFail) {
            //fprintf(stderr,"parseCommentFile : failed to Add_Instance\n");
            System.err.println("parseCommentFile : failed to Add_Instance\n");
            return TMSAPIClass.TMS_APIFail;
        }

        // break the comment to parts of MAX_STRING charactrers
        // and link them to the target with "plainText" links
        // with numbered labels begining from 1
        j = newComment.getValue().length();
        i.setValue(0);
        int label_index = 1;
        //while (true) {
            //getNextItem(newComment.getValue(), item, i);
            StringObject label_name = new StringObject(String.valueOf(label_index));
            label_index++;
           
            //checkNew(label_name, "parseCommentFile()");
            if (tellLink(Q,comm_nm, new StringObject("plainText"), new StringObject(HYPERTEXT), newComment, label_name, CMValue.TYPE_STRING) == TMSAPIClass.TMS_APIFail) {
                return TMSAPIClass.TMS_APIFail;
            }
          //      if (i.getValue() >= j ) 
          //          break;	// stop when the index reaches the end of comment.
            
        //}

        // add the unnamed link from target-> target`comment
        // under category "comment"
        //if (tellLink(node_nm, _COMMENT, INDIVIDUAL, comm_nm, NULL, NODE) == ERROR) {
        if (tellLink(Q,targetNode, category, category_from, comm_nm, null, CMValue.TYPE_NODE) == TMSAPIClass.TMS_APIFail) {
            return TMSAPIClass.TMS_APIFail;
        }
        return TMSAPIClass.TMS_APISucc;
    }

    void ABANDONED_getNextItem(String text, StringObject item, IntegerObject pos) {        
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*--------------------------------------------------------------
        getNextItem()
        ----------------------------------------------------------------
        INPUT: - text : a string
               - item : a char pointer to be filled with the next chunk
                 of maximum length MAX_STRING chars
        OUTPUT: -
        FUNCTION: divides the plain text in chunks of maximum length
                  MAX_STRING chars. It deletes any blanks at the beginning
                  of the item. Fills pointer item with the next item.
        ----------------------------------------------------------------*/
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code..."> 
        /*in file handle_comm.cpp
        void getNextItem (char* text, char* item, int* pos)
        {
            int j = 0;

            while (text[*  pos] == ' ') {
                (*  pos)++;
            }
            while (j < MAX_STRING && text[*  pos] != '\0') {
                item[j++] = text[*  pos];
                (*  pos)++;
            }
            if (j == MAX_STRING) {
                item[j - 1] = '\0';
                (*  pos)--;
            } else {
                item[j] = '\0';
            }
        }
        */
        // </editor-fold> 
        int j = 0;
        int temp;
        while(text.charAt(pos.getValue())==' '){
            temp = pos.getValue();
            temp++;
            pos.setValue(temp);           
        }
        
        //copies text to item.value
        int strLen = text.length();
        String itemValue = new String();
        
        //while(j<TMSAPIClass.MAX_STRING && pos.getValue() < strLen){
        while(j<500 && pos.getValue() < strLen){
            itemValue+= text.charAt(pos.getValue());
            temp = pos.getValue();
            temp++;
            pos.setValue(temp);
            j++;
        }
        
        /* Not needed in Java
        if (j == MAX_STRING) {
            item[j - 1] = '\0';
            (*  pos)--;
        } else {
            item[j] = '\0';
        }
        */
        item.setValue(itemValue);
    }
    
    void HyperTextName( StringObject node, StringObject categ, StringObject ret_name) {        
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*--------------------------------------------------------------
        HyperTextName()
        ----------------------------------------------------------------
        INPUT:  - node : the name of an object
                - categ : the name of a category
                - ret_name : an allocated string
        OUTPUT: -
        FUNCTION: fills the given allocated string ret_name with the
                  string <node>`<categ>
        ATTENTION: the given string ret_name must be previously allocated
        ----------------------------------------------------------------*/
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*in file handle_comm.cpp
        void HyperTextName(char* node, char* categ, char* ret_name)
        {
            sprintf(ret_name, "%s`%s", node, (categ[0] == '\0') ? _COMMENT : categ);
        }
         */
        // </editor-fold>  
        if(categ==null || categ.getValue().length()==0)
            ret_name.setValue(node.getValue().concat("`").concat(_COMMENT));
        else
            ret_name.setValue(node.getValue().concat("`").concat(categ.getValue()));
    }
   
    int  removeComment(QClass Q){
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*--------------------------------------------------------------
        HandleCommentsClass::removeComment()
        ----------------------------------------------------------------
        INPUT: -
        OUTPUT: - TMS_APISucc in case the specified comments of current
        object are deleted succesfully
        - TMS_APIFail otherwise
        FUNCTION: deletes the specified comments of current object
        ----------------------------------------------------------------*/
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*
        int HandleCommentsClass::removeComment()
        {
                int link_to_be_deleted;
                int commentSet, nodeSet;
                int card;

                QC->reset_name_scope();
                if (QC ->  set_current_node(TargetNode) < 0) {
        //      if (SetNewCurrentNode(TargetNode) == ERROR) {
                    #ifdef DEBUG_FLAG
                            fprintf(stderr, "HandleCommentsClass -- removeComment : Failed to SetNewCurrentNode() \n");
                    #endif

                    return TMS_APIFail;
                }
                if (Category.label[0] == '\0') {
                    commentSet = QC ->  get_link_from_by_category(DEFAULT_NODE, INDIVIDUAL, _COMMENT);
                } else {
                    commentSet = QC ->  get_link_from_by_category(DEFAULT_NODE, Category.name, Category.label);
                }
                if (commentSet == -1) {
                    // No attribute for (Category.name, Category.label) or (INDIVIDUAL, _COMMENT)
                    return TMS_APISucc;
                }

                card = QC ->  set_get_card(commentSet);
                if (card == 0) {
                    // No comment found
                    // That's OK here
                    QC->free_set(commentSet);
                    return TMS_APISucc;
                }
                nodeSet = QC ->  get_to_value(commentSet);
                if (nodeSet == -1) {
                    // There should exist nodes
                    QC->free_set(commentSet);
                    return TMS_APIFail;
                }
                card = QC ->  set_get_card(nodeSet);
                if (card == 0) {
                    // No comment found !!!
                    // This should never happen. The routine should have returned ealrier
                    QC->free_set(commentSet);
                    QC->free_set(nodeSet);
                    return TMS_APIFail;
                }
                link_to_be_deleted = QC ->  get_link_from_by_category(nodeSet, HYPERTEXT, PLAINTEXT);
                if (link_to_be_deleted == -1) {
                    #ifdef DEBUG_FLAG
                        fprintf(stderr, "HandleCommentsClass -- removeComment : Failed to get_link_from_by_category() \n");
                    #endif 
                    QC->free_set(commentSet);
                    QC->free_set(nodeSet);
                    QC->free_set(link_to_be_deleted);
                    return TMS_APIFail;
                }
                QC->set_union(link_to_be_deleted, commentSet) ;
                QC->free_set(commentSet);
                if (DeleteLinksInSet(QC, CC, link_to_be_deleted) == TMS_APIFail) {
                    #ifdef DEBUG_FLAG
                        fprintf(stderr, "HandleCommentsClass -- removeComment : Failed to DeleteLinksInSet() \n");
                        fprintf(stderr, "HandleCommentsClass -- removeComment() : failed to remove links of '%s' category\n",(Category.label[0] == EOS) ? _COMMENT : Category.label);
                    #endif 
                    QC->free_set(nodeSet);
                    QC->free_set(link_to_be_deleted);
                    return TMS_APIFail;
                }
                QC->free_set(link_to_be_deleted);
                if (DeleteNodesInSet(QC, CC, nodeSet) == TMS_APIFail) {
                    #ifdef DEBUG_FLAG
                        fprintf(stderr, "HandleCommentsClass -- removeComment : Failed to DeleteNodesInSet() \n");
                        fprintf(stderr, "HandleCommentsClass -- removeComment() : failed to remove links of '%s' category\n",(Category.label[0] == EOS) ? _COMMENT : Category.label);
                    #endif 

                    QC->free_set(nodeSet);
                    return TMS_APIFail;
                }
                QC->free_set(nodeSet);
                return TMS_APISucc;
        }
         */
        // </editor-fold>     
        int link_to_be_deleted;
        int commentSet, nodeSet;
        int card;

        Q.reset_name_scope();
        if (Q.set_current_node(new StringObject(TargetNode)) < 0) {
//      if (SetNewCurrentNode(TargetNode) == ERROR) {
            /*#ifdef DEBUG_FLAG
                    fprintf(stderr, "HandleCommentsClass -- removeComment : Failed to SetNewCurrentNode() \n");
            #endif
            */
            return TMSAPIClass.TMS_APIFail;
        }
       
        if (Category.label == null || Category.label.getValue().length() ==0) {
            commentSet = Q.get_link_from_by_category(0, new StringObject(TMSAPIClass.INDIVIDUAL), new StringObject(_COMMENT));
        } else {
            commentSet = Q.get_link_from_by_category(0, Category.name, Category.label);
        }
        if (commentSet == -1) {
            // No attribute for (Category.name, Category.label) or (INDIVIDUAL, _COMMENT)
            return TMSAPIClass.TMS_APISucc;
        }

        card = Q.set_get_card(commentSet);
        if (card == 0) {
            // No comment found
            // That's OK here
            Q.free_set(commentSet);
            return TMSAPIClass.TMS_APISucc;
        }
        nodeSet = Q.get_to_value(commentSet);
        if (nodeSet == -1) {
            // There should exist nodes
            Q.free_set(commentSet);
            return TMSAPIClass.TMS_APIFail;
        }
        card = Q.set_get_card(nodeSet);
        if (card == 0) {
            // No comment found !!!
            // This should never happen. The routine should have returned ealrier
            Q.free_set(commentSet);
            Q.free_set(nodeSet);
            return TMSAPIClass.TMS_APIFail;
        }
        link_to_be_deleted = Q.get_link_from_by_category(nodeSet, new StringObject(HYPERTEXT), new StringObject(PLAINTEXT));
        if (link_to_be_deleted == -1) {
            /*
            #ifdef DEBUG_FLAG
                fprintf(stderr, "HandleCommentsClass -- removeComment : Failed to get_link_from_by_category() \n");
            #endif 
            */ 
            Q.free_set(commentSet);
            Q.free_set(nodeSet);
            Q.free_set(link_to_be_deleted);
            return TMSAPIClass.TMS_APIFail;
        }
        Q.set_union(link_to_be_deleted, commentSet) ;
        Q.free_set(commentSet);
        if (DeleteLinksInSet(Q,link_to_be_deleted) == TMSAPIClass.TMS_APIFail) {
            /*
            #ifdef DEBUG_FLAG
                fprintf(stderr, "HandleCommentsClass -- removeComment : Failed to DeleteLinksInSet() \n");
                fprintf(stderr, "HandleCommentsClass -- removeComment() : failed to remove links of '%s' category\n",(Category.label[0] == EOS) ? _COMMENT : Category.label);
            #endif */
            Q.free_set(nodeSet);
            Q.free_set(link_to_be_deleted);
            return TMSAPIClass.TMS_APIFail;
        }
        Q.free_set(link_to_be_deleted);
        if (DeleteNodesInSet(Q, nodeSet) == TMSAPIClass.TMS_APIFail) {
            /*
            #ifdef DEBUG_FLAG
                fprintf(stderr, "HandleCommentsClass -- removeComment : Failed to DeleteNodesInSet() \n");
                fprintf(stderr, "HandleCommentsClass -- removeComment() : failed to remove links of '%s' category\n",(Category.label[0] == EOS) ? _COMMENT : Category.label);
            #endif 
            */ 
            
            Q.free_set(nodeSet);
            return TMSAPIClass.TMS_APIFail;
        }
        Q.free_set(nodeSet);
        return TMSAPIClass.TMS_APISucc ;
    }
    
    
    int  DeleteNodesInSet(QClass Q, int set_id){
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*--------------------------------------------------------------
        DeleteNodesInSet()
        ----------------------------------------------------------------
        INPUT:  - set_id : a nodes set
        OUTPUT: - TMS_APISucc in case the nodes in set are deleted succesfully
                - TMS_APIFail otherwise
        FUNCTION: deletes the nodes in set set_id
        ----------------------------------------------------------------*/
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*in file handle_comm.cpp
        int DeleteNodesInSet(sis_api *QC, SIS_Connection *CC, int set_id)
        {
            int	card, count ;
            int	sysid ;
            char node_name[LOGINAM_SIZE], sysClass[LOGINAM_SIZE] ;

            card = QC ->  set_get_card(set_id);
            if (card == 0) {
                // Ok, nothing to be deleted
                return TMS_APISucc;
            }
            QC->reset_set(set_id) ;
            for (count = 0; count < card; count++) {
                if (QC ->  return_full_nodes(set_id, &  sysid, node_name, sysClass) < 0) {
                    #ifdef DEBUG_FLAG
			fprintf(stderr, "-- DeleteNodesInSet : Failed to return_full_nodes() \n");
                    #endif
			
                    return TMS_APIFail;
                }

                IDENTIFIER I_node(sysid);
                if (QC- > Delete_Node(&  I_node) == APIFail) {
                    fprintf(stderr, "in DeleteNodesInSet : failed to Delete_Node\n");
                    return TMS_APIFail;
                }
                // UAPI
                //SYSID 	id(sysid) ;
                //if (sem_check->checkAndDeleteObject(id) == ERROR) {
                //#ifdef DEBUG_FLAG
                //  fprintf(stderr, "HandleCommentsClass -- removeComment : Failed to checkAndDeleteObject\n") ;
                //#endif
                //  return ERROR ;
                //}
                //
            }
            return TMS_APISucc;
        }
        */
        // </editor-fold> 
       
        int card, count;
        PrimitiveObject_Long sysid = new PrimitiveObject_Long();
        StringObject node_name = new StringObject();
        StringObject sysClass = new StringObject();


        card = Q.set_get_card(set_id);
        if (card == 0) {
            // Ok, nothing to be deleted
            return TMSAPIClass.TMS_APISucc;
        }

        Q.reset_set(set_id);
        for (count = 0; count < card; count++) {

            if (Q.return_full_nodes(set_id, sysid, node_name, sysClass) < 0) {
                /*Not translated in java - lack of know how
                #ifdef DEBUG_FLAG
                fprintf(stderr, "-- DeleteNodesInSet : Failed to return_full_nodes() \n");
                #endif
                 */
                return TMSAPIClass.TMS_APIFail;
            }
            Identifier I_node = new Identifier(sysid.getValue());
            if (Q.CHECK_Delete_Node(I_node) == QClass.APIFail) {
                System.err.println("in DeleteNodesInSet : failed to Delete_Node\n");
                return TMSAPIClass.TMS_APIFail;
            }
        // UAPI
        //SYSID 	id(sysid) ;
        //if (sem_check->checkAndDeleteObject(id) == ERROR) {
        //#ifdef DEBUG_FLAG
        //  fprintf(stderr, "HandleCommentsClass -- removeComment : Failed to checkAndDeleteObject\n") ;
        //#endif
        //  return ERROR ;
        //}
        //
        }

        return TMSAPIClass.TMS_APISucc;
                
    }

    int  DeleteLinksInSet(QClass Q, int set_id){
        // <editor-fold defaultstate="collapsed" desc="Comments..."> 
        /*--------------------------------------------------------------
        DeleteLinksInSet()
        ----------------------------------------------------------------
        INPUT: - set_id : a link set
        OUTPUT: - TMS_APISucc in case the links in set are deleted succesfully
        - TMS_APIFail otherwise
        FUNCTION: deletes the links in set set_id
        ----------------------------------------------------------------*/
        // </editor-fold> 
        // <editor-fold defaultstate="collapsed" desc="C++ code...">
        /*in file handle_comm.cpp
        int DeleteLinksInSet(sis_api *QC, SIS_Connection *CC, int set_id)
        {
                int 	count, card ;
                int	fromid, label_sysid, flag ;
                char	cls[LOGINAM_SIZE] ;
                cm_value    cmv ;

                card = QC->set_get_card(set_id) ;
                if (card == 0) {
                // Ok, nothing to be deleted
                    return TMS_APISucc ;
                }
                QC->reset_set(set_id) ;
                for (count = 0 ; count < card ; count++) {
                    if (QC->return_link_id(set_id, cls, &fromid, &label_sysid, &cmv, &flag) < 0) {
                        #ifdef DEBUG_FLAG
                            fprintf(stderr, "-- DeleteLinksInSet : Failed to return_link_id() \n") ;
                        #endif
                        return TMS_APIFail;
                    }
                    // free allocated space
                    switch (cmv.type) {
                        case TYPE_STRING : 
                        case TYPE_NODE :
                            free(cmv.value.s);
                    }

                    IDENTIFIER I_from(fromid);
                    IDENTIFIER I_link(label_sysid);
                    if (IsNamedLink(label_sysid) == 0) { // Unnamed attribute
                        if(QC->Delete_Unnamed_Attribute(&I_link) == APIFail) {
                            fprintf(stderr, "DeleteLinksInSet : failed to Delete_Named_Attribute\n");
                            return TMS_APIFail;
                        }
                    }
                    else { // Named attribute
                        if(QC->Delete_Named_Attribute(&I_link, &I_from) == APIFail) {
                            fprintf(stderr, "DeleteLinksInSet : failed to Delete_Named_Attribute\n");
                            return TMS_APIFail;
                        }
                    }
        // UAPI
        //          SYSID	id(sysid) ;
        //          if (sem_check->checkAndDeleteObject(id) == ERROR) {
        //              #ifdef DEBUG_FLAG
        //                  fprintf(stderr, "-- DeleteLinksInSet : Failed to checkAndDeleteObject\n") ;
        //              #endif
        //              return ERROR ;
        //          }
        //
                }
                return TMS_APISucc;
        }
        */
        // </editor-fold> 

        int 	count, card ;
        PrimitiveObject_Long fromid = new PrimitiveObject_Long();
        PrimitiveObject_Long label_sysid = new PrimitiveObject_Long();
        IntegerObject flag = new IntegerObject();
        StringObject cls = new StringObject() ;
        CMValue    cmv  = new CMValue();

        card = Q.set_get_card(set_id) ;
        if (card == 0) {
        // Ok, nothing to be deleted
            return TMSAPIClass.TMS_APISucc ;
        }
        Q.reset_set(set_id) ;
        for (count = 0 ; count < card ; count++) {
            if (Q.return_link_id(set_id, cls, fromid, label_sysid, cmv, flag) < 0) {
                /*
                #ifdef DEBUG_FLAG
                    fprintf(stderr, "-- DeleteLinksInSet : Failed to return_link_id() \n") ;
                #endif
                */
                return TMSAPIClass.TMS_APIFail;
            }
            // free allocated space
            switch (cmv.getType()) {
                case CMValue.TYPE_STRING : 
                case CMValue.TYPE_NODE :
                    //free(cmv.value.s);
            }

            Identifier I_from = new Identifier(fromid.getValue());
            Identifier I_link = new Identifier(label_sysid.getValue());
            if (Q.CHECK_isUnNamedLink(label_sysid.getValue())) { // Unnamed attribute
                if(Q.CHECK_Delete_Unnamed_Attribute(I_link) == QClass.APIFail) {
                    //fprintf(stderr, "DeleteLinksInSet : failed to Delete_Named_Attribute\n");
                    System.err.println("DeleteLinksInSet : failed to Delete_Named_Attribute\n");
                    return TMSAPIClass.TMS_APIFail;
                }
            }
            else { // Named attribute
                if(Q.CHECK_Delete_Named_Attribute(I_link, I_from) == QClass.APIFail) {
                    //fprintf(stderr, "DeleteLinksInSet : failed to Delete_Named_Attribute\n");
                    System.err.println("DeleteLinksInSet : failed to Delete_Named_Attribute\n");
                    return TMSAPIClass.TMS_APIFail;
                }
            }
// UAPI
//          SYSID	id(sysid) ;
//          if (sem_check->checkAndDeleteObject(id) == ERROR) {
//              #ifdef DEBUG_FLAG
//                  fprintf(stderr, "-- DeleteLinksInSet : Failed to checkAndDeleteObject\n") ;
//              #endif
//              return ERROR ;
//          }
//
        }

        return TMSAPIClass.TMS_APISucc;
    }
    
}
