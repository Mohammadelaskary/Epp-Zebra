package net.gbs.epp_project.Model.Response

data class ResponseStatus(
    val errorMessage: Any,
    val isSuccess: Boolean,
    val statusCode: Int,
    val statusMessage: String
)