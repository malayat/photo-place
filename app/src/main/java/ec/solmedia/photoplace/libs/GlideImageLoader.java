package ec.solmedia.photoplace.libs;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import ec.solmedia.photoplace.libs.base.ImageLoader;

public class GlideImageLoader implements ImageLoader {
    private RequestManager glideRequestManager;

    public void setLoaderContextActivity(Activity activity) {
        this.glideRequestManager = Glide.with(activity);
    }

    public void setLoaderContextFragement(Fragment fragment) {
        this.glideRequestManager = Glide.with(fragment);
    }

    @Override
    public void load(ImageView imageView, String URL) {
        glideRequestManager
                .load(URL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .override(600, 400)
                .into(imageView);
    }

    @Override
    public void loadAvatar(ImageView imageView, String URL) {
        glideRequestManager
                .load(URL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(imageView);
    }
}
