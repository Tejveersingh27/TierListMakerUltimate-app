package app.TierListMakerUltimate.business.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import app.TierListMakerUltimate.business.exception.ValidationException;
import app.TierListMakerUltimate.business.validation.TierListValidator;
import app.TierListMakerUltimate.models.TierList;
import app.TierListMakerUltimate.persistence.ImageFilePersistence;
import app.TierListMakerUltimate.persistence.TierListPersistence;
import app.TierListMakerUltimate.persistence.stubs.TierListPersistenceStub;

public class TierListManagerTest {
    private TierListPersistence persistence;
    private ImageFilePersistence imagePersistence;
    private TierListManager manager;

    @BeforeEach
    void setup() {
        persistence = new TierListPersistenceStub();

        // Mock the ImageFilePersistence
        imagePersistence = new ImageFilePersistence() {
            @Override
            public String saveImage(InputStream inputStream, String fileName) throws IOException {
                return "test";
            }

            @Override
            public void deleteImage(String fileName) throws IOException {
                // Do nothing
            }
        };

        manager = new TierListManager(persistence, imagePersistence, new TierListValidator());
    }

    @Test
    void testCreateTierListSuccess() {
        int id = manager.createTierList("Favourite Netflix Series List", "Thumbnail", false).getId();
        assertTrue(id > 0);

        TierList tierListItem = persistence.getTierList(id);
        assertNotNull(tierListItem);
        assertEquals("Favourite Netflix Series List", tierListItem.getName());
    }

    @Test
    void testCreateTierListInvalidNameThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.createTierList("", "Thumbnail", false);
        });
    }

    @Test
    void testRemoveTierList() {
        int id = manager.createTierList("Test List", "Thumbnail", false).getId();
        assertNotNull(persistence.getTierList(id));

        manager.removeTierList(id);
        assertNull(persistence.getTierList(id));
    }

    @Test
    void testsRemoveTierListInvalidIdThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.removeTierList(-1);
        });
    }
}
