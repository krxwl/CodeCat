package com.github.krxwl.codecat.customspinner

import android.graphics.Bitmap

class CustomItem(var spinnerItemId: Int, var spinnerItemName: String, var spinnerItemImage: Bitmap) {

    fun getCustomSpinnerItemName(): String {
        return spinnerItemName
    }

    fun getCustomSpinnerItemImage(): Bitmap {
        return spinnerItemImage
    }

    fun getCustomSpinnerId(): Int {
        return spinnerItemId
    }

}