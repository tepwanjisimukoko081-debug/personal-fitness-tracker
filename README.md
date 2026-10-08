[README.md](https://github.com/user-attachments/files/33213600/README.md)
# Personalized Fitness Tracker

A console-based Java application that lets you create a fitness profile, log workouts, and track your progress over time. Your profile and workouts are saved to a local file, so your data is still there the next time you run the program.

## Features

- **Fitness profile**: name, age, weight, height and fitness goal (Weight Loss, Muscle Gain, General Fitness, Improve Endurance)
- **BMI calculation** with a category (Underweight / Normal / Overweight / Obese). For users under 20, the category is not shown because BMI needs age-specific interpretation.
- **Workout logging**: exercise name, duration, calories burned and steps, stamped with today's date
- **Workout history**: a numbered list of every logged workout
- **Progress summary**: totals for all-time, today, and the last 7 days
- **Persistent storage** in `fitness-data.properties` (created automatically on first run)
- **Input validation** on every field, so invalid values are rejected and you are asked again

## Requirements

- Java Development Kit (JDK) **11 or higher** (the code uses `Path.of`)
- A terminal or command prompt

Check your Java version with:

```bash
java -version
javac -version
```

## Project Structure

```
personalized-fitness-tracker/
├── src/
│   └── PersonalizedFitnessTracker.java   # Source code (User, Workout, main class)
├── .gitignore
├── LICENSE
└── README.md
```

## Setup and Run

1. **Clone the repository**

   ```bash
   git clone https://github.com/<your-username>/personalized-fitness-tracker.git
   cd personalized-fitness-tracker
   ```

2. **Compile**

   ```bash
   javac -d out src/PersonalizedFitnessTracker.java
   ```

3. **Run**

   ```bash
   java -cp out PersonalizedFitnessTracker
   ```

   Run this from the project root. The data file `fitness-data.properties` is created in the folder you run the command from.

On Java 11 or newer you can also skip the compile step and run the source file directly:

```bash
java src/PersonalizedFitnessTracker.java
```

## Usage

On the first run you are asked to create a profile. After that, the main menu appears:

```
========== MAIN MENU ==========
1. View Fitness Profile
2. Add Workout
3. View Workout History
4. View Fitness Progress (all-time, today, last 7 days)
5. Exit
===============================
```

| Option | What it does |
|--------|--------------|
| 1 | Shows your profile, BMI and BMI category |
| 2 | Prompts for exercise name, duration, calories and steps, then saves the workout |
| 3 | Lists all recorded workouts |
| 4 | Shows totals for all-time, today and the last 7 days |
| 5 | Saves and exits |

### Valid input ranges

| Field | Allowed values |
|-------|----------------|
| Age | 10 to 100 |
| Weight | above 0 and up to 500 kg |
| Height | above 50 and up to 250 cm |
| Duration | 1 to 1000 minutes |
| Calories | 0 to 10000 kcal |
| Steps | 0 to 100000 |

## Data Storage

Data is stored in a plain-text `fitness-data.properties` file. To start over with a new profile, delete that file and run the program again. The file is listed in `.gitignore` so personal data is not committed to the repository.

## Notes

- BMI is a screening measure, not a diagnosis.
- Workouts saved without a date are counted only in the all-time totals.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
