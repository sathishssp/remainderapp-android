package com.example.krishna.mp3alarm.smsManagermms;

import android.telephony.SmsManager;

import com.example.krishna.mp3alarm.encryptormms.AES;
import com.example.krishna.mp3alarm.resourceManagermms.ResourceManager;

import java.io.Serializable;

/**
 * Created by Shoukhin on 7/5/2018.
 */

public class SMSManager implements Serializable {
    private static final String APP_NAME = "deviceScheduling";
    private String fullMessage;
    private String image;
    private String sound;
    private String message;
    private String timestamp;
    private String receiverPhoneNumber;
    private String phoneStatus;
    private final String SEPARATOR_CHARACTER = ">";
    private final int ACCEPTED_LENGTH_OF_MESSAGE = 6;

    public SMSManager() {

    }

    public SMSManager(String fullMessage) {
        String decryptedMessage = null;
        try {
            decryptedMessage = AES.decrypt(fullMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.fullMessage = decryptedMessage;
    }

    public boolean isFieldsValidated() {
        String splitMessages[];
        try {
            splitMessages = fullMessage.split(SEPARATOR_CHARACTER);
            if (splitMessages.length == ACCEPTED_LENGTH_OF_MESSAGE
                    && splitMessages[0].equals(APP_NAME)) {

                // Checking fields validation
                long tempLongToValidate = Long.parseLong(splitMessages[1]);
                int tempImageID = Integer.parseInt(splitMessages[2]);
                int tempSoundID = Integer.parseInt(splitMessages[3]);
                int tempPhoneStatus = Integer.parseInt(splitMessages[4]);

                if (tempImageID < 0 || tempImageID >= ResourceManager.imageResources.length) {
                    return false;
                }

                if (tempSoundID < 0 || tempSoundID >= ResourceManager.soundResources.length) {
                    return false;
                }
                if (tempPhoneStatus < -1 || tempPhoneStatus >= 3) {
                    return false;
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void splitMessage() {
        String[] splitMessages = fullMessage.split(SEPARATOR_CHARACTER);
        timestamp = splitMessages[1];
        image = splitMessages[2];
        sound = splitMessages[3];
        phoneStatus = splitMessages[4];
        message = splitMessages[5];
    }

    private String getFormattedSMS() {
        String formattedSMS = APP_NAME + SEPARATOR_CHARACTER + timestamp +
                SEPARATOR_CHARACTER + image + SEPARATOR_CHARACTER + sound +
                SEPARATOR_CHARACTER + phoneStatus + SEPARATOR_CHARACTER + message;
        String encryptedMessage = null;

        try {
            encryptedMessage = AES.encrypt(formattedSMS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedMessage;
    }

    public void sendSMS() {
        try {
            String formattedSMS = getFormattedSMS();
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(receiverPhoneNumber, null,
                    formattedSMS, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeCountryCode(String phoneNumber){

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getReceiverPhoneNumber() {
        return receiverPhoneNumber;
    }

    public void setReceiverPhoneNumber(String receiverPhoneNumber) {
        this.receiverPhoneNumber = receiverPhoneNumber;
    }

    public String getPhoneStatus() {
        return phoneStatus;
    }

    public void setPhoneStatus(String phoneStatus) {
        this.phoneStatus = phoneStatus;
    }
}
