package com.dicoding.kreatoroom.di

import android.content.Context

object Injection {
    fun provideRepository(context: Context): {
        val database =
        val dao =
        val appExecutors = AppExecutors()
        return .getInstance(dao, appExecutors)
    }
}