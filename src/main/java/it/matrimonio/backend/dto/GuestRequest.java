package it.matrimonio.backend.dto;

import it.matrimonio.backend.model.MenuType;
import it.matrimonio.backend.model.RsvpStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Surname is required")
    private String surname;

    @Email(message = "Email must be valid")
    private String email;

    private String phone;

    private String allergies;

    @NotNull(message = "Menu type is required")
    private MenuType menuType;

    @NotNull(message = "RSVP status is required")
    private RsvpStatus rsvpStatus;

    private String notes;
}