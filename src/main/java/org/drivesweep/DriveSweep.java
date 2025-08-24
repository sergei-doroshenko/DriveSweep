package org.drivesweep;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import org.drivesweep.commands.ScanCommand;
import org.drivesweep.commands.CleanCommand;
import org.drivesweep.commands.DuplicateCommand;
import org.drivesweep.commands.AnalyzeCommand;
import org.drivesweep.commands.ReportCommand;

import java.util.concurrent.Callable;

/**
 * DriveSweep - A CLI tool for managing and clearing storage on Windows
 */
@Command(
    name = "drivesweep",
    version = "1.0",
    description = "Manage and clear storage on Windows drives",
    mixinStandardHelpOptions = true,
    subcommands = {
        ScanCommand.class,
        CleanCommand.class,
        DuplicateCommand.class,
        AnalyzeCommand.class,
        ReportCommand.class
    }
)
public class DriveSweep implements Callable<Integer> {
    private static final Logger logger = LogManager.getLogger(DriveSweep.class);

    @Option(names = {"-v", "--verbose"}, description = "Enable verbose output")
    private boolean verbose;

    @Option(names = {"-q", "--quiet"}, description = "Quiet mode - display only errors")
    private boolean quiet;

    @Option(names = {"-d", "--drive"}, description = "Target drive (e.g., C:)")
    private String drive;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new DriveSweep()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public Integer call() {
        // This code is executed if no subcommand is specified
        if (verbose) {
            logger.info("Verbose mode enabled");
        }
        
        if (quiet) {
            logger.info("Quiet mode enabled");
        }
        
        if (drive != null) {
            logger.info("Target drive: {}", drive);
        }
        
        // If no subcommand is provided, print help
        CommandLine.usage(this, System.out);
        return 0;
    }
}
