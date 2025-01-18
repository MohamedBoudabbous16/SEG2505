package com.example.pcorderapplication;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.pcorderapplication.controller.StoreKeeperController;
import com.example.pcorderapplication.view.StoreKeeperActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StoreKeeperActivityTest {

    private Context context;
    private StoreKeeperController storeKeeperController;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        storeKeeperController = StoreKeeperController.getInstance(context);
        clearTestData();
        insertTestComponents();
    }

    private void clearTestData() {
        storeKeeperController.deleteAllComponents();
    }

    private void insertTestComponents() {
        String currentDate = getCurrentDateTime();
        storeKeeperController.addComponent("Intel i7", "Hardware", "CPU", 100, "Initial stock", 0, currentDate);
        storeKeeperController.addComponent("AMD Ryzen 5", "Hardware", "CPU", 100, "Initial stock", 0, currentDate);
        storeKeeperController.addComponent("NVIDIA GTX 1080", "Hardware", "GPU", 100, "Initial stock", 0, currentDate);
        storeKeeperController.addComponent("Samsung SSD 1TB", "Hardware", "Storage", 100, "Initial stock", 0, currentDate);
        storeKeeperController.addComponent("Corsair 16GB RAM", "Hardware", "Memory", 100, "Initial stock", 0, currentDate);
    }

    private String getCurrentDateTime() {
        return java.time.LocalDateTime.now().toString();
    }

    @Test
    public void testComponentsDisplayedWithQuantity100() {
        ActivityScenario.launch(StoreKeeperActivity.class);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        onData(anything())
                .inAdapterView(withId(R.id.listViewComponents))
                .atPosition(0)
                .check(matches(withText("Intel i7 (Hardware, Component) - 100")));
    }
}
