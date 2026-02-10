package com.lameault.sample_project.application;

import android.app.Application;

import com.lameault.sample_project.business.services.ItemService;
import com.lameault.sample_project.business.services.ItemServiceImpl;
import com.lameault.sample_project.persistence.ItemRepository;
import com.lameault.sample_project.persistence.real.AppDbHelper;
import com.lameault.sample_project.persistence.real.SqlItemRepository;

public class TodoApp extends Application {

    private ItemService itemService;

    @Override
    public void onCreate() {
        super.onCreate();

        ItemRepository repo = new SqlItemRepository(new AppDbHelper(getApplicationContext()));
        itemService = new ItemServiceImpl(repo);
    }

    public ItemService getItemService() {
        return itemService;
    }
}
