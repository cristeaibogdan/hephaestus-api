package org.personal.shared.exception;

record ValidationError(String detail, String jsonPointer) {}
