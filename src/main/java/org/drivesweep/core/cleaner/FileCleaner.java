package org.drivesweep.core.cleaner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Utility class for cleaning files and directories.
 */
public class FileCleaner {
    private static final Logger logger = LogManager.getLogger(FileCleaner.class);
    private static final SecureRandom random = new SecureRandom();
    
    // List of common temporary directories
    private static final List<String> TEMP_DIRECTORIES = Arrays.asList(
        System.getProperty("java.io.tmpdir"),
        System.getProperty("user.home") + "\\AppData\\Local\\Temp",
        System.getProperty("user.home") + "\\AppData\\LocalLow\\Temp",
        System.getProperty("user.home") + "\\AppData\\Roaming\\Temp"
    );
    
    // List of common browser cache directories
    private static final List<String> BROWSER_CACHE_DIRECTORIES = Arrays.asList(
        System.getProperty("user.home") + "\\AppData\\Local\\Google\\Chrome\\User Data\\Default\\Cache",
        System.getProperty("user.home") + "\\AppData\\Local\\Microsoft\\Edge\\User Data\\Default\\Cache",
        System.getProperty("user.home") + "\\AppData\\Local\\Mozilla\\Firefox\\Profiles"
    );
    
    // List of temporary file extensions
    private static final List<String> TEMP_EXTENSIONS = Arrays.asList(
        ".tmp", ".temp", ".~mp", ".bak", ".old", ".wbk", ".dmp"
    );
    
    /**
     * Clean temporary files.
     *
     * @param interactive Whether to prompt before deletion
     * @param secureDelete Whether to use secure deletion
     * @return Number of files deleted
     */
    public int cleanTemporaryFiles(boolean interactive, boolean secureDelete) {
        logger.info("Cleaning temporary files");
        int filesDeleted = 0;
        
        // Clean system temp directories
        for (String tempDir : TEMP_DIRECTORIES) {
            File dir = new File(tempDir);
            if (dir.exists() && dir.isDirectory()) {
                filesDeleted += cleanDirectory(dir, interactive, secureDelete);
            }
        }
        
        // Clean browser cache directories
        for (String cacheDir : BROWSER_CACHE_DIRECTORIES) {
            File dir = new File(cacheDir);
            if (dir.exists() && dir.isDirectory()) {
                filesDeleted += cleanDirectory(dir, interactive, secureDelete);
            }
        }
        
        return filesDeleted;
    }
    
    /**
     * Clean residual files from uninstalled applications.
     *
     * @param interactive Whether to prompt before deletion
     * @param secureDelete Whether to use secure deletion
     * @return Number of files deleted
     */
    public int cleanResidualFiles(boolean interactive, boolean secureDelete) {
        logger.info("Cleaning residual files from uninstalled applications");
        int filesDeleted = 0;
        
        // Common locations for residual files
        List<String> residualDirs = Arrays.asList(
            System.getProperty("user.home") + "\\AppData\\Local",
            System.getProperty("user.home") + "\\AppData\\Roaming",
            "C:\\ProgramData"
        );
        
        // TODO: Implement more sophisticated detection of residual files
        // This would typically involve checking the Windows registry for uninstalled applications
        // and then looking for leftover directories
        
        for (String dirPath : residualDirs) {
            File dir = new File(dirPath);
            if (dir.exists() && dir.isDirectory()) {
                // For now, just look for directories that might be residual
                // In a real implementation, this would be more sophisticated
                File[] subDirs = dir.listFiles(File::isDirectory);
                if (subDirs != null) {
                    for (File subDir : subDirs) {
                        // This is a placeholder for actual residual detection logic
                        // In a real implementation, we would check if this directory belongs to an uninstalled app
                        if (isLikelyResidual(subDir)) {
                            if (!interactive || confirmDeletion(subDir)) {
                                if (deleteFile(subDir, secureDelete)) {
                                    filesDeleted++;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return filesDeleted;
    }
    
    /**
     * Clean a directory.
     *
     * @param directory The directory to clean
     * @param interactive Whether to prompt before deletion
     * @param secureDelete Whether to use secure deletion
     * @return Number of files deleted
     */
    public int cleanDirectory(File directory, boolean interactive, boolean secureDelete) {
        logger.info("Cleaning directory: {}", directory.getAbsolutePath());
        int filesDeleted = 0;
        
        if (!directory.exists() || !directory.isDirectory()) {
            logger.warn("Directory does not exist or is not a directory: {}", directory);
            return 0;
        }
        
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (isTemporaryFile(file)) {
                        if (!interactive || confirmDeletion(file)) {
                            if (deleteFile(file, secureDelete)) {
                                filesDeleted++;
                            }
                        }
                    }
                } else if (file.isDirectory()) {
                    filesDeleted += cleanDirectory(file, interactive, secureDelete);
                    
                    // If directory is now empty, delete it
                    if (file.exists() && file.list() != null && file.list().length == 0) {
                        if (!interactive || confirmDeletion(file)) {
                            if (file.delete()) {
                                logger.info("Deleted empty directory: {}", file.getAbsolutePath());
                                filesDeleted++;
                            } else {
                                logger.warn("Failed to delete directory: {}", file.getAbsolutePath());
                            }
                        }
                    }
                }
            }
        }
        
        return filesDeleted;
    }
    
    /**
     * Delete a file, optionally using secure deletion.
     *
     * @param file The file to delete
     * @param secureDelete Whether to use secure deletion
     * @return Whether the deletion was successful
     */
    public boolean deleteFile(File file, boolean secureDelete) {
        if (secureDelete) {
            return secureDelete(file);
        } else {
            try {
                if (file.isDirectory()) {
                    FileUtils.deleteDirectory(file);
                } else {
                    Files.delete(file.toPath());
                }
                logger.info("Deleted: {}", file.getAbsolutePath());
                return true;
            } catch (IOException e) {
                logger.error("Error deleting file: {}", file.getAbsolutePath(), e);
                return false;
            }
        }
    }
    
    /**
     * Securely delete a file by overwriting it with random data before deletion.
     *
     * @param file The file to delete
     * @return Whether the deletion was successful
     */
    private boolean secureDelete(File file) {
        if (file.isDirectory()) {
            // For directories, recursively secure delete all files
            boolean success = true;
            File[] files = file.listFiles();
            if (files != null) {
                for (File subFile : files) {
                    success &= secureDelete(subFile);
                }
            }
            
            // Then delete the directory itself
            try {
                FileUtils.deleteDirectory(file);
                logger.info("Securely deleted directory: {}", file.getAbsolutePath());
                return success;
            } catch (IOException e) {
                logger.error("Error deleting directory: {}", file.getAbsolutePath(), e);
                return false;
            }
        } else {
            // For files, overwrite with random data before deletion
            try {
                long length = file.length();
                if (length > 0) {
                    byte[] randomData = new byte[8192]; // 8KB buffer
                    
                    // Overwrite the file 3 times with random data
                    for (int i = 0; i < 3; i++) {
                        try (java.io.RandomAccessFile raf = new java.io.RandomAccessFile(file, "rws")) {
                            long pos = 0;
                            while (pos < length) {
                                random.nextBytes(randomData);
                                raf.write(randomData, 0, (int) Math.min(randomData.length, length - pos));
                                pos += randomData.length;
                            }
                        }
                    }
                }
                
                // Finally delete the file
                Files.delete(file.toPath());
                logger.info("Securely deleted file: {}", file.getAbsolutePath());
                return true;
            } catch (IOException e) {
                logger.error("Error securely deleting file: {}", file.getAbsolutePath(), e);
                return false;
            }
        }
    }
    
    /**
     * Check if a file is likely a temporary file.
     *
     * @param file The file to check
     * @return Whether the file is likely a temporary file
     */
    private boolean isTemporaryFile(File file) {
        String name = file.getName().toLowerCase();
        
        // Check if the file has a temporary extension
        for (String ext : TEMP_EXTENSIONS) {
            if (name.endsWith(ext)) {
                return true;
            }
        }
        
        // Check for other common temporary file patterns
        return name.startsWith("~") || 
               name.startsWith("temp") || 
               name.contains("cache") || 
               name.contains("temp");
    }
    
    /**
     * Check if a directory is likely a residual from an uninstalled application.
     * This is a placeholder for more sophisticated detection logic.
     *
     * @param directory The directory to check
     * @return Whether the directory is likely a residual
     */
    private boolean isLikelyResidual(File directory) {
        // This is a placeholder implementation
        // In a real implementation, this would check the Windows registry
        // to see if this directory belongs to an uninstalled application
        
        // For now, just return false to prevent accidental deletion
        return false;
    }
    
    /**
     * Prompt the user for confirmation before deletion.
     *
     * @param file The file to delete
     * @return Whether the user confirmed the deletion
     */
    private boolean confirmDeletion(File file) {
        System.out.print("Delete " + file.getAbsolutePath() + "? (y/n): ");
        Scanner scanner = new Scanner(System.in);
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("y") || response.equals("yes");
    }
    
    /**
     * Clean up the tool's own temporary files.
     */
    public void cleanupOwnTemporaryFiles() {
        logger.info("Cleaning up DriveSweep's own temporary files");
        
        // This would clean up any temporary files created by the tool itself
        // For now, this is just a placeholder
    }
}
