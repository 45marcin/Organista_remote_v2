package rosiekm.organista.remote;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import rosiekm.organista.remote.R;

import static rosiekm.organista.remote.master_activity.SET_BALANSE;
import static rosiekm.organista.remote.master_activity.SET_VOLUME;

public class PopupSettingsMenu extends DialogFragment {
    SeekBar balanse, volume;
    private internalMessage main;
    private int level, audioLevel;
    TextView labelLevel, volumeLevel;

    public PopupSettingsMenu() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static PopupSettingsMenu newInstance(String title) {
        PopupSettingsMenu frag = new PopupSettingsMenu();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.popup_dev_menu, container);
        labelLevel = view.findViewById(R.id.label_level);
        volumeLevel = view.findViewById(R.id.volumeLevel);
        volume = view.findViewById(R.id.seekBar_volume);
        volume.setMax(100);
        volume.setProgress(audioLevel);
        balanse = view.findViewById(R.id.seekBar_balanse);
        balanse.setMax(200);
        balanse.setProgress(level + 100);
        labelLevel.setText(String.valueOf(level));
        volumeLevel.setText(String.valueOf(audioLevel));

        balanse.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                level = progress-100;
                labelLevel.setText(String.valueOf(level));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                main.sendToMainActivity(SET_BALANSE, seekBar.getProgress()-100);
            }
        });

        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioLevel = progress;
                volumeLevel.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                main.sendToMainActivity(SET_VOLUME, seekBar.getProgress());
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
                balanse.setMax(200);
                balanse.setProgress(level + 100);
                labelLevel.setText(String.valueOf(level));
                volumeLevel.setText(String.valueOf(audioLevel));
                volume.setProgress(audioLevel);

            }
        }, 100);

        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public void setMaster(internalMessage main){
        this.main = main;
    }

    public void setLevel(int level, int audioLevel){
        this.level = level;
        this.audioLevel = audioLevel;
       if (balanse != null) {
           balanse.setProgress(level + 100);
       }
       if (labelLevel != null){
           labelLevel.setText(level);
       }
       if (volumeLevel != null) {
           volumeLevel.setText(String.valueOf(audioLevel));
       }
       if (volume != null) {
           volume.setProgress(audioLevel);
       }
    }
}