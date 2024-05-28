package kz.sdk.portfolio.fragments

import android.system.Os.remove
import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kz.sdk.portfolio.adapters.LicenseAdapter
import kz.sdk.portfolio.adapters.SkillAdapter
import kz.sdk.portfolio.base.BaseFragment
import kz.sdk.portfolio.databinding.FragmentDeleteLicenseBinding
import kz.sdk.portfolio.databinding.FragmentDeleteSkillBinding
import kz.sdk.portfolio.firebase.UserDao
import kz.sdk.portfolio.models.License
import kz.sdk.portfolio.models.Skill
import javax.inject.Inject

class DeleteSkillFragment:BaseFragment<FragmentDeleteSkillBinding>(FragmentDeleteSkillBinding::inflate) {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var userDao: UserDao
    private var skills: MutableList<Skill> = mutableListOf()

    override fun onBindView() {
        super.onBindView()
        userDao.getData()

        val adapter = SkillAdapter()
        binding.skillRecycler.adapter = adapter
        binding.skillRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        userDao.getDataLiveData.observe(this){user ->
            skills.clear()
            user?.skills?.values?.let {skillList ->
                skills.addAll(skillList)
            }
            adapter.submitList(skills)
        }

        adapter.itemClick = {skill ->
            val keyToDelete = userDao.getDataLiveData.value?.licenses?.filterValues { it.title== skill.title }?.keys?.firstOrNull()
            keyToDelete?.let { key ->
                userDao.deleteSkillFromList(key)
                val updatedSkills = ArrayList(skills).apply {
                    remove(skill)
                }
                adapter.submitList(updatedSkills)
                skills = updatedSkills
            } ?: run {
                Log.e("CartFragment", "Failed to find key for product deletion")
            }
        }
    }

}