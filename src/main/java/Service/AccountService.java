package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    //Associated DAO
    private AccountDAO accountDAO;

    //No Arg Constructor
    public AccountService(){
        accountDAO = new AccountDAO();
    }
    //Constructor for when DAO is provided (possible for mock testing)
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    
    public List<Account> getAllAccounts(){
        return accountDAO.getAllAccounts();
    }
    
   
    public Account addAccount(Account account){
        if (account.getUsername() != "" &&
            account.getPassword().length() >= 4 &&
            !accountDAO.getAllAccounts().contains(accountDAO.getAccountByUsername(account.getUsername()))){
                return accountDAO.insertAccount(account);
            } else {
                return null;
            }
    }
    
    public Account getAccountByUsername (Account account){
        Account accountByUsername = accountDAO.getAccountByUsername(account.getUsername());
        if (accountByUsername == null){
            return null;
        }
        if (account.getPassword().equals(accountByUsername.getPassword())){
            return accountByUsername;
        } else {
            return null;
        }
    }
}



