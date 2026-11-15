package com.example.logintests.api;

import com.example.logintests.data.BookingRequest;
import io.restassured.response.Response;

public final class BookingService {

    private final RestClient client = new RestClient();

    public Response create(BookingRequest booking) {
        return client.post(ApiConstants.BOOKING_ENDPOINT, booking);
    }

    public Response get(int bookingId) {
        return client.get(bookingPath(bookingId));
    }

    public Response update(int bookingId, BookingRequest booking, String token) {
        return client.put(bookingPath(bookingId), booking, token);
    }

    public Response delete(int bookingId, String token) {
        return client.delete(bookingPath(bookingId), token);
    }

    private String bookingPath(int bookingId) {
        return ApiConstants.BOOKING_ENDPOINT + ApiConstants.PATH_SEPARATOR + bookingId;
    }
}
