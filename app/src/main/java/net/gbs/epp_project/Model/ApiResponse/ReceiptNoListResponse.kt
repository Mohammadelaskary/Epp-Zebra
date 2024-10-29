package net.gbs.epp_project.Model.ApiResponse

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import java.io.Serial

class ReceiptNoListResponse(
    @SerializedName("getList") val receiptNoList: List<String>
) : BaseResponse<List<String>>() {
    override fun getData(): List<String> {
        val re = Regex("[^A-Za-z0-9 ]")
        receiptNoList.forEach {
            re.replace(it,"")
        }
        return receiptNoList
    }

}
