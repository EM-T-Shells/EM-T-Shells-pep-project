package DAO;
import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.h2.command.Prepared;


public class MessageDAO {
    
    public Message insertMessage(Message message){
        if(isValidMessage(message)){
            Connection connection = ConnectionUtil.getConnection();
            try{
                String sql = "INSERT INTO message(posted_by, message_text, time_posted_epoch) VALUES(?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setInt(1, message.getPosted_by());
                preparedStatement.setString(2, message.getMessage_text());
                preparedStatement.setLong(3, message.getTime_posted_epoch());

                preparedStatement.executeUpdate();
                ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
                if(pkeyResultSet.next()){
                    int generated_message_id = (int)pkeyResultSet.getLong(1);
                    return new Message(
                        generated_message_id,
                        message.getPosted_by(),
                        message.getMessage_text(),
                        message.getTime_posted_epoch());
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        } 
        return null;
    }

    public List<Message> getAllMessages(){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try{
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            ResultSet rs = preparedStatement.executeQuery();

            while(rs.next()){             
                int message_id = rs.getInt("message_id");
                int retrievePosted_by = rs.getInt("posted_by");
                String retrieveMessage_text = rs.getString("message_text");
                long  time_posted_epoch = rs.getLong("time_posted_epoch");             
            
                Message message = new Message(
                    message_id, 
                    retrievePosted_by, 
                    retrieveMessage_text, 
                    time_posted_epoch);

                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public List<Message> getUserMessages(int userId){
        Connection connection = ConnectionUtil.getConnection();
        List<Message> userMessages = new ArrayList<>();
        try{
            String sql="SELECT * FROM Message WHERE posted_by=?";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                int message_id = rs.getInt("message_id");
                String message_text = rs.getString("message_text");
                long  time_posted_epoch = rs.getLong("time_posted_epoch");             
            
                Message userMessage = new Message(
                    message_id,
                    userId, 
                    message_text, 
                    time_posted_epoch);

                userMessages.add(userMessage);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return userMessages;
    }

    public Message getMessageByID(int message_id){
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;
        try{
            if(messageExist(message_id)){
                String sql = "SELECT * FROM Message WHERE message_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, message_id);

                ResultSet resultSet = preparedStatement.executeQuery();
    
                if(resultSet.next()){
                    int retrievedMessage_id = resultSet.getInt("message_id");
                    int retrievePosted_by = resultSet.getInt("posted_by");
                    String retrieveMessage_text = resultSet.getString("message_text");
                    long  time_posted_epoch = resultSet.getLong("time_posted_epoch");             
                
                    message = new Message(
                        retrievedMessage_id, 
                        retrievePosted_by, 
                        retrieveMessage_text, 
                        time_posted_epoch);
    
                    return message;
                }
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message updateMessage(int message_id, Message message){
        Connection connection = ConnectionUtil.getConnection();
        Message updatedMessage = null;
        try{
            if(messageExist(message_id)){
                String sql = "UPDATE Message SET message_text=? WHERE message_id=?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, message.getMessage_text());
                preparedStatement.setInt(2, message_id);

                int rowsUpdated = preparedStatement.executeUpdate();

                if(rowsUpdated>0){
                    updatedMessage = getMessageByID(message_id);
                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return updatedMessage;
    };

    public Message deleteMessage(int message_id) {
        Connection connection = ConnectionUtil.getConnection();
        Message deletedMessage = null;
        try {
            if (messageExist(message_id)) {
                String selectSql = "SELECT * FROM Message WHERE message_id=?";
                PreparedStatement selectStatement = connection.prepareStatement(selectSql);
                selectStatement.setInt(1, message_id);
                
                ResultSet resultSet = selectStatement.executeQuery();
                
                if (resultSet.next()) {
                    deletedMessage = new Message(
                        resultSet.getInt("message_id"),
                        resultSet.getInt("posted_by"),
                        resultSet.getString("message_text"),
                        resultSet.getLong("time_posted_epoch")
                    );
                }
                
                String deleteSql = "DELETE FROM Message WHERE message_id=?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteSql);
                deleteStatement.setInt(1, message_id);
                deleteStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return deletedMessage;
    } 
    
    public boolean messageExist(int message_id) {
        String checkExistSql = "SELECT COUNT(*) FROM Message WHERE message_id=?";
        try {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement checkExistStatement = connection.prepareStatement(checkExistSql);
            checkExistStatement.setInt(1, message_id);
            ResultSet resultSet = checkExistStatement.executeQuery();
            
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return false;
    }

    public boolean isValidMessage(Message message) {
        if(message != null){
            String message_text = message.getMessage_text();
            int posted_by = message.getPosted_by();
           
            if (message_text != null && !message_text.trim().isEmpty() && message_text.length() < 255 && isUserReal(posted_by)){
                return true;
            }
        }
        return false;
    }

    public boolean isUserReal(int posted_by) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, posted_by);
    
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            int count = rs.getInt(1);
    
            return count > 0; 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false; 
        }
    }


}
