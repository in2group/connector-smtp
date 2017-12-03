/*
 * Copyright (c) 2017, IN2 Ltd. (http://www.in2.hr) All Rights Reserved.
 *
 * IN2 Ltd. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package hr.in2.ballerina.net.smtp.actions;

import hr.in2.ballerina.net.smtp.Constants;
import hr.in2.ballerina.net.smtp.SmtpDataSource;

import org.apache.axiom.attachments.ByteArrayDataSource;
import org.ballerinalang.bre.Context;
import org.ballerinalang.connector.api.ConnectorFuture;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BConnector;
import org.ballerinalang.model.values.BRefValueArray;
import org.ballerinalang.model.values.BStringArray;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.BallerinaAction;
import org.ballerinalang.util.exceptions.BallerinaException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * {@code Send} action to send a number of mail messages to mail server.
 *
 * @since 1.0.0
 */
@BallerinaAction(
            packageName = "ballerina.net.smtp",
            actionName = "send",
            connectorName = Constants.CONNECTOR_NAME,
            args = {
                @Argument(name = "c", type = TypeKind.CONNECTOR),
                @Argument(name = "message", type = TypeKind.STRUCT),
            }
         )
public class Send extends AbstractSmtpAction {

    @Override
    public ConnectorFuture execute(Context context) {
        BConnector bConnector = (BConnector) getRefArgument(context, 0);
        BStruct bMessage = (BStruct) getRefArgument(context, 1);

        SmtpDataSource dataSource = getDataSource(bConnector);
        try {
            Message message = message(bMessage, dataSource.getSession());
            dataSource.getTransport().sendMessage(message, message.getAllRecipients());
        } catch (Throwable e) {
            throw new BallerinaException("Failed to send mail message (" + e + ")", e);
        }

        return getConnectorFuture();
    }

    protected Message message(BStruct bMessage, Session session) throws AddressException, MessagingException {
        MimeMessage message = new MimeMessage(session);
        if (bMessage.getStringField(0) != null && !bMessage.getStringField(0).equals("")) {
            message.setFrom(new InternetAddress(bMessage.getStringField(0), true));
        }
        if (bMessage.getRefField(0) != null) {
            message.addRecipients(Message.RecipientType.TO, recipients((BStringArray) bMessage.getRefField(0)));
        }
        if (bMessage.getRefField(1) != null) {
            message.addRecipients(Message.RecipientType.CC, recipients((BStringArray) bMessage.getRefField(1)));
        }
        message.setSubject(bMessage.getStringField(1), "UTF-8");
        if (bMessage.getRefField(2) != null) {
            message.setContent(multipart(bMessage));
        } else {
            message.setText(bMessage.getStringField(2), "UTF-8");
        }
        return message;
    }

    protected Multipart multipart(BStruct bMessage) throws MessagingException {
        Multipart multipart = new MimeMultipart();

        // First part is message
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText(bMessage.getStringField(2));
        multipart.addBodyPart(messageBodyPart);

        // Second part are attachments
        BRefValueArray attachments = (BRefValueArray) bMessage.getRefField(2);
        for (int i = 0; i < attachments.size(); i++) {
            messageBodyPart = new MimeBodyPart();
            DataSource source = new ByteArrayDataSource(((BStruct) attachments.get(i)).getBlobField(0));
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(((BStruct) attachments.get(i)).getStringField(0));
            multipart.addBodyPart(messageBodyPart);
        }

        return multipart;
    }

    protected Address recipient(String address) throws AddressException {
        return new InternetAddress(address, true);
    }

    protected Address[] recipients(BStringArray bRecipients) throws AddressException {
        Address[] recipients = new InternetAddress[(int) bRecipients.size()];
        for (int i = 0; i < recipients.length; i++) {
            recipients[i] = recipient(bRecipients.get(i));
        }
        return recipients;
    }

}
