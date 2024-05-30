package kz.sdk.portfolio.fragments

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.portfolio.R
import kz.sdk.portfolio.adapters.DocumentAdapter
import kz.sdk.portfolio.adapters.EventAdapter
import kz.sdk.portfolio.adapters.FilterAdapter
import kz.sdk.portfolio.base.BaseFragment
import kz.sdk.portfolio.databinding.FragmentDocumentsBinding
import kz.sdk.portfolio.firebase.UserDao
import kz.sdk.portfolio.models.Document
import kz.sdk.portfolio.models.Filter
import javax.inject.Inject



@AndroidEntryPoint
class DocumentsFragment : BaseFragment<FragmentDocumentsBinding>(FragmentDocumentsBinding::inflate) {

    private lateinit var adapter: DocumentAdapter
    private lateinit var filterAdapter: FilterAdapter

    private val documents = mutableListOf<Document>()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    @Inject
    lateinit var userDao: UserDao

    private var selectedFilterTitle: String? = null

    override fun onBindView() {
        super.onBindView()
        userDao.getData()

        adapter = DocumentAdapter()
        filterAdapter = FilterAdapter().apply {
            submitList(getFilters())
            itemClick = { filter ->
                selectedFilterTitle = filter.title
                applyFilter()
            }
        }

        with(binding) {
            editBtn.setOnClickListener {
                findNavController().navigate(R.id.action_documentsFragment_to_deleteDocumentFragment)
            }
            addBtn.setOnClickListener {
                findNavController().navigate(R.id.action_documentsFragment_to_addDocumentFragment)
            }
            documentRecycler.adapter = adapter
            documentRecycler.layoutManager = LinearLayoutManager(requireContext())
            filterRecycler.adapter = filterAdapter
            filterRecycler.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        observeDocuments()
    }

    private fun observeDocuments() {
        userDao.getDataLiveData.observe(viewLifecycleOwner) { user ->
            user?.documents?.values?.let {
                documents.clear()
                documents.addAll(it)
                applyFilter()
            }
        }
    }

    private fun applyFilter() {
        val filteredDocuments = if (selectedFilterTitle.isNullOrEmpty()) {
            documents
        } else {
            documents.filter { it.category == selectedFilterTitle }
        }
        adapter.submitList(filteredDocuments)
    }

    private fun getFilters(): List<Filter> {
        return listOf(
            Filter(1, "Диссертация"),
            Filter(2, "Статья"),
            Filter(3, "Монография")
        )
    }
}