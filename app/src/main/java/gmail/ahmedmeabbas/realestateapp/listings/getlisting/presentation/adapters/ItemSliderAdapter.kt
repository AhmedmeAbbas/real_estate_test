package gmail.ahmedmeabbas.realestateapp.listings.getlisting.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.PhotoItemBinding

class ItemSliderAdapter(
    private val photos: List<String>
) : RecyclerView.Adapter<ItemSliderAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.photo_item, parent, false)
        val binding = PhotoItemBinding.bind(view)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    override fun getItemCount() = photos.size

    class SliderViewHolder(
        private val binding: PhotoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(url: String) {
            Glide.with(itemView)
                .load(url)
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
                .into(binding.ivPreviewPhoto)
        }
    }
}