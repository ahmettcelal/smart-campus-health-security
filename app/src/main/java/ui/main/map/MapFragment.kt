package ui.main.map

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.ahmettcelal.smart_campus_health_security.R
import model.Event
import model.EventCategory
import ui.event.EventDetailActivity

/**
 * Harita ekranı
 */
class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var googleMap: GoogleMap
    private lateinit var cardPinInfo: MaterialCardView
    private lateinit var tvPinCategoryIcon: TextView
    private lateinit var tvPinTitle: TextView
    private lateinit var tvPinCategory: TextView
    private lateinit var tvPinTime: TextView
    private lateinit var btnPinDetail: MaterialButton

    private var events: List<Event> = emptyList()
    private var markerEventMap: HashMap<Marker, Event> = HashMap()
    private var selectedEvent: Event? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        
        initViews(view)
        setupMap()
        loadEvents()
        
        return view
    }

    private fun initViews(view: View) {
        cardPinInfo = view.findViewById(R.id.cardPinInfo)
        tvPinCategoryIcon = view.findViewById(R.id.tvPinCategoryIcon)
        tvPinTitle = view.findViewById(R.id.tvPinTitle)
        tvPinCategory = view.findViewById(R.id.tvPinCategory)
        tvPinTime = view.findViewById(R.id.tvPinTime)
        btnPinDetail = view.findViewById(R.id.btnPinDetail)

        btnPinDetail.setOnClickListener {
            selectedEvent?.let { event ->
                val intent = Intent(requireContext(), EventDetailActivity::class.java).apply {
                    putExtra("event_id", event.id)
                }
                startActivity(intent)
            }
        }
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        
        // Harita ayarları
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabled = true
        googleMap.uiSettings.isTiltGesturesEnabled = true
        googleMap.uiSettings.isRotateGesturesEnabled = true
        
        // Marker tıklama listener
        googleMap.setOnMarkerClickListener(this)
        
        // Harita tıklanınca kartı gizle
        googleMap.setOnMapClickListener {
            cardPinInfo.visibility = View.GONE
            selectedEvent = null
        }
        
        // Kampüs konumuna zoom (Ankara örnek koordinat)
        val campusLocation = LatLng(39.9334, 32.8597)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(campusLocation, 15f))
        
        // Event'leri haritaya ekle
        addEventsToMap()
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        val event = markerEventMap[marker]
        if (event != null) {
            selectedEvent = event
            showPinInfoCard(event)
            return true
        }
        return false
    }

    private fun addEventsToMap() {
        events.forEach { event ->
            val location = LatLng(event.latitude, event.longitude)
            val markerOptions = MarkerOptions()
                .position(location)
                .title(event.title)
                .snippet(event.description)
                .icon(BitmapDescriptorFactory.defaultMarker(getCategoryColor(event.category)))
            
            val marker = googleMap.addMarker(markerOptions)
            marker?.let {
                markerEventMap[it] = event
            }
        }
    }

    private fun getCategoryColor(category: EventCategory): Float {
        return when (category) {
            EventCategory.HEALTH -> BitmapDescriptorFactory.HUE_RED
            EventCategory.SECURITY -> BitmapDescriptorFactory.HUE_BLUE
            EventCategory.ENVIRONMENT -> BitmapDescriptorFactory.HUE_GREEN
            EventCategory.LOST_FOUND -> BitmapDescriptorFactory.HUE_ORANGE
            EventCategory.TECHNICAL -> BitmapDescriptorFactory.HUE_YELLOW
        }
    }

    private fun showPinInfoCard(event: Event) {
        tvPinCategoryIcon.text = event.category.icon
        tvPinTitle.text = event.title
        tvPinCategory.text = event.category.displayName
        tvPinTime.text = formatTime(event.createdAt)
        
        cardPinInfo.visibility = View.VISIBLE
    }

    private fun formatTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            days > 0 -> "$days gün önce"
            hours > 0 -> "$hours saat önce"
            minutes > 0 -> "$minutes dakika önce"
            else -> "Az önce"
        }
    }

    private fun loadEvents() {
        // TODO: Gerçek uygulamada API'den veya veritabanından yüklenecek
        // Şimdilik FeedFragment'taki test verilerini kullan
        events = getTestEvents()
        
        // Eğer harita hazırsa event'leri ekle
        if (::googleMap.isInitialized) {
            addEventsToMap()
        }
    }

    private fun getTestEvents(): List<Event> {
        val now = System.currentTimeMillis()
        return listOf(
            Event(
                id = "1",
                title = "Kampüs içi güvenlik kamerası arızası",
                description = "A blok girişindeki güvenlik kamerası çalışmıyor.",
                category = EventCategory.SECURITY,
                status = model.EventStatus.OPEN,
                latitude = 39.9334,
                longitude = 32.8597,
                createdBy = "user1",
                createdByName = "Ahmet Yılmaz",
                createdAt = now - 3600000,
                isFollowed = false
            ),
            Event(
                id = "2",
                title = "Sağlık merkezi acil durum",
                description = "Öğrenci yaralanması, ambulans çağrıldı.",
                category = EventCategory.HEALTH,
                status = model.EventStatus.IN_PROGRESS,
                latitude = 39.9340,
                longitude = 32.8600,
                createdBy = "user2",
                createdByName = "Mehmet Demir",
                createdAt = now - 7200000,
                isFollowed = true
            ),
            Event(
                id = "3",
                title = "Çevre kirliliği - Çöp toplama",
                description = "B blok yanında çöp birikmesi var.",
                category = EventCategory.ENVIRONMENT,
                status = model.EventStatus.RESOLVED,
                latitude = 39.9328,
                longitude = 32.8590,
                createdBy = "user3",
                createdByName = "Ayşe Kaya",
                createdAt = now - 86400000,
                isFollowed = false
            ),
            Event(
                id = "4",
                title = "Kayıp cüzdan bildirimi",
                description = "Kütüphane önünde cüzdan kaybettim.",
                category = EventCategory.LOST_FOUND,
                status = model.EventStatus.OPEN,
                latitude = 39.9338,
                longitude = 32.8595,
                createdBy = "user4",
                createdByName = "Fatma Şahin",
                createdAt = now - 1800000,
                isFollowed = true
            ),
            Event(
                id = "5",
                title = "Teknik arıza - Asansör çalışmıyor",
                description = "C blok asansörü çalışmıyor.",
                category = EventCategory.TECHNICAL,
                status = model.EventStatus.IN_PROGRESS,
                latitude = 39.9330,
                longitude = 32.8605,
                createdBy = "user5",
                createdByName = "Ali Veli",
                createdAt = now - 10800000,
                isFollowed = false
            )
        )
    }
}