package com.gmail.gerbencdg.mygoalsassistant.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.gmail.gerbencdg.mygoalsassistant.R
import com.gmail.gerbencdg.mygoalsassistant.utils.AppUtils
import com.gmail.gerbencdg.mygoalsassistant.utils.AppUtils.AppStart.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_start.*


class StartActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        setSupportActionBar(findViewById(R.id.toolbar))
        initializeNavigation()
        checkAppStart()
//        showDebugDBAddressLogToast(this)
    }

    private fun initializeNavigation() {
        navController = findNavController(this, R.id.nav_host_fragment)
        setupActionBarWithNavController(this, navController)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        NavigationUI.setupWithNavController(bottomNav, navController)
        bottomNav.setOnNavigationItemSelectedListener {
            if (it.itemId != R.id.menu_nav_goals_list) {
                Snackbar.make(
                    bottom_nav,
                    getString(R.string.feature_coming_soon),
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnNavigationItemSelectedListener false
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    private fun checkAppStart() {
        when (AppUtils.checkAppStart(this)) {
            FIRST_TIME -> {}
            FIRST_TIME_VERSION -> {}
            NORMAL -> {}
        }
    }

    /*    private fun showDebugDBAddressLogToast(context: Context) {
        if (BuildConfig.DEBUG) {
            try {
                val debugDB = Class.forName("com.amitshekhar.DebugDB")
                val getAddressLog = debugDB.getMethod("getAddressLog")
                val value = getAddressLog.invoke(null)
                Toast.makeText(context, value as String, Toast.LENGTH_LONG).show()
            } catch (ignore: Exception) {
            }
        }
    }*/
}
