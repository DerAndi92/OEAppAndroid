package de.caroliwo.hawoe_rallye.Fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.caroliwo.hawoe_rallye.Activities.MainActivity;
import de.caroliwo.hawoe_rallye.Answer;
import de.caroliwo.hawoe_rallye.AnswerField;
import de.caroliwo.hawoe_rallye.Data.DataViewModel;
import de.caroliwo.hawoe_rallye.Data.StudentEntity;
import de.caroliwo.hawoe_rallye.Field;
import de.caroliwo.hawoe_rallye.R;
import de.caroliwo.hawoe_rallye.Student;
import de.caroliwo.hawoe_rallye.Task;

// Fragment um eine einzelne Aufgabe darzustellen
public class TaskFragment extends Fragment {

    // the fragment initialization parameters
    private static final String TASK = "task";
    private DataViewModel viewModel;

    private Task task;
    List<Field> fieldList;
    int taskID;
    String password;


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
        ImageView taskIcon = (ImageView) view.findViewById(R.id.student_dialog_IV);

        // Icon setzen
        int id = context.getResources().getIdentifier(task.getIcon(), "drawable", "de.caroliwo.hawoe_rallye");
        taskIcon.setImageResource(id);
        if (task.isCompleted()) {
            taskIcon.setColorFilter(Color.parseColor("#00FF00"));
        }

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
        for (final Field field : fieldList) {
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
                            sendAnswer();
                            getFragmentManager().popBackStack();
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

                            // Nach Änderung Text in's Field-Objekt einfügen
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
                    editTextBig.setHeight(50);
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

                            // Nach Änderung Text in's Field-Objekt einfügen
                            field.setValue(s.toString());
                        }
                    });
                    fieldsContainer.addView(editTextBig, layoutParams);
                    Log.i("TasksRVAdapter-Switch", "ET big created");
                    break;

                case "inputInvisible":
                    TextView TextInvisible = new TextView(context);
                    if (field.getValue() != null) {
                        TextInvisible.setText(field.getValue());
                    }
                    TextInvisible.setVisibility(View.INVISIBLE);
                    fieldsContainer.addView(TextInvisible);
                    Log.i("TasksRVAdapter-Switch", "ET invisible created");
                    break;

                default:
                    Log.i("TasksRVAdapter-Switch", "Field type is not defined");
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
        for (Field field2: fieldList) {
            if (field2.getType().equals("inputField") | field2.getType().equals("inputText")) {
                inputsArraylist.add(new AnswerField(field2.getId(),field2.getValue()));
            }
            if (field2.getType().equals("inputInvisible")) {
                inputsArraylist.add(new AnswerField(field2.getId(), String.valueOf(true)));
            }
        }
        StudentEntity student = viewModel.getStudent();
        int group = student.getGroupId(); //groupID

        Answer task1 = new Answer(group, taskID, password, inputsArraylist);

        viewModel.sendAnswer(task1);

    }



}