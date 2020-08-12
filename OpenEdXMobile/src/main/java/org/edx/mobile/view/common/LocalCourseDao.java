package org.edx.mobile.view.common;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LocalCourseDao {
    @Query("SELECT * FROM localcourse")
    List<LocalCourse> getAll();

    @Query("SELECT * FROM localcourse WHERE uid IN (:userIds)")
    List<LocalCourse> loadAllByIds(int[] userIds);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LocalCourse users);


    @Query("SELECT * FROM localcourse WHERE name LIKE :search  LIMIT 1")
    List<LocalCourse> findUserWithName(String search);


    @Delete
    void delete(LocalCourse user);

    @Query("DELETE FROM localcourse")
    public void nukeTable();

}
