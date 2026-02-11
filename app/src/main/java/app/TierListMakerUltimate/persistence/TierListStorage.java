package app.TierListMakerUltimate.persistence;

import android.content.Context;

import app.TierListMakerUltimate.models.TierList;

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

public class TierListStorage implements TierListPersistence {
    private final Map<Integer, TierList> storage = new HashMap<>();

    public TierListStorage(Context context) {
        try {
            // Try open existing JSON file
            FileInputStream in = context.openFileInput("TierLists.json");
            byte[] data = new byte[in.available()];
            in.read(data);
            in.close();

            try {
                // Get data from the file
                String jsonString = new String(data);
                JSONArray tierLists = new JSONArray(jsonString);

                // Save the data in memory
                for (int i = 0; i < tierLists.length(); i++) {
                    JSONObject tierList = tierLists.getJSONObject(i);
                    int id = tierList.getInt("id");
                    String name = tierList.getString("name");

                    storage.put(id, new TierList(id, name));
                }
            } catch (JSONException JS) {
                System.out.println(">>> Error occurred while reading data from JSON file TierLists.json");
                throw new RuntimeException(JS);
            }
        } catch (FileNotFoundException E) {
            System.out.println(">>> Creating a new JSON file TierLists.json");

            try {
                JSONObject tierList_1 = new JSONObject();
                tierList_1.put("id", 1);
                tierList_1.put("name", "Blank name");

                JSONArray jsonArr = new JSONArray();
                jsonArr.put(tierList_1);

                try {
                    FileOutputStream out = context.openFileOutput("TierLists.json", Context.MODE_PRIVATE);
                    out.write(jsonArr.toString().getBytes());
                    out.close();
                } catch (IOException IO) {
                    System.out.println(">>> Error occurred while creating a new JSON file TierLists.json");
                    throw new RuntimeException(IO);
                }
            } catch (JSONException JE) {
                System.out.println(">>> Error occurred while initializing data for new JSON file TierLists.json");
                throw new RuntimeException(JE);
            }
        } catch (IOException E) {
            System.out.println(">>> Error occurred while reading JSON file TierLists.json");
            throw new RuntimeException(E);
        }
    }

    public List<TierList> getTierLists() {
        return new ArrayList<>(storage.values());
    }

    public TierList getTierListById(int tierListId) {
        return storage.get(tierListId);
    }

    public int insertTierList(TierList currentTierList) {
        storage.put(currentTierList.getId(), currentTierList);

        return currentTierList.getId();
    }

    public void updateTierList(TierList currentTierList) {
        storage.remove(currentTierList.getId());
        storage.put(currentTierList.getId(), currentTierList);
    }

    public void deleteTierList(int tierListId) {
        storage.remove(tierListId);
    }
}