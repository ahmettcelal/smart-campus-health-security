package model

/**
 * Olay bildirimi modeli
 */
data class Event(
    val id: String,
    val title: String,
    val description: String,
    val category: EventCategory,
    val status: EventStatus,
    val location: String? = null,    // Yer bilgisi (manuel girilen)
    val createdBy: String,            // Kullanıcı ID
    val createdByName: String,       // Kullanıcı adı
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val resolvedAt: Long? = null,    // Çözüldüğü zaman
    val imageUrls: List<String> = emptyList(),  // Olay fotoğrafları
    val adminNotes: String? = null,  // Admin notları
    val isFollowed: Boolean = false, // Kullanıcı takip ediyor mu?
    val followerCount: Int = 0       // Kaç kişi takip ediyor?
) {
    fun isResolved(): Boolean = status == EventStatus.RESOLVED
    fun isClosed(): Boolean = status == EventStatus.CLOSED
    fun isOpen(): Boolean = status == EventStatus.OPEN
    fun isInProgress(): Boolean = status == EventStatus.IN_PROGRESS
}






