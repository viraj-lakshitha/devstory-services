package lk.devstory.devstory.controllers.v1;

import lk.devstory.devstory.exceptions.BadRequestException;
import lk.devstory.devstory.model.User;
import lk.devstory.devstory.repository.UserRepository;
import lk.devstory.devstory.services.ConfirmationTokenService;
import lk.devstory.devstory.utils.MailUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/api/v1/confirmation")
public class ConfirmationTokenController {

    @Autowired
    private ConfirmationTokenService confirmationTokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MailUtils mailUtils;

    @GetMapping("/activate/{token}")
    public void activateAccount(@PathVariable String token, HttpServletResponse httpServletResponse) throws IOException {
        // Check for Token Validation
        if (confirmationTokenService.validateConfirmationToken(token)) {
            // throw exception as invalid
            throw new BadRequestException("Invalid Token");
        }

        // Find the User
        User user = userRepository.findByEmail(confirmationTokenService.getEmail(token)).get();

        if (user.isAccountExpired() & user.isActive() & user.isCredentialExpired() & user.isLocked()) {
            // throw exception as account already activate and update token as expired
            throw new BadRequestException("Account Already Activated");
        }

        // Activate User Account
        user.setLocked(true);
        user.setCredentialExpired(true);
        user.setActive(true);
        user.setAccountExpired(true);

        // Save Update Details
        User savedUser = userRepository.save(user);

        if (savedUser == null) {
            // throw exception as fail to update account details
            throw new BadRequestException("Fail to activate account with email : "+user.getEmail());
        }

        httpServletResponse.sendRedirect("https://devstory-portal.herokuapp.com/"); // Add to application.properties
    }

    @GetMapping("/renew/{email}")
    public void renewConfirmationToken(@PathVariable String email) {
        String token = confirmationTokenService.renewConfirmationToken(email);
        mailUtils.sendVerificationEmail(email, token);
    }

}
