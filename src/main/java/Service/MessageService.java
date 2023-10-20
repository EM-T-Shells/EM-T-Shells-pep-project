package Service;

import Model.Message;
import DAO.MessageDAO;

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

    public Message deleteMessage(int message_id){
        Message deletedMessage = messageDAO.deleteMessage(message_id);
        return deletedMessage;
    }

}
