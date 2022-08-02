package com.pawanjeswani.videosAssignment.view.utils

import android.widget.ImageView
import androidx.core.content.ContextCompat

fun ImageView.setDrawable(source: Int) {
    this.setImageDrawable(
        ContextCompat.getDrawable(
            this.context,
            source
        )
    )
}