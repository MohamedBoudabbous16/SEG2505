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
    private ArrayList<String> componentList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStorekeeperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        storeKeeperController = StoreKeeperController.getInstance(getApplicationContext());

        componentList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, componentList);
        ListView listView = binding.listViewComponents;
        listView.setAdapter(adapter);

        // Initialiser les composants avec une quantité de 100
        initializeComponents();

        Toast.makeText(this, "Welcome, StoreKeeper! You can manage the component stock here.", Toast.LENGTH_SHORT).show();
        Log.i("StoreManagement", "Welcome, StoreKeeper! You can manage the component stock here.");

        binding.buttonAdd.setOnClickListener(v -> addComponent());
        binding.buttonModify.setOnClickListener(v -> modifyComponent());
        binding.buttonDelete.setOnClickListener(v -> deleteComponent());
        binding.goBackArrow.setOnClickListener(v -> goBack());
    }

    private void goBack() {
        finish();
    }

    private void initializeComponents() {
        // Liste des composants à initialiser
        String[] titles = {"Intel i7", "AMD Ryzen 5", "NVIDIA GTX 1080", "Samsung SSD 1TB", "Corsair 16GB RAM"};
        String type = "Hardware";
        String subtype = "Component";
        String comment = "Initial stock";
        int quantity = 100;
        String creationDate = getCurrentDateTime();

        for (String title : titles) {
            // Ajouter chaque composant au contrôleur et à la liste d'affichage
            storeKeeperController.addComponent(title, type, subtype, quantity, comment, 0, creationDate);

            String componentDetails = String.format("%s (%s, %s) - %d", title, type, subtype, quantity);
            componentList.add(componentDetails);
        }
        adapter.notifyDataSetChanged();
    }

    private void addComponent() {
        String type = binding.editTextType.getText().toString().trim();
        String subtype = binding.editTextSubtype.getText().toString().trim();
        String title = binding.editTextTitle.getText().toString().trim();
        String quantityStr = binding.editTextQuantity.getText().toString().trim();
        String comment = binding.editTextComment.getText().toString().trim();

        if (type.isEmpty()) {
            Toast.makeText(this, "Type is required!", Toast.LENGTH_SHORT).show();
            Log.w("StoreKeeperActivity", "Type is required!");
            return;
        }
        if (subtype.isEmpty()) {
            Toast.makeText(this, "Subtype is required!", Toast.LENGTH_SHORT).show();
            Log.w("StoreKeeperActivity", "Subtype is required!");
            return;
        }
        if (title.isEmpty()) {
            Toast.makeText(this, "Title is required!", Toast.LENGTH_SHORT).show();
            Log.w("StoreKeeperActivity", "Title is required!");
            return;
        }
        if (quantityStr.isEmpty()) {
            Toast.makeText(this, "Quantity is required!", Toast.LENGTH_SHORT).show();
            Log.w("StoreKeeperActivity", "Quantity is required!");
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                Toast.makeText(this, "Quantity must be greater than zero!", Toast.LENGTH_SHORT).show();
                Log.w("StoreKeeperActivity", "Quantity must be greater than zero!");
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid quantity format!", Toast.LENGTH_SHORT).show();
            Log.e("StoreKeeperActivity", "Invalid quantity format!");
            return;
        }

        String creationDate = getCurrentDateTime();

        if (storeKeeperController.addComponent(title, type, subtype, quantity, comment, 0, creationDate)) {
            Toast.makeText(this, "Item added to DataBase", Toast.LENGTH_LONG).show();
            Log.i("StoreKeeperActivity", "Item added to DataBase");

            String componentDetails = String.format("%s (%s, %s) - %d", title, type, subtype, quantity);
            componentList.add(componentDetails);
            adapter.notifyDataSetChanged();
        } else {
            Log.e("StoreKeeperController", "Error in adding item to DataBase");
        }

        resetFields();
    }

    private void modifyComponent() {
        String title = binding.editTextTitle.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Title is required to look for the component", Toast.LENGTH_SHORT).show();
            Log.w("StoreKeeperActivity", "Title is required to look for the component");
            return;
        }

        Component component = storeKeeperController.findComponentByTitle(title);
        if (component != null) {
            binding.editTextType.setText(component.getType());
            binding.editTextType.setEnabled(false);
            binding.editTextSubtype.setText(component.getSubtype());
            binding.editTextSubtype.setEnabled(false);
            binding.editTextComment.setText(component.getComment());
            binding.editTextComment.setEnabled(false);

            String quantityStr = binding.editTextQuantity.getText().toString().trim();
            if (quantityStr.isEmpty()) {
                Toast.makeText(this, "Quantity is required!", Toast.LENGTH_SHORT).show();
                Log.w("StoreKeeperActivity", "Quantity is required!");
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityStr);
                if (quantity <= 0) {
                    Toast.makeText(this, "Quantity must be greater than zero!", Toast.LENGTH_SHORT).show();
                    Log.w("StoreKeeperActivity", "Quantity must be greater than zero!");
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid quantity format!", Toast.LENGTH_SHORT).show();
                Log.e("StoreKeeperActivity", "Invalid quantity format!");
                return;
            }

            String modificationDate = getCurrentDateTime();
            String type = binding.editTextType.getText().toString().trim();
            String subtype = binding.editTextSubtype.getText().toString().trim();
            String comment = binding.editTextComment.getText().toString().trim();

            int oldQuantity = component.getQuantity();

            if (storeKeeperController.updateComponent(title, type, subtype, quantity, comment, 0, modificationDate)) {
                Toast.makeText(this, "Item updated in DataBase", Toast.LENGTH_LONG).show();
                Log.i("StoreKeeperActivity", "Item updated in DataBase");

                int i = componentList.indexOf(String.format("%s (%s, %s) - %d", title, type, subtype, oldQuantity));
                String componentDetails = String.format("%s (%s, %s) - %d", title, type, subtype, quantity);
                if (i >= 0) {
                    componentList.set(i, componentDetails);
                }
                adapter.notifyDataSetChanged();
            } else {
                Log.e("StoreKeeperController", "Error in updating the item in DataBase");
            }
        } else {
            Toast.makeText(this, "Component not found!", Toast.LENGTH_SHORT).show();
            Log.w("StoreKeeperActivity", "Component not found!");
        }

        resetFields();
        binding.editTextType.setEnabled(true);
        binding.editTextSubtype.setEnabled(true);
        binding.editTextComment.setEnabled(true);
    }

    private void deleteComponent() {
        String title = binding.editTextTitle.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(this, "Title is required to look for the component", Toast.LENGTH_SHORT).show();
            Log.w("StoreKeeperActivity", "Title is required to look for the component");
            return;
        }

        Component component = storeKeeperController.findComponentByTitle(title);
        if (component != null) {
            String componentDetails = String.format("%s (%s, %s) - %d", component.getTitle(), component.getType(), component.getSubtype(), component.getQuantity());

            if (storeKeeperController.deleteComponent(title)) {
                Toast.makeText(this, "Item deleted from DataBase", Toast.LENGTH_LONG).show();
                Log.i("StoreKeeperActivity", "Item deleted from DataBase");

                componentList.remove(componentDetails);
                adapter.notifyDataSetChanged();
            } else {
                Log.e("StoreKeeperController", "Error in deleting the item from DataBase");
            }
        } else {
            Toast.makeText(this, "Component not found!", Toast.LENGTH_SHORT).show();
            Log.w("StoreKeeperActivity", "Component not found!");
        }

        resetFields();
        binding.editTextType.setEnabled(true);
        binding.editTextSubtype.setEnabled(true);
        binding.editTextComment.setEnabled(true);
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
/*package com.example.pcorderapplication.view;

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
    private ArrayList<String> componentList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStorekeeperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        storeKeeperController = StoreKeeperController.getInstance(getApplicationContext());


        componentList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, componentList);
        ListView listView = binding.listViewComponents;
        listView.setAdapter(adapter);

        dataUpload();
        Toast.makeText(this, "Welcome, StoreKeeper! You can manage the component stock here.", Toast.LENGTH_SHORT).show();
        Log.i("StoreManagement", "Welcome, StoreKeeper! You can manage the component stock here.");



        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = binding.editTextType.getText().toString().trim();
                String subtype = binding.editTextSubtype.getText().toString().trim();
                String title = binding.editTextTitle.getText().toString().trim();
                int quantity = Integer.parseInt(binding.editTextQuantity.getText().toString().trim());
                String comment = binding.editTextComment.getText().toString().trim();


                if (type.isEmpty()) {
                    Toast.makeText(StoreKeeperActivity.this, "Type is required!", Toast.LENGTH_SHORT).show();
                    Log.w("StoreKeeperActivity", "Type is required!");
                    return;
                }
                if (subtype.isEmpty()) {
                    Toast.makeText(StoreKeeperActivity.this, "Subtype is required!", Toast.LENGTH_SHORT).show();
                    Log.w("StoreKeeperActivity", "Subtype is required!");
                    return;
                }
                if (title.isEmpty()) {
                    Toast.makeText(StoreKeeperActivity.this, "Title is required!", Toast.LENGTH_SHORT).show();
                    Log.w("StoreKeeperActivity", "Title is required!");
                    return;
                }
                if (quantity <= 0) {
                    Toast.makeText(StoreKeeperActivity.this, "Quantity must be greater than zero!", Toast.LENGTH_SHORT).show();
                    Log.w("StoreKeeperActivity", "Quantity must be greater than zero!");
                    return;
                }


                String creationDate = getCurrentDateTime();

                if (storeKeeperController.addComponent(title, type, subtype, quantity, comment, 0, creationDate)) {

                    Toast.makeText(StoreKeeperActivity.this, "Item added to DataBase", Toast.LENGTH_LONG).show();
                    Log.i("StoreKeeperActivity", "Item added to DataBase");

                    String componentDetails = String.format("%s (%s, %s) - %d", title, type, subtype, quantity);
                    componentList.add(componentDetails);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("StoreKeeperController", "Error in adding item to DataBase");
                }


                resetFields();
            }
        });
        binding.buttonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = binding.editTextTitle.getText().toString().trim();

                if (title.isEmpty()) {
                    Toast.makeText(StoreKeeperActivity.this, "Title is required to look for the component", Toast.LENGTH_SHORT).show();
                    Log.w("StoreKeeperActivity", "Title is required to look for the component");
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
                    Log.w("StoreKeeperActivity", "Quantity must be greater than zero!");
                    return;
                }

                    String modificationDate = getCurrentDateTime();
                    String type = binding.editTextType.getText().toString().trim();
                    String subtype = binding.editTextSubtype.getText().toString().trim();
                    String comment = binding.editTextComment.getText().toString().trim();

                    int oldQuantity = component.getQuantity();

                if (storeKeeperController.updateComponent(title, type, subtype, quantity, comment, 0, modificationDate)) {
                    Toast.makeText(StoreKeeperActivity.this, "Item updated in DataBase", Toast.LENGTH_LONG).show();
                    Log.i("StoreKeeperActivity", "Item updated in DataBase");


                    int i = componentList.indexOf(String.format("%s (%s, %s) - %d", title, type, subtype, oldQuantity));
                    String componentDetails = String.format("%s (%s, %s) - %d", title, type, subtype, quantity);
                    componentList.set( i, componentDetails);
                    adapter.notifyDataSetChanged();
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

                String title = binding.editTextTitle.getText().toString().trim();

                if (title.isEmpty()) {
                    Toast.makeText(StoreKeeperActivity.this, "Title is required to look for the component", Toast.LENGTH_SHORT).show();
                    Log.w("StoreKeeperActivity", "Title is required to look for the component");
                    return;
                }
                Component component = null;
                if((component = storeKeeperController.findComponentByTitle(title))!= null)
                {
                    String componentDetails = String.format("%s (%s, %s) - %d", component.getTitle(), component.getType(), component.getSubtype(), component.getQuantity());

                    if (storeKeeperController.deleteComponent(title)) {
                        Toast.makeText(StoreKeeperActivity.this, "Item deleted from DataBase", Toast.LENGTH_LONG).show();
                        Log.i("StoreKeeperActivity", "Item deleted from DataBase");


                        componentList.remove(componentDetails);
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("StoreKeeperController", "Error in deleting ther item from DataBase");
                    }

                }

                resetFields();
                binding.editTextType.setEnabled(true);
                binding.editTextSubtype.setEnabled(true);
                binding.editTextComment.setEnabled(true);
            }
        });
        binding.goBackArrow.setOnClickListener(v -> goBack());
    }

    private void goBack() {
        finish();
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
*/

