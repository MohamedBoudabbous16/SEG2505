package com.example.pcorderapplication.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcorderapplication.controller.StoreKeeperController;
import com.example.pcorderapplication.databinding.ActivityStorekeeperBinding;
import com.example.pcorderapplication.model.entity.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class StoreKeeperActivity extends AppCompatActivity {

    private ActivityStorekeeperBinding binding;
    private StoreKeeperController storeKeeperController;
    private ArrayList<String> componentList; // List to hold components
    private ArrayAdapter<String> adapter; // Adapter for the ListView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStorekeeperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        storeKeeperController = StoreKeeperController.getInstance(getApplicationContext());

        // Initialize the component list and adapter
        componentList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, componentList);
        ListView listView = binding.listViewComponents; // Bind ListView
        listView.setAdapter(adapter);

        dataUpload();
        Toast.makeText(this, "Welcome, StoreKeeper! You can manage the component stock here.", Toast.LENGTH_SHORT).show();

         // Set adapter for the ListView

        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve values from the EditText fields
                String type = binding.editTextType.getText().toString().trim();
                String subtype = binding.editTextSubtype.getText().toString().trim();
                String title = binding.editTextTitle.getText().toString().trim();
                int quantity = Integer.parseInt(binding.editTextQuantity.getText().toString().trim());
                String comment = binding.editTextComment.getText().toString().trim();

                // Validate required fields
                if (type.isEmpty()) {
                    Toast.makeText(StoreKeeperActivity.this, "Type is required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (subtype.isEmpty()) {
                    Toast.makeText(StoreKeeperActivity.this, "Subtype is required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (title.isEmpty()) {
                    Toast.makeText(StoreKeeperActivity.this, "Title is required!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (quantity <= 0) {
                    Toast.makeText(StoreKeeperActivity.this, "Quantity must be greater than zero!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String creationDate = getCurrentDateTime();

                if (storeKeeperController.addComponent(title, type, subtype, quantity, comment, 0, creationDate)) {

                    Toast.makeText(StoreKeeperActivity.this, "Item added to DataBase", Toast.LENGTH_LONG).show();

                    // Add the new item to the list and update the ListView
                    String componentDetails = String.format("%s (%s, %s) - %d", title, type, subtype, quantity);
                    componentList.add(componentDetails);
                    adapter.notifyDataSetChanged(); // Notify the adapter to refresh the ListView
                } else {
                    Log.e("StoreKeeperController", "Error in adding item to DataBase");
                }

                // Reset the EditTexts
                resetFields();
            }
        });
        binding.buttonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve values from the EditText fields
                String title = binding.editTextTitle.getText().toString().trim();

                if (title.isEmpty()) {
                    Toast.makeText(StoreKeeperActivity.this, "Title is required to look for the component", Toast.LENGTH_SHORT).show();
                    return;
                }
                Component component = null;
                if((component = storeKeeperController.findComponentByTitle(title))!= null)
                {
                    binding.editTextType.setText(component.getType()); binding.editTextType.setEnabled(false);
                    binding.editTextSubtype.setText(component.getSubtype());binding.editTextSubtype.setEnabled(false);
                    binding.editTextComment.setText(component.getComment());binding.editTextComment.setEnabled(false);


                int quantity = Integer.parseInt(binding.editTextQuantity.getText().toString().trim());
                if (quantity <= 0) {
                    Toast.makeText(StoreKeeperActivity.this, "Quantity must be greater than zero!", Toast.LENGTH_SHORT).show();
                    return;
                }

                    String modificationDate = getCurrentDateTime();
                    String type = binding.editTextType.getText().toString().trim();
                    String subtype = binding.editTextSubtype.getText().toString().trim();
                    String comment = binding.editTextComment.getText().toString().trim();

                    int oldQuantity = component.getQuantity();

                if (storeKeeperController.updateComponent(title, type, subtype, quantity, comment, 0, modificationDate)) {
                    Toast.makeText(StoreKeeperActivity.this, "Item updated in DataBase", Toast.LENGTH_LONG).show();

                    // Add the new item to the list and update the ListView

                    int i = componentList.indexOf(String.format("%s (%s, %s) - %d", title, type, subtype, oldQuantity));
                    String componentDetails = String.format("%s (%s, %s) - %d", title, type, subtype, quantity);
                    componentList.set( i, componentDetails);
                    adapter.notifyDataSetChanged(); // Notify the adapter to refresh the ListView
                } else {
                    Log.e("StoreKeeperController", "Error in updating the item in DataBase");
                }

                }
                // Reset the EditTexts
                resetFields();
                binding.editTextType.setEnabled(true);
                binding.editTextSubtype.setEnabled(true);
                binding.editTextComment.setEnabled(true);
            }
        });

        binding.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve values from the EditText fields
                String title = binding.editTextTitle.getText().toString().trim();

                if (title.isEmpty()) {
                    Toast.makeText(StoreKeeperActivity.this, "Title is required to look for the component", Toast.LENGTH_SHORT).show();
                    return;
                }
                Component component = null;
                if((component = storeKeeperController.findComponentByTitle(title))!= null)
                {
                    String componentDetails = String.format("%s (%s, %s) - %d", component.getTitle(), component.getType(), component.getSubtype(), component.getQuantity());

                    if (storeKeeperController.deleteComponent(title)) {
                        Toast.makeText(StoreKeeperActivity.this, "Item deleted from DataBase", Toast.LENGTH_LONG).show();

                        // remove the  item from the list and update the ListView

                        componentList.remove(componentDetails);
                        adapter.notifyDataSetChanged(); // Notify the adapter to refresh the ListView
                    } else {
                        Log.e("StoreKeeperController", "Error in deleting ther item from DataBase");
                    }

                }
                // Reset the EditTexts
                resetFields();
                binding.editTextType.setEnabled(true);
                binding.editTextSubtype.setEnabled(true);
                binding.editTextComment.setEnabled(true);
            }
        });
    }

    @SuppressLint("SuspiciousIndentation")
    private void dataUpload() {
        ArrayList<String> list = storeKeeperController.upload();
        if (!list.isEmpty())
        for (String s:list) {
            componentList.add(s);
            adapter.notifyDataSetChanged();
        }
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }


    private void resetFields() {
        binding.editTextType.setText("");
        binding.editTextQuantity.setText("");
        binding.editTextTitle.setText("");
        binding.editTextSubtype.setText("");
        binding.editTextComment.setText("");
    }
}
