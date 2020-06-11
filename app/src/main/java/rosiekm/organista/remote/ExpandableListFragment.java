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

import rosiekm.organista.remote.ExpandableListDevices.Room;
import rosiekm.organista.remote.ExpandableListDevices.RoomAdapter;
import rosiekm.organista.remote.ExpandableListUSB.USBDevice;
import rosiekm.organista.remote.ExpandableListUSB.USBDeviceAdapter;


public class ExpandableListFragment extends Fragment implements  internalMessage{

    internalMessage master;
    USBDeviceAdapter adapter;
    RecyclerView mainlistView;
    ArrayList<USBDevice> rooms;
    int dataType = 0;

    public ExpandableListFragment() {
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
            adapter = new USBDeviceAdapter(rooms, this);
        }
        mainlistView.setAdapter(adapter);
        return main;

    }

    public void setMaster(internalMessage master){
        this.master = master;
    }

    ArrayList<Integer> expanded;

    public  void updateRooms(ArrayList<USBDevice> rooms){
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
        }
        if (mainlistView != null) {
            mainlistView.setAdapter(adapter);
        }
        if (this.rooms == null){
            this.rooms = new ArrayList<>();
        }
        else{
            this.rooms.clear();
        }
        this.rooms.addAll(rooms);
        adapter = new USBDeviceAdapter(this.rooms,this);
        if (expanded != null) {
            for (Integer x : expanded) {
                try {
                    adapter.toggleGroup(x);
                }
                catch (Exception e){

                }
            }
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
