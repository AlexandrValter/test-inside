package info.theinside.test.model.dto;

import info.theinside.test.model.Message;

//Маппер, который конвертирует объект класса Message в MessageDto и наоборот
public class MessageMapper {

    public static Message toMessage(MessageDto messageDto) {
        return new Message(messageDto.getMessage());
    }

    public static MessageDto toMessageDto(Message message) {
        return new MessageDto(message.getUser().getName(), message.getMessage());
    }
}