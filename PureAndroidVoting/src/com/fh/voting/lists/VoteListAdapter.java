package com.fh.voting.lists;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import com.fh.voting.model.Vote;

public class VoteListAdapter extends ArrayAdapter<Vote> {

    private List<Vote> items;
    private LayoutInflater inflater;
    private int textViewResourceId;

    public VoteListAdapter(Context context, int textViewResourceId, List<Vote> items) {
        super(context, textViewResourceId, items);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.textViewResourceId = textViewResourceId;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Vote vote = items.get(position);
        VoteHolder holder;

        if (view == null) {
            view = inflater.inflate(textViewResourceId, null);
            holder = new VoteHolder(view);
        } else {
            holder = (VoteHolder)view.getTag();
        }

        holder.fillData(vote);
        view.setTag(holder);
        return view;
    }
}
