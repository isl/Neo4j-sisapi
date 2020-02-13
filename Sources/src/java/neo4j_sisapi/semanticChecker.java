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
class semanticChecker {

    final int ERROR = -1;
    final int OK = 0;
    final int TRUE = 1;
    final int FALSE = 0;
    //Translator  translator = null;
    PQI_Set deletedInstOf = new PQI_Set();
    PQI_Set addedInstOf = new PQI_Set();
    PQI_Set deletedIsa  = new PQI_Set();
    PQI_Set addedIsa  = new PQI_Set();    
    DBaccess db = null;
    semanticChecker(DBaccess dbInst) {
        db = dbInst;
        //translator = new Translator(dbInst);
    }
    
    boolean newNodeNameIsOk(String nodeName){
        if(Configs.systemLabels.contains(nodeName)){
            return false;
        }
        
        if(nodeName.matches(Configs.regExForUnNamed)){
            return false;
        }
        return true;
    }
    boolean newNamedAttributeNameIsOk(String attrName){
        if(attrName.matches(Configs.regExForUnNamed)){
            return false;
        }
        return true;
    }
    
    boolean newUnNamedAttributeNameIsOk(String attrName){
        if(attrName.matches(Configs.regExForUnNamed)){
            return true;
        }
        return false;
    }
    
    String getNewUnnamedAttributeLogicalName(long newId){
        return "Labelabcdef"+newId;
    }
    void initUpdateSets() {
        
        deletedInstOf.set_clear();
        addedInstOf.set_clear();
        deletedIsa.set_clear();
        addedIsa.set_clear();
    }
    
    
    //int checkCurrentObject(StringObject currObjLn){
        // <editor-fold defaultstate="collapsed" desc="C++ Code">
        /*
        // --------------------------------------------------------------------------
// semanticChecker::checkCurrentObject --
//	It checks whether or not an object with given logical name has been
// already defined and if the first happens then updates the variables of
// semantic checker
// --------------------------------------------------------------------------
int	semanticChecker::checkCurrentObject( LOGINAM *currObjLn)
{
	SYSID	currS;
	int		status;

#ifdef DEBUG
	char    DEBUG_str[maxMessLen];
	DEBUG_str[0] = '\0';
	translator->getString( currObjLn, DEBUG_str);
	printf( ">>> In checkCurrentObject( %s, 0x%x)\n", DEBUG_str);
#endif

	if ( (status = translator->getSysid( currObjLn, &currS)) == NOT_EXIST) {
		errMsg( NOT_CREATE_OBJ, currObjLn, translator);
		return( ERROR);
	}
	if ( status == NOT_UNIQUE) {
		errMsg( "(>semanticChecker::checkCurrentObject) the object %s is not unique in the DB.\n", currObjLn, translator);
		return( ERROR);
	}
	if ( status == ERROR) {
		globalError.putMessage( " >semanticChecker::checkCurrentObject");
		return( ERROR);
	}

	// an entry for an object with the given logical name
	// has been made into the symbol table

	currObj = sysCatalog->loadObject( currS);
	if ( currObj == (T_O *)0) {
		errLoadObject( currS, ">semanticChecker::checkCurrentObject", translator);
		return( ERROR);
	}

	if ( currObj->IsUnresolved()) {
		errMsg( NOT_RETELL_UNRES_OBJ, currObjLn, translator);
		return( ERROR);
	}

	if ( O_IsLink( currObj->o_type)) {
		loSys = currS;
		currLinkObj = currObj;
		currS = currLinkObj->getFrom();
		currObj = sysCatalog->loadObject( currS);
		if ( currObj == (T_O *)0) {
			errLoadObject( currS, ">semanticChecker::checkCurrentObject", translator);
			return( ERROR);
		}
		setUpPointers();
		isLink = 1;
	} else
		isLink = 0;

	return( OK);
}
        */
        // </editor-fold> 
        
      //  PrimitiveObject_Long currS = new PrimitiveObject_Long();
        //int status = translator.getSysid( currObjLn, currS);
        
        //return OK;
    //}
}
