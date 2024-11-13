package net.gbs.epp_project.Model.ApiRequestBody

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Model.POLineReturn

class ReturnMaterialBody (
    @SerializedName("po_header_id"           ) var poHeaderId           : Int?               = null,
    @SerializedName("po_lines"               ) var poLines              : ArrayList<POLineReturn> = arrayListOf(),
    @SerializedName("user_id"                ) var userId               : Int?               = null,
    @SerializedName("employee_id"            ) var employeeId           : Int?               = null,
    @SerializedName("program_application_id" ) var programApplicationId : Int?               = null,
    @SerializedName("program_id"             ) var programId            : Int?               = null,
    @SerializedName("transaction_date"       ) var transactionDate      : String?            = null,
    @SerializedName("deviceSerialNo"         ) var deviceSerialNo       : String?            = null,
    @SerializedName("applang"                ) var applang              : String?            = null
)
