package cn.dwxmp.core.support.fastdfs;

import cn.csource.common.NameValuePair;
import cn.csource.fastdfs.*;
import cn.csource.fastdht.FastDHTClient;
import cn.csource.fastdht.KeyInfo;
import cn.dwxmp.core.util.PropertiesUtil;
import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author HuXinsheng
 * @version 2016年6月27日 上午9:51:06
 */
@SuppressWarnings("serial")
public class FileManager implements Config {
    private static Logger logger = LogManager.getLogger();
	private TrackerServer trackerServer;
	private StorageServer storageServer;
	private StorageClient storageClient;
	private FastDHTClient fastDHTClient;

	private ExecutorService searchService = Executors.newSingleThreadExecutor();

	static { // Initialize Fast DFS Client configurations
		try {
			String path = FileManager.class.getResource("/").toString().replace("file:/", "");
			ClientGlobal.init(path + "fdfs_client.conf");
			cn.csource.fastdht.ClientGlobal.init(path + "fdht_client.conf");
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	public static FileManager getInstance() {
		return new FileManager();
	}

	private FileManager() {
		try {
			TrackerClient trackerClient = new TrackerClient();
			this.trackerServer = trackerClient.getConnection();
			this.storageClient = new StorageClient(trackerServer, storageServer);
			fastDHTClient = new FastDHTClient(true);
		} catch (Exception ex) {
			logger.error("", ex);
		}
	}

	public void upload(final FileModel file) {
		logger.info("File Name: " + file.getFilename() + ". File Length: " + file.getContent().length);

		final NameValuePair[] meta_list = new NameValuePair[] { new NameValuePair("mime", file.getMime()),
				new NameValuePair("size", file.getSize()), new NameValuePair("filename", file.getFilename()) };

		long startTime = System.currentTimeMillis();
		String[] uploadResults = null;
		try {
			uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), meta_list);
		} catch (IOException e) {
			logger.error("IO Exception when uploadind the file: " + file.getFilename(), e);
		} catch (Exception e) {
			logger.error("Non IO Exception when uploadind the file: " + file.getFilename(), e);
		}

		if (uploadResults == null) {
			logger.error("upload file fail, error code: " + storageClient.getErrorCode());
		} else {

			final String groupName = uploadResults[0];
			final String remoteFileName = uploadResults[1];

			String fileAbsolutePath = PropertiesUtil.getString("remote.file.uri.prefix") + groupName + SEPARATOR
					+ remoteFileName;
			file.setRemotePath(fileAbsolutePath);
			logger.info("upload_file time used: " + (System.currentTimeMillis() - startTime) + " ms. group_name: "
					+ groupName + ", remoteFileName:" + " " + remoteFileName);

			searchService.execute(new Runnable() {
				public void run() {
					try {
						KeyInfo keyInfo = new KeyInfo(file.getNamespace(), file.getObjectId(), file.getKey());
						FastDfsFile fastDfsFile = new FastDfsFile();
						fastDfsFile.setGroupName(groupName);
						fastDfsFile.setFileName(remoteFileName);
						fastDfsFile.setNameValuePairs(meta_list);
						fastDHTClient.set(keyInfo, JSON.toJSONString(fastDfsFile));
					} catch (Exception e) {
						logger.error("", e);
					}
				}
			});
		}
	}

	public FileInfo getFile(String namespace, String objectId, String key) {
		try {
			KeyInfo keyInfo = new KeyInfo(namespace, objectId, key);
			String info = fastDHTClient.get(keyInfo);
			FastDfsFile fastDfsFile = JSON.parseObject(info, FastDfsFile.class);
			return storageClient.get_file_info(fastDfsFile.getGroupName(), fastDfsFile.getFileName());
		} catch (IOException e) {
			logger.error("IO Exception: Get File from Fast DFS failed", e);
		} catch (Exception e) {
			logger.error("Non IO Exception: Get File from Fast DFS failed", e);
		}
		return null;
	}

	public void deleteFile(String groupName, String remoteFileName) throws Exception {
		storageClient.delete_file(groupName, remoteFileName);
	}
}
