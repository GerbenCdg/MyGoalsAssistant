package com.gmail.gerbencdg.mygoalsassistant.viewmodel

import androidx.lifecycle.ViewModel
import com.gmail.gerbencdg.mygoalsassistant.data.GoalRepository
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal

class GoalVM(private val goalRepository: GoalRepository, private val goalId: Int) : ViewModel() {

    //fun getGoalResults() = goalRepository.getGoalResults(goalId)
    val goal = goalRepository.getGoal(goalId)

    fun getGoalWithResults() = goalRepository.getGoalWithResults(goalId)

    fun addStubGoalResult(goalType: Goal.GoalType) = goalRepository.addStubGoalResult(goalType, goalId)
}