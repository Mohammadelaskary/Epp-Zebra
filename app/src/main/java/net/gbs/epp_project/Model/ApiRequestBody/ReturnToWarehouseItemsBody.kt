package net.gbs.epp_project.Model.ApiRequestBody

import net.gbs.epp_project.Model.ReturnToWarehouseLine

data class ReturnToWarehouseItemsBody(
    var applang: String? = null,
    var deviceSerialNo: String? = null ,
    var employee_id: String? = null,
    var lines: List<ReturnToWarehouseLine>,
    var org_id: Int,
    var program_application_id: Int?=null,
    var program_id: Int? = null,
    var transaction_date: String,
    var user_id: Int? = null
)