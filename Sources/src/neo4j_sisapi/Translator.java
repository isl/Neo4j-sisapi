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
 *
 * @author Elias Tzortzakakis <tzortzak@ics.forth.gr>
 */
class Translator {
    //final int OK = 0;

    final int ERROR = -1;
    final int EXIST_ONE = 0;
    final int NOT_EXIST = 1;
    final int NOT_UNIQUE = 2;
    DBaccess db = null;
    Translator(DBaccess dbInst) {
        db = dbInst;
    }
    
    
    
    int getSysid(StringObject fulnam, PrimitiveObject_Long retSys){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        // --------------------------------------------------------------------------
// getSysid -- inputs a full path name (fulname) and returns :
//    EXIST_ONE,    if the fulname corresponds to exactly one object and its
//                sysid is returned into retSys
//    NOT_EXIST,    if the given fulname corresponds to a no-one object
//                (the retSys returns zero)
//    NOT_UNIQUE,    if the fulname is not fully resolved (corresponds to more
//                than one objects)
//    ERROR,        if an error occurs
// --------------------------------------------------------------------------

int Translator::getSysid( LOGINAM *fulnam, SYSID *retSys)
{
    LOGINAM    *fromNam;
    SYSID    fulnamS;
    Loginam    *tmpL;
    int        status;
    int        id;

    //line added by georgian to check that actually the fulnam  exists
    if (  fulnam == ( LOGINAM *)0 ) {
        return(NOT_EXIST);
        } 

    if( (id = fulnam->unnamed_id()) )
            {
            *retSys = SYSID(id);
//            printf("Translator::getSysid( LOGINAM *fulnam, SYSID *retSys) called on unnamed sysid 0x%x\n", id);
            return( EXIST_ONE);
            }

    if ( (fromNam = fulnam->getfromnam()) == (LOGINAM *)0) {
        SYSID_SET    tmp;
        if ( (tmpL = inv_convertion( fulnam)) == (Loginam *)0)
            { return( ERROR); }
        symbolTable->getSysid( tmpL, &tmp);
        delete    tmpL;
        switch ( tmp.set_get_card()) {
          case ERROR: return( ERROR);
          case    0:    *retSys = SYSID(0);
                    return( NOT_EXIST);
          case    1:    tmp.set_pos( 0);
                    *retSys = tmp.set_get_id();
                    return( EXIST_ONE);
        }
        return( NOT_UNIQUE);
    }

    if ( (status = getSysid( fromNam, &fulnamS)) == EXIST_ONE) {
        if ( (tmpL = inv_convertion( fulnam)) == (Loginam *)0)
            { return( ERROR); }
        *retSys = symbolTable->getSysid( tmpL, fulnamS);
        delete    tmpL;
        if ( retSys->id == 0)
            { return( NOT_EXIST); }
        return( EXIST_ONE);
    }

    return( status);
}
        */
        // </editor-fold> 
        
        StringObject fromNam = new StringObject();
        PrimitiveObject_Long fulnamS = new PrimitiveObject_Long();
        StringObject tmpL = new StringObject();
        int status = ERROR;
        PrimitiveObject_Long id = new PrimitiveObject_Long();

        if (fulnam == null || fulnam.getValue().length() == 0) {
            return (NOT_EXIST);
        }
        
        db.unnamed_id(fulnam, id);
        if(id.getValue()!=0){
            retSys.setValue(id.getValue());
            // printf("Translator::getSysid( LOGINAM *fulnam, SYSID *retSys) called on unnamed sysid 0x%x\n", id);
            return( EXIST_ONE);
        }
        /*
        if ( (fromNam = fulnam->getfromnam()) == (LOGINAM *)0) {
            SYSID_SET    tmp;
            if ( (tmpL = inv_convertion( fulnam)) == (Loginam *)0)
                { return( ERROR); }
            symbolTable->getSysid( tmpL, &tmp);
            delete    tmpL;
            switch ( tmp.set_get_card()) {
              case ERROR: return( ERROR);
              case    0:    *retSys = SYSID(0);
                        return( NOT_EXIST);
              case    1:    tmp.set_pos( 0);
                        *retSys = tmp.set_get_id();
                        return( EXIST_ONE);
            }
            return( NOT_UNIQUE);
        }*/
        
        /*
        if ( (status = getSysid( fromNam, &fulnamS)) == EXIST_ONE) {
        if ( (tmpL = inv_convertion( fulnam)) == (Loginam *)0)
            { return( ERROR); }
        *retSys = symbolTable->getSysid( tmpL, fulnamS);
        delete    tmpL;
        if ( retSys->id == 0)
            { return( NOT_EXIST); }
        return( EXIST_ONE);
    }

        */
        
        return (status);
    }
}
