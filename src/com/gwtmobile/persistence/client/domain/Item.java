/**
 * 
 */
package com.gwtmobile.persistence.client.domain;

import com.gwtmobile.persistence.client.Collection;
import com.gwtmobile.persistence.client.Persistable;

/**
 * @author yan
 *
 */
public interface Item extends Persistable {
	public String getName();
	public void setName(String name);
//	public double getPtr();
//	public void setPtr(double ptr);
	public Collection<MNTTag> getMNTTags();		// many to many relationship	
}
