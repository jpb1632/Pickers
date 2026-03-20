package springBootPickers.service.orders;

public class PaymentFinalizeException extends RuntimeException {

    private final boolean requiresNetworkCancel;

    private PaymentFinalizeException(
            String message,
            boolean requiresNetworkCancel,
            Throwable cause
    ) {
        super(message, cause);
        this.requiresNetworkCancel = requiresNetworkCancel;
    }

    public static PaymentFinalizeException compensatable(String message) {
        return new PaymentFinalizeException(message, true, null);
    }

    public static PaymentFinalizeException compensatable(String message, Throwable cause) {
        return new PaymentFinalizeException(message, true, cause);
    }

    public static PaymentFinalizeException nonCompensatable(String message) {
        return new PaymentFinalizeException(message, false, null);
    }

    public boolean requiresNetworkCancel() {
        return requiresNetworkCancel;
    }
}
