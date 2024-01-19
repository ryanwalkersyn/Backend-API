package Service;

import Model.Message;
import Model.Account;
import DAO.MessageDAO;
import DAO.AccountDAO;

import java.util.List;
import java.util.ArrayList;

public class MessageService {
    //Associated DAO
    public MessageDAO messageDAO;
    public AccountDAO accountDAO;

    //No Args Constructor
    public MessageService(){
        messageDAO = new MessageDAO();
        accountDAO = new AccountDAO();
    }

    //Constructor for when DAO is provided (for possible mock testing)
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
        this.accountDAO = new AccountDAO();
    }

    /**
     * TODO: Use the MessageDAO to get all Messages
     */
    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    
    public Message addMessage(Message message){
        if (message.message_text != "" &&
            message.message_text.length() <= 255 &&
            accountDAO.getAccountByID(message.posted_by) != null){
                if (getAllMessages().contains(message)){
                    return null;
                } else {
                    return messageDAO.insertMessage(message);
                }
        }
        return null;
    }

    public Message getMessageByID(int message_ID){
        if (getAllMessages().contains(messageDAO.getMessageByID(message_ID))){
            return messageDAO.getMessageByID(message_ID);
        } else {
            return null;
        }
    }

    public int deleteMessageByID(int message_ID){
        if (getMessageByID(message_ID) != null){
            return messageDAO.removeMessageByID(message_ID);
        } else {
            return -1;
        }
    }

    public Message patchMessageByID(Message message){
        if ((message.message_text != "" || message.message_text != null) &&
            message.message_text.length() <= 255 &&
            getAllMessages().contains(messageDAO.getMessageByID(message.message_id))){
                return messageDAO.updateMessage(message);
            } else {
                return null;
            }
    }

    public List<Message> getMessagesByUserID(int account_id){
        if (accountDAO.getAccountByID(account_id) != null){
            return messageDAO.getMessagesByUserID(account_id);
        } else {
            return new ArrayList<Message>();
        }
    }
}
