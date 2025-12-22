package model

/**
 * Bildirim modeli
 */
data class Notification(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val eventId: String? = null,      // Olay bildirimi ise event ID
    val userId: String,               // Bildirimi alan kullanıcı
    val isRead: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

/**
 * Bildirim tipleri
 */
enum class NotificationType {
    EVENT_STATUS_CHANGED,    // Olay durumu değişti
    EVENT_UPDATED,           // Olay güncellendi
    EMERGENCY_ALERT,         // Acil durum uyarısı (Admin tarafından)
    EVENT_FOLLOWED,          // Takip edilen olay güncellendi
    GENERAL                  // Genel bildirim
}

