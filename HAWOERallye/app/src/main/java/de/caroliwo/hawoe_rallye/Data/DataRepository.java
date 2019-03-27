package de.caroliwo.hawoe_rallye.Data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import de.caroliwo.hawoe_rallye.Answer;
import de.caroliwo.hawoe_rallye.AnswerAPI;
import de.caroliwo.hawoe_rallye.Configuration;
import de.caroliwo.hawoe_rallye.ConfigurationAPI;
import de.caroliwo.hawoe_rallye.DownloadJSONRetrofit;
import de.caroliwo.hawoe_rallye.Group;
import de.caroliwo.hawoe_rallye.GroupAPI;
import de.caroliwo.hawoe_rallye.GroupsAPI;
import de.caroliwo.hawoe_rallye.Retrofit;
import de.caroliwo.hawoe_rallye.Student;
import de.caroliwo.hawoe_rallye.StudentAPI;
import de.caroliwo.hawoe_rallye.Task;
import de.caroliwo.hawoe_rallye.TaskAPI;
import de.caroliwo.hawoe_rallye.TasksAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class DataRepository {

    // Variablen für Data-Access-Objects für Datenbank
    private ConfigurationDAO configDao;
    private StudentDAO studDao;

    // Variablen für LiveData-Datensätze aus Datenbank
    private LiveData<ConfigurationEntity> configEntity;
    private LiveData<StudentEntity> studentEntity;

    // Variablen für LiveData-Datensätze aus API-Calls
    private MutableLiveData<ArrayList<Group>> groupList;
    private MutableLiveData<ArrayList<Task>> taskList;
    private MutableLiveData<ArrayList<Student>> studentList;

    // Variablen für API-Calls an Web-Interface
    private DownloadJSONRetrofit downloadJSONRetrofit;


    // Konstruktor
    public DataRepository(Application application) {

        // Instanz der Datenbank holen
        Database database = Database.getInstance(application);
        Log.i("DataRepository", "1");

        // Data-Access-Objects zuweisen
        configDao = database.configDao();
        Log.i("DataRepository", "2");
        studDao = database.studDao();
        Log.i("DataRepository", "3");

        // LiveData-Variablen zuweisen
        configEntity = configDao.getConfigLiveData();
        studentEntity = studDao.getStudentLiveData();
        groupList = new MutableLiveData<>();
        taskList = new MutableLiveData<>();
        studentList = new MutableLiveData<>();

        // Retrofit instanziieren
        Retrofit retrofitClass = new Retrofit();
        downloadJSONRetrofit = retrofitClass.createlogInterceptor(application);
    }

    // Methoden um LiveData von API-Calls lokal zu ändern
    public void addStudentLiveData(Student student) {
        Log.i("DataRepository", "addStudentLiveData() input: " + student.toString());
        ArrayList<Student> tempList = new ArrayList<>();
        if(this.studentList.getValue() != null) {
            tempList = this.studentList.getValue();
            Log.i("DataRepository", "addStudentLiveData() tempList: " + tempList.toString()); 
        }
        tempList.add(student);
        this.studentList.setValue(tempList);
        Log.i("DataRepository", "addStudentLiveData() studentList: " + this.studentList.getValue().toString());
    }

    public void removeStudentLiveData(Student student) {
        Log.i("DataRepository", "removeStudentLiveData() student: " + student.toString());
        ArrayList<Student> tempList = new ArrayList<Student>(this.studentList.getValue());
        Log.i("DataRepository", "removeStudentLiveData() studentList.getValue(): " + this.studentList.getValue());
        tempList.remove(student);
        this.studentList.setValue(tempList);
    }

    public void changeStudentLiveData(Student newStudent) {
        Log.i("DataRepository", "changeStudent() student: " + newStudent.toString());
        ArrayList<Student> tempList = new ArrayList<Student>(this.studentList.getValue());
        Log.i("DataRepository", "changeStudent() studentList.getValue(): " + this.studentList.getValue());
        for (Student student: tempList) {
            if (student.getStudentId() == newStudent.getStudentId()) removeStudentLiveData(student);
        }
        addStudentLiveData(newStudent);
    }

    public void addGroupLiveData(Group group) {
        ArrayList<Group> tempList = new ArrayList<>();
        if(this.groupList.getValue() != null) {
            tempList = this.groupList.getValue();
        }
        tempList.add(group);
        this.groupList.setValue(tempList);
    }

    public void removeGroupLiveData(Group group) {
        ArrayList<Group> tempList = new ArrayList<Group>(this.groupList.getValue());
        tempList.remove(group);
        this.groupList.setValue(tempList);
    }

    public void changeGroupLiveData(Group newGroup) {
        ArrayList<Group> tempList = new ArrayList<Group>(this.groupList.getValue());
        for (Group group : tempList) {
            if (group.getGroupId() == newGroup.getGroupId()) removeGroupLiveData(group);
        }
        addGroupLiveData(newGroup);
    }


    public void addTaskToLiveDataList(Task task, MutableLiveData<ArrayList<Task>> list) {
        Log.i("DataRepository", "addTaskToLiveDataList() task: " + task.toString());
        ArrayList<Task> tempList = new ArrayList<>();
        if(list.getValue()!=null) {
            tempList = list.getValue();
            Log.i("DataRepository", "addTaskToLiveDataList() list: " + list.getValue().toString());
        }
        tempList.add(task);
        list.setValue(tempList);
    }


    // Methoden um eigene Student-Id in Datenbank nachzutragen
    private void correctStudentId() {

        // Checken ob Inhalt von studentList nicht null ist
        if (studentList.getValue() != null) {

            // Student-Datensatz aus Datenbank holen
            StudentEntity tempStudentEntity = getStudent();

            // Name und Kurs mit allen Gruppenmitgliedern vergleichen um eigenen Eintrag zu finden
            for (Student student : studentList.getValue()) {
                if (student.getFirst_name().equals(tempStudentEntity.getFirst_name())
                        && student.getLast_name().equals(tempStudentEntity.getLast_name())
                        && student.getCourse().equals(tempStudentEntity.getCourse())) {

                    // Richtige student-ID setzen
                    tempStudentEntity.setStudentId(student.getStudentId());
                    // Datenbankeintrag updaten
                    updateStudent(tempStudentEntity);
                }
            }
        }

    }
    private boolean studentIdIsCorrect() { return getStudent().getStudentId() != -1; }


    // Methoden welche LiveData liefern

    public LiveData<ConfigurationEntity> getConfigLiveData() {
        return configEntity;
    }

    public LiveData<StudentEntity> getStudentLiveData() {
        return studentEntity;
    }

    public LiveData<ArrayList<Group>> getGroupListLiveData() {
        return groupList;
    }

    public LiveData<ArrayList<Task>> getTaskListLiveData() {
        return taskList;
    }

    public LiveData<ArrayList<Student>> getStudentListLiveData() { return studentList; }



    // Methoden für direkte Datenbankabfrage auf dem Main-Thread

    public StudentEntity getStudent() { return studDao.getStudent(); }

    public ConfigurationEntity getConfig() { return configDao.getConfig(); }



    // Methoden welche AsyncTasks aufrufen (für Datenbank-Operationen)

    public void insertConfig(ConfigurationEntity entity) {
        new InsertConfigAsyncTask(configDao).execute(entity);
    }

    public void updateConfig(ConfigurationEntity entity) {
        new UpdateConfigAsyncTask(configDao).execute(entity);
    }

    public void deleteAllConfigs() {
        new DeleteAllConfigsAsyncTask(configDao).execute();
    }

    public void insertStudent(StudentEntity entity) {
        new InsertStudentAsyncTask(studDao).execute(entity);
    }

    public void updateStudent(StudentEntity entity) {
        new UpdateStudentAsyncTask(studDao).execute(entity);
    }

    public void deleteAllStudents() {
        new DeleteAllStudentsAsyncTask(studDao).execute();
    }


    // AsyncTask-Subklassen für Datenbank-Operationen

    private static class InsertConfigAsyncTask extends AsyncTask<ConfigurationEntity, Void, Void> {

        private ConfigurationDAO configDao;

        private InsertConfigAsyncTask(ConfigurationDAO configDao) {
            this.configDao = configDao;
        }

        @Override
        protected Void doInBackground(ConfigurationEntity... configurationEntities) {
            configDao.insert(configurationEntities[0]);
            return null;
        }
    }

    private static class UpdateConfigAsyncTask extends AsyncTask<ConfigurationEntity, Void, Void> {

        private ConfigurationDAO configDao;

        private UpdateConfigAsyncTask(ConfigurationDAO configDao) {
            this.configDao = configDao;
        }

        @Override
        protected Void doInBackground(ConfigurationEntity... configurationEntities) {
            configDao.update(configurationEntities[0]);
            return null;
        }
    }

    private static class DeleteAllConfigsAsyncTask extends AsyncTask<Void, Void, Void> {

        private ConfigurationDAO configDao;

        private DeleteAllConfigsAsyncTask(ConfigurationDAO configDao) {
            this.configDao = configDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            configDao.deleteAllEntries();
            return null;
        }
    }

    private static class InsertStudentAsyncTask extends AsyncTask<StudentEntity, Void, Void> {

        private StudentDAO studDao;

        private InsertStudentAsyncTask(StudentDAO studDao) {
            this.studDao = studDao;
        }

        @Override
        protected Void doInBackground(StudentEntity... studentEntities) {
            studDao.insert(studentEntities[0]);
            return null;
        }
    }

    private static class UpdateStudentAsyncTask extends AsyncTask<StudentEntity, Void, Void> {

        private StudentDAO studDao;

        private UpdateStudentAsyncTask(StudentDAO studDao) {
            this.studDao = studDao;
        }

        @Override
        protected Void doInBackground(StudentEntity... studentEntities) {
            studDao.update(studentEntities[0]);
            return null;
        }
    }

    private static class DeleteAllStudentsAsyncTask extends AsyncTask<Void, Void, Void> {

        private StudentDAO studDao;

        private DeleteAllStudentsAsyncTask(StudentDAO studDao) {
            this.studDao = studDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            studDao.deleteAllEntries();
            return null;
        }
    }



    // Methoden für API-Calls (Retrofit)

    //Konfigurationen laden
    public void fetchConfig () {
        Call<ConfigurationAPI> call = downloadJSONRetrofit.getConfiguration();

        //execute on background-thread
        call.enqueue(new Callback<ConfigurationAPI>() {
            @Override
            public void onResponse(Call<ConfigurationAPI> call, Response<ConfigurationAPI> response) {
                //wenn HTTP-Request nicht erfolgreich:
                if (!response.isSuccessful()) {
                    Log.i("TEST ErrorResponse: ", String.valueOf(response.code()));
                    return;
                }

                //wenn HTTP-Request erfolgreich:
                // Konfiguration extrahieren
                ConfigurationAPI configurationAPI = response.body();
                Configuration configuration = configurationAPI.getConfig();

                // Alte Datenbankeinträge löschen
                deleteAllConfigs();

                // Neue Konfiguration in Datenbank speichern
                insertConfig(new ConfigurationEntity(configuration.getPassword(), configuration.getMaxTime()));
            }

            @Override
            public void onFailure(Call<ConfigurationAPI> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("TEST Error", t.getMessage() + "Error");
            }
        });
    }

    public void fetchGroups () {
        Log.i("DataRepository", "fetchGroups()");
        Call<GroupsAPI> call = downloadJSONRetrofit.getGroups();
        Log.i("DataRepository", "Call<GroupsAPI> call = downloadJSONRetrofit.getGroups();");

        //execute on background-thread
        call.enqueue(new Callback<GroupsAPI>() {
            @Override
            public void onResponse(Call<GroupsAPI> call, Response<GroupsAPI> response) {
                Log.i("DataRepository", "onResponse()");
                //wenn HTTP-Request nicht erfolgreich:
                if (!response.isSuccessful()) {
                    Log.i("DataRepository", "response not successful");
                    Log.i("TEST ErrorResponse: ", String.valueOf(response.code()));
                    return;
                }

                //wenn HTTP-Request erfolgreich:
                Log.i("DataRepository", "response successfull");
                GroupsAPI groupsAPI = response.body();
                Log.i("DataRepository", "response.body(): " + response.body());

                // GruppenListe in groupList-LiveData speichern
                groupList.setValue(new ArrayList<>(groupsAPI.getGroupList()));
                Log.i("DataRepository", "groupList: " + groupList.toString());
            }

            @Override
            public void onFailure(Call<GroupsAPI> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("TEST Error", t.getMessage() + "Error");
                Log.i("DataRepository", "onFailure()");
            }
        });
    }

    //changeGroup
    public void changeGroupName (final Group group) {

        Call<GroupAPI> call = downloadJSONRetrofit.changeGroup(group.getGroupId(), group);

        call.enqueue(new Callback<GroupAPI>() {
            @Override
            public void onResponse(Call<GroupAPI> call, Response<GroupAPI> response) {

                if (!response.isSuccessful()) {
                    Log.i("TEST ErrorResponse: ", String.valueOf(response.code()));
                    return;
                }

                // Gruppe aus Response holen
                GroupAPI groupResponseAPI = response.body();
                Group groupResponse = groupResponseAPI.getGroup();
                Log.i("DataRepository", "changeGroupName() response: " + response.toString());
                Log.i("DataRepository", "changeGroupName() response.body(): " + response.body().toString());
                Log.i("DataRepository", "changeGroupName()  studResponse: " + groupResponse.toString());

                // Gruppenname ändern
                changeGroupLiveData(groupResponse);
            }

            @Override
            public void onFailure(Call<GroupAPI> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("TEST Error", t.getMessage() + "Error");
            }
        });
    }

    public boolean groupsAreFetched() {
        Log.i("DataRepository", "groupList != null: " + (groupList != null));
        return groupList != null;
    }

    private void addDetailsToTask(Task task) {
        for (Task tempTask : taskList.getValue()) {
            if (task.getId() == tempTask.getId()) {
                tempTask.setPassword(task.getPassword());
                tempTask.setFieldList(task.getFieldList());
                break;
            }
        }
    }

//Studierenden löschen
    public void deleteStudent (int studentID) {
        Call<Void> call = downloadJSONRetrofit.deleteStudent(studentID);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("TEST Response", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("TEST Error", t.getMessage() + "Error");
            }
        });
    }

//Studierenden hinzufügen
    public void sendStudent(Student student){
        Call<StudentAPI> call = downloadJSONRetrofit.sendStudent(student);

        call.enqueue(new Callback<StudentAPI>() {
            @Override
            public void onResponse(Call<StudentAPI> call, Response<StudentAPI> response) {

                if (!response.isSuccessful()) {
                    Log.i("ErrorRes:GroRecViewAdap", String.valueOf(response.code()));
                    return;
                }

                // Student aus Response holen
                StudentAPI studResponseAPI = response.body();
                Student studResponse = studResponseAPI.getStudent();
                Log.i("DataRepository", "sendStudent() response: " + response.toString());
                Log.i("DataRepository", "sendStudent() response.body(): " + response.body().toString());
                Log.i("DataRepository", "sendStudent() studResponse: " + studResponse.toString());

                // Student zur Gruppe hinzufügen
                addStudentLiveData(studResponse);

            }

            @Override
            public void onFailure(Call<StudentAPI> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("Error GroupRecViewAdap", t.getMessage());
            }
        });
    }

//Daten eines Studierenden ändern
    public void changeStudent(Student student) {

        Call<StudentAPI> call = downloadJSONRetrofit.changeStudent(student.getStudentId(), student);

        call.enqueue(new Callback<StudentAPI>() {
            @Override
            public void onResponse(Call<StudentAPI> call, Response<StudentAPI> response) {

                if (!response.isSuccessful()) {
                    Log.i("TEST ErrorResponse: ", String.valueOf(response.code()));
                    return;
                }

                // Student aus Response holen
                StudentAPI studResponseAPI = response.body();
                Student studResponse = studResponseAPI.getStudent();
                Log.i("DataRepository", "sendStudent() response: " + response.toString());
                Log.i("DataRepository", "sendStudent() response.body(): " + response.body().toString());
                Log.i("DataRepository", "sendStudent() studResponse: " + studResponse.toString());

                // Student in Gruppe ändern
                changeStudentLiveData(studResponse);
            }

            @Override
            public void onFailure(Call<StudentAPI> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("TEST Error", t.getMessage() + "Error");
            }
        });
    }

    //Tasks der eigenen Gruppe laden
    public void fetchTasks(final int groupID) {
        Call<TasksAPI> call = downloadJSONRetrofit.getTasks(groupID);

        //execute on background-thread
        call.enqueue(new Callback<TasksAPI>() {
            @Override
            public void onResponse(Call<TasksAPI> call, Response<TasksAPI> response) {

                //wenn HTTP-Request nicht erfolgreich:
                if (!response.isSuccessful()) {
                    Log.i("TEST Error get Tasks ", String.valueOf(response.code()));
                    return;
                }

                //wenn HTTP-Request erfolgreich:
                TasksAPI taskAPI = response.body();
                // ArrayList mit Tasks in taskList-LiveData speichern
                ArrayList<Task> tempTaskList = new ArrayList<>(taskAPI.getTaskList());
                for (Task task: tempTaskList) {
                    fetchTask(groupID, task.getId());
                }
                taskList.setValue(tempTaskList);
                Log.i("DataRepository", "fetchTasks() taskList: " + taskList.getValue().toString());
            }

            @Override
            public void onFailure(Call<TasksAPI> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("TEST Error", t.getMessage() + "Error");
            }
        });
    }

    // Eine bestimmten Task/Aufgabe einer Gruppe laden
    public void fetchTask(int groupID, int taskID) {
        Call<TaskAPI> call = downloadJSONRetrofit.getTask(taskID, groupID);

        //execute on background-thread
        call.enqueue(new Callback<TaskAPI>() {
            @Override
            public void onResponse(Call<TaskAPI> call, Response<TaskAPI> response) {

                //wenn HTTP-Request nicht erfolgreich:
                if (!response.isSuccessful()) {
                    Log.i("DataRepository ", "fetchTask() Response unsuccessfull: " + String.valueOf(response.code()));
                    return;
                }

                //wenn HTTP-Request erfolgreich:
                TaskAPI taskAPI = response.body();
                Task responseTask = taskAPI.getTask();
                Log.i("DataRepository", "fetchTask() response.body(): " + response.body());

                // Taskdetails in existierendem Task-Objekt speichern
                addDetailsToTask(responseTask);
            }

            @Override
            public void onFailure(Call<TaskAPI> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("DataRepository", "fetchTask() onFailure(): " + t.getMessage());
            }
        });
    }

    // Lösung(en) einer Aufgabe einer Gruppe abschicken
    public void sendAnswer (final Answer answer) {
        Call<AnswerAPI> call = downloadJSONRetrofit.sendAnswer(answer);

        //execute on background-thread
        call.enqueue(new Callback<AnswerAPI>() {
            @Override
            public void onResponse(Call<AnswerAPI> call, Response<AnswerAPI> response) {

                //wenn HTTP-Request nicht erfolgreich:
                if (!response.isSuccessful()) {
                    Log.i("DataRepository ", "fetchTask() Response unsuccessfull: " + String.valueOf(response.code()));
                    return;
                }

                //wenn HTTP-Request erfolgreich:
                Answer answer = response.body().getAnswer();
                Log.i("DataRepository", "fetchTask() response.body(): " + response.body());
            }

            @Override
            public void onFailure(Call<AnswerAPI> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("DataRepository", "fetchTask() onFailure(): " + t.getMessage());
            }
        });
    }

    //Eigene Gruppe laden
    public void fetchGroup(final int groupID) {
        Call<GroupAPI> call = downloadJSONRetrofit.getGroup(groupID);

        //execute on background-thread
        call.enqueue(new Callback<GroupAPI>() {
            @Override
            public void onResponse(Call<GroupAPI> call, Response<GroupAPI> response) {

                //wenn HTTP-Request nicht erfolgreich:
                if (!response.isSuccessful()) {
                    Log.i("TEST Error: getGroup ", String.valueOf(response.code()));
                    return;
                }

                //wenn HTTP-Request erfolgreich:
                GroupAPI groupAPI = response.body();
                List<Student> students = groupAPI.getGroup().getStudentList();
                // Liste in studentList-LiveData speichern
                studentList.setValue(new ArrayList<>(students));
                // Eigene Student-ID in Datenbank nachtragen
                while (!studentIdIsCorrect()) {
                    correctStudentId();
                }
            }

            @Override
            public void onFailure(Call<GroupAPI> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("TEST Error", t.getMessage() + "Error");
            }
        });
    }
}
