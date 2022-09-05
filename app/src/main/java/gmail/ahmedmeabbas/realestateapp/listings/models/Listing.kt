package gmail.ahmedmeabbas.realestateapp.listings.models

import android.net.Uri
import com.google.firebase.firestore.FieldValue
import java.util.*

data class Listing(
    var id: String? = null,
    var dateAdded: FieldValue? = FieldValue.serverTimestamp(),
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
    var price: PriceModel = PriceModel(),
    var installments: Boolean? = null,
    var downPayment: Double? = null,
    var monthlyInstallment: Double? = null,
    var installmentPeriod: Int? = null,
    var priceHistory: MutableList<PriceModel?> = mutableListOf(null),
    var longitude: String? = null,
    var latitude: String? = null,
    var additionalInfo: String? = null,
    var listingStatus: String = ListingStatus.FOR_SALE
)

sealed class Property {

    data class Apartment(
        val type: String = PropertyType.APARTMENT,
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
        val type: String = PropertyType.HOUSE,
        var lotArea: Double? = null,
        var builtArea: Double? = null,
        var yearBuilt: Int? = null,
        var structureType: String? = null,
        var finishing: String? = null,
        var constructionMoreInfo: String? = null,
        var bedrooms: Int? = null,
        var bathrooms: Int? = null,
        var kitchens: Int? = null,
        var halls: Int? = null,
        var floors: Int? = null,
        var apartments: Boolean? = null,
        var numberApartments: Int? = null,
        var basement: Boolean? = null,
        var carGarage: Boolean? = null,
        var numberCars: Int? = null,
        var houseMoreInfo: String? = null,
        var electricity: Boolean? = null,
        var water: Boolean? = null,
        var utilitiesMoreInfo: String? = null,
    ): Property()
}

data class PriceModel(
    val dateAdded: FieldValue? = FieldValue.serverTimestamp(),
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

object StructureType {
    const val RC_FRAME = "rc_frame"
    const val LOADBEARING = "loadbearing"
}

object Finishing {
    const val FINISHED = "finished"
    const val UNFINISHED = "unfinished"
    const val PARTIALLY_FINISHED = "partially_finished"
}
