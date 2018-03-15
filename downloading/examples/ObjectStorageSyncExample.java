/**
 * Copyright (c) 2016, 2018, Oracle and/or its affiliates. All rights reserved.
 */
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;
/*
//list object imports
import com.oracle.bmc.objectstorage.requests.ListObjectsRequest;
import com.oracle.bmc.objectstorage.requests.ListObjectsRequest.Builder;
import com.oracle.bmc.objectstorage.responses.ListObjectsResponse;
import com.oracle.bmc.objectstorage.model.ObjectSummary;
*/
//get object imports
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
//misc
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;


public class ObjectStorageSyncExample {

    public static void main(String[] args) throws Exception {

        String configurationFilePath = "~/.oci/config";
        String profile = "DEFAULT";

        AuthenticationDetailsProvider provider =
                new ConfigFileAuthenticationDetailsProvider(configurationFilePath, profile);

        ObjectStorage client = new ObjectStorageClient(provider);
        client.setRegion(Region.US_ASHBURN_1);

        GetNamespaceResponse namespaceResponse =
                client.getNamespace(GetNamespaceRequest.builder().build());
        String namespaceName = namespaceResponse.getValue();
        System.out.println("Using namespace: " + namespaceName);
        
        
        GetObjectRequest request = 
        		GetObjectRequest.builder()
        			.bucketName("test")
        			.namespaceName(namespaceName)
        			//.objectName("RudyTest") // change this depending on what image you wanna download
        			.objectName("cat")
        			.build();
        
        GetObjectResponse response = client.getObject(request);
  
        //saves the file to the host file
        try {
        	InputStream is = response.getInputStream();
            
            OutputStream os = new FileOutputStream(new File("/Users/Sam/Desktop/downloading/dest.jpg"));
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            //read from is to buffer
            while((bytesRead = is.read(buffer)) !=-1){
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            //flush OutputStream to write any buffered data to file
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        /*
        // list objects code
        ListObjectsRequest request = 
        		ListObjectsRequest.builder()
        			.bucketName("test")
        			.namespaceName(namespaceName)
        			.build();
        
       List<ObjectSummary> images = client.listObjects(request).getListObjects().getObjects();
       for (ObjectSummary image : images) {
           System.out.println("Found image: " + image.getName());
       }
       */
       
        client.close();
    }
}