package net.gbs.epp_project.Model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class PurchaseOrder(
    @SerializedName("po_header_id"  ) var poHeaderId    : String?    = null,
    @SerializedName("org_id"        ) var orgId         : Int?    = null,
    @SerializedName("creation_date" ) var creationDate  : String? = null,
    @SerializedName("potype"        ) var potype        : String? = null,
    @SerializedName("operatingunit" ) var operatingunit : String? = null,
    @SerializedName("pono"          ) var pono          : String? = null,
    @SerializedName("supplier"      ) var supplier      : String? = null,
    @SerializedName("site"          ) var site          : String? = null,
    @SerializedName("shipto"        ) var shipto        : String? = null,
    @SerializedName("billto"        ) var billto        : String? = null,
    @SerializedName("buyer"         ) var buyer         : String? = null,
    @SerializedName("currency"      ) var currency      : String? = null,
    @SerializedName("poCreatedUser" ) var poCreatedUser : String? = null
) {
    private val re = Regex("[^A-Za-z0-9 ]")
    val purchaseOrderNumber
        get() = re.replace(pono as String,"")


    override fun toString(): String {
        return operatingunit.toString()
    }
    companion object {
        fun toJson(purchaseOrder: PurchaseOrder): String = Gson().toJson(purchaseOrder).toString()
        fun fromJson(purchaseOrder: String): PurchaseOrder =
            Gson().fromJson(purchaseOrder, PurchaseOrder::class.java)
    }
}
