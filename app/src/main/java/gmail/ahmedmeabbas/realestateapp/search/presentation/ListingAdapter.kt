package gmail.ahmedmeabbas.realestateapp.search.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.ListingItemBinding
import gmail.ahmedmeabbas.realestateapp.search.data.ListingItem
import gmail.ahmedmeabbas.realestateapp.search.data.ListingRepositoryImpl

class ListingAdapter: ListAdapter<ListingItem, ListingAdapter.ListingViewHolder>(DiffCallback) {

    private val listings = ListingRepositoryImpl().listings

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listing_item, parent, false)
        val binding = ListingItemBinding.bind(view)
        return ListingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListingViewHolder, position: Int) {
        val item = listings[position]
        holder.bind(item)
    }

    override fun getItemCount() = listings.size

    private object DiffCallback: DiffUtil.ItemCallback<ListingItem>() {
        override fun areItemsTheSame(oldItem: ListingItem, newItem: ListingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListingItem, newItem: ListingItem): Boolean {
            return oldItem == newItem
        }
    }

    class ListingViewHolder(private val binding: ListingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(listing: ListingItem) {
                with(binding) {
                    tvPrice.text = binding.root.context.getString(R.string.price, "$", listing.price)
                    tvFloors.text = binding.root.context.getString(R.string.floors, listing.floors.toString())
                    tvBedrooms.text = binding.root.context.getString(R.string.bedrooms, listing.bedrooms.toString())
                    tvBathrooms.text = binding.root.context.getString(R.string.bathrooms, listing.bathrooms.toString())
                    tvArea.text = binding.root.context.getString(R.string.area, listing.area.toString())
                    tvAddress.text = binding.root.context.getString(R.string.address, listing.address)
                }

                when(listing.type) {
                    "apartment" -> binding.tvType.text = binding.root.context.getString(R.string.apartment)
                    "house" -> binding.tvType.text = binding.root.context.getString(R.string.house)
                    "building" -> binding.tvType.text = binding.root.context.getString(R.string.multistory)
                    "land" -> binding.tvType.text = binding.root.context.getString(R.string.land)
                    "farm" -> binding.tvType.text = binding.root.context.getString(R.string.farm)
                    else -> binding.tvType.visibility = View.GONE
                }
            }
    }
}