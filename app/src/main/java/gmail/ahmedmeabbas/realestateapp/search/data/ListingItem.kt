package gmail.ahmedmeabbas.realestateapp.search.data

data class ListingItem(
    val id: String? = null,
    val photos: List<String> = listOf(),
    val price: String? = null,
    val currency: String? = null,
    val type: String? = null,
    val floors: Long? = null,
    val bedrooms: Long? = null,
    val bathrooms: Long? = null,
    val area: String? = null,
    val address: String? = null
)
