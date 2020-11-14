package my_todo_app

import my_todo_app.security.MyUser
import org.springframework.data.repository.CrudRepository
import java.time.LocalDate
import javax.persistence.*

interface NoteRepository : CrudRepository<Note, Long> {
    fun findByOwnerUsername(username: String): Iterable<Note>
    fun findByIdAndOwnerUsername(id: Long, username: String): Note?
}

@Entity
data class Note(
        val title: String,
        val status: Status,
        val dueDate: LocalDate,
        @ManyToOne
        val owner: MyUser,
) {
    @Suppress("unused")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0
}

@Suppress("unused")
enum class Status {
    PENDING,
    IN_PROGRESS,
    DONE;

//    override fun toString() = name.split("_").joinToString(" ") { it.toLowerCase().capitalize() }
}
