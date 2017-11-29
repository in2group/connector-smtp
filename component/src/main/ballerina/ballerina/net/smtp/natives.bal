package ballerina.net.smtp;

public struct ConnectionProperties {
}

public struct Message {
    string from;
    string[] to;
    string[] cc;
    string subject;
    string content;
}

@Description { value:"SMTP Client Connector."}
@Param { value:"host: Host address of mail server" }
@Param { value:"port: Port of mail server" }
@Param { value:"options: Optional properties for mail server connection" }
public connector ClientConnector (string host, int port, ConnectionProperties options) {

    map sharedMap = {};

    @Description {value:"The send action implementation which sends a SMTP message."}
    native action send (Message message);

    @Description {value:"The close action implementation which closes the connection to mail server."}
    native action close ();

}
