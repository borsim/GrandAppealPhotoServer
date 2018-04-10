package hello;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.File;
import java.io.FileOutputStream;
import hello.storage.StorageFileNotFoundException;
import hello.storage.StorageService;
import javafx.util.Pair;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.HttpStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Date;
import java.text.SimpleDateFormat;
import hello.objectStorage.src.*;

@Controller
public class FileUploadController {
    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {

        //storageService.store(file);
        //redirectAttributes.addFlashAttribute("message",
        //        "You successfully uploaded " + file.getOriginalFilename() + "!");

        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        // calling Sam's object storage
        TestObjectStorage objectStorage = new TestObjectStorage();
        objectStorage.upload(convFile, "If this works Rudy will go insane");

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    ArrayList <Pair <String,Date> > permissions = new ArrayList <Pair <String,Date> > ();

    @GetMapping("/")
    public String redirectToWebsite() throws IOException {
        return "redirect:https://www.grandappeal.org.uk/";
    }

    @GetMapping("/health")
    public ResponseEntity<?> healthOK() throws IOException {
        ResponseEntity response = new ResponseEntity(HttpStatus.OK);
        return response;
    }

    @PostMapping("/files")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file, @RequestHeader("id") String id) throws IOException {
        int found = 0;
        for (int i=0; i < permissions.size(); i++){
            if (permissions.get(i).getKey().equals(id)) {
                Date requestDate = new Date();
                if (permissions.get(i).getValue().after(requestDate)) found = 1;
                else permissions.remove(i);
            }
        }

        if (found == 1) {
            ResponseEntity failresponse = new ResponseEntity(HttpStatus.FORBIDDEN);
            return failresponse;
        }
        else {
            File convFile = new File(file.getOriginalFilename());
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            Date uploadDate = new Date();
            String dateFormat = new SimpleDateFormat("MM-dd-HH:mm:ss").format(uploadDate);
            String objectname = dateFormat + "-" + java.util.UUID.randomUUID().toString();

            // calling Sam's object storage
            TestObjectStorage objectStorage = new TestObjectStorage();
            objectStorage.upload(convFile, objectname);

            Date permissionDate = new Date(System.currentTimeMillis() + 2*60 * 1000);
            permissions.add(new Pair(id,permissionDate));
            convFile.delete();

            ResponseEntity response = new ResponseEntity(HttpStatus.ACCEPTED);
            return response;
        }
    }

}
