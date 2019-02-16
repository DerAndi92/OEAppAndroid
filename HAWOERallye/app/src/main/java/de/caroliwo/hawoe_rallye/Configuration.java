package de.caroliwo.hawoe_rallye;

import android.os.Parcel;
import android.os.Parcelable;

public class Configuration implements Parcelable {
    private String password;
    private String maxTime;

    protected Configuration(Parcel in) {
        password = in.readString();
        maxTime = in.readString();
    }

    public static final Creator<Configuration> CREATOR = new Creator<Configuration>() {
        @Override
        public Configuration createFromParcel(Parcel in) {
            return new Configuration(in);
        }

        @Override
        public Configuration[] newArray(int size) {
            return new Configuration[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(password);
        dest.writeString(maxTime);
    }

    //GETTER + SETTER
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(String maxTime) {
        this.maxTime = maxTime;
    }
}
