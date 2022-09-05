package gmail.ahmedmeabbas.realestateapp.listings.addlisting.domain

import gmail.ahmedmeabbas.realestateapp.listings.addlisting.data.AddListingRepository
import gmail.ahmedmeabbas.realestateapp.listings.models.Property

class AddHouseUseCase (
    private val addListingRepository: AddListingRepository
) {

    private val house = Property.House()

    fun addConstructionDetails(
        lotArea: Double?,
        builtArea: Double?,
        yearBuilt: Int?,
        structureType: String?,
        finishing: String?,
        moreInfo: String?
    ) {
        house.apply {
            this.lotArea = lotArea
            this.builtArea = builtArea
            this.yearBuilt = yearBuilt
            this.structureType = structureType
            this.finishing = finishing
            this.constructionMoreInfo = moreInfo
        }
    }

    fun addHouseDetails(
        bedrooms: Int?,
        bathrooms: Int?,
        kitchens: Int?,
        halls: Int?,
        floors: Int?,
        apartments: Boolean?,
        numberApartments: Int?,
        basement: Boolean?,
        garage: Boolean?,
        numberCars: Int?,
        moreInfo: String?
    ) {
        house.apply {
            this.bedrooms = bedrooms
            this.bathrooms = bathrooms
            this.kitchens = kitchens
            this.halls = halls
            this.floors = floors
            this.apartments = apartments
            this.numberApartments = numberApartments
            this.basement = basement
            this.carGarage = garage
            this.numberCars = numberCars
            this.houseMoreInfo = moreInfo
        }
    }

    fun addUtilities(electricity: Boolean?, water: Boolean?, moreInfo: String?) {
        house.apply {
            this.electricity = electricity
            this.water = water
            this.utilitiesMoreInfo = moreInfo
        }
        addListingRepository.addHouse(house)
    }
}