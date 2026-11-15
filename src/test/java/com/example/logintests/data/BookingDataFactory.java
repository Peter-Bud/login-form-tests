package com.example.logintests.data;

import java.time.LocalDate;
import java.util.UUID;

public final class BookingDataFactory {

    private static final String FIRST_NAME_PREFIX = "Test";
    private static final String UPDATED_NAME_PREFIX = "Updated";
    private static final String LAST_NAME = "Automation";
    private static final String ADDITIONAL_NEEDS = "Breakfast";
    private static final int PRICE = 120;
    private static final int UPDATED_PRICE = 150;

    private BookingDataFactory() {}

    public static BookingRequest booking() {
        return createBooking(FIRST_NAME_PREFIX, PRICE);
    }

    public static BookingRequest updatedBooking() {
        return createBooking(UPDATED_NAME_PREFIX, UPDATED_PRICE);
    }

    private static BookingRequest createBooking(String prefix, int price) {
        LocalDate checkin = LocalDate.now().plusDays(30);
        String name = prefix + UUID.randomUUID().toString().substring(0, 8);
        BookingRequest.BookingDates dates = new BookingRequest.BookingDates(
            checkin.toString(), checkin.plusDays(2).toString());
        return new BookingRequest(name, LAST_NAME, price, true, dates, ADDITIONAL_NEEDS);
    }
}
