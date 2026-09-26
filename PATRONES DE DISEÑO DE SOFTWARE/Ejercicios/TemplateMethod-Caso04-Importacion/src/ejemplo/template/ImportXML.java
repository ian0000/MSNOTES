package ejemplo.template;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public final class ImportXML extends ImportacionInformacion<Document> {
    public ImportXML(Path destino) { super(destino); }

    @Override protected Document leerDatos(Path archivo) throws IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new DefaultHandler() {
                @Override public void error(SAXParseException e) throws SAXException { throw e; }
                @Override public void fatalError(SAXParseException e) throws SAXException { throw e; }
            });
            try (InputStream entrada = Files.newInputStream(archivo)) { return builder.parse(entrada); }
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException("No se pudo leer el XML: " + e.getMessage(), e);
        }
    }

    @Override protected List<Registro> transformarDatos(Document documento) {
        Element raiz = documento.getDocumentElement();
        if (!raiz.getTagName().equals("registros")) {
            throw new IllegalArgumentException("La raíz XML debe ser registros");
        }
        List<Registro> registros = new ArrayList<>();
        for (Node nodo = raiz.getFirstChild(); nodo != null; nodo = nodo.getNextSibling()) {
            if (nodo.getNodeType() != Node.ELEMENT_NODE) { continue; }
            Element elemento = (Element) nodo;
            if (!elemento.getTagName().equals("registro") || !elemento.hasAttribute("codigo")) {
                throw new IllegalArgumentException("Se esperaba registro con atributo codigo");
            }
            String nombre = null;
            for (Node hijo = elemento.getFirstChild(); hijo != null; hijo = hijo.getNextSibling()) {
                if (hijo.getNodeType() != Node.ELEMENT_NODE) { continue; }
                if (!hijo.getNodeName().equals("nombre") || nombre != null) {
                    throw new IllegalArgumentException("Cada registro XML necesita un único elemento nombre");
                }
                for (Node interno = hijo.getFirstChild(); interno != null; interno = interno.getNextSibling()) {
                    if (interno.getNodeType() == Node.ELEMENT_NODE) {
                        throw new IllegalArgumentException("nombre debe contener texto, no elementos");
                    }
                }
                nombre = hijo.getTextContent();
            }
            registros.add(new Registro(elemento.getAttribute("codigo"), nombre));
        }
        return registros;
    }
}
