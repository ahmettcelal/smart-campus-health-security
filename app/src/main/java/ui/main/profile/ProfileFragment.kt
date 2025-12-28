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
import model.UserRole
import ui.admin.AdminPanelActivity
import ui.auth.LoginActivity
import ui.event.EventDetailActivity
import ui.main.feed.EventAdapter
import util.EventManager
import util.UserPreferences

// ✅ Yeni ek: ChangePasswordActivity import
import ui.profile.ChangePasswordActivity

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

    // ✅ Yeni ek: Şifre değiştir butonu
    private lateinit var btnChangePassword: MaterialButton

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

        // ✅ Yeni ek: XML’de eklediğin butonun id’si bu olmalı
        btnChangePassword = view.findViewById(R.id.btnChangePassword)
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

            // ✅ Admin için şifre değiştir butonu yine görünsün
            btnChangePassword.visibility = View.VISIBLE

            // Admin için buton yerleşimi: Admin Panel -> Şifre Değiştir -> Çıkış
            val paramsChange = btnChangePassword.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
            paramsChange.topToBottom = R.id.btnAdminPanel
            paramsChange.topMargin = 16
            btnChangePassword.layoutParams = paramsChange

            val paramsLogout = btnLogout.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
            paramsLogout.topToBottom = R.id.btnChangePassword
            paramsLogout.topMargin = 16
            paramsLogout.bottomToBottom = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
            paramsLogout.bottomMargin = 16
            btnLogout.layoutParams = paramsLogout
        } else {
            btnAdminPanel.visibility = View.GONE
            tvFollowedEventsTitle.visibility = View.VISIBLE
            recyclerViewFollowedEvents.visibility = View.VISIBLE

            // ✅ User için de şifre değiştir butonu görünsün
            btnChangePassword.visibility = View.VISIBLE

            // User için buton yerleşimi: Takip edilenler -> Şifre Değiştir -> Çıkış
            val paramsChange = btnChangePassword.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
            paramsChange.topToBottom = R.id.recyclerViewFollowedEvents
            paramsChange.topMargin = 16
            btnChangePassword.layoutParams = paramsChange

            val paramsLogout = btnLogout.layoutParams as androidx.constraintlayout.widget.ConstraintLayout.LayoutParams
            paramsLogout.topToBottom = R.id.btnChangePassword
            paramsLogout.topMargin = 16
            paramsLogout.bottomToBottom = androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
            paramsLogout.bottomMargin = 16
            btnLogout.layoutParams = paramsLogout
        }

        // Çıkış butonu her zaman görünür
        btnChangePassword.visibility = View.VISIBLE
        btnLogout.visibility = View.VISIBLE
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

        // ✅ Yeni ek: Şifre Değiştir ekranına geç
        btnChangePassword.setOnClickListener {
            startActivity(Intent(requireContext(), ChangePasswordActivity::class.java))
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
