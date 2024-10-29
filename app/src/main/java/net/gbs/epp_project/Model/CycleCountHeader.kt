package net.gbs.epp_project.Model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class CycleCountHeader (
    @SerializedName("id"                ) var id                : Int?                         = null,
    @SerializedName("userId"            ) var userId            : String?                      = null,
    @SerializedName("deviceSerial"      ) var deviceSerial      : String?                      = null,
    @SerializedName("dt"                ) var dt                : String?                      = null,
    @SerializedName("tm"                ) var tm                : String?                      = null,
    @SerializedName("org_id"            ) var orgId             : Int?                         = null,
    @SerializedName("locatorId"         ) var locatorId         : String?                      = null,
    @SerializedName("itemId"            ) var itemId            : String?                      = null,
    @SerializedName("itemCode"          ) var itemCode          : String?                      = null,
    @SerializedName("isClosed"          ) var isClosed          : Boolean?                     = null,
    @SerializedName("userIdClosed"      ) var userIdClosed      : String?                      = null,
    @SerializedName("dateClosed"        ) var dateClosed        : String?                      = null,
    @SerializedName("cycleCountDetails" ) var cycleCountDetails : ArrayList<CycleCountDetails> = arrayListOf()

) {
    companion object {
        fun toJson(header :CycleCountHeader):String{
            return Gson().toJson(header)
        }
        fun fromJson(header :String):CycleCountHeader{
            return Gson().fromJson(header,CycleCountHeader::class.java)
        }
    }
}