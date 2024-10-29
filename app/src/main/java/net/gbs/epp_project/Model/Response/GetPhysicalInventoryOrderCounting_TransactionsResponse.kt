package net.gbs.epp_project.Model.Response

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.AuditTransaction

class GetPhysicalInventoryOrderCounting_TransactionsResponse(
    @SerializedName("getList") val transactionsList: List<AuditTransaction>
) :BaseResponse<List<AuditTransaction>>(){
    override fun getData(): List<AuditTransaction> = transactionsList
}
