package technited.minds.bigbannerindia.models

import com.google.gson.annotations.SerializedName

data class LocalJob(

        @field:SerializedName("description")
        val description: String? = null,

        @field:SerializedName("client")
        val client: String? = null,

        @field:SerializedName("type")
        val type: String? = null,

        @field:SerializedName("salary")
        val salary: String? = null,

        @field:SerializedName("client_id")
        val clientId: String? = null,

        @field:SerializedName("status")
        val status: String? = null
)
