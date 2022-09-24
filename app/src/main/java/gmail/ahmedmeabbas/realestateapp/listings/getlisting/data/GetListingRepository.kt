package gmail.ahmedmeabbas.realestateapp.listings.getlisting.data

import gmail.ahmedmeabbas.realestateapp.listings.models.Listing
import kotlinx.coroutines.flow.Flow

interface GetListingRepository {

    val listingsFlow: Flow<List<Listing?>>

    fun getAllListings(): Flow<List<Listing?>>
}