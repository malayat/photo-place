package ec.solmedia.photoplace.db;

import com.raizlabs.android.dbflow.annotation.Database;

@Database(version = PhotoPlaceDatabase.VERSION, name = PhotoPlaceDatabase.NAME)
public class PhotoPlaceDatabase {
    public static final int VERSION = 1;
    public static final String NAME = "PhotoPlace";
}
