package it.matrimonio.backend.dto;

import it.matrimonio.backend.model.MenuType;
import it.matrimonio.backend.model.RsvpStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicGuestUpdateRequest {

    @NotNull
    private RsvpStatus rsvpStatus;

    @NotNull
    private MenuType menuType;

    private String phone;

    private String allergies;

    private String notes;
}