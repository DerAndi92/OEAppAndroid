package de.caroliwo.hawoe_rallye.Data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.ExecutionException;

public class DataViewModel extends AndroidViewModel {

    // Referenz auf Repository für Datenzugriff
    private DataRepository repository;

    // Variablen für LiveData-Datensätze aus Repository
    private LiveData<ConfigurationEntity> configEntity;
    private LiveData<StudentEntity> studEntity;

    public DataViewModel(@NonNull Application application) {

        // Application-Kontext an Superklasse weitergeben
        super(application);
        //Log.i("DataViewModel", "1");

        // Repository instanziieren
        repository = new DataRepository(application);
        //Log.i("DataViewModel", "2");

        // LiveData-Datensätze zuweisen
        configEntity = repository.getConfigLiveData();
        studEntity = repository.getStudentLiveData();

    }

    // Methoden für LiveData-Stream
    public LiveData<ConfigurationEntity> getConfigLiveData() {
        return configEntity;
    }

    public LiveData<StudentEntity> getStudentLiveData() { return studEntity; }

    // Methoden für direkte Datenabfrage auf dem Main-Thread

    public StudentEntity getStudent() {

        return repository.getStudent();
    }

    public ConfigurationEntity getConfig() {

        return repository.getConfig();
    }

    // Andere Datenbankoperationen

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
