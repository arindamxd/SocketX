package io.arindam.socketx.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.arindam.socketx.data.local.Repository
import io.arindam.socketx.ui.home.HomeViewModel

/**
 * Created by Arindam Karmakar on 9/5/21.
 */

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(repository) as T
    }
}
