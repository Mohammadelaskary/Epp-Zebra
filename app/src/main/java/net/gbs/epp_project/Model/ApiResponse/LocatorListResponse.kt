package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.Locator
import net.gbs.epp_project.Model.LocatorAudit

class LocatorListResponse (
    @SerializedName("getList") val locatorList: List<Locator>
        ) : BaseResponse<List<Locator>>() {
    override fun getData(): List<Locator> {
        return locatorList
    }

}
