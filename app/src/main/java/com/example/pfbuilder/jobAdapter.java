package com.example.pfbuilder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class jobAdapter extends RecyclerView.Adapter<jobAdapter.viehHolder> {
    public jobAdapter(ArrayList<jobmodel> joblist, Context context) {
        this.joblist = joblist;
        this.context = context;
    }

    ArrayList<jobmodel> joblist;
    Context context;

    @NonNull
    @Override
    public viehHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.job_row_design, parent, false);
        viehHolder holder = new viehHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viehHolder holder, int position) {
        jobmodel model = joblist.get(position);
        holder.jobtitle.setText(model.getJobtitle());
        holder.company.setText(model.getCompany());
        holder.location.setText(model.getLocation());
        holder.description.setText(model.getDescription());
        holder.salary.setText(model.getSalary());
        Glide.with(context)
                .load(model.getJobicon())
                .placeholder(R.drawable.baseline_account_circle_24) // koi default image
                .into(holder.jobicon);

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
        holder.itemView.startAnimation(animation);

    }

    @Override
    public int getItemCount() {
        return joblist.size();
    }

    public class viehHolder extends RecyclerView.ViewHolder {
        TextView  jobtitle, company, location, description, salary;
        ImageView jobicon;
        public viehHolder(@NonNull View itemView) {
            super(itemView);
            jobicon = itemView.findViewById(R.id.jobicon);
            jobtitle = itemView.findViewById(R.id.textViewJobTitle);
            company = itemView.findViewById(R.id.textViewCompany);
            location = itemView.findViewById(R.id.textViewLocation);
            description = itemView.findViewById(R.id.description);
            salary = itemView.findViewById(R.id.salary);


        }
    }
}
