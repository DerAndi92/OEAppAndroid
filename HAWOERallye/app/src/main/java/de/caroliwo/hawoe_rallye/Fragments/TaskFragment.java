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
import android.text.TextWatcher;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskFragment extends Fragment {

    // the fragment initialization parameters
    private static final String TASK = "task";
    private DataViewModel viewModel;

    private Task task;
    private Task taskDetail;
    List<Field> fieldList;
    int taskID;
    String password;
//    private CustomGestureDetector gDetector


    public TaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param task Parameter 1.
     * @return A new instance of fragment TaskFragment.
     */
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
//        gDetector = new GestureDetectorCompat(getContext(), new CustomGestureDetector());

        // Variablen zuweisen
        fieldList = task.getFieldList();
        taskID = task.getId();
        password = ""; // TODO: Wie kommt man an das Passwort vom Task-Objekt?
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Kontext und Layout initialisieren
        Context context = getContext();
        View view = inflater.inflate(R.layout.fragment_task, container, false);

//        CustomConstraintLayout constraintLayout = view.findViewById(R.id.constraintLayout);


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


        for (final Field field : fieldList) {
            String type = field.getType();
            switch (type) {
                case "button":
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
                    EditText editTextInvisible = new EditText(context);
                    if (field.getValue() != null) {
                        editTextInvisible.setText(field.getValue());
                    }
                    editTextInvisible.setVisibility(View.INVISIBLE);
                    fieldsContainer.addView(editTextInvisible);
                    Log.i("TasksRVAdapter-Switch", "ET invisible created");
                    break;
                default:
                    Log.i("TasksRVAdapter-Switch", "Field type is not defined");
            }
        }


        return view;
    }

    private void sendAnswer() {
        List<AnswerField> inputsArraylist = new ArrayList<>();
        for (Field field2: fieldList) {
            if (field2.getType().equals("inputField") | field2.getType().equals("inputText")) {
                inputsArraylist.add(new AnswerField(field2.getId(),field2.getValue()));
            }
        }
        StudentEntity student = viewModel.getStudent();
        int group = student.getGroupId(); //groupID

        Answer task1 = new Answer(group, taskID, password, inputsArraylist);

        viewModel.sendAnswer(task1);

    }



//----------------------------NOCH NICHT IMPLEMENTIERT----------------------------------------------


    private class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_THRESHOLD_VELOCITY = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                getFragmentManager().popBackStack();
                return true;
            }
            return false;
        }
    }


    private class CustomConstraintLayout extends ConstraintLayout {

        private int mTouchSlop;
        private boolean mIsScrolling;
        ViewConfiguration vc = ViewConfiguration.get(getContext());
        private int mSlop = vc.getScaledTouchSlop();
        private int mMinFlingVelocity = vc.getScaledMinimumFlingVelocity();
        private int mMaxFlingVelocity = vc.getScaledMaximumFlingVelocity();
        float downX;


        public CustomConstraintLayout(Context context) {
            super(context);
        }

        public CustomConstraintLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public CustomConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }


        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            /*
             * This method JUST determines whether we want to intercept the motion.
             * If we return true, onTouchEvent will be called and we do the actual
             * scrolling there.
             */

            Log.i("TaskFragment", "CustomConstraintLayout.onInterceptTouchEvent");

            final int action = ev.getActionMasked();

            // Always handle the case of the touch gesture being complete.
            if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
                // Release the scroll.
                mIsScrolling = false;
                return false; // Do not intercept touch event, let the child handle it
            }

            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    Log.i("TaskFragment", "CustomConstraintLayout.OnInterceptTouchEvent.ACTION_DOWN");
                    downX = ev.getX();
                }

                case MotionEvent.ACTION_MOVE: {
                    Log.i("TaskFragment", "CustomConstraintLayout.OnInterceptTouchEvent.ACTION_MOVE");
                    if (mIsScrolling) {
                        // We're currently scrolling, so yes, intercept the
                        // touch event!
                        return true;
                    }

                    // If the user has dragged her finger horizontally more than
                    // the touch slop, start the scroll

                    final float xDiff = ev.getX() - ev.getHistoricalX(0);

                    // Touch slop should be calculated using ViewConfiguration
                    // constants.
                    if (xDiff > mTouchSlop) {
                        // Start scrolling!
                        mIsScrolling = true;
                        return true;
                    }
                    break;
                }

            }

            // In general, we don't want to intercept touch events. They should be
            // handled by the child view.
            return false;
        }

        @Override
        public boolean onTouchEvent(MotionEvent ev) {
            // Here we actually handle the touch event (e.g. if the action is ACTION_MOVE,
            // scroll this container).
            // This method will only be called if the touch event was intercepted in
            // onInterceptTouchEvent

            switch(ev.getAction()) {
                case MotionEvent.ACTION_MOVE: {

                    float deltaX = ev.getRawX() - downX;
                    if (Math.abs(deltaX) > mSlop && deltaX < 0) {
                        Log.i("TaskFragment", "CustomConstraintLayout.onTouchEvent.ACTION_LEFT");
                        getFragmentManager().popBackStack();
                        return true;
                    }
                }
//--------------------------------------------------------------------------------------------------
//                case MotionEvent.ACTION_UP: {
//                    if (mMinFlingVelocity <= velocityX && velocityX <= mMaxFlingVelocity
//                            && velocityY < velocityX) {
//                        // The criteria have been satisfied, do something
//                    }
//                }
//--------------------------------------------------------------------------------------------------
            }


            return false;
        }
    }

//--------------------------------------------------------------------------------------------------
//        float mLastX;
//        float mLastY;
//        float mStartY;
//        boolean mIsBeingDragged;
//
//
//        public CustomConstraintLayout(Context context) {
//            super(context);
//            gDetector = new CustomGestureDetector();
//            setOnTouchListener();
//        }
//
//        public CustomConstraintLayout(Context context, AttributeSet attrs) {
//            super(context, attrs);
//            gDetector = new CustomGestureDetector();
//        }
//
//        public CustomConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
//            super(context, attrs, defStyleAttr);
//            gDetector = new CustomGestureDetector();
//        }
//
//        @Override
//        public boolean onInterceptTouchEvent(MotionEvent event) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    mLastX = event.getX();
//                    mLastY = event.getY();
//                    mStartY = mLastY;
//                    break;
//                case MotionEvent.ACTION_CANCEL:
//                case MotionEvent.ACTION_UP:
//                    mIsBeingDragged = false;
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    float x = event.getX();
//                    float y = event.getY();
//                    float xDelta = Math.abs(x - mLastX);
//                    float yDelta = Math.abs(y - mLastY);
//
//                    float yDeltaTotal = y - mStartY;
//                    if (yDelta < xDelta && Math.abs(yDeltaTotal) > mTouchSlop) {
//                        mIsBeingDragged = true;
//                        mStartY = y;
//                        return true;
//                    }
//                    break;
//            }
//
//            return false;
//        }
//
//        @Override
//        public boolean onTouchEvent(MotionEvent event) {
//            switch (event.getAction()) {
//                case MotionEvent.ACTION_CANCEL:
//                case MotionEvent.ACTION_UP:
//                    mIsBeingDragged = false;
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    float x = event.getX();
//                    float y = event.getY();
//
//                    float xDelta = Math.abs(x - mLastX);
//                    float yDelta = Math.abs(y - mLastY);
//
//                    float yDeltaTotal = y - mStartY;
//                    if (!mIsBeingDragged && yDelta > xDelta && Math.abs(yDeltaTotal) > mTouchSlop) {
//                        mIsBeingDragged = true;
//                        mStartY = y;
//                        yDeltaTotal = 0;
//                    }
//                    if (yDeltaTotal < 0)
//                        yDeltaTotal = 0;
//
//                    if (mIsBeingDragged) {
//                        scrollTo(0, yDeltaTotal);
//                    }
//
//                    mLastX = x;
//                    mLastY = y;
//                    break;
//            }
//
//            return true;
//        }
//
//    }
}