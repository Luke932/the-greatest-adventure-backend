package it.matrimonio.backend.repository;

import it.matrimonio.backend.model.Companion;
import it.matrimonio.backend.model.RsvpStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanionRepository extends JpaRepository<Companion, Long> {
    List<Companion> findByGuestId(Long guestId);
    long countByGuest_RsvpStatus(RsvpStatus rsvpStatus);
}