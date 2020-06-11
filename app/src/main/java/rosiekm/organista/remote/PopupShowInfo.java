package rosiekm.organista.remote;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class PopupShowInfo extends DialogFragment {
    private internalMessage main;
    private ArrayList<String> myLists;
    AudioFileClass audioFileClass;

    Button addNew;
    Button select;
    Spinner myListSpinner;
    EditText newName;

    public PopupShowInfo() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static PopupShowInfo newInstance(String title) {
        PopupShowInfo frag = new PopupShowInfo();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.popup_chose_list, container);
        addNew = view.findViewById(R.id.add_new);
        newName = view.findViewById(R.id.my_list_name);

        addNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioFileClass.setAlbum(newName.getText().toString());
                main.sendToMainActivity(master_activity.ADD_TO_MY_LIST_FINAL, audioFileClass);
                dismiss();
            }
        });

        myListSpinner = view.findViewById(R.id.spinner_my_lists);


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, myLists);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        myListSpinner.setAdapter(dataAdapter);
        select = view.findViewById(R.id.select_from_list);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                audioFileClass.setAlbum(myLists.get(myListSpinner.getSelectedItemPosition()));
                main.sendToMainActivity(master_activity.ADD_TO_MY_LIST_FINAL, audioFileClass);
                dismiss();
            }
        });

        return  view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view

        // Fetch arguments from bundle and set title
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


            }
        }, 100);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void setMaster(internalMessage main){
        this.main = main;
    }

    public void setMyLists(ArrayList<String> myLists){
        this.myLists = myLists;
    }

    public void setAudioFileClass(AudioFileClass audioFileClass){
        this.audioFileClass = audioFileClass;
    }

}