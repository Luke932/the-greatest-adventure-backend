package it.matrimonio.backend.service;

import it.matrimonio.backend.dto.*;
import it.matrimonio.backend.exception.InvalidInviteTokenException;
import it.matrimonio.backend.mapper.CompanionMapper;
import it.matrimonio.backend.model.Companion;
import it.matrimonio.backend.model.Guest;
import it.matrimonio.backend.repository.CompanionRepository;
import it.matrimonio.backend.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublicInviteService {

    private final GuestRepository guestRepository;
    private final CompanionMapper companionMapper;
    private final CompanionRepository companionRepository;

    public PublicInviteResponse findByToken(String token) {

        Guest guest = guestRepository.findByAccessTokenWithCompanions(token)
                .orElseThrow(InvalidInviteTokenException::new);

        return toPublicResponse(guest);
    }

    public PublicInviteResponse updateRsvp(
            String token,
            RsvpRequest request
    ) {

        Guest guest = guestRepository.findByAccessToken(token)
                .orElseThrow(InvalidInviteTokenException::new);

        guest.setRsvpStatus(request.getRsvpStatus());

        return toPublicResponse(
                guestRepository.save(guest)
        );
    }

    private PublicInviteResponse toPublicResponse(Guest guest) {

        return PublicInviteResponse.builder()
                .name(guest.getName())
                .surname(guest.getSurname())
                .rsvpStatus(guest.getRsvpStatus())
                .companions(
                        guest.getCompanions()
                                .stream()
                                .map(companionMapper::toResponse)
                                .toList()
                )
                .build();
    }

    public CompanionResponse addCompanion(
            String token,
            CompanionRequest request
    ) {

        Guest guest = guestRepository.findByAccessToken(token)
                .orElseThrow(InvalidInviteTokenException::new);

        Companion companion = Companion.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .allergies(request.getAllergies())
                .menuType(request.getMenuType())
                .notes(request.getNotes())
                .guest(guest)
                .build();

        return companionMapper.toResponse(
                companionRepository.save(companion)
        );
    }
}