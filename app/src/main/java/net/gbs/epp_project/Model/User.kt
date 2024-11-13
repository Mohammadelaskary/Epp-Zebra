package net.gbs.epp_project.Model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("userId"              ) var notOracleUserId              : String?  = null,
    @SerializedName("userName"            ) var userName            : String?  = null,
    @SerializedName("password"            ) var password            : String?  = null,
    @SerializedName("discriminator"       ) var discriminator       : String?  = null,
    @SerializedName("isMobileUser"        ) var isMobileUser        : Boolean? = null,
    @SerializedName("isPrintingApp"       ) var isPrintingApp       : Boolean? = null,
    @SerializedName("isActive"            ) var isActive            : Boolean? = null,
    @SerializedName("scanMode"            ) var scanMode            : Int?  = null,
    @SerializedName("employeeName"        ) var employeeName        : String?  = null,
    @SerializedName("isInspection"        ) var isInspection        : Boolean? = null,
    @SerializedName("isScanBarcode"       ) var isScanBarcode       : Boolean? = null,
    @SerializedName("isManualEntry"       ) var isManualEntry       : Boolean? = null,
    @SerializedName("isAllowEditQuantity" ) var isAllowEditQuantity : Boolean? = null,
    @SerializedName("isReceive"           ) var isReceive           : Boolean? = null,
    @SerializedName("isDeliver"           ) var isDeliver           : Boolean? = null,
    @SerializedName("isDeliverRejected"   ) var isDeliverRejected   : Boolean? = null,
    @SerializedName("isItemPos"           ) var isItemPos           : Boolean? = null,
    @SerializedName("isFactory"           ) var isFactory           : Boolean? = null,
    @SerializedName("isIndirectChemical"  ) var isIndirectChemical  : Boolean? = null,
    @SerializedName("isSpareParts"        ) var isSpareParts        : Boolean? = null,
    @SerializedName("isIssueFinalProduct" ) var isIssueFinalProduct : Boolean? = null,
    @SerializedName("isReturnToVendor"    ) var isReturnToVendor    : Boolean? = null,
    @SerializedName("isReturnToWarehouse" ) var isReturnToWarehouse : Boolean? = null,
    @SerializedName("isTransfer"          ) var isTransfer          : Boolean? = null,
    @SerializedName("isCycleCount"        ) var isCycleCount        : Boolean? = null,
    @SerializedName("isPhysicalInventory" ) var isPhysicalInventory : Boolean? = null,
    @SerializedName("isItemInfo"          ) var isItemInfo          : Boolean? = null,
    @SerializedName("normalizedUserName"  ) var normalizedUserName  : String?  = null,
    @SerializedName("email"               ) var email               : String?  = null,
    @SerializedName("normalizedEmail"     ) var normalizedEmail     : String?  = null,
    @SerializedName("emailConfirmed"      ) var emailConfirmed      : Boolean? = null,
    @SerializedName("phoneNumber"         ) var phoneNumber         : String?  = null,
    @SerializedName("oracleUserId"        ) var userId              : Int?     = null,
    @SerializedName("serverDateTime"      ) var serverDateTime      : String?  = "26-09-2024",
    @SerializedName("isShowErrorMessage"  ) var isShowErrorMessage  : Boolean? = null,
) {
    val manualEnter: Boolean
        get() = scanMode == 1
}