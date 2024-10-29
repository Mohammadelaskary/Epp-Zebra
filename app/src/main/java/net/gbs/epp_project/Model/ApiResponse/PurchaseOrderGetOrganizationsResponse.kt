package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.Organization
import net.gbs.epp_project.Model.OrganizationAudit


class PurchaseOrderGetOrganizationsResponse (
    @SerializedName("getList") val  organizations : List<Organization>
        ): BaseResponse<List<Organization>>() {
    override fun getData(): List<Organization> {
        return organizations
    }
}
