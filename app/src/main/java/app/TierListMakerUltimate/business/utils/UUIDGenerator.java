package app.TierListMakerUltimate.business.utils;

import java.util.UUID;

public class UUIDGenerator implements IUUIDGenerator {
    public UUID generateUUID() {
        return UUID.randomUUID();
    }
}
