package net.gbs.epp_project.Model.Response

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.LocatorAudit

class GetLocatorListResponse (
        @SerializedName("getList") val locatorList:List<LocatorAudit>
        ):BaseResponse<List<LocatorAudit>>(){
    override fun getData(): List<LocatorAudit>  = locatorList
}
