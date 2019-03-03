package com.gmail.gerbencdg.mygoalsassistant.data

import androidx.lifecycle.LiveData
import com.gmail.gerbencdg.mygoalsassistant.data.room.AppDatabase
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal.GoalType.BINARY
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal.GoalType.NUMBER
import com.gmail.gerbencdg.mygoalsassistant.model.entities.GoalResult
import com.gmail.gerbencdg.mygoalsassistant.utils.DateUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.*

/**
 * The GoalRepository allows to save and fetch data, hiding the data source with which is communicated
 * (This could be a Database, a server, a file, ...)
 */
class GoalRepository {

    private val database = AppDatabase.getInstance()

    fun getGoal(goalId: Int): LiveData<Goal> = database.goalDao().getGoal(goalId)
    fun getGoals() = database.goalDao().getGoals()
    fun getGoalResultOfTodayForGoal(goalId: Int) = database.goalResultDao().getGoalResultOfToday(goalId)
    fun getGoalWithResults(goalId: Int) = database.goalWithResultsDao().getGoalWithResults(goalId)

    fun insertGoal(goal: Goal) {

        goal.results.forEach { it.goalId = goal.id }

        GlobalScope.async {
            database.goalDao().insertGoal(goal)
            database.goalResultDao().insertResults(goal.results)
        }
    }

    fun insertEmptyResultForTodayIfNotExisting(goalId: Int) {
        GlobalScope.async {
            val goalOfToday = database.goalResultDao().getNullableGoalResultOfToday(goalId)
            if (goalOfToday == null) {
                database.goalResultDao()
                    .insertResult(GoalResult(DateUtils.atStartOfDay(), goalId = goalId))
            }
        }
    }


    /*fun insertStubGoals() {

        insertGoal(
            Goal(
                "Drink water",
                "Drink atleast 5 glasses of water per day",
                Goal.GoalFrequency.DAILY,
                BINARY,
                results = arrayListOf(
                    GoalResult(DateUtils.from(2019, Calendar.JANUARY, 27), true),
                    GoalResult(DateUtils.from(2019, Calendar.JANUARY, 28), false),
                    GoalResult(DateUtils.from(2019, Calendar.JANUARY, 29), false),
                    GoalResult(DateUtils.from(2019, Calendar.JANUARY, 30), true)
                )
            )
        )

        insertGoal(
            Goal(
                "Sleep more",
                "Sleep at minimum for 8 hours per night",
                Goal.GoalFrequency.DAILY,
                BINARY,
                results = arrayListOf(
                    GoalResult(DateUtils.from(2019, Calendar.JANUARY, 25), false),
                    GoalResult(DateUtils.from(2019, Calendar.JANUARY, 26), true),
                    GoalResult(DateUtils.from(2019, Calendar.JANUARY, 29), true),
                    GoalResult(DateUtils.from(2019, Calendar.JANUARY, 30), true)
                )
            )
        )
    }
*/


    //TODO remove
    fun addStubGoalResult(goalType: Goal.GoalType, goalId: Int) {

        val calendar = Calendar.getInstance().apply {
            timeInMillis = DateUtils.atStartOfDay()
            add(Calendar.DAY_OF_MONTH, -(Random().nextInt(20)))
        }

        val newResult: GoalResult
        newResult = when (goalType) {
            BINARY -> GoalResult(
                calendar.timeInMillis,
                boolResult = Random().nextBoolean(), goalId = goalId
            )

            NUMBER -> GoalResult(
                calendar.timeInMillis,
                numberResult = Random().nextInt(10) + 1.toDouble(), goalId = goalId
            )
        }

        GlobalScope.async {
            database.goalResultDao().insertResult(newResult)
        }
    }

    fun updateGoalResult(goalResult: GoalResult) {
        GlobalScope.async {
            database.goalResultDao().insertResult(goalResult)
        }
    }
}