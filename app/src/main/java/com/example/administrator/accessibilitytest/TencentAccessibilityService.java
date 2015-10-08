package com.example.administrator.accessibilitytest;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Notification;
import android.app.PendingIntent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.util.List;

/**
 * Created by Administrator on 15-10-7.
 */
public class TencentAccessibilityService extends AccessibilityService{
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        switch (event.getEventType()){
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                List<CharSequence> texts = event.getText();
                if(!texts.isEmpty()){
                    for(CharSequence charSequence : texts){
                        String content = charSequence.toString();
                        if(content.contains("叶云")){
                            if (event.getParcelableData() != null
                                    &&
                                    event.getParcelableData() instanceof Notification) {
                                Notification notification = (Notification) event.getParcelableData();
                                PendingIntent pendingIntent = notification.contentIntent;
                                try {
                                    pendingIntent.send();
                                } catch (PendingIntent.CanceledException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                String className = event.getClassName().toString();
                if(className.contains("com.tencent.mobileqq")){
                    AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
                    if(accessibilityNodeInfo != null){
                        List<AccessibilityNodeInfo> accessibilityNodeInfos = accessibilityNodeInfo
                                .findAccessibilityNodeInfosByText("叶云");
                        for(AccessibilityNodeInfo info : accessibilityNodeInfos){
                            info.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            AccessibilityNodeInfo parent = info.getParent();
                            while(parent != null){
                                if(parent.isClickable()){
                                    parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                    break;
                                }
                                parent = parent.getParent();
                            }
                        }

                    }

                }
                break;
        }

    }
    @Override
    protected void onServiceConnected() {
        AccessibilityServiceInfo info = getServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_SPOKEN;
        info.notificationTimeout = 100;
        setServiceInfo(info);
        info.packageNames = new String[]{"com.tencent.mobileqq"};
        setServiceInfo(info);
        super.onServiceConnected();
    }
    @Override
    public void onInterrupt() {

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
