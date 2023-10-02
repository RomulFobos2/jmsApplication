package com.tander.jms.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageServiceBean {
    String textMessage;

    public String createTextMessage(){
        return  textMessage;
    }
}
