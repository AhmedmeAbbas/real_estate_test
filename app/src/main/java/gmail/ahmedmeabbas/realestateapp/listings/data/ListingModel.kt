package gmail.ahmedmeabbas.realestateapp.listings.data

import java.util.*

data class Listing(
    val id: String? = null,
    val state: String? = null,
    val city: String? = null,
    val region: String? = null,
    val listerId: String? = null,
    val propertyType: String? = null,
    val price: Price? = null,
    val fullAddress: String? = null,
    val floors: Int? = null,
    val bedrooms: Int? = null,
    val bathrooms: Int? = null,
    val area: Double? = null,
    val dateAdded: Date? = null,
    val longitude: String? = null,
    val latitude: String? = null,
    val description: String? = null,
    val dateBuilt: Int? = null,
    val position: String? = null,
    val distanceFromMainStreet: Float? = null,
    val directions: String? = null,
    val backupGenerator: Boolean? = null,
    val parking: Boolean? = null,
    val parkingSpaces: Int? = null,
    val parkingType: String? = null,
    val listingOwner: String? = null,
    val electricity: Boolean? = null,
    val water: Boolean? = null,
    val waterSource: String? = null,
    val buildingStatus: String? = null,
    val construction: String? = null,
    val yard: Boolean? = null,
    val basement: Boolean? = null,
    val sanitary: String? = null,
    val priceHistory: List<Price>? = null,
    val listingStatus: String? = null
)

data class Price(
    val dateAdded: Date? = null,
    val price: Double? = null
)

enum class ListingOwner {
    OWNER, BROKER
}
