package Service;

import Model.Message;
import DAO.MessageDAO;

import static org.mockito.Mockito.mockitoSession;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    public MessageService(){
        messageDAO = new MessageDAO();
    }
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message addMessage(Message message){
        Message addedMessage = messageDAO.insertMessage(message);
        return addedMessage;
    }

    public List<Message> getAllMessages(){
        List<Message> getAllMessages = messageDAO.getAllMessages();
        return getAllMessages;
    }

    public List<Message> getUserMessages(int userId){
        List<Message> getUserMessages = messageDAO.getUserMessages(userId);
        return getUserMessages;
    }

    public Message getMessageByID(int message_id){
        Message getMessageByID = messageDAO.getMessageByID(message_id);
        return getMessageByID;
    }

    public Message updateMessage(int message_id, Message message){
        Message existingMessage = messageDAO.getMessageByID(message_id);
        if (existingMessage != null) {
            String newMessageText = message.getMessage_text();
    
            if (!newMessageText.trim().isEmpty() && newMessageText.length() < 255) {
                existingMessage.setMessage_text(newMessageText);
                messageDAO.updateMessage(message_id, existingMessage);
                return existingMessage;
            }
        }
        return null;
    }

    public Message deleteMessage(int message_id){
        Message deletedMessage = messageDAO.deleteMessage(message_id);
        return deletedMessage;
    }

}
