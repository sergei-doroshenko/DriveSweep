package org.drivesweep.core.scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class for scanning files and directories.
 */
public class FileScanner {
    private static final Logger logger = LogManager.getLogger(FileScanner.class);

    /**
     * Find large files in a directory.
     *
     * @param directory The directory to scan
     * @param minSizeMB Minimum file size in MB
     * @param recursive Whether to scan recursively
     * @return List of large files
     */
    public List<File> findLargeFiles(File directory, int minSizeMB, boolean recursive) {
        logger.info("Finding large files in {}", directory.getAbsolutePath());
        
        long minSizeBytes = minSizeMB * 1024L * 1024L;
        
        try {
            return findFiles(directory, recursive, file -> {
                return file.isFile() && file.length() >= minSizeBytes;
            });
        } catch (IOException e) {
            logger.error("Error finding large files", e);
            return new ArrayList<>();
        }
    }

    /**
     * Find old files in a directory.
     *
     * @param directory The directory to scan
     * @param minAgeDays Minimum file age in days
     * @param recursive Whether to scan recursively
     * @return List of old files
     */
    public List<File> findOldFiles(File directory, int minAgeDays, boolean recursive) {
        logger.info("Finding old files in {}", directory.getAbsolutePath());
        
        try {
            Instant cutoffDate = Instant.now().minus(minAgeDays, ChronoUnit.DAYS);
            
            return findFiles(directory, recursive, file -> {
                if (!file.isFile()) {
                    return false;
                }
                
                try {
                    BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
                    Instant lastAccess = attrs.lastAccessTime().toInstant();
                    Instant lastModified = attrs.lastModifiedTime().toInstant();
                    
                    // Use the most recent of last access or last modified time
                    Instant mostRecent = lastAccess.isAfter(lastModified) ? lastAccess : lastModified;
                    
                    return mostRecent.isBefore(cutoffDate);
                } catch (IOException e) {
                    logger.warn("Error reading file attributes for {}: {}", file, e.getMessage());
                    return false;
                }
            });
        } catch (IOException e) {
            logger.error("Error finding old files", e);
            return new ArrayList<>();
        }
    }

    /**
     * Find files matching a predicate.
     *
     * @param directory The directory to scan
     * @param recursive Whether to scan recursively
     * @param predicate The predicate to match files against
     * @return List of matching files
     */
    private List<File> findFiles(File directory, boolean recursive, Predicate<File> predicate) throws IOException {
        if (!directory.exists() || !directory.isDirectory()) {
            logger.warn("Directory does not exist or is not a directory: {}", directory);
            return new ArrayList<>();
        }
        
        int maxDepth = recursive ? Integer.MAX_VALUE : 1;
        
        try (Stream<Path> walk = Files.walk(directory.toPath(), maxDepth)) {
            return walk
                .map(Path::toFile)
                .filter(predicate)
                .collect(Collectors.toList());
        }
    }
    
    /**
     * Get human-readable file size.
     *
     * @param file The file
     * @return Human-readable file size
     */
    public static String getHumanReadableSize(File file) {
        return FileUtils.byteCountToDisplaySize(file.length());
    }
    
    /**
     * Get human-readable last modified date.
     *
     * @param file The file
     * @return Human-readable last modified date
     */
    public static String getLastModifiedDate(File file) {
        return LocalDateTime.ofInstant(
            Instant.ofEpochMilli(file.lastModified()),
            ZoneId.systemDefault()
        ).toString();
    }
}
