package ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.ahmettcelal.akillikampusaglikguvenlikuygulamasi.R
import model.Event
import model.UserRole
import ui.admin.AdminPanelActivity
import ui.auth.LoginActivity
import ui.event.EventDetailActivity
import ui.main.feed.EventAdapter
import util.EventManager
import util.UserPreferences

/**
 * Profil ve ayarlar ekranı
 */
class ProfileFragment : Fragment() {

    private lateinit var tvUserName: TextView
    private lateinit var tvUserEmail: TextView
    private lateinit var tvUserRole: TextView
    private lateinit var tvUserDepartment: TextView
    private lateinit var tvFollowedEventsTitle: TextView
    private lateinit var recyclerViewFollowedEvents: RecyclerView
    private lateinit var tvNoFollowedEvents: TextView
    private lateinit var btnAdminPanel: MaterialButton
    private lateinit var btnLogout: MaterialButton

    private lateinit var followedEventsAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        initViews(view)
        loadUserInfo()
        setupButtons()
        setupFollowedEvents()

        return view
    }

    override fun onResume() {
        super.onResume()
        // Fragment görünür olduğunda takip edilen bildirimleri yenile
        loadFollowedEvents()
    }

    private fun initViews(view: View) {
        tvUserName = view.findViewById(R.id.tvUserName)
        tvUserEmail = view.findViewById(R.id.tvUserEmail)
        tvUserRole = view.findViewById(R.id.tvUserRole)
        tvUserDepartment = view.findViewById(R.id.tvUserDepartment)
        tvFollowedEventsTitle = view.findViewById(R.id.tvFollowedEventsTitle)
        recyclerViewFollowedEvents = view.findViewById(R.id.recyclerViewFollowedEvents)
        tvNoFollowedEvents = view.findViewById(R.id.tvNoFollowedEvents)
        btnAdminPanel = view.findViewById(R.id.btnAdminPanel)
        btnLogout = view.findViewById(R.id.btnLogout)
    }

    private fun loadUserInfo() {
        val userEmail = UserPreferences.getUserEmail(requireContext())
        val userName = UserPreferences.getUserName(requireContext())
        val userRole = UserPreferences.getUserRole(requireContext())

        tvUserEmail.text = userEmail ?: "Bilinmeyen"
        tvUserName.text = userName ?: "Kullanıcı"
        tvUserRole.text = when (userRole) {
            UserRole.ADMIN -> "Admin"
            UserRole.USER -> "Kullanıcı"
            else -> "Bilinmeyen"
        }

        // Birim bilgisi (test için - gerçek uygulamada UserPreferences'tan gelecek)
        tvUserDepartment.text = when (userRole) {
            UserRole.ADMIN -> "Yönetim"
            UserRole.USER -> "Bilgisayar Mühendisliği"
            else -> "Bilinmeyen"
        }

        // Admin ise Admin Panel butonunu göster, takip edilen bildirimleri gizle
        if (userRole == UserRole.ADMIN) {
            btnAdminPanel.visibility = View.VISIBLE
            tvFollowedEventsTitle.visibility = View.GONE
            recyclerViewFollowedEvents.visibility = View.GONE
            tvNoFollowedEvents.visibility = View.GONE
        } else {
            btnAdminPanel.visibility = View.GONE
            tvFollowedEventsTitle.visibility = View.VISIBLE
            recyclerViewFollowedEvents.visibility = View.VISIBLE
        }
    }

    private fun setupFollowedEvents() {
        followedEventsAdapter = EventAdapter(emptyList()) { event ->
            val intent = Intent(requireContext(), EventDetailActivity::class.java).apply {
                putExtra("event_id", event.id)
            }
            startActivity(intent)
        }
        recyclerViewFollowedEvents.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewFollowedEvents.adapter = followedEventsAdapter
    }

    private fun loadFollowedEvents() {
        val userRole = UserPreferences.getUserRole(requireContext())
        
        // Sadece kullanıcılar için takip edilen bildirimleri göster
        if (userRole == UserRole.USER) {
            val allEvents = EventManager.getAllEvents()
            val followedEvents = allEvents.filter { it.isFollowed }
            
            if (followedEvents.isEmpty()) {
                recyclerViewFollowedEvents.visibility = View.GONE
                tvNoFollowedEvents.visibility = View.VISIBLE
            } else {
                recyclerViewFollowedEvents.visibility = View.VISIBLE
                tvNoFollowedEvents.visibility = View.GONE
                followedEventsAdapter.updateEvents(followedEvents)
            }
        }
    }

    private fun setupButtons() {
        btnAdminPanel.setOnClickListener {
            val intent = Intent(requireContext(), AdminPanelActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            // Kullanıcı bilgilerini temizle
            UserPreferences.clearUser(requireContext())

            // Login ekranına dön
            val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            requireActivity().finish()
        }
    }
}
