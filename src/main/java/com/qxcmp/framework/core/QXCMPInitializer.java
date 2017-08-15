package com.qxcmp.framework.core;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 清醒内容管理平台初始化
 * <p>
 * 在平台启动的最后进行平台初始化操作
 * <p>
 * 加载所有实现了{@link QXCMPConfigurator} 接口的Spring Bean，排序以后分别执行初始化操作
 *
 * @author aaric
 * @see QXCMPConfigurator
 */
@Component
@Slf4j
@AllArgsConstructor
public class QXCMPInitializer implements ApplicationRunner {

    private ApplicationContext applicationContext;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {

        log.info("========== Start QXCMP Initialization ==========");

        applicationContext.getBeansOfType(QXCMPConfigurator.class).entrySet().stream().map(Map.Entry::getValue).sorted(Comparator.comparing(QXCMPConfigurator::order)).collect(Collectors.toList()).forEach(qxcmpConfigurator -> {
            try {
                log.info("==> [{}] Configuring: {}", qxcmpConfigurator.order(), qxcmpConfigurator.name());
                qxcmpConfigurator.config();
            } catch (Exception e) {
                log.error("Can't config {}, cause: {}", qxcmpConfigurator.name(), e.getMessage());
            }
        });

        log.info("========== Finish QXCMP Initialization ==========");
    }
}
