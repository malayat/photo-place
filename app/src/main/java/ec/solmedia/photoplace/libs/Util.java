package ec.solmedia.photoplace.libs;

import android.content.ContentResolver;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Util {
    private Geocoder geocoder;
    private static final String GRAVATAR_URL = "http://www.gravatar.com/avatar/";
    private static final String FACEBOOK_URL_AVATAR = "https://graph.facebook.com/%s/picture?type=normal";

    public Util(Geocoder geocoder) {
        this.geocoder = geocoder;
    }

    public String getAvatarUrl(String username) {
        return GRAVATAR_URL + md5(username) + "?s=64";
    }

    public String getFacebookUrlAvatar(String userID) {
        return String.format(FACEBOOK_URL_AVATAR, userID);
    }

    public Map<String, String> getNamesLocations(final double latitude, final double longitude) throws IOException {
        Map<String, String> result = new HashMap<>();

        List<Address> addressList = geocoder.getFromLocation(
                latitude, longitude, 1);
        if (addressList != null && addressList.size() > 0) {
            String cityName = addressList.get(0).getAddressLine(0);
            String stateName = addressList.get(0).getAddressLine(1);
            String countryName = addressList.get(0).getAddressLine(2);
            String postalCode = addressList.get(0).getPostalCode();
            String locality = addressList.get(0).getLocality();
            result.put("cityName", cityName);
            result.put("stateName", stateName);
            result.put("countryName", countryName);
            result.put("postalCode", postalCode == null ? "  " : postalCode);
            result.put("locality", locality);
        }

        return result;
    }

    public String getAddressFromLocation(final double latitude, final double longitude) throws IOException {
        String result = null;
        List<Address> addressList = geocoder.getFromLocation(
                latitude, longitude, 1);
        if (addressList != null && addressList.size() > 0) {
            Address address = addressList.get(0);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                sb.append(address.getAddressLine(i)).append(", ");
            }
            result = sb.toString();
        }
        return result;
    }

    private String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public File getFile(String appName) throws IOException {
        File photoFile = null;

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), appName);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        photoFile = File.createTempFile(imageFileName, ".jpg", storageDir);

        return photoFile;
    }

    public String getRealPathFromURI(Uri contentURI, ContentResolver contentResolver, File cacheDir) {
        String result = null;
        Cursor cursor = contentResolver.query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            if (contentURI.toString().contains("mediaKey")) {
                cursor.close();

                try {
                    File file = File.createTempFile("tempImg", ".jpg", cacheDir);
                    InputStream input = contentResolver.openInputStream(contentURI);
                    OutputStream output = new FileOutputStream(file);

                    try {
                        byte[] buffer = new byte[4 * 1024];
                        int read;

                        while ((read = input.read(buffer)) != -1) {
                            output.write(buffer, 0, read);
                        }
                        output.flush();
                        result = file.getAbsolutePath();
                    } finally {
                        output.close();
                        input.close();
                    }

                } catch (Exception e) {
                }
            } else {
                cursor.moveToFirst();
                int dataColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(dataColumn);
                cursor.close();
            }

        }
        return result;
    }

    public String getDateFormatedHoursMinutes(long timeStamp) {
        Date date = new Date(timeStamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm");
        return simpleDateFormat.format(date);
    }

    public String getDateFormated(long timeStamp) {
        Date date = new Date(timeStamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(date);
    }

    /*public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;*/


    /*public int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }*/

    /*private static boolean isInternetAvailable() {
        String netAddress = null;
        try {
            netAddress = new NetTask().execute("www.google.com").get();
            return !"".equals(netAddress);
        }
        catch (Exception e1) {
            return false;
        }
    }*/
}
