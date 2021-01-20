package com.github.paulosalonso.research.usecase.port;

import com.github.paulosalonso.research.domain.Answer;

public interface NotifierPort {

    void notifyAnswer(Answer answer);

}
