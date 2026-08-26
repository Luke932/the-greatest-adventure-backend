package it.matrimonio.backend.controller;

import it.matrimonio.backend.dto.GuestRequest;
import it.matrimonio.backend.dto.GuestResponse;
import it.matrimonio.backend.service.GuestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
public class GuestController {

    private final GuestService guestService;

    @GetMapping
    public ResponseEntity<List<GuestResponse>> findAll() {
        return ResponseEntity.ok(guestService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestResponse> findById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(guestService.findById(id));
    }

    @PostMapping
    public ResponseEntity<GuestResponse> create(
            @Valid @RequestBody GuestRequest request
    ) {
        GuestResponse response = guestService.save(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuestResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody GuestRequest request
    ) {
        return ResponseEntity.ok(
                guestService.update(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        guestService.delete(id);

        return ResponseEntity.noContent().build();
    }
}