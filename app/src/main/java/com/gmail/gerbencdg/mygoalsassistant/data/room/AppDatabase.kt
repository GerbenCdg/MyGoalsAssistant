package com.gmail.gerbencdg.mygoalsassistant.data.room

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gmail.gerbencdg.mygoalsassistant.application.DATABASE_NAME
import com.gmail.gerbencdg.mygoalsassistant.data.room.typeconverter.GoalFrequencyTypeConverter
import com.gmail.gerbencdg.mygoalsassistant.data.room.typeconverter.GoalTypeTypeConverter
import com.gmail.gerbencdg.mygoalsassistant.model.entities.Goal
import com.gmail.gerbencdg.mygoalsassistant.model.entities.GoalResult

@Database(entities = [Goal::class, GoalResult::class], version = 3, exportSchema = false)
@TypeConverters(GoalFrequencyTypeConverter::class, GoalTypeTypeConverter::class)

abstract class AppDatabase : RoomDatabase() {

    abstract fun goalDao(): GoalDao
    abstract fun goalResultDao(): GoalResultDao
    abstract fun goalWithResultsDao(): GoalWithResultsDao

    companion object {
        private var application: Application? = null

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(): AppDatabase {
            if (application == null)
                throw RuntimeException("The application should never be null at this point")

            if (instance == null) {
                synchronized(this) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            application!!.applicationContext,
                            AppDatabase::class.java,
                            DATABASE_NAME
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                    }
                }
            }
            return instance!!
        }

        @Synchronized
        fun initialize(app: Application) {
            if (application != null) throw RuntimeException("AppDatabase should only be initialized once !")
            application = app
        }
    }


}