package app.so.roombasics.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import app.so.roombasics.room.User
import app.so.roombasics.room.UserDao
import io.reactivex.Completable
import io.reactivex.Flowable
import kotlinx.coroutines.*

class UsersViewModel(private val userDao: UserDao) : ViewModel() {

    /**
     * Create a coroutine scope to perform room related tasks on a separate thread
     */
    private val viewModelJob = SupervisorJob()
    private val ioScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    /**
     * Get the user name of the user stored with the tag of "rx"
     * @return a [Flowable] that will emit every time the user name has been updated.
     */
    fun getRxUser(): Flowable<String> {
        return userDao.rxGetUserByTag(RX)
            .map { user -> user.userName }
    }

    /**
     * Get the user stored with the tag of "livedata"
     * @return a [User] LiveData that can be observed on the view
     */
    fun getLiveUser(): LiveData<User> {
        return userDao.liveGetUserByTag(LIVEDATA)
    }

    /**
     * Get the user stored with the tag of "coroutines"
     * @return the username
     * This method has a suspend keyword since it is calling a
     * suspend method
     */
    suspend fun getCoroutineUserName(): String? {
        return userDao.coroutineGetUserNameByTag(COROUTINES)
    }

    /**
     * Update the user name with tag "rx"
     * @param userName the new user name
     * @return a [Completable] that completes when the user name is updated
     */
    fun updateRxUserName(userName: String): Completable {
        val user = User(RX, userName)
        return userDao.rxInsertUser(user)
    }

    /**
     * Update the user name with tag "livedata"
     * @param userName the new user name
     * Here we call the insert method on a separate thread
     * as room doesn't allow us to perform tasks on the main thread
     */
    fun updateLiveUserName(userName: String){
        val user = User(LIVEDATA, userName)
        ioScope.launch { userDao.liveInsertUser(user) }
    }

    /**
     * Update the user name with tag "coroutines"
     * @param userName the new user name
     * Here we call the insert method on a separate thread
     * as the method is a suspend method.
     */
    fun updateCoroutineUserName(userName: String){
        val user = User(COROUTINES, userName)
        ioScope.launch { userDao.coroutinesInsertUser(user) }
    }


    companion object {
        const val RX = "rx"
        const val LIVEDATA = "livedata"
        const val COROUTINES = "coroutines"
    }

    /**
     * We cancel the parent job when the user exits the app
     */

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}