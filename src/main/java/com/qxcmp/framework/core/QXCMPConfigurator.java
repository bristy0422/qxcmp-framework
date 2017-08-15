package com.qxcmp.framework.core;

/**
 * 清醒内容管理平台配置接口
 * <p>
 * 平台会在启动时候加载所有实现该接口的配置，并执行配置
 *
 * @author aaric
 */
public interface QXCMPConfigurator {

    /**
     * 执行相应配置
     *
     * @throws Exception 抛出任何异常会中断当前配置，继续跳到下一个配置
     */
    void config() throws Exception;

    /**
     * 配置名称
     *
     * @return 配置名称
     */
    default String name() {
        return this.getClass().getSimpleName();
    }

    /**
     * 配置执行顺序
     * <p>
     * 值越小越先执行
     *
     * @return 配置执行顺序
     */
    default int order() {
        return 0;
    }
}
