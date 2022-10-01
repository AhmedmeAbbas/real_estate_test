package gmail.ahmedmeabbas.realestateapp.listings.getlisting.domain

import gmail.ahmedmeabbas.realestateapp.listings.models.Listing
import gmail.ahmedmeabbas.realestateapp.listings.models.Property
import gmail.ahmedmeabbas.realestateapp.search.data.ListingItem
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

fun Listing.mapToHouseItem(address: String): ListingItem {
    val house = property as Property.House

    return ListingItem(
        id = id,
        photos = photos,
        price = DecimalFormat("0.##").format(price),
        currency = currency,
        type = type,
        floors = house.floors,
        bedrooms = house.bedrooms,
        bathrooms = house.bathrooms,
        area = DecimalFormat("0.##", DecimalFormatSymbols(Locale.US)).format(house.lotArea ?: 0.0),
        address = address
    )
}

fun Listing.mapToApartmentItem(address: String): ListingItem {
    val apartment = property as Property.Apartment

    return ListingItem(
        id = id,
        photos = photos,
        price = DecimalFormat("0.##", DecimalFormatSymbols(Locale.US)).format(price),
        currency = currency,
        type = type,
        floors = null,
        bedrooms = apartment.bedrooms,
        bathrooms = apartment.bathrooms,
        area = DecimalFormat("0.##", DecimalFormatSymbols(Locale.US)).format(apartment.area ?: 0.0),
        address = address
    )
}