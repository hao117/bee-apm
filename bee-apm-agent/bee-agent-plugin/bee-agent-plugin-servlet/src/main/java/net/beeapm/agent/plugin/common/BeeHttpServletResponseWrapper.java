package net.beeapm.agent.plugin.common;

import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

/**
 * @author yuan
 * @date 2018/10/20
 */
public class BeeHttpServletResponseWrapper extends HttpServletResponseWrapper {
    private ByteArrayOutputStream byteArrayOutputStream;
    private HttpServletResponse response;
    private LogImpl log = LogManager.getLog(BeeHttpServletResponseWrapper.class);

    public BeeHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
        this.response = response;
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (byteArrayOutputStream != null) {
            return super.getOutputStream();
        }
        byteArrayOutputStream = new ByteArrayOutputStream();
        // 将数据写到 byte 中
        return new BeeServletOutputStream(byteArrayOutputStream);
    }


    @Override
    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(getOutputStream());
    }

    /**
     * 写入到原有的输出里
     */
    public void writeOriginOutputStream() {
        try {
            OutputStream os = response.getOutputStream();
            os.write(toByteArray());
            os.flush();
            os.close();
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public byte[] toByteArray() {
        if (byteArrayOutputStream == null) {
            return null;
        }
        try {
            byteArrayOutputStream.flush();
        } catch (IOException e) {
            log.error("", e);
        }
        return byteArrayOutputStream.toByteArray();
    }

}
