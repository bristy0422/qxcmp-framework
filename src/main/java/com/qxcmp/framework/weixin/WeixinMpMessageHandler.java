package com.qxcmp.framework.weixin;

import com.qxcmp.framework.weixin.event.*;
import lombok.RequiredArgsConstructor;
import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

import static me.chanjar.weixin.common.api.WxConsts.*;

/**
 * 微信公众号消息处理器
 * <p>
 * 该消息处理器分析微信公众号发来的消息，然后转成对应的微信消息事件发布
 *
 * @author aaric
 */
@Component
@RequiredArgsConstructor
public class WeixinMpMessageHandler implements WxMpMessageHandler {

    private final ApplicationContext applicationContext;

    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager) throws WxErrorException {

        BaseWeixinMessageEvent wechatMessageEvent = convertMessageToEvent(wxMessage, context, wxMpService, sessionManager, null);

        applicationContext.publishEvent(wechatMessageEvent);

        WxMpXmlOutMessage wxMpXmlOutMessage = wechatMessageEvent.getWxMpXmlOutMessage();
        if (Objects.nonNull(wxMpXmlOutMessage)) {
            wxMpXmlOutMessage.setFromUserName(wxMessage.getToUser());
            wxMpXmlOutMessage.setToUserName(wxMessage.getFromUser());
        }
        return wxMpXmlOutMessage;
    }

    /**
     * 将微信公众号消息转换为平台微信消息事件
     *
     * @param wxMessage         收到的消息
     * @param context           上下本
     * @param wxMpService       微信服务
     * @param sessionManager    会话管理
     * @param wxMpXmlOutMessage 要返回的消息
     * @return 平台微信消息事件
     */
    private BaseWeixinMessageEvent convertMessageToEvent(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService, WxSessionManager sessionManager, WxMpXmlOutMessage wxMpXmlOutMessage) {
        switch (wxMessage.getMsgType()) {
            case XML_MSG_TEXT:
                return new WeixinMpTextMessage(wxMessage, context, wxMpService, sessionManager, wxMpXmlOutMessage);
            case XML_MSG_IMAGE:
                return new WeixinMpImageMessage(wxMessage, context, wxMpService, sessionManager, wxMpXmlOutMessage);
            case XML_MSG_VOICE:
                return new WeixinMpVoiceMessage(wxMessage, context, wxMpService, sessionManager, wxMpXmlOutMessage);
            case XML_MSG_SHORTVIDEO:
                return new WeixinMpShortVideoMessage(wxMessage, context, wxMpService, sessionManager, wxMpXmlOutMessage);
            case XML_MSG_VIDEO:
                return new WeixinMpVideoMessage(wxMessage, context, wxMpService, sessionManager, wxMpXmlOutMessage);
            case XML_MSG_LOCATION:
                return new WeixinMpLocationMessage(wxMessage, context, wxMpService, sessionManager, wxMpXmlOutMessage);
            case XML_MSG_LINK:
                return new WeixinMpLinkMessage(wxMessage, context, wxMpService, sessionManager, wxMpXmlOutMessage);
            case XML_MSG_EVENT: {
                switch (wxMessage.getEvent()) {
                    case EVT_SUBSCRIBE:
                        return new WeixinMpSubscribeEvent(wxMessage, context, wxMpService, sessionManager, wxMpXmlOutMessage);
                    case EVT_UNSUBSCRIBE:
                        return new WeixinMpUnsubscribeEvent(wxMessage, context, wxMpService, sessionManager, wxMpXmlOutMessage);
                    case EVT_SCAN:
                        return new WeixinMpScanEvent(wxMessage, context, wxMpService, sessionManager, wxMpXmlOutMessage);
                    case EVT_LOCATION:
                        return new WeixinMpLocationEvent(wxMessage, context, wxMpService, sessionManager, wxMpXmlOutMessage);
                    case EVT_CLICK:
                        return new WeixinMpMenuClickEvent(wxMessage, context, wxMpService, sessionManager, wxMpXmlOutMessage);
                    case EVT_VIEW:
                        return new WeixinMpMenuViewEvent(wxMessage, context, wxMpService, sessionManager, wxMpXmlOutMessage);
                    default:
                        return new WeixinMpDefaultMessage(wxMessage, context, wxMpService, sessionManager, wxMpXmlOutMessage);
                }
            }
            default:
                return new WeixinMpDefaultMessage(wxMessage, context, wxMpService, sessionManager, wxMpXmlOutMessage);
        }
    }
}
