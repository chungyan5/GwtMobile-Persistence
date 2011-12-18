/*
 * Copyright 2010 Zhihua (Dennis) Jiang
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.gwtmobile.persistence.client;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public abstract class EntityInternal<T extends Persistable> implements Entity<T> {	
	public abstract  T newInstance(JavaScriptObject nativeObject);
	public abstract T[] newInstanceArray(int size);	//Cannot create a generic array in Java...
	public abstract JavaScriptObject getNativeObject();
	public abstract Collection<T> newCollection(JavaScriptObject nativeCollection);
	public abstract String getInverseRelationName(String rel);
	public abstract String getEntityName();
	
//	protected static String many2ManyTableName = "";				// this Table Name is Non-Empty if this entity contains Many to Many relationship  
//	protected static Entity<Item2MntTagTable> many2ManyTableEntity = null;
	
	/**
	 * @return many2ManyTableEntity if Many-2-Many existing, otherwise return NULL 
	 */
//	public static Entity<Item2MntTagTable> getMany2ManyTableEntity() {
//		return many2ManyTableEntity;
//	}
	
	public void load(Transaction transaction, String id, ScalarCallback<T> callback) {
		load(transaction, id, callback, getNativeObject(), this);
	}	
	public void load(String id, ScalarCallback<T> callback) {
		load(null, id, callback, getNativeObject(), this);
	}	
	private native void load(Transaction transaction, String id, ScalarCallback<T> callback, JavaScriptObject nativeObject, EntityInternal<T> self) /*-{
		nativeObject.load($wnd.persistence, transaction, id, function(result) {
			self.@com.gwtmobile.persistence.client.EntityInternal::processwLoadCallback(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/gwtmobile/persistence/client/ScalarCallback;)(result, callback);
		});
	}-*/;
	
	@SuppressWarnings("unused")
	private void processwLoadCallback(JavaScriptObject result, ScalarCallback<T> callback) {
		callback.onSuccess(result == null ? null : newInstance(result));
	}
	
	public void findBy(Transaction transaction, String property, boolean value, ScalarCallback<T> callback) {
		findBy(transaction, property, value, callback, getNativeObject(), this);
	}
	public void findBy(String property, boolean value, ScalarCallback<T> callback) {
		findBy(null, property, value, callback, getNativeObject(), this);
	}
	public void findBy(Transaction transaction, String property, char value, ScalarCallback<T> callback) {
		findBy(transaction, property, new String(new char[] {value}), callback, getNativeObject(), this);
	}
	public void findBy(String property, char value, ScalarCallback<T> callback) {
		findBy(null, property, new String(new char[] {value}), callback, getNativeObject(), this);
	}
	public void findBy(Transaction transaction, String property, int value, ScalarCallback<T> callback) {
		findBy(transaction, property, value, callback, getNativeObject(), this);
	}
	public void findBy(String property, int value, ScalarCallback<T> callback) {
		findBy(null, property, value, callback, getNativeObject(), this);
	}
	public void findBy(Transaction transaction, String property, double value, ScalarCallback<T> callback) {
		findBy(transaction, property, value, callback, getNativeObject(), this);
	}
	public void findBy(String property, double value, ScalarCallback<T> callback) {
		findBy(null, property, value, callback, getNativeObject(), this);
	}
	public void findBy(Transaction transaction, String property, String value, ScalarCallback<T> callback) {
		findBy(transaction, property, value, callback, getNativeObject(), this);
	}
	public void findBy(String property, String value, ScalarCallback<T> callback) {
		findBy(null, property, value, callback, getNativeObject(), this);
	}
	public void findBy(Transaction transaction, String property, Date value, ScalarCallback<T> callback) {
		findBy(transaction, property, value.getTime(), callback, getNativeObject(), this);
	}
	public void findBy(String property, Date value, ScalarCallback<T> callback) {
		findBy(null, property, value.getTime(), callback, getNativeObject(), this);
	}
	private native void findBy(Transaction transaction, String property, boolean value, ScalarCallback<T> callback, JavaScriptObject nativeObject, EntityInternal<T> self) /*-{
		nativeObject.findBy($wnd.persistence, transaction, property, value, function(result) {
			self.@com.gwtmobile.persistence.client.EntityInternal::processwLoadCallback(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/gwtmobile/persistence/client/ScalarCallback;)(result, callback);
		});
	}-*/;
	private native void findBy(Transaction transaction, String property, String value, ScalarCallback<T> callback, JavaScriptObject nativeObject, EntityInternal<T> self) /*-{
		nativeObject.findBy($wnd.persistence, transaction, property, value, function(result) {
			self.@com.gwtmobile.persistence.client.EntityInternal::processwLoadCallback(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/gwtmobile/persistence/client/ScalarCallback;)(result, callback);
		});
	}-*/;
	// Can't pass long to JSNI...
	private native void findBy(Transaction transaction, String property, double value, ScalarCallback<T> callback, JavaScriptObject nativeObject, EntityInternal<T> self) /*-{
		nativeObject.findBy($wnd.persistence, transaction, property, value, function(result) {
			self.@com.gwtmobile.persistence.client.EntityInternal::processwLoadCallback(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/gwtmobile/persistence/client/ScalarCallback;)(result, callback);
		});
	}-*/;

	public void index(String column) {
		index(column, false, getNativeObject());
	}
	public void index(String column, boolean unique) {
		index(column, unique, getNativeObject());
	}
	private native void index(String column, boolean unique, JavaScriptObject nativeObject) /*-{
		if (unique) {
			nativeObject.index(column, {unique:true});
		}else {
			nativeObject.index(column);
		}		
	}-*/;	
	
	public void index(String[] columns) {
		index(columns, false);
	}
	public void index(String[] columns, boolean unique) {
		JsArrayString jsArray = (JsArrayString) JavaScriptObject.createArray();
		for (int i = 0; i < columns.length; i++) {
			String col = columns[i];
			jsArray.set(i, col);
		}
		index(jsArray, false, getNativeObject());
	}
	private native void index(JavaScriptObject columns, boolean unique, JavaScriptObject nativeObject) /*-{
		if (unique) {
			nativeObject.index(columns, {unique:true});
		}else {
			nativeObject.index(columns);
		}		
	}-*/;
	
	private ConflictCallback conflict_callback;
	
	@SuppressWarnings("unused")
//	private void processConflictCallback(JavaScriptObject jsonJavaScriptObject) {
    private void processConflictCallback(String resultJson) {

//      JSONArray jsonArray = new JSONArray(jsonJavaScriptObject);
//      JSONValue jsonValue = jsonArray.get(0);
	    
	    /*JSONValue jsonWhole = JSONParser.parseLenient(resultJson);
	    JSONArray jsonArray = jsonWhole.isArray();
	    JSONValue json = jsonArray.get(0);
	    JSONObject jsonObject = json.isObject();*/
        String[] returnStrArray = new String[2];
	    /*returnStrArray[0] = jsonObject.get("local").isObject().get("name").toString();
	    returnStrArray[1] = jsonObject.get("remote").isObject().get("name").toString();*/
	    
//        this.conflict_callback.onSuccess(jsonValue.isString().stringValue());
//        this.conflict_callback.onSuccess(resultJson.toString());
        this.conflict_callback.onSuccess(returnStrArray);
	}
	
	public final native JavaScriptObject onConflictHandler(EntityInternal<T> self) /*-{
		return function(conflicts, updatesToPush, persistence_sync_internal_callback) {
			$entry(
                self.@com.gwtmobile.persistence.client.EntityInternal::processConflictCallback(Ljava/lang/String;)(JSON.stringify(conflicts))
			);																			// application code
			persistence_sync_internal_callback();
		}; 
	}-*/;
//    self.@com.gwtmobile.persistence.client.EntityInternal::processConflictCallback(Lcom/google/gwt/core/client/JavaScriptObject;)(conflicts)
	
	public void syncAll(final ConflictCallback conflict_callback, final Callback callback) {
		this.conflict_callback = conflict_callback;
		syncAll(getNativeObject(), onConflictHandler(this), new Callback() { 
			@Override
			public void onSuccess() {
			    /*				
			// after sync this Entity, may be sync the relationship table as well ==========================
				if (many2ManyTableName.length()!=0) {
					
					// sync many2ManyTableEntity bet. client and server ====================================
					many2ManyTableEntity.syncAll(conflict_callback, new Callback() {

						@Override
						public void onSuccess() {
							many2ManyTableEntity.syncAll(conflict_callback, new Callback() {

								@Override
								public void onSuccess() {
									
					// copy data fr. many2ManyTableEntity to local many-to-many table =========================================
									many2ManyTableEntity.all().list(new CollectionCallback<Item_MNTTags_MNTTagGwt>() {
										@Override
										public void onSuccess(Item_MNTTags_MNTTagGwt[] results) {
											// TODO Auto-generated method stub
											for (int i=0;i<results.length;i++) {
												results[i].getItem_MNTTags()
												results[i].getMNTTag_Items()
												
											}*/
											
											callback.onSuccess();
										/*}
									});
								}
								
							});
						}
						
					});
				}*/
			}
		});
	}
	  
	public native void syncAll(JavaScriptObject nativeEntity, JavaScriptObject ConflictHandler, Callback callback) /*-{
	    nativeEntity.syncAll(ConflictHandler, function() {
			callback.@com.gwtmobile.persistence.client.Callback::onSuccess()();
		});
	}-*/;
	
	
	/**
	 * @return if Many2Many Existing, return a valid Table name, otherwise a empty string return 
	 */
	public String getMany2ManyTableName() {
		return getMany2ManyTableName(getNativeObject());
	}
	
	/**
	 * @return if Many2Many Existing, return a valid Table name, otherwise a empty string return 
	 */
	public native String getMany2ManyTableName(JavaScriptObject nativeEntity) /*-{
		return "";
	}-*/;
	
}
