package net.gbs.epp_project.Model.ApiRequestBody

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Model.Line

data class AllocateItemsBody (
    @SerializedName("org_id"                 ) var orgId                : Int?             = null,
    @SerializedName("lines"                  ) var lines                : ArrayList<Line> = arrayListOf(),
    @SerializedName("user_id"                ) var userId               : Int?             = null,
    @SerializedName("employee_id"            ) var employeeId           : Int?             = null,
    @SerializedName("program_application_id" ) var programApplicationId : Int?             = null,
    @SerializedName("program_id"             ) var programId            : Int?             = null,
    @SerializedName("deviceSerialNo"         ) var deviceSerialNo       : String?          = null,
    @SerializedName("transaction_date"       ) var transaction_date       : String?          = null,
    @SerializedName("applang"                ) var appLang              : String?          = null
)