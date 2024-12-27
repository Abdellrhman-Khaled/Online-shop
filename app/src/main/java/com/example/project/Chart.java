package com.example.project;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class Chart extends AppCompatActivity {

    private BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart);

        barChart = findViewById(R.id.barChart);

        ProductDB db = new ProductDB(this);

        // Get the top 5 most sold products
        Cursor data = db.getTop5MostSoldProducts();
        if (data != null && data.getCount() > 0) {
            ArrayList<BarEntry> entries = new ArrayList<>();
            ArrayList<String> labels = new ArrayList<>();

            // Loop through the cursor to get data for the chart
            int index = 0;
            while (data.moveToNext() && index < 5) {
                int product_id = data.getInt(data.getColumnIndexOrThrow("id"));
                int soldQuantity = data.getInt(data.getColumnIndexOrThrow("sold"));

                // Add data to entries
                entries.add(new BarEntry(index, soldQuantity));
                labels.add(String.valueOf(product_id));

                index++;
            }

            // Create a BarDataSet for the chart
            BarDataSet barDataSet = new BarDataSet(entries, "Top 5 Most Sold Products");
            barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

            // Set up the BarChart with the data
            BarData barData = new BarData(barDataSet);
            barChart.setData(barData);

            // Format X-axis with product names
            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
            xAxis.setGranularity(1f); // Ensure one label per entry
            xAxis.setGranularityEnabled(true);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            barChart.invalidate(); // Refresh the chart
        } else {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }
}
