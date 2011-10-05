/*
 * Copyright 2010 Cheng chung yan (chungyan5@gmail.com)
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

public class PersistenceSync {
	
	public static native void enableSync(String dbname, String description, int size) /*-{
		$wnd.persistence.store.websql.config($wnd.persistence, dbname, description, size);
	}-*/;

	public static native void getJSON(String url, ScalarCallback<String> callback) /*-{
		$wnd.persistence.sync.getJSON(url, 
			function(jsonResult) {
				callback.@com.gwtmobile.persistence.client.Persistence::processStringCallback(Ljava/lang/String;Lcom/gwtmobile/persistence/client/ScalarCallback;)(jsonResult, callback);
//				callback.@com.gwtmobile.persistence.client.Callback::onSuccess()();
			}
		);
	
	}-*/;

}
