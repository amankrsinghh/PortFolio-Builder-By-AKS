package com.example.pfbuilder;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class update extends AppCompatActivity {

    EditText input_name, input_email, input_phone, input_job_title, input_job_location, input_about, input_skills;
    ImageView profilepic;
    Button update_btn;
    pfHelper helper;
    Uri imageUri;
    int userId;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        input_name = findViewById(R.id.input_name);
        input_email = findViewById(R.id.input_email);
        input_phone = findViewById(R.id.input_phone);
        input_job_title = findViewById(R.id.input_job_title);
        input_job_location = findViewById(R.id.input_job_location);
        input_about = findViewById(R.id.input_about);
        input_skills = findViewById(R.id.input_skills);
        profilepic = findViewById(R.id.profilepic);
        update_btn = findViewById(R.id.update_button);

        helper = new pfHelper(this);

        Intent intent = getIntent();
        userId = intent.getIntExtra("id", -1);
        input_name.setText(intent.getStringExtra("name"));
        input_job_title.setText(intent.getStringExtra("job"));
        input_job_location.setText(intent.getStringExtra("location"));
        input_about.setText(intent.getStringExtra("about"));
        input_skills.setText(intent.getStringExtra("skills"));
        input_email.setText(intent.getStringExtra("email"));
        input_phone.setText(intent.getStringExtra("phone"));
        image = intent.getStringExtra("image");

        if (image != null && !image.isEmpty()) {
            imageUri = Uri.parse(image);
            profilepic.setImageURI(imageUri);
        }

        profilepic.setOnClickListener(v -> {
            Intent pickImage = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImage.setType("image/*");
            startActivityForResult(pickImage, 101);
        });

        update_btn.setOnClickListener(v -> {
            String name = input_name.getText().toString().trim();
            String jobTitle = input_job_title.getText().toString().trim();
            String jobLocation = input_job_location.getText().toString().trim();
            String about = input_about.getText().toString().trim();
            String skills = input_skills.getText().toString().trim();
            String email = input_email.getText().toString().trim();
            String phone = input_phone.getText().toString().trim();
            String updatedImage = (imageUri != null) ? imageUri.toString() : "";

            boolean updated = helper.updateUser(userId, name, jobTitle, jobLocation, about, skills, email, phone, updatedImage);
            if (updated) {
                Toast.makeText(this, "User updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            profilepic.setImageURI(imageUri);
            try {
                final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
                getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

    }
}
