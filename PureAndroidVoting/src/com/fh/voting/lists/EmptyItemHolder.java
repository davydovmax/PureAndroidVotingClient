package com.fh.voting.lists;

import android.view.View;
import android.widget.TextView;

import com.fh.voting.R;

public class EmptyItemHolder implements IItemHolder {
    private TextView titleText;

    public EmptyItemHolder(View v) {
        titleText = (TextView) v.findViewById(R.id.txtEmptyText);
    }

    public void fillData(IListItem item) {
        titleText.setText(item.getTitle());
    }
}
