package com.gmail.gerbencdg.mygoalsassistant.view

import android.content.Context
import android.view.View
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal

abstract class GoalCreationPage(
    protected val context: Context,
    val title: String,
    protected val goal: Goal,
    val subTitle: String = ""
) {
    abstract val contentView: View
    abstract fun validateInfo(): Boolean
    abstract fun updateGoalInfo() : Goal

    enum class PageName {
        TITLE, DESCRIPTION, FREQUENCY, DURATION
    }

}