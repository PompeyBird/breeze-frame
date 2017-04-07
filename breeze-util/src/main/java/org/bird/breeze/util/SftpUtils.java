package org.bird.breeze.util;

import com.jcraft.jsch.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Created by pompey on 2017/4/7.
 */
public class SftpUtils {

    private static Logger logger = Logger.getLogger(SftpUtils.class);

    private ChannelSftp sftp;
    private  String host;
    private  int port;
    private  String username;
    private  String password;
    private  String seperator = "/";


    public SftpUtils(String host,String username,String password){
        new SftpUtils(host,22,username,password);
    }

    public SftpUtils(String host, int port, String username, String password){
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public void connect(){
        if(sftp != null){
            logger.info("sftp is not null");
        }
        JSch jsch = new JSch();
        try {
            jsch.getSession(username, host, port);
            Session sshSession = jsch.getSession(username, host, port);
            logger.debug("Session created.");
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            logger.debug("Session connected.");
            logger.debug("Opening Channel.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            logger.info("Connected to " + host + ".");
        } catch (JSchException e) {
            logger.error("Connect to SFTP Server Failed",e);
        }

    }

    public void disconnect(){
        try {
            if(this.sftp != null){
                if(this.sftp.isConnected()){
                    this.sftp.disconnect();
                    logger.info("sftp Connection Closed.");

                        if(null!=this.sftp.getSession()){
                            sftp.getSession().disconnect();
                            logger.info("sftp Session Closed.");
                        }

                }else if(this.sftp.isClosed()){
                    logger.info("sftp is closed already.");
                }
            }
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }

    public void download(String remoteDir,String remoteFileName, String localDir,String localFileName) {
        try {
            sftp.cd(remoteDir);
            File file = new File(localDir + seperator + localFileName);
            sftp.get(remoteFileName, new FileOutputStream(file));
        } catch (SftpException e) {
            logger.error("Download File:" + remoteFileName +" Failed,",e);
        } catch (FileNotFoundException e) {
            logger.error("Create Local File:" + remoteFileName +" Failed,",e);
        }


    }

    public void upload(String localDir,String localFileName,String remoteDir,String remoteFileName) {
        try {
            File localFile = new File(localDir + seperator + localFileName);
            this.upload(localFile,remoteDir,remoteFileName);
        } catch (Exception e) {
            logger.error("Create Local File:" + remoteDir + seperator + remoteFileName +" Failed,",e);
        }
    }

    public void upload(File file, String remoteDir, String remoteFileName) {
        try {
            sftp.cd(remoteDir);
            sftp.put(new FileInputStream(file), remoteFileName);
        } catch (SftpException e) {
            logger.error("Upload File:" + remoteDir + seperator + " Failed,",e);
        } catch (FileNotFoundException e) {
            logger.error("Create Local File:" + file.getAbsolutePath() +" Failed,",e);
        }
    }

}
