package it.matrimonio.backend.dto;

import it.matrimonio.backend.model.RsvpStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicInviteResponse {

    private String name;

    private String surname;

    private RsvpStatus rsvpStatus;

    private List<CompanionResponse> companions;
}