package model

/**
 * Olay bildirimi kategorileri
 */
enum class EventCategory(val displayName: String, val icon: String) {
    HEALTH("Sağlık", "🏥"),
    SECURITY("Güvenlik", "🔒"),
    ENVIRONMENT("Çevre", "🌱"),
    LOST_FOUND("Kayıp-Buluntu", "🔍"),
    TECHNICAL("Teknik Arıza", "🔧")
}






