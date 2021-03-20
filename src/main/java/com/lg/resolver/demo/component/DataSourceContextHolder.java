package com.lg.resolver.demo.component;
/**
 * 数据源上下文保存器,便于程序中可以随时取到当前的数据源,
 * 它主要利用 ThreadLocal 封装,因为 ThreadLocal 是线程隔离的,
 * 天然具有线程安全的优势。
 * 这里暴露了 set 和 get、clear 方法，
 * set 方法用于赋值当前的数据源名,
 * get 方法用于获取当前的数据源名称,
 * clear 方法用于清除 ThreadLocal 中的内容,
 * 因为 ThreadLocal 的 key 是 weakReference
 * 是有内存泄漏风险的,通过 remove 方法防止内存泄漏；
 * 利用ThreadLocal封装的保存数据源上线的上下文context
 */
public class DataSourceContextHolder {

    private static final ThreadLocal<String> context = new ThreadLocal<>();

    /**
     * 赋值
     * @param datasourceType
     */
    public static void set(String datasourceType){
        context.set(datasourceType);
    }

    /**
     * 获取值
     * @return
     */
    public static String get(){
        return context.get();
    }


    public static void clear(){
        context.remove();
    }
}
