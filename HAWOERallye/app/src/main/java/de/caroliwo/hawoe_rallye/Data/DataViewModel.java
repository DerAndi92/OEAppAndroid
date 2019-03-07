package de.caroliwo.hawoe_rallye.Data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;

import de.caroliwo.hawoe_rallye.Activities.LoadingActivity;
import de.caroliwo.hawoe_rallye.Group;
import de.caroliwo.hawoe_rallye.Student;
import de.caroliwo.hawoe_rallye.Task;

public class DataViewModel extends AndroidViewModel {

    // Referenz auf Repository für Datenzugriff
    private DataRepository repository;

    // Variablen für LiveData-Datensätze aus Repository
    private LiveData<ConfigurationEntity> configEntity;
    private LiveData<StudentEntity> studEntity;
    private LiveData<ArrayList<Group>> groupList;
    private LiveData<ArrayList<Task>> taskList;
    private LiveData<ArrayList<Task>> taskDetailList;
    private LiveData<ArrayList<Student>> studentList;

//    @Inject
    public DataViewModel(@NonNull Application application/*, DataRepository repository*/) {

        // Application-Kontext an Superklasse weitergeben
        super(application);
        Log.i("DataViewModel", "1");

        // Repository zuweisen
//        this.repository = repository;
        repository = new DataRepository(application);
        Log.i("DataViewModel", "2");

        // LiveData-Datensätze zuweisen
        configEntity = repository.getConfigLiveData();
        studEntity = repository.getStudentLiveData();
        groupList = repository.getGroupListLiveData();
        taskList = repository.getTaskListLiveData();
        taskDetailList = repository.getTaskDetailsLiveData();
        studentList = repository.getStudentListLiveData();

    }


    // LogOut-Methode um eigenen Nutzer auszuloggen
    public void logOut(int studentId) {
        deleteStudent(studentId);
        deleteAllStudents();
        Intent intent = new Intent(getApplication(), LoadingActivity.class);
        getApplication().startActivity(intent);
    }

    // Methode um LiveData lokal zu ändern
    public void addStudent(Student student) { repository.addStudentLiveData(student); }

    public void removeStudent(Student student) {
        Log.i("DataViewModel", "removeStudentLiveData() student: " + student.toString());
        repository.removeStudentLiveData(student);
    }

    // Methoden für API-Calls an's Web-Interface
    public void fetchGroups() { repository.fetchGroups(); }

    public void fetchGroup(int groupID) { repository.fetchGroup(groupID); }

    public void fetchConfig() { repository.fetchConfig(); }

    public boolean groupsAreFetched() { return repository.groupsAreFetched(); }

    //public Group getGroup(int groupID) { return repository.getGroup(groupID); }

    public void deleteStudent(int studentID) { repository.deleteStudent(studentID); }

    public void sendStudent(Student student) { repository.sendStudent(student); }

    public void changeStudent(Student student) { repository.changeStudent(student); }

    public void fetchTasks(int groupID) { repository.fetchTasks(groupID); }

    public void fetchTask(int taskID) { repository.fetchTask(taskID); }



    // Methoden für LiveData-Stream
    public LiveData<ConfigurationEntity> getConfigLiveData() { return configEntity; }

    public LiveData<StudentEntity> getStudentLiveData() { return studEntity; }

    public LiveData<ArrayList<Group>> getGroupListLiveData () { return groupList; }

    public LiveData<ArrayList<Task>> getTaskListLiveData () { return taskList; }

    public LiveData<ArrayList<Student>> getStudentListLiveData() { return studentList; }

    public LiveData<ArrayList<Task>> getTaskDetailsLiveData() { return taskDetailList; }



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
