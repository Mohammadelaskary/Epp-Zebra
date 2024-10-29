package net.gbs.epp_project.Model

import com.google.gson.annotations.SerializedName

data class Organization(
    @SerializedName("organization_id"   ) var organizationId   : Int?    = null,
    @SerializedName("organization_code" ) var organizationCode : String? = null,
    @SerializedName("organization_name" ) var organizationName : String? = null
){
    override fun toString(): String {
        return "$organizationName"
    }
}
