import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Scanner;

// ================= MAIN CLASS =================

public class PersonalizedFitnessTracker {

    private static final Path DATA_FILE = Path.of("fitness-data.properties");

    private static final String[] GOALS = {
        "Weight Loss", "Muscle Gain", "General Fitness", "Improve Endurance"
    };

    // Simple weekly activity targets (minutes) for each goal, in the same order as GOALS.
    private static final int[] WEEKLY_MINUTE_TARGETS = {250, 180, 150, 200};

    private static final Scanner scanner = new Scanner(System.in);
    private static User user;
    private static final ArrayList<Workout> workouts = new ArrayList<>();

    public static void main(String[] args) {

        System.out.println("==========================================");
        System.out.println("      PERSONALIZED FITNESS TRACKER");
        System.out.println("==========================================");

        try {
            if (loadData()) {
                System.out.println("Welcome back, " + user.getName()
                        + "! Saved profile and workouts loaded.");
            } else {
                System.out.println("No saved profile found. Let's register you.");
                createProfile();
                saveData();
            }
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Could not load saved data: " + e.getMessage());
            System.out.println("Fix or delete " + DATA_FILE + " and try again.");
            return;
        }

        int choice;

        do {
            displayMenu();
            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    user.displayProfile();
                    break;
                case 2:
                    updateProfile();
                    break;
                case 3:
                    addWorkout();
                    break;
                case 4:
                    viewWorkoutHistory();
                    break;
                case 5:
                    deleteWorkout();
                    break;
                case 6:
                    viewProgress();
                    break;
                case 7:
                    saveData();
                    System.out.println("\nData saved. Thank you for using "
                            + "Personalized Fitness Tracker!");
                    break;
                default:
                    System.out.println("\nInvalid choice. "
                            + "Please select a number from 1 to 7.");
            }

        } while (choice != 7);

        scanner.close();
    }

    // ================= MENU =================

    private static void displayMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("1. View Fitness Profile");
        System.out.println("2. Update Profile / Fitness Goal");
        System.out.println("3. Add Workout");
        System.out.println("4. View Workout History");
        System.out.println("5. Delete a Workout");
        System.out.println("6. View Fitness Progress (all-time, today, last 7 days)");
        System.out.println("7. Exit");
        System.out.println("===============================");
    }

    // ================= PROFILE =================

    private static void createProfile() {
        System.out.println("\n========== CREATE PROFILE ==========");

        String name;
        while (true) {
            name = readText("Enter your name: ");
            if (!name.isEmpty()) {
                break;
            }
            System.out.println("Name cannot be empty.");
        }

        int age = readAge();
        double weight = readWeight();
        double height = readHeight();
        String goal = chooseGoal();

        user = new User(name, age, weight, height, goal);
        System.out.println("\nProfile created successfully!");
    }

    private static void updateProfile() {
        int choice;

        do {
            System.out.println("\n========== UPDATE PROFILE ==========");
            System.out.println("1. Change fitness goal");
            System.out.println("2. Update weight");
            System.out.println("3. Update height");
            System.out.println("4. Update age");
            System.out.println("5. Back to main menu");
            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1:
                    user.setFitnessGoal(chooseGoal());
                    saveData();
                    System.out.println("Goal updated to: " + user.getFitnessGoal());
                    break;
                case 2:
                    user.setWeight(readWeight());
                    saveData();
                    System.out.println("Weight updated.");
                    break;
                case 3:
                    user.setHeight(readHeight());
                    saveData();
                    System.out.println("Height updated.");
                    break;
                case 4:
                    user.setAge(readAge());
                    saveData();
                    System.out.println("Age updated.");
                    break;
                case 5:
                    break;
                default:
                    System.out.println("Please select a number from 1 to 5.");
            }
        } while (choice != 5);
    }

    private static String chooseGoal() {
        System.out.println("\nSelect your fitness goal:");
        for (int i = 0; i < GOALS.length; i++) {
            System.out.println((i + 1) + ". " + GOALS[i]);
        }

        int option = readIntInRange("Choose an option: ", 1, GOALS.length,
                "Please select an option from 1 to " + GOALS.length + ".");

        return GOALS[option - 1];
    }

    private static int readAge() {
        return readIntInRange("Enter your age: ", 10, 100,
                "Please enter an age between 10 and 100.");
    }

    private static double readWeight() {
        return readDoubleInRange("Enter your weight (kg): ", 0, 500,
                "Please enter a weight above 0 and up to 500 kg.");
    }

    private static double readHeight() {
        return readDoubleInRange("Enter your height (cm): ", 50, 250,
                "Please enter a height above 50 and up to 250 cm.");
    }

    // ================= WORKOUTS =================

    private static void addWorkout() {
        System.out.println("\n========== ADD WORKOUT ==========");

        String exerciseName;
        while (true) {
            exerciseName = readText("Enter exercise name: ");
            if (!exerciseName.isEmpty()) {
                break;
            }
            System.out.println("Exercise name cannot be empty.");
        }

        int duration = readIntInRange("Enter duration (minutes): ", 1, 1000,
                "Please enter a duration from 1 to 1000 minutes.");
        int calories = readIntInRange("Enter calories burned: ", 0, 10000,
                "Please enter calories from 0 to 10000.");
        int steps = readIntInRange("Enter steps completed: ", 0, 100000,
                "Please enter steps from 0 to 100000.");

        workouts.add(new Workout(exerciseName, duration, calories, steps,
                LocalDate.now()));
        saveData();

        System.out.println("\nWorkout added successfully!");
    }

    private static void viewWorkoutHistory() {
        System.out.println("\n========== WORKOUT HISTORY ==========");

        if (workouts.isEmpty()) {
            System.out.println("No workouts have been recorded yet.");
            return;
        }

        printWorkoutList();
        System.out.println("=====================================");
    }

    private static void printWorkoutList() {
        for (int i = 0; i < workouts.size(); i++) {
            System.out.print((i + 1) + ". ");
            workouts.get(i).displayWorkout();
        }
    }

    private static void deleteWorkout() {
        System.out.println("\n========== DELETE WORKOUT ==========");

        if (workouts.isEmpty()) {
            System.out.println("No workouts to delete.");
            return;
        }

        printWorkoutList();

        int number = readIntInRange(
                "Enter the number of the workout to delete (0 to cancel): ",
                0, workouts.size(),
                "Please enter a number from 0 to " + workouts.size() + ".");

        if (number == 0) {
            System.out.println("Cancelled.");
            return;
        }

        String confirm = readText("Delete workout " + number + "? (y/n): ");

        if (confirm.equalsIgnoreCase("y")) {
            workouts.remove(number - 1);
            saveData();
            System.out.println("Workout deleted.");
        } else {
            System.out.println("Nothing was deleted.");
        }
    }

    // ================= PROGRESS =================

    private static void viewProgress() {
        System.out.println("\n========== FITNESS PROGRESS ==========");

        if (workouts.isEmpty()) {
            System.out.println("No workout data available.");
            return;
        }

        long totalMinutes = 0, totalCalories = 0, totalSteps = 0;
        int todayCount = 0, weekCount = 0;
        long todayMinutes = 0, todayCalories = 0, todaySteps = 0;
        long weekMinutes = 0, weekCalories = 0, weekSteps = 0;

        LocalDate today = LocalDate.now();
        LocalDate weekStart = today.minusDays(6);
        boolean hasUndated = false;

        for (Workout w : workouts) {
            totalMinutes += w.getDuration();
            totalCalories += w.getCaloriesBurned();
            totalSteps += w.getSteps();

            LocalDate d = w.getDate();
            if (d == null) {
                hasUndated = true;
                continue;
            }

            if (d.equals(today)) {
                todayCount++;
                todayMinutes += w.getDuration();
                todayCalories += w.getCaloriesBurned();
                todaySteps += w.getSteps();
            }

            if (!d.isBefore(weekStart) && !d.isAfter(today)) {
                weekCount++;
                weekMinutes += w.getDuration();
                weekCalories += w.getCaloriesBurned();
                weekSteps += w.getSteps();
            }
        }

        System.out.println("----- All-Time -----");
        System.out.println("Total Workouts : " + workouts.size());
        System.out.println("Total Duration : " + totalMinutes + " minutes");
        System.out.println("Total Calories : " + totalCalories + " kcal");
        System.out.println("Total Steps    : " + totalSteps);

        System.out.println("----- Today (" + today + ") -----");
        printPeriodTotals(todayCount, todayMinutes, todayCalories, todaySteps);

        System.out.println("----- Last 7 Days -----");
        System.out.println("Period         : " + weekStart + " to " + today);
        printPeriodTotals(weekCount, weekMinutes, weekCalories, weekSteps);

        if (hasUndated) {
            System.out.println("Some older saved workouts have no date and are "
                    + "included only in all-time totals.");
        }

        printGoalCheck(weekMinutes);
        System.out.println("======================================");
    }

    private static void printGoalCheck(long weekMinutes) {
        String goal = user.getFitnessGoal();
        int target = 150;

        for (int i = 0; i < GOALS.length; i++) {
            if (GOALS[i].equals(goal)) {
                target = WEEKLY_MINUTE_TARGETS[i];
            }
        }

        long percent = weekMinutes * 100 / target;

        System.out.println("----- Goal Check -----");
        System.out.println("Fitness Goal   : " + goal);
        System.out.println("Weekly Target  : " + target + " minutes of activity");
        System.out.println("This Week      : " + weekMinutes + " minutes ("
                + percent + "% of target)");

        if (weekMinutes >= target) {
            System.out.println("Great work! You have reached your weekly target.");
        } else {
            System.out.println("Keep going! " + (target - weekMinutes)
                    + " more minutes to reach your weekly target.");
        }
    }

    private static void printPeriodTotals(int workoutCount, long minutes,
                                          long calories, long steps) {
        System.out.println("Workouts       : " + workoutCount);
        System.out.println("Duration       : " + minutes + " minutes");
        System.out.println("Calories       : " + calories + " kcal");
        System.out.println("Steps          : " + steps);
    }

    // ================= SAVE / LOAD =================

    private static void saveData() {
        Properties data = new Properties();

        data.setProperty("user.name", user.getName());
        data.setProperty("user.age", Integer.toString(user.getAge()));
        data.setProperty("user.weight", Double.toString(user.getWeight()));
        data.setProperty("user.height", Double.toString(user.getHeight()));
        data.setProperty("user.goal", user.getFitnessGoal());
        data.setProperty("workout.count", Integer.toString(workouts.size()));

        for (int i = 0; i < workouts.size(); i++) {
            Workout w = workouts.get(i);
            String prefix = "workout." + i + ".";

            data.setProperty(prefix + "name", w.getExerciseName());
            data.setProperty(prefix + "duration", Integer.toString(w.getDuration()));
            data.setProperty(prefix + "calories", Integer.toString(w.getCaloriesBurned()));
            data.setProperty(prefix + "steps", Integer.toString(w.getSteps()));
            if (w.getDate() != null) {
                data.setProperty(prefix + "date", w.getDate().toString());
            }
        }

        try (OutputStream output = Files.newOutputStream(DATA_FILE)) {
            data.store(output, "Fitness tracker saved data");
        } catch (IOException e) {
            System.out.println("Could not save data: " + e.getMessage());
        }
    }

    private static boolean loadData() throws IOException {
        if (!Files.exists(DATA_FILE)) {
            return false;
        }

        Properties data = new Properties();

        try (InputStream input = Files.newInputStream(DATA_FILE)) {
            data.load(input);
        }

        try {
            user = new User(
                    requiredProperty(data, "user.name"),
                    Integer.parseInt(requiredProperty(data, "user.age")),
                    Double.parseDouble(requiredProperty(data, "user.weight")),
                    Double.parseDouble(requiredProperty(data, "user.height")),
                    requiredProperty(data, "user.goal"));

            int count = Integer.parseInt(requiredProperty(data, "workout.count"));
            if (count < 0) {
                throw new IllegalArgumentException("Invalid workout count.");
            }

            workouts.clear();

            for (int i = 0; i < count; i++) {
                String prefix = "workout." + i + ".";
                String savedDate = data.getProperty(prefix + "date");
                LocalDate date = savedDate == null ? null : LocalDate.parse(savedDate);

                workouts.add(new Workout(
                        requiredProperty(data, prefix + "name"),
                        Integer.parseInt(requiredProperty(data, prefix + "duration")),
                        Integer.parseInt(requiredProperty(data, prefix + "calories")),
                        Integer.parseInt(requiredProperty(data, prefix + "steps")),
                        date));
            }
        } catch (RuntimeException e) {
            // Covers number-format and date-format errors in a damaged file.
            throw new IllegalArgumentException(
                    "Saved data is damaged (" + e.getMessage() + ").");
        }

        return true;
    }

    private static String requiredProperty(Properties data, String key) {
        String value = data.getProperty(key);

        if (value == null) {
            throw new IllegalArgumentException("Saved data is missing: " + key);
        }

        return value;
    }

    // ================= INPUT HELPERS =================

    private static String readText(String message) {
        System.out.print(message);

        try {
            return scanner.nextLine().trim();
        } catch (NoSuchElementException e) {
            // Input stream closed (e.g. Ctrl+D): save and exit cleanly.
            System.out.println("\nInput ended. Saving and exiting.");
            if (user != null) {
                saveData();
            }
            System.exit(0);
            return "";
        }
    }

    private static int readInt(String message) {
        while (true) {
            String input = readText(message);

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a whole number.");
            }
        }
    }

    private static double readDouble(String message) {
        while (true) {
            String input = readText(message);

            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private static int readIntInRange(String message, int min, int max,
                                      String errorMessage) {
        while (true) {
            int value = readInt(message);

            if (value >= min && value <= max) {
                return value;
            }

            System.out.println(errorMessage);
        }
    }

    // The value must be greater than min and no more than max.
    private static double readDoubleInRange(String message, double min,
                                            double max, String errorMessage) {
        while (true) {
            double value = readDouble(message);

            if (value > min && value <= max) {
                return value;
            }

            System.out.println(errorMessage);
        }
    }
}

// ================= USER CLASS =================

class User {

    private String name;
    private int age;
    private double weight;
    private double height;
    private String fitnessGoal;

    public User(String name, int age, double weight,
                double height, String fitnessGoal) {
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.height = height;
        this.fitnessGoal = fitnessGoal;
    }

    public double calculateBMI() {
        double heightInMeters = height / 100;
        return weight / (heightInMeters * heightInMeters);
    }

    public String getBmiCategory() {
        double bmi = calculateBMI();

        if (age < 20) {
            return "Not displayed (under-20 BMI needs age-specific interpretation)";
        } else if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25) {
            return "Normal";
        } else if (bmi < 30) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    public String getName() { return name; }
    public int getAge() { return age; }
    public double getWeight() { return weight; }
    public double getHeight() { return height; }
    public String getFitnessGoal() { return fitnessGoal; }

    public void setAge(int age) { this.age = age; }
    public void setWeight(double weight) { this.weight = weight; }
    public void setHeight(double height) { this.height = height; }
    public void setFitnessGoal(String fitnessGoal) { this.fitnessGoal = fitnessGoal; }

    public void displayProfile() {
        System.out.println("\n=====================================");
        System.out.println("         FITNESS PROFILE");
        System.out.println("=====================================");
        System.out.println("Name          : " + name);
        System.out.println("Age           : " + age);
        System.out.println("Weight        : " + weight + " kg");
        System.out.println("Height        : " + height + " cm");
        System.out.println("Fitness Goal  : " + fitnessGoal);
        System.out.printf("BMI           : %.2f%n", calculateBMI());
        System.out.println("BMI Category  : " + getBmiCategory());
        System.out.println("BMI is a screening measure, not a diagnosis.");
        System.out.println("=====================================");
    }
}

// ================= WORKOUT CLASS =================

class Workout {

    private final String exerciseName;
    private final int duration;
    private final int caloriesBurned;
    private final int steps;
    private final LocalDate date;

    public Workout(String exerciseName, int duration,
                   int caloriesBurned, int steps, LocalDate date) {
        this.exerciseName = exerciseName;
        this.duration = duration;
        this.caloriesBurned = caloriesBurned;
        this.steps = steps;
        this.date = date;
    }

    public String getExerciseName() { return exerciseName; }
    public int getDuration() { return duration; }
    public int getCaloriesBurned() { return caloriesBurned; }
    public int getSteps() { return steps; }
    public LocalDate getDate() { return date; }

    public void displayWorkout() {
        System.out.println(
                "Date: " + (date == null ? "Unknown" : date)
                + " | Exercise: " + exerciseName
                + " | Duration: " + duration + " min"
                + " | Calories: " + caloriesBurned + " kcal"
                + " | Steps: " + steps);
    }
}
