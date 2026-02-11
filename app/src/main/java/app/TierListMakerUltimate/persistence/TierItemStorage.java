package app.TierListMakerUltimate.persistence;

import android.content.Context;

import app.TierListMakerUltimate.models.TierItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TierItemStorage implements TierItemPersistence {
    private final Map<Integer, TierItem> storage = new HashMap<>();

    public TierItemStorage(Context context) {
        try {
            // Try open existing JSON file
            FileInputStream in = context.openFileInput("TierItems.json");
            byte[] data = new byte[in.available()];
            in.read(data);
            in.close();

            try {
                // Get data from the file
                String jsonString = new String(data);
                JSONArray items = new JSONArray(jsonString);

                // Save the data in memory
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    int id = item.getInt("id");
                    String imagePath = item.getString("imagePath");
                    String description = item.getString("description");
                    int tierId = item.getInt("tierId");

                    storage.put(id, new TierItem(id, imagePath, description, tierId));
                }
            } catch (JSONException JS) {
                System.out.println(">>> Error occurred while reading data from JSON file TierItems.json");
                throw new RuntimeException(JS);
            }
        } catch (FileNotFoundException E) {
            System.out.println(">>> Creating a new JSON file TierItems.json");

            try {
                JSONObject item_1 = new JSONObject();
                item_1.put("id", 1);
                item_1.put("imagePath", "BlankPath");
                item_1.put("description", "Description text");
                item_1.put("tierId", 1);

                JSONArray jsonArr = new JSONArray();
                jsonArr.put(item_1);

                try {
                    FileOutputStream out = context.openFileOutput("TierItems.json", Context.MODE_PRIVATE);
                    out.write(jsonArr.toString().getBytes());
                    out.close();
                } catch (IOException IO) {
                    System.out.println(">>> Error occurred while creating a new JSON file TierItems.json");
                    throw new RuntimeException(IO);
                }
            } catch (JSONException JE) {
                System.out.println(">>> Error occurred while initializing data for new JSON file TierItems.json");
                throw new RuntimeException(JE);
            }
        } catch (IOException E) {
            System.out.println(">>> Error occurred while reading JSON file TierItems.json");
            throw new RuntimeException(E);
        }
    }

    @Override
    public List<TierItem> getItemsForTier(int tierId) {
        List<TierItem> tierItems = new ArrayList<TierItem>();

        for (TierItem item: this.storage.values()) {
            if (item.getTierId() == tierId) {
                tierItems.add(item);
            }
        }

        return tierItems;
    }

    @Override
    public TierItem getItem(int itemId) {
        for (TierItem item: this.storage.values()) {
            if (item.getId() == itemId) {
                return item;
            }
        }

        return null;
    }

    @Override
    public int insertItem(int tierId, TierItem currentItem) {   // Returns ID
        this.storage.put(currentItem.getId(), currentItem);

        return currentItem.getId();
    }

    @Override
    public void updateItem(TierItem currentItem) {
        this.storage.remove(currentItem.getId());
        this.storage.put(currentItem.getId(), currentItem);
    }

    @Override
    public void deleteItem(int itemId) {
        this.storage.remove(itemId);
    }
}