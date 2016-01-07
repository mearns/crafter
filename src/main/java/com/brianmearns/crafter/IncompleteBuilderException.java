package com.brianmearns.crafter;

/**
 *
 */
public class IncompleteBuilderException extends IllegalStateException {
    public IncompleteBuilderException() {
    }

    public IncompleteBuilderException(String s) {
        super(s);
    }

    public IncompleteBuilderException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public IncompleteBuilderException(Throwable throwable) {
        super(throwable);
    }
}
