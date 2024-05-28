package kz.sdk.portfolio.fragments

import android.system.Os.remove
import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.portfolio.adapters.EducationAdapter
import kz.sdk.portfolio.base.BaseFragment
import kz.sdk.portfolio.databinding.FragmentDeleteEducationBinding
import kz.sdk.portfolio.firebase.UserDao
import kz.sdk.portfolio.models.Education
import javax.inject.Inject


@AndroidEntryPoint
class DeleteEducationFragment:BaseFragment<FragmentDeleteEducationBinding>(FragmentDeleteEducationBinding::inflate) {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var userDao: UserDao
    private var educations: MutableList<Education> = mutableListOf()


    override fun onBindView() {
        super.onBindView()
        userDao.getData()

        val adapter = EducationAdapter()
        binding.educationRecycler.adapter = adapter
        binding.educationRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        userDao.getDataLiveData.observe(this){user ->
            educations.clear()
            user?.educations?.values?.let {educationList ->
                educations.addAll(educationList)
            }
            adapter.submitList(educations)
        }

        adapter.itemClick = {education ->
            val keyToDelete = userDao.getDataLiveData.value?.educations?.filterValues { it.title== education.title }?.keys?.firstOrNull()
            keyToDelete?.let { key ->
                userDao.deleteEducationFromList(key)
                val updatedEducations = ArrayList(educations).apply {
                    remove(education)
                }
                adapter.submitList(updatedEducations)
                educations = updatedEducations
            } ?: run {
                Log.e("CartFragment", "Failed to find key for product deletion")
            }
        }
    }


}