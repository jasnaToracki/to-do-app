package org.example;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class TaskManager {
    private List<Task> tasks = new ArrayList<>();  //TASK LIST
    private final String FILE_NAME = "tasks.json";  // FAJL U KOJI CEMO DA SNIMIMO TASKOVE


    public TaskManager addTask (Task task) {   //METOD KOJI DODAJE VEC NAPRAVLJEN TASK
        this.tasks.add(task);

        return this;
    }
    public List<Task> all () {
        return tasks;
    }
    public List<Task> getTasksDueToday () {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.with(LocalTime.MIN);
        LocalDateTime endOfDay = now.with(LocalTime.MAX);

        return this.getTasksBetweenDatesInclusive(startOfDay, endOfDay);
    }

    public List<Task> getOverdueTasks () {
        LocalDateTime now = LocalDateTime.now();
        List<Task> found = new ArrayList<>();

        for (Task task : tasks) {
            LocalDateTime taskDue = task.getDueDate(); // treba nam vreme taska da bismo ga uporedili sa trenutnim vremenom
            if (taskDue.isBefore(now) && !task.isDone()) {
                found.add(task);
            }
        }
        return found;
    }

    public List<Task> getTasksDueTomorrow () {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalDateTime startOfDay = tomorrow.with(LocalTime.MIN);
        LocalDateTime endOfDay = tomorrow.with(LocalTime.MAX);


        return this.getTasksBetweenDatesInclusive(startOfDay, endOfDay);
    }

    public boolean updateDone (long taskID, boolean done) {
        for (Task task : tasks) {
            if (task.getId() == taskID) {
                task.setDone(done);
                return true;
            }
        }
        return false;
    }

    public List<Task> searchForTask(String searchTerm) {
        List<Task> found = new ArrayList<>();

        for (Task task : tasks) {
            int index = task.getDescription().toLowerCase().indexOf(searchTerm.toLowerCase());
            if (index != -1) {
                found.add(task);
            }
        }
        return found;
    }
    public Task getTaskById (long taskId) {
        for (Task task : tasks) {
            if (task.getId() == taskId) {
                return task;
            }
        }
        return null;
    }
    public List<Task> searchTasksByTag (String userTagChoice) throws IOException {
        List<Task> foundTasksByTag = new ArrayList<>();

        for (Task task : tasks) {

            if (task.getTag() != null && task.getTag().equalsIgnoreCase(userTagChoice)) {
                foundTasksByTag.add(task);
            }
        }
        return foundTasksByTag;
    }

    // zapisace sve taskove koji se nalaze u spisku u fajlu
    public void save () throws IOException {
        JsonHandler.write(this.tasks, FILE_NAME);
    }
//        List<String> lines = new ArrayList<>();
//
//        for (Task task : tasks) {
//            lines.add(task.toCSV());
//        }
//
//        Path filePath = Paths.get(FILE_NAME);
//        Files.write(filePath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
//    }

    //ucitava taskove
    public void load () throws IOException {
        this.tasks = JsonHandler.read(FILE_NAME);
//        List<Task> tasks = new ArrayList<>();
//        Path filePath = Paths.get(FILE_NAME);
//
//        if (Files.exists(filePath)) {
//            List<String> lines = Files.readAllLines(filePath);
//
//            for (String line : lines) {
//                Task task = Task.fromCSV(line);
//                tasks.add(task);
//            }
//            this.tasks = tasks;
//        }
    }

    /* inclusive ---> ukljucuje i vreme koje je prosledjeno
       exclusive ---> ne ukljucuje vreme koje je prosledjeno
    */

    private List<Task> getTasksBetweenDatesInclusive (LocalDateTime startDate, LocalDateTime endDate) {
        List<Task> found = new ArrayList<>();
        for (Task task : tasks) {
            LocalDateTime taskDue = task.getDueDate(); //vreme taska

            if ( (taskDue.isEqual(startDate) || taskDue.isEqual(endDate) ||
                     taskDue.isAfter(startDate) && taskDue.isBefore(endDate)) &&
                    !task.isDone()
            )  {
                found.add(task);
            }
        }
        return found;
    }
}
