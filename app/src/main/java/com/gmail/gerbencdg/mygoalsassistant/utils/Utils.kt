package com.gmail.gerbencdg.mygoalsassistant.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Suppress("UNCHECKED_CAST")
inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = f() as T
    }

suspend fun <R> runAsync(block: () -> R, then: (R) -> Unit) {
    withContext(Dispatchers.Default) {
        block.invoke().also(then)
    }
}
