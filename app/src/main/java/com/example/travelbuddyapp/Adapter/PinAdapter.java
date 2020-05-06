package com.example.travelbuddyapp.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelbuddyapp.Model.Pin;
import com.example.travelbuddyapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class PinAdapter extends RecyclerView.Adapter<PinAdapter.MyViewHolder> {
    private Pin[] mDataset;
    private StorageReference mStorageRef;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public Image image;
        public TextView name;
        public TextView location;
        public TextView description;
        public ImageView pinImage;

        public MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            location = v.findViewById(R.id.location);
            description = v.findViewById(R.id.description);
            pinImage = v.findViewById(R.id.pinImage);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public PinAdapter(Pin[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PinAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pin_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//        holder.textView.setText(mDataset[position]);
        holder.name.setText(mDataset[position].getName());
        holder.location.setText(mDataset[position].getLocation());
        holder.description.setText(mDataset[position].getDescription());

        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String imagePath ="images/"+mDataset[position].getPinID()+".jpg";
        mStorageRef = FirebaseStorage.getInstance().getReference(imagePath);
        final File finalLocalFile = localFile;
        mStorageRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        // ...
                        System.out.println("Atleast successfull");

//                        System.out.println(taskSnapshot.getBytesTransferred());
                        Bitmap myBitmap = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());

                        if (!finalLocalFile.exists()) {
                            return;
                        }
                        holder.pinImage.setImageBitmap(myBitmap);


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
            }
        });

        System.out.println("Here is the local file"+localFile);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}