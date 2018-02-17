package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @BindView(R.id.image_iv)
    ImageView mImage;

    @BindView(R.id.also_known_label)
    TextView mAlsoKnownLabel;

    @BindView(R.id.also_known_tv)
    TextView mAlsoKnownAs;

    @BindView(R.id.origin_tv)
    TextView mPlaceOfOrigin;

    @BindView(R.id.place_of_origin_label)
    TextView mPlaceOfOriginLabel;

    @BindView(R.id.ingredients_tv)
    TextView mIngredients;

    @BindView(R.id.description_tv)
    TextView mDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
            return;
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
    }

    private String capitalizeFirst(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }

    private void populateUI(Sandwich sandwich) {
        // Title
        setTitle(sandwich.getMainName());

        // Image
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(mImage);

        // Also known as
        List<String> alsoKnownAs = sandwich.getAlsoKnownAs();

        StringBuilder alsoKnowAsDisplay = new StringBuilder(TextUtils.join(", ", alsoKnownAs));
        alsoKnowAsDisplay.append("\n");

        if (alsoKnownAs.isEmpty()) {
            mAlsoKnownLabel.setVisibility(View.GONE);
            mAlsoKnownAs.setVisibility(View.GONE);
        } else {
            mAlsoKnownAs.setText(alsoKnowAsDisplay.toString());
        }

        // Place of origin
        String placeOfOrigin = sandwich.getPlaceOfOrigin();
        if (!TextUtils.isEmpty(placeOfOrigin)) {
            StringBuilder sb = new StringBuilder(placeOfOrigin);
            mPlaceOfOrigin.setText(sb.append("\n").toString());
        }
        else {
            mPlaceOfOriginLabel.setVisibility(View.GONE);
            mPlaceOfOrigin.setVisibility(View.GONE);
        }

        // Ingredients
        List<String> ingredients = sandwich.getIngredients();

        StringBuilder ingredientsSb = new StringBuilder();

        for (String ingredient : ingredients) {
            ingredientsSb.append("- ").append(capitalizeFirst(ingredient)).append("\n");
        }

        mIngredients.setText(ingredientsSb.toString());

        // Description
        mDescription.setText(sandwich.getDescription());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
