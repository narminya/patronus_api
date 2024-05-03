package com.example.patronus.repository;

import com.example.patronus.models.jpa.RefreshToken;
import com.example.patronus.models.jpa.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TokenRepository  extends JpaRepository<RefreshToken, UUID> {

    Optional<RefreshToken> findByUserId(UUID userId);
    Optional<RefreshToken> findByToken(String token);
    @Query(value = "SELECT s.* FROM refreshTokens s inner join JOIN user u ON u.id = s.user_id " +
            "WHERE u.id != :id and (t.expired=false or t.revoked=false)", nativeQuery = true)
    List<RefreshToken> findAllValidTokenByUser(UUID id);
    @Modifying
    int deleteByUser(User user);
    @Modifying
    @Query(value = "UPDATE refreshTokens SET revoked = :revokedValue WHERE user_id = :userId", nativeQuery = true)
    int revokeAllByUser(User user);
}
