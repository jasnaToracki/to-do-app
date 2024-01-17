package org.example;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final TaskManager taskManager = new TaskManager();

    public static void main(String[] args) throws IOException {

        try {
            taskManager.load();
        } catch (IOException e) {

        }

        int choice;
        do {
            displayOptions();
            choice = getUserChoice();

            switch (choice) {
                case 1:
                    try {
                        addNewTask();
                    } catch (IOException e) {
                        System.out.println("\n---\nError saving tasks to file!\n---\n");
                    }
                    break;
                case 2:
                    displayTasks(taskManager.getTasksDueToday());
                    break;
                case 3:
                    displayTasks(taskManager.all());
                    break;
                case 4:
                    displayTasks(taskManager.getOverdueTasks());
                    break;
                case 5:
                    displayTasks(taskManager.getTasksDueTomorrow());
                case 6:
                    markTaskAsDone();
                    break;
                case 7:
                    markTaskAsNotDone();
                    break;
                case 8:
                    displayTasks(searchForTask());
                    break;
                case 9:
                    showTaskDetails();
                    break;
                case 10:
                    changeTaskStatus();
                    break;
                case 11:
                    changeTaskStatus2();
                    break;
                case 12:
                    changeTaskStatus3();
                    break;
                case 13:
                    changeTaskStatus4();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Invalid choice. Please enter valid option.  ");
            }
        } while (choice != 0);
    }

    private static void displayOptions() {
        System.out.println("Possible options:");
        System.out.println("1. Add new task: ");
        System.out.println("2. Show today's tasks: ");
        System.out.println("3. Show all tasks: ");
        System.out.println("4. Show overdue tasks: ");
        System.out.println("5. Show tomorrow's tasks: ");
        System.out.println("6. Mark task as DONE");
        System.out.println("7. Mark task as NOT DONE");
        System.out.println("8. Search for a task: ");
        System.out.println("9. Show task details: ");
        System.out.println("10. Change task status with Integer (DONE or NOT DONE): ");
        System.out.println("11. Change task status with String (DONE or NOT DONE): ");
        System.out.println("12. Change task status using Switch (DONE or NOT DONE): ");
        System.out.println("13. Change task status using Loop (DONE or NOT DONE)");
        System.out.println("0. Exit");
        System.out.println("------------------");
    }

    private static int getUserChoice() {
        System.out.print("Enter your choice: ");
        String choice = scanner.nextLine();

        return Integer.parseInt(choice);
    }

    private static void addNewTask() throws IOException {
        System.out.println("\n *** ADD NEW TASK***");
        System.out.print("TASK DESCRIPTION: ");
        String description = scanner.nextLine();
        System.out.print("TASK DUE DATE (yyyy-MM-dd): ");
        String dueDateString = scanner.nextLine();
        System.out.print("TASK DUE TIME: (HH:mm:ss): ");
        String dueTimeString = scanner.nextLine();

        LocalDate dueDate = LocalDate.parse(dueDateString);
        LocalTime dueTime = LocalTime.parse(dueTimeString);

        // pravi ono sto cemo proslediti tasku
        LocalDateTime due = LocalDateTime.of(dueDate, dueTime);

        //pravi novu instancu klase TASK
        Task newTask = new Task(description, due);

        //dodaje novi task u taskManager
        taskManager.addTask(newTask).save();
        //taskManager.save();
    }

    private static void displayTasks(List<Task> tasks) {
        if (!tasks.isEmpty()) {
            System.out.println("\n ------------------- \n");
        }

        for (Task task : tasks) {
            System.out.print("#" + task.getId());
            System.out.print(" " + task.getDescription(25));
            System.out.print(", Due: " + task.getDueDateFormatted());

            if (task.isDone()) {
                System.out.print(" ----D O N E!");
            } else {
                System.out.println(" ----NOT DONE");
            }
            System.out.println();
        }

        if (!tasks.isEmpty()) {
            System.out.println("\n ------------------- \n");
        }
        if (tasks.isEmpty()) {
            System.out.println("\n NO TASKS FOUND!!!\n");
        }

    }

    private static void markTaskAsDone() {
        System.out.print("Enter ID of the task you want to mark as DONE: ");
        String taskID = scanner.nextLine();

        if (taskManager.updateDone(Long.parseLong(taskID), true)) {
            try {
                taskManager.save();
            } catch (IOException e) {
                System.out.println("Error saving task.");
            }
        }
    }

    private static void markTaskAsNotDone() throws IOException {
        System.out.println("Enter ID of the task you want to mark as NOT DONE");
        String taskID = scanner.nextLine();

        if (taskManager.updateDone(Long.parseLong(taskID), false)) {
            try {
                taskManager.save();
            } catch (IOException e) {
                System.out.println("Error saving task.");
            }
        }
    }

    private static List<Task> searchForTask() {
        System.out.println("\nEnter search term: ");
        String searchTerm = scanner.nextLine();

        return taskManager.searchForTask(searchTerm);

    }

    private static void showTaskDetails() {
        System.out.print("Enter ID of the task you want to view: ");
        String taskId = scanner.nextLine();
        Task task = taskManager.getTaskById(Long.parseLong(taskId));

        if (task == null) {
            System.out.println("\n TASK WITH ENTERED ID CAN NOT BE FOUND! \n");
            return;
        }

        System.out.println("\n-----------------------------\n");
        System.out.println("Task ID:      " + task.getId());
        System.out.println("Description:  " + task.getDescription());
        System.out.println("Due date:     " + task.getDueDateFormatted());
        System.out.println("Is done:      " + task.isDone());
        System.out.println("\n-----------------------------\n");
    }

    private static int getUserStatusChoice() {
        String choice = scanner.nextLine();
        return Integer.parseInt(choice);
    }

    public static void changeTaskStatus() {
        System.out.print("Enter ID of the task you want to change the status: ");
        String taskId = scanner.nextLine();
        Task task = taskManager.getTaskById(Long.parseLong(taskId));

        if (task != null) {
            System.out.println("Task is found! To mark task as DONE enter: 1, and for NOT DONE enter: 0 ");

            int userStatus = getUserStatusChoice();

            if (userStatus == 1) {
                try {
                    taskManager.updateDone(Long.parseLong(taskId), true);
                    taskManager.save();
                } catch (IOException e) {
                    System.out.println("Error update.");
                }
            } else if (userStatus == 0) {
                try {
                    taskManager.updateDone(Long.parseLong(taskId), false);
                    taskManager.save();
                } catch (IOException e) {
                    System.out.println("Error update.");
                }
            }
        } else {
            System.out.println("No task found!");
        }
    }

    private static String getUserChoice2() {
        return scanner.nextLine();
    }

    private static void changeTaskStatus2() {
        System.out.print("Enter ID of the task you want to change the status: ");
        String taskId = scanner.nextLine();
        Task task = taskManager.getTaskById(Long.parseLong(taskId));

        if (task != null) {
            System.out.println("Task is found! To change task status enter DONE or NOT DONE: ");

            String userChoice = getUserChoice2();

            if ("done".toLowerCase().equals(userChoice)) {
                try {
                    taskManager.updateDone(Long.parseLong(taskId), true);
                    taskManager.save();
                } catch (IOException e) {
                    System.out.println("Error update.");
                }
            } else if ("not done".toLowerCase().equals(userChoice)) {
                try {
                    taskManager.updateDone(Long.parseLong(taskId), false);
                    taskManager.save();
                } catch (IOException e) {
                    System.out.println("Error update.");
                }
            }
        }
    }
    private static void changeTaskStatus3() throws IOException {
        System.out.print("Enter ID of the task you want to change the status: ");
        String taskId = scanner.nextLine();
        Task task = taskManager.getTaskById(Long.parseLong(taskId));

        if (task != null) {
            System.out.println("Task is found! To change task status enter DONE or NOT DONE: ");

            String userChoice = getUserChoice2();

            switch (userChoice.toLowerCase()) {
                case "done":
                    taskManager.updateDone(Long.parseLong(taskId), true);
                    taskManager.save();
                    break;
                case "not done":
                    taskManager.updateDone(Long.parseLong(taskId), false);
                    taskManager.save();
                    break;
                default:
                    System.out.println("Error update.");
            }
        }
    }
    private static void changeTaskStatus4 () throws IOException {

        Task task;
        do {
            System.out.print("Enter ID of the task you want to change the status: ");
            String taskId = scanner.nextLine();
            task = taskManager.getTaskById(Long.parseLong(taskId));
            System.out.print("Task is found. Enter done or not done to change the status: ");
            String userChoice = getUserChoice2();

            if ("done".toLowerCase().equals(userChoice)) {
                taskManager.updateDone(Long.parseLong(taskId), true);
                taskManager.save();
                break;
            } else if ("not done".toLowerCase().equals(userChoice)) {
                taskManager.updateDone(Long.parseLong(taskId), false);
                taskManager.save();
                break;
            } else {
                System.out.println("No task found!");
            }

        } while (task != null);
    }
}