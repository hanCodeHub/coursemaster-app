package com.cs673team1.CourseMaster.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {

    @Test
    void validate() {
        String email1 = "han@email.com";
        String email2 = "han";
        String email3 = "@email.com";
        String email4 = "han@email.c";

        assertTrue(EmailValidator.validate(email1));
        assertFalse(EmailValidator.validate(email2));
        assertFalse(EmailValidator.validate(email3));
        assertFalse(EmailValidator.validate(email4));
        assertFalse(EmailValidator.validate(null));
    }
}