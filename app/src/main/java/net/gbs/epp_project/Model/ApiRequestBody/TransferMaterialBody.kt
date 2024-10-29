package net.gbs.epp_project.Model.ApiRequestBody

import com.google.gson.annotations.SerializedName

data class TransferMaterialBody (
    @SerializedName("organization_id"       ) var organizationId       : Int?    = null,
    @SerializedName("inventory_Item_Id"     ) var inventoryItemId      : Int?    = null,
    @SerializedName("subinventoryCode_From" ) var subinventoryCodeFrom : String? = null,
    @SerializedName("locatorCode_From"      ) var locatorCodeFrom      : String? = null,
    @SerializedName("subinventoryCode_To"   ) var subinventoryCodeTo   : String? = null,
    @SerializedName("locatorCode_To"        ) var locatorCodeTo        : String? = null,
    @SerializedName("lot_num"               ) var lotNum               : String? = null,
    @SerializedName("qty"                   ) var qty                  : Double? = null,
    @SerializedName("user_id"               ) var userId               : Int?    = null,
    @SerializedName("employee_id"           ) var employeeId           : Int?    = null,
    @SerializedName("transaction_date"      ) var transactionDate      : String? = null,
    @SerializedName("userID"                ) var userID               : Int?    = null,
    @SerializedName("deviceSerialNo"        ) var deviceSerialNo       : String? = null,
    @SerializedName("applang"               ) var applang              : String? = null

)