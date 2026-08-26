package it.matrimonio.backend.repository;

import it.matrimonio.backend.model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GuestRepository extends JpaRepository<Guest, Long> {
    @Query("""
        SELECT DISTINCT g
        FROM Guest g
        LEFT JOIN FETCH g.companions
        WHERE g.id = :id
        """)
    Optional<Guest> findByIdWithCompanions(@Param("id") Long id);
}