package com.gmail.gerbencdg.mygoalsassistant.data.room.typeconverter

import androidx.room.TypeConverter
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal

class GoalFrequencyTypeConverter {
    @TypeConverter
    fun intToGoalFrequency(int : Int) : Goal.GoalFrequency{
        return Goal.GoalFrequency.values()[int]
    }

    @TypeConverter
    fun fromGoalFrequencyToInt(goalFrequency: Goal.GoalFrequency) : Int {
        return Goal.GoalFrequency.values().indexOf(goalFrequency)
    }
}