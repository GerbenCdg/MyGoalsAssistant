package com.gmail.gerbencdg.mygoalsassistant.data.room.typeconverter

import androidx.room.TypeConverter
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal

class GoalTypeTypeConverter {

    @TypeConverter
    fun intToGoalType(int: Int): Goal.GoalType {
        return Goal.GoalType.values()[int]
    }

    @TypeConverter
    fun goalTypeToInt(goalType: Goal.GoalType): Int {
        return Goal.GoalType.values().indexOf(goalType)
    }
}