package net.gbs.epp_project.Model.ApiRequestBody

import com.google.gson.annotations.SerializedName

data class MobileLogBody (
    @SerializedName("userId"       ) var userId       : String?  = null,
    @SerializedName("apiName"      ) var apiName      : String?  = null,
    @SerializedName("errorMessage" ) var errorMessage : String?  = null,
    @SerializedName("notes"        ) var notes        : String?  = null,
    @SerializedName("flag"         ) var flag         : Boolean? = null
)
