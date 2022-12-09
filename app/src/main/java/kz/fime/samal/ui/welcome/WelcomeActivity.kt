package kz.fime.samal.ui.welcome

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import kz.fime.samal.R
import kz.fime.samal.databinding.ActivityWelcomeBinding
import kz.fime.samal.ui.AuthActivity
import kz.fime.samal.ui.MainActivity
import kz.fime.samal.ui.welcome.adapters.CardAdapter
import kz.fime.samal.ui.welcome.adapters.WelcomeInfo
import kz.fime.samal.ui.welcome.adapters.WelcomeInfoAdapter
import kz.fime.samal.utils.DimensionUtils
import kz.fime.samal.utils.binding.BindingActivity
import kz.fime.samal.utils.extensions.dpToPx
import kz.fime.samal.utils.extensions.loadDrawable
import kz.fime.samal.utils.extensions.pxToDp
import kz.fime.samal.utils.extensions.spring
import timber.log.Timber

@AndroidEntryPoint
class WelcomeActivity : BindingActivity<ActivityWelcomeBinding>(ActivityWelcomeBinding::inflate) {

    private val imageIds = listOf(
            R.drawable.splash_img_1,
            R.drawable.splash_img_1,
            R.drawable.splash_img_2,
            R.drawable.splash_img_2,
            R.drawable.splash_img_3
    )

    private val c1 = arrayOf(
            R.drawable.ic_img_1_1,
            R.drawable.ic_img_1_2,
            R.drawable.ic_img_1_3,
            R.drawable.ic_img_1_4,
            R.drawable.ic_img_1_5,
            R.drawable.ic_img_1_6,
            R.drawable.ic_img_1_7
    )
    private val c3 = arrayOf(
            R.drawable.ic_img_3_1,
            R.drawable.ic_img_3_2,
            R.drawable.ic_img_3_3,
            R.drawable.ic_img_3_5,
            R.drawable.ic_img_3_6,
            R.drawable.ic_img_3_7
    )

    private val c5 = arrayOf(
            R.drawable.ic_img_5_1,
            R.drawable.ic_img_5_2,
            R.drawable.ic_img_5_3,
            R.drawable.ic_img_5_4,
            R.drawable.ic_img_5_5,
            R.drawable.ic_img_5_6,
            R.drawable.ic_img_5_7
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Init
        val l1 = mutableListOf<Int>()
        for (i in 0..20) {
            l1.add(c1[i%c1.size])
        }
        val a1 = CardAdapter()
        a1.submitList(l1)

        val l5 = mutableListOf<Int>()
        for (i in 0..20) {
            l5.add(c5[i%c5.size])
        }
        val a5 = CardAdapter()
        a5.submitList(l5)

        val l3t = mutableListOf<Int>()
        for (i in 0..17) {
            l3t.add(c3[i%c3.size])
        }
        l3t.add(c3[0])
        l3t.add(c3[1])
        l3t.add(c3[2])
        val l3b = mutableListOf<Int>()
        for (i in 0..17) {
            l3b.add(c3[i%c3.size])
        }
        l3b.add(c3[3])
        l3b.add(c3[4])
        l3b.add(c3[5])

        val a3t = CardAdapter()
        a3t.submitList(l3t)
        val a3b = CardAdapter()
        a3b.submitList(l3b)

        val mid = ((DimensionUtils.displayHeightPx()-162.dpToPx())/2).pxToDp()


        binding.run {
            setUpBottomSheet()

            sv.postDelayed({ setUpPulseAnim()}, 500)
            sv.viewTreeObserver.addOnGlobalLayoutListener {
                sv.scrollTo(425.dpToPx() - DimensionUtils.displayWidthPx()/2, 0)
            }


            rvCenterTop.adapter = a3t
            rvCenterBottom.adapter = a3b
            rvLeftMost.adapter = a1
            rvRightMost.adapter = a5

            val padding = (mid.dpToPx() + 162.dpToPx())*2
            svLeft.setPadding(0, padding, 0, 0)

            svRight.setPadding(0, 0, 0, padding)
            svRight.viewTreeObserver.addOnGlobalLayoutListener {
                svRight.fullScroll(View.FOCUS_DOWN)
            }

            rvCenterTop.setPadding(0, 0, 0, padding)
            rvCenterTop.viewTreeObserver.addOnGlobalLayoutListener {
                rvCenterTop.scrollBy(0, rvCenterTop.computeVerticalScrollRange())
            }
            rvCenterBottom.setPadding(0, padding, 0, 0)

            // Center everything
            cvCenter.postDelayed({
                svLeft.visibility = View.VISIBLE
                svRight.visibility = View.VISIBLE
                svLeft.smoothScrollBy(0, padding/2 + 5*172.dpToPx())
                svRight.smoothScrollBy(0, -(padding/2+10.dpToPx() + 5*172.dpToPx()))
                rvCenterTop.smoothScrollBy(0, -padding)
                rvCenterBottom.smoothScrollBy(0, padding)
            }, 4000)

            // Rotate to 30 degrees
            cvCenter.postDelayed({
                scrollLeft = svLeft.scrollY
                scrollRight = svRight.scrollY
                container.spring(DynamicAnimation.ROTATION).animateToFinalPosition(-30f)
            }, 5000)


            sv.postDelayed({
                vgBottom.visibility = View.VISIBLE
                vgBottom.spring(DynamicAnimation.TRANSLATION_Y).animateToFinalPosition(0f)
                sv.spring(DynamicAnimation.TRANSLATION_Y).animateToFinalPosition(-100.dpToPx().toFloat())
            }, 6000)
        }
    }

    private fun setUpPulseAnim(){
        binding.run {
            var pulsed = true

            for (i in 1..4) {
                ivCenter.postDelayed({
                    ivCenter.spring(DynamicAnimation.SCALE_X).animateToFinalPosition(0.1f)
                    ivCenter.spring(DynamicAnimation.SCALE_Y).animateToFinalPosition(0.1f)
                }, i*500L-100L)
            }

            for (i in 0..4) {
                cvCenter.postDelayed({
                    cvCenter.spring(DynamicAnimation.SCALE_X).animateToFinalPosition(if (pulsed) 1.0f else 0.75f)
                    cvCenter.spring(DynamicAnimation.SCALE_Y).animateToFinalPosition(if (pulsed) 1.0f else 0.75f)
                    ivCenter.loadDrawable(imageIds[i])
                    ivCenter.spring(DynamicAnimation.SCALE_X).animateToFinalPosition(1.0f)
                    ivCenter.spring(DynamicAnimation.SCALE_Y).animateToFinalPosition(1.0f)
                    pulsed =! pulsed
                }, i*500L)
            }
        }
    }


    var scrollLeft = 0
    var scrollRight = 0

    private fun setUpBottomSheet(){
        binding.run {

            val adapter = WelcomeInfoAdapter()
            vp.adapter = adapter
            adapter.submitList(listOf(
                    WelcomeInfo(getString(R.string.welcome_info_1), getString(R.string.welcome_info_details_1)),
                    WelcomeInfo(getString(R.string.welcome_info_2), getString(R.string.welcome_info_details_2)),
                    WelcomeInfo(getString(R.string.welcome_info_3), getString(R.string.welcome_info_details_3))
            ))


            vp.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                    val animOffset = (position*172 + positionOffset*172).dpToPx()
                    svLeft.spring(DynamicAnimation.SCROLL_Y).animateToFinalPosition((scrollLeft+animOffset))
                    svRight.spring(DynamicAnimation.SCROLL_Y).animateToFinalPosition((scrollRight-animOffset))
                }

                override fun onPageSelected(position: Int) {
                    dotLeft.setBackgroundResource(if (position == 0) R.drawable.bg_dot_selected else R.drawable.bg_dot)
                    dotCenter.setBackgroundResource(if (position == 1) R.drawable.bg_dot_selected else R.drawable.bg_dot)
                    dotRight.setBackgroundResource(if (position == 2) R.drawable.bg_dot_selected else R.drawable.bg_dot)
                }
            })
            val rv = vp.getChildAt(0)
            if (rv is RecyclerView) {rv.overScrollMode = RecyclerView.OVER_SCROLL_NEVER}

            btnAuthorize.setOnClickListener {
                startActivity(Intent(this@WelcomeActivity, AuthActivity::class.java))
            }
            btnSkip.setOnClickListener {
                startActivity(Intent(this@WelcomeActivity, MainActivity::class.java))
                finish()
            }
        }
    }

}