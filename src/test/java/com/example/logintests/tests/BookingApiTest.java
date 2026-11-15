package com.example.logintests.tests;

import com.example.logintests.business.BookingApiFacade;
import com.example.logintests.data.InvalidAuthCase;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public final class BookingApiTest {

    private BookingApiFacade bookingApiFacade;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        bookingApiFacade = new BookingApiFacade();
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        bookingApiFacade.cleanUp();
    }

    @Test(description = "Valid credentials return an authentication token")
    public void validCredentialsAreAccepted() {
        bookingApiFacade.verifyValidAuthentication();
    }

    @Test(dataProvider = "invalidCredentials", description = "Invalid credentials do not return a token")
    public void invalidCredentialsAreRejected(InvalidAuthCase authCase) {
        bookingApiFacade.verifyInvalidAuthentication(authCase);
    }

    @Test(description = "Created booking can be retrieved with the same data")
    public void createdBookingCanBeRetrieved() {
        bookingApiFacade.createBooking().verifyBooking();
    }

    @Test(description = "Booking can be updated and deleted")
    public void bookingLifecycle() {
        bookingApiFacade.createBooking()
            .verifyBooking()
            .updateBooking()
            .verifyBooking()
            .deleteBooking()
            .verifyBookingDeleted();
    }

    @Test(description = "Booking update with invalid token is rejected")
    public void invalidTokenCannotUpdateBooking() {
        bookingApiFacade.createBooking().verifyUpdateRejected().verifyBooking();
    }

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        return new Object[][] {
            { InvalidAuthCase.USERNAME },
            { InvalidAuthCase.PASSWORD },
            { InvalidAuthCase.EMPTY }
        };
    }
}
