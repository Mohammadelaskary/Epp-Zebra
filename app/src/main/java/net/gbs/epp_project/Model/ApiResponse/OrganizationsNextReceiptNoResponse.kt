package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse

class OrganizationsNextReceiptNoResponse (
    @SerializedName("receiptNo") val receiptNo: String
        ): BaseResponse<String>() {
    override fun getData(): String {
        return receiptNo
    }

}
