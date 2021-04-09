package com.durbindevs.tradiediary.di

import android.app.Application
import androidx.room.Room
import com.durbindevs.tradiediary.db.JobsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(app: Application
    ) = Room.databaseBuilder(app, JobsDatabase::class.java, "jobs_database")
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    fun provideJobsDao(db: JobsDatabase) = db.getJobsDao()

    @ApplicationScope
    @Provides
    @Singleton
    fun ProvideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention
@Qualifier
annotation class ApplicationScope