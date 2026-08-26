package it.matrimonio.backend.mapper;

import it.matrimonio.backend.dto.CompanionResponse;
import it.matrimonio.backend.model.Companion;
import org.springframework.stereotype.Component;

@Component
public class CompanionMapper {

    public CompanionResponse toResponse(Companion companion) {
        if (companion == null) {
            return null;
        }

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