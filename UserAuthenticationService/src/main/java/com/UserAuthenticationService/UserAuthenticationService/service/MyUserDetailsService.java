package com.UserAuthenticationService.UserAuthenticationService.service;



import com.UserAuthenticationService.UserAuthenticationService.entities.User;
import com.UserAuthenticationService.UserAuthenticationService.entities.UserPrincipal;
import com.UserAuthenticationService.UserAuthenticationService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository repo;
    /*
    ->This loadUsername method is called automatically by the provider.setUserDetailsService(userDetailsService)
    What username gets passed?
    ->The username comes directly from the HTTP request the Authorization header for HTTP Basic authentication.
    ->That username is passed as a parameter to loadUserByUsername() method
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = repo.findByEmail(email);//the JPARepository interface executes the SQL query and extract the data based on username
        //checking if the user exists
        if(user==null){
            System.out.println("User Not Found");
            throw new UsernameNotFoundException("User not Found");
        }
        //Since UserDetails is an interface we need to create a class UserPrinipal which implememts the UserDetails
        return new UserPrincipal(user);
    }

}
