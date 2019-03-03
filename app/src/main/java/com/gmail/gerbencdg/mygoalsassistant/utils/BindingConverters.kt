package com.gmail.gerbencdg.mygoalsassistant.utils

import android.view.View
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal
import kotlin.math.roundToInt

object BindingConverters {

    @JvmStatic
    fun listEmptyToVisibility(empty: Boolean): Int {
        return if (empty) View.VISIBLE else View.GONE
    }

    @JvmStatic
    fun filteredListNotEmptyToVisibility(list: List<Goal>?, filterPredicate: (Goal) -> Boolean): Int {
        val isNotEmpty = list?.any(filterPredicate) ?: false
        return if (isNotEmpty) View.VISIBLE else View.GONE
    }

    @JvmStatic
    fun goalToProgressBarValue(goal: Goal): Int {
        if (goal.resultOfToday == null) {
            return 0
        }

        return when (goal.type) {
            Goal.GoalType.BINARY -> {
                goal.resultOfToday?.run {
                    if (boolResult) 1
                    else  0
                }
                0
            }
            Goal.GoalType.NUMBER -> {
                goal.resultOfToday?.run {
                    numberResult.roundToInt()
                }
                0
            }
        }
    }

    @JvmStatic
    fun goalToProgressBarMax(goal: Goal) =
        when (goal.type) {
            Goal.GoalType.BINARY -> 1
            Goal.GoalType.NUMBER -> goal.aimedNumberResult.toInt()
        }
}