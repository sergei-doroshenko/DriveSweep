# DriveSweep Development Guide

This document provides guidance for developers who want to contribute to or extend the DriveSweep project.

## Project Structure

```
DriveSweep/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/
│   │   │       └── drivesweep/
│   │   │           ├── DriveSweep.java (Main class)
│   │   │           ├── commands/       (Command implementations)
│   │   │           │   ├── ScanCommand.java
│   │   │           │   ├── CleanCommand.java
│   │   │           │   ├── DuplicateCommand.java
│   │   │           │   ├── AnalyzeCommand.java
│   │   │           │   └── ReportCommand.java
│   │   │           ├── core/           (Core functionality)
│   │   │           │   ├── scanner/    (File scanning logic)
│   │   │           │   │   └── FileScanner.java
│   │   │           │   ├── cleaner/    (Cleanup operations)
│   │   │           │   │   └── FileCleaner.java
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
├── README.md                          (User documentation)
└── DEVELOPMENT.md                     (This file)
```

## Development Environment Setup

1. **Prerequisites**
   - Java 17 or higher
   - Maven 3.6 or higher
   - Git

2. **Clone the Repository**
   ```
   git clone https://github.com/yourusername/drivesweep.git
   cd drivesweep
   ```

3. **Build the Project**
   ```
   mvn clean package
   ```

4. **Run the Application**
   ```
   java -jar target/DriveSweep-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```

5. **Run Tests**
   ```
   mvn test
   ```

## Development Workflow

1. **Create a Feature Branch**
   ```
   git checkout -b feature/your-feature-name
   ```

2. **Implement Your Changes**
   - Follow the existing code style and patterns
   - Add appropriate logging
   - Write unit tests for new functionality

3. **Run Tests and Build**
   ```
   mvn clean verify
   ```

4. **Commit and Push**
   ```
   git add .
   git commit -m "Description of your changes"
   git push origin feature/your-feature-name
   ```

5. **Create a Pull Request**
   - Provide a clear description of your changes
   - Reference any related issues

## Coding Standards

1. **Java Code Style**
   - Use 4 spaces for indentation
   - Follow Java naming conventions
   - Add JavaDoc comments for public methods and classes

2. **Logging**
   - Use Log4j2 for logging
   - Use appropriate log levels (DEBUG, INFO, WARN, ERROR)
   - Include relevant context in log messages

3. **Error Handling**
   - Use exceptions appropriately
   - Provide meaningful error messages
   - Log exceptions with stack traces

4. **Testing**
   - Write unit tests for all new functionality
   - Aim for high test coverage
   - Use JUnit 5 features like parameterized tests where appropriate

## Next Steps for Implementation

### 1. File Scanning Enhancements
- [ ] Implement parallel file scanning for better performance
- [ ] Add more sophisticated file type detection
- [ ] Implement progress reporting for long-running scans
- [ ] Add filters for excluding certain file types or directories
- [ ] Implement file content preview functionality

### 2. Duplicate File Detection
- [ ] Implement content-based duplicate detection using MD5/SHA hashes
- [ ] Add fuzzy matching for similar (but not identical) files
- [ ] Implement visualization of duplicate file groups
- [ ] Add batch operations for duplicate files (delete, move, rename)

### 3. Windows Registry Integration
- [ ] Implement Windows Registry access using JNA
- [ ] Add detection of uninstalled applications based on Registry entries
- [ ] Implement scanning for leftover Registry entries
- [ ] Add cleanup of Registry entries for uninstalled applications

### 4. Disk Usage Visualization
- [ ] Implement text-based visualization of disk usage
- [ ] Add export of visualization data to formats suitable for external tools
- [ ] Implement directory size comparison functionality
- [ ] Add historical disk usage tracking

### 5. Secure Deletion
- [ ] Implement multiple secure deletion algorithms (DoD 5220.22-M, Gutmann, etc.)
- [ ] Add verification of secure deletion
- [ ] Implement batch secure deletion with progress reporting
- [ ] Add support for SSD-specific secure deletion techniques

### 6. Reporting System
- [ ] Implement comprehensive reporting framework
- [ ] Add export to multiple formats (CSV, JSON, HTML, PDF)
- [ ] Implement scheduled report generation
- [ ] Add email notification for reports

### 7. User Interface Improvements
- [ ] Enhance interactive mode with better prompts and feedback
- [ ] Add color-coded output for better readability
- [ ] Implement progress bars for long-running operations
- [ ] Add support for command history and tab completion

### 8. Performance Optimizations
- [ ] Implement caching of file metadata
- [ ] Add incremental scanning for faster subsequent scans
- [ ] Optimize memory usage for large directory structures
- [ ] Implement multi-threaded operations where appropriate

### 9. Cross-Platform Support
- [ ] Abstract platform-specific code to enable cross-platform support
- [ ] Add macOS-specific implementations
- [ ] Add Linux-specific implementations
- [ ] Implement platform detection and appropriate feature toggling

### 10. Documentation and Examples
- [ ] Create comprehensive API documentation
- [ ] Add more examples and use cases
- [ ] Create tutorials for common tasks
- [ ] Add troubleshooting guide

## Architecture Decisions

### Command Pattern
The application uses the Command pattern via Picocli to separate the CLI interface from the core functionality. This allows for:
- Clear separation of concerns
- Easy addition of new commands
- Consistent command-line interface

### Core Services
Core functionality is implemented in service classes that:
- Are independent of the CLI interface
- Can be reused across different commands
- Have clear responsibilities and boundaries

### Logging Strategy
The application uses Log4j2 for logging with:
- Console output for interactive use
- File output for troubleshooting and auditing
- Different log levels for different environments

### Error Handling
The application follows these error handling principles:
- User-facing errors are clear and actionable
- Internal errors are logged with sufficient context
- Recovery paths are provided where possible

## Contributing

1. **Reporting Issues**
   - Use the issue tracker to report bugs or request features
   - Provide detailed steps to reproduce bugs
   - Include relevant logs and error messages

2. **Submitting Pull Requests**
   - Reference related issues in your pull request
   - Ensure all tests pass
   - Update documentation as needed
   - Follow the coding standards

3. **Code Review Process**
   - All code changes require review
   - Address review comments promptly
   - Be open to feedback and suggestions

## Resources

- [Picocli Documentation](https://picocli.info/)
- [Log4j2 Documentation](https://logging.apache.org/log4j/2.x/)
- [JNA Documentation](https://github.com/java-native-access/jna/blob/master/www/GettingStarted.md)
- [Windows Registry API](https://docs.microsoft.com/en-us/windows/win32/sysinfo/registry)
