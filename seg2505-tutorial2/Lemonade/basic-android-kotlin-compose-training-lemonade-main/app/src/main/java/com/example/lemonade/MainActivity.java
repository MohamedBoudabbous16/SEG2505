package com.example.lemonade;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int currentStep = 1;
    private int squeezeCount = 0;
    private Random random;

    private ImageView imageView = findViewById(R.id.imageView);
    private TextView textView;
    private Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        random = new Random();

        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        updateUI();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onImageClick();
            }
        });
    }

    private void onImageClick() {
        switch (currentStep) {
            case 1:
                currentStep = 2;
                squeezeCount = random.nextInt(3) + 2; // Random between 2 and 4
                break;
            case 2:
                squeezeCount--;
                if (squeezeCount == 0) {
                    currentStep = 3;
                }
                break;
            case 3:
                currentStep = 4;
                break;
            case 4:
                currentStep = 1;
                break;
        }
        updateUI();
    }

    private void updateUI() {
        switch (currentStep) {
            case 1:
                textView.setText(R.string.lemon_select);
                imageView.setImageResource(R.drawable.lemon_tree);
                break;
            case 2:
                textView.setText(R.string.lemon_squeeze);
                imageView.setImageResource(R.drawable.lemon_squeeze);
                break;
            case 3:
                textView.setText(R.string.lemon_drink);
                imageView.setImageResource(R.drawable.lemon_drink);
                break;
            case 4:
                textView.setText(R.string.lemon_empty_glass);
                imageView.setImageResource(R.drawable.lemon_restart);
                break;
        }
    }
}
