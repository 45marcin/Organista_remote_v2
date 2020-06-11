package rosiekm.organista.remote;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.security.PublicKey;
import java.util.ArrayList;

import static rosiekm.organista.remote.master_activity.SHOW_USB;

public class PopupChoseUSB extends DialogFragment implements internalMessage {
    private internalMessage main;
    private ArrayList<String> myLists;
    AudioFileClass audioFileClass;
    private RecyclerView recyclerView;
    public ArrayList<String> usb;

    public PopupChoseUSB() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static PopupChoseUSB newInstance(String title) {
        PopupChoseUSB frag = new PopupChoseUSB();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    TextView title;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.popup_chose_usb, container);
        title = view.findViewById(R.id.textView5);

        recyclerView = view.findViewById(R.id.list_usb);
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(usb, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if (usb.size() == 0){
            title.setText("Brak nośników usb");
        }
        else{
            title.setText("wybierz nośnik usb");
        }
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
                if (usb.size() == 0){
                    dismiss();
                }
            }
        }, 2000);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void setMaster(internalMessage main){
        this.main = main;
    }


    @Override
    public void sendToMainActivity(int what, Object obj) {
        if (what == SHOW_USB){
            main.sendToMainActivity(what, obj);
            dismiss();
        }
    }
}