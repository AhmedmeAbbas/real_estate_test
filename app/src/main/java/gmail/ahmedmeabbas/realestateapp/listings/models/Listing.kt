package gmail.ahmedmeabbas.realestateapp.listings.models

import android.net.Uri
import com.google.firebase.firestore.FieldValue
import java.util.*

data class Listing(
    var id: String? = null,
    var dateAdded: Date? = null,
    var advertiserId: String? = null,
    var advertiser: String? = null,
    var phoneNumber: Int? = null,
    var email: String? = null,
    var state: String? = null,
    var city: String? = null,
    var region: String? = null,
    var block: Int? = null,
    var propertyNumber: Int? = null,
    var property: Property? = null,
    var photoUris: List<Uri?> = listOf(),
    var currency: String? = null,
    var price: Price = Price(),
    var installments: Boolean? = null,
    var downPayment: Double? = null,
    var monthlyInstallment: Double? = null,
    var installmentPeriod: Int? = null,
    var longitude: String? = null,
    var latitude: String? = null,
    var additionalInfo: String? = null,
    var priceHistory: MutableList<Price> = mutableListOf(Price()),
    var listingStatus: String? = null
)

sealed class Property {

    data class Apartment(
        val type: String? = null,
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
    ): Property()

    data class House(
        val type: String? = null,
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
    ): Property()
}

data class Price(
    val dateAdded: FieldValue? = null,
    val price: Double? = null
)

object Advertiser {

        const val OWNER = "owner"
        const val BROKER = "broker"
}

object ListingStatus {

    const val FOR_SALE = "for_sale"
    const val SOLD = "sold"
}
