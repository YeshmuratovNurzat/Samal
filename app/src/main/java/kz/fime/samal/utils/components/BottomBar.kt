package kz.fime.samal.utils.components

import android.view.View
import android.view.animation.LinearInterpolator
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kz.fime.samal.R
import kz.fime.samal.databinding.ComponentBottomNavBinding
import kz.fime.samal.utils.extensions.dpToPx
import kz.fime.samal.utils.extensions.spring
import kotlin.math.sin

class BottomBar(
    private val binding: ComponentBottomNavBinding
) {

    companion object {

        private var sInstance: BottomBar? = null

        fun getBottomBar() = sInstance
    }

    private val bottomBarHeight = 56.dpToPx().toFloat()

    init {
        sInstance = this
        binding.run {
            vgMain.run {
                btnHome.isChecked = true
                btnHome.setOnClickListener {
                    selectItemIndex(0)
                }
                btnCatalog.setOnClickListener {
                    selectItemIndex(1)
                }
                btnQr.setOnClickListener {
                    selectItemIndex(2)
                }
                btnCart.setOnClickListener {
                    selectItemIndex(3)
                }
                btnAcconut.setOnClickListener {
                    selectItemIndex(4)
                }
            }
        }
    }

    fun destroy(){
        sInstance = null
    }

    fun show() {
//        binding.root.spring(DynamicAnimation.TRANSLATION_Y).animateToFinalPosition(0f)
        binding.root.visibility = View.VISIBLE
//        binding.root.animate().alpha(1f).start()
    }
    fun hide() {
//        binding.root.spring(DynamicAnimation.TRANSLATION_Y).animateToFinalPosition(bottomBarHeight)
        binding.root.visibility = View.GONE
//        binding.root.animate().alpha(0f).withEndAction { binding.root.visibility = View.GONE }.start()
    }

    fun getBinding() = binding

    fun selectItemIndex(index: Int) {
        when(index) {
            0 -> {
                if (selectedTabId == HOME_TAB_ID) {
                    selectedTabId = HOME_TAB_ID
                    navigationTabReselectedListener?.invoke(selectedTabId)
                } else {
                    selectedTabId = HOME_TAB_ID
                    navigationTabSelectedListener?.invoke(selectedTabId)
                }
            }
            1 -> {
                if (selectedTabId == CATALOG_TAB_ID) {
                    selectedTabId = CATALOG_TAB_ID
                    navigationTabReselectedListener?.invoke(selectedTabId)
                } else {
                    selectedTabId = CATALOG_TAB_ID
                    navigationTabSelectedListener?.invoke(selectedTabId)
                }
            }
            2 -> {
//                if (selectedTabId == QR_TAB_ID) {
//                    navigationTabReselectedListener?.invoke(selectedTabId)
//                } else {
//                    selectedTabId = QR_TAB_ID
//                    navigationTabSelectedListener?.invoke(selectedTabId)
//                }
                selectedTabId = QR_TAB_ID
                navigationTabSelectedListener?.invoke(selectedTabId)
            }
            3 -> {
                if (selectedTabId == CART_TAB_ID) {
                    selectedTabId = CART_TAB_ID
                    navigationTabReselectedListener?.invoke(selectedTabId)
                } else {
                    selectedTabId = CART_TAB_ID
                    navigationTabSelectedListener?.invoke(selectedTabId)
                }
            }
            4 -> {
                if (selectedTabId == PROFILE_TAB_ID) {
                    selectedTabId = PROFILE_TAB_ID
                    navigationTabReselectedListener?.invoke(selectedTabId)
                } else {
                    selectedTabId = PROFILE_TAB_ID
                    navigationTabSelectedListener?.invoke(selectedTabId)
                }
            }
        }

    }

    private fun setSelectedItemIndex(index: Int){
        binding.run {
            when (index) {
                0 -> {
                    btnHome.isChecked = true
                    btnCatalog.isChecked = false
                    btnQr.isChecked = false
                    btnCart.isChecked = false
                    btnAcconut.isChecked = false
                }
                1 -> {
                    btnHome.isChecked = false
                    btnCatalog.isChecked = true
                    btnQr.isChecked = false
                    btnCart.isChecked = false
                    btnAcconut.isChecked = false
                }
                2 -> {
                    btnHome.isChecked = false
                    btnCatalog.isChecked = false
                    btnQr.isChecked = true
                    btnCart.isChecked = false
                    btnAcconut.isChecked = false
                }
                3 -> {
                    btnHome.isChecked = false
                    btnCatalog.isChecked = false
                    btnQr.isChecked = false
                    btnCart.isChecked = true
                    btnAcconut.isChecked = false
                }
                4 -> {
                    btnHome.isChecked = false
                    btnCatalog.isChecked = false
                    btnQr.isChecked = false
                    btnCart.isChecked = false
                    btnAcconut.isChecked = true
                }
            }
        }
    }


    fun showMain(){
        show()
        binding.vgMain.visibility = View.VISIBLE
//        binding.vgMain.animate().scaleX(1f).scaleY(1f).alpha(1f).setInterpolator(LinearInterpolator()).start()
        hideProductDetails()
    }


    private fun hideMain(){
//        binding.vgMain.animate().scaleX(0f).scaleY(0f).alpha(0f).setInterpolator(LinearInterpolator()).withEndAction {
//            binding.vgMain.visibility = View.GONE
//        }.start()
        binding.vgMain.visibility = View.GONE
    }

    fun showProductDetails(
        back: () -> Unit = {},
        share: () -> Unit = {},
        like: () -> Unit = {},
        addToCart: () -> Unit = {}
    ){
        binding.vgProductDetails.visibility = View.VISIBLE
//        binding.vgProductDetails.animate().scaleX(1f).scaleY(1f).alpha(1f).setInterpolator(
//            LinearInterpolator()
//        ).start()
        hideMain()
        binding.run {
            btnBack.setOnClickListener { back.invoke() }
            btnShare.setOnClickListener { share.invoke() }
            btnLike.setOnClickListener { like.invoke() }
            btnAddToCart.setOnClickListener { addToCart.invoke() }
        }
    }


    private fun hideProductDetails(){
//        binding.vgProductDetails.animate().scaleX(0f).scaleY(0f).alpha(0f).setInterpolator(
//            LinearInterpolator()
//        ).withEndAction {
//            binding.vgProductDetails.visibility = View.GONE
//        }.start()
        binding.vgProductDetails.visibility = View.GONE
    }

    var selectedTabId = HOME_TAB_ID
        set(value) {
            field = value
            when (value) {
                HOME_TAB_ID -> setSelectedItemIndex(0)
                CATALOG_TAB_ID -> setSelectedItemIndex(1)
                QR_TAB_ID -> setSelectedItemIndex(2)
                CART_TAB_ID -> setSelectedItemIndex(3)
                PROFILE_TAB_ID -> setSelectedItemIndex(4)
            }
        }

    private var navigationTabSelectedListener: ((tab: Int) -> Unit)? = {}
    private var navigationTabReselectedListener: ((tab: Int) -> Unit)? = {}

    fun setOnNavigationTabSelectedListener(tabListener: (tab: Int) -> Unit) {
        navigationTabSelectedListener = tabListener
    }

    fun setOnNavigationTabReselectedListener(tabListener: (tab: Int) -> Unit) {
        navigationTabReselectedListener = tabListener
    }
}

const val HOME_TAB_ID = R.id.nav_home
const val CATALOG_TAB_ID = R.id.nav_catalog
const val QR_TAB_ID = R.id.nav_qr
const val CART_TAB_ID = R.id.nav_cart
const val PROFILE_TAB_ID = R.id.nav_profile