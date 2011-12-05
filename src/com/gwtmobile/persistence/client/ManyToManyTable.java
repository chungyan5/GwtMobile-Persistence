/**
 * 
 */
package com.gwtmobile.persistence.client;

import java.util.LinkedList;

import com.gwtmobile.persistence.client.domain.Item;
import com.gwtmobile.persistence.client.domain.Item2MntTagTable;
import com.gwtmobile.persistence.client.domain.MNTTag;

/**
 * @author yan
 * Handle Many to Many Processes
 */
public class ManyToManyTable {

    private ClientFactoryImpl clientFactory;
    private LinkedList<Item> tagAttachedItemsResults = new LinkedList<Item>();
    private LinkedList<MNTTag> itemAttachedTagsResults = new LinkedList<MNTTag>();

    /**
     * @param clientFactory
     */
    public ManyToManyTable(ClientFactoryImpl clientFactory) {
        this.clientFactory = clientFactory;
    }

    public void tagAttachedItems(MNTTag selectedTag, final CollectionCallback<Item> callback) {
        
        if (clientFactory != null) {
            
            tagAttachedItemsResults.clear();
            
            clientFactory.getDbconn().getItem2MntTagTableEntity().all()
                .filter("mNTTag_Items", "=", selectedTag.getId())           // Selected Tag
                    .list(new CollectionCallback<Item2MntTagTable>() {

                    @Override
                    public void onSuccess(final Item2MntTagTable[] results) {
                        for (int i=0;i<results.length;i++) {
                            final int index = i;
                            clientFactory.getDbconn().getItemEntity().load(results[i].getItem_MNTTags(), new ScalarCallback<Item>() {

                                @Override
                                public void onSuccess(Item item) {
                                    tagAttachedItemsResults.add(item);
                                    if ((index+1)==results.length) {
                                        Item[] returnArray = new Item[results.length];
                                        callback.onSuccess(tagAttachedItemsResults.toArray(returnArray));
                                    }
                                }
                                
                            });
                        }
                        
                    }
                });

        }
    }
    
    public void itemAttachedTags(Item selectedItem, final CollectionCallback<MNTTag> callback) {
        
        if (clientFactory != null) {
            
            itemAttachedTagsResults.clear();
            
            clientFactory.getDbconn().getItem2MntTagTableEntity().all()
                .filter("item_MNTTags", "=", selectedItem.getId())           // Selected Item
                    .list(new CollectionCallback<Item2MntTagTable>() {

                    @Override
                    public void onSuccess(final Item2MntTagTable[] results) {
                        for (int i=0;i<results.length;i++) {
                            final int index = i;
                            clientFactory.getDbconn().getTagEntity().load(results[i].getMNTTag_Items(), new ScalarCallback<MNTTag>() {

                                @Override
                                public void onSuccess(MNTTag tag) {
                                    itemAttachedTagsResults.add(tag);
                                    if ((index+1)==results.length) {
                                        MNTTag[] returnArray = new MNTTag[results.length];
                                        callback.onSuccess(itemAttachedTagsResults.toArray(returnArray));
                                    }
                                }
                                
                            });
                        }
                        
                    }
                });

        }
    }
    
}
