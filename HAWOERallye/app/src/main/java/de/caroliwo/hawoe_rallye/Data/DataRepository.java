package de.caroliwo.hawoe_rallye.Data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.ExecutionException;

public class DataRepository {

    // Variablen für Data-Access-Objects
    private ConfigurationDAO configDao;
    private StudentDAO studDao;

    // Variablen für Datensätze aus Datenbank
    private LiveData<ConfigurationEntity> configEntity;
    private LiveData<StudentEntity> studentEntity;

    // Konstruktor
    public DataRepository(Application application) {

        // Instanz der Datenbank holen
        Database database = Database.getInstance(application);
        //Log.i("DataRepository", "1");

        // Data-Access-Objects zuweisen
        configDao = database.configDao();
        //Log.i("DataRepository", "2");
        studDao = database.studDao();
        //Log.i("DataRepository", "3");

        // LiveData-Entitäten zuweisen
        configEntity = configDao.getConfig();
        studentEntity = studDao.getStudent();


    }

    // Methoden welche LiveData liefern

    public LiveData<ConfigurationEntity> getConfig() {
        return configEntity;
    }

    public LiveData<StudentEntity> getStudent() {
        return studentEntity;
    }

    // Methoden welche AsyncTasks aufrufen

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


    // AsyncTask-Subklassen für Operationen
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

/*    private static class GetConfigAsyncTask extends AsyncTask<Void, Void, ConfigurationEntity> {

        private ConfigurationDAO configDao;

        private GetConfigAsyncTask(ConfigurationDAO configDao) {
            this.configDao = configDao;
        }

        @Override
        protected ConfigurationEntity doInBackground(Void... voids) {
            if (configDao.countEntries() > 0) {
                return configDao.getConfig();
            }
            return null;
        }
    }*/

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

 /*   private static class GetStudentAsyncTask extends AsyncTask<Void, Void, StudentEntity> {

        private StudentDAO studDao;

        private GetStudentAsyncTask(StudentDAO studDao) {
            this.studDao = studDao;
        }

        @Override
        protected StudentEntity doInBackground(Void... voids) {
            if (studDao.countEntries() > 0) {
                return studDao.getStudent();
            }
            return null;
        }
    }*/

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

}
