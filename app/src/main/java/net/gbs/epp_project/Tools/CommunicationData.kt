package net.gbs.epp_project.Tools

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class CommunicationData(val activity: Activity) {
    val sharedPreferences = activity.getPreferences(Context.MODE_PRIVATE)
        private val PROTOCOL_TYPE_NAME = "protocol"
        private val IP_ADDRESS_NAME = "Ip_address"
        private val PORT_NUM_NAME = "port_num"
        fun getProtocol(): String {
            return if (sharedPreferences.getString(
                    PROTOCOL_TYPE_NAME,
                    null
                ) != null
            ) sharedPreferences.getString(
                PROTOCOL_TYPE_NAME, null
            )!! else "http"
        }

        fun getIpAddress(): String {
            return if (sharedPreferences.getString(
                    IP_ADDRESS_NAME,
                    null
                ) != null
            ) sharedPreferences.getString(
                IP_ADDRESS_NAME, null
            )!! else "41.196.137.3"
        }

        fun getPortNumber(): String {
            return if (sharedPreferences.getString(
                    PORT_NUM_NAME,
                    null
                ) != null
            ) sharedPreferences.getString(
                PORT_NUM_NAME, null
            )!! else "2216"
        }

        fun saveProtocol(protocol: String?) {
            sharedPreferences.edit().putString(PROTOCOL_TYPE_NAME, protocol).apply()
        }

        fun saveIPAddress(ipAddress: String?) {
            sharedPreferences.edit().putString(IP_ADDRESS_NAME, ipAddress).apply()
        }

        fun savePortNum( portNum: String?) {
            sharedPreferences.edit().putString(PORT_NUM_NAME, portNum).apply()
        }

}