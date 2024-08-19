package br.univesp.tcc.webclient.model

import android.databinding.tool.ext.br
import br.univesp.tcc.database.model.User
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonClass
import com.squareup.moshi.ToJson


class UserResponse (
    val id: String?,
    val name: String?,
    val userName: String?,
    val password: String?,
    val imgPath: String?,
    val email: String?,
    val celular: Double?,
    val telegram: String?,
    val isAdmin: Boolean?,
    val createdAt: String?,
){
    val user: User get() = User(
        id = id?: "",
        name = name ?: "",
        userName = userName?: "",
        password = password?: "",
        imgPath = imgPath?: "",
        email = email?: "",
        celular = celular?: 0.0,
        telegram = telegram?: "",
        isAdmin = isAdmin?: false,
        createdAt = createdAt?: "",
    )

}

class UserJsonAdapter {
    @FromJson
    fun userFromJson(userJson: UserResponse): User {
        return User(
            id = userJson.id ?: "",
            name = userJson.name ?: "",
            userName = userJson.userName?: "",
            password = userJson.password?: "",
            imgPath = userJson.imgPath?: "",
            email = userJson.email?: "",
            celular = userJson.celular?: 0.0,
            telegram = userJson.telegram?: "",
            isAdmin = userJson.isAdmin?: false,
            createdAt = userJson.createdAt?: "",
        )
    }

    @ToJson
    fun userToJson(user: User): UserResponse {
        return UserResponse(
            id = user.id ?: "",
            name = user.name ?: "",
            userName = user.userName?: "",
            password = user.password?: "",
            imgPath = user.imgPath?: "",
            email = user.email?: "",
            celular = user.celular?: 0.0,
            telegram = user.telegram?: "",
            isAdmin = user.isAdmin?: false,
            createdAt = user.createdAt?: "",
        )
    }
}