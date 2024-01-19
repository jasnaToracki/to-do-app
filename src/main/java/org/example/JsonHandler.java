package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
u ovoj klasi cemo imati dva metoda:
 jedan koji ce da zapisuje u fajl nase taskove
 drugi koji ce da ih ucitava i sortira za nas
 */
public class JsonHandler {

    /*pravimo metod koji snima, ima dva parametra -> spisak taskova i ime fajla koji ce da snima
    jackson object mapper - mapirace klasu taskova, pretvorice je u odgovarajuci format i da snimiti u json fajl
    */
    public static void write (List<Task> tasks, String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules(); //da bi jackson shvatio da smo dodali modul, koristimo metod findAndRegisterModules()
        File file = new File(fileName);

        mapper.writeValue(file, tasks);
    }

    public static List<Task> read (String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        File file = new File(fileName);

        List<Task> tasks = null;
        try {
            tasks = mapper.readValue(file, new TypeReference<List<Task>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return tasks;

        /* readValue uzima dva parametra:
        1. file objekat - iz kog fajla ce se citati
        2. u koju klasu ce da pretvara to sto procita
         */
    }
}
