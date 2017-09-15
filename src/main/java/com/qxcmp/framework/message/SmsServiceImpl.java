package com.qxcmp.framework.message;

import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudTopic;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.model.BatchSmsAttributes;
import com.aliyun.mns.model.MessageAttributes;
import com.aliyun.mns.model.RawTopicMessage;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.qxcmp.framework.config.SystemConfigService;
import com.qxcmp.framework.core.QXCMPSystemConfigConfiguration;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class SmsServiceImpl implements SmsService {

    private SystemConfigService systemConfigService;

    private String accessKey;

    private String accessSecret;

    private String endPoint;

    private String topicRef;

    private String sign;

    private String captchaTemplateCode;

    public SmsServiceImpl(SystemConfigService systemConfigService) {
        this.systemConfigService = systemConfigService;
        config();
    }

    @Override
    public void send(List<String> phones, String templateCode, Consumer<Map<String, String>> consumer) throws ServiceException {

        Map<String, String> parameters = Maps.newLinkedHashMap();
        consumer.accept(parameters);

        CloudAccount cloudAccount = new CloudAccount(accessKey, accessSecret, endPoint);
        CloudTopic topic = cloudAccount.getMNSClient().getTopicRef(topicRef);

        RawTopicMessage topicMessage = new RawTopicMessage();
        topicMessage.setMessageBody("清醒内容管理平台");

        MessageAttributes messageAttributes = new MessageAttributes();
        BatchSmsAttributes batchSmsAttributes = new BatchSmsAttributes();

        batchSmsAttributes.setFreeSignName(sign);
        batchSmsAttributes.setTemplateCode(templateCode);

        BatchSmsAttributes.SmsReceiverParams smsReceiverParams = new BatchSmsAttributes.SmsReceiverParams();
        parameters.forEach(smsReceiverParams::setParam);

        phones.forEach(phone -> batchSmsAttributes.addSmsReceiver(phone, smsReceiverParams));

        messageAttributes.setBatchSmsAttributes(batchSmsAttributes);

        topic.publishMessage(topicMessage, messageAttributes);
    }

    @Override
    public void sendCaptcha(String phone, String captcha) throws ServiceException {
        send(ImmutableList.of(phone), captchaTemplateCode, stringStringMap -> stringStringMap.put("captcha", captcha));
    }

    @Override
    public void config() {
        accessKey = systemConfigService.getString(QXCMPSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_KEY).orElse(QXCMPSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_KEY_DEFAULT_VALUE);
        accessSecret = systemConfigService.getString(QXCMPSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_SECRET).orElse(QXCMPSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_SMS_ACCESS_SECRET_DEFAULT_VALUE);
        endPoint = systemConfigService.getString(QXCMPSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_SMS_END_POINT).orElse(QXCMPSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_SMS_END_POINT_DEFAULT_VALUE);
        topicRef = systemConfigService.getString(QXCMPSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_SMS_TOPIC_REF).orElse(QXCMPSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_SMS_TOPIC_REF_DEFAULT_VALUE);
        sign = systemConfigService.getString(QXCMPSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_SMS_SIGN).orElse(QXCMPSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_SMS_SIGN_DEFAULT_VALUE);
        captchaTemplateCode = systemConfigService.getString(QXCMPSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_SMS_CAPTCHA_TEMPLATE_CODE).orElse(QXCMPSystemConfigConfiguration.SYSTEM_CONFIG_MESSAGE_SMS_CAPTCHA_TEMPLATE_CODE_DEFAULT_VALUE);
    }

}
