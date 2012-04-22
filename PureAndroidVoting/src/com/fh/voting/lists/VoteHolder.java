package com.fh.voting.lists;

import android.view.View;
import android.widget.TextView;

import com.fh.voting.R;
import com.fh.voting.model.Vote;

public class VoteHolder implements IItemHolder {
    private TextView titleText;
    private TextView detailsText;

    public VoteHolder(View v) {
        titleText = (TextView) v.findViewById(R.id.txtVoteTitle);
        detailsText = (TextView) v.findViewById(R.id.txtVoteDetail);
    }

    public void fillData(Vote vote) {
        titleText.setText(vote.getTitle());
        detailsText.setText(vote.getText());
    }

	@Override
	public void fillData(IListItem item) {
		VoteListItem voteListItem = (VoteListItem)item;
		this.fillData(voteListItem.getVote());
	}
}
