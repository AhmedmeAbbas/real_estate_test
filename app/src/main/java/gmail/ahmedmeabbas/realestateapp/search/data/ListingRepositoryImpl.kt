package gmail.ahmedmeabbas.realestateapp.search.data

data class ListingItem(
    val id: Int = -1,
    val price: String? = null,
    val type: String? = null,
    val floors: Int? = null,
    val bedrooms: Int? = null,
    val bathrooms: Int? = null,
    val area: Double? = null,
    val address: String? = null
)

class ListingRepositoryImpl: ListingRepository {

    val listings = listOf(
        ListingItem(1, "30,000", "apartment", 2, 5, 5, 425.34, "406, Block 51, Jabra"),
        ListingItem(2, "10,000", "house",5, 4, 3, 2233.4, "143, Block 11, Riyadh"),
        ListingItem(3, "450,000", "building",9, 9, 8, 34.4, "1, Block 5, Mansheya"),
        ListingItem(4, "30,000", "apartment",2, 5, 5, 425.34, "406, Block 51, Jabra"),
        ListingItem(5, "10,000", "land",5, 4, 3, 2233.4, "143, Block 11, Riyadh"),
        ListingItem(6, "450,000", "farm",9, 9, 8, 34.4, "1, Block 5, Mansheya"),
        ListingItem(7, "30,000", "apartment",2, 5, 5, 425.34, "406, Block 51, Jabra"),
        ListingItem(8, "10,000", "house",5, 4, 3, 2233.4, "143, Block 11, Riyadh"),
        ListingItem(9, "450,000", "land",9, 9, 8, 34.4, "1, Block 5, Mansheya"),
        ListingItem(10, "30,000", "apartment",2, 5, 5, 425.34, "406, Block 51, Jabra"),
        ListingItem(11, "10,000", "building",5, 4, 3, 2233.4, "143, Block 11, Riyadh"),
        ListingItem(12, "450,000", "farm",9, 9, 8, 34.4, "1, Block 5, Mansheya")
    )
}