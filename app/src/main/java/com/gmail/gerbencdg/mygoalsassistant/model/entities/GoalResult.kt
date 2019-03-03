package com.gmail.gerbencdg.mygoalsassistant.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "goal_result")
@ForeignKey(entity = Goal::class, parentColumns = ["id"], childColumns = ["goal_id"])
data class GoalResult(

    @ColumnInfo(name = "date")
    var date: Long,

    @ColumnInfo(name = "result_bool")
    var boolResult: Boolean = false,

    @ColumnInfo(name = "result_number")
    var numberResult: Double = 0.toDouble(),

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "goal_id")
    var goalId: Int = 0

) : Comparable<GoalResult> {

    companion object {
        const val MILLISECONDS_IN_DAY = 86_400_000
    }

    override fun compareTo(other: GoalResult): Int {
        return (date / MILLISECONDS_IN_DAY).toInt() - (other.date / MILLISECONDS_IN_DAY).toInt()
    }
}