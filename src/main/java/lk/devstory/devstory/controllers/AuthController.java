package lk.devstory.devstory.controllers;

import lk.devstory.devstory.model.AuthRequest;
import lk.devstory.devstory.model.AuthResponse;
import lk.devstory.devstory.model.User;
import lk.devstory.devstory.repository.UserRepository;
import lk.devstory.devstory.security.DevStoryUserDetailsService;
import lk.devstory.devstory.services.EmailServices;
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

    @Autowired
    private EmailServices emailServices;

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

        // Save User Details
        User savedUser = userRepository.save(newUser);

        // Send Email to Activate Account
        String activateLink = "http://localhost:8081/auth/"+savedUser.getId()+"/active";
        String emailBody = "Activate Your Account, Click - "+activateLink;
        String emailSubject = "Welcome to DevStory, Activate Account";
        emailServices.sendEmail(user.getEmail(), emailBody, emailSubject);

        return ResponseEntity.ok(savedUser);
    }

    /**
     * Account Activation
     *
     * @param id
     * */
    @PutMapping("/{id}/active")
    // TODO : Authorize for Admin Users only
    public ResponseEntity<User> activateAccount(@PathVariable Long id) {
        if (userRepository.findById(id) == null) throw new BadCredentialsException("User not found!");

        User user = userRepository.findById(id).get();
        user.setActive(true);
        user.setAccountExpired(true);
        user.setCredentialExpired(true);
        user.setLocked(true);

        User result = userRepository.save(user);
        return ResponseEntity.ok(result);
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

