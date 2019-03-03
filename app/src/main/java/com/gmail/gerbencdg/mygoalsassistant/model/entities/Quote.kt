package com.gmail.gerbencdg.mygoalsassistant.model.entities

data class Quote(
    val author: String?,
    val text: String?,
    val id: Long
) {
    constructor() : this("","", 0L)
}