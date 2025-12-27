package model

/**
 * Kullanıcı modeli
 */
data class User(
    val id: String,
    val name: String,
    val email: String,
    val role: UserRole,
    val phoneNumber: String? = null,
    val profileImageUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun isAdmin(): Boolean = role == UserRole.ADMIN
    fun isUser(): Boolean = role == UserRole.USER
}






