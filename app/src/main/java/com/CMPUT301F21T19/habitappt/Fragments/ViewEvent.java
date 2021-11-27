package com.CMPUT301F21T19.habitappt.Fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.CMPUT301F21T19.habitappt.Entities.HabitEvent;
import com.CMPUT301F21T19.habitappt.Entities.User;
import com.CMPUT301F21T19.habitappt.R;
import com.CMPUT301F21T19.habitappt.Utils.SharedHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewEvent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewEvent extends Fragment {

    private HabitEvent event;

    private ImageButton editButton;
    private ImageView eventImageView;
    private TextView dateTextView;
    private TextView locationTextView;
    private TextView commentTextView;

    private FirebaseStorage storage;

    private User currentUser;

    public ViewEvent(HabitEvent event) {
        this.event = event;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_event, container, false);

        //get current user object
        currentUser = new User();


        storage = FirebaseStorage.getInstance();

        editButton = view.findViewById(R.id.edit_button);
        eventImageView = view.findViewById(R.id.event_img);
        dateTextView = view.findViewById(R.id.event_date);
        locationTextView = view.findViewById(R.id.event_location);
        commentTextView = view.findViewById(R.id.event_comment);

        eventImageView.setImageBitmap(event.getImg());
        dateTextView.setText(SharedHelper.getStringDateFromLong(event.getEventDate()));
        //locationTextView stuff here when location is done
        commentTextView.setText(event.getComment());

        if(event.getLocationLat() != -1 || event.getLocationLon() != -1){
            locationTextView.setText("Location: " + Long.toString(Math.round(event.getLocationLat()) ) + ", " + Long.toString(Math.round(event.getLocationLon())));
        }

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EditEvent(event,event.getParentHabit(),"EDIT").show(getActivity().getSupportFragmentManager(), "EDIT");
            }
        });

        DocumentReference eventInfo = currentUser.getHabitEventReference(event.getParentHabit())
                .document(event.getId());

        eventInfo.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String imgId = value.getId();
                String eventComment = (String) value.get("comments");
                String eventDate = SharedHelper.getStringDateFromLong((long) value.get("eventDate"));

                StorageReference ref = storage.getReferenceFromUrl("gs://habitappt.appspot.com/default_user/" + imgId + ".jpg");


                if(value.contains("latitude") && value.contains("longitude")){
                    Double lat = (Double) value.get("latitude");
                    Double lon = (Double) value.get("longitude");

                    locationTextView.setText("Location: " + Long.toString(Math.round(lat) ) + ", " + Long.toString(Math.round(lon)));
                }


                ref.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitMapImg = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                        eventImageView.setImageBitmap(bitMapImg);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("image retrieval failure","image retrieval failure");
                    }
                });

                dateTextView.setText(eventDate);
                commentTextView.setText(eventComment);

                //add all the location updating stuff
            }
        });


        return view;
    }
}