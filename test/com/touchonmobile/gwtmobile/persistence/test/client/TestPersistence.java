package com.touchonmobile.gwtmobile.persistence.test.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.junit.client.GWTTestCase;
import com.touchonmobile.gwtmobile.persistence.client.Callback;
import com.touchonmobile.gwtmobile.persistence.client.Collection;
import com.touchonmobile.gwtmobile.persistence.client.CollectionCallback;
import com.touchonmobile.gwtmobile.persistence.client.Entity;
import com.touchonmobile.gwtmobile.persistence.client.Persistence;
import com.touchonmobile.gwtmobile.persistence.client.ScalarCallback;
import com.touchonmobile.gwtmobile.persistence.test.domain.Category;
import com.touchonmobile.gwtmobile.persistence.test.domain.Tag;
import com.touchonmobile.gwtmobile.persistence.test.domain.Task;

public class TestPersistence extends GWTTestCase {

	Entity<Task> taskEntity;
	Entity<Tag> tagEntity;
	Entity<Category> categoryEntity;
	
	@Override
	public String getModuleName() {
		return "com.touchonmobile.gwtmobile.persistence.test.gwtmobile_persistence_test";
	}

	public void testList() {
		setupTest(new Callback() {
			public void onSuccess() {
				Collection<Task> allTasks = taskEntity.all();
				allTasks.list(new CollectionCallback<Task>() {
					public void onSuccess(Task[] results) {
						assertEquals(5, results.length);
						tearDownTest();
					}
				});
			}
		});						
	}

	public void testListEach() {
		setupTest(new Callback() {
			int count = 0;
			public void onSuccess() {
				Collection<Task> allTasks = taskEntity.all();
				allTasks.each(new ScalarCallback<Task>() {
					public void onSuccess(Task result) {
						count++;
						if (count == 5) {
							tearDownTest();
						}
					}
				});
			}
		});						
	}

	public void testListLimits() {
		setupTest(new Callback() {
			public void onSuccess() {
				Collection<Task> limit3 = taskEntity.all().limit(3);
				limit3.list(new CollectionCallback<Task>() {
					public void onSuccess(Task[] results) {
						assertEquals(3, results.length);
						tearDownTest();
					}
				});
			}
		});						
	}

	public void testListOrder() {
		setupTest(new Callback() {
			public void onSuccess() {
				taskEntity.all().order("Name", false).one(
						new ScalarCallback<Task>() {
							public void onSuccess(Task result) {
								assertEquals("Task4", result.getName());
								tearDownTest();
							}
						});
			}
		});						
	}
	
	public void testEntityFindByChar() {
		setupTest(new Callback() {
			public void onSuccess() {
				taskEntity.findBy(null, "Alphabet", 'C', new ScalarCallback<Task>(){
					@Override
					public void onSuccess(final Task result1) {
						assertNotNull(result1);
						System.out.println(result1.getAlphabet());
						taskEntity.load(null, result1.getId(), new ScalarCallback<Task>() {					
							@Override
							public void onSuccess(Task result2) {
								assertNotNull(result2);
								assertEquals(result1.getAlphabet(), result2.getAlphabet());
								tearDownTest();
							}
						});
					}});
				
			}
		});
	}

	public void testEntityFindByDate() {
		setupTest(new Callback() {
			@SuppressWarnings("deprecation")
			public void onSuccess() {
				taskEntity.findBy(null, "CompleteDate", new Date("2010/12/01"), new ScalarCallback<Task>(){
					@Override
					public void onSuccess(final Task result1) {
						assertNotNull(result1);
						System.out.println(result1.getCompleteDate().toString());
						taskEntity.load(null, result1.getId(), new ScalarCallback<Task>() {					
							@Override
							public void onSuccess(Task result2) {
								assertNotNull(result2);
								assertEquals(result1.getCompleteDate(), result2.getCompleteDate());								
								tearDownTest();
							}
						});
					}});
				
			}
		});
	}
	
	public void testEntityFindByInteger() {
		setupTest(new Callback() {
			public void onSuccess() {
				taskEntity.findBy(null, "Priority", 2, new ScalarCallback<Task>(){
					@Override
					public void onSuccess(final Task result1) {
						assertNotNull(result1);
						System.out.println(result1.getPriority());
						taskEntity.load(null, result1.getId(), new ScalarCallback<Task>() {					
							@Override
							public void onSuccess(Task result2) {
								assertNotNull(result2);
								assertEquals(result1.getPriority(), result2.getPriority());								
								tearDownTest();
							}
						});
					}});
				
			}
		});
	}
	
	public void testEntityFindByDouble() {
		setupTest(new Callback() {
			public void onSuccess() {
				taskEntity.findBy(null, "Profit", 24.68, new ScalarCallback<Task>(){
					@Override
					public void onSuccess(final Task result1) {
						assertNotNull(result1);
						System.out.println(result1.getProfit());
						taskEntity.load(null, result1.getId(), new ScalarCallback<Task>() {					
							@Override
							public void onSuccess(Task result2) {
								assertNotNull(result2);
								assertEquals(result1.getProfit(), result2.getProfit());								
								tearDownTest();
							}
						});
					}});
				
			}
		});
	}
	
	public void testEntityFindByString() {
		setupTest(new Callback() {
			public void onSuccess() {
				taskEntity.findBy(null, "Name", "Task3", new ScalarCallback<Task>(){
					@Override
					public void onSuccess(final Task result1) {
						assertNotNull(result1);
						System.out.println(result1.getName());
						taskEntity.load(null, result1.getId(), new ScalarCallback<Task>() {					
							@Override
							public void onSuccess(Task result2) {
								assertNotNull(result2);
								assertEquals(result1.getName(), result2.getName());								
								tearDownTest();
							}
						});
					}});
				
			}
		});
	}
	
	public void testEntityFindByBoolean() {
		setupTest(new Callback() {
			public void onSuccess() {
				taskEntity.findBy(null, "Done", true, new ScalarCallback<Task>(){
					@Override
					public void onSuccess(final Task result1) {
						assertNotNull(result1);
						System.out.println(result1.getDone());
						taskEntity.load(null, result1.getId(), new ScalarCallback<Task>() {					
							@Override
							public void onSuccess(Task result2) {
								assertNotNull(result2);
								assertEquals(result1.getDone(), result2.getDone());								
								tearDownTest();
							}
						});
					}});
				
			}
		});
	}
	
	public void testInstanceId() {
		setupTest(new Callback() {
			public void onSuccess() {
				taskEntity.all().one(new ScalarCallback<Task>() {					
					@Override
					public void onSuccess(Task result) {
						System.out.print(result.getId());
						assertEquals(32, result.getId().length());
						tearDownTest();
					}
				});
			}
		});						
	}

	public void testInstanceFetch() {
		setupTest(new Callback() {
			public void onSuccess() {				
				taskEntity.all().list(new CollectionCallback<Task>() {					
					@Override
					public void onSuccess(Task[] results) {
						results[0].fetch(null, categoryEntity, new ScalarCallback<Category>() {
							@Override
							public void onSuccess(Category instance) {
								System.out.print(instance.getName());
								tearDownTest();
							}
						});
					}
				});
			}
		});
	}

	public void testInstanceSelectJSON() {
		setupTest(new Callback() {
			public void onSuccess() {				
				taskEntity.all().list(new CollectionCallback<Task>() {					
					@Override
					public void onSuccess(Task[] results) {
						results[0].selectJSON(null, new String[] {"Name", "Description"}, new ScalarCallback<String>() {							
							@Override
							public void onSuccess(String jsonDump) {
								System.out.print(jsonDump);
								tearDownTest();
							}
						});
					}
				});
			}
		});
	}
	
	public void testDumpAndLoad() {
		setupTest(new Callback() {
			public void onSuccess() {
				Persistence.dumpToJson(null, new Entity[] {tagEntity, categoryEntity}, new ScalarCallback<String>() {					
					@Override
					public void onSuccess(final String jsonDump) {
						System.out.print(jsonDump);
						taskEntity.all().destroyAll(new Callback() {							
							@Override
							public void onSuccess() {
								tagEntity.all().destroyAll(new Callback() {									
									@Override
									public void onSuccess() {
										categoryEntity.all().destroyAll(new Callback() {											
											@Override
											public void onSuccess() {
												Persistence.loadFromJson(null, jsonDump, new Callback() {													
													@Override
													public void onSuccess() {
														tagEntity.all().count(new ScalarCallback<Integer>() {															
															@Override
															public void onSuccess(Integer result) {
																assertEquals(1, result.intValue());
																tearDownTest();
															}
														});
													}
												});
											}
										});
									}
								});
							}
						});
					}
				});
			}
		});						
	}
	
	private void setupTest(final Callback callback) {

		Persistence.connect("MyDB", "My DB", 5 * 1024 * 1024);
		Persistence.setAutoAdd(true);
		
			taskEntity = GWT.create(Task.class);
			tagEntity = GWT.create(Tag.class);
			categoryEntity = GWT.create(Category.class);

			Persistence.schemaSync(new Callback() {
				@SuppressWarnings("deprecation")
				public void onSuccess() 
				{
					final Category c = categoryEntity.newInstance();
					c.setName("Main");
					final Tag tag = tagEntity.newInstance();
					tag.setName("Urgent");
					for (int i = 0; i < 5; i++) {
						Task t = taskEntity.newInstance();
						t.setName("Task" + Integer.toString(i));
						//t.setName(null);
						t.setDescription("Task No #" + Integer.toString(i));
						if (i % 2 == 0) {
							t.setDone(true);
							t.setCompleteDate(new Date(2010 - 1900, 12 - 1, 1 + i));
							t.setPriority(i);
							t.setPercentage((float)i / 10);
							t.setProfit((double)i * 12.34);
							t.setAlphabet((char) ('A' + i));
							t.setJson((JSONObject) JSONParser.parse("{\"symbol\": \"ABC\", \"price\": 96.204659543522}"));
						}
						t.setCategory(c);
						t.getTags().add(tag);
					}
					Persistence.flush(callback);
				}
			});
		delayTestFinish(5000);
	}
	
	private void tearDownTest() {
		Persistence.reset(new Callback() {			
			@Override
			public void onSuccess() {
				finishTest();
			}
		});		
	}
}
