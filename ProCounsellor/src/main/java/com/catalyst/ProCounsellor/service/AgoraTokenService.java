package com.catalyst.ProCounsellor.service;

import org.springframework.stereotype.Service;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import io.agora.media.RtcTokenBuilder2;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.Date;

@Service
public class AgoraTokenService {

    private final FirebaseDatabase firebaseDatabase;
    private final DatabaseReference callRef;

    private String appId = System.getenv("AGORA_APP_ID");
    private String appCertificate = System.getenv("AGORA_APP_CERTIFICATE");
    private String tokenExpiry2 = System.getenv("AGORA_TOKEN_EXPIRY");
    private int tokenExpiry = Integer.parseInt(tokenExpiry2);

    public AgoraTokenService(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
        this.callRef = firebaseDatabase.getReference("agora_call_signaling");
    }

    public String generateToken(String channelName, int uid) {
        RtcTokenBuilder2 tokenBuilder = new RtcTokenBuilder2();
        int expireTimestamp = (int) ((new Date().getTime() / 1000) + tokenExpiry);

        return tokenBuilder.buildTokenWithUid(
                appId,
                appCertificate,
                channelName,
                uid,
                RtcTokenBuilder2.Role.ROLE_PUBLISHER,
                expireTimestamp,
                expireTimestamp
        );
    }

    public void sendCallNotification(String receiverFCMToken, String senderName, String channelId, String receiverId, String callType) {
        callRef.child(receiverId).setValueAsync(new CallSession(senderName, channelId, callType));

//        Notification notification = Notification.builder()
//                .setTitle("Incoming Call")
//                .setBody(senderName + " is calling you...")
//                .build();
//
//        Message message = Message.builder()
//                .setNotification(notification)
//                .putData("type", "incoming_call")
//                .putData("channelId", channelId)
//                .putData("callerName", senderName)
//                .setToken(receiverFCMToken)
//                .build();
//
//        try {
//            FirebaseMessaging.getInstance().send(message);
//            System.out.println("✅ Call Notification Sent!");
//        } catch (Exception e) {
//            System.err.println("❌ Error sending notification: " + e.getMessage());
//        }
    }

    static class CallSession {
        public String callerName;
        public String channelId;
        public String callType;

        public CallSession() {}
        public CallSession(String callerName, String channelId, String callType) {
            this.callerName = callerName;
            this.channelId = channelId;
            this.callType = callType;
        }
    }
}
