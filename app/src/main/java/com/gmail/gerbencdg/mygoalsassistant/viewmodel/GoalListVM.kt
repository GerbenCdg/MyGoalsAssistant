package com.gmail.gerbencdg.mygoalsassistant.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.gmail.gerbencdg.mygoalsassistant.data.GoalRepository
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal
import com.gmail.gerbencdg.mygoalsassistant.model.entities.GoalResult

class GoalListVM(private val goalRepository: GoalRepository = GoalRepository()) : ViewModel() {

    private var goals = MediatorLiveData<List<Goal>>()

    fun getGoals(): LiveData<List<Goal>> = goals

    // These predicates are used by the GoalListAdapters to filter the content of the goals per frequency
    // They are also used by a visibilityConverter to determine whether a textView should appear above the elements
    // Of the recyclerView corresponding to the given filter
    val todayGoalsPredicate : (Goal) -> Boolean = { it.frequency == Goal.GoalFrequency.DAILY}
    val weekGoalsPredicate : (Goal) -> Boolean = { it.frequency == Goal.GoalFrequency.WEEKLY}
    val oneTimeGoalsPredicate : (Goal) -> Boolean = { it.frequency == Goal.GoalFrequency.NO_REPEAT}

    init {
        val liveDataGoals = goalRepository.getGoals()

        goals.addSource(liveDataGoals) { goalsList ->

            for ((index, goal) in goalsList.withIndex()) {

                // For each goal,
                // If there is not yet a goalResult created for that goal on the current day, create it.
                goalRepository.insertEmptyResultForTodayIfNotExisting(goal.id)
                val liveDataGoalResultOfToday = goalRepository.getGoalResultOfTodayForGoal(goal.id)

                // SIDE NOTE : addSource is not adding liveData a second time if it already exists in the sources collection.
                goals.addSource(liveDataGoalResultOfToday) { goalResult ->
                    if (goalResult != null) {
                        // References need to change in order for ListAdapter.DiffCallback to have the expected behaviour
                        goalsList[index] = goal.copy(resultOfToday = goalResult)
                        goals.setValue(goalsList)
                    }
                }
            }
            goals.setValue(goalsList)
        }
    }

    fun insertGoal(goal: Goal) {
        goalRepository.insertGoal(goal)
    }

    fun updateGoalResult(goalResult: GoalResult) {
        goalRepository.updateGoalResult(goalResult)
    }

}