package net.gbs.epp_project.Model

data class AuditLocator (
    var auditOrderList : MutableList<AuditOrderSubinventory>,
    var isFullScanned  : Boolean
)