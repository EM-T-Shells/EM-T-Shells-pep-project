package Service;

import Model.Message;
import DAO.MessageDAO;

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

    public Message getAllMessages(int posted_by, String message_text){
        Message getAllMessages = messageDAO.retrieveAllMessages(posted_by, message_text);
        return getAllMessages;
    }

}
