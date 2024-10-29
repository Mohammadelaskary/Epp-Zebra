package net.gbs.epp_project.Model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class LocatorAudit(
    @SerializedName("locatorId"        ) var locatorId        : Int?    = null,
    @SerializedName("locatorCode"      ) var locatorCode      : String? = null,
    @SerializedName("buildingCode"     ) var buildingCode     : String? = null,
    @SerializedName("subInventoryId"   ) var subInventoryId   : Int?    = null,
    @SerializedName("subInventoryCode" ) var subInventoryCode : String? = null,
    @SerializedName("subInventoryDesc" ) var subInventoryDesc : String? = null,
    @SerializedName("orgId"            ) var orgId            : Int?    = null,
    @SerializedName("orgCode"          ) var orgCode          : String? = null,
    @SerializedName("orgName"          ) var orgName          : String? = null
){
    override fun toString(): String {
        return locatorCode.toString()
    }
    companion object {
        fun toJson(locator: LocatorAudit):String{
            return Gson().toJson(locator)
        }
        fun fromJson(locator:String):LocatorAudit{
            return Gson().fromJson(locator,LocatorAudit::class.java)
        }
    }
}