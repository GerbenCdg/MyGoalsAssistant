package com.gmail.gerbencdg.mygoalsassistant.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal

@Dao
interface GoalDao {

    @Query("SELECT * FROM goal")
    fun getGoals() : LiveData<MutableList<Goal>>

    @Query("SELECT * FROM goal WHERE goal.id IS :goalId")
    fun getGoal(goalId: Int) : LiveData<Goal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGoal(goal: Goal)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertGoalResult(goal: Goal, goalResult: GoalResult)

}