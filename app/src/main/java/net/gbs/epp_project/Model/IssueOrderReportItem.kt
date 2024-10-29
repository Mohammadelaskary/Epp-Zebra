package net.gbs.epp_project.Model

import com.google.gson.annotations.SerializedName

data class IssueOrderReportItem(
    @SerializedName("organizatioN_ID"              ) var organizatioNID            : Int?                     = null,
    @SerializedName("headeR_ID"                    ) var headeRID                  : Int?                     = null,
    @SerializedName("linE_ID"                      ) var linEID                    : Int?                     = null,
    @SerializedName("requesT_NUMBER"               ) var requesTNUMBER             : Int?                     = null,
    @SerializedName("linE_NUMBER"                  ) var linENUMBER                : Int?                     = null,
    @SerializedName("froM_SUBINVENTORY_CODE"       ) var froMSUBINVENTORYCODE      : String?                  = null,
    @SerializedName("froM_LOCATOR_ID"              ) var froMLOCATORID             : Int?                     = null,
    @SerializedName("froM_LOCATOR_Code"            ) var froMLOCATORCode           : String?                  = null,
    @SerializedName("tO_SUBINVENTORY_CODE"         ) var tOSUBINVENTORYCODE        : String?                  = null,
    @SerializedName("tO_LOCATOR_ID"                ) var tOLOCATORID               : Int?                     = null,
    @SerializedName("uoM_CODE"                     ) var uoMCODE                   : String?                  = null,
    @SerializedName("inventorY_ITEM_ID"            ) var inventorYITEMID           : Int?                     = null,
    @SerializedName("inventorY_ITEM_CODE"          ) var inventorYITEMCODE         : String?                  = null,
    @SerializedName("inventorY_ITEM_DESC"          ) var inventorYITEMDESC         : String?                  = null,
    @SerializedName("quantity"                     ) var quantity                  : Double?                  = null,
    @SerializedName("quantitY_DELIVERED"           ) var quantitYDELIVERED         : Double?                     = null,
    @SerializedName("quantitY_DETAILED"            ) var quantitYDETAILED          : Double?                     = null,
    @SerializedName("linE_STATUS_DESCRIPTION"      ) var linESTATUSDESCRIPTION     : String?                  = null,
    @SerializedName("transactioN_TYPE_NAME"        ) var transactioNTYPENAME       : String?                  = null,
    @SerializedName("transactioN_SOURCE_TYPE_DESC" ) var transactioNSOURCETYPEDESC : String?                  = null,
    @SerializedName("allocated_QUANTITY"           ) var allocatedQUANTITY         : Double?                     = null,
    @SerializedName("getOnHandList"                ) var getOnHandList             : ArrayList<OnHandIssueOrderItem> = arrayListOf()

)
