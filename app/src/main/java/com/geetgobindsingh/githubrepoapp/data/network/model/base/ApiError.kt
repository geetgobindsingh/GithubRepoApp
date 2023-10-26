package com.geetgobindingh.githubrepoapp.data.network.model.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApiError(
    @field:SerializedName("body")
    @field:Expose
    var message: String
) {

    @SerializedName("statusCode")
    @Expose
    var reqStatus: String? = null

    override fun toString(): String {
        return "ApiError{" +
                "message='" + message + '\''.toString() +
                ", reqStatus='" + reqStatus + '\''.toString() +
                '}'.toString()
    }
}