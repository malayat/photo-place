package ec.solmedia.photoplace.placesmap.ui;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.solmedia.photoplace.PhotoPlaceApp;
import ec.solmedia.photoplace.R;
import ec.solmedia.photoplace.entities.MyPlace;
import ec.solmedia.photoplace.libs.Util;
import ec.solmedia.photoplace.libs.base.ImageLoader;
import ec.solmedia.photoplace.placesmap.contract.PhotoPlaceMapPresenter;


/**
 * A simple {@link Fragment} subclass.
 */
public class PhotoPlacesMapFragment extends Fragment implements PhotoPlacesMapView,
        OnMapReadyCallback, GoogleMap.InfoWindowAdapter {

    @BindView(R.id.container)
    FrameLayout container;

    @Inject
    Util utils;
    @Inject
    ImageLoader imageLoader;
    @Inject
    PhotoPlaceMapPresenter presenter;

    private GoogleMap map;
    private Map<Marker, MyPlace> markers;
    private List<MyPlace> myPlaces;
    private boolean isMapReady;
    private static final int PERMISSIONS_REQUEST_LOCATION = 1;

    private boolean tracking;
    private int mapStyle;

    public static final String TAG = "PhotoPlacesMapFragment";

    public PhotoPlacesMapFragment() {
        this.myPlaces = new ArrayList<>();
        this.isMapReady = false;
        this.tracking = false;
        this.mapStyle = GoogleMap.MAP_TYPE_NORMAL;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupInjection();
        markers = new HashMap<Marker, MyPlace>();
        presenter.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_map, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        isMapReady = false;
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        this.tracking = sp.getBoolean(PhotoPlaceApp.PREF_TRACKING, false);
        this.mapStyle = Integer.parseInt(sp.getString(PhotoPlaceApp.PREF_MAP_STYLE, String.valueOf(GoogleMap.MAP_TYPE_NORMAL)));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setupInjection() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        this.tracking = sp.getBoolean(PhotoPlaceApp.PREF_TRACKING, false);
        this.mapStyle = Integer.parseInt(sp.getString(PhotoPlaceApp.PREF_MAP_STYLE, String.valueOf(GoogleMap.MAP_TYPE_NORMAL)));
        PhotoPlaceApp app = (PhotoPlaceApp) getActivity().getApplication();
        app.getPhotoPlaceMapComponent(this, this).inject(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    map.setMyLocationEnabled(tracking);
                } else {
                    Snackbar.make(getView(), "Error de permisos", Snackbar.LENGTH_LONG).show();
                }
            }
        }
    }

    private void addMarkersPlaces() {
        for (MyPlace myPlace : this.myPlaces) {
            LatLng location = new LatLng(myPlace.getLatitude(), myPlace.getLongitude());
            Log.d(TAG, "myPlace ID -> " + myPlace.toString());
            Marker marker = map
                    .addMarker(
                            new MarkerOptions()
                                    .position(location)
                            //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                            //.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_pointer)));cambia el icono del marcador
                    );
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            markers.put(marker, myPlace);
        }
        Log.d(TAG, "addMarkersPlaces Size= " + markers.size());
    }


    /*********************************
     * PhotoPlacesMapView
     ***********************************/
    @Override
    public void setPlacesMarkers(List<MyPlace> myPlaces) {
        Log.d(TAG, "setPlacesMarkers");
        this.myPlaces = myPlaces;
        if (isMapReady) {
            addMarkersPlaces();
        }
    }

    @Override
    public void removePlaceMarker(MyPlace myPlace) {
        //Log.d(TAG, "removePlaceMarker myPlace id: " +myPlace.toString());
        for (Map.Entry<Marker, MyPlace> entry : markers.entrySet()) {
            MyPlace currentMyPlace = entry.getValue();
            Marker currentMarker = entry.getKey();
            if (currentMyPlace.getId() == myPlace.getId()) {
                currentMarker.remove();
                markers.remove(currentMarker);
                //Log.d(TAG, "markers.remove ");
                break;
            }
        }
    }

    @Override
    public void moveCameraToMarker(MyPlace myPlace) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(myPlace.getLatitude(),
                        myPlace.getLongitude()), 15));

        for (Map.Entry<Marker, MyPlace> entry : markers.entrySet()) {
            MyPlace currentMyPlace = entry.getValue();
            Marker currentMarker = entry.getKey();
            if (currentMyPlace.getId() == myPlace.getId()) {
                currentMarker.showInfoWindow();
                break;
            }
        }
    }

    /******************************
     * OnMapReadyCallback
     *****************************/
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        /*if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            map.setMyLocationEnabled(tracking);
        }*/
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(tracking);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Mostrar diálogo explicativo
            } else {
                // Solicitar permiso
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                            },
                            PERMISSIONS_REQUEST_LOCATION);
                }
            }
        }

        //map.getUiSettings().setZoomControlsEnabled(true);
        map.setInfoWindowAdapter(this);
        map.setMapType(mapStyle);
        isMapReady = true;
        addMarkersPlaces();
    }

    /*******************************
     * GoogleMap.InfoWindowAdapter
     **************************/
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View window = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.fragment_marker, null);
        MyPlace myPlace = markers.get(marker);
        //render(window, myPlace);
        //CircleImageView imgAvatar = (CircleImageView)window.findViewById(R.id.imgAvatar);
        final TextView txtUser = (TextView) window.findViewById(R.id.txtPlaceName);
        final ImageView imgMain = (ImageView) window.findViewById(R.id.imgMain);
        String strNamePlace = myPlace.getName().substring(0, myPlace.getName().length() - 2).replace(",", "\n");
        txtUser.setText(strNamePlace);
        imageLoader.load(imgMain, myPlace.getPhoto());
        //imageLoader.load(imgAvatar, utils.getAvatarUrl(photo.getEmail()));
        return window;
    }


}
