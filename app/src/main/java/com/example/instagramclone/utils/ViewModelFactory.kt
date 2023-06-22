package com.example.instagramclone.utils

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.instagramclone.UserPreferences
import com.example.instagramclone.models.AuthViewModel
import com.example.instagramclone.models.PagingViewModel

class ViewModelFactory(private val pref: UserPreferences ?= null, private val application: Application ?= null,
                       private val context: Context ?= null,
                       private val token: String ?= null) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PagingViewModel::class.java)) {
            Log.d("TOKEN", "$token")
            return PagingViewModel(application!!, Injection.provideRepository(context!!), token!!) as T
        }
        else if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(pref!!) as T
        } else throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}