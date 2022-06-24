package kz.cake.web.model;

import java.time.LocalDateTime;

public class CacheItemDto<T> {
    private final T data;
    private final LocalDateTime expiresAt;

    public CacheItemDto(T data, LocalDateTime expiresAt) {
        this.data = data;
        this.expiresAt = expiresAt;
    }

    public T getData() {
        return data;
    }

    public boolean isExpires() {
        return LocalDateTime.now().isEqual(expiresAt);
    }
}
