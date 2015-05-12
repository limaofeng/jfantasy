package com.fantasy.aliyun.oss;

import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.common.utils.DateUtil;
import com.aliyun.oss.model.*;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

public class Sample {

    public static void main(String[] args) throws FileNotFoundException {
        String accessKeyId = "GjYnEEMsLVTomMzF";
        String accessKeySecret = "rYSFhN67iXR0vl0pUSatSQjEqR2e2F";
        // 以杭州为例
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";

        String bucketName = "static-jfantasy-org";

        // 初始化一个OSSClient
        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        //以设置为私有权限举例
//        client.setBucketAcl(bucketName,CannedAccessControlList.Private);

//        AccessControlList accessControlList = client.getBucketAcl(bucketName);

        //可以打印出来看结果,也可以从控制台确认
//        System.out.println(accessControlList.toString());

        // 获取 bucket 地址
        String location = client.getBucketLocation(bucketName);
        System.out.println(location);


        // 获取指定文件的输入流
        File file = new File(Sample.class.getResource("logo.gif").getPath());
        InputStream content = new FileInputStream(file);

        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();

        // 必须设置ContentLength
        meta.setContentLength(file.length());

        String key = "test/logo.gif";

        // 上传Object.
        PutObjectResult result = client.putObject(bucketName, key, content, meta);

        // 打印ETag
        System.out.println(result.getETag());

        // 获取指定bucket下的所有Object信息
        ObjectListing listing = client.listObjects(bucketName);

        // 遍历所有Object
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            System.out.println(objectSummary.getKey());
        }

        // 获取Object，返回结果为OSSObject对象
        OSSObject object = client.getObject(bucketName, key);

        // 获取Object的输入流
        InputStream objectContent = object.getObjectContent();


        // 处理Object
        // 关闭流
//        StreamUtil.
//        objectContent.close();
//        System.out.println(object.getObjectMetadata());

    }

    /**
     * @param objectName 要创建的文件夹名称,在满足Object命名规则的情况下以"/"结尾
     */
    public static void mkdir(OSSClient client, String bucketName, String objectName) throws IOException {
        ObjectMetadata objectMeta = new ObjectMetadata();
        /*这里的size为0,注意OSS本身没有文件夹的概念,这里创建的文件夹本质上是一个size为0的Object,dataStream仍然可以有数据
         */
        byte[] buffer = new byte[0];
        ByteArrayInputStream in = new ByteArrayInputStream(buffer);
        objectMeta.setContentLength(0);
        try {
            client.putObject(bucketName, objectName, in, objectMeta);
        } finally {
            in.close();
        }
    }

    /**
     * Java SDK支持的Http Header有四种，分别为：Cache-Control 、 Content-Disposition 、Content-Encoding 、 Expires
     *
     * @param client
     * @param bucketName
     * @param objectName
     * @param key
     * @param content
     */
    public void httpHeader(OSSClient client, String bucketName, String objectName, String key, InputStream content) {
        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();

        // 设置ContentLength为1000
        meta.setContentLength(1000);

        // 设置1小时后过期
        Date expire = new Date(new Date().getTime() + 3600 * 1000);
        meta.setExpirationTime(expire);
        client.putObject(bucketName, key, content, meta);
    }

    /**
     * 用户自定义元数据
     */
    public void userMetadata(OSSClient client, String bucketName, String objectName, String key, InputStream content) {
        // 创建上传Object的Metadata
        ObjectMetadata meta = new ObjectMetadata();
        // 设置自定义元数据name的值为my-data
        meta.addUserMetadata("name", "my-data");

        // 上传object
        client.putObject(bucketName, key, content, meta);
    }

    public void listObjectsRequest(OSSClient client, String bucketName, String objectName) {
        // 构造ListObjectsRequest请求
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);

        // 设置参数
        listObjectsRequest.setDelimiter("/");
        listObjectsRequest.setMarker("123");

        ObjectListing listing = client.listObjects(listObjectsRequest);

    }

    /**
     * 列出Bucket内所有文件
     */
    public void ListObjectsRequest(OSSClient client, String bucketName) {
        // 构造ListObjectsRequest请求
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);

        // List Objects
        ObjectListing listing = client.listObjects(listObjectsRequest);

        // 遍历所有Object
        System.out.println("Objects:");
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            System.out.println(objectSummary.getKey());
        }

        // 遍历所有CommonPrefix
        System.out.println("CommonPrefixs:");
        for (String commonPrefix : listing.getCommonPrefixes()) {
            System.out.println(commonPrefix);
        }
    }

    /**
     * 我们可以通过设置 Prefix 参数来获取某个目录下所有的文件：
     * @param client
     * @param bucketName
     */
    public void recursionListObjectsRequest(OSSClient client, String bucketName) {
        // 构造ListObjectsRequest请求
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);

        // 递归列出fun目录下的所有文件
        listObjectsRequest.setPrefix("fun/");

        ObjectListing listing = client.listObjects(listObjectsRequest);

        // 遍历所有Object
        System.out.println("Objects:");
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            System.out.println(objectSummary.getKey());
        }

        // 遍历所有CommonPrefix
        System.out.println("\nCommonPrefixs:");
        for (String commonPrefix : listing.getCommonPrefixes()) {
            System.out.println(commonPrefix);
        }
    }

    /**
     * 列出目录下的文件和子目录
     */
    public void subListObjectsRequest(OSSClient client, String bucketName) {
        // 构造ListObjectsRequest请求
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(bucketName);

        // "/" 为文件夹的分隔符
        listObjectsRequest.setDelimiter("/");

        // 列出fun目录下的所有文件和文件夹
        listObjectsRequest.setPrefix("fun/");

        ObjectListing listing = client.listObjects(listObjectsRequest);

        // 遍历所有Object
        System.out.println("Objects:");
        for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
            System.out.println(objectSummary.getKey());
        }

        // 遍历所有CommonPrefix
        System.out.println("\nCommonPrefixs:");
        for (String commonPrefix : listing.getCommonPrefixes()) {
            System.out.println(commonPrefix);
        }
    }


    public void getObject(OSSClient client, String bucketName, String key) throws IOException {

        // 获取Objectcd ，返回结果为OSSObject对象
        OSSObject object = client.getObject(bucketName, key);

        // 获取ObjectMeta
        ObjectMetadata meta = object.getObjectMetadata();

        // 获取Object的输入流
        InputStream objectContent = object.getObjectContent();

        // 处理Object
        // 关闭流
        objectContent.close();
    }

    /**
     * 通过GetObjectRequest获取Object
     */
    public void GetObjectRequest(OSSClient client, String bucketName, String key){
        // 新建GetObjectRequest
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);

        // 获取0~100字节范围内的数据
        getObjectRequest.setRange(0, 100);

        // 获取Object，返回结果为OSSObject对象
        OSSObject object = client.getObject(getObjectRequest);
    }

    /**
     * 直接下载Object到文件
     * @param client
     * @param bucketName
     * @param key
     */
    public void GetObjectRequestToFile(OSSClient client, String bucketName, String key){
        // 新建GetObjectRequest
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);

        // 下载Object到文件
        ObjectMetadata objectMetadata = client.getObject(getObjectRequest, new File("/path/to/file"));
    }

    /**
     * 只获取ObjectMetadata
     * @param client
     * @param bucketName
     * @param key
     */
    public void getObjectMetadata(OSSClient client, String bucketName, String key) {
        ObjectMetadata objectMetadata = client.getObjectMetadata(bucketName, key);
    }

    /**
     * 使用Chunked编码上传
     */
    public void ChunkedUpdate(OSSClient client, String bucketName, String key) throws FileNotFoundException {

        FileInputStream fin = new FileInputStream(new File(""));
        // 如果不设置content-length, 默认为chunked编码。
        PutObjectResult result = client.putObject(bucketName, key, fin, new ObjectMetadata());
    }

    /**
     * 删除Object
     * @param client
     * @param bucketName
     * @param key
     */
    public void deleteObject(OSSClient client, String bucketName, String key){
        // 删除Object
        client.deleteObject(bucketName, key);
    }

    /**
     * 拷贝一个Object
     * @param client
     * @param srcBucketName
     * @param srcKey
     * @param destBucketName
     * @param destKey
     */
    public void copyObject(OSSClient client,String srcBucketName, String srcKey, String destBucketName, String destKey) {
        // 拷贝Object
        CopyObjectResult result = client.copyObject(srcBucketName, srcKey, destBucketName, destKey);

        // 打印结果
        System.out.println("ETag: " + result.getETag() + " LastModified: " + result.getLastModified());
    }

    /**
     * 通过CopyObjectRequest拷贝Object
     */
    public void CopyObjectRequest(OSSClient client,String srcBucketName, String srcKey, String destBucketName, String destKey){

        // 创建CopyObjectRequest对象
        CopyObjectRequest copyObjectRequest = new CopyObjectRequest(srcBucketName, srcKey, destBucketName, destKey);

        // 设置新的Metadata
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType("text/html");
        copyObjectRequest.setNewObjectMetadata(meta);

        // 复制Object
        CopyObjectResult result = client.copyObject(copyObjectRequest);

        System.out.println("ETag: " + result.getETag() + " LastModified: " + result.getLastModified());
    }

    /**
     * 生成POST Policy
     */
    public void policy(OSSClient client,String bucketName) throws ParseException, UnsupportedEncodingException {

        Date expiration = DateUtil.parseIso8601Date("2015-02-25T14:25:46.000Z");
        PolicyConditions policyConds = new PolicyConditions();
        policyConds.addConditionItem("bucket", bucketName);
        // 添加精确匹配条件项 “$”必须紧接大括号
        policyConds.addConditionItem(MatchMode.Exact, PolicyConditions.COND_KEY, "user/eric/\\${filename}");
        // 添加前缀匹配条件项
        policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, "user/eric");
        policyConds.addConditionItem(MatchMode.StartWith, "x-oss-meta-tag", "dummy_etag");
        // 添加范围匹配条件项
        policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 1, 1024);

        // 生成Post Policy字符串
        String postPolicy = client.generatePostPolicy(expiration, policyConds);
        System.out.println(postPolicy);

        // 计算policy Base64编码
        byte[] binaryData = postPolicy.getBytes("utf-8");
        String encodedPolicy = BinaryUtil.toBase64String(binaryData);
        System.out.println(encodedPolicy);

        //传入Post Policy原json字串，生成postSignature
        String postSignature = client.calculatePostSignature(postPolicy);
        System.out.println(postSignature);
    }

    public void multipartUpload(OSSClient client,String bucketName){
        String key = "your-key";

        // 开始Multipart Upload
        InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(bucketName, key);
        InitiateMultipartUploadResult initiateMultipartUploadResult = client.initiateMultipartUpload(initiateMultipartUploadRequest);
        // 打印UploadId
        System.out.println("UploadId: " + initiateMultipartUploadResult.getUploadId());
    }

    /**
     * Upload Part本地上传
     * @param client
     * @param bucketName
     * @throws IOException
     */
    public void uploadPart(OSSClient client,String bucketName) throws IOException {
        String key = "your-key";

        InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(bucketName, key);
        InitiateMultipartUploadResult initiateMultipartUploadResult = client.initiateMultipartUpload(initiateMultipartUploadRequest);

        // 设置每块为 5M
        final int partSize = 1024 * 1024 * 5;
        File partFile = new File("/path/to/file.zip");
        // 计算分块数目
        int partCount = (int) (partFile.length() / partSize);
        if (partFile.length() % partSize != 0){
            partCount++;
        }
        // 新建一个List保存每个分块上传后的ETag和PartNumber
        List<PartETag> partETags = new ArrayList<PartETag>();
        for(int i = 0; i < partCount; i++){
            // 获取文件流
            FileInputStream fis = new FileInputStream(partFile);
            // 跳到每个分块的开头
            long skipBytes = partSize * i;
            fis.skip(skipBytes);
            // 计算每个分块的大小
            long size = partSize < partFile.length() - skipBytes ?
                    partSize : partFile.length() - skipBytes;
            // 创建UploadPartRequest，上传分块
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(bucketName);
            uploadPartRequest.setKey(key);
            uploadPartRequest.setUploadId(initiateMultipartUploadResult.getUploadId());
            uploadPartRequest.setInputStream(fis);
            uploadPartRequest.setPartSize(size);
            uploadPartRequest.setPartNumber(i + 1);
            UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);
            // 将返回的PartETag保存到List中。
            partETags.add(uploadPartResult.getPartETag());
            // 关闭文件
            fis.close();
        }
    }

    /**
     * Upload Part本地chunked上传
     * @param client
     * @param bucketName
     * @throws IOException
     */
    public void UploadPartChunked(OSSClient client,String bucketName) throws IOException {
        String key = "";
        String filePath = "";
        String uploadId = "";

        File file = new File(filePath);
        // 设置每块为 5M
        final int partSize = 5 * 1024 * 1024;
        int fileSize = (int) file.length();
        // 计算分块数目
        final int partCount = (file.length() % partSize != 0) ? (fileSize / partSize + 1) : (fileSize / partSize);
        List<PartETag> partETags = new ArrayList<PartETag>();

        for (int i = 0; i < partCount; i++) {
            InputStream fin = new BufferedInputStream(new FileInputStream(file));
            fin.skip(i * partSize);
            int size = (i + 1 == partCount) ? (fileSize - i * partSize) : partSize;

            UploadPartRequest req = new UploadPartRequest();
            req.setBucketName(bucketName);
            req.setKey(key);
            req.setPartNumber(i + 1);
            req.setPartSize(size);
            req.setUploadId(uploadId);
            req.setInputStream(fin);
            req.setUseChunkEncoding(true);  // 使用chunked编码

            UploadPartResult result = client.uploadPart(req);
            partETags.add(result.getPartETag());

            fin.close();
        }
    }

    /**
     * Upload Part Copy拷贝上传
     */
    public void UploadPartChunked(OSSClient client,String uploadId,String bucketName,String sourceBucketName,String sourceKey,String targetBucketName,String targetKey) throws IOException {
        ObjectMetadata objectMetadata = client.getObjectMetadata(sourceBucketName,sourceKey);

        long partSize = 1024 * 1024 * 100;
// 得到被拷贝object大小
        long contentLength = objectMetadata.getContentLength();

// 计算分块数目
        int partCount = (int) (contentLength / partSize);
        if (contentLength % partSize != 0) {
            partCount++;
        }
        System.out.println("total part count:" + partCount);
        List<PartETag> partETags = new ArrayList<PartETag>();

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < partCount; i++) {
            System.out.println("now begin to copy part:" + (i+1));
            long skipBytes = partSize * i;
// 计算每个分块的大小
            long size = partSize < contentLength - skipBytes ? partSize : contentLength - skipBytes;
// 创建UploadPartCopyRequest，上传分块
            UploadPartCopyRequest uploadPartCopyRequest = new UploadPartCopyRequest();
            uploadPartCopyRequest.setSourceKey("/" + sourceBucketName + "/" + sourceKey);
            uploadPartCopyRequest.setBucketName(targetBucketName);
            uploadPartCopyRequest.setKey(targetKey);
            uploadPartCopyRequest.setUploadId(uploadId);
            uploadPartCopyRequest.setPartSize(size);
            uploadPartCopyRequest.setBeginIndex(skipBytes);
            uploadPartCopyRequest.setPartNumber(i + 1);
            UploadPartCopyResult uploadPartCopyResult = client.uploadPartCopy(uploadPartCopyRequest);
// 将返回的PartETag保存到List中。
            partETags.add(uploadPartCopyResult.getPartETag());
            System.out.println("now end to copy part:" + (i + 1));
        }
    }

    /**
     * 完成分块上传
     */
    public void wan(OSSClient client,String bucketName,String key,List<PartETag> partETags){

        InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(bucketName, key);
        InitiateMultipartUploadResult initiateMultipartUploadResult = client.initiateMultipartUpload(initiateMultipartUploadRequest);

        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName,key, initiateMultipartUploadResult.getUploadId(), partETags);

// 完成分块上传
        CompleteMultipartUploadResult completeMultipartUploadResult =
                client.completeMultipartUpload(completeMultipartUploadRequest);

// 打印Object的ETag
        System.out.println(completeMultipartUploadResult.getETag());
    }

    /**
     * 取消分块上传事件
     */
    public void abortMultipartUpload(OSSClient client,String bucketName,String key,String uploadId){
        AbortMultipartUploadRequest abortMultipartUploadRequest = new AbortMultipartUploadRequest(bucketName, key, uploadId);

        // 取消分块上传
        client.abortMultipartUpload(abortMultipartUploadRequest);
    }

    /**
     * 获取Bucket内所有分块上传事件
     */
    public void listMultipartUploads(OSSClient client,String bucketName){
        // 获取Bucket内所有上传事件
        ListMultipartUploadsRequest listMultipartUploadsRequest=new ListMultipartUploadsRequest(bucketName);
        MultipartUploadListing listing = client.listMultipartUploads(listMultipartUploadsRequest);

        // 遍历所有上传事件
        for (MultipartUpload multipartUpload : listing.getMultipartUploads()) {
            System.out.println("Key: " + multipartUpload.getKey() + " UploadId: " + multipartUpload.getUploadId());
        }
    }

    /**
     * 获取所有已上传的块信息
     * @param client
     * @param bucketName
     */
    public void listParts(OSSClient client,String bucketName,String key,String uploadId){
        ListPartsRequest listPartsRequest = new ListPartsRequest(bucketName, key, uploadId);

        // 获取上传的所有Part信息
        PartListing partListing = client.listParts(listPartsRequest);

        // 遍历所有Part
        for (PartSummary part : partListing.getParts()) {
            System.out.println("PartNumber: " + part.getPartNumber() + " ETag: " + part.getETag());
        }
    }

    /**
     * 设定CORS规则
     * @param client
     * @param bucketName
     */
    public void cors(OSSClient client,String bucketName){
        SetBucketCORSRequest request = new SetBucketCORSRequest(bucketName);
        request.setBucketName(bucketName);
        ArrayList<SetBucketCORSRequest.CORSRule> putCorsRules = new ArrayList<SetBucketCORSRequest.CORSRule>();
//CORS规则的容器,每个bucket最多允许10条规则

        SetBucketCORSRequest.CORSRule corRule = new SetBucketCORSRequest.CORSRule();
        ArrayList<String> allowedOrigin = new ArrayList<String>();
//指定允许跨域请求的来源
        allowedOrigin.add( "http://www.b.com");
        ArrayList<String> allowedMethod = new ArrayList<String>();
//指定允许的跨域请求方法(GET/PUT/DELETE/POST/HEAD)
        allowedMethod.add("GET");
        ArrayList<String> allowedHeader = new ArrayList<String>();
//控制在OPTIONS预取指令中Access-Control-Request-Headers头中指定的header是否允许。
        allowedHeader.add("x-oss-test");
        ArrayList<String> exposedHeader = new ArrayList<String>();
//指定允许用户从应用程序中访问的响应头
        exposedHeader.add("x-oss-test1");
        corRule.setAllowedMethods(allowedMethod);
        corRule.setAllowedOrigins(allowedOrigin);
        corRule.setAllowedHeaders(allowedHeader);
        corRule.setExposeHeaders(exposedHeader);
//指定浏览器对特定资源的预取(OPTIONS)请求返回结果的缓存时间,单位为秒。
        corRule.setMaxAgeSeconds(10);
//最多允许10条规则
        putCorsRules.add(corRule);
        request.setCorsRules(putCorsRules);
        client.setBucketCORS(request);
    }

    /**
     * 获取CORS规则
     */
    public void getSors(OSSClient client,String bucketName){
        ArrayList<SetBucketCORSRequest.CORSRule> corsRules;
        //获得CORS规则列表
        corsRules =  (ArrayList<SetBucketCORSRequest.CORSRule>) client.getBucketCORSRules(bucketName);
        for (SetBucketCORSRequest.CORSRule rule : corsRules) {
            for (String allowedOrigin1 : rule.getAllowedOrigins()) {
                //获得允许跨域请求源
                System.out.println(allowedOrigin1);
            }
            for (String allowedMethod1 : rule.getAllowedMethods()) {
                //获得允许跨域请求方法
                System.out.println(allowedMethod1);
            }

            if (rule.getAllowedHeaders().size() > 0){
                for (String allowedHeader1 : rule.getAllowedHeaders()){
                    //获得允许头部列表
                    System.out.println(allowedHeader1);
                }
            }

            if (rule.getExposeHeaders().size() > 0){
                for (String exposeHeader : rule.getExposeHeaders()){
                    //获得允许头部
                    System.out.println(exposeHeader);
                }
            }

            if ( null != rule.getMaxAgeSeconds()){
                System.out.println(rule.getMaxAgeSeconds());
            }
        }
    }

    /**
     * 删除CORS规则
     */
    public void deleteSORS(OSSClient client,String bucketName){
        // 清空bucket的CORS规则
        client.deleteBucketCORSRules(bucketName);
    }

    /**
     * 设置Referer白名单
     */
    public void setReferer(OSSClient client,String bucketName){
        List<String> refererList = new ArrayList<String>();
        // 添加referer项
        refererList.add("http://www.aliyun.com");
        refererList.add("http://www.*.com");
        refererList.add("http://www.?.aliyuncs.com");
        // 允许referer字段为空，并设置Bucket Referer列表
        BucketReferer br = new BucketReferer(true, refererList);
        client.setBucketReferer(bucketName, br);
    }

    /**
     * 获取Referer白名单
     */
    public void getReferer(OSSClient client,String bucketName){
        // 获取Bucket Referer列表
        BucketReferer br = client.getBucketReferer(bucketName);
        List<String> refererList = br.getRefererList();
        for (String referer : refererList) {
            System.out.println(referer);
        }
    }

    /**
     * 清空Referer
     */
    public void removeReferer(OSSClient client,String bucketName){
        // 默认允许referer字段为空，且referer白名单为空。
        BucketReferer br = new BucketReferer();
        client.setBucketReferer(bucketName, br);
    }

    /**
     * 生成签名URL
     */

    public void generatePresignedUrl(OSSClient client,String bucketName,String key){

        // 设置URL过期时间为1小时
        Date expiration = new Date(new Date().getTime() + 3600 * 1000);

        // 生成URL
        URL url = client.generatePresignedUrl(bucketName, key, expiration);

        // 生成PUT方法的URL
        client.generatePresignedUrl(bucketName, key, expiration, HttpMethod.PUT);


        // 创建请求
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key);

        // HttpMethod为PUT
        generatePresignedUrlRequest.setMethod(HttpMethod.PUT);

        // 添加UserMetadata
        generatePresignedUrlRequest.addUserMetadata("author", "baymax");

        // 生成签名的URL
        client.generatePresignedUrl(generatePresignedUrlRequest);


        String signedUrl = "xxx";
        //客户端使用使用url签名字串发送请求
        Map<String, String> customHeaders = new HashMap<String, String>();
        // 添加GetObject请求头
//        customHeaders.put("Range", "bytes=100-1000");
        OSSObject object = client.getObject(signedUrl, null);
    }


}
