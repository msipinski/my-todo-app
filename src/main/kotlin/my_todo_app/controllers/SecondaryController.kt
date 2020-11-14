package my_todo_app.controllers

import org.springframework.security.access.annotation.Secured
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed

@Suppress("unused")
@RestController
@Secured("ROLE_ADMIN")
class SecondaryController {
    @GetMapping("/admin")
    fun admin() = "admin"
}