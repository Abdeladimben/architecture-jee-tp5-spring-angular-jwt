package ma.bdcc.bankaccount.security;

import ma.bdcc.bankaccount.security.entities.AppUser;
import ma.bdcc.bankaccount.security.repositories.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private AppUserRepository appUserRepository;

    public UserDetailsServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username);
        if (appUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return User.withUsername(appUser.getUsername())
            .password(appUser.getPassword())
            .roles(appUser.getRoles().stream()
                .map(r -> r.getRoleName().replace("ROLE_", ""))
                .toArray(String[]::new))
            .disabled(!appUser.isActive())
            .build();
    }
}
