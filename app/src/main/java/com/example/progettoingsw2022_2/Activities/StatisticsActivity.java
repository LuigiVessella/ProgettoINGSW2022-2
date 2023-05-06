package com.example.progettoingsw2022_2.Activities;

import android.app.DatePickerDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.progettoingsw2022_2.Models.Cameriere;
import com.example.progettoingsw2022_2.Models.Ordine;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AdminSingleton;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class StatisticsActivity extends AppCompatActivity {

    private List<Ordine> ordini;
    private BarChart barChart;
    private Spinner spinnerRisto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_statistics);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this);
        datePickerDialog.show();

        inizializzaComponenti();

    }

    private void inizializzaComponenti(){

        ordini = new ArrayList<>();
        barChart = (BarChart) findViewById(R.id.chart);
        spinnerRisto = findViewById(R.id.spinnerRisto);

        int numeroRistoranti = AdminSingleton.getInstance().getAccount().getRistoranti().size();
        ArrayAdapter<Integer> numeroRistoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numeroRistoranti);
        spinnerRisto.setAdapter(numeroRistoAdapter);


        spinnerRisto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inizializeCharts(Integer.parseInt(spinnerRisto.getSelectedItem().toString()));
                countOrders(Integer.parseInt(spinnerRisto.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void inizializeCharts(int numeroRistorante){

        initBarChart();

        //prendiamoci sicuramente tutti gli ordini
        for(Cameriere cameriere : AdminSingleton.getInstance().getAccount().getRistoranti().get(numeroRistorante).getCamerieri()){
            if(cameriere.getOrdini() != null) ordini.addAll(cameriere.getOrdini());
        }

        List<BarEntry> entries = new ArrayList<BarEntry>();

        for (Ordine singleOrder : ordini) {
            // turn your data into Entry objects
            entries.add(new BarEntry(singleOrder.getNumeroTavolo(), singleOrder.getConto()));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Tot. conto"); // add entries to dataset
        initBarDataSet(dataSet);
        BarData lineData = new BarData(dataSet);
        barChart.setData(lineData);
        barChart.invalidate(); //refresh

    }

    private void initBarDataSet(BarDataSet barDataSet){
        //Changing the color of the bar
        barDataSet.setColor(Color.parseColor("#304567"));
        //Setting the size of the form in the legend
        barDataSet.setFormSize(15f);
        //showing the value of the bar, default true if not set
        barDataSet.setDrawValues(false);
        //setting the text size of the value of the bar
        barDataSet.setValueTextSize(12f);
    }

    private void initBarChart(){
        //hiding the grey background of the chart, default false if not set
        barChart.setDrawGridBackground(false);
        //remove the bar shadow, default false if not set
        barChart.setDrawBarShadow(false);
        //remove border of the chart, default false if not set
        barChart.setDrawBorders(false);

        //remove the description label text located at the lower right corner
        Description description = new Description();
        description.setEnabled(true);
        description.setText("ciao sono descrizione");
        barChart.setDescription(description);

        //setting animation for y-axis, the bar will pop up from 0 to its value within the time we set
        barChart.animateY(1000);
        //setting animation for x-axis, the bar will pop up separately within the time we set
        barChart.animateX(1000);

        XAxis xAxis = barChart.getXAxis();
        //change the position of x-axis to the bottom
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //set the horizontal distance of the grid line
        xAxis.setGranularity(1f);
        //hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(false);
        //hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false);

        YAxis leftAxis = barChart.getAxisLeft();
        //hiding the left y-axis line, default true if not set
        leftAxis.setDrawAxisLine(false);

        YAxis rightAxis = barChart.getAxisRight();
        //hiding the right y-axis line, default true if not set
        rightAxis.setDrawAxisLine(false);

        Legend legend = barChart.getLegend();
        //setting the shape of the legend form to line, default square shape
        legend.setForm(Legend.LegendForm.LINE);
        //setting the text size of the legend
        legend.setTextSize(11f);
        //setting the alignment of legend toward the chart
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        //setting the stacking direction of legend
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //setting the location of legend outside the chart, default false if not set
        legend.setDrawInside(false);

    }

    public float media(int giorni, float incasso) throws IllegalArgumentException, ArithmeticException, NullPointerException {
        float media = 0;

            if (giorni < 0) throw new IllegalArgumentException("Il numero di giorni deve essere maggiore di 0");
            if (giorni == 0) throw new ArithmeticException("Il numero di giorni non puo essere 0");
            if (incasso < 0) throw new IllegalArgumentException("l'incasso deve essere maggiore di 0.");
            media = incasso / giorni;
            media = Math.round(media * 100) / 100f;

        return media;
    }

    private void countOrders(int numeroRistorante){

        HashMap<String, Integer> preparatoDaCount = new HashMap<>();
        List<Ordine> listaOrdini = new ArrayList<>();

        for(Cameriere cameriere : AdminSingleton.getInstance().getAccount().getRistoranti().get(numeroRistorante).getCamerieri()){
            if(cameriere.getOrdini() != null) listaOrdini.addAll(cameriere.getOrdini());
        }

        for (Ordine ordine : listaOrdini) {
            String preparatoDa = ordine.getEvasoDa();
            if (preparatoDaCount.containsKey(preparatoDa)) {
                preparatoDaCount.put(preparatoDa, preparatoDaCount.get(preparatoDa) + 1);
            } else {
                preparatoDaCount.put(preparatoDa, 1);
            }
        }

    }
}