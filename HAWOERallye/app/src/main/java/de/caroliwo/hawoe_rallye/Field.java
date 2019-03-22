package de.caroliwo.hawoe_rallye;

public class Field {

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
