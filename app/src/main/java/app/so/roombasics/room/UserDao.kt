package app.so.roombasics.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
    @Dao
    interface UserDao {

        /**
         * Methods to get the User info from table
         */
        @Query("SELECT * FROM User WHERE tag = :tag")
        fun rxGetUserByTag(tag: String): Flowable<User>

        @Query("SELECT * FROM User WHERE tag = :tag")
        fun liveGetUserByTag(tag: String): LiveData<User>

        @Query("SELECT userName FROM User WHERE tag = :tag")
        suspend fun coroutineGetUserNameByTag(tag: String): String


        /**
         * Methods to insert the User in table
         */

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun rxInsertUser(user: User): Completable

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun liveInsertUser(user: User)

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun coroutinesInsertUser(user: User)

        /**
         * Method to delete all the users
         */

        @Query("DELETE FROM User")
        fun deleteAllUsers()
}