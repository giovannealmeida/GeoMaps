package com.giovanne.geomaps;

import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ImageButton btHideList;
    private ListView lvList;
    private Map<String,String> markersList;
    private ArrayList<String> list;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        this.btHideList = (ImageButton) findViewById(R.id.btHideList);
        btHideList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lvList.getVisibility() == View.VISIBLE)
                    lvList.setVisibility(View.GONE);
                else
                    lvList.setVisibility(View.VISIBLE);
            }
        });
        this.lvList = (ListView) findViewById(R.id.lvMarkersList);

        markersList = new HashMap<String, String>();

        list = new ArrayList<String>();
        list.addAll(markersList.values());

        this.adapter = new ArrayAdapter<String>(this,R.layout.list_markers_item, R.id.text1, list);

        lvList.setAdapter(adapter);
        lvList.setEmptyView(findViewById(R.id.tvEmpty));

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                removeMarker(marker);
                return true;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//                addMarker(latLng);
                addZone(latLng);
            }
        });
    }

    private boolean removeMarker(Marker marker) {
        try{
            marker.remove();
            markersList.remove(marker.getId());
            list.clear();
            list.addAll(markersList.values());
            adapter.notifyDataSetChanged();
        } catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG);
            return false;
        }
        return true;
    }

    private void addMarker(LatLng latLng) {
        /*Custom icon*/
//        Marker m2 = mMap.addMarker(new MarkerOptions()
//                .position(latLng)
//                .title("Marker")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));

        Marker m = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Marker"));
        markersList.put(m.getId(),"Marker "+m.getId()+" - "+String.valueOf(latLng.latitude)+", "+String.valueOf(latLng.longitude));
        list.clear();
        list.addAll(markersList.values());
        adapter.notifyDataSetChanged();
    }

    private void addZone(LatLng latLng){
        Circle c = mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(10000)
                .strokeColor(Color.argb(144,255,0,0))
                .strokeWidth(5f)
                .fillColor(Color.argb(100,0,0,255)));

        Marker m = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Marker"));

        markersList.put(m.getId(),c.getId());
        list.clear();
        list.addAll(markersList.values());
        adapter.notifyDataSetChanged();
    }

    private boolean removeZone(Marker marker) {
        try{
            marker.remove();
            markersList.remove(marker.getId());
            list.clear();
            list.addAll(markersList.values());
            adapter.notifyDataSetChanged();
        } catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG);
            return false;
        }
        return true;
    }
}
