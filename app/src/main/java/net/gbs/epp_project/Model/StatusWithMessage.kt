package net.gbs.epp_project.Model


data class StatusWithMessage(
    val status:Status,
    val message:String = "",
)
