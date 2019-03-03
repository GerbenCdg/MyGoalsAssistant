package com.gmail.gerbencdg.mygoalsassistant.model.entities

import androidx.room.Embedded
import androidx.room.Relation

class GoalWithResults(
    @Embedded
    var goal: Goal
)
{
    // parentColumn : The id name in the embedded table; entityColumn : The id name of the embedded table stored in the second table
    @Relation(parentColumn = "id", entityColumn = "goal_id", entity = GoalResult::class)
    var goalResults: List<GoalResult> = emptyList()

}