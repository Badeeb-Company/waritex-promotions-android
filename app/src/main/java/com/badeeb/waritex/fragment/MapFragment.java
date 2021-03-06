package com.badeeb.waritex.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.badeeb.waritex.R;
import com.badeeb.waritex.model.Vendor;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.parceler.Parcels;

import java.util.List;
import java.util.zip.Inflater;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    // Logging Purpose
    public static final String TAG = MapFragment.class.getSimpleName();

    // Class Attributes
    private GoogleMap mMap;
    private MapView mMapView;
    private List<Vendor> mVendorList;

    // marker info view
    private LinearLayout markerInfoLayout;

    //current location
    private double mLatitude ;
    private double mLongitude;

    // Constants
    public final static String EXTRA_VENDOR_LIST = "EXTRA_VENDOR_LIST";
    public final static String EXTRA_CURRENT_LATITUDE = "EXTRA_CURRENT_LATITUDE";
    public final static String EXTRA_CURRENT_LONGITUDE = "EXTRA_CURRENT_LONGITUDE";
    public final static String HOME_MARKER_TITLE = "Your current location";

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        Log.d(TAG, "onCreateView - Start");

        // for hiding toolbar icons
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        // intialize the marker layout view
        markerInfoLayout = (LinearLayout) view.findViewById(R.id.marker_info_layout);

        init(view, savedInstanceState);

        Log.d(TAG, "onCreateView - End");

        return view;
    }

    private void init(View view, Bundle savedInstanceState) {

        Log.d(TAG, "init - Start");

        // Map Initialization
        MapsInitializer.initialize(getContext());
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.getMapAsync(this);

        // setting the current location value
        mLatitude = Parcels.unwrap(getArguments().getParcelable(EXTRA_CURRENT_LATITUDE));
        mLongitude = Parcels.unwrap(getArguments().getParcelable(EXTRA_CURRENT_LONGITUDE));

        // getting the vendor list values from Vendors List Activity
        mVendorList = Parcels.unwrap(getArguments().getParcelable(EXTRA_VENDOR_LIST));

        Log.d(TAG, "init - End");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(TAG, "onMapReady - Start");

        mMap = googleMap;

        // Add a marker in current location and move the camera
        LatLng currentLocation = new LatLng(mLatitude, mLongitude);

        MarkerOptions currentLocationMarker = new MarkerOptions();
        currentLocationMarker.position(currentLocation);
        currentLocationMarker.title(HOME_MARKER_TITLE);
        mMap.addMarker(currentLocationMarker);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));

        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.flag_3);
        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 100, 100, false);


        // Add markers in every vendor location
        for(int i = 0; i < mVendorList.size(); i++){
            MarkerOptions marker = new MarkerOptions();
            marker.position(new LatLng(mVendorList.get(i).getLat(), mVendorList.get(i).getLng()));
            marker.title(i+"");
            marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

            mMap.addMarker(marker);

        }

        // adding vendor info when marker clicked
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){

            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Log.d(TAG, "onMapReady - getInfoContents - Start");
                if (! marker.getTitle().equalsIgnoreCase(HOME_MARKER_TITLE)) {

                    View v = getActivity().getLayoutInflater().inflate(R.layout.marker_info,null);
                    // Get Vendor index
                    int index = Integer.parseInt(marker.getTitle());
                    Vendor vendor = mVendorList.get(index);
                    // set vendor info
                    ((TextView) v.findViewById(R.id.vendor_name)).setText(vendor.getName());
                    ((TextView) v.findViewById(R.id.vendor_address)).setText(vendor.getAddress());

                    return v;
                }

                Log.d(TAG, "onMapReady - getInfoContents - End");
                return null;
            }
        });


        // to adjust the zoom and the map options
        initCamera(mMap);

        Log.d(TAG, "onMapReady - End");
    }

    private void initCamera( GoogleMap googleMap) {

        Log.d(TAG, "initCamera - Start");

        CameraPosition position = CameraPosition.builder()
                .target(new LatLng(mLatitude,mLongitude))
                .zoom(13f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();

        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), null);

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled( true );

        Log.d(TAG, "initCamera - End");
    }

    // for hiding the toolbar toolbar icons
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_company_info).setVisible(false);
        menu.findItem(R.id.action_notifications_history).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    //----------------------------------------------------------------------------------------------
    // Following methods are called to handle mapView lifecycle

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState); mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    //----------------------------------------------------------------------------------------------

}
