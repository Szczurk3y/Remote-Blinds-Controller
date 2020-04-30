package com.szczurk3y.blindsanimation

import android.view.View
import android.widget.ImageView
import kotlin.properties.Delegates

data class Blind(
    var blind: ImageView? = null,
    var blindCoverPercentage: Int = 0
)
