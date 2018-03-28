package hello.objectStorage.src;

import com.google.common.base.Supplier;
import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.auth.SimpleAuthenticationDetailsProvider;
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

        Supplier<InputStream> privateKeySupplier = new Supplier<InputStream>() {
            @Override
            public InputStream get() {
                return TestObjectStorage.class.getClassLoader().getResourceAsStream("oci_api_key.pem");
            }
        };

        InputStream configFileInputStream = TestObjectStorage.class.getClassLoader().getResourceAsStream("oci_config");
        ConfigFileReader.ConfigFile config = ConfigFileReader.parse(configFileInputStream, "DEFAULT");

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

        String bucketName = "statuePics"; // You need to create this in the OCI console first.
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
