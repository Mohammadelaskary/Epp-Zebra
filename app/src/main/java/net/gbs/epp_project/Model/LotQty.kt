package net.gbs.epp_project.Model

import com.google.gson.annotations.SerializedName

data class LotQty (
    @SerializedName("lotName" ) var lotName : String? = null,
    @SerializedName("qty"     ) var qty     : Double?    = null
){
    override fun toString(): String {
        return "$lotName ( $qty )"
    }
}