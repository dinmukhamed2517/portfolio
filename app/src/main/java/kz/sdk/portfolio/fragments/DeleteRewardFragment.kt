package kz.sdk.portfolio.fragments

import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.portfolio.adapters.RewardAdapter
import kz.sdk.portfolio.base.BaseFragment
import kz.sdk.portfolio.databinding.FragmentDeleteRewardBinding
import kz.sdk.portfolio.firebase.UserDao
import kz.sdk.portfolio.models.Reward
import javax.inject.Inject

@AndroidEntryPoint

class DeleteRewardFragment:BaseFragment<FragmentDeleteRewardBinding>(FragmentDeleteRewardBinding::inflate){
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var userDao: UserDao
    private var rewards: MutableList<Reward> = mutableListOf()

    override fun onBindView() {
        super.onBindView()
        userDao.getData()

        val adapter = RewardAdapter()
        binding.rewardRecycler.adapter = adapter
        binding.rewardRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        userDao.getDataLiveData.observe(this){user ->
            rewards.clear()
            user?.rewards?.values?.let {rewardList ->
                rewards.addAll(rewardList)
            }
            adapter.submitList(rewards)
        }

        adapter.itemClick = {reward ->
            val keyToDelete = userDao.getDataLiveData.value?.rewards?.filterValues { it.title== reward.title }?.keys?.firstOrNull()
            keyToDelete?.let { key ->
                userDao.deleteRewardFromList(key)
                val updatedRewards = ArrayList(rewards).apply {
                    remove(reward)
                }
                adapter.submitList(updatedRewards)
                rewards = updatedRewards
            } ?: run {
                Log.e("CartFragment", "Failed to find key for product deletion")
            }
        }
    }
}