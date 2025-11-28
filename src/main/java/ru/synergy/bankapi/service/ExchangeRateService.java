package ru.synergy.bankapi.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ru.synergy.bankapi.dto.ExchangeRateDto;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExchangeRateService {

    private static final String CBR_API_URL =
            "https://www.cbr.ru/scripts/XML_daily.asp";

    public List<ExchangeRateDto> getLatestRates() throws Exception {
        List<ExchangeRateDto> rates = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new URL(CBR_API_URL).openStream());

        NodeList valuteNodes = doc.getElementsByTagName("Valute");

        for (int i = 0; i < valuteNodes.getLength(); i++) {
            Element valuteElement = (Element) valuteNodes.item(i);

            String currency = valuteElement.getElementsByTagName("CharCode")
                    .item(0).getTextContent();
            String valueStr = valuteElement.getElementsByTagName("Value")
                    .item(0).getTextContent();

            valueStr = valueStr.replace(',', '.');

            ExchangeRateDto dto = new ExchangeRateDto();
            dto.setCurrency(currency);
            dto.setRate(new BigDecimal(valueStr));
            dto.setDate(LocalDate.now());

            rates.add(dto);
        }

        return rates;
    }
}
