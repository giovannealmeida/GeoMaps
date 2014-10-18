package com.giovanne.geomaps;

import android.app.Dialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.giovanne.geomaps.adapters.AddressCustomAdapter;
import com.giovanne.geomaps.adapters.MarkerCustomAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ImageButton btHideList;
    private ListView lvList;
    private Map<Marker,Circle> zoneList;
    private ArrayList<Marker> markerList;
    private MarkerCustomAdapter markerAdapter;
    private Map<Marker,String> addressList;
    private AddressCustomAdapter addressAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if(status!= ConnectionResult.SUCCESS){ // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }else { // Google Play Services are available

            this.btHideList = (ImageButton) findViewById(R.id.btHideList);
            btHideList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (lvList.getVisibility() == View.VISIBLE)
                        lvList.setVisibility(View.GONE);
                    else
                        lvList.setVisibility(View.VISIBLE);
                }
            });
            this.lvList = (ListView) findViewById(R.id.lvMarkersList);

            zoneList = new HashMap<Marker, Circle>();

            markerList = new ArrayList<Marker>();

            addressList = new HashMap<Marker, String>();

            this.markerAdapter = new MarkerCustomAdapter(markerList, this);
            this.addressAdapter = new AddressCustomAdapter(new ArrayList<String>(addressList.values()), this);

//            lvList.setAdapter(markerAdapter);
            lvList.setAdapter(addressAdapter);
            lvList.setEmptyView(findViewById(R.id.tvEmpty));

            lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(markerList.get(position).getPosition())      // Sets the center of the map to Mountain View
                            .zoom(17)                   // Sets the zoom
//                        .bearing(90)                // Sets the orientation of the camera to east
//                        .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            });
            setUpMapIfNeeded();
        }
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
                removeZone(marker);
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
//        try{
//            marker.remove();
//            markersList.remove(marker.getId());
//            list.clear();
//            list.addAll(markersList.values());
//            markerAdapter.notifyDataSetChanged();
//        } catch (Exception e){
//            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG);
//            return false;
//        }
        return true;
    }

    private void addMarker(LatLng latLng) {
        /*Custom icon*/
//        Marker m2 = mMap.addMarker(new MarkerOptions()
//                .position(latLng)
//                .title("Marker")
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));

//        Marker m = mMap.addMarker(new MarkerOptions()
//                .position(latLng)
//                .title("Marker"));
//        markersList.put(m.getId(),"Marker "+m.getId()+" - "+String.valueOf(latLng.latitude)+", "+String.valueOf(latLng.longitude));
//        list.clear();
//        list.addAll(markersList.values());
//        markerAdapter.notifyDataSetChanged();
    }

    private void addZone(LatLng latLng){
        Circle c = mMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(50)
                .strokeColor(Color.argb(144,255,0,0))
                .strokeWidth(5f)
                .fillColor(Color.argb(100,0,0,255)));

        Marker m = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Marker"));

        zoneList.put(m,c);
//        markerList.clear();
//        markerList.addAll(zoneList.keySet());
//        markerAdapter.notifyDataSetChanged();
        addressList.put(m,getCompleteAddressString(m.getPosition()));
        addressAdapter.setData(new ArrayList<String>(addressList.values()));
        addressAdapter.notifyDataSetChanged();
    }

    private boolean removeZone(Marker marker) {
        try{
            marker.remove(); //Remove marcador
            zoneList.get(marker).remove(); //Remove círculo
            zoneList.remove(marker);
//            markerList.clear();
//            markerList.addAll(zoneList.keySet());
//            markerAdapter.notifyDataSetChanged();
            addressList.remove(marker);
            addressAdapter.setData(new ArrayList<String>(addressList.values()));
            addressAdapter.notifyDataSetChanged();
        } catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG);
            return false;
        }
        return true;
    }

    private String getCompleteAddressString(LatLng latLng) {
        String strAdd = "";
        double LATITUDE = latLng.latitude;
        double LONGITUDE = latLng.longitude;

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("Getting loction address", "" + strReturnedAddress.toString());
            } else {
                Log.w("Getting loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Getting loction address", e.getMessage());
        }
        return strAdd;
    }
}
