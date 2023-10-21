package com.example.pm1e17321;

import static android.content.Intent.ACTION_CALL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pm1e17321.Models.Contactos;
import com.example.pm1e17321.config.ContactDatabase;
import com.example.pm1e17321.config.SQLiteConexion;
import com.example.pm1e17321.config.transacciones;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    SQLiteConexion conexion;
    ListView listado;
    ArrayList<Contactos> listcontacto;
    ArrayList<String> ArregloPersonas;
    Button back,eliminar,compartir,actualizar,Verimagen;

    EditText Buscar;
    private ContactDatabase contactDatabase;
    private Button deleteButton;
    private int contactIdToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        back = (Button) findViewById(R.id.btnBack);
        eliminar = (Button) findViewById(R.id.BtnDelete);
        compartir = (Button) findViewById(R.id.btnShare);
        actualizar = (Button) findViewById(R.id.BtnActualizar);
        Verimagen = (Button) findViewById(R.id.btnImagen);
        Buscar = (EditText) findViewById(R.id.EtQuery);

        Intent intent = new Intent(this, MainActivity.class);
        contactDatabase = new ContactDatabase(this);




        try {

            //Establecemos una conexion a base de datos
            conexion = new SQLiteConexion(this, transacciones.namedb,null,1);
            listado = (ListView) findViewById(R.id.lista);
            GetPersons();

            ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_1,
                    ArregloPersonas);

            listado.setAdapter(adp);

            listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                    String ItemPerson = listcontacto.get(i).getNombres();
                    Toast.makeText(ListActivity.this,"Nombre " + ItemPerson, Toast.LENGTH_LONG);
                }
            });


        }
        catch(Exception e){
            e.toString();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });

        listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                new AlertDialog.Builder(ListActivity.this)
                        .setTitle("Accion")
                        .setMessage("Desea llamar a ")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                if (AlertDialog.BUTTON_POSITIVE==-1) {

                                    if (ActivityCompat.checkSelfPermission(ListActivity.this,android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                        // Si tienes permiso para realizar llamadas
                                        String phoneUri = "tel:" + listcontacto.get(position).getTelefono();;
                                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(phoneUri));
                                        startActivity(callIntent);
                                    } else {
                                        // Si no tienes permiso, solicítalo al usuario
                                        ActivityCompat.requestPermissions(ListActivity.this, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                                    }
                                }
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();


            }
        });


        eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                contactIdToDelete = 2;

                boolean deleted = contactDatabase.deleteContact(contactIdToDelete);

                if (deleted) {
                    Toast.makeText(ListActivity.this, "Contacto eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ListActivity.this, "Error al eliminar el contacto", Toast.LENGTH_SHORT).show();
                }
            }
        });

        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        compartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Toast.makeText(ListActivity.this,"Manten Presionado el contacto que quieras compartir", Toast.LENGTH_LONG);

            }
        });

        Verimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ListActivity.this,"Esta Funcion Aun no esta implementada. ", Toast.LENGTH_LONG);
            }
        });

        listado.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtén el contacto seleccionado
                Contactos contacto = listcontacto.get(position);

                // Crea un mensaje con la información del contacto
                String mensaje =
                        "Pais: "+contacto.getPais()+"\n"+
                        "Nombre: " + contacto.getNombres() + "\n" +
                        "Teléfono: " + contacto.getTelefono() + "\n" +
                        "Nota: " + contacto.getNota();

                // Crea una intención para compartir en WhatsApp
                Intent compartirIntent = new Intent(Intent.ACTION_SEND);
                compartirIntent.setType("text/plain");
                compartirIntent.setPackage("com.whatsapp"); // Especificar WhatsApp como destino
                compartirIntent.putExtra(Intent.EXTRA_TEXT, mensaje);

                try {
                    startActivity(compartirIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    // WhatsApp no está instalado
                    Toast.makeText(ListActivity.this, "WhatsApp no está instalado", Toast.LENGTH_SHORT).show();
                }

                return true; // Devuelve true para indicar que se ha manejado la pulsación larga
            }
        });

        Buscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Llama a una función para filtrar los resultados del listado
                filtrarResultados(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });



    }




    private void GetPersons() {

        SQLiteDatabase db = conexion.getReadableDatabase();
        Contactos person = null;
        listcontacto = new ArrayList<Contactos>();

        Cursor cursor = db.rawQuery(transacciones.SelectTablePersonas,null);

        while(cursor.moveToNext()){

            person = new Contactos();
            person.setId(cursor.getInt(0));
             person.setPais(cursor.getString(1));
            person.setNombres(cursor.getString(2));
            person.setTelefono(cursor.getInt(3));
            person.setNota(cursor.getString(4));

            listcontacto.add(person);

        }
        cursor.close();
        fillList();
    }

    private void fillList() {

        ArregloPersonas = new ArrayList<String>();

        for(int i =0;i<listcontacto.size();i++){

            ArregloPersonas.add(listcontacto.get(i).getId() + ".  " +
                    listcontacto.get(i).getPais()+ " - "+
                    listcontacto.get(i).getNombres()+ " - "+
                    listcontacto.get(i).getTelefono()+ "\n\t"+
                    listcontacto.get(i).getNota());
        }
    }

    private void filtrarResultados(String query) {
        ArrayList<Contactos> resultadosFiltrados = new ArrayList<>();
        ArrayList<Contactos> listcontactoOriginal = new ArrayList<>(listcontacto);

        for (Contactos contacto : listcontactoOriginal) {
            // Comprueba si el ID o el nombre del contacto contiene el texto de búsqueda
            if (String.valueOf(contacto.getId()).contains(query) ||
                    contacto.getNombres().toLowerCase().contains(query.toLowerCase())) {
                resultadosFiltrados.add(contacto);
            }
        }

        // Crea un adaptador con los resultados filtrados y configúralo en tu ListView
        ArrayAdapter<Contactos> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, resultadosFiltrados);
        listado.setAdapter(adapter);
    }


}