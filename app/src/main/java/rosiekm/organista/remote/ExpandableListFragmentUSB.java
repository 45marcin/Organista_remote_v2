package rosiekm.organista.remote;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import rosiekm.organista.remote.ExpandableListUSB.USBDevice;
import rosiekm.organista.remote.ExpandableListUSB.USBDeviceAdapter;
import rosiekm.organista.remote.FilesPackage.File;
import rosiekm.organista.remote.FilesPackage.ImageFile;
import rosiekm.organista.remote.FilesPackage.MyAudioFile;
import rosiekm.organista.remote.FilesPackage.VideoFile;

import static rosiekm.organista.remote.master_activity.containString;


public class ExpandableListFragmentUSB extends Fragment implements  internalMessage{

    internalMessage master;
    USBDeviceAdapter adapter;
    RecyclerView mainlistView;
    ArrayList<USBDevice> rooms;
    int dataType = 0;

    public ExpandableListFragmentUSB() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View main = inflater.inflate(R.layout.fragment_audio_files_main_list, container, false);

        mainlistView = main.findViewById(R.id.main_listview);
        mainlistView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        if (adapter == null){
            if (rooms == null){
                rooms = new ArrayList<>();
            }

        }
        adapter = new USBDeviceAdapter(rooms, this);
        mainlistView.setAdapter(adapter);
        return main;

    }

    public void setMaster(internalMessage master){
        this.master = master;
    }

    ArrayList<Integer> expanded;

    public  void updateRooms(ArrayList<File> files){

        if (adapter != null) {
            expanded = new ArrayList<Integer>();
            for (int x = 0; x < rooms.size(); x++) {
                try {
                    if (adapter.isGroupExpanded(x)) {
                        expanded.add(x);
                    }
                }
                catch (Exception e){

                }
            }
            if (mainlistView != null) {
                mainlistView.setAdapter(adapter);
            }
        }



        if (this.rooms == null){
            this.rooms = new ArrayList<>();
        }
        else{
            this.rooms.clear();
        }


        this.rooms.addAll(getUSBData(files));
        try {
            adapter = new USBDeviceAdapter(this.rooms, this);
        }
        catch (Exception e){

        }

        try {
            if (expanded != null) {
                for (Integer x : expanded) {
                    try {
                        adapter.toggleGroup(x);
                    } catch (Exception e) {

                    }
                }
            }
        }
        catch (Exception e){

        }




    }
    ArrayList<USBDevice> getUSBData(ArrayList<File> audioFiles){
        if(audioFiles != null) {
            ArrayList<String> genres_list = new ArrayList<>();
            for (File x : audioFiles) {
                if (x.getClass() == MyAudioFile.class){
                    if (!containString(genres_list, "Audio")) {
                        genres_list.add("Audio");
                    }
                }
                else if (x.getClass() == VideoFile.class){
                    if (!containString(genres_list, "Video")) {
                        genres_list.add("Video");
                    }
                }
                else if (x.getClass() == ImageFile.class){
                    if (!containString(genres_list, "Image")) {
                        genres_list.add("Image");
                    }
                }
            }

            Collections.sort(genres_list, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
            ArrayList<USBDevice> out = new ArrayList<>();
            ArrayList<File> audioFileTmp = audioFiles;
            Collections.sort(audioFileTmp, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return o1.getTitle().compareTo(o2.getTitle());
                }
            });
            //audioFileTmp.sort(new Comparator<AudioFileClass>() {
            //     @Override
            //     public int compare(AudioFileClass o1, AudioFileClass o2) {
            //         return o1.getTitle().compareTo(o2.getTitle());
            //    }
            // });
            for (String y : genres_list) {
                ArrayList<File> tmp2 = new ArrayList<>();
                for (File x : audioFileTmp) {
                    if (y.contains("Audio")){
                        if (x.getClass() == MyAudioFile.class) {
                            tmp2.add(x);
                        }
                    }
                    else if (y.contains("Video")){
                        if (x.getClass() == VideoFile.class) {
                            tmp2.add(x);
                        }
                    }
                    else if (y.contains("Image")){
                        if (x.getClass() == ImageFile.class) {
                            tmp2.add(x);
                        }
                    }

                }
                if (tmp2.size() > 0) {
                    out.add(new USBDevice(y, tmp2));
                }
            }

            return out;
        }
        else{
            ArrayList<USBDevice> out = new ArrayList<>();
            return  out;
        }
    }

    @Override
    public void sendToMainActivity(int what, Object obj) {
        Log.d("fragment", "message to me");
        master.sendToMainActivity(what, obj);
    }

    public void setDataType(int dataType){
        this.dataType = dataType;
    }
}
