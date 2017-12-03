package ballerina.net.smtp;

public struct Message {
    string from;
    string[] to;
    string[] cc;
    string subject;
    string content;
    Attachment[] attachments;
}

public struct Attachment {
    string name;
    blob content;
}

@Description { value:"SMTP Client Connector."}
@Param { value:"host: Host address of mail server" }
@Param { value:"port: Port of mail server" }
@Param { value:"properties: Optional properties for mail server connection" }
public connector ClientConnector (string host, int port, map properties) {

    map sharedMap = {};

    @Description {value:"The send action implementation which sends a SMTP message."}
    native action send (Message message);

    @Description {value:"The close action implementation which closes the connection to mail server."}
    native action close ();

}
