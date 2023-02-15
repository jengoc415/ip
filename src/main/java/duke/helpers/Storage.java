package duke.helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import java.time.format.DateTimeParseException;

import duke.tasks.Deadline;
import duke.exceptions.DukeException;
import duke.tasks.*;

public class Storage {
    private File savedFile;

    public Storage(File savedFile) {
        this.savedFile = savedFile;
    }

    public ArrayList<Task> populateTasks() throws IOException {
        Scanner sc;
        ArrayList<Task> tasks = new ArrayList<>();
        String command;
        String instr;
        String extendedInstr;
        String[] instrSplit;
        Task task;
        boolean completed;

        try {
            sc = new Scanner(savedFile);
            while (sc.hasNextLine()) {
                try {
                    extendedInstr = sc.nextLine();
                    // first character in instruction shows if task is completed
                    completed = Integer.parseInt(extendedInstr.substring(0, 1)) == 1 ? true : false;
                    instr = extendedInstr.substring(2);
                    instrSplit = instr.split(" ");
                    command = instrSplit[0];

                    if (command.equals("todo")) {
                        task = new Todo(instr);
                    } else if (command.equals("deadline")) {
                        task = new Deadline(instr);
                    } else {
                        task = new Event(instr);
                    }
                    tasks.add(task);

                    if (completed) {
                        task.setDone();
                    }
                } catch (DateTimeParseException dtpe) {

                } catch (DukeException e) {

                }
            }
            sc.close();
        } catch (FileNotFoundException fnfe) {
            savedFile.createNewFile();
        }
        return tasks;
    }

    public void saveTasks(String encodedTasks) {
        try {
            FileWriter fw = new FileWriter(savedFile);
            fw.write(encodedTasks);
            fw.flush();
            fw.close();
            for (int i = 0; i < 60; i++) {
                Thread.sleep(15);
                System.out.print(".");
            }
            System.out.print("\n");
            Thread.sleep(250);
            System.out.println("Your tasks have been successfully saved! :-)");
        } catch (IOException e) {
            System.out.println("Sorry, unable to save your tasks right now.");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}