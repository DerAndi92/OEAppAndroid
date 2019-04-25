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
    private MutableLiveData<Boolean> correctPasswordLiveData;

    // Variablen für API-Calls an Web-Interface
    private DownloadJSONRetrofit downloadJSONRetrofit;


    // Konstruktor
    public DataRepository(Application application) {

        // Instanz der Datenbank holen
        Database database = Database.getInstance(application);

        // Data-Access-Objects zuweisen
        configDao = database.configDao();
        studDao = database.studDao();

        // LiveData-Variablen zuweisen
        configEntity = configDao.getConfigLiveData();
        studentEntity = studDao.getStudentLiveData();
        groupList = new MutableLiveData<>();
        taskList = new MutableLiveData<>();
        studentList = new MutableLiveData<>();
        correctPasswordLiveData = new MutableLiveData<>();

        // Retrofit instanziieren
        Retrofit retrofitClass = new Retrofit();
        downloadJSONRetrofit = retrofitClass.createlogInterceptor(application);
    }

    // Methoden um LiveData von API-Calls lokal zu ändern
    public void addStudentLiveData(Student student) {
        ArrayList<Student> tempList = new ArrayList<>();
        if (this.studentList.getValue() != null) {
            tempList = this.studentList.getValue();
        }
        tempList.add(student);
        this.studentList.setValue(tempList);
    }

    public void removeStudentLiveData(Student student) {
        ArrayList<Student> tempList = new ArrayList<Student>(this.studentList.getValue());
        tempList.remove(student);
        this.studentList.setValue(tempList);
    }

    public void changeStudentLiveData(Student newStudent) {
        ArrayList<Student> tempList = new ArrayList<Student>(this.studentList.getValue());
        for (Student student : tempList) {
            if (student.getStudentId() == newStudent.getStudentId()) removeStudentLiveData(student);
        }
        addStudentLiveData(newStudent);
    }

    public void addGroupLiveData(Group group) {
        ArrayList<Group> tempList = new ArrayList<>();
        if (this.groupList.getValue() != null) {
            tempList = this.groupList.getValue();
        }
        tempList.add(group);
        this.groupList.setValue(tempList);
    }

    public void removeGroupLiveData(Group group) {
        ArrayList<Group> tempList = new ArrayList<>(this.groupList.getValue());
        tempList.remove(group);
        this.groupList.setValue(tempList);
    }

    public void changeGroupLiveData(Group newGroup) {
        ArrayList<Group> tempList = new ArrayList<>(this.groupList.getValue());
        for (Group group : tempList) {
            if (group.getGroupId() == newGroup.getGroupId()) removeGroupLiveData(group);
        }
        addGroupLiveData(newGroup);
    }

    public void changeTaskLiveData(Task newTask) {
        ArrayList<Task> tempList = new ArrayList<>(this.taskList.getValue());
        for (Task task : tempList) {
            if (task.getId() == newTask.getId()) /*removeTaskLiveData(task);*/ {
                tempList.set(tempList.indexOf(task), newTask);
            }
        }
        this.taskList.setValue(tempList);
    }

    public void removeTaskLiveData(Task task) {
        ArrayList<Task> tempList = new ArrayList<>(this.taskList.getValue());
        tempList.remove(task);
        this.taskList.setValue(tempList);
    }

    public void addTaskLiveData(Task task) {
        ArrayList<Task> tempList = new ArrayList<>();
        if (this.taskList.getValue() != null) {
            tempList = this.taskList.getValue();
        }
        tempList.add(task);
        this.taskList.setValue(tempList);
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

    private boolean studentIdIsCorrect() {
        return getStudent().getStudentId() != -1;
    }

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

    public LiveData<ArrayList<Student>> getStudentListLiveData() {
        return studentList;
    }

    public LiveData<Boolean> getCorrectPasswordLiveData() {
        return correctPasswordLiveData;
    }


    // Methoden für direkte Datenbankabfrage auf dem Main-Thread

    public StudentEntity getStudent() {
        return studDao.getStudent();
    }

    public ConfigurationEntity getConfig() {
        return configDao.getConfig();
    }


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
    public void fetchConfig() {
        Call<ConfigurationAPI> call = downloadJSONRetrofit.getConfiguration();

        //execute on background-thread
        call.enqueue(new Callback<ConfigurationAPI>() {
            @Override
            public void onResponse(Call<ConfigurationAPI> call, Response<ConfigurationAPI> response) {
                //wenn HTTP-Request nicht erfolgreich:
                if (!response.isSuccessful()) {
                    Log.i("loadConfig Error: ", String.valueOf(response.code()));
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
                Log.i("loadConfig Failure: ", t.getMessage());
            }
        });
    }

    public void fetchGroups() {
        Call<GroupsAPI> call = downloadJSONRetrofit.getGroups();

        //execute on background-thread
        call.enqueue(new Callback<GroupsAPI>() {
            @Override
            public void onResponse(Call<GroupsAPI> call, Response<GroupsAPI> response) {
                //wenn HTTP-Request nicht erfolgreich:
                if (!response.isSuccessful()) {
                    Log.i("fetchGroups Error: ", String.valueOf(response.code()));
                    return;
                }

                //wenn HTTP-Request erfolgreich:
                GroupsAPI groupsAPI = response.body();

                // GruppenListe in groupList-LiveData speichern
                groupList.setValue(new ArrayList<>(groupsAPI.getGroupList()));
            }

            @Override
            public void onFailure(Call<GroupsAPI> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("fetchGroups Failure: ", t.getMessage());
            }
        });
    }

    //changeGroup
    public void changeGroupName(final Group group) {

        Call<GroupAPI> call = downloadJSONRetrofit.changeGroup(group.getGroupId(), group);

        call.enqueue(new Callback<GroupAPI>() {
            @Override
            public void onResponse(Call<GroupAPI> call, Response<GroupAPI> response) {

                if (!response.isSuccessful()) {
                    Log.i("changeGroupName Error: ", String.valueOf(response.code()));
                    return;
                }

                // Gruppe aus Response holen
                GroupAPI groupResponseAPI = response.body();
                Group groupResponse = groupResponseAPI.getGroup();

                // Gruppenname ändern
                changeGroupLiveData(groupResponse);
            }

            @Override
            public void onFailure(Call<GroupAPI> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("changeGroupName Fail: ", t.getMessage());
            }
        });
    }

    public boolean groupsAreFetched() {
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
    public void deleteStudent(int studentID) {
        Call<Void> call = downloadJSONRetrofit.deleteStudent(studentID);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i("deleteStudent Error: ", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("deleteStudent Failure: ", t.getMessage());
            }
        });
    }

    //Studierenden hinzufügen
    public void sendStudent(Student student) {
        Call<StudentAPI> call = downloadJSONRetrofit.sendStudent(student);

        call.enqueue(new Callback<StudentAPI>() {
            @Override
            public void onResponse(Call<StudentAPI> call, Response<StudentAPI> response) {

                if (!response.isSuccessful()) {
                    Log.i("sendStudent Error: ", String.valueOf(response.code()));
                    return;
                }

                // Student aus Response holen
                StudentAPI studResponseAPI = response.body();
                Student studResponse = studResponseAPI.getStudent();

                // Student zur Gruppe hinzufügen
                addStudentLiveData(studResponse);

            }

            @Override
            public void onFailure(Call<StudentAPI> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("sendStudent Failure: ", t.getMessage());
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
                    Log.i("changeStudent Error: ", String.valueOf(response.code()));
                    return;
                }

                // Student aus Response holen
                StudentAPI studResponseAPI = response.body();
                Student studResponse = studResponseAPI.getStudent();

                // Student in Gruppe ändern
                changeStudentLiveData(studResponse);
            }

            @Override
            public void onFailure(Call<StudentAPI> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("changeStudent Failure: ", t.getMessage());
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
                    Log.i("fetchTasks Error: ", String.valueOf(response.code()));
                    return;
                }

                //wenn HTTP-Request erfolgreich:
                TasksAPI taskAPI = response.body();
                // ArrayList mit Tasks in taskList-LiveData speichern
                ArrayList<Task> tempTaskList = new ArrayList<>(taskAPI.getTaskList());
                for (Task task : tempTaskList) {
                    fetchTask(groupID, task.getId());
                }
                taskList.setValue(tempTaskList);
            }

            @Override
            public void onFailure(Call<TasksAPI> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("fetchTasks Failure: ", t.getMessage());
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
                    Log.i("fetchTask Error: ", String.valueOf(response.code()));
                    return;
                }

                //wenn HTTP-Request erfolgreich:
                TaskAPI taskAPI = response.body();
                Task responseTask = taskAPI.getTask();

                // Taskdetails in existierendem Task-Objekt speichern
                addDetailsToTask(responseTask);
            }

            @Override
            public void onFailure(Call<TaskAPI> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("fetchTask Failure: ", t.getMessage());
            }
        });
    }

    // Lösung(en) einer Aufgabe einer Gruppe abschicken
    public void sendAnswer(final Answer answer) {
        correctPasswordLiveData.setValue(null);
        Call<AnswerAPI> call = downloadJSONRetrofit.sendAnswer(answer);

        // execute on background-thread
        call.enqueue(new Callback<AnswerAPI>() {
            @Override
            public void onResponse(Call<AnswerAPI> call, Response<AnswerAPI> response) {

                //wenn HTTP-Request nicht erfolgreich:
                if (!response.isSuccessful()) {
                    Log.i("sendAnswer Error: ", String.valueOf(response.code()));
                    correctPasswordLiveData.setValue(false);
                    return;
                }

                //wenn HTTP-Request erfolgreich:
                Answer answer = response.body().getAnswer();
                correctPasswordLiveData.setValue(true);
            }

            @Override
            public void onFailure(Call<AnswerAPI> call, Throwable t) {
                // something went completely south (like no internet connection)
                Log.i("sendAnswer Failure: ", t.getMessage());
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
                    Log.i("fetchGroup Error: ", String.valueOf(response.code()));
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
                Log.i("fetchGroup Failure: ", t.getMessage());
            }
        });
    }
}
