gwtmobile-persistence
=====================

gwtmobile-persistence is a GWT wrapper of the Javascript object-relational mapper library [persistence.js](http://github.com/zefhemel/persistencejs). It provides client-side object persistence capability to GWT applications, a feature similar to what Hibernate provides for GWT applications on the server-side.

gwtmobile-persistence is part of [GwtMobile](http://github.com/dennisjzh/GwtMobile), a GWT mobile development platform.

Schema definition
-----------------

The schema is defined by declaring an interface for each entity. The interface needs to extend the `Persistable` marker interface.

	public interface Category extends Persistable {
		public String getName();				// Property
		public void setName(String name);
		public Collection<Task> getTasks();		// One to many relationship
	}
 
Each getter method defines a property on the entity. Setter method is optional. If a getter method returns a `Collection<T extends Persistable>` type, that getter method defines a "one to many" or "many to many" relationship. The actual relationship type is determined by the definition on the other entity, `Task` in the example.

	public interface Task extends Persistable {
		public String getName();
		public void setName(String name);
		public String getDescription();
		public void setDescription(String description);
		public boolean getDone();	
		public void setDone(boolean done);
		public Category getCategory();				//many to one relationship
		public void setCategory(Category category);
		public Collection<Tag> getTags();			//many to many relationship	
	}

If the return type of a getter method extends the `Persistable` marker interface, then the getter method defines an inverse "one to many" relationship.

	public interface Tag extends Persistable {
		public String getName();
		public void setName(String name);	
		public Collection<Task> getTasks();			// Many to many relationship
	}

If the getter methods on both ends of the relationship return a `Collection<T extends Persistable>`, that is a "many to many" relationship.

Object creation and persistence
------------------------------

The entity is created with the `GWT.create` call. For example, `GWT.create(Task.class)` creates an entity of `Task`. The type of the created entity is `Entity<Task>`.
To create an object of the entity, use the `Entity<T>.newInstance` method. The return type of the `newInstance` method is `T`. For example, `Entity<Category>.newInstance` returns an object of type `Category`. 
	
	final Entity<Task> taskEntity = GWT.create(Task.class);
	final Entity<Tag> tagEntity = GWT.create(Tag.class);
	final Entity<Category> categoryEntity = GWT.create(Category.class);
	
	Persistence.connect("YourDBName", "Your DB Description", 5 * 1024 * 1024);
	Persistence.setAutoAdd(true);	//This is optional. When set, object created by Entity.newInstance() will
									//be added to persistence automatically.

	Persistence.schemaSync(new Callback() {
		public void onSuccess() {
			final Category c = categoryEntity.newInstance();
			c.setName("Main");
			final Tag tag = tagEntity.newInstance();
			tag.setName("Urgent");
			for (int i = 0; i < 5; i++) {
				Task t = taskEntity.newInstance();
				t.setName("Task" + Integer.toString(i));
				t.setDescription("Task No #" + Integer.toString(i));
				t.setDone(i % 2 == 0);
				t.setCategory(c);
				t.getTags().add(tag);
			}
		
			Persistence.flush(new Callback() {
				public void onSuccess() {
					Collection<Task> allTasks = c.getTasks().filter("Done", "==", true);
					allTasks.list(new CollectionCallback<Task>() {
						public void onSuccess(Task[] results) {
							for (Task task : results) {
								RootPanel.get("taskNameContainer").add(new Label(task.getName()));
							}
						}
					});					
					Persistence.reset();
				}					
			});
		}
	});

How to use
----------

1. Import gwtmobile-persistence project into Eclipse.

* [Optional]Copy the latest persistence source code `lib/persistence.js`, `lib/persistence.store.sql.js` and `lib/persistence.store.websql.js` into folder `gwtmobile-persistence/src/com/touchonmobile/gwtmobile/persistence/public`, replacing existing files.

* In Eclipse, export gwtmobile-persistence as an JAR file.

* Create your GWT project in Eclipse.

* Add the gwtmobile-persistence JAR file as an external library to your project.

* Update your project configure file (.gwt.xml) to include the following:

		<script src="http://code.google.com/apis/gears/gears_init.js" />  <!-- Include this line only if you use Google Gears database.-->
		<inherits name='com.gwtmobile.persistence.gwtmobile_persistence' />
  
* Now your GWT project is ready to persist objects to the browser database!
 
Apps that use GWT Mobile Persistence
------------------------------------

[Fantasy Predictor](http://www.touchonmobile.com): Manage all you Fantasy Football teams with the help of the Fantasy Predictor. 
[Android Market](http://market.android.com/details?id=com.TouchOnMobile.FantasyPredictor), [iTunes App Store](http://itunes.apple.com/us/app/fantasy-predictor/id405605997)

