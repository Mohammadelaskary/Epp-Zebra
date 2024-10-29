package com.example.mobco.Model.ApiResponse

import com.google.gson.annotations.SerializedName

class ResponseStatusX() {
    @SerializedName("isSuccess"     ) var isSuccess     : Boolean? = null
    @SerializedName("statusCode"    ) var statusCode    : Int?     = null
    @SerializedName("statusMessage" ) var statusMessage : String?  = null
    @SerializedName("errorMessage"  ) var errorMessage  : String?  = null
}