package app.TierListMakerUltimate.persistence;

import android.content.Context;

import app.TierListMakerUltimate.models.Tier;

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

public class TierStorage implements TierPersistence {
    private final Map<Integer, Tier> storage = new HashMap<>();

    TierStorage(Context context) {
        try {
            // Try open existing JSON file
            FileInputStream in = context.openFileInput("Tiers.json");
            byte[] data = new byte[in.available()];
            in.read(data);
            in.close();

            try {
                // Get data from the file
                String jsonString = new String(data);
                JSONArray tiers = new JSONArray(jsonString);

                // Save the data in memory
                for (int i = 0; i < tiers.length(); i++) {
                    JSONObject tier = tiers.getJSONObject(i);
                    int id = tier.getInt("id");
                    String name = tier.getString("name");
                    String colorHex = tier.getString("colorHex");

                    storage.put(id, new Tier(id, name, colorHex));
                }
            } catch (JSONException JS) {
                System.out.println(">>> Error occurred while reading data from JSON file Tiers.json");
                throw new RuntimeException(JS);
            }
        } catch (FileNotFoundException E) {
            System.out.println(">>> Creating a new JSON file Tiers.json");

            try {
                JSONObject tier_1 = new JSONObject();
                tier_1.put("id", 1);
                tier_1.put("name", "Blank name");
                tier_1.put("colorHex", "#FF6600");

                JSONArray jsonArr = new JSONArray();
                jsonArr.put(tier_1);

                try {
                    FileOutputStream out = context.openFileOutput("Tiers.json", Context.MODE_PRIVATE);
                    out.write(jsonArr.toString().getBytes());
                    out.close();
                } catch (IOException IO) {
                    System.out.println(">>> Error occurred while creating a new JSON file Tiers.json");
                    throw new RuntimeException(IO);
                }
            } catch (JSONException JE) {
                System.out.println(">>> Error occurred while initializing data for new JSON file Tiers.json");
                throw new RuntimeException(JE);
            }
        } catch (IOException E) {
            System.out.println(">>> Error occurred while reading JSON file Tiers.json");
            throw new RuntimeException(E);
        }
    }

    public List<Tier> getTiersForList(int tierListId) {
//        List<Tier> tiers = new ArrayList<Tier>();
//        for (Tier tier: this.storage.values()) {
//            if (tier.getTierListId() == tierListId) {
//                tiers.add(tier);
//            }
//        }
//
//        return tiers;

        return new ArrayList<Tier>();   // Tier does not have getTierListId() method, and probably should not have
    }

    public Tier getTier(int tierId) {
        return storage.get(tierId);
    }

    public int insertTier(int tierListId, Tier currentTier) {
        storage.put(currentTier.getId(), currentTier);

        return currentTier.getId();
    }

    public void updateTier(Tier currentTier){
        storage.remove(currentTier.getId());
        storage.put(currentTier.getId(), currentTier);
    }

    public void deleteTier(int tierId){
        storage.remove(tierId);
    }
}