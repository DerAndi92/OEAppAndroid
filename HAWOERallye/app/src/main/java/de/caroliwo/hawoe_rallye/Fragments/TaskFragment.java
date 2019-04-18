package de.caroliwo.hawoe_rallye.Fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.lang.reflect.Field;

import de.caroliwo.hawoe_rallye.Activities.MainActivity;
import de.caroliwo.hawoe_rallye.Answer;
import de.caroliwo.hawoe_rallye.AnswerField;
import de.caroliwo.hawoe_rallye.Data.ConfigurationEntity;
import de.caroliwo.hawoe_rallye.Data.DataViewModel;
import de.caroliwo.hawoe_rallye.Data.StudentEntity;
import de.caroliwo.hawoe_rallye.TaskField;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Task;

// Fragment um eine einzelne Aufgabe darzustellen
public class TaskFragment extends Fragment {

    // the fragment initialization parameters
    private static final String TASK = "task";
    private DataViewModel viewModel;

    private Task task;
    List<TaskField> fieldList;
    int taskID;
    String password;
    ImageView taskIcon;

    public TaskFragment() {
        // Benötigter leerer Konstruktor
    }

    // Methode um eine neue Instanz dieses Fragments bereitzustellen
    public static TaskFragment newInstance(Task task) {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        args.putParcelable(TASK, task);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            task = getArguments().getParcelable(TASK);
        }
        // Viewmodel-Instanz holen
        viewModel = ViewModelProviders.of((MainActivity) getActivity()).get(DataViewModel.class);

        // Variablen zuweisen
        fieldList = task.getFieldList();
        taskID = task.getId();
        password = "";
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Kontext und Layout initialisieren
        Context context = getContext();
        View view = inflater.inflate(R.layout.fragment_task, container, false);

        // Views zuweisen
        TextView taskTitle = (TextView) view.findViewById(R.id.task_dialog_TV);
        TextView taskTime = (TextView) view.findViewById(R.id.task_dialog_TV3);
        TextView taskDestination = view.findViewById(R.id.task_dialog_TV2);
        taskIcon = (ImageView) view.findViewById(R.id.student_dialog_IV);

        // Icon setzen
        int id = context.getResources().getIdentifier(task.getIcon(), "drawable", "de.caroliwo.hawoe_rallye");
        taskIcon.setImageResource(id);
        //Task-Icon grün färben, wenn die Aufgabe erledigt ist
        if (task.isCompleted()) {
            taskIcon.setColorFilter(Color.parseColor("#00FF00"));
        }

        //TODO: grüne Färbung, wenn Aufgabe erledigt; Momentan erst nach neuladen der App

        // Zeit setzen
        String time_from = (String) task.getTime().getTime_from();
        String time_to = (String) task.getTime().getTime_to();
        if (time_from != null && time_to != null) {
            taskTime.setText(time_from + " - " + time_to + " Uhr");
        } else {
            taskTime.setText("");
        }

        // Text setzen
        taskTitle.setText(task.getName());
        taskDestination.setText(task.getDestination());

        // LinearLayout für dynamisches Erstellen der Dialog-Inhalte (Aufgaben)
        LinearLayout fieldsContainer = (LinearLayout) view.findViewById(R.id.FieldsContainer);

        //Für Abstand zwischen den Views im LinearLayout
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        // Für Abstand der Views zum Rand
        layoutParams.setMargins(8, 0, 8, 50);

        fieldsContainer.removeAllViews();

        //Layout-Elemente generieren
        for (final TaskField field : fieldList) {
            String type = field.getType();
            switch (type) {

                case "button":
                    // Passwort-Feld einfügen falls benötigt
                    if (task.getPassword()) {
                        EditText passwordField = new EditText(context);
                        passwordField.setHint("Passwort");
                        passwordField.setTextColor(context.getResources().getColor(R.color.colorText));
                        passwordField.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        passwordField.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                                // Nach Änderung Passwort in Variable speichern
                                password = s.toString();
                            }
                        });
                        fieldsContainer.addView(passwordField);
                    }

                    Button button = new Button(context);
                    button.setText(field.getValue());
                    button.setTextColor(context.getResources().getColor(R.color.colorText));
                    button.setBackground(context.getResources().getDrawable(R.drawable.buttonshape));
                    fieldsContainer.addView(button, layoutParams);
                    Log.i("TasksRVAdapter-Switch", "Button created");

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            if (sendAnswer()) {
//                                getFragmentManager().popBackStack();
//                            } else {
//                                Toast.makeText(getContext(), "Passwort nicht akzeptiert", Toast.LENGTH_LONG);
//                            }
                            sendAnswer();
                        }
                    });
                    break;

                case "text":
                    TextView textV = new TextView(context);
                    textV.setText(field.getValue());
                    textV.setTextColor(context.getResources().getColor(R.color.colorText));
                    fieldsContainer.addView(textV, layoutParams);
                    Log.i("TasksRVAdapter-Switch", "TV created");
                    break;

                case "inputField":
                    EditText editText = new EditText(context);
                    if (field.getValue() != null) {
                        editText.setText(field.getValue());
                    }
                    editText.setTextColor(context.getResources().getColor(R.color.colorText));
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            // Nach Änderung Text ins TaskField-Objekt einfügen
                            field.setValue(s.toString());
                        }
                    });
                    fieldsContainer.addView(editText, layoutParams);
                    Log.i("TasksRVAdapter-Switch", "ET created");
                    break;

                case "inputText":
                    EditText editTextBig = new EditText(context);
                    if (field.getValue() != null) {
                        editTextBig.setText(field.getValue());
                    }
                    editTextBig.setHeight(500);
                    editTextBig.setTextColor(context.getResources().getColor(R.color.colorText));
                    editTextBig.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            // Nach Änderung Text in's TaskField-Objekt einfügen
                            field.setValue(s.toString());
                        }
                    });
                    fieldsContainer.addView(editTextBig, layoutParams);
                    Log.i("TasksRVAdapter-Switch", "ET big created");
                    break;

                case "inputTime":
                    NumberPicker numberPicker = new NumberPicker(context);
                    numberPicker.setMinValue(0);
                    numberPicker.setMaxValue(24);
                    setNumberPickerColor(numberPicker,  Color.parseColor("#ffffff"));
                    if (field.getValue() != null) {
                        numberPicker.setValue(Integer.valueOf(field.getValue()));
                    }
                    numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                        @Override
                        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                            field.setValue(String.valueOf(newVal));
                        }
                    });
                    fieldsContainer.addView(numberPicker, layoutParams);
                    Log.i("TasksRVAdapter-Switch", "NP inputTime");
                    break;

                case "inputNumber":
                    EditText inputNumber = new EditText(context);
                    //nur Zahlen erlauben
                    inputNumber.setInputType(InputType.TYPE_CLASS_NUMBER);

                    if (field.getValue() != null) {
                        inputNumber.setText(field.getValue());
                    }
                    inputNumber.setTextColor(context.getResources().getColor(R.color.colorText));
                    inputNumber.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            // Nach Änderung Text ins TaskField-Objekt einfügen
                            field.setValue(s.toString());
                        }
                    });

                    fieldsContainer.addView(inputNumber, layoutParams);
                    Log.i("TasksRVAdapter-Switch", "ET inputTime");
                    break;

                case "inputInvisible":
                    TextView textInvisible = new TextView(context);
                    if (field.getValue() != null) {
                        textInvisible.setText(field.getValue());
                    }
                    textInvisible.setVisibility(View.INVISIBLE);
                    fieldsContainer.addView(textInvisible, layoutParams);
                    Log.i("TasksRVAdapter-Switch", "ET invisible created");
                    break;

                default:
                    Log.i("TasksRVAdapter-Switch", "TaskField type is not defined");
            }
        }
        //Zurück-Button
        Button backButton = new Button(context);
        backButton.setText("Zurück");
        backButton.setTextColor(context.getResources().getColor(R.color.colorText));
        backButton.setBackground(context.getResources().getDrawable(R.drawable.buttonshape));
        fieldsContainer.addView(backButton, layoutParams);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        return view;
    }

    private void sendAnswer() {

        List<AnswerField> inputsArraylist = new ArrayList<>();
        for (TaskField field2 : fieldList) {
            if (field2.getType().equals("inputField") || field2.getType().equals("inputText") || field2.getType().equals("inputNumber") || field2.getType().equals("inputTime")) {
                inputsArraylist.add(new AnswerField(field2.getId(), field2.getValue()));
            }
            if (field2.getType().equals("inputInvisible")) {
                inputsArraylist.add(new AnswerField(field2.getId(), String.valueOf(true)));
            }
        }
        ConfigurationEntity configEntity = viewModel.getConfig();
        StudentEntity student = viewModel.getStudent();
        int group = student.getGroupId(); //groupID

        Answer task1 = new Answer(group, taskID, password, inputsArraylist);

        //Antworten können nur abgesendet werden, wenn die AbgabeZeit nicht überschritten wurde.
        DateFormat formatter = new SimpleDateFormat();
        Date configTimeParsed;
        Date currentTime = Calendar.getInstance().getTime();
        String configTime = configEntity.getMaxTime();

        try {
            //configTime von String in Date parsen
            DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            configTimeParsed = format.parse(configTime);
            // feststellen, ob Abgabe-Zeit vor der momentanen Uhrzeit liegt
            if (/*configTimeParsed.after(currentTime) || configTime.equals(currentTime)*/true) {
            viewModel.sendAnswer(task1);
            viewModel.getCorrectPasswordLiveData().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(@Nullable Boolean aBoolean) {
                    if (aBoolean) {
                        // TODO: Erledigte Aufgabe wird danach ganz unten einsortiert, Bug oder Feature?
                        task.setCompleted(true);
                        viewModel.changeTask(task);
                        getFragmentManager().popBackStack();
                    } else {
                        // TODO: wird auch angezeigt wenn nach ehemaliger Falscheingabe richtiges Passwort eingegeben wird
                        Toast.makeText(getContext(), "Passwort nicht akzeptiert", Toast.LENGTH_LONG).show();
                        Log.i("TaskFragment", "Password Incorrect");
                    }
                }
            });

            } else {
                Toast.makeText(getContext(), "Abgabezeit vorbei.", Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void setNumberPickerColor(NumberPicker numberPicker, int color) {

        //TextColor
        try{
            Field selectorWheelPaintField = numberPicker.getClass()
                    .getDeclaredField("mSelectorWheelPaint");
            selectorWheelPaintField.setAccessible(true);
            ((Paint)selectorWheelPaintField.get(numberPicker)).setColor(color);
        }
        catch(NoSuchFieldException e){
            Log.w("setNumberPickerTextCol", e);
        }
        catch(IllegalAccessException e){
            Log.w("setNumberPickerTextCol", e);
        }
        catch(IllegalArgumentException e){
            Log.w("setNumberPickerTextCol", e);
        }

        final int count = numberPicker.getChildCount();
        for(int i = 0; i < count; i++){
            View child = numberPicker.getChildAt(i);
            if(child instanceof EditText)
                ((EditText)child).setTextColor(color);
        }
        numberPicker.invalidate();


        //Divider
        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(numberPicker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}