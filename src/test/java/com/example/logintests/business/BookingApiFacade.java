package com.example.logintests.business;

import com.example.logintests.api.ApiConstants;

import com.example.logintests.api.BookingService;
import com.example.logintests.data.BookingDataFactory;
import com.example.logintests.data.BookingRequest;
import com.example.logintests.data.CreatedBookingResponse;
import com.example.logintests.data.InvalidAuthCase;
import com.example.logintests.support.ObjectValidator;
import com.example.logintests.support.ResponseValidator;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_NOT_FOUND;
import static java.net.HttpURLConnection.HTTP_OK;

public final class BookingApiFacade {

    private final AuthApiFacade authApiFacade = new AuthApiFacade();
    private final BookingService bookingService = new BookingService();
    private final ObjectValidator objectValidator = new ObjectValidator();
    private Integer bookingId;
    private BookingRequest booking;
    private String token;

    public BookingApiFacade verifyValidAuthentication() {
        authApiFacade.authenticate();
        return this;
    }

    public BookingApiFacade verifyInvalidAuthentication(InvalidAuthCase authCase) {
        authApiFacade.verifyInvalidAuthentication(authCase);
        return this;
    }

    public BookingApiFacade createBooking() {
        token = authApiFacade.authenticate();
        booking = BookingDataFactory.booking();
        CreatedBookingResponse response = new ResponseValidator(bookingService.create(booking))
            .validateStatusCode(HTTP_OK).validateModelAndGet(CreatedBookingResponse.class);
        bookingId = response.bookingid();
        objectValidator.verifyPositiveId(bookingId);
        objectValidator.verifyObjectsEquality(response.booking(), booking);
        return this;
    }

    public BookingApiFacade verifyBooking() {
        BookingRequest response = new ResponseValidator(bookingService.get(bookingId))
            .validateStatusCode(HTTP_OK).validateModelAndGet(BookingRequest.class);
        objectValidator.verifyObjectsEquality(response, booking);
        return this;
    }

    public BookingApiFacade updateBooking() {
        booking = BookingDataFactory.updatedBooking();
        BookingRequest response = new ResponseValidator(bookingService.update(bookingId, booking, token))
            .validateStatusCode(HTTP_OK).validateModelAndGet(BookingRequest.class);
        objectValidator.verifyObjectsEquality(response, booking);
        return this;
    }

    public BookingApiFacade verifyUpdateRejected() {
        new ResponseValidator(bookingService.update(bookingId, BookingDataFactory.updatedBooking(), ApiConstants.INVALID_TOKEN))
            .validateStatusCode(HTTP_FORBIDDEN);
        return this;
    }

    public BookingApiFacade deleteBooking() {
        new ResponseValidator(bookingService.delete(bookingId, token)).validateStatusCode(HTTP_CREATED);
        return this;
    }

    public BookingApiFacade verifyBookingDeleted() {
        new ResponseValidator(bookingService.get(bookingId)).validateStatusCode(HTTP_NOT_FOUND);
        bookingId = null;
        return this;
    }

    public void cleanUp() {
        if (bookingId != null) {
            int status = bookingService.get(bookingId).statusCode();
            if (status != HTTP_NOT_FOUND) {
                new ResponseValidator(bookingService.delete(bookingId, token)).validateStatusCode(HTTP_CREATED);
            }
            bookingId = null;
        }
    }
}
