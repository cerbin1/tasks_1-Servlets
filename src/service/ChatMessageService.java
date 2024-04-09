package service;

import db.dao.ChatMessageDao;
import service.dto.ChatMessageDto;

import java.util.List;

public class ChatMessageService {
    private final ChatMessageDao chatMessageDao;

    public ChatMessageService(ChatMessageDao chatMessageDao) {
        this.chatMessageDao = chatMessageDao;
    }

    public void createChatMessage(String userId, String taskId, String messageContent) {
        Long nextMessageSequence = chatMessageDao.getNextMessageSequenceForTask(Long.parseLong(taskId));
        chatMessageDao.create(Long.parseLong(userId), Long.parseLong(taskId), messageContent, nextMessageSequence);
    }

    public List<ChatMessageDto> getTaskChatMessages(String taskId) {
        return chatMessageDao.findAllForTaskId(Long.parseLong(taskId));
    }
}
