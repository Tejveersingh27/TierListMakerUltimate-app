//package app.TierListMakerUltimate.presentation;
//
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.lameault.sample_project.R;
//import TierListMakerUltimate.application.TodoApp;
//import TierListMakerUltimate.business.validation.ValidationException;
//import TierListMakerUltimate.business.services.ItemService;
//import com.lameault.sample_project.models.Item;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MainActivity extends AppCompatActivity {
//
//    private ItemService itemService;
//
//    private EditText titleInput;
//    private EditText descInput;
//
//    private TodoAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        itemService = ((TodoApp) getApplication()).getItemService();
//
//        titleInput = findViewById(R.id.titleInput);
//        descInput = findViewById(R.id.descInput);
//
//        Button addButton = findViewById(R.id.addButton);
//
//        RecyclerView list = findViewById(R.id.todoList);
//        adapter = new TodoAdapter(new ArrayList<>());
//        list.setLayoutManager(new LinearLayoutManager(this));
//        list.setAdapter(adapter);
//        addButton.setOnClickListener(v -> {
//            try {
//                itemService.addItem(
//                        titleInput.getText().toString(),
//                        descInput.getText().toString()
//                );
//
//                clearInputs();
//                refreshList();
//            } catch (ValidationException ex) {
//                // simplest: show error on the title field (or a Toast)
//                titleInput.setError(ex.getMessage());
//                titleInput.requestFocus();
//            }
//        });
//        refreshList();
//    }
//
//    private void refreshList() {
//        List<Item> items = itemService.getAllItems();
//        adapter.setItems(items);
//    }
//
//    private void clearInputs() {
//        titleInput.setText("");
//        descInput.setText("");
//    }
//
//}
