/**
 * 
 */
package com.gwtmobile.persistence.client.domain;

import com.gwtmobile.persistence.client.Persistable;

/**
 * @author yan
 *
 */
public interface Item2MntTagTable extends Persistable {
	public String getMNTTag_Items();
	public void setMNTTag_Items(String mntTag_Items);
	public String getItem_MNTTags();
	public void setItem_MNTTags(String item_MNTTags);
}