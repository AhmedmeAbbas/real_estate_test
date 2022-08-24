package gmail.ahmedmeabbas.realestateapp.myListings.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentMyListingsBinding

class MyListingsFragment: Fragment() {

    private var _binding: FragmentMyListingsBinding? = null
    private val binding get() = _binding!!
    private val myListingsViewModel: MyListingsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyListingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeAuthState()
    }

    private fun observeAuthState() {
        myListingsViewModel.isUserSignedIn.observe(viewLifecycleOwner) { isSignedIn ->
            updateUI(isSignedIn!!)
        }
    }

    private fun updateUI(isSignedIn: Boolean) {
        if (isSignedIn) {
            if (myListingsViewModel.myListings[0].id == null) {
                binding.tvMyHomeSub.text = getString(R.string.my_home_list_empty)
                binding.btnMyHomeMain.text = getString(R.string.my_home_add_listing)
                binding.btnMyHomeMain.setOnClickListener {
                    findNavController().navigate(R.id.action_myListingsFragment_to_addListingGraph)
                }
            } else {
                hideViewsAndShowRecyclerView()
            }
        } else {
            binding.btnMyHomeMain.setOnClickListener {
                findNavController().navigate(R.id.action_global_authGraph)
            }
        }
    }

    private fun hideViewsAndShowRecyclerView() {
        with(binding) {
            ivMyHome.visibility = View.GONE
            tvMyHomeHeader.visibility = View.GONE
            tvMyHomeSub.visibility = View.GONE
            btnMyHomeMain.visibility = View.GONE
            rvMyHome.visibility = View.VISIBLE
            fabAddListing.visibility = View.VISIBLE
        }
    }

    private fun generateListingId(): String {
        val chars = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        var id = ""
        for (i in 0 until 28) {
            id += chars[(chars.indices).random()]
        }
        return id
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    companion object {
        private const val TAG = "MyHomeFragment"
    }
}