package com.example.android.raghu;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {

    String s,p;
    public Uri downloadUrl;
    private Button btnChoose, btnUpload;
    private ImageView imageView;
    private EditText locationtxt,descriptiontxt;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference,numbers;
    private FirebaseAuth mAuth;
    Button signout,like;
    String desc;
    private final int PICK_IMAGE_REQUEST = 71;
    String ghmc[] = {"medchal","dilshuknagar","ibrahimpatnam","kompally","kukatpally"};
    String pollutions[] = {"air pollution","water pollution","soil pollution","radioactive pollution"};
   // private FusedLocationProviderClient client;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  FirebaseApp.initializeApp(MainActivity.this);
        //Initialize Views
        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        imageView = (ImageView) findViewById(R.id.imgView);
        signout = (Button) findViewById(R.id.signout);
       // like = findViewById(R.id.likebtn);
    //    locationtxt = findViewById(R.id.locationtxt);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Spinner pspinner = findViewById(R.id.spinner1);
        descriptiontxt = findViewById(R.id.descriptiontxt);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("data");
        numbers = FirebaseDatabase.getInstance().getReference("ghmc");
        mAuth = FirebaseAuth.getInstance();
       // requestPermssion();
        //client = LocationServices.getFusedLocationProviderClient(this);


        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,ghmc);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 s = ghmc[position];
                Toast.makeText(getBaseContext(),"this is "+s,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(arrayAdapter);

        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,pollutions);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                p = pollutions[position];
                //Toast.makeText(getBaseContext(),"this is "+s,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        pspinner.setAdapter(arrayAdapter1);
    }


    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());

          // final String local  = locationtxt.getText().toString();
            final String local = s;
             desc = descriptiontxt.getText().toString();





            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                           // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();

                            while (!urlTask.isSuccessful());
                             downloadUrl = urlTask.getResult();
                            Toast.makeText(MainActivity.this, "uploaded at "+downloadUrl, Toast.LENGTH_SHORT).show();
                        //    String likes = like.getText().toString();
                           String id = databaseReference.push().getKey();
                           String s = downloadUrl.toString();
                            Toast.makeText(MainActivity.this,"this is "+s,Toast.LENGTH_LONG).show();
                           Data d = new Data(local,s,desc);
                            databaseReference.child(id).setValue(d);
                            Toast.makeText(MainActivity.this,"this is "+s,Toast.LENGTH_LONG).show();

                          //  sendmessage();

                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + "9963420626"));
                            intent.putExtra("sms_body", "this has been a very great issue"+"\n"+" at location :"+ s +"\n"+"description: "+ desc+"\n"+"type of pollution :" + p);
                            startActivity(intent);



                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });



        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseApp.initializeApp(this);
    }

   /* public void uploaddatabase()
    {

        final String local  = locationtxt.getText().toString();
        final String desc = descriptiontxt.getText().toString();
        String s = downloadUrl.toString();
     //   Toast.makeText(getBaseContext(),"this is "+s,Toast.LENGTH_LONG).show();
        Data d = new Data(local,s,desc);
        databaseReference.child(s).setValue(d);
        locationtxt.setText("");
        descriptiontxt.setText("");

    }*/

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }

    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser == null) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }
    };

    private void requestPermssion(){
        ActivityCompat.requestPermissions(this,new String[]{ACCESS_FINE_LOCATION},1);
    }

   /* public void sendmessage()
    {

        if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        client.getLastLocation().addOnSuccessListener(MainActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    //TextView textView = (TextView) findViewById(R.id.textlocation);
                    //textView.setText(location.toString());
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=%f,%f", latitude, longitude);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + "9963420626"));
                    intent.putExtra("sms_body", "this has been a very great issue"+"\n"+" at location :"+ uri +"\n"+"description: "+ desc+"\n"+"type of pollution :" + p);
                    startActivity(intent);

                }
            }


        });
    }*/
}

