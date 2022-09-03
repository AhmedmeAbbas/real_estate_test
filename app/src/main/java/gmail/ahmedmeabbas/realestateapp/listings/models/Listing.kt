package gmail.ahmedmeabbas.realestateapp.listings.models

import java.util.*

sealed class Listing {

    data class Apartment(
        var id: String? = null,
        var state: String? = null,
        var city: String? = null,
        var region: String? = null,
        var advertiserId: String? = null,
        var dateAdded: Date? = null,
        var propertyType: String? = null,
        var advertiser: String? = null,
        var phoneNumber: Int? = null,
        var email: String? = null,
        var fullAddress: String? = null,
        var area: Double? = null,
        var yearBuilt: Int? = null,
        var finished: Boolean? = null,
        var furnished: Boolean? = null,
        var bedrooms: Int? = null,
        var bathrooms: Int? = null,
        var kitchen: Boolean? = null,
        var livingRoom: Boolean? = null,
        var balcony: Boolean? = null,
        var floorNumber: Int? = null,
        var detailsMoreInfo: String? = null,
        var electricity: Boolean? = null,
        var water: Boolean? = null,
        var elevator: Boolean? = null,
        var parking: Boolean? = null,
        var backupGenerator: Boolean? = null,
        var security: Boolean? = null,
        var utilitiesMoreInfo: String? = null,
        var photoUrls: List<String>? = null,
        var price: Price? = null,
        var monthlyInstallments: Boolean? = null,
        var downPayment: Double? = null,
        var installment: Double? = null,
        var installmentPeriod: Int? = null,
        var longitude: String? = null,
        var latitude: String? = null,
        var additionalInfo: String? = null,
        var priceHistory: List<Price>? = null,
        var listingStatus: String? = null
    )
    data class House(
        var id: String? = null,
        var state: String? = null,
        var city: String? = null,
        var region: String? = null,
        var listerId: String? = null,
        var dateAdded: Date? = null,
        var propertyType: String = PropertyType.HOUSE,
        var advertiser: String? = null,
        var phoneNumber: Int? = null,
        var email: String? = null,
        var fullAddress: String? = null,
        var lotArea: Double? = null,
        var builtArea: Double? = null,
        var yearBuilt: Int? = null,
        var structureType: String? = null,
        var finishing: String? = null,
        var bedrooms: Int? = null,
        var bathrooms: Int? = null,
        var kitchens: Int? = null,
        var halls: Int? = null,
        var floors: Int? = null,
        var basement: Boolean? = null,
        var carGarage: Boolean? = null,
        var numberCars: Int? = null,
        var detailsMoreInfo: String? = null,
        var electricity: Boolean? = null,
        var water: Boolean? = null,
        var utilitiesMoreInfo: String? = null,
        var photoUrls: List<String>? = null,
        var price: Price? = null,
        var monthlyInstallments: Boolean? = null,
        var downPayment: Double? = null,
        var installment: Double? = null,
        var installmentPeriod: Int? = null,
        var additionalInfo: String? = null,
        var longitude: String? = null,
        var latitude: String? = null,
        var priceHistory: List<Price>? = null,
        var listingStatus: String? = null
    )
}

data class Price(
    val dateAdded: Date? = null,
    val currency: String? = null,
    val price: Double? = null
)

class Advertiser {

    companion object {
        const val OWNER = "owner"
        const val BROKER = "broker"
    }
}
