package com.capstone.roadcrackapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.roadcrackapp.model.injection.Injection
import com.capstone.roadcrackapp.model.viewmodel.Repository

class ViewModelFactory private constructor(private val repository: Repository) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
//            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
//                return MainViewModel(repository) as T
//            }
//            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
//                return LoginViewModel(repository) as T
//            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                return RegisterViewModel(repository) as T
            }
//            modelClass.isAssignableFrom(CreateViewModel::class.java) -> {
//                return CreateViewModel(repository) as T
//            }
//            modelClass.isAssignableFrom(MapViewModel::class.java) -> {
//                return MapViewModel(repository) as T
//            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: "+modelClass.name)
        }
    }
    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}