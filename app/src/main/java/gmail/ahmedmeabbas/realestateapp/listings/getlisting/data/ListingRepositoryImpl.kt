package gmail.ahmedmeabbas.realestateapp.listings.getlisting.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class ListingRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore
): ListingRepository {

    private val _listingModelFlow = MutableSharedFlow<ListingModel>()
    override val listingModelFlow = _listingModelFlow.asSharedFlow()

    override suspend fun addListing(listingModel: ListingModel) {
        val state = listingModel.state?.lowercase()
        val city = listingModel.city?.lowercase()
        db.collection("/states/$state/cities/$city/listings")
            .add(listingModel)
            .addOnSuccessListener { Log.d(TAG, "addListing: success") }
            .addOnFailureListener { Log.d(TAG, "addListing: failed af") }
    }

    override suspend fun getListingsInArea(area: String) {
        db.collection("/states/khartoum/cities/khartoum/listings")
            .whereEqualTo("region", area)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    Log.d(TAG, "getListingsInArea: docss null")
                    return@addOnSuccessListener
                }
                Log.d(TAG, "getListingsInArea: sucess")
                for (document in documents) {
                    if (document == null) {
                        Log.d(TAG, "getListingsInArea: null")
                    }
                    Log.d(TAG, "getListingsInArea: ${document.get("price")}")
                }
            }
            .addOnFailureListener { Log.d(TAG, "getListingsInArea: failed af") }
    }

    companion object {
        private const val TAG = "FakeListingRepo"
    }
}