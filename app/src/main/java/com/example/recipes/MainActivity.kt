package com.example.recipes

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.recipes.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, HomeFragment.newInstance())
                .commitNow()
        }
    }


    fun fullScreenModeOn() {
        if (Build.VERSION.SDK_INT >= 30) {
            window.insetsController?.apply {
                hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            }
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }

    fun fullScreenModeOff() {
        if (Build.VERSION.SDK_INT >= 30) {
            window.insetsController?.apply {
                show(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
            }
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_VISIBLE)
        }
    }


    private fun hasWriteExternalStoragePermission() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        val permissionsToRequest = mutableListOf<String>()
        if (!hasWriteExternalStoragePermission()) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()) {
            for (grantResult in grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    Log.d("TAG", grantResult.toString())
                }
            }
        }
    }



}