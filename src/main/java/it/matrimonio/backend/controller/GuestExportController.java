package it.matrimonio.backend.controller;

import it.matrimonio.backend.service.GuestExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
public class GuestExportController {

    private final GuestExportService guestExportService;

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportGuests() {

        byte[] csv = guestExportService.exportGuestsCsv();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"invitati_matrimonio.csv\""
                )
                .contentType(
                        MediaType.parseMediaType("text/csv; charset=UTF-8")
                )
                .body(csv);
    }
}