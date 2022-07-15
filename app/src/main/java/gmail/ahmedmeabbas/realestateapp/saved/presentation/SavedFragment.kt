package gmail.ahmedmeabbas.realestateapp.saved.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import gmail.ahmedmeabbas.realestateapp.R
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentSavedBinding

private const val NUM_TABS = 2

class SavedFragment : Fragment() {

    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewPager()
        setUpTabLayout()
        setUpTabLayoutMediator()
    }

    private fun setUpViewPager() {
        val pagerAdapter = SavedFragmentPagerAdapter(this)
        binding.savedViewPager.adapter = pagerAdapter
    }

    private fun setUpTabLayoutMediator() {
        TabLayoutMediator(binding.savedTabLayout, binding.savedViewPager) { tab, position ->
            if (position == 0) {
                tab.text = getString(R.string.saved_homes_tab)
                tab.icon = ResourcesCompat.getDrawable(
                    requireActivity().resources,
                    R.drawable.custom_home,
                    requireActivity().theme
                )
            } else {
                tab.text = getString(R.string.saved_searches_tab)
                tab.icon = ResourcesCompat.getDrawable(
                    requireActivity().resources,
                    R.drawable.ic_search,
                    requireActivity().theme
                )
            }
        }.attach()
    }

    private fun setUpTabLayout() {
        binding.savedTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.savedViewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class SavedFragmentPagerAdapter(fragment: Fragment) :
        FragmentStateAdapter(fragment) {
        override fun getItemCount() = NUM_TABS

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> SavedHomesFragment()
                1 -> SavedSearchesFragment()
                else -> SavedHomesFragment()
            }
        }
    }
}