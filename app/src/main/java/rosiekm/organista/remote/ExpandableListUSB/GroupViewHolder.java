package rosiekm.organista.remote.ExpandableListUSB;


import android.media.Image;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import rosiekm.organista.remote.R;

public class GroupViewHolder extends com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder {

    private TextView name;
    private TextView count;
        ImageView icon;

        public GroupViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.list_item_name);
            icon = itemView.findViewById(R.id.icon_image);
            count = itemView.findViewById(R.id.text_count);
        }

        public void setTitle(ExpandableGroup group) {
            name.setText(group.getTitle());
            count.setText(String.valueOf(group.getItemCount()));
        }
}