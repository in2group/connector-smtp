# Ballerina SMTP Connector

Ballerina SMTP Connector is used to connect Ballerina to mail server and send emails using SMTP protocol.

Steps to configure:
1. Extract `ballerina-smtp-connector-<version>.zip` and copy containing jars in to `<BRE_HOME>/bre/lib/`

Ballerina as email sender

```ballerina
import ballerina.net.smtp;

function main(string[] args) {
  endpoint<smtp:ClientConnector> mail {
    create smtp:ClientConnector("<HostName>", 25, {});
  }

  smtp:Message msg = {
    from: "<SenderEmailAddress>",
    to: ["<Recipient1EmailAddres>", "<Recipient2EmailAddres>"],
    subject: "Ballerina message",
    content: "Hello from Ballerina!"
  };

  mail.send(msg);
  mail.close();
}
```

For more SMTP Connector configurations please refer to the samples directory.
