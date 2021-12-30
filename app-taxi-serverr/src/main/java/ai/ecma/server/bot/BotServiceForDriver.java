package ai.ecma.server.bot;

import ai.ecma.server.entity.BotUser;
import ai.ecma.server.entity.Order;
import ai.ecma.server.entity.Route;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

/**
 * BY BAXROMJON on 12.11.2020
 */


public interface BotServiceForDriver {

    SendMessage selectLang(Update update);

    SendMessage welcome(Update update);

    SendMessage getPhoneNumber(Update update);

    SendMessage sendOrderToDriver(BotUser botUser, Order order, List<Route> routes);

    EditMessageText acceptOrder(Update update);

    EditMessageText cancelOrder(Update update);

    EditMessageText arrivedOrder(Update update);

    EditMessageText rejectedOrder(Update update);

    EditMessageText startedOrder(Update update);

    EditMessageText startWaitingOrder(Update update);

    EditMessageText stopWaitingOrder(Update update);

    EditMessageText closedOrder(Update update);

    EditMessageText rateOrder(Update update);

    EditMessageText setStarToOrder(Update update);

    DeleteMessage deleteMessage(Update update);

    EditMessageText setCriteriaToRate(Update update);
}
