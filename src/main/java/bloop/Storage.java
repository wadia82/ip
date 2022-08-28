package bloop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Deals with files.
 */
public class Storage {

    private String filePath;
    private Ui ui;

    /**
     * Constructor for Storage object.
     *
     * @param filePath File path.
     * @param ui Ui object.
     */
    public Storage(String filePath, Ui ui) {
        this.filePath = filePath;
        this.ui = ui;
    }

    /**
     * Creates a file if not already created.
     *
     * @param tasks List of tasks.
     */
    public void makeFile(ArrayList<Task> tasks) {
        try {
            File file = new File(filePath);
            boolean isCreated = file.createNewFile();
            if (!isCreated) {
                addPrevTasks(tasks);
            }
        } catch (IOException e) {
            ui.print("Couldn't create file. Sorry.");
            System.exit(0);
        }
    }

    /**
     * Adds tasks to the file.
     *
     * @param task Task to be added to the file.
     * @throws IOException If there is a problem writing to the file.
     */
    public void writeToFile(Task task) throws IOException {
        FileWriter fw = new FileWriter(filePath, true);
        fw.write(task.getType() + "-"
                + (task.getStatus() ? "1" : "0") + "-" + task.getTask() + "-" + task.getBy() + "\n");
        fw.close();
    }

    /**
     * Updates the file with the changed list.
     *
     * @param tasks List of tasks.
     * @throws IOException If there is a problem writing to the file.
     */
    public void rewriteFile(ArrayList<Task> tasks) throws IOException {
        FileWriter fw = new FileWriter(filePath);
        fw.write("");
        for (Task task : tasks) {
            writeToFile(task);
        }
        fw.close();
    }

    private void addPrevTasks(ArrayList<Task> tasks) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner sc = new Scanner(file);
        while (sc.hasNext()) {
            String[] taskArr = sc.nextLine().split("-");
            String type = taskArr[0];
            Task task;
            if (type.equals("T")) {
                task = new ToDo(taskArr[2]);
            } else if (type.equals("E")) {
                task = new Event(taskArr[2], taskArr[3]);
            } else {
                task = new Deadline(taskArr[2], taskArr[3]);
            }
            if (Integer.parseInt(taskArr[1]) == 1) {
                task.mark();
            }
            tasks.add(task);
        }
    }
}
