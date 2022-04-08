package com.example.testproject.ui.shoppingList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.testproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShoppingListFragment extends Fragment {

    private static final String TAG = "Shopping List Fragment";
    private ListView listView;
    private ArrayList<String> itemName = new ArrayList<>();
    private DatabaseReference ShoppingListRef;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        ShoppingListRef = FirebaseDatabase.getInstance().getReference("ShoppingList");
        listView = (ListView) view.findViewById(R.id.shoppingList);

        mAuth = FirebaseAuth.getInstance();
        String currentUserID = mAuth.getCurrentUser().getUid();

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, itemName);

        ShoppingListRef.child(currentUserID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.getValue(String.class);
                itemName.add(value);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                itemName.remove(snapshot.getValue(String.class));
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        listView.setAdapter(arrayAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        listView.setItemChecked(0, true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemSelected = listView.getItemAtPosition(position).toString();
                Log.d("PositionNum: ", String.valueOf(position));
                Log.d("Position: ", itemSelected);

                ShoppingListRef = FirebaseDatabase.getInstance().getReference("ShoppingList");


                ShoppingListRef.child(currentUserID)
                        .orderByChild(ShoppingListRef.getKey())
                        .equalTo(itemSelected)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                    String itemKey = childSnapshot.getKey();
                                    ShoppingListRef.child(currentUserID).child(itemKey).removeValue();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

//                String s = String.valueOf(position);
//                Log.d("Position", s);
//                ShoppingListRef = FirebaseDatabase.getInstance().getReference("ShoppingList");
//                ShoppingListRef.child(currentUserID).child(s).removeValue();
            }

        });

        return view;
    }
}