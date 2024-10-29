package net.gbs.epp_project.Model.Response

import com.google.gson.annotations.SerializedName
import net.gbs.epp_project.Base.BaseResponse
import net.gbs.epp_project.Model.User

data class SignInResponse(
    @SerializedName("user")
    val user: User
) :BaseResponse<User>(){
    override fun getData(): User {
        return user
    }

}