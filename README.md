# DriveSweep

A Java CLI tool for managing and clearing storage on Windows.

## Features

- **Storage Analysis**
  - Scan directories for large files
  - Find duplicate files (using hash comparison)
  - Identify old/unused files (based on access/modification dates)
  - Visualize disk usage (text-based visualization)

- **Storage Cleanup**
  - Clean temporary files (Windows temp folders, browser caches, etc.)
  - Secure file deletion (overwrite before delete)
  - Remove residual files left behind after uninstalling applications
  - Clean tool's own temporary files after operations

- **User Interaction**
  - Interactive prompts before deletion operations
  - Permission handling for admin-required operations
  - Progress indicators for long-running operations

- **Reporting**
  - Generate reports of disk usage
  - Track files deleted and space saved
  - Export reports in text/CSV format

## Requirements

- Java 17 or higher
- Windows operating system

## Installation

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/drivesweep.git
   cd drivesweep
   ```

2. Build the project with Maven:
   ```
   mvn clean package
   ```

3. The executable JAR will be created in the `target` directory.

## Usage

### Basic Usage

```
java -jar target/DriveSweep-1.0-SNAPSHOT-jar-with-dependencies.jar [command] [options]
```

### Available Commands

- `scan`: Scan directories for large files, old files, etc.
  ```
  java -jar drivesweep.jar scan [directory] --size 100 --age 30
  ```

- `clean`: Clean temporary files, residual files, etc.
  ```
  java -jar drivesweep.jar clean [directory] --temp --residual --secure
  ```

- `duplicate`: Find duplicate files
  ```
  java -jar drivesweep.jar duplicate [directory] --recursive --min-size 10
  ```

- `analyze`: Analyze disk usage and visualize storage patterns
  ```
  java -jar drivesweep.jar analyze [directory] --depth 3 --top 10
  ```

- `report`: Generate reports about disk usage, files deleted, space saved, etc.
  ```
  java -jar drivesweep.jar report [directory] --type usage --format csv
  ```

### Global Options

- `-v, --verbose`: Enable verbose output
- `-q, --quiet`: Quiet mode - display only errors
- `-d, --drive`: Target drive (e.g., C:)
- `-h, --help`: Show help message

## Development

### Project Structure

```
DriveSweep/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/
│   │   │       └── drivesweep/
│   │   │           ├── DriveSweep.java (Main class)
│   │   │           ├── commands/       (Command implementations)
│   │   │           ├── core/           (Core functionality)
│   │   │           │   ├── scanner/    (File scanning logic)
│   │   │           │   ├── cleaner/    (Cleanup operations)
│   │   │           │   └── report/     (Reporting functionality)
│   │   │           ├── util/           (Utility classes)
│   │   │           └── model/          (Data models)
│   │   └── resources/
│   │       └── log4j2.xml             (Logging configuration)
│   └── test/
│       └── java/
│           └── org/
│               └── drivesweep/
│                   └── DriveSweepTest.java
├── pom.xml                            (Maven configuration)
└── README.md                          (Documentation)
```

### Building from Source

```
mvn clean package
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
