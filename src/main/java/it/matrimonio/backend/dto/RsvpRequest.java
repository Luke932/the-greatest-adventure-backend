package it.matrimonio.backend.dto;

import it.matrimonio.backend.model.RsvpStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RsvpRequest {

    @NotNull(message = "Lo stato RSVP è obbligatorio")
    private RsvpStatus rsvpStatus;
}