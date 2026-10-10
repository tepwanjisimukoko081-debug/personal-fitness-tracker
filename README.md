# Personalized Fitness Tracker

A console-based Java application that lets you create a fitness profile, log workouts, and track your progress against a personal goal. Your data is saved to a local file, so it is still there the next time you run the program.

## Features

- **Register / profile**: name, age, weight, height and fitness goal (Weight Loss, Muscle Gain, General Fitness, Improve Endurance)
- **Update profile**: change your goal, weight, height or age at any time
- **BMI calculation** with a category (Underweight / Normal / Overweight / Obese). For users under 20 the category is not shown because BMI needs age-specific interpretation.
- **Workout logging**: exercise name, duration, calories burned and steps, stamped with today's date
- **Workout history** and **delete a workout** (with confirmation) to fix mistakes
- **Progress summary**: totals for all-time, today and the last 7 days
- **Goal check**: compares your last 7 days of activity with a simple weekly target for your goal
- **Persistent storage** in `fitness-data.properties` (created automatically on first run)
- **Input validation** on every field, so invalid values are rejected and you are asked again

## Requirements

- Java Development Kit (JDK) **11 or higher**
- A terminal or command prompt

Check your Java version with:

```
java -version
javac -version
```

## Project Structure

```
personal-fitness-tracker/
├── src/
│   └── PersonalizedFitnessTracker.java   # Source code (main class, User, Workout)
├── .gitignore
├── LICENSE
└── README.md
```

## Setup and Run

1. **Clone the repository**

```
git clone https://github.com/tepwanjisimukoko081-debug/personal-fitness-tracker.git
cd personal-fitness-tracker
```

2. **Compile**

```
javac -d out src/PersonalizedFitnessTracker.java
```

3. **Run** (from the project root)

```
java -cp out PersonalizedFitnessTracker
```

The data file `fitness-data.properties` is created in the folder you run the command from.

Alternatively, skip the compile step and run the source file directly:

```
java src/PersonalizedFitnessTracker.java
```

## Usage

On the first run you are asked to create a profile. After that, the main menu appears:

```
========== MAIN MENU ==========
1. View Fitness Profile
2. Update Profile / Fitness Goal
3. Add Workout
4. View Workout History
5. Delete a Workout
6. View Fitness Progress (all-time, today, last 7 days)
7. Exit
===============================
```

| Option | What it does |
| ------ | ------------ |
| 1 | Shows your profile, BMI and BMI category |
| 2 | Opens a submenu to change your goal, weight, height or age |
| 3 | Prompts for exercise name, duration, calories and steps, then saves the workout |
| 4 | Lists all recorded workouts |
| 5 | Deletes a chosen workout after confirmation |
| 6 | Shows totals for all-time, today and the last 7 days, plus a goal check |
| 7 | Saves and exits |

### Weekly targets used in the goal check

| Goal | Weekly activity target |
| ---- | ---------------------- |
| Weight Loss | 250 minutes |
| Muscle Gain | 180 minutes |
| General Fitness | 150 minutes |
| Improve Endurance | 200 minutes |

These are simple guideline values for a student project, not medical advice.

### Valid input ranges

| Field | Allowed values |
| ----- | -------------- |
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
- If the data file is damaged, the program shows an error and asks you to fix or delete it.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
