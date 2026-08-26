package it.matrimonio.backend.dto;

import it.matrimonio.backend.model.MenuType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanionResponse {

    private Long id;

    private String name;

    private String surname;

    private String email;

    private String phone;

    private String allergies;

    private MenuType menuType;

    private String notes;
}