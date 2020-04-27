package com.example.contentproviderexample;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ArrayList<String> nameArray = new ArrayList<String>();
    ArrayList<String> numberArray= new ArrayList<String>();
    ArrayList<String> titleArray = new ArrayList<String>();
    ArrayList<String>idArray=new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    String selection= null;
    String [] selectionArgs=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        nameArray.clear();
        numberArray.clear();
        titleArray.clear();
        idArray.clear();

        ContentResolver contentResolver=getContentResolver();
        Cursor cursor=contentResolver.query(AppProvider.CONTENT_URI,null,selection,selectionArgs,null);
        if(cursor!=null)
        {
            while(cursor.moveToNext())
            {
                numberArray.add(cursor.getString(cursor.getColumnIndex(AppProvider.NUMBER)));
                titleArray.add(cursor.getString(cursor.getColumnIndex(AppProvider.TITLE)));
                nameArray.add(cursor.getString(cursor.getColumnIndex(AppProvider.NAME)));
                idArray.add(cursor.getString(cursor.getColumnIndex(AppProvider.ID)));
            }
            cursor.close();
            arrayAdapter= new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,android.R.id.text1,nameArray);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent= new Intent(MainActivity.this,Main2Activity.class);
                    intent.putExtra("info","old");
                    intent.putExtra("name",nameArray.get(i));
                    intent.putExtra("number",numberArray.get(i));
                    intent.putExtra("title",titleArray.get(i));
                    intent.putExtra("id",idArray.get(i));
                    startActivity(intent);
                }
            });
        }



    }

    public void listAll(View view )
    {

        selection=null;
        selectionArgs= null;
        nameArray.clear();
        numberArray.clear();
        titleArray.clear();
        idArray.clear();
        onResume();

    }
    public void listFamily(View view)
    {
        nameArray.clear();
        numberArray.clear();
        titleArray.clear();
        idArray.clear();
        selection="title=?";
        selectionArgs= new String[]{"Aile"};
        onResume();

    }
    public void listFriends(View view)
    {
        nameArray.clear();
        numberArray.clear();
        titleArray.clear();
        idArray.clear();
        selection="title=?";
        selectionArgs= new String[]{"Arkadas"};
        onResume();

    }
    public void listImportant(View view)
    {
        nameArray.clear();
        numberArray.clear();
        titleArray.clear();
        idArray.clear();
        selection="title=?";
        selectionArgs= new String[]{"Acil"};
        onResume();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.add_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.add)
        {
            Intent intent= new Intent(MainActivity.this,Main2Activity.class);
            intent.putExtra("info","new");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
