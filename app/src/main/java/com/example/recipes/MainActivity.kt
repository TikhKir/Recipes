package com.example.recipes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.recipes.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, HomeFragment.newInstance())
                .commitNow()
        }
    }
}