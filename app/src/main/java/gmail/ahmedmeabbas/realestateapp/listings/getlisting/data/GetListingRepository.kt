package gmail.ahmedmeabbas.realestateapp.listings.getlisting.data

import gmail.ahmedmeabbas.realestateapp.listings.models.Listing
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface GetListingRepository {

    val listingsFlow: Flow<List<Listing?>>
    val listingByIdFlow: SharedFlow<Listing?>

    fun getAllListings(): Flow<List<Listing?>>

    suspend fun getListingById(listingId: String)
}