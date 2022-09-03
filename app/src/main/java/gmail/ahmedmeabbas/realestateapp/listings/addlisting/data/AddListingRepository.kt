package gmail.ahmedmeabbas.realestateapp.listings.addlisting.data

import gmail.ahmedmeabbas.realestateapp.listings.models.Listing

interface AddListingRepository {

    var chosenPropertyType: String?
    val newApartmentListing: Listing.Apartment

    fun setPropertyType(propertyType: String)

    fun getPropertyType(): String?

    fun addAdvertiserInfo(advertiserId: String, advertiser: String, phoneNumber: Int, email: String?)
}