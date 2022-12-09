package kz.fime.samal.ui

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.databinding.ActivityAuthBinding
import kz.fime.samal.databinding.ActivityMainBinding
import kz.fime.samal.utils.EventObserver
import kz.fime.samal.utils.MessageUtils
import kz.fime.samal.utils.binding.BindingActivity
import kz.fime.samal.utils.components.Message

@AndroidEntryPoint
class AuthActivity : BindingActivity<ActivityAuthBinding>(ActivityAuthBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val message = Message(binding.vgMessages)
        MessageUtils.messages.observe(this, EventObserver {
            message.postMessage(it)
        })
    }

}