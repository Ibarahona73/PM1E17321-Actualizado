package com.example.pm1e17321;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.pm1e17321.config.ContactDatabase;
import com.example.pm1e17321.config.SQLiteConexion;
import com.example.pm1e17321.config.transacciones;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    String Ruta,err;
    ImageView foto;
    EditText nombres,phone,nota;
    Spinner Pais;
    Button salvar,salvados;

    FloatingActionButton fab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> paises = new ArrayList<>();



        // Spinner
        Pais = (Spinner) findViewById(R.id.SpinPais);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paises);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Pais.setAdapter(adapter);



        foto = (ImageView) findViewById(R.id.IvPerfil);
        Pais = (Spinner) findViewById(R.id.SpinPais);
        nombres = (EditText) findViewById(R.id.EtName);
        phone = (EditText) findViewById(R.id.EtPhone);
        nota = (EditText) findViewById(R.id.EtNota);
        salvar = (Button) findViewById(R.id.btnsalvar);
        salvados = (Button) findViewById(R.id.Csalvados);
        fab = (FloatingActionButton) findViewById(R.id.Btnfab);
        permisos();

        Intent intent = new Intent(this, ListActivity.class);

        nombres.addTextChangedListener(new TextWatcher() {
        @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {
           }
          @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    // El campo "nombre" está vacío, deshabilitamos el siguiente campo
                    phone.setEnabled(false);
                } else {
                    // El campo "nombre" no está vacío, habilitamos el siguiente campo
                    phone.setEnabled(true);
                    String input = s.toString();
                    if (!input.matches("^[a-zA-Z ]+$")) {
                        nombres.setError("Nombre no válido (solo letras y espacios permitidos).");
                        salvar.setEnabled(false);
                    }
                }
            }
        });

            phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                        if (nombres.getText().toString().isEmpty()) {
                            // Si el campo "nombre" está vacío y el usuario intenta enfocar el siguiente campo, mostramos una alerta
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Campo Nombre Vacío");
                            builder.setMessage("Por favor, debe escribir un nombre antes de continuar.");
                                nombres.requestFocus(); // Devolver el foco al campo "nombre"

                            builder.show();
                        }
                    }
                }
            });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    nota.setEnabled(false);
                } else {
                    nota.setEnabled(true);
                }
                String input = s.toString();
                if (!input.matches("^[0-9]{8}$")) {
                    phone.setError("Número de teléfono no válido (8 dígitos requeridos).");
                    salvar.setEnabled(false);
                }
            }
        });

        nota.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (phone.getText().toString().isEmpty()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Campo Telefono Vacío");
                        builder.setMessage("Por favor, debe escribir un telefono antes de continuar.");
                        phone.requestFocus();
                        builder.show();
                    }
                }
            }
        });


        nota.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    salvar.setEnabled(false);
                } else {
                    salvar.setEnabled(true);
                }
                String input = s.toString();
                if (!input.matches("^[a-zA-Z ]+$")) {
                    nombres.setError("Nombre no válido (solo letras y espacios permitidos).");
                    salvar.setEnabled(false);
                }
            }
        });

        salvar.setEnabled(false);
        nota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nota.getText().toString().isEmpty()) {
                    // Si el campo "nota" está vacío, mostramos una alerta
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Campo Vacío");
                    builder.setMessage("Por favor, debe escribir una nota antes de guardar.");
                    builder.show();
                } else {
                    // Realizar la acción de guardar si el campo "nota" no está vacío
                      //
                }
            }
        });

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                permisos();
            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AddPerson();

                startActivity(intent);
            }
        });

        salvados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(intent);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText nuevoPaisEditText = new EditText(MainActivity.this);
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Nuevo País")
                        .setMessage("Escribe el nombre del nuevo país:")
                        .setView(nuevoPaisEditText)
                        .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                String nuevoPais = nuevoPaisEditText.getText().toString();
                                if (!nuevoPais.isEmpty()) {
                                    // Agregar el nuevo país al ArrayList
                                    paises.add(nuevoPais);
                                    // Notificar al Adapter que los datos han cambiado
                                    adapter.notifyDataSetChanged();
                                    // Establecer la selección en el nuevo país
                                    Pais.setSelection(paises.size() - 1);
                                }
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            }
        });


    }




    private void permisos() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 101);
        } else {
            dispatchTakePictureIntent();
        }
    }




    private void Tomarfoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 102);
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        Ruta = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(getApplicationContext(), "Permiso Denegado", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();

            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.PM1E17321.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 102);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == RESULT_OK) {
            // Add the image to the gallery
            galleryAddPic(Ruta);

            try {
                File fotos = new File(Ruta);
                foto.setImageURI(Uri.fromFile(fotos));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void AddPerson() {

        try {
            SQLiteConexion conexion = new SQLiteConexion(this, transacciones.namedb, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();

            ContentValues valores = new ContentValues();

            String selectedItem = Pais.getSelectedItem().toString();

            // Guardar la imagen en la base de datos


            valores.put(transacciones.telefono, phone.getText().toString());
            //valores.put(transacciones.Imagen, imageBytes);
            valores.put(transacciones.pais, selectedItem);
            valores.put(transacciones.nombres, nombres.getText().toString());
            valores.put(transacciones.nota, nota.getText().toString());

            long result = db.insert(transacciones.Tabla, null, valores);

            Intent intentcreate = new Intent(getApplicationContext(), MainActivity.class);
            Toast.makeText(this, getString(R.string.res), Toast.LENGTH_SHORT).show();
            db.close();
            startActivity(intentcreate);
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.err), Toast.LENGTH_SHORT).show();
        }
    }




}
