package com.gmail.gerbencdg.mygoalsassistant.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal.GoalType.BINARY
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal.GoalType.NUMBER
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.dialog_new_goal_text_input.view.*


class DescriptionGoalCreationPage(context: Context, goal: Goal) :
    GoalCreationPage(
        context, context.getString(com.gmail.gerbencdg.mygoalsassistant.R.string.create_a_goal),
        goal,
        context.getString(com.gmail.gerbencdg.mygoalsassistant.R.string.describe_your_goal)
    ) {
    override val contentView: View

    private val textInputLayout: TextInputLayout
    private val textInputEditText: TextInputEditText
    private val seekbar1: SeekBar
    private var goalNumberValue1 : Double = 0.toDouble()

    init {
        contentView = LayoutInflater.from(context)
            .inflate(com.gmail.gerbencdg.mygoalsassistant.R.layout.dialog_new_goal_text_input, null)
        textInputLayout = contentView.textInputLayout
        textInputEditText = contentView.textInputEditText
        seekbar1 = contentView.seekBar1

        textInputLayout.hint = context.getString(com.gmail.gerbencdg.mygoalsassistant.R.string.describe_your_goal)
        textInputEditText.setText(goal.description)

        val numberMatchRegex = "^\\d*[0-9](|.\\d*[0-9]|,\\d*[0-9])?\$".toRegex()
        val digitsMatchRegex = "\\d*[0-9]".toRegex()
        var ignoreTextChanged = false

        textInputEditText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
                if (ignoreTextChanged) {
                    ignoreTextChanged = false
                    return
                }

                val goalDescription = p0.toString()
                val matches = digitsMatchRegex.findAll(goalDescription)

                if (matches.firstOrNull() != null){
                    seekbar1.visibility = View.VISIBLE
                    goal.type = NUMBER

                    goalNumberValue1 = matches.first().value.toDouble()
                    val isIntegerValue = Math.floor(goalNumberValue1) == goalNumberValue1

                    if (isIntegerValue) {
                        seekbar1.max = goalNumberValue1.toInt() * 4
                        seekbar1.progress = goalNumberValue1.toInt()
                    }
                } else {
                    seekbar1.visibility = View.GONE
                    goal.type = BINARY
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        seekbar1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, p1: Int, fromUser: Boolean) {
                if (fromUser) {
                    val goalDescription = textInputEditText.text.toString()

                    textInputEditText.setText(goalDescription
                        .replaceFirst(digitsMatchRegex, seekBar.progress.toString()))

                    ignoreTextChanged = true
                }
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
    }

    override fun updateGoalInfo(): Goal {
        goal.description = textInputEditText.text.toString()
        goal.aimedNumberResult = when (goal.type) {
            NUMBER -> goalNumberValue1
            BINARY -> 0.toDouble()
        }
        return goal
    }

    override fun validateInfo(): Boolean {
        return true
    }

}