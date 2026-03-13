package app.TierListMakerUltimate.persistence.utils;

import java.util.UUID;

public class UUIDGenerator implements IUUIDGenerator {
    public UUID generateUUID() {
        return UUID.randomUUID();
    }
}
