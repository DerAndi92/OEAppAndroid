package de.caroliwo.hawoe_rallye.Data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.ExecutionException;

public class DataViewModel extends AndroidViewModel {

    // Referenz auf Repository für Datenzugriff
    private DataRepository repository;

    // Variablen für Datensätze aus Repository
    private ConfigurationEntity configEntity;
    private StudentEntity studEntity;

    public DataViewModel(@NonNull Application application) {

        // Application-Kontext an Superklasse weitergeben
        super(application);
        //Log.i("DataViewModel", "1");

        // Repository instanziieren
        repository = new DataRepository(application);
        //Log.i("DataViewModel", "2");

        //  Datensätze aus Repository holen
        try {
            //Log.i("DataViewModel", "3");
            configEntity = repository.getConfig();
            //Log.i("DataViewModel", "4");
            studEntity = repository.getStudent();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ConfigurationEntity getConfig() { return configEntity; }

    public StudentEntity getStudent() {
        return studEntity;
    }

    public void insertConfig(ConfigurationEntity entity) {
        repository.insertConfig(entity);
    }

    public void updateConfig(ConfigurationEntity entity) {
        repository.updateConfig(entity);
    }

    public void deleteAllConfigs() {
        repository.deleteAllConfigs();
    }

    public void insertStudent(StudentEntity entity) {
        repository.insertStudent(entity);
    }

    public void updateStudent(StudentEntity entity) {
        repository.updateStudent(entity);
    }

    public void deleteAllStudents() {
        repository.deleteAllStudents();
    }
}
