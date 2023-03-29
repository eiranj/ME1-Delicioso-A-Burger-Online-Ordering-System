package com.example.me1delicioso;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class Manage extends AppCompatActivity {

    private static Button btnQuery;

    TextView textView, txtDefault, txtDefault_burg, txtDefault_qty, txtDefault_ID;
    private static EditText fullname;
    private static JSONParser jsonParser = new JSONParser();
    private static String urlHostName = "https://5e00-49-145-173-94.ngrok.io/burgerdatabase/SelectItemDetails.php";
    private static String urlHostDelete = "https://5e00-49-145-173-94.ngrok.io/burgerdatabase/delete.php";
    private static String urlHostBurger = "https://5e00-49-145-173-94.ngrok.io/burgerdatabase/selectBurger.php";
    private static String urlHostQty = "https://5e00-49-145-173-94.ngrok.io/burgerdatabase/selectQty.php";
    private static String urlHostID = "https://5e00-49-145-173-94.ngrok.io/burgerdatabase/selectid.php";
    private static String TAG_MESSAGE = "message", TAG_SUCCESS = "success";
    private static String online_dataset = "";
    private static String cItemcode = "";




    private String name, burg, qty, aydi;

    String cItemSelected_name, cItemSelected_burg, cItemSelected_qty, cItemSelected_ID;
    ArrayAdapter<String> adapter_name;
    ArrayAdapter<String> adapter_burg;
    ArrayAdapter<String> adapter_qty;
    ArrayAdapter<String> adapter_ID;
    ArrayList<String> list_name;
    ArrayList<String> list_burg;
    ArrayList<String> list_qty;
    ArrayList<String> list_ID;
    AdapterView.OnItemLongClickListener longItemListener_title;
    Context context = this;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        btnQuery = (Button) findViewById(R.id.btnSearch);
        fullname = (EditText) findViewById(R.id.fullname);
        txtDefault = (TextView) findViewById(R.id.tv_default);
        listView = (ListView) findViewById(R.id.listview);
        textView = (TextView) findViewById(R.id.textView4);
        txtDefault_burg = (TextView) findViewById(R.id.txt_burg);
        txtDefault_qty = (TextView) findViewById(R.id.txt_qty);
        txtDefault_ID = (TextView) findViewById(R.id.txt_ID);

        txtDefault.setVisibility(View.GONE);
        txtDefault_burg.setVisibility(View.GONE);
        txtDefault_qty.setVisibility(View.GONE);
        txtDefault_ID.setVisibility(View.GONE);

        Toast.makeText(Manage.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cItemcode = fullname.getText().toString();
                new uploadDataToURL().execute();
                new Burg().execute();
                new Qty().execute();
                new ID().execute();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                cItemSelected_name = adapter_name.getItem(position);
                cItemSelected_burg = adapter_burg.getItem(position);
                cItemSelected_qty = adapter_qty.getItem(position);
                cItemSelected_ID = adapter_ID.getItem(position);
                androidx.appcompat.app.AlertDialog.Builder alert_confirm =
                        new androidx.appcompat.app.AlertDialog.Builder(context);
                alert_confirm.setMessage("Edit the records of" + " " + cItemSelected_name);
                alert_confirm.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        txtDefault.setText(cItemSelected_name);
                        txtDefault_burg.setText(cItemSelected_burg);
                        txtDefault_qty.setText(cItemSelected_qty);
                        txtDefault_ID.setText(cItemSelected_ID);
                        name = txtDefault.getText().toString().trim();
                        burg = txtDefault_burg.getText().toString().trim();
                        qty = txtDefault_qty.getText().toString().trim();
                        aydi = txtDefault_ID.getText().toString().trim();
                        Intent intent = new Intent(Manage.this, EditOrder.class);
                        intent.putExtra(EditOrder.FULLNAME, name);
                        intent.putExtra(EditOrder.BURGER, burg);
                        intent.putExtra(EditOrder.QTY, qty);
                        intent.putExtra(EditOrder.ID, aydi);
                        startActivity(intent);
                    }
                });
                alert_confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert_confirm.show();
                return false;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cItemSelected_name = adapter_name.getItem(position);
                cItemSelected_burg = adapter_burg.getItem(position);
                cItemSelected_qty = adapter_qty.getItem(position);
                cItemSelected_ID = adapter_ID.getItem(position);

                androidx.appcompat.app.AlertDialog.Builder alert_confirm =
                        new androidx.appcompat.app.AlertDialog.Builder(context);
                alert_confirm.setMessage("Are you sure you want to delete" + " " + cItemSelected_name);
                alert_confirm.setPositiveButton(R.string.msg2, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        txtDefault_ID.setText(cItemSelected_ID);
                        aydi = txtDefault_ID.getText().toString().trim();
                        new delete().execute();
                    }
                });
                alert_confirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert_confirm.show();
            }
        });
    }
    private class uploadDataToURL extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = " ", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(Manage.this);

        public uploadDataToURL() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setMessage(cMessage);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            int nSuccess;
            try {
                ContentValues cv = new ContentValues();
                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);
                JSONObject json = jsonParser.makeHTTPRequest(urlHostName, "POST", cv);
                if (json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1) {
                        online_dataset = json.getString(TAG_MESSAGE);
                        return online_dataset;
                    } else {
                        return json.getString(TAG_MESSAGE);
                    }
                } else {
                    return "HTTPSERVER_ERROR";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(Manage.this);
            if (s != null) {
                if (isEmpty.equals("") && !s.equals("HTTPSERVER_ERROR")) { }
                String wew = s;
                String str = wew;
                final String fnames[] = str.split("-");
                list_name = new ArrayList<String>(Arrays.asList(fnames));
                adapter_name = new ArrayAdapter<String>(Manage.this, android.R.layout.simple_list_item_1, list_name);
                listView.setAdapter(adapter_name);
                textView.setText(listView.getAdapter().getCount() + " " + "record(s) found.");
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class Burg extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(Manage.this);

        public Burg() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setMessage(cMessage);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            int nSuccess;
            try {
                ContentValues cv = new ContentValues();
                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);
                JSONObject json = jsonParser.makeHTTPRequest(urlHostBurger, "POST", cv);
                if (json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1) {
                        online_dataset = json.getString(TAG_MESSAGE);
                        return online_dataset;
                    } else {
                        return json.getString(TAG_MESSAGE);
                    }
                } else {
                    return "HTTPSERVER_ERROR";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String Gender) {
            super.onPostExecute(Gender);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(Manage.this);
            if (Gender != null) {
                if (isEmpty.equals("") && !Gender.equals("HTTPSERVER_ERROR")) { }
                String gender = Gender;
                String str = gender;
                final String Genders[] = str.split("-");
                list_burg = new ArrayList<String>(Arrays.asList(Genders));
                adapter_burg = new ArrayAdapter<String>(Manage.this, android.R.layout.simple_list_item_1, list_burg);
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class Qty extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(Manage.this);

        public Qty() {
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setMessage(cMessage);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            int nSuccess;
            try {
                ContentValues cv = new ContentValues();
                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);
                JSONObject json = jsonParser.makeHTTPRequest(urlHostQty, "POST", cv);
                if(json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1) {
                        online_dataset = json.getString(TAG_MESSAGE);
                        return online_dataset;
                    } else {
                        return json.getString(TAG_MESSAGE);
                    }
                } else {
                    return "HTTPSERVER_ERROR";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String CS) {
            super.onPostExecute(CS);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(Manage.this);
            if (CS != null) {
                if (isEmpty.equals("") && !CS.equals("HTTPSERVER_ERROR")) { }
                String CivitStat = CS;
                String str = CivitStat;
                final String Civs[] = str.split("-");
                list_qty= new ArrayList<String>(Arrays.asList(Civs));
                adapter_qty = new ArrayAdapter<String>(Manage.this, android.R.layout.simple_list_item_1, list_qty);
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class ID extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(Manage.this);

        public ID() {
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setMessage(cMessage);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            int nSuccess;
            try {
                ContentValues cv = new ContentValues();
                cPostSQL = cItemcode;
                cv.put("code", cPostSQL);
                JSONObject json = jsonParser.makeHTTPRequest(urlHostID, "POST", cv);
                if(json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1) {
                        online_dataset = json.getString(TAG_MESSAGE);
                        return online_dataset;
                    } else {
                        return json.getString(TAG_MESSAGE);
                    }
                } else {
                    return "HTTPSERVER_ERROR";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String aydi) {
            super.onPostExecute(aydi);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(Manage.this);
            if (aydi != null) {
                if (isEmpty.equals("") && !aydi.equals("HTTPSERVER_ERROR")) { }
                Toast.makeText(Manage.this, "Data selected", Toast.LENGTH_SHORT);
                String AYDI = aydi;
                String str = AYDI;
                final String ayds[] = str.split("-");
                list_ID = new ArrayList<String>(Arrays.asList(ayds));
                adapter_ID = new ArrayAdapter<String>(Manage.this, android.R.layout.simple_list_item_1, list_ID);
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
    private class delete extends AsyncTask<String, String, String> {
        String cPOST = "", cPostSQL = "", cMessage = "Querying data...";
        int nPostValueIndex;
        ProgressDialog pDialog = new ProgressDialog(Manage.this);

        public delete() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setMessage(cMessage);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            int nSuccess;
            try {
                ContentValues cv = new ContentValues();
                cPostSQL = cItemSelected_ID;
                cv.put("id", cPostSQL);
                JSONObject json = jsonParser.makeHTTPRequest(urlHostDelete, "POST", cv);
                if (json != null) {
                    nSuccess = json.getInt(TAG_SUCCESS);
                    if (nSuccess == 1) {
                        online_dataset = json.getString(TAG_MESSAGE);
                        return online_dataset;
                    } else {
                        return json.getString(TAG_MESSAGE);
                    }
                } else {
                    return "HTTPSERVER_ERROR";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String del) {
            super.onPostExecute(del);
            pDialog.dismiss();
            String isEmpty = "";
            android.app.AlertDialog.Builder alert = new AlertDialog.Builder(Manage.this);
            if (aydi != null) {
                if (isEmpty.equals("") && !del.equals("HTTPSERVER_ERROR")) { }
                Toast.makeText(Manage.this, "Data Deleted", Toast.LENGTH_SHORT);
            } else {
                alert.setMessage("Query Interrupted... \nPlease Check Internet Connection");
                alert.setTitle("Error");
                alert.show();
            }
        }
    }
}

