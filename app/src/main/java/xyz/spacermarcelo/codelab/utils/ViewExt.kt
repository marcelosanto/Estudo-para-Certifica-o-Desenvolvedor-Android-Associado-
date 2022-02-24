package xyz.spacermarcelo.codelab.utils

import android.view.View
import android.view.animation.Animation

fun View.startAnimation(anim: Animation, onEnd: () -> Unit) {
    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(anim: Animation?)  = onEnd()

        override fun onAnimationEnd(anim: Animation?)  = Unit


        override fun onAnimationRepeat(anim: Animation?) = Unit

    })

    this.startAnimation(anim)
}

fun View.setVisible(show: Boolean){
    if (show) this.visibility = View.VISIBLE else this.visibility = View.GONE
}