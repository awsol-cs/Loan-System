/*
 * Copyright (c) 2011-2017 Nexmo Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.nexmo.quickstart.sms;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.sms.MessageStatus;
import com.nexmo.client.sms.SmsSubmissionResponse;
import com.nexmo.client.sms.messages.TextMessage;

public class SendMessage {

    private final String NEXMO_API_KEY = "7289eab1";
    private final String NEXMO_API_SECRET = "UcJGzy9STheMJIdu";
    private final String TO_NUMBER = "639178372484";
    private final String NEXMO_BRAND_NAME = "ProjFiveSix";

    public void sendApprovalMessage() throws Exception {

        NexmoClient client = NexmoClient.builder().apiKey(NEXMO_API_KEY).apiSecret(NEXMO_API_SECRET).build();

        TextMessage message = new TextMessage(NEXMO_BRAND_NAME,
                TO_NUMBER,
                "Congratulations, your loan has been approved."
        );

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            System.out.println("Message sent successfully.");
        } else {
            System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
        }
    }

    public void sendRejectionMessage() throws Exception {
        
        NexmoClient client = NexmoClient.builder().apiKey(NEXMO_API_KEY).apiSecret(NEXMO_API_SECRET).build();

        TextMessage message = new TextMessage(NEXMO_BRAND_NAME,
                TO_NUMBER,
                "Sorry, your loan has been rejected."
        );

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            System.out.println("Message sent successfully.");
        } else {
            System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
        }
    }
}