package com.example.pm1e17321.config;

public class transacciones {

    //Nombre de la base de datos
    public static final String namedb = "PM1E17321";

    //Tablas de la base de datos
    public static final String Tabla = "contactos";

    //Campos de la Tabla

    public static final String id = "id";

    public static final String nombres = "nombres";

    //public static final String Imagen= "Imagen";

    public static final String pais = "pais";

    public static final String telefono = "telefono";

    public static final String nota = "nota";


    //Consultas de Base De Datos


    public static final String SelectTablePersonas = "SELECT * FROM contactos";

    public static final String CreateTablePesonas = "CREATE TABLE contactos " +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "pais TEXT, " +
            "nombres TEXT, " +
            "telefono INTEGER, " +
            "nota TEXT " +
            ")";

    public static final String DropTablePersonas = "DROP TABLE IF EXISTS contactos";








}