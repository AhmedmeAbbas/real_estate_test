package gmail.ahmedmeabbas.realestateapp.listings.getlisting.data

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.*
import gmail.ahmedmeabbas.realestateapp.listings.models.Listing
import gmail.ahmedmeabbas.realestateapp.listings.models.Property
import gmail.ahmedmeabbas.realestateapp.listings.models.PropertyType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GetListingRepositoryImpl(
    private val db: FirebaseFirestore
) : GetListingRepository {

    override val listingsFlow = getAllListings()
    private val _listingByIdFlow = MutableSharedFlow<Listing?>()
    override val listingByIdFlow = _listingByIdFlow.asSharedFlow()

    override suspend fun getListingById(listingId: String) {
        CoroutineScope(IO).launch {
            listingsFlow.collect { listings ->
                Log.d(TAG, "getListingById: size: ${listings.filter { it?.id == listingId }.size}")
                _listingByIdFlow.emit(listings.filter { it?.id == listingId }[0])
            }
        }
    }

    override fun getAllListings(): Flow<List<Listing?>> {
        return db.collection("listings/khartoum/khartoum")
            .snapshotFlow()
            .map { querySnapshot ->
                querySnapshot.documents.map { documentSnapshot ->
                    if (documentSnapshot.getString("type") == PropertyType.APARTMENT) {
                        mapResultToApartment(documentSnapshot)
                    } else {
                        mapResultToHouse(documentSnapshot)
                    }
                }
            }
    }

    private fun mapResultToApartment(document: DocumentSnapshot?): Listing {
        val propertyMap = document?.data?.get("property") as Map<*, *>
        val property = Property.Apartment()

        property.apply {
            area = propertyMap["area"] as Double?
            yearBuilt = propertyMap["yearBuilt"] as Long?
            finished = propertyMap["finished"] as Boolean?
            furnished = propertyMap["finished"] as Boolean?
            bedrooms = propertyMap["bedrooms"] as Long?
            bathrooms = propertyMap["bathrooms"] as Long?
            kitchen = propertyMap["kitchen"] as Boolean?
            livingRoom = propertyMap["livingRoom"] as Boolean?
            balcony = propertyMap["balcony"] as Boolean?
            floorNumber = propertyMap["floorNumber"] as Long?
            detailsMoreInfo = propertyMap["detailsMoreInfo"] as String?
            electricity = propertyMap["electricity"] as Boolean?
            water = propertyMap["water"] as Boolean?
            elevator = propertyMap["elevator"] as Boolean?
            parking = propertyMap["parking"] as Boolean?
            backupGenerator = propertyMap["backupGenerator"] as Boolean?
            security = propertyMap["security"] as Boolean?
            utilitiesMoreInfo = propertyMap["utilitiesMoreInfo"] as String?
        }
        return mapToListing(document, property)
    }

    private fun mapResultToHouse(document: DocumentSnapshot?): Listing {
        val propertyMap = document?.data?.get("property") as Map<*, *>
        val property = Property.House()

        property.apply {
            lotArea = propertyMap["lotArea"] as Double?
            builtArea = propertyMap["builtArea"] as Double?
            yearBuilt = propertyMap["yearBuilt"] as Long?
            structureType = propertyMap["structureType"] as String?
            finishing = propertyMap["finishing"] as String?
            constructionMoreInfo = propertyMap["constructionMoreInfo"] as String?
            bedrooms = propertyMap["bedrooms"] as Long?
            bathrooms = propertyMap["bathrooms"] as Long?
            kitchens = propertyMap["kitchens"] as Long?
            halls = propertyMap["halls"] as Long?
            floors = propertyMap["floors"] as Long?
            apartments = propertyMap["apartments"] as Boolean?
            numberApartments = propertyMap["numberApartments"] as Long?
            basement = propertyMap["basement"] as Boolean?
            carGarage = propertyMap["carGarage"] as Boolean?
            numberCars = propertyMap["numberCars"] as Long?
            houseMoreInfo = propertyMap["houseMoreInfo"] as String?
            electricity = propertyMap["electricity"] as Boolean?
            water = propertyMap["water"] as Boolean?
            utilitiesMoreInfo = propertyMap["utilitiesMoreInfo"] as String?
        }
        return mapToListing(document, property)
    }

    private fun mapToListing(document: DocumentSnapshot?, property: Property): Listing {
        val listingMap = document?.data
        val photos = getPhotos(listingMap)
        val priceHistory = getPriceHistory(listingMap)
        val listing = Listing()

        listingMap?.let {
            listing.id = it["id"] as String?
            listing.type = it["type"] as String?
            listing.dateAdded = it["dateAdded"] as Timestamp?
            listing.advertiserId = it["advertiserId"] as String?
            listing.advertiser = it["advertiser"] as String?
            listing.phoneNumber = it["phoneNumber"] as Long?
            listing.name = it["name"] as String?
            listing.email = it["email"] as String?
            listing.state = it["state"] as String?
            listing.city = it["city"] as String?
            listing.region = it["region"] as String?
            listing.block = it["block"] as Long?
            listing.propertyNumber = it["propertyNumber"] as Long?
            listing.property = property
            listing.photos = photos
            listing.currency = it["currency"] as String?
            listing.price = it["price"] as Double?
            listing.installments = it["installments"] as Boolean?
            listing.downPayment = it["downPayment"] as Double?
            listing.monthlyInstallment = it["monthlyInstallment"] as Double?
            listing.installmentPeriod = it["installmentPeriod"] as Long?
            listing.history = priceHistory
            listing.additionalInfo = it["additionalInfo"] as String?
            listing.listingStatus = it["listingStatus"] as String
        }
        return listing
    }

    private fun getPhotos(listingMap: Map<String, Any>?): List<String> {
        val photosMap = listingMap?.get("photos") as List<*>
        val photos: MutableList<String> = mutableListOf()
        for (i in photosMap.indices ) {
            photos.add(photosMap[i] as String)
        }
        return photos.toList()
    }

    private fun getPriceHistory(listingMap: Map<String, Any>?): Map<String, Double> {
        val historyMap = listingMap?.get("history") as Map<*, *>?
        val priceHistory = mutableMapOf<String, Double>()
        historyMap?.let {
            for (i in historyMap.keys) {
                priceHistory[i as String] = historyMap[i] as Double
            }
        }
        return priceHistory.toMap()
    }

    private fun Query.snapshotFlow(): Flow<QuerySnapshot> = callbackFlow {
        val listenerRegistration = addSnapshotListener { data, error ->
            if (error != null) {
                close()
                return@addSnapshotListener
            }
            if (data != null)
                trySend(data)
        }
        awaitClose {
            listenerRegistration.remove()
        }
    }

    companion object {
        private const val TAG = "GetListingRepositoryImpl"
    }
}