package rosiekm.organista.remote.ExpandableListUSB;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;

import rosiekm.organista.remote.AudioFileClass;
import rosiekm.organista.remote.FilesPackage.File;

public class USBDevice extends ExpandableGroup<File> {

    public USBDevice(String title, ArrayList<File> items) {
        super(title, items);
    }
}
