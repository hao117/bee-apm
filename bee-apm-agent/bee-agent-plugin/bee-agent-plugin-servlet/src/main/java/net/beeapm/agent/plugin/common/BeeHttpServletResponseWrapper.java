package net.beeapm.agent.plugin.common;

import net.beeapm.agent.common.BeeUtils;
import net.beeapm.agent.log.ILog;
import net.beeapm.agent.log.LogFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

/**
 * @author yuan
 * @date 2018/10/20
 */
public class BeeHttpServletResponseWrapper extends HttpServletResponseWrapper {
    private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private PrintWriter printWriter = new PrintWriter(byteArrayOutputStream);
    private BeeServletOutputStream outputStream = new BeeServletOutputStream(byteArrayOutputStream);
    private ILog log = LogFactory.getLog(BeeHttpServletResponseWrapper.class);

    public BeeHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        return printWriter;
    }

    /**
     * 写入到原有的输出里
     */
    public void writeOriginOutputStream() {
        try {
            OutputStream os = getResponse().getOutputStream();
            byte[] bytes = toByteArray();
            if (bytes != null && bytes.length > 0) {
                os.write(bytes);
            }
            os.flush();
            os.close();
        } catch (Exception e) {
            log.error("writeOriginOutputStream", e);
        }
    }

    @Override
    public void flushBuffer() {
        try {
            getResponse().flushBuffer();
        } catch (Exception e) {
            log.error("flush error", e);
        }
        BeeUtils.flush(printWriter);
        BeeUtils.flush(outputStream);
    }


    public byte[] toByteArray() {
        flushBuffer();
        return byteArrayOutputStream.toByteArray();
    }

}
