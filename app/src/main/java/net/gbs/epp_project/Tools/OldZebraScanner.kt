package net.gbs.epp_project.Tools

//import android.content.ContentValues
//import android.util.Log
//import com.symbol.emdk.barcode.ScanDataCollection
//import com.symbol.emdk.barcode.Scanner
//import com.symbol.emdk.barcode.Scanner.DataListener
//import com.symbol.emdk.barcode.Scanner.StatusListener
//import com.symbol.emdk.barcode.StatusData

class OldZebraScanner(
//    private val scanner: Scanner,
//    val dataListener: DataListener, val statusListener: StatusListener
) {
//    fun onResume(){
//        try {
//            scanner.addDataListener(dataListener)
//            scanner.addStatusListener(statusListener)
//            scanner.triggerType = Scanner.TriggerType.HARD
//            // Enable the scanner
//            scanner.enable()
//            // Starts an asynchronous Scan. The method will not turn ON the
//            // scanner. It will, however, put the scanner in a state in which
//            // the scanner can be turned ON either by pressing a hardware
//            // trigger or can be turned ON automatically.
//            if (statusData?.state?.name !="Already scanning. Wait for current scanning to complete.")
//                scanner.read()
//        } catch (ex:Exception){
////            warningDialog(requireContext(),"Error in initializing scanner")
//        }
//    }
//    fun onPause(){
//        try {
//            scanner.removeDataListener(dataListener)
//        } catch (ex:Exception){
////            warningDialog(requireContext(),"Error in initializing scanner")
//        }
//    }
//    fun onData(scanDataCollection: ScanDataCollection?):String{
//        Log.d(ContentValues.TAG, "onStatusDataRead: ${statusData?.state?.name}")
//        return scanDataCollection?.scanData?.get(0)?.data ?: ""
//    }
//    fun restartReadData(){
////        try {
////            MainActivity.scanner?.disable()
////            MainActivity.scanner?.cancelRead()
////            MainActivity.scanner?.enable()
////            // Starts an asynchronous Scan. The method will not turn ON the
////            // scanner. It will, however, put the scanner in a state in which
////            // the scanner can be turned ON either by pressing a hardware
////            // trigger or can be turned ON automatically.
////            if (statusData?.state?.name != "Already scanning. Wait for current scanning to complete.")
////                MainActivity.scanner?.read()
////        } catch (ex :Exception){
////            Log.d(ContentValues.TAG, "onStatusError: ${statusData?.state?.name}")
////        }
//    }
//    private var statusData: StatusData? = null
//    fun onStatus(statusData: StatusData?){
//        this.statusData = statusData
//        Log.d(ContentValues.TAG, "onStatus: ${statusData?.state?.name}")
//        try {
//            if (statusData!!.state.name == "IDLE"){
//                scanner.enable()
////                // Starts an asynchronous Scan. The method will not turn ON the
////                // scanner. It will, however, put the scanner in a state in which
////                // the scanner can be turned ON either by pressing a hardware
////                // trigger or can be turned ON automatically.
//                scanner.read()
//            }
//        } catch (ex:Exception){
////            warningDialog(requireContext(),"Error in initializing scanner")
//            Log.d(ContentValues.TAG, "onStatus: ${ex.message}")
//        }
//    }
}