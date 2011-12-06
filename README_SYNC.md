GWT Mobile Persistence Sync
=====================

gwtmobile-persistence Sync is a GWT wrapper of the Javascript object-relational mapper library [persistence.sync.js](http://github.com/zefhemel/persistencejs). It provides client-side object persistence capability to GWT applications, a server-side by [Slim3PersistenceSync](https://github.com/rsaccon/Slim3PersistenceSync).

gwtmobile-persistence Sync is extend of [gwtmobile-persistence](https://github.com/dennisjzh/GwtMobile-Persistence).

The relationship of this Module(Eclipse Project) and others 
-----------------

* [gwtmobile-persistence Sync] is a Google App Engine Web Application
* [Application] is a Customer Application code as library for our own Application Logic


Communication between Client and Server
-----------------

* Following Non-GWT Slim3(Slim3PersistenceSync) and persistence.sync.js post/get method and JSON protocol
** Inside server, it has a link in http://server/taskUpdates
** Inside client, it must init. this Task.enableSync('/taskUpdates'); 


Startup howto
-----------------

* add the linking of your application project into this project "Java Build Path" 
* add your application gwt code pointer under Main.gwt.xml-->"Application modules inherits"
* Update the Project name and version no. of Google Application Engine at ~/war/WEB-INF/appengine-web.xml


Technical Structure Diagram
-----------------

1. General

Preparing Table 
* Entity<T extends Persistable>
* create a T_EntityImpl extends EntityInternal<T extends Persistable>  

Create a New Record 
* T extends Persistable = Entity<T extends Persistable>.newInstance()
* Persistence.flush();

2. Many-2-ManyRelationship

Preparing Table
* Same to General
* creating a Many-2-ManyRelationship attached inside this T_EntityImpl

Connect a record to a record of another Table
* select a record(T extends Persistable)
* get a corresponding record(T extends Persistable) from another table
* put them two records into a Many-2-ManyRelationship 


Many-to-Many Relationship Sync
-----------------

Using a Strategy in "using Application level(external) Relationship table Entity"

Create a new Many-to-Many Relationship Table as Sync Entity.
* original JS operation in creating js original Many-to-Many Relationship Table
* Create same naming of Table and column by reading back their original coding and return to gwt (Manually in this moment, try Auto. Code Generation in future)
* Creating many-to-many relationship Entity in server by Application

When adding / removing any relationship
* remove some code in original updating original Table, 
** adding -- at persistence.js line 1952 and persistence.store.js line 851
** removing -- 
* update the corresponding data to many-to-many relationship entity by Application

When query the Many-to-Many Relationship Table
* remove some code in original updating original Table, 
* query the corresponding data from many-to-many relationship entity

After each Entity sync, if it has many-to-many relationship, it will sync a Relationship table in Application level 
* Creating and init. Entities one by one, if it contains many-to-many relationship, do the following in client for this many-to-many relationship by Application
** create a new many-to-many relationship entity by gwt

* Sync Entity one by one and if it contains many-to-many relationship
** sync this many-to-many relationship entity by Application






ToDo
-----------------

Relationship Sync
*consider one case only until now: Item Entities and Tag Entities, and sync their relationship only; Other cases have not yet consider. 