package com.gmail.gerbencdg.mygoalsassistant.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import com.gmail.gerbencdg.mygoalsassistant.R
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class TitleGoalCreationPage(context: Context, goal: Goal) :
    GoalCreationPage(
        context,
        context.getString(R.string.add_a_goal),
        goal,
        context.getString(R.string.new_goals_new_life)
    ) {

    override val contentView: View
    private val textInputLayout: TextInputLayout
    private val textInputEditText: TextInputEditText

    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_new_goal_text_input, null)
        with(contentView) {
            textInputLayout = findViewById(R.id.textInputLayout)
            textInputEditText = findViewById(R.id.textInputEditText)
        }

        textInputLayout.hint = context.getString(R.string.give_goal_a_title)
        textInputEditText.setText(goal.name)
    }

    override fun validateInfo(): Boolean {
        if (textInputEditText.text.isNullOrBlank()) {
            textInputLayout.error = context.getString(R.string.goal_name_mandatory_error)

            textInputEditText.addTextChangedListener(validGoalNameTextWatcher)
            return false
        }
        return true
    }

    private val validGoalNameTextWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            if (textInputEditText.text.isNullOrBlank()) {
                textInputLayout.error = context.getString(R.string.goal_name_mandatory_error)
            } else {
                textInputLayout.error = null
                textInputEditText.error = null
            }
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
    }

    override fun updateGoalInfo() : Goal {
        goal.name = textInputEditText.text.toString()
        return goal
    }
}