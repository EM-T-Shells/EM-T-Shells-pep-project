package DAO;
import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class AccountDAO {
    public List<Account> getAccount(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> account = new ArrayList<>();
        try{
            String sql = "SELECT * FROM account WHERE username=?, password=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet

    }
    public Account insertAccount(Account account){
    }
}
