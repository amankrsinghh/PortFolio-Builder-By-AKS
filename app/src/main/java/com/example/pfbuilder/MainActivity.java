package com.example.pfbuilder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    EditText input_name, input_email, input_phone, input_job_title, input_job_location, input_about, input_skills;
    Button submit_button;
    ImageView profilepic;
    Uri imageUri;
    pfHelper helper;
    String image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        submit_button = findViewById(R.id.submit_button);
        input_name = findViewById(R.id.input_name);
        input_email = findViewById(R.id.input_email);
        input_phone = findViewById(R.id.input_phone);
        input_job_title = findViewById(R.id.input_job_title);
        input_job_location = findViewById(R.id.input_job_location);
        input_about = findViewById(R.id.input_about);
        input_skills = findViewById(R.id.input_skills);
        profilepic = findViewById(R.id.profilepic);

        helper = new pfHelper(this);

        profilepic.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (checkSelfPermission(Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
                }
            } else {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    pickImage();
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
                }
            }
        });

        submit_button.setOnClickListener(v -> {
            String name = input_name.getText().toString().trim();
            String email = input_email.getText().toString().trim();
            String phone = input_phone.getText().toString().trim();
            String job_title = input_job_title.getText().toString().trim();
            String job_location = input_job_location.getText().toString().trim();
            String about = input_about.getText().toString().trim();
            String skills = input_skills.getText().toString().trim();

            if (imageUri == null) {
                Toast.makeText(this, "Please select an image!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || job_title.isEmpty() || job_location.isEmpty() || about.isEmpty() || skills.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
                 return;
            }

            image = imageUri.toString();
            helper.insertUser(name, job_title, job_location, about, skills, email, phone, image);

            Toast.makeText(this, "Portfolio Created Successfully!", Toast.LENGTH_SHORT).show();

            // Clear input fields
            input_name.setText("");
            input_email.setText("");
            input_phone.setText("");
            input_job_title.setText("");
            input_job_location.setText("");
            input_about.setText("");
            input_skills.setText("");
            profilepic.setImageResource(R.drawable.baseline_account_circle_24);

            startActivity(new Intent(MainActivity.this, portfolio.class));
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom
            );
            return insets;
        });
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            profilepic.setImageURI(imageUri);

            try {
                final int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
            } catch (SecurityException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((requestCode == 100 || requestCode == 101) &&
                grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        } else {
            Toast.makeText(this, "Permission denied. Cannot access images.", Toast.LENGTH_SHORT).show();
        }
    }

}
