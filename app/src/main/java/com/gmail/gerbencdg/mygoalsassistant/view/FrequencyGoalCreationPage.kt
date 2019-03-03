package com.gmail.gerbencdg.mygoalsassistant.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.core.view.get
import com.gmail.gerbencdg.mygoalsassistant.R
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal.GoalFrequency.*

class FrequencyGoalCreationPage(context: Context, goal: Goal) :
    GoalCreationPage(
        context,
        context.getString(R.string.repeat_your_goal),
        goal,
        context.getString(R.string.how_often)
    ) {

    override val contentView: View
    private val radioGroup: RadioGroup
    private val dayOfWeekSpinner: Spinner
    private val dailyRadioButton: RadioButton
    private val weeklyRadioButton: RadioButton
    private val onceRadioButton: RadioButton

    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_new_goal_radiobuttons, null)

        radioGroup = contentView.findViewById(R.id.radioGroup)
        dayOfWeekSpinner = contentView.findViewById(R.id.dayOfWeekSpinner)

        dailyRadioButton = contentView.findViewById(R.id.radioButton1)
        weeklyRadioButton = contentView.findViewById(R.id.radioButton2)
        onceRadioButton = contentView.findViewById(R.id.radioButton3)

        when (goal.frequency) {
            DAILY -> dailyRadioButton.isChecked = true
            WEEKLY -> weeklyRadioButton.isChecked = true
            NO_REPEAT -> onceRadioButton.isChecked = true
        }
    }

    override fun updateGoalInfo() : Goal {
        for (i in 0 until values().size) {
            if ((radioGroup[i] as RadioButton).isChecked) {
                goal.frequency = values()[i]
                break
            }
        }
        return goal
    }

    override fun validateInfo(): Boolean {
        return true
    }

}