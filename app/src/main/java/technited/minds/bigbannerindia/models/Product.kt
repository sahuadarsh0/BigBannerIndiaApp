package technited.minds.bigbannerindia.models

import com.google.gson.annotations.SerializedName
import org.parceler.Parcel

@Parcel
data class Product(

        @field:SerializedName("request_status")
        val requestStatus: String? = null,

        val image: String? = null,
        val flag: String? = null,
        val comments: List<Comment?>? = null,
        val requests: List<Request?>? = null,

        @field:SerializedName("comment_status")
        val commentStatus: String? = null,


        @field:SerializedName("client_id")
        val clientId: String? = null,

        val cdt: String? = null,
        val name: String? = null,
        val id: String? = null,
        val stock: String? = null,

        @field:SerializedName("client_name")
        val clientName: String? = null,

        val likes: List<Like?>? = null
)

@Parcel
data class Comment(
        val cdt: String? = null,
        val flag: String? = null,

        @field:SerializedName("product_id")
        val productId: String? = null,

        val comment: String? = null,
        val id: String? = null,

        @field:SerializedName("customer_name")
        val customerName: String? = null,

        @field:SerializedName("customer_gender")
        val customerGender: String? = null,

        @field:SerializedName("customer_id")
        val customerId: String? = null
)

@Parcel
data class Like(
        val cdt: String? = null,
        val flag: String? = null,
        val like: String? = null,

        @field:SerializedName("product_id")
        val productId: String? = null,

        val id: String? = null,

        @field:SerializedName("customer_name")
        val customerName: String? = null,

        @field:SerializedName("customer_id")
        val customerId: String? = null
)

@Parcel
data class Request(
        val request: String? = null,
        val cdt: String? = null,
        val flag: String? = null,

        @field:SerializedName("product_id")
        val productId: String? = null,


        val id: String? = null,

        @field:SerializedName("customer_name")
        val customerName: String? = null,

        @field:SerializedName("customer_id")
        val customerId: String? = null,

        val status: String? = null
)
