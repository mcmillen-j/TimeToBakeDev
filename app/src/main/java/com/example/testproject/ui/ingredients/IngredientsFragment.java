package com.example.testproject.ui.ingredients;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testproject.MainActivity;
import com.example.testproject.R;
import com.example.testproject.ingredient;
import com.example.testproject.ingredientAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class IngredientsFragment extends Fragment {

    private ListView listView;
    private ArrayList<String> ingredient = new ArrayList<>();
    private DatabaseReference IngredientRef, ShoppingListRef;

    private Button conversionChartBtn;

    private FirebaseAuth mAuth;

    private String currentUserID, currentRecipeName;

//    private RecyclerView recyclerView;
//    ingredientAdapter adapter; // Create Object of the Adapter class

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ingredients, container, false);

//        recyclerView = (RecyclerView) ingredientsFragmentView.findViewById(R.id.recyclerView);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        MainActivity activity = (MainActivity) getActivity();
        currentRecipeName = activity.getRecipeNameClicked();

        conversionChartBtn = (Button) view.findViewById(R.id.conversionChartBtn);

        IngredientRef = FirebaseDatabase.getInstance().getReference("Ingredients");
        listView = (ListView) view.findViewById(R.id.ingredientsList);

        ShoppingListRef = FirebaseDatabase.getInstance().getReference("ShoppingList");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_multiple_choice, ingredient);

        IngredientRef.child(currentRecipeName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String value = snapshot.getValue(String.class);
                ingredient.add(value);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                ingredient.remove(snapshot.getValue(String.class));
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

        conversionChartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.navigation_conversionChart);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemSelected = listView.getItemAtPosition(position).toString();
                ShoppingListRef.child(currentUserID)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String pushedId = ShoppingListRef.push().getKey();
                                ShoppingListRef.child(currentUserID).child(pushedId).setValue(itemSelected);
                                Toast.makeText(getContext(), "Ingredient added to your shopping list", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

        return view;
    }

    // Function to tell the app to start getting
    // data from database on starting of the activity
//    @Override public void onStart()
//    {
//        super.onStart();
//        adapter.startListening();
//    }
//
//    // Function to tell the app to stop getting
//    // data from database on stopping of the activity
//    @Override public void onStop()
//    {
//        super.onStop();
//        adapter.stopListening();
//    }
}
