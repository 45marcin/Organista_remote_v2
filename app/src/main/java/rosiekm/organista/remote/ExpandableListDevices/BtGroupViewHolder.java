package rosiekm.organista.remote.ExpandableListDevices;


import android.view.View;
import android.widget.TextView;

import rosiekm.organista.remote.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

public class BtGroupViewHolder extends GroupViewHolder {

    private TextView name;
    private TextView count;

        public BtGroupViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.list_item_name);
            count = itemView.findViewById(R.id.text_count);
        }

        public void setTitle(ExpandableGroup group) {
            name.setText(group.getTitle());
            count.setText(String.valueOf(group.getItemCount()));
        }
}