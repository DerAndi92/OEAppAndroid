package de.caroliwo.hawoe_rallye;

import android.os.Parcel;
import android.os.Parcelable;

public class TaskField implements Parcelable {

    //Für die Felder einer Aufgabe, die von der API kommen:
    //Der Typ eines Feldes in den Aufgaben ist fest vorgegeben. Es gibt folgende Typen
    //o 'button' – Ein Button.
    //o 'text' – Ein Text.
    //o 'inputField' – Ein kleines Textfeld für eine Zeile.
    //o 'inputText' – Ein großes Textfeld für mehrere Zeilen.
    //o 'inputInvisible' – Ein nicht sichtbares Element für Werte zusätzliche Werte, die gesendet werden können.

    private int id;
    private String type;
    private String value;
    private int order;


    //Parcelable
    protected TaskField(Parcel in) {
        id = in.readInt();
        type = in.readString();
        value = in.readString();
        order = in.readInt();
    }

    public static final Creator<TaskField> CREATOR = new Creator<TaskField>() {
        @Override
        public TaskField createFromParcel(Parcel in) {
            return new TaskField(in);
        }

        @Override
        public TaskField[] newArray(int size) {
            return new TaskField[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(type);
        dest.writeString(value);
        dest.writeInt(order);
    }

    //Getter & Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

}
