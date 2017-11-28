package ballerina.net.smtp;

public struct ConnectionProperties {
}

public struct Message {
}

@Description { value:"SMTP Client Connector."}
@Param { value:"host: Host address of mail server" }
@Param { value:"port: Port of mail server" }
@Param { value:"options: Optional properties for mail server connection" }
public connector ClientConnector (string host, int port, ConnectionProperties options) {

    map sharedMap = {};

    @Description {value:"The send action implementation which sends an SMTP message."}
    native action send (Message message);

}
