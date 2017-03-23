package me.lollka.lvgrabber;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public MyTestReceiver receiverForTest;
    private ProgressDialog mDialog;

    String NIC1 = "https://www.nic.lv/domain/check?domain=";
    String NIC2 = "&btn_search=P%C4%81rbaud%C4%ABt";
    String found = "mākslīgo";
    String auto;
    String fast;

    String domain;
    String domain1;
    String domain2;
    String domain3;
    String domain4;

    Button autoOk;
    Button fastOk;
    EditText fastText;
    EditText autoText;
    ListView activeDomains;

    fastCheck Fastcheck;

    ArrayList<domainholder> arrayOfDomains = new ArrayList<domainholder>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        autoOk = (Button) findViewById(R.id.autoOk);
        fastOk = (Button) findViewById(R.id.fastOk);
        fastText = (EditText) findViewById(R.id.fastText);
        autoText = (EditText) findViewById(R.id.autoText);
        activeDomains = (ListView) findViewById(R.id.activeDomains);
        getDomains();
        AddListItems();
        ClickHandlers();
        setupServiceReceiver();
    }
    public void getDomains(){
        SharedPreferences settings = getApplicationContext().getSharedPreferences("lvgrabberdata", 0);
        domain = settings.getString("domain", null);
        domain1 = settings.getString("domain1", null);
        domain2 = settings.getString("domain2", null);
        domain3 = settings.getString("domain3", null);
        domain4 = settings.getString("domain4", null);
    }
    /*
        POGAS
     */
    public void ClickHandlers() {
        fastOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fast = fastText.getText().toString();
                if (!fast.matches("[a-zA-Z0-9]*")) {
                    Toast.makeText(MainActivity.this, "Domēns nedrīkst saturēt simbolus!", Toast.LENGTH_SHORT).show();
                } else {
                    Fastcheck = new fastCheck();
                    Fastcheck.execute();
                }
            }
        });
        autoOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                auto = autoText.getText().toString();
                if (!auto.matches("[a-zA-Z0-9]*")) {
                    Toast.makeText(MainActivity.this, "Domēns nedrīkst saturēt simbolus!", Toast.LENGTH_SHORT).show();
                } else {
                    onStartService();
                }
            }
        });
    }

    /*
        Pārbaudītāji
     */
    //Ātrais pārbaudītājs
    private class fastCheck extends AsyncTask<Void, Void, Void> {
        String txt = "Mēģini vēlreiz!";

        protected void onPreExecute() {
            super.onPreExecute();

            mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Pārbaudam domēnu...");
            mDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                String parse = Jsoup.connect(NIC1 + fast + NIC2).execute().body();
                Document result = Jsoup.parse(parse);
                txt = result.select("div[class=round-box msgbox-error]").text();
                if (txt.toLowerCase().contains(found.toLowerCase())) {
                    txt = "Domēns " + fast + ".lv diemžēl nav brīvs!";
                } else {
                    txt = "Domēns " + fast + ".lv ir brīvs!";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mDialog.dismiss();
            Toast.makeText(MainActivity.this, txt, Toast.LENGTH_LONG).show();
        }
    }

    //Automātiskais pārbaudītājs
    public void onStartService() {
        SharedPreferences settings = getApplicationContext().getSharedPreferences("lvgrabberdata", 0);
        SharedPreferences.Editor editor = settings.edit();
        Intent i = new Intent(this, DomainService.class);
        if (domain == null) {
            i.putExtra("domain", auto);
            i.putExtra("foo", "bar");
            i.putExtra("receiver", receiverForTest);
            startService(i);
            editor.putString("domain",auto);
            editor.apply();
            Toast.makeText(MainActivity.this, "Domēns pievienots automātiskai pārbaudei!", Toast.LENGTH_SHORT).show();
        } else if (domain1 == null) {
            i.putExtra("domain1", auto);
            i.putExtra("foo", "bar");
            i.putExtra("receiver", receiverForTest);
            startService(i);
            editor.putString("domain1",auto);
            editor.apply();
            Toast.makeText(MainActivity.this, "Domēns pievienots automātiskai pārbaudei!", Toast.LENGTH_SHORT).show();
        } else if (domain2 == null) {
            i.putExtra("domain2", auto);
            i.putExtra("foo", "bar");
            i.putExtra("receiver", receiverForTest);
            startService(i);
            editor.putString("domain2",auto);
            editor.apply();
            Toast.makeText(MainActivity.this, "Domēns pievienots automātiskai pārbaudei!", Toast.LENGTH_SHORT).show();
        } else if (domain3 == null) {
            i.putExtra("domain3", auto);
            i.putExtra("foo", "bar");
            i.putExtra("receiver", receiverForTest);
            startService(i);
            editor.putString("domain3",auto);
            editor.apply();
            Toast.makeText(MainActivity.this, "Domēns pievienots automātiskai pārbaudei!", Toast.LENGTH_SHORT).show();
        } else if (domain4 == null) {
            i.putExtra("domain4", auto);
            i.putExtra("foo", "bar");
            i.putExtra("receiver", receiverForTest);
            startService(i);
            editor.putString("domain4",auto);
            editor.apply();
            Toast.makeText(MainActivity.this, "Domēns pievienots automātiskai pārbaudei!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "Nav brīvas vietas domēna pārbaudei!", Toast.LENGTH_LONG).show();
        }
        finish();
        startActivity(getIntent());
    }

    /*

    BACKGROUND SERVISU CRAPS

     */
    // Setup the callback for when data is received from the service
    public void setupServiceReceiver() {
        receiverForTest = new MyTestReceiver(new Handler());
        // This is where we specify what happens when data is received from the service
        receiverForTest.setReceiver(new MyTestReceiver.Receiver() {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == RESULT_OK) {
                    //String resultValue = resultData.getString("resultValue");
                    //Toast.makeText(MainActivity.this, resultValue, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static class MyTestReceiver extends ResultReceiver {
        private Receiver receiver;

        public MyTestReceiver(Handler handler) {
            super(handler);
        }

        public void setReceiver(Receiver receiver) {
            this.receiver = receiver;
        }

        public interface Receiver {
            void onReceiveResult(int resultCode, Bundle resultData);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (receiver != null) {
                receiver.onReceiveResult(resultCode, resultData);
            }
        }
    }
    /*

    Suicidālas domas jeb ArrayAdapter

     */
    public void AddListItems(){
        UsersAdapter adapter = new UsersAdapter(this, arrayOfDomains);
        activeDomains.setAdapter(adapter);
        if (domain!=null){
            domainholder newDomain = new domainholder("domain", domain);
            adapter.add(newDomain);
        }
        if (domain1!=null){
            domainholder newDomain = new domainholder("domain1", domain1);
            adapter.add(newDomain);
        }
        if (domain2!=null){
            domainholder newDomain = new domainholder("domain2", domain2);
            adapter.add(newDomain);
        }
        if (domain3!=null){
            domainholder newDomain = new domainholder("domain3", domain3);
            adapter.add(newDomain);
        }
        if (domain4!=null){
            domainholder newDomain = new domainholder("domain4", domain4);
            adapter.add(newDomain);
        }
    }
    public class UsersAdapter extends ArrayAdapter<domainholder> {
        public UsersAdapter(Context context, ArrayList<domainholder> items) {
            super(context, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            final domainholder domainholder = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.domain_list, parent, false);
            }
            // Lookup view for data population
            TextView domain = (TextView) convertView.findViewById(R.id.domain);
            // Populate the data into the template view using the data object
            domain.setText(domainholder.getDomain()+".LV");
            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    SharedPreferences settings = getApplicationContext().getSharedPreferences("lvgrabberdata", 0);
                                    SharedPreferences.Editor editor = settings.edit();
                                    editor.putString(domainholder.getDomainid(),null);
                                    editor.apply();
                                    finish();
                                    startActivity(getIntent());
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Izņemt domēnu "+domainholder.getDomain().toUpperCase()+".LV ?").setPositiveButton("Jā", dialogClickListener)
                            .setNegativeButton("Nē", dialogClickListener).show();
                    return false;
                }
            });
            // Return the completed view to render on screen
            return convertView;
        }
    }
}
