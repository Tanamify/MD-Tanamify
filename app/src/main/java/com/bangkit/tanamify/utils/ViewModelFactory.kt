package com.bangkit.tanamify.utils

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.tanamify.data.di.Injection
import com.bangkit.tanamify.repository.HistoryRepository
import com.bangkit.tanamify.ui.home.HomeViewModel
import com.bangkit.tanamify.ui.login.LoginViewModel
import com.bangkit.tanamify.ui.register.RegisterViewModel

class ViewModelFactory private constructor(
    private val mApplication: Application,
    private val historyRepository: HistoryRepository
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    val application = Injection.provideApplication(context)
                    val repository = Injection.provideRepository(context)
                    INSTANCE = ViewModelFactory(application, repository)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(historyRepository) as T
            }

            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(historyRepository) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(mApplication, historyRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class" + modelClass.name)
        }
    }
}
