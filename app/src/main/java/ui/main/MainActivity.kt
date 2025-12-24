package ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.ahmettcelal.smart_campus_health_security.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import ui.main.feed.FeedFragment
import ui.main.map.MapFragment
import ui.main.profile.ProfileFragment

class MainActivity : AppCompatActivity() {
    
    private lateinit var bottomNavigation: BottomNavigationView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        // Uygulama başlığını ayarla
        supportActionBar?.title = getString(R.string.app_name)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        bottomNavigation = findViewById(R.id.bottomNavigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_feed -> {
                    replaceFragment(FeedFragment())
                    true
                }
                R.id.nav_map -> {
                    replaceFragment(MapFragment())
                    true
                }
                R.id.nav_profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
        
        // İlk fragment'ı göster
        if (savedInstanceState == null) {
            replaceFragment(FeedFragment())
        }
    }
    
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}