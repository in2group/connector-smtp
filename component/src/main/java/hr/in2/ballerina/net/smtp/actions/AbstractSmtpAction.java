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

import org.ballerinalang.connector.api.AbstractNativeAction;
import org.ballerinalang.connector.api.ConnectorFuture;
import org.ballerinalang.model.values.BConnector;
import org.ballerinalang.model.values.BMap;
import org.ballerinalang.model.values.BString;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.nativeimpl.actions.ClientConnectorFuture;
import org.ballerinalang.util.exceptions.BallerinaException;

/**
 * {@code AbstractSmtpAction} is the base class for all SMTP actions.
 *
 * @since 1.0.0
 */
public abstract class AbstractSmtpAction extends AbstractNativeAction {

    protected boolean validateParams(BConnector connector) {
        if ((connector != null) && (connector.getRefField(0) != null)
                && (connector.getRefField(0) instanceof BStruct)) {
            return true;
        } else {
            throw new BallerinaException("Connector parameters not defined correctly.");
        }
    }

    protected SmtpDataSource getDataSource(BConnector bConnector) {
        SmtpDataSource datasource = null;
        BMap sharedMap = (BMap) bConnector.getRefField(1);
        if (sharedMap.get(new BString(Constants.DATASOURCE_KEY)) != null) {
            BValue value = sharedMap.get(new BString(Constants.DATASOURCE_KEY));
            if (value instanceof SmtpDataSource) {
                datasource = (SmtpDataSource) value;
            }
        } else {
            throw new BallerinaException("DataSource not initialized properly");
        }
        return datasource;
    }

    protected void close(SmtpDataSource dataSource) {
        try {
            dataSource.getTransport().close();
        } catch (Exception e) {
            throw new BallerinaException("Error while closing client connection", e);
        }
    }

    protected ConnectorFuture getConnectorFuture() {
        ClientConnectorFuture future = new ClientConnectorFuture();
        future.notifySuccess();
        return future;
    }

}
