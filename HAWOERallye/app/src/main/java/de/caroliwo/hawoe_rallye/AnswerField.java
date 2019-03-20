package de.caroliwo.hawoe_rallye;

public class AnswerField {

    private int id;
    private String value;

    // Konstruktor

    public AnswerField(int id, String value) {
        this.id = id;
        this.value = value;
    }


    // Getter + Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
