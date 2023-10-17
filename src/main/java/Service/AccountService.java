package Service;
import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;
    public AccountService(){
        accountDAO = new AccountDAO();
    }
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    public Account getAccount(String username, String password){
        Account account = accountDAO.getAccount(username, password);
        return account;
    }
    public Account addAccount(Account account){
        Account addedAccount = accountDAO.insertAccount(account);
        return addedAccount;
    }
}
