package net.beeapm.agent.plugin.common;

import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;

public class BeeHttpResponseWrapper extends HttpServletResponseWrapper {
    private ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    private HttpServletResponse response;
    private boolean isOutputStream = false;
    private boolean isWrite = false;
    private PrintWriter pwrite;
    private LogImpl log = LogManager.getLog(BeeHttpResponseWrapper.class);

    public BeeHttpResponseWrapper(HttpServletResponse response) {
        super(response);
        this.response = response;
    }

    @Override
    public ServletOutputStream getOutputStream() {
        isOutputStream = true;
        return new MyServletOutputStream(bytes); // 将数据写到 byte 中
    }


    @Override
    public PrintWriter getWriter() {
        try {
            isWrite = true;
            pwrite = new PrintWriter(new OutputStreamWriter(bytes, "utf-8"));// 将数据写到 byte 中
        } catch (UnsupportedEncodingException e) {
            isWrite = false;
            log.error("",e);
        }
        return pwrite;
    }

    public void out() {
        if (isOutputStream) {
            isWrite = false; //OutputStream和PrintWriter是互斥的，只要一个就可以
            try {
                OutputStream os = response.getOutputStream();
                os.write(getBytes());
                os.flush();
                os.close();
            } catch (Exception e) {
                log.error("",e);
            }
        }
        if (isWrite) {
            try {
                PrintWriter writer = response.getWriter();
                writer.append(new String(getBytes()));
                writer.flush();
                writer.close();
            } catch (Exception e) {
                log.error("",e);
            }
        }
    }

    public byte[] getBytes() {
        if (null != pwrite) {
            pwrite.close();
            return bytes.toByteArray();
        }

        if (null != bytes) {
            try {
                bytes.flush();
            } catch (IOException e) {
                log.error("",e);
            }
        }
        return bytes.toByteArray();
    }

    class MyServletOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream ostream;

        public MyServletOutputStream(ByteArrayOutputStream ostream) {
            this.ostream = ostream;
        }

        @Override
        public void write(int b) {
            ostream.write(b); // 将数据写到stream中
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
        }
    }
}
