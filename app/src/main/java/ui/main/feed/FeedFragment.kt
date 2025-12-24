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
import com.ahmettcelal.smart_campus_health_security.R
import model.Event
import model.EventCategory
import model.EventStatus
import ui.event.CreateEventActivity
import ui.event.EventDetailActivity

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
        chipAll.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
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
        
        chipOpen.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) chipAll.isChecked = false
            applyFilters()
        }
        
        chipFollowed.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) chipAll.isChecked = false
            applyFilters()
        }
        
        chipHealth.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) chipAll.isChecked = false
            applyFilters()
        }
        
        chipSecurity.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) chipAll.isChecked = false
            applyFilters()
        }
        
        chipEnvironment.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) chipAll.isChecked = false
            applyFilters()
        }
        
        chipLostFound.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) chipAll.isChecked = false
            applyFilters()
        }
        
        chipTechnical.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) chipAll.isChecked = false
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
            startActivity(intent)
        }
    }

    private fun loadEvents() {
        // TODO: Gerçek uygulamada API'den veya veritabanından yüklenecek
        // Şimdilik test verisi
        allEvents = getTestEvents()
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

    private fun getTestEvents(): List<Event> {
        val now = System.currentTimeMillis()
        return listOf(
            Event(
                id = "1",
                title = "Kampüs içi güvenlik kamerası arızası",
                description = "A blok girişindeki güvenlik kamerası çalışmıyor. Acil müdahale gerekiyor.",
                category = EventCategory.SECURITY,
                status = EventStatus.OPEN,
                latitude = 39.9334,
                longitude = 32.8597,
                createdBy = "user1",
                createdByName = "Ahmet Yılmaz",
                createdAt = now - 3600000, // 1 saat önce
                isFollowed = false
            ),
            Event(
                id = "2",
                title = "Sağlık merkezi acil durum",
                description = "Öğrenci yaralanması, ambulans çağrıldı.",
                category = EventCategory.HEALTH,
                status = EventStatus.IN_PROGRESS,
                latitude = 39.9334,
                longitude = 32.8597,
                createdBy = "user2",
                createdByName = "Mehmet Demir",
                createdAt = now - 7200000, // 2 saat önce
                isFollowed = true
            ),
            Event(
                id = "3",
                title = "Çevre kirliliği - Çöp toplama",
                description = "B blok yanında çöp birikmesi var.",
                category = EventCategory.ENVIRONMENT,
                status = EventStatus.RESOLVED,
                latitude = 39.9334,
                longitude = 32.8597,
                createdBy = "user3",
                createdByName = "Ayşe Kaya",
                createdAt = now - 86400000, // 1 gün önce
                isFollowed = false
            ),
            Event(
                id = "4",
                title = "Kayıp cüzdan bildirimi",
                description = "Kütüphane önünde cüzdan kaybettim. İçinde kimlik ve kartlar var.",
                category = EventCategory.LOST_FOUND,
                status = EventStatus.OPEN,
                latitude = 39.9334,
                longitude = 32.8597,
                createdBy = "user4",
                createdByName = "Fatma Şahin",
                createdAt = now - 1800000, // 30 dakika önce
                isFollowed = true
            ),
            Event(
                id = "5",
                title = "Teknik arıza - Asansör çalışmıyor",
                description = "C blok asansörü çalışmıyor. Teknik servis çağrıldı.",
                category = EventCategory.TECHNICAL,
                status = EventStatus.IN_PROGRESS,
                latitude = 39.9334,
                longitude = 32.8597,
                createdBy = "user5",
                createdByName = "Ali Veli",
                createdAt = now - 10800000, // 3 saat önce
                isFollowed = false
            ),
            Event(
                id = "6",
                title = "Güvenlik uyarısı - Şüpheli kişi",
                description = "Kampüs girişinde şüpheli bir kişi görüldü. Güvenlik ekibi bilgilendirildi.",
                category = EventCategory.SECURITY,
                status = EventStatus.OPEN,
                latitude = 39.9334,
                longitude = 32.8597,
                createdBy = "user6",
                createdByName = "Zeynep Yıldız",
                createdAt = now - 900000, // 15 dakika önce
                isFollowed = false
            ),
            Event(
                id = "7",
                title = "Sağlık - İlk yardım ihtiyacı",
                description = "Spor salonunda bir öğrenci düştü. İlk yardım yapıldı.",
                category = EventCategory.HEALTH,
                status = EventStatus.RESOLVED,
                latitude = 39.9334,
                longitude = 32.8597,
                createdBy = "user7",
                createdByName = "Can Öz",
                createdAt = now - 14400000, // 4 saat önce
                isFollowed = false
            ),
            Event(
                id = "8",
                title = "Çevre - Ağaç budama",
                description = "Kampüs içindeki ağaçların budanması gerekiyor.",
                category = EventCategory.ENVIRONMENT,
                status = EventStatus.OPEN,
                latitude = 39.9334,
                longitude = 32.8597,
                createdBy = "user8",
                createdByName = "Deniz Ak",
                createdAt = now - 21600000, // 6 saat önce
                isFollowed = true
            ),
            Event(
                id = "9",
                title = "Buluntu - Telefon",
                description = "Kafeteryada bir telefon buldum. Kayıp eşya bürosuna teslim edildi.",
                category = EventCategory.LOST_FOUND,
                status = EventStatus.RESOLVED,
                latitude = 39.9334,
                longitude = 32.8597,
                createdBy = "user9",
                createdByName = "Elif Su",
                createdAt = now - 172800000, // 2 gün önce
                isFollowed = false
            ),
            Event(
                id = "10",
                title = "Teknik - WiFi bağlantı sorunu",
                description = "D blokta WiFi bağlantısı çalışmıyor. Teknik ekip müdahale ediyor.",
                category = EventCategory.TECHNICAL,
                status = EventStatus.IN_PROGRESS,
                latitude = 39.9334,
                longitude = 32.8597,
                createdBy = "user10",
                createdByName = "Burak Kaya",
                createdAt = now - 5400000, // 1.5 saat önce
                isFollowed = false
            ),
            Event(
                id = "11",
                title = "Güvenlik - Park yeri ihlali",
                description = "Engelli park yerine izinsiz park edilmiş araç var.",
                category = EventCategory.SECURITY,
                status = EventStatus.OPEN,
                latitude = 39.9334,
                longitude = 32.8597,
                createdBy = "user11",
                createdByName = "Selin Demir",
                createdAt = now - 2700000, // 45 dakika önce
                isFollowed = false
            ),
            Event(
                id = "12",
                title = "Sağlık - Hijyen uyarısı",
                description = "Yemekhane lavabolarında sabun bitmiş. Yenilenmesi gerekiyor.",
                category = EventCategory.HEALTH,
                status = EventStatus.OPEN,
                latitude = 39.9334,
                longitude = 32.8597,
                createdBy = "user12",
                createdByName = "Mert Yılmaz",
                createdAt = now - 3600000, // 1 saat önce
                isFollowed = false
            )
        )
    }
}