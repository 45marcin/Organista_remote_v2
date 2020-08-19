package rosiekm.organista.remote;
//to set admin
//adb shell dpm set-device-owner rosiekm.organista.remote/.AdminReceiver
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;

import rosiekm.organista.remote.ExpandableListDevices.Room;
import rosiekm.organista.remote.ExpandableListDevices.RoomAdapter;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import rosiekm.organista.remote.ExpandableListUSB.USBDevice;
import rosiekm.organista.remote.FilesPackage.File;
import rosiekm.organista.remote.FilesPackage.ImageFile;
import rosiekm.organista.remote.FilesPackage.MyAudioFile;
import rosiekm.organista.remote.FilesPackage.VideoFile;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

public class master_activity extends AppCompatActivity implements internalMessage {

    private static int volume = -1;
    private TextView batteryTxt;
    private TextView timeText;
    BackgroundThread mBackgroundThread;
    BackgroundThread2 mBackgroundThread2;
    BackgroundThread3 mBackgroundThread3;
    BatteryManager batteryManager;
    ImageView WifiState;
    ImageView BatteryStatus;

    ConstraintLayout currentMediaBar;
    ImageButton button1;
    ImageButton stopBtn;
    ImageButton stopTimeBtn;
    ImageButton tune;
    TextView currentLabel;

    ArrayList<String> ExternalDir;

    AudioFilesMainListFragment audioFilesMainListFragment;
    AudioFilesMainListFragment audioFilesMyListFragment;

    ArrayList<File> usbData;

    String ip;

    int nowFile;

    MyBroadastReceivers myBroadastReceivers;
    public static final int AVAILABLE = 1;
    public static final int NOT_AVAILABLE = 2;
    public static final int AUDIO_FILES = 3;
    public static final int PLAY_AUDIO = 4;
    public static final int PLAYING_AUDIO = 5;
    public static final int IDLE = 6;
    public static final int SET_BALANSE = 7;
    public static final int GOT_BALANSE = 8;
    public static final int GOT_VOLUME = 9;
    public static final int SET_VOLUME = 10;
    public static final int TIME_STOP = 11;
    public static final int TIME_STOP_CANCEL = 12;
    public static final int CANT_TIME_STOP = 13;
    public static final int ENABLED_TIME_STOP = 14;
    public static final int ALREADY_TIME_STOP_CANCEL = 15;
    public static final int ADD_TO_MY_LIST = 16;
    public static final int ADD_TO_MY_LIST_FINAL = 17;
    public static final int REMOVE_FROM_MY_LIST = 18;
    public static final int MINUTE_CHANGED = 19;
    public static final int WIFI_CONNECTED = 21;
    public static final int WIFI_DISCONNECTED = 22;
    public static final int SHOW_INFO = 23;
    public static final int USB_PRESENT = 24;
    public static final int USB_NULL = 25;
    public static final int SHOW_USB = 26;
    public static final int SHOW_USB_LIST = 27;
    public static final int PLAY_VIDEO = 28;
    public static final int SHOW_IMAGE = 29;
    public static final int PLAYING_VIDEO = 30;
    public static final int PLAYING_IMAGE = 31;
    SwipeRefreshLayout swipeRefreshLayout;

    RecyclerView mainlistView;
    RoomAdapter adapter;

    ImageView usb, play, text, clock, image;

    DBInterface dbInterface;
    ArrayList<AudioFileClass> audioFileClassArrayList;
    ArrayList<AudioFileClass> MyAudioFileClassArrayList;
    ArrayList<String> myList;
    ArrayList<Room> rooms;
    ArrayList<Room> CustomRooms;

    master_activity mainActivity;
    HashMap<String, ArrayList<File>> usbDataCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_activity);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        myBroadastReceivers = new MyBroadastReceivers();
        dbInterface = new DBInterface(this);
        audioFileClassArrayList = dbInterface.getAllAudioFiles();
        rooms = getRoomsData(audioFileClassArrayList);

        nowFile = 0;
        btnTune = findViewById(R.id.btn_tune);

        usbDataCollection = new HashMap<>();

        MyAudioFileClassArrayList = dbInterface.getAllAudioFilesMyLists();
        CustomRooms = getRoomsData(MyAudioFileClassArrayList);
        myList = getRoomsList(MyAudioFileClassArrayList);
        ExternalDir = new ArrayList<>();
        audioFilesMainListFragment = new AudioFilesMainListFragment();
        audioFilesMainListFragment.updateRooms(rooms);
        audioFilesMainListFragment.setMaster(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.include, audioFilesMainListFragment, "HOME").addToBackStack("HOME").commit();
        mainActivity = this;


        audioFilesMyListFragment = new AudioFilesMainListFragment();
        audioFilesMyListFragment.setMaster(this);
        audioFilesMyListFragment.updateRooms(CustomRooms);


        audioFileClassArrayList = new ArrayList<>();


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        if (!audioFilesMainListFragment.isVisible()) {
                            getSupportFragmentManager().beginTransaction().replace(R.id.include, audioFilesMainListFragment, "HOME").addToBackStack("HOME").commit();
                        }
                        break;
                    case R.id.nav_gallery:
                        if (!audioFilesMyListFragment.isVisible()) {
                            audioFilesMyListFragment.updateRooms(CustomRooms);
                            getSupportFragmentManager().beginTransaction().replace(R.id.include, audioFilesMyListFragment, "HOME").addToBackStack("HOME").commit();
                        }
                        break;
                        /*
                    case R.id.nav_exit:
                        enableKioskMode(false);
                        finish();
                        break;
                        */
                    case R.id.nav_usb_audio:
                        mainActivity.sendToMainActivity(SHOW_USB_LIST, null);
                        //mUiHandler.dispatchMessage(mUiHandler.obtainMessage(SHOW_USB_LIST));
                        break;


                }
                drawer.closeDrawer(Gravity.LEFT);
                return false;
            }
        });


        currentLabel = findViewById(R.id.label_current_media);
        stopBtn = findViewById(R.id.btn_stop);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stop();
            }
        });
        stopTimeBtn = findViewById(R.id.btn_stop_when_you_can);
        stopTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeStop();
            }
        });


        currentMediaBar = findViewById(R.id.current_media_bar);
        adapter = new RoomAdapter(rooms, this);

        tune = findViewById(R.id.btn_tune);
        tune.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                PopupSettingsMenu editNameDialogFragment = PopupSettingsMenu.newInstance("Some Title");
                editNameDialogFragment.setLevel(balanse, volume);
                editNameDialogFragment.setMaster(mainActivity);
                editNameDialogFragment.show(fm, "fragment_edit_name");
            }
        });

        currentMediaBar.setVisibility(View.GONE);

        WifiState = findViewById(R.id.imageViewWifiState);
        batteryTxt = (TextView) this.findViewById(R.id.batteryTxt);
        BatteryStatus = findViewById(R.id.imageView_battery);
        timeText = this.findViewById(R.id.time_txt);
        this.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        batteryManager = (BatteryManager) getApplication().getSystemService(Context.BATTERY_SERVICE);


        usb = findViewById(R.id.imageView_usb);
        play = findViewById(R.id.imageView_play);
        text = findViewById(R.id.imageView_text);
        clock = findViewById(R.id.imageView_timer);
        image = findViewById(R.id.imageView_image);

        usb.setVisibility(View.GONE);
        play.setVisibility(View.GONE);
        text.setVisibility(View.GONE);
        clock.setVisibility(View.GONE);
        image.setVisibility(View.GONE);


        mBackgroundThread = new BackgroundThread();
        mBackgroundThread.start();
        mBackgroundThread2 = new BackgroundThread2();
        mBackgroundThread2.start();
        mBackgroundThread3 = new BackgroundThread3();
        mBackgroundThread3.start();

        checkAndRequestPermissions();


        ComponentName deviceAdmin = new ComponentName(this, AdminReceiver.class);
        mDpm = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        if (!mDpm.isAdminActive(deviceAdmin)) {
            Toast.makeText(this, "device owner", Toast.LENGTH_SHORT).show();
        }

        if (mDpm.isDeviceOwnerApp(getPackageName())) {
            mDpm.setLockTaskPackages(deviceAdmin, new String[]{getPackageName()});
        } else {
            Toast.makeText(this, "Not device owner", Toast.LENGTH_SHORT).show();
        }


        enableKioskMode(true);

        IntentFilter mTime = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(myBroadastReceivers, mTime);
        timeText.setText(getCurrentTime());

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void showUsb() {
        if (ExternalDir.size() > 0) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    PopupChoseUSB editNameDialogFragment = PopupChoseUSB.newInstance("Some Title");
                    editNameDialogFragment.setMaster(mainActivity);
                    editNameDialogFragment.usb = ExternalDir;

                    editNameDialogFragment.show(mainActivity.getSupportFragmentManager(), "fragment_edit_name");
                }
            });

        }
    }


    DevicePolicyManager mDpm;

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            Log.d("Battery", "update");
            Boolean charging = batteryManager.isCharging();
            if (!charging) {
                if (level > 99) {
                    BatteryStatus.setImageDrawable(getDrawable(R.drawable.ic_battery_full_black_24dp));
                } else if (level > 90) {
                    BatteryStatus.setImageDrawable(getDrawable(R.drawable.ic_battery_90_black_24dp));
                } else if (level > 80) {
                    BatteryStatus.setImageDrawable(getDrawable(R.drawable.ic_battery_80_black_24dp));
                } else if (level > 60) {
                    BatteryStatus.setImageDrawable(getDrawable(R.drawable.ic_battery_60_black_24dp));
                } else if (level > 50) {
                    BatteryStatus.setImageDrawable(getDrawable(R.drawable.ic_battery_50_black_24dp));
                } else if (level > 30) {
                    BatteryStatus.setImageDrawable(getDrawable(R.drawable.ic_battery_30_black_24dp));
                } else if (level > 20) {
                    BatteryStatus.setImageDrawable(getDrawable(R.drawable.ic_battery_20_black_24dp));
                } else {
                    BatteryStatus.setImageDrawable(getDrawable(R.drawable.ic_battery_alert_black_24dp));
                }
            } else {
                if (level > 99) {
                    BatteryStatus.setImageDrawable(getDrawable(R.drawable.ic_battery_charging_full_black_24dp));
                } else if (level > 90) {
                    BatteryStatus.setImageDrawable(getDrawable(R.drawable.ic_battery_charging_90_black_24dp));
                } else if (level > 80) {
                    BatteryStatus.setImageDrawable(getDrawable(R.drawable.ic_battery_charging_80_black_24dp));
                } else if (level > 60) {
                    BatteryStatus.setImageDrawable(getDrawable(R.drawable.ic_battery_charging_60_black_24dp));
                } else if (level > 50) {
                    BatteryStatus.setImageDrawable(getDrawable(R.drawable.ic_battery_charging_50_black_24dp));
                } else if (level > 30) {
                    BatteryStatus.setImageDrawable(getDrawable(R.drawable.ic_battery_charging_30_black_24dp));
                } else if (level > 20) {
                    BatteryStatus.setImageDrawable(getDrawable(R.drawable.ic_battery_charging_20_black_24dp));
                } else {
                    BatteryStatus.setImageDrawable(getDrawable(R.drawable.ic_battery_charging_20_black_24dp));
                }
            }
            batteryTxt.setText(String.valueOf(level) + "%");
        }
    };


    @SuppressLint("HandlerLeak")
    private final Handler mUiHandler = new Handler() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AVAILABLE: {
                    WifiState.setImageDrawable(getDrawable(R.drawable.ic_wifi_green));
                    ip = (String) msg.obj;
                    break;
                }
                case NOT_AVAILABLE: {
                    WifiState.setImageDrawable(getDrawable(R.drawable.ic_wifi_red));
                    break;
                }

                case AUDIO_FILES: {
                    //if (audioFileClassArrayList.size() != ((ArrayList<AudioFileClass>)msg.obj).size()){
                    if (true) {
                        dbInterface.dropMainDB();
                        ArrayList<String> genres_list = new ArrayList<>();
                        for (AudioFileClass x : (ArrayList<AudioFileClass>) msg.obj) {
                            dbInterface.insertDevice(x);
                            if (!containString(genres_list, x.getAlbum())) {
                                genres_list.add(x.getAlbum());
                            }
                        }

                        //genres_list.sort(new Comparator<String>() {
                        //    @Override
                        //    public int compare(String o1, String o2) {
                        //        return o1.compareTo(o2);
                        //     }
                        /// });
                        Collections.sort(genres_list, new Comparator<String>() {
                            @Override
                            public int compare(String o1, String o2) {
                                return o1.compareTo(o2);
                            }
                        });
                        rooms.clear();
                        ArrayList<AudioFileClass> audioFileTmp = (ArrayList<AudioFileClass>) msg.obj;
                        Collections.sort(audioFileTmp, new Comparator<AudioFileClass>() {
                            @Override
                            public int compare(AudioFileClass o1, AudioFileClass o2) {
                                if (o1.getNumber().equals(o2.getNumber())) {
                                    return o1.getTitle().compareTo(o2.getTitle());
                                } else {
                                    return o1.getNumber().compareTo(o2.getNumber());
                                }
                            }
                        });
                        //audioFileTmp.sort(new Comparator<AudioFileClass>() {
                        //     @Override
                        //     public int compare(AudioFileClass o1, AudioFileClass o2) {
                        //         return o1.getTitle().compareTo(o2.getTitle());
                        //    }
                        // });
                        for (String y : genres_list) {
                            ArrayList<AudioFileClass> tmp2 = new ArrayList<>();
                            for (AudioFileClass x : audioFileTmp) {
                                if (x.getAlbum().equals(y)) {
                                    tmp2.add(x);
                                }
                            }
                            if (tmp2.size() > 0) {
                                rooms.add(new Room(y, tmp2));
                            }
                        }


                        audioFilesMainListFragment.updateRooms(rooms);

                        audioFileClassArrayList.clear();
                        try {
                            audioFileClassArrayList.addAll((ArrayList<AudioFileClass>) msg.obj);
                        } catch (Exception e) {

                        }
                        Log.d("AudioFiles", String.valueOf(audioFileClassArrayList.size()));
                    }
                    break;

                }
                case PLAYING_AUDIO: {
                    try {
                        for (AudioFileClass x : audioFileClassArrayList) {
                            if (x.getPath().contains((String) msg.obj)) {
                                play.setVisibility(View.VISIBLE);
                                currentLabel.setText(x.getTitle());
                                currentMediaBar.setVisibility(View.VISIBLE);
                                SetAudioBar();
                            }
                        }

                        for (String x : ExternalDir) {
                            ArrayList<File> files = (ArrayList<File>) usbDataCollection.get(x);
                            for (File c : files) {
                                if (c.getPath().contains((String) msg.obj)) {
                                    currentLabel.setText(c.getTitle());
                                    currentMediaBar.setVisibility(View.VISIBLE);
                                    SetAudioBar();
                                }
                            }

                        }
                    } catch (Exception e) {

                    }
                    break;

                }
                case PLAYING_VIDEO: {
                    play.setVisibility(View.VISIBLE);
                    for (String x : ExternalDir) {
                        ArrayList<File> files = (ArrayList<File>) usbDataCollection.get(x);
                        try {
                            for (File c : files) {
                                if (c.getPath().contains((String) msg.obj)) {
                                    currentLabel.setText(c.getTitle());
                                    System.out.println("here1");
                                    SetVideoBar();
                                }
                            }
                        } catch (Exception e) {

                        }

                    }
                    break;

                }
                case PLAYING_IMAGE: {
                    try {
                        for (String x : ExternalDir) {
                            ArrayList<File> files = (ArrayList<File>) usbDataCollection.get(x);
                            for (File c : files) {
                                if (c.getPath().contains((String) msg.obj)) {
                                    currentLabel.setText(c.getTitle());
                                    SetImageBar();
                                }
                            }

                        }
                    } catch (Exception e) {

                    }
                    break;

                }
                case IDLE: {
                    play.setVisibility(View.GONE);
                    currentMediaBar.setVisibility(View.GONE);
                    clock.setVisibility(View.GONE);
                    stopTime = false;
                    break;
                }
                case TIME_STOP: {
                    Toast.makeText(mainActivity, "ustawiono", Toast.LENGTH_SHORT).show();
                    clock.setVisibility(View.VISIBLE);
                    stopTime = true;
                    break;
                }
                case ENABLED_TIME_STOP: {
                    clock.setVisibility(View.VISIBLE);
                    stopTime = true;
                    break;
                }
                case CANT_TIME_STOP: {
                    Toast.makeText(mainActivity, "niemożliwe", Toast.LENGTH_SHORT).show();
                    break;
                }
                case TIME_STOP_CANCEL: {
                    Toast.makeText(mainActivity, "odwołano", Toast.LENGTH_SHORT).show();
                    clock.setVisibility(View.GONE);
                    stopTime = false;
                    break;
                }
                case ALREADY_TIME_STOP_CANCEL: {
                    clock.setVisibility(View.GONE);
                    stopTime = false;
                    break;
                }
                case GOT_BALANSE:
                    balanse = (int) msg.obj;
                    break;
                case GOT_VOLUME:
                    volume = (int) msg.obj;
                    break;
                case MINUTE_CHANGED:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timeText.setText(getCurrentTime());
                        }
                    });

                    break;
                case USB_NULL:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            usb.setVisibility(View.GONE);
                        }
                    });

                    break;
                case USB_PRESENT:
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            usb.setVisibility(View.VISIBLE);
                        }
                    });

                    break;

            }

        }
    };

    ArrayList<Room> getRoomsData(ArrayList<AudioFileClass> audioFiles) {
        ArrayList<String> genres_list = new ArrayList<>();
        for (AudioFileClass x : audioFiles) {
            if (!containString(genres_list, x.getAlbum())) {
                genres_list.add(x.getAlbum());
            }
        }

        //genres_list.sort(new Comparator<String>() {
        //    @Override
        //    public int compare(String o1, String o2) {
        //        return o1.compareTo(o2);
        //     }
        /// });
        Collections.sort(genres_list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        ArrayList<Room> out = new ArrayList<>();
        ArrayList<AudioFileClass> audioFileTmp = audioFiles;
        Collections.sort(audioFileTmp, new Comparator<AudioFileClass>() {
            @Override
            public int compare(AudioFileClass o1, AudioFileClass o2) {
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
            ArrayList<AudioFileClass> tmp2 = new ArrayList<>();
            for (AudioFileClass x : audioFileTmp) {
                if (Arrays.equals(x.getAlbum().getBytes(), y.getBytes())) {
                    tmp2.add(x);
                }
            }
            if (tmp2.size() > 0) {
                out.add(new Room(y, tmp2));
            }
        }

        return out;
    }

    ArrayList<String> getRoomsList(ArrayList<AudioFileClass> audioFiles) {
        ArrayList<String> genres_list = new ArrayList<>();
        for (AudioFileClass x : audioFiles) {
            if (!containString(genres_list, x.getAlbum())) {
                genres_list.add(x.getAlbum());
            }
        }

        return genres_list;
    }


    public static boolean containString(ArrayList<String> array, String item) {
        for (String x : array) {
            if (Arrays.equals(x.getBytes(), item.getBytes())) {
                return true;
            }
        }
        return false;
    }

    public static int balanse = 0;
    public static boolean stopTime = false;

    @Override
    public void sendToMainActivity(int what, Object obj) {
        switch (what) {
            case PLAY_AUDIO:
                Toast.makeText(this, (String) obj, Toast.LENGTH_LONG).show();
                new Thread(new Runnable() {

                    @Override

                    public void run() {
                        //while (true) {
                        try {
                            if (HttpRequests.PlayAudio(ip, (String) obj) == 200) {
                                try {
                                    mUiHandler.sendMessage(mUiHandler.obtainMessage(PLAYING_AUDIO, obj));

                                } catch (Exception e) {

                                }
                                //break;
                            }

                        } catch (Exception e) {
                            Log.d("error", e.toString());
                            mBackgroundThread3.searchDevice();
                            //break;
                        }


                        //}
                    }

                }).start();
                break;
            case SET_BALANSE:
                new Thread(new Runnable() {

                    @Override

                    public void run() {
                        try {

                            HttpRequests.SetBalanse(ip, (int) obj);
                            balanse = (int) obj;
                        } catch (Exception e) {

                        }
                    }
                }).start();
                break;
            case SET_VOLUME:
                new Thread(new Runnable() {

                    @Override

                    public void run() {
                        try {

                            HttpRequests.SetVolume(ip, (int) obj);
                            volume = (int) obj;
                        } catch (Exception e) {

                        }
                    }
                }).start();
                break;

            case ADD_TO_MY_LIST:
                PopupChoseList editNameDialogFragment = PopupChoseList.newInstance("Some Title");
                editNameDialogFragment.setMaster(this);
                editNameDialogFragment.setMyLists(myList);
                editNameDialogFragment.setAudioFileClass((AudioFileClass) obj);
                editNameDialogFragment.show(mainActivity.getSupportFragmentManager(), "fragment_edit_name");
                break;
            case SHOW_USB_LIST:
                PopupChoseUSB editNameDialogFragment2 = PopupChoseUSB.newInstance("Some Title");
                editNameDialogFragment2.setMaster(this);
                editNameDialogFragment2.usb = ExternalDir;
                editNameDialogFragment2.show(mainActivity.getSupportFragmentManager(), "fragment_edit_name");
                break;


            case ADD_TO_MY_LIST_FINAL:
                try {
                    AudioFileClass tmp = (AudioFileClass) ((AudioFileClass) obj).clone();

                    tmp.setType((byte) 0x1);
                    MyAudioFileClassArrayList.add(tmp);
                    CustomRooms = getRoomsData(MyAudioFileClassArrayList);
                    myList = getRoomsList(MyAudioFileClassArrayList);
                    dbInterface.insertDeviceMyList(tmp);
                    Notify("Dodano: " + tmp.getTitle() + "\ndo listy " + tmp.getAlbum());
                } catch (Exception e) {

                }
                break;


            case REMOVE_FROM_MY_LIST:
                AudioFileClass tmp2 = (AudioFileClass) obj;
                MyAudioFileClassArrayList.remove((AudioFileClass) obj);
                CustomRooms = getRoomsData(MyAudioFileClassArrayList);
                myList = getRoomsList(MyAudioFileClassArrayList);
                audioFilesMyListFragment.updateRooms(CustomRooms);
                dbInterface.removeFromMyAudio((AudioFileClass) obj);
                Notify("Usunięto: " + tmp2.getTitle() + "\nz listy " + tmp2.getAlbum());
                break;

            case SHOW_INFO:
                try {
                    AudioFileClass tmp3 = (AudioFileClass) obj;
                    Notify(tmp3.getTitle());
                } catch (Exception e) {

                }

                try {
                    MyAudioFile tmp3 = (MyAudioFile) obj;
                    Notify(tmp3.getTitle());
                } catch (Exception e) {

                }
                break;

            case SHOW_USB:
                try {
                    ExpandableListFragmentUSB USBDeviceFragment = new ExpandableListFragmentUSB();
                    USBDeviceFragment.setMaster(mainActivity);
                    USBDeviceFragment.updateRooms(usbDataCollection.get((String) obj));
                    getSupportFragmentManager().beginTransaction().replace(R.id.include, USBDeviceFragment, "HOME").addToBackStack("HOME").commit();
                } catch (Exception e) {

                }
                break;
            case PLAY_VIDEO:
                Toast.makeText(this, (String) obj, Toast.LENGTH_LONG).show();
                new Thread(new Runnable() {

                    @Override

                    public void run() {
                        //while (true) {
                        try {
                            if (HttpRequests.PlayVideo(ip, (String) obj) == 200) {
                                try {
                                    mUiHandler.sendMessage(mUiHandler.obtainMessage(PLAYING_AUDIO, obj));

                                } catch (Exception e) {

                                }
                                //break;
                            }

                        } catch (Exception e) {
                            Log.d("error", e.toString());
                            mBackgroundThread3.searchDevice();
                            //break;
                        }


                        //}
                    }

                }).start();
                break;
            case SHOW_IMAGE:
                Toast.makeText(this, (String) obj, Toast.LENGTH_LONG).show();
                new Thread(new Runnable() {

                    @Override

                    public void run() {
                        //while (true) {
                        try {
                            if (HttpRequests.ShowImage(ip, (String) obj) == 200) {
                                try {
                                    mUiHandler.sendMessage(mUiHandler.obtainMessage(PLAYING_IMAGE, obj));

                                } catch (Exception e) {

                                }
                                //break;
                            }

                        } catch (Exception e) {
                            Log.d("error", e.toString());
                            mBackgroundThread3.searchDevice();
                            //break;
                        }


                        //}
                    }

                }).start();
                break;


        }
    }

    public class BackgroundThread extends Thread {
        private Handler mBackgroundHandler;


        public void run() {
            Looper.prepare();
            while (true) {
                try {
                    mBackgroundHandler = new Handler();
                    Log.d("run_working", "loop");
                    break;
                } catch (Exception e) {
                    Log.d(this.toString(), e.toString());
                }
            }
            doWork();
            Log.d("run_working", "run work");
            Looper.loop();

        }

        public void doWork() {
            mBackgroundHandler.post(new Runnable() {


                @Override
                public void run() {
                    mBackgroundHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    mUiHandler.sendMessage(mUiHandler.obtainMessage(0));
                                } catch (Exception e) {

                                } finally {
                                    SystemClock.sleep(5000);
                                }

                            }
                        }
                    });
                }
            });
        }


        public void exit() {
            mBackgroundHandler.getLooper().quit();
            Log.d("this", "finishing Thread");
        }


    }

    public void Notify(String msg) {
        Snackbar.make(findViewById(R.id.drawer_layout), msg, Snackbar.LENGTH_LONG).show();
    }

    public class BackgroundThread2 extends Thread {
        private Handler mBackgroundHandler;
        WifiManager wifiManager;
        String networkSSID = "OrganistaAP";
        String networkPass = "Organista123";
        //String networkSSID = "WiFi";
        //String networkPass = "netiAasmax";
        WifiConfiguration conf;

        public void ConnectNetwork() {
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            Log.d("wifi", wifiManager.getConnectionInfo().toString());
            if (wifiManager.getConnectionInfo().getSSID().contains(networkSSID) & wifiManager.getConnectionInfo().getSupplicantState() == SupplicantState.COMPLETED) {
                mUiHandler.sendMessage(mUiHandler.obtainMessage(WIFI_CONNECTED));
                return;
            } else {
                mUiHandler.sendMessage(mUiHandler.obtainMessage(WIFI_DISCONNECTED));
            }


            List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();
                    Log.d("Network", "reconnecting");
                    return;
                }
            }

            wifiManager.addNetwork(conf);


            list = wifiManager.getConfiguredNetworks();
            for (WifiConfiguration i : list) {
                if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                    wifiManager.disconnect();
                    wifiManager.enableNetwork(i.networkId, true);
                    wifiManager.reconnect();
                    return;
                }
            }
        }

        public void init(){

            wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            if (!wifiManager.isWifiEnabled()) {
                wifiManager.setWifiEnabled(true);
            }
            conf = new WifiConfiguration();
            conf.SSID = "\"" + networkSSID + "\"";
            conf.preSharedKey = "\"" + networkPass + "\"";
        }

        public void run(){
            Looper.prepare();
            while (true){
                try{
                    mBackgroundHandler = new Handler();
                    init();
                    Log.d("run_working", "loop");
                    break;
                }
                catch (Exception e){
                    Log.d(this.toString(), e.toString());
                }
            }
            doWork();
            Log.d("run_working", "run work");
            Looper.loop();

        }

        public void doWork(){
            mBackgroundHandler.post(new Runnable(){



                @Override
                public void run() {
                    mBackgroundHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            while (true){
                                try {
                                    ConnectNetwork();
                                }
                                catch (Exception e){

                                }
                                finally {
                                    SystemClock.sleep(10000);
                                }

                            }
                        }
                    });
                }
            });
        }


        public void exit() {
            mBackgroundHandler.getLooper().quit();
            Log.d("this", "finishing Thread");
        }


    }

    private  boolean checkAndRequestPermissions() {
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int locationPermission2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (locationPermission2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),1);
            return false;
        }
        return true;
    }



    public class BackgroundThread3 extends Thread {
        private Handler mBackgroundHandler;

        public void run() {
            Looper.prepare();
            while (true) {
                try {
                    mBackgroundHandler = new Handler();
                    Log.d("run_working", "loop");
                    break;
                } catch (Exception e) {
                    Log.d(this.toString(), e.toString());
                }
            }
            searchDevice();
            Looper.loop();

        }

        String discoverNetwork() throws IOException {
            String messageStr = "Where are you my play box?";
            Log.d("message br", messageStr);
            String messageRec = "I'm here my love";
            int port = 2708;
            DatagramSocket s = new DatagramSocket();
            s.setSoTimeout(20000);


            // Broadcast the message over all the network interfaces
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue; // Don't want to broadcast to the loopback interface
                }

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null) {
                        continue;
                    } else {
                        Log.d("Broadcast", broadcast.toString());
                    }

                    // Send the broadcast package!
                    try {
                        DatagramPacket sendPacket = new DatagramPacket(messageStr.getBytes(), messageStr.length(), broadcast, port);
                        s.send(sendPacket);
                    } catch (Exception e) {
                        Log.d("error", e.toString());
                    }

                    System.out.println(getClass().getName() + ">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
                }
            }

            System.out.println(getClass().getName() + ">>> Now waiting for a reply!");
            //while (true) {
            String text;
            byte[] message = new byte[120];
            DatagramPacket p = new DatagramPacket(message, message.length);
            s.receive(p);
            text = new String(message, 0, p.getLength());
            Log.d("Received", "message:" + text);
            if (text.toLowerCase().contains(messageRec.toLowerCase())) {
                s.close();
                return p.getAddress().toString();

            }
            // }
            Log.d("return", "no answer for broadcast");
            return null;
        }

        public void searchDevice() {
            mBackgroundHandler.post(new Runnable() {
                @Override
                public void run() {
                    mBackgroundHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            while (true) {
                                try {
                                    mUiHandler.sendMessage(mUiHandler.obtainMessage(NOT_AVAILABLE));
                                    String ip = "";
                                    while (true) {
                                        ip = discoverNetwork();
                                        if (ip.length() > 5){
                                            break;
                                        }
                                        SystemClock.sleep(1000);
                                    }
                                    mUiHandler.sendMessage(mUiHandler.obtainMessage(AVAILABLE, ip));
                                    Log.d("SearchDevice", ip);

                                    HashMap<String, Object> response = HttpRequests.GetAudioFiles(ip);
                                    if (response.containsKey("audioFiles")){
                                        Log.d("response", "received");
                                        mUiHandler.sendMessage(mUiHandler.obtainMessage(AUDIO_FILES, response.get("audioFiles")));
                                    }
                                    while(true) {
                                        try {
                                            HashMap<String, Object> responseHash = HttpRequests.GetStatus(ip);
                                            //if (responseHash.containsKey("audioPlaying")) {
                                            if ((Boolean) responseHash.get("audioPlaying")) {
                                                mUiHandler.sendMessage(mUiHandler.obtainMessage(PLAYING_AUDIO, responseHash.get("nowPlaying")));
                                                Log.d("audio", "status play");
                                            }
                                            else if ((Boolean) responseHash.get("videoPlaying")) {
                                                    mUiHandler.sendMessage(mUiHandler.obtainMessage(PLAYING_VIDEO, responseHash.get("nowPlaying")));
                                                    Log.d("video", "status play");

                                            }
                                            else if ((Boolean) responseHash.get("imagePlaying")) {
                                                    mUiHandler.sendMessage(mUiHandler.obtainMessage(PLAYING_IMAGE, responseHash.get("nowPlaying") ));

                                            }
                                                else{
                                                    mUiHandler.sendMessage(mUiHandler.obtainMessage(IDLE));
                                                }
                                                if ((Boolean) responseHash.get("stopTime")) {
                                                    mUiHandler.sendMessage(mUiHandler.obtainMessage(ENABLED_TIME_STOP));
                                                }
                                                else{
                                                    mUiHandler.sendMessage(mUiHandler.obtainMessage(ALREADY_TIME_STOP_CANCEL));
                                                }
                                                if (!(Boolean) responseHash.get("refreshingFiles")) {
                                                    if (((ArrayList<String>)responseHash.get("usb")).size() > 0) {
                                                        mUiHandler.sendMessage(mUiHandler.obtainMessage(USB_PRESENT));
                                                    }
                                                    else{
                                                        mUiHandler.dispatchMessage(mUiHandler.obtainMessage(USB_NULL));
                                                    }
                                                    ArrayList<String> usb = (ArrayList<String>)responseHash.get("usb");
                                                    for (String x: ExternalDir){
                                                        if (!isPresent(x, usb)){
                                                            Notify("Absent USB drive " + x);
                                                            ExternalDir.remove(x);
                                                            usbDataCollection.remove(x);
                                                        }
                                                    }
                                                    for (String x: usb){
                                                        if (!isPresent(x, ExternalDir)){
                                                            Notify("New USB drive " + x);
                                                            ExternalDir.add(x);
                                                            HashMap<String, Object> tmpData = HttpRequests.GetUSBData(ip,x);
                                                            if ((boolean)tmpData.get("data")){
                                                                usbDataCollection.put(x, (ArrayList<File>)tmpData.get("USBFiles"));
                                                            }
                                                        }
                                                    }
                                                }
                                            //}
                                            mUiHandler.sendMessage(mUiHandler.obtainMessage(GOT_BALANSE, responseHash.get("balanse") ));
                                            mUiHandler.sendMessage(mUiHandler.obtainMessage(GOT_VOLUME, responseHash.get("AudioVolume") ));
                                        }
                                        catch (Exception e){
                                            break;
                                        }
                                        SystemClock.sleep(5000);
                                    }


                                } catch (Exception e) {
                                    Log.d("error", e.toString());
                                }
                                finally {
                                    SystemClock.sleep(5000);
                                }

                            }
                        }
                    });
                }
            });
        }

        public boolean isPresent(String value, ArrayList<String> arrayList){
            for (String x: arrayList){
                if (Arrays.equals(value.toLowerCase().getBytes(), x.toLowerCase().getBytes())){
                    return  true;
                }
            }
            return false;
        }


        public void exit() {
            mBackgroundHandler.getLooper().quit();
            Log.d("this", "finishing Thread");
        }
    }


    void Stop(){
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    if (HttpRequests.StopAudio(ip) == 200) {
                        mUiHandler.sendMessage(mUiHandler.obtainMessage(IDLE));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }

    void StopVideo(){
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    if (HttpRequests.StopVideo(ip) == 200) {
                        mUiHandler.sendMessage(mUiHandler.obtainMessage(IDLE));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();

    }


    void PauseResumeVideo(){
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    HttpRequests.PauseResumVideo(ip);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }


    void TimeStop(){
        if (!stopTime) {
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (HttpRequests.StopTimeAudio(ip) == 200) {
                            mUiHandler.sendMessage(mUiHandler.obtainMessage(TIME_STOP));
                        }
                        else{
                            mUiHandler.sendMessage(mUiHandler.obtainMessage(CANT_TIME_STOP));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        }
        else{
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (HttpRequests.StopTimeAudioCancel(ip) == 200) {
                            mUiHandler.sendMessage(mUiHandler.obtainMessage(TIME_STOP_CANCEL));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            thread.start();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (volume != -1) {
            if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
                //showMessage("volume down");
                volume = volume -1;
                if (volume < 0){
                    volume = 0;
                }
                Toast.makeText(this, "Głośność " + String.valueOf(volume) + "%", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {

                    @Override

                    public void run() {
                        try {
                            HttpRequests.SetVolume(ip, volume);
                        }
                        catch (Exception e){

                        }
                    }

                }).start();
            }
            else if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
                //showMessage("volume up");

                volume = volume +1;
                if (volume > 100){
                    volume = 100;
                }
                Toast.makeText(this, "Głośność " + String.valueOf(volume) + "%", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {

                    @Override

                    public void run() {
                        try {
                            HttpRequests.SetVolume(ip, volume);

                        }
                        catch (Exception e){

                        }
                    }

                }).start();
            }
        }
        return true;
    }

    private void enableKioskMode(boolean enabled) {
        try {
            if (enabled) {
                if (mDpm.isLockTaskPermitted(this.getPackageName())) {
                    startLockTask();
                    mIsKioskEnabled = true;
                } else {

                    Toast.makeText(this, "kiosk is not permitted", Toast.LENGTH_SHORT).show();
                }
            } else {
                stopLockTask();
                mIsKioskEnabled = false;
            }
        } catch (Exception e) {
            // TODO: Log and handle appropriately
        }
    }

    boolean mIsKioskEnabled = false;


    @Override
    protected void onResume() {
        super.onResume();
    }

    public class MyBroadastReceivers extends BroadcastReceiver{

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            mUiHandler.sendMessage(mUiHandler.obtainMessage(MINUTE_CHANGED, null));
        }
    }

    public static String getCurrentTime() {
        //date output format
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }


    ArrayList<USBDevice> getUSBData(ArrayList<File> audioFiles){
        if(audioFiles != null) {
            ArrayList<String> genres_list = new ArrayList<>();
            for (File x : audioFiles) {
                if (!containString(genres_list, x.getAlbum())) {
                    genres_list.add(x.getAlbum());
                }
            }

            //genres_list.sort(new Comparator<String>() {
            //    @Override
            //    public int compare(String o1, String o2) {
            //        return o1.compareTo(o2);
            //     }
            /// });
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
                    if (Arrays.equals(x.getAlbum().getBytes(), y.getBytes())) {
                        tmp2.add(x);
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

    ArrayList<String> getUSBList(ArrayList<File> audioFiles){
        ArrayList<String> genres_list = new ArrayList<>();
        for (File x: audioFiles)
        {
            if (!containString(genres_list, x.getAlbum())){
                genres_list.add(x.getAlbum());
            }
        }

        return genres_list;
    }
    ImageButton btnTune;

    void SetAudioBar(){
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stop();
            }
        });
        stopTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeStop();
            }
        });
        stopBtn.setImageResource(R.drawable.ic_stop_black);
        stopTimeBtn.setImageResource(R.drawable.ic_timer_blac);
        stopBtn.setVisibility(View.VISIBLE);
        btnTune.setVisibility(View.VISIBLE);
        stopTimeBtn.setVisibility(View.VISIBLE);
        currentMediaBar.setVisibility(View.VISIBLE);
    }

    void SetVideoBar(){
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StopVideo();
            }
        });
        stopTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PauseResumeVideo();
            }
        });
        stopBtn.setImageResource(R.drawable.ic_stop_black);
        stopTimeBtn.setImageResource(R.drawable.ic_skip_next_black_24dp);
        btnTune.setVisibility(View.VISIBLE);
        stopBtn.setVisibility(View.VISIBLE);
        stopTimeBtn.setVisibility(View.VISIBLE);
        currentMediaBar.setVisibility(View.VISIBLE);
    }
    void SetImageBar(){
        stopBtn.setVisibility(View.VISIBLE);
        stopTimeBtn.setVisibility(View.GONE);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideImage();
            }
        });
        btnTune.setVisibility(View.GONE);
        stopBtn.setImageResource(R.drawable.ic_remove_from_queue_black_24dp);
        currentMediaBar.setVisibility(View.VISIBLE);
    }

    public void hideImage(){
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try  {
                    if (HttpRequests.StopAudio(ip) == 200) {
                        mUiHandler.sendMessage(mUiHandler.obtainMessage(IDLE));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

}
