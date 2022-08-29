package gmail.ahmedmeabbas.realestateapp.listings.addlisting.data

import com.google.firebase.firestore.FirebaseFirestore

class AddListingRepositoryImpl(
    private val db: FirebaseFirestore
) : AddListingRepository {

    override var chosenPropertyType: String? = null

    override fun setPropertyType(propertyType: String) {
        chosenPropertyType = propertyType
    }

    override fun getPropertyType(): String? {
        return chosenPropertyType
    }
}