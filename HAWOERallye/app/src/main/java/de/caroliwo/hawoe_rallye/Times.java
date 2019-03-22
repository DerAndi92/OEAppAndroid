package de.caroliwo.hawoe_rallye;

import android.os.Parcel;
import android.os.Parcelable;

//Klasse, für Zeiten der einzelnen Tasks

public class Times implements Parcelable {

    private String time_from;
    private String time_to;

    protected Times(Parcel in) {
        time_from = in.readString();
        time_to = in.readString();
    }

    public static final Creator<Times> CREATOR = new Creator<Times>() {
        @Override
        public Times createFromParcel(Parcel in) {
            return new Times(in);
        }

        @Override
        public Times[] newArray(int size) {
            return new Times[size];
        }
    };

    public String getTime_from() {
        return time_from;
    }

    public String getTime_to() {
        return time_to;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time_from);
        dest.writeString(time_to);
    }
}
