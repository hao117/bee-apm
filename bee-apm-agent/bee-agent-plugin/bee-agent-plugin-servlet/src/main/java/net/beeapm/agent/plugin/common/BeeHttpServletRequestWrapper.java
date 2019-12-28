package net.beeapm.agent.plugin.common;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author yuan
 * @date 2019/12/27
 */
public class BeeHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private byte[] body;

    public BeeHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        readBody(request);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    public void readBody(HttpServletRequest request) {
        try {
            BufferedReader reader = request.getReader();
            String str;
            StringBuilder result = new StringBuilder();
            while ((str = reader.readLine()) != null) {
                result.append(str);
            }
            body = result.toString().getBytes("utf-8");
        } catch (Exception e) {

        }
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body);

        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return bais.read();
            }
        };
    }


}
