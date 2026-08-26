package it.matrimonio.backend.service;

import it.matrimonio.backend.dto.StatsResponse;
import it.matrimonio.backend.model.RsvpStatus;
import it.matrimonio.backend.repository.CompanionRepository;
import it.matrimonio.backend.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final GuestRepository guestRepository;
    private final CompanionRepository companionRepository;

    public StatsResponse getStats() {

        long confirmedGuests =
                guestRepository.countByRsvpStatus(RsvpStatus.CONFIRMED);

        long pendingGuests =
                guestRepository.countByRsvpStatus(RsvpStatus.PENDING);

        long declinedGuests =
                guestRepository.countByRsvpStatus(RsvpStatus.DECLINED);

        long confirmedCompanions =
                companionRepository.countByGuest_RsvpStatus(
                        RsvpStatus.CONFIRMED
                );

        long pendingCompanions =
                companionRepository.countByGuest_RsvpStatus(
                        RsvpStatus.PENDING
                );

        long declinedCompanions =
                companionRepository.countByGuest_RsvpStatus(
                        RsvpStatus.DECLINED
                );

        return StatsResponse.builder()
                .totalGuests(guestRepository.count())
                .confirmedGuests(confirmedGuests)
                .pendingGuests(pendingGuests)
                .declinedGuests(declinedGuests)
                .totalCompanions(companionRepository.count())
                .confirmedPeople(confirmedGuests + confirmedCompanions)
                .pendingPeople(pendingGuests + pendingCompanions)
                .declinedPeople(declinedGuests + declinedCompanions)
                .build();
    }
}