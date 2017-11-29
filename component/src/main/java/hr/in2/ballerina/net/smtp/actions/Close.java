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

import org.ballerinalang.bre.Context;
import org.ballerinalang.connector.api.ConnectorFuture;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BConnector;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.BallerinaAction;

/**
 * {@code Close} action is used to close the mail server connection.
 *
 * @since 1.0.0
 */
@BallerinaAction(
            packageName = "ballerina.net.smtp",
            actionName = "close",
            connectorName = Constants.CONNECTOR_NAME,
            args = { @Argument(name = "c", type = TypeKind.CONNECTOR) }
        )
public class Close extends AbstractSmtpAction {

    @Override
    public ConnectorFuture execute(Context context) {
        BConnector bConnector = (BConnector) getRefArgument(context, 0);
        SmtpDataSource datasource = getDataSource(bConnector);
        close(datasource);
        return getConnectorFuture();
    }

}
