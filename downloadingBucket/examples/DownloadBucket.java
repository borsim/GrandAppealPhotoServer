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
//list object imports
import com.oracle.bmc.objectstorage.requests.ListObjectsRequest;
import com.oracle.bmc.objectstorage.model.ObjectSummary;
//get object imports
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
//misc
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.FileOutputStream;

//IMPLEMENT RUNABLE


public class DownloadBucket implements Runnable {

    public void run() {
    	
    	//classic oracle set up
        String configurationFilePath = "~/.oci/config";
        String profile = "DEFAULT";

        try {
        	 AuthenticationDetailsProvider provider =
                    new ConfigFileAuthenticationDetailsProvider(configurationFilePath, profile);
        	 
        	 ObjectStorage client = new ObjectStorageClient(provider);
             client.setRegion(Region.US_ASHBURN_1);

             //getting the namespace
             GetNamespaceResponse namespaceResponse =
                     client.getNamespace(GetNamespaceRequest.builder().build());
             String namespaceName = namespaceResponse.getValue();
             System.out.println("Using namespace: " + namespaceName);
             
             //creating the list objects request with relevant data
             ListObjectsRequest listRequest = 
             		ListObjectsRequest.builder()
             			.bucketName("statuePics")
             			.namespaceName(namespaceName)
             			.build();
            
             //setting up home for the names we will receive
             List<String> fileNames = new ArrayList<String>();

             //sending request
             List<ObjectSummary> images = client.listObjects(listRequest).getListObjects().getObjects();
             for (ObjectSummary image : images) {
                System.out.println("Found image: " + image.getName());
                fileNames.add(image.getName());
                }
             
             //variable to make unique names for downloaded images
             Integer count = new Integer(0);
             //running some command line to create folder for downloaded images
             Runtime rt = Runtime.getRuntime();
             Process pr1 = rt.exec("cd /Users/Rachel/Documents/downloadingBucket/");
             Process pr2 = rt.exec("mkdir photos");
             
             //going through each file on OS in turn and downloading it
             for (String name : fileNames) {
             	
             	//creating get object request with relevant data
             	GetObjectRequest objectRequest = 
                 		GetObjectRequest.builder()
                 			.bucketName("statuePics")
                 			.namespaceName(namespaceName)
                 			.objectName(name)
                 			.build();
                 
             	//sending request
                 GetObjectResponse response = client.getObject(objectRequest);
           
                 //saves the file to the host file
                 try {
                 	InputStream is = response.getInputStream();
                 	String filename = count.toString();
                     
                 	//making a host file
                     Process pr3 = rt.exec("touch " + filename + ".jpg");
                     //downloading image
                     OutputStream os = new FileOutputStream(new File("/Users/Rachel/Documents/downloadingBucket/photos/" + filename + ".jpg"));
                     //ensuring next filename is unique
                     count++;
                     
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
                 }
                 catch (IOException e) {
                 	e.printStackTrace();
                 }
     	
             } 
        	
        }
        catch(Exception e) {e.printStackTrace();}
      

        
   }
}
