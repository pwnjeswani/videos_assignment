package com.pawanjeswani.videosAssignment.view.utils

import android.util.DisplayMetrics
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import kotlin.math.roundToInt

fun ImageView.setDrawable(source: Int) {
    this.setImageDrawable(
        ContextCompat.getDrawable(
            this.context,
            source
        )
    )
}

fun dpToPx(dp:Int) = (dp * DisplayMetrics().density).roundToInt()

