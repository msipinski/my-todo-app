package my_todo_app.controllers

import com.samskivert.mustache.Mustache
import my_todo_app.NoteRepository
import my_todo_app.security.MyUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import java.security.Principal
import java.time.LocalDate
import javax.servlet.http.HttpServletRequest

@Suppress("unused")
@PreAuthorize("isAuthenticated()")
@ControllerAdvice(assignableTypes = [NotesController::class])
class NotesControllerAdvice {
    @Autowired
    lateinit var noteRepository: NoteRepository

    @Autowired
    lateinit var myUserRepository: MyUserRepository

    @ModelAttribute("dateToday")
    fun dateToday() = LocalDate.now()!!

    @ModelAttribute("notes")
    fun notes(principal: Principal) = Mustache.Lambda { frag, out ->
        noteRepository.findByOwnerUsername(principal.name).forEach {
            frag.execute(it, out)
        }
    }

    @ModelAttribute("alerts")
    fun alerts(request: HttpServletRequest): Map<String, Boolean> {
        val result = mutableMapOf<String, Boolean>()
        listOf(
                "added",
                "deleted",
                "updated"
        ).forEach {
            if (request.parameterMap.contains(it)) {
                result[it] = true
            }
        }
        return result
    }
}

