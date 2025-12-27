package ui.main.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ahmettcelal.akillikampusaglikguvenlikuygulamasi.R

/**
 * Harita ekranı - Kullanımdan kaldırıldı
 */
class MapFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Harita özelliği kaldırıldı, boş fragment döndür
        return View(requireContext())
    }
}
