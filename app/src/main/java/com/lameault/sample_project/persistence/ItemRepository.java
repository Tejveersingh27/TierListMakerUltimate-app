package com.lameault.sample_project.persistence;

import com.lameault.sample_project.models.Item;
import java.util.List;

public interface ItemRepository {
    List<Item> getAll();

    Item getById(int id);

    Item add(Item item);

    boolean update(Item item);

    boolean delete(int id);
}
