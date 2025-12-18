package com.core.microbill.billing.infrastructure.adapter.out.report;

import com.core.microbill.billing.infrastructure.adapter.out.client.dto.ProductDto;
import com.core.microbill.billing.infrastructure.adapter.out.client.dto.ProviderDto;
import com.core.microbill.billing.infrastructure.adapter.out.client.dto.CustomerDto;
import com.core.microbill.billing.infrastructure.adapter.out.client.dto.CustomerDto;
import com.core.microbill.billing.domain.model.Invoice;
import com.core.microbill.billing.domain.model.InvoiceItem;
import com.core.microbill.billing.infrastructure.adapter.out.client.ManagementServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JasperReportService {

    private final ManagementServiceClient managementClient;

    private static final String INVOICE_TEMPLATE = "reports/invoice.jasper";
    private static final DecimalFormat CURRENCY_FORMAT = new DecimalFormat("#,##0.00");
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] generateInvoiceReport(Invoice invoice) {
        try {
            CustomerDto customer = managementClient.getCustomer(invoice.getCustomerId());
            ProviderDto provider = managementClient.getProvider(invoice.getProviderId());

            ClassPathResource resource = new ClassPathResource(INVOICE_TEMPLATE);
            InputStream templateStream = resource.getInputStream();

            Map<String, Object> parameters = createParameters(invoice, customer, provider);
            List<InvoiceItemData> itemsData = createItemsData(invoice.getItems());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(itemsData);

            JasperPrint jasperPrint = JasperFillManager.fillReport(templateStream, parameters, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            log.error("Error generating invoice report for invoice ID: {}", invoice.getId(), e);
            throw new RuntimeException("Failed to generate invoice report", e);
        }
    }

    private Map<String, Object> createParameters(Invoice invoice, CustomerDto customer, ProviderDto provider) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put("INVOICE_ID", invoice.getId().toString());
        parameters.put("INVOICE_DATE", invoice.getIssueDate().format(DATE_FORMAT));

        parameters.put("CLIENT_NAME", customer.getName());
        parameters.put("CLIENT_DOC", customer.getDocNumber());
        parameters.put("CLIENT_MAIL", customer.getEmail());

        parameters.put("SUPPLIER_NAME", provider.getName()  );
        parameters.put("SUPPLIER_DOC", provider.getTaxId() );
        parameters.put("SUPPLIER_MAIL", provider.getEmail() );

        parameters.put("SUBTOTAL", CURRENCY_FORMAT.format(invoice.getSubtotal()));
        parameters.put("TAX", CURRENCY_FORMAT.format(invoice.getTaxTotal()));
        parameters.put("TOTAL", CURRENCY_FORMAT.format(invoice.getTotal()));

        return parameters;
    }

    private List<InvoiceItemData> createItemsData(List<InvoiceItem> items) {
        return items.stream()
                .map(item -> {
                    ProductDto product = managementClient.getProduct(item.getProductId());
                    return InvoiceItemData.builder()
                            .productName(product != null ? product.getName() : "Producto ID: " + item.getProductId())
                            .quantity(item.getQuantity())
                            .unitPrice(item.getUnitPrice().doubleValue())
                            .lineTotal(item.getQuantity() * item.getUnitPrice().doubleValue())
                            .build();
                })
                .collect(Collectors.toList());
    }
}