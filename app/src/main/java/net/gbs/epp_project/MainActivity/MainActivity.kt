package net.gbs.epp_project.MainActivity

import android.app.Activity
import android.app.Application
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController

import net.gbs.epp_project.Base.BaseViewModelFactory
import net.gbs.epp_project.Model.Status
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.CommunicationData
import net.gbs.epp_project.Tools.LoadingDialog
import net.gbs.epp_project.Tools.LocaleHelper
import net.gbs.epp_project.Tools.Tools.warningDialog
import net.gbs.epp_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity()
//    ,
//    EMDKManager.EMDKListener
{
    companion object {
        var BASE_URL = ""
//        var scanner: Scanner? = null
        fun setBaseUrl(protocol: String,ipAddress:String,portNum:String){
            BASE_URL = "$protocol://$ipAddress:$portNum/api/GBSEPPWMS/"
        }
        fun logOut(activity: Activity){
            val navController = activity.findNavController(R.id.myNavhostfragment)
            navController.navigateUp()
            navController.navigate(R.id.signInFragment)
        }
    }
//    private var emdkManager: EMDKManager? = null
//    private var barcodeManager: BarcodeManager? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var loadingDialog :LoadingDialog
    private lateinit var communicationData: CommunicationData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        communicationData = CommunicationData(this)
        setBaseUrl(communicationData.getProtocol(),communicationData.getIpAddress(),communicationData.getPortNumber())
        Log.d(TAG, "hasInternetConnectionOnCreate: ")
        loadingDialog = LoadingDialog(this)
        loadingDialog.setCancelable(false)
//        val results = EMDKManager.getEMDKManager(
//            applicationContext, this
//        )



        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)


        viewModel = ViewModelProvider(
            this,
            BaseViewModelFactory(application,this)
        )[MainActivityViewModel::class.java]
        observeGettingDate()
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

    private fun observeGettingDate() {
//        viewModel.getDateStatus.observe(this){
//            when(it.status){
//                Status.LOADING -> loadingDialog.show()
//                Status.SUCCESS -> loadingDialog.hide()
//                else -> {
//                    loadingDialog.hide()
//                    warningDialog(this,
//                        getString(R.string.error_in_server_s_date_please_contact_network_administrator))
//                }
//            }
//        }
    }

//    override fun onOpened(emdkManager: EMDKManager?) {
//        Log.d(TAG, "EMDKonOpened: ")
//        this.emdkManager = emdkManager
//        try {
//            initializeScanner()
//        } catch (e: ScannerException) {
//            e.printStackTrace()
//        }
//    }
//
//
//
//    override fun onClosed() {
//    }
//
//    // Method to initialize and enable Scanner and its listeners
//    @Throws(ScannerException::class)
//    private fun initializeScanner() {
//        if (scanner == null) {
//            // Get the Barcode Manager object
//            barcodeManager = emdkManager!!
//                .getInstance(EMDKManager.FEATURE_TYPE.BARCODE) as BarcodeManager
//            // Get default scanner defined on the device
//            scanner = barcodeManager!!.getDevice(BarcodeManager.DeviceIdentifier.DEFAULT)
//            // Add data and status listeners
////            scanner!!.addDataListener(this)
////            scanner!!.addStatusListener(this)
//            // Hard trigger. When this mode is set, the user has to manually
//            // press the trigger on the device after issuing the read call.
//
//        }
//    }

    override fun onDestroy() {
        super.onDestroy()
//        if (emdkManager != null) {
//
//// Clean up the objects created by EMDK manager
//            emdkManager!!.release()
//            emdkManager = null


//        }
    }

    override fun onResume() {
        super.onResume()
        checkDate()
    }

    private fun checkDate(){
//        if (viewModel.getStoredActualDate()==null){
//            logOut(this)
//        } else {
        Log.d(TAG, "checkDate: ${viewModel.getStoredActualDate()!!}--${viewModel.getStoredDeviceDate()}--${viewModel.getTodayDate()}")
            if (viewModel.getStoredActualDate()!!.isNotEmpty()&&viewModel.getStoredDeviceDate()!=viewModel.getTodayDate()){
                logOut(this)
            }
//        }
    }
}