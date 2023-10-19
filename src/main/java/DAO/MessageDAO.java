package DAO;
import Model.Message;
import Util.ConnectionUtil;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    //message_text is not blank, is under 255 characters, and posted_by refers to a real, existing user.

    private boolean isValidMessage(Message message) {
        String message_text = message.getMessage_text();
        int posted_by = message.getPosted_by();
       
        if (message_text != null && !message_text.trim().isEmpty() && message_text.length() <= 255 && isUserReal(posted_by)){
            return true;
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
