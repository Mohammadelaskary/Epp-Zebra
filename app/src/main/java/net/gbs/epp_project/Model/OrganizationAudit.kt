package net.gbs.epp_project.Model

import com.google.gson.annotations.SerializedName

data class OrganizationAudit(
    @SerializedName("orgId"   ) var orgId   : Int?    = null,
    @SerializedName("orgCode" ) var orgCode : String? = null,
    @SerializedName("orgName" ) var orgName : String? = null
){
    override fun toString(): String {
        return "$orgCode - $orgName"
    }
}