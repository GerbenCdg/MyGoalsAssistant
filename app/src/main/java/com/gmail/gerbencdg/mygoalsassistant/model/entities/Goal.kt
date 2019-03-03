package com.gmail.gerbencdg.mygoalsassistant.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.gmail.gerbencdg.mygoalsassistant.utils.DateUtils

@Entity(tableName = "goal")
data class Goal(

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "description")
    var description: String?,

    @ColumnInfo(name = "frequency")
    var frequency: Goal.GoalFrequency,

    @ColumnInfo(name = "goal_result_type")
    var type: Goal.GoalType,

    @ColumnInfo(name = "aimed_number_result")
    var aimedNumberResult: Double = 0.toDouble(),

    @ColumnInfo(name = "creation_date")
    var creationDate: Long = DateUtils.atStartOfDay(),

    @ColumnInfo(name = "completion_date")
    var aimedCompletionDate : Long? = 0L,

    @Ignore
    var results : List<GoalResult> = emptyList(),

    @Ignore
    var resultOfToday : GoalResult? = null,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0

) {
    constructor() : this("", "", GoalFrequency.DAILY, GoalType.BINARY)

    enum class GoalFrequency {
        DAILY, WEEKLY, NO_REPEAT
    }

    enum class GoalType{
        BINARY, NUMBER
    }


}