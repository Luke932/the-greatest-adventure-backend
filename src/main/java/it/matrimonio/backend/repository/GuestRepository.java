package it.matrimonio.backend.repository;

import it.matrimonio.backend.model.Guest;
import it.matrimonio.backend.model.MenuType;
import it.matrimonio.backend.model.RsvpStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    @Query("""
        SELECT DISTINCT g
        FROM Guest g
        LEFT JOIN FETCH g.companions
        WHERE g.id = :id
        """)
    Optional<Guest> findByIdWithCompanions(@Param("id") Long id);

    @Query("""
        SELECT DISTINCT g
        FROM Guest g
        LEFT JOIN FETCH g.companions
        """)
    List<Guest> findAllWithCompanions();

    long countByRsvpStatus(RsvpStatus rsvpStatus);

    @Query("""
        SELECT DISTINCT g
        FROM Guest g
        LEFT JOIN FETCH g.companions
        WHERE g.rsvpStatus = :rsvpStatus
        """)
    List<Guest> findByRsvpStatus(@Param("rsvpStatus") RsvpStatus rsvpStatus);
    Optional<Guest> findByAccessToken(String accessToken);
    @Query("""
    SELECT DISTINCT g
    FROM Guest g
    LEFT JOIN FETCH g.companions
    WHERE g.accessToken = :accessToken
    """)
    Optional<Guest> findByAccessTokenWithCompanions(
            @Param("accessToken") String accessToken
    );
    long countByRsvpStatusAndMenuType(
            RsvpStatus rsvpStatus,
            MenuType menuType
    );
}