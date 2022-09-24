package gmail.ahmedmeabbas.realestateapp.search.data

data class ListingItem (
    val id: Int = -1,
    val price: String? = null,
    val type: String? = null,
    val floors: Int? = null,
    val bedrooms: Int? = null,
    val bathrooms: Int? = null,
    val area: Double? = null,
    val address: String? = null
)
