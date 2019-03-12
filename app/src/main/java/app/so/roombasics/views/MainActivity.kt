package app.so.roombasics.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import app.so.roombasics.R
import app.so.roombasics.room.User
import app.so.roombasics.viewmodels.UsersViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.coroutines.CoroutineContext

/**
 * To define a coroutine scope for this activity we extend CoroutineScope interface
 */

class MainActivity : AppCompatActivity(), CoroutineScope {


    private lateinit var mJob: Job
    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    // injecting viewmodel
    private val usersViewModel: UsersViewModel by viewModel()
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mJob = Job()
    }

    override fun onStart() {
        super.onStart()
        initViews()
    }

    /**
     * Method to initialise all the views
     */

    private fun initViews() {
        add_data.setOnClickListener {
            val intent = Intent(this, DataActivity::class.java)
            startActivity(intent)
        }

        // Subscribe to the emissions of the user name from the view model.
        // Update the user name text view, at every onNext emission.
        // In case of error, log the exception.
        disposable.add(usersViewModel.getRxUser()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    rxTextView.text = it
                },
                { error ->
                    Log.e(TAG, "Unable to get username", error)
                }
            )
        )

        // Update the data by observing changes in the database
        usersViewModel.getLiveUser().observe(this, Observer<User> { user ->
            liveTextView.text = user?.userName
        })

        // Calling suspend method from a coroutine scope
        launch { coroutinesTextView.text = usersViewModel.getCoroutineUserName() }
    }

    /**
     * Here we clear all the subscriptions
     */
    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    /**
     * Cancel the parent job when user exits the app
     */
    override fun onDestroy() {
        super.onDestroy()
        mJob.cancel()
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
