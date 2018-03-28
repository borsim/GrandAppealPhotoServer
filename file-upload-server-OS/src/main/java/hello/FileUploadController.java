package hello;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.*;

import hello.objectStorage.src.*;

@Controller
public class FileUploadController {

    @GetMapping("/")
    public String redirectToWebsite() throws IOException {
        return "redirect:https://www.grandappeal.org.uk/";
    }

    @PostMapping("/files")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();

        // calling Sam's object storage
        TestObjectStorage objectStorage = new TestObjectStorage();
        objectStorage.upload(convFile,  java.util.UUID.randomUUID().toString());
        convFile.delete();

        ResponseEntity response = new ResponseEntity(HttpStatus.ACCEPTED);
        return response;
    }

}
