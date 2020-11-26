package com.example.drinksandroidapplication.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drinksandroidapplication.MainActivity;
import com.example.drinksandroidapplication.R;
import com.example.drinksandroidapplication.controller.SingletonFavorites;
import com.example.drinksandroidapplication.entities.Drink;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<Drink> dataSet;
    private Context context;
    private MainActivity mainActivity;
    private ViewGroup parent;

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewPrice;
        ImageView imageViewPhoto;

        MyViewHolder(View view) {
            super(view);

            this.textViewName = view.findViewById(R.id.textViewName);
            this.textViewPrice = view.findViewById(R.id.textViewPrice);
            this.imageViewPhoto = view.findViewById(R.id.imageViewPhoto);
        }
    }

    public CustomAdapter(MainActivity mainActivity, Context context, List<Drink> data) {
        this.dataSet = data;
        this.context = context;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drink_layout, parent, false);
        this.parent = parent;
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        final TextView textViewName = holder.textViewName;
        final TextView textViewAlcoholic = holder.textViewPrice;

        final Drink drink = dataSet.get(listPosition);

        textViewName.setText(drink.getName());
        textViewAlcoholic.setText(drink.getAlcoholic());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set the custom layout
                // create an alert builder
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                builder.setTitle(drink.getName());


                // Inflate the view with values
                final View customLayout = mainActivity.getLayoutInflater().inflate(R.layout.dialog_drink, parent, false);
                loadXMLValues(customLayout, drink);
                builder.setView(customLayout);

                // add a button
                builder.setPositiveButton("Add favorite", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SingletonFavorites.getInstance().addDrink(drink);
                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();

                //DrinkDialog drinkDialog = new DrinkDialog(mainActivity, dataSet.get(listPosition));
                //drinkDialog.show();
            }
        });
    }

    /**
     * Download task mixture/mashup from the following links. (Why invent the wheel again)
     * https://stackoverflow.com/questions/31041100/downloading-image-in-android-using-asynctask-for-recyclerview
     * https://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
     */
    private static class LoadImage extends AsyncTask<String, Void, Bitmap> {

        // WeakReference counteracts ImageView field leak.
        private final WeakReference<ImageView> imageViewReference;

        public LoadImage(ImageView imageView) {
            imageViewReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            try {
                String urldisplay = params[0];
                Bitmap mIcon11 = null;
                try {
                    InputStream in = new java.net.URL(urldisplay).openStream();
                    mIcon11 = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.e("Error", e.getMessage());
                    e.printStackTrace();
                }
                return mIcon11;
            } catch (Exception e) {
                // log error
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            // If AsyncTask is cancelled set image bitmap as null
            if (isCancelled()) {
                bitmap = null;
            }

            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                } else {
                    // If the image equals null set view as generic icon for drink.
                    Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.ic_local_bar_black_24dp);
                    imageView.setImageDrawable(placeholder);
                }
            }
        }
    }

    /**
     * Initiate the layout with values from Drink item.
     *
     * @param customLayout
     * @param drink
     */
    private void loadXMLValues(View customLayout, Drink drink) {

        new LoadImage((ImageView) customLayout.findViewById(R.id.imageView)).execute(drink.getImageURL());

        TextView textViewDrinkName = customLayout.findViewById(R.id.textView_DrinkName);
        TextView textViewInstructions = customLayout.findViewById(R.id.textView_Instructions);
        TextView textViewIngredients = customLayout.findViewById(R.id.textView_Ingredient);
        TextView textViewMeasures = customLayout.findViewById(R.id.textView_Measures);

        textViewDrinkName.setText(drink.getName());
        textViewInstructions.setText(drink.getInstructions());

        StringBuilder tmp = new StringBuilder();
        for (String ingredient : drink.getIngredients()) {
            tmp.append(ingredient).append("\n");
        }
        textViewIngredients.setText(tmp.toString());

        tmp = new StringBuilder();
        for (String measure : drink.getMeasures()) {
            tmp.append(measure).append("\n");
        }
        textViewMeasures.setText(tmp.toString());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
