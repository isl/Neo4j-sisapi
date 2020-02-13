Copyright 2015 Institute of Computer Science,
               Foundation for Research and Technology - Hellas.

Licensed under the EUPL, Version 1.1 or - as soon they will be approved
by the European Commission - subsequent versions of the EUPL (the "Licence");
You may not use this work except in compliance with the Licence.
You may obtain a copy of the Licence at:

http://ec.europa.eu/idabc/eupl

Unless required by applicable law or agreed to in writing, software distributed
under the Licence is distributed on an "AS IS" basis,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the Licence for the specific language governing permissions and limitations
under the Licence.

Contact:  POBox 1385, Heraklio Crete, GR-700 13 GREECE
Tel:+30-2810-391632
Fax: +30-2810-391638
E-mail: isl@ics.forth.gr
https://www.ics.forth.gr/isl/

Authors: Elias Tzortzakakis <tzortzak@ics.forth.gr>

This file is part of the Neo4j-sisapi project.

## Neo4j-sis-api
 
"Neo4j sis api" version 1.2 is a Java Application Program 
Interface that was built in order to communicate with a 
[Neo4j open source graph database] (http://neo4j.com/ "Neo4j") 
(tested with Community Edition version 3.2.5) configured to 
store and manage thesauri data following the Telos language 
representational framework specifications.

## Build
Instructions on how to compile the Neo4j-sisapi are included in file How to compile.txt

## Dependencies
The Neo4j-sisapi dependecies and licenses used are described in file Neo4j-sis-api-Dependencies-LicensesUsed.txt

## Functionalities
The following list contains most of the 
functionalities provided by "Neo4j sis api":

- Update Operations

   --- Begin / End / Abort Transaction

   --- Add / Delete / Rename Node

   --- Add / Delete / Rename Named Attribute

   --- Add / Delete Unnamed Attribute

   --- Add / Delete / Change Instance

   --- Add / Delete / Change ISA

- Read and traverse operations on single node or set of nodes
   
   --- Get Classes
   
   --- Get All Classes
   
   --- Get Instances
   
   --- Get All Instances
   
   --- Get Super Classes

   --- Get All Super Classes
   
   --- Get Sub Classes

   --- Get All Sub Classes

   --- Get Link From

   --- Get Inherited Link From

   --- Get Inherited Link To

   --- Get Link To

   --- Get Link From By Category

   --- Get Link From By Meta Category

   --- Get Link To By Category

   --- Get Link To By Meta Category

   --- Get To Node

   --- Get From Node

   --- Get To Node By Category

   --- Get From Node By Category

   --- Get To Node By Meta Category

   --- Get From Node By Meta Category

   --- Get From Value

   --- Get To Value

   --- Set Categories

   --- Set Depth

   --- Traverse by Category

   --- Traverse by Meta Category

   --- Return Nodes of set

   --- Return Full Nodes of set

   --- Return Link of set

   --- Return Link Ids of set

   --- Return Full Link of set

   --- Return Full Link Ids of set 

   --- Get Matched

   --- Get Matched String

   --- Get Matched Tone And Case Insensitive


- Set Operations

   --- Set Union
   
   --- Set Copy

   --- Set Intersect

   --- Set Difference
   
   --- Set Put
   
   --- Set Put Primitive
   
   --- Set Delete
   
   --- Set Member Of
            
