package it.matrimonio.backend.mapper;

import it.matrimonio.backend.dto.GuestResponse;
import it.matrimonio.backend.model.Guest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GuestMapper {

    private final CompanionMapper companionMapper;

    public GuestResponse toResponse(Guest guest) {
        if (guest == null) {
            return null;
        }

        return GuestResponse.builder()
                .id(guest.getId())
                .name(guest.getName())
                .surname(guest.getSurname())
                .email(guest.getEmail())
                .phone(guest.getPhone())
                .allergies(guest.getAllergies())
                .menuType(guest.getMenuType())
                .notes(guest.getNotes())
                .rsvpStatus(guest.getRsvpStatus())
                .companions(
                        guest.getCompanions()
                                .stream()
                                .map(companionMapper::toResponse)
                                .collect(Collectors.toList())
                )
                .build();
    }
}