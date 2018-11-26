package tronum.redditclient.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class SplashActivity: AppCompatActivity() {
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Observable.timer(1, TimeUnit.SECONDS)
            .subscribe{
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
    }
}