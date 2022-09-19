package gmail.ahmedmeabbas.realestateapp.listings.addlisting.data

import android.content.ContentResolver
import android.net.Uri
import gmail.ahmedmeabbas.realestateapp.listings.models.Listing
import gmail.ahmedmeabbas.realestateapp.listings.models.Property
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface AddListingRepository {

    val newListing: Listing
    val addListingMessagesFlow: SharedFlow<String>
    val uploadPhotosProgress: StateFlow<Int>

    fun setPropertyType(propertyType: String)

    fun getPropertyType(): String?

    fun addAdvertiserInfo(
        advertiserId: String,
        advertiser: String,
        phoneNumber: Long,
        name: String?,
        email: String?
    )

    fun addPropertyAddress(
        state: String,
        city: String,
        region: String,
        block: Int,
        propertyNumber: Int
    )

    fun addApartment(apartment: Property.Apartment)

    fun addHouse(house: Property.House)

    fun addPreviewPhotos(photoUris: List<Uri?>)

    fun getPreviewPhotos(): List<Uri>

    fun addPrice(
        currency: String,
        price: Double,
        installments: Boolean?,
        downPayment: Double?,
        monthlyInstallment: Double?,
        installmentPeriod: Int?
    )

    fun addAdditionalInfo(additionalInfo: String?)

    fun getListing(): Listing

    suspend fun submitListing(contentResolver: ContentResolver, height: Int)

    fun logResults()
}