package it.matrimonio.backend.service;

import it.matrimonio.backend.dto.CompanionRequest;
import it.matrimonio.backend.dto.CompanionResponse;
import it.matrimonio.backend.dto.GuestResponse;
import it.matrimonio.backend.exception.CompanionNotFoundException;
import it.matrimonio.backend.exception.GuestNotFoundException;
import it.matrimonio.backend.model.Companion;
import it.matrimonio.backend.model.Guest;
import it.matrimonio.backend.repository.CompanionRepository;
import it.matrimonio.backend.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanionService {

    private final CompanionRepository companionRepository;
    private final GuestRepository guestRepository;

    public List<CompanionResponse> findAll() {
        return companionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<CompanionResponse> findByGuestId(Long guestId) {

        if (!guestRepository.existsById(guestId)) {
            throw new GuestNotFoundException(guestId);
        }

        return companionRepository.findByGuestId(guestId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public CompanionResponse findById(Long id) {

        Companion companion = companionRepository.findById(id)
                .orElseThrow(() -> new CompanionNotFoundException(id));

        return toResponse(companion);
    }

    public CompanionResponse save(Long guestId, CompanionRequest request) {

        Guest guest = guestRepository.findById(guestId)
                .orElseThrow(() -> new GuestNotFoundException(guestId));

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

        return toResponse(companionRepository.save(companion));
    }

    public CompanionResponse update(Long id, CompanionRequest request) {

        Companion companion = companionRepository.findById(id)
                .orElseThrow(() -> new CompanionNotFoundException(id));

        companion.setName(request.getName());
        companion.setSurname(request.getSurname());
        companion.setEmail(request.getEmail());
        companion.setPhone(request.getPhone());
        companion.setAllergies(request.getAllergies());
        companion.setMenuType(request.getMenuType());
        companion.setNotes(request.getNotes());

        return toResponse(companionRepository.save(companion));
    }

    public void delete(Long id) {

        Companion companion = companionRepository.findById(id)
                .orElseThrow(() -> new CompanionNotFoundException(id));

        companionRepository.delete(companion);
    }

    private CompanionResponse toResponse(Companion companion) {

        return CompanionResponse.builder()
                .id(companion.getId())
                .name(companion.getName())
                .surname(companion.getSurname())
                .email(companion.getEmail())
                .phone(companion.getPhone())
                .allergies(companion.getAllergies())
                .menuType(companion.getMenuType())
                .notes(companion.getNotes())
                .build();
    }
}