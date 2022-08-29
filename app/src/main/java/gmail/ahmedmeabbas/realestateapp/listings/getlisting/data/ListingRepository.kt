package gmail.ahmedmeabbas.realestateapp.listings.getlisting.data

import kotlinx.coroutines.flow.SharedFlow

interface ListingRepository {

    val listingModelFlow: SharedFlow<ListingModel>

    suspend fun addListing(listingModel: ListingModel)

    suspend fun getListingsInArea(area: String)

    //suspend fun getListingsByRegion(): Flow<List<Listing>>
}