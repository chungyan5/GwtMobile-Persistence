/**
 * 
 */
package com.gwtmobile.persistence.client.ui;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtmobile.persistence.client.ClientFactoryImpl;
import com.gwtmobile.persistence.client.CollectionCallback;
import com.gwtmobile.persistence.client.Persistence;
import com.gwtmobile.persistence.client.ScalarCallback;
import com.gwtmobile.persistence.client.domain.Item;
import com.gwtmobile.persistence.client.domain.Item2MntTagTable;
import com.gwtmobile.persistence.client.domain.MNTTag;
import com.gwtmobile.ui.client.event.SelectionChangedEvent;
import com.gwtmobile.ui.client.page.Page;
import com.gwtmobile.ui.client.widgets.Button;
import com.gwtmobile.ui.client.widgets.CheckBox;
import com.gwtmobile.ui.client.widgets.CheckBoxGroup;
import com.gwtmobile.ui.client.widgets.ListPanel;

/**
 * Display the Main View 
 * 
 * @author yan
 *
 */
public class ItemAttachTagsDesktopView extends Page {

	interface ItemAttachTagsDesktopViewUiBinder extends UiBinder<HTMLPanel, ItemAttachTagsDesktopView> {}
	private static ItemAttachTagsDesktopViewUiBinder uiBinder = GWT.create(ItemAttachTagsDesktopViewUiBinder.class);
	
    @UiField ListPanel list;
	@UiField CheckBoxGroup cBoxGroup;
	@UiField Button resetDb;
	
	private ClientFactoryImpl clientFactory;
	private Item selectedItem;

	/**
	 * prepare main UI
	 */
	public ItemAttachTagsDesktopView(ClientFactoryImpl clientFactory, Item selectedItem) {

		// set the clientFactory for most of common objects
		this.clientFactory = clientFactory;
		
		this.selectedItem = selectedItem;
		
	    // init. this View, All composites must call initWidget() in their constructors.
		//	and must put at the end before setup all above
	    // However, any object(s) created for uiBinder is worked in initWidget(), so 
	    //	using these kind of objects after initWidget()
	    initWidget(uiBinder.createAndBindUi(this));

	    loadTagsForThisItem();
	}

	public void loadTagsForThisItem() {
		
		// load all tags =============================================================================================
		if(clientFactory != null) {
			
					clientFactory.getDbconn().getTagEntity().all().order("Name", true).list(new CollectionCallback<MNTTag>() {
						
							@Override
							public void onSuccess(final MNTTag[] resultsTag) {
								
		// for each tag, read the attached Item, if matching to Selected Item, mark checked =========================
								for (int i=0;i<resultsTag.length;i++) {
									
									final CheckBoxItemAttachTags cb = new CheckBoxItemAttachTags();
									cb.setMntTag(resultsTag[i]);
									
		//  if this tag is attached into selected Item, marked checked ===========================================================
                                    clientFactory.getDbconn().getItem2MntTagTableEntity().all()
                                    .filter("item_MNTTags", "=", selectedItem.getId())          // Selected Item
                                        .filter("mNTTag_Items", "=", resultsTag[i].getId())     //  attached Tags
                                            .count(new ScalarCallback<Integer>() {
                                                
                                                @Override
                                                public void onSuccess(Integer result) {
                                                    if (result.intValue()==1) cb.setValue(true); 
                                                }
                                            });
									/*resultsTag[i].getItems().list(new CollectionCallback<Item>() {

                                        public void onSuccess(Item[] resultsItem) {
                                            for (int j=0;j<resultsItem.length;j++)
                                                if (resultsItem[j].getName().compareTo(selectedItem.getName())==0)
                                                    cb.setValue(true);
                                            
        //  otherwise marked non-checked =============================================================================
                                            // do nothing which default as un-checked already
                                        }
									});*/
									
		//  display this tag =============================================================================
									cBoxGroup.add(cb);         // list.add(cb);
								}
							}
						});
		}
	}
	
    @UiHandler("cBoxGroup")
    void onGroup1SelectionChanged(SelectionChangedEvent e) {
        
        // click to attach this tag into corresponding item ===========================================================
        CheckBoxItemAttachTags radio = (CheckBoxItemAttachTags) cBoxGroup.getWidget(e.getSelection());
        
        //      understand the updated check box status
        boolean checked = radio.getValue();
        
        //      adding a relationship ===========================================================
        if (checked) {
            
            //          attach tag into this item
            selectedItem.getMNTTags().add(radio.getMntTag());Item2MntTagTable item2MntTagTable = clientFactory.getDbconn().getItem2MntTagTableEntity().newInstance();
                item2MntTagTable.setMNTTag_Items(radio.getMntTag().getId());
                item2MntTagTable.setItem_MNTTags(selectedItem.getId());
                Persistence.flush();

        //      remove a relationship ===========================================================
        } else {
            
            selectedItem.getMNTTags().remove(radio.getMntTag());if(clientFactory != null) {
                
                clientFactory.getDbconn().getItem2MntTagTableEntity().all()
                    .filter("mNTTag_Items", "=", radio.getMntTag().getId())
                        .filter("item_MNTTags", "=", selectedItem.getId())
                            .one(new ScalarCallback<Item2MntTagTable>() {

                    @Override
                    public void onSuccess(Item2MntTagTable result) {
                        Persistence.remove(result);
                        Persistence.flush();
                    }
                    
                });
                
            }
            
        }

    }

    // reset db handling
    @UiHandler("resetDb")
    void onButtonResetDbClicked(ClickEvent event) {

        Page ResetDbDesktopView = new ResetDbDesktopView(clientFactory);
        this.goTo(ResetDbDesktopView);
        
    }
 
    private class CheckBoxItemAttachTags extends CheckBox {
        
        private MNTTag mntTag;

        /**
         * @return the mntTag
         */
        public MNTTag getMntTag() {
            return mntTag;
        }

        /**
         * @param mntTag the mntTag to set
         */
        public void setMntTag(MNTTag mntTag) {
            this.mntTag = mntTag;
            setText(this.mntTag.getName());
        }
        
    }
    
}
