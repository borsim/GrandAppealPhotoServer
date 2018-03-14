package hello.objectStorage.src;

import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import com.oracle.bmc.objectstorage.transfer.UploadConfiguration;
import com.oracle.bmc.objectstorage.transfer.UploadManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/*
    Most code from https://github.com/oracle/oci-java-sdk/blob/master/bmc-examples/src/main/java/UploadObjectExample.java
 */
public class TestObjectStorage {

    public void upload(File file, String objectName) throws IOException {
        java.security.Security.setProperty("networkaddress.cache.ttl" , "60");


        // Note: see the file oci_config_example for what the oci_config file must contain.
        // See the following doc pages for how to generate these values:
        // * https://docs.us-phoenix-1.oraclecloud.com/Content/API/Concepts/sdkconfig.htm
        // * https://docs.us-phoenix-1.oraclecloud.com/Content/API/Concepts/apisigningkey.htm#How

        AuthenticationDetailsProvider provider =
                new ConfigFileAuthenticationDetailsProvider("/Users/Rudy/Desktop/CS/2nd Year/SPE/complete/config", "DEFAULT");


        ObjectStorage client = new ObjectStorageClient(provider);
        client.setRegion(Region.US_ASHBURN_1);

        UploadConfiguration uploadConfiguration =
                UploadConfiguration.builder()
                        .allowMultipartUploads(true)
                        .allowParallelUploads(true)
                        .build();

        UploadManager uploadManager = new UploadManager(client, uploadConfiguration);

        String bucketName = "test"; // You need to create this in the OCI console first.
        String namespaceName = "specloud95"; // This is the Cloud Account Name that you use before sign in.

        String contentType = "image/jpeg";

        PutObjectRequest request =
                PutObjectRequest.builder()
                        .bucketName(bucketName)
                        .namespaceName(namespaceName)
                        .objectName(objectName)
                        .contentType(contentType)
//                        .contentLanguage(contentLanguage)
//                        .contentEncoding(contentEncoding)
//                        .opcMeta(metadata)
                        .build();

        UploadManager.UploadRequest uploadDetails =
                UploadManager.UploadRequest.builder(file).allowOverwrite(true).build(request);

        UploadManager.UploadResponse response = uploadManager.upload(uploadDetails);
        System.out.println(response);

        GetObjectResponse getResponse =
                client.getObject(
                        GetObjectRequest.builder()
                                .namespaceName(namespaceName)
                                .bucketName(bucketName)
                                .objectName(objectName)
                                .build());

        try (final InputStream fileStream = getResponse.getInputStream()) {
            String result = org.apache.commons.io.IOUtils.toString(fileStream, "UTF-8");
            System.out.println(result);
        }

    }
}
