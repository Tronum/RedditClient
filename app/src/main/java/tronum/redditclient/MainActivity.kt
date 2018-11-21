package tronum.redditclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import tronum.redditclient.api.RedditApiService
import tronum.redditclient.api.RedditModel

class MainActivity : AppCompatActivity() {
    private val redditApiService by lazy {
        RedditApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        beginTopFetching()
    }

    private fun beginTopFetching() {
        redditApiService.getTop()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({showResult(it)}, {showError(it.message)})
    }

    private fun showResult(response: RedditModel.TopResponse) {
        Log.v("MainActivity", "Success")
    }

    private fun showError(message: String?) {
        Log.e("MainActivity", message)
    }
}
