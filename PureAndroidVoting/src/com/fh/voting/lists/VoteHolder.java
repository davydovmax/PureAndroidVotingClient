package com.fh.voting.lists;

import android.view.View;
import android.widget.TextView;

import com.fh.voting.R;
import com.fh.voting.model.Vote;
import com.fh.voting.parsers.DateTimeConverter;

public class VoteHolder implements IItemHolder {
	private TextView titleText;
	private TextView detailsText;
	private TextView txtVoteStatus;
	private TextView txtVoteStartDate;

	public VoteHolder(View v) {
		titleText = (TextView) v.findViewById(R.id.txtVoteTitle);
		detailsText = (TextView) v.findViewById(R.id.txtVoteDetail);
		txtVoteStatus = (TextView) v.findViewById(R.id.txtVoteStatus);
		txtVoteStartDate = (TextView) v.findViewById(R.id.txtVoteStartDate);
	}

	public void fillData(Vote vote) {
		titleText.setText(vote.getTitle());
		detailsText.setText(vote.getText());

		String status = "New";
		if (vote.getStatus() == Vote.Status.Public) {
			status = "Not started";
		}
		if (vote.getStatus() == Vote.Status.Started) {
			status = "Active";
		}
		if (vote.getStatus() == Vote.Status.Ended) {
			status = "Ended";
		}
		txtVoteStatus.setText(status);
		txtVoteStartDate.setText(DateTimeConverter.toDisplayString(vote.getStartDate()));
	}

	@Override
	public void fillData(IListItem item) {
		VoteListItem voteListItem = (VoteListItem) item;
		this.fillData(voteListItem.getVote());
	}
}
