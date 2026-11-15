package com.example.logintests.data;

public record BookingRequest(String firstname, String lastname, int totalprice, boolean depositpaid,
                             BookingDates bookingdates, String additionalneeds) {

    public record BookingDates(String checkin, String checkout) {}
}
