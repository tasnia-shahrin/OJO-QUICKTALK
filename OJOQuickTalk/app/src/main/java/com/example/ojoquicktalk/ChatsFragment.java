package com.example.ojoquicktalk;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsFragment extends Fragment {
    private View PrivateChatsView;


    private RecyclerView chatsList;
    private DatabaseReference ChatsRef, UsersRef;
    private FirebaseAuth mAuth;
    private String currentUserID;

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PrivateChatsView = inflater.inflate(R.layout.fragment_chats, container, false);
        chatsList = PrivateChatsView.findViewById(R.id.chats_list);
        chatsList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUserID = currentUser.getUid();
            ChatsRef = FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserID);
            UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        }
        return PrivateChatsView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (ChatsRef != null && UsersRef != null) {
            Query query = ChatsRef.orderByChild("timestamp");
            FirebaseRecyclerOptions<Contacts> options =
                    new FirebaseRecyclerOptions.Builder<Contacts>()
                            .setQuery(query, Contacts.class)
                            .build();

            FirebaseRecyclerAdapter<Contacts, ChatsViewHolder> adapter =
                    new FirebaseRecyclerAdapter<Contacts, ChatsViewHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull ChatsViewHolder holder, int position, @NonNull Contacts model) {
                            final String usersIDs = getRef(position).getKey();

                            final String[] retImage = {"default_image"};
                            UsersRef.child(usersIDs).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        if (snapshot.hasChild("image")) {
                                            retImage[0] = snapshot.child("image").getValue().toString();
                                            Picasso.get().load(retImage[0]).into(holder.profileImage);
                                        }
                                        final String retName = snapshot.child("name").getValue().toString();
                                        final String retStatus = snapshot.child("status").getValue().toString();
                                        holder.userName.setText(retName);

                                        if (snapshot.child("userState").hasChild("state")) {
                                            String state = snapshot.child("userState").child("state").getValue().toString();
                                            String date = snapshot.child("userState").child("date").getValue().toString();
                                            String time = snapshot.child("userState").child("time").getValue().toString();
                                            if (state.equals("online")) {
                                                holder.userStatus.setText("online");
                                            } else if (state.equals("offline")) {
                                                holder.userStatus.setText("Last Seen: " + date + " " + time);
                                            }
                                        } else {
                                            holder.userStatus.setText("offline");
                                        }

                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Intent chatIntent = new Intent(getContext(), ChatActivity.class);
                                                chatIntent.putExtra("visit_user_id", usersIDs);
                                                chatIntent.putExtra("visit_user_name", retName);
                                                chatIntent.putExtra("visit_image", retImage[0]);
                                                startActivity(chatIntent);
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }

                        @NonNull
                        @Override
                        public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_display_layout, parent, false);
                            return new ChatsViewHolder(view);
                        }
                    };

            chatsList.setAdapter(adapter);
            adapter.startListening();
            ArrayList<Contacts> chatList = new ArrayList();
            for (int i = 0; i < adapter.getItemCount(); i++) {
                chatList.add(adapter.getItem(i));
            }
            Collections.reverse(chatList);

            adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    super.onItemRangeInserted(positionStart, itemCount);
                    int messageCount = adapter.getItemCount();
                    int lastVisiblePosition = ((LinearLayoutManager) chatsList.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                    // If the lastVisiblePosition is at the end of the list, scroll to the newly added message
                    if (lastVisiblePosition == -1 || (positionStart >= (messageCount - 1) && lastVisiblePosition == (positionStart - 1))) {
                        chatsList.scrollToPosition(positionStart);
                    }
                }
            });
        }
    }


    class ChatsViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profileImage;
        TextView userStatus, userName;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.users_profile_image);
            userStatus = itemView.findViewById(R.id.user_status);
            userName = itemView.findViewById(R.id.user_profile_name);
        }
    }
}

