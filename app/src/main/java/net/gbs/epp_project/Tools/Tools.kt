package net.gbs.epp_project.Tools

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import com.tapadoo.alerter.Alerter
import net.gbs.epp_project.MainActivity.MainActivity
import net.gbs.epp_project.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Tools {
    fun hideToolBar(mainActivity: AppCompatActivity) {
        mainActivity.supportActionBar!!.hide()
    }

    fun showToolBar(mainActivity: AppCompatActivity) {
        mainActivity.supportActionBar!!.show()
    }
    fun showToolBar(activity: Activity){
        val toolBar = activity.findViewById<ConstraintLayout>(R.id.tool_bar)
        toolBar?.visibility = View.VISIBLE
    }
    fun hideToolBar(activity: Activity){
        val toolBar = activity.findViewById<ConstraintLayout>(R.id.tool_bar)
        toolBar?.visibility = View.GONE
    }
    fun showLogOutButton(activity: Activity){
        val logOutButton = activity.findViewById<MaterialButton>(R.id.logout)
        logOutButton?.visibility = View.VISIBLE
    }
    fun hideLogOutButton(activity: Activity){
        val logOutButton = activity.findViewById<MaterialButton>(R.id.logout)
        logOutButton?.visibility = View.INVISIBLE
    }

    fun showBackButton(activity: Activity){
        val back = activity.findViewById<MaterialButton>(R.id.back_arrow)
        back?.visibility = View.VISIBLE
    }
    fun hideBackButton(activity: Activity){
        val back = activity.findViewById<MaterialButton>(R.id.back_arrow)
        back?.visibility = View.INVISIBLE
    }
    fun changeTitle(mainTitle: String?, mainActivity: MainActivity) {
        mainActivity.supportActionBar!!.title = mainTitle
    }

    fun changeFragmentTitle(title: String, activity: Activity){
        val titleTextView = activity.findViewById<TextView>(R.id.title)
        titleTextView.text = title
    }

    fun warningDialog(context: Context, message: String) {
        CustomDialog(context, message, R.drawable.ic_warning_alert).show()
    }
    fun containsOnlyDigits(s: String): Boolean {
        return s.matches(Regex("\\d+"))
    }
    fun errorDialog(context: Context?, message: String?) {
        CustomDialog(context!!, message!!, R.drawable.baseline_error_outline_24).show()
    }

    fun successDialog(context: Context, message: String) {
        CustomDialog(context, message, R.drawable.ic_done).show()
    }

    fun loadingProgressDialog(context: Context): ProgressDialog {
        val progressDialog = ProgressDialog(context)
        progressDialog.setCancelable(false)
        progressDialog.setMessage(context.getString(R.string.loading_3dots))
        return progressDialog
    }

    fun showSuccessAlerter(message: String, activity: Activity) {
        Alerter.create(activity).setText(message)
            .setIcon(R.drawable.ic_done)
            .setBackgroundColorInt(activity.resources.getColor(R.color.alerter_success_color))
            .setDuration(1000)
            .setTextAppearance(R.style.alerter_text_appearance)
            .setEnterAnimation(com.tapadoo.alerter.R.anim.alerter_slide_in_from_top)
            .setExitAnimation(com.tapadoo.alerter.R.anim.alerter_slide_out_to_top)
            .show()
    }

    fun showErrorAlerter(message: String, activity: Activity) {
        Alerter.create(activity).setText(message)
            .setIcon(com.google.android.material.R.drawable.mtrl_ic_error)
            .setBackgroundColorInt(activity.resources.getColor(R.color.alerter_error_color))
            .setDuration(1000)
            .setTextAppearance(R.style.alerter_text_appearance)
            .setEnterAnimation(com.tapadoo.alerter.R.anim.alerter_slide_in_from_top)
            .setExitAnimation(com.tapadoo.alerter.R.anim.alerter_slide_out_to_top)
            .show()
    }

    fun getEditTextText(editInputLayout: TextInputLayout):String{
        return editInputLayout.editText?.text.toString().trim()
    }
    fun back(fragment: Fragment?) {
        val navController = NavHostFragment.findNavController(fragment!!)
        navController.popBackStack()
    }

    fun clearInputLayoutError(vararg inputLayouts: TextInputLayout) {
        for (inputLayout in inputLayouts) {
            inputLayout.editText!!.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    inputLayout.error = null
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    inputLayout.error = null
                }

                override fun afterTextChanged(s: Editable) {
                    inputLayout.error = null
                }
            })
        }
    }

    fun attachButtonsToListener(
        listener: View.OnClickListener?,
        vararg materialButtons: MaterialButton
    ) {
        for (button in materialButtons) {
            button.setOnClickListener(listener)
        }

    }

    fun datePicker(context: Context,inputLayout: TextInputLayout): MaterialDatePicker<Long> {
        val builder = MaterialDatePicker.Builder.datePicker()
        builder.setTitleText(context.getString(R.string.select_a_date))
        val datePicker = builder.build()
        datePicker.addOnPositiveButtonClickListener {
//            val calendar = Calendar.getInstance()
//            calendar.time = Date(it)
            val date = Date()
            date.time = it
            val selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(date)
            inputLayout.editText?.setText(selectedDate)
        }
        return datePicker
    }
}