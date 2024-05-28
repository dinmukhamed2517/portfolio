package kz.sdk.portfolio.fragments

import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.portfolio.R
import kz.sdk.portfolio.adapters.EducationAdapter
import kz.sdk.portfolio.adapters.LicenseAdapter
import kz.sdk.portfolio.adapters.RewardAdapter
import kz.sdk.portfolio.adapters.SkillAdapter
import kz.sdk.portfolio.base.BaseFragment
import kz.sdk.portfolio.databinding.FragmentRewardsBinding
import kz.sdk.portfolio.firebase.UserDao
import kz.sdk.portfolio.models.Education
import kz.sdk.portfolio.models.Reward
import javax.inject.Inject


@AndroidEntryPoint

class RewardsFragment:BaseFragment<FragmentRewardsBinding>(FragmentRewardsBinding::inflate) {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private val rewards: MutableList<Reward> = mutableListOf()

    @Inject
    lateinit var userDao: UserDao
    override fun onBindView() {
        val rewardAdapter = RewardAdapter()
        super.onBindView()
        userDao.getData()

        with(binding) {
            rewardRecycler.adapter = rewardAdapter
            rewardRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

            addBtn.setOnClickListener {
                findNavController().navigate(R.id.action_rewardsFragment_to_addRewardFragment)
            }
            editBtn.setOnClickListener {
                findNavController().navigate(R.id.action_rewardsFragment_to_deleteRewardFragment)
            }
        }
        userDao.getDataLiveData.observe(viewLifecycleOwner) { user ->
            user?.let {
                rewards.clear()
                it.rewards?.values?.let { rewardList ->
                    rewards.addAll(rewardList)
                }
                rewardAdapter.submitList(rewards.toList())
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (firebaseAuth.currentUser == null) {
            findNavController().navigate(R.id.action_rewardsFragment_to_loginFragment)
        }
    }

}