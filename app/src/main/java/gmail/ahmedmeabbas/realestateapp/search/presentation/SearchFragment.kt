package gmail.ahmedmeabbas.realestateapp.search.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentSearchBinding
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchFragment: Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        observeListings()
        binding.tvMap.setOnClickListener {
            binding.tvMap.visibility = View.INVISIBLE
            binding.tvList.visibility = View.VISIBLE
        }

        binding.tvList.setOnClickListener {
            binding.tvList.visibility = View.INVISIBLE
            binding.tvMap.visibility = View.VISIBLE
        }
    }

    private fun observeListings() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.uiState
                    .map { it.listings }
                    .collect {
                        if (it.filterNotNull().isNotEmpty())
                            Log.d(TAG, "observeListings: ${it[0]}")
                    }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rvSearchListings.adapter = ListingAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "SearchFragment"
    }
}