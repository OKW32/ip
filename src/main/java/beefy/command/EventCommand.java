package beefy.command;

import beefy.BeefyException;
import beefy.storage.Storage;
import beefy.task.TaskList;
import beefy.task.Task;
import java.time.LocalDateTime;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a command to add events to task list.
 */
public class EventCommand implements Command {
    private TaskList userTasks;
    private String taskDescription;
    private LocalDateTime taskFrom, taskTo;

    /**
     * Constructs a new EventCommand object with user input.
     *
     * @param userTasks User list of tasks.
     * @param userParams User input of the deadline command.
     * @throws BeefyException if there is any formatting issues.
     */
    public EventCommand(TaskList userTasks, String userParams) throws BeefyException {
        this.userTasks = userTasks;
        String[] taskDetails = userParams.trim().split("/from|/to");
        if (taskDetails.length < 3) {
            LocalDateTime currDateTime = LocalDateTime.now();
            throw new BeefyException("Use format:" + System.lineSeparator()
                    + "event (Task Description) /from (DateTime) /to (DateTime)" + System.lineSeparator()
                    + "E.g: " + System.lineSeparator()
                    + "event Learn how to use event command /from "
                    + currDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) + " /to "
                    + currDateTime.plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        }
        taskDescription = taskDetails[0].trim();
        if (taskDescription.isEmpty()) {
            throw new BeefyException("Quit fooling me, I do not see any task to add!");
        }
        try {
            taskFrom = LocalDateTime.parse(taskDetails[1].trim());
            taskTo = LocalDateTime.parse(taskDetails[2].trim());
        } catch (DateTimeParseException e) {
            LocalDateTime currDateTime = LocalDateTime.now();
            throw new BeefyException("Use format: " + System.lineSeparator()
                    + "event (Task Description) /from (DateTime) /to (DateTime)" + System.lineSeparator()
                    + "E.g: " + System.lineSeparator()
                    + "event Learn how to use event command /from "
                    + currDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) + " /to "
                    + currDateTime.plusHours(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        }
    }

    /**
     * Executes the event command, adding the event task into user task list, saving data in text file.
     *
     * @throws IOException if an IO error occurs.
     */
    @Override
    public void execute() throws IOException {
        Task addedTask = userTasks.addTask(taskDescription, taskFrom, taskTo, false);
        Storage.addToDisk(addedTask.toDiskFormat());
    }

    /**
     * Indicates whether this is an exit command.
     * Returns false since this is not an exit command.
     *
     * @return false.
     */
    @Override
    public boolean isExit() {
        return false;
    }
}
