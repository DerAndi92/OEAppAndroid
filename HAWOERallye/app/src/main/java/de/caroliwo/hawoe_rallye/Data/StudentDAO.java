package de.caroliwo.hawoe_rallye.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

//Datenbankaktionen für Student

@Dao
public interface StudentDAO {

    @Insert
    void insert(StudentEntity studEntity);

    @Update
    void update(StudentEntity studEntity);

    @Delete
    void delete(StudentEntity studEntity);

    @Query("SELECT * FROM student_table WHERE id IN (SELECT MAX(id) FROM student_table)")
    StudentEntity getStudent();

    @Query("SELECT * FROM student_table WHERE id IN (SELECT MAX(id) FROM student_table)")
    LiveData<StudentEntity> getStudentLiveData();

    @Query("SELECT COUNT(*) FROM student_table")
    LiveData<Integer> countEntries();

    @Query("DELETE FROM student_table")
    void deleteAllEntries();
}
