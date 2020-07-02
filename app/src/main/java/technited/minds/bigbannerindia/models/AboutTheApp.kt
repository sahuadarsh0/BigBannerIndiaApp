package technited.minds.bigbannerindia.models

import com.google.gson.annotations.SerializedName


data class AboutTheApp(

        @field:SerializedName("mobile")
        val mobile: String? = null,

        @field:SerializedName("mobile2")
        val mobile2: String? = null,

        @field:SerializedName("about")
        val about: String? = null,

        @field:SerializedName("id")
        val id: String? = null,

        @field:SerializedName("email")
        val email: String? = null
)
