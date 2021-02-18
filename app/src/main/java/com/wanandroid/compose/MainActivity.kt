package com.wanandroid.compose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.github.fragivity.loadRoot
import com.wanandroid.compose.ui.main.AppContent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment)
            .loadRoot {
                ComposeFragment {
                    AppContent()
                }
            }
    }
}

