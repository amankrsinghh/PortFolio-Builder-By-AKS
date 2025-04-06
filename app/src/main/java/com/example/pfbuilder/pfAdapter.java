package com.example.pfbuilder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.Manifest;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class pfAdapter extends RecyclerView.Adapter<pfAdapter.viewHolder> {
    public pfAdapter(ArrayList<pfmodel> pflist, Context context) {
        this.pflist = pflist;
        this.context = context;
    }

    ArrayList<pfmodel> pflist;
    Context context;
    pfHelper helper;

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_design,parent,false);
        return new viewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        pfmodel pf = pflist.get(position);
        holder.name.setText(pf.getName());
        holder.email.setText(pf.getEmail());
        holder.phone.setText(pf.getPhoneno());
        holder.job_title.setText(pf.getJobtitle());
        holder.job_location.setText(pf.getLocation());
        holder.about.setText(pf.getAbout());
        holder.skills.setText(pf.getSkills());
        if (pf.getImage() != null && !pf.getImage().isEmpty()) {
            Glide.with(context)
                    .load(Uri.parse(pf.getImage()))
                    .placeholder(R.drawable.baseline_account_circle_24)
                    .error(R.drawable.circle_background)
                    .circleCrop() // 👈 THIS is the key!
                    .into(holder.profilepic);

        } else {
            holder.profilepic.setImageResource(R.drawable.circle_background);
        }
        holder.delete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete User")
                    .setMessage("Are you sure you want to delete?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        deleteUser(holder.getAdapterPosition()); // Delete from DB & UI
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

//        holder.delete.setOnClickListener(v -> {
//           helper = new pfHelper(context);
//            int userId = pflist.get(holder.getAdapterPosition()).getId();
//            helper.deleteUser(userId); // सीधे DB से delete करो
//            pflist.remove(holder.getAdapterPosition()); // List से भी हटाओ
//            notifyItemRemoved(holder.getAdapterPosition()); // RecyclerView को update करो
//        });

        holder.edit.setOnClickListener(v -> {
            Intent intent = new Intent(context, update.class);
            pfmodel user = pflist.get(holder.getAdapterPosition());

            intent.putExtra("id", user.getId());
            intent.putExtra("name", user.getName());
            intent.putExtra("job", user.getJobtitle());
            intent.putExtra("location", user.getLocation());
            intent.putExtra("email", user.getEmail());
            intent.putExtra("phone", user.getPhoneno());
            intent.putExtra("about", user.getAbout());
            intent.putExtra("skills", user.getSkills());
            intent.putExtra("image", user.getImage() != null ? user.getImage() : ""); // Safe

            ((Activity) context).startActivityForResult(intent, 100);

        });


        holder.shareBtn.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    return;
                }
            }

            Bitmap bitmap = getBitmapFromView(holder.itemView);
            try {
                File cachePath = new File(context.getCacheDir(), "images");
                cachePath.mkdirs();
                File file = new File(cachePath, "image.png");
                FileOutputStream stream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                stream.flush();
                stream.close();

                Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);

                if (contentUri != null) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.setDataAndType(contentUri, context.getContentResolver().getType(contentUri));
                    shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                    shareIntent.setType("image/png");
                    context.startActivity(Intent.createChooser(shareIntent, "Share Portfolio"));
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "Sharing failed!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.jobsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, Jobs.class);
            intent.putExtra("jobTitle", pf.getJobtitle()); // ✅ This is correct
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return pflist.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView name , email, phone, job_title, job_location, about, skills;
        ImageView profilepic,delete,edit;
        Button shareBtn,jobsBtn;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name);
            email = itemView.findViewById(R.id.user_email);
            phone = itemView.findViewById(R.id.user_phoneno);
            job_title = itemView.findViewById(R.id.user_title);
            job_location = itemView.findViewById(R.id.user_location);
            about = itemView.findViewById(R.id.about_description);
            skills = itemView.findViewById(R.id.user_skill);
            profilepic = itemView.findViewById(R.id.profilepic);
            delete = itemView.findViewById(R.id.delete);
            edit = itemView.findViewById(R.id.update);
            shareBtn = itemView.findViewById(R.id.shareBtn);
            jobsBtn = itemView.findViewById(R.id.jobsBtn);
        }

    }

    public void deleteUser(int position) {
        pfHelper dbHelper = new pfHelper(context);
        int userId = pflist.get(position).getId(); // Get the ID
        boolean deleted = dbHelper.deleteUser(userId);

        if (deleted) {
            pflist.remove(position);  // Remove from list
            notifyItemRemoved(position); // Notify RecyclerView
        }
    }

    private Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        view.draw(canvas);
        return returnedBitmap;
    }




}
