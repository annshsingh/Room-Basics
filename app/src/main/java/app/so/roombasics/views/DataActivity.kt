package app.so.roombasics.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.so.roombasics.R
import app.so.roombasics.onSubmit
import app.so.roombasics.viewmodels.UsersViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_data.*
import org.koin.android.viewmodel.ext.android.viewModel

class DataActivity : AppCompatActivity() {

    // injecting view model
    private val usersViewModel: UsersViewModel by viewModel()
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        initViews()
    }

    /**
     * Method to initialise views
     * Check Extensions.kt file to know the functionality for onSubmit
     */

    private fun initViews() {

        rx_edit_text.onSubmit {
            Log.e("Data", "Here")
            updateRxUserName()
            onBackPressed()
        }

        live_edit_text.onSubmit {
            usersViewModel.updateLiveUserName(live_edit_text.text.toString())
            onBackPressed()
        }

        coroutines_edit_text.onSubmit {
            usersViewModel.updateCoroutineUserName(coroutines_edit_text.text.toString())
            onBackPressed()
        }
    }

    override fun onStop() {
        super.onStop()
        // clear all the subscriptions
        disposable.clear()
    }

    private fun updateRxUserName() {
        // Subscribe to updating the user name with tag "rx"
        disposable.add( usersViewModel.updateRxUserName(rx_edit_text.text.toString())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ Log.e(TAG, "Updated RX username")  },
                { error -> Log.e(TAG, "Unable to update RX username", error) }))
    }

    companion object {
        private val TAG = DataActivity::class.java.simpleName
    }
}
