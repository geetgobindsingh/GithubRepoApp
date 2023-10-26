package com.geetgobindingh.githubrepoapp.ui.splash.view

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.geetgobindingh.githubrepoapp.R
import com.geetgobindingh.githubrepoapp.ui.trending.view.TrendingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    //region Member variables
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreenActivity()
        setContentView(R.layout.activity_splash)
        val intent = Intent(this@SplashActivity, TrendingActivity::class.java)
        startActivity(intent)
        finish()
    }

    //region Private Helper methods
    private fun setFullScreenActivity() {
        if (window != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                )
            } else {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
        }
    }
    //endregion
}