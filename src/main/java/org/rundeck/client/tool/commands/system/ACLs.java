package org.rundeck.client.tool.commands.system;

import com.lexicalscope.jewel.cli.CommandLineInterface;
import com.simplifyops.toolbelt.Command;
import com.simplifyops.toolbelt.CommandOutput;
import okhttp3.RequestBody;
import org.rundeck.client.api.RundeckApi;
import org.rundeck.client.api.model.ACLPolicy;
import org.rundeck.client.api.model.ACLPolicyItem;
import org.rundeck.client.tool.commands.ApiCommand;
import org.rundeck.client.tool.commands.projects.ACLFileOptions;
import org.rundeck.client.tool.commands.projects.ACLNameOptions;
import org.rundeck.client.util.Client;

import java.io.IOException;

import static org.rundeck.client.tool.commands.projects.ACLs.outputListResult;
import static org.rundeck.client.tool.commands.projects.ACLs.outputPolicyResult;
import static org.rundeck.client.tool.commands.projects.ACLs.performACLModify;

/**
 * System ACLs
 */
@Command(description = "Manage System ACLs")
public class ACLs extends ApiCommand {
    public ACLs(final Client<RundeckApi> client) {
        super(client);
    }

    @Command(description = "list system acls")
    public void list(CommandOutput output) throws IOException {
        ACLPolicyItem items = client.checkError(client.getService()
                                                      .listSystemAcls());
        outputListResult(output, items, "system");
    }


    @CommandLineInterface(application = "get") interface Get extends ACLNameOptions {
    }

    @Command(description = "get a system ACL definition")
    public void get(Get options, CommandOutput output) throws IOException {
        ACLPolicy aclPolicy = client.checkError(client.getService()
                                                      .getSystemAclPolicy(options.getName()));
        outputPolicyResult(output, aclPolicy);
    }


    @CommandLineInterface(application = "upload") interface Put extends ACLNameOptions, ACLFileOptions {
    }

    @Command(description = "Upload a system ACL definition")
    public void upload(Put options, CommandOutput output)
            throws IOException
    {

        ACLPolicy aclPolicy = performACLModify(
                options,
                (RequestBody body) -> client.getService()
                                            .updateSystemAclPolicy(options.getName(), body),
                client, output
        );
        outputPolicyResult(output, aclPolicy);
    }

    @CommandLineInterface(application = "create") interface Create extends ACLNameOptions,
            ACLFileOptions
    {
    }

    @Command(description = "Create a system ACL definition")
    public void create(Create options, CommandOutput output) throws IOException {

        ACLPolicy aclPolicy = performACLModify(
                options,
                (RequestBody body) -> client.getService()
                                            .createSystemAclPolicy(options.getName(), body),
                client, output
        );
        outputPolicyResult(output, aclPolicy);
    }


    @CommandLineInterface(application = "delete") interface Delete extends ACLNameOptions {

    }

    @Command(description = "Delete a system ACL definition")
    public void delete(Delete options, CommandOutput output) throws IOException {
        client.checkError(client.getService().deleteSystemAclPolicy(options.getName()));
        output.output(String.format("Deleted System ACL Policy: %s", options.getName()));
    }
}
