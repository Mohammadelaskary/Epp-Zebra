package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.OrganizationAudit

class OrganizationAuditListResponse(
    @SerializedName("getList"        ) var getList        : ArrayList<OrganizationAudit> = arrayListOf()
): BaseResponse<List<OrganizationAudit>>() {
    override fun getData(): List<OrganizationAudit> {
        return getList
    }

}
