package de.caroliwo.hawoe_rallye.Data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

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

    @Query("SELECT COUNT(*) FROM student_table")
    int countEntries();

    @Query("DELETE FROM student_table")
    void deleteAllEntries();
}
