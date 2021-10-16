package lk.devstory.devstory.services;

import lk.devstory.devstory.model.ConfirmationToken;
import lk.devstory.devstory.repository.ConfirmationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ConfirmationTokenService {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    /**
     * Create New Confirmation Token
     *
     * @param email
     * */
    public String createNewConfirmationToken(String email) {
        ConfirmationToken token = new ConfirmationToken();

        token.setToken(UUID.randomUUID().toString());
        token.setEmail(email);
        token.setCreatedAt(LocalDateTime.now());
        token.setExpiredAt(LocalDateTime.now().plusHours(24)); // Expire in 24 Hours
        ConfirmationToken savedToken = confirmationTokenRepository.save(token);

        return savedToken.getToken();
    }


    /**
     * Renew Confirmation Token
     *
     * @param email
     * */
    public String renewConfirmationToken(String email) {
        List<ConfirmationToken> existingTokenList = confirmationTokenRepository.findAllByEmail(email);

        // Delete all existing token for the email
        for (ConfirmationToken token : existingTokenList) {
            confirmationTokenRepository.delete(token);
        }

        // Create New Token and Return Toke
        return this.createNewConfirmationToken(email);
    }


    /**
     * Validate Confirmation Token
     *
     * @param token
     * */
    public Boolean validateConfirmationToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.getById(token);

        // Check for token availability
        if (confirmationToken == null) {} // Throw ResourceNotFoundException

        // Check token Used or Expired
        return LocalDateTime.now().isBefore(confirmationToken.getExpiredAt()) || confirmationToken.getConfirmedAt() != null;
    }

    /**
     * Get Email Address by Token
     *
     * */
    public String getEmail(String token) {
        ConfirmationToken confirmationToken = confirmationTokenRepository.getById(token);
        // Check for token availability
        if (confirmationToken == null) {} // Throw ResourceNotFoundException
        return confirmationToken.getEmail();
    }

}