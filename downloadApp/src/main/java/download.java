//package main.java;

import com.google.common.base.Supplier;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.ConfigFileReader.ConfigFile;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;

//list object imports
import com.oracle.bmc.objectstorage.requests.ListObjectsRequest;
import com.oracle.bmc.objectstorage.requests.ListObjectsRequest.Builder;
import com.oracle.bmc.objectstorage.responses.ListObjectsResponse;
import com.oracle.bmc.objectstorage.model.ObjectSummary;

//get object imports
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.requests.DeleteObjectRequest;
import com.oracle.bmc.objectstorage.responses.DeleteObjectResponse;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import com.oracle.bmc.objectstorage.transfer.UploadConfiguration;
import com.oracle.bmc.objectstorage.transfer.UploadManager;

//misc
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/*
    Most code from https://github.com/oracle/oci-java-sdk/blob/master/bmc-examples/src/main/java/UploadObjectExample.java
 */

public class Download {

    public static void main(String[] args) throws Exception {

        //setup
        java.security.Security.setProperty("networkaddress.cache.ttl" , "60");

        Supplier<InputStream> privateKeySupplier = new Supplier<InputStream>() {
            public InputStream get() {
                return Download.class.getClassLoader().getResourceAsStream("oci_api_key.pem");
            }
        };

        InputStream configFileInputStream = Download.class.getClassLoader().getResourceAsStream("config");
        ConfigFile config = ConfigFileReader.parse(configFileInputStream, "DEFAULT");


        AuthenticationDetailsProvider provider =
                SimpleAuthenticationDetailsProvider.builder()
                        .tenantId(config.get("tenancy"))
                        .userId(config.get("user"))
                        .fingerprint(config.get("fingerprint"))
                        .passPhrase(config.get("pass_phrase"))
                        .privateKeySupplier(privateKeySupplier)
                        .build();


        ObjectStorage client = new ObjectStorageClient(provider);
        client.setRegion(Region.US_ASHBURN_1);

        String namespaceName = "specloud95"; // This is the Cloud Account Name that you use before sign in.
        String contentType = "image/jpeg";

        // list objects in bucket
        ListObjectsRequest requestList =
                ListObjectsRequest.builder()
                        .bucketName("uploaded")
                        .namespaceName(namespaceName)
                        .build();

        //set up home for the names we will receive
        List<String> fileNames = new ArrayList<String>();

        //sending request and add to fileNames
        List<ObjectSummary> images = client.listObjects(requestList).getListObjects().getObjects();
        int imageCount = 0;
        for (ObjectSummary image : images) {
            imageCount++;
            System.out.println("Found image: " + image.getName());
            fileNames.add(image.getName());
            if(imageCount == 50) { break; }
        }

        //variable to make unique names for downloaded images
        Integer count = new Integer(1);

        //going through each name in list and download corresponding object on OS
        for (String name : fileNames) {
            //create request for object
            GetObjectRequest request =
                    GetObjectRequest.builder()
                            .bucketName("uploaded")
                            .namespaceName(namespaceName)
                            .objectName(name)
                            .build();

            //send request
            GetObjectResponse response = client.getObject(request);

            //save object to host file
            try {
                InputStream is = response.getInputStream();
                String filename = name;

                //save image
                OutputStream os = new FileOutputStream(new File("toVerify/" + filename + ".jpg"));
                //ensure next filename is unique
                count++;

                byte[] buffer = new byte[1024];
                int bytesRead;
                //read from input stream 'is' to buffer
                while ((bytesRead = is.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                is.close();
                //flush OutputStream to write any buffered data to file
                os.flush();
                os.close();

                //delete object from OS bucket
                DeleteObjectRequest deleteReq =
                        DeleteObjectRequest.builder()
                                .bucketName("uploaded")
                                .namespaceName(namespaceName)
                                .objectName(name)
                                .build();

                client.deleteObject(deleteReq);
                

            } catch (IOException e) {
                e.printStackTrace();
            }

            
        }
        JOptionPane.showMessageDialog(null, "Done!");
    }
}