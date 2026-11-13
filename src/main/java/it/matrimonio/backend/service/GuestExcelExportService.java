package it.matrimonio.backend.service;

import it.matrimonio.backend.model.Companion;
import it.matrimonio.backend.model.Guest;
import it.matrimonio.backend.model.MenuType;
import it.matrimonio.backend.model.RsvpStatus;
import it.matrimonio.backend.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GuestExcelExportService {

    private final GuestRepository guestRepository;

    private static final String[] HEADERS = {
            "ID",
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
    };

    @Transactional(readOnly = true)
    public byte[] exportGuestsExcel() {
        log.info("Start Service");
        List<Guest> guests = guestRepository.findAllWithCompanions();

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            Styles styles = createStyles(workbook);

            List<Object[]> allPeople = buildPeopleRows(guests);

            createSummarySheet(workbook, styles);
            workbook.setForceFormulaRecalculation(true);
            createPeopleSheet(workbook, styles, "Tutti gli invitati", allPeople);

            List<Object[]> confirmedPeople = allPeople.stream()
                    .filter(row -> RsvpStatus.CONFIRMED.name().equals(row[8]))
                    .toList();

            createPeopleSheet(workbook, styles, "Confermati", confirmedPeople);

            List<Object[]> foodPeople = allPeople.stream()
                    .filter(row -> hasFoodInformation(row[6], row[7]))
                    .toList();

            createPeopleSheet(workbook, styles, "Menu e allergie", foodPeople);

            workbook.setActiveSheet(0);
            workbook.write(output);

            return output.toByteArray();

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Errore durante la generazione del file Excel degli invitati", e
            );
        }
    }


    private List<Object[]> buildPeopleRows(List<Guest> guests) {
        List<Object[]> rows = new ArrayList<>();

        for (Guest guest : guests) {

            // Invitato principale
            rows.add(new Object[]{
                    guest.getId(),
                    "Invitato principale",
                    guest.getName(),
                    guest.getSurname(),
                    guest.getEmail(),
                    guest.getPhone(),
                    guest.getAllergies(),
                    enumValue(guest.getMenuType()),
                    enumValue(guest.getRsvpStatus()),
                    guest.getNotes(),
                    "",
                    "",
                    ""
            });

            // Accompagnatori
            if (guest.getCompanions() != null) {
                for (Companion companion : guest.getCompanions()) {
                    rows.add(new Object[]{
                            companion.getId(),
                            "Accompagnatore",
                            companion.getName(),
                            companion.getSurname(),
                            companion.getEmail(),
                            companion.getPhone(),
                            companion.getAllergies(),
                            enumValue(companion.getMenuType()),
                            enumValue(guest.getRsvpStatus()),
                            companion.getNotes(),
                            guest.getId(),
                            guest.getName(),
                            guest.getSurname()
                    });
                }
            }
        }

        return rows;
    }

    private void createSummarySheet(Workbook workbook, Styles styles) {
        Sheet sheet = workbook.createSheet("Riepilogo");

        sheet.setColumnWidth(0, 8500);
        sheet.setColumnWidth(1, 5500);
        sheet.setColumnWidth(2, 5500);

        Row title = sheet.createRow(0);
        title.setHeightInPoints(35);

        Cell titleCell = title.createCell(0);
        titleCell.setCellValue("ERIKA & LUCA");
        titleCell.setCellStyle(styles.title);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 2));

        Row subtitle = sheet.createRow(1);
        subtitle.setHeightInPoints(25);

        Cell subtitleCell = subtitle.createCell(0);
        subtitleCell.setCellValue("GESTIONE INVITATI · 18 GIUGNO 2027");
        subtitleCell.setCellStyle(styles.subtitle);

        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 2));

        Row header = sheet.createRow(3);
        header.createCell(0).setCellValue("INDICATORE");
        header.createCell(1).setCellValue("VALORE");

        for (int i = 0; i < 2; i++) {
            header.getCell(i).setCellStyle(styles.header);
        }


        String[][] metrics = {
                {"Totale persone", "COUNTA('Tutti gli invitati'!A2:A10000)"},
                {"Invitati principali", "COUNTIF('Tutti gli invitati'!B2:B10000,\"Invitato principale\")"},
                {"Accompagnatori", "COUNTIF('Tutti gli invitati'!B2:B10000,\"Accompagnatore\")"},
                {"Confermati", "COUNTIF('Tutti gli invitati'!I2:I10000,\"CONFIRMED\")"},
                {"In attesa", "COUNTIF('Tutti gli invitati'!I2:I10000,\"PENDING\")"},
                {"Declinati", "COUNTIF('Tutti gli invitati'!I2:I10000,\"DECLINED\")"},
                {"Menu standard confermati", "COUNTIFS('Tutti gli invitati'!H2:H10000,\"STANDARD\",'Tutti gli invitati'!I2:I10000,\"CONFIRMED\")"},
                {"Menu celiaco confermati", "COUNTIFS('Tutti gli invitati'!H2:H10000,\"CELIAC\",'Tutti gli invitati'!I2:I10000,\"CONFIRMED\")"},
                {"Menu vegetariano confermati", "COUNTIFS('Tutti gli invitati'!H2:H10000,\"VEGETARIAN\",'Tutti gli invitati'!I2:I10000,\"CONFIRMED\")"},
                {"Menu vegano confermati", "COUNTIFS('Tutti gli invitati'!H2:H10000,\"VEGAN\",'Tutti gli invitati'!I2:I10000,\"CONFIRMED\")"},
                {"Altri menu confermati", "COUNTIFS('Tutti gli invitati'!H2:H10000,\"OTHER\",'Tutti gli invitati'!I2:I10000,\"CONFIRMED\")"}
        };


        int rowIndex = 4;

        for (String[] metric : metrics) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(metric[0]);
            row.createCell(1).setCellFormula(metric[1]);

            row.getCell(0).setCellStyle(styles.body);
            row.getCell(1).setCellStyle(styles.metric);
        }

        sheet.createFreezePane(0, 4);
    }

    private void createPeopleSheet(
            Workbook workbook,
            Styles styles,
            String sheetName,
            List<Object[]> people
    ) {
        Sheet sheet = workbook.createSheet(sheetName);

        Row header = sheet.createRow(0);
        header.setHeightInPoints(32);

        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(styles.header);
        }

        int rowIndex = 1;

        for (Object[] person : people) {
            Row row = sheet.createRow(rowIndex);
            row.setHeightInPoints(30);

            for (int column = 0; column < person.length; column++) {
                Cell cell = row.createCell(column);
                Object value = person[column];

                if (value instanceof Number number) {
                    cell.setCellValue(number.doubleValue());
                } else {
                    cell.setCellValue(value != null ? value.toString() : "");
                }

                cell.setCellStyle(rowIndex % 2 == 0
                        ? styles.bodyAlternate
                        : styles.body);

                if (column == 8) {
                    applyRsvpStyle(cell, styles);
                }

                if (column == 6 && value != null
                        && !value.toString().isBlank()) {
                    cell.setCellStyle(styles.allergy);
                }
            }

            rowIndex++;
        }

        sheet.setAutoFilter(new CellRangeAddress(
                0,
                Math.max(0, rowIndex - 1),
                0,
                HEADERS.length - 1
        ));

        sheet.createFreezePane(2, 1);

        int[] widths = {
                3500, 6500, 5000, 5500, 8500, 5500, 8500,
                5500, 5000, 10000, 5000, 6500, 6500
        };

        for (int i = 0; i < widths.length; i++) {
            sheet.setColumnWidth(i, widths[i]);
        }
    }

    private void applyRsvpStyle(Cell cell, Styles styles) {
        String status = cell.getStringCellValue();

        switch (status) {
            case "CONFIRMED" -> cell.setCellStyle(styles.confirmed);
            case "PENDING" -> cell.setCellStyle(styles.pending);
            case "DECLINED" -> cell.setCellStyle(styles.declined);
            default -> cell.setCellStyle(styles.body);
        }
    }

    private boolean hasFoodInformation(Object allergies, Object menu) {
        return (allergies != null && !allergies.toString().isBlank())
                || (menu != null && !menu.toString().isBlank());
    }

    private String enumValue(Enum<?> value) {
        return value != null ? value.name() : "";
    }

    private Styles createStyles(Workbook workbook) {
        Styles styles = new Styles();

        Font whiteBold = workbook.createFont();
        whiteBold.setBold(true);
        whiteBold.setColor(IndexedColors.WHITE.getIndex());

        Font titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 20);
        titleFont.setColor(IndexedColors.WHITE.getIndex());

        Font subtitleFont = workbook.createFont();
        subtitleFont.setBold(true);
        subtitleFont.setFontHeightInPoints((short) 11);
        subtitleFont.setColor(IndexedColors.WHITE.getIndex());

        styles.title = workbook.createCellStyle();
        styles.title.setFont(titleFont);
        styles.title.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
        styles.title.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.title.setVerticalAlignment(VerticalAlignment.CENTER);

        styles.subtitle = workbook.createCellStyle();
        styles.subtitle.setFont(subtitleFont);
        styles.subtitle.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
        styles.subtitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.subtitle.setVerticalAlignment(VerticalAlignment.CENTER);

        styles.header = workbook.createCellStyle();
        styles.header.setFont(whiteBold);
        styles.header.setFillForegroundColor(IndexedColors.DARK_GREEN.getIndex());
        styles.header.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.header.setAlignment(HorizontalAlignment.CENTER);
        styles.header.setVerticalAlignment(VerticalAlignment.CENTER);
        styles.header.setWrapText(true);
        setBorders(styles.header);

        styles.body = baseBodyStyle(workbook, IndexedColors.WHITE.getIndex());
        styles.bodyAlternate = baseBodyStyle(
                workbook, IndexedColors.LIGHT_GREEN.getIndex()
        );

        styles.metric = baseBodyStyle(workbook, IndexedColors.LIGHT_TURQUOISE.getIndex());
        styles.metric.setFont(createBoldFont(workbook));

        styles.confirmed = baseBodyStyle(workbook, IndexedColors.LIGHT_GREEN.getIndex());
        styles.pending = baseBodyStyle(workbook, IndexedColors.LIGHT_YELLOW.getIndex());
        styles.declined = baseBodyStyle(workbook, IndexedColors.ROSE.getIndex());
        styles.allergy = baseBodyStyle(workbook, IndexedColors.LIGHT_ORANGE.getIndex());

        return styles;
    }

    private CellStyle baseBodyStyle(Workbook workbook, short background) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(background);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        setBorders(style);
        return style;
    }

    private Font createBoldFont(Workbook workbook) {
        Font font = workbook.createFont();
        font.setBold(true);
        return font;
    }

    private void setBorders(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    }

    private static class Styles {
        private CellStyle title;
        private CellStyle subtitle;
        private CellStyle header;
        private CellStyle body;
        private CellStyle bodyAlternate;
        private CellStyle metric;
        private CellStyle confirmed;
        private CellStyle pending;
        private CellStyle declined;
        private CellStyle allergy;
    }
}