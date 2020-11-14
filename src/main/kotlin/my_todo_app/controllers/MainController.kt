package my_todo_app.controllers

import my_todo_app.NoteRepository
import my_todo_app.security.MyRole
import my_todo_app.security.MyUser
import my_todo_app.security.MyUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@Suppress("unused")
@Controller
class MainController {
    @Autowired
    lateinit var noteRepository: NoteRepository

    @Autowired
    lateinit var myUserRepository: MyUserRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @GetMapping
    fun home() = "home"
//    fun home() = "redirect:/notes"

    @GetMapping("/register")
    fun register() = "register"

    @PostMapping("/register")
    fun register(registerUser: RegisterUser) =
            if (registerUser.isValid() && myUserRepository.findByUsername(registerUser.username) == null) {
                myUserRepository.save(registerUser.toUser(passwordEncoder))
                "redirect:/?registered"
            } else {
                "redirect:/register?error"
            }
}

data class RegisterUser(
        val username: String,
        val password: String,
        val password2: String
) {
    fun isValid(): Boolean {
        return password == password2
    }

    fun toUser(passwordEncoder: PasswordEncoder) = MyUser(username, passwordEncoder.encode(password), setOf(MyRole.USER))
}