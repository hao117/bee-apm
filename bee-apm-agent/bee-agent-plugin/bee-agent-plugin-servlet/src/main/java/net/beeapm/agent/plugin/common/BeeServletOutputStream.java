package net.beeapm.agent.plugin.common;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;

/**
 * @author yuanlong.chen
 * @date 2019/12/27
 */
public class BeeServletOutputStream extends ServletOutputStream {
    private ByteArrayOutputStream output;

    public BeeServletOutputStream(ByteArrayOutputStream output) {
        this.output = output;
    }

    @Override
    public void write(int b) {
        // 将数据写到stream中
        output.write(b);
    }

    @Override
    public boolean isReady() {
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
    }
}
