package ec.solmedia.photoplace.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import ec.solmedia.photoplace.db.PhotoPlaceDatabase;

@Table(database = PhotoPlaceDatabase.class)
public class MyPlace extends BaseModel implements Parcelable {

    @Column
    @PrimaryKey(autoincrement = true)
    private int id;

    @Column
    private String placeId;
    @Column
    private String name;
    @Column
    private String address;
    @Column
    private String photo;
    @Column
    private String phone;
    @Column
    private String url;
    @Column
    private String description;
    @Column
    private long date;
    @Column
    private float raiting;
    @Column
    private String email;
    @Column
    private double latitude;
    @Column
    private double longitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public float getRaiting() {
        return raiting;
    }

    public void setRaiting(float raiting) {
        this.raiting = raiting;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "MyPlace{" +
                "id=" + id +
                ", placeId='" + placeId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", url='" + url + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", raiting=" + raiting +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

    public MyPlace() {
    }

    protected MyPlace(Parcel in) {
        placeId = in.readString();
        name = in.readString();
        address = in.readString();
        photo = in.readString();
        phone = in.readString();
        url = in.readString();
        description = in.readString();
        date = in.readLong();
        raiting = in.readFloat();
        email = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(placeId);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(photo);
        dest.writeString(phone);
        dest.writeString(url);
        dest.writeString(description);
        dest.writeLong(date);
        dest.writeFloat(raiting);
        dest.writeString(email);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<MyPlace> CREATOR = new Parcelable.Creator<MyPlace>() {
        @Override
        public MyPlace createFromParcel(Parcel in) {
            return new MyPlace(in);
        }

        @Override
        public MyPlace[] newArray(int size) {
            return new MyPlace[size];
        }
    };
}
