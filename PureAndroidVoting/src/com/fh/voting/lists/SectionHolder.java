package com.fh.voting.lists;

import android.view.View;
import android.widget.TextView;

import com.fh.voting.R;

public class SectionHolder implements IItemHolder {
    private TextView titleText;

    public SectionHolder(View v) {
        titleText = (TextView) v.findViewById(R.id.txtHeaderText);
    }

    public void fillData(IListItem item) {
        titleText.setText(item.getTitle());
    }
}
