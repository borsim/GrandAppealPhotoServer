# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/).

## Unreleased
### Fixed
- N/A

### Changed
- N/A

### Added
- N/A

## 1.2.27 - 2018-02-08

### Fixed
- Bug fix for not serializing dates in request bodies correctly to RFC-3339

### Added
- Support for DNS Service. An example of calling this service is available [here](https://github.com/oracle/oci-java-sdk/blob/master/bmc-examples/src/main/java/DnsExample.java)
- Support for Reserved Public IPs in Virtual Networking Service
- Support for path route sets in Load Balancing Service
- Support for automated and policy-based backups, read-only volume attachments, and incremental backups in Block Storage Service
- Support for filtering by backupId in ListDbSystems operation in Database Service

## 1.2.23 - 2018-01-29

### Fixed
- Javadoc for the Object Storage Service is being generated again

## 1.2.22 - 2018-01-25

### Added
- Support for VNC console connections in Compute Service
- Support for using the `ObjectReadWithoutList` public access type when creating and updating buckets
- Support for dynamic groups in Identity Service
- Support for instance principals authentication when calling OCI services, an example can be found on [GitHub](https://github.com/oracle/oci-java-sdk/blob/master/bmc-examples/src/main/java/InstancePrincipalsAuthenticationDetailsProviderExample.java)
- Support for configuring idle timeout for listeners in Load Balancer Service
- Better documentation on every model class on how the change in version [1.2.16 - 2017-10-12](#1216---2017-10-12) to only serialize and transmit fields that were explicitly set to null affects the `equals(Object)` and `hashCode()` methods

## 1.2.21 - 2018-01-11

### Added
- Support for tagging:
  - Support for creating, updating, retrieving and listing tags and tag namespaces (these operations can be found in Identity Service)
  - Support for adding freeform and defined tags to resources in Core Services (Networking, Compute, and Block Volume) and Identity Service
- Support for bringing your own custom image for emulation mode virtual machines in Compute Service

## 1.2.20 - 2017-12-11

### Added
- Support for retrieving custom operation metadata, such as the OCID of a resource, from responsePayload attribute on the AuditEvent model of the Audit Service
- Support for public peering for FastConnect
- Support for specifying an authorized entity name in a Letter of Authority for FastConnect
- Support for showing a list of bandwidth shapes for FastConnect provider

### Deprecated
- The `listVirtualCircuitBandwidthShapes` operation in `VirtualNetwork` has been deprecated. Use the `listFastConnectProviderVirtualCircuitBandwidthShapes` operation instead
- When using `CreateVirtualCircuitDetails`, supplying a `providerName` is deprecated and `providerServiceId` should be used instead

## 1.2.18 - 2017-11-27

### Changed
- Passphrases are now passed as char[] instead of as String
- Requests are now buffered in memory by default, except by the ObjectStorageClient and ObjectStorageAsyncClient. This allows for better error messages on PUT and POST requests. If you do not want to buffer requests in memory, pass an instance of `com.oracle.bmc.http.DefaultConfigurator.NonBuffering` to the constructor of the client.

### Added
- Support for VCN to VCN peering within region
- Support option for second NIC on X7 bare metal instances
- Support for user-managed boot volumes
- Support for creating database from backup in Database service
- Support for sort and filter in ListLoadBalancers method in Load Balancer Service

### Deprecated
- Methods accepting passphrases as String are deprecated; use char[] instead

## 1.2.17 - 2017-11-02

### Added
- Support for updating audit retention policy in Audit service
- Support for archive storage tier, object rename and namespace metadata in Object Storage service
- Support for fast clones of volumes in Block Storage service
- Support for backup and restore in Database service
- Support for sorting and filtering in list APIs in Core Services

## 1.2.16 - 2017-10-12

### Changed
- Removed javax.validation.constraints annotations from model classes. The annotations were not used, and may not necessarily be the same as the constraints enforced by the services.
- The clients only serialize and transmit fields that were explicitly set. If you want to transmit a field that is `null`, please set it to `null` explicitly.

### Added
- Support for database as a service (DBaaS)
- Support for VNIC routes and source/destination check
- Support for specifying block volume size in GB
- Support for updating console history metadata and specifying a display name when capturing console history
- Support for FRA Region (eu-frankfurt-1)
- Exceptions expose client-side request id in cases of timeout or client-side failure

### Deprecated
- Passing the block volume size in MB is deprecated

## 1.2.15 - 2017-09-11

### Changed
- Maven packages renamed from `oracle-bmc-*` to `oci-*` (group id renamed from `com.oracle.bmc.sdk` to `com.oracle.oci.sdk`)
- Default configuration file location changed from `~/.oraclebmc/config` to `~/.oci/config`; old location deprecated (see "Deprecated" below)

### Added
- Support for instance console connections
- Support for Load Balancer health status API
- Support for compartment renaming
- Support for CustomerSecretKeys management

### Deprecated
- The previous default configuration file location, `~/.oraclebmc/config`, has been deprecated: please use `~/.oci/config` instead. The old location still works, if file at new location does not exist.

## 1.2.13 - 2017-08-10
### Fixed
- Duplicable stream support (https://github.com/oracle/bmcs-java-sdk/issues/11)
- Fixed NullPointerException on invalid PEM key file

## 1.2.12 - 2017-07-20
### Fixed
- Reduced overhead in signing and serialization
- Removed possibility of using different serializer for signing and transmission

### Added
- Support for compute image import/export
- Support for multiple VNICs
- Support for secondary IPs

## 1.2.10 - 2017-06-09
### Added
- Support for object storage pre-authenticated requests and public buckets.
- Support for load balancer session persistence operations.
- Support for VCN local load balancer operations.
- Support for nested instance metadata operations.

## 1.2.8 - 2017-05-18
### Added
- Extended support for FastConnect
- Support for Private VCN
- Support for IAD Region (us-ashburn-1)

## 1.2.7 - 2017-04-27
### Changed
- A new value called UnknownEnumValue has been added to all enums that are used in responses returned from services. If a service returns a value that cannot be recognized by the version of the SDK, then the enum will be set to this value. Previously this would throw an exception.

### Added
- Support for FastConnect
- Support for Load Balancer Service

## 1.2.5 - 2017-04-06
### Changed
- Added opc-client-request-id truncation logic in MultipartObjectAssembler to prevent failures

### Added
- Support for DHCP Search Domain Option
- Support for Compute API 'getWindowsInstanceInitialCredentials'

## 1.2.4 - 2017-03-28
### Fixed
- Allow UUID in path elements
- Better validate path parameters before making requests (https://github.com/oracle/bmcs-java-sdk/issues/5)

### Changed
- Simplified classes that perform signing a little
- Move auth caching to an annotation

### Added
- New low level APIs for multi-part upload in Object Storage
- New high level abstractions for uploading (UploadManager, MultipartObjectAssembler) in Object Storage

## 1.2.3 - 2017-03-16
### Fixed
- Allow service responses to deserialize to base classes when unknown discriminators returned (vs. throwing exceptions)

### Changed
- Added a new layer for authentication details to provide for other forms of keyId based auth

### Added
- New DNS label feature
- New request signer classes to use directly with other HTTP clients
- New client constructors to allow more control over how requests are signed

## 1.2.2 - 2017-02-23
### Fixed
- Bugs in config file parsing

### Changed
- Updated APIs for VCN for stateless security lists
- Updated APIs for Compute for ipxe script support

### Added
- Support for Audit service
- BOM module for SDK
- More examples

## 1.2.0 - 2016-12-16
### Fixed
- Minor bug fixes from Fortify results
- 'Accept' content header based on expected response
- Bug in exponential backoff overflow

### Changed
- Build configuration (pom.xml files) greatly simplified
- Updated APIs for Object Storage
- Updated documentation
- Customizable request signer

## 1.1.0 - 2016-11-18
### Fixed
- Prevent NPE when no content-type header returned
- Waiter for DrgAttachment handles 404s for Detached state now
- Encoding all path and query parameters

### Changed
- Improved exception messaging when parsing private keys
- Annotations for internal classes exposed for documentation only
- Minor update to core/virtual network APIs and docs
- Doc updates

### Added
- 'content-*' headers can be set when calling PutObject in Object Storage Service
- Warning messages if auth key OCIDs do not match expected format
- Simplifications to load config from default location ("~/.oraclebmc/config")

## 1.0.1 - 2016-11-15
### Fixed
- Removed usage of the Grizzly Jersey connector, which was causing problems with PUT/POST requests

## 1.0.0 - 2016-10-20
### Added
- Initial Release
- Support added for Core Services, Identity Service, Object Storage Service
