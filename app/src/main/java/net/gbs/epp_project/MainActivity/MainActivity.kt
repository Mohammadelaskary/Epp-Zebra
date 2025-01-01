package net.gbs.epp_project.MainActivity

import android.app.Activity
import android.app.Application
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import net.gbs.epp_project.Base.BaseViewModelFactory
import net.gbs.epp_project.Base.LocalStorage
import net.gbs.epp_project.MainActivity.ReloginDialog.ReloginDialog
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.Network.ApiFactory.BaseUrlProvider.Companion.updateBaseUrl
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.CommunicationData
import net.gbs.epp_project.Tools.FormatDateTime
import net.gbs.epp_project.Tools.LoadingDialog
import net.gbs.epp_project.Tools.LocaleHelper
import net.gbs.epp_project.Tools.Tools
import net.gbs.epp_project.Tools.Tools.warningDialog
import net.gbs.epp_project.Ui.SplashAndSignIn.SignInFragment
import net.gbs.epp_project.Ui.SplashAndSignIn.SignInFragment.Companion.USER
import net.gbs.epp_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(),ReloginDialog.OnReloginDialogButtonsClicked
//    ,
//    EMDKManager.EMDKListener
{
    companion object {

//        var scanner: Scanner? = null
//        fun setBaseUrl(protocol: String,ipAddress:String,portNum:String){
//            BASE_URL = "$protocol://$ipAddress:$portNum/api/GBSEPPWMS/"
//        }
        fun logOut(activity: Activity){
            USER = null
            deactivateSession()
            val navController = activity.findNavController(R.id.myNavhostfragment)
            navController.navigateUp()
            navController.navigate(R.id.signInFragment)
        }



        private lateinit var loginSessionJob: Job
        lateinit var reloginDialog:ReloginDialog
        const val sessionTime:Long = 30*60*1000
        fun startSession(){
            Log.d(TAG, "onstartSession: ")
            loginSessionJob = CoroutineScope(Dispatchers.IO).launch{
                delay(sessionTime)
                withContext(Dispatchers.Main){
                    reloginDialog.show()
                }
            }
        }
        private fun deactivateSession() {
            if (loginSessionJob.isActive)
                loginSessionJob.cancel()
        }


    }
//    private var emdkManager: EMDKManager? = null
//    private var barcodeManager: BarcodeManager? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private var loadingDialog :LoadingDialog? = null
    fun updateUrlOnScreen(){
        binding.ipPortNo.text = "${CommunicationData(this).getIpAddress()} (${CommunicationData(this).getPortNumber()})"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
        viewModel = ViewModelProvider(
            this,
            BaseViewModelFactory(this.application!!,this)
        )[MainActivityViewModel::class.java]
        reloginDialog = ReloginDialog(this,this)
        reloginDialog.setCancelable(false)
        if (loadingDialog==null)
            loadingDialog = LoadingDialog(this)
        loadingDialog!!.setCancelable(false)
//        val results = EMDKManager.getEMDKManager(
//            applicationContext, this
//        )

        observeLogin()


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)


        viewModel = ViewModelProvider(
            this,
            BaseViewModelFactory(application,this)
        )[MainActivityViewModel::class.java]
        LocaleHelper.onCreate(this)

        if (LocaleHelper.getLanguage(this).equals("ar")) {
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        } else {
            window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        }

        binding.backArrow.setOnClickListener { v-> onBackPressed() }
        binding.logout.setOnClickListener{v->
            logOut(this)
        }

    }

    private fun observeLogin() {
        viewModel.signInStatus.observe(this){
            when(it.status){
                Status.LOADING -> loadingDialog!!.show()
                Status.SUCCESS -> loadingDialog!!.hide()
                else -> {
                    loadingDialog!!.hide()
                    warningDialog(this,it.message)
                }
            }
            Log.d(TAG, "observeLogin: ${it.status}")
        }
        viewModel.signInLiveData.observe(this){
            USER = it
            startSession()
            try {
                if (FormatDateTime.compareTwoTimes(
                        it.serverDateTime!!.replace("T", " ").substring(0, 19),
                        viewModel.getDeviceTodayDate()
                    )
                ) {
                    reloginDialog.dismiss()
                    Tools.showSuccessAlerter(
                        getString(R.string.you_logged_in_successfully),
                        this
                    )
                } else {
                    warningDialog(
                        this,
                        getString(R.string.device_date_or_time_or_both_not_correct_please_correct_device_date_and_time_and_try_to_sign_in_again) + "\n" + getString(
                            R.string.correct_date_time_is
                        ) + "\n${FormatDateTime(it.serverDateTime!!.replace("T", " ").substring(0, 19)).year()}-${FormatDateTime(it.serverDateTime!!.replace("T", " ").substring(0, 19)).month()}-${
                            FormatDateTime(
                                it.serverDateTime!!.replace("T", " ").substring(0, 19)
                            ).day()
                        } ${FormatDateTime(it.serverDateTime!!.replace("T", " ").substring(0, 19)).hours()}:${FormatDateTime(it.serverDateTime!!.replace("T", " ").substring(0, 19)).minutes()}"
                    )
                }
            } catch (ex:Exception){
                Log.d(TAG, "observeSignIn: ${ex.message}")
            }
            SignInFragment.manualEnter = it.manualEnter
            SignInFragment.isAllowChangeQuantity = it.isAllowEditQuantity!!
        }
    }


    override fun onResume() {
        super.onResume()
//        checkDate()
        updateUrlOnScreen()

    }



    private fun checkDate(){
        Log.d(TAG, "checkDate: ${viewModel.getStoredActualDate()!!}--${viewModel.getStoredDeviceDate()}--${viewModel.getTodayDate()}")
            if (viewModel.getStoredActualDate()!!.isNotEmpty()&&viewModel.getStoredDeviceDate()!=viewModel.getTodayDate()){
                logOut(this)
            }

    }

    override fun onDestroy() {
        super.onDestroy()
        USER = null
    }

    override fun OnReloginButtonClicked(
        userName: String,
        password: String,
    ) {
        viewModel.logIn(userName,password)
    }

    override fun OnLogOutButtonClicked() {
        reloginDialog.dismiss()
        logOut(this)
    }
}