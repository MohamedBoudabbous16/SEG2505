package com.example.pcorderapplication;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.core.IsAnything.anything;

import android.content.Context;
import android.widget.ListView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.pcorderapplication.controller.RequesterController;
import com.example.pcorderapplication.controller.StoreKeeperController;
import com.example.pcorderapplication.model.entity.Component;
import com.example.pcorderapplication.view.RequesterActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class RequesterActivityTest {

    private Context context;
    private RequesterController requesterController;
    private StoreKeeperController storeKeeperController;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        requesterController = RequesterController.getInstance(context);
        storeKeeperController = StoreKeeperController.getInstance(context);
        clearTestData();
        insertTestComponents();
    }

    private void clearTestData() {
        storeKeeperController.deleteAllComponents();
    }

    private void insertTestComponents() {
        String currentDate = java.time.LocalDateTime.now().toString();
        storeKeeperController.addComponent("Intel i7", "Hardware", "CPU", 100, "Initial stock", 0, currentDate);
        storeKeeperController.addComponent("AMD Ryzen 5", "Hardware", "CPU", 100, "Initial stock", 0, currentDate);
        storeKeeperController.addComponent("NVIDIA GTX 1080", "Hardware", "GPU", 100, "Initial stock", 0, currentDate);
        storeKeeperController.addComponent("Samsung SSD 1TB", "Hardware", "Storage", 100, "Initial stock", 0, currentDate);
    }

    @Test
    public void testCreateOrder() {
        ActivityScenario.launch(RequesterActivity.class);

        // Entrer le titre du composant
        onView(withId(R.id.editTextTitle))
                .perform(replaceText("Intel i7"), closeSoftKeyboard());

        // Entrer la quantité
        onView(withId(R.id.editTextQuantity))
                .perform(replaceText("10"), closeSoftKeyboard());

        // Cliquer sur le bouton de création de commande
        onView(withId(R.id.createOrderButton))
                .perform(click());

        // Vérifier que l'élément a été ajouté au ListView
        onData(anything())
                .inAdapterView(withId(R.id.ordersListView))
                .atPosition(0)
                .check(matches(withText("1 : Intel i7 (Hardware, CPU) - 10")));
    }

    @Test

    public void testDeleteOrder() {
        ActivityScenario.launch(RequesterActivity.class);

        // Ajouter une commande
        List<Component> components = new ArrayList<>();
        components.add(new Component("Intel i7", "Hardware", "CPU", 10, "", "", ""));
        requesterController.createOrder(components);

        // Entrer l'ID de la commande pour suppression
        onView(withId(R.id.editTextTitle))
                .perform(replaceText("1"), closeSoftKeyboard());

        // Cliquer sur le bouton de suppression
        onView(withId(R.id.deleteOrderButton))
                .perform(click());

        // Vérifier que la liste est vide
        onView(withId(R.id.ordersListView))
                .check((view, noViewFoundException) -> {
                    ListView listView = (ListView) view;
                    assert listView.getCount() == 0; // Vérifie que la liste est vide
                });
    }

}
