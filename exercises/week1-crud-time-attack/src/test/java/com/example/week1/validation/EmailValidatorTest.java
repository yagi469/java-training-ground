package com.example.week1.validation;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {
    private final EmailValidator validator = new EmailValidator();

    @Test
    void validEmails() {
        assertTrue(validator.isValid("test@example.com", null));
        assertTrue(validator.isValid("user.name+tag@sub.domain.co", null));
    }

    @Test
    void invalidEmails() {
        assertFalse(validator.isValid("plainaddress", null));
        assertFalse(validator.isValid("@missinguser.com", null));
        assertFalse(validator.isValid("user@.com", null));
    }
}

