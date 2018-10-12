package net.beeapm.agent.plugin.handler;

import net.beeapm.agent.common.BeeTraceContext;
import net.beeapm.agent.log.LogImpl;
import net.beeapm.agent.log.LogManager;
import net.beeapm.agent.model.Span;
import net.beeapm.agent.plugin.common.RequestBodyHolder;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.*;

/**
 * Created by yuan on 2018/8/5.
 */
public class RequestInputStreamHandler extends AbstractHandler {
    private static final LogImpl log = LogManager.getLog(RequestInputStreamHandler.class.getSimpleName());
    @Override
    public Span before(String className,String methodName, Object[] allArguments,Object[] extVal) {
        return null;
    }

    @Override
    public Object after(String className,String methodName, Object[] allArguments,Object result, Throwable t,Object[] extVal) {
        InputStream is = (InputStream)result;
        if(is == null){
            return null;
        }
        try {
            String gid = BeeTraceContext.getGId();
            if(!gid.equals(RequestBodyHolder.getGid())){
                log.debug("read InputStream ...");
                RequestBodyHolder.remove();
                StringBuilder sb = new StringBuilder();
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String body = sb.toString();
                RequestBodyHolder.setRequestBody(body);
            }
            log.debug("beeRequestBody=" + RequestBodyHolder.getRequestBody());
            final ByteArrayInputStream bais = new ByteArrayInputStream(RequestBodyHolder.getRequestBody().getBytes());
            result = new ServletInputStream() {
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
                public int read(){
                    return bais.read();
                }

                @Override
                public int read(byte[] b, int off, int len){
                    return bais.read( b, off, len );
                }
            };
            return result;
        }catch (Exception e){
            log.error("",e);
        }
        return result;
    }

}
