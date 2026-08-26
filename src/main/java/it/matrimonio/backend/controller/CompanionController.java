package it.matrimonio.backend.controller;

import it.matrimonio.backend.dto.CompanionRequest;
import it.matrimonio.backend.dto.CompanionResponse;
import it.matrimonio.backend.service.CompanionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CompanionController {

    private final CompanionService companionService;

    @GetMapping("/guests/{guestId}/companions")
    public List<CompanionResponse> findByGuestId(
            @PathVariable Long guestId) {

        return companionService.findByGuestId(guestId);
    }

    @GetMapping("/companions/{id}")
    public CompanionResponse findById(
            @PathVariable Long id) {

        return companionService.findById(id);
    }

    @PostMapping("/guests/{guestId}/companions")
    @ResponseStatus(HttpStatus.CREATED)
    public CompanionResponse save(
            @PathVariable Long guestId,
            @Valid @RequestBody CompanionRequest request) {

        return companionService.save(guestId, request);
    }

    @PutMapping("/companions/{id}")
    public CompanionResponse update(
            @PathVariable Long id,
            @Valid @RequestBody CompanionRequest request) {

        return companionService.update(id, request);
    }

    @DeleteMapping("/companions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long id) {

        companionService.delete(id);
    }
}