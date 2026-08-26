package it.matrimonio.backend.dto;

import it.matrimonio.backend.model.MenuType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanionRequest {

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

    private String notes;
}