package com.cowboy76.guesthouse.exception;

public interface ErrorMessageFactory<T extends Exception> {
	
	Class<?> getExceptionClass();
	
	ErrorMessage getErrorMessage(T ex);
	
	int getResponseCode();
}
