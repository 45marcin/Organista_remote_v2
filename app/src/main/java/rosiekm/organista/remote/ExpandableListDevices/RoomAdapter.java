package rosiekm.organista.remote.ExpandableListDevices;


import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import rosiekm.organista.remote.AudioFileClass;

import rosiekm.organista.remote.R;
import rosiekm.organista.remote.StringUtils;
import rosiekm.organista.remote.internalMessage;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import static rosiekm.organista.remote.master_activity.ADD_TO_MY_LIST;
import static rosiekm.organista.remote.master_activity.PLAY_AUDIO;
import static rosiekm.organista.remote.master_activity.REMOVE_FROM_MY_LIST;
import static rosiekm.organista.remote.master_activity.SHOW_INFO;

public class RoomAdapter extends ExpandableRecyclerViewAdapter<BtGroupViewHolder, BtDeviceViewHolder> {

    public RoomAdapter(List<? extends ExpandableGroup> groups, internalMessage main) {
        super(groups);
        this.main = main;
    }

    internalMessage main;

    @Override
    public BtGroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_group, parent, false);
        return new BtGroupViewHolder(view);
    }

    @Override
    public BtDeviceViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_song, parent, false);
        return new BtDeviceViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(BtDeviceViewHolder holder, int flatPosition, ExpandableGroup group,
                                      int childIndex) {
        final AudioFileClass artist = ((Room) group).getItems().get(childIndex);
        holder.setName(artist.getTitle());
        holder.length.setText(StringUtils.getTimeInString(((Room) group).getItems().get(childIndex).getLength()));
        holder.playBtn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                main.sendToMainActivity(PLAY_AUDIO,((Room) group).getItems().get(childIndex).getPath());
                return false;
            }
        });
        holder.infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.sendToMainActivity(SHOW_INFO,((Room) group).getItems().get(childIndex));
            }
        });
        switch (((Room) group).getItems().get(childIndex).getType()){
            case 0x0:
                holder.fncBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        main.sendToMainActivity(ADD_TO_MY_LIST,((Room) group).getItems().get(childIndex));
                    }
                });
                holder.fncBtn.setImageResource(R.drawable.ic_playlist_add_black_24dp);
                break;
            case 0x1:
                holder.fncBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        main.sendToMainActivity(REMOVE_FROM_MY_LIST,((Room) group).getItems().get(childIndex));
                    }
                });
                holder.fncBtn.setImageResource(R.drawable.ic_delete_sweep_black_24dp);
                break;
        }

    }

    @Override
    public void onBindGroupViewHolder(BtGroupViewHolder holder, int flatPosition,
                                      ExpandableGroup group) {
        holder.setTitle(group);

    }
}