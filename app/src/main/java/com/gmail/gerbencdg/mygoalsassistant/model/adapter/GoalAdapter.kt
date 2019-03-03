package com.gmail.gerbencdg.mygoalsassistant.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gmail.gerbencdg.mygoalsassistant.BR
import com.gmail.gerbencdg.mygoalsassistant.R
import com.gmail.gerbencdg.mygoalsassistant.databinding.ListItemGoalBinding
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal.GoalType.BINARY
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal.GoalType.NUMBER
import com.gmail.gerbencdg.mygoalsassistant.model.entities.GoalResult
import kotlinx.android.synthetic.main.list_item_goal.view.*
import kotlin.math.roundToInt


class GoalAdapter(
    private var navigateToGoalItemFragment: (goalId: Int) -> Unit,
    private var updateGoalResultCallback: UpdateGoalResultCallback,
    private var filterPredicate: (Goal) -> Boolean
) :
    ListAdapter<Goal, GoalAdapter.ViewHolder>(GoalDiffCallBack()) {

    class ViewHolder(
        private val updateGoalResultCallback: UpdateGoalResultCallback,
        private val binding: ListItemGoalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private val rootView = binding.root

        fun setOnClickListener(clickListener: View.OnClickListener) {
            rootView.setOnClickListener(clickListener)
        }

        val goal get() = binding.goal

        fun bind(goal: Goal) {
            binding.setVariable(BR.goal, goal)
            binding.executePendingBindings()

            setGoalsInfo(goal)
        }

        private fun setGoalsInfo(goal: Goal) {

            setProgressButtonClickListener(goal)
            setProgressBarValue(goal)

            if (goal.resultOfToday != null) {
                setProgressButtonState(goal, goal.resultOfToday!!)
            }
        }

        private fun setProgressButtonState(goal: Goal, resultOfToday: GoalResult) {

            val context = rootView.context
            val goalProgressButton = rootView.goalProgressButton

            if (goal.type == NUMBER && resultOfToday.numberResult < goal.aimedNumberResult
                || goal.type == BINARY && !resultOfToday.boolResult
            ) {

                goalProgressButton.text = when (goal.type) {
                    BINARY -> context.getString(R.string.done)
                    NUMBER -> context.getString(R.string.plus1)
                }
            } else {
                goalProgressButton.text = context.getString(R.string.check_sign)
            }
        }

        private fun setProgressBarValue(goal: Goal) {
            val goalProgressBar = rootView.goalProgressBar
            if (goal.resultOfToday == null) {
                goalProgressBar.progress = 0
                return
            }

            when (goal.type) {
                BINARY -> {
                    goalProgressBar.max = 1
                    goal.resultOfToday?.apply {
                        if (boolResult) goalProgressBar.progress = 1
                        else goalProgressBar.progress = 0
                    }
                }
                NUMBER -> {
                    goalProgressBar.max = goal.aimedNumberResult.toInt()
                    goal.resultOfToday?.apply {
                        goalProgressBar.progress = numberResult.roundToInt()
                    }
                }
            }

        }

        private fun setProgressButtonClickListener(goal: Goal) {

            rootView.goalProgressButton.setOnClickListener {
                if (goal.resultOfToday == null)
                    return@setOnClickListener

                when (goal.type) {
                    BINARY -> {
                        goal.resultOfToday?.apply {
                            boolResult = true
                            setProgressButtonState(goal, goal.resultOfToday!!)
                        }
                    }
                    NUMBER -> {
                        goal.resultOfToday?.apply {
                            numberResult += 1
                            setProgressButtonState(goal, this)
                        }
                    }
                }
                setProgressBarValue(goal)
                goal.resultOfToday?.apply {
                    updateGoalResultCallback.onGoalResultUpdate(this)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            updateGoalResultCallback,
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.list_item_goal, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val goal = getItem(position)
        with(holder) {
            bind(goal)
            setOnClickListener(View.OnClickListener {
                navigateToGoalItemFragment(goal.id)
            })
        }
    }

    override fun submitList(list: List<Goal>?) {
        super.submitList(list?.filter(filterPredicate) ?: list)
    }

    interface UpdateGoalResultCallback {
        fun onGoalResultUpdate(goalResult: GoalResult)
    }

    private class GoalDiffCallBack : DiffUtil.ItemCallback<Goal>() {
        override fun areItemsTheSame(oldItem: Goal, newItem: Goal): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Goal, newItem: Goal): Boolean {
            if (oldItem.resultOfToday?.goalId == newItem.resultOfToday?.goalId) {
                return true // We don't need to refresh the view as it has already been handled by the ViewHolder
            }

            return oldItem == newItem
        }
    }
}