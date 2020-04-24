package com.szczurk3y.blindsanimation

import android.view.animation.Animation
import android.widget.ImageView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo

class PointingHandAnimationListener(val item: ImageView) : Animation.AnimationListener {
    override fun onAnimationRepeat(p0: Animation?) {
        YoYo.with(Techniques.FadeInUp)
            .playOn(item)
    }

    override fun onAnimationEnd(p0: Animation?) {
        item.clearAnimation()
        item.alpha = 0f
    }

    override fun onAnimationStart(p0: Animation?) {
        YoYo.with(Techniques.FadeInUp)
            .playOn(item)
    }
}