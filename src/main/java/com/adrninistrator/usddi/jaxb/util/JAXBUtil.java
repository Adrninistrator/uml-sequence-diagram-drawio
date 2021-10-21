package com.adrninistrator.usddi.jaxb.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.Writer;

/**
 * @author adrninistrator
 * @date 2021/9/18
 * @description:
 */
public class JAXBUtil {

    public static void javaBeanToXml(Object obj, Writer writer, Class... classesToBeBound) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(classesToBeBound);
        Marshaller marshaller = jaxbContext.createMarshaller();
        // 格式化输出，即按标签自动换行，否则会在一行输出
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        // 不生成xml头信息，默认会生成
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
        // 设置编码
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        marshaller.marshal(obj, writer);

        // 控制台输出
        // marshaller.marshal(obj, System.out);
    }

    private JAXBUtil() {
        throw new IllegalStateException("illegal");
    }
}
