package rosiekm.organista.remote.ExpandableListUSB;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

import rosiekm.organista.remote.AudioFileClass;
import rosiekm.organista.remote.ExpandableListDevices.BtDeviceViewHolder;
import rosiekm.organista.remote.ExpandableListDevices.BtGroupViewHolder;
import rosiekm.organista.remote.ExpandableListDevices.Room;
import rosiekm.organista.remote.FilesPackage.File;
import rosiekm.organista.remote.FilesPackage.ImageFile;
import rosiekm.organista.remote.FilesPackage.MyAudioFile;
import rosiekm.organista.remote.FilesPackage.VideoFile;
import rosiekm.organista.remote.R;
import rosiekm.organista.remote.StringUtils;
import rosiekm.organista.remote.internalMessage;

import static rosiekm.organista.remote.master_activity.ADD_TO_MY_LIST;
import static rosiekm.organista.remote.master_activity.PLAY_AUDIO;
import static rosiekm.organista.remote.master_activity.PLAY_VIDEO;
import static rosiekm.organista.remote.master_activity.REMOVE_FROM_MY_LIST;
import static rosiekm.organista.remote.master_activity.SHOW_IMAGE;
import static rosiekm.organista.remote.master_activity.SHOW_INFO;

public class USBDeviceAdapter extends ExpandableRecyclerViewAdapter<GroupViewHolder, FileViewHolder> {



    public USBDeviceAdapter(List<? extends ExpandableGroup> groups, internalMessage main) {
        super(groups);
        this.main = main;
    }

    internalMessage main;

    @Override
    public GroupViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_group_usb, parent, false);
        return new GroupViewHolder(view);
    }



    @Override
    public FileViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_song, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(FileViewHolder holder, int flatPosition, ExpandableGroup group,
                                      int childIndex) {
        final File file = ((USBDevice) group).getItems().get(childIndex);
        holder.fncBtn.setVisibility(View.GONE);
        holder.setName(file.getTitle());

        File tmp = (File)((USBDevice) group).getItems().get(childIndex);
        if (tmp.getClass() == MyAudioFile.class) {
             holder.length.setText(StringUtils.getTimeInString(((MyAudioFile)tmp).getLength()));
            holder.length.setVisibility(View.VISIBLE);
        }
        else{
            holder.length.setVisibility(View.GONE);
        }
        //holder.length.setText(StringUtils.getTimeInString(((Room) group).getItems().get(childIndex).getLength()));
        holder.playBtn.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (tmp.getClass() == MyAudioFile.class) {
                    main.sendToMainActivity(PLAY_AUDIO, tmp.getPath());
                }
                else if (tmp.getClass() == VideoFile.class) {
                    main.sendToMainActivity(PLAY_VIDEO, tmp.getPath());
                }
                else if (tmp.getClass() == ImageFile.class) {
                    main.sendToMainActivity(SHOW_IMAGE, tmp.getPath());
                }
                return false;
            }
        });
        holder.infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main.sendToMainActivity(SHOW_INFO,((USBDevice) group).getItems().get(childIndex));
            }
        });
    }

    @Override
    public void onBindGroupViewHolder(GroupViewHolder holder, int flatPosition,
                                      ExpandableGroup group) {
        holder.setTitle(group);
        if (group.getTitle().toLowerCase().contains("audio")){
            holder.icon.setImageResource(R.drawable.ic_music_note_black_24dp);
        }
        else if (group.getTitle().toLowerCase().contains("image")){
            holder.icon.setImageResource(R.drawable.ic_image_black_24dp);
        }
        else {
            holder.icon.setImageResource(R.drawable.ic_movie_black_24dp);
        }

    }
}