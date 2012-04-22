package com.fh.voting.activities;

import java.util.ArrayList;

import com.fh.voting.R;
import com.fh.voting.Server;
import com.fh.voting.SharedPreferenceManager;
import com.fh.voting.VoteDebug;
import com.fh.voting.lists.EmptyListItem;
import com.fh.voting.lists.IListItem;
import com.fh.voting.lists.SectionListAdapter;
import com.fh.voting.lists.SectionListItem;
import com.fh.voting.lists.VoteListItem;
import com.fh.voting.model.Vote;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

public class HomeActivity extends Activity {
	private VoteDebug voteDebug;
	private Server server;
	private SharedPreferenceManager preferences;
	private ListView lstCategories;
	
	public final static String ITEM_TITLE = "title";  
    public final static String ITEM_CAPTION = "caption"; 
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Window window = getWindow();
    	window.setFormat(PixelFormat.RGBA_8888);
        
        this.voteDebug = new VoteDebug(this);
        this.preferences = new SharedPreferenceManager(this);
        this.server = new Server(preferences.getPhoneId());
        this.lstCategories = (ListView)this.findViewById(R.id.lstCategories);
        
        if (this.preferences.isFirstStart()){
        	this.preferences.setNotFirstStart();
        }
    }
    
    @Override 
    protected void onResume() {
    	super.onResume();
    	// check registration and register if needed
        // will trigger registration activity
        if(this.isRegistrationRequired()) {
        	return;
        }
    	
        // load votes from web service
		this.loadVotes();
    };
    
    private boolean isRegistrationRequired() {
    	// check registration on server
        boolean isServerRegistered = false;
        try {
			isServerRegistered = this.server.getIsRegistered();
		} catch (Exception e1) {
			isServerRegistered = false;
		}        
        
        // start registration activity if needed
        if(!isServerRegistered || !this.preferences.isRegistered()) {
        	// show registration activity
        	Intent intent = new Intent(HomeActivity.this, RegisterActivity.class);
            startActivity(intent);
            return true;
        }
        
    	return false;
	}
    
    private void loadVotes() {
		ArrayList<Vote> votes = null;
        
        try {
        	votes = this.server.getMyVotes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
//        VoteListAdapter adapter = new VoteListAdapter(this, R.layout.vote_list_item, votes);
//        this.lstCategories.setAdapter(adapter);
        
        ArrayList<IListItem> items = new ArrayList<IListItem>();
        
        // my votes
        items.add(new SectionListItem("My Votes"));
        if(votes.size() == 0) {
        	items.add(new EmptyListItem("You have no own votes, but you can create one. Just press this item."));
        }
        else {
        	for(int i = 0; i < 5 && i < votes.size(); ++i) {
		    	items.add(new VoteListItem(votes.get(i)));
		    }
        }
        
        // pending votes
        items.add(new SectionListItem("Pending Votes"));
        items.add(new EmptyListItem("No pending votes or invitations."));
        
        // top public votes
        items.add(new SectionListItem("Top Votes"));
        items.add(new EmptyListItem("No available public votes."));
        
        SectionListAdapter adapter = new SectionListAdapter(this, 
        		R.layout.header_list_item, R.layout.empty_list_item, R.layout.vote_list_item, items);
        this.lstCategories.setAdapter(adapter);
    }
    
    public void onResetClicked(View v) {
		this.voteDebug.ResetClient();
    }
    
    public void onManageServerClicked(View v) {
    	Intent intent = new Intent(HomeActivity.this, ManageServerActivity.class);
        startActivity(intent);
    }
}