package it.matrimonio.backend.service;

import it.matrimonio.backend.model.Companion;
import it.matrimonio.backend.model.Guest;
import it.matrimonio.backend.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestExportService {

    private final GuestRepository guestRepository;

    @Transactional(readOnly = true)
    public byte[] exportGuestsCsv() {
        List<Guest> guests = guestRepository.findAllWithCompanions();

        StringBuilder csv = new StringBuilder();

        // BOM UTF-8: aiuta Excel a riconoscere correttamente gli accenti
        csv.append('\uFEFF');

        appendRow(csv,
                "ID persona",
                "Tipo persona",
                "Nome",
                "Cognome",
                "Email",
                "Telefono",
                "Allergie",
                "Menu",
                "Stato RSVP",
                "Note",
                "ID invitato principale",
                "Nome invitato principale",
                "Cognome invitato principale"
        );

        for (Guest guest : guests) {

            // Riga dell'invitato principale
            appendRow(csv,
                    value(guest.getId()),
                    "INVITATO PRINCIPALE",
                    guest.getName(),
                    guest.getSurname(),
                    guest.getEmail(),
                    guest.getPhone(),
                    guest.getAllergies(),
                    value(guest.getMenuType()),
                    value(guest.getRsvpStatus()),
                    guest.getNotes(),
                    "",
                    "",
                    ""
            );

            // Una riga per ogni accompagnatore
            if (guest.getCompanions() != null) {
                for (Companion companion : guest.getCompanions()) {
                    appendRow(csv,
                            value(companion.getId()),
                            "ACCOMPAGNATORE",
                            companion.getName(),
                            companion.getSurname(),
                            companion.getEmail(),
                            companion.getPhone(),
                            companion.getAllergies(),
                            value(companion.getMenuType()),
                            value(guest.getRsvpStatus()),
                            companion.getNotes(),
                            value(guest.getId()),
                            guest.getName(),
                            guest.getSurname()
                    );
                }
            }
        }

        return csv.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void appendRow(StringBuilder csv, String... values) {
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                // Separatore ; più adatto alle impostazioni italiane di Excel
                csv.append(';');
            }

            csv.append(escapeCsv(values[i]));
        }

        csv.append("\r\n");
    }

    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }

        // Le virgolette interne vengono raddoppiate secondo lo standard CSV
        String escaped = value.replace("\"", "\"\"");

        if (escaped.contains(";")
                || escaped.contains("\"")
                || escaped.contains("\n")
                || escaped.contains("\r")) {
            return "\"" + escaped + "\"";
        }

        return escaped;
    }

    private String value(Object value) {
        return value != null ? value.toString() : "";
    }
}