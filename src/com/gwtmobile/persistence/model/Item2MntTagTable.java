package com.gwtmobile.persistence.model;

import java.io.Serializable;

import com.google.appengine.api.datastore.Key;
import com.gwtmobile.persistence.meta.Item2MntTagTableMeta;
import com.gwtmobile.persistence.meta.ItemMeta;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import org.persistencejs.Sync;

@Model(schemaVersion = 1)
public class Item2MntTagTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Attribute(primaryKey = true)
    private Key key;

    @Attribute(version = true)
    private Long version;

    @Sync(timestamp = true)
    private Long lastChange;

    @Attribute(persistent = false)
    private boolean dirty = false;

    @Attribute(persistent = false)
    private boolean syncDirty = false;

    @Sync
    private String mNTTag_Items;
    
    @Sync
    private String item_MNTTags;
    
    /**
     * Returns the key.
     *
     * @return the key
     */
    public Key getKey() {
        return key;
    }

    /**
     * Sets the key.
     *
     * @param key
     *            the key
     */
    public void setKey(Key key) {
        this.key = key;
    }

    /**
     * Returns the version.
     *
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Sets the version.
     *
     * @param version
     *            the version
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * Sets the lastChange.
     *
     * @param lastChange
     *            the lastChange
     */
    public void setLastChange(Long lastChange) {
        this.lastChange = lastChange;
    }

    /**
     * Returns the lastChange.
     *
     * @return the lastChange
     */
    public Long getLastChange() {
        return lastChange;
    }

    /**
     * Sets the dirty.
     *
     * @param dirty
     *            the dirty
     */
    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    /**
     * Returns the dirty.
     *
     * @return the dirty
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * Sets the syncDirty.
     *
     * @param syncDirty
     *            the syncDirty
     */
    public void setSyncDirty(boolean syncDirty) {
        this.syncDirty = syncDirty;
    }

    /**
     * Returns the syncDirty.
     *
     * @return the syncDirty
     */
     public boolean isSyncDirty() {
     	return syncDirty;
     }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Item2MntTagTable other = (Item2MntTagTable) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    /**
     * @param mNTTag_Items the mNTTag_Items to set
     */
    public void setMNTTag_Items(String mNTTag_Items) {
        Item2MntTagTableMeta.get().syncMNTTag_Items(this, mNTTag_Items);  
        this.mNTTag_Items = mNTTag_Items;
    }

    /**
     * @return the mNTTag_Items
     */
    public String getMNTTag_Items() {
        return mNTTag_Items;
    }

    /**
     * @param item_MNTTags the item_MNTTags to set
     */
    public void setItem_MNTTags(String item_MNTTags) {
        Item2MntTagTableMeta.get().syncItem_MNTTags(this, item_MNTTags);  
        this.item_MNTTags = item_MNTTags;
    }

    /**
     * @return the item_MNTTags
     */
    public String getItem_MNTTags() {
        return item_MNTTags;
    }
}
