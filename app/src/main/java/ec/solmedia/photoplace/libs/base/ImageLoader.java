package ec.solmedia.photoplace.libs.base;

import android.widget.ImageView;

public interface ImageLoader {
    void load(ImageView imageView, String URL);
    void loadAvatar(ImageView imageView, String URL);
}
