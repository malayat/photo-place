package ec.solmedia.photoplace.placeslist.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.solmedia.photoplace.PhotoPlaceApp;
import ec.solmedia.photoplace.R;
import ec.solmedia.photoplace.entities.MyPlace;
import ec.solmedia.photoplace.placesdetail.ui.PhotoPhotoPlaceDetailActivity;
import ec.solmedia.photoplace.placeslist.adapter.OnItemClickListener;
import ec.solmedia.photoplace.placeslist.adapter.PhotoPlaceListAdapter;
import ec.solmedia.photoplace.placeslist.contract.PhotoPlaceListPresenter;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlacesListFragment extends Fragment implements PhotoPlaceListView, OnItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.container)
    FrameLayout container;

    @Inject
    PhotoPlaceListPresenter presenter;
    @Inject
    PhotoPlaceListAdapter adapter;

    public static final String TAG = "PlacesListFragment";

    //private static final int PERMISSION_REQUEST_EXTERNAL_STORAGE = 5;

    public PlacesListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupInjection();
        presenter.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_list, container, false);
        ButterKnife.bind(this, view);
        setupRecyclerView();
        presenter.getPlaces();
        return view;
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_EXTERNAL_STORAGE:
                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(container,"Ahora ya puede compartir sus fotos.", Snackbar.LENGTH_LONG).show();
                } else {
                    Snackbar.make(container,"Sin los permisos necesarios no se puede compartir", Snackbar.LENGTH_LONG).show();
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void setupInjection() {
        PhotoPlaceApp app = (PhotoPlaceApp) getActivity().getApplication();
        app.getPhotoPlaceListComponent(this, this, this).inject(this);
    }

    /*************
     * PhotoPlaceListView
     **********************/
    @Override
    public void showList() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideList() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setPlaces(List<MyPlace> data) {
        adapter.setMyPlaces(data);
    }

    @Override
    public void notificationSave() {
        presenter.getPlaces();
    }

    @Override
    public void removePlace(MyPlace myPlace) {
        //Log.e(TAG, "removePlace-> " +myPlace.toString());
        adapter.removePlace(myPlace);
    }

    /**************
     * OnItemClickListener
     ********************/
    @Override
    public void onAddressClick(MyPlace myPlace) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("geo:" + myPlace.getLatitude() + "," + myPlace.getLongitude()));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onShareClick(MyPlace myPlace, ImageView img) {
        /*if(ContextCompat.checkSelfPermission(
                getActivity().getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, PERMISSION_REQUEST_EXTERNAL_STORAGE);
            }
        } else {*/
        Bitmap bitmap = ((GlideBitmapDrawable) img.getDrawable()).getBitmap();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), bitmap, null, null);
        Uri imageUri = Uri.parse(path);

        share.putExtra(Intent.EXTRA_STREAM, imageUri);
        startActivity(Intent.createChooser(share, getString(R.string.photolist_message_share)));
        //}

    }

    @Override
    public void onDeleteClick(MyPlace myPlace) {
        presenter.removePlace(myPlace);
    }

    @Override
    public void onImageClick(MyPlace myPlace) {
        Intent intent = new Intent(getActivity(), PhotoPhotoPlaceDetailActivity.class);
        intent.putExtra(PhotoPhotoPlaceDetailActivity.EXTRA_DETAIL_VIEW, myPlace);
        startActivity(intent);
    }

    @Override
    public void onGoogleMapsClick(MyPlace myPlace) {
        presenter.moveCameraToMarker(myPlace);
        ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewPager);
        viewPager.setCurrentItem(1);
    }
}
