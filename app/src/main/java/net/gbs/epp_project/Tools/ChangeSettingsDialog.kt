package net.gbs.epp_project.Tools

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View


import kotlinx.coroutines.*
import net.gbs.epp_project.MainActivity.MainActivity
import net.gbs.epp_project.R
import net.gbs.epp_project.Tools.Tools.loadingProgressDialog
import net.gbs.epp_project.Tools.Tools.warningDialog
import net.gbs.epp_project.databinding.ChangeSettingsDialogLayoutBinding

import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class ChangeSettingsDialog(context: Context, activity: Activity,val onButtonsClicked: OnButtonsClicked) :
    Dialog(context), View.OnClickListener {
    private val application: Application
    private val activity: Activity
    private lateinit var binding: ChangeSettingsDialogLayoutBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var communicationData: CommunicationData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChangeSettingsDialogLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        communicationData = CommunicationData(activity)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        val metrics: DisplayMetrics = context.resources.displayMetrics
        val width: Int = metrics.widthPixels
        val height: Int = metrics.heightPixels
        this.window?.setLayout(6 * width / 7, 4 * height / 7)
        progressDialog = loadingProgressDialog(context)

        when (communicationData.getProtocol()) {
            "http" -> binding.http.setChecked(true)
            "https" -> binding.https.setChecked(true)
        }
        binding.ip.editText?.setText(communicationData.getIpAddress())
        binding.port.editText?.setText(communicationData.getPortNumber())
        binding.save.setOnClickListener(this)

    }

    override fun setOnDismissListener(listener: DialogInterface.OnDismissListener?) {
        super.setOnDismissListener(listener)
        val protocolId: Int = binding.protocol.getCheckedRadioButtonId()
        when (protocolId) {
            R.id.http -> protocol = "http"
            R.id.https -> protocol = "https"
        }
        ipAddress = binding.ip.editText?.text.toString().trim()
        portNum = binding.port.editText?.text.toString().trim()
        if (!ipAddress!!.isEmpty()) {
            onButtonsClicked.OnSaveButtonClicked(protocol!!,ipAddress!!,portNum)
        } else binding.ip.setError(application.getString(R.string.please_enter_ip_address))
    }

    private var ipAddress: String? = null
    private var portNum = ""
    private var protocol: String? = null

    init {
        this.application = activity.application
        this.activity = activity
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.save -> {
                val protocolId: Int = binding.protocol.getCheckedRadioButtonId()
                when (protocolId) {
                    R.id.http -> protocol = "http"
                    R.id.https -> protocol = "https"
                }
                ipAddress = binding.ip.editText?.text.toString().trim()
                portNum = binding.port.editText?.text.toString().trim()
                if (!ipAddress!!.isEmpty()) {
                        onButtonsClicked.OnSaveButtonClicked(protocol!!,ipAddress!!,portNum)
                } else binding.ip.setError(application.getString(R.string.please_enter_ip_address))
            }
        }
    }

//    suspend fun hasInternetConnection(newBaseUrl: String) {
////        return Single.fromCallable {
//            var isOnline = false
//            try {
//                val url = URL(newBaseUrl)
//                val urlc = url.openConnection() as HttpURLConnection
//                urlc.setRequestProperty(
//                    "User-Agent",
//                    "Android Application:" + Build.VERSION.SDK_INT
//                )
//                urlc.setRequestProperty("Connection", "close")
//                urlc.connectTimeout = 1000 * 30 // mTimeout is in seconds
//                urlc.connect()
//                if (urlc.responseCode == 200) {
//                    isOnline = true
//                }
//            } catch (e1: IOException) {
//                e1.printStackTrace()
//                isOnline = false
//            }
//            isOnline
//        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
//            .doOnEvent { aBoolean, throwable ->
//                progressDialog.dismiss()
//                if (aBoolean) {
//                    CommunicationData.saveProtocol(application, protocol)
//                    CommunicationData.saveIPAddress(application, ipAddress)
//                    CommunicationData.savePortNum(application, portNum)
//                    showSuccessAlerter(context.getString(R.string.saved_successfully), activity)
//                    refreshUi(activity as MainActivity)
//                } else warningDialog(context, context.getString(R.string.wrong_ip))
//            }
//    }
    fun hasInternetConnection(newBaseUrl: String){
        progressDialog.show()
    Log.d(TAG, "hasInternetConnection: $newBaseUrl")
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL(newBaseUrl)
                val urlc = url.openConnection() as HttpURLConnection
                urlc.setRequestProperty(
                    "User-Agent",
                    "Android Application:" + Build.VERSION.SDK_INT
                )
                urlc.setRequestProperty("Connection", "close")
                urlc.connectTimeout = 1000 * 30 // mTimeout is in seconds
                urlc.connect()
                print(urlc.responseCode)
                Log.d(TAG, "hasInternetConnection: ${urlc.responseCode}")
                if (urlc.responseCode == 200) {
                    communicationData.saveProtocol(protocol)
                    communicationData.saveIPAddress(ipAddress)
                    communicationData.savePortNum(portNum)

                    withContext(Dispatchers.Main) {
                        dismiss()
//                        successDialog(activity, activity.getString(R.string.saved_successfully))
                        Tools.showSuccessAlerter(activity.getString(R.string.saved_successfully),activity)
                        delay(1000L)
                        refreshUi(activity as MainActivity)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        warningDialog(context, context.getString(R.string.wrong_ip))
                        dismiss()
                        Tools.showErrorAlerter(activity.getString(R.string.wrong_ip),activity)
                    }
                }
            } catch (e1: IOException) {
                e1.printStackTrace()
                withContext(Dispatchers.Main) {
                    warningDialog(context, context.getString(R.string.wrong_ip))
                    dismiss()
                    Tools.showErrorAlerter(activity.getString(R.string.wrong_ip),activity)
                }
            }
            withContext(Dispatchers.Main) {
                progressDialog.dismiss()
            }
        }
    }
    companion object {
        fun refreshUi(activity: MainActivity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
    }

    interface OnButtonsClicked {
        fun OnSaveButtonClicked (protocol: String,ipAddress:String,portNum:String)
        fun OnDialogDismessed (protocol: String,ipAddress:String,portNum:String)
    }
}