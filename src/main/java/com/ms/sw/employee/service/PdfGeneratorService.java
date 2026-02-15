package com.ms.sw.employee.service;


import com.lowagie.text.pdf.BaseFont;
import com.ms.sw.employee.dto.SalarySlipData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
@Slf4j
public class PdfGeneratorService {

    @Value("${salary.pdf.storage.path:/salary-slips}")
    private String pdfStoragePath;

    private static final String TEMPLATE_PATH = "templates/salary-template.html";

    /**
     * Generates a salary slip PDF from the provided data.
     *
     * @param data all salary calculation data
     * @return file path where PDF was saved
     */
    public String generateSalaryPdf(SalarySlipData data) {

        log.info("Generating PDF for employee: {} ({})", data.employeeName(), data.personalId());

        try {
            // 1. Read HTML template
            String htmlTemplate = readHtmlTemplate();

            // 2. Replace placeholders with actual data
            String htmlContent = fillTemplate(htmlTemplate, data);

            // 3. Create directory structure
            String directoryPath = createDirectoryStructure(data.year(), data.month());

            // 4. Generate filename
            String fileName = String.format("%s_%d-%02d.pdf",
                    data.personalId(),
                    data.year(),
                    data.month());

            String pdfPath = directoryPath + "/" + fileName;

            // 5. Convert HTML to PDF and save
            convertHtmlToPdf(htmlContent, pdfPath);

            log.info("PDF generated successfully: {}", pdfPath);
            return pdfPath;

        } catch (IOException e) {
            log.error("Failed to generate PDF for employee: {}", data.personalId(), e);
            throw new RuntimeException("Failed to generate salary slip PDF", e);
        }
    }

    private String readHtmlTemplate() throws IOException {
        ClassPathResource resource = new ClassPathResource(TEMPLATE_PATH);
        return new String(resource.getInputStream().readAllBytes());
    }

    private String fillTemplate(String template, SalarySlipData data) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String monthName = getHebrewMonth(data.month());

        return template
                .replace("{{companyName}}", fixHebrew(data.companyName()))
                .replace("{{taxId}}", data.companyId())
                .replace("{{companyAddress}}", fixHebrew(data.companyAddress()))


                .replace("{{monthName}}", fixHebrew(monthName))
                .replace("{{year}}", String.valueOf(data.year()))

                .replace("{{employeeName}}", fixHebrew(data.employeeName()))
                .replace("{{personalId}}", data.personalId())
                .replace("{{department}}", fixHebrew(data.department()))
                .replace("{{paymentDate}}", data.paymentDate().format(dateFormatter))

                .replace("{{regularHours}}", String.format("%.2f", data.regularHours()))
                .replace("{{overtime125Hours}}", String.format("%.2f", data.overtime125Hours()))
                .replace("{{overtime150Hours}}", String.format("%.2f", data.overtime150Hours()))
                .replace("{{travelDays}}", String.valueOf(data.travelDays()))

                .replace("{{hourlyRate}}", String.format("%.2f", data.hourlyRate()))
                .replace("{{overtime125Rate}}", String.format("%.2f", data.hourlyRate() * 1.25))
                .replace("{{overtime150Rate}}", String.format("%.2f", data.hourlyRate() * 1.50))

                .replace("{{regularPay}}", String.format("%.2f", data.regularPay()))
                .replace("{{overtime125Pay}}", String.format("%.2f", data.overtime125Pay()))
                .replace("{{overtime150Pay}}", String.format("%.2f", data.overtime150Pay()))
                .replace("{{travelAllowance}}", String.format("%.2f", data.travelAllowance()))
                .replace("{{grossSalary}}", String.format("%.2f", data.grossSalary()))

                .replace("{{employeePension}}", String.format("%.2f", data.employeePension()))
                .replace("{{nationalInsurance}}", String.format("%.2f", data.nationalInsurance()))
                .replace("{{taxableIncome}}", String.format("%.2f", data.taxableIncome()))
                .replace("{{incomeTax}}", String.format("%.2f", data.incomeTax()))
                .replace("{{creditPoints}}", String.format("%.2f", data.creditPoints()))
                .replace("{{totalDeductions}}", String.format("%.2f", data.totalDeductions()))
                .replace("{{netSalary}}", String.format("%.2f", data.netSalary()))

                .replace("{{employerPension}}", String.format("%.2f", data.employerPension()))
                .replace("{{employerSeverance}}", String.format("%.2f", data.employerSeverance()))
                .replace("{{employerNI}}", String.format("%.2f", data.employerNI()))
                .replace("{{totalEmployerCost}}", String.format("%.2f", data.totalEmployerCost()))

                .replace("{{generationDate}}", data.paymentDate().format(dateFormatter));
    }

    private String createDirectoryStructure(int year, int month) throws IOException {
        String dirPath = String.format("%s/%d/%02d", pdfStoragePath, year, month);
        Path path = Paths.get(dirPath);

        if (!Files.exists(path)) {
            Files.createDirectories(path);
            log.info("Created directory: {}", dirPath);
        }

        return dirPath;
    }

    private void convertHtmlToPdf(String htmlContent, String pdfPath) throws IOException {

        try (OutputStream os = new FileOutputStream(pdfPath)) {

            ITextRenderer renderer = new ITextRenderer();
            String fontPath = Objects.requireNonNull(getClass().getResource("/fonts/ARIAL.TTF")).toString();
            renderer.getFontResolver().addFont(
                    fontPath,
                    BaseFont.IDENTITY_H,
                    BaseFont.EMBEDDED
            );
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(os);
        } catch (Exception e) {
            log.error("Error creating PDF. Font path might be wrong.", e);
            throw new IOException("Failed to convert HTML to PDF", e);
        }
    }
    private String getHebrewMonth(int month) {
        String[] hebrewMonths = {
                "ינואר", "פברואר", "מרץ", "אפריל", "מאי", "יוני",
                "יולי", "אוגוסט", "ספטמבר", "אוקטובר", "נובמבר", "דצמבר"
        };
        return hebrewMonths[month - 1];
    }
    private String fixHebrew(String text) {
        if (text == null) return "";

        boolean hasHebrew = text.chars().anyMatch(c -> c >= 0x0590 && c <= 0x05FF);
        if (!hasHebrew) return text;

        StringBuilder result = new StringBuilder();
        String[] words = text.split(" ");

        for (int i = words.length - 1; i >= 0; i--) {
            String word = words[i];

            if (word.chars().anyMatch(c -> c >= 0x0590 && c <= 0x05FF)) {
                result.append(new StringBuilder(word).reverse());
            } else {
                result.append(word);
            }
            if (i > 0) result.append(" ");
        }

        return result.toString();
    }
}