package kz.sdk.portfolio.fragments

import android.system.Os.remove
import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.portfolio.adapters.EducationAdapter
import kz.sdk.portfolio.adapters.LicenseAdapter
import kz.sdk.portfolio.base.BaseFragment
import kz.sdk.portfolio.databinding.FragmentDeleteLicenseBinding
import kz.sdk.portfolio.firebase.UserDao
import kz.sdk.portfolio.models.Education
import kz.sdk.portfolio.models.License
import javax.inject.Inject



@AndroidEntryPoint
class DeleteLicenseFragment:BaseFragment<FragmentDeleteLicenseBinding>(FragmentDeleteLicenseBinding::inflate) {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var userDao: UserDao
    private var licenses: MutableList<License> = mutableListOf()


    override fun onBindView() {
        super.onBindView()
        userDao.getData()

        val adapter = LicenseAdapter()
        binding.licenseRecycler.adapter = adapter
        binding.licenseRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        userDao.getDataLiveData.observe(this){user ->
            licenses.clear()
            user?.licenses?.values?.let {licenseList ->
                licenses.addAll(licenseList)
            }
            adapter.submitList(licenses)
        }

        adapter.itemClick = {license ->
            val keyToDelete = userDao.getDataLiveData.value?.licenses?.filterValues { it.title== license.title }?.keys?.firstOrNull()
            keyToDelete?.let { key ->
                userDao.deleteLicenseFromList(key)
                val updatedLicenses = ArrayList(licenses).apply {
                    remove(license)
                }
                adapter.submitList(updatedLicenses)
                licenses = updatedLicenses
            } ?: run {
                Log.e("CartFragment", "Failed to find key for product deletion")
            }
        }
    }
}