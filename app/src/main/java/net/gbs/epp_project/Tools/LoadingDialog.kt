package net.gbs.epp_project.Tools

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import net.gbs.epp_project.R

class LoadingDialog(private val context: Context) : Dialog(context,) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
        setCancelable(false)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}