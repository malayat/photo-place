package ec.solmedia.photoplace.main.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import ec.solmedia.photoplace.PhotoPlaceApp;
import ec.solmedia.photoplace.R;
import ec.solmedia.photoplace.libs.Util;
import ec.solmedia.photoplace.libs.base.ImageLoader;
import ec.solmedia.photoplace.login.ui.LoginActivity;
import ec.solmedia.photoplace.main.adapters.MainSectionsPagerAdapter;
import ec.solmedia.photoplace.main.contract.MainPresenter;
import ec.solmedia.photoplace.main.ui.settings.SettingsActivity;
import ec.solmedia.photoplace.placeslist.ui.PlacesListFragment;
import ec.solmedia.photoplace.placesmap.ui.PhotoPlacesMapFragment;

public class MainActivity extends AppCompatActivity implements MainView, LocationListener,
        GoogleApiClient.ConnectionCallbacks, OnConnectionFailedListener, ResultCallback<PlaceLikelihoodBuffer> {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.imgUser)
    CircleImageView imgUser;
    @BindView(R.id.txtEmail)
    TextView txtEmail;
    @BindView(R.id.txtName)
    TextView txtName;

    @Inject
    Util util;
    @Inject
    MainPresenter presenter;
    @Inject
    SharedPreferences sharedPreferences;
    @Inject
    MainSectionsPagerAdapter adapter;
    @Inject
    Context context;

    @Inject
    ImageLoader imageLoader;

    private Snackbar mainSnackbar;
    private GoogleApiClient mGoogleApiClient;
    private Location lastKnowLocation;
    protected LocationRequest mLocationRequest;
    private String photoPath;

    private boolean resolvingError = false;
    private static final int REQUEST_RESOLVE_ERROR = 0;
    private static final int REQUEST_PICTURE = 2;
    private static final int PERMISSION_REQUEST_LOCATION = 1;
    private static final int PERMISSION_REQUEST_EXTERNAL_STORAGE = 5;

    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private static final String TAG = "MainActivity";
    private static final String LOCATION_KEY = "lastKnowLocation";

    private Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setupInjection();
        setupNavigation();
        setupGoogleAPIClient();

        presenter.onCreate();
    }

    private void setupInjection() {
        /*String[] titles = new String[]{getString(R.string.main_title_map), getString(R.string.main_title_list)};
        Fragment[] fragments = new Fragment[]{new PhotoPlacesMapFragment(), new PlacesListFragment(), };*/
        String[] titles = new String[]{getString(R.string.main_title_list), getString(R.string.main_title_map)};
        Fragment[] fragments = new Fragment[]{new PlacesListFragment(), new PhotoPlacesMapFragment()};
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        PhotoPlaceApp app = (PhotoPlaceApp) getApplication();
        app.getMainComponent(this, this, titles, fragments, getSupportFragmentManager()).inject(this);
    }

    private void setupNavigation() {
        String name = sharedPreferences.getString(PhotoPlaceApp.NAME_KEY, getString(R.string.app_name));
        String email = sharedPreferences.getString(PhotoPlaceApp.EMAIL_KEY, "");
        String id = sharedPreferences.getString(PhotoPlaceApp.ID_KEY, "10153936397478860");

        /*Log.e(TAG, "name -------_> " + name);
        Log.e(TAG, "email -------_> " + email);
        Log.e(TAG, "id -------_> " + id);*/

        String urlAvatar = util.getFacebookUrlAvatar(id);

        Log.e(TAG, "usrl avatar -------_> " + urlAvatar);

        txtName.setText(name);
        txtEmail.setText(email);
        imageLoader.loadAvatar(imgUser, urlAvatar);

        setSupportActionBar(toolbar);

        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
    }

    private void setupGoogleAPIClient() {
        if (this.mGoogleApiClient == null) {
            this.mGoogleApiClient = new GoogleApiClient
                    .Builder(this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            createLocationRequest();
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photoplace_menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            navigateToPreferencesScreen();
        }
        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void navigateToPreferencesScreen() {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.fab)
    public void onClick() {
        if (isGpsEnableAndroid()) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.d(TAG, "onClick checkSelfPermission");
                    requestPermissions(new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, PERMISSION_REQUEST_EXTERNAL_STORAGE);
                }
            } else {
                startPhotoPlaceProcess();
            }
        } else {
            buildAlertMessageNoGps();
        }
    }

    private boolean isGpsEnableAndroid() {
        int locationMode = 1;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                Log.e(TAG, e.getLocalizedMessage());
            }
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if (TextUtils.isEmpty(locationProviders)) {
                locationMode = Settings.Secure.LOCATION_MODE_OFF;
            }
        }

        return locationMode == Settings.Secure.LOCATION_MODE_OFF ? false : true;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.main_message_gps_status)
                .setCancelable(false)
                .setPositiveButton(R.string.main_message_dialog_yes, new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(R.string.main_message_dialog_no, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        Snackbar.make(viewPager, R.string.main_error_gps_not_available, Snackbar.LENGTH_LONG).show();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void startPhotoPlaceProcess() {
        runOnUiThread(
                new Runnable() {
                    @Override
                    public void run() {
                        locationUpdating();
                        startLocationUpdates();
                    }
                }
        );
    }

    private void startLocationUpdates() {
        Log.d(TAG, "START-LocationUpdates");
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdates() {
        Log.d(TAG, "STOP-LocationUpdates");
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    private void startPickTakePhoto() {
        Intent chooserIntent = null;

        List<Intent> intentList = new ArrayList<>();

        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePhotoIntent.putExtra("return-data", true);
        File photoFile = null;
        try {
            photoFile = util.getFile(getString(R.string.app_name));
            this.photoPath = photoFile.getAbsolutePath();
        } catch (IOException e) {
            Snackbar.make(viewPager, R.string.main_error_dispatch_camera, Snackbar.LENGTH_SHORT).show();
            Log.e(TAG, "Error", e);
        }

        if (photoFile != null) {
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
                intentList = addIntentsToList(intentList, takePhotoIntent);
            }
        }

        if (pickIntent.resolveActivity(getPackageManager()) != null) {
            intentList = addIntentsToList(intentList, pickIntent);
        }

        if (intentList.size() > 0) {
            chooserIntent = Intent.createChooser(intentList.remove(intentList.size() - 1),
                    getString(R.string.main_message_picture_source));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentList.toArray(new Parcelable[]{}));
        }

        startActivityForResult(chooserIntent, REQUEST_PICTURE);
    }

    private List<Intent> addIntentsToList(List<Intent> list, Intent intent) {
        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resInfo) {
            String packageName = resolveInfo.activityInfo.packageName;
            Intent targetedIntent = new Intent(intent);
            targetedIntent.setPackage(packageName);
            list.add(targetedIntent);
        }
        return list;
    }

    private void addPicToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void logout() {
        LoginManager.getInstance().logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void locationUpdating() {
        mainSnackbar = Snackbar.make(viewPager,
                getResources().getString(R.string.main_message_location_updating),
                Snackbar.LENGTH_INDEFINITE).setAction(
                R.string.main_message_action_cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fab.setVisibility(View.VISIBLE);
                        stopLocationUpdates();
                    }
                }
        );
        mainSnackbar.show();
        fab.setVisibility(View.GONE);
    }

    private void locationUpdated() {
        Toast.makeText(this, getResources()
                        .getString(R.string.main_message_location_updated),
                Toast.LENGTH_SHORT).show();
        fab.setVisibility(View.VISIBLE);
        mainSnackbar.dismiss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient).isLocationAvailable()) {
                        this.lastKnowLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    } else {
                        Snackbar.make(viewPager, R.string.main_error_location_not_available, Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(viewPager, R.string.main_error_location_permission, Snackbar.LENGTH_LONG).show();
                }
                break;
            case PERMISSION_REQUEST_EXTERNAL_STORAGE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startPhotoPlaceProcess();
                } else {
                    Snackbar.make(viewPager, R.string.main_error_storage_permission, Snackbar.LENGTH_LONG).show();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            resolvingError = false;
            if (resultCode == RESULT_OK) {
                if (!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.connect();
                }
            }
        } else if (resultCode == RESULT_OK && requestCode == REQUEST_PICTURE) {
            boolean fromCamera = (data == null || data.getData() == null);

            if (fromCamera) {
                addPicToGallery();
            } else {//desde la galeria
                File fileToDelete = new File(photoPath);
                Log.d(TAG, "Eliminado archivo temporal " + fileToDelete.delete());
                photoPath = util.getRealPathFromURI(data.getData(), getContentResolver(), getCacheDir());
            }

            if (this.place != null && this.lastKnowLocation != null) {
                Log.d(TAG, "Se obtuvo resultados del Google Places API");
                presenter.savePlaceApiPlace(this.place, this.lastKnowLocation, this.photoPath);
            } else if (this.lastKnowLocation != null) {
                Log.d(TAG, "Se obtuvo resultados del LocationRequest");
                presenter.savePlace(lastKnowLocation, photoPath);
            }

            if (this.lastKnowLocation == null && this.place == null) {
                //TODO: entra aqui alguna vez?
                Log.e(TAG, "Entra aqui alguna vez, sino mejor borrarlo. No se pudo obtener la ubicacion");
                Snackbar.make(viewPager, R.string.main_error_save_location_not_available, Snackbar.LENGTH_SHORT).show();
            }
        } else {
            if (photoPath != null) {
                File fileToDelete = new File(photoPath);
                Log.d(TAG, "Eliminado archivo temporal " + fileToDelete.delete());
            }
        }
    }

    /*************************
     * MainView
     ******************************/
    @Override
    public void onSavePlace() {
        Snackbar.make(viewPager, R.string.main_message_save_complete, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveError() {
        Snackbar.make(viewPager, R.string.main_error_save, Snackbar.LENGTH_LONG).show();
    }

    /*******************
     * ConnectionCallbacks
     *************************/
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected");
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                }, PERMISSION_REQUEST_LOCATION);
            }
        } else {
            if (lastKnowLocation == null) {
                if (LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient).isLocationAvailable()) {
                    Log.d(TAG, "UUUUUUUUUUUUUU -> Actualizando la ubicación");
                    this.lastKnowLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }


    /******************
     * OnConnectionFailedListener
     *****************************/
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        if (resolvingError) {
            return;
        } else if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, "EEEEERRRR00000RRRRR Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
            }
        } else {
            GoogleApiAvailability.getInstance().getErrorDialog(
                    this, result.getErrorCode(), REQUEST_RESOLVE_ERROR).show();
        }
    }

    /*******************
     * LocationListener
     ************************/

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "LLLLLLLLLLLLL Location Updated!");
        lastKnowLocation = location;
        stopLocationUpdates();
        callPlaceDetectionApi();
    }

    private void callPlaceDetectionApi() throws SecurityException {
        Log.d(TAG, "callPlaceDetectionApi");
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(this);
    }

    @Override
    public void onResult(@NonNull PlaceLikelihoodBuffer placeLikelihoods) {
        if (placeLikelihoods != null) {
            Log.d(TAG, "ESTATUS PLACE API " + placeLikelihoods.getStatus().getStatusCode() + " - " + placeLikelihoods.getStatus().getStatusMessage());
            if (placeLikelihoods.getStatus().isSuccess()) {
                PlaceLikelihood placeLikelihood = placeLikelihoods.get(0);
                if (placeLikelihood != null && placeLikelihood.getPlace() != null &&
                        !TextUtils.isEmpty(placeLikelihood.getPlace().getName())) {
                    place = placeLikelihood.getPlace().freeze();
                }
            }
        }
        placeLikelihoods.release();
        locationUpdated();
        startPickTakePhoto();
    }
}
