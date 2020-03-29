package com.kqp.awaken.data.message;

/**
 * Functional interface to template messages.
 * See {@link MessageTemplater}.
 */
@FunctionalInterface
public interface MessageTemplate {
    String template();
}