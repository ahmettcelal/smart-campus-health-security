package model

/**
 * Olay bildirimi durumları
 */
enum class EventStatus(val displayName: String) {
    OPEN("Açık"),                    // Yeni bildirilen olay
    IN_PROGRESS("İnceleniyor"),      // İncelenmekte olan olay
    RESOLVED("Çözüldü"),             // Çözülmüş olay
    CLOSED("Sonlandırıldı")          // Yanlış/uygunsuz olay (admin tarafından kapatıldı)
}

