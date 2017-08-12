package com.latihanandroid.kerjakan;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.latihanandroid.kerjakan.adapter.KerjaanAdapter;
import com.latihanandroid.kerjakan.model.Kerjaan;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements KerjaanAdapter.ListItemClickListener {

    private Realm realm;

    private KerjaanAdapter kerjaanAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv_lakukan = (RecyclerView) findViewById(R.id.rv_lakukan);

        realm = Realm.getDefaultInstance();

        rv_lakukan.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_lakukan.setLayoutManager(layoutManager);
        rv_lakukan.setItemAnimator(new DefaultItemAnimator());

        kerjaanAdapter = new KerjaanAdapter(realm.where(Kerjaan.class).findAll(), this);
        rv_lakukan.setAdapter(kerjaanAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tambahItemLakukan();
            }
        });

        // refresh data in adapter
        kerjaanAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(Kerjaan kerjaan) {
        bukaUbahHapusDialog(kerjaan);
    }

    private void addToRealm(final String lakukan) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Kerjaan kerjaan = realm.createObject(Kerjaan.class);
                kerjaan.setItemKerjaan(lakukan);
            }
        });
    }

    private void updateRealmData(final String kerjaanLama, final String kerjaanBaru) {

        // obtain the results of a query
        final Kerjaan kerjaan = realm.where(Kerjaan.class).equalTo("itemKerjaan", kerjaanLama).findFirst();

        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                // update the data
                kerjaan.setItemKerjaan(kerjaanBaru);
            }
        });
    }

    private void deleteRealmData(String lakukan) {
        // obtain the results of a query
        final RealmResults<Kerjaan> results = realm.where(Kerjaan.class)
                .equalTo("itemKerjaan", lakukan)
                .findAll();

        // All changes to data must happen in a transaction
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                // Delete all matches
                results.deleteAllFromRealm();
            }
        });

    }

    private void tambahItemLakukan() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_lakukan, null);

        final EditText et_lakukan = (EditText) dialogView.findViewById(R.id.et_lakukan);

        alert.setView(dialogView);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String lakukan = et_lakukan.getText().toString().trim();

                // mengecek apakah user mengetikkan sesuatu pada lakukan field
                if (lakukan.length() > 0) {
                    addToRealm(lakukan);

                    // refresh data in adapter
                    kerjaanAdapter.notifyDataSetChanged();
                }
            }
        });

        alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // just dismiss this dialog
            }
        });

        final AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void bukaUbahHapusDialog(final Kerjaan kerjaan) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_lakukan, null);

        final EditText et_lakukan = (EditText) dialogView.findViewById(R.id.et_lakukan);
        et_lakukan.setText(kerjaan.getItemKerjaan());

        alert.setView(dialogView);

        alert.setPositiveButton("Ubah", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String lakukan = et_lakukan.getText().toString().trim();

                // mengecek apakah user mengetikkan sesuatu pada lakukan field
                if (lakukan.length() > 0) {

                    updateRealmData(kerjaan.getItemKerjaan(), lakukan);

                    // refresh data in adapter
                    kerjaanAdapter.notifyDataSetChanged();
                }
            }
        });

        alert.setNegativeButton("Hapus", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteRealmData(kerjaan.getItemKerjaan());

                // refresh data in adapter
                kerjaanAdapter.notifyDataSetChanged();
            }
        });

        final AlertDialog dialog = alert.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        realm.close();
    }
}
