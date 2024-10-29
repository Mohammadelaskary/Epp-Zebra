package net.gbs.epp_project.Model

data class ReturnToWarehouseLine(
    val linE_NUMBER: Int,
    val line_id: Int,
    val locatoR_CODE: String,
    val qty: Double,
    val subinventorY_CODE: String
)