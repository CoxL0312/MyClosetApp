package com.example.mycloset

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.mycloset.ui.SplashFragment

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SplashFragment())
                .commit()
        }
    }
}
