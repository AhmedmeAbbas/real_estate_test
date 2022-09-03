package gmail.ahmedmeabbas.realestateapp.listings.addlisting.data

import android.net.Uri
import gmail.ahmedmeabbas.realestateapp.listings.models.Listing
import gmail.ahmedmeabbas.realestateapp.listings.models.Property

interface AddListingRepository {

    var chosenPropertyType: String?
    val newListing: Listing

    fun setPropertyType(propertyType: String)

    fun getPropertyType(): String?

    fun addAdvertiserInfo(
        advertiserId: String,
        advertiser: String,
        phoneNumber: Int,
        email: String?
    )

    fun addPropertyAddress(
        state: String,
        city: String,
        region: String,
        block: Int,
        propertyNumber: Int
    )

    fun addProperty(property: Property)

    fun addPhotos(photoUris: List<Uri?>)

    fun addPrice(
        currency: String,
        price: Double,
        installments: Boolean?,
        downPayment: Double?,
        monthlyInstallment: Double?,
        installmentPeriod: Int?
    )

    fun addAdditionalInfo(additionalInfo: String?)

    fun logResults()
}