package com.gmail.gerbencdg.mygoalsassistant.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import com.gmail.gerbencdg.mygoalsassistant.R
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal
import java.util.*

class DurationGoalCreationPage(context: Context, goal: Goal) :
    GoalCreationPage(
        context,
        context.getString(R.string.how_long_question),
        goal
    ) {

    override val contentView: View
    private val durationTextView: TextView
    private val durationSlider: SeekBar

    init {
        contentView = LayoutInflater.from(context).inflate(R.layout.dialog_new_goal_slider, null)

        durationTextView = contentView.findViewById(R.id.durationTextView)
        durationSlider = contentView.findViewById(R.id.durationSlider)

        durationSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(s: SeekBar?, progress: Int, b: Boolean) {
                durationTextView.text = when (progress) {
                    0 -> "For ${progress + 1} day"
                    in 0..16 -> "For ${progress + 1} days"
                    in 17..25 -> "For ${progress - 14} weeks"
                    in 26..35 -> "For ${progress - 23} months"
                    else -> "For always and forever :)"
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })
        durationSlider.progress = 9
        durationTextView.text = "For ${durationSlider.progress + 1} days"
    }

    override fun validateInfo(): Boolean {
        return true
    }

    override fun updateGoalInfo(): Goal {
        goal.aimedCompletionDate = Calendar.getInstance().apply {
            when (durationSlider.progress) {
                in 0..16 -> add(Calendar.DAY_OF_MONTH, durationSlider.progress + 1)
                in 17..25 -> add(Calendar.WEEK_OF_YEAR, durationSlider.progress - 14)
                in 26..33 -> add(Calendar.MONTH, durationSlider.progress - 23)
                else -> timeInMillis = 0
            }
        }.time.time
        return goal
    }


}