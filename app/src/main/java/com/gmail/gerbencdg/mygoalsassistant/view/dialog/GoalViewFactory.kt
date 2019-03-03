package com.gmail.gerbencdg.mygoalsassistant.view.dialog

import android.content.Context
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal
import com.gmail.gerbencdg.mygoalsassistant.view.*
import com.gmail.gerbencdg.mygoalsassistant.view.GoalCreationPage.PageName.*

class GoalViewFactory {

    companion object {
        fun create(context: Context, goalCreationPageName: GoalCreationPage.PageName, goal : Goal): GoalCreationPage =
            when (goalCreationPageName) {
                TITLE -> TitleGoalCreationPage(context, goal)
                DESCRIPTION -> DescriptionGoalCreationPage(context, goal)
                FREQUENCY -> FrequencyGoalCreationPage(context, goal)
                DURATION -> DurationGoalCreationPage(context, goal)
            }
    }

}