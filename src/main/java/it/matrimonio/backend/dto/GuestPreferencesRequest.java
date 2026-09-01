package it.matrimonio.backend.dto;

import it.matrimonio.backend.model.MenuType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestPreferencesRequest {

    private String phone;

    private String allergies;

    private MenuType menuType;

    private String notes;
}