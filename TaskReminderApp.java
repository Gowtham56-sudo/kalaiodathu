import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;
import java.util.List;

public class TaskReminderApp {
    private Frame mainFrame;
    private TextField taskField;
    private TextField timeField;
    private final List<Task> tasks;
    private java.awt.List taskListDisplay;
    private Timer timer;

    public TaskReminderApp() {
        tasks = new ArrayList<>();
        prepareGUI();
    }

    public static void main(String[] args) {
        TaskReminderApp app = new TaskReminderApp();
        app.showEventDemo();
    }

    private void prepareGUI() {
        mainFrame = new Frame("Task Reminder App");
        mainFrame.setSize(400, 400);
        mainFrame.setLayout(new FlowLayout());
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        Label taskLabel = new Label("Task:");
        taskField = new TextField(20);

        Label timeLabel = new Label("Time (HH:mm):");
        timeField = new TextField(10);

        Button addButton = new Button("Add Task");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        taskListDisplay = new java.awt.List(10); // Display up to 10 tasks
        taskListDisplay.setSize(300, 150);

        mainFrame.add(taskLabel);
        mainFrame.add(taskField);
        mainFrame.add(timeLabel);
        mainFrame.add(timeField);
        mainFrame.add(addButton);
        mainFrame.add(new Label("Tasks:"));
        mainFrame.add(taskListDisplay);

        mainFrame.setVisible(true);

        startTimer();
    }

    private void showEventDemo() {
        // GUI is already displayed in prepareGUI
    }

    private void addTask() {
        String taskName = taskField.getText();
        String time = timeField.getText();

        if (taskName.isEmpty() || time.isEmpty()) {
            showAlert("Error", "Task and time cannot be empty!");
            return;
        }

        try {
            String[] timeParts = time.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            Task task = new Task(taskName, calendar.getTime());
            tasks.add(task);

            // Add task to the list display
            taskListDisplay.add(taskName + " at " + time);

            showAlert("Success", "Task added successfully!");
        } catch (Exception ex) {
            showAlert("Error", "Invalid time format. Use HH:mm.");
        }
    }

    private void startTimer() {
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkTasks();
            }
        });
        timer.start();
    }

    private void checkTasks() {
        Date now = new Date();
        Iterator<Task> iterator = tasks.iterator();

        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (now.after(task.getTime())) {
                showAlert("Reminder", "Time for: " + task.getName());
                iterator.remove();

                // Remove task from the list display
                for (int i = 0; i < taskListDisplay.getItemCount(); i++) {
                    if (taskListDisplay.getItem(i).startsWith(task.getName())) {
                        taskListDisplay.remove(i);
                        break;
                    }
                }
            }
        }
    }

    private void showAlert(String title, String message) {
        Dialog dialog = new Dialog(mainFrame, title, true);
        dialog.setSize(300, 150);
        dialog.setLayout(new FlowLayout());

        Label messageLabel = new Label(message);
        Button okButton = new Button("OK");
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(false);
            }
        });

        dialog.add(messageLabel);
        dialog.add(okButton);
        dialog.setVisible(true);
    }

    class Task {
        private String name;
        private Date time;

        public Task(String name, Date time) {
            this.name = name;
            this.time = time;
        }

        public String getName() {
            return name;
        }

        public Date getTime() {
            return time;
        }
    }
}