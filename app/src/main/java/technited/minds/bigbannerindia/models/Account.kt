package technited.minds.bigbannerindia.models

import com.google.gson.annotations.SerializedName

data class Account(

        @field:SerializedName("msg")
        val msg: String? = null,

        @field:SerializedName("gender")
        val gender: String? = null,

        @field:SerializedName("requests")
        val requests: Int? = null,

        @field:SerializedName("likes")
        val likes: Int? = null
)
