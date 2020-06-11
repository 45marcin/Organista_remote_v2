package rosiekm.organista.remote.ExpandableListDevices;

import rosiekm.organista.remote.AudioFileClass;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;

public class Room extends ExpandableGroup<AudioFileClass> {

    public Room(String title, ArrayList<AudioFileClass> items) {
        super(title, items);
    }
}
