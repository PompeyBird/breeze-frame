package org.bird.breeze.util;

import org.apache.log4j.Logger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by pompey on 2017/1/15.
 */
public class SerializeUtils {

    private static Logger logger = Logger.getLogger(SerializeUtils.class);

    public static byte[] objectToByte(Object object){

        byte[] bytes = null;

        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);

            bytes = bos.toByteArray();

            bos.close();
            oos.close();

        }catch (Exception e){
            logger.error("序列化对象失败", e);
        }
        return bytes;
    }

    public static Object byteToObject(byte[] bytes){

        Object object = null;

        try{

            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);

            object = ois.readObject();

            bis.close();
            ois.close();

        }catch (Exception e){
            logger.error("反序列化对象失败", e);
        }

        return object;
    }
}
