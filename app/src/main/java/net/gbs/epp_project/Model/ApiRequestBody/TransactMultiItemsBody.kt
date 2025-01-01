package net.gbs.epp_project.Model.ApiRequestBody

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Model.Line
import net.gbs.epp_project.Model.MoveOrderLine
import net.gbs.epp_project.Model.TransactMultiLine

data class TransactMultiItemsBody (
    @SerializedName("org_id"                 ) var orgId                : Int?             = null,
    @SerializedName("lines"                  ) var lines                : List<TransactMultiLine> = listOf(),
    @SerializedName("user_id"                ) var userId               : Int?             = null,
    @SerializedName("employee_id"            ) var employeeId           : Int?             = null,
    @SerializedName("program_application_id" ) var programApplicationId : Int?             = null,
    @SerializedName("program_id"             ) var programId            : Int?             = null,
    @SerializedName("transaction_date"       ) var transactionDate      : String?          = null,
    @SerializedName("deviceSerialNo"         ) var deviceSerialNo       : String?          = null,
    @SerializedName("applang"                ) var applang              : String?          = null,
    @SerializedName("is_FinalProduct"        ) var isFinalProduct       : Boolean?         = null
)
