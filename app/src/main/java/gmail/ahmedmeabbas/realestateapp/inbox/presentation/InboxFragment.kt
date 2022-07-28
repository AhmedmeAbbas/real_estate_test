package gmail.ahmedmeabbas.realestateapp.inbox.presentation

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
import gmail.ahmedmeabbas.realestateapp.databinding.FragmentInboxBinding

private const val NUM_TABS = 3

class InboxFragment : Fragment() {

    private var _binding: FragmentInboxBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInboxBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpViewPager()
        setUpTabLayout()
        setUpTabLayoutMediator()
    }

    private fun setUpViewPager() {
        val pagerAdapter = InboxFragmentPagerAdapter(this)
        binding.inboxViewPager.adapter = pagerAdapter
    }

    private fun setUpTabLayoutMediator() {
        TabLayoutMediator(binding.inboxTabLayout, binding.inboxViewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = getString(R.string.requests_tab)
                    tab.icon = ResourcesCompat.getDrawable(
                        requireActivity().resources,
                        R.drawable.ic_request,
                        requireActivity().theme
                    )
                }
                1 -> {
                    tab.text = getString(R.string.chats_tab)
                    tab.icon = ResourcesCompat.getDrawable(
                        requireActivity().resources,
                        R.drawable.custom_chat,
                        requireActivity().theme
                    )
                }
                2 -> {
                    tab.text = getString(R.string.notifications_tab)
                    tab.icon = ResourcesCompat.getDrawable(
                        requireActivity().resources,
                        R.drawable.custom_notification,
                        requireActivity().theme
                    )
                }
            }
        }.attach()
    }

    private fun setUpTabLayout() {
        binding.inboxTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.inboxViewPager.currentItem = tab!!.position
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

    private inner class InboxFragmentPagerAdapter(fragment: Fragment) :
        FragmentStateAdapter(fragment) {
        override fun getItemCount() = NUM_TABS

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> RequestsFragment()
                1 -> ChatsFragment()
                2 -> NotificationsFragment()
                else -> RequestsFragment()
            }
        }
    }
}