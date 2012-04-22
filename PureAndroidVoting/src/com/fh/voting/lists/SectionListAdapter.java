package com.fh.voting.lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

public class SectionListAdapter extends ArrayAdapter<IListItem> {

    private List<IListItem> votesSectionsList;
    private HolderFactory holderFactory;

    public SectionListAdapter(Context context, int sectionResourceId, int emptyResourceId, int voteResourceId, 
    		List<IListItem> votesSectionsList) {
        super(context, voteResourceId, votesSectionsList);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        holderFactory = new HolderFactory(inflater, sectionResourceId, emptyResourceId, voteResourceId);
        this.votesSectionsList = votesSectionsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        IListItem item = votesSectionsList.get(position);
        if(view == null) {
        	view = holderFactory.CreateView(item);
        }
        else {
        	view = holderFactory.UpgradeView(view, item);
        }
        return view;
    }
    
    private class HolderFactory {
    	int sectionResourceId = 0;
    	int emptyResourceId = 0;
    	int voteResourceId = 0;
    	private LayoutInflater inflater;
    	
    	public HolderFactory(LayoutInflater inflater, int sectionResourceId, int emptyResourceId, int voteResourceId) {
    		this.inflater = inflater;
    		this.sectionResourceId = sectionResourceId;
    		this.emptyResourceId = emptyResourceId;
    		this.voteResourceId = voteResourceId;
    	}
    	
    	public View CreateView(IListItem item) {
    		View view = this.inflater.inflate(this.getResourseId(item) , null);
    		IItemHolder holder = this.getHolder(item, view);
    		holder.fillData(item);
    		view.setTag(holder);
    		
    		return view;
    	}
    	
    	public View UpgradeView(View view, IListItem item) {
    		IItemHolder holder = (IItemHolder)view.getTag();
    		if(!this.checkHolder(item, holder)) {
    			return this.CreateView(item);
    		}
    		
    		holder.fillData(item);
    		view.setTag(holder);
    		
    		return view;
    	}
    	
    	private boolean checkHolder(IListItem item, IItemHolder holder) {
    		if(item instanceof VoteListItem && holder instanceof VoteHolder) {
    			return true;
    		}
    		else if (item instanceof SectionListItem && holder instanceof SectionHolder) {
    			return true;
    		}
    		else if (item instanceof EmptyListItem && holder instanceof EmptyItemHolder) {
    			return true;
    		}
    		
    		return false;
    	}
    	
    	private IItemHolder getHolder(IListItem item, View view) {
    		if(item instanceof VoteListItem) {
    			return new VoteHolder(view);
    		}
    		else if (item instanceof SectionListItem) {
    			return new SectionHolder(view);
    		}
    		else if (item instanceof EmptyListItem) {
    			return new EmptyItemHolder(view);
    		}
    		
    		throw new IllegalArgumentException();
    	}
    	
    	private int getResourseId(IListItem item) {
    		if(item instanceof VoteListItem) {
    			return this.voteResourceId;
    		}
    		else if (item instanceof SectionListItem) {
    			return this.sectionResourceId;
    		}
    		else if (item instanceof EmptyListItem) {
    			return this.emptyResourceId;
    		}
    		
    		throw new IllegalArgumentException();
    	}
    }
}
