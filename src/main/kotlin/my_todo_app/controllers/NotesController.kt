package my_todo_app.controllers

import my_todo_app.Note
import my_todo_app.NoteRepository
import my_todo_app.Status
import my_todo_app.security.MyUser
import my_todo_app.security.MyUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.ui.set
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import java.security.Principal
import java.time.LocalDate

@Suppress("unused")
@PreAuthorize("isAuthenticated()")
@Controller
class NotesController {
    @Autowired
    lateinit var noteRepository: NoteRepository

    @Autowired
    lateinit var myUserRepository: MyUserRepository

    @GetMapping("/notes")
    fun notes(principal: Principal) = "notes"

    //TODO: show error if no note
    @GetMapping("/note")
    fun note(model: Model, id: Long, principal: Principal) = "note".also {
        noteRepository.findByIdAndOwnerUsername(id, principal.name)?.let { note ->
            model["note"] = note
        }
    }

    @PostMapping("/add-note")
    fun addNote(@ModelAttribute note: NoteRequest, principal: Principal) = "redirect:/notes?added".also {
        noteRepository.save(note.toNote(myUserRepository.findByUsername(principal.name)!!))
    }

    @PostMapping("/update-note")
    fun updateNote(note: NoteRequest, principal: Principal) = "redirect:/notes?updated".also {
        noteRepository.save(note.toNote(myUserRepository.findByUsername(principal.name)!!))
    }

    //TODO: change to POST
    @GetMapping("/delete-note")
    fun deleteNote(id: Long, principal: Principal) = "redirect:/notes?deleted".also {
        val note = noteRepository.findById(id).orElse(null)
        if (note?.owner?.username == principal.name) {
            noteRepository.delete(note)
        }
    }
}

data class NoteRequest(
        val id: Long?,
        val title: String,
        val status: Status,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        val dueDate: LocalDate
) {
    fun toNote(owner: MyUser) = Note(title, status, dueDate, owner).apply { id = this@NoteRequest.id ?: 0 }
}
