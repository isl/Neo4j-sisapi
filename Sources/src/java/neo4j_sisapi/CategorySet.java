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
public class CategorySet {
    //public final static int NUMBER_OF_CATEGORIES = 40;

    //<editor-fold desc="C++ Code">
    /*
        typedef char name_buffer[INPUT_LOGINAM_SIZE];
        struct category_set {
            name_buffer fcl;
            name_buffer cat;
            int direction;  FORWARD, BACKWARD, BOTH_DIR 
        };
        typedef struct category_set categories_set[NUMBER_OF_CATEGORIES];

    */
    //</editor-fold>
    String fcl,cat;
    //private long Neo4jId;
    QClass.Traversal_Direction direction;

    /*
    long getCategoryId(){
        return this.Neo4jId;
    }
    void setCategoryId(long newId){
        this.Neo4jId = newId;
    }
    */
    public CategorySet(String fcl, String cat, QClass.Traversal_Direction direction){
        //super();
        //this.Neo4jId = -1;
        this.fcl = fcl;
        this.cat = cat;
        this.direction = direction;
    }
    
    /*
    public CategorySet(){
        //this.fcl = " ";
        //this.cat = " ";
        this.fcl = "";
        this.cat = "";
        //this.Neo4jId = -1;
        this.direction = QClass.Traversal_Direction.FORWARD;
    }
    */
    
    @Override
    public String toString() {
        return "[CategorySet: "+
                //this.Neo4jId + " " +      
                this.fcl + " " + 
                this.cat + " " +
                this.direction.name() + " ]";
    }
}
