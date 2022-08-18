package gmail.ahmedmeabbas.realestateapp.listings.data

import kotlinx.coroutines.flow.SharedFlow

interface ListingRepository {

    val listingFlow: SharedFlow<Listing>

    suspend fun addListing(listing: Listing)

    suspend fun getListingsInArea(area: String)

    //suspend fun getListingsByRegion(): Flow<List<Listing>>
}