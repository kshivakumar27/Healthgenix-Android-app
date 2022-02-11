package org.snowcorp.login;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.qap.ctimelineview.TimelineRow;
import org.qap.ctimelineview.TimelineViewAdapter;

import java.util.ArrayList;
import java.util.Date;

public class TimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);

        // Create Timeline rows List
        ArrayList<TimelineRow> timelineRowsList = new ArrayList<>();

// Create new timeline row (Row Id)
        TimelineRow myRow = new TimelineRow(0);

// To set the row Date (optional)
        myRow.setDate(new Date());
// To set the row Title (optional)
        myRow.setTitle("Title");
// To set the row Description (optional)
        myRow.setDescription("Description");
// To set the row bitmap image (optional)
        myRow.setImage(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
// To set row Below Line Color (optional)
        myRow.setBellowLineColor(Color.argb(255, 0, 0, 0));
// To set row Below Line Size in dp (optional)
        myRow.setBellowLineSize(6);
// To set row Image Size in dp (optional)
        myRow.setImageSize(40);
// To set background color of the row image (optional)
        myRow.setBackgroundColor(Color.argb(255, 0, 0, 0));
// To set the Background Size of the row image in dp (optional)
        myRow.setBackgroundSize(60);
// To set row Date text color (optional)
        myRow.setDateColor(Color.argb(255, 0, 0, 0));
// To set row Title text color (optional)
        myRow.setTitleColor(Color.argb(255, 0, 0, 0));
// To set row Description text color (optional)
        myRow.setDescriptionColor(Color.argb(255, 0, 0, 0));

// Add the new row to the list
        timelineRowsList.add(myRow);

        timelineRowsList.add(myRow);
        timelineRowsList.add(myRow);
        timelineRowsList.add(myRow);
        timelineRowsList.add(myRow);
        timelineRowsList.add(myRow);
        timelineRowsList.add(myRow);
        timelineRowsList.add(myRow);
        timelineRowsList.add(myRow);
        timelineRowsList.add(myRow);
        timelineRowsList.add(myRow);
        timelineRowsList.add(myRow);
        timelineRowsList.add(myRow);
        timelineRowsList.add(myRow);
        timelineRowsList.add(myRow);
// Create the Timeline Adapter
        ArrayAdapter<TimelineRow> myAdapter = new TimelineViewAdapter(this, 0, timelineRowsList,
                //if true, list will be sorted by date
                false);

// Get the ListView and Bind it with the Timeline Adapter
        ListView myListView = (ListView) findViewById(R.id.timeline_listView);
        myListView.setAdapter(myAdapter);
    }
}
