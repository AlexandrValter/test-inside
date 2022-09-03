package info.theinside.test.service;

import info.theinside.test.model.dto.MessageDto;

import java.util.Collection;

public interface MessageService {

    Collection<MessageDto> addMessage(MessageDto messageDto, String bearer);
}