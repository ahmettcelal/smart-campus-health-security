package ui.main.feed

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.ahmettcelal.akillikampusaglikguvenlikuygulamasi.R
import model.Event
import model.EventCategory
import model.EventStatus
import ui.event.CreateEventActivity
import ui.event.EventDetailActivity
import util.EventManager

/**
 * Anasayfa ekranı - Bildirim akışı
 */
class FeedFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EventAdapter
    private lateinit var etSearch: TextInputEditText
    private lateinit var fabCreateEvent: FloatingActionButton
    
    // Filter chips
    private lateinit var chipAll: Chip
    private lateinit var chipOpen: Chip
    private lateinit var chipFollowed: Chip
    private lateinit var chipHealth: Chip
    private lateinit var chipSecurity: Chip
    private lateinit var chipEnvironment: Chip
    private lateinit var chipLostFound: Chip
    private lateinit var chipTechnical: Chip

    private var allEvents: List<Event> = emptyList()
    private var filteredEvents: List<Event> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feed, container, false)
        
        initViews(view)
        setupRecyclerView()
        setupFilters()
        setupSearch()
        setupFab()
        loadEvents()
        
        return view
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerViewEvents)
        etSearch = view.findViewById(R.id.etSearch)
        fabCreateEvent = view.findViewById(R.id.fabCreateEvent)
        
        chipAll = view.findViewById(R.id.chipAll)
        chipOpen = view.findViewById(R.id.chipOpen)
        chipFollowed = view.findViewById(R.id.chipFollowed)
        chipHealth = view.findViewById(R.id.chipHealth)
        chipSecurity = view.findViewById(R.id.chipSecurity)
        chipEnvironment = view.findViewById(R.id.chipEnvironment)
        chipLostFound = view.findViewById(R.id.chipLostFound)
        chipTechnical = view.findViewById(R.id.chipTechnical)
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter(emptyList()) { event ->
            val intent = Intent(requireContext(), EventDetailActivity::class.java).apply {
                putExtra("event_id", event.id)
            }
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private fun setupFilters() {
        // Chip'leri tıklanabilir yap
        chipAll.isClickable = true
        chipOpen.isClickable = true
        chipFollowed.isClickable = true
        chipHealth.isClickable = true
        chipSecurity.isClickable = true
        chipEnvironment.isClickable = true
        chipLostFound.isClickable = true
        chipTechnical.isClickable = true
        
        chipAll.setOnClickListener {
            if (!chipAll.isChecked) {
                chipAll.isChecked = true
                // Tümü seçiliyse diğer filtreleri temizle
                chipOpen.isChecked = false
                chipFollowed.isChecked = false
                chipHealth.isChecked = false
                chipSecurity.isChecked = false
                chipEnvironment.isChecked = false
                chipLostFound.isChecked = false
                chipTechnical.isChecked = false
            }
            applyFilters()
        }
        
        chipOpen.setOnClickListener {
            chipOpen.isChecked = !chipOpen.isChecked
            if (chipOpen.isChecked) chipAll.isChecked = false
            applyFilters()
        }
        
        chipFollowed.setOnClickListener {
            chipFollowed.isChecked = !chipFollowed.isChecked
            if (chipFollowed.isChecked) chipAll.isChecked = false
            applyFilters()
        }
        
        chipHealth.setOnClickListener {
            chipHealth.isChecked = !chipHealth.isChecked
            if (chipHealth.isChecked) chipAll.isChecked = false
            applyFilters()
        }
        
        chipSecurity.setOnClickListener {
            chipSecurity.isChecked = !chipSecurity.isChecked
            if (chipSecurity.isChecked) chipAll.isChecked = false
            applyFilters()
        }
        
        chipEnvironment.setOnClickListener {
            chipEnvironment.isChecked = !chipEnvironment.isChecked
            if (chipEnvironment.isChecked) chipAll.isChecked = false
            applyFilters()
        }
        
        chipLostFound.setOnClickListener {
            chipLostFound.isChecked = !chipLostFound.isChecked
            if (chipLostFound.isChecked) chipAll.isChecked = false
            applyFilters()
        }
        
        chipTechnical.setOnClickListener {
            chipTechnical.isChecked = !chipTechnical.isChecked
            if (chipTechnical.isChecked) chipAll.isChecked = false
            applyFilters()
        }
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                applyFilters()
            }
        })
    }

    private fun setupFab() {
        fabCreateEvent.setOnClickListener {
            val intent = Intent(requireContext(), CreateEventActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_CREATE_EVENT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CREATE_EVENT && resultCode == android.app.Activity.RESULT_OK) {
            // Yeni bildirim oluşturuldu, listeyi yenile
            loadEvents()
        }
    }

    override fun onResume() {
        super.onResume()
        // Fragment görünür olduğunda listeyi yenile (yeni bildirimler için)
        loadEvents()
    }

    companion object {
        private const val REQUEST_CODE_CREATE_EVENT = 1001
    }

    private fun loadEvents() {
        // TODO: Gerçek uygulamada API'den veya veritabanından yüklenecek
        // EventManager'dan tüm bildirimleri al (test verileri dahil)
        allEvents = EventManager.getAllEvents()
        applyFilters()
    }

    private fun applyFilters() {
        var filtered = allEvents.toList()

        // Eğer "Tümü" seçili değilse filtreleri uygula
        if (!chipAll.isChecked) {
            // Status filter
            if (chipOpen.isChecked) {
                filtered = filtered.filter { it.status == EventStatus.OPEN }
            }

            // Followed filter
            if (chipFollowed.isChecked) {
                filtered = filtered.filter { it.isFollowed }
            }

            // Category filters
            val selectedCategories = mutableListOf<EventCategory>()
            if (chipHealth.isChecked) selectedCategories.add(EventCategory.HEALTH)
            if (chipSecurity.isChecked) selectedCategories.add(EventCategory.SECURITY)
            if (chipEnvironment.isChecked) selectedCategories.add(EventCategory.ENVIRONMENT)
            if (chipLostFound.isChecked) selectedCategories.add(EventCategory.LOST_FOUND)
            if (chipTechnical.isChecked) selectedCategories.add(EventCategory.TECHNICAL)
            
            if (selectedCategories.isNotEmpty()) {
                filtered = filtered.filter { it.category in selectedCategories }
            }
        }

        // Search filter (her zaman çalışır)
        val searchQuery = etSearch.text.toString().trim().lowercase()
        if (searchQuery.isNotEmpty()) {
            filtered = filtered.filter {
                it.title.lowercase().contains(searchQuery) ||
                it.description.lowercase().contains(searchQuery)
            }
        }

        // Sort by creation time (newest first)
        filtered = filtered.sortedByDescending { it.createdAt }

        filteredEvents = filtered
        adapter.updateEvents(filteredEvents)
    }
}