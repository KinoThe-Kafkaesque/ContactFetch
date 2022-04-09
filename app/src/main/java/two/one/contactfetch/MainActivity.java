package two.one.contactfetch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.telephony.TelephonyManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import two.one.contactfetch.adapter.ContactAdapter;
import two.one.contactfetch.entities.Contact;
import two.one.contactfetch.entities.Device;
import two.one.contactfetch.web.ContactController;
import two.one.contactfetch.web.RetrofitSettings;


@SuppressLint("MissingPermission")
public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private CountryCodePicker countryCodePicker;
    private EditText number;
    private EditText nom;
    ProgressDialog progressDoalog;
    Button find;
    static MainActivity mainActivity;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        countryCodePicker = findViewById(R.id.ccp);
        number = findViewById(R.id.phoneText);
        nom = findViewById(R.id.nom);
        find = findViewById(R.id.FindPhoneButton);
        find.setOnClickListener(this::onClick);
        getDeviceIMEI();
        sync();
        findAll();

    }

    public void onClick(View v) {
        String code = countryCodePicker.getSelectedCountryCodeWithPlus();
        String num = number.getText().toString().trim();

        String phoneNumber = code+" "+ num.substring(0,3)+"-"+num.substring(3);
        String name = nom.getText().toString().trim();
        System.out.println(phoneNumber);

        if (v == find) {
            System.out.println("###############################################");
            for (Contact c : findByNameOrNumber(phoneNumber, name))
                System.out.println( c.getNom());
            generateDataList(findByNameOrNumber(phoneNumber, name));
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public String getDeviceIMEI() {
        Permissons.Request_PHONE_STATEE(this, 20);
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public List<Contact> getNumber(ContentResolver cr) {
        List<Contact> contacts = new ArrayList<>();

        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (phones.moveToNext()) {
            @SuppressLint("Range") String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            @SuppressLint("Range") String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            contacts.add(new Contact(name, phoneNumber, new Device(getDeviceIMEI())));
        }
        phones.close();// close cursor
        return contacts;
    }

    public static MainActivity getInstance() {
        return mainActivity;
    }

    public void sync() {
        progressDoalog = new ProgressDialog(MainActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        ContactController service = RetrofitSettings.getRetrofitInstance().create(ContactController.class);
        Call<Device> call = service.getUser(getDeviceIMEI());
        call.enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                progressDoalog.dismiss();
                if (response.body() == null) {


                    Call<List<Contact>> createUser = service.createUser(getNumber(mainActivity.getContentResolver()));
                    createUser.enqueue(
                            new Callback<List<Contact>>() {
                                @Override
                                public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                                    System.out.println(response.body());
                                }

                                @Override
                                public void onFailure(Call<List<Contact>> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            }
                    );
                }

            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {
                progressDoalog.dismiss();
                t.printStackTrace();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public List<Contact> findByNameOrNumber(String number, String name) {

        List<Contact> contacts = new ArrayList<>();

        /*Create handle for the RetrofitInstance interface*/
        ContactController service = RetrofitSettings.getRetrofitInstance().create(ContactController.class);
        Call<List<Contact>> call = service.getByNumber(number, getDeviceIMEI());
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                if (response.body() != null)
                    contacts.addAll(response.body());
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                t.printStackTrace();

            }
        });
        Call<List<Contact>> call1 = service.getByName(name, getDeviceIMEI());
        call1.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                if (response.body() != null)
                    contacts.addAll(response.body());

            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                t.printStackTrace();

            }
        });
        progressDoalog.dismiss();

        return contacts;
    }

    public void findAll() {


        /*Create handle for the RetrofitInstance interface*/
        ContactController service = RetrofitSettings.getRetrofitInstance().create(ContactController.class);
        Call<List<Contact>> call = service.getAll(getDeviceIMEI());
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
               for (Contact c : response.body()){
                   System.out.println(c.getNom());
               }
                if (response.body() != null)
                generateDataList(response.body());

            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                t.printStackTrace();

            }
        });


    }

    private void generateDataList(List<Contact> contactList) {
        recyclerView = findViewById(R.id.ContactRecyclerView);
        contactAdapter = new ContactAdapter(this, contactList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(contactAdapter);
    }

}
