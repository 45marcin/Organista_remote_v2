package rosiekm.organista.remote.ExpandableListUSB;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import rosiekm.organista.remote.R;


public class FileViewHolder extends ChildViewHolder {

    public TextView name, length;
    public ImageButton playBtn, fncBtn, infoBtn;

    public FileViewHolder(View itemView) {
        super(itemView);
        this.name = itemView.findViewById(R.id.song_label);
        this.length = itemView.findViewById(R.id.song_length);
        this.fncBtn = itemView.findViewById(R.id.song_fnc);
        this.playBtn = itemView.findViewById(R.id.song_play);
        this.infoBtn = itemView.findViewById(R.id.btn_info);
    }

    public void setName(String name){
        this.name.setText(name);
    }
}