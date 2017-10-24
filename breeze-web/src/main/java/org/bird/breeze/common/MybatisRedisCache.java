package org.bird.breeze.common;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.cache.Cache;
import org.apache.log4j.Logger;
import org.bird.breeze.common.util.SerializeUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class MybatisRedisCache implements Cache {

	private static final Logger logger = Logger.getLogger(MybatisRedisCache.class);
    
    /** The ReadWriteLock. */
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
 
    private JedisPool jedisPool;
    private static final int DB_INDEX = 1;
    private final String COMMON_CACHE_KEY = "COM:";
    private static final String UTF_8 = "utf-8";
  
    /**
     * 按照一定规则标识key
     */
    private String getKey(Object key) {
        StringBuilder accum = new StringBuilder();
        accum.append(COMMON_CACHE_KEY);
        accum.append(this.id).append(":");
        accum.append(DigestUtils.md5Hex(String.valueOf(key)));
        return accum.toString();
    }
  
    /**
     * redis key规则前缀
     */
    private String getKeys() {
        return COMMON_CACHE_KEY + this.id + ":*";
    }
 
    private String id;
 
    private Properties properties;
 
    {
        properties = getProp();
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(Integer.valueOf(properties
                .getProperty("redis.maxIdle")));
        jedisPool = new JedisPool(config, properties.getProperty("redis.ip"),
                Integer.valueOf(properties.getProperty("redis.port")),
                Integer.valueOf(properties.getProperty("redis.timeout")));
    }
     
    /**
     * 加载项目redis连接属性文件
     */
    private Properties getProp(){
        if(properties == null || properties.isEmpty()){
            String propName = "breeze-config.properties";
            properties = new Properties();
            InputStream is = null;
            BufferedReader bf = null;
            try {
                is= MybatisRedisCache.class.getClassLoader().getResourceAsStream(propName);//将地址加在到文件输入流中
                bf = new BufferedReader(new InputStreamReader(is,"UTF-8"));//转为字符流，设置编码为UTF-8防止出现乱码
                properties.load(bf);//properties对象加载文件输入流
            } catch (UnsupportedEncodingException e) {
                logger.error(propName + "编码格式转换失败，不支持指定编码。" +  e);
            } catch (FileNotFoundException e) {
                logger.error(propName + "属性文件common.properties不存在。" +  e);
            } catch (IOException e) {
                logger.error(propName + "属性文件common.properties读取失败。" +  e);
            } catch (Exception e) {
                logger.error(propName + "属性文件common.properties读取失败。" +  e);
            } finally {
                try {//文件流关闭
                    if(bf != null){
                        bf.close();
                    }
                    if(is != null ){
                        is.close();
                    }
                } catch (IOException e) {
                    logger.error("关闭文件流失败。" +  e);
                }
            }
        }
        return properties;
    }
 
    public MybatisRedisCache() {
    }
 
    public MybatisRedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("必须传入ID");
        }
        logger.debug("MybatisRedisCache:id=" + id);
        this.id = id;
    }
 
    @Override
    public String getId() {
        return this.id;
    }
 
    @Override
    public int getSize() {
        Jedis jedis = null;
        int result = 0;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();
            jedis.select(DB_INDEX);
            Set<byte[]> keys = jedis.keys(getKeys().getBytes(UTF_8));
            if (null != keys && !keys.isEmpty()) {
                result = keys.size();
            }
            logger.debug(this.id+"---->>>>总缓存数:" + result);
        } catch (Exception e) {
            borrowOrOprSuccess = false;
            if (jedis != null)
//                jedisPool.returnBrokenResource(jedis);
            	jedis.close();
        } finally {
            if (borrowOrOprSuccess)
//                jedisPool.returnResource(jedis);
            	jedis.close();
        }
        return result;
 
    }
 
    @Override
    public void putObject(Object key, Object value) {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();
            jedis.select(DB_INDEX);
             
            byte[] keys = getKey(key).getBytes(UTF_8);
            jedis.set(keys, SerializeUtil.serialize(value));
            logger.debug("添加缓存--------"+this.id);
            //getSize();
        } catch (Exception e) {
            borrowOrOprSuccess = false;
            if (jedis != null)
//                jedisPool.returnBrokenResource(jedis);
            	jedis.close();
        } finally {
            if (borrowOrOprSuccess)
//                jedisPool.returnResource(jedis);
            	jedis.close();
        }
 
    }
 
    @Override
    public Object getObject(Object key) {
        Jedis jedis = null;
        Object value = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();
            jedis.select(DB_INDEX);
            value = SerializeUtil.unserialize(jedis.get(getKey(key).getBytes(UTF_8)));
            logger.debug("从缓存中获取-----"+this.id);
            //getSize();
        } catch (Exception e) {
            borrowOrOprSuccess = false;
            if (jedis != null)
//                jedisPool.returnBrokenResource(jedis);
            	jedis.close();
        } finally {
            if (borrowOrOprSuccess)
//                jedisPool.returnResource(jedis);
            	jedis.close();
        }
        return value;
    }
 
    @Override
    public Object removeObject(Object key) {
        Jedis jedis = null;
        Object value = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();
            jedis.select(DB_INDEX);
            value = jedis.del(getKey(key).getBytes(UTF_8));
            logger.debug("LRU算法从缓存中移除-----"+this.id);
            //getSize();
        } catch (Exception e) {
            borrowOrOprSuccess = false;
            if (jedis != null)
//                jedisPool.returnBrokenResource(jedis);
            	jedis.close();
        } finally {
            if (borrowOrOprSuccess)
//                jedisPool.returnResource(jedis);
            	jedis.close();
        }
        return value;
    }
 
    @Override
    public void clear() {
        Jedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = jedisPool.getResource();
            jedis.select(DB_INDEX);
            Set<byte[]> keys = jedis.keys(getKeys().getBytes(UTF_8));
            logger.debug("出现CUD操作，清空对应Mapper缓存======>"+keys.size());
            for (byte[] key : keys) {
                jedis.del(key);
            }
            //下面是网上流传的方法，极大的降低系统性能，没起到加入缓存应有的作用，这是不可取的。
            //jedis.flushDB();
            //jedis.flushAll();
        } catch (Exception e) {
            borrowOrOprSuccess = false;
            if (jedis != null)
//                jedisPool.returnResource(jedis);
            	jedis.close();
        } finally {
            if (borrowOrOprSuccess)
//                jedisPool.returnResource(jedis);
            	jedis.close();
        }
    }
 
    @Override
    public ReadWriteLock getReadWriteLock() {
        return readWriteLock;
    }
     
}
