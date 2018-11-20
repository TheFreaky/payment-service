package ru.kpfu.itis.model;

/**
 * 20.11.18
 *
 * @author Kuznetsov Maxim
 */
public enum PaymentInterval {
    FAST(15),
    NORMAL(24*60),
    SLOW(24*60*30);

    private Integer interval;

    PaymentInterval(Integer interval) {
        this.interval = interval;
    }

    public static PaymentInterval valueOf(Integer interval) {
        for (PaymentInterval value : PaymentInterval.values()) {
            if (value.getInterval().equals(interval)) {
                return value;
            }
        }

        throw new IllegalArgumentException();
    }

    public Integer getInterval() {
        return interval;
    }
}
