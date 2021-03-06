package de.caroliwo.hawoe_rallye.Data;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import android.content.Intent;
import androidx.annotation.NonNull;

import java.util.ArrayList;

import de.caroliwo.hawoe_rallye.Activities.LoadingActivity;
import de.caroliwo.hawoe_rallye.Answer;
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
    private LiveData<ArrayList<Student>> studentList;
    private LiveData<Boolean> correctPasswordLiveData;

    public DataViewModel(@NonNull Application application) {

        // Application-Kontext an Superklasse weitergeben
        super(application);

        // Repository zuweisen
        repository = new DataRepository(application);

        // LiveData-Datensätze zuweisen
        configEntity = repository.getConfigLiveData();
        studEntity = repository.getStudentLiveData();
        groupList = repository.getGroupListLiveData();
        taskList = repository.getTaskListLiveData();
        studentList = repository.getStudentListLiveData();
        correctPasswordLiveData = repository.getCorrectPasswordLiveData();
    }

    // LogOut-Methode um eigenen Nutzer auszuloggen
    public void logOut(int studentId) {
        deleteStudent(studentId);
        deleteAllStudents();
        Intent intent = new Intent(getApplication(), LoadingActivity.class);
        getApplication().startActivity(intent);
    }

    // Methode um LiveData lokal zu ändern
    public void addStudent(Student student) {
        repository.addStudentLiveData(student);
    }

    public void removeStudent(Student student) {
        repository.removeStudentLiveData(student);
    }

    public void changeTask(Task task) {
        repository.changeTaskLiveData(task);
    }

    // Methoden für API-Calls an's Web-Interface
    public void fetchGroups() {
        repository.fetchGroups();
    }

    public void fetchGroup(int groupID) {
        repository.fetchGroup(groupID);
    }

    public void fetchConfig() {
        repository.fetchConfig();
    }

    public void changeGroupName(Group group) {
        repository.changeGroupName(group);
    }

    public boolean groupsAreFetched() {
        return repository.groupsAreFetched();
    }

    public void deleteStudent(int studentID) {
        repository.deleteStudent(studentID);
    }

    public void sendStudent(Student student) {
        repository.sendStudent(student);
    }

    public void changeStudent(Student student) {
        repository.changeStudent(student);
    }

    public void fetchTasks(int groupID) {
        repository.fetchTasks(groupID);
    }

    public void fetchTask(int groupID, int taskID) {
        repository.fetchTask(groupID, taskID);
    }

    public void sendAnswer(Answer answer) {
        repository.sendAnswer(answer);
    }


    // Methoden für LiveData-Stream
    public LiveData<ConfigurationEntity> getConfigLiveData() {
        return configEntity;
    }

    public LiveData<StudentEntity> getStudentLiveData() {
        return studEntity;
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
