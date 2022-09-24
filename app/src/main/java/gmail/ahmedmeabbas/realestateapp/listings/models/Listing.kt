package gmail.ahmedmeabbas.realestateapp.listings.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp
import java.text.SimpleDateFormat
import java.util.*

data class Listing(
    var id: String? = null,
    var type: String? = null,
    @ServerTimestamp var dateAdded: Timestamp? = null,
    var advertiserId: String? = null,
    var advertiser: String? = null,
    var phoneNumber: Long? = null,
    var name: String? = null,
    var email: String? = null,
    var state: String? = null,
    var city: String? = null,
    var region: String? = null,
    var block: Long? = null,
    var propertyNumber: Long? = null,
    var property: Property? = null,
    var photos: List<String> = listOf(),
    var currency: String? = null,
    var price: Double? = null,
    var installments: Boolean? = null,
    var downPayment: Double? = null,
    var monthlyInstallment: Double? = null,
    var installmentPeriod: Long? = null,
    var history: Map<String, Double>? = emptyMap(),
    var longitude: String? = null,
    var latitude: String? = null,
    var additionalInfo: String? = null,
    var listingStatus: String = ListingStatus.FOR_SALE
)

sealed class Property {

    data class Apartment(
        var area: Double? = null,
        var yearBuilt: Long? = null,
        var finished: Boolean? = null,
        var furnished: Boolean? = null,
        var bedrooms: Long? = null,
        var bathrooms: Long? = null,
        var kitchen: Boolean? = null,
        var livingRoom: Boolean? = null,
        var balcony: Boolean? = null,
        var floorNumber: Long? = null,
        var detailsMoreInfo: String? = null,
        var electricity: Boolean? = null,
        var water: Boolean? = null,
        var elevator: Boolean? = null,
        var parking: Boolean? = null,
        var backupGenerator: Boolean? = null,
        var security: Boolean? = null,
        var utilitiesMoreInfo: String? = null
    ) : Property()

    data class House(
        var lotArea: Double? = null,
        var builtArea: Double? = null,
        var yearBuilt: Long? = null,
        var structureType: String? = null,
        var finishing: String? = null,
        var constructionMoreInfo: String? = null,
        var bedrooms: Long? = null,
        var bathrooms: Long? = null,
        var kitchens: Long? = null,
        var halls: Long? = null,
        var floors: Long? = null,
        var apartments: Boolean? = null,
        var numberApartments: Long? = null,
        var basement: Boolean? = null,
        var carGarage: Boolean? = null,
        var numberCars: Long? = null,
        var houseMoreInfo: String? = null,
        var electricity: Boolean? = null,
        var water: Boolean? = null,
        var utilitiesMoreInfo: String? = null
    ) : Property()
}

object Currency {
    const val USD = "USD"
    const val SDG = "SDG"
}

object Advertiser {
    const val OWNER = "owner"
    const val BROKER = "broker"
}

object ListingStatus {
    const val FOR_SALE = "for_sale"
    const val SOLD = "sold"
}

object StructureType {
    const val RC_FRAME = "rc_frame"
    const val LOADBEARING = "loadbearing"
}

object Finishing {
    const val FINISHED = "finished"
    const val UNFINISHED = "unfinished"
    const val PARTIALLY_FINISHED = "partially_finished"
}
