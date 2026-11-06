package by.kovalevskiy.BinanceAvrPrice.exeption;

public class PriceNotFoundException extends RuntimeException {
    public PriceNotFoundException(String message) {
        super(message);
    }
}
