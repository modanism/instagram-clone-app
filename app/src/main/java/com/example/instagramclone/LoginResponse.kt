package com.example.instagramclone

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class LoginResponse(

	@field:SerializedName("loginResult")
	val loginResult: LoginResult,

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

@Parcelize
data class LoginResult(

	@field:SerializedName("name")
	var name: String? = null,

	@field:SerializedName("userId")
	var userId: String? = null,

	@field:SerializedName("token")
	var token: String? = null
) : Parcelable
