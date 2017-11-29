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
package hr.in2.ballerina.net.smtp;

import org.ballerinalang.connector.api.ConnectorFuture;
import org.ballerinalang.model.types.BType;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.nativeimpl.actions.ClientConnectorFuture;
import org.ballerinalang.util.exceptions.BallerinaException;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;

/**
 * {@code SmtpDataSource} util class for SMTP Connector initialization.
 *
 * @since 1.0.0
 */
public class SmtpDataSource implements BValue {

    private Session session;
    private Transport transport;

    public SmtpDataSource() {
    }

    public boolean init(String host, long port, BStruct options) {
        Properties mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.host", host);
        mailServerProperties.put("mail.smtp.port", port);

        try {
            session = Session.getDefaultInstance(mailServerProperties, null);
            transport = session.getTransport("smtp");
            transport.connect();
        } catch (Throwable e) {
            throw new BallerinaException("Cannnot open connection to " + host + ":" + port, e);
        }
        return true;
    }

    protected ConnectorFuture getConnectorFuture() {
        ClientConnectorFuture future = new ClientConnectorFuture();
        future.notifySuccess();
        return future;
    }

    public Session getSession() {
        return session;
    }

    public Transport getTransport() {
        return transport;
    }

    @Override
    public String stringValue() {
        return null;
    }

    @Override
    public BType getType() {
        return null;
    }

    @Override
    public BValue copy() {
        return null;
    }

}
