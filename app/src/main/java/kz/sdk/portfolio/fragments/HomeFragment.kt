package kz.sdk.portfolio.fragments

import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import kz.sdk.portfolio.R
import kz.sdk.portfolio.adapters.EventAdapter
import kz.sdk.portfolio.base.BaseFragment
import kz.sdk.portfolio.databinding.FragmentHomeBinding
import kz.sdk.portfolio.models.Event

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private lateinit var adapter: EventAdapter
    override fun onBindView() {
        super.onBindView()
        adapter = EventAdapter()
        loadEvents()
        with(binding) {
            eventRecycler.adapter = adapter
            eventRecycler.layoutManager = LinearLayoutManager(requireContext())
            editBtn.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_deleteEventsFragment)
            }
        }
        adapter.itemClick = {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToEventDetailsFragment(it))
        }
    }

    private fun loadEvents() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("Events")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val events = mutableListOf<Event>()
                snapshot.children.forEach { dataSnapshot ->
                    val event = dataSnapshot.getValue(Event::class.java)
                    event?.let {
                        events.add(it)
                    }
                }
                adapter.submitList(events)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load events: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}