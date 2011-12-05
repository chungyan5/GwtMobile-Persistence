/**
 * 
 */
package com.gwtmobile.persistence.client.ui;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;


import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.gwtmobile.persistence.client.ClientFactoryImpl;
import com.gwtmobile.persistence.client.CollectionCallback;
import com.gwtmobile.persistence.client.ManyToManyTable;
import com.gwtmobile.persistence.client.domain.Item;
import com.gwtmobile.persistence.client.domain.MNTTag;
import com.gwtmobile.ui.client.event.SelectionChangedEvent;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.ListPanel;

/**
 * Display the Main View 
 * 
 * @author yan
 *
 */
public class TagAttachItemsDesktopView extends Page {

	interface ItemAttachTagsDesktopViewUiBinder extends UiBinder<HTMLPanel, TagAttachItemsDesktopView> {}
	private static ItemAttachTagsDesktopViewUiBinder uiBinder = GWT.create(ItemAttachTagsDesktopViewUiBinder.class);
	
	@UiField ListPanel listSelectedTags;
	@UiField ListPanel listRelatedTags;
	@UiField ListPanel listRelatedItems;
	
	private ManyToManyTable manyToManyTable;
	private ClientFactoryImpl clientFactory;
	private MNTTag selectedTag;

	/**
	 * a Memory for Selected Tag(s) which in FIFO order 
	 */
	private LinkedList<MNTTag> linkedListSelectedTags = new LinkedList<MNTTag>();
	
	public void pushSelectTag(MNTTag tag) {
		
		// adding selected Tag ================================================
		linkedListSelectedTags.addLast(tag);
		displaySelectedTagsScreen();
		
		// get attached item(s) ===============================================
		//	looping of linkedListSelectedTags for all tag(s)
		// 		get the tag and its attached item(s)
		manyToManyTable.tagAttachedItems(tag, new CollectionCallback<Item>() {

            @Override
            public void onSuccess(Item[] results) {
                
        //      compare "existing treeSetAttachedItems" and "new tag attached Items" and find the same item(s)
                TreeSet<Item> treeSetTempAttachedItems = new TreeSet<Item>(new Comparator<Item>() {

                    @Override
                    public int compare(Item arg0, Item arg1) {
                        return arg0.getName().compareTo(arg1.getName());
                    }
                    
                });
                
                if (linkedListSelectedTags.size()!=1) {
                    for (Item item : results) {
                        // if this item already exist in last treeSetAttachedItems, and also existing in this new tag, then adding this new item 
                        if (treeSetAttachedItems.contains(item)) 
                            treeSetTempAttachedItems.add(item);    
                    }
                } else {
                    for (Item item : results) {
                        treeSetTempAttachedItems.add(item);
                    }
                }
                
        //      save the attached items list
                treeSetAttachedItems.clear();
                treeSetAttachedItems.addAll(treeSetTempAttachedItems);
                
                displayRelatedItemsScreen();
                
        // set the related tag(s) =============================================
        //  base the each attached item, search the related tag(s)
        //  if already existing of related tag, ignore it
                final int noOfAttachedItem = treeSetAttachedItems.size();
                int cntOfAttachedItem = 0;
                Iterator<Item> it = treeSetAttachedItems.iterator();
                while(it.hasNext()) {
                    final int tempCntOfAttachedItem = ++cntOfAttachedItem;
                    
                    manyToManyTable.itemAttachedTags(it.next(), new CollectionCallback<MNTTag>() {

                        @Override
                        public void onSuccess(MNTTag[] results) {
        //  save the attached items list
                          for (MNTTag tag : results) {
                              treeSetRelatedTags.add(tag);
                          }
                          
                          if (tempCntOfAttachedItem==noOfAttachedItem) {
                              
        //      remove Tag(s) in Related Section if already in Selected section 
                              for (int i=0;i<linkedListSelectedTags.size();i++) {
                                  if (treeSetRelatedTags.contains(linkedListSelectedTags.get(i)))
                                      treeSetRelatedTags.remove(linkedListSelectedTags.get(i));
                              }
                              
                              displayRelatedTagsScreen();
                          }
                          
                        }
                        
                    });
                }
                
            }
		    
		});/*tag.getItems().list(new CollectionCallback<Item>() {
        public void onSuccess(Item[] results) {

        }
        });*/
		
	}
	
	/*public int getSizeSelectTag() {
		return linkedListSelectedTags.size();
	}
	
	public MNTTag getSelectTag(int index) {
		return linkedListSelectedTags.get(index);
	}
	
	public MNTTag[] getArraySelectTag() {
		return (MNTTag[]) linkedListSelectedTags.toArray();
	}
	
	public boolean selectTagContain(Object o) {
		return linkedListSelectedTags.contains(o);
	}
	
	public MNTTag popSelectTag() {
		return linkedListSelectedTags.removeFirst();
	}
	
	public void clrSelectTags() {
		linkedListSelectedTags.clear();
	}*/
	
	/**
	 * a Memory for Related Tag(s) which in sorting(ascending) order and cannot contain duplicate elements 
	 */
	private TreeSet<MNTTag> treeSetRelatedTags = new TreeSet<MNTTag>(new Comparator<MNTTag>() {

		@Override
		public int compare(MNTTag arg0, MNTTag arg1) {
			return arg0.getName().compareTo(arg1.getName());
		}
		
	});
	
	public void addRelatedTag(MNTTag tag) {
		treeSetRelatedTags.add(tag);
	}
	
	public int getSizeRelatedTag() {
		return treeSetRelatedTags.size();
	}
	
	public void clrRelatedTags() {
		treeSetRelatedTags.clear();
	}
	
	/**
	 * a Memory for Attached Item(s) which in sorting(ascending) order and cannot contain duplicate elements 
	 */
	private TreeSet<Item> treeSetAttachedItems = new TreeSet<Item>(new Comparator<Item>() {

		@Override
		public int compare(Item arg0, Item arg1) {
			return arg0.getName().compareTo(arg1.getName());
		}
		
	});
	
	/**
	 * prepare main UI of Tag Attach Items
	 */
	public TagAttachItemsDesktopView(ClientFactoryImpl clientFactory, MNTTag selectedTag) {

		// set the clientFactory for most of common objects
		this.clientFactory = clientFactory;
		
		this.manyToManyTable = new ManyToManyTable(this.clientFactory);
		
		this.selectedTag = selectedTag;
		
	    // init. this View, All composites must call initWidget() in their constructors.
		//	and must put at the end before setup all above
	    // However, any object(s) created for uiBinder is worked in initWidget(), so 
	    //	using these kind of objects after initWidget()
	    initWidget(uiBinder.createAndBindUi(this));

	    // clean Selected Tag and related Tag
//	    clrSelectTags();
//	    clrRelatedTags();
	    
	    // add the first tag
	    pushSelectTag(selectedTag);
	    
	}
	
	public void displaySelectedTagsScreen() {
		
		// clear old display ====================================
		listSelectedTags.clear();
		
		// show the selected tags ====================================
		Iterator<MNTTag> itSelectedTags = linkedListSelectedTags.iterator();
		while (itSelectedTags.hasNext()) {
			Label lb = new Label(itSelectedTags.next().getName());
			lb.setStyleName("selectTags");
			listSelectedTags.add(lb);
		}
	}
	
	public void displayRelatedTagsScreen() {
		
		// clear old display ====================================
		listRelatedTags.clear();
		
		// show the related tags =====================================
		Iterator<MNTTag> itRelatedTags = treeSetRelatedTags.iterator();
		while (itRelatedTags.hasNext()) {
			Label lb = new Label(itRelatedTags.next().getName());
			lb.setStyleName("relatedTags");
			listRelatedTags.add(lb);
		}
		
	}
	
	public void displayRelatedItemsScreen() {
		
		// clear old display ====================================
		listRelatedItems.clear();
		
		// show the related items ====================================
		Iterator<Item> itRelatedItems = treeSetAttachedItems.iterator();
		while (itRelatedItems.hasNext()) {
			Label lb = new Label(itRelatedItems.next().getName());
			lb.setStyleName("relatedItems");
			listRelatedItems.add(lb);
		}
	}
	
	// Click the selected item
    @UiHandler("listRelatedTags")
    void onListSelectionChanged(SelectionChangedEvent e) {
    	MNTTag[] tags = treeSetRelatedTags.toArray(new MNTTag[0]);
	    pushSelectTag(tags[e.getSelection()]);
    }
    
}
