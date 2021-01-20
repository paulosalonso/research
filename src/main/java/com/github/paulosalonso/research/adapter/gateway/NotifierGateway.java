package com.github.paulosalonso.research.adapter.gateway;

import com.github.paulosalonso.research.domain.Answer;
import com.github.paulosalonso.research.usecase.port.NotifierPort;
import org.springframework.stereotype.Component;

@Component
public class NotifierGateway implements NotifierPort {

    @Override
    public void notifyAnswer(Answer answer) {

    }
}
