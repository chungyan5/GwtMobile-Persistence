package com.gwtmobile.persistence.model;

import java.io.Serializable;

import com.google.appengine.api.datastore.Key;
import com.gwtmobile.persistence.meta.ItemMeta;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import org.persistencejs.Sync;

@Model(schemaVersion = 1)
public class Item implements Serializable {

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
    private String name;
    
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
        Item other = (Item) obj;
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
     * @param name the name to set
     */
    public void setName(String name) {
        ItemMeta.get().syncName(this, name);  
        this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
}
