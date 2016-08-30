package com.olegshan.service;

import com.olegshan.model.Greeting;

import java.util.concurrent.Future;

/**
 * Created by olegshan on 30.08.2016.
 */
public interface EmailService {

    Boolean send(Greeting greeting);

    void sendAsync(Greeting greeting);

    Future<Boolean> sendAsyncWithResult(Greeting greeting);
}
