package it.matrimonio.backend.controller;

import it.matrimonio.backend.dto.*;
import it.matrimonio.backend.service.PublicInviteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/invites")
@RequiredArgsConstructor
public class PublicInviteController {

    private final PublicInviteService publicInviteService;

    @GetMapping("/{token}")
    public PublicInviteResponse getInvite(
            @PathVariable String token
    ) {
        return publicInviteService.findByToken(token);
    }

    @PatchMapping("/{token}/rsvp")
    public PublicInviteResponse updateRsvp(
            @PathVariable String token,
            @Valid @RequestBody RsvpRequest request
    ) {
        return publicInviteService.updateRsvp(token, request);
    }

    @PostMapping("/{token}/companions")
    public CompanionResponse addCompanion(
            @PathVariable String token,
            @Valid @RequestBody CompanionRequest request
    ) {
        return publicInviteService.addCompanion(token, request);
    }
}