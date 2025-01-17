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

//import com.gwtmobile.persistence.client.domain.Item2MntTagTable;


public interface Persistable {
	String getId();
	<T extends Persistable> void fetch(Transaction transaction, Entity<T> entity, ScalarCallback<T> callback);
	<T extends Persistable> void fetch(Entity<T> entity, ScalarCallback<T> callback);
	void selectJSON(Transaction transaction, String[] propertySpec, ScalarCallback<String> callback);
	void selectJSON(String[] propertySpec, ScalarCallback<String> callback);
//	Entity<Item2MntTagTable> getMany2ManyTableEntity();
}
