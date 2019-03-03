package com.gmail.gerbencdg.mygoalsassistant.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.gmail.gerbencdg.mygoalsassistant.model.entities.GoalWithResults

@Dao
interface GoalWithResultsDao {

    @Transaction
    @Query("SELECT * FROM goal WHERE goal.id IN (:goalId)")
    fun getGoalWithResults(goalId : Int) : LiveData<GoalWithResults>

}