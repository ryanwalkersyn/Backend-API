package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    
    //Associated Services
    MessageService messageService;
    AccountService accountService;

    //Constructor
    public SocialMediaController(){
        this.messageService = new MessageService();
        this.accountService = new AccountService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        //Account Management Handlers
        app.post("/register", this::postRegisterHandler);
        app.post("/login", this::postLoginHandler);

        //Message Handlers
        app.post("/messages", this::postMessagesHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIDHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIDHandler);
        app.patch("messages/{message_id}", this::patchMessageByIDHandler);
        app.get("/accounts/{account_id}/messages",this::getAllMessagesByUserHandler);
        

        return app;
    }

    private void postRegisterHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount != null){
            ctx.json(mapper.writeValueAsString(addedAccount));
            ctx.status(200);
        } else {
            ctx.status(400);
        }
    }

    private void postLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account getAccount = accountService.getAccountByUsername(account);
        if(getAccount != null){
            ctx.json(mapper.writeValueAsString(getAccount));
            ctx.status(200);
        } else {
            ctx.status(401);
        }
    }

    private void postMessagesHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addMessage = messageService.addMessage(message);
        if(addMessage != null){
            ctx.json(mapper.writeValueAsString(addMessage));
            ctx.status(200);
        } else {
            ctx.status(400);
        }

    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
        ctx.status(200);
        
    }


    private void getMessageByIDHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message getMessage = messageService.getMessageByID(message_id);

        if(getMessage != null){
            ctx.json(mapper.writeValueAsString(getMessage));
            ctx.status(200);
        }
    }


    private void deleteMessageByIDHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message toDelete = messageService.getMessageByID(message_id);
        int row = messageService.deleteMessageByID(message_id);
        if(row > 0){
            ctx.json(mapper.writeValueAsString(toDelete));
        }
        ctx.status(200);
    }


    private void patchMessageByIDHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        message.setMessage_id(message_id);
        Message updateMessage = messageService.patchMessageByID(message);
        if(updateMessage != null){
            ctx.json(mapper.writeValueAsString(updateMessage));
            ctx.status(200);
        } else {
            ctx.status(400);
        }

    }

    private void getAllMessagesByUserHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int account_id = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getMessagesByUserID(account_id);
        ctx.json(messages);
        ctx.status(200);

    }


}
