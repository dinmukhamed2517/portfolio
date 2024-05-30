package kz.sdk.portfolio.fragments

import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.portfolio.adapters.DocumentAdapter
import kz.sdk.portfolio.base.BaseFragment
import kz.sdk.portfolio.databinding.FragmentDeleteDocumentBinding
import kz.sdk.portfolio.firebase.UserDao
import kz.sdk.portfolio.models.Document
import javax.inject.Inject
@AndroidEntryPoint
class DeleteDocumentFragment:BaseFragment<FragmentDeleteDocumentBinding>(FragmentDeleteDocumentBinding::inflate) {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var userDao: UserDao
    private var documents: MutableList<Document> = mutableListOf()

    override fun onBindView() {
        super.onBindView()
        userDao.getData()

        val adapter = DocumentAdapter()
        binding.documentRecycler.adapter = adapter
        binding.documentRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.backBtn.setOnClickListener {
            findNavController().popBackStack()
        }
        userDao.getDataLiveData.observe(this){user ->
            documents.clear()
            user?.documents?.values?.let {rewardList ->
                documents.addAll(rewardList)
            }
            adapter.submitList(documents)
        }

        adapter.itemClick = {document ->
            val keyToDelete = userDao.getDataLiveData.value?.documents?.filterValues { it.title== document.title }?.keys?.firstOrNull()
            keyToDelete?.let { key ->
                userDao.deleteDocumentToList(key)
                val updatedDocuments = ArrayList(documents).apply {
                    remove(document)
                }
                adapter.submitList(updatedDocuments)
                documents = updatedDocuments
            } ?: run {
                Log.e("CartFragment", "Failed to find key for product deletion")
            }
        }
    }

}