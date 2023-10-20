package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import static org.mockito.ArgumentMatchers.nullable;

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
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/login", this::postAccountHandler);
        app.post("/register", this::postRegisterHandler);

        app.post("/messages", this::postMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);

        return app;

    }

    private void postAccountHandler(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Account verifiedAccount = accountService.getAccount(account.getUsername(), account.getPassword());
        if(verifiedAccount != null){
            ctx.json(verifiedAccount);
            ctx.status(200);
        } else{
            ctx.status(401);
        }
    }

    private void postRegisterHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount==null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(addedAccount));
        }
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage==null){
            ctx.status(400);
        }else{
            ctx.json(mapper.writeValueAsString(addedMessage));
        }
    }

    private void getAllMessagesHandler(Context ctx){
        List<Message> messages = messageService.getAllMessages();
        ctx.json(messages);
    }

}