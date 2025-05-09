package com.catalyst.ProCounsellor.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.catalyst.ProCounsellor.service.AgoraTokenService;

@RestController
@RequestMapping("/api/agora")
public class AgoraController {
	@Autowired
    private AgoraTokenService agoraTokenService;
	
    @GetMapping("/token")
    public String getAgoraToken(@RequestParam String channelName, @RequestParam int uid) {
        return agoraTokenService.generateToken(channelName, uid);
    }
    
    @PostMapping("/{callId}/picked")
    public void pickedCall(@PathVariable String callId) {
    	agoraTokenService.pickedCall(callId);
    }

    //Updates endTime, status, and calculates duration.
    @PostMapping("/{callId}/end")
    public void endCall(@PathVariable String callId) {
    	agoraTokenService.endCall(callId);
    }
    
    @PostMapping("/{callId}/declined")
    public void declinedCall(@PathVariable String callId) {
    	agoraTokenService.declinedCall(callId);
    }
    
    //start call
    @PostMapping("/send")
    public String sendCallNotification(@RequestBody CallNotificationRequest request) throws ExecutionException, InterruptedException {
        agoraTokenService.sendCallNotification(
            request.getReceiverFCMToken(), 
            request.getSenderName(), 
            request.getChannelId(),
            request.getReceiverId(),
            request.getCallType()
        );
        return "✅ Notification sent successfully!";
    }
    
    @PostMapping("/send-cancel-call-notification")
    public String sendCancelCallNotification(@RequestBody CallNotificationRequest request) throws ExecutionException, InterruptedException {
        agoraTokenService.sendVoIPCallNotification(
            request.getVoipToken(), 
            request.getSenderName(), 
            request.getChannelId(),
            request.getReceiverId(),
            request.getCallType(),
            true
        );
        return "✅ Notification cancelled successfully!";
    }
    
    // ✅ Create a class to map JSON request
    public static class CallNotificationRequest {
        private String receiverFCMToken;
        private String voipToken;
        private String senderName;
        private String channelId;
        private String receiverId;
        private String callType;

        // Getters & Setters
        public String getReceiverFCMToken() { return receiverFCMToken; }
        public void setReceiverFCMToken(String receiverFCMToken) { this.receiverFCMToken = receiverFCMToken; }
        
        public String getVoipToken() { return voipToken; }
        public void setVoipToken(String voipToken) { this.voipToken = voipToken; }
        
        public String getSenderName() { return senderName; }
        public void setSenderName(String senderName) { this.senderName = senderName; }

        public String getChannelId() { return channelId; }
        public void setChannelId(String channelId) { this.channelId = channelId; }
        
        public String getReceiverId() { return receiverId; }
        public void setReceiverId(String receiverId) { this.receiverId = receiverId; }

        public String getCallType() { return callType; }
        public void setcallType(String callType) { this.callType = callType; }
    }
}
