package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.IssueOrderLists

class MoveOrderGetDetailsByHEADER_IDResponse (
    @SerializedName("issueOrderLists" ) var issueOrderLists : IssueOrderLists = IssueOrderLists()
): BaseResponse<IssueOrderLists>() {
    override fun getData(): IssueOrderLists {
        return issueOrderLists
    }

}
