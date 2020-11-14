package my_todo_app.security

import org.springframework.data.repository.CrudRepository
import org.springframework.security.core.GrantedAuthority
import javax.persistence.*

interface MyUserRepository : CrudRepository<MyUser, Long> {
    fun findByUsername(username: String): MyUser?
}

@Entity
data class MyUser(
        @Column(unique = true, nullable = false)
        val username: String,
        val password: String,
        @ElementCollection(fetch = FetchType.EAGER)
        val roles: Set<MyRole>,
) {
    @Suppress("unused")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id = 0L
}

enum class MyRole : GrantedAuthority {
    USER,
    ADMIN;

    override fun getAuthority(): String = "ROLE_$this"
}
