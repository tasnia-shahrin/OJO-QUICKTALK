package com.example.ojoquicktalk;


import android.content.Context;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {

        static void showToast(Context context,String message){
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
        }

        static CollectionReference getCollectionReferenceForNotes(){
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            return FirebaseFirestore.getInstance().collection("notes")
                    .document(currentUser.getUid()).collection("my_notes");
        }

        public static String timestampToString(Timestamp timestamp) {
            if (timestamp != null) {
                Date date = timestamp.toDate();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                return sdf.format(date);
            } else {
                // Handle the case where the timestamp is null, return a default value, or handle the error.
                return "Timestamp not available";
            }
        }

    }

