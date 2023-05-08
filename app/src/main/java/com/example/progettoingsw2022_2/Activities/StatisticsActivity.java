package com.example.progettoingsw2022_2.Activities;

import android.app.DatePickerDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.progettoingsw2022_2.Models.Cameriere;
import com.example.progettoingsw2022_2.Models.Ordine;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AdminSingleton;
import com.example.progettoingsw2022_2.SingletonModels.CameriereSingleton;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class StatisticsActivity extends AppCompatActivity {

    private ArrayList<Ordine> ordini;
    private BarChart barChart;
    private Spinner spinnerRisto;
    private TextView mediaText;
    private Button selectDataButton;
    private LinearLayout scrollLinear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_statistics);


        inizializzaComponenti();

    }

    private void inizializzaComponenti(){

        ordini = new ArrayList<>();
        barChart = (BarChart) findViewById(R.id.chart);
        spinnerRisto = findViewById(R.id.spinnerRisto);
        mediaText = findViewById(R.id.incassiTextView);
        scrollLinear = findViewById(R.id.linearLayoutScrollForCharts);
        selectDataButton = findViewById(R.id.selectDataButton);


        int numeroRistoranti = AdminSingleton.getInstance().getAccount().getRistoranti().size();
         List<Integer>numRistArray = new ArrayList<>();

        for(int i = 0; i < numeroRistoranti; i++) {
            numRistArray.add(i);
        }

        ArrayAdapter<Integer> numeroRistoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numRistArray);
        spinnerRisto.setAdapter(numeroRistoAdapter);


        selectDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        spinnerRisto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                inizializeCharts(Integer.parseInt(spinnerRisto.getSelectedItem().toString()));
                countOrders(Integer.parseInt(spinnerRisto.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(StatisticsActivity.this, "Seleziona un ristorante", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inizializeCharts(int numeroRistorante){
        ordini.removeAll(ordini);
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
        description.setText("");
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

            if (giorni < 0) throw new IllegalArgumentException("Il numero di giorni non può essere un numero negativo");
            if (giorni == 0) throw new ArithmeticException("Il numero di giorni non puo essere 0");
            if (incasso < 0) throw new IllegalArgumentException("l'incasso deve essere maggiore di 0.");
            media = incasso / giorni;
            media = Math.round(media * 100) / 100f;

        return media;
    }

    //data inizio selezionata da utente. data fine giorno corrente. la media sarà su questi giorni
    public int getIncassoRangeGiorni(LocalDate dataInizio, ArrayList<Ordine> orders) throws DateTimeParseException {
        int incassoTotale = 0;
        LocalDate endDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Ordine ordine : orders) {
            try {
                LocalDate orderDate = LocalDate.parse(ordine.getDataOrdine(), formatter);
                if (orderDate.isEqual(dataInizio) || orderDate.isAfter(dataInizio) && orderDate.isBefore(endDate)) {
                    incassoTotale += ordine.getConto();
                }
            } catch (DateTimeParseException e) {
                // La data non è nel formato atteso
                throw new DateTimeParseException("Data non nel formato atteso", ordine.getDataOrdine(), 0);
            }
        }

        return incassoTotale;
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


        for (Map.Entry<String, Integer> entry : preparatoDaCount.entrySet()) {
            TextView txt = new TextView(this);
            txt.setText(entry.getKey() + " = " + entry.getValue());
            txt.setTextSize(20);
            scrollLinear.addView(txt);
        }

    }


    private void showDatePickerDialog() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // Imposta la data corrente come predefinita nel dialog
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Crea un nuovo dialog di selezione della data e mostra
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth1) -> {
                    // Gestisci la data selezionata dall'utente
                    // Esempio: aggiorna un campo di testo con la data selezionata
                    String selectedDate = dayOfMonth1 + "/" + (month1 + 1) + "/" + year1;
                    LocalDate startDate = LocalDate.parse(selectedDate, dateFormat);

                    try {
                        long diffInMillies = Math.abs(Calendar.getInstance().getTime().getTime() - dateFormat.parse(selectedDate).getTime());
                        long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                        System.out.println("differenza giorni: " + diff);
                        float incassoTotale = getIncassoRangeGiorni(startDate, ordini);
                        float incassoMedio = media((int)diff, incassoTotale);
                        mediaText.setText(incassoMedio + "€" + " incassati ");
                    } catch (ParseException e) {
                        mediaText.setText("Formato data non corretto");
                    }
                    catch (IllegalArgumentException e) {
                        mediaText.setText("Impossibile calcolare la media");
                    }
                    catch (NullPointerException e){
                        mediaText.setText("Impossibile calcolare la media");
                    }
                    catch (ArithmeticException e) {
                        mediaText.setText("Seleziona un numero corretto di giorni");
                    }
                },
                year, month, dayOfMonth);

        datePickerDialog.show();
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMaxDate(Calendar.getInstance().getTime().getTime());

    }
}