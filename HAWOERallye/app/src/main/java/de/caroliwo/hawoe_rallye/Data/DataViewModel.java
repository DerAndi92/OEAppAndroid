package de.caroliwo.hawoe_rallye.Data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import de.caroliwo.hawoe_rallye.Group;

public class DataViewModel extends AndroidViewModel {

    // Referenz auf Repository für Datenzugriff
    private DataRepository repository;

    // Variablen für LiveData-Datensätze aus Repository
    private LiveData<ConfigurationEntity> configEntity;
    private LiveData<StudentEntity> studEntity;
    private LiveData<ArrayList<Group>> groupList;


    public DataViewModel(@NonNull Application application) {

        // Application-Kontext an Superklasse weitergeben
        super(application);
        Log.i("DataViewModel", "1");

        // Repository instanziieren
        repository = new DataRepository(application);
        Log.i("DataViewModel", "2");

        // LiveData-Datensätze zuweisen
        configEntity = repository.getConfigLiveData();
        studEntity = repository.getStudentLiveData();
        groupList = repository.getGroupListLiveData();

    }

    // Methoden für API-Calls an's Web-Interface
    public void fetchGroups() {
        repository.fetchGroups();
    }

    public boolean groupsAreFetched() {
        return repository.groupsAreFetched();
    }

    /*public Group getGroup(int groupID) {
        return repository.getGroup(groupID);
    }*/

    public LiveData<ArrayList<Group>> getGroupListLiveData () {
        return groupList;
    }

    public void deleteStudent(int studentID) {
        repository.deleteStudent(studentID);
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
