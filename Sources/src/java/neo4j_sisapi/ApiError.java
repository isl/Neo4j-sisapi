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
//similar to telosError class
class ApiError {
    //char message[MAX_NUM_OF_THREADS][maxMessLen]; 	/* string describing the occured error */
    //int  ThreadFlag[MAX_NUM_OF_THREADS];		        /* flags indicating an error existence */
    String message ="";
    String errorCode ="";//coming from messages
    String[] errorArgs = null;
    
    int  ThreadFlag; 
    //int  findTidPos(void);
    
    /*
    telosError();
      int flag(void);
      void getMessage(char* mess);
      void printMessage();
      void reset();
      void free_pid(void);
      void putMessage(char* s);
      short checkError(char* s);
    */
    
    int flag(){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        // Returns the ThreadF for the current thread.
        int telosError::flag(void)
        {
            int idx;
            idx = findTidPos();
            return(ThreadFlag[idx]);
        }
        */
        // </editor-fold> 
        
        return this.ThreadFlag;
    }
    
    void getMessage(StringObject mess){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        void telosError::getMessage(char* mess)
        {
            if(mess == 0)
               return;      // To avoid NULL pointers.
            int idx;
            idx = findTidPos();
            if(idx != 0)
               strcpy(mess, message[idx]);
        }
        */
        // </editor-fold>         
        mess.setValue(message);
    }
    void getErrorCode(StringObject mess){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        void telosError::getMessage(char* mess)
        {
            if(mess == 0)
               return;      // To avoid NULL pointers.
            int idx;
            idx = findTidPos();
            if(idx != 0)
               strcpy(mess, message[idx]);
        }
        */
        // </editor-fold>         
        mess.setValue(errorCode);
    }    
    
    void printMessage(){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        void telosError::printMessage()
        {
            int idx;
            idx = findTidPos();
            if(idx != 0)
               printf("%s\n", message[idx]);
        }
        */
        // </editor-fold>         
    }
    
    void reset(){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        void telosError::reset()
        {
           int idx;
           idx = findTidPos();
           message[idx][0] = 0;
           ThreadFlag[idx] = 0;
        };
        */
        // </editor-fold>         
        
        this.message = "";
        this.errorCode = "";
        this.ThreadFlag = 0;
    }
    
    void putMessage(String s){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        void telosError::putMessage(char* cptr)
        {
        // the following two lines are added by Manos Theodorakis
        // because a core is generated

            if ( cptr == (char *)0)
                { return; }
           int len;
           int idx;
           idx = findTidPos();
           if(idx != 0)
           {
               len = strlen(message[idx]);
               strncat(&message[idx][len], cptr, maxMessPos-len);
               ThreadFlag[idx] = 1;
           }
        }
        */
        // </editor-fold>                
        this.message = s;
    }
    
    void setMessageWithErrorCodeAndArgs(String keyword, String... args){
        String msg = Messages.allMessages.get(keyword);
        if(msg!=null && msg.length()>0){
            errorCode = keyword;
            
            if(args!=null){
                errorArgs = new String[args.length];
                
                for(int i=0;i< args.length ; i++){
                    errorArgs[i] = args[i];
                }
                msg = String.format(msg, (Object[]) args);
            }
            else{
                errorArgs = null;
            }
            putMessage(msg);
        }        
    }
    
    int checkError(String s){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">        
        /*
        short telosError::checkError(char* s)
        {
           int idx;
           idx = findTidPos();
           if(idx != 0)
           {
              if(ThreadFlag[idx])
              {
                 putMessage(s);
                 return(-1);
              }
              else
                return(0);
           }
           return 0;
        }
        */
        // </editor-fold>         
        
        if(flag()!=0){
            putMessage(s);
            return(QClass.APIFail);
        }        
        return QClass.APISucc;
    }
}

