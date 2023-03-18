package com.example.chatgpt;

public class modules {

    public static String SEND_BY_ME = "Me";
    public static String SEND_BY_BOT = "Bot";



    String sendby,message;

    public modules(String sendby, String message) {
        this.sendby = sendby;
        this.message = message;
    }

    public String getSendby() {
        return sendby;
    }

    public void setSendby(String sendby) {
        this.sendby = sendby;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
