package com.taxifind.kts.main;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taxifind.kts.POJOs.Distance;

import java.text.DecimalFormat;
import java.util.List;

public class LazyAdapter extends BaseAdapter {

    private Activity activity;
    private String[] items;
    private List<Distance> distances;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapter(Activity a, List<Distance> d) {
        this.distances = d;
        this.activity = a;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader();
    }

    public int getCount() {
        return distances.size();
    }

    public Distance getItem(int position) {
        return distances.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.list_row, null);

        TextView title = (TextView)vi.findViewById(R.id.title); // title
        TextView artist = (TextView)vi.findViewById(R.id.artist); // artist name
        TextView duration = (TextView)vi.findViewById(R.id.duration); // duration
        ImageView thumb_image= (ImageView)vi.findViewById(R.id.list_image); // thumb image

        title.setText(distances.get(position).getRankname() );
        DecimalFormat f = new DecimalFormat("##.00");
        artist.setText(f.format(distances.get(position).getDistance()) + " km");
        duration.setText("");
        imageLoader.DisplayImage(thumb_image);
        return vi;
    }
}