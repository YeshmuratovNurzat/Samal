package kz.fime.samal.ui

import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kz.fime.samal.R
import kz.fime.samal.data.SessionManager
import kz.fime.samal.data.repositories.FcmRepository
import kz.fime.samal.databinding.ActivityMainBinding
import kz.fime.samal.utils.EventObserver
import kz.fime.samal.utils.MessageUtils
import kz.fime.samal.utils.binding.BindingActivity
import kz.fime.samal.utils.components.BottomBar
import kz.fime.samal.utils.components.Message
import kz.fime.samal.utils.extensions.setupWithNavController
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(ActivityMainBinding::inflate),
    CoroutineScope {

    @Inject
    lateinit var fcmRepository: FcmRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val message = Message(binding.vgMessages)
        MessageUtils.messages.observe(this, EventObserver {
            message.postMessage(it)
        })

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val bottomBar = BottomBar(binding.vgBottomBar)
        val currentNavController = bottomBar.setupWithNavController(
            navGraphIds = listOf(
                R.navigation.nav_home,
                R.navigation.nav_catalog,
                R.navigation.nav_qr,
                R.navigation.nav_cart,
                R.navigation.nav_profile
            ),
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
            intent = intent
        )

        sendPushToken()
    }

    private fun sendPushToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            val token = task.result
            Timber.d("Firebase token %s", token ?: "no token")
            token?.let {
                launch {
                    if (SessionManager.getPushToken().isEmpty())
                        fcmRepository.addPushToken(token)
                    else {
                        fcmRepository.editPushToken(token)
                    }
                }
            }
        })
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob()

    override fun onDestroy() {
        super.onDestroy()
        BottomBar.getBottomBar()?.destroy()
    }
}