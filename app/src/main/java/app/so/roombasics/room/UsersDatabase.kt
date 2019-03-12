package app.so.roombasics.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class UsersDatabase : RoomDatabase() {

    /**
     * Static instance of the database
     */
    companion object {

        private var INSTANCE: UsersDatabase? = null

        fun getDatabase(context: Context): UsersDatabase {
            if (INSTANCE == null) INSTANCE =
                Room.databaseBuilder(context.applicationContext, UsersDatabase::class.java, "users_database")
                    //.fallbackToDestructiveMigration()    //if no migration rules specified
                    //.allowMainThreadQueries()           //if you want to run Queries on Main thread
                    .build()
            return INSTANCE!!
        }
    }

    /**
     * List of all the DAOs
     */
    abstract fun getUserDao(): UserDao
}