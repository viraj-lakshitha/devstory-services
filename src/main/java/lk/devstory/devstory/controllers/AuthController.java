package lk.devstory.devstory.controllers;

import lk.devstory.devstory.model.AuthRequest;
import lk.devstory.devstory.model.AuthResponse;
import lk.devstory.devstory.model.User;
import lk.devstory.devstory.repository.UserRepository;
import lk.devstory.devstory.security.DevStoryUserDetailsService;
import lk.devstory.devstory.utils.JwtUtils;
import lk.devstory.devstory.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DevStoryUserDetailsService devStoryUserDetailsService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private AuthRequest authRequest;


    /**
     * Create new user
     *
     * @param user
     * @return savedUser
     * */
    @PostMapping("/signup")
    public ResponseEntity<User> createNewUser(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) throw new BadCredentialsException("Email already exist!");

        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setFullName(user.getFullName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(newUser);
        return ResponseEntity.ok(savedUser);
    }

    /**
     * Upgrade user role to ADMIN
     *
     * @param id
     * @return user
     * */
    @PutMapping("/{id}/upgrade")
    public ResponseEntity<User> upgradeUserRoleToAdmin(@PathVariable Long id) {
        if (userRepository.findById(id).get().getRole().equals(Role.ROLE_ADMIN)) throw new BadCredentialsException("User already ADMIN");

        User user = userRepository.findById(id).get();
        user.setRole(Role.ROLE_ADMIN);

        User updateUser = userRepository.save(user);
        return ResponseEntity.ok(updateUser);
    }

    /**
     * Create AuthToken
     *
     * @param authRequest
     * @return new Jwt Token
     * */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> userLogin(@RequestBody AuthRequest authRequest) throws Exception {
        this.authRequest = authRequest;
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect Username or Password");
        }
        final UserDetails userDetails = devStoryUserDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtils.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}

