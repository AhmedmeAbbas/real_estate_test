package gmail.ahmedmeabbas.realestateapp.listings.addlisting.data

import com.google.firebase.firestore.FirebaseFirestore
import gmail.ahmedmeabbas.realestateapp.listings.models.Listing

class AddListingRepositoryImpl(
    private val db: FirebaseFirestore
) : AddListingRepository {

    override var chosenPropertyType: String? = null
    override val newApartmentListing: Listing.Apartment = Listing.Apartment()

    override fun setPropertyType(propertyType: String) {
        chosenPropertyType = propertyType
    }

    override fun getPropertyType(): String? {
        return chosenPropertyType
    }

    override fun addAdvertiserInfo(advertiserId: String, advertiser: String, phoneNumber: Int, email: String?) {
        newApartmentListing.advertiserId = advertiserId
        newApartmentListing.advertiser = advertiser
        newApartmentListing.phoneNumber = phoneNumber
        newApartmentListing.email = email
    }
}