package net.beeapm.agent.annotation;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author yuan
 * @date 2019/12/20
 */
@SupportedAnnotationTypes({"net.beeapm.agent.annotation.BeePlugin"})
public class BeeAnnotationProcessorImpl extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return false;
        }
        Filer filer = processingEnv.getFiler();

        Map<String, List<String>> serviceMap = new HashMap<String, List<String>>(8);

        for (Element e : roundEnv.getElementsAnnotatedWith(BeePlugin.class)) {
            BeePlugin annotation = e.getAnnotation(BeePlugin.class);
            if (annotation == null) {
                continue;
            }
            if (!e.getKind().isClass() && !e.getKind().isInterface()) {
                continue;
            }
            TypeElement type = (TypeElement) e;
            List serviceList = serviceMap.get(annotation.type());
            if (serviceList == null) {
                serviceList = new ArrayList(8);
                serviceMap.put(annotation.type(), serviceList);
            }
            serviceList.add(annotation.name() + "=" + type.getQualifiedName().toString());
        }

        if (serviceMap.isEmpty()) {
            return false;
        }

        for (Map.Entry<String, List<String>> entry : serviceMap.entrySet()) {
            List<String> list = entry.getValue();
            if (list == null || list.isEmpty()) {
                continue;
            }
            String fileName = "bee-" + entry.getKey() + ".def";
            try {
                FileObject f = filer.createResource(StandardLocation.CLASS_OUTPUT, "", fileName);
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(f.openOutputStream(), "UTF-8"));
                for (int i = 0; i < list.size(); i++) {
                    String service = list.get(i);
                    pw.println(service);
                    System.out.println("     ["+fileName+"] "+ service);
                }
                pw.close();
            } catch (IOException x) {
                processingEnv.getMessager()
                        .printMessage(Diagnostic.Kind.ERROR, String.format("failed to write file %s: %s", fileName, x.getMessage()));
            }
        }
        return false;
    }

}
