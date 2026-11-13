package it.matrimonio.backend.controller;

import it.matrimonio.backend.service.GuestExcelExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/guests")
@RequiredArgsConstructor
@Slf4j
public class GuestExportController {

    private final GuestExcelExportService guestExcelExportService;

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportGuests() {
        log.info("Start Export File");
        byte[] excel = guestExcelExportService.exportGuestsExcel();

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"invitati_matrimonio.xlsx\""
                )
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                ))
                .body(excel);
    }
}