//package main.java;

import com.google.common.base.Supplier;
//import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.ConfigFileReader.ConfigFile;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
//import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
//import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import com.oracle.bmc.objectstorage.transfer.UploadConfiguration;
import com.oracle.bmc.objectstorage.transfer.UploadManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

/*
    Most code from https://github.com/oracle/oci-java-sdk/blob/master/bmc-examples/src/main/java/UploadObjectExample.java
 */
public class Upload {

    public static void main(String[] args) throws IOException {
    	
    	java.security.Security.setProperty("networkaddress.cache.ttl" , "60");

    	Supplier<InputStream> privateKeySupplier = new Supplier<InputStream>() {
    		@Override
            public InputStream get() {
                return Upload.class.getClassLoader().getResourceAsStream("oci_api_key.pem");
            }
        };

        InputStream configFileInputStream = Upload.class.getClassLoader().getResourceAsStream("config");
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

        UploadConfiguration uploadConfiguration =
                UploadConfiguration.builder()
                        .allowMultipartUploads(true)
                        .allowParallelUploads(true)
                        .build();

        UploadManager uploadManager = new UploadManager(client, uploadConfiguration);

        String namespaceName = "specloud95"; // This is the Cloud Account Name that you use before sign in.
        String contentType = "image/jpeg";
        
        //uplpoads all files in verified dir
        String bucketName1 = "verified";
        
        File dir1 = new File("verified");
        List<File> files1 = Arrays.asList(dir1.listFiles());
        for (File subFile : files1) {
        	if (subFile.isFile()){
        		
        		String objectName = subFile.getName();
        		
        		PutObjectRequest request =
                        PutObjectRequest.builder()
                                .bucketName(bucketName1)
                                .namespaceName(namespaceName)
                                .objectName(objectName)
                                .contentType(contentType)
//                                .contentLanguage(contentLanguage)
//                                .contentEncoding(contentEncoding)
//                                .opcMeta(metadata)
                                .build();
        		
        		UploadManager.UploadRequest uploadDetails =
                UploadManager.UploadRequest.builder(subFile).allowOverwrite(true).build(request);
 
                uploadManager.upload(uploadDetails);
 
                GetObjectResponse getResponse =
                		client.getObject(
                				GetObjectRequest.builder()
                                	.namespaceName(namespaceName)
                                    .bucketName(bucketName1)
                                    .objectName(objectName)
                                    .build());
                            }
                        }

      //uplpoads all files in unsuitable dir
        String bucketName2 = "unsuitable";
        
        File dir2 = new File("unsuitable");
        List<File> files2 = Arrays.asList(dir2.listFiles());
        for (File subFile : files2) {
        	if (subFile.isFile()){
        		
        		String objectName = subFile.getName();
        		
        		PutObjectRequest request =
                        PutObjectRequest.builder()
                                .bucketName(bucketName2)
                                .namespaceName(namespaceName)
                                .objectName(objectName)
                                .contentType(contentType)
//                                .contentLanguage(contentLanguage)
//                                .contentEncoding(contentEncoding)
//                                .opcMeta(metadata)
                                .build();
        		
        		UploadManager.UploadRequest uploadDetails =
                UploadManager.UploadRequest.builder(subFile).allowOverwrite(true).build(request);
 
                uploadManager.upload(uploadDetails);
 
                GetObjectResponse getResponse =
                		client.getObject(
                				GetObjectRequest.builder()
                                	.namespaceName(namespaceName)
                                    .bucketName(bucketName2)
                                    .objectName(objectName)
                                    .build());
                            }
                        }


        
        JOptionPane.showMessageDialog(null, "Done!");

    }
}
