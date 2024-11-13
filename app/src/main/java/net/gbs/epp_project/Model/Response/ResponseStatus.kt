package net.gbs.epp_project.Model.Response

data class ResponseStatus(
    val errorMessage: String,
    val isSuccess: Boolean,
    val statusCode: Int,
    val statusMessage: String
)