package com.example.contentproviderexample;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLOutput;

public class Main2Activity extends AppCompatActivity {
    EditText name;
    EditText number;
    Button save,update,delete,add;
    String getName;
    String getId;
    String getNumber;
    String getTitle;
    RadioGroup radioGroup;
    RadioButton r1,r2,r3;
    int selectedId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        name=findViewById(R.id.editTextName);
        number=findViewById(R.id.editTextNumber);
        save=findViewById(R.id.buttonSave);
        update=findViewById(R.id.buttonUpdate);
        delete=findViewById(R.id.buttonDelete);
        add=findViewById(R.id.buttonAdd);
        radioGroup=findViewById(R.id.radioGroup);
        r1=findViewById(R.id.radioFamily);
        r2=findViewById(R.id.radioFriend);
        r3=findViewById(R.id.radioImportant);
        Bundle bundle= getIntent().getExtras();
        getName=bundle.getString("name");
        getNumber=bundle.getString("number");
        getTitle=bundle.getString("title");
        getId=bundle.getString("id");
        String getInfo=bundle.getString("info");
        System.out.println(getInfo);
        if(getInfo.matches("old"))
        {
            String a=r1.getText().toString();
            name.setText(getName);
            number.setText(getNumber);
            save.setVisibility(View.INVISIBLE);
            update.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            add.setVisibility(View.INVISIBLE);
          if(getTitle.matches(r1.getText().toString()))
          {
              r1.setChecked(true);
          }
          else if (getTitle.matches(r2.getText().toString()))
          {
              r2.setChecked(true);
          }
          else
          {
              r3.setChecked(true);
          }

        }
        else
        {
            update.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.INVISIBLE);
            save.setVisibility(View.VISIBLE);
            name.setText("");
            number.setText("");
        }


    }
    public void ContactList(View view)
    {
        if(ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(Main2Activity.this,new String[] {Manifest.permission.READ_CONTACTS},1);
        }
        else
        {
            Intent intent= new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent,2);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1&&grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            Intent intent= new Intent(Intent.ACTION_PICK,ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent,2);
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2&&resultCode==RESULT_OK&&data!=null)
        {
            Uri uri=data.getData();
            Cursor cursor=getContentResolver().query(uri,null,null,null,null);
            if(cursor!=null)
            {
                while(cursor.moveToNext())
                {
                    getName=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    String id=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                    {


                       //Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);


                        Cursor findNumber=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null,null);


                        while(findNumber.moveToNext())
                        {
                            getNumber=findNumber.getString(findNumber.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            Toast.makeText(this, ""+getNumber, Toast.LENGTH_SHORT).show();
                        }
                        findNumber.close();
                    }
                }

            }
            cursor.close();

            number.setText(getNumber);
            name.setText(getName);
        }
    }
    public void Save(View view)
    {
        selectedId=radioGroup.getCheckedRadioButtonId();
        switch (selectedId)
        {
            case R.id.radioFamily:
                getTitle=r1.getText().toString();
                Toast.makeText(this, ""+getTitle, Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioFriend:
                getTitle=r2.getText().toString();
                break;
            case R.id.radioImportant:
                getTitle=r3.getText().toString();
                break;
            default:
                break;

        }

        //AppProvider.NAME bunlar sqlite column isimlerini temsil ediyor bunlar yerine put içine direk column ismini versende olur

        ContentValues contentValues=new ContentValues();
        contentValues.put(AppProvider.NAME,name.getText().toString());
        contentValues.put(AppProvider.NUMBER,number.getText().toString());
        contentValues.put(AppProvider.TITLE,getTitle);
        getContentResolver().insert(AppProvider.CONTENT_URI,contentValues);
        finish();




    }
    public void Delete (View view)
    {
        getContentResolver().delete(AppProvider.CONTENT_URI,"id=? ",new String[]{getId});
        finish();

    }
    public void Update(View view)
    {
        selectedId=radioGroup.getCheckedRadioButtonId();
        switch (selectedId)
        {
            case R.id.radioFamily:
                getTitle=r1.getText().toString();
                Toast.makeText(this, ""+getTitle, Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioFriend:
                getTitle=r2.getText().toString();
                break;
            case R.id.radioImportant:
                getTitle=r3.getText().toString();
                break;
            default:
                break;

        }
        ContentValues contentValues=new ContentValues();
        contentValues.put(AppProvider.NAME,name.getText().toString());
        contentValues.put(AppProvider.NUMBER,number.getText().toString());
        contentValues.put(AppProvider.TITLE,getTitle);
        getContentResolver().update(AppProvider.CONTENT_URI,contentValues,"id=?",new String[]{getId});
        finish();

    }
}
