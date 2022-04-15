package edu.polytech.fridge.ui.fridge;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import edu.polytech.fridge.R;
import edu.polytech.fridge.databinding.FragmentFridgeBinding;
import edu.polytech.fridge.ui.fridge.view.FoodAdapter;
import edu.polytech.fridge.ui.fridge.view.FoodViewModel;

public class FridgeFragmentFoods extends Fragment {
    private FragmentFridgeBinding binding;
    private FoodAdapter foodAdapterForUserFridge;
    private RecyclerView recyclerViewToDisplayFridgeFood;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFridgeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Fridge.generateFridgeTemplateWithFakeFoods();

        setUpFridgeContent();
        setUpSpinner();
        setUpAddFoodButton();

        return root;
    }

    private void setUpSpinner() {
        Spinner spinner = binding.spinnerFilter;
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        getContext(),
                        R.array.filter_by_fridge,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getItemAtPosition(position).toString()) {
                    case "Date": filterListExpirationDate(); break;
                    case "Quantity": filterListQuantity(); break;
                    case "Name":
                    default:
                        filterListAlphabeticOrder(); break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setUpAddFoodButton() {
        FloatingActionButton faButtonAddFood = binding.addFood;
        faButtonAddFood.setOnClickListener(view -> addFoodOnFridgeActivity());
    }

    private void setUpFridgeContent() {
        foodAdapterForUserFridge = new FoodAdapter(Fridge.getInstance().getFoodList());
        recyclerViewToDisplayFridgeFood = binding.simpleRecyclerview;
        recyclerViewToDisplayFridgeFood.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewToDisplayFridgeFood.setHasFixedSize(true);
        recyclerViewToDisplayFridgeFood.setAdapter(foodAdapterForUserFridge);

    }

    private void filterListAlphabeticOrder() {
        Fridge.getInstance().getFoodList().sort(Comparator.comparing(FoodViewModel::getFoodName));
        recyclerViewToDisplayFridgeFood.setAdapter(new FoodAdapter(Fridge.getInstance().getFoodList()));
    }

    private void filterListQuantity() {
        Fridge.getInstance().getFoodList().sort(Comparator.comparing(FoodViewModel::getCurrentQuantity).reversed());
        recyclerViewToDisplayFridgeFood.setAdapter(new FoodAdapter(Fridge.getInstance().getFoodList()));
    }

    private void filterListExpirationDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Fridge.getInstance().getFoodList().sort(Comparator.comparing((FoodViewModel foodViewModel) -> {
            try {
                return format.parse(foodViewModel.getExpirationDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }));
        recyclerViewToDisplayFridgeFood.setAdapter(new FoodAdapter(Fridge.getInstance().getFoodList()));
    }

    public void addFoodOnFridgeActivity() {
        startActivity(new Intent(getActivity(), FridgeFindFoodsActivity.class));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}