package org.example;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.text.BreakIterator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicLong;

public class Task {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private long id;
    private String description;
    private LocalDateTime dueDate;
    private String tag;
    private boolean done;


    public Task (String description, LocalDateTime dueDate, String tag) {
        this.description = description;
        this.dueDate = dueDate;
        this.id = ID_GENERATOR.incrementAndGet();
        this.done = false;
        this.tag = tag;
    }
    public Task () {}  //prazan konstruktor koji ce jackson da koristi. pravi instancu klase pa setuje vrednosti

    // metod da vidimo id svakog taska
    public long getId () {
        return this.id;
    }

    public void setId (long id) {
        this.id = id;
        ID_GENERATOR.set(id);
    }
    public String getDescription() {
        return description;
    }
    public String getDescription (int maxLenght) {
        if (description.length() <= maxLenght) {
            return description;
        }
        String shortDescription;

        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(description);
        int end= breakIterator.following(maxLenght);

        if (end == BreakIterator.DONE) {  //breakIterator.DONE znaci da ne moze da pronadje to sto trazimo
            shortDescription = description.substring(0, maxLenght);
        } else {
            shortDescription = description.substring(0, end).trim() + "..."; //(0, end) znaci da hocemo od nule do prvog razmaka
        }

        return shortDescription;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getTag () {
        return this.tag;
    }

    public String setTag (String tag) {
        this.tag = tag;
        return tag;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public Task setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;

        return this;
    }
    @JsonIgnore
    public String getDueDateFormatted () {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy. HH:mm");
        return dueDate.format(dtf);
    }

    //GET za boolean vrednost pocinje sa IS
    public boolean isDone () {
        return this.done;
    }

    //SET za done
    public Task setDone (boolean done) {
        this.done = done;

        return this;
    }
    public static Task fromCSV (String csvString) {
        String[] values = csvString.split(",");       // prolazi kroz listu stringova, parsira tekst i od toga pravi taskove
        String tag = values[3];
        Task task = new Task (values[1], LocalDateTime.parse(values[2]), tag);
        task.id = Long.parseLong(values[0]);
        ID_GENERATOR.set(Long.parseLong(values[0]));

        try {
            task.setDone(Boolean.parseBoolean(values[4]));
        } catch (IndexOutOfBoundsException e) {
            task.setDone(false);

        }
        return task;
    }
    public String toCSV () {
        return this.id + "," +
                this.description + "," +
                this.dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "," +
                this.tag + "," +
                this.done;
    }


    //da mi ne bi printao ovako: org.example.Task@45018215, org.example.Task@65d6b83b
    public String toString() {
        return "\n id #" + id +
                ", description: " + description +
                ", dueDate: " + dueDate + // ISO format!!!
                ", tag #" + tag +
                ", done --> " + done + "\n";
    }

    public void addTag(String tagChoice) {;
    }
}
