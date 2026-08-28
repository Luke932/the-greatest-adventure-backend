package it.matrimonio.backend.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatsResponse {

    private long totalGuests;

    private long confirmedGuests;

    private long pendingGuests;

    private long declinedGuests;

    private long totalCompanions;

    private long confirmedPeople;

    private long pendingPeople;

    private long declinedPeople;

    private long standardPeople;

    private long celiacPeople;

    private long vegetarianPeople;

    private long veganPeople;

    private long otherMenuPeople;
}