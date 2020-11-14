package my_todo_app.controllers

import com.samskivert.mustache.Mustache
import com.samskivert.mustache.Template
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ModelAttribute
import java.io.Writer
import java.security.Principal

@Suppress("unused")
@ControllerAdvice
class MyControllerAdvice {
    @ModelAttribute("authenticated")
    fun authenticated(principal: Principal?) = principal

    @ModelAttribute("layout")
    fun layout() = object : Mustache.Lambda {
        var body: Any? = null
        override fun execute(frag: Template.Fragment?, out: Writer?) {
            body = frag?.execute()
        }
    }
}