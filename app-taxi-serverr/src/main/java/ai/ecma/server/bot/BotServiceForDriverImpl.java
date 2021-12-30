package ai.ecma.server.bot;

import ai.ecma.server.entity.*;
import ai.ecma.server.entity.enums.LangEnum;
import ai.ecma.server.entity.enums.OrderStatus;
import ai.ecma.server.entity.enums.RoleName;
import ai.ecma.server.exceptions.ResourceNotFoundException;
import ai.ecma.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.sql.Timestamp;
import java.util.*;

/**
 * BY BAXROMJON on 12.11.2020
 */

@Service
public class BotServiceForDriverImpl implements BotServiceForDriver {
    @Autowired
    BotUserRepository botUserRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    MissedOrderRepository missedOrderRepository;
    @Autowired
    CarRepository carRepository;
    @Autowired
    CriteriaRepository criteriaRepository;
    @Autowired
    RateRepository rateRepository;

    /**
     * HAYDOVCHI BIRINCHI SAFAR KIRGANDA TILNI TANLASH KNOPKALARINI BERIB YUBORAMIZ
     *
     * @param update
     * @return
     */
    @Override
    public SendMessage selectLang(Update update) {
        Long chatId = update.getMessage().getChatId();
        Optional<BotUser> optionalBotUser = botUserRepository.findByChatId(chatId);
        if (!optionalBotUser.isPresent()) {
            BotUser botUser = new BotUser();
            botUser.setChatId(chatId);
            botUser.setState(BotState.SELECT_LANG);
            botUserRepository.save(botUser);
        }
        SendMessage sendMessage = makeSendMessageMarkdownHasMessage(update);
        InlineKeyboardMarkup inlineKeyboardMarkup = makeLangInline();
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(BotConstant.SELECT_UZ)
                .append("\n")
                .append(BotConstant.SELECT_OZ)
                .append("\n")
                .append(BotConstant.SELECT_RU)
                .append("\n")
                .append(BotConstant.SELECT_EN);
        sendMessage.setText(stringBuilder.toString());
        return sendMessage;
    }

    /**
     * HAYDOVCHI TILNI TANLAGACH, UNGA SALOM BERAMIZ VA SHARE CONTACT BUTTON BERAMIZ
     *
     * @param update
     * @return
     */
    @Override
    public SendMessage welcome(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        BotUser botUser = botUserRepository.findByChatId(chatId).orElseThrow(() -> new ResourceNotFoundException("botUser", "chatId", chatId));
        String data = update.getCallbackQuery().getData();
        String langSubstr = data.substring(data.indexOf("Lang#") + 5);
        botUser.setLang(LangEnum.valueOf(langSubstr));
        botUser.setState(BotState.SHARE_CONTACT);
        botUserRepository.save(botUser);
        SendMessage sendMessage = makeSendMessageMarkdownHasCallback(update);
        StringBuilder builder = new StringBuilder();
        LangEnum lang = botUser.getLang();
        builder
                .append(lang.equals(LangEnum.UZ) ? BotConstant.WELCOME_DRIVER_UZ
                        : lang.equals(LangEnum.OZ) ? BotConstant.WELCOME_DRIVER_OZ
                        : lang.equals(LangEnum.RU) ? BotConstant.WELCOME_DRIVER_RU :
                        BotConstant.WELCOME_DRIVER_EN);
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardButton keyboardButton = new KeyboardButton();
        keyboardButton.setText("Share your number >").setRequestContact(true);
        keyboardFirstRow.add(keyboardButton);
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setText(builder.toString());
        return sendMessage;
    }

    /**
     * HAYDOVCHI SHARE CONTACT QILGACH, UNGA CABINETIGA KIRITAMIZ. AGAR HAYDOVCHI BO'LMASA SURING DEYMIZ
     *
     * @param update
     * @return
     */
    @Override
    public SendMessage getPhoneNumber(Update update) {
        Long chatId = update.getMessage().getChatId();
        SendMessage sendMessage = makeSendMessageMarkdownHasMessage(update);
        BotUser botUser = botUserRepository.findByChatId(chatId).orElseThrow(() -> new ResourceNotFoundException("botUser", "chatId", chatId));
        String phoneNumber = update.getMessage().getContact().getPhoneNumber();
        phoneNumber = phoneNumber.startsWith("+") ? phoneNumber : "+" + phoneNumber;
        Optional<User> optionalDriver = userRepository.findByPhoneNumberAndRoleName(phoneNumber, RoleName.ROLE_DRIVER.name());
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove());
        if (!optionalDriver.isPresent()) {
            sendMessage.setText(BotConstant.NOT_DRIVER);
            return sendMessage;
        }
        botUser.setPhoneNumber(phoneNumber);
        botUser.setState(BotState.DRIVER_CABINET);
        botUserRepository.save(botUser);
        sendMessage.setText(BotConstant.WELCOME_CABINET_UZ);
        return sendMessage;
    }

    /**
     * HAYDOVCHIG YANGI ZAKAZ YUBORISH
     *
     * @param botUser
     * @param order
     * @param routes
     * @return
     */
    @Override
    public SendMessage sendOrderToDriver(BotUser botUser, Order order, List<Route> routes) {
        SendMessage xabar = new SendMessage();
        xabar.setParseMode(ParseMode.MARKDOWN);
        xabar.setChatId(botUser.getChatId());
        ///INLINE BUTTON YASHASH
        InlineKeyboardMarkup buttonchalar = makeInlineButtonForOrder(order);
        //BUTTONNI XABARGA QO'SHISH
        xabar.setReplyMarkup(buttonchalar);
        xabar.setText(getAboutOrder(order, routes).toString());
        return xabar;
    }

    /**
     * HAYDOVCHI ACCEPT QILSA ZAKAZNI OFFORMIT QILAMIZ. AGAR BU ZAKAZ QABUL QILISH MUDDATI O'TGAN BO'LSA, UNGA UZR AYTAMIZ
     *
     * @param update
     * @return
     */
    @Override
    public EditMessageText acceptOrder(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        EditMessageText editMessageText = makeEditMessage(update);
        Order order = getOrderFromUpdateCallbackQueryData(update, 7);
        BotUser botUser = botUserRepository.findByChatId(chatId).orElseThrow(() -> new ResourceNotFoundException("botUser", "chatId", chatId));
        String phoneNumber = botUser.getPhoneNumber();
        User driver = userRepository.findByPhoneNumberAndRoleName(phoneNumber, RoleName.ROLE_DRIVER.name()).orElseThrow(() -> new ResourceNotFoundException("botUser", "chatId", chatId));
        Car car = carRepository.findByDriverId(driver.getId()).orElseThrow(() -> new ResourceNotFoundException("botUser", "chatId", chatId));
        boolean missed = missedOrderRepository.existsByCarIdAndOrderId(car.getId(), order.getId());
        List<Route> routes = order.getRoutes();
        StringBuilder aboutOrder = getAboutOrder(order, routes);
        if (missed) {
            aboutOrder
                    .append("\n")
                    .append("Oka bu zakaz o'tib ketdi");
            editMessageText.setText(aboutOrder.toString());
            return editMessageText;
        }
        order.setCar(car);
        order.setStatus(OrderStatus.WAITING);
        order.setAcceptedAt(new Timestamp(System.currentTimeMillis()));
        orderRepository.save(order);
        User user = userRepository.findById(order.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("user", "id", order.getCreatedBy()));
        aboutOrder
                .append("\n")
                .append("*Mijoz telefon raqami: *")
                .append(user.getPhoneNumber());
        editMessageText.setText(aboutOrder.toString());
        InlineKeyboardMarkup inlineKeyboardMarkup = makeInlineArrivedAndReject(order);
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        return editMessageText;
    }

    /**
     * CANCEL QILSA BALINI KAMAYTIRAMIZ, AGAR AVVAL SHU ZAKAZ UCHUN BALINI KAMAYTIRMAGAN BO'LSAK
     *
     * @param update
     * @return
     */
    @Override
    public EditMessageText cancelOrder(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        EditMessageText editMessageText = makeEditMessage(update);
        Order order = getOrderFromUpdateCallbackQueryData(update, 7);
        List<Route> routes = order.getRoutes();
        StringBuilder aboutOrder = getAboutOrder(order, routes);
        aboutOrder
                .append("\n")
                .append("Sizdan bekor qilindi");
        editMessageText.setText(aboutOrder.toString());
        return editMessageText;
    }

    /**
     * KLIENTNI BOSHLANG'ICH MANZILIGA KELGANDA BOSADI
     *
     * @param update
     * @return
     */
    @Override
    public EditMessageText arrivedOrder(Update update) {
        EditMessageText editMessageText = makeEditMessage(update);
        Order order = getOrderFromUpdateCallbackQueryData(update, 8);
        StringBuilder aboutOrder = getAboutOrder(order, order.getRoutes());
        User client = userRepository.findById(order.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("user", "id", order.getCreatedBy()));
        aboutOrder.append("\n")
                .append("*Mijoz telefon raqami: *")
                .append(client.getPhoneNumber());
        if (order.getStatus().equals(OrderStatus.CANCELED)) {
            aboutOrder
                    .append("❌❌❌❌❌❌❌❌❌❌");
            editMessageText.setText(aboutOrder.toString());
            return editMessageText;
        }
        order.setArrivedAt(new Timestamp(System.currentTimeMillis()));
        order.setStatus(OrderStatus.IN_PROGRESS);
        orderRepository.save(order);
        InlineKeyboardMarkup knopka = makeInlineStartAndRejected(order);
        editMessageText.setReplyMarkup(knopka);
        editMessageText.setText(aboutOrder.toString());
        return editMessageText;
    }


    /**
     * ZAKAZNI RAD ETISH*****(KO'P KOD BOR)
     *
     * @param update
     * @return
     */
    @Override
    public EditMessageText rejectedOrder(Update update) {
        return null;
    }


    /**
     * KLIENTNI OLIB YURISHNI BISHLADI. BU YERDA SEKUNDOMERNI O'CHIRAMIZ
     *
     * @param update
     * @return
     */
    @Override
    public EditMessageText startedOrder(Update update) {
        EditMessageText editMessageText = makeEditMessage(update);
        Order order = getOrderFromUpdateCallbackQueryData(update, 6);
        StringBuilder aboutOrder = getAboutOrder(order, order.getRoutes());
        User client = userRepository.findById(order.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("user", "id", order.getCreatedBy()));
        aboutOrder.append("\n")
                .append("*Mijoz telefon raqami: *")
                .append(client.getPhoneNumber());
        if (order.getStatus().equals(OrderStatus.CANCELED)) {
            aboutOrder
                    .append("❌❌❌❌❌❌❌❌❌❌");
            editMessageText.setText(aboutOrder.toString());
            return editMessageText;
        }
        order.setStartedAt(new Timestamp(System.currentTimeMillis()));
        orderRepository.save(order);
        InlineKeyboardMarkup knopkalar = makeInlineStartWaitingAndClosed(order);
        editMessageText.setReplyMarkup(knopkalar);
        editMessageText.setText(aboutOrder.toString());
        return editMessageText;
    }


    /**
     * YO'LGA CHIQQANDAN KEYINGI KUTISHLAR UCHUN SEKUNDOMERNI YOQISH
     *
     * @param update
     * @return
     */
    @Override
    public EditMessageText startWaitingOrder(Update update) {
        return null;
    }

    /**
     * YO'LDA TO'XTAGANLIGI UCHUN YOQQAN SEKUNDOMERNI O'CHIRISH
     *
     * @param update
     * @return
     */
    @Override
    public EditMessageText stopWaitingOrder(Update update) {
        return null;
    }

    /**
     * KLIENTNI ELTIB QO'YGACH, ZAKAZNI YOPISH
     *
     * @param update
     * @return
     */
    @Override
    public EditMessageText closedOrder(Update update) {
        EditMessageText editMessageText = makeEditMessage(update);
        Order order = getOrderFromUpdateCallbackQueryData(update, 7);
        StringBuilder aboutOrder = getAboutOrder(order, order.getRoutes());
        User client = userRepository.findById(order.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("user", "id", order.getCreatedBy()));
        aboutOrder.append("\n")
                .append("*Mijoz telefon raqami: *")
                .append(client.getPhoneNumber());
        editMessageText.setText(aboutOrder.toString());
        if (!order.getStatus().equals(OrderStatus.CLOSED)) {
            order.setStatus(OrderStatus.CLOSED);
            order.setClosedAt(new Timestamp(System.currentTimeMillis()));
            orderRepository.save(order);
            InlineKeyboardMarkup knopka = makeInlineRate(order);
            editMessageText.setReplyMarkup(knopka);
        }
        return editMessageText;
    }


    /**
     * ZAKZNI YOPGACH KLEINTGA BAHO BERISH
     *
     * @param update
     * @return
     */
    @Override
    public EditMessageText rateOrder(Update update) {
        EditMessageText editMessageText = makeEditMessage(update);
        Order order = getOrderFromUpdateCallbackQueryData(update, 5);
        StringBuilder aboutOrder = getAboutOrder(order, order.getRoutes());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> lists = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonList1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonList2 = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
            inlineKeyboardButton1.setText("⭐");
            inlineKeyboardButton1.setCallbackData("Stars#" + i + "#" + order.getId());
            keyboardButtonList1.add(inlineKeyboardButton1);
        }
        lists.add(keyboardButtonList1);
        lists.add(keyboardButtonList2);
        inlineKeyboardMarkup.setKeyboard(lists);
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        editMessageText.setText(aboutOrder.toString());
        return editMessageText;
    }

    /**
     * YULDUZCHALAR BOSILGANDA SONIGA QARAB CRITERIA CHIQARAMIZ
     *
     * @param update
     * @return
     */
    @Override
    public EditMessageText setStarToOrder(Update update) {
        EditMessageText editMessageText = makeEditMessage(update);
        Order order = getOrderFromUpdateCallbackQueryData(update, 8);
        StringBuilder aboutOrder = getAboutOrder(order, order.getRoutes());
        User client = userRepository.findById(order.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("user", "id", order.getCreatedBy()));
        aboutOrder.append("\n")
                .append("*Mijoz telefon raqami: *")
                .append(client.getPhoneNumber());
        String data = update.getCallbackQuery().getData();
        int ball = Integer.parseInt(data.substring(data.indexOf("#") + 1, data.lastIndexOf("#")));
        aboutOrder
                .append("\n")
                .append("*Baho: *")
                .append(ball);
        editMessageText.setText(aboutOrder.toString());
        Rate rate = new Rate();
        rate.setCreatedBy(orderRepository.getDriverIdByOrderId(order.getId()));
        rate.setStarCount(ball);
        rate.setOrder(order);
        rateRepository.save(rate);
        InlineKeyboardMarkup inlineKeyboardMarkup = makeInlineCriteria(ball, order);
        editMessageText.setReplyMarkup(inlineKeyboardMarkup);
        return editMessageText;
    }


    @Override
    public EditMessageText setCriteriaToRate(Update update) {
        EditMessageText editMessageText = makeEditMessage(update);
        String data = update.getCallbackQuery().getData();
        int beginIndex = data.indexOf("#") + 1;
        int lastIndex = data.lastIndexOf("#");
        Integer criteriaId = Integer.parseInt(data.substring(beginIndex, lastIndex));
        Order order = getOrderFromUpdateCallbackQueryData(update, lastIndex+1);
        StringBuilder aboutOrder = getAboutOrder(order, order.getRoutes());
        User client = userRepository.findById(order.getCreatedBy()).orElseThrow(() -> new ResourceNotFoundException("user", "id", order.getCreatedBy()));
        aboutOrder.append("\n")
                .append("*Mijoz telefon raqami: *")
                .append(client.getPhoneNumber());
        aboutOrder
                .append("\n");
        editMessageText.setText(aboutOrder.toString());
        Rate rate = rateRepository.findByOrderIdAndDriverRate(order.getId()).orElseThrow(() -> new ResourceNotFoundException("rate", "orderIdAndDriver", order.getId()));
        List<Criteria> criteriaList = rate.getCriteriaList();
        Criteria criteria = criteriaRepository.findById(criteriaId).orElseThrow(() -> new ResourceNotFoundException("criteria", "id", criteriaId));
        criteriaList.add(criteria);
        rate.setCriteriaList(criteriaList);
        rateRepository.save(rate);
        return editMessageText;
    }

    public DeleteMessage deleteMessage(Update update) {
        return new DeleteMessage(update.getCallbackQuery().getMessage().getChatId(), update.getCallbackQuery().getMessage().getMessageId());
    }

    public SendMessage makeSendMessageForOrder(BotUser botUser, Order order, List<Route> routes) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        sendMessage.setChatId(botUser.getChatId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(BotConstant.ALL_SUM_UZ)
                .append(order.getFare())
                .append("\n")
                .append(BotConstant.FROM_LOCATION)
                .append(routes.get(0).getFromLat())
                .append(", ")
                .append(routes.get(0).getFromLon())
                .append("\n")
                .append(BotConstant.TO_LOCATION)
                .append(routes.get(routes.size() - 1).getToLat())
                .append(", ")
                .append(routes.get(routes.size() - 1).getToLon());
        InlineKeyboardMarkup inlineKeyboardMarkup = makeInlineButtonForOrder(order);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setText(stringBuilder.toString());
        return sendMessage;
    }


    ////////PRIVATE METHODS//////////////

    private SendMessage makeSendMessageHtmlHasMessage(Update update) {
        return new SendMessage()
                .setChatId(update.getMessage().getChatId()).setParseMode(ParseMode.HTML);
    }

    private SendMessage makeSendMessageMarkdownHasMessage(Update update) {
        return new SendMessage()
                .setChatId(update.getMessage().getChatId()).setParseMode(ParseMode.MARKDOWN);
    }

    private SendMessage makeSendMessageHtmlHasCallback(Update update) {
        return new SendMessage()
                .setChatId(update.getCallbackQuery().getMessage().getChatId()).setParseMode(ParseMode.HTML);
    }

    private SendMessage makeSendMessageMarkdownHasCallback(Update update) {
        return new SendMessage()
                .setChatId(update.getCallbackQuery().getMessage().getChatId()).setParseMode(ParseMode.MARKDOWN);
    }

    private InlineKeyboardMarkup makeLangInline() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        List<InlineKeyboardButton> thirdRow = new ArrayList<>();
        List<InlineKeyboardButton> fourthRow = new ArrayList<>();
        InlineKeyboardButton uz = new InlineKeyboardButton();
        InlineKeyboardButton oz = new InlineKeyboardButton();
        InlineKeyboardButton ru = new InlineKeyboardButton();
        InlineKeyboardButton en = new InlineKeyboardButton();
        uz.setText(BotConstant.LANG_UZ);
        oz.setText(BotConstant.LANG_OZ);
        ru.setText(BotConstant.LANG_RU);
        en.setText(BotConstant.LANG_EN);
        uz.setCallbackData("Lang#" + LangEnum.UZ);
        oz.setCallbackData("Lang#" + LangEnum.OZ);
        ru.setCallbackData("Lang#" + LangEnum.RU);
        en.setCallbackData("Lang#" + LangEnum.EN);
        firstRow.add(uz);
        secondRow.add(oz);
        thirdRow.add(ru);
        fourthRow.add(en);
        rows.addAll(Arrays.asList(firstRow, secondRow, thirdRow, fourthRow));
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup makeInlineArrivedAndReject(Order order) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        InlineKeyboardButton reject = new InlineKeyboardButton();
        InlineKeyboardButton arrived = new InlineKeyboardButton();
        reject.setText(BotConstant.REJECT_ORDER_UZ);
        arrived.setText(BotConstant.ARRIVED_ORDER_UZ);
        arrived.setCallbackData("Arrived#" + order.getId());
        reject.setCallbackData("Reject#" + order.getId());
        firstRow.add(arrived);
        secondRow.add(reject);
        rows.addAll(Arrays.asList(firstRow, secondRow));
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup makeInlineStartAndRejected(Order order) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        InlineKeyboardButton reject = new InlineKeyboardButton();
        InlineKeyboardButton started = new InlineKeyboardButton();
        reject.setText(BotConstant.REJECT_ORDER_UZ);
        started.setText(BotConstant.STARTED_ORDER_UZ);
        started.setCallbackData("Start#" + order.getId());
        reject.setCallbackData("Reject#" + order.getId());
        firstRow.add(started);
        secondRow.add(reject);
        rows.addAll(Arrays.asList(firstRow, secondRow));
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup makeInlineStartWaitingAndClosed(Order order) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        InlineKeyboardButton startWaiting = new InlineKeyboardButton();
        InlineKeyboardButton closed = new InlineKeyboardButton();
        startWaiting.setText(BotConstant.START_WAITING_ORDER_UZ);
        closed.setText(BotConstant.CLOSED_ORDER_UZ);
        closed.setCallbackData("Closed#" + order.getId());
        startWaiting.setCallbackData("StartWaiting#" + order.getId());
        firstRow.add(startWaiting);
        secondRow.add(closed);
        rows.addAll(Arrays.asList(firstRow, secondRow));
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup makeInlineRate(Order order) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        InlineKeyboardButton rate = new InlineKeyboardButton();
        rate.setText(BotConstant.RATE_ORDER_UZ);
        rate.setCallbackData("Rate#" + order.getId());
        firstRow.add(rate);
        rows.add(firstRow);
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup makeInlineCriteria(int starCount, Order order) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<Criteria> criteriaList = criteriaRepository.findAll();
        for (Criteria criteria : criteriaList) {
            List<InlineKeyboardButton> firstRow = new ArrayList<>();
            InlineKeyboardButton cr = new InlineKeyboardButton();
            if (criteria.isForClient()) {
                if (starCount > 3) {
                    if (!criteria.isNegative()) {
                        cr.setText(criteria.getName());
                        cr.setCallbackData("Criteria#" + criteria.getId() + "#" + order.getId());
                        firstRow.add(cr);
                        rows.add(firstRow);
                    }
                } else {
                    if (criteria.isNegative()) {
                        cr.setText(criteria.getName());
                        cr.setCallbackData("Criteria#" + criteria.getId() + "#" + order.getId());
                        firstRow.add(cr);
                        rows.add(firstRow);
                    }
                }
            }
        }
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    private InlineKeyboardMarkup makeInlineButtonForOrder(Order order) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        InlineKeyboardButton accept = new InlineKeyboardButton();
        InlineKeyboardButton cancel = new InlineKeyboardButton();
        accept.setText(BotConstant.ACCEPT_ORDER_UZ);
        cancel.setText(BotConstant.CANCEL_ORDER_UZ);
        accept.setCallbackData("Accept#" + order.getId());
        cancel.setCallbackData("Cancel#" + order.getId());
        firstRow.add(accept);
        secondRow.add(cancel);
        rows.addAll(Arrays.asList(firstRow, secondRow));
        inlineKeyboardMarkup.setKeyboard(rows);
        return inlineKeyboardMarkup;
    }

    private StringBuilder getAboutOrder(Order order, List<Route> routes) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(BotConstant.ALL_SUM_UZ)
                .append(String.format("%,.0f", order.getFare()).replace(",", " "))
                .append("\n")
                .append(BotConstant.FROM_LOCATION)
                .append(routes.get(0).getFromLat())
                .append(", ")
                .append(routes.get(0).getFromLon())
                .append("\n")
                .append(BotConstant.TO_LOCATION)
                .append(routes.get(routes.size() - 1).getToLat())
                .append(", ")
                .append(routes.get(routes.size() - 1).getToLon());
        return stringBuilder;
    }

    private EditMessageText makeEditMessage(Update update) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setParseMode(ParseMode.MARKDOWN);
        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessageText.setChatId(update.getCallbackQuery().getMessage().getChatId());
        return editMessageText;
    }

    private Order getOrderFromUpdateCallbackQueryData(Update update, int index) {
        String data = update.getCallbackQuery().getData();
        data = data.substring(index);
        String finalData = data;
        return orderRepository.findById(UUID.fromString(data)).orElseThrow(() -> new ResourceNotFoundException("order", "id", finalData));
    }
}
