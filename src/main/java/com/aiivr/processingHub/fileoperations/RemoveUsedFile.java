package com.aiivr.processingHub.fileoperations;

import com.aiivr.processingHub.logger.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;



@Component
public class RemoveUsedFile {

    @Autowired
    Logger logger;

    public void deleteUsedFile(String fileName){
        Path path = Paths.get("/path/to/your/"+fileName);
        try {
            Files.delete(path); // Throws an exception if file doesn't exist
            System.out.println("File deleted successfully");
        } catch (NoSuchFileException e) {
            System.out.println("No such file exists");
        } catch (DirectoryNotEmptyException e) {
            System.out.println("Directory is not empty");
        } catch (IOException e) {
            System.out.println("I/O error occurred: " + e.getMessage());
        }

        logger.logData("Removed file "+fileName);

    }

}
