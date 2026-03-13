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
import java.util.List;

import app.TierListMakerUltimate.business.exception.InitializationException;
import app.TierListMakerUltimate.business.exception.NotFoundException;
import app.TierListMakerUltimate.business.exception.ImageException;
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
            public void deleteImage(String fileName) {
                // Do nothing
            }
        };

        manager = new TierListManager(persistence, imagePersistence, new TierListValidator());
    }

    @Test
    void testConstructorNullDependenciesThrowsException() {
        assertThrows(InitializationException.class, () -> new TierListManager(null, imagePersistence, new TierListValidator()));
        assertThrows(InitializationException.class, () -> new TierListManager(persistence, null, new TierListValidator()));
        assertThrows(InitializationException.class, () -> new TierListManager(persistence, imagePersistence, null));
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
    void testCreateTierListWithStreamSuccess() {
        TierList list = manager.createTierList("Stream List", false, null, "png");
        assertNotNull(list);
        assertEquals("test", list.getThumbnailPath());
    }

    @Test
    void testCreateTierListInvalidNameThrowsException() {
        assertThrows(ValidationException.class, () -> {
            manager.createTierList("", "Thumbnail", false);
        });
    }
    @Test
    void testGetTierListSuccess() {
        TierList created = manager.createTierList("List ", "thumbnailPath", false);
        TierList fetched = manager.getTierList(created.getId());
        assertEquals(created.getName(), fetched.getName());
    }

    @Test
    void testGetTierListNotFoundThrowsException() {
        assertThrows(NotFoundException.class, () -> {
            manager.getTierList(9999);
        });
    }

    @Test
    void testGetAllTierLists() {
        manager.createTierList("List1", "T1", false);
        manager.createTierList("List2", "T2", false);
        List<TierList> list = manager.getAllTierLists();
        assertTrue(list.size() >= 2);
    }

    @Test
    void testGetAllTemplates() {
        manager.createTierList("Temp", "T", true);
        List<TierList> templates = manager.getAllTemplates();
        assertNotNull(templates);
        assertTrue(!templates.isEmpty());
        assertTrue(templates.get(0).isTemplate());
    }

    @Test
    void testUpdateTierListSuccess() {
        TierList list = manager.createTierList("Old Name", "thumbnailPath", false);
        TierList updated = new TierList(list.getId(), "New Name", "thumbnailPath", false);
        manager.updateTierList(updated);
        assertEquals("New Name", manager.getTierList(list.getId()).getName());
    }

    @Test
    void testUpdateTierListNotFoundThrowsException() {
        TierList tierList = new TierList(888, "Name", "T", false);
        assertThrows(NotFoundException.class, () -> manager.updateTierList(tierList));
    }

    @Test
    void testUpdateTierListWithStreamSuccess() {
        TierList list = manager.createTierList("Old Name", "old_thumb", false);
        TierList updateInfo = new TierList(list.getId(), "New Name", null, false);
        manager.updateTierList(updateInfo, null, "jpg");
        assertEquals("test", manager.getTierList(list.getId()).getThumbnailPath());
        assertEquals("New Name", manager.getTierList(list.getId()).getName());
    }

    @Test
    void testRemoveTierList() {
        int id = manager.createTierList("Test List", "Thumbnail", false).getId();
        assertNotNull(persistence.getTierList(id));

        manager.removeTierList(id);
        assertThrows(NotFoundException.class, () -> manager.getTierList(id));
    }

    @Test
    void testRemoveNotExistingTierListThrowsException() {
        assertThrows(NotFoundException.class, () -> {
            manager.removeTierList(7777);
        });
    }

    @Test
    void testCreateTierListImageFailureThrowsException() {
        ImageFilePersistence failing = new ImageFilePersistence() {
            @Override
            public String saveImage(InputStream is, String ext) throws IOException {
                throw new IOException("Simulated disk error");
            }
            @Override
            public void deleteImage(String name) {}
        };
        TierListManager failingManager = new TierListManager(persistence, failing, new TierListValidator());
        // Updated to expect ImageException instead of PersistenceException
        assertThrows(ImageException.class, () -> failingManager.createTierList("Name", false, null, "jpg"));
    }

    @Test
    void testCopyTierListSuccess() {
        TierList list = manager.createTierList("Original", "thumb", false);
        TierList copied = manager.copy(list.getId(), true);

        assertNotNull(copied);
        assertEquals("Original", copied.getName());
        assertTrue(copied.isTemplate());
        assertTrue(copied.getId() != list.getId());
    }
}
