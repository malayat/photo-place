package ec.solmedia.photoplace.placesdetail.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ec.solmedia.photoplace.PhotoPlaceApp;
import ec.solmedia.photoplace.R;
import ec.solmedia.photoplace.entities.MyPlace;
import ec.solmedia.photoplace.libs.Util;
import ec.solmedia.photoplace.libs.base.ImageLoader;

public class PhotoPhotoPlaceDetailActivity extends AppCompatActivity implements PhotoPlaceDetailView {

    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_phono)
    TextView tvPhono;
    @BindView(R.id.tv_url)
    TextView tvUrl;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.rating)
    RatingBar rating;
    @BindView(R.id.header_image)
    ImageView headerImage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapser)
    CollapsingToolbarLayout collapser;

    @Inject
    Util util;
    @Inject
    ImageLoader imageLoader;

    public static final String EXTRA_DETAIL_VIEW = "MyPlaceData";
    private static final String TAG = "PlaceDetailActivity";

    private MyPlace myPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);
        ButterKnife.bind(this);

        setupInjection();

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            this.myPlace = bundle.getParcelable(EXTRA_DETAIL_VIEW);
        }

        setupToolbar();
        populateView();
    }

    private void setupInjection() {
        PhotoPlaceApp app = (PhotoPlaceApp) getApplication();
        app.getPhotoPlaceDetailComponent(this, this).inject(this);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (myPlace != null) {
            collapser.setTitle(myPlace.getName());
            imageLoader.load(headerImage, myPlace.getPhoto());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void populateView() {
        if (myPlace != null) {
            tvAddress.setText(myPlace.getAddress());
            tvPhono.setText(myPlace.getPhone());
            tvUrl.setText(myPlace.getUrl());
            tvDescription.setText(myPlace.getDescription());
            tvDate.setText(util.getDateFormatedHoursMinutes(myPlace.getDate()));
            rating.setRating(myPlace.getRaiting());
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photoplace_menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_edit:
                navigateToEditActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToEditActivity() {
        Log.e(TAG, "DetailActivity navigate to Edit Activity");
    }
}
