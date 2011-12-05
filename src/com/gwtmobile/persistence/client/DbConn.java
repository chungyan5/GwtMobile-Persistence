/**
 * 
 */
package com.gwtmobile.persistence.client;


import com.google.gwt.core.client.GWT;
import com.gwtmobile.persistence.client.Callback;
import com.gwtmobile.persistence.client.Entity;
import com.gwtmobile.persistence.client.Persistence;
import com.gwtmobile.persistence.client.domain.Item;
import com.gwtmobile.persistence.client.domain.Item2MntTagTable;
import com.gwtmobile.persistence.client.domain.MNTTag;

/**
 * @author yan
 *
 */
public class DbConn {

	public Entity<MNTTag> tagEntity;
	
	public Entity<Item> itemEntity;
	
    public Entity<Item2MntTagTable> item2MntTagTableEntity;
    
	public DbConn() {
	    conn2Db();
	}
	
	public void conn2Db() {
		
		Persistence.connect("MntDb", "Mnt Db", 5*1024*1024);	// create or connect to 5M for Db
		
		Persistence.setAutoAdd(true);							// object will be added to persistence automatically.

		tagEntity = GWT.create(MNTTag.class);					// create entities
		itemEntity = GWT.create(Item.class);
		item2MntTagTableEntity = GWT.create(Item2MntTagTable.class);
		
		Persistence.schemaSync();                               // synchronized (activated) with the database and defined entity
		
	}
	
	/**
	 * @return the tagEntity
	 */
	public Entity<MNTTag> getTagEntity() {
		return tagEntity;
	}

	/**
	 * @param tagEntity the tagEntity to set
	 */
	public void setTagEntity(Entity<MNTTag> tagEntity) {
		this.tagEntity = tagEntity;
	}

	/**
	 * @return the itemEntity
	 */
	public Entity<Item> getItemEntity() {
		return itemEntity;
	}

	/**
	 * @param itemEntity the itemEntity to set
	 */
	public void setItemEntity(Entity<Item> itemEntity) {
		this.itemEntity = itemEntity;
	}

    /**
     * @return the item2MntTagTableEntity
     */
    public Entity<Item2MntTagTable> getItem2MntTagTableEntity() {
        return item2MntTagTableEntity;
    }

    /**
     * @param item2MntTagTableEntity the item2MntTagTableEntity to set
     */
    public void setItem2MntTagTableEntity(
            Entity<Item2MntTagTable> item2MntTagTableEntity) {
        this.item2MntTagTableEntity = item2MntTagTableEntity;
    }

}
