package com.sm.dinio.utils.extensions

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.github.islamkhsh.CardSliderViewPager


fun CardSliderViewPager.removeOverScroll(){
    (getChildAt(0) as RecyclerView).removeOverScroll()
}

fun ViewPager2.removeOverScroll(){
    (getChildAt(0) as RecyclerView).removeOverScroll()
}

fun RecyclerView.removeOverScroll(){
    overScrollMode = RecyclerView.OVER_SCROLL_NEVER
    isVerticalScrollBarEnabled = false
    isHorizontalScrollBarEnabled = false
}



