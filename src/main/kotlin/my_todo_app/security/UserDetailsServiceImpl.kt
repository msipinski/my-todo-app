package my_todo_app.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class UserDetailsServiceImpl : UserDetailsService {
    @Autowired
    lateinit var myUserRepository: MyUserRepository

    override fun loadUserByUsername(username: String?): UserDetails = MyUserDetails(
            myUserRepository.findByUsername(username!!)
                    ?: throw UsernameNotFoundException("User $username not found")
    )
}