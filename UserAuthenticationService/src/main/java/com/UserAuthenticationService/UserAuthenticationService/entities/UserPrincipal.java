    package com.UserAuthenticationService.UserAuthenticationService.entities;

    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.util.Collection;
    import java.util.List;

    public class UserPrincipal implements UserDetails {
        //@Autowired
        private User user;
        public UserPrincipal(User user) {
            this.user=user;
        }

        //If your building a full fledged application then we have to use all the methods
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            //Authorities means assigning roles
            return List.of(new SimpleGrantedAuthority(user.getRole().name()));
        }

        @Override
        public String getPassword() {
            return user.getPassword();
        }

        @Override
        public String getUsername() {
            return user.getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            //initailly we assume the the account is not expired
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            //initailly we assume the the account is not locked
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            //initailly we assume the credentials not expired
            return true;
        }

        @Override
        public boolean isEnabled() {
            //initailly we assume the account is enabled
            return true;
        }
    }
