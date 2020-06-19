package technited.minds.bigbannerindia.models

import com.google.gson.annotations.SerializedName

data class Service(

        @field:SerializedName("image")
        val image: String? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("id")
        val id: String? = null
)
