package com.gmail.gerbencdg.mygoalsassistant.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gmail.gerbencdg.mygoalsassistant.R
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal.GoalType.BINARY
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal.GoalType.NUMBER
import com.gmail.gerbencdg.mygoalsassistant.model.entities.GoalResult
import kotlinx.android.synthetic.main.list_item_goal_result.view.*
import java.text.DateFormat
import java.util.*

class GoalResultAdapter(private var goal: Goal) : ListAdapter<GoalResult, GoalResultAdapter.ViewHolder>(
    GoalResultDiffBack()
) {

    class ViewHolder(private var rootView: View) : RecyclerView.ViewHolder(rootView) {

        fun setGoalResultsInfo(goal: Goal, goalResult: GoalResult) {

            val context = rootView.context
            rootView.goalResultDateTextView.text = DateFormat.getDateInstance().format(Date(goalResult.date))

            rootView.goalResultValueTextView.text = when (goal.type) {

                BINARY -> if (goalResult.boolResult) {
                    context.getString(R.string.success)
                } else {
                    context.getString(R.string.fail)
                }

                NUMBER -> if (goalResult.numberResult.rem(1) != 0.toDouble()) {
                    goalResult.numberResult.toString()
                } else {
                    goalResult.numberResult.toInt().toString()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_item_goal_result, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val goalResult = getItem(position)
        holder.setGoalResultsInfo(goal, goalResult)
    }


    private class GoalResultDiffBack : DiffUtil.ItemCallback<GoalResult>() {

        override fun areItemsTheSame(oldItem: GoalResult, newItem: GoalResult): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GoalResult, newItem: GoalResult): Boolean {
            return oldItem == newItem
        }

    }
}