package technited.minds.bigbannerindia.models

import com.google.gson.annotations.SerializedName

data class Customer(

        val id: String? = null,

        val name: String? = null,

        val gender: String? = null,

        val state_id: String? = null,

        val district_id: String? = null,

        val city_id: String? = null,

        val locality_id: String? = null,

        val postal_code_id: String? = null,

        val mobile: String? = null,

        val address: String? = null,

        val email: String? = null,

        val password: String? = null

) {
    val state: String? = null
    val district: String? = null
    val city: String? = null
    val locality: String? = null

    @field:SerializedName("postal_code")
    val postalCode: String? = null
}
