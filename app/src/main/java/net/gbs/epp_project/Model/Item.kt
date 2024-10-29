package net.gbs.epp_project.Model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Item(
    @SerializedName("itemId"                     ) var itemId                     : Int?              = null,
    @SerializedName("orgId"                      ) var orgId                      : Int?              = null,
    @SerializedName("orgCode"                    ) var orgCode                    : String?           = null,
    @SerializedName("orgName"                    ) var orgName                    : String?           = null,
    @SerializedName("itemCode"                   ) var itemCode                   : String?           = null,
    @SerializedName("itemDescription"            ) var itemDescription            : String?           = null,
    @SerializedName("category"                   ) var category                   : String?           = null,
    @SerializedName("subCategory"                ) var subCategory                : String?           = null,
    @SerializedName("subInventory"               ) var subInventory               : String?           = null,
    @SerializedName("locator"                    ) var locator                    : String?           = null,
    @SerializedName("uom"                        ) var uom                        : String?           = null,
    @SerializedName("org"                        ) var org                        : String?           = null,
    @SerializedName("physicalInventoryCountings" ) var physicalInventoryCountings : ArrayList<String> = arrayListOf(),
){
    override fun toString(): String {
        return itemDescription!!
    }
    companion object {
        fun toJson(item: Item): String {
            return Gson().toJson(item)
        }

        fun fromJson(item: String): Item {
            return Gson().fromJson(item, Item::class.java)
        }
    }
}
