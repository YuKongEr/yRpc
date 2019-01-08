package cn.yukonga.yrpc.core.protocol;

/**
 * @author: yukong
 * @date: 2018/12/29 14:33
 */
public interface Parse {

    /**
     * 序列化
     * @param obj 序列化对象
     * @return 字节数组
     */
     <T> byte[] serialize(T obj);
    /**
     * 反序列化
     * @param bytes 字节数组
     * @param cls 转化对象类型
     * @return 转化对象
     */
    <T> T deSerialize(byte[] bytes, Class<T> cls);

}
