/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.openxdata.oc.handler;

import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.ZOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.openxdata.model.ResponseHeader;
import org.openxdata.server.admin.model.User;

/**
 *
 * @author kay
 */
public class AccessDeniedHandler implements RequestHandler
{
    @Override
    public void handleRequest(User user, InputStream is, OutputStream os) throws IOException
    {
        ZOutputStream zos = new ZOutputStream(os, JZlib.Z_BEST_COMPRESSION);
        DataOutputStream zdos = new DataOutputStream(zos);
        ResponseHeader rh = new ResponseHeader(ResponseHeader.STATUS_ACCESS_DENIED);
        rh.write(zdos);
        zos.finish();
        zdos.flush();
    }
}
