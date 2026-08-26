package it.matrimonio.backend.service;

import it.matrimonio.backend.dto.GuestRequest;
import it.matrimonio.backend.dto.GuestResponse;
import it.matrimonio.backend.dto.RsvpRequest;
import it.matrimonio.backend.exception.GuestNotFoundException;
import it.matrimonio.backend.mapper.CompanionMapper;
import it.matrimonio.backend.model.Guest;
import it.matrimonio.backend.model.RsvpStatus;
import it.matrimonio.backend.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GuestService {

    private final GuestRepository guestRepository;
    private final CompanionMapper companionMapper;

    public List<GuestResponse> findAll() {
        return guestRepository.findAllWithCompanions()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public GuestResponse findById(Long id) {
        Guest guest = guestRepository.findByIdWithCompanions(id)
                .orElseThrow(() -> new GuestNotFoundException(id));

        return toResponse(guest);
    }
    public List<GuestResponse> findByRsvpStatus(RsvpStatus rsvpStatus) {
        return guestRepository.findByRsvpStatus(rsvpStatus)
                .stream()
                .map(this::toResponse)
                .toList();
    }
    public GuestResponse updateRsvp(Long id, RsvpRequest request) {

        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new GuestNotFoundException(id));

        guest.setRsvpStatus(request.getRsvpStatus());

        return toResponse(guestRepository.save(guest));
    }

    public GuestResponse save(GuestRequest request) {

        Guest guest = Guest.builder()
                .name(request.getName())
                .surname(request.getSurname())
                .email(request.getEmail())
                .phone(request.getPhone())
                .allergies(request.getAllergies())
                .menuType(request.getMenuType())
                .rsvpStatus(request.getRsvpStatus())
                .notes(request.getNotes())
                .accessToken(UUID.randomUUID().toString())
                .build();

        return toResponse(guestRepository.save(guest));
    }

    public GuestResponse update(Long id, GuestRequest request) {

        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new GuestNotFoundException(id));

        guest.setName(request.getName());
        guest.setSurname(request.getSurname());
        guest.setEmail(request.getEmail());
        guest.setPhone(request.getPhone());
        guest.setAllergies(request.getAllergies());
        guest.setMenuType(request.getMenuType());
        guest.setRsvpStatus(request.getRsvpStatus());
        guest.setNotes(request.getNotes());

        return toResponse(guestRepository.save(guest));
    }

    public void delete(Long id) {

        Guest guest = guestRepository.findById(id)
                .orElseThrow(() -> new GuestNotFoundException(id));

        guestRepository.delete(guest);
    }

    private GuestResponse toResponse(Guest guest) {

        return GuestResponse.builder()
                .id(guest.getId())
                .name(guest.getName())
                .surname(guest.getSurname())
                .email(guest.getEmail())
                .phone(guest.getPhone())
                .allergies(guest.getAllergies())
                .menuType(guest.getMenuType())
                .rsvpStatus(guest.getRsvpStatus())
                .notes(guest.getNotes())
                .companions(
                        guest.getCompanions()
                                .stream()
                                .map(companionMapper::toResponse)
                                .toList()
                )
                .build();
    }
}