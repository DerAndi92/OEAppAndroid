package de.caroliwo.hawoe_rallye.Data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import de.caroliwo.hawoe_rallye.DownloadJSONRetrofit;
import de.caroliwo.hawoe_rallye.Group;
import de.caroliwo.hawoe_rallye.GroupsAPI;
import de.caroliwo.hawoe_rallye.Retrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepository {

    // Variablen für Data-Access-Objects für Datenbank
    private ConfigurationDAO configDao;
    private StudentDAO studDao;

    // Variablen für LiveData-Datensätze aus Datenbank
    private LiveData<ConfigurationEntity> configEntity;
    private LiveData<StudentEntity> studentEntity;
    private MutableLiveData<ArrayList<Group>> groupList;

    // Variablen für API-Calls an Web-Interface
    private DownloadJSONRetrofit downloadJSONRetrofit;
    //private ArrayList<Group> groupList;

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

        // LiveData-variablen zuweisen
        configEntity = configDao.getConfigLiveData();
        studentEntity = studDao.getStudentLiveData();
        groupList = new MutableLiveData<>();

        // Retrofit instanziieren
        Retrofit retrofitClass = new Retrofit();
        downloadJSONRetrofit = retrofitClass.createlogInterceptor();


    }



    // Methoden welche LiveData liefern

    public LiveData<ConfigurationEntity> getConfigLiveData() {
        return configEntity;
    }

    public LiveData<StudentEntity> getStudentLiveData() {
        return studentEntity;
    }



    // Methoden für direkte Datenabfrage auf dem Main-Thread

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

    public void fetchGroups () {
        Log.i("DataRepository", "fetchGroups()");
        Call<GroupsAPI> call = downloadJSONRetrofit.getGroups();
        Log.i("DataRepository", "Call<GroupsAPI> call = downloadJSONRetrofit.getGroups();");
        //final MutableLiveData<ArrayList<Group>> groupList = new MutableLiveData<>();

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

    public boolean groupsAreFetched() {
        Log.i("DataRepository", "groupList != null: " + (groupList != null));
        return groupList != null;
    }

    /*public Group getGroup(int groupID) {
        for (Group group: groupList) {
            if (groupID == group.getGroupId()) return group;
        }
        return null;
    }*/

    public LiveData<ArrayList<Group>> getGroupListLiveData() {
        return groupList;
    }

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



}
