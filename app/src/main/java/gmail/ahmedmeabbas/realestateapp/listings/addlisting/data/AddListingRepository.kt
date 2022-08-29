package gmail.ahmedmeabbas.realestateapp.listings.addlisting.data

interface AddListingRepository {

    var chosenPropertyType: String?

    fun setPropertyType(propertyType: String)

    fun getPropertyType(): String?
}