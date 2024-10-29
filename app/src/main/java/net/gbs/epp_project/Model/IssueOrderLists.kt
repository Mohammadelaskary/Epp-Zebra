package net.gbs.epp_project.Model

import com.google.gson.annotations.SerializedName

class IssueOrderLists (
    @SerializedName("getMoveOrderLinesList" ) var getMoveOrderLinesList : ArrayList<IssueOrderReportItem> = arrayListOf(),
    @SerializedName("getOnHandList"         ) var getOnHandList         : ArrayList<OnHandIssueOrderItem>         = arrayListOf()
)
