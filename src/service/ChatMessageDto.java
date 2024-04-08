package service;

public class ChatMessageDto {
    private final String senderName;
    private final String senderId;
    private final String content;

    public ChatMessageDto(String senderName, String senderId, String content) {
        this.senderName = senderName;
        this.senderId = senderId;
        this.content = content;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }
}
