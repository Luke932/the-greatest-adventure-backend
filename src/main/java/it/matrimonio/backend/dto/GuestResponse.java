package it.matrimonio.backend.dto;

import it.matrimonio.backend.model.MenuType;
import it.matrimonio.backend.model.RsvpStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestResponse {

    private Long id;

    private String name;

    private String surname;

    private String email;

    private String phone;

    private String allergies;

    private MenuType menuType;

    private RsvpStatus rsvpStatus;

    private String notes;

    private List<CompanionResponse> companions;
}