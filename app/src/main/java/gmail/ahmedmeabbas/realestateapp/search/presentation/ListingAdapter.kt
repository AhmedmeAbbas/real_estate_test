package gmail.ahmedmeabbas.realestateapp.search.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.ListingItemBinding
import gmail.ahmedmeabbas.realestateapp.listings.getlisting.presentation.adapters.ItemSliderAdapter
import gmail.ahmedmeabbas.realestateapp.listings.models.Currency
import gmail.ahmedmeabbas.realestateapp.listings.models.PropertyType
import gmail.ahmedmeabbas.realestateapp.search.data.ListingItem
import java.util.*

class ListingAdapter(
    private val listingClickListener: ListingClickListener
) : ListAdapter<ListingItem, ListingAdapter.ListingViewHolder>(DiffCallback) {

    interface ListingClickListener {
        fun onItemClicked(view: View, listingItem: ListingItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listing_item, parent, false)
        val binding = ListingItemBinding.bind(view)
        return ListingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListingViewHolder, position: Int) {
        val item = getItem(position)
        if (item.id != null) {
            holder.bind(item)
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<ListingItem>() {
        override fun areItemsTheSame(oldItem: ListingItem, newItem: ListingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListingItem, newItem: ListingItem): Boolean {
            return oldItem == newItem
        }
    }

    inner class ListingViewHolder(private val binding: ListingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(listingItem: ListingItem) {
            with(binding) {
                listingViewPager.adapter = ItemSliderAdapter(listingItem.photos)
                TabLayoutMediator(
                    listingTabLayout,
                    listingViewPager
                ) { _, _ -> }.attach()
                listingItem.price?.let {
                    setUpPrice(listingItem)
                }

                if (listingItem.floors == null) tvFloors.visibility = View.GONE
                tvFloors.text = binding.root.context.getString(
                    R.string.listing_item_floors,
                    listingItem.floors.toString()
                )

                tvBedrooms.text = binding.root.context.getString(
                    R.string.listing_item_bedrooms,
                    listingItem.bedrooms?.toString() ?: "0"
                )
                tvBathrooms.text = binding.root.context.getString(
                    R.string.listing_item_bathrooms,
                    listingItem.bathrooms?.toString() ?: "0"
                )
                tvArea.text = binding.root.context.getString(
                    R.string.listing_item_area_m2,
                    listingItem.area.toString()
                )
                tvAddress.text =
                    binding.root.context.getString(
                        R.string.listing_item_address,
                        listingItem.address
                    )
            }

            when (listingItem.type) {
                PropertyType.APARTMENT -> binding.tvType.text =
                    binding.root.context.getString(R.string.property_type_apartment)
                PropertyType.HOUSE -> binding.tvType.text =
                    binding.root.context.getString(R.string.property_type_house)
                PropertyType.BUILDING -> binding.tvType.text =
                    binding.root.context.getString(R.string.property_type_building)
                PropertyType.STORE -> binding.tvType.text =
                    binding.root.context.getString(R.string.property_type_store)
                PropertyType.LAND -> binding.tvType.text =
                    binding.root.context.getString(R.string.property_type_land)
                PropertyType.WAREHOUSE -> binding.tvType.text =
                    binding.root.context.getString(R.string.property_type_warehouse)
                PropertyType.FARM -> binding.tvType.text =
                    binding.root.context.getString(R.string.property_type_farm)
                else -> binding.tvType.visibility = View.GONE
            }

            binding.cvListingItem.setOnClickListener {
                listingClickListener.onItemClicked(itemView, listingItem)
            }
        }

        private fun setUpPrice(listingItem: ListingItem) {
            if (Locale.getDefault().language == "en" && listingItem.currency == itemView.context.getString(
                    R.string.add_listing_currency_usd
                )
            ) {
                binding.tvPrice.text =
                    itemView.context.getString(
                        R.string.double_string,
                        itemView.context.getString(R.string.add_listing_usd_prefix),
                        listingItem.price
                    )
            } else {
                binding.tvPrice.text = itemView.context.getString(
                    R.string.double_string,
                    listingItem.price,
                    " ${getCurrencySuffix(listingItem)}"
                )
            }
        }

        private fun getCurrencySuffix(listingItem: ListingItem): String {
            return when (listingItem.currency) {
                Currency.USD -> itemView.context.getString(R.string.add_listing_currency_usd)
                Currency.SDG -> itemView.context.getString(R.string.add_listing_currency_sdg)
                else -> ""
            }
        }
    }
}