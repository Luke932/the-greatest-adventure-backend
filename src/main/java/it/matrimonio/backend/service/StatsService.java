package it.matrimonio.backend.service;

import it.matrimonio.backend.dto.StatsResponse;
import it.matrimonio.backend.model.MenuType;
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


        // ========================================
        // MENU - SOLO PERSONE CONFERMATE
        // ========================================

        long standardPeople =
                guestRepository.countByRsvpStatusAndMenuType(
                        RsvpStatus.CONFIRMED,
                        MenuType.STANDARD
                )
                        +
                        companionRepository.countByGuest_RsvpStatusAndMenuType(
                                RsvpStatus.CONFIRMED,
                                MenuType.STANDARD
                        );


        long celiacPeople =
                guestRepository.countByRsvpStatusAndMenuType(
                        RsvpStatus.CONFIRMED,
                        MenuType.CELIAC
                )
                        +
                        companionRepository.countByGuest_RsvpStatusAndMenuType(
                                RsvpStatus.CONFIRMED,
                                MenuType.CELIAC
                        );


        long vegetarianPeople =
                guestRepository.countByRsvpStatusAndMenuType(
                        RsvpStatus.CONFIRMED,
                        MenuType.VEGETARIAN
                )
                        +
                        companionRepository.countByGuest_RsvpStatusAndMenuType(
                                RsvpStatus.CONFIRMED,
                                MenuType.VEGETARIAN
                        );


        long veganPeople =
                guestRepository.countByRsvpStatusAndMenuType(
                        RsvpStatus.CONFIRMED,
                        MenuType.VEGAN
                )
                        +
                        companionRepository.countByGuest_RsvpStatusAndMenuType(
                                RsvpStatus.CONFIRMED,
                                MenuType.VEGAN
                        );


        long otherMenuPeople =
                guestRepository.countByRsvpStatusAndMenuType(
                        RsvpStatus.CONFIRMED,
                        MenuType.OTHER
                )
                        +
                        companionRepository.countByGuest_RsvpStatusAndMenuType(
                                RsvpStatus.CONFIRMED,
                                MenuType.OTHER
                        );


        return StatsResponse.builder()

                .totalGuests(
                        guestRepository.count()
                )

                .confirmedGuests(
                        confirmedGuests
                )

                .pendingGuests(
                        pendingGuests
                )

                .declinedGuests(
                        declinedGuests
                )

                .totalCompanions(
                        companionRepository.count()
                )

                .confirmedPeople(
                        confirmedGuests + confirmedCompanions
                )

                .pendingPeople(
                        pendingGuests + pendingCompanions
                )

                .declinedPeople(
                        declinedGuests + declinedCompanions
                )

                .standardPeople(
                        standardPeople
                )

                .celiacPeople(
                        celiacPeople
                )

                .vegetarianPeople(
                        vegetarianPeople
                )

                .veganPeople(
                        veganPeople
                )

                .otherMenuPeople(
                        otherMenuPeople
                )

                .build();
    }
}