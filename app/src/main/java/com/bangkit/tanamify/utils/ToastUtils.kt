package com.bangkit.tanamify.utils

import android.content.Context
import android.widget.Toast

object ToastUtils {
    private var toast: Toast? = null

    fun showToast(context: Context, message: String) {
        if (toast != null) {
            toast?.cancel()
        }
        toast = Toast.makeText(context.applicationContext, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

    fun cancelToast() {
        toast?.cancel()
    }
}
