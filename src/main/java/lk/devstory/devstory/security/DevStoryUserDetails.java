package lk.devstory.devstory.security;

import lk.devstory.devstory.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class DevStoryUserDetails implements UserDetails {

    private String username;
    private String password;
    private boolean active;
    private boolean locked;
    private boolean credentialsExpired;
    private boolean accountExpired;
    private List<GrantedAuthority> authorityList;

    public DevStoryUserDetails() {}

    public DevStoryUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.accountExpired = user.isAccountExpired();
        this.active = user.isActive();
        this.locked = user.isLocked();
        this.credentialsExpired = user.isCredentialExpired();
        this.authorityList = Arrays.stream(user.getRole().toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }
}
