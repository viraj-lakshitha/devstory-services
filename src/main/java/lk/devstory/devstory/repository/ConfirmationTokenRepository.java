package lk.devstory.devstory.repository;

import lk.devstory.devstory.model.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, String> {
    List<ConfirmationToken> findAllByEmail(String email);
}
