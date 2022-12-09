package kz.fime.samal.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import kz.fime.samal.data.SessionManager
import kz.fime.samal.ui.welcome.WelcomeActivity

class LauncherActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SessionManager.inject(applicationContext)
        when {
            SessionManager.isFirstLaunch -> {
                SessionManager.isFirstLaunch = false
                startActivity(Intent(this, WelcomeActivity::class.java))
            }
            else -> {
                startActivity(Intent(this, MainActivity::class.java))
//                if (SessionManager.token.isEmpty()) {
//                    startActivity(Intent(this, AuthActivity::class.java))
//                } else
//                    startActivity(Intent(this, MainActivity::class.java))
            }
        }
        finish()
    }
}