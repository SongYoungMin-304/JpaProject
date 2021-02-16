package com.example.demo.exception;

public class NotEnoughStockException extends RuntimeException{
	
	public NotEnoughStockException() {
		
	}
	
	public NotEnoughStockException(String message) {
		super(message);
	}
	
	public NotEnoughStockException(String message, Throwable caues) {
		super(message, caues);
	}
	
	public NotEnoughStockException(Throwable cause) {
		super(cause);
	}
}
